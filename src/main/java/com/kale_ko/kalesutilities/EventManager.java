package com.kale_ko.kalesutilities;

import com.kale_ko.api.spigot.Task;
import com.kale_ko.api.spigot.TextStyler;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.defaults.HelpCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldSaveEvent;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;
import java.io.File;

public class EventManager implements Listener {
    private final Main plugin;
    private int taskId;

    public EventManager(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        plugin.playerData.set(player.getUniqueId() + ".name", player.getName());

        if (plugin.playerData.getBoolean(player.getUniqueId() + ".ban.banned")) {
            player.kickPlayer(TextStyler.noPrefix(plugin.config.getString("messages.banned-message").replaceAll("%reason%", plugin.playerData.getString(player.getUniqueId() + ".ban.reason")), plugin.config));
            return;
        }

        plugin.playerData.set(player.getUniqueId() + ".joins", plugin.playerData.getInt(player.getUniqueId() + ".joins") + 1);
        plugin.playerConfig.saveConfig();

        if ((plugin.playerData.getString(player.getUniqueId() + ".nickname") != null)) player.setDisplayName(plugin.playerData.getString(player.getUniqueId() + ".nickname"));

        if ((plugin.playerData.getString(player.getUniqueId() + ".prefix") == null)) plugin.playerData.set(player.getUniqueId() + ".prefix", "&l[Default]");

        if (plugin.config.getBoolean("scoreboard.enabled")) startTask(player);

        if (plugin.config.getBoolean("double-jump.enabled")) player.setAllowFlight(true);

        if (plugin.config.getBoolean("custom-join-leave-messages")) event.setJoinMessage(TextStyler.noPrefix(plugin.config.getString("messages.join-message").replaceAll("%player%", plugin.playerData.getString(player.getUniqueId() + ".prefix") + " " + player.getDisplayName()), plugin.config));

        if (plugin.config.getBoolean("welcome-message-enabled")) plugin.commandRegister.sendMessage(player, TextStyler.noPrefix(plugin.config.getString("messages.welcome-message"), plugin.config));
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        if (plugin.config.getBoolean("scoreboard.enabled")) {
            Task task = new Task(player.getUniqueId());
            if (task.hasId()) task.stop();
        }

        if (!plugin.playerData.getBoolean(player.getUniqueId() + ".ban.banned")) {
            if (plugin.config.getBoolean("custom-join-leave-messages")) event.setQuitMessage(TextStyler.noPrefix(plugin.config.getString("messages.leave-message").replaceAll("%player%", plugin.playerData.getString(player.getUniqueId() + ".prefix") + " " + player.getDisplayName()), plugin.config));
        } else {
            event.setQuitMessage("");
        }
    }

    @EventHandler
    public void onPlayerSpawn(PlayerSpawnLocationEvent event) {
        if (plugin.config.getBoolean("spawn-enabled")) event.setSpawnLocation(new Location(Bukkit.getWorld(plugin.serverData.getString("spawnLocation.world")), plugin.serverData.getDouble("spawnLocation.x"), plugin.serverData.getDouble("spawnLocation.y"), plugin.serverData.getDouble("spawnLocation.z"), (float) plugin.serverData.getDouble("spawnLocation.rotation"), 0F));
    }

    @EventHandler
    public void onPlayerChat(PlayerChatEvent event) {
        if (!plugin.serverData.getBoolean("chatmuted")) {
            if (plugin.playerData.getBoolean(event.getPlayer().getUniqueId() + ".mute.muted")) {
                plugin.commandRegister.sendMessage(event.getPlayer(), plugin.config.getString("messages.muted-message").replaceAll("%reason%", plugin.playerData.getString(event.getPlayer().getUniqueId() + ".mute.reason")));
                event.setCancelled(true);
            } else {
                event.setFormat(TextStyler.noPrefix(plugin.config.getString("message-format")
                        .replaceAll("%player%", plugin.playerData.getString(event.getPlayer().getUniqueId() + ".prefix") + " " + event.getPlayer().getDisplayName())
                        .replaceAll("%message%", event.getMessage()), plugin.config));
            }
        } else {
            if (!plugin.commandRegister.checkPermission(event.getPlayer(), "admin.mutechat")) return;

            plugin.commandRegister.sendMessage(event.getPlayer(), plugin.config.getString("messages.chatmuted"));
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onServerping(ServerListPingEvent event) throws Exception {
        StringBuilder line1 = new StringBuilder(plugin.config.getString("motd.line1"));
        StringBuilder line2 = new StringBuilder(plugin.config.getString("motd.line2"));

        if (plugin.config.getBoolean("motd.centered")) {
            for (int i = 0; i < (68 - TextStyler.removeColorCodes(line1.toString()).length()) / 2; i++) {
                line1.insert(0, " ");
            }

            for (int i = 0; i < (68 - TextStyler.removeColorCodes(line2.toString()).length()) / 2; i++) {
                line2.insert(0, " ");
            }
        }

        event.setMotd(TextStyler.noExtra(line1.toString() + "\n&r" + line2.toString()));

        event.setServerIcon(Bukkit.loadServerIcon(new File("plugins\\KalesUtilities\\" + plugin.config.getString("server-icon"))));
    }

    @EventHandler
    public void onPlayerToggleFlight(PlayerToggleFlightEvent event) {
        if (event.getPlayer().getGameMode() == GameMode.CREATIVE || event.getPlayer().getGameMode() == GameMode.SPECTATOR) return;

        event.setCancelled(true);
        event.getPlayer().setVelocity(event.getPlayer().getLocation().getDirection().multiply(plugin.config.getDouble("double-jump.velocity-multiplayer")).setY(Math.max(-3, Math.min(3, event.getPlayer().getVelocity().getY() + plugin.config.getDouble("double-jump.y-velocity")))));
    }

    @EventHandler
    public void onWorldLoad(WorldLoadEvent event) {
        event.getWorld().setGameRuleValue("doDaylightCycle", "false");
        event.getWorld().setGameRuleValue("doWeatherCycle", "false");
    }

    @EventHandler
    public void onWorldSave(WorldSaveEvent event) {
        event.getWorld().setGameRuleValue("doDaylightCycle", "false");
        event.getWorld().setGameRuleValue("doWeatherCycle", "false");
    }

    @EventHandler
    public void SignChangeEvent(SignChangeEvent event) {
        event.setLine(0, TextStyler.noPrefix(event.getLine(0), plugin.config));
        event.setLine(1, TextStyler.noPrefix(event.getLine(1), plugin.config));
        event.setLine(2, TextStyler.noPrefix(event.getLine(2), plugin.config));
        event.setLine(3, TextStyler.noPrefix(event.getLine(3), plugin.config));
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK) && event.getClickedBlock().getType() != Material.AIR) {
            for (String location : plugin.serverData.getStringList("command-signs")) {
                String command = location.split(",")[0];

                plugin.log(location);
                plugin.log(command);

                if (location.equalsIgnoreCase(event.getClickedBlock().getX() + "," + event.getClickedBlock().getY() + "," + event.getClickedBlock().getZ() + "," + command)) {
                    plugin.log("Correct spot");

                    String[] theArgs = new String[2];
                    theArgs[0] = event.getPlayer().getName();
                    theArgs[1] = "/" + command;

                    plugin.log(theArgs[1]);

                    plugin.onCommand(Bukkit.getConsoleSender(), new HelpCommand(), "sudo", theArgs);
                }
            }
        }
    }

    @EventHandler
    public void onVehicleExit(VehicleExitEvent event) {
        event.getVehicle().remove();
    }

    @EventHandler
    public void onPlayerItemDamage(PlayerItemDamageEvent event) {
        event.setCancelled(true);
    }

    public void startTask(Player player) {
        taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            final Task task = new Task(player.getUniqueId());

            @Override
            public void run() {
                if (!task.hasId()) task.setId(taskId);

                if (plugin.config.getBoolean("scoreboard.enabled")) com.kale_ko.kalesutilities.Scoreboard.updateScoreBoard(player, plugin);
            }
        }, 0, 20);
    }
}
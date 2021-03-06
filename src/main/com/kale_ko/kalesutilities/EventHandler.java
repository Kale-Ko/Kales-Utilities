package com.kale_ko.kalesutilities;

import com.kale_ko.api.spigot.*;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.ScoreboardManager;

@SuppressWarnings({ "deprecation" })
public class EventHandler implements Listener {
    Main plugin;

    private int taskId;

    public EventHandler(Main plugin) {
        this.plugin = plugin;
    }

    @org.bukkit.event.EventHandler
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

        startTask(player);

        event.setJoinMessage(TextStyler.noPrefix(plugin.config.getString("messages.join-message").replaceAll("%player%", player.getDisplayName()), plugin.config));

        plugin.sendMessage(player, TextStyler.noPrefix(plugin.config.getString("messages.welcome-message"), plugin.config));
    }

    @org.bukkit.event.EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        Task task = new Task(player.getUniqueId());
        if (task.hasId()) task.stop();

        if (!plugin.playerData.getBoolean(player.getUniqueId() + ".ban.banned")) {
            event.setQuitMessage(TextStyler.noPrefix(plugin.config.getString("messages.leave-message").replaceAll("%player%", player.getDisplayName()), plugin.config));
        } else {
            event.setQuitMessage("");
        }
    }

    @org.bukkit.event.EventHandler
    public void onPlayerChat(PlayerChatEvent event) {
        if (plugin.playerData.getBoolean(event.getPlayer().getUniqueId() + ".mute.muted")) {
           plugin.sendMessage(event.getPlayer(), TextStyler.noPrefix(plugin.config.getString("messages.muted-message").replaceAll("%reason%", plugin.playerData.getString(event.getPlayer().getUniqueId() + ".mute.reason")), plugin.config));
            event.setCancelled(true);
        } else {
            event.setFormat(TextStyler.noPrefix(plugin.config.getString("message-format").replaceAll("%player%", event.getPlayer().getDisplayName()).replaceAll("%message%", event.getMessage()), plugin.config));
        }
    }

    @org.bukkit.event.EventHandler
    public void onServerping(ServerListPingEvent event) {
        StringBuilder line1 = new StringBuilder(plugin.config.getString("motd.line1"));
        StringBuilder line2 = new StringBuilder(plugin.config.getString("motd.line2"));

        if (plugin.config.getBoolean("motd.centered")) {
            plugin.log(TextStyler.removeColorCodes(line1.toString()));
            plugin.log(TextStyler.removeColorCodes(line2.toString()));
            for (int i = 0; i < (68 - TextStyler.removeColorCodes(line1.toString()).length()) / 2; i++) {
                line1.insert(0, " ");
            }

            for (int i = 0; i < (68 - TextStyler.removeColorCodes(line2.toString()).length()) / 2; i++) {
                line2.insert(0, " ");
            }
        }

        event.setMotd(TextStyler.noExtra(line1.toString() + "\n&r" + line2.toString()));

        // event.setServerIcon(icon);
        // event.setMaxPlayers(maxPlayers);
    }

    public void startTask(Player player) {
        taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            final Task task = new Task(player.getUniqueId());

            @Override
            public void run() {
                if (!task.hasId())
                    task.setId(taskId);

                createScoreBoard(player, task);

                task.run++;
            }
        }, 0, 20);
    }

    public void createScoreBoard(Player player, Task task) {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        org.bukkit.scoreboard.Scoreboard board = manager.getNewScoreboard();

        Objective objective = board.registerNewObjective("customboard" + task.run, "dummy");

        objective.setDisplayName(TextStyler.noPrefix(plugin.config.getString("scoreboard.title"), plugin.config));

        int index = 0;
        for (String line : plugin.config.getStringList("scoreboard.values")) {
            Score newline = objective.getScore(TextStyler
                    .noPrefix(TextStyler.replacePlaceholders(line, player, plugin.playerData), plugin.config));
            newline.setScore(plugin.config.getStringList("scoreboard.values").size() - index);
            index++;

            if (index == plugin.config.getStringList("scoreboard.values").size()) {
                objective.setDisplaySlot(DisplaySlot.SIDEBAR);
                player.setScoreboard(board);
            }
        }
    }
}
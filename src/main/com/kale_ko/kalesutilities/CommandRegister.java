package com.kale_ko.kalesutilities;

import com.kale_ko.api.spigot.AutoCompleter;
import com.kale_ko.api.spigot.DataManager;
import com.kale_ko.api.spigot.TextStyler;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.WeatherType;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("deprecation")
public class CommandRegister {
    public Main plugin;

    public FileConfiguration config;
    public DataManager playerConfig;
    public FileConfiguration playerData;

    public long time = 6000;
    public WeatherType weather = WeatherType.CLEAR;

    public CommandRegister(Main plugin) {
        this.plugin = plugin;
    }

    public Boolean execute(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        config = plugin.config;
        playerConfig = plugin.playerConfig;
        playerData = plugin.playerData;

        if (label.equalsIgnoreCase("kalesutilities")) {
            if (args.length > 0) {
                if (args[0].equalsIgnoreCase("help")) {
                    if (!checkPermission(sender, "help")) return true;

                    sendMessage(sender, "Type /kalesutilities help for this");
                    sendMessage(sender, "Type /kalesutilities reload to reload the config");
                    sendMessage(sender, "Type /nickname {nickname} to change your name");
                    sendMessage(sender, "Type /realname {nickname} to get the realname of a nicked person");
                    sendMessage(sender, "Type /resetnickname to reset your nickname");
                    sendMessage(sender, "Type /weather (clear, rain) to lock the weather");
                    sendMessage(sender, "Type /time (day, night, {time}) to lock the time");
                    sendMessage(sender, "Type /sudo {player} {command} to run a command or chat as someone else");
                    sendMessage(sender, "Type /lagclear to kill all the items, tnt, ect");
                    sendMessage(sender, "Type /killall to kill all the mobs");
                } else if (args[0].equalsIgnoreCase("reload")) {
                    if (!checkPermission(sender, "reload")) return true;

                    sendMessage(sender, config.getString("messages.reload"));

                    plugin.reloadConfig();
                    playerConfig.reloadConfig();
                    plugin.config = plugin.getConfig();
                    config = plugin.config;
                    plugin.playerData = playerConfig.getConfig();
                    playerData = plugin.playerData;

                    for (Player player : Bukkit.getOnlinePlayers()) {
                        player.setDisplayName(playerData.getString(player.getUniqueId() + ".nickname"));
                    }
                } else {
                    sendMessage(sender, config.getString("messages.not-a-command"));
                }
            } else {
                if (!checkPermission(sender, "kalesutilities.help")) return true;

                sendMessage(sender, "Type /kalesutilities help for this");
                sendMessage(sender, "Type /kalesutilities reload to reload the config");
                sendMessage(sender, "Type /nickname {nickname} to change your name");
                sendMessage(sender, "Type /realname {nickname} to get the realname of a nicked person");
                sendMessage(sender, "Type /resetnickname to reset your nickname");
                sendMessage(sender, "Type /weather (clear, rain) to lock the weather");
                sendMessage(sender, "Type /time (day, night, {time}) to lock the time");
                sendMessage(sender, "Type /sudo {player} {command} to run a command or chat as someone else");
                sendMessage(sender, "Type /lagclear to kill all the items, tnt, ect");
                sendMessage(sender, "Type /killall to kill all the mobs");
            }

            return true;
        } else if (label.equalsIgnoreCase("nickname") || label.equalsIgnoreCase("nick")) {
            if (!checkConsole(sender) || !checkPermission(sender, "nickname")) return true;

            Player player = (Player) sender;

            player.setDisplayName(TextStyler.noExtra(args[0]));
            playerData.set(player.getUniqueId() + ".nickname", args[0]);
            playerConfig.saveConfig();

            sendMessage(sender, config.getString("messages.setnickname").replaceAll("%nickname%", args[0]));

            return true;
        } else if (label.equalsIgnoreCase("realname") || label.equalsIgnoreCase("getname")) {
            if (!checkPermission(sender, "nickname")) return true;

            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.getDisplayName().contains(args[0])) sendMessage(sender, config.getString("messages.realname").replaceAll("%nickname%", player.getDisplayName()).replaceAll("%name%", player.getName()));
            }

            return true;
        } else if (label.equalsIgnoreCase("resetnickname") || label.equalsIgnoreCase("resetname")) {
            if (!checkConsole(sender) || !checkPermission(sender, "nickname")) return true;

            Player player = (Player) sender;

            player.setDisplayName(player.getName());
            playerData.set(player.getUniqueId() + ".nickname", player.getName());
            playerConfig.saveConfig();

            sendMessage(sender, config.getString("messages.resetname"));

            return true;
        } else if (label.equalsIgnoreCase("weather") || label.equalsIgnoreCase("setweather")) {
            if (!checkPermission(sender, "weather")) return true;

            if (args[0].equalsIgnoreCase("clear")) {
                weather = WeatherType.CLEAR;

                sendMessage(sender, config.getString("messages.set-weather").replaceAll("%weather%", "clear"));
            } else if (args[0].equalsIgnoreCase("rain")) {
                weather = WeatherType.DOWNFALL;

                sendMessage(sender, config.getString("messages.set-weather").replaceAll("%weather%", "rain"));
            } else {
                sendMessage(sender, config.getString("messages.not-a-weather"));
            }

            return true;
        } else if (label.equalsIgnoreCase("time") || label.equalsIgnoreCase("settime")) {
            if (!checkPermission(sender, "time")) return true;

            if (args[0].equalsIgnoreCase("day")) {
                time = 6000;

                sendMessage(sender, config.getString("messages.set-time").replaceAll("%time%", "day"));
            } else if (args[0].equalsIgnoreCase("night")) {
                time = 18000;

                sendMessage(sender, config.getString("messages.set-time").replaceAll("%time%", "night"));
            } else {
                if (Long.parseLong(args[0]) > -1) {
                    time = Long.parseLong(args[0]) ;

                    sendMessage(sender, config.getString("messages.set-time").replaceAll("%time%", args[0]));
                } else {
                    sendMessage(sender, config.getString("messages.not-a-time"));
                }
            }

            return true;
        } else if (label.equalsIgnoreCase("sudo") || label.equalsIgnoreCase("runcommandas") || label.equalsIgnoreCase("runas")) {
            if (!checkPermission(sender, "sudo")) return true;

            if (args[1].startsWith("/")) {
                StringBuilder sudocommand = new StringBuilder();

                int index = 0;
                for (String string : args) {
                    if (index != 0) sudocommand.append(string).append(" ");

                    index++;
                }

                if (args[0].equalsIgnoreCase("*") || args[0].equalsIgnoreCase("@a")) {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        Bukkit.getServer().dispatchCommand(player, sudocommand.substring(1));
                    }
                } else {
                    Bukkit.getServer().dispatchCommand(Bukkit.getPlayer(args[0]), sudocommand.substring(1));
                }

                sendMessage(sender, config.getString("messages.sudo-command").replaceAll("%command%", sudocommand.toString()).replaceAll("%player%", args[0]));
            } else {
                StringBuilder sudomessage = new StringBuilder();

                int index = 0;
                for (String string : args) {
                    if (index != 0) sudomessage.append(string).append(" ");

                    index++;
                }

                if (args[0].equalsIgnoreCase("*") || args[0].equalsIgnoreCase("@a")) {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        player.sendMessage(sudomessage.toString());
                    }
                } else {
                    Bukkit.getPlayer(args[0]).sendMessage(sudomessage.toString());
                }

                sendMessage(sender, config.getString("messages.sudo-message").replaceAll("%message%", sudomessage.toString()).replaceAll("%player%", args[0]));
            }

            return true;
        } else if (label.equalsIgnoreCase("kick")) {
            if (!checkPermission(sender, "admin.kick")) return true;

            StringBuilder reason = new StringBuilder();

            int index = 0;
            for (String string : args) {
                if (index != 0) reason.append(string).append(" ");

                index++;
            }

            Bukkit.getPlayer(args[0]).kickPlayer(TextStyler.noPrefix(config.getString("messages.kick-message").replaceAll("%reason%", reason.toString()), config));

            return true;
        } else if (label.equalsIgnoreCase("mute")) {
            if (!checkPermission(sender, "admin.mute")) return true;

            Player player = Bukkit.getPlayer(args[0]);

            StringBuilder reason = new StringBuilder();

            int index = 0;
            for (String string : args) {
                if (index != 0) reason.append(string).append(" ");

                index++;
            }

            playerData.set(player.getUniqueId() + ".mute.muted", true);
            playerData.set(player.getUniqueId() + ".mute.reason", reason.toString());
            playerConfig.saveConfig();

            sendMessage(Bukkit.getPlayer(args[0]), TextStyler.noPrefix(config.getString("messages.mute-message").replaceAll("%reason%", reason.toString()), config));

            return true;
        } else if (label.equalsIgnoreCase("unmute")) {
            if (!checkPermission(sender, "admin.mute")) return true;

            OfflinePlayer player = Bukkit.getOfflinePlayer(args[0]);

            @SuppressWarnings("MismatchedQueryAndUpdateOfStringBuilder") StringBuilder reason = new StringBuilder();

            int index = 0;
            for (String string : args) {
                if (index != 0) reason.append(string).append(" ");

                index++;
            }

            playerData.set(player.getUniqueId() + ".mute.muted", false);
            playerData.set(player.getUniqueId() + ".mute.reason", "");
            playerConfig.saveConfig();

            sendMessage(Bukkit.getPlayer(args[0]), TextStyler.noPrefix(config.getString("messages.unmute-message").replaceAll("%reason%", reason.toString()), config));

            return true;
        } else if (label.equalsIgnoreCase("ban")) {
            if (!checkPermission(sender, "admin.ban")) return true;

            Player player = Bukkit.getPlayer(args[0]);

            StringBuilder reason = new StringBuilder();

            int index = 0;
            for (String string : args) {
                if (index != 0) reason.append(string).append(" ");

                index++;
            }

            playerData.set(player.getUniqueId() + ".ban.banned", true);
            playerData.set(player.getUniqueId() + ".ban.reason", reason.toString());
            playerConfig.saveConfig();

            Bukkit.getPlayer(args[0]).kickPlayer(TextStyler.noPrefix(config.getString("messages.ban-message").replaceAll("%reason%", reason.toString()), config));

            return true;
        } else if (label.equalsIgnoreCase("unban")) {
            if (!checkPermission(sender, "admin.ban")) return true;

            OfflinePlayer player = Bukkit.getOfflinePlayer(args[0]);

            @SuppressWarnings("MismatchedQueryAndUpdateOfStringBuilder") StringBuilder reason = new StringBuilder();

            int index = 0;
            for (String string : args) {
                if (index != 0) reason.append(string).append(" ");

                index++;
            }

            playerData.set(player.getUniqueId() + ".ban.banned", false);
            playerData.set(player.getUniqueId() + ".ban.reason", "");
            playerConfig.saveConfig();

            return true;
        } else if (label.equalsIgnoreCase("lagclear") || label.equalsIgnoreCase("clearlag")) {
            if (!checkPermission(sender, "lagclear")) return true;

            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "kill @e[type=minecraft:item]");
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "kill @e[type=minecraft:tnt]");
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "kill @e[type=minecraft:experience_orb]");

            sendMessage(sender, config.getString("messages.lagclear"));

            return true;
        } else if (label.equalsIgnoreCase("killall") || label.equalsIgnoreCase("butcher")) {
            if (!checkPermission(sender, "lagclear")) return true;

            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "kill @e[type=!minecraft:player]");

            sendMessage(sender, config.getString("messages.killall"));

            return true;
        } else if (label.equalsIgnoreCase("message") || label.equalsIgnoreCase("msg")) {
            if (!checkConsole(sender)) return true;

            Player player = (Player) sender;

            StringBuilder message = new StringBuilder();

            int index = 0;
            for (String string : args) {
                if (index != 0) message.append(string).append(" ");

                index++;
            }

            Bukkit.getPlayer(args[0]).sendMessage(TextStyler.noPrefix(config.getString("messages.dirrectmessage").replaceAll("%player%", player.getDisplayName()).replaceAll("%message%", message.toString()), config));

            return true;
        }

        return false;
    }

    public void registerTickEvent() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            for (World world : Bukkit.getServer().getWorlds()) {
                world.setTime(time);
                world.setFullTime(time);

                for (Player player : world.getPlayers()) {
                    player.setPlayerWeather(weather);
                }

                world.setGameRuleValue("doDaylightCycle", "false");
                world.setGameRuleValue("doWeatherCycle", "false");
            }
        }, 0, 20);
    }

    public void registerCommands() {
        List<String> subcommands = new ArrayList<>();
        subcommands.add("help");
        subcommands.add("reload");
        plugin.getCommand("kalesutilities").setTabCompleter(new AutoCompleter(subcommands));

        subcommands.clear();
        subcommands.add("clear");
        subcommands.add("rain");
        plugin.getCommand("weather").setTabCompleter(new AutoCompleter(subcommands));

        subcommands.clear();
        subcommands.add("day");
        subcommands.add("night");
        plugin.getCommand("time").setTabCompleter(new AutoCompleter(subcommands));
    }

    public Boolean checkPermission(CommandSender sender, String permission) {
        if (sender.hasPermission("kalesutilities." + permission)) {
            return true;
        } else {
            sender.sendMessage(config.getString("messages.nopermission").replaceAll("%permission%", permission));
            return false;
        }
    }

    public Boolean checkConsole(CommandSender sender) {
        if (sender instanceof Player) {
            return true;
        } else {
            sendMessage(sender, config.getString("messages.no-console"));

            return false;
        }
    }

    public void sendMessage(CommandSender sender, String message) {
        sender.sendMessage(TextStyler.style(message, config));
    }
}
package com.kale_ko.kalesutilities;

import com.kale_ko.api.spigot.*;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.WeatherType;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.ArrayList;
import java.util.List;

public class Main extends JavaPlugin {
    public FileConfiguration config = this.getConfig();
    public DataManager playerConfig = new DataManager("players.yml", this);
    public FileConfiguration playerData = playerConfig.getConfig();

    public long time = 6000;
    public WeatherType weather = WeatherType.CLEAR;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        this.getConfig().options().copyDefaults(true);
        saveConfig();
        config = this.getConfig();

        this.getServer().getPluginManager().registerEvents(new EventHandler(this), this);

        List<String> subcommands = new ArrayList<>();
        subcommands.add("help");
        subcommands.add("reload");
        this.getCommand("kalesutilities").setTabCompleter(new AutoCompleter(subcommands));

        subcommands.clear();
        subcommands.add("clear");
        subcommands.add("rain");
        this.getCommand("weather").setTabCompleter(new AutoCompleter(subcommands));
        this.getCommand("setweather").setTabCompleter(new AutoCompleter(subcommands));

        subcommands.clear();
        subcommands.add("day");
        subcommands.add("night");
        this.getCommand("time").setTabCompleter(new AutoCompleter(subcommands));
        this.getCommand("settime").setTabCompleter(new AutoCompleter(subcommands));

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
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

        log("Enabled");
    }

    @Override
    public void onDisable() {
        saveConfig();

        log("Disabled");
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("kalesutilities")) {
            if (args.length > 0) {
                if (args[0].equalsIgnoreCase("help")) {
                    sendMessage(sender, "Type /kalesutilities help for this");
                    sendMessage(sender, "Type /kalesutilities reload to reload the config");
                    sendMessage(sender, "Type /nickname {nickname} to change your name");
                    sendMessage(sender, "Type /realname {nickname} to get the realname of a nicked person");
                    sendMessage(sender, "Type /resetnickname to reset your nickname");
                    sendMessage(sender, "Type /weather (clear, rain) to lock the weather");
                    sendMessage(sender, "Type /time (day, night, {time}) to lock the time");
                    sendMessage(sender, "Type /sudo {player} {command} to run a command or chat as someone else");
                } else if (args[0].equalsIgnoreCase("reload")) {
                    sendMessage(sender, config.getString("messages.reload"));

                    this.reloadConfig();
                    config = this.getConfig();
                } else {
                    sendMessage(sender, config.getString("messages.not-a-command"));
                }
            } else {
                sendMessage(sender, "Type /kalesutilities help for this");
                sendMessage(sender, "Type /kalesutilities reload to reload the config");
                sendMessage(sender, "Type /nickname {nickname} to change your name");
                sendMessage(sender, "Type /realname {nickname} to get the realname of a nicked person");
                sendMessage(sender, "Type /resetnickname to reset your nickname");
                sendMessage(sender, "Type /weather (clear, rain) to lock the weather");
                sendMessage(sender, "Type /time (day, night, {time}) to lock the time");
                sendMessage(sender, "Type /sudo {player} {command} to run a command or chat as someone else");
            }

            return true;
        } else if (label.equalsIgnoreCase("nickname") || label.equalsIgnoreCase("nick") || label.equalsIgnoreCase("name") || label.equalsIgnoreCase("setname")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;

                if (!config.getStringList("forbiden-nicknames").contains(args[0])) {
                    player.setDisplayName(TextStyler.noExtra(args[0]));

                    playerData.set(player.getUniqueId() + ".nickname", args[0]);
                    playerConfig.saveConfig();

                    sendMessage(sender, config.getString("messages.setnickname").replaceAll("%nickname%", args[0]));
                } else {
                    sendMessage(sender, config.getString("messages.disallowed-name"));
                }
            } else {
                isConsole(sender);
            }

            return true;
        } else if (label.equalsIgnoreCase("realname") || label.equalsIgnoreCase("getname")) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.getDisplayName().contains(args[0])) sendMessage(sender, config.getString("messages.realname").replaceAll("%nickname%", player.getDisplayName()).replaceAll("%name%", player.getName()));
            }

            return true;
        } else if (label.equalsIgnoreCase("resetnickname") || label.equalsIgnoreCase("resetname")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;

                player.setDisplayName(player.getName());

                sendMessage(sender, config.getString("messages.resetname"));
            } else {
                isConsole(sender);
            }

            return true;
        } else if (label.equalsIgnoreCase("weather") || label.equalsIgnoreCase("setweather")) {
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
                        Bukkit.getServer().dispatchCommand(player, sudomessage.substring(1));
                    }
                } else {
                    Bukkit.getServer().dispatchCommand(Bukkit.getPlayer(args[0]), sudomessage.substring(1));
                }

                sendMessage(sender, config.getString("messages.sudo-message").replaceAll("%message%", sudomessage.toString()).replaceAll("%player%", args[0]));
            }

            return true;
        } else if (label.equalsIgnoreCase("kick")) {
            StringBuilder reason = new StringBuilder();

            int index = 0;
            for (String string : args) {
                if (index != 0) reason.append(string).append(" ");

                index++;
            }

            Bukkit.getPlayer(args[0]).kickPlayer(TextStyler.noPrefix(config.getString("messages.kick-message").replaceAll("%reason%", reason.toString()), config));
        } else if (label.equalsIgnoreCase("ban")) {
            Player player = Bukkit.getPlayer(args[0]);

            StringBuilder reason = new StringBuilder();

            int index = 0;
            for (String string : args) {
                if (index != 0) reason.append(string).append(" ");

                index++;
            }

            playerData.set(player.getUniqueId() + ".banned", true);
            playerData.set(player.getUniqueId() + ".reason", reason.toString());
            playerConfig.saveConfig();

            Bukkit.getPlayer(args[0]).kickPlayer(TextStyler.noPrefix(config.getString("messages.ban-message").replaceAll("%reason%", reason.toString()), config));
        } else if (label.equalsIgnoreCase("unban")) {
            OfflinePlayer player = Bukkit.getOfflinePlayer(args[0]);

            StringBuilder reason = new StringBuilder();

            int index = 0;
            for (String string : args) {
                if (index != 0) reason.append(string).append(" ");

                index++;
            }

            playerData.set(player.getUniqueId() + ".banned", false);
            playerData.set(player.getUniqueId() + ".reason", "");
            playerConfig.saveConfig();
        }

        return false;
    }

    public void isConsole(CommandSender sender) {
        sendMessage(sender, config.getString("messages.no-console"));
    }

    public void sendMessage(CommandSender sender, String message) {
        sender.sendMessage(TextStyler.style(message, config));
    }

    public void log(String message) {
        getLogger().info(message);
    }

    public void warn(String message) {
        getLogger().warning(message);
    }

    public void error(String message) {
        getLogger().severe(message);
    }
}
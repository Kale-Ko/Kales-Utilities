package com.kale_ko.kalesutilities;

import com.kale_ko.api.spigot.DataManager;
import com.kale_ko.api.spigot.TextStyler;
import com.kale_ko.kalesutilities.commands.*;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CommandRegister {
    public Main plugin;
    public FileConfiguration config;
    public DataManager playerConfig;
    public FileConfiguration playerData;
    public DataManager serverConfig;
    public FileConfiguration serverData;

    public long time = 6000;
    public WeatherType weather = WeatherType.CLEAR;

    public CommandRegister(Main plugin) {
        this.plugin = plugin;
        this.config = plugin.config;
        this.playerConfig = plugin.playerConfig;
        this.playerData = plugin.playerData;
        this.serverConfig = plugin.serverConfig;
        this.serverData = plugin.serverData;
    }

    public Boolean execute(CommandSender sender, Command command, String label, String[] args) {
        config = plugin.config;
        playerConfig = plugin.playerConfig;
        playerData = plugin.playerData;
        serverConfig = plugin.serverConfig;
        serverData = plugin.serverData;

        if (label.equalsIgnoreCase("kalesutilities")) {
            return new KalesUtilities().execute(sender, args, this);
        } else if (label.equalsIgnoreCase("nickname") || label.equalsIgnoreCase("nick")) {
            return new Name().setNickname(sender, args, this);
        } else if (label.equalsIgnoreCase("realname") || label.equalsIgnoreCase("getname")) {
            return new Name().getNickname(sender, args, this);
        } else if (label.equalsIgnoreCase("resetnickname") || label.equalsIgnoreCase("resetname")) {
            return new Name().resetNickname(sender, args, this);
        } else if (label.equalsIgnoreCase("prefix")) {
            return new Name().setPrefix(sender, args, this);
        } else if (label.equalsIgnoreCase("weather") || label.equalsIgnoreCase("setweather")) {
            return new Weather().execute(sender, args, this);
        } else if (label.equalsIgnoreCase("time") || label.equalsIgnoreCase("settime")) {
            return new Time().execute(sender, args, this);
        } else if (label.equalsIgnoreCase("sudo") || label.equalsIgnoreCase("runcommandas") || label.equalsIgnoreCase("runas")) {
            return new Sudo().execute(sender, args, this);
        } else if (label.equalsIgnoreCase("kick")) {
            return new Kick().execute(sender, args, this);
        } else if (label.equalsIgnoreCase("mute")) {
            return new Mute().mute(sender, args, this);
        } else if (label.equalsIgnoreCase("unmute")) {
            return new Mute().unMute(sender, args, this);
        } else if (label.equalsIgnoreCase("mutechat")) {
            return new Mute().muteChat(sender, args, this);
        } else if (label.equalsIgnoreCase("ban")) {
            return new Ban().unBan(sender, args, this);
        } else if (label.equalsIgnoreCase("unban")) {
            return new Ban().ban(sender, args, this);
        } else if (label.equalsIgnoreCase("lagclear") || label.equalsIgnoreCase("clearlag")) {
            return new Lagclear().lagClear(sender, command, args, this);
        } else if (label.equalsIgnoreCase("killall") || label.equalsIgnoreCase("butcher")) {
            return new Lagclear().killAll(sender, command, args, this);
        } else if (label.equalsIgnoreCase("message") || label.equalsIgnoreCase("msg")) {
            return new Message().execute(sender, args, this);
        } else if (label.equalsIgnoreCase("setworldspawn") || label.equalsIgnoreCase("setspawn")) {
            return new Spawn().set(sender, args, this);
        } else if (label.equalsIgnoreCase("spawn")) {
            return new Spawn().goTo(sender, args, this);
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
            }
        }, 0, 20);
    }

    public void registerPermissions() {
        Map<String, Boolean> children = new HashMap<>();

        children.put("kalesutilities.help", true);
        children.put("kalesutilities.reload", true);
        children.put("kalesutilities.spawn", true);
        children.put("kalesutilities.admin", true);
        children.put("kalesutilities.name", true);
        Bukkit.getPluginManager().addPermission(new Permission("kalesutilities.*", PermissionDefault.OP, children));


        Bukkit.getPluginManager().addPermission(new Permission("kalesutilities.help", PermissionDefault.TRUE));


        Bukkit.getPluginManager().addPermission(new Permission("kalesutilities.reload"));


        Bukkit.getPluginManager().addPermission(new Permission("kalesutilities.spawn", PermissionDefault.TRUE));


        children.clear();
        children.put("kalesutilities.admin.moderation", true);
        children.put("kalesutilities.admin.world", true);
        children.put("kalesutilities.admin.sudo", true);
        Bukkit.getPluginManager().addPermission(new Permission("kalesutilities.admin", children));

        children.clear();
        children.put("kalesutilities.admin.moderation.kick", true);
        children.put("kalesutilities.admin.moderation.mute", true);
        children.put("kalesutilities.admin.moderation.ban", true);
        children.put("kalesutilities.admin.moderation.mutechat", true);
        Bukkit.getPluginManager().addPermission(new Permission("kalesutilities.admin.moderation", children));
        Bukkit.getPluginManager().addPermission(new Permission("kalesutilities.admin.moderation.kick"));
        Bukkit.getPluginManager().addPermission(new Permission("kalesutilities.admin.moderation.mute"));
        Bukkit.getPluginManager().addPermission(new Permission("kalesutilities.admin.moderation.ban"));
        Bukkit.getPluginManager().addPermission(new Permission("kalesutilities.admin.moderation.mutechat"));

        children.clear();
        children.put("kalesutilities.admin.world.weather", true);
        children.put("kalesutilities.admin.world.time", true);
        children.put("kalesutilities.admin.world.lagclear", true);
        children.put("kalesutilities.admin.world.setspawn", true);
        Bukkit.getPluginManager().addPermission(new Permission("kalesutilities.admin.world", children));
        Bukkit.getPluginManager().addPermission(new Permission("kalesutilities.admin.world.weather"));
        Bukkit.getPluginManager().addPermission(new Permission("kalesutilities.admin.world.time"));
        Bukkit.getPluginManager().addPermission(new Permission("kalesutilities.admin.world.lagclear"));
        Bukkit.getPluginManager().addPermission(new Permission("kalesutilities.admin.world.setspawn"));

        Bukkit.getPluginManager().addPermission(new Permission("kalesutilities.admin.sudo"));


        children.clear();
        children.put("kalesutilities.name.nickname", true);
        children.put("kalesutilities.name.prefix", true);
        Bukkit.getPluginManager().addPermission(new Permission("kalesutilities.name", children));
        Bukkit.getPluginManager().addPermission(new Permission("kalesutilities.name.nickname"));
        Bukkit.getPluginManager().addPermission(new Permission("kalesutilities.name.prefix"));
    }

    public Boolean checkForParameters(CommandSender sender, String[] neededArgs, String[] args) {
        if (args.length < neededArgs.length) {
            sendMessage(sender, config.getString("messages.need-parameters").replaceAll("%amount%", Integer.toString(neededArgs.length)).replaceAll("%parameters%", Arrays.toString(neededArgs)));

            return false;
        } else {
            return true;
        }
    }

    public Boolean checkPermission(CommandSender sender, String permission) {
        if (sender.hasPermission("kalesutilities." + permission)) {
            return true;
        } else {
            sendMessage(sender, config.getString("messages.nopermission").replaceAll("%permission%", permission));

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
        sender.sendMessage(TextStyler.style(message, plugin.config));
    }
}
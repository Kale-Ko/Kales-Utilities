/**
    @license
    MIT License
    Copyright (c) 2021 Kale Ko
    See https://kaleko.ga/license.txt
*/

package com.kale_ko.kalesutilities.commands;

import com.kale_ko.kalesutilities.CommandRegister;
import com.kale_ko.kalesutilities.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Spawn {
    Main plugin;
    CommandRegister commandRegister;

    public boolean set(CommandSender sender, String[] args, CommandRegister commandRegister) {
        this.commandRegister = commandRegister;
        this.plugin = commandRegister.plugin;

        String[] neededArgs = new String[1];
        neededArgs[0] = "location";
        if (!commandRegister.checkForParameters(sender, neededArgs, args) || !commandRegister.checkPermission(sender, "admin.world.setspawn")) return true;

        if (args[0].equalsIgnoreCase("here")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;

                plugin.serverData.set("spawnLocation.world", player.getLocation().getWorld().getName());
                plugin.serverData.set("spawnLocation.x", player.getLocation().getX());
                plugin.serverData.set("spawnLocation.y", player.getLocation().getY());
                plugin.serverData.set("spawnLocation.z", player.getLocation().getZ());
                plugin.serverData.set("spawnLocation.rotation", player.getLocation().getYaw());
                plugin.serverConfig.saveConfig();
            } else {
                commandRegister.sendMessage(sender, plugin.config.getString("messages.setspawn.noconsole"));
            }
        } else {
            if (!args[0].equalsIgnoreCase("~")) plugin.serverData.set("spawnLocation.world", args[0]);
            if (!args[1].equalsIgnoreCase("~")) plugin.serverData.set("spawnLocation.x", Double.valueOf(args[1]));
            if (!args[2].equalsIgnoreCase("~")) plugin.serverData.set("spawnLocation.y", Double.valueOf(args[2]));
            if (!args[3].equalsIgnoreCase("~")) plugin.serverData.set("spawnLocation.z", Double.valueOf(args[3]));
            if (!args[4].equalsIgnoreCase("~")) plugin.serverData.set("spawnLocation.rotation", Double.valueOf(args[4]));
            plugin.serverConfig.saveConfig();

            if (sender instanceof Player) {
                Player player = (Player) sender;

                if (args[0].equalsIgnoreCase("~")) plugin.serverData.set("spawnLocation.world", player.getLocation().getWorld().getName());
                if (args[1].equalsIgnoreCase("~")) plugin.serverData.set("spawnLocation.x", player.getLocation().getX());
                if (args[2].equalsIgnoreCase("~")) plugin.serverData.set("spawnLocation.y", player.getLocation().getY());
                if (args[3].equalsIgnoreCase("~")) plugin.serverData.set("spawnLocation.z", player.getLocation().getZ());
                if (args[4].equalsIgnoreCase("~")) plugin.serverData.set("spawnLocation.rotation", player.getLocation().getYaw());
                plugin.serverConfig.saveConfig();
            } else {
                if (args[0].equalsIgnoreCase("~") || args[1].equalsIgnoreCase("~") || args[2].equalsIgnoreCase("~") || args[3].equalsIgnoreCase("~") || args[4].equalsIgnoreCase("~")) commandRegister.sendMessage(sender, plugin.config.getString("messages.setspawn.noconsole"));
            }
        }

        return true;
    }

    public boolean goTo(CommandSender sender, String[] args, CommandRegister commandRegister) {
        this.commandRegister = commandRegister;
        this.plugin = commandRegister.plugin;

        if (!commandRegister.checkConsole(sender) || !commandRegister.checkPermission(sender, "spawn")) return true;

        Player player = (Player) sender;

        player.teleport(new Location(Bukkit.getWorld(plugin.serverData.getString("spawnLocation.world")), plugin.serverData.getDouble("spawnLocation.x"), plugin.serverData.getDouble("spawnLocation.y"), plugin.serverData.getDouble("spawnLocation.z"), (float) plugin.serverData.getDouble("spawnLocation.rotation"), 0F));

        return true;
    }
}
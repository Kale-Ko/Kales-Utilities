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
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Lagclear {
    Main plugin;
    CommandRegister commandRegister;

    public boolean lagClear(CommandSender sender, Command command,  String[] args, CommandRegister commandRegister) {
        this.commandRegister = commandRegister;
        this.plugin = commandRegister.plugin;

        if (!commandRegister.checkConsole(sender) || !commandRegister.checkPermission(sender, "admin.world.lagclear")) return true;

        Player player = (Player) sender;

        String previos = player.getWorld().getGameRuleValue("sendCommandFeedback");
        player.getWorld().setGameRuleValue("sendCommandFeedback","false");

        String[] theArgs = new String[2];
        theArgs[0] = sender.getName();
        theArgs[1] = "/execute as @e[type=minecraft:item,limit=1] run kill @e[type=minecraft:item]";
        plugin.onCommand(Bukkit.getConsoleSender(), command, "sudo", theArgs);
        theArgs[1] = "/execute as @e[type=minecraft:tnt,limit=1] run kill @e[type=minecraft:tnt]";
        plugin.onCommand(Bukkit.getConsoleSender(), command, "sudo", theArgs);
        theArgs[1] = "/execute as @e[type=minecraft:experience_orb,limit=1] run kill @e[type=minecraft:experience_orb]";
        plugin.onCommand(Bukkit.getConsoleSender(), command, "sudo", theArgs);

        player.getWorld().setGameRuleValue("sendCommandFeedback", previos);

        commandRegister.sendMessage(sender, plugin.config.getString("messages.lagclear"));

        return true;
    }

    public boolean killAll(CommandSender sender, Command command, String[] args, CommandRegister commandRegister) {
        this.commandRegister = commandRegister;
        this.plugin = commandRegister.plugin;

        if (!commandRegister.checkConsole(sender) || !commandRegister.checkPermission(sender, "admin.world.lagclear")) return true;

        Player player = (Player) sender;

        String previos = player.getWorld().getGameRuleValue("sendCommandFeedback");
        player.getWorld().setGameRuleValue("sendCommandFeedback","false");

        String[] theArgs = new String[2];
        theArgs[0] = sender.getName();
        theArgs[1] = "/execute as @e[type=!minecraft:player,limit=1] run kill @e[type=!minecraft:player]";
        plugin.onCommand(Bukkit.getConsoleSender(), command, "sudo", theArgs);

        player.getWorld().setGameRuleValue("sendCommandFeedback", previos);

        commandRegister.sendMessage(sender, plugin.config.getString("messages.killall"));

        return true;
    }
}
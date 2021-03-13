package com.kale_ko.kalesutilities.commands;

import com.kale_ko.kalesutilities.CommandRegister;
import com.kale_ko.kalesutilities.Main;
import org.bukkit.command.CommandSender;

public class Time {
    Main plugin;
    CommandRegister commandRegister;

    public boolean execute(CommandSender sender, String[] args, CommandRegister commandRegister) {
        this.commandRegister = commandRegister;
        this.plugin = commandRegister.plugin;

        String[] neededArgs = new String[1];
        neededArgs[0] = "time";
        if (!commandRegister.checkPermission(sender, "admin.world.time") || !commandRegister.checkForParameters(sender, neededArgs, args)) return true;

        if (args[0].equalsIgnoreCase("day")) {
            commandRegister.time = 6000;

            commandRegister.sendMessage(sender, plugin.config.getString("messages.set-time").replaceAll("%time%", "day"));
        } else if (args[0].equalsIgnoreCase("night")) {
            commandRegister.time = 18000;

            commandRegister.sendMessage(sender, plugin.config.getString("messages.set-time").replaceAll("%time%", "night"));
        } else {
            if (Long.parseLong(args[0]) > -1) {
                commandRegister.time = Long.parseLong(args[0]) ;

                commandRegister.sendMessage(sender, plugin.config.getString("messages.set-time").replaceAll("%time%", args[0]));
            } else {
                commandRegister.sendMessage(sender, plugin.config.getString("messages.not-a-time"));
            }
        }

        return true;
    }
}
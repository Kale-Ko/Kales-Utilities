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
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Sudo {
    Main plugin;
    CommandRegister commandRegister;

    public boolean execute(CommandSender sender, String[] args, CommandRegister commandRegister) {
        this.commandRegister = commandRegister;
        this.plugin = commandRegister.plugin;

        String[] neededArgs = new String[2];
        neededArgs[0] = "player";
        neededArgs[1] = "command/message";
        if (!commandRegister.checkPermission(sender, "admin.sudo") || !commandRegister.checkForParameters(sender, neededArgs, args)) return true;

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

            commandRegister.sendMessage(sender, plugin.config.getString("messages.sudo-command").replaceAll("%command%", sudocommand.toString()).replaceAll("%player%", args[0]));
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

            commandRegister.sendMessage(sender, plugin.config.getString("messages.sudo-message").replaceAll("%message%", sudomessage.toString()).replaceAll("%player%", args[0]));
        }

        return true;
    }
}
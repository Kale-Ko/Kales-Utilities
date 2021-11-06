/**
    @license
    MIT License
    Copyright (c) 2021 Kale Ko
    See https://kaleko.ga/license.txt
*/

package com.kale_ko.kalesutilities.commands;

import com.kale_ko.api.spigot.TextStyler;
import com.kale_ko.kalesutilities.CommandRegister;
import com.kale_ko.kalesutilities.Main;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Mute {
    Main plugin;
    CommandRegister commandRegister;

    public boolean mute(CommandSender sender, String[] args, CommandRegister commandRegister) {
        this.commandRegister = commandRegister;
        this.plugin = commandRegister.plugin;

        String[] neededArgs = new String[2];
        neededArgs[0] = "player";
        neededArgs[1] = "reason";
        if (!commandRegister.checkPermission(sender, "admin.moderation.mute") || !commandRegister.checkForParameters(sender, neededArgs, args)) return true;

        Player player = Bukkit.getPlayer(args[0]);

        StringBuilder reason = new StringBuilder();

        int index = 0;
        for (String string : args) {
            if (index != 0) reason.append(string).append(" ");

            index++;
        }

        plugin.playerData.set(player.getUniqueId() + ".mute.muted", true);
        plugin.playerData.set(player.getUniqueId() + ".mute.reason", reason.toString());
        plugin.playerConfig.saveConfig();

        commandRegister.sendMessage(Bukkit.getPlayer(args[0]), TextStyler.noPrefix(plugin.config.getString("messages.mute-message").replaceAll("%reason%", reason.toString()), plugin.config));

        return true;
    }

    public boolean unMute(CommandSender sender, String[] args, CommandRegister commandRegister) {
        this.commandRegister = commandRegister;
        this.plugin = commandRegister.plugin;

        String[] neededArgs = new String[2];
        neededArgs[0] = "player";
        neededArgs[1] = "reason";
        if (!commandRegister.checkPermission(sender, "admin.moderation.mute") || !commandRegister.checkForParameters(sender, neededArgs, args)) return true;

        OfflinePlayer player = Bukkit.getOfflinePlayer(args[0]);

        StringBuilder reason = new StringBuilder();

        int index = 0;
        for (String string : args) {
            if (index != 0) reason.append(string).append(" ");

            index++;
        }

        plugin.playerData.set(player.getUniqueId() + ".mute.muted", false);
        plugin.playerData.set(player.getUniqueId() + ".mute.reason", "");
        plugin.playerConfig.saveConfig();

        commandRegister.sendMessage(Bukkit.getPlayer(args[0]), TextStyler.noPrefix(plugin.config.getString("messages.unmute-message").replaceAll("%reason%", reason.toString()), plugin.config));

        return true;
    }

    public boolean muteChat(CommandSender sender, String[] args, CommandRegister commandRegister) {
        this.commandRegister = commandRegister;
        this.plugin = commandRegister.plugin;

        if (!commandRegister.checkPermission(sender, "admin.moderation.mutechat")) return true;

        if (!plugin.serverData.getBoolean("chatmuted")) {
            plugin.serverData.set("chatmuted", true);
            plugin.serverConfig.saveConfig();

            if (sender instanceof Player) {
                Player player = (Player) sender;

                commandRegister.sendMessage(sender, plugin.config.getString("messages.mute-chat").replaceAll("%player%",  player.getDisplayName()));
            } else {
                commandRegister.sendMessage(sender, plugin.config.getString("messages.mute-chat").replaceAll("%player%",  "console"));
            }
        } else {
            plugin.serverData.set("chatmuted", false);
            plugin.serverConfig.saveConfig();

            if (sender instanceof Player) {
                Player player = (Player) sender;

                commandRegister.sendMessage(sender, plugin.config.getString("messages.unmute-chat").replaceAll("%player%",  player.getDisplayName()));
            } else {
                commandRegister.sendMessage(sender, plugin.config.getString("messages.unmute-chat").replaceAll("%player%",  "console"));
            }
        }

        return true;
    }
}
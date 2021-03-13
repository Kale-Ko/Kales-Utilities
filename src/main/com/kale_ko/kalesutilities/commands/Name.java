package com.kale_ko.kalesutilities.commands;

import com.kale_ko.api.spigot.TextStyler;
import com.kale_ko.kalesutilities.CommandRegister;
import com.kale_ko.kalesutilities.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Name {
    Main plugin;
    CommandRegister commandRegister;

    public boolean setNickname(CommandSender sender, String[] args, CommandRegister commandRegister) {
        this.commandRegister = commandRegister;
        this.plugin = commandRegister.plugin;

        String[] neededArgs = new String[1];
        neededArgs[0] = "name";
        if (!commandRegister.checkConsole(sender) || !commandRegister.checkPermission(sender, "name.nickname") || !commandRegister.checkForParameters(sender, neededArgs, args)) return true;

        Player player = (Player) sender;

        player.setDisplayName(TextStyler.noExtra(args[0]));
        plugin.playerData.set(player.getUniqueId() + ".nickname", args[0]);
        plugin.playerConfig.saveConfig();

        commandRegister.sendMessage(sender, plugin.config.getString("messages.setnickname").replaceAll("%nickname%", args[0]));

        return true;
    }

    public Boolean getNickname(CommandSender sender, String[] args, CommandRegister commandRegister) {
        this.commandRegister = commandRegister;
        this.plugin = commandRegister.plugin;

        String[] neededArgs = new String[1];
        neededArgs[0] = "nickname";
        if (!commandRegister.checkPermission(sender, "name.nickname") || !commandRegister.checkForParameters(sender, neededArgs, args)) return true;

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getDisplayName().contains(args[0])) commandRegister.sendMessage(sender, plugin.config.getString("messages.realname").replaceAll("%nickname%", player.getDisplayName()).replaceAll("%name%", player.getName()));
        }

        return true;
    }

    public Boolean resetNickname(CommandSender sender, String[] args, CommandRegister commandRegister) {
        this.commandRegister = commandRegister;
        this.plugin = commandRegister.plugin;

        if (!commandRegister.checkConsole(sender) || !commandRegister.checkPermission(sender, "name.nickname")) return true;

        Player player = (Player) sender;

        player.setDisplayName(player.getName());
        plugin.playerData.set(player.getUniqueId() + ".nickname", player.getName());
        plugin.playerConfig.saveConfig();

        commandRegister.sendMessage(sender, plugin.config.getString("messages.resetname"));

        return true;
    }

    public boolean setPrefix(CommandSender sender, String[] args, CommandRegister commandRegister) {
        this.commandRegister = commandRegister;
        this.plugin = commandRegister.plugin;

        String[] neededArgs = new String[1];
        neededArgs[0] = "prefix";
        if (!commandRegister.checkConsole(sender) || !commandRegister.checkPermission(sender, "prefix") || !commandRegister.checkForParameters(sender, neededArgs, args)) return true;

        Player player = (Player) sender;

        plugin.playerData.set(player.getUniqueId() + ".prefix", args[0]);
        plugin.playerConfig.saveConfig();

        commandRegister.sendMessage(sender, plugin.config.getString("name.prefix").replaceAll("%prefix%", args[0]));

        return true;
    }
}
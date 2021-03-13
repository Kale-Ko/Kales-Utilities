package com.kale_ko.kalesutilities.commands;

import com.kale_ko.api.spigot.TextStyler;
import com.kale_ko.kalesutilities.CommandRegister;
import com.kale_ko.kalesutilities.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class Kick {
    Main plugin;
    CommandRegister commandRegister;

    public boolean execute(CommandSender sender, String[] args, CommandRegister commandRegister) {
        this.commandRegister = commandRegister;
        this.plugin = commandRegister.plugin;

        String[] neededArgs = new String[2];
        neededArgs[0] = "player";
        neededArgs[1] = "reason";
        if (!commandRegister.checkPermission(sender, "admin.moderation.kick") || !commandRegister.checkForParameters(sender, neededArgs, args)) return true;

        StringBuilder reason = new StringBuilder();

        int index = 0;
        for (String string : args) {
            if (index != 0) reason.append(string).append(" ");

            index++;
        }

        Bukkit.getPlayer(args[0]).kickPlayer(TextStyler.noPrefix(plugin.config.getString("messages.kick-message").replaceAll("%reason%", reason.toString()), plugin.config));

        return true;
    }
}
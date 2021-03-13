package com.kale_ko.kalesutilities.commands;

import com.kale_ko.api.spigot.TextStyler;
import com.kale_ko.kalesutilities.CommandRegister;
import com.kale_ko.kalesutilities.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Message {
    Main plugin;
    CommandRegister commandRegister;

    public boolean execute(CommandSender sender, String[] args, CommandRegister commandRegister) {
        this.commandRegister = commandRegister;
        this.plugin = commandRegister.plugin;

        String[] neededArgs = new String[2];
        neededArgs[0] = "player";
        neededArgs[1] = "message";
        if (!commandRegister.checkConsole(sender) || !commandRegister.checkForParameters(sender, neededArgs, args)) return true;

        Player player = (Player) sender;

        StringBuilder message = new StringBuilder();

        int index = 0;
        for (String string : args) {
            if (index != 0) message.append(string).append(" ");

            index++;
        }

        Bukkit.getPlayer(args[0]).sendMessage(TextStyler.noPrefix(plugin.config.getString("messages.dirrectmessage").replaceAll("%player%", player.getDisplayName()).replaceAll("%message%", message.toString()), plugin.config));

        return true;
    }
}
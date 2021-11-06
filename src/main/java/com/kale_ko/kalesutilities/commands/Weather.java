/**
    @license
    MIT License
    Copyright (c) 2021 Kale Ko
    See https://kaleko.ga/license.txt
*/

package com.kale_ko.kalesutilities.commands;

import com.kale_ko.kalesutilities.CommandRegister;
import com.kale_ko.kalesutilities.Main;
import org.bukkit.WeatherType;
import org.bukkit.command.CommandSender;

public class Weather {
    Main plugin;
    CommandRegister commandRegister;

    public boolean execute(CommandSender sender, String[] args, CommandRegister commandRegister) {
        this.commandRegister = commandRegister;
        this.plugin = commandRegister.plugin;

        String[] neededArgs = new String[1];
        neededArgs[0] = "weather";
        if (!commandRegister.checkPermission(sender, "admin.world.weather") || !commandRegister.checkForParameters(sender, neededArgs, args)) return true;

        if (args[0].equalsIgnoreCase("clear")) {
            commandRegister.weather = WeatherType.CLEAR;

            commandRegister.sendMessage(sender, plugin.config.getString("messages.set-weather").replaceAll("%weather%", "clear"));
        } else if (args[0].equalsIgnoreCase("rain")) {
            commandRegister.weather = WeatherType.DOWNFALL;

            commandRegister.sendMessage(sender, plugin.config.getString("messages.set-weather").replaceAll("%weather%", "rain"));
        } else {
            commandRegister.sendMessage(sender, plugin.config.getString("messages.not-a-weather"));
        }

        return true;
    }
}
/**
    @license
    MIT License
    Copyright (c) 2021 Kale Ko
    See https://kaleko.ga/license.txt
*/

package com.kale_ko.api.spigot;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class TextStyler {
    public static String style(String text, FileConfiguration config) {
        return ChatColor.translateAlternateColorCodes('&', config.getString("prefix") + " &r" + config.getString("default-message-color") + replacePlaceholders(text));
    }

    public static String noPrefix(String text, FileConfiguration config) {
        return ChatColor.translateAlternateColorCodes('&', config.getString("default-message-color") + replacePlaceholders(text));
    }

    public static String noExtra(String text) {
        return ChatColor.translateAlternateColorCodes('&', replacePlaceholders(text));
    }

    public static String replacePlaceholders(String text, Player player, FileConfiguration playerData) {
        return text
            .replaceAll("%player%", playerData.getString(player.getUniqueId() + ".prefix") + " " + player.getDisplayName())
            .replaceAll("%online%", Integer.toString(Bukkit.getOnlinePlayers().size()))
            .replaceAll("%max%", Integer.toString(Bukkit.getMaxPlayers()))
            .replaceAll("%joins%", Integer.toString(playerData.getInt(player.getUniqueId() + ".joins")));
    }

    public static String replacePlaceholders(String text) {
        return text.replaceAll("%online%", Integer.toString(Bukkit.getOnlinePlayers().size())).replaceAll("%max%", Integer.toString(Bukkit.getMaxPlayers()));
    }

    public static String removeColorCodes(String text) {
        return text
            .replaceAll("&0", "").replaceAll("&1", "").replaceAll("&2", "").replaceAll("&3", "")
            .replaceAll("&4", "").replaceAll("&5", "").replaceAll("&6", "").replaceAll("&7", "")
            .replaceAll("&8", "").replaceAll("&9", "").replaceAll("&a", "").replaceAll("&b", "")
            .replaceAll("&c", "").replaceAll("&d", "").replaceAll("&e", "").replaceAll("&f", "")
            .replaceAll("&g", "").replaceAll("&k", "").replaceAll("&l", "").replaceAll("&m", "")
            .replaceAll("&n", "").replaceAll("&o", "").replaceAll("&r", "");
    }
}
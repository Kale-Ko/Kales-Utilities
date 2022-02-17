package com.kale_ko.kalesutilities.commands;

import com.kale_ko.api.spigot.TextStyler;
import com.kale_ko.kalesutilities.CommandRegister;
import com.kale_ko.kalesutilities.Main;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Ban {
    Main plugin;
    CommandRegister commandRegister;

    public boolean ban(CommandSender sender, String[] args, CommandRegister commandRegister) {
        this.commandRegister = commandRegister;
        this.plugin = commandRegister.plugin;

        String[] neededArgs = new String[2];
        neededArgs[0] = "player";
        neededArgs[1] = "reason";
        if (!commandRegister.checkPermission(sender, "admin.moderation.ban") || !commandRegister.checkForParameters(sender, neededArgs, args)) return true;

        Player player = Bukkit.getPlayer(args[0]);

        StringBuilder reason = new StringBuilder();

        int index = 0;
        for (String string : args) {
            if (index != 0) reason.append(string).append(" ");

            index++;
        }

        commandRegister.playerData.set(player.getUniqueId() + ".ban.banned", true);
        commandRegister.playerData.set(player.getUniqueId() + ".ban.reason", reason.toString());
        commandRegister.playerConfig.saveConfig();

        Bukkit.getPlayer(args[0]).kickPlayer(TextStyler.noPrefix(plugin.config.getString("messages.ban-message").replaceAll("%reason%", reason.toString()), plugin.config));

        return true;
    }

    public boolean unBan(CommandSender sender, String[] args, CommandRegister commandRegister) {
        this.commandRegister = commandRegister;
        this.plugin = commandRegister.plugin;

        String[] neededArgs = new String[2];
        neededArgs[0] = "player";
        neededArgs[1] = "reason";
        if (!commandRegister.checkPermission(sender, "admin.moderation.ban") || !commandRegister.checkForParameters(sender, neededArgs, args)) return true;

        OfflinePlayer player = Bukkit.getOfflinePlayer(args[0]);

        plugin.playerData.set(player.getUniqueId() + ".ban.banned", false);
        plugin.playerData.set(player.getUniqueId() + ".ban.reason", "");
        plugin.playerConfig.saveConfig();

        return true;
    }
}
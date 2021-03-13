package com.kale_ko.kalesutilities.commands;

import com.kale_ko.kalesutilities.CommandRegister;
import com.kale_ko.kalesutilities.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KalesUtilities {
    Main plugin;
    CommandRegister commandRegister;

    public boolean execute(CommandSender sender, String[] args, CommandRegister commandRegister) {
        this.commandRegister = commandRegister;
        this.plugin = commandRegister.plugin;

        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("help")) {
                if (!checkPermission(sender, "help")) return true;

                sendMessage(sender, "Type /kalesutilities help for this");
                sendMessage(sender, "Type /kalesutilities reload to reload the config");
                sendMessage(sender, "Type /nickname {nickname} to change your name");
                sendMessage(sender, "Type /realname {nickname} to get the realname of a nicked person");
                sendMessage(sender, "Type /resetnickname to reset your nickname");
                sendMessage(sender, "Type /weather (clear, rain) to lock the weather");
                sendMessage(sender, "Type /time (day, night, {time}) to lock the time");
                sendMessage(sender, "Type /sudo {player} {command} to run a command or chat as someone else");
                sendMessage(sender, "Type /lagclear to kill all the items, tnt, ect");
                sendMessage(sender, "Type /killall to kill all the mobs");
            } else if (args[0].equalsIgnoreCase("reload")) {
                if (!checkPermission(sender, "reload")) return true;

                sendMessage(sender, plugin.config.getString("messages.reload"));

                plugin.reloadConfig();
                plugin.playerConfig.reloadConfig();
                plugin.serverConfig.reloadConfig();
                plugin.config = plugin.getConfig();
                plugin.playerData = plugin.playerConfig.getConfig();
                plugin.serverData = plugin.serverConfig.getConfig();

                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.setDisplayName(plugin.playerData.getString(player.getUniqueId() + ".nickname"));
                }
            } else {
                sendMessage(sender, plugin.config.getString("messages.not-a-command"));
            }
        } else {
            if (!checkPermission(sender, "help")) return true;

            sendMessage(sender, "Type /kalesutilities help for this");
            sendMessage(sender, "Type /kalesutilities reload to reload the config");
            sendMessage(sender, "Type /nickname {nickname} to change your name");
            sendMessage(sender, "Type /realname {nickname} to get the realname of a nicked person");
            sendMessage(sender, "Type /resetnickname to reset your nickname");
            sendMessage(sender, "Type /weather (clear, rain) to lock the weather");
            sendMessage(sender, "Type /time (day, night, {time}) to lock the time");
            sendMessage(sender, "Type /sudo {player} {command} to run a command or chat as someone else");
            sendMessage(sender, "Type /lagclear to kill all the items, tnt, ect");
            sendMessage(sender, "Type /killall to kill all the mobs");
        }

        return true;
    }

    public Boolean checkPermission(CommandSender sender, String permission) {
        return commandRegister.checkPermission(sender, permission);
    }

    public void sendMessage(CommandSender sender, String message) {
        commandRegister.sendMessage(sender, message);
    }
}
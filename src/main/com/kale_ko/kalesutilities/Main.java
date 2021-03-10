package com.kale_ko.kalesutilities;

import com.kale_ko.api.spigot.DataManager;
import com.kale_ko.api.spigot.TextStyler;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    public FileConfiguration config;
    public DataManager playerConfig = new DataManager("players.yml", this);
    public FileConfiguration playerData;
    public DataManager serverConfig = new DataManager("data.yml", this);
    public FileConfiguration serverData;

    public CommandRegister commandRegister = new CommandRegister(this);

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        this.getConfig().options().copyDefaults(true);
        this.saveConfig();
        config = this.getConfig();
        playerData = playerConfig.getConfig();
        serverData = serverConfig.getConfig();

        commandRegister.registerCommands();
        commandRegister.registerTickEvent();

        this.getServer().getPluginManager().registerEvents(new EventManager(this), this);

        log("Enabled");
    }

    @Override
    public void onDisable() {
        log("Saving..");

        saveConfig();
        playerConfig.saveConfig();
        serverConfig.saveConfig();

        log("Disabled");
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        return commandRegister.execute(sender, command, label, args);
    }

    public void sendMessage(CommandSender sender, String message) {
        sender.sendMessage(TextStyler.style(message, config));
    }

    public void log(String message) {
        getLogger().info(message);
    }

    public void warn(String message) {
        getLogger().warning(message);
    }

    public void error(String message) {
        getLogger().severe(message);
    }
}
package com.kale_ko.kalesutilities;

import com.kale_ko.api.spigot.DataManager;
import com.kale_ko.api.spigot.TextStyler;
import org.bukkit.Bukkit;
import org.bukkit.WeatherType;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    public FileConfiguration config = this.getConfig();
    public DataManager playerConfig = new DataManager("players.yml", this);
    public FileConfiguration playerData = playerConfig.getConfig();

    public long time = 6000;
    public WeatherType weather = WeatherType.CLEAR;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        this.getConfig().options().copyDefaults(true);
        saveConfig();
        config = this.getConfig();

        new Command().registerCommands(this);

        this.getServer().getPluginManager().registerEvents(new EventHandler(this), this);

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            for (World world : Bukkit.getServer().getWorlds()) {
                world.setTime(time);
                world.setFullTime(time);

                for (Player player : world.getPlayers()) {
                    player.setPlayerWeather(weather);
                }

                world.setGameRuleValue("doDaylightCycle", "false");
                world.setGameRuleValue("doWeatherCycle", "false");
            }
        }, 0, 20);

        log("Enabled");
    }

    @Override
    public void onDisable() {
        log("Saving..");

        saveConfig();
        playerConfig.saveConfig();

        log("Disabled");
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        return new Command().execute(sender, command, label, args, this);
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
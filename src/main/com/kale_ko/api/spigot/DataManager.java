package com.kale_ko.api.spigot;

import com.kale_ko.kalesutilities.Main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DataManager {
    private final String filename;
    private final Main plugin;
    private FileConfiguration dataConfig;
    private File configFile;

    public DataManager(String filename, Main plugin) {
        this.filename = filename;
        this.plugin = plugin;

        this.saveDefaultConfig();
    }

    public void reloadConfig() {
        if (this.configFile == null) this.configFile = new File(this.plugin.getDataFolder(), filename);

        this.dataConfig = YamlConfiguration.loadConfiguration(this.configFile);

        InputStream defaultStream = this.plugin.getResource(filename);
        if (defaultStream != null) {
            YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream));
            this.dataConfig.setDefaults(defaultConfig);
        }
    }

    public FileConfiguration getConfig() {
        if (this.dataConfig == null) {
            this.reloadConfig();
        }

        return this.dataConfig;
    }

    public void saveConfig() {
        if (this.dataConfig == null || this.configFile == null)
            return;

        try {
            this.getConfig().save(this.configFile);
        } catch (IOException e) {
            new Main().error("Could not save config" + e);
        }
    }

    public void saveDefaultConfig() {
        if (this.configFile == null) this.configFile = new File(this.plugin.getDataFolder(), filename);

        if (!this.configFile.exists()) this.plugin.saveResource(filename, false);
    }
}
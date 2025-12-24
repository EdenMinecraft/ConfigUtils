package com.github.longboyy.configutils.parse;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;

public abstract class FileConfigParser extends ConfigParser {
    public FileConfigParser(Plugin plugin, File configFile) {
        super(plugin);
        if (!configFile.exists()) {
            configFile.getParentFile().mkdirs();
            plugin.saveResource(configFile.getName(), false);
        }
        this.configFile = YamlConfiguration.loadConfiguration(configFile);
    }
}

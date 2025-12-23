package com.github.longboyy.configutils.parse;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import java.util.logging.Logger;

public abstract class ConfigParser {
    protected final Plugin plugin;
    protected final FileConfiguration configFile;
    protected final Logger logger;

    public ConfigParser(final Plugin plugin, FileConfiguration configFile) {
        this.plugin = plugin;
        this.configFile = configFile;
        this.logger = plugin.getLogger();
    }

    /**
     * Parses this civ plugin's config. It will also save a default config (if one is not already present) and reload
     * the config, so there's no need to do so yourself beforehand.
     *
     * @return Returns true if the config was successfully parsed.
     */
    public boolean parse(){
        // Allow child class parsing
        final boolean worked = parseInternal(this.configFile);
        if(worked){
            this.logger.info("Config parsed: " + this.configFile.getName());
        } else {
            this.logger.warning("Failed to parse config: " + (this.configFile == null ? "Unknown" : this.configFile.getName()));
        }
        return worked;
    }

    /**
     * An internal parser method intended to be overridden by child classes.
     *
     * @param config The root config section.
     * @return Return true if the
     */
    protected abstract boolean parseInternal(final ConfigurationSection config);
}

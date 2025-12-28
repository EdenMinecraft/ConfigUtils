package com.github.longboyy.configutils.parse;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;

public abstract class PluginConfigParser extends ConfigParser {

    private boolean debug;
    private boolean logReplies;

    public PluginConfigParser(Plugin plugin) {
        super(plugin);
        File pluginConfigFile = new File(plugin.getDataFolder(), "config.yml");
        if (!pluginConfigFile.exists()) {
            pluginConfigFile.getParentFile().mkdirs();
            plugin.saveResource(pluginConfigFile.getName(), false);
        }
        this.configFile = YamlConfiguration.loadConfiguration(pluginConfigFile);
    }

    public final boolean isDebugEnabled() { return this.debug; }
    public final boolean logReplies() { return this.logReplies; }

    @Override
    public boolean parse() {
        // Parse debug value
        this.debug = this.configFile.getBoolean("debug", false);
        this.logger.info("Debug mode: " + (this.debug ? "enabled" : "disabled"));

        // Parse reply logging value
        this.logReplies = this.configFile.getBoolean("logReplies", false);
        this.logger.info("Logging replies: " + (this.logReplies ? "enabled" : "disabled"));

        return super.parse();
    }

    /**
     * This should reset all config values back to their defaults. Child classes should override this if they parse
     * additional values that should be reset.
     */
    public void reset() {
        this.debug = false;
        this.logReplies = false;
    }
}

package com.github.longboyy.configutils.parse;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

public abstract class PluginConfigParser extends ConfigParser {

    private boolean debug;
    private boolean logReplies;

    public PluginConfigParser(Plugin plugin) {
        super(plugin);
    }

    public final boolean isDebugEnabled() { return this.debug; }
    public final boolean logReplies() { return this.logReplies; }

    @Override
    public boolean parse() {
        this.plugin.saveDefaultConfig();

        if(this.configFile == null){
            this.configFile = plugin.getConfig();
        }

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

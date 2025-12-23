package com.github.longboyy.configutils.parse;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

public abstract class FileConfigParser extends ConfigParser {
    public FileConfigParser(Plugin plugin, FileConfiguration configFile) {
        super(plugin, configFile);
    }

    @Override
    public boolean parse() {

        return super.parse();
    }
}

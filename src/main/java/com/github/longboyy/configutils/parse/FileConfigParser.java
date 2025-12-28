package com.github.longboyy.configutils.parse;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;

public abstract class FileConfigParser extends ConfigParser {

    private final File fileHandle;

    public FileConfigParser(Plugin plugin, File configFile) {
        super(plugin);
        this.fileHandle = configFile;
        this.reset();
    }

    @Override
    public void reset() {
        if (!fileHandle.exists()) {
            fileHandle.getParentFile().mkdirs();
            plugin.saveResource(fileHandle.getName(), false);
        }
        this.configFile = YamlConfiguration.loadConfiguration(fileHandle);
    }
}

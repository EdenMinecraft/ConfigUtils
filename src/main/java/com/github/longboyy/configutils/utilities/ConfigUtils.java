package com.github.longboyy.configutils.utilities;

import com.github.longboyy.configutils.models.range.DoubleRange;
import com.github.longboyy.configutils.models.range.IntRange;
import org.bukkit.configuration.ConfigurationSection;

public class ConfigUtils {

    public static IntRange getIntRange(ConfigurationSection section, String key){
        String raw = section.getString(key);
        if(raw == null){
            return null;
        }

        String[] parts = raw.split("-");
        if(parts.length != 2){
            return null;
        }

        int minVal;
        int maxVal;

        try {
            int a = Integer.parseInt(parts[0]);
            int b = Integer.parseInt(parts[1]);

            minVal = Math.min(a, b);
            maxVal = Math.max(a, b);
        }catch(Exception e) {
            return null;
        }

        return new IntRange(minVal, maxVal);
    }

    public static DoubleRange getDoubleRange(ConfigurationSection section, String key){
        String raw = section.getString(key);
        if(raw == null){
            return null;
        }

        String[] parts = raw.split("-");
        if(parts.length != 2){
            return null;
        }

        double minVal;
        double maxVal;

        try {
            double a = Double.parseDouble(parts[0]);
            double b = Double.parseDouble(parts[1]);

            minVal = Math.min(a, b);
            maxVal = Math.max(a, b);
        }catch(Exception e) {
            return null;
        }

        return new DoubleRange(minVal, maxVal);
    }

}

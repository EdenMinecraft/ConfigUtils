package com.github.longboyy.configutils.utilities;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import com.github.longboyy.configutils.models.ConfigItemStack;
import com.github.longboyy.configutils.models.range.DoubleRange;
import com.github.longboyy.configutils.models.range.IntRange;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class ConfigUtils {

    public static boolean isRange(ConfigurationSection parent, String key){
        String raw = parent.getString(key);
        return raw != null && raw.split("-").length == 2;
    }

    public static IntRange getIntRange(ConfigurationSection parent, String key){
        String raw = parent.getString(key);
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
            int a = Integer.parseInt(parts[0].trim());
            int b = Integer.parseInt(parts[1].trim());

            minVal = Math.min(a, b);
            maxVal = Math.max(a, b);
        }catch(Exception e) {
            return null;
        }

        return new IntRange(minVal, maxVal);
    }

    public static DoubleRange getDoubleRange(ConfigurationSection parent, String key){
        String raw = parent.getString(key);
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
            double a = Double.parseDouble(parts[0].trim());
            double b = Double.parseDouble(parts[1].trim());

            minVal = Math.min(a, b);
            maxVal = Math.max(a, b);
        }catch(Exception e) {
            return null;
        }

        return new DoubleRange(minVal, maxVal);
    }

    public static Material getMaterial(ConfigurationSection parent, String key){
        String materialName = parent.getString(key);
        if(materialName == null || materialName.isEmpty()){
            return null;
        }

        try {
            return Material.getMaterial(materialName);
        }catch(Exception e) {
            return null;
        }
    }

    public static ConfigItemStack getItemStack(ConfigurationSection parent, String key){
        ConfigurationSection itemSection = parent.getConfigurationSection(key);
        if(itemSection == null){
            return null;
        }

        Material material = getMaterial(itemSection, "material");
        if(material == null){
            return null;
        }

        //int amount = itemSection.getInt("amount", 1);
        int amount;
        IntRange amountRange = null;
        if(itemSection.isInt("amount")){
            amount = itemSection.getInt("amount");
        }else {
            amount = 1;
            amountRange = getIntRange(itemSection, "amount");
        }

        ItemStack item = new ItemStack(material, amount);
        ItemMeta itemMeta = item.getItemMeta();

        String displayName = itemSection.getString("displayName");
        if(displayName != null && !displayName.isEmpty()){
            itemMeta.displayName(MiniMessage.miniMessage().deserialize(displayName));
        }

        List<String> loreLines = itemSection.getStringList("lore");
        if(!loreLines.isEmpty()){
            var loreComponents = loreLines.stream().map(MiniMessage.miniMessage()::deserialize).toList();
            itemMeta.lore(loreComponents);
        }

        List<String> enchantList = parent.getStringList("enchants");
        if(!enchantList.isEmpty()){
            var enchantRegistry = RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT);
            for(String enchant : enchantList){
                String[] parts = enchant.split(":");

                try{
                    Enchantment enchantment = enchantRegistry.get(NamespacedKey.minecraft(parts[0]));
                    if(enchantment != null){
                        int level = parts.length > 1 ? Integer.parseInt(parts[1]) : 1;
                        item.addEnchantment(enchantment, level);
                    }
                }catch(Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }

        // if this is a player head, check for skullTexture
        if(material == Material.PLAYER_HEAD && itemMeta instanceof SkullMeta skullMeta){
            String skullTexture = itemSection.getString("skullTexture");
            if(skullTexture != null && !skullTexture.isEmpty()){
                // custom texture
                PlayerProfile profile = Bukkit.createProfile(UUID.randomUUID());
                profile.getProperties().add(new ProfileProperty("textures", skullTexture));
                skullMeta.setPlayerProfile(profile);
            }
        }

        item.setItemMeta(itemMeta);

        if(amountRange != null){
            return new ConfigItemStack(item, amountRange);
        }

        return new ConfigItemStack(item, amount);
    }

    public static List<ConfigurationSection> getSectionList(ConfigurationSection parent, String identifier) {
        List<ConfigurationSection> sections = new ArrayList<>();

        List<Map<?, ?>> mapList = parent.getMapList(identifier);
        for (Map<?, ?> map : mapList) {
            MemoryConfiguration section = new MemoryConfiguration();
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                section.set(entry.getKey().toString(), entry.getValue());
            }
            sections.add(section);
        }

        return sections;
    }

}

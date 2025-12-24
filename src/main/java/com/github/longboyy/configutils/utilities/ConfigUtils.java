package com.github.longboyy.configutils.utilities;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import com.github.longboyy.configutils.models.range.DoubleRange;
import com.github.longboyy.configutils.models.range.IntRange;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.List;
import java.util.UUID;

public final class ConfigUtils {

    public static boolean isRange(ConfigurationSection section, String key){
        String raw = section.getString(key);
        return raw != null && raw.split("-").length == 2;
    }

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

    public static Material getMaterial(ConfigurationSection section, String key){
        String materialName = section.getString(key);
        if(materialName == null || materialName.isEmpty()){
            return null;
        }

        try {
            return Material.getMaterial(materialName);
        }catch(Exception e) {
            return null;
        }
    }

    public static ItemStack getItemStack(ConfigurationSection section, String key){
        ConfigurationSection itemSection = section.getConfigurationSection(key);
        if(itemSection == null){
            return null;
        }

        Material material = getMaterial(itemSection, "material");
        if(material == null){
            return null;
        }

        int amount = itemSection.getInt("amount", 1);

        ItemStack item = new ItemStack(material, amount);
        ItemMeta itemMeta = item.getItemMeta();

        String displayName = itemSection.getString("displayName");
        if(displayName != null && !displayName.isEmpty()){
            itemMeta.displayName(Component.text(displayName));
        }

        List<String> loreLines = itemSection.getStringList("lore");
        if(!loreLines.isEmpty()){
            var loreComponents = loreLines.stream().map(Component::text).toList();
            itemMeta.lore(loreComponents);
        }

        List<String> enchantList = section.getStringList("enchants");
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

        return item;
    }

}

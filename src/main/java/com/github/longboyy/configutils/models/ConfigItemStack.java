package com.github.longboyy.configutils.models;

import com.github.longboyy.configutils.models.range.IntRange;
import org.bukkit.inventory.ItemStack;

public class ConfigItemStack {

    private final ItemStack item;
    private final int quantity;
    private final IntRange quantityRange;

    public ConfigItemStack(ItemStack item, IntRange range){
        this.item = item;
        this.quantityRange = range;
        this.quantity = -1;
    }

    public ConfigItemStack(ItemStack item, int quantity){
        this.item = item;
        this.quantity = quantity;
        this.quantityRange = null;
    }

    public ItemStack generateItemStack(){
        ItemStack newItem = this.item.clone();
        if(this.quantityRange == null){
            newItem.setAmount(this.quantity);
            return newItem;
        }

        newItem.setAmount(this.quantityRange.getRandom());
        return newItem;
    }

}

package me.stutiguias.cdsc.model;

import java.util.List;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

public class MiniGameClass {

    private final String id;
    private final String displayName;
    private final List<ItemStack> items;
    private final List<PotionEffect> effects;

    public MiniGameClass(String id, String displayName, List<ItemStack> items, List<PotionEffect> effects) {
        this.id = id;
        this.displayName = displayName;
        this.items = items;
        this.effects = effects;
    }

    public String getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public List<ItemStack> getItems() {
        return items;
    }

    public List<PotionEffect> getEffects() {
        return effects;
    }
}

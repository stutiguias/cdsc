package me.stutiguias.cdsc.model;

import java.util.Collection;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

public class MiniGamePlayer {

    private final String playerId;
    private final String clanTag;
    private final String classId;
    private final ItemStack[] previousInventory;
    private final ItemStack[] previousArmor;
    private final Location previousLocation;
    private final Collection<PotionEffect> previousEffects;

    public MiniGamePlayer(String playerId, String clanTag, String classId, ItemStack[] previousInventory, ItemStack[] previousArmor, Location previousLocation, Collection<PotionEffect> previousEffects) {
        this.playerId = playerId;
        this.clanTag = clanTag;
        this.classId = classId;
        this.previousInventory = previousInventory;
        this.previousArmor = previousArmor;
        this.previousLocation = previousLocation;
        this.previousEffects = previousEffects;
    }

    public String getPlayerId() {
        return playerId;
    }

    public String getClanTag() {
        return clanTag;
    }

    public String getClassId() {
        return classId;
    }

    public ItemStack[] getPreviousInventory() {
        return previousInventory;
    }

    public ItemStack[] getPreviousArmor() {
        return previousArmor;
    }

    public Location getPreviousLocation() {
        return previousLocation;
    }

    public Collection<PotionEffect> getPreviousEffects() {
        return previousEffects;
    }
}

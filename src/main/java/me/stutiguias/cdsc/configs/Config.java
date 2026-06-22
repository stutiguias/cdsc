/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package me.stutiguias.cdsc.configs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import me.stutiguias.cdsc.init.Cdsc;
import me.stutiguias.cdsc.model.MiniGameClass;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 *
 * @author Daniel
 */
public class Config {
    
    private ConfigAccessor config;
        
    public boolean ClanOwnerCanBreakArea;
    public boolean ClanOwnerCanPlaceArea;
    public boolean Dontdropduringevent;
    public boolean Dontdieduringevent;
    
    public int CoreLife;
    public String CoreBlock;
    
    public String DataBaseType;
    public String Host;
    public String Username;
    public String Password;
    public String Port;
    public String Database;
    public boolean AutoEventEnabled;
    public int AutoEventIntervalMinutes;
    public int AutoEventDurationMinutes;
    public boolean MiniGameEnabled;
    public HashMap<String, MiniGameClass> MiniGameClasses = new HashMap<>();
    public String MiniGameDefaultClass;
    
    public Config(Cdsc plugin) {
 
        try {
            config = new ConfigAccessor(plugin,"config.yml");
            config.setupConfig();
            FileConfiguration fc = config.getConfig();   
                        
            if(config.updateKeepingCompatibleValues("configversion")){
                fc = config.getConfig();  
            }
            
            DataBaseType = fc.getString("DataBase.Type");
            Host  = fc.getString("MySQL.Host");
            Username = fc.getString("MySQL.Username");
            Password = fc.getString("MySQL.Password");
            Port = fc.getString("MySQL.Port");
            Database = fc.getString("MySQL.Database");
           
            ClanOwnerCanBreakArea = fc.getBoolean("DefaultFlags.ClanOwnerCanBreakArea");
            ClanOwnerCanPlaceArea = fc.getBoolean("DefaultFlags.ClanOwnerCanPlaceArea");
            Dontdropduringevent = fc.getBoolean("Dontdropduringevent");
            Dontdieduringevent = fc.getBoolean("Dontdieduringevent");
            
            CoreLife = fc.getInt("CoreLife");
            CoreBlock = fc.getString("CoreBlock");
            AutoEventEnabled = fc.getBoolean("AutoEvent.Enabled");
            AutoEventIntervalMinutes = fc.getInt("AutoEvent.IntervalMinutes");
            AutoEventDurationMinutes = fc.getInt("AutoEvent.DurationMinutes");
            MiniGameEnabled = fc.getBoolean("MiniGame.Enabled", false);
            MiniGameDefaultClass = fc.getString("MiniGame.DefaultClass", "warrior");
            loadMiniGameClasses(fc);
            
        }catch(IOException ex){
            ex.printStackTrace();
            plugin.getLogger().log(Level.WARNING, "Erro Loading Config");
        }
    }
    
    public void reloadConfig() {
        config.reloadConfig();
    }
    
    public boolean DropDuringEvent() {
        return Dontdropduringevent == false;
    }
    
    public boolean DieDuringEvent() {
        return Dontdieduringevent == false;
    }

    public void setAutoEvent(boolean enabled, int intervalMinutes, int durationMinutes) {
        FileConfiguration fc = config.getConfig();
        fc.set("AutoEvent.Enabled", enabled);
        fc.set("AutoEvent.IntervalMinutes", intervalMinutes);
        fc.set("AutoEvent.DurationMinutes", durationMinutes);
        config.saveConfig();

        AutoEventEnabled = enabled;
        AutoEventIntervalMinutes = intervalMinutes;
        AutoEventDurationMinutes = durationMinutes;
    }

    private void loadMiniGameClasses(FileConfiguration fc) {
        MiniGameClasses.clear();
        if(!fc.isConfigurationSection("MiniGame.Classes")) {
            loadFallbackMiniGameClasses();
            return;
        }

        for(String key:fc.getConfigurationSection("MiniGame.Classes").getKeys(false)) {
            String path = "MiniGame.Classes." + key;
            String display = fc.getString(path + ".Display", key);
            List<ItemStack> items = parseItems(fc.getStringList(path + ".Items"));
            List<PotionEffect> effects = parseEffects(fc.getStringList(path + ".Effects"));
            MiniGameClasses.put(key.toLowerCase(), new MiniGameClass(key.toLowerCase(), display, items, effects));
        }

        if(MiniGameClasses.isEmpty()) {
            loadFallbackMiniGameClasses();
        }
    }

    private void loadFallbackMiniGameClasses() {
        List<ItemStack> warriorItems = new ArrayList<>();
        warriorItems.add(new ItemStack(Material.IRON_SWORD, 1));
        warriorItems.add(new ItemStack(Material.SHIELD, 1));
        MiniGameClasses.put("warrior", new MiniGameClass("warrior", "&cWarrior", warriorItems, new ArrayList<PotionEffect>()));

        List<ItemStack> archerItems = new ArrayList<>();
        archerItems.add(new ItemStack(Material.BOW, 1));
        archerItems.add(new ItemStack(Material.ARROW, 32));
        MiniGameClasses.put("archer", new MiniGameClass("archer", "&aArcher", archerItems, new ArrayList<PotionEffect>()));
    }

    private List<ItemStack> parseItems(List<String> configuredItems) {
        List<ItemStack> items = new ArrayList<>();
        for(String configuredItem:configuredItems) {
            String[] parts = configuredItem.split(":");
            Material material = Material.getMaterial(parts[0].toUpperCase());
            if(material == null) continue;
            int amount = 1;
            if(parts.length > 1) {
                try {
                    amount = Integer.parseInt(parts[1]);
                } catch(NumberFormatException ex) {
                    amount = 1;
                }
            }
            items.add(new ItemStack(material, amount));
        }
        return items;
    }

    private List<PotionEffect> parseEffects(List<String> configuredEffects) {
        List<PotionEffect> effects = new ArrayList<>();
        for(String configuredEffect:configuredEffects) {
            String[] parts = configuredEffect.split(":");
            PotionEffectType type = PotionEffectType.getByName(parts[0].toUpperCase());
            if(type == null) continue;
            int amplifier = 0;
            int durationSeconds = 3600;
            if(parts.length > 1) {
                try {
                    amplifier = Math.max(0, Integer.parseInt(parts[1]) - 1);
                } catch(NumberFormatException ex) {
                    amplifier = 0;
                }
            }
            if(parts.length > 2) {
                try {
                    durationSeconds = Integer.parseInt(parts[2]);
                } catch(NumberFormatException ex) {
                    durationSeconds = 3600;
                }
            }
            effects.add(new PotionEffect(type, durationSeconds * 20, amplifier));
        }
        return effects;
    }
}

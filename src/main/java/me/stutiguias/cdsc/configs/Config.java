/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package me.stutiguias.cdsc.configs;

import java.io.IOException;
import java.util.logging.Level;
import me.stutiguias.cdsc.init.Cdsc;
import org.bukkit.configuration.file.FileConfiguration;

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
    
    public Config(Cdsc plugin) {
 
        try {
            config = new ConfigAccessor(plugin,"config.yml");
            config.setupConfig();
            FileConfiguration fc = config.getConfig();   
                        
            if(!fc.isSet("configversion") || fc.getInt("configversion") != 9){
                config.MakeOld();
                config.setupConfig();
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
}

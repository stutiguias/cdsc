/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package me.stutiguias.cdsc.configs;

import java.io.IOException;
import java.util.logging.Level;
import me.stutiguias.cdsc.init.Cdsc;
import me.stutiguias.cdsc.init.ConfigAccessor;
import org.bukkit.configuration.file.FileConfiguration;

/**
 *
 * @author Daniel
 */
public class Config {
    
    private ConfigAccessor config;
    private final Cdsc plugin;
        
    public boolean UpdaterNotify;
    public boolean ClanOwnerCanBreakArea;
    public int CoreLife;
    
    public Config(Cdsc plugin) {
        this.plugin = plugin;
        
        try {
            config = new ConfigAccessor(plugin,"config.yml");
            config.setupConfig();
            FileConfiguration fc = config.getConfig();   
                        
            if(!fc.isSet("configversion") || fc.getInt("configversion") != 1){ 
                config.MakeOld();
                config.setupConfig();
                fc = config.getConfig();  
            }
                             
            UpdaterNotify = fc.getBoolean("UpdaterNotify");
            ClanOwnerCanBreakArea = fc.getBoolean("DefaultFlags.ClanOwnerCanBreakArea");
            CoreLife = fc.getInt("CoreLife");
            
        }catch(IOException ex){
            ex.printStackTrace();
            plugin.getLogger().log(Level.WARNING, "Erro Loading Config");
        }
    }
    
    public void reloadConfig() {
        config.reloadConfig();
    }
    
}

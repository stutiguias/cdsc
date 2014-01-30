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
public class Translate {
    
    private ConfigAccessor config;
    
    public String StartEventForAll;
    public String StartEventForOne;
    public String StopEventForAll;
    public String StopEventForOne;
    public String ProtectWarning;
    public String ProtectOn;
    public String ClanOwn;
    public String CoreHit;
    public String CoreBroke;
    public String ItemsStore;
    public String ItemsAppears;
            
    public Translate(Cdsc plugin) {
 
        try {
            config = new ConfigAccessor(plugin,"msg.yml");
            config.setupConfig();
            FileConfiguration fc = config.getConfig();   
                        
            if(!fc.isSet("configversion") || fc.getInt("configversion") != 1){ 
                config.MakeOld();
                config.setupConfig();
                fc = config.getConfig();  
            }
            
            StartEventForAll = fc.getString("StartEvent.ForAll");
            StartEventForOne = fc.getString("StartEvent.ForOne");
            ProtectWarning = fc.getString("ProtectWarning");
            StopEventForAll = fc.getString("StopEvent.ForAll");
            StopEventForOne = fc.getString("StopEvent.ForOne");
            ProtectOn = fc.getString("ProtectOn");
            ClanOwn = fc.getString("ClanOwn");
            CoreHit = fc.getString("CoreHit");
            CoreBroke = fc.getString("CoreBroke");
            ItemsStore = fc.getString("ItemsStore");
            ItemsAppears = fc.getString("ItemsMagicAppears");
            
        }catch(IOException ex){
            ex.printStackTrace();
            plugin.getLogger().log(Level.WARNING, "Erro Loading Config");
        }
    }
    
    public void reloadConfig() {
        config.reloadConfig();
    }

}

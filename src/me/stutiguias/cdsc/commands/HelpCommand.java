/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package me.stutiguias.cdsc.commands;

import me.stutiguias.cdsc.init.Cdsc;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Daniel
 */
public class HelpCommand extends CommandHandler {

    public HelpCommand(Cdsc plugin) {
        super(plugin);
    }

    @Override
    protected Boolean OnCommand(CommandSender sender, String[] args) {
            
        this.sender = sender; 
        
        SendMessage(MsgHr);
        SendMessage(" &7Castle Defence for Simple Clans ");
        
        SendMessage(MsgHr);
        
        if(plugin.hasPermission((Player)sender,"cdsc.define")){
            SendMessage("&6/cd <d or define> <areaName> <clanTag> &e| &7Save Select area");
        }
        
        if(plugin.hasPermission((Player)sender,"cdsc.wand")){
            SendMessage("&6/cd <w or wand>  &e| &7Get Special Wand to make area");
        }
                
        if(plugin.hasPermission((Player)sender,"cdsc.update")){
            SendMessage("&6/cd <sc or setcore> &e| &7SetCore of an existing area");
        }
         
        if(plugin.hasPermission((Player)sender,"cdsc.setexit")){
            SendMessage("&6/cd <se or setexit> <areaName> &e| &7SetExit spot for not allowed");
        }  
        
        if(plugin.hasPermission((Player)sender,"cdsc.list")){
            SendMessage("&6/cd <l or list> &e| &7List all areas");
        }   
                
        if(plugin.hasPermission((Player)sender,"cdsc.info")){
            SendMessage("&6/cd <i or info> &e| &7info about area you are");
        }   
        
        if(plugin.hasPermission((Player)sender,"cdsc.delete")){
            SendMessage("&6/cd <dl or delete> <areaName> &e| &7Delete an area");
        }
                    
        if(plugin.hasPermission((Player)sender,"cdsc.start")){
            SendMessage("&6/cd <s or start> <nothing | areaName> &e| &7Start Event");
        }    
        
        if(plugin.hasPermission((Player)sender,"cdsc.end")){
            SendMessage("&6/cd <e or end> <nothing | areaName> &e| &7End event");
        }
        
        if(plugin.hasPermission((Player)sender,"cdsc.setspawn")){
            SendMessage("&6/cd spawn <areaName> &e| &7Set spawn for this area");
        }
        if(plugin.hasPermission((Player)sender,"cdsc.setflag")){
            SendMessage("&6/cd <setflag or sf> <flag> <areaName> &e| &7Set flag for this area");
        }
        
        if(plugin.hasPermission((Player)sender,"cdsc.tp")){
            SendMessage("&6/cd <tp or teleport> <areaName> &e| &7Teleport to an area");
        }
        
        if(plugin.hasPermission((Player)sender,"cdsc.reload")){
            SendMessage("&6/cd reload &e| &7Reload the plugin");
        }
        
        SendMessage(MsgHr);
        
        return true;
    }

    @Override
    protected Boolean isInvalid(CommandSender sender, String[] args) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package me.stutiguias.cdsc.commands;

import me.stutiguias.cdsc.init.Cdsc;
import me.stutiguias.cdsc.model.Area;
import org.bukkit.command.CommandSender;

/**
 *
 * @author Daniel
 */
public class StartEventCommand  extends CommandHandler {

    public StartEventCommand(Cdsc plugin) {
        super(plugin);
    }

    @Override
    protected Boolean OnCommand(CommandSender sender, String[] args) {
        this.sender =  sender;
        
        if(!plugin.hasPermission(sender.getName(),"cdsc.start")) return false; 
        
        if (args.length < 2) {
            
            Cdsc.EventOccurring = true;
                   
            for(Area area:Cdsc.Areas) {
                area.setCoreLife(Cdsc.config.CoreLife);
            }
            BrcstMsg("&6Event Defence Castle Started for all Castle!!!");    
            return true;
        }else{
            Area area = plugin.getArea(args[1]);
            if(area == null) {
                SendMessage("&4Area not found");
                return true;
            }
            area.setEvent(true);
            area.setCoreLife(Cdsc.config.CoreLife);
            BrcstMsg("&6Event Defence Castle Started for %s!!!", new Object[]{ args[1] });
        }  
        BrcstMsg("&6Protect is now &4OFF&6!");
        return true;
        
    }
    
}

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
public class StopEventCommand extends CommandHandler {

    public StopEventCommand(Cdsc plugin) {
        super(plugin);
    }

    @Override
    protected Boolean OnCommand(CommandSender sender, String[] args) {
        this.sender =  sender;
        
        if(!plugin.hasPermission(sender.getName(),"cdsc.end")) return false;
        
        if (args.length < 2) {
            
            Cdsc.EventOccurring = false;
            BrcstMsg(Cdsc.msg.StopEventForAll);    
            
        }else{
            
            Area area = plugin.getArea(args[1]);
            if(area == null) {
                SendMessage("&4Area not found");
                return true;
            }
            area.setEvent(false);
            BrcstMsg(Cdsc.msg.StopEventForOne, new Object[]{ args[1] });
        }  
        
        BrcstMsg(Cdsc.msg.ProtectOn);
        return true;
    }
    
}

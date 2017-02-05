/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package me.stutiguias.cdsc.commands;

import me.stutiguias.cdsc.init.BlockHandler;
import me.stutiguias.cdsc.init.Cdsc;
import me.stutiguias.cdsc.model.Area;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
        
        if(isInvalid(sender, args)) return true;
        
        if (args.length < 2) {
            
            StopEventForAll();
            
        }else{
            
            if(!StopEventForOne(args)) return true;
        }  
        
        BrcstMsg(Cdsc.msg.ProtectOn);
        return true;
    }

    @Override
    protected Boolean isInvalid(CommandSender sender, String[] args) {
      if(sender.getName().equalsIgnoreCase("CONSOLE")) return false;
      if(!plugin.hasPermission((Player)sender,"cdsc.end")){
          SendMessage("&4You don't have permission");
          return true;
      }
      return false;
    }
    
    private Boolean StopEventForAll() {
        Cdsc.EventOccurring = false;
        BrcstMsg(Cdsc.msg.StopEventForAll); 
        for(Area area:Cdsc.Areas) {
            area.setEvent(false);
        }
        return true;
    }
    
    private Boolean StopEventForOne(String[] args) {
        Area area = plugin.getArea(args[1]);
        if(area == null) {
            SendMessage("&4Area not found");
            return false;
        }
        area.setEvent(false);
        new BlockHandler(plugin).ReBuild(area);
        BrcstMsg(Cdsc.msg.StopEventForOne, new Object[]{ args[1] });
        return true;
    }
}

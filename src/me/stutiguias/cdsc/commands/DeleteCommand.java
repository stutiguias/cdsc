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
public class DeleteCommand extends CommandHandler {

    public DeleteCommand(Cdsc plugin) {
        super(plugin);
    }

    @Override
    protected Boolean OnCommand(CommandSender sender, String[] args) {
        this.sender =  sender;
                
        if(!plugin.hasPermission(sender.getName(),"cdsc.delete")) return false;
        
        if (args.length < 1) {
            SendMessage("&4Wrong arguments");
            return true;
        }   
        
        String name = args[1];
        
        Area area = plugin.getArea(name);
        
        if(area == null) {
         SendMessage("&4Area name not found.");
         return true;
        }
        
        Cdsc.Areas.remove(area);
        Cdsc.db.Delete(area);
        return true;
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package me.stutiguias.cdsc.commands;

import me.stutiguias.cdsc.init.Cdsc;
import me.stutiguias.cdsc.model.Area;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Daniel
 */
public class SetExitCommand extends CommandHandler {

    public SetExitCommand(Cdsc plugin) {
        super(plugin);
    }

    @Override
    protected Boolean OnCommand(CommandSender sender, String[] args) {
        this.sender =  sender;
        
        if(!plugin.hasPermission(sender.getName(),"cdsc.setexit")) return false;
                
        Player player = (Player)sender;
        
        Location location = player.getTargetBlock(null,2).getLocation();
        
        if (args.length < 1) {
            SendMessage("&4Wrong arguments");
            return true;
        }   
 
        Area area = plugin.getArea(args[1]);
        
        if(area == null) {
         SendMessage("&4Area name not found.");
         return true;
        }
        if(area.getCoreLocation() == null) {
            SendMessage("&4You need to set area core first");
            return true;
        }
        int index = plugin.getAreaIndex(area.getCoreLocation());
        
        Cdsc.Areas.get(index).setExit(location);
        Cdsc.db.SetExit(Cdsc.Areas.get(index));
        SendMessage("&6Exit Point for Area %s setup successful.", new Object[] { area.getName() });
        return true;
        
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package me.stutiguias.cdsc.commands;

import me.stutiguias.cdsc.init.Cdsc;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Daniel
 */
public class SetCoreCommand extends CommandHandler {

    public SetCoreCommand(Cdsc plugin) {
        super(plugin);
    }

    @Override
    protected Boolean OnCommand(CommandSender sender, String[] args) {
        this.sender =  sender;
        
        if(!plugin.hasPermission(sender.getName(),"cdsc.sc")) return false;
            
        Player player = (Player)sender;

        Location location = player.getTargetBlock(null,2).getLocation();
        
        int index = plugin.getAreaIndex(location);
        
        if(index == -1) {
            SendMessage("&4 Not inside any area");
            return true;
        }

        Cdsc.Areas.get(index).setCoreLocation(location);
        Cdsc.db.SetCore(Cdsc.Areas.get(index));
        
        SendMessage("&6 Core set !!");
        
        return true;
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package me.stutiguias.cdsc.commands;

import java.util.Set;
import me.stutiguias.cdsc.init.Cdsc;
import org.bukkit.Location;
import org.bukkit.Material;
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
        
        if(isInvalid(sender, args)) return true;
            
        Player player = (Player)sender;

        Location location = player.getTargetBlock((Set<Material>)null,2).getLocation();
        
        player.getWorld().getBlockAt(location).setTypeId(Cdsc.config.CoreBlockId);
        
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

    @Override
    protected Boolean isInvalid(CommandSender sender, String[] args) {
        if(!plugin.hasPermission((Player)sender,"cdsc.sc")) {
            SendMessage("&4You don't have permission");
            return true;
        }
        return false;
    }
    
}

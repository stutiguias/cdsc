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
public class TeleportCommand extends CommandHandler {

    public TeleportCommand(Cdsc plugin) {
        super(plugin);
    }

    @Override
    protected Boolean OnCommand(CommandSender sender, String[] args) {
        this.sender =  sender;
        
        if(isInvalid(sender, args)) return true;
        
        Player player = (Player)sender;
        
        String name = args[1];
        
        for(Area area:Cdsc.Areas) {
            if(area.getName().equalsIgnoreCase(name)) {
                if(area.getSpawn() == null) {
                    Location firstspot = area.getFirstSpot();
                    firstspot.setY(firstspot.getY() + 1);
                    player.teleport(firstspot);
                }
                player.teleport(area.getSpawn());
            }
        }
        
        return true;
        
    }

    @Override
    protected Boolean isInvalid(CommandSender sender, String[] args) {
        if(!plugin.hasPermission(sender.getName(),"cdsc.tp")) {
            SendMessage("&4You don't have permission");
            return true;
        }
        if (args.length < 2) {
            SendMessage("&4Wrong arguments on command tp");
            return true;
        }
        return false;
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package me.stutiguias.cdsc.commands;

import me.stutiguias.cdsc.init.Cdsc;
import me.stutiguias.cdsc.model.Area;
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
        
        if(!plugin.hasPermission(sender.getName(),"cdsc.tp")) return false;
                
        Player player = (Player)sender;
               
        if (args.length < 2) {
            SendMessage("&4Wrong arguments on command tp");
            return true;
        }
        
        String name = args[1];
        
        for(Area area:Cdsc.Areas) {
            if(area.getName().equalsIgnoreCase(name)) {
                player.teleport(area.getFirstSpot());
            }
        }
        
        return true;
        
    }
    
}

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
public class InfoCommand extends CommandHandler {

    public InfoCommand(Cdsc plugin) {
        super(plugin);
    }

    @Override
    protected Boolean OnCommand(CommandSender sender, String[] args) {
        this.sender =  sender;
        
        if(isInvalid(sender, args)) return true;
                
        Player player = (Player)sender;

        Location location = player.getLocation();
        
        int index = plugin.getAreaIndex(location);
        
        if(index == -1) {
            SendMessage("&4 Not inside any area");
            return true;
        }
        Area area = Cdsc.Areas.get(index);
        
        SendMessage(MsgHr);
        SendMessage("&3Name: &6%s", new Object[]{ area.getName() });
        SendMessage("&3Clan: &6%s", new Object[]{ area.getClanTag() });
        SendMessage("&3Flags: &6%s", new Object[]{ area.getFlags() });
        SendMessage(MsgHr);
        
        return true;
    }

    @Override
    protected Boolean isInvalid(CommandSender sender, String[] args) {
        if(!plugin.hasPermission((Player)sender,"cdsc.info")) {
            SendMessage("&4You don't have permission");
            return true;
        }
        return false;
    }
    
}

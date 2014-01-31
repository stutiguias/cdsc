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
public class ListCommand extends CommandHandler {

    public ListCommand(Cdsc plugin) {
        super(plugin);
    }

    @Override
    protected Boolean OnCommand(CommandSender sender, String[] args) {
        this.sender =  sender;
        
        if(isInvalid(sender, args)) return true;
 
        SendMessage(MsgHr);
        for(Area area:Cdsc.Areas){
            SendMessage("&3Name: &6%s", new Object[]{ area.getName() });
        }
        SendMessage(MsgHr);
        return true;
    }

    @Override
    protected Boolean isInvalid(CommandSender sender, String[] args) {
       if(!plugin.hasPermission(sender.getName(),"cdsc.list")) {
           SendMessage("&4You don't have permission");
           return true;
       }
       if(Cdsc.Areas.isEmpty()) {
            SendMessage("&4 Areas empty");
            return true;
       }
       return false;
    }
    
}

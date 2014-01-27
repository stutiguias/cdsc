/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package me.stutiguias.cdsc.commands;

import me.stutiguias.cdsc.init.Cdsc;
import org.bukkit.command.CommandSender;

/**
 *
 * @author Daniel
 */
public class UpdateCommand extends CommandHandler {

    public UpdateCommand(Cdsc plugin) {
        super(plugin);
    }

    @Override
    protected Boolean OnCommand(CommandSender sender, String[] args) {
        this.sender =  sender;
        
        if(!plugin.hasPermission(sender.getName(),"cdsc.update")) return false;
        plugin.Update();
        return true;
    }
    
}

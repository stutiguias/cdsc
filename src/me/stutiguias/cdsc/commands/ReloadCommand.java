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
public class ReloadCommand extends CommandHandler {

    public ReloadCommand(Cdsc plugin) {
        super(plugin);
    }

    @Override
    protected Boolean OnCommand(CommandSender sender, String[] args) {
        this.sender =  sender;
        
        if(isInvalid(sender, args)) return true;
        
        SendMessage("&6Reloading!");
        plugin.OnReload();
        SendMessage("&6Reload Done!");    
        return true;
    }

    @Override
    protected Boolean isInvalid(CommandSender sender, String[] args) {
        if(!plugin.hasPermission(sender.getName(),"cdsc.reload")) {
            SendMessage("&4You don't have permission");
            return true;
        }
        return false;
    }
    
}

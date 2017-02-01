/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.stutiguias.cdsc.commands;

import me.stutiguias.cdsc.init.Cdsc;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Daniel
 */
public class SetFlagCommand extends CommandHandler {

    public SetFlagCommand(Cdsc plugin) {
        super(plugin);
    }

    @Override
    protected Boolean OnCommand(CommandSender sender, String[] args) {
        this.sender =  sender;
        
        if(isInvalid(sender, args)) return true;
        
        String flag = args[1];
        String areaname = args[2];
        
        return true;
    }

    @Override
    protected Boolean isInvalid(CommandSender sender, String[] args) {
        if(!plugin.hasPermission((Player)sender,"cdsc.setflag")) {
            SendMessage("&4You don't have permission");
            return true;
        }
        if (args.length < 2) {
            SendMessage("&4Wrong arguments");
            return true;
        } 
        return false;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package me.stutiguias.cdsc.commands;

import me.stutiguias.cdsc.init.Cdsc;
import me.stutiguias.cdsc.init.Util;
import org.bukkit.command.CommandSender;

/**
 *
 * @author Daniel
 */
public abstract class CommandHandler extends Util {   
    
    protected final String MsgHr = "&e-----------------------------------------------------";

    public CommandHandler(Cdsc plugin) {
        super(plugin);
    }
    
    protected abstract Boolean OnCommand(CommandSender sender, String[] args);
}

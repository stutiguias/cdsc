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
public class DefineCommand extends CommandHandler {

    public DefineCommand(Cdsc plugin) {
        super(plugin);
    }

    @Override
    protected Boolean OnCommand(CommandSender sender, String[] args) {
        this.sender =  sender;
        
        if(isInvalid(sender, args)) return true;
        
        String name = args[1];
        
        Area area = plugin.getArea(name);
        
        if(area != null) {
         SendMessage("&4This name is already in use.");
         return true;
        }
        
        String clanTag = args[2];
        String flag = "";
        
        if(!Cdsc.config.ClanOwnerCanBreakArea) flag += ",denyclanbreak";
        if(!Cdsc.config.ClanOwnerCanPlaceArea) flag += ",denyclanplace";
        
        Location FirstSpot = Cdsc.AreaCreating.get((Player)sender).getFirstSpot();
        Location SecondSpot = Cdsc.AreaCreating.get((Player)sender).getSecondSpot();

        Cdsc.AreaCreating.remove((Player)sender);
        
        area = new Area(FirstSpot,SecondSpot,name,clanTag,flag);
        
        if(Cdsc.db.InsertArea(area)){
            Cdsc.Areas.add(area);
            SendMessage("&6Area %s successfully define to clan %s",new Object[]{ name,clanTag });
            return true;
        }
        
        SendMessage("&4Erro on Insert to DB!");
        return true;  
    }

    @Override
    protected Boolean isInvalid(CommandSender sender, String[] args) {
        if(!plugin.hasPermission(sender.getName(),"cdsc.define")){
            SendMessage("&4You don't have permission");
            return true;
        }
                   
        if(NotYetDefinePoints()) {
            SendMessage("&4Need to set all points");
            return true;
        }
        
        if (args.length < 3) {
            SendMessage("&4Wrong arguments on command define");
            return true;
        }
        return false;
    }
    
    private Boolean NotYetDefinePoints() {
        return !Cdsc.AreaCreating.containsKey((Player)sender) || Cdsc.AreaCreating.get((Player)sender).getFirstSpot() == null || Cdsc.AreaCreating.get((Player)sender).getSecondSpot() == null;
    }
}

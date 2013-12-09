/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.stutiguias.cdsc.commands;

import me.stutiguias.cdsc.init.Cdsc;
import me.stutiguias.cdsc.model.Area;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 * @author Daniel
 */
public class CdscCommands implements CommandExecutor {
     
    private final Cdsc plugin;
    private CommandSender sender;
    private String[] args;
    private final String MsgHr = "&e-----------------------------------------------------";
    
    public CdscCommands(Cdsc plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        
        if (sender.getName().equalsIgnoreCase("CONSOLE")) return true;
        if (!(sender instanceof Player)) return false;
        
        this.sender = sender;
        this.args = args;
        
        if (args.length == 0) return Help();
        
        switch(args[0].toLowerCase())
        {
            case "reload":
                if(!plugin.hasPermission(sender.getName(),"cdsc.reload")) return false;
                return Reload();
            case "update":
                if(!plugin.hasPermission(sender.getName(),"cdsc.update")) return false;
                return Update();
                
            case "w":
            case "wand":
                if(!plugin.hasPermission(sender.getName(),"cdsc.wand")) return false;
                return Wand();
            case "d":
            case "define" :
                if(!plugin.hasPermission(sender.getName(),"cdsc.define")) return false;
                return Define();
            case "dl":
            case "delete":
                if(!plugin.hasPermission(sender.getName(),"cdsc.delete")) return false;
                SendFormatMessage("&4Not Implement yet");
                return true;
            case "tp":
            case "teleport":
                if(!plugin.hasPermission(sender.getName(),"cdsc.tp")) return false;
                return teleportToArea();
            case "s":
            case "start":
                if(!plugin.hasPermission(sender.getName(),"cdsc.start")) return false;
                return StartEvent();
            case "e":
            case "end":
                if(!plugin.hasPermission(sender.getName(),"cdsc.end")) return false;
                return StopEvent();
            case "sc":
            case "setcore":
                if(!plugin.hasPermission(sender.getName(),"cdsc.sc")) return false;
                return SetCore();
                
            case "?":
            case "help":
            default:
                return Help();
        }       
    } 
        
    private boolean SetCore() {
        Player player = (Player)sender;

        Location location = player.getTargetBlock(null,2).getLocation();
        
        int index = plugin.getAreaIndex(location);
        
        if(index == -1) {
            SendFormatMessage("&4 Not inside any area");
            return true;
        }

        Cdsc.Areas.get(index).setCoreLocation(location);
        
        SendFormatMessage("&6 Core set !!");
        
        return true;
    }
    
    private boolean StartEvent() {
        Cdsc.EventOccurring = true;
        plugin.BroadcastEventStart();
        return true;
    }
    
    private boolean StopEvent() {
        Cdsc.EventOccurring = false;
        plugin.BroadcastEventEnd();
        return true;
    }
    
    public boolean Update() {
        plugin.Update();
        return true;
    }
    
    public boolean Wand() {
        Player player = (Player)sender;
        ItemStack itemStack = new ItemStack(Material.STICK,1);
        
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName("CDSC Wand");
        itemStack.setItemMeta(itemMeta);
        
        player.setItemInHand(itemStack);
        return true;
    }
        
    public boolean Reload() {
        SendFormatMessage("&6Reloading!");
        plugin.OnReload();
        SendFormatMessage("&6Reload Done!");    
        return true;
    }
    
    public boolean Help() {
        
        SendFormatMessage(MsgHr);
        SendFormatMessage(" &7Castle Defence for Simple Clans ");
        
        SendFormatMessage(MsgHr);
        
        if(plugin.hasPermission(sender.getName(),"cdsc.define")){
            SendFormatMessage("&6/cd <d|define> <areaName> <clanTag>");
        }
        
        if(plugin.hasPermission(sender.getName(),"cdsc.wand")){
            SendFormatMessage("&6/cd <w|wand>");
        }
        
        if(plugin.hasPermission(sender.getName(),"cdsc.delete")){
            SendFormatMessage("&6/cd <d|delete> <areaName>");
        }
                    
        if(plugin.hasPermission(sender.getName(),"cdsc.start")){
            SendFormatMessage("&6/cd <s|start>");
        }    
        
        if(plugin.hasPermission(sender.getName(),"cdsc.end")){
            SendFormatMessage("&6/cd <e|end>");
        }
        
        if(plugin.hasPermission(sender.getName(),"cdsc.tp")){
            SendFormatMessage("&6/cd <tp|teleport> <areaName>");
        }
  
        if(plugin.hasPermission(sender.getName(),"cdsc.update")){
            SendFormatMessage("&6/cd update");
        }
        
        if(plugin.hasPermission(sender.getName(),"cdsc.reload")){
            SendFormatMessage("&6/cd reload");
        }
        
        SendFormatMessage(MsgHr);
        
        return true;
    }

    public boolean teleportToArea() {
        Player player = (Player)sender;
               
        if (args.length < 1) {
            SendFormatMessage("&4Wrong arguments on command tp");
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
    
    public boolean Define() {
           
        if(!Cdsc.AreaCreating.containsKey((Player)sender)
        || Cdsc.AreaCreating.get((Player)sender).getFirstSpot() == null
        || Cdsc.AreaCreating.get((Player)sender).getSecondSpot() == null) {
            SendFormatMessage("&4Need to set all points");
            return false;
        }
        
        if (args.length < 2) {
            SendFormatMessage("&4Wrong arguments on command define");
            return true;
        }
        
        String name = args[1];
        
        Area area = plugin.getArea(name);
        
        if(area != null) {
         SendFormatMessage("&4This name is already in use.");
         return true;
        }
        
        String clanTag = args[2];
        String flag = "";
        
        if(!Cdsc.config.ClanOwnerCanBreakArea) flag += ",denyclanbreak";

        Location FirstSpot = Cdsc.AreaCreating.get((Player)sender).getFirstSpot();
        Location SecondSpot = Cdsc.AreaCreating.get((Player)sender).getSecondSpot();

        Cdsc.AreaCreating.remove((Player)sender);
        area = new Area(FirstSpot,SecondSpot,name,clanTag,flag);
        
        if(Cdsc.db.InsertArea(area)){
            Cdsc.Areas.add(area);
            SendFormatMessage(String.format("&6Area %s successfully define to clan %s",new Object[]{ name,clanTag }));
            return true;
        }
        
        SendFormatMessage(String.format("&4Erro on Insert to DB!"));
        return true;                
    }
    
    public void SendFormatMessage(String msg) {
        sender.sendMessage(plugin.parseColor(msg));
    }
    
}

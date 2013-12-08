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
            case "tp":
            case "teleport":
                if(!plugin.hasPermission(sender.getName(),"cdsc.tp")) return false;
                return teleportToArea();
                
            case "?":
            case "help":
            default:
                return Help();
        }       
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
            SendFormatMessage("&6/cd <d|define> <name>");
        }
        
        if(plugin.hasPermission(sender.getName(),"cdsc.wand")){
            SendFormatMessage("&6/cd <w|wand>");
        }
        
        if(plugin.hasPermission(sender.getName(),"cdsc.delete")){
            SendFormatMessage("&6/cd <d|delete> <spawnerName>");
        }
        
                
        if(plugin.hasPermission(sender.getName(),"cdsc.tp")){
            SendFormatMessage("&6/cd <tp|teleport> <name>");
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
        
        return true;
    }
    
    public boolean Define() {

        if (args.length < 1) {
            SendFormatMessage("&4Wrong arguments on command setspawn");
            return true;
        }
        
        String name = args[1];
        
        //SendFormatMessage("&4this name is already in use.");
    
        if(Cdsc.AreaCreating.containsKey((Player)sender)){
            
            if(Cdsc.AreaCreating.get((Player)sender).FirstSpot == null
            || Cdsc.AreaCreating.get((Player)sender).SecondSpot == null) {
                SendFormatMessage("&4Need to set all points");
                return false;
            }
            
            Location FirstSpot = Cdsc.AreaCreating.get((Player)sender).FirstSpot;
            Location SecondSpot = Cdsc.AreaCreating.get((Player)sender).SecondSpot;
        
            Cdsc.AreaCreating.remove((Player)sender);
            Cdsc.Areas.add(new Area(FirstSpot,SecondSpot));
            
            SendFormatMessage("&6Area successfully added.");
            return true;
        }
                        
        SendFormatMessage("&4Need to set all points");
        return false;
    }
    
    public void SendFormatMessage(String msg) {
        sender.sendMessage(plugin.parseColor(msg));
    }
    
}

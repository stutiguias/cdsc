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
                return Delete();
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
            case "i":
            case "info":
                if(!plugin.hasPermission(sender.getName(),"cdsc.info")) return false;
                return Info();
            case "l":
            case "list":
                if(!plugin.hasPermission(sender.getName(),"cdsc.list")) return false;
                return List();
                
            case "?":
            case "help":
            default:
                return Help();
        }       
    } 
        
    public boolean List() {
        if(Cdsc.Areas.isEmpty()) {
            SendMessage("&4 Areas empty");
            return true;
        }
        SendMessage(MsgHr);
        for(Area area:Cdsc.Areas){
            SendMessage("&3Name: &6%s", new Object[]{ area.getName() });
        }
        SendMessage(MsgHr);
        return true;
    }
    
    public boolean Info(){
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
    
    private boolean Delete() {
                     
        if (args.length < 1) {
            SendMessage("&4Wrong arguments");
            return true;
        }   
        
        String name = args[1];
        
        Area area = plugin.getArea(name);
        
        if(area == null) {
         SendMessage("&4Area name not found.");
         return true;
        }
        
        Cdsc.Areas.remove(area);
        Cdsc.db.Delete(area);
        return true;
    }
    
    private boolean SetCore() {
        Player player = (Player)sender;

        Location location = player.getTargetBlock(null,2).getLocation();
        
        int index = plugin.getAreaIndex(location);
        
        if(index == -1) {
            SendMessage("&4 Not inside any area");
            return true;
        }

        Cdsc.Areas.get(index).setCoreLocation(location);
        Cdsc.db.SetCore(Cdsc.Areas.get(index));
        
        SendMessage("&6 Core set !!");
        
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
        SendMessage("&6Reloading!");
        plugin.OnReload();
        SendMessage("&6Reload Done!");    
        return true;
    }
    
    public boolean Help() {
        
        SendMessage(MsgHr);
        SendMessage(" &7Castle Defence for Simple Clans ");
        
        SendMessage(MsgHr);
        
        if(plugin.hasPermission(sender.getName(),"cdsc.define")){
            SendMessage("&6/cd <d or define> <areaName> <clanTag> &e| &7Save Select area");
        }
        
        if(plugin.hasPermission(sender.getName(),"cdsc.wand")){
            SendMessage("&6/cd <w or wand>  &e| &7Get Special Wand to make area");
        }
                
        if(plugin.hasPermission(sender.getName(),"cdsc.update")){
            SendMessage("&6/cd <sc or setcore> &e| &7SetCore of an existing area");
        }
                
        if(plugin.hasPermission(sender.getName(),"cdsc.list")){
            SendMessage("&6/cd <l or list> &e| &7List all areas");
        }   
                
        if(plugin.hasPermission(sender.getName(),"cdsc.info")){
            SendMessage("&6/cd <i or info> &e| &7info about area you are");
        }   
        
        if(plugin.hasPermission(sender.getName(),"cdsc.delete")){
            SendMessage("&6/cd <dl or delete> <areaName> &e| &7Delete an area");
        }
                    
        if(plugin.hasPermission(sender.getName(),"cdsc.start")){
            SendMessage("&6/cd <s or start> &e| &7Start Event");
        }    
        
        if(plugin.hasPermission(sender.getName(),"cdsc.end")){
            SendMessage("&6/cd <e or end> &e| &7End event");
        }
        
        if(plugin.hasPermission(sender.getName(),"cdsc.tp")){
            SendMessage("&6/cd <tp or teleport> <areaName> &e| &7Teleport to an area");
        }
  
        if(plugin.hasPermission(sender.getName(),"cdsc.update")){
            SendMessage("&6/cd update &e| &7 Update the plugin");
        }
        
        if(plugin.hasPermission(sender.getName(),"cdsc.reload")){
            SendMessage("&6/cd reload &e| &7Reload the plugin");
        }
        
        SendMessage(MsgHr);
        
        return true;
    }

    public boolean teleportToArea() {
        Player player = (Player)sender;
               
        if (args.length < 1) {
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
    
    public boolean Define() {
           
        if(!Cdsc.AreaCreating.containsKey((Player)sender)
        || Cdsc.AreaCreating.get((Player)sender).getFirstSpot() == null
        || Cdsc.AreaCreating.get((Player)sender).getSecondSpot() == null) {
            SendMessage("&4Need to set all points");
            return false;
        }
        
        if (args.length < 2) {
            SendMessage("&4Wrong arguments on command define");
            return true;
        }
        
        String name = args[1];
        
        Area area = plugin.getArea(name);
        
        if(area != null) {
         SendMessage("&4This name is already in use.");
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
            SendMessage("&6Area %s successfully define to clan %s",new Object[]{ name,clanTag });
            return true;
        }
        
        SendMessage("&4Erro on Insert to DB!");
        return true;                
    }
    
    public void SendMessage(String msg) {
        sender.sendMessage(plugin.parseColor(msg));
    }
        
    public void SendMessage(String msg,Object[] args) {
        sender.sendMessage(plugin.parseColor(String.format(msg,args)));
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.stutiguias.cdsc.listener;

import me.stutiguias.cdsc.init.Cdsc;
import me.stutiguias.cdsc.model.Area;
import net.sacredlabyrinth.phaed.simpleclans.ClanPlayer;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
/**
 *
 * @author Daniel
 */
public class PlayerListener implements Listener {
    
    private final Cdsc plugin;
    
    public PlayerListener(Cdsc plugin) {
        this.plugin = plugin;
    }
        
    @EventHandler(priority= EventPriority.NORMAL)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
 
        if(plugin.config.UpdaterNotify && plugin.hasPermission(player,"cdsc.update") && Cdsc.update)
        {
          player.sendMessage(plugin.parseColor("&6An update is available: " + Cdsc.name + ", a " + Cdsc.type + " for " + Cdsc.version + " available at " + Cdsc.link));
          player.sendMessage(plugin.parseColor("&6Type /cd update if you would like to automatically update."));
        }

    }
    
    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockBreak(BlockBreakEvent event) {
        if(Cdsc.Areas.isEmpty()) return;
        
        Location location = event.getBlock().getLocation();       
        Player player = (Player)event.getPlayer();
        
        if(!isValidEvent(player, location,"break")) event.setCancelled(true);
        
    }
    
    @EventHandler(priority = EventPriority.NORMAL)
    public void onMoveInside(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location location = event.getTo();
        
        if(Cdsc.EventOccurring) return;
        
        if(!isValidEvent(player, location,"move")) {
            event.setCancelled(true);
        }
    }
    
    
    public boolean isInsideProtection(double x,double x2,double z,double z2,Location location) {
       return location.getX() <= x && location.getX() >= x2 && location.getZ() <= z && location.getZ() >= z2;
    }
    
    @EventHandler
    public void PlayerInteract(PlayerInteractEvent event){
        if( !event.hasItem() 
         || !event.getItem().hasItemMeta() 
         || !event.getItem().getItemMeta().hasDisplayName()
         || !event.getItem().getItemMeta().getDisplayName().equals("CDSC Wand")) return;
        
        if(!event.hasBlock()) return;
        
        Player player = event.getPlayer();
        if(!Cdsc.AreaCreating.containsKey(player)) Cdsc.AreaCreating.put(player, new Area());
        
        Location location = event.getClickedBlock().getLocation();
        String setOn = String.format("( %s,%s )",new Object[] { location.getX() , location.getZ() } );
        
        if(event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
            Cdsc.AreaCreating.get(player).setFirstSpot(location);
            
            String msg = String.format("&6First Spot Set on %s",new Object[] { setOn } );
            player.sendMessage(plugin.parseColor(msg));
            event.setCancelled(true);
        }
        
        if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            Cdsc.AreaCreating.get(player).setSecondSpot(location);
            
            String msg = String.format("&6Second Spot Set on %s",new Object[] { setOn } );
            player.sendMessage(plugin.parseColor(msg));
            event.setCancelled(true);            
        }
        
    }
    
    public boolean isValidEvent(Player player,Location location,String event) {
        
        ClanPlayer clanPlayer = plugin.getSimpleClan().getClanManager().getClanPlayer(player);
        
        Area area = plugin.getArea(location);
        
        if(area == null) return true;
        
        switch (event) {
            case "break":
                return isValidBreak(area, player, location, clanPlayer);
            case "move":
                return isValidMove(area, clanPlayer);
            default:
                return true;
        }
    }
    
    public boolean isValidBreak(Area area,Player player,Location location,ClanPlayer clanPlayer) {
                
        if(area.getCore().distance(location) == 0) {
            player.sendMessage(plugin.parseColor("&4 You hit the core"));
            // TODO : Logic hit the core
            return false;
        }
        
        if(clanPlayer == null || area.getFlags().contains("denyclanbreak") ) {
            return false;
        }
        
        return area.getClanTag().equalsIgnoreCase(clanPlayer.getClan().getTag());
    }
    
    public boolean isValidMove(Area area,ClanPlayer clanPlayer) {
        return area.getClanTag().equalsIgnoreCase(clanPlayer.getClan().getTag());
    }
}

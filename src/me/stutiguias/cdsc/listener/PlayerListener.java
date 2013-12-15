/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.stutiguias.cdsc.listener;

import java.util.ArrayList;
import java.util.List;
import me.stutiguias.cdsc.init.Cdsc;
import me.stutiguias.cdsc.init.Util;
import me.stutiguias.cdsc.model.Area;
import net.sacredlabyrinth.phaed.simpleclans.ClanPlayer;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
/**
 *
 * @author Daniel
 */
public class PlayerListener extends Util implements Listener {
    
    public PlayerListener(Cdsc plugin) {
        super(plugin);
    }
        
    @EventHandler(priority= EventPriority.NORMAL)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
 
        if(Cdsc.config.UpdaterNotify && plugin.hasPermission(player,"cdsc.update") && Cdsc.update)
        {
          SendMessage(player,"&6An update is available: " + Cdsc.name + ", a " + Cdsc.type + " for " + Cdsc.version + " available at " + Cdsc.link);
          SendMessage(player,"&6Type /cd update if you would like to automatically update.");
        }

    }
    
    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockPlace(BlockPlaceEvent event) {
        if(Cdsc.Areas.isEmpty()) return;
                
        Location location = event.getBlock().getLocation();       
        Player player = (Player)event.getPlayer();
        
        if(!isValidEvent(player, location,"place")) {
            if(plugin.hasPermission(player,"cdsc.bypass")) return;
            event.setCancelled(true);
        }
    }
    
    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockBreak(BlockBreakEvent event) {
        if(Cdsc.Areas.isEmpty()) return;
        
        Location location = event.getBlock().getLocation();       
        Player player = (Player)event.getPlayer();
        
        if(!isValidEvent(player, location,"break")) {
            if(plugin.hasPermission(player,"cdsc.bypass")) return;
            event.setCancelled(true);
        }
        
    }
    
    @EventHandler(priority = EventPriority.NORMAL)
    public void onMoveInside(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location location = event.getTo();
        
        if(Cdsc.EventOccurring) return;
        
        if(!isValidEvent(player, location,"move")) {
            if(plugin.hasPermission(player,"cdsc.bypass")) return;
            player.teleport(event.getFrom());
        }
    
    }
    
    @EventHandler(priority = EventPriority.NORMAL)
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

            SendMessage(player,"&6First Spot Set on %s",new Object[] { setOn });
            event.setCancelled(true);
        }
        
        if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            Cdsc.AreaCreating.get(player).setSecondSpot(location);
            
            SendMessage(player,"&6Second Spot Set on %s",new Object[] { setOn });
            event.setCancelled(true);            
        }
        
    }
    
    @EventHandler(priority = EventPriority.NORMAL)
    public void onItemDrop(EntityDeathEvent event) {
        if(Cdsc.Areas.isEmpty()) return;
        if(!Cdsc.config.Dontdropduringevent) return;
        
        Location location = event.getEntity().getLocation();       
        if(event.getEntityType() != EntityType.PLAYER) return;
        Player player = (Player)event.getEntity();
        
        if(!isValidEvent(player, location,"drop")) {
            event.setDroppedExp(0);
            if(event.getDrops().isEmpty())return;
            List<ItemStack> tmpDrops = new ArrayList<>(event.getDrops());
            Cdsc.InventorySave.put(player,tmpDrops);
            for(ItemStack isDrop : tmpDrops)
            {
               event.getDrops().remove(isDrop);
            }
        }
    }
    
    @EventHandler()
    public void onPlayerReSpawn(PlayerRespawnEvent event) {
         if(Cdsc.Areas.isEmpty()) return;
         if(!Cdsc.EventOccurring) return;
         if(Cdsc.InventorySave.isEmpty()) return;
         
         Player player = (Player)event.getPlayer();
         
         if(Cdsc.InventorySave.get(player).isEmpty()) return;
         
         for(ItemStack item:Cdsc.InventorySave.get(player)){
             player.getInventory().addItem(item);
         }
         
         Cdsc.InventorySave.remove(player);
         
    }
    public boolean isValidEvent(Player player,Location location,String event) {

        Area area = plugin.getArea(location);
        if(area == null) return true;
        
        ClanPlayer clanPlayer = plugin.getSimpleClan().getClanManager().getClanPlayer(player);
        
        switch (event) {
            case "place":
                return isValidPlace(area, clanPlayer);
            case "break":
                return isValidBreak(area, player, location, clanPlayer);
            case "move":
                return isValidMove(area, clanPlayer);
            case "drop":
                return isValidDrop();
            default:
                return true;
        }
    }
    
    public boolean isValidDrop() {
        return !Cdsc.EventOccurring;
    }
    
    public boolean isValidPlace(Area area,ClanPlayer clanPlayer) {

        if(area.getFlags().contains("denyclanplace") ) return false;
        
        return isAllowedClan(area.getClanTag(), clanPlayer);
    }    
    
    public boolean isValidBreak(Area area,Player player,Location location,ClanPlayer clanPlayer) {
                
        if(area.getCoreLocation() != null && area.getCoreLocation().distance(location) == 0) {
            return HitCore(location, clanPlayer, player);
        }
        
        if(area.getFlags().contains("denyclanbreak") ) return false;
        
        return isAllowedClan(area.getClanTag(), clanPlayer);
    }
    
    public boolean isValidMove(Area area,ClanPlayer clanPlayer) {
        return isAllowedClan(area.getClanTag(), clanPlayer);
    }
    
    public boolean isAllowedClan(String clanTag,ClanPlayer clanPlayer) {
        if(clanPlayer == null || clanPlayer.getClan() == null) return false;
        return clanTag.equalsIgnoreCase(clanPlayer.getClan().getTag());
    }
    
    public boolean HitCore(Location location,ClanPlayer clanPlayer,Player player) {
        
        if(clanPlayer == null || clanPlayer.getClan() == null) return false;
        
        int index = plugin.getAreaIndex(location);
       
        if(clanPlayer.getClan().getTag().equals(Cdsc.Areas.get(index).getClanTag())) { 
            SendMessage(player,"&6Your Clan Own this area and not allow to hit the core!");
            return false;
        }

        int coreLife = Cdsc.Areas.get(index).getCoreLife();
        coreLife -= 1;

        if(coreLife == 0) {
            
            Cdsc.Areas.get(index).setClanTag(clanPlayer.getClan().getTag());
            Cdsc.Areas.get(index).setCoreLife(Cdsc.config.CoreLife);
            Cdsc.db.UpdateArea(Cdsc.Areas.get(index));
            
            BrcstMsg("&6The core broke ! &1%s&6 Clan win the area !",new Object[] { clanPlayer.getClan().getTag() });
            Cdsc.EventOccurring = false;
            
        }else{ 
            
            Cdsc.Areas.get(index).setCoreLife(coreLife);
            SendMessage(player,"&6You hit the core - Core life is %s",new Object[]{ coreLife } );
            
        }
        
        return false;
        
    }
    
}

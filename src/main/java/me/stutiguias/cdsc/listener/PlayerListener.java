/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.stutiguias.cdsc.listener;

import fr.neatmonster.nocheatplus.hooks.NCPExemptionManager;
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
 
        if(Cdsc.config.UpdaterNotify && plugin.hasPermission(player,"cdsc.update") && Cdsc.update)
        {
          SendMsg(player,"&6An update is available: " + Cdsc.name + ", a " + Cdsc.type + " for " + Cdsc.version + " available at " + Cdsc.link);
          SendMsg(player,"&6Type /cd update if you would like to automatically update.");
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
            player.teleport(event.getFrom());
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
            SendMsg(player,msg);
            event.setCancelled(true);
        }
        
        if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            Cdsc.AreaCreating.get(player).setSecondSpot(location);
            
            String msg = String.format("&6Second Spot Set on %s",new Object[] { setOn } );
            SendMsg(player,msg);
            event.setCancelled(true);            
        }
        
    }
    
    public boolean isValidEvent(Player player,Location location,String event) {

        Area area = plugin.getArea(location);
        if(area == null) return true;
        
        ClanPlayer clanPlayer = plugin.getSimpleClan().getClanManager().getClanPlayer(player);
        
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
            SendMsg(player,"&6Your Clan Own this area and not allow to hit the core!");
            return false;
        }

        int coreLife = Cdsc.Areas.get(index).getCoreLife();
        coreLife -= 1;

        if(coreLife == 0) {
            
            Cdsc.Areas.get(index).setClanTag(clanPlayer.getClan().getTag());
            Cdsc.Areas.get(index).setCoreLife(Cdsc.config.CoreLife);
            Cdsc.db.UpdateArea(Cdsc.Areas.get(index));
            
            BrcstMsg(String.format("&6The core broke ! &1%s&6 Clan win the area !",new Object[] { clanPlayer.getClan().getTag() }));
            Cdsc.EventOccurring = false;
        }else{   
            Cdsc.Areas.get(index).setCoreLife(coreLife);
            SendMsg(player,"&6You hit the core - Core life is " + coreLife );
        }
        
        return false;
        
    }
    
    public void SendMsg(Player player,String msg) {
        player.sendMessage(plugin.parseColor(msg));
    }
    
    public void BrcstMsg(String msg) {
        plugin.getServer().broadcastMessage(plugin.parseColor(msg));
    }
}

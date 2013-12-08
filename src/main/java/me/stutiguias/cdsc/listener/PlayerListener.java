/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.stutiguias.cdsc.listener;

import me.stutiguias.cdsc.init.Cdsc;
import me.stutiguias.cdsc.model.Area;
import net.sacredlabyrinth.phaed.simpleclans.ClanPlayer;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
/**
 *
 * @author Daniel
 */
public class PlayerListener implements Listener {
    
    private final Cdsc plugin;
    
    public PlayerListener(Cdsc plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onChunkUnload(ChunkUnloadEvent event) {
       
        Chunk chunk = event.getChunk();
        Entity[] entities = chunk.getEntities();
        
    }
        
    @EventHandler(priority= EventPriority.NORMAL)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
 
        if(plugin.UpdaterNotify && plugin.hasPermission(player,"cdsc.update") && Cdsc.update)
        {
          player.sendMessage(plugin.parseColor("&6An update is available: " + Cdsc.name + ", a " + Cdsc.type + " for " + Cdsc.version + " available at " + Cdsc.link));
          player.sendMessage(plugin.parseColor("&6Type /cd update if you would like to automatically update."));
        }

    }
    
    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockBreak(BlockBreakEvent event) {
        
        Location location = event.getBlock().getLocation();
        
        Player player = (Player)event.getPlayer();
        
        ClanPlayer clanPlayer = plugin.getSimpleClan().getClanManager().getClanPlayer(player);
        if(clanPlayer != null) Cdsc.logger.info(clanPlayer.getClan().getName());
        
        for(Area area:Cdsc.Areas) {
            
            double xx = area.FirstSpot.getX();
            double yx = area.SecondSpot.getX();
            double xz = area.FirstSpot.getZ();
            double yz = area.SecondSpot.getZ();

            double x,x2,z,z2;

            if(xx > yx) {
                 x = xx;
                 x2 = yx;
            } else {
                 x = yx;
                 x2 = xx;    
            }

            if(yz > xz) {
               z = yz;
               z2 = xz;
            } else {
               z = xz; 
               z2 = xz;
            }

            if(isBlockInside(x, x2, z, z2, location)) event.setCancelled(true);                
        }
        
    }
    
    public boolean isBlockInside(double x,double x2,double z,double z2,Location location) {
       return location.getX() < x && location.getX() > x2 && location.getZ() < z && location.getZ() > z2;
    }
    
    @EventHandler
    public void PlayerInteract(PlayerInteractEvent event){
        if( !event.hasItem() 
         || !event.getItem().hasItemMeta() 
         || !event.getItem().getItemMeta().hasDisplayName()
         || !event.getItem().getItemMeta().getDisplayName().equals("CDSC Wand")) return;
        
        Player player = event.getPlayer();
        if(!Cdsc.AreaCreating.containsKey(player))
            Cdsc.AreaCreating.put(player, new Area());
        
        if(event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
            Cdsc.AreaCreating.get(player).FirstSpot = event.getClickedBlock().getLocation();
            player.sendMessage(plugin.parseColor("&6First Spot Set!"));
            event.setCancelled(true);
        }
        
        if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            Cdsc.AreaCreating.get(player).SecondSpot = event.getClickedBlock().getLocation();
            player.sendMessage(plugin.parseColor("&6Second Spot Set!"));
            event.setCancelled(true);            
        }
        
    }
}

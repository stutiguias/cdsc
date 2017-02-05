/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.stutiguias.cdsc.listener;

import java.util.HashMap;
import me.stutiguias.cdsc.init.ActionHandler;
import me.stutiguias.cdsc.init.Cdsc;
import me.stutiguias.cdsc.init.CombatTag;
import me.stutiguias.cdsc.model.Area;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
/**
 *
 * @author Daniel
 */
public class PlayerListener extends ListenerHandle {
    
    public HashMap<Player , ItemStack[]> items = new HashMap<>();
    public ActionHandler action;
    public CombatTag CombatTag;
    
    public PlayerListener(Cdsc plugin) {
        super(plugin);
        action = new ActionHandler(plugin);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockPlace(BlockPlaceEvent event) {
        if(Cdsc.Areas.isEmpty()) return;
                      
        Player player = (Player)event.getPlayer();
        
        if(this.action.NeedCancelBlockPlace(player, event.getBlock())) {
            if(plugin.hasPermission(player,"cdsc.bypass")) return;
            event.setCancelled(true);
        }
    }
    
    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockBreak(BlockBreakEvent event) {
        if(Cdsc.Areas.isEmpty()) return;
             
        Player player = (Player)event.getPlayer();
        
        if(this.action.NeedCancelBlockBreak(player, event.getBlock())) {
            if(plugin.hasPermission(player,"cdsc.bypass")) return;
            event.setCancelled(true);
        }
        
    }
    
    @EventHandler(priority = EventPriority.NORMAL)
    public void onMoveInside(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location location = event.getTo();

        if(this.action.NeedCancelBlockMove(player, location)) {
            if(plugin.hasPermission(player,"cdsc.bypass")) return;
            Area area = plugin.getArea(location);
            if(area.getExit() == null) {
                Location tpTo = event.getFrom();
                tpTo.setZ(event.getFrom().getZ() - 1);
                player.teleport(tpTo);
            }else{
                player.teleport(area.getExit());
            }
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
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onDamage(EntityDamageByEntityEvent event){
      if(Cdsc.Areas.isEmpty()) return;
      if(Cdsc.config.DieDuringEvent()) return;   
      
      Area area = plugin.getArea(event.getEntity().getLocation());
      if(area == null) return;
      if(Cdsc.EventNotEnable(area)) return;
 
        Entity defender;

        defender = event.getEntity();
        if(defender == null) {
            return;
        }
        
        if(defender instanceof Player) {
            Player df = (Player)defender;

            if (df.getHealth() - event.getDamage() <= 0) {
                event.setCancelled(true);
                if(plugin.getServer().getPluginManager().getPlugin("CombatTag") != null){
                    CombatTag = new CombatTag(plugin);
                }
                if(CombatTag != null) CombatTag.Get().untagPlayer(df);
                df.teleport(area.getExit());
                df.setHealth(20);
            }
        }
      
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onRespawn(PlayerRespawnEvent event){
        if(Cdsc.Areas.isEmpty()) return;
        if(Cdsc.config.DropDuringEvent()) return;
        
        Player player = (Player)event.getPlayer();

       if(items.containsKey(player)){
           for(ItemStack stack : items.get(player)){
               if(stack != null){
                   player.getInventory().addItem(stack);
               }
           }
           items.remove(player);
           player.updateInventory();
           SendMessage(player,Cdsc.msg.ItemsAppears);
           event.setRespawnLocation(this.action.RespawnInLocation.get(player));
           this.action.RespawnInLocation.remove(player);
       }
    }
 
    @EventHandler(priority = EventPriority.HIGH)
    public void onDeath(PlayerDeathEvent event){
        if(Cdsc.Areas.isEmpty()) return;
        if(Cdsc.config.DropDuringEvent()) return;
        if(event.getEntityType() != EntityType.PLAYER) return;

        Player player = (Player)event.getEntity();
        
        if(this.action.NeedCancelDrop(player, player.getLocation())) {
            event.setDroppedExp(0);
            if(event.getDrops().isEmpty())return;
            ItemStack[] content = player.getInventory().getContents();
            items.put(player, content);
            event.getDrops().clear();
            SendMessage(player,Cdsc.msg.ItemsStore);
        }
    }
 
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.stutiguias.cdsc.listener;

import java.util.HashMap;
import me.stutiguias.cdsc.init.Cdsc;
import me.stutiguias.cdsc.init.CombatTag;
import me.stutiguias.cdsc.init.SimpleClan;
import me.stutiguias.cdsc.model.Area;
import net.sacredlabyrinth.phaed.simpleclans.ClanPlayer;
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
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
/**
 *
 * @author Daniel
 */
public class PlayerListener extends ListenerHandle {
    
    public HashMap<Player , ItemStack[]> items = new HashMap<>();
    public HashMap<Player , Location> RespawnInLocation = new HashMap<>();
    public CombatTag CombatTag;
    public SimpleClan simpleClan;
    
    public PlayerListener(Cdsc plugin) {
        super(plugin);
        simpleClan = new SimpleClan(plugin);
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
        
        if(NeedCancelEvent(player, location,"place")) {
            if(plugin.hasPermission(player,"cdsc.bypass")) return;
            event.setCancelled(true);
        }
    }
    
    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockBreak(BlockBreakEvent event) {
        if(Cdsc.Areas.isEmpty()) return;
        
        Location location = event.getBlock().getLocation();       
        Player player = (Player)event.getPlayer();
        
        if(NeedCancelEvent(player, location,"break")) {
            if(plugin.hasPermission(player,"cdsc.bypass")) return;
            event.setCancelled(true);
        }
        
    }
    
    @EventHandler(priority = EventPriority.NORMAL)
    public void onMoveInside(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location location = event.getTo();

        if(NeedCancelEvent(player, location,"move")) {
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
           event.setRespawnLocation(RespawnInLocation.get(player));
           RespawnInLocation.remove(player);
       }
    }
 
    @EventHandler(priority = EventPriority.HIGH)
    public void onDeath(PlayerDeathEvent event){
        if(Cdsc.Areas.isEmpty()) return;
        if(Cdsc.config.DropDuringEvent()) return;
        if(event.getEntityType() != EntityType.PLAYER) return;

        Player player = (Player)event.getEntity();
        
        if(NeedCancelEvent(player, player.getLocation(),"drop")) {
            event.setDroppedExp(0);
            if(event.getDrops().isEmpty())return;
            ItemStack[] content = player.getInventory().getContents();
            items.put(player, content);
            event.getDrops().clear();
            SendMessage(player,Cdsc.msg.ItemsStore);
        }
    }

    public boolean NeedCancelEvent(Player player,Location location,String event) {

        Area area = plugin.getArea(location);
        if(area == null) return false;
        
        ClanPlayer clanPlayer = simpleClan.Get().getClanManager().getClanPlayer(player);
        
        switch (event) {
            case "place":
                return BlockPlace(area, clanPlayer);
            case "break":
                return BlockBreak(area, player, location, clanPlayer);
            case "move":
                return BlockMove(area, clanPlayer);
            case "drop":
                return Drop(area,clanPlayer,player);
            default:
                return false;
        }
    }
    
    public boolean Drop(Area area,ClanPlayer clanPlayer,Player player) {
        if(Cdsc.EventEnable(area)) {
            SetReSpawnLoc(area, clanPlayer, player);
            return true;
        }
        return false;
    }
     
    public void SetReSpawnLoc(Area area,ClanPlayer clanPlayer,Player player) {
        if(isValidClan(area.getClanTag(), clanPlayer)) {
            RespawnInLocation.put(player, area.getSpawn());
            return;
        }
        
        RespawnInLocation.put(player, area.getExit());
    }
    
    public boolean BlockPlace(Area area,ClanPlayer clanPlayer) {
        if(area.getFlags().contains("denyclanplace") ) return true;
        return !isValidClan(area.getClanTag(), clanPlayer);
    }    
    
    public boolean BlockBreak(Area area,Player player,Location location,ClanPlayer clanPlayer) {
                
        if(area.getCoreLocation() != null && area.getCoreLocation().distance(location) == 0) {
            return HitCore(location, clanPlayer, player);
        }
        
        if(area.getFlags().contains("denyclanbreak") ) return true;
        
        return !isValidClan(area.getClanTag(), clanPlayer);
    }
    
    public boolean BlockMove(Area area,ClanPlayer clanPlayer) {
        if(Cdsc.EventEnable(area)) return false;
        return !isValidClan(area.getClanTag(), clanPlayer);
    }
    
    public boolean isValidClan(String clanTag,ClanPlayer clanPlayer) {
        if(clanPlayer == null || clanPlayer.getClan() == null) return false;
        return clanTag.equalsIgnoreCase(clanPlayer.getClan().getTag()) != false;
    }
    
    public boolean HitCore(Location location,ClanPlayer clanPlayer,Player player) {
        
        if(clanPlayer == null || clanPlayer.getClan() == null) return true;
        
        int index = plugin.getAreaIndex(location);
       
        if(clanPlayer.getClan().getTag().equals(Cdsc.Areas.get(index).getClanTag())) { 
            SendMessage(player,Cdsc.msg.ClanOwn);
            return true;
        }

        int coreLife = Cdsc.Areas.get(index).getCoreLife();
        coreLife -= 1;

        if(coreLife == 0) {
            
            Area area = Cdsc.Areas.get(index);
            area.setClanTag(clanPlayer.getClan().getTag());
            area.setCoreLife(Cdsc.config.CoreLife);
            
            if(area.onEvent())
                area.setEvent(false);
            else
                Cdsc.EventOccurring = false;
            
            Cdsc.db.UpdateArea(area);
            
            BrcstMsg(Cdsc.msg.CoreBroke,new Object[] { area.getName() , clanPlayer.getClan().getTag() });
            
        }else{ 
            
            Cdsc.Areas.get(index).setCoreLife(coreLife);
            BrcstMsg(Cdsc.msg.CoreHit,new Object[]{ coreLife } );
            
        }
        
        return true;
        
    }
    
}

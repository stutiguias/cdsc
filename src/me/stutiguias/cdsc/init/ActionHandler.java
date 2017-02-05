/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.stutiguias.cdsc.init;

import java.util.HashMap;
import me.stutiguias.cdsc.model.Area;
import net.sacredlabyrinth.phaed.simpleclans.ClanPlayer;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

/**
 *
 * @author Daniel
 */
public class ActionHandler extends Util {

    private Area area;
    private ClanPlayer clanPlayer;
    public SimpleClan simpleClan;
    public HashMap<Player, Location> RespawnInLocation = new HashMap<>();
    private final BlockHandler blockHandler;
    
    public ActionHandler(Cdsc plugin) {
        super(plugin);
        simpleClan = new SimpleClan(plugin);
        blockHandler = new BlockHandler(plugin);
    }
    
    public Area LoadArea(Location location){ 
        return plugin.getArea(location);
    }
    
    public ClanPlayer LoadClan(Player player) {
        return simpleClan.Get().getClanManager().getClanPlayer(player);
    }
    
    public void LoadConfig(Player player,Location location) {
        area = LoadArea(location);
        clanPlayer = LoadClan(player);
    }
    
    public boolean NeedCancelBlockBreak(Player player,Block block) {
        LoadConfig(player, block.getLocation());
        if(area == null) return false;
        
        if (area.getCoreLocation() != null && area.getCoreLocation().distance(block.getLocation()) == 0) {
            return HitCore(block.getLocation(), clanPlayer, player);
        }

        if (area.getFlags().contains("blockbreakduringevent") && Cdsc.EventEnable(area)) {
            blockHandler.Protect(block, area);
            return false;
        }
        
        if (area.getFlags().contains("denyclanbreak")) {
            return true;
        }

        return !isValidClan(area.getClanTag(), clanPlayer);
    }
    
    public boolean NeedCancelBlockPlace(Player player,Block block) {
        LoadConfig(player, block.getLocation());
        if(area == null) return false;
        
        if (area.getFlags().contains("blockplaceduringevent") && Cdsc.EventEnable(area)) return false;
        if (area.getFlags().contains("denyclanplace")) return true;
        return !isValidClan(area.getClanTag(), clanPlayer);
    }

    public boolean NeedCancelDrop(Player player, Location location) {
        LoadConfig(player,location);
        if(area == null) return false;
        
        if (Cdsc.EventEnable(area)) {
            SetReSpawnLoc(area, clanPlayer, player);
            return true;
        }
        return false;
    }
    
    public boolean NeedCancelBlockMove(Player player, Location location) {
        LoadConfig(player,location);
        if(area == null) return false;
        
        if (Cdsc.EventEnable(area)) {
            return false;
        }
        return !isValidClan(area.getClanTag(), clanPlayer);
    }
    
    public void SetReSpawnLoc(Area area, ClanPlayer clanPlayer, Player player) {
        if (isValidClan(area.getClanTag(), clanPlayer)) {
            RespawnInLocation.put(player, area.getSpawn());
            return;
        }

        RespawnInLocation.put(player, area.getExit());
    }

    public boolean isValidClan(String clanTag, ClanPlayer clanPlayer) {
        if (clanPlayer == null || clanPlayer.getClan() == null) {
            return false;
        }
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

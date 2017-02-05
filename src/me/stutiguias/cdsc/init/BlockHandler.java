/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.stutiguias.cdsc.init;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.UUID;
import java.util.logging.Level;
import me.stutiguias.cdsc.model.Area;
import me.stutiguias.cdsc.model.SaveInfo;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

/**
 *
 * @author Daniel
 */
public class BlockHandler extends Util {
    
    public List<Location> AlreadySaveLocation = new ArrayList<>();

    public BlockHandler(Cdsc plugin) {
        super(plugin);
        
    }
    
    public void Protect(Block block,Area area) {
        if(AlreadySaveLocation.contains(block.getLocation())) return;
        AlreadySaveLocation.add(block.getLocation());
        if(Cdsc.Store.containsKey(area)){
            Cdsc.Store.get(area).add(new SaveInfo(block.getType(),block.getLocation(),area.getName()));
        }else{
            Queue<SaveInfo> storetemp = new LinkedList<>();
            storetemp.add(new SaveInfo(block.getType(),block.getLocation(),area.getName()));
            Cdsc.Store.put(area, storetemp);
        }
        
    }
    
    public void ReBuild(Area area) {
        if(Cdsc.Store.containsKey(area)) {
            Queue<SaveInfo> storetemp = Cdsc.Store.get(area);
            World world = plugin.getServer().getWorld(UUID.fromString(area.getWorld()));
            int size = storetemp.size();
            for (int i = 0; i < size; i++) {
                SaveInfo saveinfo = storetemp.poll();
                if (saveinfo == null) break;
                Block block = world.getBlockAt(saveinfo.getLocation());
                block.setType(saveinfo.getMaterial());
            } 
        }
    }
}

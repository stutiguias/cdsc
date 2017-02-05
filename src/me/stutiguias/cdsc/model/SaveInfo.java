/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.stutiguias.cdsc.model;

import org.bukkit.Location;
import org.bukkit.Material;

/**
 *
 * @author Daniel
 */
public class SaveInfo {
    
    private Location location;
    private String Owner;
    private Material material
;
    public SaveInfo(Material material,Location location,String owner){
       this.material = material;
       this.location = location;
       this.Owner = owner;
    }
    
    /**
     * @return the location
     */
    public Location getLocation() {
        return location;
    }

    /**
     * @param location the location to set
     */
    public void setLocation(Location location) {
        this.location = location;
    }

    /**
     * @return the Owner
     */
    public String getOwner() {
        return Owner;
    }

    /**
     * @param Owner the Owner to set
     */
    public void setOwner(String Owner) {
        this.Owner = Owner;
    }

    /**
     * @return the block
     */
    public Material getMaterial() {
        return material;
    }

    /**
     * @param block the block to set
     */
    public void setBlock(Material material) {
        this.material = material;
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.stutiguias.cdsc.model;

import org.bukkit.Location;

/**
 *
 * @author Daniel
 */
public class Area {
    
    public Area(){
    }
    
    public Area(Location FirstSpot,Location SecondSpot){
        this.FirstSpot = FirstSpot;
        this.SecondSpot = SecondSpot;
    }
    
    public Location FirstSpot;
    public Location SecondSpot;
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.stutiguias.cdsc.model;

import me.stutiguias.cdsc.init.Cdsc;
import org.bukkit.Location;

/**
 *
 * @author Daniel
 */
public class Area {
    
    public Area(){
    }
    
    public Area(Location FirstSpot,Location SecondSpot,String name,String clanTag,String flag){
        this.FirstSpot = FirstSpot;
        this.SecondSpot = SecondSpot;
        this.name = name;
        this.clanTag = clanTag;
        this.flags = flag; 
        this.CoreLife = Cdsc.config.CoreLife;
    }
    
    private Location CoreLocation;
    private int CoreLife;
    private Location FirstSpot;
    private Location SecondSpot;
    private String name;
    private String clanTag;
    private String flags;
    private Location Exit;
    private boolean Event;
    
    /**
     * @return the FirstSpot
     */
    public Location getFirstSpot() {
        return FirstSpot;
    }

    /**
     * @param FirstSpot the FirstSpot to set
     */
    public void setFirstSpot(Location FirstSpot) {
        this.FirstSpot = FirstSpot;
    }

    /**
     * @return the SecondSpot
     */
    public Location getSecondSpot() {
        return SecondSpot;
    }

    /**
     * @param SecondSpot the SecondSpot to set
     */
    public void setSecondSpot(Location SecondSpot) {
        this.SecondSpot = SecondSpot;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the tag
     */
    public String getClanTag() {
        return clanTag;
    }

    /**
     * @param clanTag the tag to set
     */
    public void setClanTag(String clanTag) {
        this.clanTag = clanTag;
    }

    /**
     * @return the flags
     */
    public String getFlags() {
        return flags;
    }

    /**
     * @param flags the flags to set
     */
    public void setFlags(String flags) {
        this.flags = flags;
    }

    /**
     * @return the Core
     */
    public Location getCoreLocation() {
        return CoreLocation;
    }

    /**
     * @param CoreLocation the Core to set
     */
    public void setCoreLocation(Location CoreLocation) {
        this.CoreLocation = CoreLocation;
    }

    /**
     * @return the CoreLife
     */
    public int getCoreLife() {
        return CoreLife;
    }

    /**
     * @param CoreLife the CoreLife to set
     */
    public void setCoreLife(int CoreLife) {
        this.CoreLife = CoreLife;
    }

    /**
     * @return the Exit
     */
    public Location getExit() {
        return Exit;
    }

    /**
     * @param Exit the Exit to set
     */
    public void setExit(Location Exit) {
        this.Exit = Exit;
    }

    /**
     * @return the Event
     */
    public boolean isEvent() {
        return Event;
    }

    /**
     * @param Event the Event to set
     */
    public void setEvent(boolean Event) {
        this.Event = Event;
    }
}

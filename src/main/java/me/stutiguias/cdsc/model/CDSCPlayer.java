/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package me.stutiguias.cdsc.model;

/**
 *
 * @author Daniel
 */
public class CDSCPlayer {
    
    public CDSCPlayer() {
    }
    
    public CDSCPlayer(String name,Boolean ban,long expTime){ 
        
        this.name = name;
        this.ban = ban;
        
    }
    
    private String name;
    private Boolean ban;

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
     * @return the ban
     */
    public Boolean getBan() {
        return ban;
    }

    /**
     * @param ban the ban to set
     */
    public void setBan(Boolean ban) {
        this.ban = ban;
    }

}

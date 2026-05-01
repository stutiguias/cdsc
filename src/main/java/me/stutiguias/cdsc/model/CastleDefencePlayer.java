/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package me.stutiguias.cdsc.model;

import me.stutiguias.cdsc.init.Cdsc;
import me.stutiguias.cdsc.init.Util;
import org.bukkit.entity.Player;

import java.util.Objects;

/**
 *
 * @author Daniel
 */
public class CastleDefencePlayer extends Util {

    private SimpleClan simpleClan;

    public CastleDefencePlayer(Cdsc plugin) {
        super(plugin);
        // TODO : Load only if config yes
        this.simpleClan = new SimpleClan(plugin);
    }

    private Player player;
    public void SetPlayer(Player player){
        this.player = player;
        this.UUID = player.getUniqueId().toString();
    }

    private String UUID;
    public void SetUUID(String UUID){ this.UUID = UUID; }

    private Boolean ban;
    public Boolean getBan() {
        return ban;
    }
    public void setBan(Boolean ban) {
        this.ban = ban;
    }

    public String getClaTag() {
        // TODO : CHECK IF IS SC
        if(player == null) player = plugin.getServer().getPlayer(UUID);
        return simpleClan.LoadClan(player).getTag();
    }

    public boolean haveCla() {
        // TODO : CHECK IF IS SC
        return simpleClan.LoadClan(player) != null && simpleClan.LoadClan(player).getClan() != null;
    }

    public String getPlayerName() { return Objects.requireNonNull(player).getName(); }
}

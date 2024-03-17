/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package me.stutiguias.cdsc.model;

import me.stutiguias.cdsc.init.Cdsc;
import me.stutiguias.cdsc.init.Util;
import net.sacredlabyrinth.phaed.simpleclans.ClanPlayer;
import net.sacredlabyrinth.phaed.simpleclans.SimpleClans;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 *
 * @author Daniel
 */
public class SimpleClan extends Util {

    public SimpleClan(Cdsc plugin) {
        super(plugin);
    }
    private ClanPlayer clanPlayer;

    public SimpleClans getInstance() {
        Plugin simpleclan = plugin.getServer().getPluginManager().getPlugin("SimpleClans");
        if (!(simpleclan instanceof SimpleClans)) return null;
        return (SimpleClans)simpleclan;
    }

    public ClanPlayer LoadClan(Player player) {
        return getInstance().getClanManager().getClanPlayer(player);
    }
}

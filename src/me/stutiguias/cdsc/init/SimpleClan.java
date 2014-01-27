/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package me.stutiguias.cdsc.init;

import net.sacredlabyrinth.phaed.simpleclans.SimpleClans;
import org.bukkit.plugin.Plugin;

/**
 *
 * @author Daniel
 */
public class SimpleClan extends Util {

    public SimpleClan(Cdsc plugin) {
        super(plugin);
    }
        
    public SimpleClans Get() {
        
        Plugin simpleclan = plugin.getServer().getPluginManager().getPlugin("SimpleClans");

        // may not be loaded
        if (simpleclan == null || !(simpleclan instanceof SimpleClans)) {
            return null;
        }

        return (SimpleClans)simpleclan;
    }
}

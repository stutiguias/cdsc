/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package me.stutiguias.cdsc.listener;

import me.stutiguias.cdsc.init.Cdsc;
import me.stutiguias.cdsc.init.Util;
import org.bukkit.event.Listener;

/**
 *
 * @author Daniel
 */
public abstract class ListenerHandle extends Util implements Listener {

    public ListenerHandle(Cdsc plugin) {
        super(plugin);
    }
    
}

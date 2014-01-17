/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.stutiguias.cdsc.init;

import com.trc202.CombatTagApi.CombatTagApi;

/**
 *
 * @author Daniel
 */
public class CombatTag extends Util {

    public CombatTag(Cdsc plugin) {
        super(plugin);
    }

    public CombatTagApi Get() {
        if (plugin.getServer().getPluginManager().getPlugin("CombatTag") != null) {
            return new CombatTagApi((com.trc202.CombatTag.CombatTag) plugin.getServer().getPluginManager().getPlugin("CombatTag"));
        }
        return null;
    }
}

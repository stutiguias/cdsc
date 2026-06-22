package me.stutiguias.cdsc.commands;

import me.stutiguias.cdsc.init.Cdsc;
import me.stutiguias.cdsc.minigame.MiniGameManager;
import me.stutiguias.cdsc.model.Area;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetGameSpawnCommand extends CommandHandler {

    public SetGameSpawnCommand(Cdsc plugin) {
        super(plugin);
    }

    @Override
    protected Boolean OnCommand(CommandSender sender, String[] args) {
        this.sender = sender;
        if(isInvalid(sender, args)) return true;

        String role = args[1].toLowerCase();
        Area area = plugin.getArea(args[2]);
        if(area == null) {
            SendMessage("&4Area not found");
            return true;
        }

        Cdsc.miniGame.setSpawn(area, "*", role, ((Player)sender).getLocation());
        SendMessage("&6Mini-game %s spawn set for area %s.", new Object[] { role, area.getName() });
        return true;
    }

    @Override
    protected Boolean isInvalid(CommandSender sender, String[] args) {
        if(!(sender instanceof Player)) return true;
        if(!plugin.hasPermission((Player)sender,"cdsc.setgamespawn")) {
            SendMessage("&4You don't have permission");
            return true;
        }
        if(args.length < 3) {
            SendMessage("&4Wrong arguments. Use /cd setgamespawn <defender|attacker> <areaName>");
            return true;
        }
        String role = args[1].toLowerCase();
        if(!role.equals(MiniGameManager.ROLE_DEFENDER) && !role.equals(MiniGameManager.ROLE_ATTACKER)) {
            SendMessage("&4Role must be defender or attacker.");
            return true;
        }
        return false;
    }
}

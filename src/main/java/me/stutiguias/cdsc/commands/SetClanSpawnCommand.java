package me.stutiguias.cdsc.commands;

import me.stutiguias.cdsc.init.Cdsc;
import me.stutiguias.cdsc.minigame.MiniGameManager;
import me.stutiguias.cdsc.model.Area;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetClanSpawnCommand extends CommandHandler {

    public SetClanSpawnCommand(Cdsc plugin) {
        super(plugin);
    }

    @Override
    protected Boolean OnCommand(CommandSender sender, String[] args) {
        this.sender = sender;
        if(isInvalid(sender, args)) return true;

        Area area = plugin.getArea(args[1]);
        if(area == null) {
            SendMessage("&4Area not found");
            return true;
        }

        String clanTag = args[2].toLowerCase();
        String role = args.length > 3 ? args[3].toLowerCase() : roleFor(area, clanTag);
        Cdsc.miniGame.setSpawn(area, clanTag, role, ((Player)sender).getLocation());
        SendMessage("&6Mini-game spawn set for clan %s in area %s.", new Object[] { clanTag, area.getName() });
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
            SendMessage("&4Wrong arguments. Use /cd setclanspawn <areaName> <clanTag> [defender|attacker]");
            return true;
        }
        if(args.length > 3 && !args[3].equalsIgnoreCase(MiniGameManager.ROLE_DEFENDER) && !args[3].equalsIgnoreCase(MiniGameManager.ROLE_ATTACKER)) {
            SendMessage("&4Role must be defender or attacker.");
            return true;
        }
        return false;
    }

    private String roleFor(Area area, String clanTag) {
        if(area.getClanTag() != null && area.getClanTag().equalsIgnoreCase(clanTag)) {
            return MiniGameManager.ROLE_DEFENDER;
        }
        return MiniGameManager.ROLE_ATTACKER;
    }
}

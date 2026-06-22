package me.stutiguias.cdsc.commands;

import me.stutiguias.cdsc.init.Cdsc;
import me.stutiguias.cdsc.model.Area;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MiniGameJoinCommand extends CommandHandler {

    public MiniGameJoinCommand(Cdsc plugin) {
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

        if(Cdsc.miniGame.join((Player)sender, area)) {
            SendMessage("&6You joined the mini-game.");
            return true;
        }

        SendMessage("&4Could not join the mini-game. Check if the event is active and you have a clan.");
        return true;
    }

    @Override
    protected Boolean isInvalid(CommandSender sender, String[] args) {
        if(!(sender instanceof Player)) return true;
        if(!Cdsc.miniGame.isEnabled()) {
            SendMessage("&4Mini-game is disabled.");
            return true;
        }
        if(args.length < 2) {
            SendMessage("&4Wrong arguments. Use /cd join <areaName>");
            return true;
        }
        return false;
    }
}

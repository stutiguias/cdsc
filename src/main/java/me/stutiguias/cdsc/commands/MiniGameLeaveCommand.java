package me.stutiguias.cdsc.commands;

import me.stutiguias.cdsc.init.Cdsc;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MiniGameLeaveCommand extends CommandHandler {

    public MiniGameLeaveCommand(Cdsc plugin) {
        super(plugin);
    }

    @Override
    protected Boolean OnCommand(CommandSender sender, String[] args) {
        this.sender = sender;
        if(isInvalid(sender, args)) return true;

        if(Cdsc.miniGame.leave((Player)sender)) {
            SendMessage("&6You left the mini-game.");
            return true;
        }

        SendMessage("&4You are not playing the mini-game.");
        return true;
    }

    @Override
    protected Boolean isInvalid(CommandSender sender, String[] args) {
        if(!(sender instanceof Player)) return true;
        if(!Cdsc.miniGame.isEnabled()) {
            SendMessage("&4Mini-game is disabled.");
            return true;
        }
        return false;
    }
}

package me.stutiguias.cdsc.commands;

import me.stutiguias.cdsc.init.Cdsc;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MiniGameClassCommand extends CommandHandler {

    public MiniGameClassCommand(Cdsc plugin) {
        super(plugin);
    }

    @Override
    protected Boolean OnCommand(CommandSender sender, String[] args) {
        this.sender = sender;
        if(isInvalid(sender, args)) return true;

        String classId = args[1].toLowerCase();
        if(!Cdsc.miniGame.hasClass(classId)) {
            SendMessage("&4Class not found.");
            return true;
        }

        Cdsc.miniGame.setSelectedClass((Player)sender, classId);
        SendMessage("&6Mini-game class selected: &e%s", new Object[] { classId });
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
            SendMessage("&4Wrong arguments. Use /cd class <className>");
            return true;
        }
        return false;
    }
}

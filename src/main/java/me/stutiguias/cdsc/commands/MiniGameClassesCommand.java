package me.stutiguias.cdsc.commands;

import me.stutiguias.cdsc.init.Cdsc;
import me.stutiguias.cdsc.model.MiniGameClass;
import org.bukkit.command.CommandSender;

public class MiniGameClassesCommand extends CommandHandler {

    public MiniGameClassesCommand(Cdsc plugin) {
        super(plugin);
    }

    @Override
    protected Boolean OnCommand(CommandSender sender, String[] args) {
        this.sender = sender;
        if(isInvalid(sender, args)) return true;

        SendMessage(MsgHr);
        for(MiniGameClass miniGameClass:Cdsc.miniGame.getClasses()) {
            SendMessage("&6%s &e- &7%s", new Object[] { miniGameClass.getId(), miniGameClass.getDisplayName() });
        }
        SendMessage(MsgHr);
        return true;
    }

    @Override
    protected Boolean isInvalid(CommandSender sender, String[] args) {
        if(!Cdsc.miniGame.isEnabled()) {
            SendMessage("&4Mini-game is disabled.");
            return true;
        }
        return false;
    }
}

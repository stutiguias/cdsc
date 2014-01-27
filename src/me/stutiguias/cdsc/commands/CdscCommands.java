/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.stutiguias.cdsc.commands;

import java.util.HashMap;
import me.stutiguias.cdsc.init.Cdsc;
import me.stutiguias.cdsc.init.Util;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Daniel
 */
public class CdscCommands extends Util implements CommandExecutor {
       
    private final HashMap<String,CommandHandler> avaibleCommands;
    
    public CdscCommands(Cdsc plugin) {
        super(plugin);
        avaibleCommands = new HashMap<>();
        avaibleCommands.put("help", new HelpCommand(plugin));
        avaibleCommands.put("reload", new ReloadCommand(plugin));
        avaibleCommands.put("update", new UpdateCommand(plugin));
        avaibleCommands.put("wand", new WandCommand(plugin));
        avaibleCommands.put("define", new DefineCommand(plugin));
        avaibleCommands.put("delete",new DeleteCommand(plugin));
        avaibleCommands.put("teleport", new TeleportCommand(plugin));
        avaibleCommands.put("start", new StartEventCommand(plugin));
        avaibleCommands.put("end",new StopEventCommand(plugin));
        avaibleCommands.put("setcore", new SetCoreCommand(plugin));
        avaibleCommands.put("info", new InfoCommand(plugin));
        avaibleCommands.put("list", new ListCommand(plugin));
        avaibleCommands.put("setexit", new SetExitCommand(plugin));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        this.sender = sender;
        
        if (sender.getName().equalsIgnoreCase("CONSOLE")) return true;
        if (!(sender instanceof Player)) return false;
 
        if(args.length < 0 || args.length == 0) return CommandNotFound();
        
        String executedCommand = args[0].toLowerCase();

        if(avaibleCommands.containsKey(executedCommand))
            return avaibleCommands.get(executedCommand).OnCommand(sender,args);
        else
            return CommandNotFound();
        
    } 
    
    private boolean CommandNotFound() {
        SendMessage("&eCommand not found try /cd help ");
        return true;
    }

}

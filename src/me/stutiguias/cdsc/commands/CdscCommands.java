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
    private final HashMap<String,CommandHandler> avaibleConsoleCommands;
    
    public CdscCommands(Cdsc plugin) {
        super(plugin);
        avaibleCommands = new HashMap<>();
        avaibleConsoleCommands = new HashMap<>();
        
        HelpCommand helpCommand = new HelpCommand(plugin);
        WandCommand wandCommand = new WandCommand(plugin);
        DefineCommand defineCommand = new DefineCommand(plugin);
        TeleportCommand teleportCommand = new TeleportCommand(plugin);
        SetCoreCommand setCoreCommand = new SetCoreCommand(plugin);
        ListCommand listCommand = new ListCommand(plugin);
        SetExitCommand setExitCommand = new SetExitCommand(plugin);
        InfoCommand infoCommand = new InfoCommand(plugin);
        StartEventCommand startEventCommand = new StartEventCommand(plugin);
        StopEventCommand stopEventCommand = new StopEventCommand(plugin);
        
        avaibleCommands.put("help",     helpCommand);
        avaibleCommands.put("?",        helpCommand);
        
        avaibleCommands.put("reload",   new ReloadCommand(plugin));
        
        avaibleCommands.put("update",   new UpdateCommand(plugin));
        
        avaibleCommands.put("w",        wandCommand);
        avaibleCommands.put("wand",     wandCommand);
        
        avaibleCommands.put("d",        defineCommand);
        avaibleCommands.put("define",   defineCommand);
        
        avaibleCommands.put("delete",   new DeleteCommand(plugin));
        
        avaibleCommands.put("tp",       teleportCommand);
        avaibleCommands.put("teleport", teleportCommand);
        
        avaibleCommands.put("start",    new StartEventCommand(plugin));
        avaibleCommands.put("end",      new StopEventCommand(plugin));
        
        avaibleCommands.put("sc",       setCoreCommand);
        avaibleCommands.put("setcore",  setCoreCommand);
        
        avaibleCommands.put("i",        infoCommand);
        avaibleCommands.put("info",     infoCommand);

        avaibleCommands.put("l",        listCommand);
        avaibleCommands.put("list",     listCommand);
        
        avaibleCommands.put("se",       setExitCommand);
        avaibleCommands.put("setexit",  setExitCommand);
        
        avaibleCommands.put("spawn",    new SpawnCommand(plugin));
        
        avaibleConsoleCommands.put("start", startEventCommand);
        avaibleConsoleCommands.put("stop", stopEventCommand);
        
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        this.sender = sender;
        String executedCommand = args[0].toLowerCase();
        if(args.length < 0 || args.length == 0) return CommandNotFound();
        
        if (sender.getName().equalsIgnoreCase("CONSOLE")) {
            if(avaibleConsoleCommands.containsKey(executedCommand)) {
                return avaibleConsoleCommands.get(executedCommand).OnCommand(sender, args);
            }
            return CommandConsoleHelp();
        }
        
        if (!(sender instanceof Player)) return false;
 
        if(avaibleCommands.containsKey(executedCommand))
            return avaibleCommands.get(executedCommand).OnCommand(sender,args);
        else
            return CommandNotFound();
        
    } 
    
    private boolean CommandNotFound() {
        SendMessage("&eCommand not found try /cd help");
        return true;
    }

    private boolean CommandConsoleHelp() {
        SendMessage("&eOnly Start and Stop is avaible by console");
        return true;
    }    
    
}

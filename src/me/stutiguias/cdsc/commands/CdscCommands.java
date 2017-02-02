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
       
    private final HashMap<String,CommandHandler> availableCommands;
    private final HashMap<String,CommandHandler> availableConsoleCommands;
    
    public CdscCommands(Cdsc plugin) {
        super(plugin);
        availableCommands = new HashMap<>();
        availableConsoleCommands = new HashMap<>();
        
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
        SetFlagCommand setFlagCommand = new SetFlagCommand(plugin);
        DelFlagCommand delFlagCommand = new DelFlagCommand(plugin);
        
        availableCommands.put("help",     helpCommand);
        availableCommands.put("?",        helpCommand);
        
        availableCommands.put("reload",   new ReloadCommand(plugin));
        
        availableCommands.put("w",        wandCommand);
        availableCommands.put("wand",     wandCommand);
        
        availableCommands.put("d",        defineCommand);
        availableCommands.put("define",   defineCommand);
        
        availableCommands.put("dl",   new DeleteCommand(plugin));
        availableCommands.put("delete",   new DeleteCommand(plugin));
        
        availableCommands.put("tp",       teleportCommand);
        availableCommands.put("teleport", teleportCommand);
        
        availableCommands.put("start",    new StartEventCommand(plugin));
        availableCommands.put("end",      new StopEventCommand(plugin));
        
        availableCommands.put("sc",       setCoreCommand);
        availableCommands.put("setcore",  setCoreCommand);
        
        availableCommands.put("i",        infoCommand);
        availableCommands.put("info",     infoCommand);

        availableCommands.put("l",        listCommand);
        availableCommands.put("list",     listCommand);
        
        availableCommands.put("se",       setExitCommand);
        availableCommands.put("setexit",  setExitCommand);
        
        availableCommands.put("setflag",  setFlagCommand);
        availableCommands.put("sf",       setFlagCommand);
        
        availableCommands.put("delflag",  delFlagCommand);
        availableCommands.put("dlf",      delFlagCommand);
        
        availableCommands.put("spawn",    new SpawnCommand(plugin));
        
        availableConsoleCommands.put("start", startEventCommand);
        availableConsoleCommands.put("stop", stopEventCommand);
        
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        this.sender = sender;
        
        String executedCommand;
        if (args.length < 0 || args.length == 0) {
                executedCommand = "help";
        } else {
                executedCommand = args[0].toLowerCase();
        }
        
        if(args.length < 0 || args.length == 0) return CommandNotFound();
        
        if (sender.getName().equalsIgnoreCase("CONSOLE")) {
            if(availableConsoleCommands.containsKey(executedCommand)) {
                return availableConsoleCommands.get(executedCommand).OnCommand(sender, args);
            }
            return CommandConsoleHelp();
        }
        
        if (!(sender instanceof Player)) return false;
 
        if(availableCommands.containsKey(executedCommand))
            return availableCommands.get(executedCommand).OnCommand(sender,args);
        else
            return CommandNotFound();
        
    } 
    
    private boolean CommandNotFound() {
        SendMessage("&eCommand not found try /cd help");
        return true;
    }

    private boolean CommandConsoleHelp() {
        SendMessage("&eOnly Start and Stop is available by console");
        return true;
    }    
    
}

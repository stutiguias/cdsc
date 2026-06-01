package me.stutiguias.cdsc.commands;

import me.stutiguias.cdsc.init.Cdsc;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetAutoEventCommand extends CommandHandler {

    public SetAutoEventCommand(Cdsc plugin) {
        super(plugin);
    }

    @Override
    protected Boolean OnCommand(CommandSender sender, String[] args) {
        this.sender = sender;

        if(isInvalid(sender, args)) return true;

        String action = args[1].toLowerCase();

        if(action.equals("status")) {
            SendMessage("&6Auto-event: &e%s &6Interval: &e%s &6Duration: &e%s", new Object[] {
                    Cdsc.config.AutoEventEnabled ? "on" : "off",
                    Cdsc.config.AutoEventIntervalMinutes,
                    Cdsc.config.AutoEventDurationMinutes
            });
            return true;
        }

        if(action.equals("off")) {
            Cdsc.config.setAutoEvent(false, Cdsc.config.AutoEventIntervalMinutes, Cdsc.config.AutoEventDurationMinutes);
            plugin.restartAutoEventScheduler();
            SendMessage("&6Auto-event disabled.");
            return true;
        }

        int intervalMinutes = Integer.parseInt(args[2]);
        int durationMinutes = Integer.parseInt(args[3]);

        Cdsc.config.setAutoEvent(true, intervalMinutes, durationMinutes);
        plugin.restartAutoEventScheduler();
        SendMessage("&6Auto-event enabled. Interval: &e%s &6minutes. Duration: &e%s &6minutes.", new Object[] {
                intervalMinutes,
                durationMinutes
        });
        return true;
    }

    @Override
    protected Boolean isInvalid(CommandSender sender, String[] args) {
        if(sender instanceof Player && !plugin.hasPermission((Player)sender,"cdsc.setautoevent")) {
            SendMessage("&4You don't have permission");
            return true;
        }

        if(args.length < 2) {
            SendMessage("&4Wrong arguments. Use /cd setautoevent <on|off|status> [intervalMinutes] [durationMinutes]");
            return true;
        }

        String action = args[1].toLowerCase();
        if(action.equals("off") || action.equals("status")) return false;

        if(!action.equals("on")) {
            SendMessage("&4Wrong arguments. Use /cd setautoevent <on|off|status> [intervalMinutes] [durationMinutes]");
            return true;
        }

        if(args.length < 4) {
            SendMessage("&4Wrong arguments. Use /cd setautoevent on <intervalMinutes> <durationMinutes>");
            return true;
        }

        try {
            int intervalMinutes = Integer.parseInt(args[2]);
            int durationMinutes = Integer.parseInt(args[3]);
            if(intervalMinutes <= 0 || durationMinutes <= 0) {
                SendMessage("&4Interval and duration must be greater than zero.");
                return true;
            }
            if(durationMinutes >= intervalMinutes) {
                SendMessage("&4Duration must be lower than interval.");
                return true;
            }
        } catch(NumberFormatException ex) {
            SendMessage("&4Interval and duration must be numbers.");
            return true;
        }

        return false;
    }
}

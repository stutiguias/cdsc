package me.stutiguias.cdsc.init;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.stutiguias.cdsc.commands.CdscCommands;
import me.stutiguias.cdsc.configs.Config;
import me.stutiguias.cdsc.listener.PlayerListener;
import me.stutiguias.cdsc.listener.SignListener;
import me.stutiguias.cdsc.metrics.Metrics;
import me.stutiguias.cdsc.model.Area;
import me.stutiguias.cdsc.model.CDSCPlayer;
import me.stutiguias.cdsc.updater.Updater;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import net.sacredlabyrinth.phaed.simpleclans.SimpleClans;
import static org.bukkit.Bukkit.getServer;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class Cdsc extends JavaPlugin {
    
    public String prefix = "[CDSC] ";
    public static final Logger logger = Logger.getLogger("Minecraft");

    private final PlayerListener playerListener = new PlayerListener(this);
    private final SignListener SignListener = new SignListener(this);

    public static HashMap<Player,Area> AreaCreating;
    public static HashMap<String,CDSCPlayer> PlayerProfiles;
    
    public static List<Area> Areas;
    public static boolean EventOccurring;
    
    public Permission permission = null;
    public Economy economy = null;

    public static Config config;
    
    public static boolean update = false;
    public static String name = "";
    public static String type = "";
    public static String version = "";
    public static String link = "";

    @Override
    public void onEnable() {
               
        File dir = getDataFolder();
        if (!dir.exists()) {
          dir.mkdirs();
        }
        
        AreaCreating = new HashMap<>();
        PlayerProfiles = new HashMap<>();
        Areas = new ArrayList<>();
        config = new Config(this);
        
        
        
        PluginManager pm = getServer().getPluginManager();
        
        pm.registerEvents(playerListener, this);
        pm.registerEvents(SignListener,this);
        
        setupPermissions();
        setupEconomy();
        
        
        
        // Metrics 
        try {
         logger.log(Level.INFO, "{0} {1} - Sending Metrics, Thank You!", new Object[]{prefix, "[Metrics]"});
         Metrics metrics = new Metrics(this);
         metrics.start();
        } catch (IOException e) {
         logger.log(Level.WARNING, "{0} {1} !! Failed to submit the stats !! ", new Object[]{prefix, "[Metrics]"});
        }
       
        if(config.UpdaterNotify){
            Updater updater = new Updater(this, 49809, this.getFile(), Updater.UpdateType.NO_DOWNLOAD, false); // Start Updater but just do a version check
            
            update = updater.getResult() == Updater.UpdateResult.UPDATE_AVAILABLE; // Determine if there is an update ready for us
            name = updater.getLatestName(); // Get the latest name
            version = updater.getLatestGameVersion(); // Get the latest game version
            type = updater.getLatestType(); // Get the latest game version
            link = updater.getLatestFileLink(); // Get the latest link
        }
        
        getCommand("cd").setExecutor(new CdscCommands(this));
    }

    @Override
    public void onDisable() {
        getServer().getPluginManager().disablePlugin(this);
    }
    
    public void OnReload() {
        config.reloadConfig();
        getServer().getPluginManager().disablePlugin(this);
        getServer().getPluginManager().enablePlugin(this);
    }
    
    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        permission = rsp.getProvider();
        return permission != null;
    }

    private Boolean setupEconomy() {
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(Economy.class);
        if (economyProvider != null) {
                economy = economyProvider.getProvider();
        }
        return (economy != null);
    }
 
    public String parseColor(String message) {
         for (ChatColor color : ChatColor.values()) {
            message = message.replaceAll(String.format("&%c", color.getChar()), color.toString());
        }
        return message;
    }
     
    public void Update() {
        Updater updater = new Updater(this, 49809, this.getFile(), Updater.UpdateType.NO_VERSION_CHECK, true);
    }
    
    public boolean hasPermission(String PlayerName,String Permission) {
       return permission.has(getServer().getPlayer(PlayerName).getWorld(),PlayerName,Permission);
    }
    
    public boolean hasPermission(Player player, String Permission) {
        return permission.has(player.getWorld(), player.getName(), Permission.toLowerCase());
    }        
    
    public long getCurrentMilli() {
            return System.currentTimeMillis();
    }
    
    public SimpleClans getSimpleClan() {
        
        Plugin plugin = getServer().getPluginManager().getPlugin("SimpleClans");

        // may not be loaded
        if (plugin == null || !(plugin instanceof SimpleClans)) {
            return null;
        }

        return (SimpleClans) plugin;
    }
    
    public int getAreaIndex(Location location) {
        for (int i = 0; i < Cdsc.Areas.size(); i++) {
            
            Area area = Cdsc.Areas.get(i);
            
            double fx = area.getFirstSpot().getX();
            double fz = area.getFirstSpot().getZ();
            
            double sx = area.getSecondSpot().getX();
            double sz = area.getSecondSpot().getZ();

            double x,x2,z,z2;

            if(fx > sx) {
                 x = fx;
                 x2 = sx;
            } else {
                 x = sx;
                 x2 = fx;    
            }

            if(sz > fz) {
               z = sz;
               z2 = fz;
            } else {
               z = fz; 
               z2 = sz;
            }

            if(isInsideProtection(x, x2, z, z2, location)) {
                return i;
            }                
        }
        return 9999;
    }

    public Area getArea(Location location) {
         int index = getAreaIndex(location);
         if(index == 9999) return null;
         return Areas.get(index);
    }
    
    public boolean isInsideProtection(double x,double x2,double z,double z2,Location location) {
       return location.getX() <= x && location.getX() >= x2 && location.getZ() <= z && location.getZ() >= z2;
    }
}
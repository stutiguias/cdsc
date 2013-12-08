package me.stutiguias.cdsc.init;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.stutiguias.cdsc.commands.CdscCommands;
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
import org.bukkit.configuration.file.FileConfiguration;
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
    
    public Permission permission = null;
    public Economy economy = null;

    private ConfigAccessor config;

    public boolean UpdaterNotify;
    
    public static boolean update = false;
    public static String name = "";
    public static String type = "";
    public static String version = "";
    public static String link = "";

    @Override
    public void onEnable() {
        
        AreaCreating = new HashMap<>();
        PlayerProfiles = new HashMap<>();
        Areas = new ArrayList<>();
        Load();
        
        getCommand("cd").setExecutor(new CdscCommands(this));
        
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
       
        if(UpdaterNotify){
            Updater updater = new Updater(this, 49809, this.getFile(), Updater.UpdateType.NO_DOWNLOAD, false); // Start Updater but just do a version check
            
            update = updater.getResult() == Updater.UpdateResult.UPDATE_AVAILABLE; // Determine if there is an update ready for us
            name = updater.getLatestName(); // Get the latest name
            version = updater.getLatestGameVersion(); // Get the latest game version
            type = updater.getLatestType(); // Get the latest game version
            link = updater.getLatestFileLink(); // Get the latest link
        }
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
    
    private void Load(){

        try {
            config = new ConfigAccessor(this, "config.yml");
            config.setupConfig();
            FileConfiguration fc = config.getConfig();   
                        
            if(!fc.isSet("configversion") || fc.getInt("configversion") != 1){ 
                config.MakeOld();
                config.setupConfig();
                fc = config.getConfig();
                
                UpdaterNotify = fc.getBoolean("UpdaterNotify");
            }
             
        }catch(IOException ex){
            getLogger().log(Level.WARNING, "Erro Loading Config");
        }
         
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

        // WorldGuard may not be loaded
        if (plugin == null || !(plugin instanceof SimpleClans)) {
            return null; // Maybe you want throw an exception instead
        }

        return (SimpleClans) plugin;
    }
    
}
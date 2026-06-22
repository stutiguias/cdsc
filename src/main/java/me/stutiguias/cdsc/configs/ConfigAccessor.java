/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.stutiguias.cdsc.configs;

/**
 *
 * @author Daniel
 */
import java.io.*;
import java.util.logging.Level;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class ConfigAccessor {

    private final String fileName;
    private final JavaPlugin plugin;
    
    private File configFile;
    private FileConfiguration fileConfiguration;

    public ConfigAccessor(JavaPlugin plugin, String fileName) {
        if (!plugin.isEnabled()) {
            throw new IllegalArgumentException("plugin must be initialized");
        }
        this.plugin = plugin;
        this.fileName = fileName;
    }

    public void setupConfig() throws IOException {
        configFile = new File(plugin.getDataFolder(), fileName);
        
        if(!configFile.exists()) {
            configFile.createNewFile();
            copy(plugin.getResource(fileName), configFile);
        }

        fileConfiguration = null;
    }
    
    private void copy(java.io.InputStream input, File file) {
        try {
            OutputStream out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while((len=input.read(buf))>0){
                out.write(buf,0,len);
            }
            out.close();
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void reloadConfig() {
        if (configFile == null) {
            File dataFolder = plugin.getDataFolder();
            if (dataFolder == null) {
                throw new IllegalStateException();
            }
            configFile = new File(dataFolder, fileName);
        }
        fileConfiguration = YamlConfiguration.loadConfiguration(configFile);

        // Look for defaults in the jar
        InputStream defConfigStream = plugin.getResource(fileName);

        if (defConfigStream != null) {
            Reader reader = new InputStreamReader(defConfigStream);
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(reader);
            fileConfiguration.setDefaults(defConfig);
        }
    }

    public FileConfiguration getConfig() {
        if (fileConfiguration == null) {
            this.reloadConfig();
        }
        return fileConfiguration;
    }

    public void saveConfig() {
        if (fileConfiguration == null || configFile == null) {
        } else {
            try {
                getConfig().save(configFile);
            } catch (IOException ex) {
                plugin.getLogger().log(Level.SEVERE, "Could not save config to " + configFile, ex);
            }
        }
    }
    
    public void saveDefaultConfig() {
        if (!configFile.exists()) {            
            this.plugin.saveResource(fileName, false);
        }
    }

    public boolean hasDifferentConfigVersion(String versionPath) {
        FileConfiguration currentConfig = getConfig();
        InputStream defConfigStream = plugin.getResource(fileName);

        if (defConfigStream == null) {
            return false;
        }

        try (Reader reader = new InputStreamReader(defConfigStream)) {
            YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(reader);

            if (!defaultConfig.isSet(versionPath)) {
                return false;
            }

            if (!currentConfig.contains(versionPath, true)) {
                return true;
            }

            return currentConfig.getInt(versionPath) != defaultConfig.getInt(versionPath);
        } catch (IOException ex) {
            plugin.getLogger().log(Level.WARNING, "Could not check config version for " + fileName, ex);
            return false;
        }
    }

    public boolean updateKeepingCompatibleValues(String versionPath) throws IOException {
        if (!hasDifferentConfigVersion(versionPath)) {
            return false;
        }

        File oldFile = new File(plugin.getDataFolder(), fileName + "_old");

        if (!MakeOld()) {
            return false;
        }

        setupConfig();

        FileConfiguration oldConfig = YamlConfiguration.loadConfiguration(oldFile);
        FileConfiguration newConfig = getConfig();

        for (String key:oldConfig.getKeys(true)) {
            if (key.equalsIgnoreCase(versionPath) || oldConfig.isConfigurationSection(key)) {
                continue;
            }

            if (newConfig.contains(key, true)) {
                newConfig.set(key, oldConfig.get(key));
            }
        }

        saveConfig();
        reloadConfig();
        return true;
    }
    
    public boolean MakeOld() {
        File file = new File(plugin.getDataFolder(),fileName + "_old");
        file.delete();
        return configFile.renameTo(new File(plugin.getDataFolder(),fileName + "_old"));
    }
}

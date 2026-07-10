package me.stutiguias.cdsc.minigame;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import me.stutiguias.cdsc.init.Cdsc;
import me.stutiguias.cdsc.model.Area;
import me.stutiguias.cdsc.model.CastleDefencePlayer;
import me.stutiguias.cdsc.model.MiniGameClass;
import me.stutiguias.cdsc.model.MiniGamePlayer;
import me.stutiguias.cdsc.model.MiniGameSession;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

public class MiniGameManager {

    public static final String ROLE_DEFENDER = "defender";
    public static final String ROLE_ATTACKER = "attacker";
    private static final String DEFAULT_CLAN = "*";

    private final Cdsc plugin;
    private final Map<String, MiniGameSession> sessions = new HashMap<>();
    private final Map<UUID, String> playerAreas = new HashMap<>();
    private final Map<UUID, String> selectedClasses = new HashMap<>();
    private final Map<String, Map<String, Location>> spawnCache = new HashMap<>();

    public MiniGameManager(Cdsc plugin) {
        this.plugin = plugin;
    }

    public boolean isEnabled() {
        return Cdsc.config != null && Cdsc.config.MiniGameEnabled;
    }

    public void start(Area area) {
        if(!isEnabled() || area == null) return;
        if(sessions.containsKey(area.getName().toLowerCase())) return;
        loadSpawnCache(area);
        sessions.put(area.getName().toLowerCase(), new MiniGameSession(area));
    }

    public void end(Area area) {
        if(!isEnabled() || area == null) return;
        MiniGameSession session = sessions.remove(area.getName().toLowerCase());
        if(session == null) return;

        for(String playerId:new ArrayList<>(session.getPlayers().keySet())) {
            Player player = plugin.getServer().getPlayer(UUID.fromString(playerId));
            if(player != null) {
                restorePlayer(player, session.removePlayer(playerId), true);
            }
            playerAreas.remove(UUID.fromString(playerId));
        }
    }

    public void endAll() {
        if(!isEnabled()) return;
        for(String areaName:new ArrayList<>(sessions.keySet())) {
            MiniGameSession session = sessions.get(areaName);
            if(session != null) {
                end(session.getArea());
            }
        }
    }

    public boolean join(Player player, Area area) {
        if(!isEnabled() || player == null || area == null) return false;
        MiniGameSession session = sessions.get(area.getName().toLowerCase());
        if(session == null) return false;
        if(playerAreas.containsKey(player.getUniqueId())) return false;

        String clanTag = getClanTag(player);
        if(clanTag == null) return false;

        String classId = selectedClasses.containsKey(player.getUniqueId())
                ? selectedClasses.get(player.getUniqueId())
                : Cdsc.config.MiniGameDefaultClass.toLowerCase();
        MiniGameClass miniGameClass = Cdsc.config.MiniGameClasses.get(classId);
        if(miniGameClass == null) {
            miniGameClass = firstClass();
        }
        if(miniGameClass == null) return false;

        MiniGamePlayer miniGamePlayer = new MiniGamePlayer(
                player.getUniqueId().toString(),
                clanTag,
                miniGameClass.getId(),
                player.getInventory().getContents(),
                player.getInventory().getArmorContents(),
                player.getLocation(),
                new ArrayList<>(player.getActivePotionEffects()));

        session.addPlayer(miniGamePlayer);
        playerAreas.put(player.getUniqueId(), area.getName().toLowerCase());
        applyClass(player, miniGameClass);

        Location spawn = getSpawn(area, clanTag, getRole(area, clanTag));
        if(spawn != null) {
            player.teleport(spawn);
        }
        return true;
    }

    public boolean leave(Player player) {
        if(!isEnabled() || player == null) return false;
        String areaName = playerAreas.remove(player.getUniqueId());
        if(areaName == null) return false;

        MiniGameSession session = sessions.get(areaName);
        if(session == null) return false;

        MiniGamePlayer miniGamePlayer = session.removePlayer(player.getUniqueId().toString());
        restorePlayer(player, miniGamePlayer, true);
        return true;
    }

    public boolean isPlaying(Player player) {
        return isEnabled() && player != null && playerAreas.containsKey(player.getUniqueId());
    }

    public Location getRespawnLocation(Player player, Area fallbackArea) {
        if(!isPlaying(player)) return null;
        String areaName = playerAreas.get(player.getUniqueId());
        MiniGameSession session = sessions.get(areaName);
        Area area = session != null ? session.getArea() : fallbackArea;
        if(area == null) return null;

        MiniGamePlayer miniGamePlayer = session != null ? session.getPlayers().get(player.getUniqueId().toString()) : null;
        String clanTag = miniGamePlayer != null ? miniGamePlayer.getClanTag() : getClanTag(player);
        if(clanTag == null) return null;
        return getSpawn(area, clanTag, getRole(area, clanTag));
    }

    public void respawn(Player player) {
        if(!isPlaying(player)) return;
        String areaName = playerAreas.get(player.getUniqueId());
        MiniGameSession session = sessions.get(areaName);
        if(session == null) return;
        MiniGamePlayer miniGamePlayer = session.getPlayers().get(player.getUniqueId().toString());
        if(miniGamePlayer == null) return;
        MiniGameClass miniGameClass = Cdsc.config.MiniGameClasses.get(miniGamePlayer.getClassId());
        if(miniGameClass != null) {
            applyClass(player, miniGameClass);
        }
    }

    public void setSelectedClass(Player player, String classId) {
        if(player == null || classId == null) return;
        selectedClasses.put(player.getUniqueId(), classId.toLowerCase());
    }

    public Collection<MiniGameClass> getClasses() {
        return Cdsc.config.MiniGameClasses.values();
    }

    public boolean hasClass(String classId) {
        return classId != null && Cdsc.config.MiniGameClasses.containsKey(classId.toLowerCase());
    }

    public boolean setSpawn(Area area, String clanTag, String role, Location spawn) {
        if(area == null || clanTag == null || role == null || spawn == null) return false;
        String normalizedClanTag = clanTag.toLowerCase();
        String normalizedRole = role.toLowerCase();
        if(!Cdsc.db.SetMiniGameSpawn(area.getName(), normalizedClanTag, normalizedRole, spawn)) return false;

        Map<String, Location> spawns = spawnCache.get(areaKey(area));
        if(spawns == null) {
            spawns = new HashMap<>();
            spawnCache.put(areaKey(area), spawns);
        }
        spawns.put(spawnKey(normalizedRole, normalizedClanTag), spawn);
        return true;
    }

    private Location getSpawn(Area area, String clanTag, String role) {
        Map<String, Location> spawns = spawnCache.get(areaKey(area));
        if(spawns == null) {
            spawns = new HashMap<>();
        }
        Location clanSpawn = spawns.get(spawnKey(role, clanTag));
        if(clanSpawn != null) return clanSpawn;

        Location roleSpawn = spawns.get(spawnKey(role, DEFAULT_CLAN));
        if(roleSpawn != null) return roleSpawn;

        if(area.getSpawn() != null) return area.getSpawn();
        if(area.getExit() != null) return area.getExit();
        return area.getFirstSpot();
    }

    private String getRole(Area area, String clanTag) {
        if(area.getClanTag() != null && area.getClanTag().equalsIgnoreCase(clanTag)) {
            return ROLE_DEFENDER;
        }
        return ROLE_ATTACKER;
    }

    private String getClanTag(Player player) {
        CastleDefencePlayer castleDefencePlayer = new CastleDefencePlayer(plugin);
        castleDefencePlayer.SetPlayer(player);
        if(!castleDefencePlayer.haveCla()) return null;
        return castleDefencePlayer.getClaTag();
    }

    private void applyClass(Player player, MiniGameClass miniGameClass) {
        player.getInventory().clear();
        player.getInventory().setArmorContents(new ItemStack[4]);
        for(ItemStack item:miniGameClass.getItems()) {
            player.getInventory().addItem(item);
        }
        for(PotionEffect effect:player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }
        for(PotionEffect effect:miniGameClass.getEffects()) {
            player.addPotionEffect(effect);
        }
        player.updateInventory();
    }

    private void restorePlayer(Player player, MiniGamePlayer miniGamePlayer, boolean teleportBack) {
        if(player == null || miniGamePlayer == null) return;
        player.getInventory().setContents(miniGamePlayer.getPreviousInventory());
        player.getInventory().setArmorContents(miniGamePlayer.getPreviousArmor());
        for(PotionEffect effect:player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }
        for(PotionEffect effect:miniGamePlayer.getPreviousEffects()) {
            player.addPotionEffect(effect);
        }
        if(teleportBack && miniGamePlayer.getPreviousLocation() != null) {
            player.teleport(miniGamePlayer.getPreviousLocation());
        }
        player.updateInventory();
    }

    private MiniGameClass firstClass() {
        for(MiniGameClass miniGameClass:Cdsc.config.MiniGameClasses.values()) {
            return miniGameClass;
        }
        return null;
    }

    private void loadSpawnCache(Area area) {
        spawnCache.put(areaKey(area), Cdsc.db.GetMiniGameSpawns(area.getName()));
    }

    private String areaKey(Area area) {
        return area.getName().toLowerCase();
    }

    private String spawnKey(String role, String clanTag) {
        return role.toLowerCase() + ":" + clanTag.toLowerCase();
    }
}

package me.stutiguias.cdsc.model;

import java.util.HashMap;
import java.util.Map;

public class MiniGameSession {

    private final Area area;
    private final long startedAt;
    private final Map<String, MiniGamePlayer> players = new HashMap<>();

    public MiniGameSession(Area area) {
        this.area = area;
        this.startedAt = System.currentTimeMillis();
    }

    public Area getArea() {
        return area;
    }

    public long getStartedAt() {
        return startedAt;
    }

    public Map<String, MiniGamePlayer> getPlayers() {
        return players;
    }

    public boolean hasPlayer(String playerId) {
        return players.containsKey(playerId);
    }

    public void addPlayer(MiniGamePlayer player) {
        players.put(player.getPlayerId(), player);
    }

    public MiniGamePlayer removePlayer(String playerId) {
        return players.remove(playerId);
    }
}

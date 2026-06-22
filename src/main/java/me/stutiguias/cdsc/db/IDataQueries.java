/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.stutiguias.cdsc.db;

import me.stutiguias.cdsc.db.connection.WALConnection;
import java.util.List;
import java.util.Map;
import me.stutiguias.cdsc.model.Area;
import org.bukkit.Location;

/**
 *
 * @author Daniel
 */
public interface IDataQueries {

        void initTables(); // Init Tables
        Integer getFound(); // Found On Last Search
        WALConnection getConnection();
        
        boolean     InsertArea(Area area);
        boolean     UpdateArea(Area area);
        boolean     Delete(Area area);
        boolean     SetExit(Area area);
        boolean     SetCore(Area area);
        boolean     SetSpawn(Area area);
        boolean     SetFlag(Area area);
        boolean     SetMiniGameSpawn(String areaName, String clanTag, String role, Location spawn);
        Map<String, Location> GetMiniGameSpawns(String areaName);
        List<Area>  GetAreas();
        void        close();
        
}

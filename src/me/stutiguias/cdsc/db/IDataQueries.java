/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.stutiguias.cdsc.db;

import me.stutiguias.cdsc.db.connection.WALConnection;
import java.util.List;
import me.stutiguias.cdsc.model.Area;

/**
 *
 * @author Daniel
 */
public interface IDataQueries {

        void initTables(); // Init Tables
        Integer getFound(); // Found On Last Search
        WALConnection getConnection();
        
        boolean InsertArea(Area area);
        List<Area> getAreas();
        boolean SetCore(Area area);
        boolean Delete(Area area);
        boolean UpdateArea(Area area);
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.stutiguias.cdsc.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import me.stutiguias.cdsc.init.Cdsc;
import me.stutiguias.cdsc.model.Area;

/**
 *
 * @author Daniel
 */
public class Queries implements IDataQueries {
        
    protected Cdsc plugin;
    protected WALConnection connection;
    protected Integer found;
    
    public Queries(Cdsc plugin) {
        this.plugin = plugin;
    }

    @Override
    public void initTables() {
        throw new UnsupportedOperationException("Implement On Children.");
    }

    @Override
    public Integer getFound() {
        return found;
    }
        
    @Override
    public WALConnection getConnection() {
        throw new UnsupportedOperationException("Implement On Children.");
    }
    
    public void closeResources(WALConnection conn, Statement st, ResultSet rs) {
        if (null != rs) {
            try {
                rs.close();
            } catch (SQLException e) {
            }
        }
        if (null != st) {
            try {
                st.close();
            } catch (SQLException e) {
            }
        }
        if (null != conn) conn.close();
    }
        
    public int tableVersion() {
        int version = 0;
        WALConnection conn = getConnection();
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
                st = conn.prepareStatement("SELECT dbversion FROM WA_DbVersion");
                rs = st.executeQuery();
                while (rs.next()) {
                        version = rs.getInt("dbversion");
                }
        } catch (SQLException e) {
                Cdsc.logger.log(Level.WARNING, "{0} Unable to check if table version ", plugin.prefix);
                Cdsc.logger.warning(e.getMessage());
        } finally {
                closeResources(conn, st, rs);
        }
        return version;
    }
    
    public void executeRawSQL(String sql) {
        WALConnection conn = getConnection();
        Statement st = null;
        ResultSet rs = null;

        try {
            st = conn.createStatement();
            st.executeUpdate(sql);
        } catch (SQLException e) {
            Cdsc.logger.log(Level.WARNING, "{0} Exception executing raw SQL {1}", new Object[]{plugin.prefix, sql});
            Cdsc.logger.warning(e.getMessage());
        } finally {
            closeResources(conn, st, rs);
        }
    }

    @Override
    public boolean InsertArea(Area area) {
        return true;
    }

    @Override
    public List<Area> getAreas() {
        List<Area> areas = new ArrayList<>();
        
        return areas;
    }
    
}

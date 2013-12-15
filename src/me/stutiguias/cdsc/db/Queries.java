/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.stutiguias.cdsc.db;

import me.stutiguias.cdsc.db.connection.WALConnection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import me.stutiguias.cdsc.init.Cdsc;
import me.stutiguias.cdsc.init.Util;
import me.stutiguias.cdsc.model.Area;

/**
 *
 * @author Daniel
 */
public class Queries extends Util implements IDataQueries {
        
    protected WALConnection connection;
    protected Integer found;
    
    public Queries(Cdsc plugin) {
        super(plugin);
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
        WALConnection conn = getConnection();
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
                st = conn.prepareStatement("INSERT INTO CDSC_Areas (name, first, second, core, corelife, clantag, flags) VALUES (?,?,?,?,?,?,?)");
                st.setString(1, area.getName());
                st.setString(2, ToString(area.getFirstSpot()));
                st.setString(3, ToString(area.getSecondSpot()));
                st.setString(4, ToString(area.getCoreLocation()));
                st.setInt(5, area.getCoreLife());
                st.setString(6, area.getClanTag());
                st.setString(7, area.getFlags());
                st.executeUpdate();
        } catch (SQLException e) {
                Cdsc.logger.log(Level.WARNING, "{0} Unable to insert area", plugin.prefix);
                Cdsc.logger.warning(e.getMessage());
        } finally {
                closeResources(conn, st, rs);
        }
        return true;
    }

    @Override
    public List<Area> getAreas() {
        List<Area> areas = new ArrayList<>();
                
        WALConnection conn = getConnection();
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
                st = conn.prepareStatement("SELECT * FROM CDSC_Areas");
                rs = st.executeQuery();
                while (rs.next()) {
                        Area area = new Area();
                        area.setName(rs.getString("name"));
                        area.setCoreLife(rs.getInt("corelife"));
                        area.setCoreLocation(toLocation(rs.getString("core")));
                        area.setFirstSpot(toLocation(rs.getString("first")));
                        area.setSecondSpot(toLocation(rs.getString("second")));
                        area.setFlags(rs.getString("flags"));
                        area.setClanTag(rs.getString("clantag"));
                        areas.add(area);
                }
        } catch (SQLException e) {
                Cdsc.logger.log(Level.WARNING, "{0} Unable to get areas", new Object[]{plugin.prefix});
                Cdsc.logger.warning(e.getMessage());
        } finally {
                closeResources(conn, st, rs);
        }
        return areas;
    }

    @Override
    public boolean SetCore(Area area) {
        WALConnection conn = getConnection();
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
                st = conn.prepareStatement("UPDATE CDSC_Areas SET core = ? WHERE name = ?");
                st.setString(1, ToString(area.getCoreLocation()));
                st.setString(2, area.getName() );
                st.executeUpdate();
        } catch (SQLException e) {
                Cdsc.logger.log(Level.WARNING, "{0} Unable to update DB", plugin.prefix);
                Cdsc.logger.warning(e.getMessage());
        } finally {
                closeResources(conn, st, rs);
        }
        return true;
    }

    @Override
    public boolean Delete(Area area) {
        WALConnection conn = getConnection();
        PreparedStatement st = null;
        ResultSet rs = null;
        int result = 0;
        
        try {
                st = conn.prepareStatement("DELETE FROM CDSC_Areas WHERE name = ?");
                st.setString(1, area.getName());
                result = st.executeUpdate();
        } catch (SQLException e) {
                Cdsc.logger.log(Level.WARNING, "{0} Unable to delete", new Object[]{plugin.prefix});
        } finally {
                closeResources(conn, st, rs);
        }
        return result != 0;
    }

    @Override
    public boolean UpdateArea(Area area) {
        WALConnection conn = getConnection();
        PreparedStatement st = null;
        ResultSet rs = null;
        int result = 0;
        try {
                st = conn.prepareStatement("UPDATE CDSC_Areas SET first = ?, second = ?, core = ?, corelife = ?, clantag = ?, flags = ? WHERE name = ?");
                st.setString(1, ToString(area.getFirstSpot()) );
                st.setString(2, ToString(area.getSecondSpot()) );
                st.setString(3, ToString(area.getCoreLocation()) );
                st.setInt(4, area.getCoreLife() );
                st.setString(5, area.getClanTag() );
                st.setString(6, area.getFlags() );
                st.setString(7, area.getName() );
                result = st.executeUpdate();
        } catch (SQLException e) {
                Cdsc.logger.log(Level.WARNING, "{0} Unable to update DB", plugin.prefix);
                Cdsc.logger.warning(e.getMessage());
        } finally {
                closeResources(conn, st, rs);
        }
        return result != 0;
    }
    
}

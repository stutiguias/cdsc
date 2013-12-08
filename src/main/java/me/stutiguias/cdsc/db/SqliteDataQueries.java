/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.stutiguias.cdsc.db;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.logging.Level;
import me.stutiguias.cdsc.init.Cdsc;

/**
 *
 * @author Daniel
 */
public class SqliteDataQueries extends Queries {

    public SqliteDataQueries(Cdsc plugin) {
        super(plugin);
    }

    @Override
    public WALConnection getConnection() {
            try {
                    Driver driver = (Driver) Class.forName("org.sqlite.JDBC").newInstance();
                    WALDriver jDriver = new WALDriver(driver);
                    DriverManager.registerDriver(jDriver);
                    connection = new WALConnection(DriverManager.getConnection("jdbc:sqlite:" + plugin.getDataFolder() + File.separator + "data.db"));
                    return connection;
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | SQLException e) {
                    Cdsc.logger.log(Level.SEVERE, "{0} Exception getting SQLite WALConnection", plugin.prefix);
                    Cdsc.logger.warning(e.getMessage());
            }
            return null;
    }
	
    private boolean tableExists(String tableName) {
        boolean exists = false;
        WALConnection conn = getConnection();
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
                st = conn.prepareStatement("SELECT name FROM sqlite_master WHERE type = 'table' and name LIKE ?");
                st.setString(1, tableName);
                rs = st.executeQuery();
                while (rs.next()) {
                        exists = true;
                }
        } catch (SQLException e) {
                Cdsc.logger.log(Level.WARNING, "{0} Unable to check if table exists: {1}", new Object[]{plugin.prefix, tableName});
                Cdsc.logger.warning(e.getMessage());
        } finally {
                closeResources(conn, st, rs);
        }
        return exists;
    }

    @Override
    public void initTables() {
                File dbFile = new File(plugin.getDataFolder() + File.separator +  "data.db");
                if(!dbFile.exists()) {
                    try {
                        dbFile.createNewFile();
                    } catch (IOException ex) {
                        Cdsc.logger.log(Level.WARNING,"{0} Can`t create file db", plugin.prefix);
                    }
                }
                if (!tableExists("CDSC_Players")) {
			Cdsc.logger.log(Level.INFO, "{0} Creating table CDSC_Players", plugin.prefix);
			executeRawSQL("CREATE TABLE CDSC_Players (id INTEGER PRIMARY KEY, name VARCHAR(255), pass VARCHAR(255), money DOUBLE, itemsSold INTEGER, itemsBought INTEGER, earnt DOUBLE, spent DOUBLE, canBuy INTEGER, canSell INTEGER, isAdmin INTEGER);");
		}
		if (!tableExists("CDSC_Areas")) {
			Cdsc.logger.log(Level.INFO, "{0} Creating table CDSC_Areas", plugin.prefix);
			executeRawSQL("CREATE TABLE CDSC_Areas (id INTEGER PRIMARY KEY, name INTEGER, damage INTEGER, player VARCHAR(255), quantity INTEGER, price DOUBLE, created INTEGER, ench VARCHAR(45), tableid INTEGER(1));");
		}
                if (!tableExists("CDSC_DbVersion")) {
                        Cdsc.logger.log(Level.INFO, "{0} Creating table CDSC_DbVersion", plugin.prefix);
                        executeRawSQL("CREATE TABLE CDSC_DbVersion (id INTEGER PRIMARY KEY, dbversion INTEGER);");
                        executeRawSQL("INSERT INTO CDSC_DbVersion (dbversion) VALUES (1)");
                }
    }
    
}

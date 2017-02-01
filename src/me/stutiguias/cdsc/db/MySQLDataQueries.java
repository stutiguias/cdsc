package me.stutiguias.cdsc.db;

import me.stutiguias.cdsc.db.connection.WALConnectionPool;
import me.stutiguias.cdsc.db.connection.WALConnection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import me.stutiguias.cdsc.init.Cdsc;

public class MySQLDataQueries extends Queries {

        private WALConnectionPool pool;
        
	public MySQLDataQueries(Cdsc plugin, String dbHost, String dbPort, String dbUser, String dbPass, String dbName) {
		super(plugin);
                try {
                        Cdsc.logger.log(Level.INFO, "{0} Starting pool....", plugin.prefix);
                        pool = new WALConnectionPool("com.mysql.jdbc.Driver", "jdbc:mysql://"+ dbHost +":"+ dbPort +"/"+ dbName, dbUser, dbPass);
                }catch(InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
                        Cdsc.logger.log(Level.WARNING, "{0} Exception getting mySQL WALConnection", plugin.prefix);
			Cdsc.logger.warning(e.getMessage());
                }
                initTables();
	}

        @Override
	public WALConnection getConnection() {
		try {
			return pool.getConnection();
		} catch (SQLException e) {
			Cdsc.logger.log(Level.WARNING, "{0} Exception getting mySQL WALConnection", plugin.prefix);
			Cdsc.logger.warning(e.getMessage());
		}
		return null;
	}
        
	public boolean tableExists(String tableName) {
		boolean exists = false;
		WALConnection conn = getConnection();
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SHOW TABLES LIKE ?");
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
	public final void initTables() {
		if (!tableExists("CDSC_Players")) {
			Cdsc.logger.log(Level.INFO, "{0} Creating table CDSC_Players", plugin.prefix);
			executeRawSQL("CREATE TABLE CDSC_Players (id INT NOT NULL AUTO_INCREMENT, PRIMARY KEY(id), name VARCHAR(255), banned INT);");
		}
		if (!tableExists("CDSC_Areas")) {
			Cdsc.logger.log(Level.INFO, "{0} Creating table CDSC_Areas", plugin.prefix);
			executeRawSQL("CREATE TABLE CDSC_Areas (id INT NOT NULL AUTO_INCREMENT, PRIMARY KEY(id), name VARCHAR(255), first VARCHAR(255), second VARCHAR(255), core VARCHAR(255), corelife INT, clantag VARCHAR(255), flags VARCHAR(255) );");
		}
                if (!tableExists("CDSC_DbVersion")) {
                        Cdsc.logger.log(Level.INFO, "{0} Creating table CDSC_DbVersion", plugin.prefix);
                        executeRawSQL("CREATE TABLE CDSC_DbVersion (id INT NOT NULL AUTO_INCREMENT, PRIMARY KEY(id), dbversion INT);");
                        executeRawSQL("INSERT INTO CDSC_DbVersion (dbversion) VALUES (1)");
                }                
                if (tableVersion() == 1) {
                        Cdsc.logger.log(Level.INFO, "{0} Update DB version to 2", plugin.prefix);
                        executeRawSQL("ALTER TABLE CDSC_Areas ADD COLUMN `exit` VARCHAR(255) AFTER `flags` ");
                        executeRawSQL("UPDATE CDSC_DbVersion SET dbversion = 2 where id = 1");
                }
                if (tableVersion() == 2) {
                        Cdsc.logger.log(Level.INFO, "{0} Update DB version to 3", plugin.prefix);
                        executeRawSQL("ALTER TABLE CDSC_Areas ADD COLUMN `spawn` VARCHAR(255) AFTER `exit` ");
                        executeRawSQL("UPDATE CDSC_DbVersion SET dbversion = 3 where id = 1");
                }
                if (tableVersion() == 3) {
                        Cdsc.logger.log(Level.INFO, "{0} Update DB version to 4", plugin.prefix);
                        executeRawSQL("ALTER TABLE CDSC_Areas ADD COLUMN `world` VARCHAR(255) AFTER `spawn` ");
                        executeRawSQL("UPDATE CDSC_DbVersion SET dbversion = 4 where id = 1");
                }
	}
    
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.stutiguias.cdsc.db;

/**
 *
 * @author Daniel
 */
public interface IDataQueries {

        void initTables(); // Init Tables
        Integer getFound(); // Found On Last Search
        WALConnection getConnection();
}

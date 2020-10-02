/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.dialmedu.wooap.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.sql.DataSource;
import javax.swing.JOptionPane;
import org.apache.commons.dbcp.BasicDataSource;

/**
 *
 * @author Servicio al Cliente
 */
public class PoolManager {
    static int maxEnable = 15;
    static int minEnable = 8;
    static ArrayList<Connection> poolEnable = new ArrayList();
    static ArrayList<Connection> poolActive = new ArrayList();
    private static BasicDataSource basicDataSource;
    static DataSource dataSource;
    
    public static Connection getConnection(Conexion db) throws SQLException{
        if( basicDataSource == null){
           createPoolConections(db);
        }
        try{
            if(basicDataSource.getNumActive() >= maxEnable-1 || (basicDataSource.getNumActive() == 0 && basicDataSource.getNumIdle() == 0)){
                createPoolConections(db);
            }
            printDataSourceStats(basicDataSource);
            return basicDataSource.getConnection();
        } catch(Exception ex){
            JOptionPane.showMessageDialog(null, "No se puedo realizar la conexion");
        }
        return null;
    }
    
    public static void printDataSourceStats(BasicDataSource bds) {
        System.out.println("NumActive: " + bds.getNumActive());
        System.out.println("NumIdle: " + bds.getNumIdle());
     }
    
    public static void close() throws SQLException{
        if(basicDataSource != null){
           basicDataSource.close();
        }
    }
    
    private static void createPoolConections(Conexion db) throws SQLException{
        basicDataSource = new BasicDataSource();
        basicDataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        basicDataSource.setUsername(db.getUser());
        basicDataSource.setPassword(db.getPass());
        basicDataSource.setUrl("jdbc:mysql://"+db.getUrl()+ "?rewriteBatchedStatements=true&useCompression=true&serverTimezone=UTC");
        basicDataSource.setMaxActive(maxEnable);
        basicDataSource.setMaxIdle(minEnable);
        // Opcional. Sentencia SQL que le puede servir a BasicDataSource
        // para comprobar que la conexion es correcta.
    }
}

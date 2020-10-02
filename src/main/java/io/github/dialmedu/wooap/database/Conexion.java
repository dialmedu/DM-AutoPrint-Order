/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.dialmedu.wooap.database;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import static java.lang.System.out;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author diego
 */
public class Conexion {
    
    String url = "";
    String user = "";
    String pass = "";
    private Statement st;
    /**
     * Variable que contiene la conexion.
     */
    private Connection con;
    private PreparedStatement preparedStatement = null;
    
    public ResultSet Consultar(String sql) {
        try {
            st = con.createStatement();
            ResultSet resultado = st.executeQuery(sql);
            return resultado;
        } catch (Exception e) {
            System.out.println("Log database: Error " + e + "\nSQL: " + sql);
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Ejecuto una consulta tipo INSERT, UPDATE o DELETE en la base de datos.
     *
     * @param sql la consulta en lenguaje SQL
     * @return correcto: si la consulta se ha realizado false: Si la consulta no se
     * realizo
     */
    public String Ejecutar(String sql) {
        try {
            st = con.createStatement();
            st.execute(sql);
            return "correcto";

        } catch (Exception e) {
            System.out.println("Log database: Error " + e + "\nSQL: " + sql);
            e.printStackTrace();
            return e.toString();
        }
    }

    public boolean setStatemet(){
         try {
           con = PoolManager.getConnection(this);
           if(con != null){
                st = con.createStatement();
                return true;
            }
        } catch (SQLException e) {
            st = null;
            out.println("Error conexion database: " + e);
        }
        return false;
    }
    
    /**
     * Cierra la conexion actual con la base de datos.
     */
    public void cerrar_conexion() {
        try {
            if(st != null && st.isClosed() == false){
                st.close();
            }
            if(con != null && con.isClosed() == false){
                con.close(); 
            }
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null,
                    ex);
        }
    }
    
    public Conexion Leer() {
        File archivo = null;
        FileReader fr = null;
        BufferedReader br = null;

        try {
            // Apertura del fichero y creacion de BufferedReader para poder
            // hacer una lectura comoda (disponer del metodo readLine()).
            archivo = new File("source/db.config");
            if (!archivo.exists()) {
                archivo.createNewFile();
                return this;
            }
            fr = new FileReader(archivo);
            br = new BufferedReader(fr);

            // Lectura del fichero
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] l = linea.split("=");
                try {
                    switch (l[0].toUpperCase()) {
                        case "URL":
                            this.url = l[1];
                            break;
                        case "USR":
                            this.user = l[1];
                            break;
                        case "PSS":
                            this.pass = l[1];
                            break;
                        default:
                            break;
                    }
                } catch (Exception e) {
                    //puede generar una excepcion de fuera de rango
                    //por el split
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // En el finally cerramos el fichero, para asegurarnos
            // que se cierra tanto si todo va bien como si salta 
            // una excepcion.
            try {
                if (null != fr) {
                    fr.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return this;
    }
    
    public String getUrl() {
        return url;
    }

    public String getUser() {
        return user;
    }

    public String getPass() {
        return pass;
    }
    
    public Connection getCon() {
        return con;
    }
}

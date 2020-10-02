/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.dialmedu.wooap.autoprint;

import io.github.dialmedu.wooap.database.Conexion;
import io.github.dialmedu.wooap.jaspert.ManagerReportJaspert;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author diego
 */
public class Controller {
    
    public static Conexion getConexion(){
        return new Conexion().Leer();
    }
    
    public static int totalPrinted(){
        int printed = 0;
        Conexion con = getConexion();
        if(con.setStatemet()){
           String sql = "SELECT COUNT(id) as total FROM `pedidos` WHERE pedidos.printed IS TRUE";
           ResultSet response = con.Consultar(sql);
            try {
                if(response.next()){
                    printed = response.getInt("total");
                }
            } catch (SQLException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return printed;
    }
    
    public static boolean activar(){
        boolean response = false;
        Conexion con = getConexion();
        if(con.setStatemet()){
           String sql = "SELECT * FROM `pedidos` WHERE pedidos.printed IS FALSE";
           ResultSet pedidos = con.Consultar(sql);
            try {
                ManagerReportJaspert.MESSAGE_ERROR = "";
                while(pedidos.next()){
                   response = true; 
                   printPedido("PedidoReporte.jasper",
                           pedidos.getString("pedido_numero"),
                           pedidos.getString("cliente_nombre"),
                           pedidos.getDouble("cantidad"));
                }
                String sql_update = "UPDATE `pedidos` SET printed = TRUE  WHERE pedidos.printed IS FALSE";
                con.Ejecutar(sql_update);
            } catch (SQLException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return response;
    }
    
    public static void printPedido(String reporteFile,String pedido, String nombre, double cantidad){
        if(pedido.isEmpty()){
            return;
        }
        Map parametros =  new HashMap();
        parametros.put("pedido_numero", pedido);
        parametros.put("cantidad", nombre);
        parametros.put("cliente_nombre", String.valueOf(cantidad));
        ManagerReportJaspert.createReport(reporteFile,parametros,"PedidoReporte",true,true);
    }
    
}

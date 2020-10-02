/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.dialmedu.wooap.jaspert;

import io.github.dialmedu.wooap.database.Conexion;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author diego
 */
public class ManagerReportJaspert {
    
    private static final String MESSAGE_FILENOEXIST =  "<html>No se cuentra con este Reporte en su Sistema </html>";
    public static String MESSAGE_ERROR = ""; 
    public static ArrayList<String> working = new ArrayList();
    private static String sourceFolder = "soruce";
    
    public static void showMessageFileNoExist(String nameReport){
        MESSAGE_ERROR += nameReport;
        JOptionPane.showMessageDialog(null, nameReport + "\n" +MESSAGE_FILENOEXIST , "Mesaje", JOptionPane.WARNING_MESSAGE);
    }
    
    /**
     * Buscar el archivo de *.jaspert , crea un JasperPrint pasando el 
     * Map de parametros y el objeto Connection segun db.config, si no encuentra el archivo
     *  *.jaspert retorna null
     * @param reporteFile
     * @param parametros
     * @return JaspertPrint or null if file no exist
     */
    public static JasperPrint getJaspertPrintFile(String reporteFile, Map parametros){
        Conexion con = new Conexion();
        con.setStatemet();
        JasperPrint jasperPrint  = null;
        java.util.Locale locale = new Locale( "es", "CO" );
        parametros.put( JRParameter.REPORT_LOCALE, locale );
        JasperReport reporte = getJaspertReportFile(reporteFile);
        if(reporte != null){
            try {
                jasperPrint = JasperFillManager.fillReport(reporte, parametros, con.getCon()); 
            } catch (JRException ex) {
                Logger.getLogger(ManagerReportJaspert.class.getName()).log(Level.SEVERE, null, ex);
                MESSAGE_ERROR += ex.getMessage();
            }
        }else{
            jasperPrint  = null;
        }
        return  jasperPrint;
    }

     private static JasperReport getJaspertReportFile(String nameJaspertFile){
        File archivo = getJaspertFile(nameJaspertFile);
        JasperReport reporte = null;
        if ( archivo != null ) {
            try {
                reporte = (JasperReport) JRLoader.loadObject(archivo);
            } catch (JRException ex) {
                reporte = null;
                Logger.getLogger(ManagerReportJaspert.class.getName()).log(Level.SEVERE, null, ex);
                MESSAGE_ERROR += ex.getMessage();
            }
        }
        return reporte;
    }
     
    private static File getJaspertFile(String nameJaspertFile){
        File archivo;
        archivo = new File(sourceFolder+File.pathSeparator+nameJaspertFile);
        if (archivo.exists()) {
            return archivo;
        }
        return null;
    }
    
    public static void viewReport(JasperPrint jPrint,String title){
        JasperViewer jv = new JasperViewer(jPrint, false);
        jv.setAlwaysOnTop(true);
        jv.setTitle(title);
        jv.setVisible(true);
    }
    
    public static void printReport(JasperPrint jPrint, boolean selectPrinter){
        try {
            JasperPrintManager.printReport(jPrint, selectPrinter);
        } catch (JRException ex) {
            Logger.getLogger(ManagerReportJaspert.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void createReport(String reporteFile,Map parametros,String title, boolean show, boolean print){
        createReport(reporteFile,parametros,title,show,print,false);   
    }
    
    private static void createReport(String reporteFile,Map parametros,String title, boolean show, boolean print, boolean selectPrinter){
        String nameWork = reporteFile+"_"+Calendar.getInstance().getTimeInMillis();
        ManagerReportJaspert.working.add(nameWork);
        Loader cargando = new Loader(new ManagerReportCreate(reporteFile,parametros,title,show,print,selectPrinter));
        //cargando.setTextOperation(title);
        cargando.start();
        cargando.setVisible(true);
        ManagerReportJaspert.working.remove(nameWork);
    }
    
    public static boolean isWorking(){
        return ManagerReportJaspert.working.size() > 0;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.dialmedu.wooap.jaspert;

import java.util.Map;
import net.sf.jasperreports.engine.JasperPrint;

/**
 *
 * @author dialmedu
 */
public class ManagerReportCreate extends LoaderRunnable {
    
    String reporteFile;
    Map parametros;
    String reportTitle = "Reporte";
    boolean show = true;
    boolean print = false;
    boolean selectPrinter = false;
    
    protected boolean ejecutando;
    long inicio;
    double time = 0;
    
    String _TEXT_CREATE_REPORT = "Construyendo Reporte";
    String _TEXT_SHOW_REPORT = "Visualizando Reporte";
    String _TEXT_PRINT_REPORT = "Imprimiendo Reporte";
    String _TEXT_LONG_PROCESS = "Espere un momento más... ";
    String _TEXT_SHOW_LONG_PROCESS = "Atencion, Proceso Extenso";
    
    public ManagerReportCreate(String reporteFile,Map parametros,String reportTitle, boolean show, boolean print,boolean selectPrinter ){
        this.reporteFile = reporteFile;
        this.parametros = parametros;
        this.reportTitle = reportTitle;
        this.operation =  "Creando "+ this.reportTitle;
        this.show = show;
        this.print = print;
        this.selectPrinter = selectPrinter;
    }
    
    @Override
    public void run() {
        
        ejecutando = true;
        this.watchTime();
        
        this.resumen = this._TEXT_CREATE_REPORT;
        JasperPrint jasperPrint = ManagerReportJaspert.getJaspertPrintFile(this.reporteFile,this.parametros);
        if(jasperPrint != null){
            if(this.show){
                this.resumen = this._TEXT_PRINT_REPORT;
                ManagerReportJaspert.viewReport(jasperPrint,this.reportTitle);
            }
            if(this.print){
                this.resumen = this._TEXT_PRINT_REPORT;
                ManagerReportJaspert.printReport(jasperPrint,this.selectPrinter);
            }
        }else{
            ManagerReportJaspert.showMessageFileNoExist(this.reporteFile);
        }
        ejecutando = false;
    }
    
    protected void watchTime(){
        Thread hilo = new Thread(new Runnable() {
            @Override
            public void run() {
                ManagerReportCreate.this.inicio = System.currentTimeMillis();
                while (ejecutando) {
                    time = (ManagerReportCreate.this.inicio - System.currentTimeMillis()) / 100;
                    String messageTime = "\n Tiempo: "+time +" Seg";
                    if (time > 30) {
                        ManagerReportCreate.this.operation = _TEXT_LONG_PROCESS + messageTime;
                    }
                    if (time > 50 ) {
                        ManagerReportCreate.this.operation = _TEXT_SHOW_LONG_PROCESS + messageTime;
                    }
                }
            }
        });
        hilo.start();
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.dialmedu.wooap.autoprint;

import com.formdev.flatlaf.FlatDarculaLaf;

/**
 *
 * @author diego
 */
public class Main {
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
         FlatDarculaLaf.install();
         Update frame = new Update();
         frame.setVisible(true);
        // TODO code application logic here
    }
    
}

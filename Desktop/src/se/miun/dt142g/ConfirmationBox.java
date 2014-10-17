/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.miun.dt142g;

import java.awt.Component;
import javax.swing.JOptionPane;

/**
 *
 * @author Ali Omran
 */
public class ConfirmationBox {
    
    static public int confirm(Component a, String s){
        int n = JOptionPane.showConfirmDialog(a, "Ta bort " + s + "?", "Confirm", JOptionPane.YES_NO_OPTION);
        return n;
    }
    
}

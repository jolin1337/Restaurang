package se.miun.dt142g;

import java.awt.Component;
import javax.swing.JOptionPane;

/**
 * A confimation of when you remove an item in any view we have
 * @author Ali Omran
 */
public class ConfirmationBox {
    
    static public int confirm(Component a, String s){
        int n = JOptionPane.showConfirmDialog(a, "Ta bort " + s + "?", "Confirm", JOptionPane.YES_NO_OPTION);
        return n;
    }
    
}

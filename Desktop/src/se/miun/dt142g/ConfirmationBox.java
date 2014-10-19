package se.miun.dt142g;

import java.awt.Component;
import javax.swing.JOptionPane;

/**
 * A confimation of when you remove an item in any view we have
 *
 * @author Ali Omran
 */
public class ConfirmationBox {

    /**
     * Displays a confirmation box witch displays an userfriendly message when 
     * user wants to remove an item.
     * @param a - The component to attach this confirmationbox to
     * @param s - The identifier of the object to remove
     * @return 
     */
    static public int confirm(Component a, String s) {
        int n = JOptionPane.showConfirmDialog(a, "Ta bort " + s + "?", "Confirm", JOptionPane.YES_NO_OPTION);
        return n;
    }

}

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
    static public int confirm(Component a, String msg) {
        String[] options = new String[2];
        options[0] = new String("Ja");
        options[1] = new String("Nej");
        String title = "Bekräfta";
        int n = JOptionPane.showOptionDialog(a, msg, title, 0, JOptionPane.QUESTION_MESSAGE, null, options, null);
//                JOptionPane.showOptionDialog(frame.getContentPane(),"Message!","Title", 0,JOptionPane.INFORMATION_MESSAGE,null,options,null);
        return n;
    }

}

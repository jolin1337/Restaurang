/*
 * Title:
 * Subject: 
 */

package miun.dt142g.bookings;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import miun.dt142g.data.Booking;
//import com.michaelbaranov.microba.calendar.DatePicker;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyVetoException;
import java.util.Date;
/**
 *
 * @author Simple
 */
public class NewBooking extends JPanel {
    Booking booking=null;
    JButton addBookingBtn = new JButton("Godkänn bokning");

    private JLabel addLabel(String labelName) {
        JLabel label = new JLabel("<html><div style='margin: 10px 0 3px 3px;'>" + labelName + "</div></html>");
        Box  fixHeight = Box.createHorizontalBox();
        fixHeight.add( label );
        fixHeight.add( Box.createHorizontalGlue() );
        add(fixHeight); 
        return label;
    }
    private JTextField addTextField(String textName) {
        JTextField textField = new JTextField(textName);
        textField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        add(textField);  
        return textField;
    }
    public final void newBooking(Booking b){
        removeAll();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        addLabel("Namn: ");
        addTextField("Anders Svensson");
        
        addLabel("Antal personer: ");
        addTextField("");
        
        addLabel("Antal personer: ");
        addTextField("");
        
        addLabel("Starttid: ");
        addTextField("");
        
        addLabel("Varaktighet: ");
        addTextField("");
        
        addLabel("Datum: ");
        add(Box.createRigidArea(new Dimension(0, 10)));

        //final DatePicker datePicker = new DatePicker(new Date());
        //datePicker.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        //this.add(datePicker);
                
        add(Box.createRigidArea(new Dimension(0, 10)));
        addBookingBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        add(addBookingBtn);
        
        addBookingBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
               // NewBooking.this.add(new BookingPanel(new Booking(b.getUniqueId(), "", "1/3-37", 0, 0, 0)));
            }
        });
    }
}

/*
 * Title:
 * Subject: 
 */

package miun.dt142g.bookings;

import java.awt.Dimension;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import miun.dt142g.data.Booking;
import com.michaelbaranov.microba.calendar.DatePicker;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
/**
 *
 * @author Simple
 */
public class NewBooking extends JPanel {
    Booking booking=null;
    JButton addBookingBtn = new JButton("Godkänn bokning");
    JTextField nameField;
    JTextField personsField;
    JTextField timeField;
    JTextField durationField;
    private JLabel missingBookingInput;
    private JLabel invalidBookingInput;

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
        this.booking = b;
        removeAll();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        
        addLabel("Namn: ");
        nameField = addTextField("Anders Svensson");
        
        addLabel("Antal personer: ");
        personsField = addTextField("");
        
        addLabel("Starttid: ");
        timeField = addTextField("");
                
        addLabel("Varaktighet: ");
        durationField = addTextField("");
        
        addLabel("Datum: ");
        add(Box.createRigidArea(new Dimension(0, 10)));
        
        
        final DatePicker datePicker = new DatePicker(new Date());
        datePicker.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        this.add(datePicker);
                
        add(Box.createRigidArea(new Dimension(0, 10)));
        addBookingBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        add(addBookingBtn);
        
        missingBookingInput = addLabel("Vänligen fyll i alla fält för att godkänna bokningen.");
        invalidBookingInput = addLabel("Vänligen ange endast heltal.");
        add(missingBookingInput);
        add(invalidBookingInput);
        missingBookingInput.setVisible(false);
        invalidBookingInput.setVisible(false);
        
        addBookingBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                missingBookingInput.setVisible(false);
                if (nameField.getText().isEmpty() || personsField.getText().isEmpty() || durationField.getText().isEmpty() || timeField.getText().isEmpty()){
                    missingBookingInput.setVisible(true);
                    return;
                }
                if (!( isInteger(personsField.getText()) && isInteger(durationField.getText()) && isInteger(timeField.getText())  )){
                    invalidBookingInput.setVisible(true);
                    return;
                }
                booking.setName(nameField.getText());
                booking.setPersons(Integer.parseInt(personsField.getText()));
                booking.setTime(Integer.parseInt(timeField.getText()));
                booking.setDuration(Integer.parseInt(durationField.getText()));
                booking.setDate(datePicker.getDate());
                
                if(ae.getSource().getClass() != JButton.class)
                    return;
                JButton btn = (JButton)ae.getSource();
                if(btn.getParent() == null)
                    return;
                Container parent = btn.getParent().getParent();
                parent.remove(btn.getParent());
                parent.revalidate();
            }
        });
    }
    private boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
}

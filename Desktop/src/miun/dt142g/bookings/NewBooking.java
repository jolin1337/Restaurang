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
import java.util.Calendar;
import java.util.Date;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import miun.dt142g.Controller;
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
    JTextField phoneNrField; 
    private JLabel missingBookingInput;
    private JLabel invalidBookingInput;
    private JLabel invalidPhoneInput;
    private JLabel phoneNrInput; 
    private JLabel invalidDateInput;
    private JSpinner spinner;
    Date bookingTime;
    Controller remote = null;

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
    public final void newBooking(Booking b, Controller c){
        this.remote = c;
        this.booking = b;
        removeAll();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        
        addLabel("Namn: ");
        nameField = addTextField("");
        
        addLabel("Telefon: ");
        phoneNrField = addTextField("");
        
        addLabel("Antal personer: ");
        personsField = addTextField("");
        
        /* Time spinner */
        addLabel("Tid: ");
        SpinnerDateModel model = new SpinnerDateModel();
        model.setCalendarField(Calendar.MINUTE);
        spinner = new JSpinner();
        spinner.setModel(model);
        spinner.setEditor(new JSpinner.DateEditor(spinner, "h:mm a"));
        add(spinner);
        /* Time spinner */
        
        
        
        addLabel("Datum: ");
        add(Box.createRigidArea(new Dimension(0, 10)));
        
        final DatePicker datePicker = new DatePicker(new Date());
        datePicker.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        this.add(datePicker);
        
        addLabel("Varaktighet: ");
        durationField = addTextField("");
                
        add(Box.createRigidArea(new Dimension(0, 10)));
        addBookingBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        add(addBookingBtn);
        
        missingBookingInput = addLabel("Vänligen fyll i alla fält för att godkänna bokningen.");
        invalidBookingInput = addLabel("Vänligen ange endast heltal.");
        invalidPhoneInput = addLabel("Vänligen ange ett telefonnummer som är mellan 7 och 9 siffror");
        invalidDateInput = addLabel("Vänligen ange ett datum som inte är i det förflutna.");
        add(missingBookingInput);
        add(invalidBookingInput);
        add(invalidPhoneInput);
        invalidPhoneInput.setVisible(false);
        missingBookingInput.setVisible(false);
        invalidBookingInput.setVisible(false);
        invalidDateInput.setVisible(false);
        
        addBookingBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                missingBookingInput.setVisible(false);

                Object date = spinner.getValue();
                bookingTime = new Date();
                bookingTime = (Date)date;
                boolean invalidInput = false;
                
                if (nameField.getText().isEmpty() || personsField.getText().isEmpty() || durationField.getText().isEmpty() || phoneNrField.getText().isEmpty()){
                    missingBookingInput.setVisible(true);
                    invalidInput = true;
                }
                
                if (!( phoneNrField.getText().length() > 6 && phoneNrField.getText().length() < 10 )){
                    invalidPhoneInput.setVisible(true);
                    invalidInput = true;
                }
                
                if (!( isInteger(personsField.getText()) && isInteger(durationField.getText()) && isInteger(phoneNrField.getText()) )){
                    invalidBookingInput.setVisible(true);
                    invalidInput = true;
                }
                
                // Invalid input if date before "now" + 1 hour
                if (datePicker.getDate().getTime() < new Date().getTime()){
                    invalidDateInput.setVisible(true);
                    invalidInput = true;
                }
                
                if (invalidInput){
                    return;
                }
                
                
                // Merge datePicker with timePicker
                booking.setName(nameField.getText());
                booking.setPersons(Integer.parseInt(personsField.getText()));
                booking.setPhoneNr(Integer.parseInt(phoneNrField.getText()));
                bookingTime.setYear(datePicker.getDate().getYear());
                bookingTime.setMonth(datePicker.getDate().getMonth());
                bookingTime.setDate(datePicker.getDate().getDate());

                booking.setDate(bookingTime);
                booking.setDuration(Integer.parseInt(durationField.getText()));
                
                if(ae.getSource().getClass() != JButton.class)
                    return;
                JButton btn = (JButton)ae.getSource();
                if(btn.getParent() == null)
                    return;
                Container parent = btn.getParent().getParent();
                parent.remove(btn.getParent());
                parent.revalidate();
                remote.setViewBookings();
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

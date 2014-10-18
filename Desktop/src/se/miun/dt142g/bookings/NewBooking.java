/*
 * Title:
 * Subject: 
 */

package se.miun.dt142g.bookings;

import java.awt.Dimension;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import se.miun.dt142g.data.Booking;
import com.michaelbaranov.microba.calendar.DatePicker;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.JFormattedTextField;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.SpinnerDateModel;
import javax.swing.border.Border;
import se.miun.dt142g.Controller;

/**
 * The class to prompt user for a new booking
 * @author Simple
 */
public class NewBooking extends JPanel  {
    private Booking booking=null;
    private final JButton addBookingBtn = new JButton("Godkänn bokning");
    private JTextField nameField;
    private JTextField personsField;
    private JTextField timeField;
    private JTextField durationField;
    private JTextField phoneNrField; 
    private JTextArea errorMessagesTextArea;
    private final String newLine = "\n";
    private String missingBookingInput;
    private String invalidNumberInput;
    private String phoneNrInput; 
    private String invalidDateInput;
    private JSpinner spinner;
    private Date bookingTime = new Date();
    private Controller remote = null;

    public NewBooking(){
        setBackground(Color.white);
    }
    
    public final void newBooking(Booking b, Controller c){
        this.remote = c;
        this.booking = b;
        removeAll();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        
//        addLabel("Namn: ");
        nameField = addTextField("");
        nameField.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, Color.white), BorderFactory.createTitledBorder("Namn:")));
        
//        addLabel("Telefon: ");
        phoneNrField = addTextField("");
        phoneNrField.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, Color.white), BorderFactory.createTitledBorder("Telefon:")));
        
//        addLabel("Antal personer: ");
        personsField = addTextField("");
        personsField.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, Color.white), BorderFactory.createTitledBorder("Antal personer:")));
        
        /* Time spinner */
//        addLabel("Tid: ");
        SpinnerDateModel model = new SpinnerDateModel();
        model.setCalendarField(Calendar.MINUTE);
        spinner = new JSpinner();

        spinner.setModel(model);
        spinner.setEditor(new JSpinner.DateEditor(spinner, "HH:mm"));
        spinner.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, Color.white), BorderFactory.createTitledBorder("Antal personer:")));
        add(spinner);
        /* Time spinner */
        
        
        
//        addLabel("Datum: ");
        add(Box.createRigidArea(new Dimension(0, 10)));

        
        Border bbb = BorderFactory.createTitledBorder("Antal personer:");
        final DatePicker datePicker = new DatePicker(new Date());
        datePicker.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        datePicker.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, Color.white), BorderFactory.createTitledBorder("Antal personer:")));

        this.add(datePicker);
//        addLabel("Varaktighet i timmar: ");
        durationField = addTextField("");
        durationField.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, Color.white), BorderFactory.createTitledBorder("Varaktighet i timmar:")));
                
        add(Box.createRigidArea(new Dimension(0, 10)));
        addBookingBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        add(addBookingBtn);
        
        missingBookingInput = "Vänligen fyll i alla fält för att godkänna bokningen.";
        invalidNumberInput = "Vänligen ange endast heltal i personer fältet och varaktighet fältet.";
        invalidDateInput = "Vänligen ange ett datum som inte är i det förflutna.";
        errorMessagesTextArea = new JTextArea();
        errorMessagesTextArea.setEditable(false);
        
        add(errorMessagesTextArea);
        errorMessagesTextArea.setVisible(false);
        
        addBookingBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                errorMessagesTextArea.setText("");
                errorMessagesTextArea.setVisible(false);

                bookingTime = (Date)spinner.getValue();
                boolean invalidInput = false;
                
                if (nameField.getText().isEmpty() || personsField.getText().isEmpty() || durationField.getText().isEmpty() || phoneNrField.getText().isEmpty()){
                    errorMessagesTextArea.append(missingBookingInput + newLine);
                    invalidInput = true;
                }
                
                if (!( isInteger(personsField.getText()) && isInteger(durationField.getText()) )){
                    errorMessagesTextArea.append(invalidNumberInput + newLine);
                    invalidInput = true;
                }
                
                // Invalid input if choosen date & time is earlier than the present date and time
                mergeDateWithTime(bookingTime, datePicker.getDate());
                if (bookingTime.getTime() < new Date().getTime()){
                    errorMessagesTextArea.append(invalidDateInput + newLine);
                    invalidInput = true;
                }
                
                if (invalidInput){
                    errorMessagesTextArea.setVisible(true);
                    return;
                }
                
                
                // Merge datePicker with timePicker
                booking.setName(nameField.getText());
                booking.setPersons(Integer.parseInt(personsField.getText()));
                booking.setPhoneNr(phoneNrField.getText());

                booking.setDate(bookingTime);
                booking.setDuration(Integer.parseInt(durationField.getText()));
                
                if(ae.getSource().getClass() != JButton.class)
                    return;
                JButton btn = (JButton)ae.getSource();
                if(btn.getParent() == null)
                    return;
                Container parent = btn.getParent().getParent();
                remote.setViewBookings();
                parent.revalidate();
            }
        });
    }
    
    /**
    * Merges d1's "calendar" with d2's time.
    * @param d1 Date with time
    * @param d2 Date with year, month and day
    * @return return value saved in d1. 
    * 
    */
    private void mergeDateWithTime(Date d1, Date d2){
        d1.setYear(d2.getYear());
        d1.setMonth(d2.getMonth());
        d1.setDate(d2.getDate());
    }
    
    /**
    * Verifies whether a string is an integer or alphabetic
    * @param s String to verify
    * @return returns true if the string is an integer
    */
    private boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
    
    /**
    * Creates a formatted JLabel instance with the specified text. 
    * @param labelName
    * @return returns the JLabel
    */
    private JLabel addLabel(String labelName) {
        JLabel label = new JLabel("<html><div style='margin: 10px 0 3px 3px;'>" + labelName + "</div></html>");
        Box fixHeight = Box.createHorizontalBox();
        fixHeight.add( label );
        fixHeight.add( Box.createHorizontalGlue() );
        add(fixHeight); 
        return label;
    }
    
    /**
    * Creates a formatted JTextField instance with the specified text. 
    * @param textName 
    * @return returns the JTextField
    */
    private JTextField addTextField(String textName) {
        JTextField textField = new JTextField(textName);
        textField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        add(textField);  
        return textField;
    }
}

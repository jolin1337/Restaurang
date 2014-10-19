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
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.SpinnerDateModel;
import javax.swing.border.Border;
import se.miun.dt142g.Controller;
import se.miun.dt142g.Settings;

/**
 * The class guides the user of this application to create a new booking
 *
 * @author Simple
 */
public class NewBooking extends JPanel {

    /**
     * The booking source object to save the information to
     */
    private Booking booking = null;
    /**
     * A button for confirmation
     */
    private final JButton addBookingBtn = new JButton("Godkänn bokning");
    /**
     * Textfield to input the name of the person to reservate a table
     */
    private JTextField nameField;
    /**
     * Textfield to input the amount of persons 
     */
    private JTextField personsField;
    /**
     * Textfield to input the time of the booking takes place
     */
    private JTextField timeField;
    /**
     * This tels us how long they are booking a table
     */
    private JTextField durationField;
    /**
     * The phone number field for the user reference
     */
    private JTextField phoneNrField;
    /**
     * Some place to place errors that might happend if the user puts wrong text 
     * in a field
     */
    private JTextArea errorMessagesTextArea;
    /**
     * A newLine character
     */
    private final String newLine = "\n";
    /**
     * The string displaying if the user missed some field to input
     */
    private final String missingBookingInput = "Vänligen fyll i alla fält för att godkänna bokningen.";
    /**
     * The string displaying if the user typed some characters instead of numbers
     */
    private final String invalidNumberInput = "Vänligen ange endast heltal i personer fältet och varaktighet fältet.";
    /**
     * The string displaying if the user filled in the date wrong
     */
    private final String invalidDateInput = "Vänligen ange ett datum som inte är i det förflutna.";
    /**
     * The spinner for the time when the booking takes place
     */
    private JSpinner spinner;
    private Date bookingTime = new Date();
    private Controller remote = null;

    /**
     * Constructs this object
     */
    public NewBooking() {
        setBackground(Settings.Styles.applicationBgColor);
    }
    
    /**
     * This is called every time we want a new booking to be added
     * @param b - The booking we can alter
     * @param c - The remote to switch view
     */
    public final void newBooking(Booking b, Controller c) {
        this.remote = c;
        this.booking = b;
        removeAll();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        nameField = addTextField("");
        nameField.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, Color.white), BorderFactory.createTitledBorder("Namn:")));

        phoneNrField = addTextField("");
        phoneNrField.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, Color.white), BorderFactory.createTitledBorder("Telefon:")));

        personsField = addTextField("");
        personsField.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, Color.white), BorderFactory.createTitledBorder("Antal personer:")));

        /* Time spinner */
        SpinnerDateModel model = new SpinnerDateModel();
        model.setCalendarField(Calendar.MINUTE);
        spinner = new JSpinner();

        spinner.setModel(model);
        spinner.setEditor(new JSpinner.DateEditor(spinner, "HH:mm"));
        spinner.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, Settings.Styles.applicationBgColor), BorderFactory.createTitledBorder("Antal personer:")));
        add(spinner);
        /* Time spinner */

        add(Box.createRigidArea(new Dimension(0, 10)));

        Border bbb = BorderFactory.createTitledBorder("Antal personer:");
        final DatePicker datePicker = new DatePicker(new Date());
        datePicker.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        datePicker.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, Settings.Styles.applicationBgColor), BorderFactory.createTitledBorder("Antal personer:")));

        this.add(datePicker);
        durationField = addTextField("");
        durationField.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, Settings.Styles.applicationBgColor), BorderFactory.createTitledBorder("Varaktighet i timmar:")));

        add(Box.createRigidArea(new Dimension(0, 10)));
        addBookingBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        add(addBookingBtn);

        errorMessagesTextArea = new JTextArea();
        errorMessagesTextArea.setEditable(false);

        add(errorMessagesTextArea);
        errorMessagesTextArea.setVisible(false);

        addBookingBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                errorMessagesTextArea.setText("");
                errorMessagesTextArea.setVisible(false);

                bookingTime = (Date) spinner.getValue();
                boolean invalidInput = false;

                if (nameField.getText().isEmpty() || personsField.getText().isEmpty() || durationField.getText().isEmpty() || phoneNrField.getText().isEmpty()) {
                    errorMessagesTextArea.append(missingBookingInput + newLine);
                    invalidInput = true;
                }

                if (!(isInteger(personsField.getText()) && isInteger(durationField.getText()))) {
                    errorMessagesTextArea.append(invalidNumberInput + newLine);
                    invalidInput = true;
                }

                // Invalid input if choosen date & time is earlier than the present date and time
                mergeDateWithTime(bookingTime, datePicker.getDate());
                if (bookingTime.getTime() < new Date().getTime()) {
                    errorMessagesTextArea.append(invalidDateInput + newLine);
                    invalidInput = true;
                }

                if (invalidInput) {
                    errorMessagesTextArea.setVisible(true);
                    return;
                }

                // Merge datePicker with timePicker
                booking.setName(nameField.getText());
                booking.setPersons(Integer.parseInt(personsField.getText()));
                booking.setPhoneNr(phoneNrField.getText());

                booking.setDate(bookingTime);
                booking.setDuration(Integer.parseInt(durationField.getText()));

                if (ae.getSource().getClass() != JButton.class) {
                    return;
                }
                JButton btn = (JButton) ae.getSource();
                if (btn.getParent() == null) {
                    return;
                }
                Container parent = btn.getParent().getParent();
                remote.setViewBookings();
                parent.revalidate();
            }
        });
    }

    /**
     * Merges d1's "calendar" with d2's time.
     *
     * @param d1 Date with time
     * @param d2 Date with year, month and day
     * @return return value saved in d1.
     *
     */
    private void mergeDateWithTime(Date d1, Date d2) {
        //d1.setTime(d2.getTime() - d2.getTime()%(1000*60*60*24) + d1.getTime() % (1000*60*60*24));
        d1.setYear(d2.getYear());
        d1.setMonth(d2.getMonth());
        d1.setDate(d2.getDate());
    }

    /**
     * Verifies whether a string is an integer or alphabetic
     *
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
     *
     * @param labelName
     * @return returns the JLabel
     */
    private JLabel addLabel(String labelName) {
        JLabel label = new JLabel("<html><div style='margin: 10px 0 3px 3px;'>" + labelName + "</div></html>");
        Box fixHeight = Box.createHorizontalBox();
        fixHeight.add(label);
        fixHeight.add(Box.createHorizontalGlue());
        add(fixHeight);
        return label;
    }

    /**
     * Creates a formatted JTextField instance with the specified text.
     *
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

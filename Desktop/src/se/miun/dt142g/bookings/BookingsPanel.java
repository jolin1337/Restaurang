package se.miun.dt142g.bookings;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import se.miun.dt142g.Controller;
import se.miun.dt142g.DataSource;
import se.miun.dt142g.Settings;
import se.miun.dt142g.data.Booking;

/**
 * Holds the user interface for the bookings
 * @author Marcus
 */
public class BookingsPanel extends JPanel {

    // Variables declaration - do not modify  
    /**
     * The table labels
     */
    Object[] headers = new Object[]{"Namn", "Telefon", "Antal", "Datum", "Varaktighet (timmar)"};
    /**
     * Indicates if we have shanged the view to NewBooking
     */
    private boolean newBookingP = false;
    /**
     * Indicates if we have pushed the remove button
     */
    private boolean removeBooking = false;
    /**
     * The container for all components in this view
     */
    private final JPanel thisPanel = this;
    
    /**
     * The remote to make it posible to change tabviews
     */
    private final Controller remote;
    /**
     * The data source of this object to fill
     */
    private final Bookings bookings;
    /**
     * Add button for new booking
     */
    private JButton addBooking;
    /**
     * Remove button to remove an booking object
     */
    private JButton remove;
    
    //instance table model
    DefaultTableModel model = new DefaultTableModel() {

        @Override
        public boolean isCellEditable(int row, int column) {
            // Disable editing on the header
            return !(row == 0);
        }
    };
    /**
     * THis is the table conntaining all bookings/reservations
     */
    JTable table = new JTable(model);

    // End of variables declaration
    
    /**
     * Constructs a bookingpanel. The controller is the remote object to make it
     * able to change views
     * 
     * @param c - The remote
     * @throws se.miun.dt142g.DataSource.WrongKeyException If we were unable to 
     * connect to the server/database
     */
    public BookingsPanel(Controller c) throws DataSource.WrongKeyException {
        this.remote = c;                // Sets the remote
        this.bookings = new Bookings(); // create new dataobject
        this.bookings.dbConnect();      // Try to connect to database
        this.bookings.loadData();       // Load the data from database to this booking
        setBackground(Settings.Styles.applicationBgColor);
        initComponents();               // Create all contents of this view
    }

    /**
     * Initiates all components and adding them to the container
     */
    private void initComponents() {

        table.setRowHeight(40);
        remove = new JButton("Ta bort selekterad rad");
        remove.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        add(Box.createRigidArea(new Dimension(0, 15)));
        add(remove);

        add(Box.createRigidArea(new Dimension(0, 30)));

        // Create a couple of columns 
        for (int i = 0; i < 5; i++) {
            model.addColumn("Col" + i);
        }
        // Append a row 
        model.addRow(headers);
        for (Booking bok : bookings) {
            model.addRow(new Object[]{bok.getName(), bok.getPhoneNr(), bok.getPersons(),
                bok.getDateString(), bok.getDuration()});
        }
        resizeColumnWidth(table);
        add(table);
        

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(Box.createRigidArea(new Dimension(1, 10)));
        addBooking = new JButton("Lägg till bokning");

        addBooking.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        add(addBooking);
        addListeners();
    }

    /**
     * Adds listeners for components strictly in BookingsPanel only.
     */
    private void addListeners(){
        
        // Remove booking listener
        remove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Delete row

                removeBooking = true;
                thisPanel.revalidate();
            }
        });
     
        /**
         * This adds an tablemodel listener to the table
         */
        table.getModel().addTableModelListener(new TableModelListener() {
            
            /**
             * This function syncronizes information to the server when a user 
             * edits any of the cells available for editing
             * @param e - The tablemodell
             */
            @Override
            public void tableChanged(TableModelEvent e) {
                // If the type of change is an update (not an removal or creation)
                if (e.getType() == TableModelEvent.UPDATE) {
                    String nameCellValue = table.getValueAt(table.getSelectedRow(), 0).toString();
                    String phoneNrCellValue = table.getValueAt(table.getSelectedRow(), 1).toString();
                    String personsCellValue = table.getValueAt(table.getSelectedRow(), 2).toString();
                    String dateCellValue = table.getValueAt(table.getSelectedRow(), 3).toString();
                    String durationCellValue = table.getValueAt(table.getSelectedRow(), 4).toString();

                    // Get booking instance
                    Booking bok = bookings.getBookingByIndex(table.getSelectedRow() - 1);

                    // Update the fields of the matched booking datasource
                    bok.setName(nameCellValue);
                    bok.setPhoneNr(phoneNrCellValue);

                    // Persons field
                    if (isInteger(personsCellValue)) {
                        bok.setPersons(Integer.parseInt(personsCellValue));
                    } else {
                        model.setValueAt(bok.getPersons(), table.getSelectedRow(), e.getColumn());
                    }

                    // Update the date of the matched booking datasource
                    String oldDate = new SimpleDateFormat(Settings.Styles.dateFormat).format(bok.getDate());
                    try {
                        if (isValidDate(dateCellValue, Settings.Styles.dateFormat)) {
                            bok.setDate(parseDate(dateCellValue, Settings.Styles.dateFormat));
                        } else {
                            model.setValueAt(oldDate, table.getSelectedRow(), e.getColumn());
                        }
                    } catch (ParseException ex) {
                        model.setValueAt(oldDate, table.getSelectedRow(), e.getColumn());
                        System.err.println("Datumet lyckades inte redigeras i bokningar.");
                        Logger.getLogger(BookingsPanel.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    // Duration field
                    if (isInteger(durationCellValue)) {
                        bok.setDuration(Integer.parseInt(durationCellValue));
                    } else {
                        model.setValueAt(bok.getDuration(), table.getSelectedRow(), e.getColumn());
                    }

                    // Update database with new values
                    try {
                        // sync to server here
                        bookings.update();
                    } catch (DataSource.WrongKeyException ex) {
                        System.err.println("Det gick tyvärr inte att uppdatera.");
                        Logger.getLogger(BookingsPanel.class.getName()).log(Level.SEVERE, null, ex);
                        System.err.println("Förlåt, ingen uppdatering. vi ska försöka visa det för användaren...");
                        
                        JOptionPane.showMessageDialog(BookingsPanel.this,
                            Settings.Strings.serverConnectionError,
                            "Server error",
                            JOptionPane.ERROR_MESSAGE);
                        remote.setConnectionView();
                    }
                }
            }
        });

        // Add booking listener
        addBooking.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Booking b = new Booking(bookings.getUniqueId(), "", new Date(), 0, 0, "");
                bookings.addBooking(b);
                remote.setViewNewBooking(b);
                newBookingP = true;
            }
        });
    }
    
    /**
     * Resizes a JTable according to the cell-contents
     * @param table table to be resized
     */
    private void resizeColumnWidth(JTable table) {
        final TableColumnModel columnModel = table.getColumnModel();
        for (int column = 0; column < table.getColumnCount(); column++) {
            int width = 50; // Min width
            for (int row = 0; row < table.getRowCount(); row++) {
                TableCellRenderer renderer = table.getCellRenderer(row, column);
                Component comp = table.prepareRenderer(renderer, row, column);
                width = Math.max(comp.getPreferredSize().width, width);
            }
            columnModel.getColumn(column).setPreferredWidth(width);
        }
    }

    /**
     * Prase a string to a specified date format
     * @param date to be parsed
     * @param format format to be used
     * @return a formatted date
     * @throws ParseException
     */
    private Date parseDate(String date, String format) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat(format);
        df.setLenient(false);
        return df.parse(date.trim());
    }
    
    /**
     * Validates a string to verify whether it's a valid date format or not.
     * @param date the date to be validated
     * @param format the format to validate with
     * @return whether the string is valid
     */
    private boolean isValidDate(String date, String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        df.setLenient(false);
        try {
            df.parse(date.trim());
        } catch (ParseException pe) {
            return false;
        }
        return true;
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
    
    @Override
    public void revalidate() {
        super.revalidate();
        if (newBookingP) {
            Booking bok = bookings.getBookingByIndex(bookings.getRows() - 1);
            if (!bok.isValid()) {
                newBookingP = false;
                return;
            }
            bookings.addBookingDb(bok);
            model.addRow(new Object[]{
                bok.getName(), bok.getPhoneNr(), bok.getPersons(), bok.getDateString(), bok.getDuration()
            });
            resizeColumnWidth(table);
            newBookingP = false;
            try {
                bookings.update();
            } catch (DataSource.WrongKeyException ex) {
                JOptionPane.showMessageDialog(BookingsPanel.this,
                    Settings.Strings.serverConnectionError,
                    "Server error",
                    JOptionPane.ERROR_MESSAGE);
                remote.setConnectionView();
            }
        } else if (removeBooking) {
            removeBooking = false;
            try {
                if (table.getSelectedRow() > 0) {
                    bookings.removeBooking(
                            bookings.getBookingByIndex(table.getSelectedRow()-1)
                            .getId());
                    model.removeRow(table.getSelectedRow());
                }
            } catch (IndexOutOfBoundsException e) {
                System.err.println("BookingsPanel(removeBooking) IndexOutOfBoundsException: " + e.getMessage());
            }

        }
    }
}

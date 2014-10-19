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
import javax.swing.JLabel;
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
    
    /**
     * Instantiate table model
     * This is for making the first row disabled for editing
     */
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
    /**
     * The table labels
     */
    Object[] headers = new Object[]{"Namn", "Telefon", "Antal", "Datum", "Varaktighet (timmar)"};
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
        this.bookings = new Bookings(); // create new dataobject
        this.bookings.dbConnect();      // Try to connect to database
        this.bookings.loadData();       // Load the data from database to this booking
        initComponents();               // Create all contents of this view
        this.remote = c;                // Sets the remote
    }

    /**
     * Initiates all components and adding them to the container
     */
    private void initComponents() {

        table.setRowHeight(40);
        remove = new JButton("Ta bort selekterad rad");
        remove.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        this.add(Box.createRigidArea(new Dimension(0, 15)));
        this.add(remove);
        this.remove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Delete row

                removeBooking = true;
                thisPanel.revalidate();
            }
        });
        this.add(Box.createRigidArea(new Dimension(0, 30)));

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
                    // Get the selected row that was edited
                    Booking bok = bookings.getBookingByIndex(table.getSelectedRow() - 1);
                    System.out.println("Editing row: " + table.getSelectedRow() + " Reservation name: " + bok.getName());
                    
                    // Update the fields of the matched booking datasource
                    bok.setName(table.getValueAt(table.getSelectedRow(), 0).toString());
                    bok.setPhoneNr(table.getValueAt(table.getSelectedRow(), 1).toString());
                    bok.setPersons(Integer.parseInt(table.getValueAt(table.getSelectedRow(), 2).toString()));
                    
                    // Update the date of the matched booking datasource
                    SimpleDateFormat df = new SimpleDateFormat(Settings.Styles.dateFormat);
                    Date tmpDate = bok.getDate();
                    String s = df.format(tmpDate);
                    try {
                        if (isValidDate(table.getValueAt(table.getSelectedRow(), 3).toString(), Settings.Styles.dateFormat))
                            bok.setDate(parseDate(table.getValueAt(table.getSelectedRow(), 3).toString(), Settings.Styles.dateFormat));
                        else
                            model.setValueAt(s, table.getSelectedRow(), e.getColumn());
                        } catch (ParseException ex) {
                        model.setValueAt(s, table.getSelectedRow(), e.getColumn());
                        System.out.println("The date is invalid and not updated in bookings object.");
                        Logger.getLogger(BookingsPanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    bok.setDuration(Integer.parseInt(table.getValueAt(table.getSelectedRow(), 4).toString()));
                    try {
                        // sync to server here
                        bookings.update();
                    } catch (DataSource.WrongKeyException ex) {
                        System.out.println("Sorry no update lets try to show that to the user...");
                        
                        JOptionPane.showMessageDialog(BookingsPanel.this,
                            Settings.Strings.serverConnectionError,
                            "Server error",
                            JOptionPane.ERROR_MESSAGE);
                        remote.setConnectionView();
                    }
                }
            }
        });

        // Sets this views layout
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(Box.createRigidArea(new Dimension(1, 10)));
        this.setBackground(Settings.Styles.applicationBgColor);
        addBooking = new JButton("Lägg till bokning");
        this.addBooking.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Booking b = new Booking(bookings.getUniqueId(), "", new Date(), 0, 0, "");
                bookings.addBooking(b); // add the new booking and sync to the server
                remote.setViewNewBooking(b);
                newBookingP = true;
            }
        });
        addBooking.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        this.add(addBooking);
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
     * Creates a formatted JLabel instance with the specified text.
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
        } else if (removeBooking) {
            removeBooking = false;
            try {
                if (table.getSelectedRow() > 0) {
                    bookings.removeBooking(
                            bookings.getBookingByIndex(table.getSelectedRow())
                            .getId());
                    model.removeRow(table.getSelectedRow());
                }
            } catch (IndexOutOfBoundsException e) {
                System.err.println("BookingsPanel(removeBooking) IndexOutOfBoundsException: " + e.getMessage());
            }

        }
    }
}

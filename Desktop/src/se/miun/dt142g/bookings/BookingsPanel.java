/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
 *
 * @author Marcus
 */
public class BookingsPanel extends JPanel {

    // Variables declaration - do not modify                     
    private final Bookings bookings;
    private final JPanel thisPanel = this;
    private JButton addBooking;
    private JButton remove;
    private JButton submit;
    private final Controller remote;
    private boolean newBookingP = false;
    private boolean removeBooking = false;
    
    //instance table model
    DefaultTableModel model = new DefaultTableModel() {

        @Override
        public boolean isCellEditable(int row, int column) {
            // Disable editing on the header
            return !(row == 0);
        }
    };
    JTable table = new JTable(model);
    Object[] headers = new Object[]{"Namn", "Telefon", "Antal", "Datum", "Varaktighet (timmar)"};


    // End of variables declaration  
    public BookingsPanel(Controller c) throws DataSource.WrongKeyException {
        this.bookings = new Bookings();
        this.bookings.dbConnect();
        this.bookings.loadData();
        initComponents();
        this.remote = c;
    }

    /**
     * Initiates all components and adding them to the container
     */
    private void initComponents() {

        table.setRowHeight(33);
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
        
        table.getModel().addTableModelListener(new TableModelListener() {
            public void tableChanged(TableModelEvent e) {
                if (e.getType() == TableModelEvent.UPDATE) {
                    Booking bok = bookings.getBookingByIndex(table.getSelectedRow() - 1);
                    System.out.println("rad: " + table.getSelectedRow() + " Boknings namn: " + bok.getName());
                    bok.setName(table.getValueAt(table.getSelectedRow(), 0).toString());
                    bok.setPhoneNr(table.getValueAt(table.getSelectedRow(), 1).toString());
                    bok.setPersons(Integer.parseInt(table.getValueAt(table.getSelectedRow(), 2).toString()));
                    
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
                        System.out.println("Datumet lyckades inte redigeras i bokningar.");
                        Logger.getLogger(BookingsPanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    bok.setDuration(Integer.parseInt(table.getValueAt(table.getSelectedRow(), 4).toString()));
                    try {
                        bookings.update();
                    } catch (DataSource.WrongKeyException ex) {
                        System.out.println("Det gick tyvärr inte att uppdatera.");
                        Logger.getLogger(BookingsPanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(Box.createRigidArea(new Dimension(1, 10)));
        this.setBackground(Color.white);
        addBooking = new JButton("Lägg till bokning");
        this.addBooking.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Booking b = new Booking(bookings.getUniqueId(), "", new Date(), 0, 0, "");
                bookings.addBooking(b);
                remote.setViewNewBooking(b);
                newBookingP = true;
            }
        });
        addBooking.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        this.add(addBooking);
    }

    /**
     * Resizes a JTable according to the cell-contents
     *
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
     *
     * @param date to be parsed
     * @param format format to be used
     * @return
     * @throws ParseException
     */
    private Date parseDate(String date, String format) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat(format);
        df.setLenient(false);
        return df.parse(date.trim());
    }
    
    /**
     * 
     * @param date the date to be validated
     * @param format the format to validate with
     * @return whether the string is valid
     * @throws ParseException 
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

    @Override
    public void revalidate() {
        /*try {
         if (bookings != null)
         bookings.update();
         } catch (DataSource.WrongKeyException ex) {
         Logger.getLogger(BookingsPanel.class.getName()).log(Level.SEVERE, null, ex);
         }*/
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
                if (table.getSelectedRow() > 0) {;
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

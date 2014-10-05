/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package miun.dt142g.bookings;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import miun.dt142g.Controller;
import miun.dt142g.DataSource;
import miun.dt142g.data.Booking;

/**
 *
 * @author Marcus
 */
public class BookingsPanel extends JPanel {

    // Variables declaration - do not modify                     
    private Bookings bookings;
    private JPanel labels = new JPanel();

    private JButton addBooking;
    private JLabel nameLabel;
    private JLabel personsLabel;
    private JLabel dateLabel;
    private JLabel timeLabel;
    private JLabel timeLengthLabel;
    private final Controller fjarr;

    private final DefaultTableModel model = new DefaultTableModel();

    private boolean newBookingP = false;

    // End of variables declaration     
    public BookingsPanel(Controller fjarr) throws DataSource.WrongKeyException {
        this.bookings = new Bookings();
        this.bookings.dbConnect();
        this.bookings.loadData();
        initComponents();
        this.fjarr = fjarr;

    }

    @SuppressWarnings("empty-statement")
    private void initComponents() {
        labels.setVisible(true);

        JTable table = new JTable(model);

        // Create a couple of columns 
        model.addColumn("Col1");
        model.addColumn("Col2");
        model.addColumn("Col3");
        model.addColumn("Col4");
        model.addColumn("Col5");

        // Append a row 
        model.addRow(new Object[]{"Namn", "Antal", "Datum", "Tid", "Varaktighet"});
        for (Booking bok : bookings) {

            model.addRow(new Object[]{bok.getName(), bok.getPersons(),
                bok.getDateString(), bok.getTime(), bok.getDuration()});
        }
        add(table);

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(Box.createRigidArea(new Dimension(1, 10)));
        this.setBackground(Color.white);
        addBooking = new JButton("Lägg till bokning");
        this.addBooking.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Date date = new Date();
                Booking b = new Booking(bookings.getUniqueId(), "", date, 0, 0, 0);
                bookings.addBooking(b);
                fjarr.setViewNewBooking(b);
                newBookingP = true;
            }
        });
        addBooking.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        this.add(addBooking);
        this.setVisible(true);
    }

    @Override
    public void revalidate() {
        super.revalidate();
        if (newBookingP) {
            Booking bok = bookings.getBookingByIndex(bookings.getRows() - 1);
            model.addRow(new Object[]{
                bok.getName(), bok.getPersons(), bok.getDateString(), bok.getTime(), bok.getDuration()
            });
            newBookingP = false;
        }
    }
}

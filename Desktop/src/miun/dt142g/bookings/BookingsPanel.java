/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package miun.dt142g.bookings;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
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
    private final Bookings bookings;
    private final JPanel thisPanel = this;
    private JButton addBooking;
    private JButton remove; 
    private final Controller fjarr;
    private final JComboBox removeList = new JComboBox();
    private boolean newBookingP = false;
    private boolean removeBooking = false;
    private int removeIndex = 0;
    private final DefaultTableModel model = new DefaultTableModel();
    JTable table = new JTable(model);
    // End of variables declaration  
    
    
    public BookingsPanel(Controller fjarr) throws DataSource.WrongKeyException {
        this.bookings = new Bookings();
        this.bookings.dbConnect();
        this.bookings.loadData();
        initComponents();
        this.fjarr = fjarr;

    }

    /*@SuppressWarnings("empty-statement")*/
    private void initComponents() {

        table.setRowHeight(33);
        remove = new JButton("Ta bort");
        addLabel("Ta bort bokning");
        removeList.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        remove.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        this.add(removeList);
        this.add(Box.createRigidArea(new Dimension(0, 15)));
        this.add(remove);
        this.remove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Delete row
                removeIndex = removeList.getSelectedIndex();
                removeBooking = true;
                thisPanel.revalidate();
            }
        });
        this.add(Box.createRigidArea(new Dimension(0, 30)));
        
        
        // Create a couple of columns 
        for (int i = 0; i < 5; i++) {
            model.addColumn("Col"+i);
        }

        // Append a row 
        model.addRow(new Object[]{"Namn", "Antal", "Datum", "Tid", "Varaktighet"});
        for (Booking bok : bookings) {
            model.addRow(new Object[]{bok.getName(), bok.getPersons(),
                bok.getDateString(), bok.getTime(), bok.getDuration()});
            removeList.addItem(bok.getName());
        }
        add(table);


        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(Box.createRigidArea(new Dimension(1, 10)));
        this.setBackground(Color.white);
        addBooking = new JButton("Lägg till bokning");
        this.addBooking.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Booking b = new Booking(bookings.getUniqueId(), "", new Date(), 0, 0, 0);
                bookings.addBooking(b);
                fjarr.setViewNewBooking(b);
                newBookingP = true;
            }
        });
        addBooking.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        this.add(addBooking);
        this.setVisible(true);
    }

    private JLabel addLabel(String labelName) {
        JLabel label = new JLabel("<html><div style='margin: 10px 0 3px 3px;'>" + labelName + "</div></html>");
        Box  fixHeight = Box.createHorizontalBox();
        fixHeight.add( label );
        fixHeight.add( Box.createHorizontalGlue() );
        add(fixHeight); 
        return label;
    }
        
    @Override
    public void revalidate() {
        super.revalidate();
        if (newBookingP) {
            Booking bok = bookings.getBookingByIndex(bookings.getRows() - 1);
            model.addRow(new Object[]{
                bok.getName(), bok.getPersons(), bok.getDateString(), bok.getTime(), bok.getDuration()
            });
            removeList.addItem(bok.getName());
            newBookingP = false;
        }else if (removeBooking){
            try {
                if (removeList.getItemCount() == 0)
                    return;
                model.removeRow(removeIndex+1);
                removeList.removeItemAt(removeIndex);
                removeBooking = false;
            } catch (IndexOutOfBoundsException e) {
                System.err.println("BookingsPanel(removeBooking) IndexOutOfBoundsException: " + e.getMessage());
            }

        }
    }
}

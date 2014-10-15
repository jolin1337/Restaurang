/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package miun.dt142g.bookings;

import java.awt.Color;
import java.awt.Component;
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
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
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
    private JButton submit; 
    private final Controller fjarr;
    private boolean newBookingP = false;
    private boolean removeBooking = false;
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
            model.addColumn("Col"+i);
        }

        // Append a row 
        model.addRow(new Object[]{"Namn","Telefon", "Antal", "Datum", "Varaktighet"});
        for (Booking bok : bookings) {
            model.addRow(new Object[]{bok.getName(), bok.getPhoneNr(), bok.getPersons(),
                bok.getDateString(), bok.getDuration()});
        }
        resizeColumnWidth(table);
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
        
        submit = new JButton("Synkronisera med server");
        this.submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bookings.update();
            }
        });
        submit.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        this.add(submit);
    }
    
    // Haxxor thing to resize table according to contents
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
            if (bok.getName().isEmpty() || bok.getPersons() == 0 || bok.getDuration() == 0){
                    newBookingP = false;
                    return;
            }
            model.addRow(new Object[]{
                bok.getName(), bok.getPhoneNr(), bok.getPersons(), bok.getDateString(), bok.getDuration()
            });
            resizeColumnWidth(table);
            newBookingP = false;
        }
        else if (removeBooking){
            removeBooking = false;
            try {
                if (table.getSelectedRow() > 0 ){;
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

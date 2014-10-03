/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package miun.dt142g.bookings;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import miun.dt142g.Controller;
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
    private Controller fjarr;
    // End of variables declaration     
    public BookingsPanel(Controller fjarr) {
        initComponents();
        this.fjarr = fjarr;
        
    }
    
    private void initComponents(){
        this.bookings = new Bookings();
        this.bookings.dbConnect();
        this.bookings.loadData();
        initiateLabels();
        this.add(labels);
        labels.setVisible(true);

        for (Booking bok : bookings) {
            this.add(new BookingPanel(bok));
        }
        
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        
        addBooking = new JButton("Lägg till bokning");
        this.addBooking.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                Date date = new Date();
                Booking b = new Booking(bookings.getUniqueId(), "", date, 0, 0, 0);
                bookings.addBooking(b);
                fjarr.setViewNewBooking(b);
                
                /*BookingsPanel.this.remove(addBooking);
                BookingsPanel.this.add(new BookingPanel(new Booking(bookings.getUniqueId(), "", "1/3-37", 0, 0, 0)));
                BookingsPanel.this.add(addBooking);
                BookingsPanel.this.revalidate();*/
                
            }  
        });
        this.add(addBooking);
        this.setVisible(true);
    }
    private void initiateLabels(){
        labels.setLayout(new BoxLayout(labels, BoxLayout.X_AXIS));
        nameLabel = new JLabel("Namn:");
        nameLabel.setBorder(BorderFactory.createEmptyBorder(0,40,0,0));
        
        personsLabel = new JLabel("Antal: ");
        personsLabel.setBorder(BorderFactory.createEmptyBorder(0,80,0,0));
        
        dateLabel = new JLabel("Datum: "); 
        dateLabel.setBorder(BorderFactory.createEmptyBorder(0,80,0,0));
        
        timeLabel = new JLabel("Tid: "); 
        timeLabel.setBorder(BorderFactory.createEmptyBorder(0,80,0,0));
        
        timeLengthLabel = new JLabel("Längd: "); 
        timeLengthLabel.setBorder(BorderFactory.createEmptyBorder(0,80,0,0));
        
        labels.add(nameLabel);
        labels.add(personsLabel);
        labels.add(dateLabel);
        labels.add(timeLabel);
        labels.add(timeLengthLabel);

    }
}

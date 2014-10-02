/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package miun.dt142g.bookings;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.chrono.JapaneseDate;
import java.util.Date;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import miun.dt142g.data.Booking;

/**
 *
 * @author Nanashi-
 */
public class BookingsPanel extends JPanel {
    
    // Variables declaration - do not modify                     
    private Bookings bookings; 
    
    private JButton addBooking; 
    private JLabel nameLabel; 
    private JLabel personsLabel;
    private JLabel dateLabel; 
    private JLabel timeLabel; 
    private JLabel timeLengthLabel; 
    // End of variables declaration     
    public BookingsPanel() {
        initComponents();
        
    }
    
    public void initComponents(){
        this.bookings = new Bookings();
        this.bookings.loadData();
        
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        JPanel labels = new JPanel();
        labels.setLayout(new BoxLayout(labels, BoxLayout.X_AXIS));
        nameLabel = new JLabel("Namn:");
        personsLabel = new JLabel("Antal: ");
        dateLabel = new JLabel("Datum: "); 
        timeLabel = new JLabel("Tid: "); 
        timeLengthLabel = new JLabel("Längd: "); 
        
        labels.add(nameLabel);
        labels.add(personsLabel);
        labels.add(dateLabel);
        labels.add(timeLabel);
        labels.add(timeLengthLabel);
        labels.setVisible(true);
        
        addBooking = new JButton("Lägg till bokning");
        this.addBooking.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                BookingsPanel.this.remove(addBooking);
                BookingsPanel.this.add(new BookingPanel(new Booking(bookings.getUniqueId(), "", 0, 0, 0, 0)));
                BookingsPanel.this.add(addBooking);
                BookingsPanel.this.revalidate();   
            }  
        });
        
        this.add(addBooking);
        this.setVisible(true);
        
    }
}

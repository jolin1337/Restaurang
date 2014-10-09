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
import java.text.SimpleDateFormat;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import miun.dt142g.data.Booking;

// !important
// This complete class might not be necessary to keep.
/**
 *
 * @author ulf
 */
public class BookingPanel extends JPanel{
    private Booking bookings; 
    private final JButton remove; 
    
    private final JTextField nameField; 
    private final JTextField personsField; 
    private final JTextField dateField; 
    private final JTextField timeField;
    private final JTextField timeLengthField;
    private final Booking booking; 
    
    public BookingPanel(Booking booking){
        this.booking = booking; 
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.setBackground(Color.white);
        
        remove = new JButton("X");
        remove.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                Container parent = BookingPanel.this.getParent(); 
                parent.remove(BookingPanel.this);
                parent.revalidate();
                parent.repaint();
                //add remove from database

            }
        });
        
        
        nameField = new JTextField(booking.getName()); 
        personsField = new JTextField(Integer.toString(booking.getPersons())); 
        SimpleDateFormat ft = new SimpleDateFormat ("dd/MM-yy 'at' hh:mm");
        
        dateField = new JTextField(ft.format(booking.getDate())); 
        timeField = new JTextField(Integer.toString(booking.getTime())); 
        timeLengthField = new JTextField(booking.getDuration()); 
        
        //element layout
        nameField.setMaximumSize(new Dimension(Integer.MAX_VALUE,25));
        personsField.setMaximumSize(new Dimension(Integer.MAX_VALUE,25));
        dateField.setMaximumSize(new Dimension(Integer.MAX_VALUE,25));
        timeField.setMaximumSize(new Dimension(Integer.MAX_VALUE,25));
        timeLengthField.setMaximumSize(new Dimension(Integer.MAX_VALUE,25));

        this.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        this.add(remove);
        this.add(nameField);
        this.add(personsField);
        this.add(dateField);
        this.add(timeField);
        this.add(timeLengthField);
        this.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        this.setVisible(true);
        
    }
}

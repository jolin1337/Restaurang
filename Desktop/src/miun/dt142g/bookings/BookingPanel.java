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
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import miun.dt142g.data.Booking;

/**
 *
 * @author ulf
 */
public class BookingPanel extends JPanel{
    private Booking bookings; 
    private JButton remove; 
//    private JLabel name; 
//    private JLabel persons; 
//    private JLabel date; 
//    private JLabel time; 
//    private JLabel timeLength; 
    
    private JTextField nameField; 
    private JTextField personsField; 
    private JTextField dateField; 
    private JTextField timeField;
    private JTextField timeLengthField;
    private Booking booking; 
    
    public BookingPanel(Booking booking){
        this.booking = booking; 
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBackground(Color.white);
        
        remove = new JButton("X");
        remove.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                Container parent = BookingPanel.this.getParent(); 
                parent.remove(BookingPanel.this);
                parent.revalidate();
                
                //add remove from database

            }
        });
        
        nameField = new JTextField(); 
        personsField = new JTextField(); 
        dateField = new JTextField(); 
        timeField = new JTextField(); 
        timeLengthField = new JTextField(); 
        
        //element layout
        nameField.setSize(new Dimension(100,25));
        personsField.setSize(new Dimension(50,25));
        dateField.setSize(new Dimension(100,25));
        timeField.setSize(new Dimension(50,25));
        timeLengthField.setSize(new Dimension(50,25));
        
        this.add(remove);
        this.add(nameField);
        this.add(personsField);
        this.add(dateField);
        this.add(timeField);
        this.add(timeLengthField);
        this.setVisible(true);
        
    }
}

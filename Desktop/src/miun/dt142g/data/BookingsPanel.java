/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package miun.dt142g.data;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author Nanashi-
 */
public class BookingsPanel extends JFrame {
    
    // Variables declaration - do not modify                     
    private javax.swing.JSlider minuteSlider;
    private javax.swing.JTextField name;
    private javax.swing.JLabel personsLabel;
    private javax.swing.JSlider personsSlider;
    private javax.swing.JLabel timeDivider;
    private javax.swing.JLabel timeLabel;
    private javax.swing.JSlider hourSlider;
    // End of variables declaration     
    public BookingsPanel() {
        initComponents();
        
    }
    
    public void initComponents(){
        name = new javax.swing.JTextField();
        timeLabel = new javax.swing.JLabel();
        hourSlider = new javax.swing.JSlider();
        timeDivider = new javax.swing.JLabel();
        minuteSlider = new javax.swing.JSlider();

        personsSlider = new javax.swing.JSlider();
        personsLabel = new javax.swing.JLabel();

        name.setText("Name");
        timeLabel.setText("Time:");
        hourSlider.setMaximum(24);
        timeDivider.setText(":");
        minuteSlider.setMaximum(59);
        
        personsSlider.setMaximum(30);
        personsSlider.setMinimum(1);
        personsLabel.setText("Persons:");
    }
}

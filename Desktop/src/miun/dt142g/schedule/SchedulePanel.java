/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package miun.dt142g.schedule;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import miun.dt142g.data.Schedule;

/**
 *
 * @author hagh1200
 */
public class SchedulePanel extends JPanel{
    
    public SchedulePanel(Schedule s){
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setBackground(Color.white);
       

        JLabel date = new JLabel(s.getDate());
        JLabel time = new JLabel(s.getStartTime() + "-" + s.getEndTime()); 
        time.setFont(new Font("Calibri", Font.BOLD, 20));
        date.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        JLabel username = new JLabel("Användare: ");
        username.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        JTextField usrname = new JTextField(s.getUsername());
        usrname.setEditable(false);
        usrname.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        add(username);
        add(usrname);
        add(date);
        add(time);
        add(new JSeparator());
       

    }
}

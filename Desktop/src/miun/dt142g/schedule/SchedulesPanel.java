/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package miun.dt142g.schedule;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import miun.dt142g.data.Schedule;

/**
 *
 * @author hagh1200
 */
public class SchedulesPanel extends JPanel{
    
    private Schedules schedules = new Schedules();
    
    
    public SchedulesPanel(){
        schedules.loadData();
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setBackground(Color.white);
        final JButton addBtn = new JButton("Lägg till ny tid");
        addBtn.setMinimumSize(new Dimension(50, 25));
        addBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        
        for(Schedule s: schedules){
            SchedulePanel sp = new SchedulePanel(s);
            add(sp);
            
        }
        add(addBtn);
        
        
        
        addBtn.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent ae) {
                
                JPanel tmp = new JPanel();
                tmp.setLayout(new BoxLayout(tmp, BoxLayout.PAGE_AXIS));
                JLabel user = new JLabel("Användare:");
                final JTextField userField = new JTextField();
                JLabel date = new JLabel("Datum:");
                final JTextField dateField = new JTextField();
                JLabel startTime = new JLabel("Starttid:");
                final JTextField startTimeField = new JTextField();
                JLabel endTime = new JLabel("Sluttid:");
                final JTextField endTimeField = new JTextField();
                tmp.add(user);
                tmp.add(userField);
                tmp.add(date);
                tmp.add(dateField);
                tmp.add(startTime);
                tmp.add(startTimeField);
                tmp.add(endTime);
                tmp.add(endTimeField);
                JButton done = new JButton("Klar");
                tmp.add(done);
                
                done.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent ae) {
                        Schedule a = new Schedule(userField.getText(), dateField.getText(), startTimeField.getText(), endTimeField.getText());
                        schedules.addSchedule(a);
                        removeAll();
                        for(Schedule s: schedules){
                            SchedulePanel sp = new SchedulePanel(s);
                                add(sp);
                                add(addBtn);
                        }   
                        
                    }
                });
                
                add(tmp);
                revalidate();
            }
            
        });
        
    }
}

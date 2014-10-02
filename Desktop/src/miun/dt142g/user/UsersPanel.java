/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package miun.dt142g.user;

import miun.dt142g.data.User;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 *
 * @author Ali Omran
 */
public class UsersPanel extends JPanel {
    
    private JButton add;
    
    public UsersPanel(){
        Users s = new Users();
        //.addUser();
        
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setBackground(Color.WHITE);
        add = new JButton("Lägg till användare");
        
        add.setMinimumSize(new Dimension(50, 25));
        add.setPreferredSize(new Dimension(50, 25));
      
        
        
        for (User user : s)
        {
            UserPanel pn = new UserPanel(user);
            add(pn);
        }

        add.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        add(add);  
        add.addActionListener(new ActionListener(){
            
            @Override
            public void actionPerformed(ActionEvent event)
            {
                UsersPanel.this.remove(add);
                UserPanel p = new UserPanel(new User(2,"as","asdddd","ddddddd","24535456546"));
                add(p);
                add(add);
                
            }
            
        });
           
    }
    
}

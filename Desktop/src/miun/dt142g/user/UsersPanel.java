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
import miun.dt142g.DataSource;

/**
 *
 * @author Ali Omran
 */
public class UsersPanel extends JPanel {
    
    private JButton addUserBtn;
    private Users usrs = new Users();
    
    public UsersPanel() throws DataSource.WrongKeyException{
        usrs.dbConnect();
        usrs.loadData();
        //.addUser();
        
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setBackground(Color.WHITE);
        addUserBtn = new JButton("Lägg till användare");
        
        addUserBtn.setMinimumSize(new Dimension(50, 25));
        addUserBtn.setPreferredSize(new Dimension(50, 25));
      
        
        
        for (User user : usrs) {
            UserPanel pn = new UserPanel(user);
            add(pn);
        }

        addUserBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        add(addUserBtn);  
        addUserBtn.addActionListener(new ActionListener(){
            
            @Override
            public void actionPerformed(ActionEvent event)
            {
                remove(addUserBtn);
                UserPanel p = new UserPanel(new User(2,"","","",""));
                add(p);
                add(addUserBtn);
                
            }
            
        });
           
    }
    
}

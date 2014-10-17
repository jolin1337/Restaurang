/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.miun.dt142g.user;

import se.miun.dt142g.data.User;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import se.miun.dt142g.Controller;
import se.miun.dt142g.DataSource;

/**
 *
 * @author Ali Omran
 */
public class UsersPanel extends JPanel {
    
    private final JButton addUserBtn = new JButton("Lägg till användare");
    private final JButton serverSyncBtn = new JButton("Synkronisera med servern");
    private Users usrs = new Users();
    final Controller remote;
    
    public UsersPanel(Controller c) throws DataSource.WrongKeyException{
        usrs.dbConnect();
        usrs.loadData();
        usrs.listToJsonArray();
        remote = c;
        
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setBackground(Color.WHITE);
        
        addUserBtn.setMinimumSize(new Dimension(50, 25));
        addUserBtn.setPreferredSize(new Dimension(50, 25));
      
        
        
        for (User user : usrs) {
            UserPanel pn = new UserPanel(user, remote);
            add(pn);
        }

        addUserBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        add(addUserBtn);  
        add(serverSyncBtn);
        addUserBtn.addActionListener(userPanelActionListener);
        
        serverSyncBtn.addActionListener(userPanelActionListener);
           
    }

    ActionListener userPanelActionListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent ae) {
            Object src = ae.getSource();
            if(src == addUserBtn) {
                UsersPanel.this.remove(addUserBtn);
                UsersPanel.this.remove(serverSyncBtn);
                User a =new User(-1,"","","","");
                usrs.addUser(a);
                UserPanel p = new UserPanel(a, remote);
                UsersPanel.this.add(p);
                UsersPanel.this.add(addUserBtn);
                UsersPanel.this.add(serverSyncBtn);
                UsersPanel.this.revalidate();
                remote.setSavedTab(UsersPanel.this, false);
            }
            else if(src == serverSyncBtn) {
                remove(addUserBtn);
                remove(serverSyncBtn);
                usrs.update();
                removeAll();
                for (User user : usrs) {
                    UserPanel pn = new UserPanel(user, remote);
                    add(pn);
                }
                add(addUserBtn);
                add(serverSyncBtn);
                revalidate();
                remote.setSavedTab(UsersPanel.this, true);
            }
        }
    };

    
}

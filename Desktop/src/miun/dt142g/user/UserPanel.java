/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package miun.dt142g.user;

import miun.dt142g.data.User;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author Ali Omran
 */
public class UserPanel extends JPanel{
    
    private JButton remove;
    private JLabel name,tele, epost, password;
    private TextField user, pwd, mail, telenr;
    
    public UserPanel(User user){
        
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(10,10,10,10));
        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        JPanel a = new JPanel();
        remove = new JButton("X");
        remove.setMaximumSize(new Dimension(25,25));
        
        
        a.setLayout(new BoxLayout(a, BoxLayout.PAGE_AXIS));
        a.setBackground(Color.WHITE);
        
        
        name = new JLabel("Namn:");
        name.setMaximumSize(new Dimension(60,25));
        password = new JLabel("Lösenord:");

        epost = new JLabel("Mail:");
        tele = new JLabel("Tel:");
        
        this.user = new TextField(user.getUsername());
        this.user.setMaximumSize(new Dimension(600,40));
        pwd = new TextField(user.getPassword());
        pwd.setMaximumSize(new Dimension(600,40));
        mail = new TextField(user.getUsername());
        mail.setMaximumSize(new Dimension(600,40));
        telenr = new TextField(user.getPhoneNumber());
        telenr.setMaximumSize(new Dimension(600,40));

        
        leftAlignLabel(name, a);
        a.add(this.user);
        leftAlignLabel(password, a);
        a.add(pwd);
        leftAlignLabel(epost, a);
        a.add(mail);
        leftAlignLabel(tele, a);
        a.add(telenr);
        add(remove);
        add(a);
        remove.addActionListener(new ActionListener(){
            
            @Override
            public void actionPerformed(ActionEvent event)
            {
                Container parent = UserPanel.this.getParent();
                parent.remove(UserPanel.this);
                parent.revalidate();
            }
            
        });
    }
    
    public static void leftAlignLabel(JLabel label, JPanel c){
        Box b = Box.createHorizontalBox();
        b.add(label);
        b.add(Box.createHorizontalGlue());
        c.add(b);
        
    }
    
}

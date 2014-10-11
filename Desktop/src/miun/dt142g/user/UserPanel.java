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
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import miun.dt142g.Settings;
import miun.dt142g.ConfirmationBox;

/**
 *
 * @author Ali Omran
 */
public class UserPanel extends JPanel{
    
    private final JButton remove;
    private final JLabel name,tele, epost, password;
    private final JTextField user, pwd, mail, telenr;
    
    public UserPanel(User user){
        
        setBackground(Color.WHITE);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        JPanel a = new JPanel();
        remove = new JButton("X");
        remove.setMaximumSize(new Dimension(25,25));
        
        
        a.setLayout(new BoxLayout(a, BoxLayout.LINE_AXIS));
        a.setBackground(Color.WHITE);
        
        JPanel inputs = new JPanel();
        inputs.setLayout(new BoxLayout(inputs, BoxLayout.Y_AXIS));
        inputs.setBackground(Color.WHITE);
        
        name = new JLabel("Namn:");
        name.setMaximumSize(new Dimension(60,25));
        password = new JLabel("Lösenord:");

        epost = new JLabel("Mail:");
        tele = new JLabel("Tel:");
        
        this.user = new JTextField(user.getUsername());
        this.user.setMaximumSize(new Dimension(Integer.MAX_VALUE,40));
        pwd = new JTextField(user.getPassword());
        pwd.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        mail = new JTextField(user.getUsername());
        mail.setMaximumSize(new Dimension(Integer.MAX_VALUE,40));
        telenr = new JTextField(user.getPhoneNumber());
        telenr.setMaximumSize(new Dimension(Integer.MAX_VALUE,40));

        leftAlignLabel(name, inputs);
        inputs.add(this.user);
        leftAlignLabel(password, inputs);
        inputs.add(pwd);
        leftAlignLabel(epost, inputs);
        inputs.add(mail);
        leftAlignLabel(tele, inputs);
        inputs.add(telenr);
        inputs.add(Box.createGlue());
        a.add(remove);
        a.add(inputs);
        a.setMaximumSize(new Dimension(Integer.MAX_VALUE, 400));
        add(a);
        add(Box.createRigidArea(new Dimension(1, 10)));
        add(new JSeparator());

        remove.addActionListener(new ActionListener(){
            
            @Override
            public void actionPerformed(ActionEvent event)
            {
                int n = ConfirmationBox.confirm(UserPanel.this, UserPanel.this.user.getText());
                if(n == 0){
                    Container parent = UserPanel.this.getParent();
                    parent.remove(UserPanel.this);
                    parent.revalidate();
                }
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

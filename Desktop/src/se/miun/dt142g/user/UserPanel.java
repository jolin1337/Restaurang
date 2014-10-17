/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.miun.dt142g.user;

import se.miun.dt142g.data.User;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import se.miun.dt142g.ConfirmationBox;
import se.miun.dt142g.Controller;

/**
 *
 * @author Ali Omran
 */
public class UserPanel extends JPanel {
    
    private JButton remove;
    private final JLabel name,tele, epost, password;
    private final JTextField editUser, pwd, mail, telenr;
    private final User usr;
    private final Controller remote;
    
    public UserPanel(final User user, Controller c){
        remote = c;
        this.usr = user;
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
        
        editUser = new JTextField(user.getUsername());
        editUser.setMaximumSize(new Dimension(Integer.MAX_VALUE,40));
        editUser.addFocusListener(userPanelFocusListener);
        editUser.addKeyListener(userPanelKeyListener);
        pwd = new JTextField(user.getPassword());        
        pwd.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        pwd.addFocusListener(userPanelFocusListener);
        pwd.addKeyListener(userPanelKeyListener);
        mail = new JTextField(user.getMail());
        mail.setMaximumSize(new Dimension(Integer.MAX_VALUE,40));
        mail.addFocusListener(userPanelFocusListener);
        mail.addKeyListener(userPanelKeyListener);
        telenr = new JTextField(user.getPhoneNumber());
        telenr.setMaximumSize(new Dimension(Integer.MAX_VALUE,40));
        telenr.addFocusListener(userPanelFocusListener);
        telenr.addKeyListener(userPanelKeyListener);

        leftAlignLabel(name, inputs);
        inputs.add(this.editUser);
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

        remove.addActionListener(userPanelActionListener);
    }
    
    public static void leftAlignLabel(JLabel label, JPanel c){
        Box b = Box.createHorizontalBox();
        b.add(label);
        b.add(Box.createHorizontalGlue());
        c.add(b);
        
    }
    
    ActionListener userPanelActionListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent ae) {
            int n = ConfirmationBox.confirm(UserPanel.this, UserPanel.this.editUser.getText());
            if(n == 0){
                usr.setRemove();
                Container parent = UserPanel.this.getParent();
                parent.remove(UserPanel.this);
                remove= new JButton("X");
                parent.revalidate();
                remote.setSavedTab((JComponent)parent, false);
            }
        }
    };
    
    FocusListener userPanelFocusListener = new FocusListener() {
        @Override
        public void focusGained(FocusEvent e) {

        }

        @Override
        public void focusLost(FocusEvent e) {
            usr.setUsername(editUser.getText());
            usr.setPassword(pwd.getText());
            usr.setMail(mail.getText());
            usr.setPhoneNumber(telenr.getText());

        }
    };
    KeyListener userPanelKeyListener = new KeyListener() {

        @Override
        public void keyTyped(KeyEvent ke) {
            remote.setSavedTab((JComponent)UserPanel.this.getParent(), false);
        }

        @Override
        public void keyPressed(KeyEvent ke) {
        }

        @Override
        public void keyReleased(KeyEvent ke) {
        }
    };

    
    public User getUser(){
        return this.usr;
    }
    
}

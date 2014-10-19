/*
 * This code is created for one purpose only. And should not be used for any 
 * other purposes unless the author of this file has apporved. 
 * 
 * This code is a piece of a project in the course DT142G on Mid. Sweden university
 * Created by students for this projekt only
 */
package se.miun.dt142g;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * This class represents the veiw shown if there was an error to connect to the
 * server
 *
 * @author Johannes Lindén
 * @since 2014-10-11
 * @version 1.3
 */
class LoginPage extends JPanel {
    /**
     * The alternative one choise, change the safekey textfield
     */
    JTextField alt1Txt = new JTextField(DataSource.getSafeKey());

    /**
     * Constructs the entire view in default constructor
     */
    public LoginPage() {
        setBackground(Color.white);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        JLabel info = new JLabel("<html><p>Something is wrong with the </p><p>server or your internet connection</p>"
                + "<br/><p>You have two different approaches:</p></html>");
        add(info);
        JLabel alt1 = new JLabel("1. Enter a new authentication key:");
        alt1.setFont(new Font("Calibri", Font.ITALIC, 22));
        add(alt1);
        alt1Txt.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        add(alt1Txt);
        
        add(Box.createRigidArea(new Dimension(1, 10)));
        JButton alt2 = new JButton("2. Reconnect and se if it works.");
        add(alt2);
        alt2.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                Container parent = LoginPage.this.getParent();
                try {
                    DataSource.setSafeKey(alt1Txt.getText());
                    parent.remove(LoginPage.this);
                    parent.add(new MainDT142GStarter());
                } catch (DataSource.WrongKeyException ex) {
                    parent.add(LoginPage.this);
                } finally {
                    parent.revalidate();
                    parent.repaint();
                }
            }
        });
    }

}

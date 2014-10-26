package se.miun.dt142g.user;

import se.miun.dt142g.data.User;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.BorderFactory;
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
import se.miun.dt142g.Settings;

/**
 * Graphical representation of a single user
 *
 * @author Ali Omran
 */
public class UserPanel extends JPanel {

    /**
     * JButton object to remove this userpanel and user object
     */
    private JButton remove;
    /**
     * Textfields for input of user information
     */
    private final JTextField editUser, pwd, mail, telenr;
    /**
     * the User object for this userpanel
     */
    private final User usr;
    /**
     * The remote controller to switch tab-view
     */
    private final Controller remote;
    /**
     * Field edit listener for updating upon focus lost
     */
    private JFieldEditListener userPanelListener = null;

    /**
     * UserPanel constructor initializes object and graphics for this user panel
     *
     * @param user The User object for this UserPanel
     * @param c Controller to set tab and to add asterisk if tab is unsaved
     */
    public UserPanel(final User user, Controller c) {
        remote = c;
        this.usr = user;
        setBackground(Settings.Styles.applicationBgColor);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        JPanel a = new JPanel();
        remove = new JButton("X");
        remove.setMaximumSize(new Dimension(25, 25));

        a.setLayout(new BoxLayout(a, BoxLayout.LINE_AXIS));
        a.setBackground(Settings.Styles.applicationBgColor);

        JPanel inputs = new JPanel();
        inputs.setLayout(new BoxLayout(inputs, BoxLayout.Y_AXIS));
        inputs.setBackground(Settings.Styles.applicationBgColor);

        editUser = new JTextField(user.getUsername());
        editUser.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        editUser.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, Settings.Styles.darkBg), BorderFactory.createTitledBorder("Namn:")));
        editUser.addFocusListener(userPanelFocusListener);
        editUser.addKeyListener(userPanelKeyListener);
        pwd = new JTextField(user.getPassword());
        pwd.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        pwd.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, Settings.Styles.darkBg), BorderFactory.createTitledBorder("Lösenord:")));
        pwd.addFocusListener(userPanelFocusListener);
        pwd.addKeyListener(userPanelKeyListener);
        mail = new JTextField(user.getMail());
        mail.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        mail.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, Settings.Styles.darkBg), BorderFactory.createTitledBorder("Mail:")));
        mail.addFocusListener(userPanelFocusListener);
        mail.addKeyListener(userPanelKeyListener);
        telenr = new JTextField(user.getPhoneNumber());
        telenr.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        telenr.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, Settings.Styles.darkBg), BorderFactory.createTitledBorder("Tel:")));
        telenr.addFocusListener(userPanelFocusListener);
        telenr.addKeyListener(userPanelKeyListener);

        inputs.add(this.editUser);
        inputs.add(pwd);
        inputs.add(mail);
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

    /**
     * Left aligned label
     *
     * @param label label to align left and add to UserPanel
     * @param c contoller
     */
    public static void leftAlignLabel(JLabel label, JPanel c) {
        Box b = Box.createHorizontalBox();
        b.add(label);
        b.add(Box.createHorizontalGlue());
        c.add(b);

    }
    /**
     * ActionListener object for removing this panel from UsersPanel object and
     * removing the user from from the list of users
     */
    ActionListener userPanelActionListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent ae) {
            int n = ConfirmationBox.confirm(UserPanel.this, "Ta bort " + UserPanel.this.editUser.getText() + "?");
            if (n == 0) {
                usr.setRemove();
                Container parent = UserPanel.this.getParent();
                parent.remove(UserPanel.this);
                remove = new JButton("X");
                parent.revalidate();
                remote.setSavedTab((JComponent) parent, false);
                userPanelListener.onFieldEdit();
            }
        }
    };

    /**
     * Focus listener to save data from fields on focus lost
     */
    FocusListener userPanelFocusListener = new FocusListener() {
        @Override
        public void focusGained(FocusEvent e) {

        }

        @Override
        public void focusLost(FocusEvent e) {
            updateFields();
        }
    };

    /**
     * key lister to save data from fields on enter press in fields and set
     * saved tab to false for controller
     */
    KeyListener userPanelKeyListener = new KeyListener() {

        @Override
        public void keyTyped(KeyEvent ke) {
            remote.setSavedTab((JComponent) UserPanel.this.getParent(), false);
        }

        @Override
        public void keyPressed(KeyEvent ke) {
            if (ke.getKeyCode() == KeyEvent.VK_ENTER) {
                updateFields();
            }
        }

        @Override
        public void keyReleased(KeyEvent ke) {
        }
    };

    /**
     * Save data from fields
     */
    public void updateFields() {
        usr.setUsername(editUser.getText());
        usr.setPassword(pwd.getText());
        usr.setMail(mail.getText());
        usr.setPhoneNumber(telenr.getText());
        if (!(userPanelListener == null)) {
            userPanelListener.onFieldEdit();
        }
    }

    /**
     * @return returns the user for this userPanel object
     */
    public User getUser() {
        return this.usr;
    }

    /**
     * @return the userPanelListener
     */
    public JFieldEditListener getUserPanelListener() {
        return userPanelListener;
    }

    /**
     * @param userPanelListener the userPanelListener to set
     */
    public void setUserPanelListener(JFieldEditListener userPanelListener) {
        this.userPanelListener = userPanelListener;
    }

    /**
     * Field edit listener interface to be implemented in UsersPanel
     */
    public interface JFieldEditListener {

        public void onFieldEdit();
    }

}

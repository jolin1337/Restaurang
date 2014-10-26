package se.miun.dt142g.user;

import se.miun.dt142g.data.User;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import se.miun.dt142g.Controller;
import se.miun.dt142g.DataSource;
import se.miun.dt142g.Settings;
import se.miun.dt142g.user.UserPanel.JFieldEditListener;

/**
 * Graphical interface for the users
 *
 * @author Ali Omran
 */
public class UsersPanel extends JPanel {

    /**
     * Button to add a user
     */
    private final JButton addUserBtn = new JButton("Lägg till användare");
    /**
     * New instance of users
     */
    private Users usrs = new Users();
    /**
     * An instance of the controller class to handle the tab-view
     */
    final Controller remote;

    /**
     * Initiate components
     *
     * @param c - The controller instance
     * @throws se.miun.dt142g.DataSource.WrongKeyException
     */
    public UsersPanel(Controller c) throws DataSource.WrongKeyException {
        usrs.dbConnect();
        usrs.loadData();
        usrs.listToJsonArray();
        remote = c;

        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setBackground(Settings.Styles.applicationBgColor);

        addUserBtn.setMinimumSize(new Dimension(50, 25));
        addUserBtn.setPreferredSize(new Dimension(50, 25));

        for (User user : usrs) {
            UserPanel pn = new UserPanel(user, remote);
            pn.setUserPanelListener(userPanelListener);
            add(pn);
        }

        addUserBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        add(addUserBtn);
        addUserBtn.addActionListener(userPanelActionListener);

    }

    ActionListener userPanelActionListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent ae) {
            Object src = ae.getSource();
            if (src == addUserBtn) {
                UsersPanel.this.remove(addUserBtn);
                User a = new User(-1, "", "", "", "");
                usrs.addUser(a);
                UserPanel p = new UserPanel(a, remote);
                p.setUserPanelListener(userPanelListener);
                UsersPanel.this.add(p);
                UsersPanel.this.add(addUserBtn);
                UsersPanel.this.revalidate();
                remote.setSavedTab(UsersPanel.this, false);
                userPanelListener.onFieldEdit();
            }
        }
    };

    /**
     * Actionlistener for when the Jfield has been modified
     */
    private JFieldEditListener userPanelListener = new JFieldEditListener() {
        @Override
        public void onFieldEdit() {
            remove(addUserBtn);
            usrs.update();
            removeAll();
            for (User user : usrs) {
                UserPanel pn = new UserPanel(user, remote);
                pn.setUserPanelListener(userPanelListener);
                add(pn);
            }
            add(addUserBtn);
            revalidate();
            remote.setSavedTab(UsersPanel.this, true);
        }
    };
}

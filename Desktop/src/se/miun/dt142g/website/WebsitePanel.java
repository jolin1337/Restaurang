/*
 * This code is created for one purpose only. And should not be used for any 
 * other purposes unless the author of this file has apporved. 
 * 
 * This code is a piece of a project in the course DT142G on Mid. Sweden university
 * Created by students for this projekt only
 */
package se.miun.dt142g.website;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import se.miun.dt142g.Controller;
import se.miun.dt142g.DataSource;
import se.miun.dt142g.Settings;
import se.miun.dt142g.data.AboutUs;
import se.miun.dt142g.data.EventPost;

/**
 * This class describes the website information to edit
 *
 * @author Johannes Lindén
 * @since 2014-10-11
 * @version 1.3
 */
public class WebsitePanel extends JPanel {
    List<EventPostPanel> eventPostPanels = new ArrayList<>();
    EventPosts eventPosts = new EventPosts();
    AboutUs about = new AboutUs();
    
    JButton newEventPostBtn = new JButton("Lägg till nytt evenemang");
    JButton newEventPostBtnTop = new JButton("Lägg till nytt evenemang");
    JButton submitBtn = new JButton(Settings.Strings.submit);
    JButton submitBtnTop = new JButton(Settings.Strings.submit);
    
    JTextArea openEdit = new JTextArea();
    JTextArea contactEdit = new JTextArea();
    
    Controller remote = null;
    
    public WebsitePanel(Controller c) throws DataSource.WrongKeyException {
        eventPosts.dbConnect();
        eventPosts.loadData();
        about.dbConnect();
        about.loadData();
        remote = c;

        setBackground(Settings.Styles.applicationBgColor);
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        openEdit.setText(about.getDataOpen());
        openEdit.addKeyListener(textFieldKeyListener);
        openEdit.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, Color.white), BorderFactory.createTitledBorder("Öppettider:")));
        add(openEdit);
   
        contactEdit.setText(about.getDataContacts());
        contactEdit.addKeyListener(textFieldKeyListener);
        contactEdit.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, Color.white), BorderFactory.createTitledBorder("Kontaktinformation:")));
        add(contactEdit);
        
        newEventPostBtnTop.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        newEventPostBtnTop.addActionListener(websiteEventListener);
        add(newEventPostBtnTop);
        
        submitBtnTop.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        submitBtnTop.addActionListener(websiteEventListener);
        add(submitBtnTop);
        
        for(EventPost ep : eventPosts) {
            add(Box.createRigidArea(new Dimension(1, 10)));
            EventPostPanel ep1 = new EventPostPanel(ep, remote);
            add(ep1);
            eventPostPanels.add(ep1);
        }
        newEventPostBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        newEventPostBtn.addActionListener(websiteEventListener);
        add(newEventPostBtn);
        
        submitBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        submitBtn.addActionListener(websiteEventListener);
        add(submitBtn);
    }
    ActionListener websiteEventListener = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                Object src = ae.getSource();
                
                // If we should submit
                if(src == submitBtn || src == submitBtnTop) {
                    about.setDataOpen(openEdit.getText());
                    about.setDataContacts(contactEdit.getText());
                    about.update();
                    for(EventPostPanel evPostPanel : eventPostPanels) 
                        evPostPanel.updateEvent();
                    try {
                        eventPosts.update();
                    } catch (DataSource.WrongKeyException ex) {

                        JOptionPane.showMessageDialog(WebsitePanel.this,
                            Settings.Strings.serverConnectionError,
                            "Server error",
                            JOptionPane.ERROR_MESSAGE);
                        remote.setConnectionView();
                    }
                    int index = 0;
                    for(EventPost p : eventPosts)
                        eventPostPanels.get(++index-1).setUpdatedEvent(p);
                    remote.setSavedTab(WebsitePanel.this, true);
                }
                // If we should add new event
                else if(src == newEventPostBtn || src == newEventPostBtnTop) {
                    remove(newEventPostBtn);
                    remove(submitBtn);
                    EventPost ep = new EventPost(eventPosts.getUniqueId());
                    add(Box.createRigidArea(new Dimension(1, 10)));
                    EventPostPanel ep1 = new EventPostPanel(ep, remote);
                    add(ep1);
                    eventPostPanels.add(ep1);
                    eventPosts.addEvent(ep);
                    add(newEventPostBtn);
                    add(submitBtn);
                    WebsitePanel.this.revalidate();
                    WebsitePanel.this.repaint();
                    Container parent = WebsitePanel.this.getParent().getParent();
                    if(parent instanceof JScrollPane) {
                        final JScrollBar vertical = ((JScrollPane)parent).getVerticalScrollBar();
                        Runnable myTask = new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(100);
                                } catch (InterruptedException ex) {
                                    Logger.getLogger(WebsitePanel.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                vertical.setValue( vertical.getMaximum() );
                                WebsitePanel.this.revalidate();
                                WebsitePanel.this.repaint();
                            }
                        };
                        Thread thread = new Thread(myTask);
                        thread.start();

                    }
                    remote.setSavedTab(WebsitePanel.this, false);
                }
            }
        };
    KeyListener textFieldKeyListener = new KeyListener() {

        @Override
        public void keyTyped(KeyEvent ke) {
            remote.setSavedTab(WebsitePanel.this, false);

        }

        @Override
        public void keyPressed(KeyEvent ke) {
        }

        @Override
        public void keyReleased(KeyEvent ke) {
        }

    };
}

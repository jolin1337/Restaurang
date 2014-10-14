/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package miun.dt142g.website;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import miun.dt142g.DataSource;
import miun.dt142g.data.AboutUs;
import miun.dt142g.data.EventPost;

/**
 *
 * @author Johannes
 */
public class WebsitePanel extends JPanel {
    List<EventPostPanel> eventPostPanels = new ArrayList<>();
    EventPosts eventPosts = new EventPosts();
    AboutUs about = new AboutUs();
    
    JButton newEventPostBtn = new JButton("Lägg till nytt evenemang");
    JButton submitBtn = new JButton("Syncronizera med server");
    
    JTextArea openEdit = new JTextArea();
    JTextArea contactEdit = new JTextArea();
    
    public WebsitePanel() throws DataSource.WrongKeyException {
        eventPosts.dbConnect();
        eventPosts.loadData();
        about.dbConnect();
        about.loadData();
        
        setBackground(Color.WHITE);
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        submitBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        submitBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                about.setDataOpen(openEdit.getText());
                about.setDataContacts(contactEdit.getText());
                about.update();
                for(EventPostPanel evPostPanel : eventPostPanels) 
                    evPostPanel.updateEvent();
                try {
                    eventPosts.update();
                } catch (DataSource.WrongKeyException ex) {
                    
                    JOptionPane.showMessageDialog(WebsitePanel.this,
                        "There is an error in the authentication to the server or the server is down. Please check this out before do any changes!",
                        "Server error",
                        JOptionPane.ERROR_MESSAGE);
                }
                int index = 0;
                for(EventPost p : eventPosts)
                    eventPostPanels.get(++index-1).setUpdatedEvent(p);
            }
        });
        
        JLabel open = new JLabel("<html><div style='margin: 10px 0 3px 3px;'>Öppetider</div></html>");
        Box  leftJustify = Box.createHorizontalBox();
        leftJustify.add( open );
        leftJustify.add( Box.createHorizontalGlue() );
        add(leftJustify);     
        openEdit.setText(about.getDataOpen());
        add(openEdit);
   
        JLabel contact = new JLabel("<html><div style='margin: 10px 0 3px 3px;'>Kontaktinformation</div></html>");
        leftJustify = Box.createHorizontalBox();
        leftJustify.add( contact );
        leftJustify.add( Box.createHorizontalGlue() );
        add(leftJustify);
        contactEdit.setText(about.getDataContacts());
        add(contactEdit);
        
        for(EventPost ep : eventPosts) {
            add(Box.createRigidArea(new Dimension(1, 10)));
            EventPostPanel ep1 = new EventPostPanel(ep);
            add(ep1);
            eventPostPanels.add(ep1);
        }
        newEventPostBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        newEventPostBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                remove(newEventPostBtn);
                remove(submitBtn);
                EventPost ep = new EventPost(eventPosts.getUniqueId());
                add(Box.createRigidArea(new Dimension(1, 10)));
                EventPostPanel ep1 = new EventPostPanel(ep);
                add(ep1);
                eventPostPanels.add(ep1);
                eventPosts.addEvent(ep);
                add(newEventPostBtn);
                add(submitBtn);
                WebsitePanel.this.revalidate();
            }
        });
        add(newEventPostBtn);
        add(submitBtn);
    }
}

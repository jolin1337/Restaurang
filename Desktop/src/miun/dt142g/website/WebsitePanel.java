/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package miun.dt142g.website;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.TextArea;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import miun.dt142g.data.AboutUs;
import miun.dt142g.food.EventPosts;

/**
 *
 * @author Johannes
 */
public class WebsitePanel extends JPanel {
    List<EventPostPanel> eventPostPanels = new ArrayList<>();
    EventPosts eventPosts = new EventPosts();
    AboutUs about = new AboutUs();
    public WebsitePanel() {
        eventPosts.dbConnect();
        eventPosts.loadData();
        about.dbConnect();
        about.loadData();
        
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(10,10,10,10));
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        JLabel open = new JLabel("<html><div style='margin: 10px 0 3px 3px;'>Öppetider</div></html>");
        Box  leftJustify = Box.createHorizontalBox();
        leftJustify.add( open );
        leftJustify.add( Box.createHorizontalGlue() );
        add(leftJustify);     
        TextArea openEdit = new TextArea();
        openEdit.setText(about.getDataOpen());
        add(openEdit);
   
        JLabel contact = new JLabel("<html><div style='margin: 10px 0 3px 3px;'>Kontakt</div></html>");
        leftJustify = Box.createHorizontalBox();
        leftJustify.add( contact );
        leftJustify.add( Box.createHorizontalGlue() );
        add(leftJustify);
        TextArea contactEdit = new TextArea();
        contactEdit.setText(about.getDataContacts());
        add(contactEdit);
        
        for(int i=eventPosts.getRows();i>0; i--) {
            add(Box.createRigidArea(new Dimension(1, 10)));
            EventPostPanel ep1 = new EventPostPanel(eventPosts.getEvent(i-1));
            add(ep1);
        }
    }
}

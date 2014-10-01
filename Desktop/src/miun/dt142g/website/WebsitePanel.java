/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package miun.dt142g.website;

import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
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
    
    Button newEventPostBtn = new Button("Lägg till nytt evenemang");
    
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
   
        JLabel contact = new JLabel("<html><div style='margin: 10px 0 3px 3px;'>Kontaktinformation</div></html>");
        leftJustify = Box.createHorizontalBox();
        leftJustify.add( contact );
        leftJustify.add( Box.createHorizontalGlue() );
        add(leftJustify);
        TextArea contactEdit = new TextArea();
        contactEdit.setText(about.getDataContacts());
        add(contactEdit);
        
        for(EventPost ep : eventPosts) {
            add(Box.createRigidArea(new Dimension(1, 10)));
            EventPostPanel ep1 = new EventPostPanel(ep);
            add(ep1);
            eventPostPanels.add(ep1);
        }
        
        newEventPostBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                remove(newEventPostBtn);
                EventPost ep = new EventPost(eventPosts.getRows());
                add(Box.createRigidArea(new Dimension(1, 10)));
                EventPostPanel ep1 = new EventPostPanel(ep);
                add(ep1);
                eventPostPanels.add(ep1);
                add(newEventPostBtn);
                WebsitePanel.this.revalidate();
            }
        });
        add(newEventPostBtn);
    }
}
/**
package miun.dt142g.website;

import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import miun.dt142g.data.AboutUs;
import miun.dt142g.data.EventPost;

/**
 *
 * @author Johannes
 
public class WebsitePanel extends JScrollPane {
    List<EventPostPanel> eventPostPanels = new ArrayList<>();
    EventPosts eventPosts = new EventPosts();
    AboutUs about = new AboutUs();
    
    Button newEventPostBtn = new Button("Lägg till nytt evenemang");
    
    JPanel container = new JPanel();
    public WebsitePanel() {
        eventPosts.dbConnect();
        eventPosts.loadData();
        about.dbConnect();
        about.loadData();
        
        container.setBackground(Color.WHITE);
        container.setBorder(new EmptyBorder(10,10,10,10));
        container.setLayout(new BoxLayout(container, BoxLayout.PAGE_AXIS));
        JLabel open = new JLabel("<html><div style='margin: 10px 0 3px 3px;'>Öppetider</div></html>");
        Box  leftJustify = Box.createHorizontalBox();
        leftJustify.add( open );
        leftJustify.add( Box.createHorizontalGlue() );
        container.add(leftJustify);     
        TextArea openEdit = new TextArea();
        openEdit.setText(about.getDataOpen());
        container.add(openEdit);
   
        JLabel contact = new JLabel("<html><div style='margin: 10px 0 3px 3px;'>Kontaktinformation</div></html>");
        leftJustify = Box.createHorizontalBox();
        leftJustify.add( contact );
        leftJustify.add( Box.createHorizontalGlue() );
        container.add(leftJustify);
        TextArea contactEdit = new TextArea();
        contactEdit.setText(about.getDataContacts());
        container.add(contactEdit);
        
        for(EventPost ep : eventPosts) {
            container.add(Box.createRigidArea(new Dimension(1, 10)));
            EventPostPanel ep1 = new EventPostPanel(ep);
            container.add(ep1);
            eventPostPanels.add(ep1);
        }
        
        newEventPostBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                remove(newEventPostBtn);
                EventPost ep = new EventPost(eventPosts.getRows());
                container.add(Box.createRigidArea(new Dimension(1, 10)));
                EventPostPanel ep1 = new EventPostPanel(ep);
                container.add(ep1);
                eventPostPanels.add(ep1);
                container.add(newEventPostBtn);
                WebsitePanel.this.revalidate();
            }
        });
        container.add(newEventPostBtn);
    }
}

 */
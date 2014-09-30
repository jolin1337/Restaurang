/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package miun.dt142g.website;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Label;
import java.awt.TextArea;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author Johannes
 */
public class WebsitePanel extends JPanel {
    public WebsitePanel() {
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(10,10,10,10));
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        JLabel open = new JLabel("<html><div style='margin: 10px 0 3px 3px;'>Öppetider</div></html>");
        Box  leftJustify = Box.createHorizontalBox();
        leftJustify.add( open );
        leftJustify.add( Box.createHorizontalGlue() );
        add(leftJustify);
        
        TextArea openEdit = new TextArea();
        add(openEdit);
        JLabel contact = new JLabel("<html><div style='margin: 10px 0 3px 3px;'>Kontakt</div></html>");
        leftJustify = Box.createHorizontalBox();
        leftJustify.add( contact );
        leftJustify.add( Box.createHorizontalGlue() );
        add(leftJustify);
        TextArea contactEdit = new TextArea();
        add(contactEdit);
        
        EventPost ep1 = new EventPost();
        add(ep1);
        add(Box.createRigidArea(new Dimension(1, 10)));
        EventPost ep2 = new EventPost();
        add(ep2);
    }
}

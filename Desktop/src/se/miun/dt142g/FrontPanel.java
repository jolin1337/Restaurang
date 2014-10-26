/*
 * This code is created for one purpose only. And should not be used for any 
 * other purposes unless the author of this file has apporved. 
 * 
 * This code is a piece of a project in the course DT142G on Mid. Sweden university
 * Created by students for this projekt only
 */
package se.miun.dt142g;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * This class is a static start page/panel
 *
 * @author Johannes Lindén
 * @since 2014-10-11
 * @version 1.3
 */
public class FrontPanel extends JPanel {

    private BufferedImage image;

    /**
     * Sets the background for the main window
     */
    public FrontPanel() {
        try {
            setBackground(Color.white);
            image = ImageIO.read(new File("res/graphics/logo.png"));
        } catch (IOException ex) {
            add(new JLabel("Ingen bild/logga hittades"));
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, null); // see javadoc for more info on the parameters            
    }
}

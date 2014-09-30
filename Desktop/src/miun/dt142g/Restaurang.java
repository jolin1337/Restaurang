/*
 * This is a main file for the system. 
 */
package miun.dt142g;

import miun.dt142g.website.WebsitePanel;
import java.awt.BorderLayout;
import java.awt.ScrollPane;
import javax.swing.JFrame;
import miun.dt142g.website.WebsitePanel;
/**
 *
 * @author jolin1337
 */
public class Restaurang {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        JFrame tmp = new JFrame("Start Screen");

        //2. Optional: What happens when the frame closes?
        tmp.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //3. Create components and put them in the frame.
        //...create emptyLabel...
        ScrollPane sp = new ScrollPane(ScrollPane.SCROLLBARS_AS_NEEDED);
        sp.add(new WebsitePanel());
        tmp.getContentPane().add(sp, BorderLayout.CENTER);

        //4. Size the frame.
        tmp.pack();
        tmp.setSize(600, 600);

        //5. Show it.
        tmp.setVisible(true);
    }
    
}

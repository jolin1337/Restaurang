/*
 */
package se.miun.dt142g;

import java.awt.ScrollPane;
import javax.swing.JFrame;
/**
 * This is an test file for desktop application
 * 
 * @author Johannes Lindén
 * @since 2014-10-11
 * @version 1.3
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
        //Dishes dishes = new Dishes();
        //dishes.dbConnect();
        //dishes.loadData();
        //sp.add(new DishDetailPanel(dishes.getDish(0)));
        //sp.add(new DishesPanel());
//        tmp.getContentPane().add(new BookingsPanel(), BorderLayout.CENTER);

        //4. Size the frame.
        tmp.pack();
        tmp.setSize(600, 600);

        //5. Show it.
        tmp.setVisible(true);
    }
    
}

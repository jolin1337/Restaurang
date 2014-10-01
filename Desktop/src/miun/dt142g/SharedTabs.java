/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package miun.dt142g;


import javax.swing.JTabbedPane;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import miun.dt142g.food.DishesPanel;
import miun.dt142g.website.WebsitePanel;

/**
 *
 * @author Tomas
 */
public class SharedTabs extends JPanel{

    public SharedTabs() {
        //super(new GridLayout(1, 1)); //Not needed?
        
        JTabbedPane tabbedPane = new JTabbedPane();
        
        JComponent panel1 = new DishesPanel();
        tabbedPane.addTab("Rätter", panel1);
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);
        
        JComponent panel2 = new WebsitePanel();
        //Currently WebsitePanel is very large, 
        //needs a scrollbar to implement preferred size.
        //panel2.setPreferredSize(new Dimension(410, 50));
        tabbedPane.addTab("Hemsida", panel2);
        tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);
        
        //Add more tabs using this template
        /*
        JComponent panel3 = new ClassName();
        tabbedPane.addTab("Fliknamn", panel3);
        tabbedPane.setMnemonicAt(2, KeyEvent.VK_3);
        */
        
        /*
        JComponent panel4 = new ClassName();
        tabbedPane.addTab("Fliknamn", panel4);
        tabbedPane.setMnemonicAt(3, KeyEvent.VK_4);
        */
        
        //Add the tabbed pane to this panel.
        add(tabbedPane);
        
        //The following line enables to use scrolling tabs.
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from
     * the event dispatch thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("Restaurang");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //Add content to the window.
        frame.add(new SharedTabs(), BorderLayout.CENTER);
        
        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }
    
    
    public static void main(String[] args) {
        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
		createAndShowGUI();
            }
        });
    }
    
}


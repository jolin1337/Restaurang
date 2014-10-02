/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package miun.dt142g;


import javax.swing.JTabbedPane;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.ScrollPane;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import miun.dt142g.data.Dish;
import miun.dt142g.food.DishDetailPanel;
import miun.dt142g.food.DishesPanel;
import miun.dt142g.inventory.InventoryPanel;
import miun.dt142g.user.UsersPanel;
import miun.dt142g.website.WebsitePanel;

/**
 *
 * @author Tomas
 */
public class SharedTabs extends JPanel {
    DishDetailPanel dishDetailView = new DishDetailPanel(null);
    JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
    List<JComponent> panels = new ArrayList<>();
    
    Controller fjarr = new Controller() {

        @Override
        public void setViewDishes() {
            tabbedPane.setSelectedIndex(0);
        }

        @Override
        public void setViewWebsite() {
            tabbedPane.setSelectedIndex(1);
        }

        @Override
        public void setViewDishDetail(Dish d) {
            dishDetailView.setDish(d);
            tabbedPane.addTab("Rätten i detaij", dishDetailView);
            tabbedPane.revalidate();
            tabbedPane.setSelectedComponent(dishDetailView);
        }

        @Override
        public void setViewInventory() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        
        @Override
        public void setViewUsers(){
            tabbedPane.setSelectedIndex(4);
        }
        
    };
    public SharedTabs() {
        //super(new GridLayout(1, 1)); //Not needed?
        setLayout(new BorderLayout());
        DishesPanel panel1 = new DishesPanel(fjarr);
        panel1.setViewSwitch(fjarr);
        panels.add(panel1);
        JComponent panel2 = new WebsitePanel();
        panels.add(panel2);
        InventoryPanel panel3 = new InventoryPanel(); 
        panels.add(panel3);
        UsersPanel panel4 = new UsersPanel();
        panels.add(panel4);
        String[] titles = {"Rätter", "Hemsida","Inventarie", "Användare"};
        int i = 0;
        for(JComponent panel : panels) {
            ScrollPane sp = new ScrollPane(ScrollPane.SCROLLBARS_AS_NEEDED);
            sp.add(panel);
            tabbedPane.addTab(titles[i], sp);
            i++;
        }
        
        //Currently WebsitePanel is very large, 
        //needs a scrollbar to implement preferred size.
        //panel2.setPreferredSize(new Dimension(410, 50));
        
        tabbedPane.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent ce) {
                if(tabbedPane.isAncestorOf(dishDetailView) && tabbedPane.getSelectedComponent() != dishDetailView)
                    tabbedPane.remove(dishDetailView);
            }
        });
        
        
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
        frame.setMinimumSize(new Dimension(600,600));
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


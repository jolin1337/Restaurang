/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package miun.dt142g;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.ScrollPane;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import miun.dt142g.bookings.BookingsPanel;
import miun.dt142g.bookings.NewBooking;
import miun.dt142g.data.Booking;
import miun.dt142g.data.Dish;
import miun.dt142g.food.DishDetailPanel;
import miun.dt142g.food.DishesPanel;
import miun.dt142g.food.MenuPanel;
import miun.dt142g.inventory.InventoryPanel;
import miun.dt142g.user.UsersPanel;
import miun.dt142g.website.WebsitePanel;
import miun.dt142g.Settings.Styles;
import miun.dt142g.schedule.SchedulesPanel;

/**
 *
 * @author Tomas
 */
public class SharedTabs extends JPanel {
    DishDetailPanel dishDetailView;
    JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
    List<JComponent> panels = new ArrayList<>();
    NewBooking newBooking = new NewBooking();
    
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

        @Override
        public void setViewNewBooking(Booking b) {
            newBooking.newBooking(b);
            tabbedPane.addTab("Bokning i detaij", newBooking);
            tabbedPane.revalidate();
            tabbedPane.setSelectedComponent(newBooking);
        }
        
    };
    public SharedTabs() throws DataSource.WrongKeyException {
        dishDetailView = new DishDetailPanel(null);
        
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
        MenuPanel panel5 = new MenuPanel(fjarr, new String[]{"A la Carte"});
        panels.add(panel5);
        MenuPanel panel6 = new MenuPanel(fjarr, new String[]{"Måndag","Tisdag","Onsdag","Torsdag","Fredag"});
        panels.add(panel6);
        BookingsPanel panel7 = new BookingsPanel(fjarr);
        panels.add(panel7);
        SchedulesPanel panel8 = new SchedulesPanel();
        panels.add(panel8);
        String[] titles = {"Rätter", "Hemsida","Inventarie", "Användare", "A La Carté","Veckans Meny", "Bokningar", "Schema"};
        int i = 0;
        for(JComponent panel : panels) {
            ScrollPane sp = new ScrollPane(ScrollPane.SCROLLBARS_AS_NEEDED);
            sp.add(panel);
            panel.setBorder(new EmptyBorder(10,10,10,10));
            tabbedPane.addTab(titles[i], sp);
            i++;
        }
        
        tabbedPane.addChangeListener(new ChangeListener() {
            
            @Override
            public void stateChanged(ChangeEvent ce) {
                for(JComponent j : panels) {
                    j.revalidate();
                }
                if(tabbedPane.isAncestorOf(newBooking) && tabbedPane.getSelectedComponent() != newBooking)
                    tabbedPane.remove(newBooking);
                if(tabbedPane.isAncestorOf(dishDetailView) && tabbedPane.getSelectedComponent() != dishDetailView)
                    tabbedPane.remove(dishDetailView);
            }
        });
        
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
        ImageIcon img = new ImageIcon("res/graphics/logo.png");
        frame.setIconImage(img.getImage());
        
        try {
            SharedTabs st = new SharedTabs();
            //Add content to the window.
            frame.add(st, BorderLayout.CENTER);
        } catch (DataSource.WrongKeyException ex) {
            frame.getContentPane().add(new LoginPage());
        }
        
        //Display the window.
        frame.pack();
        frame.setMinimumSize(new Dimension(700,700));
        frame.setVisible(true);
    }
    
    public static void main(String[] args) {
        
        UIManager.put("Button.font", new Font("Calibri", Font.PLAIN, 22));
        UIManager.put("Button.background", Styles.btnBackground);
        UIManager.put("Button.foreground", Styles.btnForeground);
        
        UIManager.put("Label.font", new Font("Calibri", Font.BOLD, 25));
        
        UIManager.put("TextArea.font", new Font("Calibri", Font.PLAIN, 22));
        UIManager.put("TextArea.background", Styles.fieldColor);
        UIManager.put("TextArea.border", BorderFactory.createLoweredBevelBorder());
        
        UIManager.put("TextField.font", new Font("Calibri", Font.PLAIN, 32));
        UIManager.put("TextField.background", Styles.fieldColor);
        UIManager.put("TextField.selectionBackground", Color.RED);
        UIManager.put("TextField.selectionForeground", Color.WHITE);
        UIManager.put("TextField.caretForeground", Color.pink);
        
        UIManager.put("Table.font", new Font("Calibri", Font.PLAIN, 22));
        UIManager.put("Table.selectionBackground", Styles.fieldColor);
        
        UIManager.put("ComboBox.background", Styles.fieldColor);
        UIManager.put("ComboBox.selectionBackground", Color.RED);
        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
		createAndShowGUI();
            }
        });        
    } 
}


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.miun.dt142g;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import se.miun.dt142g.bookings.BookingsPanel;
import se.miun.dt142g.bookings.NewBooking;
import se.miun.dt142g.data.Booking;
import se.miun.dt142g.data.Dish;
import se.miun.dt142g.food.DishDetailPanel;
import se.miun.dt142g.food.DishesPanel;
import se.miun.dt142g.food.MenuPanel;
import se.miun.dt142g.inventory.InventoryPanel;
import se.miun.dt142g.user.UsersPanel;
import se.miun.dt142g.website.WebsitePanel;
import se.miun.dt142g.Settings.Styles;
import se.miun.dt142g.schedule.SchedulesPanel;

/**
 *
 * @author Tomas
 */
public class SharedTabs extends JPanel {

    DishDetailPanel dishDetailView;
    JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
    List<JComponent> panels = new ArrayList<>();
    NewBooking newBooking = new NewBooking();
    DishesPanel dishesPanel;
    JScrollPane newBookingsScrollPane;

    Controller remote = new Controller() {

        @Override
        public void setViewDishes() {
            dishesPanel.updateTextFieldContents();
            tabbedPane.setSelectedIndex(0);
        }
        public void setViewDishes(boolean saved){
            setViewDishes();
            setSavedTab(dishesPanel, saved);
        }

        @Override
        public void setViewWebsite() {
            tabbedPane.setSelectedIndex(1);
        }

        @Override
        public void setViewDishDetail(Dish d, ActionListener removeEvent) {
            try {
                dishDetailView.setRemoveEventListener(removeEvent);
                dishDetailView.setDish(d);
            } catch (DataSource.WrongKeyException ex) {
                this.setConnectionView();
            }

            tabbedPane.addTab("Rätten i detalj", dishDetailView);
            tabbedPane.revalidate();
            tabbedPane.setSelectedComponent(dishDetailView);
        }

        @Override
        public void setViewInventory() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void setViewUsers() {
            tabbedPane.setSelectedIndex(4);
        }

        @Override
        public void setViewNewBooking(Booking b) {
            newBooking.newBooking(b, remote);
            //tabbedPane.addTab("Bokning i detaij", newBooking);
                    newBookingsScrollPane = new JScrollPane(newBooking, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                    newBookingsScrollPane.setMinimumSize(new Dimension(500,700));
                    tabbedPane.addTab("Bokning i detalj", newBookingsScrollPane);
                    newBooking.setBorder(new EmptyBorder(10, 10, 10, 10));

            newBookingsScrollPane.revalidate();
            tabbedPane.setSelectedComponent(newBookingsScrollPane);
        }
        
        @Override
        public void setViewBookings() {
            tabbedPane.setSelectedIndex(6);
        }
        
        @Override
        public void setConnectionView() {
            Container parent = SharedTabs.this.getParent();
            parent.add(new LoginPage());
            parent.remove(SharedTabs.this);
            parent.revalidate();
            parent.repaint();
        }
        
        /**
         * adds a "*" in the tab of tabView if savedState is false otherwise the
         * original name
         * @param tabView    - The view for us to set the saved state of
         * @param savedState - The state to set in the tab itself
         */
        public void setSavedTab(JComponent tabView, boolean savedState) {
            int tabIndex = 0;
            for(JComponent tabToChange : panels) {
                if(tabToChange == tabView) {
                        showStar(savedState,tabIndex);
                    return;
                }
                tabIndex++;
            }
            showStar(savedState,tabIndex);
        }
          
        private void showStar(boolean savedState,int tabIndex){
            if(!savedState) {
                String c = tabbedPane.getTitleAt(tabIndex);
                if(c.charAt(c.length()-1) != '*')
                tabbedPane.setTitleAt(tabIndex, tabbedPane.getTitleAt(tabIndex) + "*" );
            }
            else {
                String c = tabbedPane.getTitleAt(tabIndex);
                if(c.charAt(c.length()-1) == '*')
                    tabbedPane.setTitleAt(tabIndex, c.substring(0,c.length()-1));
            }
        }
             
    };

    public SharedTabs() throws DataSource.WrongKeyException {
        dishDetailView = new DishDetailPanel(null, remote);
        dishesPanel = new DishesPanel(remote);

        setLayout(new BorderLayout());
        dishesPanel.setViewSwitch(remote);
        panels.add(dishesPanel);
        JComponent panel2 = new WebsitePanel(remote);
        panels.add(panel2);
        InventoryPanel panel3 = new InventoryPanel();
        panels.add(panel3);
        UsersPanel panel4 = new UsersPanel(remote);
        panels.add(panel4);
        MenuPanel panel5 = new MenuPanel(remote, Settings.aLaCarte);
        panels.add(panel5);
        MenuPanel panel6 = new MenuPanel(remote, Settings.weekDays);
        panels.add(panel6);
        BookingsPanel panel7 = new BookingsPanel(remote);
        panels.add(panel7);
        SchedulesPanel panel8 = new SchedulesPanel();
        panels.add(panel8);
        String[] titles = {"Rätter", "Hemsida", "Inventarie", "Användare", "A La Carté", "Veckans Meny", "Bokningar", "Schema"};
        int i = 0;
        for (JComponent panel : panels) {
            JScrollPane sp = new JScrollPane(panel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            sp.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
            sp.setMinimumSize(new Dimension(700,700));
            //sp.add(panel);
            panel.setBorder(new EmptyBorder(10, 10, 10, 10));
            tabbedPane.addTab(titles[i] + " ", sp);
            i++;
        }

        tabbedPane.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent ce) {
                for (JComponent j : panels) {
                    j.revalidate();
                }
                if (tabbedPane.isAncestorOf(newBookingsScrollPane) && tabbedPane.getSelectedComponent() != newBookingsScrollPane) {
                    tabbedPane.remove(newBookingsScrollPane);
                }
                if (tabbedPane.isAncestorOf(dishDetailView) && tabbedPane.getSelectedComponent() != dishDetailView) {
                    tabbedPane.remove(dishDetailView);
                }
            }
        });

        //Add the tabbed pane to this panel.
        add(tabbedPane);

        //The following line enables to use scrolling tabs.
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
    }

    /**
     * Create the GUI and show it. For thread safety, this method should be
     * invoked from the event dispatch thread.
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
        frame.setMinimumSize(new Dimension(700, 700));
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
        
        UIManager.put("FormattedTextField.background", Styles.fieldColor);
        UIManager.put("FormattedTextField.font", new Font("Calibri", Font.PLAIN, 22));
        
        UIManager.put("TextField.font", new Font("Calibri", Font.PLAIN, 32));
        UIManager.put("TextField.background", Styles.fieldColor);
        UIManager.put("TextField.selectionBackground", Color.RED);
        UIManager.put("TextField.selectionForeground", Color.WHITE);
        UIManager.put("TextField.caretForeground", Color.black);

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

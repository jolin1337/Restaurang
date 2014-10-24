package se.miun.dt142g;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
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
import javax.swing.plaf.ColorUIResource;
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
 * This class is the main class for this project on desktop side. It is used for
 * displaying various of panels for editing everything on the system
 *
 * @author Tomas
 */
public class MainDT142GStarter extends JPanel {

    /**
     * The special view for displaying details.
     */
    private final DishDetailPanel dishDetailView;
    /**
     * The special view for adding a new reservation
     */
    private final NewBooking newBooking;
    /**
     * The tab menu panel that enables us to convenient switch between edit
     * views
     */
    private final JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
    /**
     * A container for variaous of editing panels (assuming JPanel or ScrollPane
     * but others are ok as well)
     */
    private final List<JComponent> panels = new ArrayList<>();
    /**
     * A view for dishespanel made a variable for it for easy access. Also
     * exists in panels list
     */
    private final DishesPanel dishesPanel;
    /**
     * This is the container scrollpane for new bookings (reservations)
     */
    private final JScrollPane newBookingsScrollPane;

    /**
     * This class own implementation of Controller/remote for all the views
     * currently existing
     */
    Controller remote = new Controller() {

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
        public void setViewDishes(boolean saved) {
            setViewDishes();
            setSavedTab(dishesPanel, saved);
        }

        @Override
        public void setViewDishes() {
            // updates the textfields of dishes view if something has happend 
            // since last visit
            dishesPanel.updateTextFieldContents();
            // Set selected tab to be the dishespanel tab
            tabbedPane.setSelectedIndex(1);
        }

        @Override
        public void setViewWebsite() {
            tabbedPane.setSelectedIndex(2);
        }

        @Override
        public void setViewInventory() {
            tabbedPane.setSelectedIndex(3);
        }

        @Override
        public void setViewNewBooking(Booking b) {
            newBooking.newBooking(b, remote);
            tabbedPane.addTab("Bokning i detalj", newBookingsScrollPane);

            newBookingsScrollPane.revalidate();
            tabbedPane.setSelectedComponent(newBookingsScrollPane);
        }

        @Override
        public void setViewUsers() {
            tabbedPane.setSelectedIndex(5);
        }

        @Override
        public void setViewBookings() {
            tabbedPane.setSelectedIndex(7);
        }

        @Override
        public void setConnectionView() {
            Container parent = MainDT142GStarter.this.getParent();
            parent.add(new LoginPage());
            parent.remove(MainDT142GStarter.this);
            parent.revalidate();
            parent.repaint();
        }

        /**
         * adds a "*" in the tab of tabView if savedState is false otherwise the
         * original name
         *
         * @param tabView - The view for us to set the saved state of
         * @param savedState - The state to set in the tab itself
         */
        @Override
        public void setSavedTab(JComponent tabView, boolean savedState) {
            int tabIndex = 0;
            for (JComponent tabToChange : panels) {
                if (tabToChange == tabView) {
                    showStar(savedState, tabIndex);
                    return;
                }
                tabIndex++;
            }
            showStar(savedState, tabIndex);
        }

        /**
         * Sets an astrix in the tab name to indicate unsaved modifications were
         * found
         *
         * @param savedState the state whether it's been saved or not
         * @param tabIndex The corresponding tab
         */
        private void showStar(boolean savedState, int tabIndex) {
            if (!savedState) {
                String c = tabbedPane.getTitleAt(tabIndex);
                if (c.charAt(c.length() - 1) != '*') {
                    tabbedPane.setTitleAt(tabIndex, tabbedPane.getTitleAt(tabIndex) + "*");
                }
            } else {
                String c = tabbedPane.getTitleAt(tabIndex);
                if (c.charAt(c.length() - 1) == '*') {
                    tabbedPane.setTitleAt(tabIndex, c.substring(0, c.length() - 1));
                }
            }
        }

    };

    /**
     * Constructs all the views in tabs
     *
     * @throws se.miun.dt142g.DataSource.WrongKeyException if it was unable to
     * load/sync data from server
     */
    public MainDT142GStarter() throws DataSource.WrongKeyException {
        setLayout(new BorderLayout());

        dishDetailView = new DishDetailPanel(null, remote);
        newBooking = new NewBooking();
        newBooking.setBorder(new EmptyBorder(10, 10, 10, 10));

        newBookingsScrollPane = new JScrollPane(newBooking, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        newBookingsScrollPane.setMinimumSize(new Dimension(500, 700));

        JComponent panel1 = new FrontPanel();
        panels.add(panel1);
        dishesPanel = new DishesPanel(remote);
        dishesPanel.setViewSwitch(remote);
        panels.add(dishesPanel);

        JComponent panel2 = new WebsitePanel(remote);
        panels.add(panel2);
        InventoryPanel panel3 = new InventoryPanel(remote);
        panels.add(panel3);
        UsersPanel panel4 = new UsersPanel(remote);
        panels.add(panel4);
        MenuPanel panel5 = new MenuPanel(remote, Settings.aLaCarte);
        panels.add(panel5);
        MenuPanel panel6 = new MenuPanel(remote, Settings.weekDays);
        panels.add(panel6);
        BookingsPanel panel7 = new BookingsPanel(remote);
        panels.add(panel7);
        /**
         * If the scheduler works we uncomment this code and everything is awesome!
         * SchedulesPanel panel8 = new SchedulesPanel();
         * panels.add(panel8);
         */
        int i = 0;
        for (JComponent panel : panels) {
            JScrollPane sp = new JScrollPane(panel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            sp.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
            sp.setMinimumSize(new Dimension(700, 700));
            //sp.add(panel);
            panel.setBorder(new EmptyBorder(10, 10, 10, 10));
            tabbedPane.addTab(Settings.Strings.tabTitles[i] + " ", sp);
            i++;
        }

        // This event listener is used for deleting the temporary tabs (special ones)
        tabbedPane.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent ce) {
                // Revalidate all tabs
                for (JComponent j : panels) {
                    j.revalidate();
                }

                // Remove the special tabs if they are visible
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
        ImageIcon img = new ImageIcon(Settings.Strings.logoSrc);
        frame.setIconImage(img.getImage());

        try {
            MainDT142GStarter st = new MainDT142GStarter();
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

        UIManager.put("Spinner.background", Styles.fieldColor);

        UIManager.put("TabbedPane.background", Styles.fieldColor.brighter());
        UIManager.put("TabbedPane.foreground", Styles.btnBackground.darker().darker().darker());
        UIManager.put("TabbedPane.selected", Styles.fieldColor);
        
        UIManager.put("TabbedPane.contentAreaColor ", Color.GREEN);
        UIManager.put("TabbedPane.borderColor", Color.RED);
        UIManager.put("TabbedPane.darkShadow", Color.TRANSLUCENT);
        UIManager.put("TabbedPane.light", Color.RED);
        UIManager.put("TabbedPane.focus", Color.LIGHT_GRAY);
        UIManager.put("TabbedPane.selectHighlight", Color.RED);
        UIManager.put("TabbedPane.borderHightlightColor", Color.TRANSLUCENT); 
        UIManager.put("TabbedPane.contentBorderInsets", new Insets(0, 0, 0, 0));
        
        
        UIManager.put("TitledBorder.font", new Font("Calibri", Font.PLAIN, 22));
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

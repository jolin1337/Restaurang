/*
 * This code is created for one purpose only. And should not be used for any 
 * other purposes unless the author of this file has apporved. 
 * 
 * This code is a piece of a project in the course DT142G on Mid. Sweden university
 * Created by students for this projekt only
 */
package se.miun.dt142g.food;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import se.miun.dt142g.Controller;
import se.miun.dt142g.ConfirmationBox;
import se.miun.dt142g.DataSource;
import se.miun.dt142g.Settings;
import se.miun.dt142g.data.Dish;

/**
 * This class displays a jpanel containing a list item with a detail button
 *
 * @author Johannes Lindén
 * @since 2014-10-11
 * @version 1.3
 */
public class DishPanel extends JPanel {

    /**
     * A boolean to verify the status of whether the panel is removed or not
     */
    boolean isRemovedPanel = false;
    /**
     * An instance to the dish class
     */
    Dish dish;
    /**
     * A textfield for the dish name
     */
    JTextField name;
    /**
     * An instance to the controller class
     */
    Controller remote = null;
    /**
     * A new dishgroup instance
     */
    DishGroups dishGroups = new DishGroups();

    /**
     * Default constructor which initiates all components
     *
     * @param dish - The dish to initiate
     * @param c - The controller instance
     */
    public DishPanel(Dish dish, final Controller c) {
        remote = c;
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setBackground(Settings.Styles.applicationBgColor);
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 65));
        this.dish = dish;
        JPanel item = new JPanel(new BorderLayout());
        item.setBackground(Settings.Styles.applicationBgColor);
        JButton remove = new JButton("X");
        add(remove, BorderLayout.WEST);
        name = new JTextField();
        name.setText(dish.getName());
        name.setMaximumSize(new Dimension(Integer.MAX_VALUE, 65));
        name.addKeyListener(textFieldKeyListener);
        add(name, BorderLayout.CENTER);
        JButton detail = new JButton("Detaljer");
        detail.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                if (c != null) {
                    updateDishName();
                    c.setViewDishDetail(DishPanel.this.dish, removeEvent);
                }
            }
        });
        remove.addActionListener(removeEvent);
        //detail.setMaximumSize(new Dimension(Integer.MAX_VALUE, detail.getPreferredSize().height));
        add(detail, BorderLayout.EAST);
        // item.setBorder(new EmptyBorder(10,10,10,10));
        //add(item);

    }

    /**
     * A setter for the name textfield, value retrieved from the dish
     */
    public void updateTextFieldContent() {
        name.setText(dish.getName());
    }

    /**
     * A setter for the dish name, value retrieved from the textfield
     */
    public void updateDishName() {
        dish.setName(name.getText());
    }

    /**
     * Getter for a dish
     *
     * @return this dish
     */
    public Dish getDish() {
        return dish;
    }

    /**
     * A status function to verify whether the panel has been removed or not
     *
     * @return true if the panel has been removed, otherwise false
     */
    boolean isRemoved() {
        return isRemovedPanel;
    }

    ActionListener removeEvent = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent ae) {
            try {
                dishGroups.loadData();
            } catch (DataSource.WrongKeyException ex) {
                Logger.getLogger(DishPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
            List<String> groupNames = dishGroups.getDishGroups(dish.getId());
            if (!groupNames.isEmpty()) {
                String toDialog = "Kan inte ta bort rätten,\nrätten används i:\n";
                for (String s : groupNames) {
                    toDialog += s + "\n";
                }
                JOptionPane.showMessageDialog(DishPanel.this, toDialog);
            } else {
                int n = ConfirmationBox.confirm(DishPanel.this, "Ta bort " + name.getText() + "?");
                if (n == 0) {
                    isRemovedPanel = true;
                    Container parent = DishPanel.this.getParent();
                    remote.setSavedTab((JComponent) DishPanel.this.getParent(), false);
                    parent.remove(DishPanel.this);
                    parent.revalidate();
                    parent.repaint();

                    if (remote != null) {
                        remote.setViewDishes();
                    }
                }
            }
        }
    };
    KeyListener textFieldKeyListener = new KeyListener() {

        @Override
        public void keyTyped(KeyEvent ke) {
            remote.setSavedTab((JComponent) DishPanel.this.getParent(), false);
        }

        @Override
        public void keyPressed(KeyEvent ke) {
        }

        @Override
        public void keyReleased(KeyEvent ke) {
        }
    };
}

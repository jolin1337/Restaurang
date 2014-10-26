/*
 * This code is created for one purpose only. And should not be used for any 
 * other purposes unless the author of this file has apporved. 
 * 
 * This code is a piece of a project in the course DT142G on Mid. Sweden university
 * Created by students for this projekt only
 */
package se.miun.dt142g.food;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import se.miun.dt142g.Controller;
import se.miun.dt142g.DataSource;
import se.miun.dt142g.Settings;
import se.miun.dt142g.data.Dish;

/**
 * This class displays a jpanel containing a list of dishes to edit
 *
 * @author Johannes Lindén
 * @since 2014-10-11
 * @version 1.3
 */
public class DishesPanel extends JPanel {

    /**
     * A list of all dishpanels
     */
    List<DishPanel> dishPanels = new ArrayList<>();
    /**
     * Dishes collection
     *
     * @see Dishes.java
     */
    Dishes dishes = new Dishes();
    /**
     * Button object for adding another dish
     */
    JButton addDishBtn = new JButton("Lägg till rätt");
    /**
     * Button object for synchronizing with server
     */
    JButton submitBtn = new JButton(Settings.Strings.submit);
    /**
     * Controller object to enable changing tab and adding asterisk for not
     * saved status
     */
    private Controller remote = null;

    /**
     * Initiates all components
     *
     * @param c - The controller instance
     * @throws se.miun.dt142g.DataSource.WrongKeyException
     */
    public DishesPanel(Controller c) throws DataSource.WrongKeyException {
        this.remote = c;
        dishes.dbConnect();
        dishes.loadData();

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Settings.Styles.applicationBgColor);
        JButton addDishBtnTop = new JButton("Lägg till rätt");
        addDishBtnTop.addActionListener(addNewDishEvent);
        add(addDishBtnTop);
        addDishBtnTop.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));

        JButton submitBtnTop = new JButton(Settings.Strings.submit);
        submitBtnTop.addActionListener(submitEvent);
        add(submitBtnTop);
        submitBtnTop.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));

        for (Dish dish : dishes) {
            DishPanel dp = new DishPanel(dish, remote);
            add(dp);
            dishPanels.add(dp);
        }
        addDishBtn.addActionListener(addNewDishEvent);
        add(addDishBtn);
        addDishBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));

        submitBtn.addActionListener(submitEvent);
        add(submitBtn);
        submitBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
    }

    /**
     * Sets the controller variable remote
     *
     * @param c the controller object
     */
    public void setViewSwitch(Controller c) {
        this.remote = c;
    }

    /**
     * Updates all textfields in this DishesPanel object
     */
    public void updateTextFieldContents() {
        for (DishPanel d : dishPanels) {
            d.updateTextFieldContent();
        }
    }

    /**
     * Actionlistener object for synchronizing with server, this object is added
     * as action listener for the submit button
     */
    ActionListener submitEvent = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent ae) {
            dishes.clear();
            List<DishPanel> dishesToRemove = new ArrayList<>();
            for (DishPanel dp : dishPanels) {
                if (dp.isRemoved()) {
                    dishesToRemove.add(dp);
                    continue;
                }
                dp.updateDishName();
                dishes.addDish(dp.getDish());
            }
            for (DishPanel dp : dishesToRemove) {
                dishPanels.remove(dp);
            }
            try {
                dishes.update();
                remote.setSavedTab(DishesPanel.this, true);
            } catch (DataSource.WrongKeyException ex) {
                JOptionPane.showMessageDialog(DishesPanel.this,
                        Settings.Strings.serverConnectionError,
                        "Server error",
                        JOptionPane.ERROR_MESSAGE);
                if (remote != null) {
                    remote.setConnectionView();
                }
            }
        }
    };

    /**
     * action listener object for adding a new dish, this object is added as
     * action listener for the button for adding a new dish.
     */
    ActionListener addNewDishEvent = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent ae) {
            Dish dish = new Dish(-1, "", 0.0f, null);
            DishPanel dp = new DishPanel(dish, remote);
            remove(addDishBtn);
            remove(submitBtn);
            add(dp);

            add(addDishBtn);
            add(submitBtn);
            dishPanels.add(dp);
            remote.setSavedTab(DishesPanel.this, false);
            DishesPanel.this.revalidate();
        }
    };
}

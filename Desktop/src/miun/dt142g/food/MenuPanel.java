/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package miun.dt142g.food;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import miun.dt142g.Controller;
import miun.dt142g.data.Dish;

/**
 *
 * @author Tomas
 */
public class MenuPanel extends JPanel {

    List<SingleDishPanel> dishPanels = new ArrayList<>();
    DishGroups dishGroups = new DishGroups();
    JButton addDishBtn;
    private Controller fjarr = null;
    private ActionListener addDishBtnListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent ae) {
            SingleDishPanel dp = new SingleDishPanel(new DishGroup(dishGroups.getUniqueId(), -1, ""), fjarr);
            remove(addDishBtn);
            add(dp);
            add(addDishBtn);
            dishPanels.add(dp);
            MenuPanel.this.revalidate();
        }
    };

    public MenuPanel(Controller c, String[] groupNames) {
        this.fjarr = c;
        dishGroups.dbConnect();
        dishGroups.loadData();

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.white);
        setBorder(new EmptyBorder(10, 10, 10, 10));
        List<DishGroup> dishList = dishGroups.getDishesInGroup(groupNames);
        String previous = "";
        JPanel groupContainer = new JPanel();
        for (DishGroup dishGroup : dishList) {
            if (previous.isEmpty()) {
                JLabel groupTitle = new JLabel(dishGroup.getGroup());
                add(groupTitle);
                previous = dishGroup.getGroup();
            } else if (!previous.equals(dishGroup.getGroup())) {
                addDishBtn = new JButton("Lägg till rätt");
                addDishBtn.addActionListener(addDishBtnListener);
                add(addDishBtn);
                addDishBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
            }
            if (!previous.equals(dishGroup.getGroup())) {
                JLabel groupTitle = new JLabel(dishGroup.getGroup());
                add(groupTitle);
                previous = dishGroup.getGroup();
            }
            SingleDishPanel dp = new SingleDishPanel(dishGroup, fjarr);
            add(dp);
            dishPanels.add(dp);
        }
        addDishBtn = new JButton("Lägg till rätt");
        addDishBtn.addActionListener(addDishBtnListener);
        add(addDishBtn);
        addDishBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));

    }

    public void setViewSwitch(Controller c) {
        this.fjarr = c;
    }

    class SingleDishPanel extends JPanel {

        DishGroup dish;

        public SingleDishPanel(DishGroup dish, final Controller c) {
            this.dish = dish;

            /**
             * Set layout
             */
            setLayout(new BorderLayout());
            setBorder(new EmptyBorder(10, 10, 10, 10));
            setBackground(Color.white);
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
            /**
             * Create removebutton
             */
            JButton remove = new JButton("X");
            add(remove, BorderLayout.WEST);
            /**
             * Create combobox - Inefficient to iterate through list every time
             */
            JComboBox myComboBox = new JComboBox();
            for (Dish d : dishGroups.getDishes()) {
                myComboBox.addItem(d);
            }
            myComboBox.setSelectedIndex(dish.getDishId());

            myComboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
            add(myComboBox, BorderLayout.CENTER);

            remove.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent ae) {
                    Container parent = SingleDishPanel.this.getParent();
                    parent.remove(SingleDishPanel.this);
                    parent.revalidate();
                }
            });
        }
    }
}

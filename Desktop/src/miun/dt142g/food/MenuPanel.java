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
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import miun.dt142g.Controller;
import miun.dt142g.DataSource;
import miun.dt142g.ConfirmationBox;
import miun.dt142g.Settings;
import miun.dt142g.data.Dish;

/**
 *
 * @author Tomas
 */
public class MenuPanel extends JPanel {

    DishGroups dishGroups = new DishGroups();
    JButton addDishBtn;
    private Controller fjarr = null;
    private ActionListener addDishBtnListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent ae) {
            if (ae.getSource() instanceof JButton) {
                Dishes dishes = dishGroups.getDishes();
                try {
                    dishes.loadData();
                } catch (DataSource.WrongKeyException ex) {
                    JOptionPane.showMessageDialog(MenuPanel.this,
                        Settings.Strings.serverConnectionError,
                        "Server error",
                        JOptionPane.ERROR_MESSAGE);
                }
                if(dishes.getRows() <= 0) {
                    
                    JOptionPane.showMessageDialog(MenuPanel.this,
                        Settings.Strings.noDishesCreatedError,
                        "Inga tillgängliga rätter",
                        JOptionPane.WARNING_MESSAGE);
                }
                JButton actionButton = (JButton) ae.getSource();
                JPanel groupContainer = (JPanel) actionButton.getParent();
                
                
                SingleDishPanel dp = new SingleDishPanel(dishes.getDishByIndex(0), fjarr);
                
                
                groupContainer.remove(actionButton);
                groupContainer.add(dp);
                groupContainer.add(actionButton);
                MenuPanel.this.revalidate();
            }
        }
    };

    public MenuPanel(Controller c, String[] groupNames) throws DataSource.WrongKeyException {
        this.fjarr = c;
        dishGroups.dbConnect();
        dishGroups.loadData();

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.white);
        List<DishGroup> dishList = dishGroups.getDishGroups(groupNames);
        String previous = "";
        for (DishGroup dishGroup : dishList) {
            
            JLabel groupTitle = new JLabel(dishGroup.getGroup());
            add(groupTitle);
            
            JPanel groupContainer = new JPanel();
            groupContainer.setLayout(new BoxLayout(groupContainer, BoxLayout.Y_AXIS));
            groupContainer.setBackground(Color.white);
            for(Integer dishId : dishGroup.getDishes()) {
                Dish dish = dishGroups.getDishes().getDish(dishId);
                SingleDishPanel dp = new SingleDishPanel(dish, fjarr);
                groupContainer.add(dp);
            }
            addDishBtn = new JButton("Lägg till rätt");
            addDishBtn.addActionListener(addDishBtnListener);
            addDishBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, addDishBtn.getPreferredSize().height));
            groupContainer.add(addDishBtn);
            add(groupContainer);
                /*
            if (previous.isEmpty()) {
                JLabel groupTitle = new JLabel(dishGroup.getGroup());
                groupContainer.add(groupTitle);
                previous = dishGroup.getGroup();
            } else if (!previous.equals(dishGroup.getGroup())) {
                addDishBtn = new JButton("Lägg till rätt");
                addDishBtn.addActionListener(addDishBtnListener);
                addDishBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, addDishBtn.getPreferredSize().height));
                groupContainer.add(addDishBtn);
                
                groupContainer = new JPanel();
                groupContainer.setLayout(new BoxLayout(groupContainer, BoxLayout.Y_AXIS));
                groupContainer.setBackground(Color.white);
            }
            if (!previous.equals(dishGroup.getGroup())) {
                JLabel groupTitle = new JLabel(dishGroup.getGroup());
                groupContainer.add(groupTitle);
                previous = dishGroup.getGroup();
            }
            SingleDishPanel dp = new SingleDishPanel(dishGroup, fjarr);
            groupContainer.add(dp);
            add(groupContainer);*/
        }
        /*addDishBtn = new JButton("Lägg till rätt");
        addDishBtn.addActionListener(addDishBtnListener);
        addDishBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, addDishBtn.getPreferredSize().height));
        groupContainer.add(addDishBtn);*/
        
        add(Box.createGlue());
    }

    public void setViewSwitch(Controller c) {
        this.fjarr = c;
    }

    class SingleDishPanel extends JPanel {

        Dish dish;
        JComboBox myComboBox;
        public SingleDishPanel(Dish dish, final Controller c) {
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
            myComboBox = new JComboBox();
            populateComboBox(dish);

            myComboBox.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent ae) {
                    try {
                        dishGroups.getDishes().loadData();
                    } catch (DataSource.WrongKeyException ex) {
                        JOptionPane.showMessageDialog(MenuPanel.this,
                            Settings.Strings.serverConnectionError,
                            "Server error",
                            JOptionPane.ERROR_MESSAGE);
                    }
                    myComboBox.removeAllItems();
                    populateComboBox(SingleDishPanel.this.dish);
                }
            });
            myComboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
            add(myComboBox, BorderLayout.CENTER);

            remove.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent ae) {
                    int n = ConfirmationBox.confirm(SingleDishPanel.this, myComboBox.getSelectedItem().toString());
                    if(n == 0){
                        Container parent = SingleDishPanel.this.getParent();
                        parent.remove(SingleDishPanel.this);
                        parent.revalidate();
                    }
                }
            });
        }
        
        final void populateComboBox(Dish dish) {
            for (Dish d : dishGroups.getDishes()) {
                myComboBox.addItem(d);
                if(d.equals(dish))
                    myComboBox.setSelectedItem(d);
            }
        }
    }
}

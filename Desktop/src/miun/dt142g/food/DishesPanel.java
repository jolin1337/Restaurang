/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package miun.dt142g.food;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import miun.dt142g.Controller;
import miun.dt142g.DataSource;
import miun.dt142g.Settings;
import miun.dt142g.data.Dish;
import miun.dt142g.website.WebsitePanel;

/**
 *
 * @author Johannes
 */
public class DishesPanel extends JPanel {

    List<DishPanel> dishPanels = new ArrayList<>();
    Dishes dishes = new Dishes();
    JButton addDishBtn = new JButton("Lägg till rätt");
    JButton submitBtn = new JButton(Settings.Strings.submit);
    private Controller remote = null;

    public DishesPanel(Controller c) throws DataSource.WrongKeyException {
        this.remote = c;
        dishes.dbConnect();
        dishes.loadData();

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.white);
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

    public void setViewSwitch(Controller c) {
        this.remote = c;
    }

    public void updateTextFieldContents(){
        for(DishPanel d: dishPanels){
            d.updateTextFieldContent();
        }
    }
    
    ActionListener submitEvent = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                dishes.clear();
                List<DishPanel> dishesToRemove = new ArrayList<>();
                for(DishPanel dp : dishPanels) {
                    if(dp.isRemoved()) {
                        dishesToRemove.add(dp);
                        continue;
                    }
                    dp.updateDishName();
                    dishes.addDish(dp.getDish());
                }
                for(DishPanel dp : dishesToRemove){
                        dishPanels.remove(dp);
                }
                try {
                    dishes.update();
                } catch (DataSource.WrongKeyException ex) {
                    JOptionPane.showMessageDialog(DishesPanel.this,
                        Settings.Strings.serverConnectionError,
                        "Server error",
                        JOptionPane.ERROR_MESSAGE);
                    if(remote != null)
                        remote.setConnectionView();
                }
            }
        };
    ActionListener addNewDishEvent = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                Dish dish = new Dish(dishes.getUniqueId(), "", 0.0f, null);
                DishPanel dp = new DishPanel(dish, remote);
                remove(addDishBtn);
                remove(submitBtn);
                add(dp);
                
                add(addDishBtn);
                add(submitBtn);
                dishPanels.add(dp);
                DishesPanel.this.revalidate();
            }
        };
}

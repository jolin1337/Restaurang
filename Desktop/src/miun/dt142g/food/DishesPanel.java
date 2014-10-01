/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package miun.dt142g.food;

import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.TextField;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import miun.dt142g.data.Dish;

/**
 *
 * @author Johannes
 */
public class DishesPanel extends JPanel {
    List<DishPanel> dishPanels = new ArrayList<>();
    Dishes dishes = new Dishes();
    public DishesPanel() {
        dishes.dbConnect();
        dishes.loadData();
        
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.white);
        setBorder(new EmptyBorder(10,10,10,10));
        for(Dish dish : dishes) {
            DishPanel dp = new DishPanel(dish);
            add(dp);
            dishPanels.add(dp);
        }
    }
}

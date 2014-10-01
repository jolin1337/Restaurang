/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package miun.dt142g.food;

import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import miun.dt142g.data.Dish;
import miun.dt142g.data.Ingredient;

/**
 *
 * @author Johannes
 */
public class DishDetailPanel extends JPanel {
    Dish dish;
    Inventory inv = new Inventory();
    public DishDetailPanel(Dish dish) {
        inv.dbConnect();
        inv.loadData();
        setBackground(Color.white);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        
        addLabel("Namn");
        addTextField(dish.getName());
        
        addLabel("Ingredienser");
        for(Ingredient ing : inv) {
            Button remove = new Button("X");
            JComboBox jListInventory;
            JTextField ingEdit = new JTextField(ing.getName());
            JPanel horiView = new JPanel();
            horiView.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
            horiView.setLayout(new BoxLayout(horiView, BoxLayout.LINE_AXIS));
            horiView.add(remove);
            horiView.add(ingEdit);
            add(horiView);
        }
        
        addLabel("Pris");
        addTextField(Float.toString(dish.getPrice()));
    }
    private void addLabel(String labelName) {
        JLabel label = new JLabel("<html><div style='margin: 10px 0 3px 3px;'>" + labelName + "</div></html>");
        Box  fixHeight = Box.createHorizontalBox();
        fixHeight.add( label );
        fixHeight.add( Box.createHorizontalGlue() );
        add(fixHeight);   
    }
    private void addTextField(String textName) {
        JTextField textField = new JTextField(textName);
        textField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        add(textField);   
    }
}

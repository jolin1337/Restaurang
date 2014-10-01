/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package miun.dt142g.food;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Label;
import java.awt.TextField;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import miun.dt142g.data.Dish;

/**
 *
 * @author Johannes
 */
public class DishDetailPanel extends JPanel {
    Dish dish;
    public DishDetailPanel(Dish dish) {
        setBackground(Color.white);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        
        addLabel("Namn");
        addTextField(dish.getName());
        
        addLabel("Ingredienser");
        
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

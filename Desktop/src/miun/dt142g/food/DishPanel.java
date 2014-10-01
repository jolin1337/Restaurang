/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package miun.dt142g.food;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.TextField;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import miun.dt142g.data.Dish;

/**
 *
 * @author Johannes
 */
public class DishPanel extends JPanel {
    Dish dish;
    public DishPanel(Dish dish) {
        setBackground(Color.white);
        this.dish = dish;
        JPanel item = new JPanel(new FlowLayout());
        item.setBackground(Color.white);
        Button remove = new Button("X");
        item.add(remove, FlowLayout.LEFT);
        TextField name = new TextField();
        name.setText(dish.getName());
        name.setPreferredSize(new Dimension(200, 25));
        item.add(name, FlowLayout.CENTER);
        Button detail = new Button("Detaljer");
        //detail.setMaximumSize(new Dimension(Integer.MAX_VALUE, detail.getPreferredSize().height));
        item.add(detail, FlowLayout.RIGHT);
       // item.setBorder(new EmptyBorder(10,10,10,10));
        add(item);
    }
}

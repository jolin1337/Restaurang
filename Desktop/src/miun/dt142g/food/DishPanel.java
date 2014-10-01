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
import javax.swing.BorderFactory;
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
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(10,10,10,10));
        setBackground(Color.white);
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        this.dish = dish;
        JPanel item = new JPanel(new BorderLayout());
        item.setBackground(Color.white);
        Button remove = new Button("X");
        add(remove, BorderLayout.WEST);
        TextField name = new TextField();
        name.setText(dish.getName());
        name.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        add(name, BorderLayout.CENTER);
        Button detail = new Button("Detaljer");
        //detail.setMaximumSize(new Dimension(Integer.MAX_VALUE, detail.getPreferredSize().height));
        add(detail, BorderLayout.EAST);
       // item.setBorder(new EmptyBorder(10,10,10,10));
        //add(item);
    }
}

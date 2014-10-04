/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.I
 */
package miun.dt142g.food;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import miun.dt142g.Controller;
import miun.dt142g.data.Dish;

/**
 *
 * @author Johannes
 */
public class DishPanel extends JPanel {
    Dish dish;
    public DishPanel(Dish dish, final Controller c) {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(10,10,10,10));
        setBackground(Color.white);
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        this.dish = dish;
        JPanel item = new JPanel(new BorderLayout());
        item.setBackground(Color.white);
        JButton remove = new JButton("X");
        add(remove, BorderLayout.WEST);
        JTextField name = new JTextField();
        name.setText(dish.getName());
        name.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        add(name, BorderLayout.CENTER);
        JButton detail = new JButton("Detaljer");
        detail.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                if(c != null)
                    c.setViewDishDetail(DishPanel.this.dish);
            }
        });
        remove.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                Container parent = DishPanel.this.getParent();
                parent.remove(DishPanel.this);
                parent.revalidate();
            }
        });
        //detail.setMaximumSize(new Dimension(Integer.MAX_VALUE, detail.getPreferredSize().height));
        add(detail, BorderLayout.EAST);
       // item.setBorder(new EmptyBorder(10,10,10,10));
        //add(item);
    }
}

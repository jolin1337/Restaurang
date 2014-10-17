/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.I
 */
package se.miun.dt142g.food;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import se.miun.dt142g.Controller;
import se.miun.dt142g.ConfirmationBox;
import se.miun.dt142g.data.Dish;
import se.miun.dt142g.website.EventPostPanel;

/**
 *
 * @author Johannes
 */
public class DishPanel extends JPanel {

    boolean isRemovedPanel = false;
    Dish dish;
    JTextField name;
    Controller remote = null;

    public DishPanel(Dish dish, final Controller c) {
        remote = c;
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setBackground(Color.white);
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        this.dish = dish;
        JPanel item = new JPanel(new BorderLayout());
        item.setBackground(Color.white);
        JButton remove = new JButton("X");
        add(remove, BorderLayout.WEST);
        name = new JTextField();
        name.setText(dish.getName());
        name.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        name.addKeyListener(textFieldKeyListener);
        add(name, BorderLayout.CENTER);
        JButton detail = new JButton("Detaljer");
        detail.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                if (c != null) {
                    updateDishName();
                    c.setViewDishDetail(DishPanel.this.dish, removeEvent);
                }
            }
        });
        remove.addActionListener(removeEvent);
        //detail.setMaximumSize(new Dimension(Integer.MAX_VALUE, detail.getPreferredSize().height));
        add(detail, BorderLayout.EAST);
        // item.setBorder(new EmptyBorder(10,10,10,10));
        //add(item);

    }

    public void updateTextFieldContent() {
        name.setText(dish.getName());
    }

    public void updateDishName() {
        dish.setName(name.getText());
    }

    public Dish getDish() {
        return dish;
    }

    boolean isRemoved() {
        return isRemovedPanel;
    }
    ActionListener removeEvent = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent ae) {
            int n = ConfirmationBox.confirm(DishPanel.this, name.getText());
            if (n == 0) {
                isRemovedPanel = true;
                Container parent = DishPanel.this.getParent();
                remote.setSavedTab((JComponent)DishPanel.this.getParent(), false);
                parent.remove(DishPanel.this);
                parent.revalidate();
                parent.repaint();
                
                if(remote != null)
                    remote.setViewDishes();
            }
        }
    };
     KeyListener textFieldKeyListener = new KeyListener() {

        @Override
        public void keyTyped(KeyEvent ke) {
            remote.setSavedTab((JComponent)DishPanel.this.getParent(), false);
        }

        @Override
        public void keyPressed(KeyEvent ke) {
        }

        @Override
        public void keyReleased(KeyEvent ke) {
        }
    };
}

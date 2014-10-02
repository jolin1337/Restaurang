/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package miun.dt142g.food;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JComboBox;
import miun.dt142g.Controller;
import miun.dt142g.data.Dish;

/**
 *
 * @author Tomas
 */
public class AlaCartePanel extends JPanel {
    List<SingleDishPanel> dishPanels = new ArrayList<>();
    Dishes dishes = new Dishes();
    Button addDishBtn = new Button("Lägg till rätt");
    private Controller fjarr = null;
    public AlaCartePanel(Controller c) {
        this.fjarr = c;
        dishes.dbConnect();
        dishes.loadData();
        
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.white);
        setBorder(new EmptyBorder(10,10,10,10));
        for(Dish dish : dishes) {
            SingleDishPanel dp = new SingleDishPanel(dish, fjarr);
            add(dp);
            dishPanels.add(dp);
        }
        addDishBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                SingleDishPanel dp = new SingleDishPanel(new Dish(dishes.getUniqueId(), "", 0.0f, null), fjarr);
                remove(addDishBtn);
                add(dp);
                add(addDishBtn);
                dishPanels.add(dp);
                AlaCartePanel.this.revalidate();
            }
        });
        add(addDishBtn);
        addDishBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
    }
    
    public void setViewSwitch(Controller c) {
        this.fjarr = c;
    }
    
    class SingleDishPanel extends JPanel {

        Dish dish;

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
            Button remove = new Button("X");
            add(remove, BorderLayout.WEST);
            /**
             * Create combobox - Inefficient to iterate through list every time
             */
            JComboBox myComboBox = new JComboBox();
            for (Dish d : dishes) {
                myComboBox.addItem(d);
            }
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

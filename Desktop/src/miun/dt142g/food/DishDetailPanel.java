/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package miun.dt142g.food;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
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
    Dish dish=null;
    Inventory inv = new Inventory();
    JButton addIngBtn = new JButton("Lägg till ingrediens");
    JPanel ingredientsPanel = new JPanel();
    public DishDetailPanel(Dish dish) {
        inv.dbConnect();
        inv.loadData();
        setBackground(Color.white);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        ingredientsPanel.setLayout(new BoxLayout(ingredientsPanel, BoxLayout.Y_AXIS));
        if(dish == null)
            return;
        setDish(dish);
    }
    private JLabel addLabel(String labelName) {
        JLabel label = new JLabel("<html><div style='margin: 10px 0 3px 3px;'>" + labelName + "</div></html>");
        Box  fixHeight = Box.createHorizontalBox();
        fixHeight.add( label );
        fixHeight.add( Box.createHorizontalGlue() );
        add(fixHeight); 
        return label;
    }
    private JTextField addTextField(String textName) {
        JTextField textField = new JTextField(textName);
        textField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        add(textField);  
        return textField;
    }
    public final void setDish(Dish d) {
        removeAll();
        dish = d;
        addLabel("Namn");
        addTextField(dish.getName());
        
        addLabel("Ingredienser");
        List<Integer> ingredients = dish.getIngredients();
        if(ingredients != null)
            for(Integer ingredient : ingredients) {
                JButton remove = new JButton("X");
                remove.setMaximumSize(new Dimension(35, 35));
                remove.addActionListener(removeIngredientListener);
                JComboBox jListInventory = new JComboBox();
                for(Ingredient ing : inv) {
                    jListInventory.addItem(ing);
                    if(ing.getId() == ingredient) {
                        jListInventory.setSelectedItem(ing);
                    }       
                }
                //JTextField ingEdit = new JTextField(name);
                JPanel horiView = new JPanel();
                horiView.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
                horiView.setLayout(new BoxLayout(horiView, BoxLayout.LINE_AXIS));
                horiView.add(remove);
                horiView.add(jListInventory);
                ingredientsPanel.add(horiView);
            }
        ingredientsPanel.setMaximumSize(ingredientsPanel.getPreferredSize());
        add(ingredientsPanel);
        addIngBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        add(addIngBtn);
        addIngBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                JButton remove = new JButton("X");
                remove.setMaximumSize(new Dimension(35, 35));
                remove.addActionListener(removeIngredientListener);
                
                JComboBox jListInventory = new JComboBox();
                String name = "";
                for(Ingredient ing : inv) {
                    jListInventory.addItem(ing);
                }
                JPanel horiView = new JPanel();
                horiView.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
                horiView.setLayout(new BoxLayout(horiView, BoxLayout.LINE_AXIS));
                horiView.add(remove);
                horiView.add(jListInventory);
                ingredientsPanel.add(horiView);
                ingredientsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, ingredientsPanel.getPreferredSize().height));
                ingredientsPanel.revalidate();
            }
        });
        addLabel("Pris");
        addTextField(Float.toString(dish.getPrice()));
    }
    ActionListener removeIngredientListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent ae) {
            if(ae.getSource().getClass() != JButton.class)
                return;
            JButton btn = (JButton)ae.getSource();
            if(btn.getParent() == null)
                return;
            Container parent = btn.getParent().getParent();
            parent.remove(btn.getParent());
            ingredientsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, ingredientsPanel.getPreferredSize().height));
            parent.revalidate();
        }
    };
}

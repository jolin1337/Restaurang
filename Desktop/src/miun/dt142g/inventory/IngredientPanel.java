/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package miun.dt142g.inventory;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import miun.dt142g.data.Ingredient;

/**
 *
 * @author ulf
 */
public class IngredientPanel extends JPanel {
    
    private JButton close; 
    private JTextField ingredientName; 
    private JTextField amount; 
    private JLabel amountLabel; 
    private Ingredient ingredient; 
    
    public IngredientPanel(Ingredient ingredient){
        super(); 

        this.ingredient = ingredient; 
        this.ingredientName = new JTextField(ingredient.getName()); 
        this.amount = new JTextField(ingredient.getAmount());
        this.close = new JButton("X");
        this.amountLabel = new JLabel("Port."); 
        
        
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.setBackground(Color.white);
        this.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        
        amount.setColumns(3);
        amount.setMaximumSize(new Dimension(50,50));
        
        close.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                Container parent = IngredientPanel.this.getParent(); 
                parent.remove(IngredientPanel.this);
                parent.revalidate();
                
                //add remove from database

            }
        });
        
        this.add(close);
        this.add(ingredientName);
        this.add(amount);
        this.add(amountLabel);
        this.setVisible(true);
        
    }
}

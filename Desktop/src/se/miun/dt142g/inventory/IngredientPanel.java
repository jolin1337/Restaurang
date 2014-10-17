/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.miun.dt142g.inventory;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import se.miun.dt142g.ConfirmationBox;
import se.miun.dt142g.data.Ingredient;
import se.miun.dt142g.food.Inventory;

/**
 * JPanel for a single Ingredient. Fields update when their focus is lost. 
 * Remove button "X" removes the panel and ingredient after confirmation from 
 * user. 
 * 
 * @author ulf
 * @see Jpanel
 * @see FocusListener
 */
public class IngredientPanel extends JPanel implements FocusListener{
    
    private final JButton close; 
    private final JTextField ingredientName; 
    private final JTextField amount; 
    private final JLabel amountLabel; 
    private final Ingredient ingredient; 
    private final Inventory inventory;
    
    /**
     * Constructor initializes the panel and adds focus listeners and 
     * click listeners
     * 
     * @param ingredient The Ingredient to represent in the panel
     */
    public IngredientPanel(final Ingredient ingredient, final Inventory inventory){
        super(); 
        this.inventory = inventory; 
        this.ingredient = ingredient; 
        
        this.ingredientName = new JTextField(ingredient.getName()); 
        this.amount = new JTextField(Integer.toString(ingredient.getAmount()));
        this.close = new JButton("X");
        this.amountLabel = new JLabel("Port."); 
        
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.setBackground(Color.white);
        this.ingredientName.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        this.close.setMaximumSize(new Dimension(100, 35));
        amount.setColumns(3);
        amount.setMaximumSize(new Dimension(50,50));
        
        amount.addFocusListener(this);
        ingredientName.addFocusListener(this);
        
        close.addActionListener(new ActionListener() {

            /**
             * Event listener for close button with confirmation
             * @param ae 
             */
            @Override
            public void actionPerformed(ActionEvent ae) {
                int n = ConfirmationBox.confirm(IngredientPanel.this, ingredientName.getText());
                if(n == 0){
                    Container parent = IngredientPanel.this.getParent(); 
                    parent.remove(IngredientPanel.this);
                    inventory.update();
                    parent.revalidate();
                    parent.repaint();
                    ingredient.setFlaggedForRemoval(true);
                }
            }
        });
        
        this.add(Box.createRigidArea(new Dimension(1, 20)));
        this.add(close);
        this.add(ingredientName);
        this.add(amount);
        this.add(amountLabel);
        this.add(Box.createRigidArea(new Dimension(1, 20)));
        
    }
    
    /**
     * Returns the ingredient associated with this panel
     * @return The ingredient associated with this panel
     */
    public Ingredient getIngredient(){
        return this.ingredient;
    } 

    /**
     * Required abstract method implementation (not used)
     * @param e 
     */
    @Override
    public void focusGained(FocusEvent e) {
        //do nothing
    }

    /**
     * Updates ingredients with data from fields when focus is lost
     * @param e 
     */
    @Override
    public void focusLost(FocusEvent e) {
        updateFromFields(); 
    }
    
    public void updateFromFields(){
        if (isInteger(this.amount.getText()))
            this.ingredient.setAmount(Integer.parseInt(this.amount.getText()));
        this.ingredient.setName(this.ingredientName.getText());
        inventory.update();
    }
    
    /**
     * checks to see if Integer.parseInt(String s) is possible
     * @param s String to check
     * @return true if the parameter s is numeric
     */
    private boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
}

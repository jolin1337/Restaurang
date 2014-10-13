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
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import miun.dt142g.ConfirmationBox;
import miun.dt142g.data.Ingredient;

/**
 *
 * @author ulf
 */
public class IngredientPanel extends JPanel implements FocusListener{
    
    private JButton close; 
    private final JTextField ingredientName; 
    private final JTextField amount; 
    private final JLabel amountLabel; 
    private Ingredient ingredient; 
    
    public IngredientPanel(Ingredient ingredient){
        super(); 

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

            @Override
            public void actionPerformed(ActionEvent ae) {
                int n = ConfirmationBox.confirm(IngredientPanel.this, ingredientName.getText());
                if(n == 0){
                    Container parent = IngredientPanel.this.getParent(); 
                    parent.remove(IngredientPanel.this);
                    parent.revalidate();
                    parent.repaint();
                //add remove from database
                
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
    
    public Ingredient getIngredient(){
        return this.ingredient;
    } 

    @Override
    public void focusGained(FocusEvent e) {
        //do nothing
    }

    @Override
    public void focusLost(FocusEvent e) {
        if (isInteger(this.amount.getText()))
            this.ingredient.setAmount(Integer.parseInt(this.amount.getText()));
        this.ingredient.setName(this.ingredientName.getText());
        System.out.println("apor");
    }
    
    private boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
}

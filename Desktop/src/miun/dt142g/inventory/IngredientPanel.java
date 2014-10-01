/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package miun.dt142g.inventory;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author ulf
 */
public class IngredientPanel extends JPanel {
    
    private JButton close; 
    private JTextField ingredient; 
    private JTextField amount; 
    private JLabel amountLabel; 
    
    public IngredientPanel(){
        super(); 
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.setBackground(Color.white);
        ingredient = new JTextField(); 
        amount = new JTextField();
        close = new JButton("X");
        amountLabel = new JLabel("Port."); 
        amount.setColumns(3);
        amount.setMaximumSize(new Dimension(50,50));
        
        
        this.add(close);
        this.add(ingredient);
        this.add(amount);
        this.add(amountLabel);
        this.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        this.setVisible(true);
        
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package miun.dt142g.inventory;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import miun.dt142g.data.Ingredient;
import miun.dt142g.food.Inventory;

/**
 *
 * @author ulf
 */
public class InventoryPanel extends JPanel {
    
    private JButton addIngredient;
    private Inventory inventory;
    
    public InventoryPanel(){
        super(); 
        
        //initiatilizing variables
        this.inventory = new Inventory(); 
        this.addIngredient = new JButton("Lägg till ingrediens");
        
        //connecting to database and loading inventory
        this.inventory.dbConnect();
        this.inventory.loadData();
        
        //setting up layout an adding existing inventory items 
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBackground(Color.white); 
        for(Ingredient ingredient: inventory) {
            add(new IngredientPanel(ingredient));
        }
        
        this.add(addIngredient); 
        
        
        //adding button event listener to add IngredientPanel
        this.addIngredient.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                InventoryPanel.this.remove(addIngredient);
                InventoryPanel.this.add(new IngredientPanel(new Ingredient(inventory.getUniqueId(),"",0)));
                InventoryPanel.this.add(addIngredient);
                InventoryPanel.this.revalidate();   
            }  
        });
        
        this.setVisible(true);
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package miun.dt142g.inventory;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import miun.dt142g.DataSource;
import miun.dt142g.data.Ingredient;
import miun.dt142g.food.Inventory;

/**
 *
 * @author ulf
 */
public class InventoryPanel extends JPanel {
    
    private JButton submit; 
    private JButton addIngredient;
    private Inventory inventory;
    private List<IngredientPanel> panels = new ArrayList<IngredientPanel>();
    
    public InventoryPanel() throws DataSource.WrongKeyException{
        super(); 
        
        //initiatilizing variables
        this.inventory = new Inventory(); 
        this.addIngredient = new JButton("Lägg till ingrediens");
        this.submit = new JButton("Synkronisera");
        
        
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
        this.add(submit);
        addIngredient.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        submit.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        
        
        //adding button event listener to add IngredientPanel
        this.addIngredient.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                Ingredient ing = new Ingredient(inventory.getUniqueId(), "", 0);
                IngredientPanel panel = new IngredientPanel(ing);
                panels.add(panel);
                inventory.addIngredient(ing);
                
                remove(addIngredient);
                remove(submit);
                
                add(panel);
                add(addIngredient);
                add(submit);
                revalidate();   
            }  
        });
        
        //adding button event listener to add submit
        this.submit.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                remove(addIngredient);
                remove(submit);
                inventory.update();
                add(addIngredient);
                add(submit);
                revalidate();   
            }  
        });   
    }
}

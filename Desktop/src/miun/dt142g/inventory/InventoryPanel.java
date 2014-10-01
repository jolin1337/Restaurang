/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package miun.dt142g.inventory;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 *
 * @author ulf
 */
public class InventoryPanel extends JPanel {
    
    private JButton addIngredient;
    
    
    public InventoryPanel(){
        super();
        addIngredient = new JButton("Lägg till ingrediens");
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBackground(Color.white); 
        this.add(addIngredient); 
        this.setVisible(true);
        addIngredient.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                InventoryPanel.this.remove(addIngredient);
                InventoryPanel.this.add(new IngredientPanel());
                InventoryPanel.this.add(addIngredient);
                InventoryPanel.this.revalidate();
                
            }
            
        });
        
    }
}

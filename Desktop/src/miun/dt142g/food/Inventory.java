/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package miun.dt142g.food;

import java.util.ArrayList;
import java.util.Arrays;
import miun.dt142g.data.Ingredient;

/**
 *
 * @author Ulf
 */
public class Inventory {
    private final ArrayList<Ingredient> ingredientes;
    
    public Inventory(Ingredient ingredient[]){
       ingredientes = new ArrayList<>(Arrays.asList(ingredient));
    }
    
    public Ingredient getIngredient(int id){
        return ingredientes.get(ingredientes.indexOf(id)); 
    }
    
    public void addIngredient(Ingredient ingredient){
        ingredientes.add(ingredient);
    }
    
    public void remove(int id){
        ingredientes.remove(this.getIngredient(id)); 
    }
    
    public void editIngredient(int id, Ingredient ingredient){
        ingredientes.set(ingredientes.indexOf(id), ingredient);
    }
}

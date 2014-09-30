/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package miun.dt142g.data;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Ulf
 */
public class Ingredients {
        private ArrayList<Ingredient> ingredientes;
    
    public Ingredients(Ingredient ingredient[]){
       ingredientes = new ArrayList<Ingredient>(Arrays.asList(ingredient));
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

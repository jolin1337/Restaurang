/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package miun.dt142g.food;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import miun.dt142g.DataSource;
import miun.dt142g.data.Ingredient;

/**
 *
 * @author Ulf
 */
public class Inventory extends DataSource implements Iterable<Ingredient> {
    private final List<Ingredient> ingredientes = new ArrayList<>();
    
    public Inventory(){
    }
    
    public Ingredient getIngredient(int id){
        for(Ingredient ing : ingredientes)
            if(ing.getId() == id)
                return ing;
        return null;
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

    @Override
    public void loadData() {
        ingredientes.add(new Ingredient(0, "Fisk", 10));
        ingredientes.add(new Ingredient(0, "Potatis", 10));
        ingredientes.add(new Ingredient(0, "Gurka", 10));
    }

    @Override
    public void update() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getUniqueId() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Iterator<Ingredient> iterator() {
        return ingredientes.iterator();
    }
}

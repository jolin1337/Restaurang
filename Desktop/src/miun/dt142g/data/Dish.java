/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package miun.dt142g.data;

import java.util.List;

/**
 *
 * @author Ulf
 */
public class Dish {
    private final int id; 
    private String name; 
    private float price; 
    private final List<Integer> ingredients; 
    
    public Dish(int id, String name, float price, List<Integer> ingredients){
        this.id = id; 
        this.name = name; 
        this.price = price;
        this.ingredients = ingredients;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the price
     */
    public float getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(float price) {
        this.price = price;
    }

    /**
     * @param ingred - the ingredient to retrieve
     * @return the ingredients
     */
    public int getIngredient(int ingred) {
        return ingredients.get(ingred);
    }
    public void addIngredient(int id) {
        ingredients.add(id);
    }
    public void removeIngredient(int index) {
        ingredients.remove(id);
    }
    public void editIngredient(int index, int id) {
        ingredients.set(index, id);
    }
    
    public boolean equals(Dish x){
        return this.id==x.id; 
    }
    
    @Override
    public String toString(){
        return this.getName() + "\n" +Float.toString(this.getPrice())+":-";
    }
            
}

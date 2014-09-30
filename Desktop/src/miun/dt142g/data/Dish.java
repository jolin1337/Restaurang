/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package miun.dt142g.data;

/**
 *
 * @author Ulf
 */
public class Dish {
    private int id; 
    private String name; 
    private int price; 
    private int ingredients[]; 
    
    public Dish(int id, String name, int price, int ingredients[]){
        setId(id); 
        setName(name); 
        setPrice(price);
        setIngredients(ingredients);
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
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
    public int getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(int price) {
        this.price = price;
    }

    /**
     * @return the ingredients
     */
    public int[] getIngredients() {
        return ingredients;
    }

    /**
     * @param ingredients the ingredients to set
     */
    public void setIngredients(int[] ingredients) {
        this.ingredients = ingredients;
    }
    
    public boolean equals(Dish x){
        if(this.getId()==x.getId())
            return true; 
        else
            return false; 
    }
    
    @Override
    public String toString(){
        return this.getName() + "\n" +Integer.toString(this.getPrice())+":-";
    }
            
}

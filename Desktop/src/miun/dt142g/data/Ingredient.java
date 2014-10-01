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
public class Ingredient {
    private int id; 
    private String name; 
    private int amount; 

    public Ingredient(int id, String name, int amount) {
        this.id = id;
        this.name = name;
        this.amount = amount;
    }
    
    public Ingredient(){
        
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
     * @return the amount
     */
    public int getAmount() {
        return amount;
    }

    /**
     * @param amount the amount to set
     */
    public void setAmount(int amount) {
        this.amount = amount;
    }
    
    @Override
    public String toString() {
        return this.name;
    }
}

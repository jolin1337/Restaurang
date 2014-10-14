/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package miun.dt142g.data;

/**
 * Data representation of an ingredient. 
 * @author Ulf
 * @see Comparable
 */
public class Ingredient implements Comparable<Ingredient>{
    private int id; 
    private String name; 
    private int amount; 
    private boolean flaggedForRemoval = false; 

    public Ingredient(int id, String name, int amount) {
        this.id = id;
        this.name = name;
        this.amount = amount;
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
    
    /**
     * Returns name of ingredient
     * @return the name of the ingredient
     */
    @Override
    public String toString() {
        return this.getName();
    }

    /**
     * Implements abstract method for Comparable
     * @param t Ingredient object to compare to
     * @return -1 if this.name sorts before t.name, 0 if this.name == t.name and
     * 1 if this.name sorts after t.name
     */
    @Override
    public int compareTo(Ingredient t) {
        return this.getName().compareTo(t.getName());
    }

    /**
     * Checks to see if the ingredient is flagged for removal in database
     * @return the flaggedForRemoval
     */
    public boolean isFlaggedForRemoval() {
        return flaggedForRemoval;
    }

    /**
     * Sets the flag for removal from database. 
     * @param flaggedForRemoval the flaggedForRemoval to set
     */
    public void setFlaggedForRemoval(boolean flaggedForRemoval) {
        this.flaggedForRemoval = flaggedForRemoval;
    }
}

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
    private int servings; 

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
     * @return the servings
     */
    public int getServings() {
        return servings;
    }

    /**
     * @param servings the servings to set
     */
    public void setServings(int servings) {
        this.servings = servings;
    }
}

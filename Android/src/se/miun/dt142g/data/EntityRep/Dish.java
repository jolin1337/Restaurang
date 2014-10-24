/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.miun.dt142g.data.EntityRep;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Ulf
 */
public class Dish implements Comparable<Dish> {
    private boolean deleted = false;
    private boolean inStock = false; 
    private int id;
    private String name;
    private float price;
    private boolean special = false;
    private int count = 1;
    private List<Integer> ingredientIds = new ArrayList<Integer>(); 

    public Dish(int id, String name, float price, List<Integer> ingredientIds, boolean inStock) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.inStock = inStock;
        for(Integer i : ingredientIds){
            this.ingredientIds.add(i);
        }
        String s = "Fisk";
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public int getId() {
        return id;
    }
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
    public float getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(float price) {
        this.price = price;
    }


    public boolean equals(Dish x) {
        return this.id == x.id;
    }

    @Override
    public String toString() {
        return this.getName() + "\n      " + Float.toString(this.getPrice()) + ":-";
    }

    /*
    public String toJsonString() {
        // Set all properties of this event here to export the event to a json object
        JSONObject value = new JSONObject();
        try {
            value.put("id", getId())
                    .put("name", getName())
                    .put("price", getPrice());
        } catch (JSONException ex) {
        }
        return value.toString();
    }*/

    @Override
    public int compareTo(Dish t) {
        return name.toLowerCase().compareTo(t.getName().toLowerCase());
    }

    /**
     * @return the inStock
     */
    public boolean isInStock() {
        return inStock;
    }

    /**
     * @param inStock the inStock to set
     */
    public void setInStock(boolean inStock) {
        this.inStock = inStock;
    }

    public void setSpecial(boolean special) {
        this.special = special;
    }

    public void setCount(int count) {
        this.count = count;
    }
    public boolean getSpecial() {
        return special;
    }

    public int getCount() {
        return count;
    }

    public List<Integer> getIngredientIds() {
        return ingredientIds;
    }

    public void setIngredientIds(List<Integer> ingredientIds) {
        this.ingredientIds = ingredientIds;
    }
    
}

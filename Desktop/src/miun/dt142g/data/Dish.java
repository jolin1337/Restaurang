/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package miun.dt142g.data;

import java.util.ArrayList;
import java.util.List;
import miun.dt142g.food.Inventory;

/**
 *
 * @author Ulf
 */
public class Dish {

    private final int id;
    private String name;
    private float price;
    private List<Integer> ingredients;

    public Dish(int id, String name, float price, List<Integer> ingredients) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.ingredients = ingredients;
    }

    public int getId() {
        return id;
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
        initIngredients();
        return ingredients.get(ingred);
    }

    public List<Integer> getIngredients() {
        initIngredients();
        return ingredients;
    }

    public void addIngredient(int id) {
        initIngredients();
        ingredients.add(id);
    }

    public void removeIngredient(int index) {
        initIngredients();
        ingredients.remove(id);
    }

    public void editIngredient(int index, int id) {
        initIngredients();
        ingredients.set(index, id);
    }

    private void initIngredients() {
        if (ingredients == null) {
            ingredients = new ArrayList<>();
        }
    }

    public boolean equals(Dish x) {
        return this.id == x.id;
    }

    @Override
    public String toString() {
        return this.getName() + "\n" + Float.toString(this.getPrice()) + ":-";
    }

}

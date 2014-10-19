package se.miun.dt142g.data;

import java.text.DecimalFormat;
import java.text.NumberFormat;
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

    private int id;
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
        if (x == null) {
            return false;
        }
        return this.id == x.getId();
    }

    @Override
    public String toString() {
        NumberFormat moneyFormat = new DecimalFormat("#0.00");
        return this.getName() + " " + moneyFormat.format(this.getPrice()) + " kr";
    }

    public String toJsonString() {
        // Set all properties of this event here to export the event to a json object
        JSONObject value = new JSONObject();
        try {
            JSONArray ings = new JSONArray(ingredients);
            value.put("id", getId())
                    .put("name", getName())
                    .put("price", getPrice())
                    .put("ingredients", ings);
        } catch (JSONException ex) {
        }
        return value.toString();
    }

    @Override
    public int compareTo(Dish t) {
        return name.toLowerCase().compareTo(t.getName().toLowerCase());
    }
}

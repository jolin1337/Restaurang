package se.miun.dt142g.data;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This is a datasource class that describes an dish in this system
 *
 * @author Ulf
 */
public class Dish implements Comparable<Dish> {

    /**
     * The identifier of this datasource
     */
    private int id;
    /**
     * The name of this dish
     */
    private String name;
    /**
     * The price of this dish
     */
    private float price;
    /**
     * Ingredients that this dish needs to create
     */
    private List<Integer> ingredients;

    /**
     * Contructs a dish with the initial values of the parameters
     *
     * @param id
     * @param name
     * @param price
     * @param ingredients
     */
    public Dish(int id, String name, float price, List<Integer> ingredients) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.ingredients = ingredients;
    }

    /**
     * Getter of identifier
     *
     * @return the id of this dish
     */
    public int getId() {
        return id;
    }

    /**
     * Setter of identifier
     *
     * @param id - The id of this dish
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Getter of the name this dish is called
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Setter for the name of this dish
     *
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter of the price this dish costs
     *
     * @return the price
     */
    public float getPrice() {
        return price;
    }

    /**
     * Setter of the price this dish costs
     *
     * @param price the price to set
     */
    public void setPrice(float price) {
        this.price = price;
    }

    /**
     * Getter for one ingredient
     *
     * @param ingred - the index of ingredient to retrieve
     * @return the ingredients
     */
    public int getIngredient(int ingred) {
        initIngredients();
        return ingredients.get(ingred);
    }

    /**
     * Getter for all ingredients
     *
     * @return a list of ingredient pk:s
     */
    public List<Integer> getIngredients() {
        initIngredients();
        return ingredients;
    }

    /**
     * Add an ingredient with pk: id
     *
     * @param id - The identifier of the ingredient to add
     */
    public void addIngredient(int id) {
        initIngredients();
        ingredients.add(id);
    }

    /**
     * Removes an ingredient by index
     *
     * @param index - the index of the ingredient to remove
     */
    public void removeIngredient(int index) {
        initIngredients();
        ingredients.remove(id);
    }

    /**
     * Update an ingredient
     *
     * @param index - The index of the ingredient to update
     * @param id - The new ingredient pk
     */
    public void editIngredient(int index, int id) {
        initIngredients();
        ingredients.set(index, id);
    }

    /**
     * initiates an ingredient array if non was specified
     */
    private void initIngredients() {
        if (ingredients == null) {
            ingredients = new ArrayList<>();
        }
    }

    /**
     * Compares to whether the dish equals this dish
     *
     * @param x the dish to compare
     * @return true if the given dish id is equivalent to this dish id, false
     * otherwise
     */
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

    /**
     * Generates an Json representation of this objects attributes
     *
     * @return A string containing the json object of this object
     */
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

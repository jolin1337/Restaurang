/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package miun.dt142g.food;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import miun.dt142g.DataSource;
import miun.dt142g.data.Ingredient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class is a container class for se.miun.dt142g.data.Ingredient
 * 
 * An instance of the class stores Ingredients in an ArrayList and uses superclass
 * DataSource methods for server interaction. 
 * 
 * @author Ulf
 * @see Iterable
 * @see Datasource
 * 
 */
public class Inventory extends DataSource implements Iterable<Ingredient> {
    private List<Ingredient> ingredients = new ArrayList<>();
    
    /**
     * Constructor does nothing. Class relies on it's other methods for explicit
     * interaction through DataSource. 
     */
    public Inventory(){
    }
    
    /**
     * Searches list of ingredients for an Ingredient with the given id and
     * if it's found returns that Ingredient
     * @param id the id of the Ingredient object to get. 
     * @return returns Ingredient object if an Ingredient with correct id is 
     * in the list of ingredients, else returns null
     */
    public Ingredient getIngredient(int id){
        for(Ingredient ing : ingredients)
            if(ing.getId() == id)
                return ing;
        return null;
    }
    
    /**
     * Adds Ingredient to list of ingredients. 
     * @param ingredient The Ingredient to add to list of ingredients
     */
    public void addIngredient(Ingredient ingredient){
        ingredients.add(ingredient);
        
    }
    
    /**
     * Adds Ingredient from json object to list of ingredients
     * @param jsonIngredient json object representing an Ingredient to add
     * @return returns true if the ingredient was successfully added
     */
    public boolean addJsonIngredient(JSONObject jsonIngredient){

        int id;
        String name;
        int amount;
        Ingredient ingredient;
        
        try {
            id = jsonIngredient.getInt("id");
            name = jsonIngredient.getString("name");
            amount = jsonIngredient.getInt("amount");    
            ingredients.add(new Ingredient(id, name, amount));
        } catch (JSONException ex) {
            Logger.getLogger(Inventory.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return false;
    }
    
    /**
     * Loads ingredients list from database. 
     */
    @Override
    public void loadData() {
        
        ingredients.clear();
        JSONObject response = null;
        JSONArray data = null; 
        try {
            response = getJsonRequest("inventory");
            data = response.getJSONArray("data");
        } catch (JSONException ex) {
            Logger.getLogger(Inventory.class.getName()).log(Level.SEVERE, null, ex);
        }
        for(int i = 0;i<data.length();i++){
            try {
                addJsonIngredient(data.getJSONObject(i));
            } catch (JSONException ex) {
                Logger.getLogger(Inventory.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        Collections.sort(ingredients);
    }

    /**
     * Updates ingredients list in database
     */
    @Override
    public void update() {
        try {
            JSONArray data = new JSONArray(); 
            for(Ingredient ing : this.ingredients) {
                JSONObject jsonDataElement = new JSONObject();
                JSONObject jsonIngredient = new JSONObject();
                if (ing.isFlaggedForRemoval()) {
                    if(canBeRemoved(ing.getId())){
                        jsonIngredient.put("id", ing.getId());
                        jsonIngredient.put("remove", true);
                        jsonDataElement.put("data", jsonIngredient);
                        data.put(jsonDataElement);
                    }
                    else {
                        // Do something like popup indicating ingredient couldn't be removed.
                    }
                }
                else {
                    /*if(ing.getId()<0)
                        jsonIngredient.put("id", -1);*/
                    //else
                    jsonIngredient.put("id", ing.getId());
                    jsonIngredient.put("name", ing.getName());
                    jsonIngredient.put("amount", ing.getAmount());
                    jsonDataElement.put("data", jsonIngredient);
                    data.put(jsonDataElement);
                }
            }
            JSONObject send = new JSONObject(); 
            send.put("data", data);
            System.out.println("Json object to send: " + send.toString());
            String urlParams = "key=" + key + "&table=inventory&data="+send.toString();
            System.out.println("Update status: " +getRequest("updaterow", urlParams));
            
            ingredients = new ArrayList<>();
            loadData();
        } 
        catch (JSONException ex) {
            Logger.getLogger(Inventory.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Possibly deprecated. Unique ingredient id is handled in database, id of 
     * -1 sent to database means the ingredient is new. 
     * @return -1
     */
    @Override
    public int getUniqueId() {
        int id  = -1;
        for(Ingredient ing : ingredients){
            if(ing.getId() <= id)
                id = ing.getId()-1;
        }
        return id; 
    }

    /**
     * to enable iteration 
     * @return returns iterator of List<Ingredient> object
     */
    @Override
    public Iterator<Ingredient> iterator() {
        return ingredients.iterator();
    }
    
    /**
     * Checks if ingredient currently is in any dish_has_ingredient entry
     * @param id the id of ingredient to check
     * @return returns true if it's safe to remove ingredient. 
     */
    private boolean canBeRemoved(int id){
        JSONObject dishHasInventory = null;
        JSONArray data = null; 
        try {
            dishHasInventory = getJsonRequest("dish_has_inventory");
            data = dishHasInventory.getJSONArray("data");
        } catch (JSONException ex) {
            Logger.getLogger(Inventory.class.getName()).log(Level.SEVERE, null, ex);
        }
        for(int i = 0;i<data.length();i++){
            try {
                if(id == data.getJSONObject(i).getInt("inventory_id")){
                    return false; 
                }
            } catch (JSONException ex) {
                Logger.getLogger(Inventory.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return true; 
    }
}

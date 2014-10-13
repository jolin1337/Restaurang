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
 *
 * @author Ulf
 */
public class Inventory extends DataSource implements Iterable<Ingredient> {
    private final List<Ingredient> ingredients = new ArrayList<>();
    
    public Inventory(){
    }
    
    public Ingredient getIngredient(int id){
        for(Ingredient ing : ingredients)
            if(ing.getId() == id)
                return ing;
        return null;
    }
    
    public void addIngredient(Ingredient ingredient){
        ingredients.add(ingredient);
        
    }
    
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
    
    public void remove(int id){
        ingredients.remove(this.getIngredient(id)); 
    }
    
    public void editIngredient(int id, Ingredient ingredient){
        ingredients.set(ingredients.indexOf(id), ingredient);
    }

    @Override
    public void loadData() {
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
        //ingredients.add(new Ingredient(getUniqueId(), "Fisk", 10));
        //ingredients.add(new Ingredient(0, "Potatis", 10));
        //ingredients.add(new Ingredient(0, "Gurka", 10));
    }

    @Override
    public void update() {
        JSONArray data = new JSONArray(); 
        for(Ingredient ing : this.ingredients){
            try {
                JSONObject jsonDataElement = new JSONObject();
                JSONObject jsonIngredient = new JSONObject();
                jsonIngredient.put("id", ing.getId());
                jsonIngredient.put("name", ing.getName());
                jsonIngredient.put("amount", ing.getAmount());
                jsonDataElement.put("data", jsonIngredient);
                data.put(jsonDataElement);
            } catch (JSONException ex) {
                Logger.getLogger(Inventory.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        JSONObject send = new JSONObject(); 
        try {
            send.put("data", data);
        } catch (JSONException ex) {
            Logger.getLogger(Inventory.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Json object to send: " + send.toString());
        String urlParams = "key=" + key + "&table=inventory&data="+send.toString();
        System.out.println("Update status: " +getRequest("updaterow", urlParams));
    }

    @Override
    public int getUniqueId() {
        return -1;
//        int id  = 0;
//        for(Ingredient ing : ingredients){
//            if(ing.getId() >= id)
//                id = ing.getId()+1;
//        }
//        return id; 
    }

    @Override
    public Iterator<Ingredient> iterator() {
        return ingredients.iterator();
    }
}

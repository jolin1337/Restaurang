/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.miun.dt142g.data.handler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import se.miun.dt142g.data.EntityRep.Dish;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import se.miun.dt142g.data.entityhandler.DataSource;

/**
 *
 * @author Ulf
 */
public class Dishes extends DataSource implements Iterable<Dish> {
    private final List<Dish> dishes = new ArrayList<Dish>();
    private final List<Ingredient> ingredients = new ArrayList<Ingredient>(); 
    
    public Dishes(){
    }
    
    public int getRows() {
        return dishes.size();
    }
    public Dish getDish(int id){
        for(Dish d : dishes)
            if(d.getId() == id)
                return d;
        return null; 
    }
    public Dish getDishByIndex(int index){
        return dishes.get(index);
    }
    
    public void addDish(Dish dish){
        for(Dish d : dishes) {
            if(d.getId() == dish.getId() && d.getId() > -1) {
                editDish(dish);
                return;
            }
        }
        dishes.add(dish);
    }
    
    public void removeDish(int id){
        dishes.remove(this.getDish(id)); 
    }
    
    public void editDish(Dish dish){
        for(Dish d : dishes) {
            if(d.getId() == dish.getId()) {
                d.setName(dish.getName());
                d.setPrice(dish.getPrice());
                return;
            }
        }
    }

    @Override
    public void loadData() throws WrongKeyException {
        List<Ingredient> tempIngredients = getIngredientsDataList(); 
        ingredients.clear();
        for(Ingredient ing : tempIngredients){
            ingredients.add(ing);
        }
        List<Dish> ds = getDataList();
        dishes.clear();
        for(Dish dish : ds)
            dishes.add(dish);
        
        Collections.sort(dishes);
    }

    @Override
    public void update() throws WrongKeyException {
        /*
        List<Dish> ds = getDataList();
        String str = "&table=dish&data={\"data\":[";
        String strRm = "&table=dish&data={\"data\":[";
        for(Dish dish : ds)
            strRm += "{\"data\":{\"remove\":true,\"id\":" + dish.getId() + "}},";
        for (Dish dish : dishes) {
            int id = dish.getId();
            dish.setId(-1);
            str += "{\"data\":" + dish.toJsonString() + "},";
            dish.setId(id);
        }
        if(dishes.isEmpty())
            str += ",";
        if(ds.isEmpty())
            strRm += ",";
        System.out.println(str.substring(0, str.length()-1) + "]}");
        // System.out.println(strRm.substring(0, strRm.length()-1) + "]}");
        System.out.println("Updatestatus: " + getRequest("updaterow", "key=" + key + strRm.substring(0, strRm.length()-1) + "]}"));
        System.out.println("Updatestatus: " + getRequest("updaterow", "key=" + key + str.substring(0, str.length()-1) + "]}"));
        
        // To make sure that we have the correct id:s/pk:s
        loadData();*/
        loadData();
    }
    private List<Dish> getDataList() throws WrongKeyException {
        List<Dish> currentEvents = new ArrayList<Dish>();
        JSONObject json;
        String jsonStr = getRequest("gettable", "key=" + key + "&table=dish");
        if(jsonStr.equals("expired_key")) {
            dbConnect();
            jsonStr = getRequest("gettable", "key=" + key + "&table=dish");
        }
        try {
            json = new JSONObject(jsonStr);
        } catch (JSONException ex) {
            System.out.println("Could not verify the JSON parse of json object: " + jsonStr);
            return currentEvents;
        }

        JSONArray jsonArr;
        try {
            jsonArr = json.getJSONArray("data");
        } catch (JSONException ex) {
            return currentEvents;
        }
        boolean inStock = false; 
        JSONObject obj;
        Dish d = null;
        List<Integer> ingredientIds = new ArrayList<Integer>();


        try {
            for (int i = jsonArr.length(); i > 0; i--) {
                inStock = false;
                // Get the properties of the json object and update this event.
                obj = jsonArr.getJSONObject(i-1);
                int id = obj.getInt("id");
                String name = obj.getString("name");
                int price = obj.getInt("price");
                // Dish ingredientslist
                JSONArray ings = obj.getJSONArray("ingredients");
                for (int j = ings.length(); j > 0; j--) {
                    ingredientIds.add(ings.getInt(j-1));
                }
                inStock = isInStock(ingredientIds);
                d = new Dish(id, name, price, ingredientIds, inStock);
                
                currentEvents.add(d);
        }
        } catch (JSONException ex) {
            if (d != null) {
                System.out.println("Unable to parse json object: " + d);
            } else {
                System.out.println("Unable to parse json object: null");
            }
        }

        return currentEvents;            // return that we have changed this entity
    }

    private List<Ingredient> getIngredientsDataList(){
        JSONObject json;
        JSONArray data; 
        List<Ingredient> ings = new ArrayList<Ingredient>(); 
        String jsonStr = getRequest("gettable", "key=" + key + "&table=inventory");
        if(jsonStr.equals("expired_key")) {
            try {
                dbConnect();
            } catch (WrongKeyException ex) {
                Logger.getLogger(Dishes.class.getName()).log(Level.SEVERE, null, ex);
            }
            jsonStr = getRequest("gettable", "key=" + key + "&table=inventory");
        }
        try { 
            json = new JSONObject(jsonStr);
            data = json.getJSONArray("data"); 
            JSONObject jsonIngredient; 
            for(int i = 0; i < data.length(); i++){
                jsonIngredient = data.getJSONObject(i); 
                ings.add(new Ingredient(jsonIngredient.getInt("id"), jsonIngredient.getInt("amount")));
            }
            
        } catch (JSONException ex) {
            Logger.getLogger(Dishes.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return ings; 
    }
    
    private boolean isInStock(List<Integer> ingredientIds){
        if (ingredientIds.isEmpty()){
            return true; 
        }
        for(Ingredient ing : ingredients){
            for(Integer i : ingredientIds){
                if (i == ing.getId() && ing.getAmount()<1){
                    return false; 
                }
            }
        }
        return true; 
    }
    
    @Override
    public Iterator<Dish> iterator() {
        return dishes.iterator();
    }
    
    @Deprecated
    @Override
    public int getUniqueId() {
        return -1;
    }

    void clear() {
        dishes.clear();
    }
    
    
    /**
     * Creates a CharSequence representation of dishes. 
     * @return Returns CharSequence array of dishes. 
     */
    public CharSequence[] toCharSequence() {
        CharSequence[] charDishes = new CharSequence[dishes.size()];
        int i = 0;
        for(Dish dish : dishes){
            charDishes[i] = dish.toString();
            if(!dish.isInStock()){
                charDishes[i] += "(Slut)"; 
            }
            i++;
        }
        return charDishes;
    }
    @Override
    public String toString() {
        return "Dishes size = " + dishes.size();
    }
    
    public class Ingredient{
        private int id; 
        private int amount; 
        
        public Ingredient(int id, int amount){
            this.id = id; 
            this.amount = amount; 
        }

        /**
         * @return the id
         */
        public int getId() {
            return id;
        }

        /**
         * @return the amount
         */
        public int getAmount() {
            return amount;
        }
    }
}

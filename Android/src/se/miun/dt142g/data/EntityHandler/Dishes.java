/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.miun.dt142g.data.EntityHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import se.miun.dt142g.DataSource;
import se.miun.dt142g.data.EntityRep.Dish;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Ulf
 */
public class Dishes extends DataSource implements Iterable<Dish> {
    private final String table="dish";
    private final List<Dish> dishes = new ArrayList<Dish>();
    
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
    public void load() {
        
        String jsonStr = sendRequestFromThread("gettable", "&table=dish");
        /*List<Dish> ds = getDataList();
        dishes.clear();
        for(Dish dish : ds)
            dishes.add(dish);
        
        Collections.sort(dishes);*/
    }
    @Override
    public void loadData(String url, String responseText) throws WrongKeyException {
        if(url.equals("gettable")) {
            List<Dish> ds = getDataList(responseText);
            dishes.clear();
            for(Dish d : ds) {
                dishes.add(d);
            }
        }
    }
    
    public CharSequence[] toCharSequence() {
        return (CharSequence[])dishes.toArray();
    }

    @Override
    public void update() throws WrongKeyException {
        List<Dish> ds = dishes;
        load(); // changed dishes values to the ones on server
        String str = "&table=dish&data={\"data\":[";
        String strRm = "&table=dish&data={\"data\":[";
        for(Dish dish : dishes)
            strRm += "{\"data\":{\"remove\":true,\"id\":" + dish.getId() + "}},";
        for (Dish dish : ds) {
            int id = dish.getId();
            dish.setId(-1);
            str += "{\"data\":" + dish.toJsonString() + "},";
            dish.setId(id);
        }
        if(ds.isEmpty())
            str += ",";
        if(dishes.isEmpty())
            strRm += ",";
        System.out.println(str.substring(0, str.length()-1) + "]}");
        // System.out.println(strRm.substring(0, strRm.length()-1) + "]}");
        System.out.println("Updatestatus: " + sendRequestFromThread("updaterow", strRm.substring(0, strRm.length()-1) + "]}"));
        System.out.println("Updatestatus: " + sendRequestFromThread("updaterow", str.substring(0, str.length()-1) + "]}"));
        
        // To make sure that we have the correct id:s/pk:s
        sendRequestFromThread("gettable", "&table=" + table);
    }
    private List<Dish> getDataList(String responseText) throws WrongKeyException {
        List<Dish> currentEvents = new ArrayList<Dish>();
        JSONObject json;
        try {
            json = new JSONObject(responseText);
        } catch (JSONException ex) {
            System.out.println("Could not verify the JSON parse of json object: " + responseText);
            return currentEvents;
        }

        JSONArray jsonArr;
        try {
            jsonArr = json.getJSONArray("data");
        } catch (JSONException ex) {
            return currentEvents;
        }
        for (int i = jsonArr.length(); i > 0; i--) {
            JSONObject obj;
            Dish d = null;
            try {
                // Get the properties of the json object and update this event.
                obj = jsonArr.getJSONObject(i-1);
                int id = obj.getInt("id");
                String name = obj.getString("name");
                int price = obj.getInt("price");
                d = new Dish(id, name, price);
                
                currentEvents.add(d);
            } catch (JSONException ex) {
                if (d != null) {
                    System.out.println("Unable to parse json object: " + d);
                } else {
                    System.out.println("Unable to parse json object: null");
                }
            }
        }
        return currentEvents;            // return that we have changed this entity
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
}

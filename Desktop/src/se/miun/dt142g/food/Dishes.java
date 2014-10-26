/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.miun.dt142g.food;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import se.miun.dt142g.DataSource;
import se.miun.dt142g.data.Dish;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A container class for all dishes
 *
 * @author Ulf
 */
public class Dishes extends DataSource implements Iterable<Dish> {

    /**
     * A list to hold all dishes
     */
    private final List<Dish> dishes = new ArrayList<>();

    /**
     * Default constructor
     */
    public Dishes() {
    }

    /**
     * Retrieves the amount of dish instances
     *
     * @return the amount of instances of dishes, eg. the amount of rows in the
     * list
     */
    public int getRows() {
        return dishes.size();
    }

    /**
     * Retrieves a dish by id
     *
     * @param id - The id of the requested dish to be found
     * @return the dish if it has been found, otherwise null
     */
    public Dish getDish(int id) {
        for (Dish d : dishes) {
            if (d.getId() == id) {
                return d;
            }
        }
        return null;
    }

    /**
     * Retrieves a dish by index in the list
     *
     * @param index - The index to be looked at
     * @return the dish if it has been found, otherwise null
     */
    public Dish getDishByIndex(int index) {
        return dishes.get(index);
    }

    /**
     * Adds a new dish
     *
     * @param dish - The dish to be added
     */
    public void addDish(Dish dish) {
        for (Dish d : dishes) {
            if (d.getId() == dish.getId() && d.getId() > -1) {
                editDish(dish);
                return;
            }
        }
        dishes.add(dish);
    }

    /**
     * Remove a dish by id
     *
     * @param id - id of the dish to be removed
     */
    public void removeDish(int id) {
        dishes.remove(this.getDish(id));
    }

    /**
     * Edit a dish by replacing it with a new dish with the same id as the
     * existing dish
     *
     * @param dish - The dish to replace with
     */
    public void editDish(Dish dish) {
        for (Dish d : dishes) {
            if (d.getId() == dish.getId()) {
                d.setName(dish.getName());
                d.setPrice(dish.getPrice());
                d.getIngredients().clear();
                for (int ing : dish.getIngredients()) {
                    d.getIngredients().add(ing);
                }
                return;
            }
        }
    }

    @Override
    public void loadData() throws WrongKeyException {
        List<Dish> ds = getDataList();
        dishes.clear();
        for (Dish dish : ds) {
            dishes.add(dish);
        }

        Collections.sort(dishes);
    }

    @Override
    public void update() throws WrongKeyException {
        List<Dish> ds = getDataList();
        String str = "&table=dish&data={\"data\":[";
        String strRm = "&table=dish&data={\"data\":[";
        for (Dish dish : ds) {
            strRm += "{\"data\":{\"remove\":true,\"id\":" + dish.getId() + "}},";
        }
        for (Dish dish : dishes) {
            int id = dish.getId();
            dish.setId(-1);
            str += "{\"data\":" + dish.toJsonString() + "},";
            dish.setId(id);
        }
        if (dishes.isEmpty()) {
            str += ",";
        }
        if (ds.isEmpty()) {
            strRm += ",";
        }
        System.out.println(str.substring(0, str.length() - 1) + "]}");
        // System.out.println(strRm.substring(0, strRm.length()-1) + "]}");
        System.out.println("Updatestatus: " + getRequest("updaterow", "key=" + key + strRm.substring(0, strRm.length() - 1) + "]}"));
        System.out.println("Updatestatus: " + getRequest("updaterow", "key=" + key + str.substring(0, str.length() - 1) + "]}"));

        // To make sure that we have the correct id:s/pk:s
        loadData();
    }

    /**
     * Retrieves a list of all dishes from the database
     *
     * @return a list of all dishes retrieved from the database
     * @throws se.miun.dt142g.DataSource.WrongKeyException
     */
    private List<Dish> getDataList() throws WrongKeyException {
        List<Dish> currentEvents = new ArrayList<>();
        JSONObject json;
        String jsonStr = getRequest("gettable", "key=" + key + "&table=dish");
        if (jsonStr.equals("expired_key")) {
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
        for (int i = jsonArr.length(); i > 0; i--) {
            JSONObject obj;
            Dish d = null;
            try {
                // Get the properties of the json object and update this event.
                obj = jsonArr.getJSONObject(i - 1);
                int id = obj.getInt("id");
                String name = obj.getString("name");
                int price = obj.getInt("price");
                JSONArray ings = obj.getJSONArray("ingredients");
                List<Integer> ingredients = new ArrayList<>();
                for (int j = ings.length(); j > 0; j--) {
                    ingredients.add(ings.getInt(j - 1));
                }
                Collections.sort(ingredients);
                d = new Dish(id, name, price, ingredients);

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

    /**
     * Clears all dishes
     */
    void clear() {
        dishes.clear();
    }

    /**
     * Retrieves the names of all dishes associated with the ingredient id
     *
     * @param ingredientId - id to match with the associated dishes
     * @return a list of all dishes associated with the ingredient
     */
    public List<String> getDishNames(int ingredientId) {
        List<String> dishNames = new ArrayList<String>();
        for (Dish d : dishes) {
            for (Integer i : d.getIngredients()) {
                if (i == ingredientId) {
                    dishNames.add(d.getName());
                    break;
                }
            }
        }
        return dishNames;
    }
}

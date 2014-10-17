/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.miun.dt142g.food;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import se.miun.dt142g.data.Dish;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
/**
 *
 * @author Tomas
 */
public class DishGroup {
    private List<Integer> dishIds;
    private String group;

    /**
     * @return the group
     */
    public String getGroup() {
        return group;
    }

    /**
     * @param group the group to set
     */
    public void setGroup(String group) {
        this.group = group;
    }

    public List<Integer> getDishes() {
        initDishIds();
        return dishIds;
    }
    public void addDish(int dishIndex) {
        initDishIds();
        if(!dishIds.contains(dishIndex))
            dishIds.add(dishIndex);
    }

    public void setDishIds(List<Integer> dishId) {
        this.dishIds = dishId;
    }

    public DishGroup(List<Integer> dishId, String group) {
        this.dishIds = dishId;
        this.group = group;
    }
    public DishGroup(String group) {
        this.dishIds = null;
        this.group = group;
    }

    private void initDishIds() {
        if(dishIds == null)
            setDishIds(new ArrayList<Integer>());
    }
    
    @Override
    public boolean equals(Object groupName) {
        return (groupName instanceof String || groupName instanceof DishGroup) 
                && toString().equals(groupName.toString());
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 11 * hash + Objects.hashCode(this.group);
        return hash;
    }
    
    @Override
    public String toString() {
        return group;
    }

    String toJsonString() {
        
        // Set all properties of this event here to export the event to a json object
        JSONObject value = new JSONObject();
        try {
            JSONArray dishes = new JSONArray(dishIds);
            value.put("name", getGroup())
                    .put("dishes", dishes);
        } catch (JSONException ex) {
        }
        return value.toString();
    }

}

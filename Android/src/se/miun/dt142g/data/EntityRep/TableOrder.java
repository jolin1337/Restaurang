/*
 * This code is created for one purpose only. And should not be used for any 
 * other purposes unless the author of this file has apporved. 
 * 
 * This code is a piece of a project in the course DT142G on Mid. Sweden university
 * Created by students for this projekt only
 */
package se.miun.dt142g.data.EntityRep;

import java.util.Date;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Johannes
 */
public class TableOrder {
    private int id;
    private int table;
    private Date timeOfOrder;
    private List<Integer> orderedDishes;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getTimeOfOrder() {
        return timeOfOrder;
    }

    public void setTimeOfOrder(Date timeOfOrder) {
        this.timeOfOrder = timeOfOrder;
    }

    public List<Integer> getOrderedDishes() {
        return orderedDishes;
    }

    public void setOrderedDishes(List<Integer> orderedDishes) {
        this.orderedDishes = orderedDishes;
    }
    
    public String toJsonString() {
        // Set all properties of this event here to export the event to a json object
        JSONObject value = new JSONObject();
        try {
            JSONArray orders = new JSONArray(getOrderedDishes());
            value.put("id", getId())
                    .put("timeOrder", getTimeOfOrder())
                    .put("orderDishes", orders);
        } catch (JSONException ex) {
        }
        return value.toString();
    }

    /**
     * @return the table
     */
    public int getTable() {
        return table;
    }

    /**
     * @param table the table to set
     */
    public void setTable(int table) {
        this.table = table;
    }
}

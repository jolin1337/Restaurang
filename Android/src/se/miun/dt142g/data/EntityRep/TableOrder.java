/*
 * This code is created for one purpose only. And should not be used for any 
 * other purposes unless the author of this file has apporved. 
 * 
 * This code is a piece of a project in the course DT142G on Mid. Sweden university
 * Created by students for this projekt only
 */
package se.miun.dt142g.data.EntityRep;

import java.util.ArrayList;
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

    public TableOrder(int tbl) {
        id = -1;
        timeOfOrder = new Date();
        table = tbl;
    }
    
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
    public void setTimeOfOrder(int timeOfOrder) {
        if(this.timeOfOrder == null)
            this.timeOfOrder = new Date();
        this.timeOfOrder.setTime(timeOfOrder);
    }
    
    public String toJsonString() {
        // Set all properties of this event here to export the event to a json object
        JSONObject value = new JSONObject();
        try {
            value.put("id", getId())
                    .put("table", table)
                    .put("timeOrder", getTimeOfOrder().getTime());
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

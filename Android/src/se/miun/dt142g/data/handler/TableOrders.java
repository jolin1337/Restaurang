/*
 * This code is created for one purpose only. And should not be used for any 
 * other purposes unless the author of this file has apporved. 
 * 
 * This code is a piece of a project in the course DT142G on Mid. Sweden university
 * Created by students for this projekt only
 */
package se.miun.dt142g.data.handler;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import se.miun.dt142g.data.entityhandler.DataSource;
import se.miun.dt142g.data.EntityRep.TableOrder;

/**
 * This class describes all Tableorders in database
 *
 * @author Johannes Lindén
 * @since 2014-10-09
 * @version 1.2
 */
public class TableOrders extends DataSource implements Iterable<TableOrder> {

    List<TableOrder> tableOrders;
    Dishes dishes = new Dishes();
    String table = "tableorder";

    public TableOrders() {
        tableOrders = new ArrayList<TableOrder>();
    }

    private void parseTable(String jsonStr) {
        try {
            tableOrders.clear();
            JSONObject json = new JSONObject(jsonStr);
            JSONArray data = json.getJSONArray("data");
            for(int i=data.length(); i > 0; i--) {
                JSONObject row = data.getJSONObject(i-1);
                TableOrder order = new TableOrder();
                order.setId(row.getInt("id"));
                order.setTimeOfOrder(new Date(row.getInt("timeOfOrder")));
                JSONArray dishes = row.getJSONArray("orders");
                List<Integer> dishesIndicies = new ArrayList<Integer>();
                for(int j=dishes.length(); j > 0; j--) {
                    int dishIndex = dishes.getInt(j-1);
                    dishesIndicies.add(dishIndex);
                }
                order.setOrderedDishes(dishesIndicies);
                tableOrders.add(order);
            }
        } catch (JSONException ex) {
        }
        
    }
    
    
    @Override
    public void update() throws WrongKeyException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getUniqueId() {
        int id = -1; 
        for(TableOrder to : tableOrders){
            if (to.getId()<=id){
                id-=1;
            }
        }
        return id; 
    }

    public Iterator<TableOrder> iterator() {
        return tableOrders.iterator();
    }

    @Override
    public void loadData() throws WrongKeyException {
        dbConnect();
        parseTable(getRequest("gettable", "key=" + key + "&table=" + table));
    }
}

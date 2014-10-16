/*
 * This code is created for one purpose only. And should not be used for any 
 * other purposes unless the author of this file has apporved. 
 * 
 * This code is a piece of a project in the course DT142G on Mid. Sweden university
 * Created by students for this projekt only
 */
package se.miun.dt142g.data.EntityHandler;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import se.miun.dt142g.DataSource;
import se.miun.dt142g.data.EntityRep.TableOrder;

/**
 *
 * @author Johannes
 */
public class TableOrders extends DataSource implements Iterable<TableOrder> {

    List<TableOrder> tableOrders;
    String table = "tableorder";

    public TableOrders() {
        tableOrders = new ArrayList<TableOrder>();
    }

    private void parseTable(String jsonStr) {
        try {
            JSONObject json = new JSONObject(jsonStr);
            JSONArray data = json.getJSONArray("data");
            for(int i=data.length(); i > 0; i--) {
                JSONObject row = data.getJSONObject(i-1).getJSONObject("data");
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
    public void loadData(String url, String responseText) throws WrongKeyException {
        if (url.equals("login")) {
            key = responseText;
        } else if (url.equals("gettable")) {
            parseTable(responseText);
        } else if(url.equals("updaterow")) {
            load();
        }
    }

    @Override
    public void load() {
        try {
            if (key.length()==0)
                dbConnect();
            
            String params = "key=" + key +"&table=" + table; 
            System.out.println("Getrequest results: " + getRequest("gettable", params));
            
        
        } catch (WrongKeyException ex) {
            Logger.getLogger(Reservations.class.getName()).log(Level.SEVERE, null, ex);
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
}

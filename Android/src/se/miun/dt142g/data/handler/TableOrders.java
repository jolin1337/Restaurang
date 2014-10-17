/*
 * This code is created for one purpose only. And should not be used for any 
 * other purposes unless the author of this file has apporved. 
 * 
 * This code is a piece of a project in the course DT142G on Mid. Sweden university
 * Created by students for this projekt only
 */
package se.miun.dt142g.data.handler;

import java.text.SimpleDateFormat;
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

    private List<TableOrder> parseTable(String jsonStr) {
        List<TableOrder> res = new ArrayList<TableOrder>();
        try { 
            JSONObject json = new JSONObject(jsonStr);
            JSONArray data = json.getJSONArray("data");
            for(int i=data.length(); i > 0; i--) {
                JSONObject row = data.getJSONObject(i-1);
                TableOrder order = new TableOrder();
                order.setId(row.getInt("id"));
                order.setTimeOfOrder(new Date(row.getInt("timeOfOrder")));
                JSONArray ds = row.getJSONArray("orders");
                List<Integer> dishesIndicies = new ArrayList<Integer>();
                for(int j=ds.length(); j > 0; j--) {
                    int dishIndex = ds.getInt(j-1);
                    dishesIndicies.add(dishIndex);
                }
                order.setOrderedDishes(dishesIndicies);
                res.add(order);
            }
        } catch (JSONException ex) {
        }
        
        return res;
        
    }
    
    
    @Override
    public void update() throws WrongKeyException {
        List<TableOrder> tableOs = parseTable(getRequest("gettable", "key=" + key + "&table=" + table));
        
        List<TableOrder> toRemove = new ArrayList<TableOrder>();
        for(TableOrder t1 : tableOs) {
            boolean found = false;
            for(TableOrder t2 : tableOrders) {
                if(t1.getId() == t2.getId()) {
                    found = true;
                }
            }
            if(found == false)
                toRemove.add(t1);
        }
        
        String str = "key=" + key + "&table=" + table + "&data=" + toJsonStringOf(tableOrders, false);
        String strRm = "key=" + key + "&table=" + table + "&data=" + toJsonStringOf(toRemove, true);
        System.out.println("Updatestatus: " + getRequest("updaterow", strRm));
        System.out.println("Updatestatus: " + getRequest("updaterow", str));
        
    }
    
    private String toJsonStringOf(List<TableOrder> tblOrders, boolean removeFlag) {
        try {
            JSONObject json = new JSONObject();
            JSONArray data = new JSONArray();
            for(TableOrder rm : tblOrders) {
                JSONObject data2 = new JSONObject();
                JSONObject item = new JSONObject();
                item.put("id", rm.getId());
                if(!removeFlag) {
                    item.put("timeOfOrder", rm.getTimeOfOrder().getTime());
                    JSONArray orders = new JSONArray(rm.getOrderedDishes());
                    item.put("orders", orders);
                }
                else item.put("remove", true);
                data2.put("data", item);
                data.put(data);
            }
            json.put("data", data);
            return json.toString();
        }
        catch(JSONException ex) {}
        return "";
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
        dishes.loadData();
        tableOrders.clear();
        List<TableOrder> tbos = parseTable(getRequest("gettable", "key=" + key + "&table=" + table));
        if(tbos.size() != 6) {
            tableOrders.clear();
            for(int i=0;i<6;i++) {
                tableOrders.add(new TableOrder());
            }
            update();
            return;
        }
        for(TableOrder tableOrder : tbos)
            tableOrders.add(tableOrder);
    }
    
    public Dishes getDishes() {
        return dishes;
    }
    
    public TableOrder getTableOrderByIndex(int index) {
        return tableOrders.get(index);
    }
}

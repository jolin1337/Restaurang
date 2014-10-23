/*
 * This code is created for one purpose only. And should not be used for any 
 * other purposes unless the author of this file has apporved. 
 * 
 * This code is a piece of a project in the course DT142G on Mid. Sweden university
 * Created by students for this projekt only
 */
package se.miun.dt142g.data.entityhandler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import se.miun.dt142g.data.EntityRep.TableHasDish;
import se.miun.dt142g.data.EntityRep.TableOrder;
import static se.miun.dt142g.data.entityhandler.DataSource.key;

/**
 *
 * @author Johannes
 */
public class TableDishRelations extends DataSource implements Iterable<TableHasDish> {
    private List<TableHasDish> relations;
    private int table;

    public TableDishRelations() {
        relations = new ArrayList<TableHasDish>();
    }
    
    public static int indexOfRelation(List<TableHasDish> relations, int tableId, int dishId) {
        int index = 0;
        for(TableHasDish d : relations) {
            if(d.getDish().id == dishId && d.getTableOrder().getId() == tableId)
                return index;
            index++;
        }
        return -1;
    }
    
    private static List<TableHasDish> parse(String json) {
        List<TableHasDish> res = new ArrayList<TableHasDish>();
        try {
            JSONObject rel = new JSONObject(json);
            JSONArray arr = rel.getJSONArray("data");
            for(int index = arr.length(); index > 0; index--) {
                JSONObject item = arr.getJSONObject(index-1);
                JSONObject tableOrder = item.getJSONObject("tableOrder");
                JSONObject dish = item.getJSONObject("dish");
                int relIndex = indexOfRelation(res, tableOrder.getInt("id"), dish.getInt("id"));
                if(relIndex > -1) {
                    res.get(relIndex).parseDish(dish);
                    res.get(relIndex).parseTableOrder(tableOrder);
                    res.get(relIndex).getDish().dishCount++;
                }
                else {
                    TableHasDish thd = new TableHasDish();
                    thd.parseDish(dish);
                    thd.parseTableOrder(tableOrder);
                    res.add(thd);
                }
            }
        } catch (JSONException ex) {
            Logger.getLogger(TableDishRelations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return res;
    }
    
    @Override
    public void loadData() throws WrongKeyException {
        List<TableHasDish> res = parse(getRequest("gettable", "key=" + key + "&table=" + table));
        relations.clear();
        for(TableHasDish d : res) {
            relations.add(d);
        }
    }

    @Override
    public void update() throws WrongKeyException {
        List<TableHasDish> tableOs = parse(getRequest("gettable", "key=" + key + "&table=" + table));
        List<TableHasDish> toRemove = new ArrayList<TableHasDish>();
        for(TableHasDish t1 : tableOs) {
            boolean found = false;
            if(indexOfRelation(relations, t1.getTableOrder().getId(), t1.getDish().id) == -1)
                toRemove.add(t1);
        }
        
        String str = "key=" + key + "&table=" + table + "&data=" + toJsonStringOf(relations, false);
        String strRm = "key=" + key + "&table=" + table + "&data=" + toJsonStringOf(toRemove, true);
    }

    @Override
    public int getUniqueId() {
        return -1;
    }

    public Iterator<TableHasDish> iterator() {
        return relations.iterator();
    }

    private String toJsonStringOf(List<TableHasDish> relations, boolean removeFlag) {
        JSONObject base = new JSONObject();
        
        try {
            JSONObject json = new JSONObject();
            JSONArray data = new JSONArray();
            for(TableHasDish rm : relations) {

                JSONObject data2 = new JSONObject();
                JSONObject item = new JSONObject();
                item.put("id", rm.getTableOrder().getId());
                item.put("dishId", rm.getDish().id);
                item.put("tableNr", rm.getTableOrder().getTable());
                if(!removeFlag) {
                    if(rm.getDish().special)
                        item.put("special", 1);
                    else
                        item.put("special", 0);
                    item.put("dishCount", rm.getDish().dishCount);
                    item.put("timeOfOrder", rm.getTableOrder().getTimeOfOrder().getTime());
                }
                else 
                    item.put("remove", true);
                data2.put("data", item);
                data.put(data2);
            }
            json.put("data", data);
            return json.toString();
        }
        catch(JSONException ex) {}
        return "";
    }

    public List<TableHasDish> getRelations() {
        return relations;
    }
    
}

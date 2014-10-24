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
import se.miun.dt142g.data.handler.Dishes;
import se.miun.dt142g.data.handler.TableOrders;

/**
 *
 * @author Johannes
 */
public class TableDishRelations extends DataSource implements Iterable<TableHasDish> {
    private final List<TableHasDish> relations;
    private final static String table = "tabledishrelation";
    private final Dishes dishes = new Dishes();
    private final TableOrders tableOrders = new TableOrders();
    public static int currentTableOrder = 0;

    public TableDishRelations() {
        relations = new ArrayList<TableHasDish>();
    }
    public Dishes getDishes() {
        return dishes;
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
        tableOrders.loadData();
        dishes.loadData();
        List<TableHasDish> res = parse(getRequest("gettable", "key=" + key + "&table=" + table));
        relations.clear();
        for(TableHasDish d : res) {
            relations.add(d);
        }
    }
    private void loadDataIds() {
        List<TableHasDish> res = parse(getRequest("gettable", "key=" + key + "&table=" + table));
        
        for(TableHasDish d1 : res) {
            for(TableHasDish d2 : relations) {
                if(d1.getDish().id == d2.getDish().id && d1.getTableOrder().getTable() == d2.getTableOrder().getTable()) {
                    d2.getTableOrder().setId(d1.getTableOrder().getId());
                }
            }
        }
    }

    @Override
    public void update() throws WrongKeyException {
        tableOrders.loadData();
        dishes.loadData();
        List<TableHasDish> tableOs = parse(getRequest("gettable", "key=" + key + "&table=" + table));
        List<TableHasDish> toRemove = new ArrayList<TableHasDish>();
        for(TableHasDish t1 : tableOs) {
            boolean found = false;
            if(indexOfRelation(relations, t1.getTableOrder().getId(), t1.getDish().id) == -1)
                toRemove.add(t1);
        }
        for(int i = relations.size(); i > 0; i--) {
            TableHasDish hd1 = relations.get(i-1);
            for(int j = relations.size(); j > 0; j--) {
                TableHasDish hd2 = relations.get(j-1);
                if(i-1 != j-1 && hd1.getDish().id == hd2.getDish().id 
                        && hd1.getTableOrder().getTable() == hd2.getTableOrder().getTable()) {
                    if(hd1.getTableOrder().getId() > -1) {
                        hd1.getDish().dishCount++;
                        hd1.getDish().special |= hd2.getDish().special;
                        relations.remove(hd2);
                        break;
                    }
                    else if(hd2.getTableOrder().getId() > -1) {
                        hd2.getDish().dishCount++;
                        hd2.getDish().special |= hd1.getDish().special;
                        relations.remove(hd1);
                        break;
                    }
                }
            }
        }
        
        String str = "key=" + key + "&table=" + table + "&data=" + toJsonStringOf(relations, false);
        String strRm = "key=" + key + "&table=" + table + "&data=" + toJsonStringOf(toRemove, true);
        
        System.out.println("Updatestatus: " + getRequest("updaterow", strRm));
        System.out.println("Updatestatus: " + getRequest("updaterow", str));
        loadData();
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

    public void clearDishesFromTable(int tbl) {
        
        List<TableHasDish> res = getDishesFromTable(tbl);
        for(TableHasDish hd : res) {
            if(tbl == hd.getTableOrder().getTable())
                relations.remove(hd);
        }
    }
    public List<TableHasDish> getDishesFromTable(int tbl) {
        List<TableHasDish> res = new ArrayList<TableHasDish>();
        for(TableHasDish hd : relations) {
            if(tbl == hd.getTableOrder().getTable())
                res.add(hd);
        }
        return res;
    }

    public int getTableSize() {
        List<Integer> res = new ArrayList<Integer>();
        for(TableHasDish hd : relations) {
            boolean found = false;
            for(Integer i : res) {
                if(i == hd.getTableOrder().getTable())
                    found = false;
            }
            if(!found)
                res.add(hd.getTableOrder().getTable());
        }
        return res.size();
    }

    public TableOrder getTable(int tbl) {
        for(TableHasDish hd : relations) {
            if(tbl == hd.getTableOrder().getTable())
                return hd.getTableOrder();
        }
        return null;
    }
    public TableOrder getTableOrder(int tbl) {
        return tableOrders.getTable(tbl);
    }

    
}

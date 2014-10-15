/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package miun.dt142g.food;
import java.util.ArrayList;
import java.util.List;
import miun.dt142g.DataSource;
import miun.dt142g.Settings;
import miun.dt142g.data.Dish;
import miun.dt142g.data.EventPost;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
/**
 *
 * @author Nikander Gielen
 */
public class DishGroups extends DataSource{
    private final String table = "dishgroup";
    private final List<DishGroup> dishGroups = new ArrayList<>();
    private Dishes dishes = new Dishes();

    /**
     * @param groupNames
     * @return the dishesInGroup
     */
    public List<DishGroup> getDishGroups(String[] groupNames) {
        List<DishGroup> dishesInGroup = new ArrayList<>();
        for(DishGroup d : dishGroups){
            for (String groupName : groupNames) {
                if (d.getGroup() == groupName) {
                    dishesInGroup.add(d);
                }
            }
        }
        
        return dishesInGroup;
    }

    @Override
    public void loadData() throws WrongKeyException {  
        dishes.loadData();
        dishGroups.clear();
        List<DishGroup> dGroups = getDataList();
        for(String day : Settings.weekDays)
            if(!dGroups.contains(day))
                dishGroups.add(new DishGroup(day));
        if(!dGroups.contains(Settings.aLaCarte[0]))
            dishGroups.add(new DishGroup(Settings.aLaCarte[0]));
        for(DishGroup dishGroup : dGroups)
            dishGroups.add(dishGroup);
        /*dishGroups.add(new DishGroup(getUniqueId(), 1, "Måndag")); 
        dishGroups.add(new DishGroup(getUniqueId(), 2, "Måndag")); 
        dishGroups.add(new DishGroup(getUniqueId(), 0, "Tisdag")); 
        dishGroups.add(new DishGroup(getUniqueId(), 0, "Onsdag")); 
        dishGroups.add(new DishGroup(getUniqueId(), 0, "Torsdag")); 
        dishGroups.add(new DishGroup(getUniqueId(), 0, "Fredag")); 
        dishGroups.add(new DishGroup(getUniqueId(), 0, "A la Carte")); 
        dishGroups.add(new DishGroup(getUniqueId(), 2, "A la Carte"));
        dishGroups.add(new DishGroup(getUniqueId(), 1, "A la Carte"));*/
    }

    @Override
    public void update() throws WrongKeyException {
        dishes.loadData();
        List<DishGroup> dg = getDataList();
        String str = "&table=dish&data={\"data\":[";
        String strRm = "&table=dish&data={\"data\":[";
        for(DishGroup dishGroup : dg)
            strRm += "{\"data\":{\"remove\":true,\"id\":" + dishGroup.getGroup()+ "}},";
        for (DishGroup dishGroup : dishGroups) {
            String gName = dishGroup.getGroup();
            dishGroup.setGroup("");
            str += "{\"data\":" + dishGroup.toJsonString() + "},";
            dishGroup.setGroup(gName);
        }
        if(dishGroups.isEmpty())
            str += ",";
        if(dg.isEmpty())
            strRm += ",";
        System.out.println(str.substring(0, str.length()-1) + "]}");
        // System.out.println(strRm.substring(0, strRm.length()-1) + "]}");
        System.out.println("Updatestatus: " + getRequest("updaterow", "key=" + key + strRm.substring(0, strRm.length()-1) + "]}"));
        System.out.println("Updatestatus: " + getRequest("updaterow", "key=" + key + str.substring(0, str.length()-1) + "]}"));
        
        // To make sure that we have the correct id:s/pk:s
        loadData();
    }
    
    private List<DishGroup> getDataList() throws WrongKeyException {
        List<DishGroup> currentGroups = new ArrayList<>();
        JSONObject json;
        String jsonStr = getRequest("gettable", "key=" + key + "&table=" + table);
        if(jsonStr.equals("expired_key")) {
            dbConnect();
            jsonStr = getRequest("gettable", "key=" + key + "&table=" + table);
        }
        try {
            json = new JSONObject(jsonStr);
        } catch (JSONException ex) {
            System.out.println("Could not verify the JSON parse of json object: " + jsonStr);
            return currentGroups;
        }

        JSONArray jsonArr;
        try {
            jsonArr = json.getJSONArray("data");
        } catch (JSONException ex) {
            return currentGroups;
        }
        for (int i = jsonArr.length(); i > 0; i--) {
            JSONObject obj;
            DishGroup dg = null;
            try {
                obj = jsonArr.getJSONObject(i-1);
                
                // Get the properties of the json object and update this event.
                dg = new DishGroup(obj.getString("name"));
                JSONArray groupDishes = obj.getJSONArray("dishes");
                for (int j = groupDishes.length(); j > 0; j--) {
                    dg.addDish(groupDishes.getInt(j-1));
                }
                currentGroups.add(dg);
            } catch (JSONException ex) {
                if (dg != null) {
                    System.out.println("Unable to parse json object: " + dg);
                } else {
                    System.out.println("Unable to parse json object: null");
                }
            }
        }
        return currentGroups;            // return that we have changed this entity
    }
    

    @Deprecated
    @Override
    public int getUniqueId() {
        return -1;
    }

    Dishes getDishes() {
        return dishes;
    }
    
}

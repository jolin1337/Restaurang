package se.miun.dt142g.food;

import java.util.ArrayList;
import java.util.List;
import se.miun.dt142g.DataSource;
import se.miun.dt142g.Settings;
import se.miun.dt142g.data.Dish;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This is a DataSource object that describes an entire DishGroup table.
 *
 * @author Nikander Gielen
 */
public class DishGroups extends DataSource {

    /**
     * Table name identifier to the server of the DishGroup
     */
    private final String table = "dishgroup";
    /**
     * An array of all the dishes this group contains (the relations)
     */
    private final List<DishGroup> dishGroups = new ArrayList<>();
    /**
     * Contains an instance of all dishes on the database
     */
    private final Dishes dishes = new Dishes();

    /**
     * @return Returns the set of dishes currently available on the database
     */
    Dishes getDishes() {
        return dishes;
    }

    /**
     * @param groupNames - Filters the dishes and returns only the dishes that
     * is a memeber of the groups defined by the names in groupName
     * @return A list of dishes that is a member at least one of the groups in
     * groupNames
     */
    public List<DishGroup> getDishGroups(String[] groupNames) {
        List<DishGroup> dishesInGroup = new ArrayList<>();
        for (String groupName : groupNames) {
            for (DishGroup d : dishGroups) {
                if (d.getGroup().equals(groupName)) {
                    dishesInGroup.add(d);
                    break;
                }
            }
        }

        return dishesInGroup;
    }

    /**
     * Finds a DishGroup object that has the name group
     *
     * @param group - The group name of the desired group to find
     * @return The dishgroup that was found or null otherwise
     */
    public DishGroup findGroup(String group) {
        for (DishGroup dishGroup : dishGroups) {
            if (dishGroup.getGroup().equals(group)) {
                return dishGroup;
            }
        }
        return null;
    }

    /**
     * Remove all dishes from a group with name group. if no group found nothing
     * will happen.
     *
     * @param group - removes the group from dishGroups
     */
    public void removeAllFromGroup(String group) {
        DishGroup dishGroup = findGroup(group);
        if (dishGroup != null) {
            dishGroup.getDishes().clear();
        }
    }

    /**
     * Add a dish to dishgroups
     *
     * @param group - Which group the dish should be added into
     * @param dish - The dish to add
     */
    public void addDishToGroup(String group, Dish dish) {
        DishGroup dishGroup = findGroup(group);
        if (dishGroup != null) {
            dishGroup.addDish(dish.getId());
        }
    }

    /**
     * A method to verify whether a group exists or not
     *
     * @param dishGroup - The list of groups to iterate
     * @param group - The group to verify
     * @return true if the group exists, otherwise false
     */
    static private boolean groupExists(List<DishGroup> dishGroup, String group) {
        for (DishGroup gdish : dishGroup) {
            if (gdish.getGroup().equals(group)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void loadData() throws WrongKeyException {
        dishes.loadData();
        dishGroups.clear();
        List<DishGroup> dGroups = getDataList();
        for (String day : Settings.weekDays) {
            if (!groupExists(dGroups, day)) {
                dishGroups.add(new DishGroup(day));
            }
        }
        if (!groupExists(dGroups, Settings.aLaCarte[0])) {
            dishGroups.add(new DishGroup(Settings.aLaCarte[0]));
        }
        for (DishGroup dishGroup : dGroups) {
            dishGroups.add(dishGroup);
        }
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
        String str = "&table=" + table + "&data={\"data\":[";
        String strRm = "&table=" + table + "&data={\"data\":[";
        for (DishGroup dishGroup : dg) {
            strRm += "{\"data\":{\"remove\":true,\"name\":\"" + dishGroup.getGroup() + "\"}},";
        }
        for (DishGroup dishGroup : dishGroups) {
            str += "{\"data\":" + dishGroup.toJsonString() + "},";
        }
        if (dishGroups.isEmpty()) {
            str += ",";
        }
        if (dg.isEmpty()) {
            strRm += ",";
        }
        System.out.println(str.substring(0, str.length() - 1) + "]}");
        System.out.println(strRm.substring(0, strRm.length() - 1) + "]}");
        System.out.println("Updatestatus: " + getRequest("updaterow", "key=" + key + strRm.substring(0, strRm.length() - 1) + "]}"));
        System.out.println("Updatestatus: " + getRequest("updaterow", "key=" + key + str.substring(0, str.length() - 1) + "]}"));

        // To make sure that we have the correct id:s/pk:s
        loadData();
    }

    /**
     * Retrieves a list from the database of all groups
     *
     * @return the list of all groups
     * @throws se.miun.dt142g.DataSource.WrongKeyException
     */
    private List<DishGroup> getDataList() throws WrongKeyException {
        List<DishGroup> currentGroups = new ArrayList<>();
        JSONObject json;
        String jsonStr = getRequest("gettable", "key=" + key + "&table=" + table);
        if (jsonStr.equals("expired_key")) {
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
                obj = jsonArr.getJSONObject(i - 1);

                // Get the properties of the json object and update this event.
                dg = new DishGroup(obj.getString("name"));
                JSONArray groupDishes = obj.getJSONArray("dishes");
                for (int j = groupDishes.length(); j > 0; j--) {
                    dg.addDish(groupDishes.getInt(j - 1));
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

    /**
     * Retrieves a list of all groups a dish belongs to
     *
     * @param dishId - The dish to find the belonging groups
     * @return a list of all groups the dish belongs to
     */
    public List<String> getDishGroups(int dishId) {
        List<String> groupNames = new ArrayList<String>();
        for (DishGroup dg : dishGroups) {
            for (Integer d : dg.getDishes()) {
                if (d == dishId) {
                    groupNames.add(dg.getGroup());
                }
            }
        }
        return groupNames;
    }

    @Deprecated
    @Override
    public int getUniqueId() {
        return -1;
    }

}

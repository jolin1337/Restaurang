package se.miun.dt142g.food;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * DishGroup contains a set of dishes for a particular group
 *
 * @author Tomas
 */
public class DishGroup {

    /**
     * List of dishes included in this DishGroup object
     */
    private List<Integer> dishIds;

    /**
     * Name of the group for this DishGroup object
     */
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

    /**
     * @return A List with the Ids of the dishes for this DishGroup object
     */
    public List<Integer> getDishes() {
        initDishIds();
        return dishIds;
    }

    /**
     * Adds a dish Id to the list of dish Ids for this DishGroup object
     *
     * @param dishIndex the Id of the Dish to add
     */
    public void addDish(int dishIndex) {
        initDishIds();
        if (!dishIds.contains(dishIndex)) {
            dishIds.add(dishIndex);
        }
    }

    /**
     * Sets the List of dish Ids to the list of the parameter
     *
     * @param dishId the List of dish Ids to set
     */
    public void setDishIds(List<Integer> dishId) {
        this.dishIds = dishId;
    }

    /**
     * Constructor for Dishgroup, sets dishIds list and group name
     *
     * @param dishId List of Ids to set for the list of Ids
     * @param group Name of the group
     */
    public DishGroup(List<Integer> dishId, String group) {
        this.dishIds = dishId;
        this.group = group;
    }

    /**
     * Constructor for Dishgroup, set dish Ids to null and sets group name from
     * parameter
     *
     * @param group Name of the group
     */
    public DishGroup(String group) {
        this.dishIds = null;
        this.group = group;
    }

    /**
     * Sets the dish ids to a new empty list array
     */
    private void initDishIds() {
        if (dishIds == null) {
            setDishIds(new ArrayList<Integer>());
        }
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

    /**
     * Converts this DishGroup to a json string with its values
     *
     * @return A string with json syntax
     */
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

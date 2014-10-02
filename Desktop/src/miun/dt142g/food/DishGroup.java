/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package miun.dt142g.food;
import java.util.ArrayList;
import miun.dt142g.data.Dish;
/**
 *
 * @author Tomas
 */
public class DishGroup {
    private int dishId;
    private String group;
    private final int groupId;

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

    public int getDishId() {
        return dishId;
    }

    public void setDishId(int dishId) {
        this.dishId = dishId;
    }

    public DishGroup(int groupId, int dishId, String group) {
        this.groupId = groupId;
        this.dishId = dishId;
        this.group = group;
    }

    public int getGroupId() {
        return groupId;
    }

}

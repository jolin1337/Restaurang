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
    private ArrayList<Dish> dishesInGroup;
    private String group;

    public void addDish(Dish d){
        dishesInGroup.add(d);
    }
        
    /**
     * Remove the dish(es) from the group with name equal to id
     * Possibly replaceable with Dish as parameter
     * @param id 
     */
    public void remove(String id) {
        for (Dish d : dishesInGroup) {
            if (id == null ? d.toString() == null : id.equals(d.toString())) {
                dishesInGroup.remove(d);
            }
        }
    }
    /**
     * @return the dishesInGroup
     */
    public ArrayList<Dish> getDishesInGroup() {
        return dishesInGroup;
    }

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
    
}

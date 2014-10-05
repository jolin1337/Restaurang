/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package miun.dt142g.food;
import java.util.ArrayList;
import java.util.List;
import miun.dt142g.DataSource;
import miun.dt142g.data.Dish;
/**
 *
 * @author Nikander Gielen
 */
public class DishGroups extends DataSource{
    private final List<DishGroup> dishGroups = new ArrayList<>();
    private Dishes dishes = new Dishes();


    public void addGroup(String groupName, int dishId){
        dishGroups.add(new DishGroup(getUniqueId(),dishId,groupName));
    }
        
    public void removeGroup(int id) {
        for (DishGroup d : dishGroups) {
            if (id == d.getGroupId()) {
                dishGroups.remove(d);
            }
        }
    }
    /**
     * @param groupNames
     * @return the dishesInGroup
     */
    public List<DishGroup> getDishesInGroup(String[] groupNames) {
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
    public void dbConnect() throws WrongKeyException{
        super.dbConnect();
        dishes.dbConnect();
    }

    @Override
    public void loadData() {  
        dishes.loadData();
        dishGroups.add(new DishGroup(getUniqueId(), 1, "Måndag")); 
        dishGroups.add(new DishGroup(getUniqueId(), 2, "Måndag")); 
        dishGroups.add(new DishGroup(getUniqueId(), 0, "Tisdag")); 
        dishGroups.add(new DishGroup(getUniqueId(), 0, "Onsdag")); 
        dishGroups.add(new DishGroup(getUniqueId(), 0, "Torsdag")); 
        dishGroups.add(new DishGroup(getUniqueId(), 0, "Fredag")); 
        dishGroups.add(new DishGroup(getUniqueId(), 0, "A la Carte")); 
        dishGroups.add(new DishGroup(getUniqueId(), 2, "A la Carte"));
        dishGroups.add(new DishGroup(getUniqueId(), 1, "A la Carte"));
    }

    @Override
    public void update() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getUniqueId() {
         int id = 0;
        for(DishGroup d : dishGroups) {
            if(d.getGroupId() > id)
                id = d.getGroupId()+1;
        }
        return id;
    }

    Dishes getDishes() {
        return dishes;
    }
    
}

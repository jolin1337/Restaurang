/*
 * This code is created for one purpose only. And should not be used for any 
 * other purposes unless the author of this file has apporved. 
 * 
 * This code is a piece of a project in the course DT142G on Mid. Sweden university
 * Created by students for this projekt only
 */
package se.miun.dt142g.data.EntityRep;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Johannes
 */
public class TableHasDish {
    public class DishIndex {
        public int id = -1;
        public boolean special = false;
        public int dishCount = 1;

        private void setDish(Dish dish) {
            id = dish.getId();
        }
    }
    private DishIndex dish = new DishIndex();
    private TableOrder tableOrder = null;

    public DishIndex getDish() {
        if(this.dish == null)
            this.dish = new DishIndex();
        return dish;
    }

    public void setDish(DishIndex dish) {
        this.dish = dish;
    }
    public void setDish(Dish dish) {
        if(this.dish == null)
            this.dish = new DishIndex();
        this.dish.setDish(dish);
    }

    public TableOrder getTableOrder() {
        return tableOrder;
    }

    public void setTableOrder(TableOrder tableOrder) {
        this.tableOrder = tableOrder;
    }

    public void parseDish(JSONObject dish) throws JSONException {
            if(this.dish == null)
                this.dish = new DishIndex();
            this.dish.dishCount = dish.getInt("dishCount");
            this.dish.special = dish.getInt("special") > 0;
            this.dish.id = dish.getInt("id");
    }

    public void parseTableOrder(JSONObject tableOrder) throws JSONException {
        if(this.tableOrder == null)
            this.tableOrder = new TableOrder(tableOrder.getInt("id"));
        this.tableOrder.setId(tableOrder.getInt("id"));
        this.tableOrder.setTable(tableOrder.getInt("tableNr"));
        this.tableOrder.setTimeOfOrder(tableOrder.getInt("timeOfOrder"));
    }
    
    
}

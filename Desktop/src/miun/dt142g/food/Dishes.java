/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package miun.dt142g.food;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import miun.dt142g.DataSource;
import miun.dt142g.data.Dish;

/**
 *
 * @author Ulf
 */
public class Dishes extends DataSource implements Iterable<Dish> {
    private final List<Dish> dishes = new ArrayList<>();
    
    public Dishes(){
    }
    
    public int getRows() {
        return dishes.size();
    }
    public Dish getDish(int id){
        return dishes.get(dishes.indexOf(id)); 
    }
    
    public void addDish(Dish dish){
        dishes.add(dish);
    }
    
    public void removeDish(int id){
        dishes.remove(this.getDish(id)); 
    }
    
    public void editDish(int id, Dish dish){
        dishes.set(dishes.indexOf(id), dish);
    }

    @Override
    public void loadData() {
        dishes.add(new Dish(0, "Fisk", 100.2f, null));
        dishes.add(new Dish(1, "Fikon", 10.6f, null));
        dishes.add(new Dish(2, "Pasta", 56.9f, null));
    }

    @Override
    public void update() {
    }

    @Override
    public Iterator<Dish> iterator() {
        return dishes.iterator();
    }
}

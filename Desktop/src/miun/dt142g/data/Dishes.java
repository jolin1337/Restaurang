/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package miun.dt142g.data;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Ulf
 */
public class Dishes{
    private final ArrayList<Dish> dishes;
    
    public Dishes(Dish dish[]){
       dishes = new ArrayList<>(Arrays.asList(dish));
    }
    
    public Dish getDish(int id){
        return dishes.get(dishes.indexOf(id)); 
    }
    
    public void addDish(Dish dish){
        dishes.add(dish);
    }
    
    public void remove(int id){
        dishes.remove(this.getDish(id)); 
    }
    
    public void editDish(int id, Dish dish){
        dishes.set(dishes.indexOf(id), dish);
    }
}

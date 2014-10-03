/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package miun.dt142g;

import miun.dt142g.data.Booking;
import miun.dt142g.data.Dish;

/**
 *
 * @author Johannes
 */
public interface Controller {
    void setViewWebsite ();
    
    void setViewDishes();
    
    void setViewDishDetail(Dish d);
    void setViewNewBooking(Booking b);
    
    void setViewInventory();
    
    void setViewUsers();
    
    /**
     * ... more comming ...
     */
}

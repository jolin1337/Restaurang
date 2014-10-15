/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package miun.dt142g;

import java.awt.event.ActionListener;
import miun.dt142g.data.Booking;
import miun.dt142g.data.Dish;

/**
 *
 * @author Johannes
 */
public interface Controller {
    public void setViewWebsite ();
    
    public void setViewDishes();
    
    public void setViewDishDetail(Dish d, ActionListener removeEvent);
    
    public void setViewNewBooking(Booking b);
    
    public void setViewBookings();
    
    public void setViewInventory();
    
    public void setViewUsers();

    public void setConnectionView();
    
    /**
     * ... more comming ...
     */
}

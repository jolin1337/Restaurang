/*
 * This code is created for one purpose only. And should not be used for any 
 * other purposes unless the author of this file has apporved. 
 * 
 * This code is a piece of a project in the course DT142G on Mid. Sweden university
 * Created by students for this projekt only
 */
package se.miun.dt142g;

import java.awt.event.ActionListener;
import javax.swing.JComponent;
import se.miun.dt142g.data.Booking;
import se.miun.dt142g.data.Dish;


/**
 * Controlls witch view to be displayed and the user needs to implement how this
 * is done. Each function represents a view except setSavedTabs witch is used for
 * setting a star at the tab title.
 *
 * @author Johannes Lindén
 * @since 2014-10-07
 * @version 1.0
 */
public interface Controller {
    /**
     * The websiteview witch controlls general things on the website like events
     * opening hours and contact information
     */
    public void setViewWebsite ();
    
    /**
     * The Dishes view shows a list of all dishes available for the menues
     */
    public void setViewDishes();
    
    /**
     * This view is called from DishDetail view to give some feedback if there
     * was any changes made to that dish
     * @param saved - The indicator if true then something has changed otherwise
     * false
     */
    public void setViewDishes(boolean saved);
    
    /**
     * This view contains details on a dish. Like ingredients and price for the 
     * dish. You have the posibility to eneter the name of the dish here as well
     * @param d - The dish to edit details on
     * @param removeEvent - An event lister if the user wants to remove the 
     * current dish.
     */
    public void setViewDishDetail(Dish d, ActionListener removeEvent);
    
    /**
     * This view is used when the user wants to create a new Reservation for a
     * table. THis creates a view that the user can alter the reservation details
     * @param b - A new booking element reference
     */
    public void setViewNewBooking(Booking b);
    
    /**
     * This view displays a table of all the reservations that is done.
     */
    public void setViewBookings();
    
    /**
     * This view displays a list of all ingredients currently known to the system
     * and the amount (port.) for each ingredient in the inventory
     */
    public void setViewInventory();
    
    /**
     * This displays a grouplist consisting of user attributes.
     */
    public void setViewUsers();

    /**
     * If there was some errors when connecting to the dataabse this view is 
     * displayed that you have the posibility to reconnect
     */
    public void setConnectionView();
    
    /**
     * This is not an view but it alters the views (tabView) title text to 
     * display a asterix if savedState is true oteherwise just the name.
     * @param tabView - The top view you want to alter
     * @param savedState - If false an asterix becomes visible at the right of 
     * the text in the title. if true the asterix disapears. The asterix indicates
     * an unsaved state
     */
    public void setSavedTab(JComponent tabView, boolean savedState);
}

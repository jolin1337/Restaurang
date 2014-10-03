/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package miun.dt142g.bookings;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import miun.dt142g.DataSource;
import miun.dt142g.data.Booking;

/**
 *
 * @author Marcus
 */
public class Bookings extends DataSource implements Iterable<Booking> {
    private final List<Booking> bookings = new ArrayList<>();

    public Bookings() {
    }

    public int getRows() {
        return bookings.size();
    }

    public Booking getBooking(int id){
        for(Booking b : bookings)
            if(b.getId() == id)
                return b;
        return null; 
    }
    
   public void addBooking(Booking booking){
        bookings.add(booking);
    }
   
    public void removeBooking(int id){
        bookings.remove(this.getBooking(id)); 
    }
    
    public void editBooking(int id, Booking booking){
        bookings.set(bookings.indexOf(id), booking);
    }
    
    @Override
    public void loadData() {
        Date date = new Date();
        bookings.add(new Booking(0, "chocklad", date, 0, 0, 0));
        bookings.add(new Booking(1, "majs", date, 0, 17, 44));
        bookings.add(new Booking(2, "pannkaka", date, 0, 0, 0));
        
    }

    @Override
    public void update() {
    }

    @Override
    public int getUniqueId() {
        int id  = 0;
        for(Booking bok : bookings){
            if(bok.getId() > id)
                id = bok.getId()+1;
        }
        return id; 
    } 

    @Override
    public Iterator<Booking> iterator() {
        return bookings.iterator();
    }
    
}

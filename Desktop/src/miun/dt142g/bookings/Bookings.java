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
import java.util.logging.Level;
import java.util.logging.Logger;
import miun.dt142g.DataSource;
import miun.dt142g.data.Booking;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

    /**
    * Returns the requested booking if it exists. 
    * @param id of the requested booking
    * @return the booking instance or null if it doesn't exist.
    */
    public Booking getBooking(int id){
        for(Booking b : bookings)
            if(b.getId() == id)
                return b;
        return null; 
    }
    public Booking getBookingByIndex(int index){
        return bookings.get(bookings.size()-1);
    }
    
   public void addBooking(Booking booking){
        bookings.add(booking);
    }
   
    public void removeBooking(int id){
        bookings.remove(this.getBooking(id));
        removeBookingDb(id);
    }
    
    public void editBooking(int id, Booking booking){
        bookings.set(bookings.indexOf(id), booking);
    }
    
    @Override
    public void loadData() {
        JSONObject response = null;
        JSONArray data = null; 
        try {
            response = getJsonRequest("booking");
            data = response.getJSONArray("data");
        } catch (JSONException ex) {
            Logger.getLogger(Bookings.class.getName()).log(Level.SEVERE, null, ex);
        }
        for(int i = 0;i<data.length();i++){
            try {
                addJsonBooking(data.getJSONObject(i));
            } catch (JSONException ex) {
                Logger.getLogger(Bookings.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
    * Attempts to create a new booking instance and add it to the bookings iterator 
    * @param bok the booking to be added.
    */
    private void addJsonBooking(JSONObject bok){
        try { 
            Booking b = new Booking(bok.getInt("id"), bok.getString("name"), new Date(bok.getLong("date")), bok.getInt("duration"), bok.getInt("persons"), bok.getInt("phone"));
            bookings.add(b);
        } catch (JSONException ex) {
            Logger.getLogger(Bookings.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
        @Override
    public void update() {
        try {
            JSONArray data = new JSONArray(); 
            for(Booking bok : this.bookings) {
                JSONObject jsonDataElement = new JSONObject();
                JSONObject jsonBooking = new JSONObject();
                if (bok.getId()<0)
                    jsonBooking.put("id", -1);
                else
                jsonBooking.put("id", bok.getId());
                jsonBooking.put("name", bok.getName());
                jsonBooking.put("phone", Integer.toString(bok.getPhoneNr()));
                jsonBooking.put("date", Long.toString(bok.getDate().getTime()));
                
                jsonBooking.put("duration", bok.getDuration());
                jsonBooking.put("persons", bok.getPersons());
                jsonDataElement.put("data", jsonBooking);
                data.put(jsonDataElement);
            }
            JSONObject send = new JSONObject(); 
            send.put("data", data);
            String urlParams = "key=" + key + "&table=booking&data="+send.toString();
            System.out.println("Update status: " +getRequest("updaterow", urlParams));
        } 
        catch (JSONException ex) {
            Logger.getLogger(Booking.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public int getUniqueId() { 
        int id  = -1;
        for(Booking bok : bookings){
            if(bok.getId() <= id)
                id = bok.getId()-1;
        }
        return id; 
    } 

    @Override
    public Iterator<Booking> iterator() {
        return bookings.iterator();
    }
    
    /**
    * Removes an instance of bookings in the databse based on specified ID 
    * @param id is the ID of the booking instance to be removed
    * @return returns whether the instance has successfully been remoeved or not
    */
    public boolean removeBookingDb(int id) {
        JSONObject objectToRemove = new JSONObject();
        JSONObject jsonData = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONObject containerToSend = new JSONObject();
        try {
            objectToRemove.put("id", id);
            objectToRemove.put("remove", true);
            jsonData.put("data", objectToRemove);
            jsonArray.put(jsonData);
            containerToSend.put("data", jsonArray);
        } catch (JSONException ex) {
            Logger.getLogger(Bookings.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        String params = "key=" +key + "&table=booking&data="+containerToSend.toString();
        System.out.println("Update status: " +getRequest("updaterow", params));
    return true;
    }
}

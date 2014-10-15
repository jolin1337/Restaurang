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
//        Date date = new Date();
//        // Get time by user input in hours, minutes and date
//        // date.getTime() + 1000*3600*hours + 1000*60*minutes;
//        bookings.add(new Booking(0, "chocklad", date, 1, 0, 0));
//        bookings.add(new Booking(1, "majs", date, 2, 3, 0));
//        bookings.add(new Booking(2, "pannkaka", date, 2, 0, 0));
        
    }

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
                
                Date d = new Date();
                d.setTime(bok.getDate().getTime());
                System.out.println("Time at input: "+d);
                
                jsonBooking.put("duration", bok.getDuration());
                jsonBooking.put("persons", bok.getPersons());
                jsonDataElement.put("data", jsonBooking);
                data.put(jsonDataElement);
            }
            JSONObject send = new JSONObject(); 
            send.put("data", data);
            System.out.println("Same value in json format: "+send.toString());
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

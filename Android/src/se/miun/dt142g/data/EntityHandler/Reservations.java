/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.miun.dt142g.data.EntityHandler;

import se.miun.dt142g.data.EntityRep.Reservation;
import java.util.ArrayList;
import static java.util.Collections.sort;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import se.miun.dt142g.DataSource;


/**
 *
 * @author Ulf
 */
public class Reservations extends DataSource implements Iterable<Reservation>{
    private static List<Reservation> reservations = new ArrayList<Reservation>(); 
    
    public Reservations() {
    }
    
    @Deprecated
    public boolean readReservations() {
        return true;
    }
     
    /**
     * Gets a list of reservations that are for the same day as parameter day
     * @param day day of the month to get reservations for
     * @return List of reservations
     */
    public List<Reservation> getReservations(int day){
        List<Reservation> temp = new ArrayList<Reservation>(); 
        for(Reservation r : reservations){
            if(r.getDate().getDate()==day)
                temp.add(r);
        }
        sort(temp);
        return temp; 
    }

    public Iterator<Reservation> iterator() {
        return reservations.iterator();
    }

    /**
     * <Warning> Don't call this function manually. Should only be called by 
     * ServerConnect Class in DataSource. 
     * 
     * Interprets response from server and deals with the response as needed. 
     * 
     * @param url Parameter sent with getRequest. 
     * @param responseText  Response from server
     * @throws se.miun.dt142g.DataSource.WrongKeyException 
     */
    @Override
    public void loadData(String url, String responseText) throws WrongKeyException {
        if (url.equals("login")) {
            key = responseText;
        } else if (url.equals("gettable")) {
            parseReservation(responseText);
        }
    }

    /**
     * Parses a string to json objects and calls addJsonBooking for each object to
     * add them in reservations.
     * @param jsonStr Json string representation to parse into json objects.
     */
    private void parseReservation(String responseText){
        reservations.clear();
        JSONObject response;
        JSONArray data = null; 
        
        try {
            response = new JSONObject(responseText);
            data = response.getJSONArray("data");
        } catch (JSONException ex) {
            Logger.getLogger(Reservations.class.getName()).log(Level.SEVERE, null, ex);
        }
        for(int i = 0;i<data.length();i++){
            try {
                addJsonBooking(data.getJSONObject(i));
            } catch (JSONException ex) {
                Logger.getLogger(Reservations.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }
    
    /**
     * Adds a reservation from a JSONObject
     * @param res JSONObject of reservation to add
     */
    private void addJsonBooking(JSONObject res){
        try { 
            Reservation b = new Reservation(res.getString("name"), new Date(res.getLong("date")), res.getInt("duration"), res.getInt("persons"), res.getString("phone"));
            reservations.add(b);
        } catch (JSONException ex) {
            Logger.getLogger(Reservations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    @Override
    public void update() throws WrongKeyException {
        //not needed in this class
    }

    @Override
    public int getUniqueId() {
        //not needed in this class
        return 0; 
    }

    /**
     * Loads dishes from database. Use in activity where appropriate or use for 
     * polling the server for data. 
     */
    @Override
    public void load() {
            //if (key.length()==0)
            //dbConnect();
            
            String params = "key=" + key +"&table=booking"; 
            System.out.println("Getrequest results: " + sendRequestFromThread("gettable", params));
            
        
    }
}

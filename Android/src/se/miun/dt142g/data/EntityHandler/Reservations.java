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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
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
    private List<Reservation> reservations = new ArrayList<Reservation>(); 
    
    public Reservations() {
    }
    public boolean readReservations() {
        return true;
    }
     
    
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

    @Override
    public void loadData(String url, String responseText) throws WrongKeyException {
        if (url.equals("login")) {
            key = responseText;
        } else if (url.equals("gettable")) {
            parseReservation(responseText);
        }
    }

    private void parseReservation(String responseText){
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
        //don't think it's needed in this class
    }

    @Override
    public int getUniqueId() {
        //not needed in this class
        return 0; 
    }

    @Override
    public void load() {
        ServerConnect connection = new ServerConnect(); 
        try {
        dbConnect();
        System.out.print("The key is: " + key);
        //String response = connection.execute("gettable", "key=" + getSafeKey()+"&table=bookings").get();
        //System.out.println(response);
//        String params = "key=" + key + "&table=bookings";
//        String response = getRequest("gettable", params);
//        System.out.println(response);
//        } catch (InterruptedException ex) {
//        } catch (ExecutionException ex) {
        } catch (WrongKeyException ex) {
            Logger.getLogger(Reservations.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

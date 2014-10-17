/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package se.miun.dt142g.data;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;


/**
 *
 * @author Marcus
 */
public class Booking {
    private int id;
    
    // Variables for adding a table
    private int duration, persons = 1;
    private String phoneNr = ""; 
    private String name;
    private Date date;
    
    // Construct a new booking
    public Booking(int id, String name, Date date, int duration, int persons, String phoneNr){
        this.id = id;
        this.name = name;
        this.date = date;
        this.duration = duration; 
        this.persons = persons;
        this.phoneNr = phoneNr;
    }

    /**
     * @return the date
     */
    public Date getDate() {
        return date;
    }
    public String getDateString() {
        DateFormat df = new SimpleDateFormat("dd/MM-yy 'kl:' HH:mm");
        return df.format(this.date);
    }

    /**
     * @param date the date to set
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * @return the duration
     */
    public int getDuration() {
        return duration;
    }

    /**
     * @param duration the duration to set
     */
    public void setDuration(int duration) {
        this.duration = duration;
    }

    /**
     * @return the persons
     */
    public int getPersons() {
        return persons;
    }

    /**
     * @param persons the persons to set
     */
    public void setPersons(int persons) {
        this.persons = persons;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the phoneNr
     */
    public String getPhoneNr() {
        return phoneNr;
    }

    /**
     * @param phoneNr the phoneNr to set
     */
    public void setPhoneNr(String phoneNr) {
        this.phoneNr = phoneNr;
    }
    
    
    public String toJsonString() {
        
        try {
            JSONObject jsonDataElement = new JSONObject();
            JSONObject jsonBooking = new JSONObject();
            if (getId()<0)
                jsonBooking.put("id", -1);
            if ( isValid() )
                    return "";
            jsonBooking.put("id", getId());
            jsonBooking.put("name", getName());
            jsonBooking.put("phone", getPhoneNr());
            jsonBooking.put("date", Long.toString(getDate().getTime()));
            
            jsonBooking.put("duration", getDuration());
            jsonBooking.put("persons", getPersons());
            jsonDataElement.put("data", jsonBooking);
            return jsonBooking.toString();
        } catch (JSONException ex) {
            Logger.getLogger(Booking.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }
    public boolean isValid() {
        return getName().isEmpty() || getPhoneNr().isEmpty() || (getDuration() == 0) || (getPersons() == 0);
    }
}

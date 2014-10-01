/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package miun.dt142g.data;


/**
 *
 * @author Marcus
 */
public class Booking {
    private final int id;
    
    // Variables for adding a table
    private int hour, min, persons = 1;
    private String name;
    
    // Construct a new booking
    public Booking(int id, String name, int hour, int min, int persons){
        this.id = id;
        this.name = name;
        this.hour = hour;
        this.min = min;
        this.persons = persons;
    }
    
    public int getId() {
        return id;
    }

    public int getPersons() {
        return persons;
    }
    
    public String getName() {
        return name;
    }

    public int getHour() {
        return hour;
    }

    public int getMin() {
        return min;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public void setPersons(int persons) {
        this.persons = persons;
    }

    public void setName(String name) {
        this.name = name;
    }
}

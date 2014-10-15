/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.miun.dt142g.data.EntityRep;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author Ulf
 */
public class Reservation implements Comparable<Reservation>{
    private Date date;
    private String name; 
    private int id; 
    private int duration; 
    private int persons; 

    public Reservation(int id, String name, Date date, int duration, int persons) {
        setId(id);
        setName(name);
        setDate(date);
        setDuration(duration);
        setPersons(persons);
    }

    /**
     * @return the date
     */
    public Date getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(Date date) {
        this.date = date; 
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

    public int compareTo(Reservation another) {
        if (this.getDate().getTime()<another.getDate().getTime())
            return -1; 
        if (this.getDate().getTime()>another.getDate().getTime())
            return 1; 
        return 0;
    }
    
    @Override
    public String toString(){
        DateFormat theTime = new SimpleDateFormat("HH.mm");
        return theTime.format(this.getDate()) + "\n"+this.getName()+"\nAntal: "+Integer.toString(this.getPersons())+"\n\n";
    }

    /**
     * @return the time
     */
    public int getHour() {
        return (int)date.getTime()%(3600*1000);
    }
    
    public int getMinute(){
        return (int)date.getTime()%(60*1000);
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
}

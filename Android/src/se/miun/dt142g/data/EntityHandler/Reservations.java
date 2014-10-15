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
import se.miun.dt142g.DataSource;


/**
 *
 * @author Ulf
 */
public class Reservations extends DataSource implements Iterable<Reservation>{
    private List<Reservation> reservations; 
    
    public Reservations() {
    }
    public boolean readReservations() {
        Date date1 = new Date(); 
        Date date2 = new Date();
        Date date3 = new Date();
        Date date4 = new Date();
        Date date5 = new Date();

        date2.setTime(date2.getTime()+1*(24*3600*1000));
        date3.setTime(date3.getTime()+2*(24*3600*1000));
        date4.setTime(date4.getTime()+3*(24*3600*1000));
        date5.setTime(date5.getTime()+4*(24*3600*1000));
        reservations = new ArrayList<Reservation>();

        reservations.add(new Reservation(1, "Ulf", date1,  2, 1 ));
        reservations.add(new Reservation(2, "Johannes", date1, 2, 1 ));
        reservations.add(new Reservation(3, "Björn", date1 ,2 , 2 ));
        reservations.add(new Reservation(4, "Markus med k", date1, 2, 1 ));
        reservations.add(new Reservation(5, "Findus", date1, 2, 1 ));
        reservations.add(new Reservation(6, "Sara", date1, 2, 1 ));
        reservations.add(new Reservation(7, "Bengta", date1, 2, 1 ));
        reservations.add(new Reservation(8, "Oden", date1, 2, 1 ));
        
        reservations.add(new Reservation(9, "Ulf", date2, 2, 2 ));
        reservations.add(new Reservation(10, "Johannes", date2, 2, 2 ));
        reservations.add(new Reservation(11, "Björn", date2, 2, 2 ));
        reservations.add(new Reservation(12, "Markus med k", date2, 2, 2 ));
                
        reservations.add(new Reservation(13, "Ulf", date3, 2, 3 ));
        reservations.add(new Reservation(14, "Johannes", date3, 2, 3 ));
        reservations.add(new Reservation(15, "Björn", date3, 2, 3 ));
        reservations.add(new Reservation(16, "Markus med k", date3, 2, 3 ));
        
        reservations.add(new Reservation(9, "Ulf", date4, 2, 4 ));
        reservations.add(new Reservation(10, "Johannes", date4, 2, 4 ));
        reservations.add(new Reservation(11, "Björn", date4, 2, 4 ));
        reservations.add(new Reservation(12, "Markus med k", date4, 2, 4 ));

        reservations.add(new Reservation(13, "Ulf", date5, 2, 5 ));
        reservations.add(new Reservation(14, "Johannes",date5, 2, 5 ));
        reservations.add(new Reservation(15, "Björn", date5, 2, 5 ));
        reservations.add(new Reservation(16, "Markus med k", date5, 2, 5 ));
        
        System.out.println("Not yet implemented!");
        return true;
    }
     
    
    public List<Reservation> getReservations(int day){
        List<Reservation> temp = new ArrayList<Reservation>(); 
        return temp;/*
        for(Reservation r : reservations){
            if(r.getDate().getDate()==day)
                temp.add(r);
        }
        sort(temp);
        return temp; */
    }

    public Iterator<Reservation> iterator() {
        return reservations.iterator();
    }

    @Override
    public void loadData(String responseText) throws WrongKeyException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void update() throws WrongKeyException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getUniqueId() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}

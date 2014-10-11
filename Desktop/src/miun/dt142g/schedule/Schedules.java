/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package miun.dt142g.schedule;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import miun.dt142g.DataSource;
import miun.dt142g.data.Schedule;

/**
 *
 * @author hagh1200
 */
public class Schedules extends DataSource implements Iterable<Schedule>{
    private List<Schedule> schedules = new ArrayList<>();

    @Override
    public void loadData() {
        schedules.add(new Schedule("Hajra", "2014-10-13", "20:00","20:30"));
       schedules.add(new Schedule("Hajra", "2014-10-11", "20:00","20:30"));
       schedules.add(new Schedule("Hajra", "2014-10-01", "20:00","20:30"));
       schedules.add(new Schedule("Hajra", "2014-10-03", "20:00","20:30"));
        
    }
    
    public void addSchedule(Schedule s){
        schedules.add(s);
}

    @Override
    public void update() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getUniqueId() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Iterator<Schedule> iterator() {
        return schedules.iterator();
    }
    
}

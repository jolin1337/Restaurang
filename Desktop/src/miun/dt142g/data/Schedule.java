/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package miun.dt142g.data;

/**
 *
 * @author hagh1200
 */
public class Schedule {

    private String date; 
    private String userName;
    private String startTime;
    private String endTime;
    
    
    
    public Schedule(String u , String d , String st, String et){
        date = d;
        userName = u;
        startTime = st;
        endTime = et;
    }
    public void setDate(String d){
        date = d;
    }
    public String getDate(){
        return date;
    }
    
    public void setUsername(String u){
        userName = u;
    }
    
    public String getUsername(){
        return userName;
    }
    public void setStartTime(String st){
        startTime = st;
    }
    public String getStartTime(){
        return startTime;
    }
    public void setEndTime(String et){
        endTime = et; 
    }
    
    public String getEndTime (){
        return endTime;
    }
  
            
}
    
   
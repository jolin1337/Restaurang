/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.miun.dt142g.data;

/**
 * A dataclass for the timetable which is deprecated
 *
 * @author hagh1200
 */
@Deprecated
public class Schedule {

    /**
     * Date of the work shift
     */
    private String date;
    /**
     * Name of the persons work shift
     */
    private String userName;
    /**
     * Start time of the work shift
     */
    private String startTime;
    /**
     * End time of the work shift
     */
    private String endTime;

    /**
     * Constructor for this class
     *
     * @param u sets the username
     * @param d Sets the date
     * @param st Sets the start time
     * @param et Sets the end time
     */
    public Schedule(String u, String d, String st, String et) {
        date = d;
        userName = u;
        startTime = st;
        endTime = et;
    }

    /**
     * Setter for the date
     *
     * @param d The date to set
     */
    public void setDate(String d) {
        date = d;
    }

    /**
     * Getter for the date
     *
     * @return date of the instance
     */
    public String getDate() {
        return date;
    }

    /**
     * Setter for the username
     *
     * @param u username to set
     */
    public void setUsername(String u) {
        userName = u;
    }

    /**
     * Getter for the username
     *
     * @return username of the instance is returned
     */
    public String getUsername() {
        return userName;
    }

    /**
     * Setter for the start time
     *
     * @param st time to set
     */
    public void setStartTime(String st) {
        startTime = st;
    }

    /**
     * Getter for the start time
     *
     * @return the start time
     */
    public String getStartTime() {
        return startTime;
    }

    /**
     * Setter for the end time
     *
     * @param et time to set
     */
    public void setEndTime(String et) {
        endTime = et;
    }

    /**
     * Getter for the end time
     *
     * @return the end time
     */
    public String getEndTime() {
        return endTime;
    }

}

/*
 * This code is created for one purpose only. And should not be used for any 
 * other purposes unless the author of this file has apporved. 
 * 
 * This code is a piece of a project in the course DT142G on Mid. Sweden university
 * Created by students for this projekt only
 */
package se.miun.dt142g;

/**
 *
 * @author Johannes
 */
public interface DataEntityListener {
    public void onUpdateTable();
    public void onReadTable();
    public void onFaildRequest(String req);
}

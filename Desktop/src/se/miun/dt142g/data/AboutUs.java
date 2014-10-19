/*
 * This code is created for one purpose only. And should not be used for any 
 * other purposes unless the author of this file has apporved. 
 * 
 * This code is a piece of a project in the course DT142G on Mid. Sweden university
 * Created by students for this projekt only
 */
package se.miun.dt142g.data;

import java.util.logging.Level;
import java.util.logging.Logger;
import se.miun.dt142g.DataSource;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class describes a datasource object that uses special rows in the table
 * Info in the database. This class contains opening hours and contact information
 *
 * @author Johannes Lindén
 * @since 2014-10-11
 * @version 1.3
 */
public class AboutUs extends DataSource {

    /**
     * The opening hours id used by the server
     */
    private final String whatOpen = "openings";
    /**
     * The contact id used by the server
     */
    private final String whatContacts = "contacts";
    /**
     * The data caring information about the openings hours
     */
    private String dataOpen = "";
    /**
     * The data caring the information about the contact information
     */
    private String dataContacts = "";

    public AboutUs() {
    }

    /**
     * Getter for the data opning hours
     * @return The data containging information about the opening hours
     */
    public String getDataOpen() {
        return dataOpen;
    }

    /**
     * Setter for the data opening hours
     * @param dataOpen - The data that this object will have
     */
    public void setDataOpen(String dataOpen) {
        this.dataOpen = dataOpen;
    }

    /**
     * Getter for the data contacts
     * @return The data containging information about the contacts
     */
    public String getDataContacts() {
        return dataContacts;
    }

    /**
     * Setter for the data contacts
     * @param dataContacts - The data that this object will have
     */
    public void setDataContacts(String dataContacts) {
        this.dataContacts = dataContacts;
    }

    @Override
    public void loadData() {
        try {
            JSONObject data = new JSONObject(getRequest("gettable", "key=" + key + "&table=info"));
            JSONArray dataArr = data.getJSONArray("data");
            dataOpen = "Öppetider är...";
            dataContacts = "Du når oss på...";
            for(int i = dataArr.length();i > 0;i--) {
                JSONObject obj = dataArr.getJSONObject(i-1);
                if(obj.getString("what").equals("openings"))
                    dataOpen = obj.getString("data");
                else if(obj.getString("what").equals("contacts"))
                    dataContacts = obj.getString("data");
            }
        } catch (JSONException ex) {
            Logger.getLogger(AboutUs.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void update() {
        // {"data":[{"what":"contacts","data":"Some%20example%20=)%202"}]}
        String str = "key=" + key + "&table=info&data={\"data\":["
                + "{\"what\":\"" + whatOpen + "\",\"data\":\"" + getDataOpen() + "\"},"
                + "{\"what\":\"" + whatContacts + "\",\"data\":\"" + getDataContacts() + "\"}]}";
        str = str.replace("\n", "\\n");
        System.out.println("updatestatus: " + getRequest("updaterow", str));
    }

    /**
     * THis is not used
     * @return - A unique id for a new object of this type
     * @deprecated - Never use
     */
    @Deprecated
    @Override
    public int getUniqueId() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}

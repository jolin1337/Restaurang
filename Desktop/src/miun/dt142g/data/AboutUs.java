/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package miun.dt142g.data;

import java.util.logging.Level;
import java.util.logging.Logger;
import miun.dt142g.DataSource;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Johannes
 */
public class AboutUs extends DataSource {

    private final String whatOpen = "openings";
    private final String whatContacts = "contacts";
    private String dataOpen = "";
    private String dataContacts = "";

    public AboutUs() {
    }

    public String getDataOpen() {
        return dataOpen;
    }

    public void setDataOpen(String dataOpen) {
        this.dataOpen = dataOpen;
    }

    public String getDataContacts() {
        return dataContacts;
    }

    public void setDataContacts(String dataContacts) {
        this.dataContacts = dataContacts;
    }

    @Override
    public void loadData() {
        /**try {
            JSONObject data = new JSONObject(getRequest("gettable", "key=" + key + "&table=info"));
            JSONArray dataArr = data.getJSONArray("data");
            for(int i = dataArr.length();i > 0;i--) {
                JSONObject obj = dataArr.getJSONObject(i);
                //if(obj.getString("what"))
            }
            dataOpen = "Öppetider är...";
            dataContacts = "Du når oss på...";
        } catch (JSONException ex) {
            Logger.getLogger(AboutUs.class.getName()).log(Level.SEVERE, null, ex);
        }*/
        dataContacts = "djhfjka";
        dataOpen = "jkldaf";
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

    @Override
    public int getUniqueId() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}

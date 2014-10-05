/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package miun.dt142g;

import java.io.InputStream;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Johannes
 */
public abstract class DataSource {

    // TODO: Implement loadData funktion in each inherited class
    public abstract void loadData();
    protected String key = "";
    private final String serverUrl = "http://localhost:8080/Server/";

    // TODO: create db connection
    public void dbConnect() {
        key = getRequest("login?key=enrolignyckel");
        System.out.println(key);
    }

    private String getRequest(String param) {
        try {
            URL url = new URL(serverUrl + param);
            InputStream keyStream = url.openStream();
            
            String responseText = "";
            int byteRead;
            while ((byteRead = keyStream.read()) > -1) {
                responseText += (char)byteRead;
            }
            return responseText;
        } catch (Exception e) {
            Logger.getLogger(DataSource.class.getName()).log(Level.SEVERE, null, e);
        }
        return "";
    }
    
    protected JSONObject getJsonRequest(String table) throws JSONException{
        return new JSONObject(getRequest("gettable?table="+table+"&key="+key));
    }

    protected void upploadData(String... data) {
        // TODO: Update data
    }

    public abstract void update();

    public abstract int getUniqueId();
}

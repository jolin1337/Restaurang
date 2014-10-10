/*
 * This code is created for one purpose only. And should not be used for any 
 * other purposes unless the author of this file has apporved. 
 * 
 * This code is a piece of a project in the course DT142G on Mid. Sweden university
 * Created by students for this projekt only
 */
package miun.dt142g;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * This class connects us to the server and is a base class of data objects
 *
 * @author Johannes Lindén
 * @since 2014-10-09
 * @version 1.2
 */
public abstract class DataSource {
    static String safeKey = "dt142g-awesome";
    /**
     * An abstract method for loading data to its datasource
     */
    public abstract void loadData();
    /**
     * A key for authorization
     */
    protected static String key = "";
    /**
     * The url to the java ee server
     */
    private static final String serverUrl = "http://localhost:8080/Server/";

    /**
     * This method connects us with key to the server
     * @throws miun.dt142g.DataSource.WrongKeyException 
     */
    public void dbConnect() throws WrongKeyException {
        if(!key.equals("")) {
            if(!getRequest("test", "key=" + key).equals("true"))
                throw new WrongKeyException("Still not connected");
            return;
        }
        key = getRequest("login", "key=" + safeKey);
        String req = getRequest("test", "key=" + key);
        //System.out.println(req);
        if (!req.equals("true")) {
            key = "";
            throw new WrongKeyException("Not correct key");
        }
    }
    
    /**
     * Send a post request to server with a suburl of url and some optional
     * specifik params
     * @param url    - The sub url to request from the base url 
     * DataSource.serverUrl
     * @param params - The specifik params to send with the url to the server
     * @return A string of the requested information (the result of the request)
     */
    protected static String getRequest(String url, String params) {
        try {
            URL obj = new URL(serverUrl + url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            //add reuqest header
            con.setRequestMethod("POST");
            con.setRequestProperty("User-Agent", "User-Agent");
            con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

            // Send post request
            con.setDoOutput(true);
            
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(params);
            wr.flush();

            int responseCode = con.getResponseCode();
            if(!url.equals("test")) {
                System.out.println("\nSending 'POST' request to URL : " + url);
                //System.out.println("Post parameters : " + params);
                System.out.println("Response Code : " + responseCode);
            }
            if(responseCode != 200) return "";
            
            StringBuilder response;
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            //print result
            return response.toString();

        } catch (Exception ex) {
            Logger.getLogger(DataSource.class.getName()).log(Level.SEVERE, null, ex);
        }
        /*try {
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
         }*/
        return "";
    }

    protected JSONObject getJsonRequest(String table) throws JSONException {
        return new JSONObject(getRequest("gettable", "table=" + table + "&key=" + key));
    }

    protected void upploadData(String... data) {
        // TODO: Update data
    }

    public abstract void update();

    public abstract int getUniqueId();

    public static class WrongKeyException extends Exception {

        public WrongKeyException(String not_correct_key) {
            super(not_correct_key);
        }
    }
    
    static void setSafeKey(String k) {
        safeKey = k;
    }
    static String getSafeKey() {
        return safeKey;
    }
}

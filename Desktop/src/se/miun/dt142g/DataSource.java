/*
 * This code is created for one purpose only. And should not be used for any 
 * other purposes unless the author of this file has apporved. 
 * 
 * This code is a piece of a project in the course DT142G on Mid. Sweden university
 * Created by students for this projekt only
 */
package se.miun.dt142g;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
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
    private static String safeKey = "dt142g-awesome";
    /**
     * An abstract method for loading data to its datasource
     * @throws se.miun.dt142g.DataSource.WrongKeyException
     */
    public abstract void loadData() throws WrongKeyException;
    /**
     * A key for authorization
     */
    protected static String key = "";

    /**
     * This method connects us with key to the server
     * @throws se.miun.dt142g.DataSource.WrongKeyException If the connection was 
     * refused
     */
    public void dbConnect() throws WrongKeyException {
        if(!key.equals("")) {
            if(!getRequest("test", "key=" + key).equals("true")) {
                String tmpKey = getRequest("login", "key=" + safeKey);
                if(!tmpKey.isEmpty() && getRequest("test", "key=" + tmpKey).equals("true")) {
                    key = tmpKey;
                    return;
                }
                throw new WrongKeyException("Still not connected");
            }
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
            System.out.println(params);
            int bracketIndex = params.indexOf("{");
            if(bracketIndex != -1){
                String temp = params.substring(bracketIndex);
                temp = temp.replaceAll("&", "och");
                params = params.substring(0, bracketIndex)+temp;
            }
            System.out.println(params);
            URL obj = new URL(Settings.Strings.serverURL + url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            //add reuqest header
            con.setRequestMethod("POST");
            con.setRequestProperty("User-Agent", "User-Agent");
            con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

            // Send post request
            con.setDoOutput(true);
            
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            String str = URLEncoder.encode(params, "UTF-8").replace("%3D", "=").replace("%26", "&");
            wr.writeBytes(str);
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
            return URLDecoder.decode(response.toString(), "UTF-8");

        } catch (Exception ex) {
            Logger.getLogger(DataSource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    /**
     * <warning>JSONException has to be cauth when this function is called</warning>
     * Sens an getRequest and directly converts it to an JSONObject
     * @param table
     * @return
     * @throws JSONException 
     */
    protected JSONObject getJsonRequest(String table) throws JSONException {
        return new JSONObject(getRequest("gettable", "table=" + table + "&key=" + key));
    }


    /**
     * This function is requered for all that extends this class
     * It will update/sync to the server the current information of the object
     * @throws se.miun.dt142g.DataSource.WrongKeyException if no connection available
     */
    public abstract void update() throws WrongKeyException;

    /**
     * This function does not do anything more than returns -1. Case of time
     * limitation we do not remove it
     * @return returns a new unique id or -1 in some cases witch makes this 
     * function unstable
     */
    @Deprecated
    public abstract int getUniqueId();

    /**
     * This exception class describes if there was no key match on the server
     * or we can't connect at all. Then we need to change the key and try again
     * until it works
     */
    public static class WrongKeyException extends Exception {
        public WrongKeyException(String not_correct_key) {
            super(not_correct_key);
        }
    }
    
    /**
     * Alter the safekey to login with
     * @param k - The key that is very secret. This key are the one that enables
     * you to login
     */
    static void setSafeKey(String k) {
        safeKey = k;
    }
    /**
     * Getter for safekey
     * @return The safekey string
     */
    static String getSafeKey() {
        return safeKey;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package miun.dt142g;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import static javax.ws.rs.core.HttpHeaders.USER_AGENT;
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
    private static final String serverUrl = "http://localhost:8080/Server/";

    // TODO: create db connection
    public void dbConnect() throws WrongKeyException {
        key = getRequest("login", "key=dt142g-awesome");
        String req = getRequest("test", "key=" + key);
        System.out.println(req);
        if (!req.equals("true")) {
            throw new WrongKeyException("Not correct key");
        }
    }

    private static String getRequest(String url, String params) {
        try {
            URL obj = new URL(serverUrl + url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            //add reuqest header
            con.setRequestMethod("POST");
            con.setRequestProperty("User-Agent", USER_AGENT);
            con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

            // Send post request
            con.setDoOutput(true);
            try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
                wr.writeBytes(params);
                wr.flush();
            }

            int responseCode = con.getResponseCode();
            System.out.println("\nSending 'POST' request to URL : " + url);
            System.out.println("Post parameters : " + params);
            System.out.println("Response Code : " + responseCode);

            StringBuilder response;
            try (BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()))) {
                String inputLine;
                response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
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
}

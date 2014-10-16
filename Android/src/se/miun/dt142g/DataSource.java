/*
 * This code is created for one purpose only. And should not be used for any 
 * other purposes unless the author of this file has apporved. 
 * 
 * This code is a piece of a project in the course DT142G on Mid. Sweden university
 * Created by students for this projekt only
 */
package se.miun.dt142g;

import android.os.AsyncTask;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;
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
    /**
     * Update event listener
     */
    DataEntityListener tableListener = null;
    
    private static String safeKey = "dt142g-awesome";

    /**
     * An abstract method for loading data to its datasource
     */
    public abstract void loadData(String url, String responseText) throws WrongKeyException;
    /**
     * An abstract method for loading data to its datasource
     */
    public abstract void load() ;
    /**
     * A key for authorization
     */
    protected static String key = "";
    /**
     * The url to the java ee server
     */
    protected static final String serverUrl = "http://10.0.2.2:8080/Server/";

    /**
     * Send a post request to server with a suburl of url and some optional
     * specifik params
     *
     * @param url - The sub url to request from the base url
     * DataSource.serverUrl
     * @param params - The specifik params to send with the url to the server
     * @return A string of the requested information (the result of the request)
     */
    protected String sendRequestFromThread(String url, String params) {
        String response = "";

        //try {
            ServerConnect connection = new ServerConnect();
            connection.execute(serverUrl + url, params);
            //response = connection.get();
            return response;
        /*} catch (InterruptedException ex) {
            Logger.getLogger(DataSource.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ExecutionException ex) {
            Logger.getLogger(DataSource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return response;*/
    }

    public abstract void update() throws WrongKeyException;

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

    public DataEntityListener getTableListener() {
        return tableListener;
    }

    public void setTableListener(DataEntityListener tableListener) {
        this.tableListener = tableListener;
    }
    
    /**
     *
     * @author Johannes
     */
    protected class ServerConnect extends AsyncTask<String, Void, Integer> {
        boolean run = true;
        public ServerConnect(){}
        @Override
        protected Integer doInBackground(String... urls) {
            //while(run) {
                if(!(urls.length > 2 && urls[2].equals( "keep_it" )))
                    stopAfterRunningCycle();
                try {
                    if (urls.length > 1) {
                        dbConnect();
                        String res = processRequest(urls[0], "key=" + key + urls[1]);
                        if(!res.equals("false") && !res.equals("expired_key"))
                            loadData(urls[0], res);
                        System.out.println("Updatestatus: " + res);
                    }

                } catch (Exception e) {
                }
                if(tableListener != null && urls.length > 1)
                    tableListener.onFaildRequest(urls[0]);
            //}
            return 0;
        }
        
        void stopAfterRunningCycle() {
            run = false;
        }

        /**
         * This method connects us with key to the server
         *
         * @throws miun.dt142g.DataSource.WrongKeyException
         */
        protected void dbConnect() throws WrongKeyException{
            if (!key.isEmpty()) {
                if (!processRequest("test", "key=" + key).equals("true")) {
                    throw new WrongKeyException("Still not connected");
                }
                return;
            }
            key = processRequest("login", "key=" + safeKey);
            String req = processRequest("test", "key=" + key);
            //System.out.println(req);
            if (!req.equals("true")) {
                key = "";
                throw new WrongKeyException("Not correct key");
            }
        }
        protected void onProgressUpdate(Integer... progress) {
            //setProgressPercent(progress[0]);
        }

        protected void onPostExecute(Long result) {
            //showDialog("Downloaded " + result + " bytes");
        }
        
        /**
         * Send a post request to server with a suburl of url and some optional
         * specifik params
         *
         * @param url - The sub url to request from the base url
         * DataSource.serverUrl
         * @param params - The specifik params to send with the url to the server
         * @return A string of the requested information (the result of the request)
         */
        protected String processRequest(String url, String params) {
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
                if (!url.equals("test")) {
                    System.out.println("\nSending 'POST' request to URL : " + url);
                    //System.out.println("Post parameters : " + params);
                    System.out.println("Response Code : " + responseCode);
                }
                if (responseCode != 200) {
                    return "";
                }

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
            return "";
        }
    }
}

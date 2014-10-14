/*
 * This code is created for one purpose only. And should not be used for any 
 * other purposes unless the author of this file has apporved. 
 * 
 * This code is a piece of a project in the course DT142G on Mid. Sweden university
 * Created by students for this projekt only
 */
package miun.dt142g.website;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import miun.dt142g.DataSource;
import miun.dt142g.data.EventPost;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.apache.pdfbox.io.IOUtils;

/**
 *
 * @author Johannes
 */
public class EventPosts extends DataSource implements Iterable<EventPost> {

    private final List<EventPost> events = new ArrayList<>();
    
    private final int BUFFER_SIZE = 255;

    @Override
    public void loadData() throws WrongKeyException{
        events.clear();
        List<EventPost> evs = getDataList();
        for(EventPost ev : evs) {
            events.add(ev);
        }
        Collections.sort(events);
    }

    @Override
    public void update() throws WrongKeyException {
        List<EventPost> evs = getDataList();
        String str = "&table=event&data={\"data\":[";
        String strRm = "&table=event&data={\"data\":[";
        for(EventPost ev : evs)
            strRm += "{\"data\":{\"remove\":true,\"id\":" + ev.getId() + "}},";
        for (EventPost ev : events) {
            int id = ev.getId();
            ev.setId(-1);
            str += "{\"data\":" + ev.toJsonString() + "},";
            ev.setId(id);
            upploadImg(ev.getId(), ev.getImgSrc());
        }
        if(events.isEmpty())
            str += ",";
        if(evs.isEmpty())
            strRm += ",";
        // System.out.println(str.substring(0, str.length()-1) + "]}");
        // System.out.println(strRm.substring(0, strRm.length()-1) + "]}");
        System.out.println("Updatestatus: " + getRequest("updaterow", "key=" + key + strRm.substring(0, strRm.length()-1) + "]}"));
        System.out.println("Updatestatus: " + getRequest("updaterow", "key=" + key + str.substring(0, str.length()-1) + "]}"));
        
        // To make sure that we have the correct id:s/pk:s
        loadData();
    }

    private List<EventPost> getDataList() throws WrongKeyException {
        List<EventPost> currentEvents = new ArrayList<>();
        JSONObject json;
        String jsonStr = getRequest("gettable", "key=" + key + "&table=event");
        if(jsonStr.equals("expired_key")) {
            dbConnect();
            jsonStr = getRequest("gettable", "key=" + key + "&table=event");
        }
        try {
            json = new JSONObject(jsonStr);
        } catch (JSONException ex) {
            System.out.println("Could not verify the JSON parse of json object: " + jsonStr);
            return currentEvents;
        }

        JSONArray jsonArr;
        try {
            jsonArr = json.getJSONArray("data");
        } catch (JSONException ex) {
            return currentEvents;
        }
        for (int i = jsonArr.length(); i > 0; i--) {
            JSONObject obj;
            EventPost p = null;
            try {
                obj = jsonArr.getJSONObject(i-1);

                p = new EventPost(obj.getInt("id"));
                // Get the properties of the json object and update this event.
                p.setDescription(obj.getString("description")); // Set description

                // Set the title 
                p.setTitle(obj.getString("title"));
                
                p.setImgSrc(obj.getString("image"));              // Set the image url

                // Set the date
                p.setPubDate(obj.getString("pubDate"));
                currentEvents.add(p);
            } catch (JSONException ex) {
            } catch (Exception exr) {
                if (p != null) {
                    System.out.println("Unable to parse json object: " + p);
                } else {
                    System.out.println("Unable to parse json object: null");
                }
            }
        }
        return currentEvents;            // return that we have changed this entity
    }

    private boolean upploadImg(int id, String url) {
        try {
            File uploadFile = new File(url);
            FormMultiPartUtility multipart = new FormMultiPartUtility(serverUrl + "upload", "utf-8");
            multipart.addFilePart("fileUpload", uploadFile);
 
            multipart.finish();
        } catch (IOException ex) {
            Logger.getLogger(EventPosts.class.getName()).log(Level.SEVERE, null, ex);
        }
        /*
        try {
            File uploadFile = new File(url);
            
            System.out.println("File to upload: " + url);
            
            // creates a HTTP connection
            URL httpUrl = new URL(serverUrl + "upload");
            HttpURLConnection httpConn = (HttpURLConnection) httpUrl.openConnection();
            httpConn.setUseCaches(false);
            httpConn.setDoOutput(true);
            httpConn.setRequestMethod("POST");
            // sets file name as a HTTP header
            httpConn.setRequestProperty("fileName", uploadFile.getName());
            httpConn.setRequestProperty("eventId", Integer.toString(id));
            
            FileInputStream inputStream = null;
            // Opens input stream of the file for reading data
            try ( // opens output stream of the HTTP connection for writing data
                    OutputStream outputStream = httpConn.getOutputStream()) {
                // Opens input stream of the file for reading data
                inputStream = new FileInputStream(uploadFile);
                
                byte[] buffer = new byte[BUFFER_SIZE];
                int bytesRead;
                System.out.println("Start writing data...");
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                System.out.println("Data was written.");
            }
            catch(FileNotFoundException ex) {}
            finally {
                if(inputStream != null)
                    inputStream.close();
                else return false;
            }
            
            // always check HTTP response code from server
            int responseCode = httpConn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // reads server's response
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        httpConn.getInputStream()));
                String response = reader.readLine();
                System.out.println("Server's response: " + response);
                return true;
            } else {
                System.out.println("Server returned non-OK code: " + responseCode);
            }
        } catch (MalformedURLException ex) {
            Logger.getLogger(EventPosts.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(EventPosts.class.getName()).log(Level.SEVERE, null, ex);
        }*/
        return false;
    }
    public EventPost getEvent(int i) {
        return events.get(i);
    }

    public void addEvent(int id, String pubDate, String imgSrc, String title, String desc) {
        events.add(new EventPost(id, pubDate, imgSrc, title, desc));
    }
    void addEvent(EventPost ep) {
        events.add(ep);
    }

    public void editEvent(int id, String pubDate, String imgSrc, String title, String desc) {
        for (EventPost ep : events) {
            if (ep.getId() == id) {
                ep.setPubDate(pubDate);
                ep.setImgSrc(imgSrc);
                ep.setTitle(title);
                ep.setDescription(desc);
                return;
            }
        }
    }

    public int getRows() {
        return events.size();
    }

    @Override
    public Iterator<EventPost> iterator() {
        return events.iterator();
    }

    @Override
    public int getUniqueId() {
        int id = 0;
        for(EventPost e : events) {
            if(e.getId() > id)
                id = e.getId() + 1;
        }
        return id;
    }

}

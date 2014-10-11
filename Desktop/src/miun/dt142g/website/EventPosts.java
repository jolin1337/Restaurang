/*
 * This code is created for one purpose only. And should not be used for any 
 * other purposes unless the author of this file has apporved. 
 * 
 * This code is a piece of a project in the course DT142G on Mid. Sweden university
 * Created by students for this projekt only
 */
package miun.dt142g.website;

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

/**
 *
 * @author Johannes
 */
public class EventPosts extends DataSource implements Iterable<EventPost> {

    private final List<EventPost> events = new ArrayList<>();

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
        }
        if(events.isEmpty())
            str += ",";
        if(evs.isEmpty())
            strRm += ",";
        System.out.println(str.substring(0, str.length()-1) + "]}");
        System.out.println(strRm.substring(0, strRm.length()-1) + "]}");
        System.out.println("Updatestatus: " + getRequest("updaterow", "key=" + key + strRm.substring(0, strRm.length()-1) + "]}"));
        System.out.println("Updatestatus: " + getRequest("updaterow", "key=" + key + str.substring(0, str.length()-1) + "]}"));
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

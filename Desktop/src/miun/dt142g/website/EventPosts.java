/*
 * This code is created for one purpose only. And should not be used for any 
 * other purposes unless the author of this file has apporved. 
 * 
 * This code is a piece of a project in the course DT142G on Mid. Sweden university
 * Created by students for this projekt only
 */
package miun.dt142g.website;

import java.util.ArrayList;
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
    public void loadData() {
        events.add(new EventPost(0, "20141001", "path/to/image/poster.png", "Fest med Anders", "Kul va?"));
        events.add(new EventPost(1, "Fest med Anders 2", "Kul va?"));
    }

    @Override
    public void update() {
        List<EventPost> evs = getDataList();
        String str = "&table=event&data={\"data\":[";
        for (EventPost ev : evs) {
            if (!events.contains(ev)) {
                str += "{\"remove\":true,\"data\":{\"id\":" + ev.getId() + "}},";
            }
            str += "{\"data\":" + ev.toJsonString() + "},";
        }
        getRequest("updaterow", "key=" + key + str.substring(0, str.length()-1) + "]}");
    }

    private List<EventPost> getDataList() {
        List<EventPost> currentEvents = new ArrayList<>();
        JSONObject json;
        String jsonStr = getRequest("gettable", "?key=" + key + "&table=event");
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
                obj = jsonArr.getJSONObject(i);

                p = new EventPost(obj.getInt("id"));
                // Get the properties of the json object and update this event.
                p.setDescription(obj.getString("description")); // Set description
                p.setImgSrc(obj.getString("img"));              // Set the image url

                // Set the date
                p.setPubDate(obj.getString("pubdate"));

                // Set the title 
                p.setTitle(obj.getString("title"));
            } catch (JSONException ex) {
                Logger.getLogger(EventPosts.class.getName()).log(Level.SEVERE, null, ex);
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}

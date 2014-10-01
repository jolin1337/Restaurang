/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package miun.dt142g.website;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import miun.dt142g.DataSource;
import miun.dt142g.data.EventPost;

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
        // TODO: Update the database
    }
    
    public EventPost getEvent(int i) {
        return events.get(i);
    }
    public void addEvent(int id, String pubDate, String imgSrc, String title, String desc) {
        events.add(new EventPost(id, pubDate, imgSrc, title, desc));
    }
    public void editEvent(int id, String pubDate, String imgSrc, String title, String desc) {
        for(EventPost ep : events) {
            if(ep.getId() == id) {
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

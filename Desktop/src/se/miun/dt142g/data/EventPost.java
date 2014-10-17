/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.miun.dt142g.data;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Johannes
 */
public class EventPost implements Comparable<EventPost> {
    private int id;

    private String pubDate="";
    private String imgSrc="";
    private String title="";
    private String description="";

    
    public EventPost(int id) {
        this.id = id;
        this.pubDate = new SimpleDateFormat("yyyyMMdd").format(new Date());
    }
    public EventPost(int id, String pubDate, String imgSrc, String title, String description) {
        this.id = id;
        this.pubDate = pubDate;
        this.imgSrc = imgSrc;
        this.title = title;
        this.description = description;
    }

    public EventPost(int id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.pubDate = new SimpleDateFormat("yyyyMMdd").format(new Date());
    }
    
    public int getId() {
        return id;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getImgSrc() {
        return imgSrc;
    }

    public void setImgSrc(String imgSrc) {
        this.imgSrc = imgSrc;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final EventPost other = (EventPost) obj;
        return other.getDescription().equals(getDescription()) && 
                other.getId() == getId() &&
                other.getImgSrc().equals(getImgSrc()) && 
                other.getPubDate().equals(getPubDate()) &&
                other.getTitle().equals(getTitle());
    }

    public String toJsonString() {
        
        // Set all properties of this event here to export the event to a json object
        JSONObject value = new JSONObject();
        try {
            String imgName = getImgSrc();
            if(imgName.lastIndexOf("/") > -1)
                imgName = imgName.substring(getImgSrc().lastIndexOf("/")+1);
            if(imgName.lastIndexOf("\\") > -1)
                imgName = imgName.substring(getImgSrc().lastIndexOf("\\")+1);
            value.put("id", getId())
                    .put("image", imgName)
                    .put("pubDate", getPubDate())
                    .put("title", getTitle())
                    .put("description", getDescription());
        } catch (JSONException ex) {
        }
        return value.toString();
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int compareTo(EventPost t) {
        try {
            Long l = Long.parseLong(t.getPubDate());
            return l.compareTo(Long.parseLong(getPubDate()));
        } catch(NullPointerException | NumberFormatException ex) {
            return 0;
        }
    }
}

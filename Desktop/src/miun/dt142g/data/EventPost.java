/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package miun.dt142g.data;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Johannes
 */
public class EventPost {
    private final int id;

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
}

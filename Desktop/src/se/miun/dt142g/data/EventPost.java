/*
 * This code is created for one purpose only. And should not be used for any 
 * other purposes unless the author of this file has apporved. 
 * 
 * This code is a piece of a project in the course DT142G on Mid. Sweden university
 * Created by students for this projekt only
 */
package se.miun.dt142g.data;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This is a dataclass that represents an event (happening).
 *
 * @author Johannes Lindén
 * @since 2014-10-04
 * @version 1.0
 */
public class EventPost implements Comparable<EventPost> {

    /**
     * The id (PK on db) To identify this data object
     */
    private int id;

    /**
     * The date when this event takes place in a string format
     */
    private String pubDate = "";
    /**
     * An img name for this post
     */
    private String imgSrc = "";
    /**
     * The describing title for this eventpost
     */
    private String title = "";
    /**
     * The description on what happened on the event
     */
    private String description = "";

    /**
     * Constructs an eventpost with a desired id
     *
     * @param id - The id this post will get
     */
    public EventPost(int id) {
        this.id = id;

        // Set the pudDate to current date for now
        this.pubDate = new SimpleDateFormat("yyyyMMdd").format(new Date());
    }

    /**
     * Constructs an eventpost with all attributes decided
     *
     * @param id - The identifier for this event
     * @param pubDate - The startdate for this event
     * @param imgSrc - The source to image poster
     * @param title - The title for this post
     * @param description - The description of this event
     */
    public EventPost(int id, String pubDate, String imgSrc, String title, String description) {
        this.id = id;
        this.pubDate = pubDate;
        this.imgSrc = imgSrc;
        this.title = title;
        this.description = description;
    }

    /**
     * Constructs an simplified eventpost attribute collection
     *
     * @param id - The identifier for this event
     * @param title - The title for this post
     * @param description - The description of this event
     */
    public EventPost(int id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.pubDate = new SimpleDateFormat("yyyyMMdd").format(new Date());
    }

    /**
     * gets the id from this event
     *
     * @return the unique identifier of this post event
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the identifier of this object
     *
     * @param id - The new id this object retrieves
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the date when this event takes place
     *
     * @return the date this event takes place
     */
    public String getPubDate() {
        return pubDate;
    }

    /**
     * Setter for start date of this event
     *
     * @param pubDate
     */
    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    /**
     * Getter for the poster image name
     *
     * @return the image name or sometimes url
     */
    public String getImgSrc() {
        return imgSrc;
    }

    /**
     * Setter for image name for this poster
     *
     * @param imgSrc the image name or sometimes url
     */
    public void setImgSrc(String imgSrc) {
        this.imgSrc = imgSrc;
    }

    /**
     * Getter for title of this event that describes what is happening
     *
     * @return The title of the event
     */
    public String getTitle() {
        return title;
    }

    /**
     * Setter for title in this eventpost
     *
     * @param title - The title to set this eventpost to have
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Getter for description
     *
     * @return the description text
     */
    public String getDescription() {
        return description;
    }

    /**
     * Setter for description
     *
     * @param description - The description this eventpost will have
     */
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
        return other.getDescription().equals(getDescription())
                && other.getId() == getId()
                && other.getImgSrc().equals(getImgSrc())
                && other.getPubDate().equals(getPubDate())
                && other.getTitle().equals(getTitle());
    }

    /**
     * Converts this EventPost object to an json string
     *
     * @return An json string representation of this object if it is valid
     * otherwise empty "{}"
     */
    public String toJsonString() {

        // Set all properties of this event here to export the event to a json object
        JSONObject value = new JSONObject();
        try {
            // Set ImgSrc
            String imgName = getImgSrc();
            // If it contains slashes remove them here and take the right side 
            // of the last occurrence of an slahs
            if (imgName.lastIndexOf("/") > -1) {
                imgName = imgName.substring(getImgSrc().lastIndexOf("/") + 1);
            }
            // If it contains back slashes remove them here and take the right 
            // side of the last occurrence of an back slahs
            if (imgName.lastIndexOf("\\") > -1) {
                imgName = imgName.substring(getImgSrc().lastIndexOf("\\") + 1);
            }
            // Insert all attributes in an json object
            value.put("id", getId())
                    .put("image", imgName)
                    .put("pubDate", getPubDate())
                    .put("title", getTitle())
                    .put("description", getDescription());
        } catch (JSONException ex) { // Nothing to do when something has gone wrong
        }
        return value.toString(); // return this object representation if it is valid
        // otherwise empty "{}"
    }

    @Override
    public int compareTo(EventPost t) {
        try {
            Long l = Long.parseLong(t.getPubDate());
            return l.compareTo(Long.parseLong(getPubDate()));
        } catch (NullPointerException | NumberFormatException ex) {
            return 0;
        }
    }
}

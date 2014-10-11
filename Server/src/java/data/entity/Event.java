/*
 * This code is created for one purpose only. And should not be used for any 
 * other purposes unless the author of this file has apporved. 
 * 
 * This code is a piece of a project in the course DT142G on Mid. Sweden university
 * Created by students for this projekt only
 */
package data.entity;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * This entity describes an event on the restaurant
 *
 * @author Johannes Lindén
 * @since 2014-10-07
 * @version 1.0
 */
@Entity
@Table(name = "EVENT", catalog = "", schema = "APP")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Event.findAll", query = "SELECT e FROM Event e"),
    @NamedQuery(name = "Event.findById", query = "SELECT e FROM Event e WHERE e.id = :id"),
    @NamedQuery(name = "Event.findByImgsrc", query = "SELECT e FROM Event e WHERE e.imgsrc = :imgsrc"),
    @NamedQuery(name = "Event.findByPubdate", query = "SELECT e FROM Event e WHERE e.pubdate = :pubdate"),
    @NamedQuery(name = "Event.findByTitle", query = "SELECT e FROM Event e WHERE e.title = :title")})
public class Event extends JsonEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    @NotNull
    @Column(name = "ID")
    /**
     * The id (pk) of this entity
     */
    private Integer id;
    @Size(max = 255)
    @Column(name = "IMGSRC")
    /**
     * The image url for this event
     */
    private String imgsrc;
    @Column(name = "PUBDATE")
    @Temporal(TemporalType.DATE)
    /**
     * The date when this event take place
     */
    private Date pubdate;
    @Size(max = 255)
    @Column(name = "TITLE")
    /**
     * The big title of this event
     */
    private String title;
    @Lob
    @Column(name = "DESCRIPTION")
    /**
     * Detail description on how this event went afterwards
     */
    private String description;

    public Event() {
    }

    /**
     * Initialize this event with an id
     *
     * @param id - the primary key identifier for this entity
     */
    public Event(Integer id) {
        this.id = id;
    }

    /**
     * Getter for the primary key
     *
     * @return the id of this entity
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets the id for this entity
     *
     * @param id - The id that this entity will get if possible
     */
    @Deprecated
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Getter for the image url
     *
     * @return the url of the event poster
     */
    public String getImgsrc() {
        if(imgsrc != null)
            return imgsrc;
        return "";
    }

    /**
     * Setter of the image url
     *
     * @param imgsrc - The url of the event poster upploaded to the server
     */
    public void setImgsrc(String imgsrc) {
        this.imgsrc = imgsrc;
    }

    /**
     * Getter for the startdate of this event
     *
     * @return the date object of the startdate
     */
    public Date getPubdate() {
        if(pubdate != null)
            return pubdate;
        return null;
    }

    /**
     * Setter for the startdate of this event
     *
     * @param pubdate - The date this event will have when it takes place
     */
    public void setPubdate(Date pubdate) {
        this.pubdate = pubdate;
    }

    /**
     * Getter for the big title of this event
     *
     * @return The title that is shown on the event
     */
    public String getTitle() {
        if(title != null)
            return title;
        return "";
    }

    /**
     * Setter for the big title of this event
     *
     * @param title - The title you want this event to have
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Getter for the description of this event
     *
     * @return The description this event has. Before the event has taken place
     * this will almost always be empty. Afterwards it describes the event
     */
    public String getDescription() {
        if(description != null)
            return description;
        return "";
    }

    /**
     * Setter for the description of this event
     *
     * @param description - The descirption you want this event to have...
     * Describe the event!
     */
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (!(object instanceof Event)) {
            return false;
        }
        Event other = (Event) object;
        return id.equals(other.id);
    }

    @Override
    public String toString() {
        return "data.entity.Event[ id=" + id + " ]";
    }

    @Override
    public String toJsonString() {
        // Set all properties of this event here to export the event to a json object
        JsonObjectBuilder valueBuilder = Json.createObjectBuilder()
                .add("id", getId())
                .add("image", getImgsrc());
        if(getPubdate() != null) {
            String date = new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(pubdate);
            valueBuilder.add("pubDate", date);
        }
        else
            valueBuilder.add("pubDate", "-1");
        return valueBuilder.add("title", getTitle())
                .add("description", getDescription())
                .build().toString();
    }

    @Override
    public boolean setEntityByJson(JsonObject obj, EntityManager em) {
        try {
            // Get the properties of the json object and update this event.
            setDescription(obj.getString("description", null)); // Set description
            setImgsrc(obj.getString("image",    null));              // Set the image url

            // Set the title 
            setTitle(obj.getString("title", null));

            // Get the date from json object
            Date date = new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).parse(obj.getString("pubDate", ""));
            // Set the date
            setPubdate(date);
        } catch (ParseException ex) {
            setPubdate(null);   // Something went wrong with the date property. Set it to null
        } catch (Exception exr) {
            return false;       // Something critical. return not edited
        }
        return true;            // return that we have changed this entity
    }
}

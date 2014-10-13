/*
 * Title:
 * Subject: 
 */
package data.entity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonValue;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Simple
 */
@Entity
@Table(name = "BOOKING", catalog = "", schema = "APP")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Booking.findAll", query = "SELECT b FROM Booking b"),
    @NamedQuery(name = "Booking.findById", query = "SELECT b FROM Booking b WHERE b.id = :id"),
    @NamedQuery(name = "Booking.findByName", query = "SELECT b FROM Booking b WHERE b.name = :name"),
    @NamedQuery(name = "Booking.findByDate", query = "SELECT b FROM Booking b WHERE b.startDate = :date")})
public class Booking extends JsonEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column(name = "ID")
    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "DURATION")
    private int duration;
    @Column(name = "PERSONS")
    private int persons = 1;
    @Column(name = "NAME")
    private String name;
    @Column(name = "STARTDATE")
    private String startDate;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Booking)) {
            return false;
        }
        Booking other = (Booking) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "data.entity.Booking[ id=" + id + " ]";
    }

    @Override
    public boolean setEntityByJson(JsonObject obj, EntityManager em) {
        try{
            this.duration = obj.getInt("duration", 2);
            this.persons = obj.getInt("persons", 1);
            this.name = obj.getString("name", "Anders Svensson");
            Date d = new Date();
            SimpleDateFormat ft = new SimpleDateFormat ("dd/MM-yy 'at' hh:mm");
            this.startDate = ft.format(d);
        }catch(NullPointerException | ClassCastException ex){
            return false;
        }
        return true;
    }

    @Override
    public String toJsonString() {
        JsonObject value = Json.createObjectBuilder()
                .add("id", getId())
                .add("name", getName())
                .add("persons", getPersons())
                .add("duration", getDuration())
                .add("date", getStartDate())
                .build();
        return value.toString();
    }

    /**
     * @return the duration
     */
    public int getDuration() {
        return duration;
    }

    /**
     * @param duration the duration to set
     */
    public void setDuration(int duration) {
        this.duration = duration;
    }

    /**
     * @return the persons
     */
    public int getPersons() {
        return persons;
    }

    /**
     * @param persons the persons to set
     */
    public void setPersons(int persons) {
        this.persons = persons;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the startDate
     */
    public String getStartDate() {
        return startDate;
    }

    /**
     * @param startDate the startDate to set
     */
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
    
}

/*
 * This code is created for one purpose only. And should not be used for any 
 * other purposes unless the author of this file has apporved. 
 * 
 * This code is a piece of a project in the course DT142G on Mid. Sweden university
 * Created by students for this projekt only
 */
package data.entity;

import java.io.Serializable;
import javax.json.Json;
import javax.json.JsonObject;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * This entity describes additional information that does not fit right in a
 * table. For example it stores oppening hours and contact information
 *
 * @author Johannes Lindén
 * @since 2014-10-07
 * @version 1.0
 */
@Entity
@Table(name = "INFO", catalog = "", schema = "APP")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Info.findAll", query = "SELECT i FROM Info i"),
    @NamedQuery(name = "Info.findByWhat", query = "SELECT i FROM Info i WHERE i.what = :what")})
public class Info extends JsonEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "WHAT")
    /**
     * The pk of this information piece
     */
    private String what;
    @Lob
    @Column(name = "DATA")
    /**
     * The information data itself
     */
    private String dataInformation;

    public Info() {
    }

    /**
     * Set the information type pk in the initialization of a new info entity
     *
     * @param what - The pk of this entity
     */
    public Info(String what) {
        this.what = what;
    }
    
    public static String getPK() {
        return "what";
    }

    /**
     * Getter for the type of information of this entity
     *
     * @return The what attribute of this entity
     */
    public String getWhat() {
        return what;
    }

    /**
     * Setter for the type of information of this entity
     *
     * @param what - The type you want this entity to have
     */
    public void setWhat(String what) {
        this.what = what;
    }

    /**
     * Getter for the dataInformation of the information this entity have
     *
     * @return Data of this information
     */
    public String getDataInformation() {
        return dataInformation;
    }

    /**
     * Setter for the dataInformation of this information entity.
     *
     * @param dataInformation - The dataInformation you want to set
     */
    public void setDataInformation(String dataInformation) {
        this.dataInformation = dataInformation;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (what != null ? what.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (!(object instanceof Info)) {
            return false;
        }
        Info other = (Info) object;
        return what.equals(other.what);
    }

    @Override
    public String toString() {
        return "data.entity.Info[ what=" + what + " ]";
    }

    @Override
    public String toJsonString() {
        // Create an json object for the attributes of this entity
        JsonObject value = Json.createObjectBuilder()
                .add("what", getWhat())
                .add("data", getDataInformation())
                .build();
        return value.toString(); // return the string representation of this object.
    }

    @Override
    public boolean setEntityByJson(JsonObject obj, EntityManager em) {
        // Set this infotmation objects attribute
        setWhat(obj.getString("what", ""));
        setDataInformation(obj.getString("data", ""));
        return true; // This method can't fail :)
    }
}

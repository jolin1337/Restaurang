/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
 *
 * @author jolin1337
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
    private String what;
    @Lob
    @Column(name = "DATA")
    private String data;

    public Info() {
    }

    public Info(String what) {
        this.what = what;
    }

    public String getWhat() {
        return what;
    }

    public void setWhat(String what) {
        this.what = what;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
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
        JsonObject value = Json.createObjectBuilder()
                .add("what", getWhat())
                .add("data", getData())
                .build();
        return value.toString();
    }

    @Override
    public boolean setEntityByJson(JsonObject obj, EntityManager em) {
        setWhat(obj.getString("what", ""));
        setData(obj.getString("data", ""));
        return true;
    }
}

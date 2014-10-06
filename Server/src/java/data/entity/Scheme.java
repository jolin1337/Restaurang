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
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jolin1337
 */
@Entity
@Table(name = "SCHEME", catalog = "", schema = "APP")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Scheme.findAll", query = "SELECT s FROM Scheme s"),
    @NamedQuery(name = "Scheme.findById", query = "SELECT s FROM Scheme s WHERE s.id = :id")})
public class Scheme implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private Integer id;

    public Scheme() {
    }

    public Scheme(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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
        if (object == null) {
            return false;
        }
        if (!(object instanceof Scheme)) {
            return false;
        }
        Scheme other = (Scheme) object;
        return id.equals(other.id);
    }

    @Override
    public String toString() {
        return "data.entity.Scheme[ id=" + id + " ]";
    }

    public String toJsonString() {
        JsonObject value = Json.createObjectBuilder()
                .add("id", getId())
                .build();
        return value.toString();
    }
}

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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
 * @author Ali Omran
 */
@Entity
@Table(name = "RESTAURANTUSER", catalog = "", schema = "APP")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RestaurantUser.findAll", query = "SELECT r FROM RestaurantUser r"),
   // @NamedQuery(name = "Restaurantuser.findById", query = "SELECT r FROM Restaurantuser r WHERE r.id = :id"),
 //   @NamedQuery(name = "Restaurantuser.findByUsername", query = "SELECT r FROM Restaurantuser r WHERE r.username = :username"),
   // @NamedQuery(name = "Restaurantuser.findByPassword", query = "SELECT r FROM Restaurantuser r WHERE r.password = :password"),
   // @NamedQuery(name = "Restaurantuser.findByPhonenr", query = "SELECT r FROM Restaurantuser r WHERE r.phonenr = :phonenr"),
    @NamedQuery(name = "RestaurantUser.findByEmail", query = "SELECT r FROM RestaurantUser r WHERE r.email = :email")})
public class RestaurantUser extends JsonEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Integer id;
    @Lob
    @Column(name = "USERNAME")
    private String username;
    @Lob
    @Column(name = "PASSWORD")
    private String password;
    @Lob
    @Column(name = "PHONENR")
    private String phonenr;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Lob
    @Column(name = "EMAIL")
    private String email;

    public RestaurantUser() {
    }

    public RestaurantUser(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhonenr() {
        return phonenr;
    }

    public void setPhonenr(String phonenr) {
        this.phonenr = phonenr;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
        if (!(object instanceof RestaurantUser)) {
            return false;
        }
        RestaurantUser other = (RestaurantUser) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "data.entity.Restaurantuser[ id=" + id + " ]";
    }

    @Override
    public boolean setEntityByJson(JsonObject obj, EntityManager em) {
        try{
            setUsername(obj.getString("username"));
            setPassword(obj.getString("password"));
            setPhonenr(obj.getString("phonenr"));
            setEmail(obj.getString("email"));
        } catch(Exception ex){
            return false;
        }
        return true;
        
    }

    @Override
    public String toJsonString() {
        JsonObject value = Json.createObjectBuilder()
                .add("id", getId())
                .add("username", getUsername())
                .add("password", getPassword())
                .add("phonenr", getPhonenr())
                .add("email", getEmail())
                .build();
        return value.toString();
    }
    
}

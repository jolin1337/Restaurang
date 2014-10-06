/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data.entity;

import java.io.Serializable;
import java.util.List;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonValue;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author jolin1337
 */
@Entity
@Table(name = "DISH", catalog = "", schema = "APP")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Dish.findAll", query = "SELECT d FROM Dish d"),
    @NamedQuery(name = "Dish.findById", query = "SELECT d FROM Dish d WHERE d.id = :id"),
    @NamedQuery(name = "Dish.findByName", query = "SELECT d FROM Dish d WHERE d.name = :name"),
    @NamedQuery(name = "Dish.findByPrice", query = "SELECT d FROM Dish d WHERE d.price = :price")})
public class Dish implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    @NotNull
    @Column(name = "ID")
    private Integer id;
    @Size(max = 255)
    @Column(name = "NAME")
    private String name;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "PRICE")
    private Double price;
    @ManyToMany(mappedBy = "dishList")
    private List<Dishgroup> dishgroupList;
    @JoinTable(name = "DISH_HAS_INVENTORY", joinColumns = {
        @JoinColumn(name = "DISH_ID", referencedColumnName = "ID")}, inverseJoinColumns = {
        @JoinColumn(name = "INVENTORY_ID", referencedColumnName = "ID")})
    @ManyToMany
    private List<Inventory> inventoryList;

    public Dish() {
    }

    public Dish(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        if (name != null) {
            return name;
        }
        return "";
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        if (price != null) {
            return price;
        }
        return 0.0;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @XmlTransient
    public List<Dishgroup> getDishgroupList() {
        return dishgroupList;
    }

    public void setDishgroupList(List<Dishgroup> dishgroupList) {
        this.dishgroupList = dishgroupList;
    }

    @XmlTransient
    public List<Inventory> getInventoryList() {
        return inventoryList;
    }

    public void setInventoryList(List<Inventory> inventoryList) {
        this.inventoryList = inventoryList;
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
        if (!(object instanceof Dish)) {
            return false;
        }
        Dish other = (Dish) object;
        return name.equals(other.name);
    }

    @Override
    public String toString() {
        return "data.entity.Dish[ id=" + id + " ]";
    }

    public String toJsonString() {
        JsonArrayBuilder ingredients = Json.createArrayBuilder();
        for (Inventory i : inventoryList) {
            JsonObject obj = Json.createObjectBuilder().add("as", i.getId()).build();
            JsonValue val = obj.get("as");
            ingredients.add(val);
        }
        JsonObject value = Json.createObjectBuilder()
                .add("id", getId())
                .add("name", getName())
                .add("price", getPrice())
                .add("ingredients", ingredients.build())
                .build();
        return value.toString();
    }
}

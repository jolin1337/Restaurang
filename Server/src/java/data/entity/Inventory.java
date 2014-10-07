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
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonValue;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author jolin1337
 */
@Entity
@Table(name = "INVENTORY", catalog = "", schema = "APP")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Inventory.findAll", query = "SELECT i FROM Inventory i"),
    @NamedQuery(name = "Inventory.findById", query = "SELECT i FROM Inventory i WHERE i.id = :id"),
    @NamedQuery(name = "Inventory.findByAmount", query = "SELECT i FROM Inventory i WHERE i.amount = :amount")})
public class Inventory extends JsonEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private Integer id;
    @Lob
    @Column(name = "NAME")
    private String name;
    @Column(name = "AMOUNT")
    private Integer amount;
    @ManyToMany(mappedBy = "inventoryList")
    private List<Dish> dishList;

    public Inventory() {
    }

    public Inventory(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    @XmlTransient
    public List<Dish> getDishList() {
        return dishList;
    }

    public void setDishList(List<Dish> dishList) {
        this.dishList = dishList;
    }
    public void addToDish(Dish inv) {
        if(inv != null && dishList != null && dishList.indexOf(inv) < 0)
            dishList.add(inv);
    }
    public void removeDish(Dish inv) {
        if(inv != null && dishList != null && dishList.indexOf(inv) > -1)
            dishList.remove(inv);
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
        if (!(object instanceof Inventory)) {
            return false;
        }
        Inventory other = (Inventory) object;
        return id.equals(other.id);
    }

    @Override
    public String toString() {
        return "data.entity.Inventory[ id=" + id + " ]";
    }

    @Override
    public String toJsonString() {
        //System.out.println("InventoryList size: " + inventoryList.size());
        JsonArrayBuilder dishes = Json.createArrayBuilder();
        for (Dish i : dishList) {
            JsonObject obj = Json.createObjectBuilder().add("as", i.getId()).build();
            JsonValue val = obj.get("as");
            dishes.add(val);
        }
        JsonObject value = Json.createObjectBuilder()
                .add("id", getId())
                .add("name", getName())
                .add("amount", getAmount())
                .add("dishes", dishes.build())
                .build();
        return value.toString();
    }


    @Override
    public boolean setEntityByJson(JsonObject obj, EntityManager em) {
        try {
            JsonNumber jamount = obj.getJsonNumber("amount");
            if (jamount != null) {
                setAmount(jamount.intValueExact());
            }
            setName(obj.getString("name", null));
            if(obj.containsKey("dishes") ) {
                /*
                 * Will not be supportet right??!?!?!
                 *
                JsonArray ingredients = obj.getJsonArray("dishes");

                for(JsonValue ing : ingredients) {
                    if(ing.getValueType() == JsonValue.ValueType.NUMBER) {
                        int pk_inv = ((JsonNumber)ing).intValue();
                        if(pk_inv >= 0) {
                            Dish dish = em.find(Dish.class, pk_inv);
                            if(dish != null) {
                                addToDish(dish);
                                // inv.getDishList().add(this);
                            }
                        } else {
                            Dish dish = em.find(Dish.class, -(pk_inv+1));
                            if(dish != null) {
                                removeDish(dish);
                                // inv.getDishList().remove(this);
                            }
                        }
                    }
                }*/
            }
        } catch(Exception ex) {
            return false;
        }
        return true;
    }
}

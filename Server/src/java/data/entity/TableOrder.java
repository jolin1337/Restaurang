/*
 * This code is created for one purpose only. And should not be used for any 
 * other purposes unless the author of this file has apporved. 
 * 
 * This code is a piece of a project in the course DT142G at Mid. Sweden University
 * Created by students for this project only
 */
package data.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonValue;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
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

/**
 * TableOrder entity contains a list of dishes a table has ordered,
 * a table ID and the time at which the order was placed.
 *
 * @author Tomas
 */
@Entity
@Table(name = "TABLEORDER", catalog = "", schema = "APP")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TableOrder.findAllOrders", query = "SELECT o FROM TableOrder o")})
public class TableOrder extends JsonEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "tableID")
    /**
     * The primary key of this entity
     */
    private String id;
    @JoinTable(name = "TABLE_HAS_ORDER", joinColumns = {
        @JoinColumn(name = "TABLE_ID", referencedColumnName = "tableID")}, inverseJoinColumns = {
        @JoinColumn(name = "DISH_ID", referencedColumnName = "ID")})
    @ManyToMany
    private Date timeOfOrder;
    private List<Dish> orderedDishes;

    /**
     * Getter of the table ID
     * @return the table ID
     */
    public String getId() {
        return id;
    }
        
    /**
     * Set the table ID
     * @param id the table ID
     */
    public void setId(String id) {
        this.id = id;
    }
        
    /**
     * Getter of the ordered dishes for this table
     * @return the dishes ordered by this table
     */
    public List<Dish> getOrderedDishes() {
        return orderedDishes;
    }

    /**
     * Sets the ordered dishes for this table
     * @param orderedDishes The list of orders
     */
    public void setOrderedDishes(List<Dish> orderedDishes) {
        this.orderedDishes = orderedDishes;
    }
    
    /**
     * Returns the time at which the order was placed
     * @return the timeOfOrder
     */
    public Date getTimeOfOrder() {
        return timeOfOrder;
    }

    /**
     * Sets as a date object the time at which the order was placed.
     * @param timeOfOrder set the time at which the order was placed
     */
    public void setTimeOfOrder(Date timeOfOrder) {
        this.timeOfOrder = timeOfOrder;
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
        if (!(object instanceof TableOrder)) {
            return false;
        }
        TableOrder other = (TableOrder) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
    
    /**
     * Adds one dish to this tables orders.
     * <p>
     * Warning, this wont always work since there is a possibility 
     * that the dish is not an existing dish. This will generate an error
     * </p>
     * @param dish - The dish you want to add
     */
    public void addToOrders(Dish dish) {
        if (dish != null && orderedDishes != null && orderedDishes.indexOf(dish) < 0) {
            orderedDishes.add(dish);
        }
    }
    
    /**
     * Removes an existing order if it exists for this table.
     * @param dish - The dish to remove
     */
    public void removeOrder(Dish dish) {
        if (dish != null && orderedDishes != null && orderedDishes.indexOf(dish) > -1) {
            orderedDishes.remove(dish);
        }
    }
    
    /**
     * Converts the TableOrder entity into a Json string.
     * @return the entity as Json string.
     */
    @Override
    public String toJsonString() { 
        // First we need to build an array of orders for this table
        JsonArrayBuilder orders = Json.createArrayBuilder(); 
        for (Dish i : orderedDishes) {
        
            JsonObject obj = Json.createObjectBuilder().add("as", i.getId()).build();
            JsonValue val = obj.get("as");
            orders.add(val); // add a index of the current dish
        }
        
        // Create the main json object for the string
        JsonObject value = Json.createObjectBuilder()
                .add("tableID", getId())
                .add("orders", orders.build())
                .add("timeOfOrder", getTimeOfOrder().toString())
                .build();
        return value.toString();
    }
    
    @Override
    public boolean setEntityByJson(JsonObject obj, EntityManager em) {
        try {
            // Parse the json object for insertion in this entity
            setId(obj.getString("tableID", ""));
            JsonArray orders = obj.getJsonArray("orders");
            for (JsonValue itDish : orders) {
                if (itDish.getValueType() == JsonValue.ValueType.NUMBER) {
                    int pk_inv = ((JsonNumber) itDish).intValueExact();
                    if (pk_inv >= 0) {
                        Dish dish = em.find(Dish.class, pk_inv);
                        if (dish != null) {
                            addToOrders(dish);
                        }
                    } else {
                        Dish dish = em.find(Dish.class, -(pk_inv + 1));
                        if (dish != null) {
                            removeOrder(dish);
                        }
                    }
                }
            }
        } catch (Exception e) {
            // Could not add the information to this entity
            System.out.println("Wrong json object in setEntityByJson");
            return false;
        }
        return true;
    }
    
}

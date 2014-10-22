/*
 * This code is created for one purpose only. And should not be used for any 
 * other purposes unless the author of this file has apporved. 
 * 
 * This code is a piece of a project in the course DT142G at Mid. Sweden University
 * Created by students for this project only
 */
package data.entity;

import com.sun.xml.ws.rx.rm.runtime.transaction.TransactionException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.TransactionRequiredException;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

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
    @NamedQuery(name = "TableOrder.findAll", query = "SELECT o FROM TableOrder o ORDER BY o.timeOfOrder"),
    @NamedQuery(name = "TableOrder.findByTableID", query = "SELECT o FROM TableOrder o WHERE o.id = :id")})
public class TableOrder extends JsonEntity implements Serializable {
    @Column(name = "TABLE_NR")
    private Short tableNr;
    @Column(name = "SPECIAL")
    private Short special;
    @OneToMany(cascade = CascadeType.DETACH, mappedBy = "tableOrder")
    private Collection<Tablehasdish> tablehasdishCollection;
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.AUTO) 
    @NotNull
    @Column(name = "ID")
    /**
     * The primary key of this entity
     */
    private Long id;
    @Column(name = "TIMEOFORDER")
    @Temporal(TemporalType.DATE) 
    private Date timeOfOrder;

    /** 
     * Getter of the table ID
     * @return the table ID
     */
    public long getId() {
        return id;
    }
        
    /**
     * Set the table ID
     * @param id the table ID
     */
    public void setId(long id) {
        this.id = id;
    }

    public Short getTableNr() {
        return tableNr;
    }

    public void setTableNr(Short tableNr) {
        this.tableNr = tableNr;
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
     * @param em - The entitymanager to get the relation with dishes with
     * @return true on successed updating/added entity otherwise false
     */
    public boolean addToOrders(Dish dish, EntityManager em) {
        try {
            TypedQuery<Tablehasdish> thdQuery = em.createNamedQuery("Tablehasdish.findByTablenr", Tablehasdish.class);

            List<Tablehasdish> thds = thdQuery.getResultList();
            for(Tablehasdish thd : thds) {
                if(thd.getDish().getId().equals(dish.getId())) {
                    thd.setDish(dish);
                    thd.setDishCount(thd.getDishCount() + 1);
                    em.merge(thd);
                    return true;
                }
            }
            Tablehasdish thd = new Tablehasdish(dish.getId(), id);
            thd.setDishCount(1);
            em.persist(thd);
            return true;
        }
        catch(TransactionRequiredException | IllegalArgumentException | EntityExistsException ex) {
            return false;
        }
    }
    
    /**
     * Removes an existing order if it exists for this table.
     * @param dish - The dish to remove
     * @param em - The entitymanager to get the relation with dishes with
     * @return true on successed updating/removed entity otherwise false
     */
    public boolean removeOrder(Dish dish, EntityManager em) {
        TypedQuery<Tablehasdish> thdQuery = em.createNamedQuery("Tablehasdish.findByTablenr", Tablehasdish.class);
        
        List<Tablehasdish> thds = thdQuery.getResultList();
        for(Tablehasdish thd : thds) {
            if(thd.getDish().getId().equals(dish.getId())) {
                em.detach(thd);
                return true;
            }
        }
        return false;
    }
    
    /**
     * Converts the TableOrder entity into a Json string.
     * @return the entity as Json string.
     */
    @Override
    public String toJsonString() { 
        
        // Create the main json object for the string
        JsonObjectBuilder value = Json.createObjectBuilder()
                .add("id", getId())
                .add("table", getTableNr())
                .add("timeOfOrder", getTimeOfOrder().getTime());
        if(special > 0)
            value.add("special", 1);
        else value.add("special", 0);
        return value.build().toString();
    }
    
    @Override
    public boolean setEntityByJson(JsonObject obj, EntityManager em) {
        try {
            special = (short)(obj.getInt("special") % 2);
            timeOfOrder = new Date(obj.getJsonNumber("timeOfOrder").longValue());
            tableNr = (short)obj.getInt("table");
            // Parse the json object for insertion in this entity
            JsonArray orders = obj.getJsonArray("orders");
            for (JsonValue itDish : orders) {
                if (itDish.getValueType() == JsonValue.ValueType.NUMBER) {
                    int pk_inv = ((JsonNumber) itDish).intValueExact();
                    if (pk_inv >= 0) {
                        Dish dish = em.find(Dish.class, pk_inv);
                        if (dish != null) {
                            decreaseDishInventory(dish.getId(), em);
                            addToOrders(dish, em);

                        }
                    } else {
                        Dish dish = em.find(Dish.class, -(pk_inv + 1));
                        if (dish != null) {
                            removeOrder(dish, em);
                        }
                    }
                }
            }
        } catch (Exception e) {
            // Could not add the information to this entity
            System.out.println("Wrong json object in setEntityByJson: " + obj.toString());
            return false;
        }
        return true;
    }
    
    private boolean decreaseDishInventory(int dishId, EntityManager em){
        TypedQuery<Inventory> query= em.createNamedQuery(
                "Inventory.findInventoryForDish", Inventory.class);
        query.setParameter("id", dishId);
        List<Inventory> inventory = query.getResultList();
//        for(Inventory i: inventory){
//            if (i.getAmount()==0)
//                return false; 
//        }
        for (Inventory i : inventory){
            i.setAmount(i.getAmount()-1);
        }
        return true;
    }
    
    private void subtractIngredient(int ingredientId){
        
    }

    public TableOrder() {
    }

    @XmlTransient
    public Collection<Tablehasdish> getTablehasdishCollection() {
        return tablehasdishCollection;
    }

    public void setTablehasdishCollection(Collection<Tablehasdish> tablehasdishCollection) {
        this.tablehasdishCollection = tablehasdishCollection;
    }
}

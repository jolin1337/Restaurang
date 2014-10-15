/*
 * This code is created for one purpose only. And should not be used for any 
 * other purposes unless the author of this file has apporved. 
 * 
 * This code is a piece of a project in the course DT142G on Mid. Sweden university
 * Created by students for this projekt only
 */
package data.entity;

import java.io.Serializable;
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
import javax.xml.bind.annotation.XmlTransient;

/**
 * This entity describes an group of dishes
 *
 * @author Johannes Lindén
 * @since 2014-10-07
 * @version 1.0
 */
@Entity
@Table(name = "DISHGROUP", catalog = "", schema = "APP")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Dishgroup.alaCarte", query = "SELECT d FROM Dishgroup d WHERE "
            + "d.name != 'mon' AND d.name != 'tue' AND d.name != 'wed' AND "
            + "d.name != 'thu' AND d.name != 'fri' AND d.name != 'sat' AND "
            + "d.name != 'sun'"), 
    @NamedQuery(name = "Dishgroup.weekMenu", query = "SELECT d FROM Dishgroup d WHERE "
            + "d.name = 'mon' OR d.name = 'tue' OR d.name = 'wed' OR "
            + "d.name = 'thu' OR d.name = 'fri' OR d.name = 'sat' OR "
            + "d.name = 'sun'"),
    @NamedQuery(name = "Dishgroup.findAll", query = "SELECT d FROM Dishgroup d"),
    @NamedQuery(name = "Dishgroup.findByName", query = "SELECT d FROM Dishgroup d WHERE d.name = :name")})
public class Dishgroup extends JsonEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "NAME")
    /**
     * The pk of this entity but also the name of the group => you can not have
     * multiple groups with same name
     */
    private String name;
    @JoinTable(name = "GROUP_HAS_DISH", joinColumns = {
        @JoinColumn(name = "GROUP_ID", referencedColumnName = "NAME")}, inverseJoinColumns = {
        @JoinColumn(name = "DISH_ID", referencedColumnName = "ID")})
    @ManyToMany
    /**
     * A list of the dishes this group has
     */
    private List<Dish> dishList;

    public Dishgroup() {
    }

    /**
     * Initializes this entity with an pk (name)
     *
     * @param name - The name of the group
     */
    public Dishgroup(String name) {
        this.name = name;
    }
    
    /**
     * Retrieves the PK name related to this entity
     * @return 
     */
    public static String getPK() {
        return "name";
    }

    /**
     * Getter for the pk(name) of this group
     *
     * @return the name this group was named
     */
    public String getName() {
        return name;
    }

    /**
     * Setter for name of this group
     *
     * @param name - The name you want this group to have
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * Getter of the list with all dishes in this group
     * @return The list of dishes
     */
    @XmlTransient
    public List<Dish> getDishList() {
        return dishList;
    }

    /**
     * Sets the entire list of dishes in this group
     * @param dishList The list dishes you want this entity to have
     */
    public void setDishList(List<Dish> dishList) {
        this.dishList = dishList;
    }

    /**
     * Adds one dish to the end of this group's dishes if it does not already 
     * exists. 
     * <p>
     * Warning, This does not mean it always work since there are an posibility 
     * that the dish is not an existing dish. This will generate an error
     * </p>
     * @param dish - The dish you want to add
     */
    public void addToDishes(Dish dish) {
        if (dish != null && dishList != null && dishList.indexOf(dish) < 0) {
            dishList.add(dish);
        }
    }

    /**
     * Removes an existing dish if it exists in this group.
     * @param dish - The dish entity to remove
     */
    public void removeDish(Dish dish) {
        if (dish != null && dishList != null && dishList.indexOf(dish) > -1) {
            dishList.remove(dish);
        }
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (name != null ? name.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (!(object instanceof Dishgroup)) {
            return false;
        }
        Dishgroup other = (Dishgroup) object;
        return this.name.equals(other.name);
    }

    @Override
    public String toString() {
        return "data.entity.Dishgroup[ name=" + name + " ]";
    }

    @Override
    public String toJsonString() {
        // Create an json object to return.
        
        // First we need to build an array of dishes in this group
        JsonArrayBuilder dishes = Json.createArrayBuilder(); 
        for (Dish i : dishList) {
        
            JsonObject obj = Json.createObjectBuilder().add("as", i.getId()).build();
            JsonValue val = obj.get("as");
            dishes.add(val); // add a index of the current dish
        }
        
        // Create the main json object for the string
        JsonObject value = Json.createObjectBuilder()
                .add("name", getName())
                .add("dishes", dishes.build())
                .build();
        return value.toString();
    }

    @Override
    public boolean setEntityByJson(JsonObject obj, EntityManager em) {
        try {
            // Parse the json object for insertion in this entity
            String gname = obj.getString("name", "");
            if(gname.isEmpty()) return false;
            setName(gname);
            JsonArray dishes = obj.getJsonArray("dishes");
            for (JsonValue itDish : dishes) {
                if (itDish.getValueType() == JsonValue.ValueType.NUMBER) {
                    int pk_inv = ((JsonNumber) itDish).intValueExact();
                    if (pk_inv >= 0) {
                        Dish dish = em.find(Dish.class, pk_inv);
                        if (dish != null) {
                            addToDishes(dish);
                        }
                    } else {
                        Dish dish = em.find(Dish.class, -(pk_inv + 1));
                        if (dish != null) {
                            removeDish(dish);
                        }
                    }
                }
            }
        } catch (Exception e) {
            // Could not add the information to this entity
            System.out.println("Wrong json object in setEntityByJson");
            return false;
        }
        // yes every thing worked perfect!
        return true;
    }

}

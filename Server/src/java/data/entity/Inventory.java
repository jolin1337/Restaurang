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
import javax.json.JsonArrayBuilder;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonValue;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
 * This entity describes an ingredient
 *
 * @author Johannes Lindén
 * @since 2014-10-07
 * @version 1.0
 */
@Entity
@Table(name = "INVENTORY", catalog = "", schema = "APP")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Inventory.findAll", query = "SELECT i FROM Inventory i"),
    @NamedQuery(name = "Inventory.findInventoryForDish", query = "SELECT i FROM Inventory i,Dish d WHERE i.dishList.id = d.id AND d.id = :id"),
    @NamedQuery(name = "Inventory.findById", query = "SELECT i FROM Inventory i WHERE i.id = :id"),
    @NamedQuery(name = "Inventory.findByAmount", query = "SELECT i FROM Inventory i WHERE i.amount = :amount")})
public class Inventory extends JsonEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    @NotNull
    @Column(name = "ID")
    /**
     * The pk of this entity
     */
    private Integer id;
    @Lob
    @Column(name = "NAME")
    /**
     * The name of this ingredient
     */
    private String name;
    @Column(name = "AMOUNT")
    /**
     * The count of how many you have of this ingredient
     */
    private Integer amount;
    @ManyToMany(mappedBy = "inventoryList")
    /**
     * A list of all dishes that uses this ingredient
     */
    private List<Dish> dishList;

    public Inventory() {
    }

    /**
     * Initializes this entity with an specified identifier
     *
     * @param id - the id this entity will have
     */
    public Inventory(Integer id) {
        this.id = id;
    }

    /**
     * Getter of the identifier of this entity
     *
     * @return The id of this entity
     */
    public Integer getId() {
        return id;
    }

    /**
     * This function sets the id on this ingredient. Notice not a good habit of
     * changing this
     *
     * @param id - the id you want it to have
     */
    @Deprecated
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Gets the name of this ingredient
     *
     * @return The current name of this ingredient
     */
    public String getName() {
        return name;
    }

    /**
     * Setter for this ingredient name.
     *
     * @param name - The name you want this ingredient to have
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter for the amount of this ingredient.
     *
     * @return How many portions of this ingredient do you have?
     */
    public Integer getAmount() {
        return amount;
    }

    /**
     * Setter for the amount of this ingredient.
     *
     * @param amount - Have you bought new ones of this ingredient? shure lets
     * add them to the amount propery
     */
    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    /**
     * Getter of the dishes that uses this ingredient.
     *
     * @return The list of dishes
     */
    @XmlTransient
    public List<Dish> getDishList() {
        return dishList;
    }

    /**
     * Setter of the dishlist. This method sets all the dishes that uses this
     * ingredient.
     *
     * @param dishList The list you want this ingredient to have
     */
    public void setDishList(List<Dish> dishList) {
        this.dishList = dishList;
    }

    /**
     * Adds an signle dish to this ingredient dishlist. Means a dish require
     * this dish.
     *
     * @param dish- The dish to add in the ingredient (or reverse =))
     */
    public void addToDish(Dish dish) {
        if (dish != null && dishList != null && dishList.indexOf(dish) < 0) {
            dishList.add(dish);
        }
    }

    /**
     * Remove a single dish from this ingredient dishlist. Means a dish does not
     * anymore require this ingredient.
     *
     * @param dish -The dish to remove in the ingredient (or reverse =))
     */
    public void removeDish(Dish dish) {
        if (dish != null && dishList != null && dishList.indexOf(dish) > -1) {
            dishList.remove(dish);
        }
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
        // Create the json object to get the json string representation of this
        // entity

        // Create a dish JsonArray
        JsonArrayBuilder dishes = Json.createArrayBuilder();
        for (Dish i : dishList) {
            JsonObject obj = Json.createObjectBuilder().add("as", i.getId()).build();
            JsonValue val = obj.get("as");
            dishes.add(val); // add a dish pk from this inventory to json object
        }

        // Create the main json object for the result
        JsonObject value = Json.createObjectBuilder()
                .add("id", getId())
                .add("name", getName())
                .add("amount", getAmount())
                .add("dishes", dishes.build())
                .build();
        return value.toString(); // return the representation
    }

    @Override
    public boolean setEntityByJson(JsonObject obj, EntityManager em) {
        try {
            // Parse the json object to set this entity 

            // Get the amount of this ingredient from json object
            JsonNumber jamount = obj.getJsonNumber("amount");
            if (jamount != null) {
                setAmount(jamount.intValueExact());
            }
            // Get the name from json object
            setName(obj.getString("name", null));
            if (obj.containsKey("dishes")) {
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
        } catch (Exception ex) {
            return false; // something went wrong...
        }
        return true; // succeded to edit this inventory
    }
}

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
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Johannes
 */
@Entity
@Table(name = "TABLEHASDISH")
@NamedQueries({
    @NamedQuery(name = "Tablehasdish.findAll", query = "SELECT t FROM Tablehasdish t"),
    @NamedQuery(name = "Tablehasdish.findByDishCount", query = "SELECT t FROM Tablehasdish t WHERE t.dishCount = :dishCount"),
    @NamedQuery(name = "Tablehasdish.findByDishId", query = "SELECT t FROM Tablehasdish t WHERE t.tablehasdishPK.dishId = :dishId"),
    @NamedQuery(name = "Tablehasdish.findByTablenr", query = "SELECT t FROM Tablehasdish t WHERE t.tablehasdishPK.dishId = :dishId"),
    @NamedQuery(name = "Tablehasdish.findAllOrders", query = "SELECT d FROM TableOrder o, Tablehasdish d WHERE o.id = d.tablehasdishPK.tablenr")})
public class Tablehasdish extends JsonEntity implements Serializable {

    public static TablehasdishPK getPKOf(int tableNr, long id, int dishId, EntityManager em) {
        TypedQuery<Tablehasdish> query = em.createNamedQuery("Tablehasdish.findByTablenr", Tablehasdish.class);
        query.setParameter("dishId", dishId);
        List<Tablehasdish> tbls = query.getResultList();
        TablehasdishPK pk = null;

        for(Tablehasdish tbl : tbls) {
            if(tbl.getTableOrder().getId() == id && tbl.getDish().getId() == dishId)
                pk = tbl.getTablehasdishPK();
        }
        return pk;
    }
    @Basic(optional = false)
    @NotNull
    @Column(name = "DISHSPECIAL")
    private short dishspecial;
    private static final long serialVersionUID = 1L; 
    @EmbeddedId
    protected TablehasdishPK tablehasdishPK;
    @Column(name = "DISH_COUNT")
    private Integer dishCount;
    @JoinColumn(name = "TABLENR", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false, cascade = CascadeType.DETACH)
    private TableOrder tableOrder;
    @JoinColumn(name = "DISH_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false, cascade = CascadeType.DETACH)
    private Dish dish;

    public Tablehasdish() {
    }

    public Tablehasdish(TablehasdishPK tablehasdishPK) {
        this.tablehasdishPK = tablehasdishPK;
    }

    public Tablehasdish(int dishId, long tablenr) {
        this.tablehasdishPK = new TablehasdishPK(dishId, tablenr);
    }

    public TablehasdishPK getTablehasdishPK() {
        return tablehasdishPK;
    }

    public void setTablehasdishPK(TablehasdishPK tablehasdishPK) {
        this.tablehasdishPK = tablehasdishPK;
    }

    public Integer getDishCount() {
        return dishCount;
    }

    public void setDishCount(Integer dishCount) {
        this.dishCount = dishCount;
    }

    public TableOrder getTableOrder() {
        return tableOrder;
    }

    public void setTableOrder(TableOrder tableOrder) {
        this.tableOrder = tableOrder;
    }

    public Dish getDish() {
        return dish;
    }

    public void setDish(Dish dish) {
        this.dish = dish;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tablehasdishPK != null ? tablehasdishPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Tablehasdish)) {
            return false;
        }
        Tablehasdish other = (Tablehasdish) object;
        if ((this.tablehasdishPK == null && other.tablehasdishPK != null) || (this.tablehasdishPK != null && !this.tablehasdishPK.equals(other.tablehasdishPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "data.entity.Tablehasdish[ tablehasdishPK=" + tablehasdishPK + " ]";
    }

    @Override
    public boolean setEntityByJson(JsonObject obj, EntityManager em) {
        int dishId = obj.getInt("dishId");
        setDishspecial((short)(obj.getInt("special")%2));
        setDishCount(obj.getInt("dishCount"));
        Dish d = em.find(Dish.class, dishId);
        
        if(tablehasdishPK == null) {
            tablehasdishPK = new TablehasdishPK(d.getId(), tableOrder.getId());
        }
        TableOrder tmp = em.find(TableOrder.class, (long)obj.getInt("id"));
        if(tmp != null) {
            tmp.setTimeOfOrder(obj.getInt("timeOfOrder"));
            tableOrder = tmp;
        }
        if(d != null) {
            dish = d;
        }
        else {
            return false;
        }
        return true;
    }

    @Override
    public String toJsonString() {/*
        JsonObject jsonObj = Json.createObjectBuilder()
                .add("tableOrderId", tablehasdishPK.getTablenr())
                .add("dishId", tablehasdishPK.getDishId()) 
                .add("dishCount",dishCount).build(); 
        return jsonObj.toString();*/
        
        
        
        // Create the main json object for the string
        JsonObjectBuilder value = Json.createObjectBuilder()
                .add("tableOrder", tableOrder.toJsonObject())
                .add("dish", 
                        Json.createObjectBuilder()
                            .add("id", dish.getId())
                            .add("special", getDishspecial())
                            .add("dishCount", getDishCount())
                            .build());
        return value.build().toString();
    }

    public short getDishspecial() {
        return dishspecial;
    }

    public void setDishspecial(short dishspecial) {
        this.dishspecial = dishspecial;
    }
    
}

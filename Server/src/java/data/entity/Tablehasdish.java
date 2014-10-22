/*
 * This code is created for one purpose only. And should not be used for any 
 * other purposes unless the author of this file has apporved. 
 * 
 * This code is a piece of a project in the course DT142G on Mid. Sweden university
 * Created by students for this projekt only
 */
package data.entity;

import java.io.Serializable;
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
    @NamedQuery(name = "Tablehasdish.findByTablenr", query = "SELECT t FROM Tablehasdish t WHERE t.tablehasdishPK.tablenr = :tablenr"),
    @NamedQuery(name = "Tablehasdish.findAllOrders", query = "SELECT d FROM TableOrder o, Tablehasdish d WHERE o.id = d.tablehasdishPK.tablenr")})
public class Tablehasdish extends JsonEntity implements Serializable {
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
                .add("tableOrder", tableOrder.toJsonString())
                .add("dish", dish.toJsonString())
                .add("dishCount", dishCount);
        if(getDishspecial() > 0)
            value.add("special", 1);
        else 
            value.add("special", 0);
        return value.build().toString();
    }

    public short getDishspecial() {
        return dishspecial;
    }

    public void setDishspecial(short dishspecial) {
        this.dishspecial = dishspecial;
    }
    
}

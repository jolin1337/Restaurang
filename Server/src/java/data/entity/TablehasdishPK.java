/*
 * This code is created for one purpose only. And should not be used for any 
 * other purposes unless the author of this file has apporved. 
 * 
 * This code is a piece of a project in the course DT142G on Mid. Sweden university
 * Created by students for this projekt only
 */
package data.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Johannes
 */
@Embeddable
public class TablehasdishPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "DISH_ID")
    private int dishId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "TABLENR")
    private long tablenr;

    public TablehasdishPK() {
    }

    public TablehasdishPK(int dishId, long tablenr) {
        this.dishId = dishId;
        this.tablenr = tablenr;
    }

    public int getDishId() {
        return dishId;
    }

    public void setDishId(int dishId) {
        this.dishId = dishId;
    }

    public long getTablenr() {
        return tablenr;
    }

    public void setTablenr(long tablenr) {
        this.tablenr = tablenr;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) dishId;
        hash += (int) tablenr;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TablehasdishPK)) {
            return false;
        }
        TablehasdishPK other = (TablehasdishPK) object;
        if (this.dishId != other.dishId) {
            return false;
        }
        if (this.tablenr != other.tablenr) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "data.entity.TablehasdishPK[ dishId=" + dishId + ", tablenr=" + tablenr + " ]";
    }
    
}

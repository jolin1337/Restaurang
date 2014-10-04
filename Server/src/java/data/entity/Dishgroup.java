/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data.entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name = "DISHGROUP", catalog = "", schema = "APP")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Dishgroup.findAll", query = "SELECT d FROM Dishgroup d"),
    @NamedQuery(name = "Dishgroup.findByName", query = "SELECT d FROM Dishgroup d WHERE d.name = :name")})
public class Dishgroup implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "NAME")
    private String name;
    @JoinTable(name = "GROUP_HAS_DISH", joinColumns = {
        @JoinColumn(name = "GROUP_ID", referencedColumnName = "NAME")}, inverseJoinColumns = {
        @JoinColumn(name = "DISH_ID", referencedColumnName = "ID")})
    @ManyToMany
    private List<Dish> dishList;

    public Dishgroup() {
    }

    public Dishgroup(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlTransient
    public List<Dish> getDishList() {
        return dishList;
    }

    public void setDishList(List<Dish> dishList) {
        this.dishList = dishList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (name != null ? name.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Dishgroup)) {
            return false;
        }
        Dishgroup other = (Dishgroup) object;
        if ((this.name == null && other.name != null) || (this.name != null && !this.name.equals(other.name))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "data.entity.Dishgroup[ name=" + name + " ]";
    }
    
}
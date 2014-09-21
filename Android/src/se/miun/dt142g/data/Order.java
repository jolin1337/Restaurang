/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.miun.dt142g.data;

/**
 *
 * @author Johannes
 */
public class Order {
    public String name = "Inte Specificerad";
    private boolean isDeleted = false;
    public Order(String name, boolean deleted) {
        this.name = name;
        this.isDeleted = deleted;
    }
    public Order(String name) {
        this.name = name;
    }
    public Order() {
    }
    
    public void delete(boolean delete) {
        isDeleted = delete;
    }
    public void delete() {
        isDeleted = true;
    }
    public void restore() {
        isDeleted = false;
    }
    public boolean deleted() {
        return isDeleted;
    }
   
    @Override
    public String toString() {
        if(deleted())
            return "== Tryck här för att ångra ? ==";
        return name;
    }
}

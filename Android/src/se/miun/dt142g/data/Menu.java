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
public class Menu {
    public String name = "Inte Specificerad";
    private boolean isDeleted = false;
    public float price = 0.0f;
    public Menu(String name, float price) {
        this.name = name;
        this.price = price;
    }
    public Menu(String name, float price, boolean deleted) {
        this.name = name;
        this.price = price;
        this.isDeleted = deleted;
    }
    public Menu(String name, boolean deleted) {
        this.name = name;
        this.isDeleted = deleted;
    }
    public Menu(String name) {
        this.name = name;
    }
    public Menu() {
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

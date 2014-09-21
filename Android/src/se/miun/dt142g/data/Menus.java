/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.miun.dt142g.data;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Johannes
 */
public class Menus {
    List<Menu> menus;
    public Menus() {
    }
    public boolean readMenus() {
        menus = new ArrayList<Menu>();
        menus.add(new Menu("Spagetti", 45));
        menus.add(new Menu("Fisk", 100));
        menus.add(new Menu("Gröt", 20));
        menus.add(new Menu("Potatisbullar", 50));
        menus.add(new Menu("Stuvning", 80));
        // TODO: Read menus
        System.out.println("Not yet implemented!");
        return true;
    }
    public void addMenu(Menu m) {
        menus.add(m);
    }
    public void addMenu(String name, int price) {
        menus.add(new Menu(name, price));
    }
    public void removeMenu(int i) {
        menus.remove(i);
    }
    public void removeMenu(Menu m) {
        menus.remove(m);
    }
    public Menu getMenu(int pos) {
        return menus.get(pos);
    }
    public Menu getMenu(String menuName) {
        if(menus == null) readMenus();
        if(menus == null) return null;
        for(Menu m : menus)
            if(m.name.contentEquals(menuName))
                return m;
        return null;
    }

    public CharSequence[] toCharSequence() {
        CharSequence[] charSeq = new CharSequence[menus.size()];
        int i=0;
        for(Menu m : menus) {
            charSeq[i] = m.toString();
            i++;
        }
        return charSeq;
    }
}

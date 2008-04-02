/**
 * 
 */
package com.sun.faces.sandbox.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jason Lee
 *
 */
public class Menu {
    protected String name;
    protected List<MenuItem> menuItems;
    
    public Menu() {
        //
    }
    
    public Menu(String name) {
        this.name = name;
    }
    
    public Menu (String name, List<MenuItem> menuItems) {
        this.name = name;
        this.menuItems = menuItems;
    }
    
    public Menu (String name, MenuItem menuItem) {
        this.name = name;
        this.addMenuItem(menuItem);
    }
    
    public List<MenuItem> getMenuItems() {
        return menuItems;
    }
    public void setMenuItems(List<MenuItem> menuItems) {
        this.menuItems = menuItems;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public void addMenuItem(MenuItem item) {
        if (menuItems == null) {
            menuItems = new ArrayList<MenuItem>();
        }
        menuItems.add(item);
    }
    
    public String toString() {
        StringBuilder sb = new StringBuilder();
        
        sb.append ("[Menu name: '")
            .append (name)
            .append((menuItems == null) ? "'" : " menuItems : " + menuItems.toString())
            .append("]");
        
        return sb.toString();
    }
}
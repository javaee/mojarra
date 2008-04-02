/**
 * 
 */
package com.sun.faces.sandbox.model;

/**
 * @author <a href="mailto:jdlee@dev.java.net">Jason Lee</a>
 *
 */
public class MenuItem {
    protected String link;
    protected String label;
    protected Menu subMenu;

    public MenuItem() {
        //
    }
    
    public MenuItem(String label, String link) {
        this (label, link, null);
    }
    
    public MenuItem (String label, String link, Menu menu) {
        this.label = label;
        this.link = link;
        this.subMenu = menu;
    }
    
    public String getLabel() {
        return label;
    }
    public void setLabel(String label) {
        this.label = label;
    }
    public String getLink() {
        return link;
    }
    public void setLink(String link) {
        this.link = link;
    }
    public Menu getSubMenu() {
        return subMenu;
    }
    public void setSubMenu(Menu subMenu) {
        this.subMenu = subMenu;
    }
    
    public String toString() {
        StringBuilder sb = new StringBuilder();
        
        sb.append ("[MenuItem label: '")
            .append (label)
            .append ("' link: '")
            .append (link)
            .append ((subMenu == null) ? "'" : " subMenu : " + subMenu.toString())
            .append ("]");
        
        return sb.toString();
    }
}
/**
 * 
 */
package com.sun.faces.sandbox.test.webapp;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import com.sun.faces.sandbox.model.FileHolder;
import com.sun.faces.sandbox.model.FileHolderImpl;
import com.sun.faces.sandbox.model.HtmlNode;
import com.sun.faces.sandbox.model.Menu;
import com.sun.faces.sandbox.model.MenuItem;
import com.sun.faces.sandbox.model.MenuNode;
import com.sun.faces.sandbox.model.TextNode;
import com.sun.faces.sandbox.model.TreeNode;

/**
 * @author lee
 *
 */
public class TestBean {
    protected Date date;
    protected FileHolder fileHolder = new FileHolderImpl();
    
    public TestBean() {
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public TreeNode getTree() {
        TreeNode top = new TextNode ("Top Node!");
        TreeNode category = null;
        
        category = new TextNode("Books for Java Programmers");
        top.add(category);
        category.add(new TextNode("The Java Tutorial: A Short Course on the Basics"));
        category.add(new TextNode("The Java Tutorial Continued: The Rest of the JDK"));
        category.add(new TextNode("The JFC Swing Tutorial: A Guide to Constructing GUIs"));
        category.add(new MenuNode("Menu Test", "http://blogs.steeplesoft.com"));

        category = new TextNode("Books for Java Implementers");
        top.add(category);
        category.add(new TextNode("The Java Virtual Machine Specification"));
        category.add(new HtmlNode("The Java Language Specification", "<b>Gilad Brach!</b>"));
        
        return top;
    }

    public Menu getMenu() {
        Menu menu = new Menu();
        menu.setName("productsandservices");
        menu.addMenuItem(buildCommunicationMenu());
        menu.addMenuItem(buildShoppingMenu());
        menu.addMenuItem(buildEntertainmentMenu());
        menu.addMenuItem(buildInformationMenu());
        
        return menu;
    }
    
    protected MenuItem buildCommunicationMenu() {
        MenuItem comm = new MenuItem();
        comm.setLabel("Communication");
        comm.setLink("http://communication.yahoo.com");
        
        Menu subMenu = new Menu("CommSubMenu");
        subMenu.addMenuItem(new MenuItem("360", "http://360.yahoo.com"));
        subMenu.addMenuItem(new MenuItem("Alerts", "http://alerts.yahoo.com"));
        subMenu.addMenuItem(new MenuItem("Avatars", "http://avatars.yahoo.com"));
        subMenu.addMenuItem(new MenuItem("Groups", "http://groups.yahoo.com"));
        subMenu.addMenuItem(new MenuItem("Internet Access", "http://promo.yahoo.com/broadband/"));
        comm.setSubMenu(subMenu);
        MenuItem pim = new MenuItem ("PIM", null);
        Menu pimSubMenu = new Menu();
        pimSubMenu.addMenuItem(new MenuItem("Yahoo! Mail", "http://mail.yahoo.com"));
        pimSubMenu.addMenuItem(new MenuItem("Yahoo! Address Book", "http://addressbook.yahoo.com"));
        pimSubMenu.addMenuItem(new MenuItem("Yahoo! Calendar", "http://calendar.yahoo.com"));
        pimSubMenu.addMenuItem(new MenuItem("Yahoo! Notepad", "http://notepad.yahoo.com"));
        pim.setSubMenu(pimSubMenu);
        subMenu.addMenuItem(pim);
        subMenu.addMenuItem(new MenuItem("Member Directory", "http://members.yahoo.com"));
        subMenu.addMenuItem(new MenuItem("Messenger", "http://messenger.yahoo.com"));
        subMenu.addMenuItem(new MenuItem("Mobile", "http://mobile.yahoo.com"));
        subMenu.addMenuItem(new MenuItem("Photos", "http://photos.yahoo.com"));
        
        return comm;
    }
    
    protected MenuItem buildShoppingMenu() {
        MenuItem shopping = new MenuItem("Shopping", "http://shopping.yahoo.com");
        Menu subMenu = new Menu ("ShoppingSubMenu");
        
        subMenu.addMenuItem(new MenuItem("Auctions", "http://auctions.shopping.yahoo.com"));

        subMenu.addMenuItem(new MenuItem("Autos", "http://autos.yahoo.com"));
        subMenu.addMenuItem(new MenuItem("Classifieds", "http://classifieds.yahoo.com"));
        subMenu.addMenuItem(new MenuItem("Flowers &amp; Gifts", "http://shopping.yahoo.com/b:Flowers%20%26%20Gifts:20146735"));
        subMenu.addMenuItem(new MenuItem("Points", "http://points.yahoo.com"));
        subMenu.addMenuItem(new MenuItem("Real Estate", "http://realestate.yahoo.com"));

        subMenu.addMenuItem(new MenuItem("Travel", "http://travel.yahoo.com"));
        subMenu.addMenuItem(new MenuItem("Wallet", "http://wallet.yahoo.com"));
        subMenu.addMenuItem(new MenuItem("Yellow Pages", "http://yp.yahoo.com"));
        shopping.setSubMenu(subMenu);
        
        return shopping;
    }
    
    protected MenuItem buildEntertainmentMenu() {
        MenuItem entertainment = new MenuItem("Entertainment", "http://entertainment.yahoo.com");
        Menu subMenu = new Menu("EntertainmentSubMenu");
    
        subMenu.addMenuItem(new MenuItem("Fantasy Sports", "http://fantasysports.yahoo.com"));
        subMenu.addMenuItem(new MenuItem("Games", "http://games.yahoo.com"));
        subMenu.addMenuItem(new MenuItem("Kids", "http://www.yahooligans.com"));
        subMenu.addMenuItem(new MenuItem("Music", "http://music.yahoo.com"));

        subMenu.addMenuItem(new MenuItem("Movies", "http://movies.yahoo.com"));
        subMenu.addMenuItem(new MenuItem("Radio", "http://music.yahoo.com/launchcast"));
        subMenu.addMenuItem(new MenuItem("Travel", "http://travel.yahoo.com"));
        subMenu.addMenuItem(new MenuItem("TV", "http://tv.yahoo.com"));

        entertainment.setSubMenu(subMenu);
        return entertainment;
    }
    
    protected MenuItem buildInformationMenu() {
        MenuItem info = new MenuItem ("Information", null);
        Menu subMenu = new Menu("InformationSubMenu");
        
        subMenu.addMenuItem(new MenuItem("Downloads", "http://downloads.yahoo.com"));
        subMenu.addMenuItem(new MenuItem("Finance", "http://finance.yahoo.com"));
        subMenu.addMenuItem(new MenuItem("Health", "http://health.yahoo.com"));
        subMenu.addMenuItem(new MenuItem("Local", "http://local.yahoo.com"));

        subMenu.addMenuItem(new MenuItem("Maps &#38; Directions", "http://maps.yahoo.com"));
        subMenu.addMenuItem(new MenuItem("My Yahoo!", "http://my.yahoo.com"));
        subMenu.addMenuItem(new MenuItem("News", "http://news.yahoo.com"));
        subMenu.addMenuItem(new MenuItem("Search", "http://search.yahoo.com"));
        subMenu.addMenuItem(new MenuItem("Small Business", "http://smallbusiness.yahoo.com"));

        subMenu.addMenuItem(new MenuItem("Weather", "http://weather.yahoo.com"));

        info.setSubMenu(subMenu);
        return info;
    }
    
    public byte[] getPdf() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            InputStream is = this.getClass().getClassLoader().getResourceAsStream("/sample.pdf");
            
            int count = 0;
            byte[] buffer = new byte[4096];
            while ((count = is.read(buffer)) != -1) {
                if (count > 0) {
                    baos.write(buffer, 0, count);
                }
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        
        return baos.toByteArray();
    }
    
    public byte[] getImage() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            InputStream is = this.getClass().getClassLoader().getResourceAsStream("/sample.png");
            
            int count = 0;
            byte[] buffer = new byte[4096];
            while ((count = is.read(buffer)) != -1) {
                if (count > 0) {
                    baos.write(buffer, 0, count);
                }
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        
        return baos.toByteArray();
    }

    public String getDestination() {
        return "jsp/success";
    }

    public FileHolder getFileHolder() {
        return fileHolder;
    }

    public String[] getFileNames() {
        String[] fileNames = fileHolder.getFileNames().toArray(new String[]{});
        
        return fileNames;
    }
}

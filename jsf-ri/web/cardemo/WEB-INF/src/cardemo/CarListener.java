package cardemo;

import javax.faces.CommandListener;
import javax.faces.CommandEvent;
import javax.faces.CommandFailedException;
import javax.faces.ValueChangeListener;
import javax.faces.ValueChangeEvent;
import javax.faces.NavigationHandler;
import javax.faces.UIComponent;
import javax.faces.ObjectManager;
import javax.faces.FacesEvent;
import java.io.*;
import java.lang.System;

import javax.servlet.http.*;
import javax.servlet.*;

/**
 * The listener interface for handling command events.
 * An object should implement this interface if it needs
 * to respond to a command event.
 */
public class CarListener implements CommandListener{
    
    private ServletContext servletContext;
    
    public CarListener( ) {
        System.out.println("CarListener Created");
    }
    
    public void doCommand(CommandEvent e, NavigationHandler nh )
    throws CommandFailedException {
        String gifName1 = "crop_civic_regular.gif";
        String gifName2 = "current.gif";
        
        UIComponent source = e.getSourceComponent();
        String sourceId = source.getId();
        String cmdName = e.getCommandName();
        System.out.println("Command is: " + cmdName);
        ObjectManager ot = ObjectManager.getInstance();
        FacesEvent fe = (FacesEvent) e;
        HttpServletRequest req = (HttpServletRequest) ((fe.getFacesContext()).getRequest());
        CurrentOptionServer optServer = (CurrentOptionServer) ot.get(req, "CurrentOptionServer");
        servletContext = req.getSession().getServletContext();
        
        // first car selected
        if (cmdName.equals("buyCar1")) {
            synchronized(servletContext) {
                try {
                    /*
                    java.io.File file = new File(".." + File.separator +
                    "webapps" + File.separator + "cardemo" +
                    File.separator + "pictures" +
                    File.separator + "crop_honda_civic_regular.gif");
                     
                    if (file.renameTo(new File(".." + File.separator +
                    "webapps" + File.separator + "cardemo" +
                    File.separator + "pictures" +
                    File.separator + "current.gif"))) {
                        System.out.println("Image copy succeeded!");
                    }
                    else {
                        System.out.println("Image copy FAILED!!!");
                    }
                     */
                    // nasty, nasty, nasty hack
                    int data = 0;
                    File fin = new File(servletContext.getRealPath("pictures") +
                        File.separator + "crop_honda_civic_regular.gif");
                    File fout = new File(servletContext.getRealPath("pictures") +
                        File.separator + "current.gif");
                    
                    FileInputStream fis = null;
                    FileOutputStream fos = null;
                    
                    try {
                        fis = new FileInputStream(fin);
                        fos = new FileOutputStream(fout);
                    } catch (FileNotFoundException ignored) {}
                    
                    try {
                        
                        while ((data=fis.read()) != -1  ) {
                            fos.write(data);
                        }
                        
                        fis.close();
                        fos.close();

                    } catch (IOException ignoredAsWell) {}
                    
                    optServer.setCarTitle("Honda Civic 4dr Sedan");
                    optServer.setCarDesc("If all cars were as well built as the Honda Civic, we'd probably live in a world with less repair shops and more car washes. The Civic's bullet-proof build quality is but one of the reasons for its success; one could also point to the car's excellent resale value and loyal repeat buyer base. But if you really want to understand why so many love this car, you need only talk to a group of Civic owners.");
                    optServer.setBasePrice("$10000");
                    optServer.setCurrentPrice("$10000");
                    if (nh != null) {
                        nh.handleCommandSuccess(cmdName);
                    }
                }
                catch ( CommandFailedException ce) {
                    if ( nh != null ) {
                        nh.handleCommandException(cmdName, ce);
                    }
                    throw ce;
                }
            }
        }
        
        // second car selected
        else if (cmdName.equals("buyCar2")) {
            synchronized(servletContext) {
                try {
                    // grievously nasty hack: rename current.gif
                    /*
                    java.io.File file = new File(".." + File.separator +
                    "webapps" + File.separator + "cardemo" +
                    File.separator + "pictures" +
                    File.separator + "crop_acura_sport_regular.gif");
                    
                    if (file.renameTo(new File(".." + File.separator +
                    "webapps" + File.separator + "cardemo" +
                    File.separator + "pictures" +
                    File.separator + "current.gif"))) {
                        System.out.println("Image copy succeeded!");
                    }
                    else {
                        System.out.println("Image copy FAILED!!!");
                    }
                    */
                    int data = 0;
                    File fin = new File(servletContext.getRealPath("pictures") +
                        File.separator + "crop_acura_sport_regular.gif");
                    File fout = new File(servletContext.getRealPath("pictures") +
                        File.separator + "current.gif");
                    
                    FileInputStream fis = null;
                    FileOutputStream fos = null;
                    
                    try {
                        fis = new FileInputStream(fin);
                        fos = new FileOutputStream(fout);
                    } catch (FileNotFoundException ignored) {}
                    
                    try {
                        
                        while ((data=fis.read()) != -1  ) {
                            fos.write(data);
                        }
                        
                        fis.close();
                        fos.close();

                    } catch (IOException ignoredAsWell) {}
                    optServer.setCarTitle("Acura RSX");
                    optServer.setCarDesc("Acura introduces an all-new sport coupe to its line this year, dropping the legendary Integra name in favor of RSX. Two trim levels are offered, a base model and Type-S. The latter comes with a blisteringly fast, high-output 2.0-liter engine producing 200 horsepower. A six-speed manual transmission, high-end Bose sound system and leather-clad sport seats are also standard equipment.");
                    optServer.setBasePrice("$15000");
                    optServer.setCurrentPrice("$15000");
                    if (nh != null) {
                        nh.handleCommandSuccess(cmdName);
                    }
                }
                catch ( CommandFailedException ce) {
                    if ( nh != null ) {
                        nh.handleCommandException(cmdName, ce);
                    }
                    throw ce;
                }
            }
        }
        
        // third car selected
        else if (cmdName.equals("buyCar3")) {
            synchronized(servletContext) {
                try {
                    /*
                    java.io.File file = new File(".." + File.separator +
                    "webapps" + File.separator + "cardemo" +
                    File.separator + "pictures" +
                    File.separator + "crop_lexus_regular.gif");
                    
                    if (file.renameTo(new File(".." + File.separator +
                    "webapps" + File.separator + "cardemo" +
                    File.separator + "pictures" +
                    File.separator + "current.gif"))) {
                        System.out.println("Image copy succeeded!");
                    }
                    else {
                        System.out.println("Image copy FAILED!!!");
                    }
                    */
                    int data = 0;
                    File fin = new File(servletContext.getRealPath("pictures") +
                        File.separator + "crop_lexus_regular.gif");
                    File fout = new File(servletContext.getRealPath("pictures") +
                        File.separator + "current.gif");
                    
                    FileInputStream fis = null;
                    FileOutputStream fos = null;
                    
                    try {
                        fis = new FileInputStream(fin);
                        fos = new FileOutputStream(fout);
                    } catch (FileNotFoundException ignored) {}
                    
                    try {
                        
                        while ((data=fis.read()) != -1  ) {
                            fos.write(data);
                        }
                        
                        fis.close();
                        fos.close();

                    } catch (IOException ignoredAsWell) {}
                    optServer.setCarTitle("Lexus LS 300");
                    optServer.setCarDesc("Someone once said that perfection is a road, not a destination. If this is true, then the engineers and designers at Lexus probably have the route memorized. With no apparent fear of losing their way, the Lexus team has allowed their flagship sedan, the LS 430, to momentarily detour from its journey. For the time being, it is parked comfortably at the crossroads where science and art converge to create an extraordinary driving experience.");
                    optServer.setBasePrice("$30000");
                    optServer.setCurrentPrice("$30000");
                    if (nh != null) {
                        nh.handleCommandSuccess(cmdName);
                    }
                }
                catch ( CommandFailedException ce) {
                    if ( nh != null ) {
                        nh.handleCommandException(cmdName, ce);
                    }
                    throw ce;
                }
            }
        }
        
        // fourth car selected
        else if (cmdName.equals("buyCar4")) {
            synchronized(servletContext) {
                try {
                    /*
                    java.io.File file = new File(".." + File.separator +
                    "webapps" + File.separator + "cardemo" +
                    File.separator + "pictures" +
                    File.separator + "crop_ford_explorer_regular.gif");
                    
                    if (file.renameTo(new File(".." + File.separator +
                    "webapps" + File.separator + "cardemo" +
                    File.separator + "pictures" +
                    File.separator + "current.gif"))) {
                        System.out.println("Image copy succeeded!");
                    }
                    else {
                        System.out.println("Image copy FAILED!!!");
                    }
                    */
                    int data = 0;
                    File fin = new File(servletContext.getRealPath("pictures") +
                        File.separator + "crop_ford_explorer_regular.gif");
                    File fout = new File(servletContext.getRealPath("pictures") +
                        File.separator + "current.gif");
                    
                    FileInputStream fis = null;
                    FileOutputStream fos = null;
                    
                    try {
                        fis = new FileInputStream(fin);
                        fos = new FileOutputStream(fout);
                    } catch (FileNotFoundException ignored) {}
                    
                    try {
                        
                        while ((data=fis.read()) != -1  ) {
                            fos.write(data);
                        }
                        
                        fis.close();
                        fos.close();

                    } catch (IOException ignoredAsWell) {}
                    optServer.setCarTitle("Ford Explorer");
                    optServer.setCarDesc("The Ford Explorer has been completely redesigned for 2002. Ford engineers have replaced the old Explorer's solid rear axle with a new independent rear suspension, placing the differential high up in the frame. This design gives the Explorer greater ground clearance while vastly improving both its ride and handling. It also frees-up space for an optional hide-away third-row seat, capable of expanding the Explorer's seating capacity to seven.");
                    optServer.setBasePrice("$20000");
                    optServer.setCurrentPrice("$20000");
                    if (nh != null) {
                        nh.handleCommandSuccess(cmdName);
                    }
                }
                catch ( CommandFailedException ce) {
                    if ( nh != null ) {
                        nh.handleCommandException(cmdName, ce);
                    }
                    throw ce;
                }
            }
        }
    }
    
    
    
    public boolean requiresValidation(CommandEvent e ) {
        return false;
    }
    
}


/*
 * $Id: EventHandler.java,v 1.5 2002/01/25 18:45:20 visvan Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package basic;

import javax.faces.CommandListener;
import javax.faces.CommandEvent;
import javax.faces.CommandFailedException;
import javax.faces.ValueChangeListener;
import javax.faces.ValueChangeEvent;
import javax.faces.NavigationHandler;

import javax.servlet.http.*;
import javax.servlet.*;

/**
 * The listener interface for handling command events.
 * An object should implement this interface if it needs
 * to respond to a command event.
 */
public class EventHandler implements CommandListener,ValueChangeListener{

    public EventHandler ( ) {
        //super ( cp, ep );
        // System.out.println("Completion path " + cp );
        // System.out.println("Error path " + ep );
        // System.out.println("Event Handler constructor");
    }

    public void doCommand(CommandEvent e, NavigationHandler nh )  
            throws CommandFailedException {

       if ( nh != null ) {
           System.out.println("Command " + e.getCommandName() + " Execution Successful");
           nh.handleCommandSuccess(e.getCommandName());
       } 
       System.out.println("Name of UIComponent that generated event " + e.getSourceId());
       System.out.println("CommandEvent processing successful");
    }

    public void handleValueChange(ValueChangeEvent e) {
       System.out.println("Name of UIComponent that generated event " + e.getSourceId());
       System.out.println("New Value " + e.getNewValue());
       System.out.println("ValueChangeEvent processing successful");
    }

    
}

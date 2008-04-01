/*
 * $Id: BasicListener.java,v 1.1 2002/03/08 02:02:32 jvisvanathan Exp $
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
import javax.faces.UIComponent;

import javax.servlet.http.*;
import javax.servlet.*;

/**
 * The listener interface for handling command events.
 * An object should implement this interface if it needs
 * to respond to a command event.
 */
public class BasicListener implements CommandListener{

    public BasicListener ( ) {
    }

    public void doCommand(CommandEvent e, NavigationHandler nh )  
            throws CommandFailedException {

       if ( nh != null ) {
           System.out.println("Command " + e.getCommandName() + " Execution Successful");
           nh.handleCommandSuccess(e.getCommandName());
       } 
       UIComponent source = e.getSourceComponent();
       System.out.println("Name of UIComponent that generated event " + 
			  source.getId());
       System.out.println("CommandEvent processing successful");
    }


    public boolean requiresValidation(CommandEvent e ) {
        return true;
    }
    
}

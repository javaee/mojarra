/*
 * $Id: EventHandler.java,v 1.3 2002/01/10 22:32:51 edburns Exp $
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
import javax.faces.AbstractCommand;
import javax.faces.ValueChangeEvent;
import javax.servlet.http.*;
import javax.servlet.*;

/**
 * The listener interface for handling command events.
 * An object should implement this interface if it needs
 * to respond to a command event.
 */
public class EventHandler extends AbstractCommand implements CommandListener,ValueChangeListener{

    public EventHandler () {
    }

    public EventHandler (String cp, String ep ) {
        super ( cp, ep );
        // System.out.println("Completion path " + cp );
        // System.out.println("Error path " + ep );
        // System.out.println("Event Handler constructor");
    }

    public void doCommand(CommandEvent e)  throws CommandFailedException {
       System.out.println("Name of UIComponent that generated event " + e.getSourceName());
       System.out.println("CommandEvent processing successful");
    }

    public void handleValueChange(ValueChangeEvent e) {
       System.out.println("Name of UIComponent that generated event " + e.getSourceName());
       System.out.println("New Value " + e.getNewValue());
       System.out.println("CommandEvent processing successful");
    }

    
}

/*
 * $Id: EventDispatcherFactoryImpl.java,v 1.3 2002/01/10 22:20:09 edburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// EventDispatcherFactoryImpl.java

package com.sun.faces;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.Log;
import org.mozilla.util.ParameterCheck;

import javax.servlet.ServletRequest;
import javax.faces.EventDispatcher;
import javax.faces.EventDispatcherFactory;
import javax.faces.FacesException;
import java.util.EventObject;

/**
 *
 *  <B>EventDispatcherFactoryImpl</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: EventDispatcherFactoryImpl.java,v 1.3 2002/01/10 22:20:09 edburns Exp $
 * 
 * @see javax.faces.EventDispatcherFactory
 * @see	javax.faces.EventDispatcher
 *
 */

public class EventDispatcherFactoryImpl extends EventDispatcherFactory
{
    //
    // Protected Constants
    //

    //
    // Class Variables
    //

    //
    // Instance Variables
    //

    // Attribute Instance Variables

    // Relationship Instance Variables

    //
    // Constructors and Initializers    
    //

    public EventDispatcherFactoryImpl()
    {
       super();
    }

    //
    // Class methods
    //

    public EventDispatcher newEventDispatcher(EventObject e) 
            throws FacesException {

        ParameterCheck.nonNull(e);
        Class eventDisClass;
        EventDispatcher result = null;

        String event_classname = null;

        // PENDING ( visvan ) read this from Properties file
        if ( e instanceof javax.faces.CommandEvent ) {
            event_classname = "com.sun.faces.CommandDispatcherImpl";
        } else if ( e instanceof javax.faces.ValueChangeEvent ) {
              event_classname = "com.sun.faces.ValueChangeDispatcherImpl";
        }

        Assert.assert_it(event_classname != null );
        try {
            eventDisClass = Class.forName(event_classname);
            result = (EventDispatcher) eventDisClass.newInstance();
        }
        catch (IllegalAccessException iae) {
            throw new FacesException("IllegalAccessException for " +
                                 event_classname + ": " + iae.getMessage());
        }
        catch (InstantiationException ie) {
            throw new FacesException("Can't create instance for " +
                                 event_classname + ": " + ie.getMessage());
        }
        catch (ClassNotFoundException cfe) {
            throw new FacesException("Can't find class for " +
                                 event_classname + ": " + cfe.getMessage());
        }
        return result;
    }

    //
    // General Methods
    //

} // end of class EventDispatcherFactoryImpl

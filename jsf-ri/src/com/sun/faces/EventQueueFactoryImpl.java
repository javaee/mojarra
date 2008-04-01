/*
 * $Id: EventQueueFactoryImpl.java,v 1.4 2002/04/11 22:52:39 eburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// EventQueueFactoryImpl.java

package com.sun.faces;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.Log;
import org.mozilla.util.ParameterCheck;


import java.util.Map;

import javax.faces.FacesException;
import javax.faces.FacesFactory;
import javax.faces.EventQueue;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.ServletContext;

/**
 *
 *  <B>EventQueueFactoryImpl</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: EventQueueFactoryImpl.java,v 1.4 2002/04/11 22:52:39 eburns Exp $
 * @author Jayashri Visvanathan
 * 
 * @see	javax.faces.EventQueueFactory
 * @see	javax.faces.EventQueue
 *
 */

public class EventQueueFactoryImpl extends Object implements FacesFactory
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

    public EventQueueFactoryImpl()
    {
        super();
    }

    //
    // Methods from EventQueueFactory
    //

    //
    // Class methods
    //

    //
    // Methods from FacesFactory
    //

public Object newInstance(String facesName, ServletRequest req, 
			  ServletResponse res) throws FacesException
{
    throw new FacesException("Can't create EventQueue from Request and Response");
}

public Object newInstance(String facesName, ServletContext ctx) throws FacesException
{
    throw new FacesException("Can't create EventQueue from ServletContext");
}

public Object newInstance(String facesName) throws FacesException
{
    return new EventQueueImpl();
}

public Object newInstance(String facesName, Map args) throws FacesException
{
    throw new FacesException("Can't create EventQueue from map");
}



} // end of class EventQueueFactoryImpl

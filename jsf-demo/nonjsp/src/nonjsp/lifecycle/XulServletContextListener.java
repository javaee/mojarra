/*
 * $Id: XulServletContextListener.java,v 1.1 2003/02/13 23:34:30 horwat Exp $
 *
 * Copyright 2000-2003 by Sun Microsystems, Inc.,
 * 901 San Antonio Road, Palo Alto, California, 94303, U.S.A.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Sun Microsystems, Inc. ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with Sun.
 */

// XulServletContextListener.java

package nonjsp.lifecycle;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletContextEvent;

import javax.faces.FactoryFinder;
import javax.faces.context.FacesContextFactory;
import javax.faces.lifecycle.LifecycleFactory;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.ViewHandler;

/**
 *
 * <B>XulServletContextListener</B> is a class that sets the
 * XulViewHandler to be the default ViewHandler for the web
 * application.
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: XulServletContextListener.java,v 1.1 2003/02/13 23:34:30 horwat Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class XulServletContextListener implements ServletContextListener
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

    public XulServletContextListener()
    {
    }

    //
    // Class methods
    //

    //
    // General Methods
    //

    //
    // Methods from ServletContextListener
    //

    public void contextInitialized(ServletContextEvent event) 
    {
        //Set the ViewHandler to the Xul implementation
        ViewHandler handler = new XulViewHandlerImpl();
        LifecycleFactory factory = (LifecycleFactory)
            FactoryFinder.getFactory(FactoryFinder.LIFECYCLE_FACTORY);
        Lifecycle lifecycle =
            factory.getLifecycle(LifecycleFactory.DEFAULT_LIFECYCLE);
        lifecycle.setViewHandler(handler); 
    }

    public void contextDestroyed(ServletContextEvent e)
    {
    }

} // end of class XulServletContextListener

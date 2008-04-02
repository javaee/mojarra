/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 * 
 * You can obtain a copy of the License at
 * https://javaserverfaces.dev.java.net/CDDL.html or
 * legal/CDDLv1.0.txt. 
 * See the License for the specific language governing
 * permission and limitations under the License.
 * 
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at legal/CDDLv1.0.txt.    
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 * 
 * [Name of File] [ver.__] [Date]
 * 
 * Copyright 2005 Sun Microsystems Inc. All Rights Reserved
 */

// XulServletContextListener.java

package nonjsp.lifecycle;

import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;
import javax.faces.application.ViewHandler;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import nonjsp.application.XulViewHandlerImpl;

/**
 * <B>XulServletContextListener</B> is a class that sets the
 * XulViewHandler to be the default ViewHandler for the web
 * application.
 * <p/>
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: XulServletContextListener.java,v 1.3 2005/12/14 22:27:33 rlubke Exp $
 */

public class XulServletContextListener implements ServletContextListener {

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

    public XulServletContextListener() {
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

    public void contextInitialized(ServletContextEvent event) {
        //Set the ViewHandler to the Xul implementation
        ViewHandler handler = new XulViewHandlerImpl();
        ApplicationFactory factory = (ApplicationFactory)
              FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
        Application application =
              factory.getApplication();
        application.setViewHandler(handler);
    }


    public void contextDestroyed(ServletContextEvent e) {
    }

} // end of class XulServletContextListener

/*
 * $Id: CarDemoServletContextListener.java,v 1.2 2003/01/29 18:46:19 jvisvanathan Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package cardemo;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.ParameterCheck;

import javax.servlet.ServletContextListener;
import javax.servlet.ServletContextEvent;
import javax.faces.FactoryFinder;
import javax.faces.lifecycle.LifecycleFactory;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.ApplicationHandler;
import javax.faces.convert.ConverterFactory;

import com.sun.faces.context.MessageResourcesImpl;
import javax.faces.context.MessageResourcesFactory;
import javax.faces.context.MessageResources;

/**
 *
 *  <B>CarDemoServletContextListener</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: CarDemoServletContextListener.java,v 1.2 2003/01/29 18:46:19 jvisvanathan Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class CarDemoServletContextListener implements ServletContextListener
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

    public CarDemoServletContextListener()
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

    public void contextInitialized(ServletContextEvent e) 
    {
        // System.out.println("CarDemoServletContextListener reached");

        ApplicationHandler handler = new CarDemoApplicationHandler();
        LifecycleFactory factory = (LifecycleFactory)
            FactoryFinder.getFactory(FactoryFinder.LIFECYCLE_FACTORY);
        Lifecycle lifecycle =
            factory.getLifecycle(LifecycleFactory.DEFAULT_LIFECYCLE);
        lifecycle.setApplicationHandler(handler); 

        // register CreditCardConverter
        ConverterFactory convertFactory =
                (ConverterFactory) FactoryFinder.getFactory(
                FactoryFinder.CONVERTER_FACTORY);
        convertFactory.addConverter("creditcard", new CreditCardConverter());
        // System.out.println("Registered CreditCardConverter");

        // register CarDemo MessageResources.
        MessageResourcesFactory mrFactory =
                (MessageResourcesFactory) FactoryFinder.getFactory(
                FactoryFinder.MESSAGE_RESOURCES_FACTORY);
        MessageResourcesImpl carResource = 
                new MessageResourcesImpl("carResources", 
                "cardemo/CarDemoResources");
        mrFactory.addMessageResources("carResources", carResource);
        // System.out.println("Registered CarDemoResources");
    }

    public void contextDestroyed(ServletContextEvent e)
    {
    }

} // end of class CarDemoServletContextListener

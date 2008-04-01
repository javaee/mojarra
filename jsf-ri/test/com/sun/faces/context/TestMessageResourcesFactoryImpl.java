/*
 * $Id: TestMessageResourcesFactoryImpl.java,v 1.2 2002/07/25 16:36:35 eburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestMessageResourcesFactoryImpl.java

package com.sun.faces.context;

//import junit.framework.TestCase;
import org.apache.cactus.ServletTestCase;
import com.sun.faces.context.MessageResourcesImpl;
import com.sun.faces.context.MessageResourcesFactoryImpl;

import javax.faces.context.MessageResourcesFactory;
import javax.faces.context.MessageResources;
import java.util.Iterator;
/**
 *
 *  <B>TestMessageResourceFactoryImpl</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestMessageResourcesFactoryImpl.java,v 1.2 2002/07/25 16:36:35 eburns Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class TestMessageResourcesFactoryImpl extends ServletTestCase
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

    public TestMessageResourcesFactoryImpl() {super("TestMessageResourcesFactory");}
    public TestMessageResourcesFactoryImpl(String name) {super(name);}
    
    //
    // Class methods
    //

    //
    // Methods from TestCase
    //

    //
    // General Methods
    //

    public void testAddMethod() 
    {
        boolean gotException = false;
        MessageResourcesFactoryImpl rf = new MessageResourcesFactoryImpl();

        System.out.println("Testing addMessageResources");
        try {
            rf.addMessageResources(null,null);
        } catch ( NullPointerException fe) {
            gotException = true;
        }
        assertTrue(gotException);
        gotException = false;
        
        MessageResourcesImpl msgResource = 
	    new MessageResourcesImpl("TestResources",
				     MessageResourcesFactoryImpl.JSF_API_RESOURCE_FILENAME);
        try {
            rf.addMessageResources("TestResources", msgResource);
        } catch ( Exception fe) {
            gotException = true;
        }
        assertTrue(!gotException);
        gotException = false;
         
        // make sure it was added
        MessageResources result = rf.getMessageResources("TestResources");
        assertTrue(result == msgResource);
        
        try {
            rf.addMessageResources("TestResources", msgResource);
        } catch ( IllegalArgumentException fe) {
            gotException = true;
        }
        assertTrue(gotException);
    }
    
    public void testGetMethods() {
        boolean gotException = false;
        MessageResourcesFactoryImpl rf = new MessageResourcesFactoryImpl();

        System.out.println("Testing getMessageResources");
        try {
             MessageResources resourcesImpl = rf.getMessageResources(null);
        } catch ( NullPointerException fe) {
            gotException = true;
        }
        assertTrue(gotException);
        gotException = false;
        
        MessageResources apiMessages1 = 
            rf.getMessageResources(MessageResourcesFactory.FACES_API_MESSAGES);
        assertTrue(apiMessages1 != null );
        
        MessageResources riMessages = 
            rf.getMessageResources(MessageResourcesFactory.FACES_IMPL_MESSAGES);
        assertTrue(riMessages != null );
        
        MessageResources apiMessages2 = 
            rf.getMessageResources(MessageResourcesFactory.FACES_API_MESSAGES);
        assertTrue(apiMessages1 == apiMessages2 );
        
        System.out.println("Testing getMessageResourcesIds");
        Iterator it = rf.getMessageResourcesIds();
        
        assertTrue ( it.hasNext());
        String id1 = (String) it.next();
        assertTrue( (id1 == (MessageResourcesFactory.FACES_API_MESSAGES)) ||
                (id1 == (MessageResourcesFactory.FACES_IMPL_MESSAGES)));
       
        String id2 = (String) it.next();
        assertTrue( (id2 == (MessageResourcesFactory.FACES_API_MESSAGES)) ||
                (id2 == (MessageResourcesFactory.FACES_IMPL_MESSAGES)));
    }    

    public void testGetMethodsException() {
        boolean gotException = false;
        MessageResourcesFactoryImpl rf = new MessageResourcesFactoryImpl();

	try {
	    rf.getMessageResources("bogusString");
	}
	catch (IllegalArgumentException e) {
	    gotException = true;
	}
	assertTrue(gotException);

    }

} // end of class TestMessageResourcesFactoryImpl

/*
 * $Id: TestMessageFactoryImpl.java,v 1.2 2003/10/30 20:30:41 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestMessageFactoryImpl.java

package com.sun.faces.util;

import com.sun.faces.ServletFacesTestCase;

import org.mozilla.util.Assert;
import org.mozilla.util.ParameterCheck;

import javax.faces.component.UIViewRoot;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import com.sun.faces.util.Util;
import java.util.Iterator;
import javax.faces.FacesException;
import java.util.Locale;

/**
 *
 *  <B>TestMessageFactoryImpl</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestMessageFactoryImpl.java,v 1.2 2003/10/30 20:30:41 eburns Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class TestMessageFactoryImpl extends ServletFacesTestCase
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

    public TestMessageFactoryImpl() {super("TestMessageFactoryImpl");}
    public TestMessageFactoryImpl(String name) {super(name);}
    //
    // Class methods
    //

    //
    // Methods from TestCase
    //
    public void setUp() {
        super.setUp();
        UIViewRoot viewRoot = new UIViewRoot();
	viewRoot.setViewId("viewId");
	getFacesContext().setViewRoot(viewRoot);
     }   
    
    //
    // General Methods
    //
    
    public void testGetMethods() {
        boolean gotException = false;
        FacesMessage msg = null;
        
        FacesContext facesContext = getFacesContext();
        Assert.assert_it(facesContext != null );

        System.out.println("Testing get methods");
        try {
            msg = MessageFactory.getMessage((FacesContext) null, (String)null);
        } catch ( NullPointerException fe) {
            gotException = true;
        }
        assertTrue(gotException);
        gotException = false;
        msg = null;
        
        // if msgId doesn't exist in the resource, null must be returned
        try {
            msg = MessageFactory.getMessage(facesContext,"MSG01", "param1");
	    assertTrue(null == msg);
        } catch ( FacesException fe ) {
	    assertTrue(false);
        }    

        
        Object[] params1 = {"JavaServerFaces"};
        msg = MessageFactory.getMessage(facesContext, "MSG0001", params1);
        assertTrue ( msg != null );
        assertTrue((msg.getSummary()).equals("'JavaServerFaces' is not a valid number."));
        
        msg = MessageFactory.getMessage( facesContext,"MSG0003", "userId");
        assertTrue ( msg != null );
        assertTrue((msg.getSummary()).equals("'userId' field cannot be empty."));
        
        msg = MessageFactory.getMessage( facesContext, "MSG0004", "userId", "1000","10000");
        assertTrue ( msg != null );
        assertTrue((msg.getSummary()).equals("'userId' out of range. Value should be between '1000' and '10000'.")); 
    }

    public void testFindCatalog() {

        boolean gotException = false;
        FacesMessage msg = null;

        // if no locale is set, it should use the fall back,
        // JSFMessages.xml
        msg = MessageFactory.getMessage(getFacesContext(),"MSG0003", "userId");
        assertTrue ( msg != null );
        assertTrue((msg.getSummary()).equals("'userId' field cannot be empty."));

        // passing an invalid locale should use fall back.
        Locale en_locale = new Locale("eng", "us");
        getFacesContext().getViewRoot().setLocale(en_locale);
        System.out.println("Testing get methods");
        try {
            msg = MessageFactory.getMessage(getFacesContext(),"MSG0003", "userId");
        } catch ( Exception fe) {
            gotException = true;
        }
        assertTrue(!gotException);
        gotException = false;
        msg = null;
      
        en_locale = new Locale("en", "us"); 
        getFacesContext().getViewRoot().setLocale(en_locale);
        msg = MessageFactory.getMessage(getFacesContext(),"MSG0003", "userId");
        assertTrue ( msg != null );
        assertTrue((msg.getSummary()).equals("'userId' field cannot be empty."));
        msg = null;

    }

} // end of class TestMessageListImpl

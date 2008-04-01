/*
 * $Id: TestNavigationMap.java,v 1.1 2002/01/24 18:38:17 rogerk Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestNavigationMap.java

package com.sun.faces;

import com.sun.faces.NavigationMapImpl;

import junit.framework.TestCase;

import javax.faces.Constants;
import javax.faces.NavigationHandler;
import javax.faces.NavigationMap;

import java.io.IOException;

/**
 *
 *  <B>TestNavigationMap</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestNavigationMap.java,v 1.1 2002/01/24 18:38:17 rogerk Exp $
 * 
 *
 */

public class TestNavigationMap extends TestCase
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
    NavigationMapImpl navMap = null;

    // Attribute Instance Variables

    // Relationship Instance Variables

    //
    // Constructors and Initializers    
    //

    public TestNavigationMap(String name) {
        super(name);
    }

    public void setUp() {
        navMap = new NavigationMapImpl();
    }

    //
    // Class methods
    //

    //
    // General Methods
    //

    public void testNavigationMap() {
        boolean gotException = false;

        // Test put method
        System.out.println("Test put method - Success Test");
        try {
            navMap.put("myCommand", Constants.OUTCOME_SUCCESS,
                NavigationHandler.FORWARD, "Welcome.jsp");
            System.out.println("Successfully added entry...");

            navMap.put("myCommand", Constants.OUTCOME_FAILURE,
                NavigationHandler.REDIRECT, "Error.jsp");
            System.out.println("Successfully added entry...");
        } catch (Exception e) {
            System.out.println("Exception:"+ e.getMessage());
            gotException = true;
        }
        assertTrue(!gotException); 

        System.out.println("Test put method - Failure Test");
        gotException = false;
        try {
            navMap.put("myCommand", "FOO_OUTCOME", 
                NavigationHandler.REDIRECT, "");
        } catch (Exception e) {
            gotException = true;
            System.out.println("Expected Exception:"+ e.getMessage());
        }
        assertTrue(gotException);

        System.out.println("Test put method - Failure Test");
        gotException = false;
        try {
            navMap.put("myCommand", Constants.OUTCOME_SUCCESS, -1, "");
        } catch (Exception e) {
            gotException = true;
            System.out.println("Expected Exception:"+ e.getMessage());
        }
        assertTrue(gotException);

        System.out.println("Test put method - Failure Test");
        gotException = false;
        try {
            navMap.put("myCommand", Constants.OUTCOME_SUCCESS, 
                NavigationHandler.FORWARD, "Welcome.jsp");
        } catch (Exception e) {
            gotException = true;
            System.out.println("Expected Exception:"+ e.getMessage());
        }
        assertTrue(gotException);

        System.out.println("Test getTargetAction method..");
        gotException = false;
        try {
            int targetAction = navMap.getTargetAction("myCommand", 
                Constants.OUTCOME_SUCCESS);
            System.out.println("Found target action:"+targetAction);
        } catch (Exception e) {
            gotException = true;
            System.out.println("Unexpected Exception:"+e.getMessage());
        }
        assertTrue(!gotException);

        System.out.println("Test getTargetPath method..");
        gotException = false;
        try {
            String targetPath = navMap.getTargetPath("myCommand", 
                Constants.OUTCOME_SUCCESS);
            System.out.println("Found target path:"+targetPath);
        } catch (Exception e) {
            gotException = true;
            System.out.println("Unexpected Exception:"+e.getMessage());
        }
        assertTrue(!gotException);

        System.out.println("Test remove method - Failure Test..");
        gotException = false;
        try {
            navMap.remove("fooCommand", "fooOutcome");
        } catch (Exception e) {
            gotException = true;
            System.out.println("Expected Exception:"+e.getMessage());
        }
        assertTrue(gotException);

        System.out.println("Test remove method ..");
        gotException = false;
        try {
            navMap.remove("myCommand", Constants.OUTCOME_SUCCESS);
        } catch (Exception e) {
            gotException = true;
            System.out.println("Unexpected Exception:"+e.getMessage());
        }
        assertTrue(!gotException);
        
    }

} // end of class TestNavigationMap

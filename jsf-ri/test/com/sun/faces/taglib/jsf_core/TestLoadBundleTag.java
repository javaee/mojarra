/*
 * $Id: TestLoadBundleTag.java,v 1.14 2006/03/29 23:05:02 rlubke Exp $
 */

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

// TestLoadBundleTag.java

package com.sun.faces.taglib.jsf_core;

import com.sun.faces.cactus.ServletFacesTestCase;
import com.sun.faces.util.Util;

import javax.faces.component.UIViewRoot;
import javax.faces.application.Application;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;

import java.util.Map;


/**
 * @version $Id: TestLoadBundleTag.java,v 1.14 2006/03/29 23:05:02 rlubke Exp $
 */

public class TestLoadBundleTag extends ServletFacesTestCase {

//
// Protected Constants
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

    public TestLoadBundleTag() {
        super("TestLoadBundleTag.java");
    }


    public TestLoadBundleTag(String name) {
        super(name);
    }

//
// Class methods
//

//
// General Methods
//

    public void testLoadBundle() throws Exception {
        getFacesContext().setViewRoot(Util.getViewHandler(getFacesContext()).createView(getFacesContext(), null));
        LoadBundleTag tag = new LoadBundleTag();
        ExpressionFactory factory =
            getFacesContext().getApplication().getExpressionFactory();
        ValueExpression expr =
            factory.createValueExpression(getFacesContext().getELContext(),
                "com.sun.faces.TestMessages",String.class);
                                         
        tag.setBasename(expr);
        tag.setVar("messages");
        tag.doStartTag();
        assertEquals("Didn't get expected value",
                     ((Map) getFacesContext().getExternalContext()
                            .getRequestMap()
                            .get("messages")).get("buckaroo"),
                     "banzai");
        assertEquals("Didn't get expected value",
                     ((Map) getFacesContext().getExternalContext()
                            .getRequestMap()
                            .get("messages")).get("john"),
                     "bigboote");
        assertEquals("???notpresent???",
                     ((Map) getFacesContext().getExternalContext()
                            .getRequestMap()
                            .get("messages")).get("notpresent"));

    }


    //test out full Map contract implementation of LoadBundleTag
    public void testLoadBundleMap() throws Exception {
        boolean gotException = false;
        Object key = "buckaroo";
        Object value = "banzai";
        ExpressionFactory factory =
            getFacesContext().getApplication().getExpressionFactory();
        ValueExpression expr =
            factory.createValueExpression(getFacesContext().getELContext(),
                "com.sun.faces.TestMessages",String.class);
                                          
        getFacesContext().setViewRoot(Util.getViewHandler(getFacesContext()).createView(getFacesContext(), null));

        LoadBundleTag tag = new LoadBundleTag();
        tag.setBasename(expr);
        tag.setVar("messages");
        tag.doStartTag();
        Map testMap = (Map) getFacesContext().getExternalContext()
            .getRequestMap()
            .get("messages");

        LoadBundleTag tag2 = new LoadBundleTag();
        tag2.setBasename(expr);
        tag2.setVar("messages2");
        tag2.doStartTag();
        Map testMap2 = (Map) getFacesContext().getExternalContext()
            .getRequestMap()
            .get("messages2");

        try {
            testMap.clear();
        } catch (UnsupportedOperationException ex) {
            gotException = true;
        }
        assertTrue("Map.clear() should not be supported for immutable Map",
                   gotException);
        gotException = false;

        assertTrue("key not in Map", testMap.containsKey(key));
        assertTrue("value not in Map", testMap.containsValue(value));
        assertTrue("entrySet not correct for Map",
                   testMap.entrySet().equals(testMap2.entrySet()));
        assertTrue("Same maps are not equal", testMap.equals(testMap2));
        assertEquals("value not in Map", testMap.get(key), value);
        //two equal sets should have same hashcode
        assertTrue("HashCode not valid",
                   testMap.hashCode() == testMap2.hashCode());
        assertFalse("Map should not be empty", testMap.isEmpty());
        assertTrue("keySet not valid", testMap.keySet().contains(key));
        try {
            testMap.put(key, value);
        } catch (UnsupportedOperationException ex) {
            gotException = true;
        }
        assertTrue("Map.put() should not be supported for immutable Map",
                   gotException);
        gotException = false;

        try {
            testMap.putAll(new java.util.HashMap());
        } catch (UnsupportedOperationException ex) {
            gotException = true;
        }
        assertTrue("Map.putAll() should not be supported for immutable Map",
                   gotException);
        gotException = false;

        try {
            testMap.remove(key);
        } catch (UnsupportedOperationException ex) {
            gotException = true;
        }
        assertTrue("Map.remove() should not be supported for immutable Map",
                   gotException);
        gotException = false;

        assertTrue("Map size incorrect", testMap.size() == 4);
        assertTrue("values from Map incorrect",
                   testMap.values().contains(value));

    }

} // end of class TestLoadBundleTag

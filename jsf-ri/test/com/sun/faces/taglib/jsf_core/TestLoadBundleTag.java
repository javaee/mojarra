/*
 * $Id: TestLoadBundleTag.java,v 1.5 2004/01/27 21:06:15 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestLoadBundleTag.java

package com.sun.faces.taglib.jsf_core;

import com.sun.faces.ServletFacesTestCase;

import java.util.Map;
import javax.faces.context.FacesContext;
import javax.faces.component.UIViewRoot;


/**
 *
 * @version $Id: TestLoadBundleTag.java,v 1.5 2004/01/27 21:06:15 eburns Exp $
 * 
 *
 */

public class TestLoadBundleTag extends ServletFacesTestCase 
{
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

    public TestLoadBundleTag() {super("TestLoadBundleTag.java");}
    public TestLoadBundleTag(String name) {super(name);}

//
// Class methods
//

//
// General Methods
//

    public void testLoadBundle() throws Exception {
	getFacesContext().setViewRoot(new UIViewRoot());
	LoadBundleTag tag = new LoadBundleTag();
	tag.setBasename("com.sun.faces.TestMessages");
	tag.setVar("messages");
	tag.doStartTag();
	assertEquals("Didn't get expected value", 
		     ((Map)getFacesContext().getExternalContext().getRequestMap().get("messages")).get("buckaroo"),
		     "banzai");
	assertEquals("Didn't get expected value", 
		     ((Map)getFacesContext().getExternalContext().getRequestMap().get("messages")).get("john"),
		     "bigboote");
	assertEquals("???notpresent???",
		     ((Map)getFacesContext().getExternalContext().getRequestMap().get("messages")).get("notpresent"));

    }

    //test out full Map contract implementation of LoadBundleTag
    public void testLoadBundleMap() throws Exception {
        boolean gotException = false;
        Object key = "buckaroo";
        Object value = "banzai";
	getFacesContext().setViewRoot(new UIViewRoot());

	LoadBundleTag tag = new LoadBundleTag();
	tag.setBasename("com.sun.faces.TestMessages");
	tag.setVar("messages");
	tag.doStartTag();
        Map testMap = (Map)getFacesContext().getExternalContext().getRequestMap().get("messages");

	LoadBundleTag tag2 = new LoadBundleTag();
	tag2.setBasename("com.sun.faces.TestMessages");
	tag2.setVar("messages2");
	tag2.doStartTag();
        Map testMap2 = (Map)getFacesContext().getExternalContext().getRequestMap().get("messages2");

        try {
            testMap.clear();
        } catch (UnsupportedOperationException ex) {
            gotException = true;
        }
        assertTrue("Map.clear() should not be supported for immutable Map", gotException);
        gotException = false;

        assertTrue("key not in Map", testMap.containsKey(key));
        assertTrue("value not in Map", testMap.containsValue(value));
        assertTrue("entrySet not correct for Map", testMap.entrySet().equals(testMap2.entrySet()));
        assertTrue("Same maps are not equal", testMap.equals(testMap2));
        assertEquals("value not in Map", testMap.get(key), value);
        //two equal sets should have same hashcode
        assertTrue("HashCode not valid", testMap.hashCode() == testMap2.hashCode());
        assertFalse("Map should not be empty", testMap.isEmpty());
        assertTrue("keySet not valid", testMap.keySet().contains(key));
        try {
            testMap.put(key, value);
        } catch (UnsupportedOperationException ex) {
            gotException = true;
        }
        assertTrue("Map.put() should not be supported for immutable Map", gotException);
        gotException = false;

        try {
            testMap.putAll(new java.util.HashMap());
        } catch (UnsupportedOperationException ex) {
            gotException = true;
        }
        assertTrue("Map.putAll() should not be supported for immutable Map", gotException);
        gotException = false;

        try {
            testMap.remove(key);
        } catch (UnsupportedOperationException ex) {
            gotException = true;
        }
        assertTrue("Map.remove() should not be supported for immutable Map", gotException);
        gotException = false;

        assertTrue("Map size incorrect", testMap.size() == 4);
        assertTrue("values from Map incorrect", testMap.values().contains(value));

    }

} // end of class TestLoadBundleTag

/*
 * $Id: TestLoadBundleTag.java,v 1.2 2003/12/17 15:15:44 rkitain Exp $
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
 *  <B>TestLoadBundleTag.java</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestLoadBundleTag.java,v 1.2 2003/12/17 15:15:44 rkitain Exp $
 * 
 * @see	Blah
 * @see	Bloo
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

    }

} // end of class TestLoadBundleTag

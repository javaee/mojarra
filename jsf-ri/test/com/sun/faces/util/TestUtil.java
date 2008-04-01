/*
 * $Id: TestUtil.java,v 1.2 2002/08/09 20:13:10 eburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestUtil.java

package com.sun.faces.util;

import org.mozilla.util.Assert;
import org.mozilla.util.ParameterCheck;

import com.sun.faces.ServletFacesTestCase;

import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;

/**
 *
 *  <B>TestUtil</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestUtil.java,v 1.2 2002/08/09 20:13:10 eburns Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class TestUtil extends ServletFacesTestCase
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

    public TestUtil() {super("TestUtil");}
    public TestUtil(String name) {super(name);}

//
// Class methods
//

//
// General Methods
//

    public void testRenderPassthruAttributes() {
	UIInput input = new UIInput();
	input.setComponentId("testRenderPassthruAttributes");
	input.setAttribute("notPresent", "notPresent");
	input.setAttribute("onblur", "javascript:f.blur()");
	String result = Util.renderPassthruAttributes(getFacesContext(),
						      input);
	String expectedResult = "onblur=\"javascript:f.blur()\" ";
	assertTrue(result.equals(expectedResult));
    }


} // end of class TestUtil

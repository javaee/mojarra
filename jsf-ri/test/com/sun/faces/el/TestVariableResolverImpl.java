/*
 * $Id: TestVariableResolverImpl.java,v 1.2 2003/03/24 19:45:38 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestVariableResolverImpl.java

package com.sun.faces.el;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.ParameterCheck;

import com.sun.faces.ServletFacesTestCase;
import com.sun.faces.TestBean;
import com.sun.faces.TestBean.InnerBean;
import com.sun.faces.TestBean.Inner2Bean;

import org.apache.cactus.WebRequest;

import javax.faces.el.VariableResolver;
import javax.faces.component.UINamingContainer;


/**
 *
 *  <B>TestVariableResolverImpl</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestVariableResolverImpl.java,v 1.2 2003/03/24 19:45:38 eburns Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class TestVariableResolverImpl extends ServletFacesTestCase
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

    public TestVariableResolverImpl() {super("TestFacesContext");}
    public TestVariableResolverImpl(String name) {super(name);}
//
// Class methods
//

//
// Methods from TestCase
//

//
// General Methods
//

    public void testScopedLookup() {
	TestBean testBean = new TestBean();
	InnerBean newInner, oldInner = new InnerBean();
	testBean.setInner(oldInner);
	VariableResolver variableResolver = new VariableResolverImpl();
	Object result = null;
	getFacesContext().getExternalContext().getSessionMap().remove("TestBean");

	//
	// Get tests
	//

	// application
	getFacesContext().getExternalContext().getApplicationMap().put("TestBean", 
								       testBean);
	result = variableResolver.resolveVariable(getFacesContext(), 
						  "TestBean");
	assertTrue(result == testBean);
	getFacesContext().getExternalContext().getApplicationMap().remove("TestBean");
	// session
	getFacesContext().getExternalContext().getSessionMap().put("TestBean", 
								       testBean);
	result = variableResolver.resolveVariable(getFacesContext(), 
						  "TestBean");
	assertTrue(result == testBean);
	getFacesContext().getExternalContext().getSessionMap().remove("TestBean");

	// session
	getFacesContext().getExternalContext().getRequestMap().put("TestBean", 
								       testBean);

	result = variableResolver.resolveVariable(getFacesContext(), 
						  "TestBean");
	assertTrue(result == testBean);
	getFacesContext().getExternalContext().getRequestMap().remove("TestBean");

    }

    public void testImplicitObjects() {
	VariableResolver variableResolver = new VariableResolverImpl();
	Object result = null;

	//
	// test scope maps
	//

	// ApplicationMap
	assertTrue(variableResolver.resolveVariable(getFacesContext(),
						    "applicationScope") ==
		   getFacesContext().getExternalContext().getApplicationMap());

	// SessionMap
	assertTrue(variableResolver.resolveVariable(getFacesContext(),
						    "sessionScope") ==
		   getFacesContext().getExternalContext().getSessionMap());

	// RequestMap
	assertTrue(variableResolver.resolveVariable(getFacesContext(),
						    "requestScope") ==
		   getFacesContext().getExternalContext().getRequestMap());

	// 
	// test request objects
	//

	// cookie
	assertTrue(variableResolver.resolveVariable(getFacesContext(),
						    "cookie") ==
		   getFacesContext().getExternalContext().getRequestCookieMap());

	// header
	assertTrue(variableResolver.resolveVariable(getFacesContext(),
						    "header") ==
		   getFacesContext().getExternalContext().getRequestHeaderMap());

	// headerValues
	assertTrue(variableResolver.resolveVariable(getFacesContext(),
						    "headerValues") ==
		   getFacesContext().getExternalContext().getRequestHeaderValuesMap());

	// parameter
	assertTrue(variableResolver.resolveVariable(getFacesContext(),
						    "param") ==
		   getFacesContext().getExternalContext().getRequestParameterMap());

	// parameterValues
	assertTrue(variableResolver.resolveVariable(getFacesContext(),
						    "paramValues") ==
		   getFacesContext().getExternalContext().getRequestParameterValuesMap());

	//
	// misc
	//

	// initParameter
	assertTrue(variableResolver.resolveVariable(getFacesContext(),
						    "initParam") ==
		   getFacesContext().getExternalContext().getInitParameterMap());
	
	
	// facesContext
	assertTrue(variableResolver.resolveVariable(getFacesContext(),
						    "facesContext") ==
		   getFacesContext());

	// tree
	// create a dummy root for the tree.
	UINamingContainer root = new UINamingContainer() {
                public String getComponentType() { return "root"; }
            };
	root.setComponentId("root");
	getFacesContext().setTree(new com.sun.faces.tree.SimpleTreeImpl(getFacesContext(), root, "newTree"));
	
	assertTrue(variableResolver.resolveVariable(getFacesContext(),
						    "tree") ==
		   getFacesContext().getTree().getRoot());

	
    }

    // Negative tests (should throw exceptions)
    public void testNegative() throws Exception {
	VariableResolver variableResolver = new VariableResolverImpl();

        Object value = null;

        // ---------- NullPointerException Returns ----------

        try {
            value = variableResolver.resolveVariable(getFacesContext(), null);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException e) {
            ; // Expected result
        }
            
        try {
            value = variableResolver.resolveVariable(null, "foo");
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException e) {
            ; // Expected result
        }
            
        try {
            value = variableResolver.resolveVariable(null, null);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException e) {
            ; // Expected result
        }


    }


} // end of class TestVariableResolverImpl

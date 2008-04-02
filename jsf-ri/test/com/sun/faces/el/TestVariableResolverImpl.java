/*
 * $Id: TestVariableResolverImpl.java,v 1.15 2004/02/26 20:34:25 eburns Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestVariableResolverImpl.java

package com.sun.faces.el;

import com.sun.faces.ServletFacesTestCase;
import com.sun.faces.TestBean;
import com.sun.faces.TestBean.InnerBean;
import com.sun.faces.application.ApplicationImpl;
import com.sun.faces.config.ManagedBeanFactory;
import com.sun.faces.config.beans.ManagedBeanBean;
import org.apache.cactus.WebRequest;

import javax.faces.FactoryFinder;
import javax.faces.application.ApplicationFactory;
import javax.faces.component.UIViewRoot;
import javax.faces.el.VariableResolver;


/**
 * <B>TestVariableResolverImpl</B> is a class ...
 * <p/>
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestVariableResolverImpl.java,v 1.15 2004/02/26 20:34:25 eburns Exp $
 */

public class TestVariableResolverImpl extends ServletFacesTestCase {

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

    public TestVariableResolverImpl() {
        super("TestFacesContext");
    }


    public TestVariableResolverImpl(String name) {
        super(name);
    }
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
        getFacesContext().getExternalContext().getSessionMap().remove(
            "TestBean");

        //
        // Get tests
        //

        // application
        getFacesContext().getExternalContext().getApplicationMap().put(
            "TestBean",
            testBean);
        result = variableResolver.resolveVariable(getFacesContext(),
                                                  "TestBean");
        assertTrue(result == testBean);
        getFacesContext().getExternalContext().getApplicationMap().remove(
            "TestBean");
        // session
        getFacesContext().getExternalContext().getSessionMap().put("TestBean",
                                                                   testBean);
        result = variableResolver.resolveVariable(getFacesContext(),
                                                  "TestBean");
        assertTrue(result == testBean);
        getFacesContext().getExternalContext().getSessionMap().remove(
            "TestBean");

        // session
        getFacesContext().getExternalContext().getRequestMap().put("TestBean",
                                                                   testBean);

        result = variableResolver.resolveVariable(getFacesContext(),
                                                  "TestBean");
        assertTrue(result == testBean);
        getFacesContext().getExternalContext().getRequestMap().remove(
            "TestBean");

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
        assertTrue(
            variableResolver.resolveVariable(getFacesContext(),
                                             "headerValues") ==
            getFacesContext().getExternalContext().getRequestHeaderValuesMap());

        // parameter
        assertTrue(variableResolver.resolveVariable(getFacesContext(),
                                                    "param") ==
                   getFacesContext().getExternalContext()
                   .getRequestParameterMap());

        // parameterValues
        assertTrue(
            variableResolver.resolveVariable(getFacesContext(),
                                             "paramValues") ==
            getFacesContext().getExternalContext()
            .getRequestParameterValuesMap());

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
        UIViewRoot page = new UIViewRoot();
        page.setId("root");
        page.setViewId("newTree");
        getFacesContext().setViewRoot(page);

        assertTrue(variableResolver.resolveVariable(getFacesContext(),
                                                    "view") ==
                   getFacesContext().getViewRoot());


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


    /**
     * This test verifies that if the variable resolver does not find a
     * managed bean it tries to instantiate it if it was added to the
     * application's managed bean factory list.
     */
    public void testManagedBean() throws Exception {
        String beanName = "com.sun.faces.TestBean";

        ManagedBeanBean cmb = new ManagedBeanBean();

        cmb.setManagedBeanClass(beanName);
        cmb.setManagedBeanScope("session");

        ManagedBeanFactory mbf = new ManagedBeanFactory(cmb);

        ApplicationFactory aFactory = (ApplicationFactory) FactoryFinder.getFactory(
            FactoryFinder.APPLICATION_FACTORY);
        ApplicationImpl application = (ApplicationImpl) aFactory.getApplication();
        application.addManagedBeanFactory(beanName, mbf);

        VariableResolver variableResolver = application.getVariableResolver();

        Object result = variableResolver.resolveVariable(getFacesContext(),
                                                         beanName);

        assertTrue(result instanceof TestBean);
    }

} // end of class TestVariableResolverImpl

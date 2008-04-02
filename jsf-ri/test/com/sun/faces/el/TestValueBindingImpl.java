/*
 * $Id: TestValueBindingImpl.java,v 1.32 2004/05/07 13:53:25 eburns Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestValueBindingImpl.java

package com.sun.faces.el;

import com.sun.faces.ServletFacesTestCase;
import com.sun.faces.TestBean;
import com.sun.faces.TestBean.Inner2Bean;
import com.sun.faces.TestBean.InnerBean;
import com.sun.faces.application.ApplicationImpl;
import com.sun.faces.util.Util;
import org.apache.cactus.WebRequest;

import javax.faces.FacesException;
import javax.faces.component.StateHolder;
import javax.faces.component.UIForm;
import javax.faces.component.UIInput;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.el.EvaluationException;
import javax.faces.el.PropertyNotFoundException;
import javax.faces.el.ReferenceSyntaxException;
import javax.faces.el.ValueBinding;
import javax.servlet.http.HttpServletRequest;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * <B>TestValueBindingImpl</B> is a class ...
 * <p/>
 * <B>Lifetime And Scope</B> <P>
 */

public class TestValueBindingImpl extends ServletFacesTestCase {

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

    public TestValueBindingImpl() {
        super("TestValueBindingImpl");
    }


    public TestValueBindingImpl(String name) {
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

    public void populateRequest(WebRequest theRequest) {
        theRequest.addHeader("ELHeader", "ELHeader");
        theRequest.addHeader("multiheader", "1");
        theRequest.addHeader("multiheader", "2");
        theRequest.addParameter("ELParam", "ELParam");
        theRequest.addParameter("multiparam", "one");
        theRequest.addParameter("multiparam", "two");
        theRequest.addCookie("cookie", "monster");
    }


    public void beginELGet(WebRequest theRequest) {
        populateRequest(theRequest);
    }


    public void testELGet() {
        TestBean testBean = new TestBean();
        InnerBean newInner, oldInner = new InnerBean();
        testBean.setInner(oldInner);
        ValueBindingImpl valueBinding = new ValueBindingImpl(
            getFacesContext().getApplication());
        Object result = null;
        ExternalContext extContext = getFacesContext().getExternalContext();

        Map myMap = new HashMap();
        TestBean myBean = new TestBean();
        myBean.setOne("one");
        myMap.put("myBean", myBean);
        extContext.getRequestMap().put("myMap", myMap);

        //
        // Get tests
        //
        
        valueBinding.setRef("myMap.myBean.one");
        result = valueBinding.getValue(getFacesContext());
        assertTrue("one".equals(result));

        valueBinding.setRef("myMap[\"myBean\"].one");
        result = valueBinding.getValue(getFacesContext());
        assertTrue("one".equals(result));

        valueBinding.setRef("myMap.myBean['one']");
        result = valueBinding.getValue(getFacesContext());
        assertTrue("one".equals(result));

        // Simple tests, verify that bracket and dot operators work
        valueBinding.setRef("TestBean.inner");
        getFacesContext().getExternalContext().getSessionMap().put("TestBean",
                                                                   testBean);
        result = valueBinding.getValue(getFacesContext());
        assertTrue(result == oldInner);

        valueBinding.setRef("TestBean[\"inner\"]");
        result = valueBinding.getValue(getFacesContext());
        assertTrue(result == oldInner);

        valueBinding.setRef("TestBean[\"inner\"].customers[1]");
        result = valueBinding.getValue(getFacesContext());
        assertTrue(result instanceof String);
        assertTrue(result.equals("Jerry"));

        // try out the implicit objects
        valueBinding.setRef("sessionScope.TestBean.inner");
        result = valueBinding.getValue(getFacesContext());
        assertTrue(result == oldInner);

        valueBinding.setRef("header.ELHeader");
        result = valueBinding.getValue(getFacesContext());
        assertTrue(requestsHaveSameAttributeSet(
            (HttpServletRequest) getFacesContext().getExternalContext()
                                 .getRequest(),
            (HttpServletRequest) request));
        assertTrue(request.getHeader("ELHeader").equals("ELHeader"));
        assertTrue(result.equals("ELHeader"));

        valueBinding.setRef("param.ELParam");
        result = valueBinding.getValue(getFacesContext());
        assertTrue(result.equals("ELParam"));

        String multiparam[] = null;
        valueBinding.setRef("paramValues.multiparam");
        multiparam = (String[])
            valueBinding.getValue(getFacesContext());
        assertTrue(null != multiparam);
        assertTrue(2 == multiparam.length);
        assertTrue(multiparam[0].equals("one"));
        assertTrue(multiparam[1].equals("two"));

        valueBinding.setRef("headerValues.multiheader");
        Enumeration multiHeader = (Enumeration)
            valueBinding.getValue(getFacesContext());
        assertTrue(null != multiHeader);
        int elements = 0;
        while (multiHeader.hasMoreElements()) {
            elements++;
            // PENDING(edburns): due to an apparent bug in cactus, multiple
            // calls to WebRequest.addHeader() still result in all the values
            // being concatenated together into a comma separated String.
            String element = (String) multiHeader.nextElement();
            assertTrue(element.equals("1,2"));
        }
        assertTrue(1 == elements);

        valueBinding.setRef("initParam.testInitParam");
        result = valueBinding.getValue(getFacesContext());
        assertTrue(null != result);
        assertTrue(result.equals("testInitParam"));
	
        // PENDING(craigmcc) - Comment out this test because on my platform
        // the getRequestCookies() call returns null
        /*
	valueBinding.setRef("cookie.cookie");
	result = valueBinding.getValue(getFacesContext());
	assertTrue(null != result);
	assertTrue(result instanceof Cookie);
	assertTrue(((Cookie) result).getValue().equals("monster"));
        */

    }


    public void beginELSet(WebRequest theRequest) {
        populateRequest(theRequest);
    }


    public void testELSet() {
        TestBean testBean = new TestBean();
        InnerBean newInner, oldInner = new InnerBean();
        testBean.setInner(oldInner);
        ValueBindingImpl valueBinding = new ValueBindingImpl(
            getFacesContext().getApplication());
        Object result = null;
        ExternalContext extContext = getFacesContext().getExternalContext();

        Map myMap = new HashMap();
        TestBean myBean = new TestBean();
        myMap.put("myBean", myBean);
        extContext.getRequestMap().put("myMap", myMap);

        //
        // Set tests
        //
        valueBinding.setRef("myMap.myBean.one");
        valueBinding.setValue(getFacesContext(), "one");
        Map map = (Map) extContext.getRequestMap().get("myMap");
        assertTrue("one".equals(((TestBean) map.get("myBean")).getOne()));
        myBean = new TestBean();
        map.put("myBean", myBean);
        extContext.getRequestMap().put("myMap", myMap);

        // test that we can set null as the value
        valueBinding.setRef("myMap.myBean.prop");
        valueBinding.setValue(getFacesContext(), null);
        map = (Map) extContext.getRequestMap().get("myMap");
        assertEquals(null, ((TestBean) map.get("myBean")).getOne());
        myBean = new TestBean();
        map.put("myBean", myBean);
        extContext.getRequestMap().put("myMap", myMap);


        valueBinding.setRef("myMap[\"myBean\"].one");
        valueBinding.setValue(getFacesContext(), "one");
        map = (Map) extContext.getRequestMap().get("myMap");
        assertTrue("one".equals(((TestBean) map.get("myBean")).getOne()));
        myBean = new TestBean();
        map.put("myBean", myBean);
        extContext.getRequestMap().put("myMap", myMap);

        // test that we can set the property to null
        valueBinding.setRef("myMap[\"myBean\"].prop");
        valueBinding.setValue(getFacesContext(), null);
        map = (Map) extContext.getRequestMap().get("myMap");
        assertEquals(null, ((TestBean) map.get("myBean")).getProp());
        myBean = new TestBean();
        map.put("myBean", myBean);
        extContext.getRequestMap().put("myMap", myMap);

        valueBinding.setRef("myMap.myBean['one']");
        valueBinding.setValue(getFacesContext(), "one");
        map = (Map) extContext.getRequestMap().get("myMap");
        assertTrue("one".equals(((TestBean) map.get("myBean")).getOne()));
        myBean = new TestBean();
        map.put("myBean", myBean);
        extContext.getRequestMap().put("myMap", myMap);

        // set the prop to null
        valueBinding.setRef("myMap.myBean['prop']");
        valueBinding.setValue(getFacesContext(), null);
        map = (Map) extContext.getRequestMap().get("myMap");
        assertEquals(null, ((TestBean) map.get("myBean")).getOne());
        myBean = new TestBean();
        map.put("myBean", myBean);
        extContext.getRequestMap().put("myMap", myMap);


        valueBinding.setRef("NonExist");
        valueBinding.setValue(getFacesContext(), "value");
        result = extContext.getRequestMap().get("NonExist");
        assertTrue("value".equals(result));
        extContext.getRequestMap().remove("NonExist");

        extContext.getSessionMap().put("Key", "oldValue");
        valueBinding.setRef("Key");
        valueBinding.setValue(getFacesContext(), "newValue");
        result = extContext.getSessionMap().get("Key");
        assertTrue("newValue".equals(result));
        extContext.getSessionMap().remove("Key");

        newInner = new InnerBean();
        valueBinding.setRef("TestBean.inner");
        valueBinding.setValue(getFacesContext(), newInner);
        result = valueBinding.getValue(getFacesContext());
        assertTrue(result == newInner);
        assertTrue(oldInner != newInner);

        oldInner = newInner;
        newInner = new InnerBean();
        valueBinding.setRef("TestBean[\"inner\"]");
        valueBinding.setValue(getFacesContext(), newInner);
        result = valueBinding.getValue(getFacesContext());
        assertTrue(result == newInner);
        assertTrue(oldInner != newInner);

        String
            oldCustomer0 = null,
            oldCustomer1 = null,
            customer0 = null,
            customer1 = null;

        valueBinding.setRef("TestBean[\"inner\"].customers[0]");
        oldCustomer0 = customer0 =
            (String) valueBinding.getValue(getFacesContext());
        valueBinding.setRef("TestBean[\"inner\"].customers[1]");
        oldCustomer1 = customer1 =
            (String) valueBinding.getValue(getFacesContext());

        valueBinding.setRef("TestBean[\"inner\"].customers[0]");
        valueBinding.setValue(getFacesContext(), "Jerry");
        valueBinding.setRef("TestBean[\"inner\"].customers[1]");
        valueBinding.setValue(getFacesContext(), "Mickey");

        valueBinding.setRef("TestBean[\"inner\"].customers[0]");
        customer0 = (String) valueBinding.getValue(getFacesContext());
        valueBinding.setRef("TestBean[\"inner\"].customers[1]");
        customer1 = (String) valueBinding.getValue(getFacesContext());
        assertTrue(customer0.equals("Jerry"));
        assertTrue(customer1.equals("Mickey"));

        valueBinding.setRef("TestBean[\"inner\"].customers[0]");
        assertTrue(valueBinding.getValue(getFacesContext()) != oldCustomer0);
        valueBinding.setRef("TestBean[\"inner\"].customers[1]");
        assertTrue(valueBinding.getValue(getFacesContext()) != oldCustomer1);

        // put in a map to the customers Collection
        Inner2Bean inner2 = new Inner2Bean();
        assertTrue(null == inner2.getNicknames().get("foo"));
        valueBinding.setRef("TestBean[\"inner\"].customers[2]");
        valueBinding.setValue(getFacesContext(), inner2);
        valueBinding.setRef("TestBean[\"inner\"].customers[2]");
        assertTrue(valueBinding.getValue(getFacesContext()) == inner2);

        valueBinding.setRef("TestBean[\"inner\"].customers[2].nicknames.foo");
        valueBinding.setValue(getFacesContext(), "bar");
        assertTrue(((String) inner2.getNicknames().get("foo")).equals("bar"));

        // PENDING(edburns): test set with implicit objects.

    }


    public void testReadOnly_singleCase() {
        ValueBindingImpl valueBinding = new ValueBindingImpl(
            getFacesContext().getApplication());

        // these are mutable Maps
        valueBinding.setRef("applicationScope");
        assertTrue(!valueBinding.isReadOnly(getFacesContext()));
        valueBinding.setRef("sessionScope");
        assertTrue(!valueBinding.isReadOnly(getFacesContext()));
        valueBinding.setRef("requestScope");
        assertTrue(!valueBinding.isReadOnly(getFacesContext()));

        // these are immutable Maps
        valueBinding.setRef("param");
        assertTrue(valueBinding.isReadOnly(getFacesContext()));
        valueBinding.setRef("paramValues");
        assertTrue(valueBinding.isReadOnly(getFacesContext()));
        valueBinding.setRef("header");
        assertTrue(valueBinding.isReadOnly(getFacesContext()));
        valueBinding.setRef("headerValues");
        assertTrue(valueBinding.isReadOnly(getFacesContext()));
        valueBinding.setRef("cookie");
        assertTrue(valueBinding.isReadOnly(getFacesContext()));
        valueBinding.setRef("initParam");
        assertTrue(valueBinding.isReadOnly(getFacesContext()));
    }


    public void testReadOnly_multipleCase() {
        ValueBindingImpl valueBinding = new ValueBindingImpl(
            getFacesContext().getApplication());

        // these are mutable Maps
        valueBinding.setRef("applicationScope.value");
        assertTrue(!valueBinding.isReadOnly(getFacesContext()));
        valueBinding.setRef("sessionScope.value");
        assertTrue(!valueBinding.isReadOnly(getFacesContext()));
        valueBinding.setRef("requestScope.value");
        assertTrue(!valueBinding.isReadOnly(getFacesContext()));

        // these are immutable Maps
        valueBinding.setRef("param.value");
        assertTrue(valueBinding.isReadOnly(getFacesContext()));
        valueBinding.setRef("paramValues.value");
        assertTrue(valueBinding.isReadOnly(getFacesContext()));
        valueBinding.setRef("header.value");
        assertTrue(valueBinding.isReadOnly(getFacesContext()));
        valueBinding.setRef("headerValues.value");
        assertTrue(valueBinding.isReadOnly(getFacesContext()));
        valueBinding.setRef("cookie.value");
        assertTrue(valueBinding.isReadOnly(getFacesContext()));
        valueBinding.setRef("initParam.value");
        assertTrue(valueBinding.isReadOnly(getFacesContext()));

        // tree
        // create a dummy root for the tree.
        UIViewRoot page = Util.getViewHandler(getFacesContext()).createView(getFacesContext(), null);
        page.setId("root");
        page.setViewId("newTree");
        getFacesContext().setViewRoot(page);
        valueBinding.setRef("view.childCount");
        assertTrue(valueBinding.isReadOnly(getFacesContext()));

        TestBean testBean = (TestBean) getFacesContext().getExternalContext()
            .getSessionMap()
            .get("TestBean");
        assertTrue(null != testBean);
        valueBinding.setRef("TestBean.readOnly");
        assertTrue(valueBinding.isReadOnly(getFacesContext()));
        valueBinding.setRef("TestBean.one");
        assertTrue(!valueBinding.isReadOnly(getFacesContext()));

        InnerBean inner = new InnerBean();
        testBean.setInner(inner);
        valueBinding.setRef("TestBean[\"inner\"].customers[1]");
        assertTrue(!valueBinding.isReadOnly(getFacesContext()));


    }


    public void testGetType_singleCase() {
        ValueBindingImpl valueBinding = new ValueBindingImpl(
            getFacesContext().getApplication());

        // these are mutable Maps
        valueBinding.setRef("applicationScope");
        assertTrue(
            Map.class.isAssignableFrom(valueBinding.getType(getFacesContext())));
        valueBinding.setRef("sessionScope");
        assertTrue(
            Map.class.isAssignableFrom(valueBinding.getType(getFacesContext())));
        valueBinding.setRef("requestScope");
        assertTrue(
            Map.class.isAssignableFrom(valueBinding.getType(getFacesContext())));

        // these are immutable Maps
        valueBinding.setRef("param");
        assertTrue(
            Map.class.isAssignableFrom(valueBinding.getType(getFacesContext())));
        valueBinding.setRef("paramValues");
        assertTrue(
            Map.class.isAssignableFrom(valueBinding.getType(getFacesContext())));
        valueBinding.setRef("header");
        assertTrue(
            Map.class.isAssignableFrom(valueBinding.getType(getFacesContext())));
        valueBinding.setRef("headerValues");
        assertTrue(
            Map.class.isAssignableFrom(valueBinding.getType(getFacesContext())));
        valueBinding.setRef("cookie");
        assertTrue(
            Map.class.isAssignableFrom(valueBinding.getType(getFacesContext())));
        valueBinding.setRef("initParam");
        assertTrue(
            Map.class.isAssignableFrom(valueBinding.getType(getFacesContext())));
    }


    public void beginGetType_multipleCase(WebRequest theRequest) {
        populateRequest(theRequest);
    }


    public void testGetType_multipleCase() {
        ValueBindingImpl valueBinding = new ValueBindingImpl(
            getFacesContext().getApplication());
        String property = "testValueBindingImpl_property";
        getFacesContext().getExternalContext().getApplicationMap().put(
            property,
            property);

        getFacesContext().getExternalContext().getSessionMap().put(property,
                                                                   property);

        getFacesContext().getExternalContext().getRequestMap().put(property,
                                                                   property);

        // these are mutable Maps
        valueBinding.setRef("applicationScope." + property);
        assertTrue(
            valueBinding.getType(getFacesContext()).getName().equals(
                "java.lang.String"));
        valueBinding.setRef("sessionScope." + property);
        assertTrue(
            valueBinding.getType(getFacesContext()).getName().equals(
                "java.lang.String"));
        valueBinding.setRef("requestScope." + property);
        assertTrue(
            valueBinding.getType(getFacesContext()).getName().equals(
                "java.lang.String"));

        // these are immutable Maps
        valueBinding.setRef("param." + "ELParam");
        assertTrue(
            valueBinding.getType(getFacesContext()).getName().equals(
                "java.lang.String"));
        valueBinding.setRef("paramValues.multiparam");
        assertTrue(
            valueBinding.getType(getFacesContext()).getName().equals(
                "[Ljava.lang.String;"));

        valueBinding.setRef("header.ELHeader");
        assertTrue(
            valueBinding.getType(getFacesContext()).getName().equals(
                "java.lang.String"));
        valueBinding.setRef("headerValues.multiheader");
        assertTrue(
            java.util.Enumeration.class.isAssignableFrom(
                valueBinding.getType(getFacesContext())));
        // PENDING(craigmcc) - Comment out this test because on my platform
        // the getRequestCookies() call returns null
        /*
	valueBinding.setRef("cookie.cookie");
	assertTrue(valueBinding.getType(getFacesContext()).getName().equals("javax.servlet.http.Cookie"));
        */
        valueBinding.setRef("initParam['javax.faces.STATE_SAVING_METHOD']");
        assertTrue(
            valueBinding.getType(getFacesContext()).getName().equals(
                "java.lang.String"));

        // tree
        // create a dummy root for the tree.
        UIViewRoot page = Util.getViewHandler(getFacesContext()).createView(getFacesContext(), null);
        page.setId("root");
        page.setViewId("newTree");
        getFacesContext().setViewRoot(page);
        valueBinding.setRef("view");
        Class c = valueBinding.getType(getFacesContext());
        assertTrue(javax.faces.component.UIComponent.class.isAssignableFrom(c));

        TestBean testBean = (TestBean) getFacesContext().getExternalContext()
            .getSessionMap()
            .get("TestBean");
        assertTrue(null != testBean);
        valueBinding.setRef("TestBean.readOnly");
        assertTrue(
            valueBinding.getType(getFacesContext()).getName().equals(
                "java.lang.String"));
        valueBinding.setRef("TestBean.one");
        assertTrue(
            valueBinding.getType(getFacesContext()).getName().equals(
                "java.lang.String"));

        InnerBean inner = new InnerBean();
        testBean.setInner(inner);
        valueBinding.setRef("TestBean[\"inner\"].customers[1]");
        assertTrue(
            valueBinding.getType(getFacesContext()).getName().equals(
                "java.lang.String"));

        valueBinding.setRef("TestBean[\"inner\"]");
        assertTrue(
            valueBinding.getType(getFacesContext()).getName().equals(
                "com.sun.faces.TestBean$InnerBean"));

        int[] intArray = {1, 2, 3};
        getFacesContext().getExternalContext().getRequestMap().put("intArray",
                                                                   intArray);
        valueBinding.setRef("requestScope.intArray");
        assertTrue(
            valueBinding.getType(getFacesContext()).getName().equals("[I"));
    }


    public void testGetScopePositive() {
        ValueBindingImpl valueBinding = new ValueBindingImpl(
            getFacesContext().getApplication());
        TestBean testBean = new TestBean();
        getFacesContext().getExternalContext().getApplicationMap().
            put("TestApplicationBean", testBean);

        valueBinding.setRef("TestApplicationBean");
        assertEquals("application",
                     Util.getScope("TestApplicationBean", null));

        valueBinding.setRef("TestApplicationBean.one");
        assertEquals("application",
                     Util.getScope("TestApplicationBean.one", null));

        valueBinding.setRef("TestApplicationBean.inner.two");
        assertEquals("application",
                     Util.getScope("TestApplicationBean.inner.two", null));

        valueBinding.setRef("applicationScope.TestApplicationBean");
        assertEquals("application",
                     Util.getScope(
                         "applicationScope.TestApplicationBean", null));
        valueBinding.setRef("applicationScope.TestApplicationBean.inner.two");
        assertEquals("application",
                     Util.getScope(
                         "applicationScope.TestApplicationBean.inner.two", null));

        getFacesContext().getExternalContext().getSessionMap().
            put("TestSessionBean", testBean);
        valueBinding.setRef("TestSessionBean");
        assertEquals("session",
                     Util.getScope("TestSessionBean", null));

        valueBinding.setRef("TestSessionBean.one");
        assertEquals("session",
                     Util.getScope("TestSessionBean.one", null));

        valueBinding.setRef("TestSessionBean.inner.two");
        assertEquals("session",
                     Util.getScope("TestSessionBean.inner.two", null));

        valueBinding.setRef("sessionScope.TestSessionBean");
        assertEquals("session",
                     Util.getScope("sessionScope.TestSessionBean", null));

        valueBinding.setRef("sessionScope.TestSessionBean.inner.two");
        assertEquals("session",
                     Util.getScope(
                         "sessionScope.TestSessionBean.inner.two", null));

        getFacesContext().getExternalContext().getRequestMap().
            put("TestRequestBean", testBean);
        valueBinding.setRef("TestRequestBean");
        assertEquals("request",
                     Util.getScope("TestRequestBean", null));

        valueBinding.setRef("TestRequestBean.one");
        assertEquals("request",
                     Util.getScope("TestRequestBean.one", null));

        valueBinding.setRef("TestRequestBean.inner.two");
        assertEquals("request",
                     Util.getScope("TestRequestBean.inner.two", null));

        valueBinding.setRef("requestScope.TestRequestBean");
        assertEquals("request",
                     Util.getScope("requestScope.TestRequestBean", null));

        valueBinding.setRef("requestScope.TestRequestBean.inner.two");
        assertEquals("request",
                     Util.getScope(
                         "requestScope.TestRequestBean.inner.two", null));

        valueBinding.setRef("TestNoneBean");
        assertNull(Util.getScope("TestNoneBean", null));

        valueBinding.setRef("TestNoneBean.one");
        assertNull(Util.getScope("TestNoneBean.one", null));
        valueBinding.setRef("TestNoneBean.inner.two");
        assertNull(Util.getScope("TestNoneBean.inner.two", null));

    }


    public void testGetScopeNegative() {
        ValueBindingImpl valueBinding = new ValueBindingImpl(
            getFacesContext().getApplication());
        String property = null;

        property = "[]";
        valueBinding.setRef(property);
        assertNull(Util.getScope(property, null));
        property = "][";
        assertNull(Util.getScope(property, null));
        property = "";
        assertNull(Util.getScope(property, null));
        property = null;
        assertNull(Util.getScope(property, null));
        property = "foo.sessionScope";
        assertNull(Util.getScope(property, null));
        property = "sessionScope";
        assertNull(Util.getScope(property, null));
        property = "sessionScope[";
        assertNull(Util.getScope(property, null));

    }


    // negative test for case when valueRef is merely
    // one of the reserved scope names.
    public void testReservedScopeIdentifiers() {
        ValueBindingImpl valueBinding = new ValueBindingImpl(
            getFacesContext().getApplication());
        boolean exceptionThrown = false;

        try {
            valueBinding.setRef("applicationScope");
            valueBinding.setValue(getFacesContext(), "value");
        } catch (ReferenceSyntaxException rse) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);

        exceptionThrown = false;
        try {
            valueBinding.setRef("sessionScope");
            valueBinding.setValue(getFacesContext(), "value");
        } catch (ReferenceSyntaxException rse) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);

        exceptionThrown = false;
        try {
            valueBinding.setRef("requestScope");
            valueBinding.setValue(getFacesContext(), "value");
        } catch (ReferenceSyntaxException rse) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);

        exceptionThrown = false;
        try {
            valueBinding.setRef("facesContext");
            valueBinding.setValue(getFacesContext(), "value");
        } catch (ReferenceSyntaxException rse) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);

        exceptionThrown = false;
        try {
            valueBinding.setRef("cookies");
            valueBinding.setValue(getFacesContext(), "value");
        } catch (ReferenceSyntaxException rse) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);

        exceptionThrown = false;
        try {
            valueBinding.setRef("header");
            valueBinding.setValue(getFacesContext(), "value");
        } catch (ReferenceSyntaxException rse) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);

        exceptionThrown = false;
        try {
            valueBinding.setRef("headerValues");
            valueBinding.setValue(getFacesContext(), "value");
        } catch (ReferenceSyntaxException rse) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);

        exceptionThrown = false;
        try {
            valueBinding.setRef("initParam");
            valueBinding.setValue(getFacesContext(), "value");
        } catch (ReferenceSyntaxException rse) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);

        exceptionThrown = false;
        try {
            valueBinding.setRef("param");
            valueBinding.setValue(getFacesContext(), "value");
        } catch (ReferenceSyntaxException rse) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);

        exceptionThrown = false;
        try {
            valueBinding.setRef("paramValues");
            valueBinding.setValue(getFacesContext(), "value");
        } catch (ReferenceSyntaxException rse) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);

        exceptionThrown = false;
        try {
            valueBinding.setRef("view");
            valueBinding.setValue(getFacesContext(), "value");
        } catch (ReferenceSyntaxException rse) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }


    public void testInvalidExpression() {
        ValueBindingImpl valueBinding = new ValueBindingImpl(
            getFacesContext().getApplication());

        boolean exceptionThrown = false;
        try {
            valueBinding.setRef("");
            valueBinding.getValue(getFacesContext());
        } catch (FacesException e) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);

        exceptionThrown = false;
        try {
            valueBinding.setRef("!");
            valueBinding.getValue(getFacesContext());
        } catch (PropertyNotFoundException e) {
            exceptionThrown = true;
        } catch (EvaluationException ee) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);

        exceptionThrown = false;
        try {
            valueBinding.setRef("..");
            valueBinding.getValue(getFacesContext());
        } catch (PropertyNotFoundException e) {
            exceptionThrown = true;
        } catch (EvaluationException ee) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);

        exceptionThrown = false;
        try {
            valueBinding.setRef(".foo");
            valueBinding.getValue(getFacesContext());
        } catch (PropertyNotFoundException e) {
            exceptionThrown = true;
        } catch (EvaluationException ee) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);

        exceptionThrown = false;
        try {
            valueBinding.setRef("()");
            valueBinding.getValue(getFacesContext());
        } catch (PropertyNotFoundException e) {
            exceptionThrown = true;
        } catch (EvaluationException ee) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);

        exceptionThrown = false;
        try {
            valueBinding.setRef("[]");
            valueBinding.getValue(getFacesContext());
        } catch (PropertyNotFoundException e) {
            exceptionThrown = true;
        } catch (EvaluationException ee) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);

        exceptionThrown = false;
        try {
            valueBinding.setRef("${applicationScope}");
            valueBinding.getValue(getFacesContext());
        } catch (PropertyNotFoundException e) {
            exceptionThrown = true;
        } catch (EvaluationException ee) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);

        exceptionThrown = false;
        try {
            valueBinding.setRef("applicationScope >= sessionScope");
            valueBinding.getValue(getFacesContext());
        } catch (PropertyNotFoundException e) {
            exceptionThrown = true;
        } catch (EvaluationException ee) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);

        exceptionThrown = false;
        try {
            valueBinding.setRef("foo applicationScope");
            valueBinding.getValue(getFacesContext());
        } catch (PropertyNotFoundException e) {
            exceptionThrown = true;
        } catch (EvaluationException ee) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);

    }


    public void testStateHolderSmall() throws Exception {
        StateHolderSaver saver = null;
        ValueBinding binding =
            getFacesContext().getApplication().createValueBinding(
                "#{TestBean.indexProperties[0]}");

        assertEquals("ValueBinding not expected value", "Justyna",
                     (String) binding.getValue(getFacesContext()));
        saver = new StateHolderSaver(getFacesContext(), binding);
        binding = null;
        binding = (ValueBinding) saver.restore(getFacesContext());
        assertEquals("ValueBinding not expected value", "Justyna",
                     (String) binding.getValue(getFacesContext()));
    }


    public void testStateHolderMedium() throws Exception {
        UIViewRoot root = null;
        UIForm form = null;
        UIInput input = null;
        Object state = null;
        getFacesContext().setViewRoot(root = Util.getViewHandler(getFacesContext()).createView(getFacesContext(), null));
        root.getChildren().add(form = new UIForm());
        form.getChildren().add(input = new UIInput());
        input.setValueBinding("buckaroo",
                              getFacesContext().getApplication()
                              .createValueBinding(
                                  "#{TestBean.indexProperties[0]}"));
        state = root.processSaveState(getFacesContext());

        // synthesize the tree structure
        getFacesContext().setViewRoot(root = Util.getViewHandler(getFacesContext()).createView(getFacesContext(), null));
        root.getChildren().add(form = new UIForm());
        form.getChildren().add(input = new UIInput());
        root.processRestoreState(getFacesContext(), state);

        assertEquals("ValueBinding not expected value", "Justyna",
                     (String) input.getValueBinding("buckaroo").getValue(
                         getFacesContext()));

    }


    public void testGetExpressionString() throws Exception {
        ApplicationImpl app =
            (ApplicationImpl) getFacesContext().getApplication();
        String ref = null;
        ValueBinding vb = null;

        ref = "#{NewCustomerFormHandler.minimumAge}";
        vb = app.createValueBinding(ref);
        assertEquals(ref, vb.getExpressionString());

        ref = "minimum age is #{NewCustomerFormHandler.minimumAge}";
        vb = app.createValueBinding(ref);
        assertEquals(ref, vb.getExpressionString());
    }


    public void testMixedELValueParser() throws Exception {
        String[] args = {"one"};
        com.sun.faces.el.MixedELValueParser.main(args);
    }


    class StateHolderSaver extends Object implements Serializable {

        protected String className = null;
        protected Object savedState = null;


        public StateHolderSaver(FacesContext context, Object toSave) {
            className = toSave.getClass().getName();

            if (toSave instanceof StateHolder) {
                // do not save an attached object that is marked transient.
                if (!((StateHolder) toSave).isTransient()) {
                    savedState = ((StateHolder) toSave).saveState(context);
                } else {
                    className = null;
                }
            }
        }


        /**
         * @return the restored {@link StateHolder} instance.
         */

        public Object restore(FacesContext context)
            throws IllegalStateException {
            Object result = null;
            Class toRestoreClass = null;
            if (className == null) {
                return null;
            }

            try {
                toRestoreClass = loadClass(className, this);
            } catch (ClassNotFoundException e) {
                throw new IllegalStateException(e.getMessage());
            }

            if (null != toRestoreClass) {
                try {
                    result = toRestoreClass.newInstance();
                } catch (InstantiationException e) {
                    throw new IllegalStateException(e.getMessage());
                } catch (IllegalAccessException a) {
                    throw new IllegalStateException(a.getMessage());
                }
            }

            if (null != result && null != savedState &&
                result instanceof StateHolder) {
                // don't need to check transient, since that was done on
                // the saving side.
                ((StateHolder) result).restoreState(context, savedState);
            }
            return result;
        }


        private Class loadClass(String name,
                                Object fallbackClass)
            throws ClassNotFoundException {
            ClassLoader loader =
                Thread.currentThread().getContextClassLoader();
            if (loader == null) {
                loader = fallbackClass.getClass().getClassLoader();
            }
            return loader.loadClass(name);
        }
    }

} // end of class TestValueBindingImpl

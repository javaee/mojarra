/*
<<<<<<< TestValueExpressionImpl.java
 * $Id: TestValueExpressionImpl.java,v 1.8 2006/03/29 23:04:54 rlubke Exp $
=======
 * $Id: TestValueExpressionImpl.java,v 1.8 2006/03/29 23:04:54 rlubke Exp $
>>>>>>> 1.32.18.5
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

// TestValueExpressionImpl.java
package com.sun.faces.el;

import com.sun.faces.cactus.ServletFacesTestCase;
import com.sun.faces.TestBean;
import com.sun.faces.cactus.TestBean.Inner2Bean;
import com.sun.faces.cactus.TestBean.InnerBean;
import com.sun.faces.application.ApplicationImpl;
import com.sun.faces.spi.ManagedBeanFactory.Scope;
import com.sun.faces.util.Util;
import org.apache.cactus.WebRequest;
import javax.faces.component.StateHolder;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import javax.el.ValueExpression;
import javax.el.ELException;
import javax.el.PropertyNotFoundException;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * <B>TestValueExpressionImpl </B> is a class ... <p/><B>Lifetime And Scope </B>
 * <P>
 */

public class TestValueExpressionImpl extends ServletFacesTestCase
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
    protected ValueExpression valueExpression;
    
    // Attribute Instance Variables

    // Relationship Instance Variables

    //
    // Constructors and Initializers
    //

    public TestValueExpressionImpl()
    {
        super("TestValueExpressionImpl");
    }

    public TestValueExpressionImpl(String name)
    {
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

    protected ValueExpression create(String ref) throws Exception
    {
        return (getFacesContext().getApplication().getExpressionFactory().
            createValueExpression(getFacesContext().getELContext(),("#{" + ref + "}"), Object.class));
    }

    public void populateRequest(WebRequest theRequest)
    {
        theRequest.addHeader("ELHeader", "ELHeader");
        theRequest.addHeader("multiheader", "1");
        theRequest.addHeader("multiheader", "2");
        theRequest.addParameter("ELParam", "ELParam");
        theRequest.addParameter("multiparam", "one");
        theRequest.addParameter("multiparam", "two");
        theRequest.addCookie("cookie", "monster");
    }

    public void beginELGet(WebRequest theRequest)
    {
        populateRequest(theRequest);
    }

    public void testELGet() throws Exception
    {
        TestBean testBean = new TestBean();
        InnerBean newInner, oldInner = new InnerBean();
        testBean.setInner(oldInner);
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

        valueExpression = this.create("myMap.myBean.one");
        result = valueExpression.getValue(getFacesContext().getELContext());
        assertEquals("one", result);

        valueExpression = this.create("myMap[\"myBean\"].one");
        result = valueExpression.getValue(getFacesContext().getELContext());
        assertTrue("one".equals(result));

        valueExpression = this.create("myMap.myBean['one']");
        result = valueExpression.getValue(getFacesContext().getELContext());
        assertTrue("one".equals(result));

        // Simple tests, verify that bracket and dot operators work
        valueExpression = this.create("TestBean.inner");
        getFacesContext().getExternalContext().getSessionMap().put("TestBean",
                testBean);
        result = valueExpression.getValue(getFacesContext().getELContext());
        assertTrue(result == oldInner);

        valueExpression = this.create("TestBean[\"inner\"]");
        result = valueExpression.getValue(getFacesContext().getELContext());
        assertTrue(result == oldInner);

        valueExpression = this.create("TestBean[\"inner\"].customers[1]");
        result = valueExpression.getValue(getFacesContext().getELContext());
        assertTrue(result instanceof String);
        assertTrue(result.equals("Jerry"));

        // try out the implicit objects
        valueExpression = this.create("sessionScope.TestBean.inner");
        result = valueExpression.getValue(getFacesContext().getELContext());
        assertTrue(result == oldInner);

        valueExpression = this.create("header.ELHeader");
        result = valueExpression.getValue(getFacesContext().getELContext());
        assertTrue(requestsHaveSameAttributeSet(
                (HttpServletRequest) getFacesContext().getExternalContext()
                        .getRequest(), (HttpServletRequest) request));
        assertTrue(request.getHeader("ELHeader").equals("ELHeader"));
        assertTrue(result.equals("ELHeader"));

        valueExpression = this.create("param.ELParam");
        result = valueExpression.getValue(getFacesContext().getELContext());
        assertTrue(result.equals("ELParam"));

        String multiparam[] = null;
        valueExpression = this.create("paramValues.multiparam");
        multiparam = (String[]) valueExpression.getValue(getFacesContext().getELContext());
        assertTrue(null != multiparam);
        assertTrue(2 == multiparam.length);
        assertTrue(multiparam[0].equals("one"));
        assertTrue(multiparam[1].equals("two"));

        valueExpression = this.create("headerValues.multiheader");
        String[] multiHeader = (String[]) valueExpression
                .getValue(getFacesContext().getELContext());
        assertTrue(null != multiHeader);
        assertTrue(1 == multiHeader.length);
        assertTrue(multiHeader[0].equals("1,2"));

        valueExpression = this.create("initParam.testInitParam");
        result = valueExpression.getValue(getFacesContext().getELContext());
        assertTrue(null != result);
        assertTrue(result.equals("testInitParam"));

        // PENDING(craigmcc) - Comment out this test because on my platform
        // the getRequestCookies() call returns null
        /*
         * valueExpression.setRef("cookie.cookie"); result =
         * valueExpression.getValue(getFacesContext().getELContext()); assertTrue(null != result);
         * assertTrue(result instanceof Cookie); assertTrue(((Cookie)
         * result).getValue().equals("monster"));
         */

    }

    public void beginELSet(WebRequest theRequest)
    {
        populateRequest(theRequest);
    }

    public void testELSet() throws Exception
    {
        TestBean testBean = new TestBean();
        InnerBean newInner, oldInner = new InnerBean();
        testBean.setInner(oldInner);
        ValueExpression valueExpression = null;
        Object result = null;
        ExternalContext extContext = getFacesContext().getExternalContext();

        Map myMap = new HashMap();
        TestBean myBean = new TestBean();
        myMap.put("myBean", myBean);
        extContext.getRequestMap().put("myMap", myMap);

        //
        // Set tests
        //
        valueExpression = this.create("myMap.myBean.one");
        valueExpression.setValue(getFacesContext().getELContext(), "one");
        Map map = (Map) extContext.getRequestMap().get("myMap");
        assertTrue("one".equals(((TestBean) map.get("myBean")).getOne()));
        myBean = new TestBean();
        map.put("myBean", myBean);
        extContext.getRequestMap().put("myMap", myMap);

        // test that we can set null as the value
        valueExpression = this.create("myMap.myBean.prop");
        valueExpression.setValue(getFacesContext().getELContext(), null);
        map = (Map) extContext.getRequestMap().get("myMap");
        assertEquals(null, ((TestBean) map.get("myBean")).getOne());
        myBean = new TestBean();
        map.put("myBean", myBean);
        extContext.getRequestMap().put("myMap", myMap);

        valueExpression = this.create("myMap[\"myBean\"].one");
        valueExpression.setValue(getFacesContext().getELContext(), "one");
        map = (Map) extContext.getRequestMap().get("myMap");
        assertTrue("one".equals(((TestBean) map.get("myBean")).getOne()));
        myBean = new TestBean();
        map.put("myBean", myBean);
        extContext.getRequestMap().put("myMap", myMap);

        // test that we can set the property to null
        valueExpression = this.create("myMap[\"myBean\"].prop");
        valueExpression.setValue(getFacesContext().getELContext(), null);
        map = (Map) extContext.getRequestMap().get("myMap");
        String msg = "Default Message";
        if (((TestBean) map.get("myBean")).getProp() != null)
        {
            msg = ((TestBean) map.get("myBean")).getProp().getClass().getName();
        }
        assertEquals(msg, null, ((TestBean) map.get("myBean")).getProp());
        myBean = new TestBean();
        map.put("myBean", myBean);
        extContext.getRequestMap().put("myMap", myMap);

        valueExpression = this.create("myMap.myBean['one']");
        valueExpression.setValue(getFacesContext().getELContext(), "one");
        map = (Map) extContext.getRequestMap().get("myMap");
        assertTrue("one".equals(((TestBean) map.get("myBean")).getOne()));
        myBean = new TestBean();
        map.put("myBean", myBean);
        extContext.getRequestMap().put("myMap", myMap);

        // set the prop to null
        valueExpression = this.create("myMap.myBean['prop']");
        valueExpression.setValue(getFacesContext().getELContext(), null);
        map = (Map) extContext.getRequestMap().get("myMap");
        assertEquals(null, ((TestBean) map.get("myBean")).getOne());
        myBean = new TestBean();
        map.put("myBean", myBean);
        extContext.getRequestMap().put("myMap", myMap);

        valueExpression = this.create("NonExist");
        valueExpression.setValue(getFacesContext().getELContext(), "value");
        result = extContext.getRequestMap().get("NonExist");
        assertTrue("value".equals(result));
        extContext.getRequestMap().remove("NonExist");

        extContext.getSessionMap().put("Key", "oldValue");
        valueExpression = this.create("Key");
        valueExpression.setValue(getFacesContext().getELContext(), "newValue");
        result = extContext.getSessionMap().get("Key");
        assertTrue("newValue".equals(result));
        extContext.getSessionMap().remove("Key");

        newInner = new InnerBean();
        valueExpression = this.create("TestBean.inner");
        valueExpression.setValue(getFacesContext().getELContext(), newInner);
        result = valueExpression.getValue(getFacesContext().getELContext());
        assertTrue(result == newInner);
        assertTrue(oldInner != newInner);

        oldInner = newInner;
        newInner = new InnerBean();
        valueExpression = this.create("TestBean[\"inner\"]");
        valueExpression.setValue(getFacesContext().getELContext(), newInner);
        result = valueExpression.getValue(getFacesContext().getELContext());
        assertTrue(result == newInner);
        assertTrue(oldInner != newInner);

        String oldCustomer0 = null, oldCustomer1 = null, customer0 = null, customer1 = null;

        valueExpression = this.create("TestBean[\"inner\"].customers[0]");
        oldCustomer0 = customer0 = (String) valueExpression
                .getValue(getFacesContext().getELContext());
        valueExpression = this.create("TestBean[\"inner\"].customers[1]");
        oldCustomer1 = customer1 = (String) valueExpression
                .getValue(getFacesContext().getELContext());

        valueExpression = this.create("TestBean[\"inner\"].customers[0]");
        valueExpression.setValue(getFacesContext().getELContext(), "Jerry");
        valueExpression = this.create("TestBean[\"inner\"].customers[1]");
        valueExpression.setValue(getFacesContext().getELContext(), "Mickey");

        valueExpression = this.create("TestBean[\"inner\"].customers[0]");
        customer0 = (String) valueExpression.getValue(getFacesContext().getELContext());
        valueExpression = this.create("TestBean[\"inner\"].customers[1]");
        customer1 = (String) valueExpression.getValue(getFacesContext().getELContext());
        assertTrue(customer0.equals("Jerry"));
        assertTrue(customer1.equals("Mickey"));

        valueExpression = this.create("TestBean[\"inner\"].customers[0]");
        assertTrue(valueExpression.getValue(getFacesContext().getELContext()) != oldCustomer0);
        valueExpression = this.create("TestBean[\"inner\"].customers[1]");
        assertTrue(valueExpression.getValue(getFacesContext().getELContext()) != oldCustomer1);

        // put in a map to the customers Collection
        Inner2Bean inner2 = new Inner2Bean();
        assertTrue(null == inner2.getNicknames().get("foo"));
        valueExpression = this.create("TestBean[\"inner\"].customers[2]");
        valueExpression.setValue(getFacesContext().getELContext(), inner2);
        valueExpression = this.create("TestBean[\"inner\"].customers[2]");
        assertTrue(valueExpression.getValue(getFacesContext().getELContext()) == inner2);

        valueExpression = this
                .create("TestBean[\"inner\"].customers[2].nicknames.foo");
        valueExpression.setValue(getFacesContext().getELContext(), "bar");
        assertTrue(((String) inner2.getNicknames().get("foo")).equals("bar"));
    }
    
    public void testNullReference() throws Exception
    {
        boolean exceptionThrown = false;
        // no exception should be thrown as per the EL spec if expression is null.
        try
        {
            getFacesContext().getApplication().getExpressionFactory().
                createValueExpression(getFacesContext().getELContext(),null, Object.class);
        }
        catch (NullPointerException npe) {
            exceptionThrown = false;
        }
        catch (ELException ee) { exceptionThrown= true; };
        assertTrue(exceptionThrown);
    }

    public void testLiterals() throws Exception
    {
        ValueExpression vb = null;
        Object result = null;
        ExternalContext extContext = getFacesContext().getExternalContext();
        
        vb = getFacesContext().getApplication().getExpressionFactory().
            createValueExpression(getFacesContext().getELContext(),"test", Object.class);
        assertEquals("test", vb.getValue(getFacesContext().getELContext()));
        
        assertEquals(String.class, vb.getType(getFacesContext().getELContext()));
        try
        {
            vb.setValue(getFacesContext().getELContext(), "other");
            fail("Literal's setValue(..) should have thrown a EvaluationException");
        }
        catch (javax.el.ELException ee) {}
    }

    public void testReadOnly_singleCase() throws Exception
    {

        // these are mutable Maps
        /*
         * properties on these maps are mutable, but not the object itself....
         * see
         */
        valueExpression = this.create("applicationScope");
        assertTrue(valueExpression.isReadOnly(getFacesContext().getELContext()));
        valueExpression = this.create("sessionScope");
        assertTrue(valueExpression.isReadOnly(getFacesContext().getELContext()));
        valueExpression = this.create("requestScope");
        assertTrue(valueExpression.isReadOnly(getFacesContext().getELContext()));

        // these are immutable Maps
        valueExpression = this.create("param");
        assertTrue(valueExpression.isReadOnly(getFacesContext().getELContext()));
        valueExpression = this.create("paramValues");
        assertTrue(valueExpression.isReadOnly(getFacesContext().getELContext()));
        valueExpression = this.create("header");
        assertTrue(valueExpression.isReadOnly(getFacesContext().getELContext()));
        valueExpression = this.create("headerValues");
        assertTrue(valueExpression.isReadOnly(getFacesContext().getELContext()));
        valueExpression = this.create("cookie");
        assertTrue(valueExpression.isReadOnly(getFacesContext().getELContext()));
        valueExpression = this.create("initParam");
        assertTrue(valueExpression.isReadOnly(getFacesContext().getELContext()));
    }

    public void testReadOnly_multipleCase() throws Exception
    {

        // these are mutable Maps
        valueExpression = this.create("applicationScope.value");
        valueExpression.setValue(getFacesContext().getELContext(), "value");
        String value = (String) valueExpression.getValue(getFacesContext().getELContext());
        assertTrue(!valueExpression.isReadOnly(getFacesContext().getELContext()));
        valueExpression = this.create("sessionScope.value");
        assertTrue(!valueExpression.isReadOnly(getFacesContext().getELContext()));
        valueExpression = this.create("requestScope.value");
        assertTrue(!valueExpression.isReadOnly(getFacesContext().getELContext()));

        // these are immutable Maps
        valueExpression = this.create("param.value");
        assertTrue(valueExpression.isReadOnly(getFacesContext().getELContext()));
        valueExpression = this.create("paramValues.value");
        assertTrue(valueExpression.isReadOnly(getFacesContext().getELContext()));
        valueExpression = this.create("header.value");
        assertTrue(valueExpression.isReadOnly(getFacesContext().getELContext()));
        valueExpression = this.create("headerValues.value");
        assertTrue(valueExpression.isReadOnly(getFacesContext().getELContext()));
        valueExpression = this.create("cookie.value");
        assertTrue(valueExpression.isReadOnly(getFacesContext().getELContext()));
        valueExpression = this.create("initParam.value");
        assertTrue(valueExpression.isReadOnly(getFacesContext().getELContext())); 

        // tree
        // create a dummy root for the tree.
        UIViewRoot page = Util.getViewHandler(getFacesContext()).createView(
                getFacesContext(), null);
        page.setId("root");
        page.setViewId("newTree");
        getFacesContext().setViewRoot(page);
        valueExpression = this.create("view.childCount");
        assertTrue(valueExpression.isReadOnly(getFacesContext().getELContext()));

        com.sun.faces.cactus.TestBean testBean = (com.sun.faces.cactus.TestBean) getFacesContext().getExternalContext()
                .getSessionMap().get("TestBean");
        assertTrue(null != testBean);
        valueExpression = this.create("TestBean.readOnly");
        assertTrue(valueExpression.isReadOnly(getFacesContext().getELContext()));
        valueExpression = this.create("TestBean.one");
        assertTrue(!valueExpression.isReadOnly(getFacesContext().getELContext()));

        InnerBean inner = new InnerBean();
        testBean.setInner(inner);
        valueExpression = this.create("TestBean[\"inner\"].customers[1]");
        assertTrue(!valueExpression.isReadOnly(getFacesContext().getELContext()));

    }

    public void testGetType_singleCase() throws Exception
    {

        // these are mutable Maps
        valueExpression = this.create("applicationScope");
        assertTrue(valueExpression.getType(getFacesContext().getELContext()) == null);
        valueExpression = this.create("sessionScope");
        assertTrue(valueExpression.getType(getFacesContext().getELContext()) == null);
        valueExpression = this.create("requestScope");
        assertTrue(valueExpression.getType(getFacesContext().getELContext()) == null);

        // these are immutable Maps
        valueExpression = this.create("param");
        assertTrue(valueExpression.getType(getFacesContext().getELContext()) == null);
        valueExpression = this.create("paramValues");
        assertTrue(valueExpression.getType(getFacesContext().getELContext()) == null);
        valueExpression = this.create("header");
        assertTrue(valueExpression.getType(getFacesContext().getELContext()) == null);
        valueExpression = this.create("headerValues");
        assertTrue(valueExpression.getType(getFacesContext().getELContext()) == null);
        valueExpression = this.create("cookie");
        assertTrue(valueExpression.getType(getFacesContext().getELContext()) == null);
        valueExpression = this.create("initParam");
        assertTrue(valueExpression.getType(getFacesContext().getELContext()) == null);
    }

    public void beginGetType_multipleCase(WebRequest theRequest)
    {
        populateRequest(theRequest);
    }

    public void testGetType_multipleCase() throws Exception
    {
        String property = "testValueExpressionImpl_property";
        getFacesContext().getExternalContext().getApplicationMap().put(
                property, property);

        getFacesContext().getExternalContext().getSessionMap().put(property,
                property);

        getFacesContext().getExternalContext().getRequestMap().put(property,
                property);

        // these are mutable Maps
        valueExpression = this.create("applicationScope." + property);
        assertTrue(valueExpression.getType(getFacesContext().getELContext()).getName().equals(
                "java.lang.Object"));
        valueExpression = this.create("sessionScope." + property);
        assertTrue(valueExpression.getType(getFacesContext().getELContext()).getName().equals(
                "java.lang.Object"));
        valueExpression = this.create("requestScope." + property);
        assertTrue(valueExpression.getType(getFacesContext().getELContext()).getName().equals(
                "java.lang.Object"));

        // these are immutable Maps
        valueExpression = this.create("param." + "ELParam");
        assertTrue(valueExpression.getType(getFacesContext().getELContext()).getName().equals(
                "java.lang.Object"));
        valueExpression = this.create("paramValues.multiparam");
        assertTrue(valueExpression.getType(getFacesContext().getELContext()).getName().equals(
                "java.lang.Object"));

        valueExpression = this.create("header.ELHeader");
        assertTrue(valueExpression.getType(getFacesContext().getELContext()).getName().equals(
                "java.lang.Object"));
        valueExpression = this.create("headerValues.multiheader");
        assertTrue(valueExpression.getType(getFacesContext().getELContext()).getName().equals(
                "java.lang.Object"));
       // assertTrue(java.util.Enumeration.class.isAssignableFrom(valueExpression
       //         .getType(getFacesContext().getELContext())));
        // PENDING(craigmcc) - Comment out this test because on my platform
        // the getRequestCookies() call returns null
        /*
         * valueExpression = this.create("cookie.cookie");
         * assertTrue(valueExpression.getType(getFacesContext().getELContext()).getName().equals("javax.servlet.http.Cookie"));
         */
        valueExpression = this
                .create("initParam['javax.faces.STATE_SAVING_METHOD']");
        assertTrue(valueExpression.getType(getFacesContext().getELContext()).getName().equals(
                "java.lang.Object"));   

        // tree
        // create a dummy root for the tree.
        UIViewRoot page = Util.getViewHandler(getFacesContext()).createView(
                getFacesContext(), null);
        page.setId("root");
        page.setViewId("newTree");
        getFacesContext().setViewRoot(page);
        valueExpression = this.create("view");
        Class c = valueExpression.getType(getFacesContext().getELContext());
        assertTrue(c == null);

        com.sun.faces.cactus.TestBean testBean = (com.sun.faces.cactus.TestBean) getFacesContext().getExternalContext()
                .getSessionMap().get("TestBean");
        assertTrue(null != testBean);
        valueExpression = this.create("TestBean.readOnly");
        assertTrue(valueExpression.getType(getFacesContext().getELContext()).getName().equals(
                "java.lang.String"));
        valueExpression = this.create("TestBean.one");
        assertTrue(valueExpression.getType(getFacesContext().getELContext()).getName().equals(
                "java.lang.String"));

        InnerBean inner = new InnerBean();
        testBean.setInner(inner);
        valueExpression = this.create("TestBean[\"inner\"].customers[1]");
        assertTrue(valueExpression.getType(getFacesContext().getELContext()).getName().equals(
                "java.lang.Object"));

        valueExpression = this.create("TestBean[\"inner\"]");
        assertTrue(valueExpression.getType(getFacesContext().getELContext()).getName().equals(
                "com.sun.faces.cactus.TestBean$InnerBean"));

        int[] intArray =
        { 1, 2, 3 };
        getFacesContext().getExternalContext().getRequestMap().put("intArray",
                intArray);
        valueExpression = this.create("requestScope.intArray");
       
        assertTrue(valueExpression.getType(getFacesContext().getELContext()).getName().equals(
                "java.lang.Object"));
       // assertTrue(valueExpression.getType(getFacesContext().getELContext()).getName().equals(
       //         "[I"));
    }

    public void testGetScopePositive() throws Exception
    {
        TestBean testBean = new TestBean();
        getFacesContext().getExternalContext().getApplicationMap().put(
                "TestApplicationBean", testBean);

        valueExpression = this.create("TestApplicationBean");
        assertEquals(Scope.APPLICATION, Util.getScope("TestApplicationBean", null));

        valueExpression = this.create("TestApplicationBean.one");
        assertEquals(Scope.APPLICATION, Util.getScope("TestApplicationBean.one",
                null));

        valueExpression = this.create("TestApplicationBean.inner.two");
        assertEquals(Scope.APPLICATION, Util.getScope(
                "TestApplicationBean.inner.two", null));

        valueExpression = this.create("applicationScope.TestApplicationBean");
        assertEquals(Scope.APPLICATION, Util.getScope(
                "applicationScope.TestApplicationBean", null));
        valueExpression = this
                .create("applicationScope.TestApplicationBean.inner.two");
        assertEquals(Scope.APPLICATION, Util.getScope(
                "applicationScope.TestApplicationBean.inner.two", null));

        getFacesContext().getExternalContext().getSessionMap().put(
                "TestSessionBean", testBean);
        valueExpression = this.create("TestSessionBean");
        assertEquals(Scope.SESSION, Util.getScope("TestSessionBean", null));

        valueExpression = this.create("TestSessionBean.one");
        assertEquals(Scope.SESSION, Util.getScope("TestSessionBean.one", null));

        valueExpression = this.create("TestSessionBean.inner.two");
        assertEquals(Scope.SESSION, Util
                .getScope("TestSessionBean.inner.two", null));

        valueExpression = this.create("sessionScope.TestSessionBean");
        assertEquals(Scope.SESSION, Util.getScope("sessionScope.TestSessionBean",
                null));

        valueExpression = this.create("sessionScope.TestSessionBean.inner.two");
        assertEquals(Scope.SESSION, Util.getScope(
                "sessionScope.TestSessionBean.inner.two", null));

        getFacesContext().getExternalContext().getRequestMap().put(
                "TestRequestBean", testBean);
        valueExpression = this.create("TestRequestBean");
        assertEquals(Scope.REQUEST, Util.getScope("TestRequestBean", null));

        valueExpression = this.create("TestRequestBean.one");
        assertEquals(Scope.REQUEST, Util.getScope("TestRequestBean.one", null));

        valueExpression = this.create("TestRequestBean.inner.two");
        assertEquals(Scope.REQUEST, Util
                .getScope("TestRequestBean.inner.two", null));

        valueExpression = this.create("requestScope.TestRequestBean");
        assertEquals(Scope.REQUEST, Util.getScope("requestScope.TestRequestBean",
                null));

        valueExpression = this.create("requestScope.TestRequestBean.inner.two");
        assertEquals(Scope.REQUEST, Util.getScope(
                "requestScope.TestRequestBean.inner.two", null));

        valueExpression = this.create("TestNoneBean");
        assertNull(Util.getScope("TestNoneBean", null));

        valueExpression = this.create("TestNoneBean.one");
        assertNull(Util.getScope("TestNoneBean.one", null));
        valueExpression = this.create("TestNoneBean.inner.two");
        assertNull(Util.getScope("TestNoneBean.inner.two", null));

    }

    public void testGetScopeNegative() throws Exception {
        ValueExpression valueExpression = null;
        String property = null;
        /*
        property = "[]";
        valueExpression = this.factory.createValueExpression(property);
        assertNull(Util.getScope(property, null));
        property = "][";
        assertNull(Util.getScope(property, null));
        property = "";
        assertNull(Util.getScope(property, null));
        property = null;
        assertNull(Util.getScope(property, null));
        property = "foo.sessionScope";
        assertNull(Util.getScope(property, null));        
        */

    }

    // negative test for case when valueRef is merely
    // one of the reserved scope names.
    public void testReservedScopeIdentifiers() throws Exception
    {
        boolean exceptionThrown = false;

        try
        {
            valueExpression = this.create("applicationScope");
            valueExpression.setValue(getFacesContext().getELContext(), "value");
        }
        catch (ELException ee)
        {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);

        exceptionThrown = false;
        try
        {
            valueExpression = this.create("sessionScope");
            valueExpression.setValue(getFacesContext().getELContext(), "value");
        }
        catch (ELException ee)
        {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);

        exceptionThrown = false;
        try
        {
            valueExpression = this.create("requestScope");
            valueExpression.setValue(getFacesContext().getELContext(), "value");
        }
        catch (ELException ee)
        {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);

        exceptionThrown = false;
        try
        {
            valueExpression = this.create("facesContext");
            valueExpression.setValue(getFacesContext().getELContext(), "value");
        }
        catch (ELException ee)
        {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);

        exceptionThrown = false;
        try
        {
            valueExpression = this.create("cookie");
            valueExpression.setValue(getFacesContext().getELContext(), "value");
        }
        catch (ELException ee)
        {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);

        exceptionThrown = false;
        try
        {
            valueExpression = this.create("header");
            valueExpression.setValue(getFacesContext().getELContext(), "value");
        }
        catch (ELException ee)
        {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);

        exceptionThrown = false;
        try
        {
            valueExpression = this.create("headerValues");
            valueExpression.setValue(getFacesContext().getELContext(), "value");
        }
        catch (ELException ee)
        {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);

        exceptionThrown = false;
        try
        {
            valueExpression = this.create("initParam");
            valueExpression.setValue(getFacesContext().getELContext(), "value");
        }
        catch (ELException ee)
        {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);

        exceptionThrown = false;
        try
        {
            valueExpression = this.create("param");
            valueExpression.setValue(getFacesContext().getELContext(), "value");
        }
        catch (ELException ee)
        {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);

        exceptionThrown = false;
        try
        {
            valueExpression = this.create("paramValues");
            valueExpression.setValue(getFacesContext().getELContext(), "value");
        }
        catch (ELException ee)
        {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);

        exceptionThrown = false;
        try
        {
            valueExpression = this.create("view");
            valueExpression.setValue(getFacesContext().getELContext(), "value");
        }
        catch (ELException ee)
        {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }

    public void testInvalidExpression() throws Exception
    {

        boolean exceptionThrown = false;
        try
        {
            valueExpression = this.create("");
            valueExpression.getValue(getFacesContext().getELContext());
        }
        catch (ELException e)
        {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);

        exceptionThrown = false;
        try
        {
            valueExpression = this.create("!");
            valueExpression.getValue(getFacesContext().getELContext());
        }
        catch (ELException ee)
        {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);

        exceptionThrown = false;
        try
        {
            valueExpression = this.create("..");
            valueExpression.getValue(getFacesContext().getELContext());
        }
        catch (PropertyNotFoundException e)
        {
            exceptionThrown = true;
        }
        catch (ELException ee)
        {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);

        exceptionThrown = false;
        try
        {
            valueExpression = this.create(".foo");
            valueExpression.getValue(getFacesContext().getELContext());
        }
        catch (PropertyNotFoundException e)
        {
            exceptionThrown = true;
        }
        catch (ELException ee)
        {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);

        exceptionThrown = false;
        try
        {
            valueExpression = this.create("()");
            valueExpression.getValue(getFacesContext().getELContext());
        }
        catch (PropertyNotFoundException e)
        {
            exceptionThrown = true;
        }
        catch (ELException ee)
        {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);

        exceptionThrown = false;
        try
        {
            valueExpression = this.create("[]");
            valueExpression.getValue(getFacesContext().getELContext());
        }
        catch (PropertyNotFoundException e)
        {
            exceptionThrown = true;
        }
        catch (ELException ee)
        {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);

        exceptionThrown = false;
        try
        {
            valueExpression = this.create("applicationScope}");
            valueExpression.getValue(getFacesContext().getELContext());
        }
        catch (PropertyNotFoundException e)
        {
            exceptionThrown = true;
        }
        catch (ELException ee)
        {
            exceptionThrown = true;
        }
        assertTrue(!exceptionThrown);

        exceptionThrown = false;
        try
        {
            valueExpression = this.create("applicationScope >= sessionScope");
            valueExpression.getValue(getFacesContext().getELContext());
        }
        catch (PropertyNotFoundException e)
        {
            exceptionThrown = true;
        }
        catch (ELException ee)
        {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);

        exceptionThrown = false;
        try
        {
            valueExpression = this.create("foo applicationScope");
            valueExpression.getValue(getFacesContext().getELContext());
        }
        catch (PropertyNotFoundException e)
        {
            exceptionThrown = true;
        }
        catch (ELException ee)
        {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);

    }

   /* public void testStateHolderSmall() throws Exception
    {
        StateHolderSaver saver = null;
        ValueExpression binding = getFacesContext().getApplication().getExpressionFactory().
                createValueExpression("#{TestBean.indexProperties[0]}", Object.class, null);

        assertEquals("ValueExpression not expected value", "Justyna",
                (String) binding.getValue(getFacesContext().getELContext()));
        saver = new StateHolderSaver(getFacesContext(), binding);
        binding = null;
        binding = (ValueExpression) saver.restore(getFacesContext());
        assertEquals("ValueExpression not expected value", "Justyna",
                (String) binding.getValue(getFacesContext().getELContext()));
    }

    public void testStateHolderMedium() throws Exception
    {
        UIViewRoot root = null;
        UIForm form = null;
        UIInput input = null;
        Object state = null;
        getFacesContext().setViewRoot(
                root = Util.getViewHandler(getFacesContext()).createView(
                        getFacesContext(), null));
        root.getChildren().add(form = new UIForm());
        form.getChildren().add(input = new UIInput());
        input.setValueExpression("buckaroo", (getFacesContext().getApplication().getExpressionFactory().
                createValueExpression("#{TestBean.indexProperties[0]}", Object.class, null)));
        state = root.processSaveState(getFacesContext());

        // synthesize the tree structure
        getFacesContext().setViewRoot(
                root = Util.getViewHandler(getFacesContext()).createView(
                        getFacesContext(), null));
        root.getChildren().add(form = new UIForm());
        form.getChildren().add(input = new UIInput());
        root.processRestoreState(getFacesContext(), state);

        assertEquals("ValueExpression not expected value", "Justyna",
                (String) input.getValueExpression("buckaroo").getValue(
                        getFacesContext().getELContext()));

    } */

    public void testGetExpressionString() throws Exception
    {
        ApplicationImpl app = (ApplicationImpl) getFacesContext()
                .getApplication();
        String ref = null;
        ValueExpression vb = null;

        ref = "#{NewCustomerFormHandler.minimumAge}";
        vb = app.getExpressionFactory().createValueExpression(getFacesContext().getELContext(),ref, Object.class);
        assertEquals(ref, vb.getExpressionString());

        ref = "minimum age is #{NewCustomerFormHandler.minimumAge}";
        vb = app.getExpressionFactory().createValueExpression(getFacesContext().getELContext(),ref, Object.class);
        assertEquals(ref, vb.getExpressionString());
    }

    class StateHolderSaver extends Object implements Serializable
    {

        protected String className = null;

        protected Object savedState = null;

        public StateHolderSaver(FacesContext context, Object toSave)
        {
            className = toSave.getClass().getName();

            if (toSave instanceof StateHolder)
            {
                // do not save an attached object that is marked transient.
                if (!((StateHolder) toSave).isTransient())
                {
                    savedState = ((StateHolder) toSave).saveState(context);
                }
                else
                {
                    className = null;
                }
            }
        }

        /**
         * @return the restored {@link StateHolder}instance.
         */

        public Object restore(FacesContext context)
                throws IllegalStateException
        {
            Object result = null;
            Class toRestoreClass = null;
            if (className == null)
            {
                return null;
            }

            try
            {
                toRestoreClass = loadClass(className, this);
            }
            catch (ClassNotFoundException e)
            {
                System.out.println("ClassNotFound Exception");
                throw new IllegalStateException(e.getMessage());
            }

            if (null != toRestoreClass)
            {
                try
                {
                    result = toRestoreClass.newInstance();
                }
                catch (InstantiationException e)
                {
                    System.out.println("Instantiation Exception");
                    e.printStackTrace();
                    throw new IllegalStateException(e.getMessage());
                }
                catch (IllegalAccessException a)
                {
                    System.out.println("IleegalAccess Exception");
                    throw new IllegalStateException(a.getMessage());
                }
            }

            if (null != result && null != savedState
                    && result instanceof StateHolder)
            {
                // don't need to check transient, since that was done on
                // the saving side.
                ((StateHolder) result).restoreState(context, savedState);
            }
            return result;
        }

        private Class loadClass(String name, Object fallbackClass)
                throws ClassNotFoundException
        {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            if (loader == null)
            {
                loader = fallbackClass.getClass().getClassLoader();
            }
            return loader.loadClass(name);
        }
    }

} // end of class TestValueExpressionImpl

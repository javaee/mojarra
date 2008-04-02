/*
 * $Id: TestValueExpressionImpl_Model.java,v 1.5 2006/03/29 22:39:43 rlubke Exp $
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

// TestValueExpressionImpl_Model.java

package com.sun.faces.el;

import com.sun.faces.cactus.ServletFacesTestCase;
import com.sun.faces.TestBean;
import com.sun.faces.cactus.TestBean.Inner2Bean;
import com.sun.faces.cactus.TestBean.InnerBean;

import javax.faces.context.FacesContext;
import javax.el.ELException;
import javax.el.ValueExpression;
import javax.el.ELContext;

/**
 * <B>TestValueExpressionImpl_Model</B> is a class ...
 * <p/>
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestValueExpressionImpl_Model.java,v 1.5 2006/03/29 22:39:43 rlubke Exp $
 */

public class TestValueExpressionImpl_Model extends ServletFacesTestCase {

    ValueExpression valueExpression = null;


    // ------------------------------------------------------------ Constructors


    public TestValueExpressionImpl_Model() {

        super("TestValueExpressionImpl");

    }


    public TestValueExpressionImpl_Model(String name) {

        super(name);

    }


    // ---------------------------------------------------------- Public Methods


    public ValueExpression create(String ref) throws Exception {

    	return (getFacesContext().getApplication().getExpressionFactory().
            createValueExpression(getFacesContext().getELContext(),("#{" + ref + "}"), Object.class));

    }


    public void setUp() {

        super.setUp();
        valueExpression = null;

    }


    public void tearDown() {

        valueExpression = null;
        super.tearDown();

    }


    public void testGet() throws Exception {

        FacesContext facesContext = getFacesContext();
        System.out.println("Testing getValue() with model bean in context");
        assertTrue(facesContext != null);
        TestBean testBeanResult = null, testBean = new TestBean();
        InnerBean inner = new InnerBean();
        Inner2Bean inner2 = new Inner2Bean();
        String result;

        // Init the beans
        testBean.setOne("one");
        inner.setTwo("two");
        inner2.setThree("three");
        inner.setInner2(inner2);
        testBean.setInner(inner);

        assertTrue(facesContext != null);
        assertTrue(facesContext.getExternalContext().getSession(false) != null);

        facesContext.getExternalContext().getRequestMap().remove("TestBean");
        facesContext.getExternalContext().getSessionMap().remove("TestBean");
        facesContext.getExternalContext().getApplicationMap().put("TestBean",
                                                                  testBean);

        // Test zero levels of nesting
        valueExpression = this.create("applicationScope.TestBean");
        testBeanResult = (TestBean) valueExpression.getValue(getFacesContext().getELContext());
        assertTrue(testBeanResult != null);
        assertTrue(testBeanResult == testBean);

        // Test one level of nesting
        valueExpression = this.create("applicationScope.TestBean.one");
        result = (String) valueExpression.getValue(getFacesContext().getELContext());
        assertTrue(result.equals("one"));

        valueExpression = this.create("applicationScope.TestBean.inner");
        inner = (InnerBean) valueExpression.getValue(getFacesContext().getELContext());
        assertTrue(null != inner);

        // Test two levels of nesting
        valueExpression = this.create("applicationScope.TestBean.inner.two");
        result = (String) valueExpression.getValue(getFacesContext().getELContext());
        assertTrue(result.equals("two"));

        valueExpression = this.create("applicationScope.TestBean.inner.inner2");
        inner2 = (Inner2Bean)
            valueExpression.getValue(getFacesContext().getELContext());
        assertTrue(null != inner2);

        // Test three levels of nesting
        valueExpression = this.create("applicationScope.TestBean.inner.inner2.three");
        result = (String) valueExpression.getValue(getFacesContext().getELContext());
        assertTrue(result.equals("three"));

    }


    public void testModelType() {

        /***************** PENDING(edburns):

         // Test model type
         System.out.println("Testing getModelType()");
         Class classType = null;
         String className = null;

         // Test zero levels of nesting
         classType = facesContext.getModelType("applicationScope.TestBean");
         assertTrue(classType != null);
         className = classType.getName();
         assertTrue(className.equals(testBean.getClass().getName()));

         classType = facesContext.getModelType("applicationScope.TestBean.inner.pin");
         assertTrue(classType != null);
         className = classType.getName();
         assertTrue(className.equals("java.lang.Integer"));

         classType = facesContext.getModelType("applicationScope.TestBean.inner.result");
         assertTrue(classType != null);
         className = classType.getName();
         assertTrue(className.equals("java.lang.Boolean"));

         classType = facesContext.getModelType("applicationScope.TestBean.one");
         assertTrue(classType != null);
         className = classType.getName();
         assertTrue(className.equals("java.lang.String"));
         *********************/

    }


    public void testSet() throws Exception {

        FacesContext facesContext = getFacesContext();
        System.out.println("Testing setValue() with model bean in session ");
        TestBean testBean = new TestBean();
        InnerBean inner = new InnerBean();
        Inner2Bean innerInner = new Inner2Bean();
        Object result = null;

        getFacesContext().getExternalContext().getSessionMap().put("TestBean",
                                                                   testBean);
        boolean exceptionThrown = false;
        System.setProperty(TestBean.PROP, TestBean.FALSE);
        valueExpression = this.create("TestBean.one");
        valueExpression.setValue(getFacesContext().getELContext(), "one");
        assertTrue(System.getProperty(TestBean.PROP).equals(TestBean.TRUE));

        InnerBean newInner = new InnerBean();
        valueExpression = this.create("TestBean.inner");
        valueExpression.setValue(getFacesContext().getELContext(), newInner);
        result = valueExpression.getValue(getFacesContext().getELContext());
        assertTrue(result == newInner);
        
        // Test two levels of nesting
        System.setProperty(TestBean.PROP, TestBean.FALSE);
        valueExpression = this.create("sessionScope.TestBean.inner.two");
        valueExpression.setValue(getFacesContext().getELContext(), "two");
        assertTrue(System.getProperty(TestBean.PROP).equals(TestBean.TRUE));

        Inner2Bean newInner2 = new Inner2Bean();
        valueExpression = this.create("TestBean.inner.inner2");
        valueExpression.setValue(getFacesContext().getELContext(), newInner2);
        result = valueExpression.getValue(getFacesContext().getELContext());
        assertTrue(result == newInner2);
        
        System.setProperty(TestBean.PROP, TestBean.FALSE);
        valueExpression = this.create("sessionScope.TestBean.inner.inner2");
        valueExpression.setValue(getFacesContext().getELContext(), innerInner);
        assertTrue(System.getProperty(TestBean.PROP).equals(TestBean.TRUE));

        
        // Test three levels of nesting
        System.setProperty(TestBean.PROP, TestBean.FALSE);
        valueExpression = this.create("sessionScope.TestBean.inner.inner2.three");
        valueExpression.setValue(getFacesContext().getELContext(), "three");
        assertTrue(System.getProperty(TestBean.PROP).equals(TestBean.TRUE));

    }


    public void testSetNull() throws Exception {

        FacesContext facesContext = getFacesContext();
        System.out.println(
            "Testing setValue() with model bean in session with null rValues");
        TestBean testBean = new TestBean();
        InnerBean inner = new InnerBean();
        Inner2Bean innerInner = new Inner2Bean();

        getFacesContext().getExternalContext().getSessionMap().put("TestBean",
                                                                   testBean);

        // Test one level of nesting
        valueExpression = this.create("TestBean.one");
        valueExpression.setValue(getFacesContext().getELContext(), null);
        assertTrue(testBean.getOne() == null);

        System.setProperty(TestBean.PROP, TestBean.FALSE);
        valueExpression = this.create("sessionScope.TestBean.inner");
        valueExpression.setValue(getFacesContext().getELContext(), inner);
        assertTrue(System.getProperty(TestBean.PROP).equals(TestBean.TRUE));

        valueExpression = this.create("sessionScope.TestBean.inner");
        valueExpression.setValue(getFacesContext().getELContext(), null);
        assertTrue(testBean.getInner() == null);

        // Inner bean does not exist anymore. So this should result in an
        // exception.  Should throw a PropertyNotFoundException according
        // to page 92 of the EL Spec
        boolean exceptionThrown = false;
        valueExpression = this.create("sessionScope.TestBean.inner.two");
        try {
            valueExpression.setValue(getFacesContext().getELContext(), null);
        } catch (javax.el.PropertyNotFoundException ee) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);

    }


    public void testSetWithNoCurlyBraces() throws Exception {

        FacesContext facesContext = getFacesContext();
        System.out.println("Testing setValue() with model bean in request ");
        TestBean testBean = new TestBean();
        InnerBean inner = new InnerBean();
        Inner2Bean innerInner = new Inner2Bean();

        facesContext.getExternalContext().getSessionMap().remove("TestBean");
        facesContext.getExternalContext().getRequestMap().put("TestBean",
                                                              testBean);

        // Test implicit scopes direct access to some scope objects should
        // throw an illegalArgumentException
        boolean gotException = false;
        try {
            valueExpression = this.create("header.header-one");
            valueExpression.setValue(getFacesContext().getELContext(), testBean);
        } catch (javax.el.ELException pnf) {
            gotException = true;
        }
        assertTrue(gotException);

        // Test one level of nesting
        System.setProperty(TestBean.PROP, TestBean.FALSE);
        valueExpression = this.create("TestBean.one");
        valueExpression.setValue(getFacesContext().getELContext(), "one");
        assertTrue(System.getProperty(TestBean.PROP).equals(TestBean.TRUE));

        System.setProperty(TestBean.PROP, TestBean.FALSE);
        valueExpression = this.create("requestScope.TestBean.inner");
        valueExpression.setValue(getFacesContext().getELContext(), inner);
        assertTrue(System.getProperty(TestBean.PROP).equals(TestBean.TRUE));

        // Test two levels of nesting
        System.setProperty(TestBean.PROP, TestBean.FALSE);
        valueExpression = this.create("requestScope.TestBean.inner.two");
        valueExpression.setValue(getFacesContext().getELContext(), "two");
        assertTrue(System.getProperty(TestBean.PROP).equals(TestBean.TRUE));

        System.setProperty(TestBean.PROP, TestBean.FALSE);
        valueExpression = this.create("requestScope.TestBean.inner.inner2");
        valueExpression.setValue(getFacesContext().getELContext(), innerInner);
        assertTrue(System.getProperty(TestBean.PROP).equals(TestBean.TRUE));

        // Test three levels of nesting
        System.setProperty(TestBean.PROP, TestBean.FALSE);
        valueExpression = this.create("requestScope.TestBean.inner.inner2.three");
        valueExpression.setValue(getFacesContext().getELContext(), "three");
        assertTrue(System.getProperty(TestBean.PROP).equals(TestBean.TRUE));

    }

} // end of class TestValueExpressionImpl_Model

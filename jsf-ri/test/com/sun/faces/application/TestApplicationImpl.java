/*
 * $Id: TestApplicationImpl.java,v 1.28 2005/09/15 00:46:00 rlubke Exp $
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

// TestApplicationImpl.java

package com.sun.faces.application;

import com.sun.faces.JspFacesTestCase;
import com.sun.faces.TestComponent;
import com.sun.faces.RIConstants;
import com.sun.faces.util.Util;
import com.sun.faces.util.TestingUtil;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Map;

import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.application.ApplicationFactory;
import javax.faces.application.NavigationHandler;
import javax.faces.application.StateManager;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.el.PropertyResolver;
import javax.faces.el.ReferenceSyntaxException;
import javax.faces.el.ValueBinding;
import javax.faces.el.VariableResolver;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;

import javax.el.ELException;
import javax.el.ValueExpression;

/**
 * <B>TestApplicationImpl</B> is a class ...
 * <p/>
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestApplicationImpl.java,v 1.28 2005/09/15 00:46:00 rlubke Exp $
 */

public class TestApplicationImpl extends JspFacesTestCase {

//
// Protected Constants
//
    public static final String HANDLED_ACTIONEVENT1 = "handledValueEvent1";
    public static final String HANDLED_ACTIONEVENT2 = "handledValueEvent2";

//
// Class Variables
//

//
// Instance Variables
//
    private ApplicationImpl application = null;

// Attribute Instance Variables

// Relationship Instance Variables

//
// Constructors and Initializers    
//

    public TestApplicationImpl() {
        super("TestApplicationImpl");
    }


    public TestApplicationImpl(String name) {
        super(name);
    }
//
// Class methods
//

//
// General Methods
//

    public void setUp() {
        super.setUp();
        ApplicationFactory aFactory =
            (ApplicationFactory) FactoryFinder.getFactory(
                FactoryFinder.APPLICATION_FACTORY);
        application = (ApplicationImpl) aFactory.getApplication();
    }


    public void testAccessors() {

        assertTrue(application.getELResolver() != null);
        assertTrue(application.getExpressionFactory() != null);
        
        // 1. Verify "getActionListener" returns the same ActionListener
        //    instance if called multiple times.
        //
        ActionListener actionListener1 = new ValidActionListener();
        application.setActionListener(actionListener1);
        ActionListener actionListener2 = application.getActionListener();
        ActionListener actionListener3 = application.getActionListener();
        assertTrue((actionListener1 == actionListener2) &&
                   (actionListener1 == actionListener3));

        // 2. Verify "getNavigationHandler" returns the same NavigationHandler
        //    instance if called multiple times.
        //
        NavigationHandler navigationHandler1 = new NavigationHandlerImpl();
        application.setNavigationHandler(navigationHandler1);
        NavigationHandler navigationHandler2 = application.getNavigationHandler();
        NavigationHandler navigationHandler3 = application.getNavigationHandler();
        assertTrue((navigationHandler1 == navigationHandler2) &&
                   (navigationHandler1 == navigationHandler3));

        // 3. Verify "getPropertyResolver" returns the same PropertyResolver
        //    instance if called multiple times.
        //
        PropertyResolver propertyResolver1 = application.getPropertyResolver();
        PropertyResolver propertyResolver2 = application.getPropertyResolver();
        PropertyResolver propertyResolver3 = application.getPropertyResolver();
        assertTrue((propertyResolver1 == propertyResolver2) &&
                   (propertyResolver1 == propertyResolver3));

        // 4. Verify "getVariableResolver" returns the same VariableResolver
        //    instance if called multiple times.
        //
        VariableResolver variableResolver1 = application.getVariableResolver();
        VariableResolver variableResolver2 = application.getVariableResolver();
        VariableResolver variableResolver3 = application.getVariableResolver();
        assertTrue((variableResolver1 == variableResolver2) &&
                   (variableResolver1 == variableResolver3));

        // 5. Verify "getStateManager" returns the same StateManager
        //    instance if called multiple times.
        //
        StateManager stateManager1 = new StateManagerImpl();
        application.setStateManager(stateManager1);
        StateManager stateManager2 = application.getStateManager();
        StateManager stateManager3 = application.getStateManager();
        assertTrue((stateManager1 == stateManager2) &&
                   (stateManager1 == stateManager3));
    }


    public void testExceptions() {
        boolean thrown;

        // 1. Verify NullPointer exception which occurs when attempting
        //    to set a null ActionListener
        //
        thrown = false;
        try {
            application.setActionListener(null);
        } catch (NullPointerException e) {
            thrown = true;
        }
        assertTrue(thrown);

        // 3. Verify NullPointer exception which occurs when attempting
        //    to set a null NavigationHandler
        //
        thrown = false;
        try {
            application.setNavigationHandler(null);
        } catch (NullPointerException e) {
            thrown = true;
        }
        assertTrue(thrown);

        // 4. Verify ISE occurs when attempting to set PropertyResolver
        // after application init time
        thrown = false;
        try {
            application.setPropertyResolver(null);
        } catch (IllegalStateException e) {
            thrown = true;
        }
        assertTrue(thrown);

        // 5. Verify NullPointer exception which occurs when attempting
        //    to get a ValueBinding with a null ref
        //
        thrown = false;
        try {
            application.createValueBinding(null);
        } catch (Exception e) {
            thrown = true;
        }
        assertTrue(thrown);

        // 6.Verify ISE occurs when attempting to set VariableResolver
        // after application init time
        //
        thrown = false;
        try {
            application.setVariableResolver(null);
        } catch (IllegalStateException e) {
            thrown = true;
        }
        assertTrue(thrown);
       
        // 7. Verify NullPointer exception which occurs when attempting
        //    to set a null StateManager
        //
        thrown = false;
        try {
            application.setStateManager(null);
        } catch (NullPointerException e) {
            thrown = true;
        }
        assertTrue(thrown);

        thrown = false;
        try {
            application.createValueBinding("improperexpression");
        } catch (ReferenceSyntaxException e) {
            thrown = true;
        }
        assertFalse(thrown);

        thrown = false;
        try {
            application.createValueBinding("improper expression");
        } catch (ReferenceSyntaxException e) {
            thrown = true;
        }
        assertFalse(thrown);

        thrown = false;
        try {
            application.createValueBinding("improper\texpression");
        } catch (ReferenceSyntaxException e) {
            thrown = true;
        }
        assertFalse(thrown);

        thrown = false;
        try {
            application.createValueBinding("improper\rexpression");
        } catch (ReferenceSyntaxException e) {
            thrown = true;
        }
        assertFalse(thrown);

        thrown = false;
        try {
            application.createValueBinding("improper\nexpression");
        } catch (ReferenceSyntaxException e) {
            thrown = true;
        }
        assertFalse(thrown);

        thrown = false;
        try {
            application.createValueBinding("#improperexpression");
        } catch (ReferenceSyntaxException e) {
            thrown = true;
        }
        assertFalse(thrown);

        thrown = false;
        try {
            application.createValueBinding("#{improperexpression");
        } catch (ReferenceSyntaxException e) {
            thrown = true;
        }
        assertTrue(thrown);

        thrown = false;
        try {
            application.createValueBinding("improperexpression}");
        } catch (ReferenceSyntaxException e) {
            thrown = true;
        }
        assertFalse(thrown);

        thrown = false;
        try {
            application.createValueBinding("{improperexpression}");
        } catch (ReferenceSyntaxException e) {
            thrown = true;
        }
        assertFalse(thrown);

        thrown = false;
        try {
            application.createValueBinding("improperexpression}#");
        } catch (ReferenceSyntaxException e) {
            thrown = true;
        } 
        assertFalse(thrown);


        thrown = false;
        try {
            application.createValueBinding("#{proper[\"a key\"]}");
        } catch (ReferenceSyntaxException e) {
            thrown = true;
        }
        assertFalse(thrown);

        try {
            application.createValueBinding("#{proper[\"a { } key\"]}");
        } catch (ReferenceSyntaxException e) {
            thrown = true;
        }
        assertFalse(thrown);

        try {
            application.createValueBinding("bean.a{indentifer");
        } catch (ReferenceSyntaxException e) {
            thrown = true;
        }
        assertFalse(thrown);

        thrown = false;
        try {
            application.createValueBinding("bean['invalid'");
        } catch (ReferenceSyntaxException e) {
            thrown = true;
        }
        assertFalse(thrown);

        thrown = false;
        try {
            application.createValueBinding("bean[[\"invalid\"]].foo");
        } catch (ReferenceSyntaxException e) {
            thrown = true;
        }
        assertFalse(thrown);

        thrown = false;
        try {
            application.createValueBinding("#{bean[\"[a\"]}");
        } catch (ReferenceSyntaxException e) {
            thrown = true;
        }
        assertFalse(thrown);

        try {
            application.createValueBinding("#{bean[\".a\"]}");
        } catch (ReferenceSyntaxException e) {
            thrown = true;
        }
        assertFalse(thrown);
    }


    public class InvalidActionListener implements ActionListener {

        public void processAction(ActionEvent event) {
            System.setProperty(HANDLED_ACTIONEVENT1, HANDLED_ACTIONEVENT1);
        }
    }

    public class ValidActionListener implements ActionListener {

        public void processAction(ActionEvent event) {
            System.setProperty(HANDLED_ACTIONEVENT2, HANDLED_ACTIONEVENT2);
        }
    }

    //
    // Test Config related methods
    //

    public void testAddComponentPositive() {
        TestComponent
            newTestComponent = null,
            testComponent = new TestComponent();


        application.addComponent(testComponent.getComponentType(),
                                 "com.sun.faces.TestComponent");
        assertTrue(
            null !=
            (newTestComponent =
             (TestComponent)
            application.createComponent(testComponent.getComponentType())));
        assertTrue(newTestComponent != testComponent);

    }


    public void testGetComponentWithRefNegative() {
        ValueBinding valueBinding = null;
        boolean exceptionThrown = false;
        UIComponent result = null;
        getFacesContext().getExternalContext().getSessionMap().put("TAIBean",
                                                                   this);
        assertTrue(null != (valueBinding =
                            application.createValueBinding(
                                "#{sessionScope.TAIBean}")));

        try {
            result = application.createComponent(valueBinding, getFacesContext(),
                                                 "notreached");
            assertTrue(false);
        } catch (FacesException e) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }
    
    public void testGetComponentExpressionRefNegative() throws ELException{
        ValueExpression valueBinding = null;
        boolean exceptionThrown = false;
        UIComponent result = null;
        getFacesContext().getExternalContext().getSessionMap().put("TAIBean",
                                                                   this);
        assertTrue(null != (valueBinding =
                            application.getExpressionFactory().createValueExpression(
                            getFacesContext().getELContext(), "#{sessionScope.TAIBean}", Object.class)));

        try {
            result = application.createComponent(valueBinding, getFacesContext(),
                                                 "notreached");
            assertTrue(false);
        } catch (FacesException e) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
        
        // make sure FacesException is thrown when a bogus ValueExpression is
        // passed to createComponent. JSF RI Issue 162
        assertTrue(null != (valueBinding =
                            application.getExpressionFactory().createValueExpression(
                            getFacesContext().getELContext(), "#{a.b}", Object.class)));

        try {
            result = application.createComponent(valueBinding, getFacesContext(),
                                                 "notreached");
            assertTrue(false);
        } catch (FacesException e) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }


    public void testSetViewHandlerException() throws Exception {
        ViewHandler handler = new ViewHandlerImpl();
        UIViewRoot root = Util.getViewHandler(getFacesContext()).createView(getFacesContext(), null);
        root.setViewId("/view");
        root.setId("id");
        getFacesContext().setViewRoot(root);

        boolean exceptionThrown = false;
        try {
            application.setViewHandler(handler);
        } catch (IllegalStateException ise) {
            exceptionThrown = true;
        }
        assertTrue(!exceptionThrown);
        
        try {
            handler.renderView(getFacesContext(),
                               getFacesContext().getViewRoot());
            application.setViewHandler(handler);
        } catch (IllegalStateException ise) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);

        // and test setting the StateManager too.
        exceptionThrown = false;
        try {
            application.setStateManager(new StateManagerImpl());
        } catch (IllegalStateException ise) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }

    // Ensure ApplicationImpl.setDefaultLocale(null) throws NPE
    public void testSetDefaultLocaleNPE() throws Exception {
        try {
            application.setDefaultLocale(null);
            assertTrue(false);
        } catch (NullPointerException npe) {
            ; // we're ok
        }
    }
    
    public void testResourceBundle() throws Exception {
        ResourceBundle rb = null;
        UIViewRoot root = new UIViewRoot();
        root.setLocale(Locale.ENGLISH);
        getFacesContext().setViewRoot(root);
       
        // negative test, non-existant rb
        rb = application.getResourceBundle(getFacesContext(), "bogusName");
        
        assertNull(rb);
        
        // basic test, existing rb
        rb = application.getResourceBundle(getFacesContext(), "testResourceBundle");
        
        assertNotNull(rb);
        
        String value = rb.getString("value1");
        assertEquals("Jerry", value);
        
        // switch locale to German
        getFacesContext().getViewRoot().setLocale(Locale.GERMAN);
        rb = application.getResourceBundle(getFacesContext(), "testResourceBundle");
        
        assertNotNull(rb);
        
        value = rb.getString("value1");
        assertEquals("Bernhard", value);
        
        // switch to a different rb
        rb = application.getResourceBundle(getFacesContext(), "testResourceBundle2");
        
        assertNotNull(rb);
        value = rb.getString("label");
        assertEquals("Abflug", value);
        
    }
    
    public void testLegacyPropertyResolversWithUnifiedEL() {
      
        ValueExpression ve1 = application.getExpressionFactory().
            createValueExpression(getFacesContext().getELContext(),
                "#{mixedBean.customPRTest1}", Object.class);
        Object result = ve1.getValue(getFacesContext().getELContext());     
        assertTrue(result.equals("TestPropertyResolver"));
        
        ValueExpression ve2 = application.getExpressionFactory().
            createValueExpression(getFacesContext().getELContext(),
                "#{mixedBean.customPRTest2}", Object.class);
        result = ve2.getValue(getFacesContext().getELContext());      
        assertTrue(result.equals("PropertyResolverTestImpl"));
    }
    
    public void testLegacyVariableResolversWithUnifiedEL() {
      
        ValueExpression ve1 = application.getExpressionFactory().
            createValueExpression(getFacesContext().getELContext(),
                "#{customVRTest1}", Object.class);
        Object result = ve1.getValue(getFacesContext().getELContext());        
        assertTrue(result.equals("TestVariableResolver"));
        
        ValueExpression ve2 = application.getExpressionFactory().
            createValueExpression(getFacesContext().getELContext(),
                "#{customVRTest2}", Object.class);
        result = ve2.getValue(getFacesContext().getELContext());      
        assertTrue(result.equals("TestOldVariableResolver"));
    }
    
    public static void clearResourceBundlesFromAssociate(ApplicationImpl application) {
        ApplicationAssociate associate = (ApplicationAssociate)
            TestingUtil.invokePrivateMethod("getAssociate",
                                            RIConstants.EMPTY_CLASS_ARGS,
                                            RIConstants.EMPTY_METH_ARGS,
                                            ApplicationImpl.class,
                                            application);       
        if (null != associate) {
            Map resourceBundles = (Map) 
                TestingUtil.getPrivateField("resourceBundles",
                                            ApplicationAssociate.class,
                                            associate);
            if (null != resourceBundles) {
                resourceBundles.clear();
            }
        }
    }


} // end of class TestApplicationImpl

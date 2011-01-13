/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2010 Sun Microsystems, Inc. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 * 
 * Contributor(s):
 * 
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

// TestApplicationImpl.java

package com.sun.faces.application;

import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.List;
import java.util.Date;
import java.util.ArrayList;
import java.io.IOException;
import java.net.URL;

import javax.el.ELException;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.view.facelets.ResourceResolver;
import javax.faces.render.RenderKitFactory;
import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;
import javax.faces.application.NavigationHandler;
import javax.faces.application.StateManager;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.component.UIInput;
import javax.faces.component.UIOutput;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.context.ExternalContext;
import javax.faces.convert.Converter;
import javax.faces.convert.IntegerConverter;
import javax.faces.el.PropertyResolver;
import javax.faces.el.ReferenceSyntaxException;
import javax.faces.el.ValueBinding;
import javax.faces.el.VariableResolver;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;
import javax.faces.event.ListenerFor;
import javax.faces.event.PostAddToViewEvent;
import javax.faces.event.ComponentSystemEventListener;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ListenersFor;
import javax.faces.event.PreRenderComponentEvent;

import com.sun.faces.RIConstants;
import com.sun.faces.TestComponent;
import com.sun.faces.TestForm;
import com.sun.faces.facelets.FaceletFactory;
import com.sun.faces.facelets.Facelet;
import com.sun.faces.facelets.impl.DefaultFaceletFactory;
import com.sun.faces.config.WebConfiguration;
import static com.sun.faces.config.WebConfiguration.WebContextInitParameter.*;
import static com.sun.faces.config.WebConfiguration.BooleanWebContextInitParameter.DisableFaceletJSFViewHandler;
import com.sun.faces.cactus.JspFacesTestCase;
import com.sun.faces.cactus.TestingUtil;

/**
 * <B>TestApplicationImpl</B> is a class ...
 * <p/>
 * <B>Lifetime And Scope</B> <P>
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

        // 4. Verify NPE occurs when attempting to set
        // a null PropertyResolver
        thrown = false;
        try {
            application.setPropertyResolver(null);
        } catch (NullPointerException npe) {
            thrown = true;
        }
        assertTrue(thrown);

        // 5. Verify NPE occurs when attempting to set
        // a null VariableResolver
        thrown = false;
        try {
            application.setVariableResolver(null);
        } catch (NullPointerException npe) {
            thrown = true;
        }
        assertTrue(thrown);

        // 5. Verify ISE occurs when attempting to set
        // a VariableResolver after a request has been processed
        ApplicationAssociate associate =
             ApplicationAssociate.getInstance(
                  getFacesContext().getExternalContext());
        associate.setRequestServiced();
        thrown = false;
        try {
            application.setVariableResolver(application.getVariableResolver());
        } catch (IllegalStateException e) {
            thrown = true;
        }
        assertTrue(thrown);

        // 6. Verify ISE occurs when attempting to set
        // a PropertyResolver after a request has been processed
        thrown = false;
        try {
            application.setPropertyResolver(application.getPropertyResolver());
        } catch (IllegalStateException e) {
            thrown = true;
        }
        assertTrue(thrown);

        // 7. Verify NullPointer exception which occurs when attempting
        //    to get a ValueBinding with a null ref
        //
        thrown = false;
        try {
            application.createValueBinding(null);
        } catch (Exception e) {
            thrown = true;
        }
        assertTrue(thrown);

        // 8.Verify NullPointerException occurs when attempting to pass a
        // null VariableResolver
        //
        thrown = false;
        try {
            application.setVariableResolver(null);
        } catch (NullPointerException e) {
            thrown = true;
        }
        assertTrue(thrown);
       
        // 9. Verify NullPointer exception which occurs when attempting
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


    public void testCreateComponentExtension() {
        application.addComponent(TestForm.COMPONENT_TYPE,
                                 TestForm.class.getName());
        UIComponent c = application.createComponent(TestForm.COMPONENT_TYPE);
        assertTrue(c != null);
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
        // RELEASE_PENDING - FIX.  There seems to be a problem
        // with the test framework exposing two different applicationassociate
        // instances.  As such, the flag denoting that a request has
        // been processed is never flagged and thus this test fails.
        /*
        ViewHandler handler = new ViewHandlerImpl();
        UIViewRoot root = Util.getViewHandler(getFacesContext()).createView(getFacesContext(), null);
        root.setViewId("/view");
        root.setId("id");
        root.setLocale(Locale.US);
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
        */
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

    public void testConverterUpdate() {        

        FacesContext context = getFacesContext();
        Application app = context.getApplication();

        Converter intConverter = application.createConverter("javax.faces.Integer");
        Converter intConverter2 = application.createConverter(Integer.TYPE);
        Converter intConverter3 = application.createConverter(Integer.class);

        assertTrue(IntegerConverter.class.equals(intConverter.getClass())
             && IntegerConverter.class.equals(intConverter2.getClass())
             && IntegerConverter.class.equals(intConverter3.getClass()));

        app.addConverter("javax.faces.Integer", CustomIntConverter.class.getName());

        intConverter = application.createConverter("javax.faces.Integer");
        intConverter2 = application.createConverter(Integer.TYPE);
        intConverter3 = application.createConverter(Integer.class);

        assertTrue(CustomIntConverter.class.equals(intConverter.getClass())
             && CustomIntConverter.class.equals(intConverter2.getClass())
             && CustomIntConverter.class.equals(intConverter3.getClass()));

        app.addConverter(Integer.TYPE, IntegerConverter.class.getName());

        intConverter = application.createConverter("javax.faces.Integer");
        intConverter2 = application.createConverter(Integer.TYPE);
        intConverter3 = application.createConverter(Integer.class);

        assertTrue(IntegerConverter.class.equals(intConverter.getClass())
             && IntegerConverter.class.equals(intConverter2.getClass())
             && IntegerConverter.class.equals(intConverter3.getClass()));

        app.addConverter(Integer.class, CustomIntConverter.class.getName());

        intConverter = application.createConverter("javax.faces.Integer");
        intConverter2 = application.createConverter(Integer.TYPE);
        intConverter3 = application.createConverter(Integer.class);

        assertTrue(CustomIntConverter.class.equals(intConverter.getClass())
             && CustomIntConverter.class.equals(intConverter2.getClass())
             && CustomIntConverter.class.equals(intConverter3.getClass()));

        // reset to the standard converter
        app.addConverter("javax.faces.Integer", IntegerConverter.class.getName());
    }


    public void testComponentAnnotatations() throws Exception {

        Application application = getFacesContext().getApplication();
        application.addComponent("CustomInput", CustomOutput.class.getName());
        CustomOutput c = (CustomOutput) application.createComponent("CustomInput");
        CustomOutput c2 = (CustomOutput) application.createComponent("CustomInput");
        UIViewRoot root = getFacesContext().getViewRoot();
        root.getChildren().add(c);
        root.getChildren().add(c2);
        assertTrue(c.getEvent() instanceof PostAddToViewEvent);
        assertTrue(c2.getEvent() instanceof PostAddToViewEvent);
        List<UIComponent> headComponents = root.getComponentResources(getFacesContext(), "head");
        System.out.println(headComponents.toString());
        assertTrue(headComponents.size() == 1);
        assertTrue(headComponents.get(0) instanceof UIOutput);
        assertTrue("test".equals(headComponents.get(0).getAttributes().get("library")));
        List<UIComponent> bodyComponents = root.getComponentResources(getFacesContext(), "body");
        assertTrue(bodyComponents.size() == 1);
        assertTrue(bodyComponents.get(0) instanceof UIOutput);
        assertTrue("test.js".equals(bodyComponents.get(0).getAttributes().get("name")));
        assertTrue("body".equals(bodyComponents.get(0).getAttributes().get("target")));

        application.addComponent("CustomInput2", CustomOutput2.class.getName());
        CustomOutput2 c3 = (CustomOutput2) application.createComponent("CustomInput2");
        root.getChildren().add(c3);
        assertTrue(c3.getEvent() instanceof PostAddToViewEvent);
        c3.reset();
        c3.encodeAll(getFacesContext());
        assertTrue(c3.getEvent() instanceof PreRenderComponentEvent);

    }


    public void testEvaluateExpressionGet() {

        FacesContext ctx = getFacesContext();
        ExternalContext extCtx = ctx.getExternalContext();
        Application app = getFacesContext().getApplication();

        extCtx.getRequestMap().put("date", new Date());
        Date d = app.evaluateExpressionGet(ctx, "#{requestScope.date}", Date.class);
        assertNotNull(d);
        extCtx.getRequestMap().put("list", new ArrayList());
        List l = app.evaluateExpressionGet(ctx, "#{requestScope.list}", List.class);
        assertNotNull(l);
        Object o = app.evaluateExpressionGet(ctx, "#{requestScope.list}", Object.class);
        assertNotNull(o);

    }


    public void testDecoratedFaceletFactory() {

        FacesContext ctx = getFacesContext();
        WebConfiguration webConfig = WebConfiguration.getInstance(ctx.getExternalContext());
        webConfig.overrideContextInitParameter(FaceletFactory,
                                               "com.sun.faces.application.TestApplicationImpl$CustomFaceletFactory");
        ctx.getExternalContext().getApplicationMap().remove("com.sun.faces.ApplicationAssociate");
        webConfig.overrideContextInitParameter(DisableFaceletJSFViewHandler,
                                               false);
        ApplicationImpl impl = new ApplicationImpl();
        ApplicationAssociate associate = ApplicationAssociate.getInstance(ctx.getExternalContext());
        assertEquals(CustomFaceletFactory.class.getName(),
                     CustomFaceletFactory.class.getName(),
                     associate.getFaceletFactory().getClass().getName());
        assertEquals(DefaultFaceletFactory.class.getName(),
                     DefaultFaceletFactory.class.getName(),
                     ((CustomFaceletFactory) associate.getFaceletFactory()).getDelegate().getClass().getName());

    }

    public void testOverrideFaceletFactory() {

        FacesContext ctx = getFacesContext();
        WebConfiguration webConfig = WebConfiguration.getInstance(ctx.getExternalContext());
        webConfig.overrideContextInitParameter(FaceletFactory,
                                               "com.sun.faces.application.TestApplicationImpl$CustomFaceletFactory2");
        ctx.getExternalContext().getApplicationMap().remove("com.sun.faces.ApplicationAssociate");
        webConfig.overrideContextInitParameter(DisableFaceletJSFViewHandler,
                                               false);
        ApplicationImpl impl = new ApplicationImpl();
        ApplicationAssociate associate = ApplicationAssociate.getInstance(ctx.getExternalContext());
        assertEquals(CustomFaceletFactory2.class.getName(),
                     CustomFaceletFactory2.class.getName(),
                     associate.getFaceletFactory().getClass().getName());

    }


    // ---------------------------------------------------------- Public Methods
    
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


    // ----------------------------------------------------------- Inner Classes

    public static final class CustomFaceletFactory2 extends FaceletFactory {
        public Facelet getFacelet(String uri) throws IOException {
            return null;
        }

        public Facelet getFacelet(URL url) throws IOException {
            return null;
        }

        public Facelet getMetadataFacelet(String uri) throws IOException {
            return null;
        }

        public Facelet getMetadataFacelet(URL url) throws IOException {
            return null;
        }

        public ResourceResolver getResourceResolver() {
            return null;
        }

        public long getRefreshPeriod() {
            return 0;
        }
    }

    public static final class CustomFaceletFactory extends FaceletFactory {

        private FaceletFactory delegate;

        public CustomFaceletFactory(FaceletFactory delegate) {
            this.delegate = delegate;
        }

        public Facelet getFacelet(String uri) throws IOException {
            return delegate.getFacelet(uri);
        }

        public Facelet getFacelet(URL url) throws IOException {
            return delegate.getFacelet(url);
        }

        public Facelet getMetadataFacelet(String uri) throws IOException {
            return delegate.getMetadataFacelet(uri);
        }

        public Facelet getMetadataFacelet(URL url) throws IOException {
            return delegate.getMetadataFacelet(url);
        }

        public ResourceResolver getResourceResolver() {
            return delegate.getResourceResolver();
        }

        public long getRefreshPeriod() {
            return delegate.getRefreshPeriod();
        }

        public FaceletFactory getDelegate() {
            return delegate;
        }

    }

    public static class CustomIntConverter implements Converter {

        private IntegerConverter delegate = new IntegerConverter();

        public Object getAsObject(FacesContext context, UIComponent component, String value) {
            return delegate.getAsObject(context, component, value);
        }

        public String getAsString(FacesContext context, UIComponent component, Object value) {
            return delegate.getAsString(context, component, value);
        }
    }

    @ListenerFor(systemEventClass=PostAddToViewEvent.class,
                 sourceClass= CustomOutput.class)
    @ResourceDependencies({
        @ResourceDependency(name="#{'test.js'}",library="test",target="#{'body'}"),
        @ResourceDependency(name="test.css",library="#{'test'}")
    })
    public static final class CustomOutput
          extends UIOutput
          implements ComponentSystemEventListener {

        private boolean processEventInvoked;
        private ComponentSystemEvent event;

        public void processEvent(ComponentSystemEvent event)
        throws AbortProcessingException {
            processEventInvoked = true;
            this.event = event;
        }

        public void reset() {
            processEventInvoked = false;
            event = null;
        }

        public boolean isProcessEventInvoked() {
            return processEventInvoked;
        }

        public ComponentSystemEvent getEvent() {
            return event;
        }
    }

    @ListenersFor({
        @ListenerFor(systemEventClass = PostAddToViewEvent.class,
                     sourceClass = CustomOutput.class),
        @ListenerFor(systemEventClass = PreRenderComponentEvent.class,
                     sourceClass = CustomOutput.class)
    })
    public static final class CustomOutput2
          extends UIOutput
          implements ComponentSystemEventListener {

        private boolean processEventInvoked;
        private ComponentSystemEvent event;

        public void processEvent(ComponentSystemEvent event)
              throws AbortProcessingException {
            processEventInvoked = true;
            this.event = event;
        }

        public void reset() {
            processEventInvoked = false;
            event = null;
        }

        public boolean isProcessEventInvoked() {
            return processEventInvoked;
        }

        public ComponentSystemEvent getEvent() {
            return event;
        }
    }


} // end of class TestApplicationImpl

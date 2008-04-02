/*
 * $Id: ValidatorTestCase.java,v 1.1 2004/05/12 02:00:49 eburns Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.validator;


import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.event.FacesEvent;
import javax.faces.mock.MockApplication;
import javax.faces.mock.MockExternalContext;
import javax.faces.mock.MockFacesContext;
import javax.faces.mock.MockHttpServletRequest;
import javax.faces.mock.MockHttpServletResponse;
import javax.faces.mock.MockHttpSession;
import javax.faces.mock.MockLifecycle;
import javax.faces.mock.MockRenderKit;
import javax.faces.mock.MockRenderKitFactory;
import javax.faces.mock.MockServletConfig;
import javax.faces.mock.MockServletContext;
import javax.faces.mock.MockValueBinding;
import javax.faces.TestUtil;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.faces.validator.Validator;
import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;

import javax.faces.component.UIViewRoot;

/**
 * <p>Base unit tests for all {@link Validator} implementations.</p>
 */

public class ValidatorTestCase extends TestCase {


    // ------------------------------------------------------ Instance Variables


    // Mock object instances for our tests
    protected MockApplication         application = null;
    protected MockServletConfig       config = null;
    protected MockExternalContext     externalContext = null;
    protected MockFacesContext        facesContext = null;
    protected MockLifecycle           lifecycle = null;
    protected MockHttpServletRequest  request = null;
    protected MockHttpServletResponse response = null;
    protected MockServletContext      servletContext = null;
    protected MockHttpSession         session = null;


    // ------------------------------------------------------------ Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public ValidatorTestCase(String name) {
        super(name);
    }


    // ---------------------------------------------------- Overall Test Methods


    // Set up instance variables required by this test case.
    public void setUp() throws Exception {

        // Set up Servlet API Objects
        servletContext = new MockServletContext();
        servletContext.addInitParameter("appParamName", "appParamValue");
        servletContext.setAttribute("appScopeName", "appScopeValue");
        config = new MockServletConfig(servletContext);
        session = new MockHttpSession();
        session.setAttribute("sesScopeName", "sesScopeValue");
        request = new MockHttpServletRequest(session);
        request.setAttribute("reqScopeName", "reqScopeValue");
        response = new MockHttpServletResponse();

        // Set up Faces API Objects
	FactoryFinder.setFactory(FactoryFinder.APPLICATION_FACTORY,
				 "javax.faces.mock.MockApplicationFactory");
	FactoryFinder.setFactory(FactoryFinder.RENDER_KIT_FACTORY,
				 "javax.faces.mock.MockRenderKitFactory");

        externalContext =
            new MockExternalContext(servletContext, request, response);
        lifecycle = new MockLifecycle();
        facesContext = new MockFacesContext(externalContext, lifecycle);
        ApplicationFactory applicationFactory = (ApplicationFactory)
            FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
        application = (MockApplication) applicationFactory.getApplication();
        facesContext.setApplication(application);
	UIViewRoot root = facesContext.getApplication().getViewHandler().createView(facesContext, null);
	root.setViewId("/viewId");
        facesContext.setViewRoot(root);
        RenderKitFactory renderKitFactory = (RenderKitFactory)
            FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
        RenderKit renderKit = new MockRenderKit();
        try {
            renderKitFactory.addRenderKit(RenderKitFactory.HTML_BASIC_RENDER_KIT,
                                          renderKit);
        } catch (IllegalArgumentException e) {
            ;
        }
        super.setUp();

    }


    // Return the tests included in this test case.
    public static Test suite() {
        return (new TestSuite(ValidatorTestCase.class));
    }


    // Tear down instance variables required by ths test case
    public void tearDown() throws Exception {

        super.tearDown();
        application = null;
        config = null;
        externalContext = null;
        facesContext = null;
        lifecycle = null;
        request = null;
        response = null;
        servletContext = null;
        session = null;

    }

    public void testNoOp() {
    }

}

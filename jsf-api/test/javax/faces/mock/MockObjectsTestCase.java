/*
 * $Id: MockObjectsTestCase.java,v 1.6.38.2 2007/04/27 21:27:01 ofung Exp $
 */

/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
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

package javax.faces.mock;


import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;
import javax.faces.component.UIViewRoot;
import javax.faces.el.MethodBinding;
import javax.faces.el.MethodNotFoundException;
import javax.faces.el.PropertyNotFoundException;
import javax.faces.el.ValueBinding;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;



/**
 * <p>Simple unit tests for Mock Objects that have behavior.</p>
 */


public class MockObjectsTestCase extends TestCase {


    // ------------------------------------------------------------ Constructors


    // Construct a new instance of this test case.
    public MockObjectsTestCase(String name) {
        super(name);
    }


    // ---------------------------------------------------- Overall Test Methods


    // Set up instance variables required by this test case.
    public void setUp() {

        // Set up Servlet API Objects
        servletContext = new MockServletContext();
        servletContext.addInitParameter("appParamName", "appParamValue");
        servletContext.setAttribute("appScopeName", "appScopeValue");
        servletContext.setAttribute("sameKey", "sameKeyAppValue");
        config = new MockServletConfig(servletContext);
        session = new MockHttpSession();
        session.setAttribute("sesScopeName", "sesScopeValue");
        session.setAttribute("sameKey", "sameKeySesValue");
        request = new MockHttpServletRequest(session);
        request.setAttribute("reqScopeName", "reqScopeValue");
        request.setAttribute("sameKey", "sameKeyReqValue");
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
	UIViewRoot root = new UIViewRoot();
	root.setViewId("/viewId");
        facesContext.setViewRoot(root);
        ApplicationFactory applicationFactory = (ApplicationFactory)
            FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
        application = (MockApplication) applicationFactory.getApplication();
        facesContext.setApplication(application);
        RenderKitFactory renderKitFactory = (RenderKitFactory)
            FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
        RenderKit renderKit = new MockRenderKit();
        try {
            renderKitFactory.addRenderKit(RenderKitFactory.HTML_BASIC_RENDER_KIT,
                                          renderKit);
        } catch (IllegalArgumentException e) {
            ;
        }

        request.setAttribute("test", new TestMockBean());

    }


    // Return the tests included in this test case.
    public static Test suite() {

        return (new TestSuite(MockObjectsTestCase.class));

    }


    // Tear down instance variables required by this test case.
    public void tearDown() {

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


    // ------------------------------------------------- Individual Test Methods


    public void testMethodBindingGetTypePositive() throws Exception {

        Class argsString[] = new Class[] { String.class };
        Class argsNone[] = new Class[0];

        checkMethodBindingGetType("test.getCommand", argsNone, String.class);
        checkMethodBindingGetType("test.setCommand", argsString,  null);
        checkMethodBindingGetType("test.getInput", argsNone, String.class);
        checkMethodBindingGetType("test.setInput", argsString, null);
        checkMethodBindingGetType("test.getOutput", argsNone, String.class);
        checkMethodBindingGetType("test.setOutput", argsString, null);
        checkMethodBindingGetType("test.combine", argsNone, String.class);

    }


    public void testMethodBindingInvokePositive() throws Exception {

        TestMockBean bean = (TestMockBean) request.getAttribute("test");
        MethodBinding mb = null;
        Class argsString[] = new Class[] { String.class };
        Class argsNone[] = new Class[0];
        assertEquals("::", bean.combine());

        mb = application.createMethodBinding("test.setCommand", argsString);
        mb.invoke(facesContext, new String[] { "command" });
        assertEquals("command", bean.getCommand());
        mb = application.createMethodBinding("test.setInput", argsString);
        mb.invoke(facesContext, new String[] { "input" });
        assertEquals("input", bean.getInput());
        mb = application.createMethodBinding("test.setOutput", argsString);
        mb.invoke(facesContext, new String[] { "output" });
        assertEquals("output", bean.getOutput());
        mb = application.createMethodBinding("test.combine", null);
        assertEquals("command:input:output", bean.combine());
        assertEquals("command:input:output", mb.invoke(facesContext, null));

    }


    // Positive tests for ValueBinding.getValue()
    public void testValueBindingGetValuePositive() throws Exception {

        // Implicit search
        checkValueBindingGetValue("appScopeName", "appScopeValue");
        checkValueBindingGetValue("sesScopeName", "sesScopeValue");
        checkValueBindingGetValue("reqScopeName", "reqScopeValue");
        checkValueBindingGetValue("sameKey", "sameKeyReqValue"); // Req scope

        // Explicit scope search
        checkValueBindingGetValue("applicationScope.appScopeName",
                                  "appScopeValue");
        checkValueBindingGetValue("applicationScope.sameKey",
                                  "sameKeyAppValue");
        checkValueBindingGetValue("sessionScope.sesScopeName",
                                  "sesScopeValue");
        checkValueBindingGetValue("sessionScope.sameKey",
                                  "sameKeySesValue");
        checkValueBindingGetValue("requestScope.reqScopeName",
                                  "reqScopeValue");
        checkValueBindingGetValue("requestScope.sameKey",
                                  "sameKeyReqValue");

    }


    // Positive tests for ValueBinding.putValue()
    public void testValueBindingPutValuePositive() throws Exception {

        ValueBinding vb = null;

        // New top-level variable
        assertNull(request.getAttribute("newSimpleName"));
        assertNull(session.getAttribute("newSimpleName"));
        assertNull(servletContext.getAttribute("newSimpleName"));
        vb = application.createValueBinding("newSimpleName");
        vb.setValue(facesContext, "newSimpleValue");
        assertEquals("newSimpleValue", request.getAttribute("newSimpleName"));
        assertNull(session.getAttribute("newSimpleName"));
        assertNull(servletContext.getAttribute("newSimpleName"));

        // New request-scope variable
        assertNull(request.getAttribute("newReqName"));
        assertNull(session.getAttribute("newReqName"));
        assertNull(servletContext.getAttribute("newReqName"));
        vb = application.createValueBinding("requestScope.newReqName");
        vb.setValue(facesContext, "newReqValue");
        assertEquals("newReqValue", request.getAttribute("newReqName"));
        assertNull(session.getAttribute("newReqName"));
        assertNull(servletContext.getAttribute("newReqName"));

        // New session-scope variable
        assertNull(request.getAttribute("newSesName"));
        assertNull(session.getAttribute("newSesName"));
        assertNull(servletContext.getAttribute("newSesName"));
        vb = application.createValueBinding("sessionScope.newSesName");
        vb.setValue(facesContext, "newSesValue");
        assertNull(request.getAttribute("newSesName"));
        assertEquals("newSesValue", session.getAttribute("newSesName"));
        assertNull(servletContext.getAttribute("newSesName"));

        // New application-scope variable
        assertNull(request.getAttribute("newAppName"));
        assertNull(session.getAttribute("newAppName"));
        assertNull(servletContext.getAttribute("newAppName"));
        vb = application.createValueBinding("applicationScope.newAppName");
        vb.setValue(facesContext, "newAppValue");
        assertNull(request.getAttribute("newAppName"));
        assertNull(session.getAttribute("newAppName"));
        assertEquals("newAppValue", servletContext.getAttribute("newAppName"));

        // Old top-level variable (just created)
        assertEquals("newSimpleValue", request.getAttribute("newSimpleName"));
        assertNull(session.getAttribute("newSimpleName"));
        assertNull(servletContext.getAttribute("newSimpleName"));
        vb = application.createValueBinding("newSimpleName");
        vb.setValue(facesContext, "newerSimpleValue");
        assertEquals("newerSimpleValue", request.getAttribute("newSimpleName"));
        assertNull(session.getAttribute("newSimpleName"));
        assertNull(servletContext.getAttribute("newSimpleName"));

        // Old hierarchically found variable
        assertEquals("sameKeyAppValue", servletContext.getAttribute("sameKey"));
        assertEquals("sameKeySesValue", session.getAttribute("sameKey"));
        assertEquals("sameKeyReqValue", request.getAttribute("sameKey"));
        vb = application.createValueBinding("sameKey");
        vb.setValue(facesContext, "sameKeyNewValue");
        assertEquals("sameKeyAppValue", servletContext.getAttribute("sameKey"));
        assertEquals("sameKeySesValue", session.getAttribute("sameKey"));
        assertEquals("sameKeyNewValue", request.getAttribute("sameKey"));


    }


    // --------------------------------------------------------- Private Methods


    private void checkMethodBindingGetType(String ref, Class params[],
                                           Class expected) throws Exception {

        MethodBinding mb = application.createMethodBinding(ref, params);
        assertNotNull("MethodBinding[" + ref + "] exists", mb);
        assertEquals("MethodBinding[" + ref + "] type",
                     expected,
                     mb.getType(facesContext));

    }


    private void checkValueBindingGetValue(String ref, Object expected) {

        ValueBinding vb = application.createValueBinding(ref);
        assertNotNull("ValueBinding[" + ref + "] exists", vb);
        assertEquals("ValueBinding[" + ref + "] value",
                     expected,
                     vb.getValue(facesContext));

    }


}

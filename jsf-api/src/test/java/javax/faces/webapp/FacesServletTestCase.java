
/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2010 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
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
package javax.faces.webapp;

import com.sun.faces.junit.JUnitFacesTestCase;
import com.sun.faces.mock.MockApplication;
import com.sun.faces.mock.MockExternalContext;
import com.sun.faces.mock.MockFacesContext;
import com.sun.faces.mock.MockHttpServletRequest;
import com.sun.faces.mock.MockHttpServletResponse;
import com.sun.faces.mock.MockHttpSession;
import com.sun.faces.mock.MockLifecycle;
import com.sun.faces.mock.MockRenderKit;
import com.sun.faces.mock.MockServletConfig;
import com.sun.faces.mock.MockServletContext;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import javax.faces.FactoryFinder;
import javax.faces.application.ApplicationFactory;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContextFactory;
import javax.faces.lifecycle.LifecycleFactory;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.servlet.http.HttpServletResponse;
import junit.framework.Test;
import junit.framework.TestSuite;

public class FacesServletTestCase extends JUnitFacesTestCase {
    
    // this is private in FacesServlet to not break backwards compatibility
    private static final String ALLOWED_HTTP_METHODS_ATTR_COPY = 
            "com.sun.faces.allowedHttpMethods";
    
    public FacesServletTestCase(String name) {
        super(name);
    }
    
    public static Test suite() {

        return (new TestSuite(FacesServletTestCase.class));

    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        // Set up Servlet API Objects
        servletContext.addInitParameter("appParamName", "appParamValue");
        servletContext.setAttribute("appScopeName", "appScopeValue");
        session.setAttribute("sesScopeName", "sesScopeValue");
        request.setAttribute("reqScopeName", "reqScopeValue");

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

    }
    
    public void testPositiveInitWithNoContextParams() throws Exception {
        FacesServlet me = new FacesServlet();
        me.init(config);
        this.sendRequest(me, "OPTIONS");
        assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        this.sendRequest(me, "GET");
        assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        this.sendRequest(me, "HEAD");
        assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        this.sendRequest(me, "POST");
        assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        this.sendRequest(me, "PUT");
        assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        this.sendRequest(me, "DELETE");
        assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        this.sendRequest(me, "TRACE");
        assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        this.sendRequest(me, "CONNECT");
        assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        
    }
    
    public void testPositiveInitWithContextParamsOfKnownHttpMethods() throws Exception {
        FacesServlet me = new FacesServlet();
        servletContext.addInitParameter(ALLOWED_HTTP_METHODS_ATTR_COPY, "GET   POST");
        me.init(config);
        this.sendRequest(me, "OPTIONS");
        assertEquals(HttpServletResponse.SC_BAD_REQUEST, response.getStatus());
        this.sendRequest(me, "GET");
        assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        this.sendRequest(me, "HEAD");
        assertEquals(HttpServletResponse.SC_BAD_REQUEST, response.getStatus());
        this.sendRequest(me, "POST");
        assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        this.sendRequest(me, "PUT");
        assertEquals(HttpServletResponse.SC_BAD_REQUEST, response.getStatus());
        this.sendRequest(me, "DELETE");
        assertEquals(HttpServletResponse.SC_BAD_REQUEST, response.getStatus());
        this.sendRequest(me, "TRACE");
        assertEquals(HttpServletResponse.SC_BAD_REQUEST, response.getStatus());
        this.sendRequest(me, "CONNECT");
        assertEquals(HttpServletResponse.SC_BAD_REQUEST, response.getStatus());
        
    }
    
    public void testNegativeInitWithContextParamsOfKnownHttpMethods() throws Exception {
        FacesServlet me = new FacesServlet();
        servletContext.addInitParameter(ALLOWED_HTTP_METHODS_ATTR_COPY, "GET   POST GET  POST");
        me.init(config);
        this.sendRequest(me, "OPTIONS");
        assertEquals(HttpServletResponse.SC_BAD_REQUEST, response.getStatus());
        this.sendRequest(me, "GET");
        assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        this.sendRequest(me, "HEAD");
        assertEquals(HttpServletResponse.SC_BAD_REQUEST, response.getStatus());
        this.sendRequest(me, "POST");
        assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        this.sendRequest(me, "PUT");
        assertEquals(HttpServletResponse.SC_BAD_REQUEST, response.getStatus());
        this.sendRequest(me, "DELETE");
        assertEquals(HttpServletResponse.SC_BAD_REQUEST, response.getStatus());
        this.sendRequest(me, "TRACE");
        assertEquals(HttpServletResponse.SC_BAD_REQUEST, response.getStatus());
        this.sendRequest(me, "CONNECT");
        assertEquals(HttpServletResponse.SC_BAD_REQUEST, response.getStatus());
        
    }
    
    public void testPositiveInitWithContextParamsOfWildcardHttpMethods() throws Exception {
        FacesServlet me = new FacesServlet();
        servletContext.addInitParameter(ALLOWED_HTTP_METHODS_ATTR_COPY, "*");
        me.init(config);
        this.sendRequest(me, "OPTIONS");
        assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        this.sendRequest(me, "GET");
        assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        this.sendRequest(me, "HEAD");
        assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        this.sendRequest(me, "POST");
        assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        this.sendRequest(me, "PUT");
        assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        this.sendRequest(me, "DELETE");
        assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        this.sendRequest(me, "TRACE");
        assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        this.sendRequest(me, "CONNECT");
        assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        this.sendRequest(me, "BOO_YA");
        assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        
    }
    
    public void testNegativeInitWithContextParamsOfWildcardHttpMethods() throws Exception {
        FacesServlet me = new FacesServlet();
        servletContext.addInitParameter(ALLOWED_HTTP_METHODS_ATTR_COPY, "* * * *");
        me.init(config);
        this.sendRequest(me, "OPTIONS");
        assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        this.sendRequest(me, "GET");
        assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        this.sendRequest(me, "HEAD");
        assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        this.sendRequest(me, "POST");
        assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        this.sendRequest(me, "PUT");
        assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        this.sendRequest(me, "DELETE");
        assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        this.sendRequest(me, "TRACE");
        assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        this.sendRequest(me, "CONNECT");
        assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        this.sendRequest(me, "BOO_YA");
        assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        
    }
    
    public void testPositiveInitWithContextParamsOfUnknownAndKnownHttpMethods() throws Exception {
        FacesServlet me = new FacesServlet();
        servletContext.addInitParameter(ALLOWED_HTTP_METHODS_ATTR_COPY, "GET\tPOST\tGETAAAAA");
        me.init(config);
        this.sendRequest(me, "OPTIONS");
        assertEquals(HttpServletResponse.SC_BAD_REQUEST, response.getStatus());
        this.sendRequest(me, "GET");
        assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        this.sendRequest(me, "HEAD");
        assertEquals(HttpServletResponse.SC_BAD_REQUEST, response.getStatus());
        this.sendRequest(me, "POST");
        assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        this.sendRequest(me, "PUT");
        assertEquals(HttpServletResponse.SC_BAD_REQUEST, response.getStatus());
        this.sendRequest(me, "DELETE");
        assertEquals(HttpServletResponse.SC_BAD_REQUEST, response.getStatus());
        this.sendRequest(me, "TRACE");
        assertEquals(HttpServletResponse.SC_BAD_REQUEST, response.getStatus());
        this.sendRequest(me, "CONNECT");
        assertEquals(HttpServletResponse.SC_BAD_REQUEST, response.getStatus());
        this.sendRequest(me, "BOO_YA");
        assertEquals(HttpServletResponse.SC_BAD_REQUEST, response.getStatus());
        this.sendRequest(me, "GETAAAAA");
        assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        
    }
    
    private void sendRequest(FacesServlet me, String method) throws Exception {
        request.setMethod(method);
        request.setPathElements("/test", "/test", "/test", "");
        me.service(request, response);

    }
}

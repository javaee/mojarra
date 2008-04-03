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


package com.sun.faces.application.resource;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;

import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import com.sun.faces.cactus.ServletFacesTestCase;
import org.apache.cactus.WebRequest;
import org.apache.cactus.WebResponse;
import org.apache.cactus.server.ServletContextWrapper;

/**
 * Tests com.sun.faces.application.resource.ResourceHandlerImpl
 */
public class TestResourceHandlerImpl extends ServletFacesTestCase {

     public TestResourceHandlerImpl() {
        super("TestResourceHandlerImpl");
    }


    public TestResourceHandlerImpl(String name) {
        super(name);
    }


    @Override
    public void setUp() {
        super.setUp();
    }


    @Override
    public void tearDown() {
        super.tearDown();
    }


    // ------------------------------------------------------------ Test Methods


    public void testCreateResource() throws Exception {

        ResourceHandler handler = getFacesContext().getApplication().getResourceHandler();
        assertTrue(handler != null);
        assertTrue(handler instanceof ResourceHandlerImpl);

        Resource resource = handler.createResource("duke-nv.gif");
        assertTrue(resource != null);
        assertTrue(resource.getLibraryName() == null);
        assertTrue("duke-nv.gif".equals(resource.getResourceName()));
        assertTrue("image/gif".equals(resource.getContentType()));

        resource = handler.createResource("duke-nv.gif", "nvLibrary");
        assertTrue(resource != null);
        assertTrue("nvLibrary".equals(resource.getLibraryName()));
        assertTrue("duke-nv.gif".equals(resource.getResourceName()));
        assertTrue("image/gif".equals(resource.getContentType()));

        resource = handler.createResource("images/duke-nv.gif", "nvLibrary");
        assertTrue(resource != null);
        assertTrue("nvLibrary".equals(resource.getLibraryName()));
        assertTrue("images/duke-nv.gif".equals(resource.getResourceName()));
        assertTrue("image/gif".equals(resource.getContentType()));

        resource = handler.createResource("duke-nv.gif", "nvLibrary", "text/xml");
        assertTrue(resource != null);
        assertTrue("nvLibrary".equals(resource.getLibraryName()));
        assertTrue("duke-nv.gif".equals(resource.getResourceName()));
        assertTrue("text/xml".equals(resource.getContentType()));

        resource = handler.createResource("foo.jpg");
        assertTrue(resource == null);

        resource = handler.createResource("duke-nv.gif", "nonExistant");
        assertTrue(resource == null);
        
    }


    public void beginIsResourceRequestPrefixMapped(WebRequest req) {
        req.setURL("localhost:8080", "/test", "/faces/", "/javax.faces.resource/duke-nv.gif", null);
    }

    public void testIsResourceRequestPrefixMapped() throws Exception {
        ResourceHandler handler = getFacesContext().getApplication().getResourceHandler();
        assertTrue(handler != null);

        assertTrue(handler.isResourceRequest(getFacesContext()));
    }


    public void beginIsResourceRequestExtensionMapped(WebRequest req) {
        req.setURL("localhost:8080", "/test", "/javax.faces.resource/duke-nv.gif.faces", null, null);
    }

    public void testIsResourceRequestExtensionMapped() throws Exception {
        ResourceHandler handler = getFacesContext().getApplication().getResourceHandler();
        assertTrue(handler != null);

        assertTrue(handler.isResourceRequest(getFacesContext()));
    }


    public void beginIsResourceRequestExcludesPrefixMapped1(WebRequest req) {
        req.setURL("localhost:8080", "/test", "/faces/", "/javax.faces.resource/test.jsp", null);
    }

    public void testIsResourceRequestExcludesPrefixMapped1() throws Exception {
        ResourceHandler handler = getFacesContext().getApplication().getResourceHandler();
        assertTrue(handler != null);

        assertTrue(!handler.isResourceRequest(getFacesContext()));
    }


    public void beginIsResourceRequestExcludesPrefixMapped2(WebRequest req) {
        req.setURL("localhost:8080", "/test", "/faces/", "/javax.faces.resource/test.properties", null);
    }

    public void testIsResourceRequestExcludesPrefixMapped2() throws Exception {
        ResourceHandler handler = getFacesContext().getApplication().getResourceHandler();
        assertTrue(handler != null);

        assertTrue(!handler.isResourceRequest(getFacesContext()));
    }


    public void beginIsResourceRequestExcludesPrefixMapped3(WebRequest req) {
        req.setURL("localhost:8080", "/test", "/faces/", "/javax.faces.resource/test.xhtml", null);
    }

    public void testIsResourceRequestExcludesPrefixMapped3() throws Exception {
        ResourceHandler handler = getFacesContext().getApplication().getResourceHandler();
        assertTrue(handler != null);

        assertTrue(!handler.isResourceRequest(getFacesContext()));
    }


    public void beginIsResourceRequestExcludesPrefixMapped4(WebRequest req) {
        req.setURL("localhost:8080", "/test", "/faces/", "/javax.faces.resource/test.class", null);
    }

    public void testIsResourceRequestExcludesPrefixMapped4() throws Exception {
        ResourceHandler handler = getFacesContext().getApplication().getResourceHandler();
        assertTrue(handler != null);

        assertTrue(!handler.isResourceRequest(getFacesContext()));
    }


    public void beginIsResourceRequestExcludeExtensionMapped1(WebRequest req) {
        req.setURL("localhost:8080", "/test", "/javax.faces.resource/duke-nv.jsp.faces", null, null);
    }

    public void testIsResourceRequestExcludeExtensionMapped1() throws Exception {
        ResourceHandler handler = getFacesContext().getApplication().getResourceHandler();
        assertTrue(handler != null);

        assertTrue(!handler.isResourceRequest(getFacesContext()));
    }


    public void beginIsResourceRequestExcludeExtensionMapped2(WebRequest req) {
        req.setURL("localhost:8080", "/test", "/javax.faces.resource/duke-nv.properties.faces", null, null);
    }

    public void testIsResourceRequestExcludeExtensionMapped2() throws Exception {
        ResourceHandler handler = getFacesContext().getApplication().getResourceHandler();
        assertTrue(handler != null);

        assertTrue(!handler.isResourceRequest(getFacesContext()));
    }


    public void beginIsResourceRequestExcludeExtensionMapped3(WebRequest req) {
        req.setURL("localhost:8080", "/test", "/javax.faces.resource/duke-nv.xhtml.faces", null, null);
    }

    public void testIsResourceRequestExcludeExtensionMapped3() throws Exception {
        ResourceHandler handler = getFacesContext().getApplication().getResourceHandler();
        assertTrue(handler != null);

        assertTrue(!handler.isResourceRequest(getFacesContext()));
    }


    public void beginIsResourceRequestExcludeExtensionMapped4(WebRequest req) {
        req.setURL("localhost:8080", "/test", "/javax.faces.resource/duke-nv.class.faces", null, null);
    }

    public void testIsResourceRequestExcludeExtensionMapped4() throws Exception {
        ResourceHandler handler = getFacesContext().getApplication().getResourceHandler();
        assertTrue(handler != null);

        assertTrue(!handler.isResourceRequest(getFacesContext()));
    }

    ////////////////////////////////////////////////////////////////////////////
    //  The next 5 tests validate a user specified exclude.
    //  In this case, .gif is excluded as a valid resource request.
    //  This should cause the default exclusions of .jsp, .class, .xhtml, and
    //  .properties to now be considered valid
    ////////////////////////////////////////////////////////////////////////////
    public void beginUserSpecifiedResourceExclude1(WebRequest req) {
        req.setURL("localhost:8080", "/test", "/javax.faces.resource/duke-nv.gif.faces", null, null);
    }

    public void testUserSpecifiedResourceExclude1() throws Exception {
        // documenting this once - this is hack in order to support dynamic init
        // parameters. Unfortunately, the config object (which one can obtain
        // the ServletContextWrapper from isn't available at the time the
        // 'begin' methods are invoked.  So instead, leverage the knowledge that
        // the init parameters are checked when the ResourceHandlerImpl is constructed
        // and set the init parameters in the context before constructing.
        ServletContextWrapper wrapper = (ServletContextWrapper) getFacesContext().getExternalContext().getContext();
        wrapper.setInitParameter("javax.faces.resource.EXCLUDES", ".gif");
        ResourceHandler handler = new ResourceHandlerImpl();

        assertTrue(!handler.isResourceRequest(getFacesContext()));
    }


    public void beginUserSpecifiedResourceExclude2(WebRequest req) {
        req.setURL("localhost:8080", "/test", "/javax.faces.resource/duke-nv.class.faces", null, null);
    }

    public void testUserSpecifiedResourceExclude2() throws Exception {
        ServletContextWrapper wrapper = (ServletContextWrapper) getFacesContext().getExternalContext().getContext();
        wrapper.setInitParameter("javax.faces.resource.EXCLUDES", ".gif");
        ResourceHandler handler = new ResourceHandlerImpl();

        assertTrue(handler.isResourceRequest(getFacesContext()));
    }


    public void beginUserSpecifiedResourceExclude3(WebRequest req) {
        req.setURL("localhost:8080", "/test", "/javax.faces.resource/duke-nv.properties.faces", null, null);
    }

    public void testUserSpecifiedResourceExclude3() throws Exception {
        ServletContextWrapper wrapper = (ServletContextWrapper) getFacesContext().getExternalContext().getContext();
        wrapper.setInitParameter("javax.faces.resource.EXCLUDES", ".gif");
        ResourceHandler handler = new ResourceHandlerImpl();

        assertTrue(handler.isResourceRequest(getFacesContext()));
    }


    public void beginUserSpecifiedResourceExclude4(WebRequest req) {
        req.setURL("localhost:8080", "/test", "/javax.faces.resource/duke-nv.xhtml.faces", null, null);
    }

    public void testUserSpecifiedResourceExclude4() throws Exception {
        ServletContextWrapper wrapper = (ServletContextWrapper) getFacesContext().getExternalContext().getContext();
        wrapper.setInitParameter("javax.faces.resource.EXCLUDES", ".gif");
        ResourceHandler handler = new ResourceHandlerImpl();

        assertTrue(handler.isResourceRequest(getFacesContext()));
    }


    public void beginUserSpecifiedResourceExclude5(WebRequest req) {
        req.setURL("localhost:8080", "/test", "/javax.faces.resource/duke-nv.jsp.faces", null, null);
    }

    public void testUserSpecifiedResourceExclude5() throws Exception {
        ServletContextWrapper wrapper = (ServletContextWrapper) getFacesContext().getExternalContext().getContext();
        wrapper.setInitParameter("javax.faces.resource.EXCLUDES", ".gif");
        ResourceHandler handler = new ResourceHandlerImpl();

        assertTrue(handler.isResourceRequest(getFacesContext()));
    }


    public void beginHandleResourceRequest1(WebRequest req) {
        req.setURL("localhost:8080", "/test", "/javax.faces.resource/duke-nv.gif.faces", null, null);
    }

    public void testHandleResourceRequest1() throws Exception {

        ResourceHandler handler = getFacesContext().getApplication().getResourceHandler();
        assertTrue(handler != null);
        HttpServletResponse response = (HttpServletResponse) getFacesContext().getExternalContext().getResponse();
        TestResponseWrapper wrapper = new TestResponseWrapper(response);
        getFacesContext().getExternalContext().setResponse(wrapper);
        byte[] control = getBytes(getFacesContext().getExternalContext().getResource("/resources/duke-nv.gif"));
        handler.handleResourceRequest(getFacesContext());
        byte[] test = wrapper.getBytes();
        assertTrue(Arrays.equals(control, test));
        assertTrue(response.containsHeader("content-length"));
        assertTrue(response.containsHeader("cache-control"));
        assertTrue(response.containsHeader("content-type"));
    }


    public void beginHandleResourceRequest2(WebRequest req) {
        req.setURL("localhost:8080", "/test", "/javax.faces.resource/duke-nv.gif.faces?ln=nvLibrary", null, null);
    }

    public void testHandleResourceRequest2() throws Exception {

        ResourceHandler handler =
              getFacesContext().getApplication().getResourceHandler();
        assertTrue(handler != null);
        HttpServletResponse response = (HttpServletResponse) getFacesContext()
              .getExternalContext().getResponse();
        TestResponseWrapper wrapper = new TestResponseWrapper(response);
        getFacesContext().getExternalContext().setResponse(wrapper);
        byte[] control = getBytes(getFacesContext()
              .getExternalContext().getResource("/resources/nvLibrary/duke-nv.gif"));
        handler.handleResourceRequest(getFacesContext());
        byte[] test = wrapper.getBytes();
        assertTrue(Arrays.equals(control, test));
        assertTrue(response.containsHeader("content-length"));
        assertTrue(response.containsHeader("cache-control"));
        assertTrue(response.containsHeader("content-type"));
        
    }




// ---------------------------------------------------------- Helper Methods


    private byte[] getBytes(URL url) throws Exception {

        URLConnection c = url.openConnection();
        c.setUseCaches(false);
        InputStream in = c.getInputStream();
        return getBytes(in);

    }

    private byte[] getBytes(InputStream in) throws Exception {

        ByteArrayOutputStream o = new ByteArrayOutputStream();
        for (int i = in.read(); i != -1; i = in.read()) {
            o.write(i);
        }
        in.close();
        return o.toByteArray();

    }


// ----------------------------------------------------------- Inner Classes


    private static class TestResponseWrapper extends HttpServletResponseWrapper {

        private TestServletOutputStream out;
        public byte[] getBytes() {
            return out.getBytes();
        }

        public TestResponseWrapper(HttpServletResponse httpServletResponse) {
            super(httpServletResponse);
        }

        public ServletOutputStream getOutputStream() throws IOException {
            out = new TestServletOutputStream(super.getOutputStream());
            return out;
        }

        private class TestServletOutputStream extends ServletOutputStream {
            private ServletOutputStream wrapped;
            private ByteArrayOutputStream out = new ByteArrayOutputStream();

            public TestServletOutputStream(ServletOutputStream wrapped) {
                this.wrapped = wrapped;
            }
            public void write(int b) throws IOException {
                wrapped.write(b);
                out.write(b);
            }

            public void write(byte b[]) throws IOException {
                wrapped.write(b);
                out.write(b);
            }

            public void write(byte b[], int off, int len) throws IOException {
                wrapped.write(b, off, len);
                out.write(b, off, len);
            }

            public void flush() throws IOException {
                wrapped.flush();
                out.flush();
            }

            public void close() throws IOException {
                wrapped.close();
                out.close();
            }

            public byte[] getBytes() {
                return out.toByteArray();
            }
        }
    }

}

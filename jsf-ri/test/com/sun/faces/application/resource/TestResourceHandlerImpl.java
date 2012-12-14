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


package com.sun.faces.application.resource;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.zip.GZIPOutputStream;

import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import com.sun.faces.cactus.ServletFacesTestCase;
import com.sun.faces.config.WebConfiguration;
import com.sun.faces.util.Util;
import com.sun.faces.application.ApplicationAssociate;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import javax.faces.application.Application;
import org.apache.cactus.WebRequest;
import org.apache.cactus.WebResponse;

/**
 * Tests com.sun.faces.application.resource.ResourceHandlerImpl
 */
public class TestResourceHandlerImpl extends ServletFacesTestCase {

    /* HTTP Date format required by the HTTP/1.1 RFC */
    private static final String RFC1123_DATE_PATTERN =
          "EEE, dd MMM yyyy HH:mm:ss zzz";

    private static final TimeZone GMT = TimeZone.getTimeZone("GMT");


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


    public void testAjaxIsAvailable() {

        ResourceHandler handler = getFacesContext().getApplication().getResourceHandler();
        assertTrue(handler != null);
        assertTrue(handler instanceof ResourceHandlerImpl);

        assertNotNull(handler.createResource("jsf.js", "javax.faces"));
    }

    public void testAjaxCompression() throws Exception {
        
        ResourceHandler handler = getFacesContext().getApplication().getResourceHandler();
        Resource resource =  handler.createResource("jsf-uncompressed.js", "javax.faces");

        InputStream stream = resource.getInputStream();

        int origSize = getBytes(stream).length;

        resource =  handler.createResource("jsf.js", "javax.faces");

        stream = resource.getInputStream();

        int compSize = getBytes(stream).length;

        //  If we're not getting 30% compression, something's gone horribly wrong.
        assertTrue("compressed file less than 30% smaller: orig "+origSize+" comp: "+compSize,
                origSize * 0.7 > compSize);

    }


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

        resource = handler.createResource("duke-nv.gif", "nvLibrary", null);
        assertTrue(resource != null);
        assertTrue("nvLibrary".equals(resource.getLibraryName()));
        assertTrue("duke-nv.gif".equals(resource.getResourceName()));
        assertTrue("image/gif".equals(resource.getContentType()));

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


    ////////////////////////////////////////////////////////////////////////////
    public void beginHandleResourceRequestExcludesPrefixMapped1(WebRequest req) {
        req.setURL("localhost:8080", "/test", "/faces/", "/javax.faces.resource/test.jsp", null);
    }

    public void testHandleResourceRequestExcludesPrefixMapped1() throws Exception {
        ResourceHandler handler = getFacesContext().getApplication().getResourceHandler();
        assertTrue(handler != null);

        handler.handleResourceRequest(getFacesContext());
    }

    public void endHandleResourceRequestExcludesPrefixMapped1(WebResponse res) {
        assertTrue(res.getStatusCode() == 404);
    }


    ////////////////////////////////////////////////////////////////////////////
    public void beginHandleResourceRequestExcludesPrefixMapped2(WebRequest req) {
        req.setURL("localhost:8080", "/test", "/faces/", "/javax.faces.resource/test.properties", null);
    }

    public void testHandleResourceRequestExcludesPrefixMapped2() throws Exception {
        ResourceHandler handler = getFacesContext().getApplication().getResourceHandler();
        assertTrue(handler != null);

        handler.handleResourceRequest(getFacesContext());
    }

    public void endHandleResourceRequestExcludesPrefixMapped2(WebResponse res) {
        assertTrue(res.getStatusCode() == 404);
    }


    ////////////////////////////////////////////////////////////////////////////
    public void beginHandleResourceRequestExcludesPrefixMapped3(WebRequest req) {
        req.setURL("localhost:8080", "/test", "/faces/", "/javax.faces.resource/test.xhtml", null);
    }

    public void testHandleResourceRequestExcludesPrefixMapped3() throws Exception {
        ResourceHandler handler = getFacesContext().getApplication().getResourceHandler();
        assertTrue(handler != null);

        handler.handleResourceRequest(getFacesContext());
    }

    public void endHandleResourceRequestExcludesPrefixMapped3(WebResponse res) {
        assertTrue(res.getStatusCode() == 404);
    }


    ////////////////////////////////////////////////////////////////////////////
    public void beginHandleResourceRequestExcludesPrefixMapped4(WebRequest req) {
        req.setURL("localhost:8080", "/test", "/faces/", "/javax.faces.resource/test.class", null);
    }

    public void testHandleResourceRequestExcludesPrefixMapped4() throws Exception {
        ResourceHandler handler = getFacesContext().getApplication().getResourceHandler();
        assertTrue(handler != null);

        handler.handleResourceRequest(getFacesContext());
    }

    public void endHandleResourceRequestExcludesPrefixMapped4(WebResponse res) {
        assertTrue(res.getStatusCode() == 404);
    }

    ////////////////////////////////////////////////////////////////////////////
    public void beginHandleResourceRequestExcludeExtensionMapped1(WebRequest req) {
        req.setURL("localhost:8080", "/test", "/javax.faces.resource/duke-nv.jsp.faces", null, null);
    }

    public void testHandleResourceRequestExcludeExtensionMapped1() throws Exception {
        ResourceHandler handler = getFacesContext().getApplication().getResourceHandler();
        assertTrue(handler != null);

        handler.handleResourceRequest(getFacesContext());
    }

    public void endHandleResourceRequestExcludesExtensionMapped1(WebResponse res) {
        assertTrue(res.getStatusCode() == 404);
    }


    ////////////////////////////////////////////////////////////////////////////
    public void beginHandleResourceRequestExcludeExtensionMapped2(WebRequest req) {
        req.setURL("localhost:8080", "/test", "/javax.faces.resource/duke-nv.properties.faces", null, null);
    }

    public void testHandleResourceRequestExcludeExtensionMapped2() throws Exception {
        ResourceHandler handler = getFacesContext().getApplication().getResourceHandler();
        assertTrue(handler != null);

        handler.handleResourceRequest(getFacesContext());
    }

    public void endHandleResourceRequestExcludesExtensionMapped2(WebResponse res) {
        assertTrue(res.getStatusCode() == 404);
    }


    ////////////////////////////////////////////////////////////////////////////
    public void beginHandleResourceRequestExcludeExtensionMapped3(WebRequest req) {
        req.setURL("localhost:8080", "/test", "/javax.faces.resource/duke-nv.xhtml.faces", null, null);
    }

    public void testHandleResourceRequestExcludeExtensionMapped3() throws Exception {
        ResourceHandler handler = getFacesContext().getApplication().getResourceHandler();
        assertTrue(handler != null);

        handler.handleResourceRequest(getFacesContext());
    }

    public void endHandleResourceRequestExcludesExtensionMapped3(WebResponse res) {
        assertTrue(res.getStatusCode() == 404);
    }


    ////////////////////////////////////////////////////////////////////////////
    public void beginHandleResourceRequestExcludeExtensionMapped4(WebRequest req) {
        req.setURL("localhost:8080", "/test", "/javax.faces.resource/duke-nv.class.faces", null, null);
    }

    public void testHandleResourceRequestExcludeExtensionMapped4() throws Exception {
        ResourceHandler handler = getFacesContext().getApplication().getResourceHandler();
        assertTrue(handler != null);

        handler.handleResourceRequest(getFacesContext());
    }

    public void endHandleResourceRequestExcludesExtensionMapped4(WebResponse res) {
        assertTrue(res.getStatusCode() == 404);
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
        WebConfiguration webconfig = WebConfiguration.getInstance(getFacesContext().getExternalContext());
        webconfig.overrideContextInitParameter(WebConfiguration.WebContextInitParameter.ResourceExcludes, ".gif");
        ResourceHandler handler = new ResourceHandlerImpl();
        Application app = getFacesContext().getApplication();
        ResourceHandler oldResourceHandler = app.getResourceHandler();
        app.setResourceHandler(handler);

	try {
	    handler.handleResourceRequest(getFacesContext());
	} finally {
            app.setResourceHandler(oldResourceHandler);
        }

    }

    public void endUserSpecifiedResourceExclude1(WebResponse res) {
        assertTrue(res.getStatusCode() == 404);
    }

    ////////////////////////////////////////////////////////////////////////////
    public void beginUserSpecifiedResourceExclude2(WebRequest req) {
        req.setURL("localhost:8080", "/test", "/javax.faces.resource/com.sun.faces.application.ApplicationImpl.class.faces", null, null);
    }

    public void testUserSpecifiedResourceExclude2() throws Exception {
        WebConfiguration webconfig = WebConfiguration.getInstance(getFacesContext().getExternalContext());
        webconfig.overrideContextInitParameter(WebConfiguration.WebContextInitParameter.ResourceExcludes, ".gif");
        ResourceHandler handler = new ResourceHandlerImpl();
        Application app = getFacesContext().getApplication();
        ResourceHandler oldResourceHandler = app.getResourceHandler();
        app.setResourceHandler(handler);

	try {
	    assertTrue(handler.isResourceRequest(getFacesContext()));
	} finally {
            app.setResourceHandler(oldResourceHandler);
        }

    }

    public void endUserSpecifiedResourceExclude2(WebResponse res) {
        assertTrue(res.getStatusCode() == 200);
    }


    ////////////////////////////////////////////////////////////////////////////
    public void beginUserSpecifiedResourceExclude3(WebRequest req) {
        req.setURL("localhost:8080", "/test", "/javax.faces.resource/com.sun.faces.LogStrings.properties.faces", null, null);
    }

    public void testUserSpecifiedResourceExclude3() throws Exception {
        WebConfiguration webconfig = WebConfiguration.getInstance(getFacesContext().getExternalContext());
        webconfig.overrideContextInitParameter(WebConfiguration.WebContextInitParameter.ResourceExcludes, ".gif");
        ResourceHandler handler = new ResourceHandlerImpl();
        Application app = getFacesContext().getApplication();
        ResourceHandler oldResourceHandler = app.getResourceHandler();
        app.setResourceHandler(handler);

	try {
	    assertTrue(handler.isResourceRequest(getFacesContext()));
	} finally {
            app.setResourceHandler(oldResourceHandler);
        }

    }

    public void endUserSpecifiedResourceExclude3(WebResponse res) {
        assertTrue(res.getStatusCode() == 200);
    }



    //==========================================================================
    // Validate a resource streamed from the docroot of a webapp
    //
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
        assertTrue(response.containsHeader("last-modified"));
        assertTrue(response.containsHeader("expires"));
        assertTrue(response.containsHeader("etag"));
        assertTrue(response.containsHeader("content-type"));
        
    }


    //==========================================================================
    // Validate a resource streamed from a JAR
    //
    public void beginHandleResourceRequest2(WebRequest req) {
        req.setURL("localhost:8080", "/test", "/javax.faces.resource/duke-nv.gif.faces", null, null);
        req.addParameter("ln", "nvLibrary-jar");
    }

    public void testHandleResourceRequest2() throws Exception {

        ResourceHandler handler =
              getFacesContext().getApplication().getResourceHandler();
        assertTrue(handler != null);
        HttpServletResponse response = (HttpServletResponse) getFacesContext()
              .getExternalContext().getResponse();
        TestResponseWrapper wrapper = new TestResponseWrapper(response);
        getFacesContext().getExternalContext().setResponse(wrapper);
        byte[] control = getBytes(Util.getCurrentLoader(this)
              .getResource("META-INF/resources/nvLibrary-jar/duke-nv.gif"));
        handler.handleResourceRequest(getFacesContext());
        byte[] test = wrapper.getBytes();
        assertTrue(Arrays.equals(control, test));
        assertTrue(response.containsHeader("content-length"));
        assertTrue(response.containsHeader("last-modified"));
        assertTrue(response.containsHeader("expires"));
        assertTrue(response.containsHeader("etag"));
        assertTrue(response.containsHeader("content-type"));
        
    }


    //==========================================================================
    // Validate a 304 is returned when a request contains the If-Modified-Since
    // request header and the resource hasn't changed on the server side.
    //
    public void beginHandleResourceRequest3(WebRequest req) {
        req.setURL("localhost:8080", "/test", "/javax.faces.resource/duke-nv.gif.faces", null, null);
        long 
                curTime = System.currentTimeMillis(),
                threeHoursAgo = curTime - 10800000L;
        facesService.setModificationTime("resources/duke-nv.gif", 
                threeHoursAgo);
        facesService.setModificationTime("resources/nvLibrary/duke-nv.gif",
                threeHoursAgo);
        SimpleDateFormat format =
                new SimpleDateFormat(RFC1123_DATE_PATTERN, Locale.US);
        format.setTimeZone(GMT);
        Date headerValue = new Date(curTime);
        
        req.addParameter("ln", "nvLibrary");
        req.addHeader("If-Modified-Since", format.format(headerValue));
    }


    public void testHandleResourceRequest3() throws Exception {
        ResourceHandler handler =
              getFacesContext().getApplication().getResourceHandler();
        assertTrue(handler != null);
        handler.handleResourceRequest(getFacesContext());
    }

    public void endHandleResourceRequest3(WebResponse res) {
        assertTrue(res.getStatusCode() == 304);
    }


    //==========================================================================
    // Validate a 404 is returned when a request for a non existant resource
    // is made
    //
    public void beginHandleResourceRequest4(WebRequest req) {
        req.setURL("localhost:8080", "/test", "/javax.faces.resource/duke-v.gif.faces", null, null);
        req.addParameter("ln", "nvLibrary");
    }


    public void testHandleResourceRequest4() throws Exception {
        ResourceHandler handler =
              getFacesContext().getApplication().getResourceHandler();
        assertTrue(handler != null);
        handler.handleResourceRequest(getFacesContext());
    }

    public void endHandleResourceRequest4(WebResponse res) {
        assertTrue(res.getStatusCode() == 404);
    }


    //==========================================================================
    // Validate a 404 is returned when a request for an excluded resource is made
    //
    public void beginHandleResourceRequest5(WebRequest req) {
        req.setURL("localhost:8080", "/test", "/javax.faces.resource/duke-nv.class.faces", null, null);
        req.addParameter("ln", "nvLibrary");
    }


    public void testHandleResourceRequest5() throws Exception {
        ResourceHandler handler =
              getFacesContext().getApplication().getResourceHandler();
        assertTrue(handler != null);
        handler.handleResourceRequest(getFacesContext());
    }

    public void endHandleResourceRequest5(WebResponse res) {
        assertTrue(res.getStatusCode() == 404);
    }


    //==========================================================================
    // Validate a resource streamed from the docroot of a webapp is compressed
    //
    public void beginHandleResourceRequest6(WebRequest req) {
        req.setURL("localhost:8080", "/test", "/javax.faces.resource/duke-nv.gif.faces", null, null);
        req.addHeader("accept-encoding", "deflate");
        req.addHeader("accept-encoding", "gzip");
    }

    public void testHandleResourceRequest6() throws Exception {

        WebConfiguration config = WebConfiguration.getInstance();
        config.overrideContextInitParameter(WebConfiguration.WebContextInitParameter.CompressableMimeTypes, "image/gif");
        ApplicationAssociate associate = ApplicationAssociate.getInstance(getFacesContext().getExternalContext());
        Application app = getFacesContext().getApplication();
        ResourceHandler oldResourceHandler = app.getResourceHandler();
        associate.setResourceManager(new ResourceManager(associate.getResourceCache()));
        ResourceHandler handler = new ResourceHandlerImpl();
        app.setResourceHandler(handler);

        HttpServletResponse response = (HttpServletResponse) getFacesContext().getExternalContext().getResponse();
        TestResponseWrapper wrapper = new TestResponseWrapper(response);
        getFacesContext().getExternalContext().setResponse(wrapper);
        byte[] control = getBytes(getFacesContext().getExternalContext().getResource("/resources/duke-nv.gif"), true);
        handler.handleResourceRequest(getFacesContext());
        byte[] test = wrapper.getBytes();
        try {
	    assertTrue(Arrays.equals(control, test));
	    assertTrue(response.containsHeader("content-length"));
	    assertTrue(response.containsHeader("last-modified"));
	    assertTrue(response.containsHeader("expires"));
	    assertTrue(response.containsHeader("etag"));
	    assertTrue(response.containsHeader("content-type"));
	    assertTrue(response.containsHeader("content-encoding"));
        } finally {
            app.setResourceHandler(oldResourceHandler);
        }

    }


    //==========================================================================
    // Validate a resource streamed from a JAR is compressed
    //
    public void beginHandleResourceRequest7(WebRequest req) {
//        req.setURL("localhost:8080", "/test", "/javax.faces.resource/duke-nv.gif.faces", null, null);
//        req.addParameter("ln", "nvLibrary-jar");
//        req.addHeader("accept-encoding", "gzip,deflate");
    }

    public void testHandleResourceRequest7() throws Exception {
//
//        WebConfiguration config = WebConfiguration.getInstance();
//        config.overrideContextInitParameter(WebConfiguration.WebContextInitParameter.CompressableMimeTypes, "image/gif");
//        ApplicationAssociate associate = ApplicationAssociate.getInstance(getFacesContext().getExternalContext());
//        Application app = getFacesContext().getApplication();
//        ResourceHandler oldResourceHandler = app.getResourceHandler();
//        associate.setResourceManager(new ResourceManager(associate.getResourceCache()));
//        ResourceHandler handler = new ResourceHandlerImpl();
//        app.setResourceHandler(handler);
//        HttpServletResponse response = (HttpServletResponse) getFacesContext()
//              .getExternalContext().getResponse();
//        TestResponseWrapper wrapper = new TestResponseWrapper(response);
//        getFacesContext().getExternalContext().setResponse(wrapper);
//        byte[] control = getBytes(Util.getCurrentLoader(this)
//              .getResource("META-INF/resources/nvLibrary-jar/duke-nv.gif"), true);
//        handler.handleResourceRequest(getFacesContext());
//        byte[] test = wrapper.getBytes();
//        try {
//            assertTrue(Arrays.equals(control, test));
//            assertTrue(response.containsHeader("content-length"));
//            assertTrue(response.containsHeader("last-modified"));
//            assertTrue(response.containsHeader("expires"));
//            assertTrue(response.containsHeader("etag"));
//            assertTrue(response.containsHeader("content-type"));
//            assertTrue(response.containsHeader("content-encoding"));
//        } finally {
//            app.setResourceHandler(oldResourceHandler);
//        }
    }

    //==========================================================================
    // Validate a resource streamed from the docroot of a webapp isn't compressed
    // when the client doesn't send the accept-encoding request header
    //
    public void beginHandleResourceRequest8(WebRequest req) {
        req.setURL("localhost:8080", "/test", "/javax.faces.resource/duke-nv.gif.faces", null, null);
    }

    public void testHandleResourceRequest8() throws Exception {

        WebConfiguration config = WebConfiguration.getInstance();
        config.overrideContextInitParameter(WebConfiguration.WebContextInitParameter.CompressableMimeTypes, "image/gif");
        ApplicationAssociate associate = ApplicationAssociate.getInstance(getFacesContext().getExternalContext());
        associate.setResourceManager(new ResourceManager(associate.getResourceCache()));
        ResourceHandler handler = new ResourceHandlerImpl();
        Application app = getFacesContext().getApplication();
        ResourceHandler oldResourceHandler = app.getResourceHandler();
        app.setResourceHandler(handler);
        HttpServletResponse response = (HttpServletResponse) getFacesContext().getExternalContext().getResponse();
        TestResponseWrapper wrapper = new TestResponseWrapper(response);
        getFacesContext().getExternalContext().setResponse(wrapper);
        byte[] control = getBytes(getFacesContext().getExternalContext().getResource("/resources/duke-nv.gif"));
        handler.handleResourceRequest(getFacesContext());
        byte[] test = wrapper.getBytes();
	try {
	    assertTrue(Arrays.equals(control, test));
	    assertTrue(response.containsHeader("content-length"));
	    assertTrue(response.containsHeader("last-modified"));
	    assertTrue(response.containsHeader("expires"));
	    assertTrue(response.containsHeader("etag"));
	    assertTrue(response.containsHeader("content-type"));
	    assertTrue(!response.containsHeader("content-encoding"));
	} finally {
            app.setResourceHandler(oldResourceHandler);
        }

    }


    //==========================================================================
    // Validate a resource streamed from a JAR isn't compressed
    // when the client doesn't send the accept-encoding request header
    //
    public void beginHandleResourceRequest9(WebRequest req) {
//        req.setURL("localhost:8080", "/test", "/javax.faces.resource/duke-nv.gif.faces", null, null);
//        req.addParameter("ln", "nvLibrary-jar");
    }

    public void testHandleResourceRequest9() throws Exception {

//        WebConfiguration config = WebConfiguration.getInstance();
//        config.overrideContextInitParameter(WebConfiguration.WebContextInitParameter.CompressableMimeTypes, "image/gif");
//        ApplicationAssociate associate = ApplicationAssociate.getInstance(getFacesContext().getExternalContext());
//        associate.setResourceManager(new ResourceManager(associate.getResourceCache()));
//        ResourceHandler handler = new ResourceHandlerImpl();
//        Application app = getFacesContext().getApplication();
//        ResourceHandler oldResourceHandler = app.getResourceHandler();
//        app.setResourceHandler(handler);
//        HttpServletResponse response = (HttpServletResponse) getFacesContext()
//              .getExternalContext().getResponse();
//        TestResponseWrapper wrapper = new TestResponseWrapper(response);
//        getFacesContext().getExternalContext().setResponse(wrapper);
//        byte[] control = getBytes(getFacesContext()
//              .getExternalContext().getResource("/resources/nvLibrary/duke-nv.gif"));
//        handler.handleResourceRequest(getFacesContext());
//        byte[] test = wrapper.getBytes();
//	try {
//	    assertTrue(Arrays.equals(control, test));
//	    assertTrue(response.containsHeader("content-length"));
//	    assertTrue(response.containsHeader("last-modified"));
//	    assertTrue(response.containsHeader("expires"));
//	    assertTrue(response.containsHeader("etag"));
//	    assertTrue(response.containsHeader("content-type"));
//	    assertTrue(!response.containsHeader("content-encoding"));
//	} finally {
//            app.setResourceHandler(oldResourceHandler);
//        }

    }


    //==========================================================================
    // Validate an accept-encoding of gzip;q=0 means non-compressed content
    // is sent to the user-agent
    //
    public void beginHandleResourceRequest10(WebRequest req) {
        req.setURL("localhost:8080", "/test", "/javax.faces.resource/duke-nv.gif.faces", null, null);
        req.addHeader("accept-encoding", "gzip;q=0, deflate");
    }

    public void testHandleResourceRequest10() throws Exception {

        WebConfiguration config = WebConfiguration.getInstance();
        config.overrideContextInitParameter(WebConfiguration.WebContextInitParameter.CompressableMimeTypes, "image/gif");
        ApplicationAssociate associate = ApplicationAssociate.getInstance(getFacesContext().getExternalContext());
        associate.setResourceManager(new ResourceManager(associate.getResourceCache()));
        ResourceHandler handler = new ResourceHandlerImpl();
        Application app = getFacesContext().getApplication();
        ResourceHandler oldResourceHandler = app.getResourceHandler();
        app.setResourceHandler(handler);
        HttpServletResponse response = (HttpServletResponse) getFacesContext().getExternalContext().getResponse();
        TestResponseWrapper wrapper = new TestResponseWrapper(response);
        getFacesContext().getExternalContext().setResponse(wrapper);
        byte[] control = getBytes(getFacesContext().getExternalContext().getResource("/resources/duke-nv.gif"), false);
        handler.handleResourceRequest(getFacesContext());
        byte[] test = wrapper.getBytes();
	try {
	    assertTrue(Arrays.equals(control, test));
	    assertTrue(response.containsHeader("content-length"));
	    assertTrue(response.containsHeader("last-modified"));
	    assertTrue(response.containsHeader("expires"));
	    assertTrue(response.containsHeader("etag"));
	    assertTrue(response.containsHeader("content-type"));
	    assertTrue(!response.containsHeader("content-encoding"));
	} finally {
            app.setResourceHandler(oldResourceHandler);
        }

	    
    }


    //==========================================================================
    // Validate an accept-encoding of that doesn't include gzip, and includes
    // *;q=0 will not send compressed content to the user-agent
    // is sent to the user-agent
    //
    public void beginHandleResourceRequest11(WebRequest req) {
//        req.setURL("localhost:8080", "/test", "/javax.faces.resource/duke-nv.gif.faces", null, null);
//        req.addHeader("accept-encoding", "deflate");
//        req.addHeader("accept-encoding", "*;q=0");
    }

    public void testHandleResourceRequest11() throws Exception {

//        WebConfiguration config = WebConfiguration.getInstance();
//        config.overrideContextInitParameter(WebConfiguration.WebContextInitParameter.CompressableMimeTypes, "image/gif");
//        ApplicationAssociate associate = ApplicationAssociate.getInstance(getFacesContext().getExternalContext());
//        associate.setResourceManager(new ResourceManager(associate.getResourceCache()));
//        ResourceHandler handler = new ResourceHandlerImpl();
//        Application app = getFacesContext().getApplication();
//        ResourceHandler oldResourceHandler = app.getResourceHandler();
//        app.setResourceHandler(handler);
//        HttpServletResponse response = (HttpServletResponse) getFacesContext().getExternalContext().getResponse();
//        TestResponseWrapper wrapper = new TestResponseWrapper(response);
//        getFacesContext().getExternalContext().setResponse(wrapper);
//        byte[] control = getBytes(getFacesContext().getExternalContext().getResource("/resources/duke-nv.gif"), false);
//        handler.handleResourceRequest(getFacesContext());
//        byte[] test = wrapper.getBytes();
//	try {
//	    assertTrue(Arrays.equals(control, test));
//	    assertTrue(response.containsHeader("content-length"));
//	    assertTrue(response.containsHeader("last-modified"));
//	    assertTrue(response.containsHeader("expires"));
//	    assertTrue(response.containsHeader("etag"));
//	    assertTrue(response.containsHeader("content-type"));
//	    assertTrue(!response.containsHeader("content-encoding"));
//	} finally {
//            app.setResourceHandler(oldResourceHandler);
//        }
//
//
    }


    //==========================================================================
    // Validate an accept-encoding of that doesn't include gzip, and includes
    // * will send compressed content
    //
    public void beginHandleResourceRequest12(WebRequest req) {
        req.setURL("localhost:8080", "/test", "/javax.faces.resource/duke-nv.gif.faces", null, null);
        req.addHeader("accept-encoding", "identity;q=1.0");
        req.addHeader("accept-encoding", "*;q=0.5");
        req.addHeader("accept-encoding", "deflate;q=1.0");
    }

    public void testHandleResourceRequest12() throws Exception {

        WebConfiguration config = WebConfiguration.getInstance();
        config.overrideContextInitParameter(WebConfiguration.WebContextInitParameter.CompressableMimeTypes, "image/gif");
        ApplicationAssociate associate = ApplicationAssociate.getInstance(getFacesContext().getExternalContext());
        associate.setResourceManager(new ResourceManager(associate.getResourceCache()));
        ResourceHandler handler = new ResourceHandlerImpl();
        Application app = getFacesContext().getApplication();
        ResourceHandler oldResourceHandler = app.getResourceHandler();
        app.setResourceHandler(handler);
        HttpServletResponse response = (HttpServletResponse) getFacesContext().getExternalContext().getResponse();
        TestResponseWrapper wrapper = new TestResponseWrapper(response);
        getFacesContext().getExternalContext().setResponse(wrapper);
        byte[] control = getBytes(getFacesContext().getExternalContext().getResource("/resources/duke-nv.gif"), true);
        handler.handleResourceRequest(getFacesContext());
        byte[] test = wrapper.getBytes();
	try {
	    assertTrue(Arrays.equals(control, test));
	    assertTrue(response.containsHeader("content-length"));
	    assertTrue(response.containsHeader("last-modified"));
	    assertTrue(response.containsHeader("expires"));
	    assertTrue(response.containsHeader("etag"));
	    assertTrue(response.containsHeader("content-type"));
	    assertTrue(response.containsHeader("content-encoding"));
	} finally {
            app.setResourceHandler(oldResourceHandler);
        }

    }


    //==========================================================================
    // Validate an accept-encoding of that doesn't include gzip will not send
    // compressed content.
    //
    public void beginHandleResourceRequest13(WebRequest req) {
        req.setURL("localhost:8080", "/test", "/javax.faces.resource/duke-nv.gif.faces", null, null);
        req.addHeader("accept-encoding", "identity;q=0.5, deflate;q=1.0");
    }

    public void testHandleResourceRequest13() throws Exception {

        WebConfiguration config = WebConfiguration.getInstance();
        config.overrideContextInitParameter(WebConfiguration.WebContextInitParameter.CompressableMimeTypes, "image/gif");
        ApplicationAssociate associate = ApplicationAssociate.getInstance(getFacesContext().getExternalContext());
        associate.setResourceManager(new ResourceManager(associate.getResourceCache()));
        ResourceHandler handler = new ResourceHandlerImpl();
        Application app = getFacesContext().getApplication();
        ResourceHandler oldResourceHandler = app.getResourceHandler();
        app.setResourceHandler(handler);
        HttpServletResponse response = (HttpServletResponse) getFacesContext().getExternalContext().getResponse();
        TestResponseWrapper wrapper = new TestResponseWrapper(response);
        getFacesContext().getExternalContext().setResponse(wrapper);
        byte[] control = getBytes(getFacesContext().getExternalContext().getResource("/resources/duke-nv.gif"), false);
        handler.handleResourceRequest(getFacesContext());
        byte[] test = wrapper.getBytes();
        assertTrue(Arrays.equals(control, test));
	try {
	    assertTrue(response.containsHeader("content-length"));
	    assertTrue(response.containsHeader("last-modified"));
	    assertTrue(response.containsHeader("expires"));
	    assertTrue(response.containsHeader("etag"));
	    assertTrue(response.containsHeader("content-type"));
	    assertTrue(!response.containsHeader("content-encoding"));
	} finally {
            app.setResourceHandler(oldResourceHandler);
        }

    }

    public void testLibraryExistsNegative() throws Exception {
        ResourceHandler handler = getFacesContext().getApplication().getResourceHandler();
        assertNotNull(handler);

        assertFalse(handler.libraryExists("oeunhtnhtnhhnhh"));

    }


    //==========================================================================
    // Validate the fix for issue 1162.
    //
    public void beginHandleResourceRequest14(WebRequest req) {
        req.setURL("localhost:8080", "/test", "/javax.faces.resource/web.xml.faces", null, "ln=../WEB-INF");
    }

    public void testHandleResourceRequest14() throws Exception {

        ResourceHandler handler =
              getFacesContext().getApplication().getResourceHandler();
        assertTrue(handler != null);
        handler.handleResourceRequest(getFacesContext());
        
    }

    public void endHandleResourceRequest14(WebResponse res) {
        assertTrue(res.getStatusCode() == 404);
    }


    //==========================================================================
    // Validate the fix for issue 1162.
    //
    public void beginHandleResourceRequest15(WebRequest req) {
        req.setURL("localhost:8080", "/test", "/javax.faces.resource/web.xml.faces", null, "ln=nvLibrary/../../WEB-INF");
    }

    public void testHandleResourceRequest15() throws Exception {

        ResourceHandler handler =
              getFacesContext().getApplication().getResourceHandler();
        assertTrue(handler != null);
        handler.handleResourceRequest(getFacesContext());

    }

    public void endHandleResourceRequest15(WebResponse res) {
        assertTrue(res.getStatusCode() == 404);
    }



// ---------------------------------------------------------- Helper Methods


    private byte[] getBytes(URL url) throws Exception {

        return getBytes(url, false);

    }

    private byte[] getBytes(URL url, boolean compress) throws Exception {
        URLConnection c = url.openConnection();
        c.setUseCaches(false);
        InputStream in = c.getInputStream();
        return ((compress) ? getCompressedBytes(in) : getBytes(in));
    }

    private byte[] getBytes(InputStream in) throws Exception {

        ByteArrayOutputStream o = new ByteArrayOutputStream();
        for (int i = in.read(); i != -1; i = in.read()) {
            o.write(i);
        }
        in.close();
        return o.toByteArray();

    }

    private byte[] getCompressedBytes(InputStream in) throws Exception {

        ByteArrayOutputStream o = new ByteArrayOutputStream();
        GZIPOutputStream compress = new GZIPOutputStream(o);
        for (int i = in.read(); i != -1; i = in.read()) {
            compress.write(i);
        }
        compress.flush();
        compress.close();
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

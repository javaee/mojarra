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
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;

import com.sun.faces.cactus.ServletFacesTestCase;
import com.sun.faces.cactus.TestingUtil;
import com.sun.faces.util.Util;
import com.sun.faces.util.RequestStateManager;
import org.apache.cactus.WebRequest;

/**
 * Test class for com.sun.faces.application.resource.ResourceImpl
 */
public class TestResourceImpl extends ServletFacesTestCase {

    public TestResourceImpl() {
        super("TestResourceImpl");
    }


    public TestResourceImpl(String name) {
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

    public void beginToURIPrefixMapping(WebRequest req) {
        req.setURL("localhost:8080", "/test", "/faces", "/foo.jsp", null);
    }

    public void testToURIPrefixMapping() throws Exception {
        ResourceHandler handler = getFacesContext().getApplication().getResourceHandler();
        assertTrue (handler != null);

        Resource resource = handler.createResource("duke-nv.gif");
        assertTrue (resource != null);
        String expectedURI = "/test/faces/javax.faces.resource/duke-nv.gif";
        assertTrue(expectedURI.equals(resource.getRequestPath()));

        resource = handler.createResource("duke-nv.gif", "nvLibrary");
        assertTrue(resource != null);
        expectedURI = "/test/faces/javax.faces.resource/duke-nv.gif?ln=nvLibrary";
        assertTrue(expectedURI.equals(resource.getRequestPath()));

        resource = handler.createResource("duke.gif");
        assertTrue(resource != null);
        expectedURI = "/test/faces/javax.faces.resource/duke.gif?v=1.1";
        assertTrue(expectedURI.equals(resource.getRequestPath()));

        resource = handler.createResource("duke.gif", "nvLibrary");
        assertTrue(resource != null);
        expectedURI = "/test/faces/javax.faces.resource/duke.gif?ln=nvLibrary&v=1.1";
        assertTrue(expectedURI.equals(resource.getRequestPath()));

        resource = handler.createResource("duke.gif", "vLibrary");
        assertTrue(resource != null);
        expectedURI = "/test/faces/javax.faces.resource/duke.gif?ln=vLibrary&v=2.01.1";
        assertTrue(expectedURI.equals(resource.getRequestPath()));

        resource = handler.createResource("duke-nv.gif", "vLibrary");
        assertTrue(resource != null);
        expectedURI = "/test/faces/javax.faces.resource/duke-nv.gif?ln=vLibrary&v=2.0";
        assertTrue(expectedURI.equals(resource.getRequestPath()));

    }

    public void beginToURIExtensionMapping(WebRequest req) {
        req.setURL("localhost:8080", "/test", "/foo.faces", null, null);
    }

    public void testToURIExtensionMapping() throws Exception {
        ResourceHandler handler = getFacesContext().getApplication().getResourceHandler();
        assertTrue (handler != null);

        Resource resource = handler.createResource("duke-nv.gif");
        assertTrue (resource != null);
        String expectedURI = "/test/javax.faces.resource/duke-nv.gif.faces";
        assertTrue(expectedURI.equals(resource.getRequestPath()));

        resource = handler.createResource("duke-nv.gif", "nvLibrary");
        assertTrue(resource != null);
        expectedURI = "/test/javax.faces.resource/duke-nv.gif.faces?ln=nvLibrary";
        assertTrue(expectedURI.equals(resource.getRequestPath()));

        resource = handler.createResource("duke.gif");
        assertTrue(resource != null);
        expectedURI = "/test/javax.faces.resource/duke.gif.faces?v=1.1";
        assertTrue(expectedURI.equals(resource.getRequestPath()));

        resource = handler.createResource("duke.gif", "nvLibrary");
        assertTrue(resource != null);
        expectedURI = "/test/javax.faces.resource/duke.gif.faces?ln=nvLibrary&v=1.1";
        assertTrue(expectedURI.equals(resource.getRequestPath()));

        resource = handler.createResource("duke.gif", "vLibrary");
        assertTrue(resource != null);
        expectedURI = "/test/javax.faces.resource/duke.gif.faces?ln=vLibrary&v=2.01.1";
        assertTrue(expectedURI.equals(resource.getRequestPath()));

        resource = handler.createResource("duke-nv.gif", "vLibrary");
        assertTrue(resource != null);
        expectedURI = "/test/javax.faces.resource/duke-nv.gif.faces?ln=vLibrary&v=2.0";
        assertTrue(expectedURI.equals(resource.getRequestPath()));

    }

    public void testWebppResourceGetInputStream() throws Exception {

        // validate the behavior of getInputStream() for a webapp-based resource
        ResourceHandler handler = getFacesContext().getApplication().getResourceHandler();
        assertTrue (handler != null);

        // step 1 - non-versioned
        byte[] controlBytes = getBytes(getFacesContext().getExternalContext().getResource("/resources/duke-nv.gif"));
        Resource resource = handler.createResource("duke-nv.gif");
        assertTrue(resource != null);
        InputStream in = resource.getInputStream();
        assertTrue(in != null);
        byte[] underTest = getBytes(in);
        assertTrue(Arrays.equals(controlBytes, underTest));

        // step 2 - versioned
        controlBytes = getBytes(getFacesContext().getExternalContext().getResource("/resources/duke.gif/1.1"));
        resource = handler.createResource("duke.gif");
        assertTrue(resource != null);
        in = resource.getInputStream();
        assertTrue(in != null);
        underTest = getBytes(in);
        assertTrue(Arrays.equals(controlBytes, underTest));
        
    }


    public void testJarResourceGetInputStream() throws Exception {

        // validate the behavior of getInputStream() for a webapp-based resource
        ResourceHandler handler = getFacesContext().getApplication().getResourceHandler();
        assertTrue (handler != null);

        // step 1 - non-versioned
        byte[] controlBytes = getBytes(Util.getCurrentLoader(this.getClass()).getResource("META-INF/resources/duke-jar-nv.gif"));
        Resource resource = handler.createResource("duke-jar-nv.gif");
        assertTrue(resource != null);
        InputStream in = resource.getInputStream();
        assertTrue(in != null);
        byte[] underTest = getBytes(in);
        assertTrue(Arrays.equals(controlBytes, underTest));

        // step 2 - versioned
        controlBytes = getBytes(Util.getCurrentLoader(this.getClass()).getResource("META-INF/resources/duke-jar.gif/1.1"));
        resource = handler.createResource("duke-jar.gif");
        assertTrue(resource != null);
        in = resource.getInputStream();
        assertTrue(in != null);
        underTest = getBytes(in);
        assertTrue(Arrays.equals(controlBytes, underTest));

    }


    public void testWebappGetURL() throws Exception {

        // validate the behavior of getURL() for a webapp-based resource
        ResourceHandler handler = getFacesContext().getApplication().getResourceHandler();
        assertTrue (handler != null);

        URL control = getFacesContext().getExternalContext().getResource("/resources/duke-nv.gif");
        assertTrue(control != null);
        Resource resource = handler.createResource("duke-nv.gif");
        assertTrue(resource != null);
        URL test = resource.getURL();
        assertTrue(test != null);
        assertTrue(control.equals(test));

    }


    public void testJarGetURL() throws Exception {

        // validate the behavior of getURL() for a webapp-based resource
        ResourceHandler handler = getFacesContext().getApplication().getResourceHandler();
        assertTrue (handler != null);

        URL control = Util.getCurrentLoader(this.getClass()).getResource("META-INF/resources/duke-jar-nv.gif");
        assertTrue(control != null);
        Resource resource = handler.createResource("duke-jar-nv.gif");
        assertTrue(resource != null);
        URL test = resource.getURL();
        assertTrue(test != null);
        assertTrue(control.equals(test));

    }



    public void testGetContentType() throws Exception {

        ResourceHandler handler = getFacesContext().getApplication().getResourceHandler();
        assertTrue (handler != null);

        // non-versioned resource containing one path element
        Resource resource = handler.createResource("duke-jar.gif");
        assertTrue(resource != null);
        assertTrue("image/gif".equals(resource.getContentType()));

        // versioned resource containing one path element
        resource = handler.createResource("duke.gif");
        assertTrue(resource != null);
        assertTrue("image/gif".equals(resource.getContentType()));

        // non-versioned resource containing multiple path elements
        resource = handler.createResource("images/duke-nv.gif", "nvLibrary");
        assertTrue(resource != null);
        assertTrue("image/gif".equals(resource.getContentType()));

    }


    public void testDefaultHeaders() throws Exception {

        ResourceHandler handler = getFacesContext().getApplication().getResourceHandler();
        assertTrue(handler != null);
        RequestStateManager.set(getFacesContext(),
                                RequestStateManager.RESOURCE_REQUEST,
                                Boolean.TRUE);
        Resource resource = handler.createResource("duke-jar.gif");
        assertTrue(resource != null);
        Map<String,String> headers = resource.getResponseHeaders();
        assertTrue(headers != null);
        assertTrue(headers.size() == 3);
        assertTrue(headers.get("Expires") != null);
        assertTrue(headers.get("ETag") != null);
        assertTrue(headers.get("Last-Modified") != null);

        // now assert that an empty map is returned if we're not servicing
        // a resource request
        RequestStateManager.set(getFacesContext(),
                                RequestStateManager.RESOURCE_REQUEST,
                                Boolean.FALSE);
        headers = resource.getResponseHeaders();
        assertTrue(headers.isEmpty());
        
    }


    @SuppressWarnings({"deprecation"})
    public void testUserAgentNeedsUpdate1() throws Exception {

        ResourceHandler handler = getFacesContext().getApplication().getResourceHandler();
        assertTrue(handler != null);

        // no If-Modified-Since request header, so this should always
        // return true
        Resource resource = handler.createResource("duke-nv.gif");
        assertTrue(resource.userAgentNeedsUpdate(getFacesContext()));

        // set the creation date of the ResourceHandler back in time so that
        // if the header was present it would return true - the lack of the header
        // should result in true being returned in this case
        Date date = new Date();
        date.setYear(1980);
        long origTime = (Long) TestingUtil.invokePrivateMethod("getCreationTime",
                                                               null,
                                                               null,
                                                               ResourceHandlerImpl.class,
                                                               handler);
        TestingUtil.invokePrivateMethod("setCreationTime",
                                        new Class[] { Long.TYPE },
                                        new Object[] { date.getTime() },
                                        ResourceHandlerImpl.class,
                                        handler);
        assertTrue(resource.userAgentNeedsUpdate(getFacesContext()));
        TestingUtil.invokePrivateMethod("setCreationTime",
                                        new Class[] { Long.TYPE },
                                        new Object[] { origTime },
                                        ResourceHandlerImpl.class,
                                        handler);
    }


    public void beginUserAgentNeedsUpdate2(WebRequest req) {
        req.addHeader("If-Modified-Since", "Sat, 29 Oct 1994 19:43:31 GMT");
    }

    public void testUserAgentNeedsUpdate2() throws Exception {

        ResourceHandler handler = getFacesContext().getApplication().getResourceHandler();
        assertTrue(handler != null);
        // If-Modified-Since request header, so this should always
        // return true
        Resource resource = handler.createResource("duke-nv.gif");
        assertTrue(!resource.userAgentNeedsUpdate(getFacesContext()));

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

}

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

package com.sun.faces.application.resource;

import com.sun.faces.cactus.ServletFacesTestCase;
import com.sun.faces.cactus.TestingUtil;
import com.sun.faces.util.Util;
import org.apache.cactus.WebRequest;

import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.context.FacesContext;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Test class for com.sun.faces.application.resource.ResourceImpl
 */
public class TestResourceImpl extends ServletFacesTestCase {

    /* HTTP Date format required by the HTTP/1.1 RFC */
    private static final String RFC1123_DATE_PATTERN =
          "EEE, dd MMM yyyy HH:mm:ss zzz";

    private static final TimeZone GMT = TimeZone.getTimeZone("GMT");

    public TestResourceImpl() {
        super("TestResourceImpl");
	initLocalHostPath();
    }


    public TestResourceImpl(String name) {
        super(name);
	initLocalHostPath();
    }

    private String localHostPath = "localhost:8080";

    private void initLocalHostPath() {
	String containerPort = System.getProperty("container.port");
	if (null == containerPort || 0 == containerPort.length()) {
	    containerPort = "8080";
	}
	localHostPath = "localhost:" + containerPort;
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
        req.setURL(localHostPath, "/test", "/faces", "/foo.jsp", null);
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
        expectedURI = "/test/faces/javax.faces.resource/duke.gif?v=1_1";
        assertTrue(expectedURI.equals(resource.getRequestPath()));

        resource = handler.createResource("duke.gif", "nvLibrary");
        assertTrue(resource != null);
        expectedURI = "/test/faces/javax.faces.resource/duke.gif?ln=nvLibrary&v=1_1";
        assertTrue(expectedURI.equals(resource.getRequestPath()));

        resource = handler.createResource("duke.gif", "vLibrary");
        assertTrue(resource != null);
        expectedURI = "/test/faces/javax.faces.resource/duke.gif?ln=vLibrary&v=2_01_1";
        assertTrue(expectedURI.equals(resource.getRequestPath()));

        resource = handler.createResource("duke-nv.gif", "vLibrary");
        assertTrue(resource != null);
        expectedURI = "/test/faces/javax.faces.resource/duke-nv.gif?ln=vLibrary&v=2_0";
        assertTrue(expectedURI.equals(resource.getRequestPath()));

    }
    
    public void testFaceletResources() throws Exception {
        ResourceHandler handler = getFacesContext().getApplication().getResourceHandler();
        assertTrue (handler != null);

        Resource resource = handler.createResource("test.xhtml");
        assertNotNull(resource);
    }

    public void beginToURIExtensionMapping(WebRequest req) {
        req.setURL(localHostPath, "/test", "/foo.faces", null, null);
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
        expectedURI = "/test/javax.faces.resource/duke.gif.faces?v=1_1";
        assertTrue(expectedURI.equals(resource.getRequestPath()));

        resource = handler.createResource("duke.gif", "nvLibrary");
        assertTrue(resource != null);
        expectedURI = "/test/javax.faces.resource/duke.gif.faces?ln=nvLibrary&v=1_1";
        assertTrue(expectedURI.equals(resource.getRequestPath()));

        resource = handler.createResource("duke.gif", "vLibrary");
        assertTrue(resource != null);
        expectedURI = "/test/javax.faces.resource/duke.gif.faces?ln=vLibrary&v=2_01_1";
        assertTrue(expectedURI.equals(resource.getRequestPath()));

        resource = handler.createResource("duke-nv.gif", "vLibrary");
        assertTrue(resource != null);
        expectedURI = "/test/javax.faces.resource/duke-nv.gif.faces?ln=vLibrary&v=2_0";
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
        controlBytes = getBytes(getFacesContext().getExternalContext().getResource("/resources/duke.gif/1_1.gif"));
        resource = handler.createResource("duke.gif");
        assertTrue(resource != null);
        in = resource.getInputStream();
        assertTrue(in != null);
        underTest = getBytes(in);
        assertTrue(Arrays.equals(controlBytes, underTest));
        
    }

    public void testEqualsOnResourceAndRelatedClasses() throws Exception {
        // validate the behavior of getInputStream() for a webapp-based resource
        ResourceHandler handler = getFacesContext().getApplication().getResourceHandler();
        assertTrue (handler != null);

        Object
                x = handler.createResource("duke-nv.gif", "nvLibrary", "image/gif"),
                y = handler.createResource("duke-nv.gif", "nvLibrary", "image/gif"),
                z = handler.createResource("duke-nv.gif", "nvLibrary", "image/gif");
        this.verifyEqualsContractPositive(x, y, z);

        y = handler.createResource("simple.css");
        assertFalse(x.equals(y));


        VersionInfo
                viA = new VersionInfo("1.0", null),
                viB = new VersionInfo("1.0", null),
                viC = new VersionInfo("1.0", null);
        this.verifyEqualsContractPositive(viA, viB, viC);

        ResourceHelper helper = new ClasspathResourceHelper();
        FacesContext context = this.getFacesContext();

        LibraryInfo
                liA = helper.findLibrary("vLibrary-jar", null, null, context),
                liB = helper.findLibrary("vLibrary-jar", null, null, context),
                liC = helper.findLibrary("vLibrary-jar", null, null, context);
        this.verifyEqualsContractPositive(liA, liB, liC);

        liB = helper.findLibrary("vLibrary", null, null, context);
        assertFalse(liA.equals(liB));


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
        /*
        controlBytes = getBytes(Util.getCurrentLoader(this.getClass()).getResource("META-INF/resources/duke-jar.gif/1_1.gif"));
        resource = handler.createResource("duke-jar.gif");
        assertTrue(resource != null);
        in = resource.getInputStream();
        assertTrue(in != null);
        underTest = getBytes(in);
        assertTrue(Arrays.equals(controlBytes, underTest));
        */

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

        req.addHeader("If-Modified-Since", format.format(headerValue));
    }

    public void testUserAgentNeedsUpdate2() throws Exception {

        ResourceHandler handler = getFacesContext().getApplication().getResourceHandler();
        assertTrue(handler != null);
        // If-Modified-Since request header, so this should always
        // return true
        Resource resource = handler.createResource("duke-nv.gif");
        assertTrue(!resource.userAgentNeedsUpdate(getFacesContext()));

    }


    public void testResourceImplSerialization() throws Exception {

        ResourceHandler handler = getFacesContext().getApplication().getResourceHandler();
        assertTrue(handler != null);

        Resource resource = handler.createResource("duke-nv.gif");
        byte[] serializedBytes = serialize(resource);
        resource = (Resource) deserialize(serializedBytes);
        assertNotNull(resource);
        assertNull(resource.getLibraryName());
        assertEquals("duke-nv.gif", "duke-nv.gif", resource.getResourceName());
        assertEquals("image/gif", "image/gif", resource.getContentType());


        resource = handler.createResource("duke-nv.gif", "nvLibrary");
        serializedBytes = serialize(resource);
        resource = (Resource) deserialize(serializedBytes);
        assertNotNull(resource);
        assertEquals("nvLibrary", "nvLibrary", resource.getLibraryName());
        assertEquals("duke-nv.gif", "duke-nv.gif", resource.getResourceName());
        assertEquals("image/gif", "image/gif", resource.getContentType());

    }


    /**
     * Added for issue 1274.
     */
    public void testResourceELEval() throws Exception {

        ResourceHandler handler = getFacesContext().getApplication().getResourceHandler();
        assertNotNull(handler);

        Resource resource = handler.createResource("simple-with-el.css");
        assertNotNull(resource);

        byte[] bytes = getBytes(resource.getInputStream());
      
        ByteArrayInputStream bai = new ByteArrayInputStream(bytes);
        BufferedReader reader = new BufferedReader(new InputStreamReader(bai));
        List<String> lines = new ArrayList<String>();

        for (String l = reader.readLine(); l != null; l = reader.readLine()) {
            String t = l.trim();
            if (t.length() > 0) {
                lines.add(t);
            }
        }

        assertEquals(4, lines.size());

        final String[] expectedLines = {
            "# /test",
            "# /test",
            "h2 { color: red }",
            "# /test}"
        };

        for (int i = 0, len = expectedLines.length; i < len; i++) {
            assertEquals(expectedLines[i], expectedLines[i], lines.get(i));
        }
        
    }

    /**
     * Added for issue 3331
     */
    public void testResourceELEvalAfterPrematureClosureOfStream() throws Exception {

        ResourceHandler handler = getFacesContext().getApplication().getResourceHandler();
        handler.createResource("simple-with-el.css").getInputStream().close();

        testResourceELEval();
    }

    // ---------------------------------------------------------- Helper Methods


    private byte[] serialize(Object object) throws Exception {

        ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
        ObjectOutputStream oout = new ObjectOutputStream(bytesOut);
        oout.writeObject(object);
        oout.flush();
        oout.close();
        return bytesOut.toByteArray();

    }


    private Object deserialize(byte[] bytes) throws Exception {

        ByteArrayInputStream bytesIn = new ByteArrayInputStream(bytes);
        ObjectInputStream in = new ObjectInputStream(bytesIn);
        return in.readObject();

    }


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

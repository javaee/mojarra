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

import java.io.File;
import java.io.IOException;

import javax.faces.context.ExternalContext;

import com.sun.faces.cactus.ServletFacesTestCase;
import com.sun.faces.config.WebConfiguration;

/**
 * Validate the ResourceManager.
 *
 * @since 2.0
 */
public class TestResourceManager extends ServletFacesTestCase {

    ResourceManager manager;

    public TestResourceManager() {
        super("TestResourceManager");
    }


    public TestResourceManager(String name) {
        super(name);
    }


    @Override public void setUp() {
        super.setUp();
        manager = new ResourceManager();
    }


    @Override public void tearDown() {
        super.tearDown();
        manager = null;
    }

    // ------------------------------------------------------------ Test Methods


    public void testWebappNonVersionedResource() throws Exception {
        ResourceInfo resource = manager.findResource(null, "duke-nv.gif", "image/gif", getFacesContext());
        assertTrue(resource != null);
        assertTrue(resource.getLibraryInfo() == null);
        assertTrue(resource.getHelper() instanceof WebappResourceHelper);
        assertTrue(resource.getVersion() == null);
        assertTrue(!resource.isCompressable());
        assertTrue(resource.getCompressedPath() == null);
        assertTrue("duke-nv.gif".equals(resource.getName()));
        assertTrue("/resources/duke-nv.gif".equals(resource.getPath()));
    }

    public void testWebappVersionedResource() throws Exception {
        ResourceInfo resource = manager.findResource(null, "duke.gif", "image/gif", getFacesContext());
        assertTrue(resource != null);
        assertTrue(resource.getLibraryInfo() == null);
        assertTrue(resource.getHelper() instanceof WebappResourceHelper);
        assertTrue(!resource.isCompressable());
        assertTrue(resource.getCompressedPath() == null);
        assertTrue("1_1".equals(resource.getVersion().toString()));
        assertTrue("duke.gif".equals(resource.getName()));
        assertTrue("/resources/duke.gif/1_1.gif".equals(resource.getPath()));
    }

    public void testWebappNonVersionedLibraryVersionedResource() throws Exception {
        ResourceInfo resource = manager.findResource("nvLibrary", "duke.gif", "image/gif", getFacesContext());
        assertTrue(resource != null);

        // validate the library
        assertTrue(resource.getLibraryInfo() != null);
        assertTrue("nvLibrary".equals(resource.getLibraryInfo().getName()));
        assertTrue(resource.getLibraryInfo().getVersion() == null);
        assertTrue(resource.getLibraryInfo().getHelper() instanceof WebappResourceHelper);
        assertTrue("/resources/nvLibrary".equals(resource.getLibraryInfo().getPath()));

        // validate the resource
        assertTrue(resource.getHelper() instanceof WebappResourceHelper);
        assertTrue(!resource.isCompressable());
        assertTrue(resource.getCompressedPath() == null);
        assertTrue("1_1".equals(resource.getVersion().toString()));
        assertTrue("duke.gif".equals(resource.getName()));
        assertTrue("/resources/nvLibrary/duke.gif/1_1.gif".equals(resource.getPath()));
    }

    public void testWebappNonVersionedLibraryNonVersionedResource() throws Exception {
        ResourceInfo resource = manager.findResource("nvLibrary", "duke-nv.gif", "image/gif", getFacesContext());
        assertTrue(resource != null);

        // validate the library
        assertTrue(resource.getLibraryInfo() != null);
        assertTrue("nvLibrary".equals(resource.getLibraryInfo().getName()));
        assertTrue(resource.getLibraryInfo().getVersion() == null);
        assertTrue(resource.getLibraryInfo().getHelper() instanceof WebappResourceHelper);
        assertTrue("/resources/nvLibrary".equals(resource.getLibraryInfo().getPath()));

        // validate the resource
        assertTrue(resource.getHelper() instanceof WebappResourceHelper);
        assertTrue(!resource.isCompressable());
        assertTrue(resource.getCompressedPath() == null);
        assertTrue(resource.getVersion() == null);
        assertTrue("duke-nv.gif".equals(resource.getName()));
        assertTrue("/resources/nvLibrary/duke-nv.gif".equals(resource.getPath()));
    }

    public void testWebappVersionedLibraryNonVersionedResource() throws Exception {
        ResourceInfo resource = manager.findResource("vLibrary", "duke-nv.gif", "image/gif", getFacesContext());
        assertTrue(resource != null);

        // validate the library
        assertTrue(resource.getLibraryInfo() != null);
        assertTrue("vLibrary".equals(resource.getLibraryInfo().getName()));
        assertTrue("2_0".equals(resource.getLibraryInfo().getVersion().toString()));
        assertTrue(resource.getLibraryInfo().getHelper() instanceof WebappResourceHelper);
        assertTrue("/resources/vLibrary/2_0".equals(resource.getLibraryInfo().getPath()));

        // validate the resource
        assertTrue(resource.getHelper() instanceof WebappResourceHelper);
        assertTrue(!resource.isCompressable());
        assertTrue(resource.getCompressedPath() == null);
        assertTrue(resource.getVersion() == null);
        assertTrue("duke-nv.gif".equals(resource.getName()));
        assertTrue("/resources/vLibrary/2_0/duke-nv.gif".equals(resource.getPath()));
    }

    public void testWebappVersionedLibraryVersionedResource() throws Exception {
        ResourceInfo resource = manager.findResource("vLibrary", "duke.gif", "image/gif", getFacesContext());
        assertTrue(resource != null);

        // validate the library
        assertTrue(resource.getLibraryInfo() != null);
        assertTrue("vLibrary".equals(resource.getLibraryInfo().getName()));
        assertTrue("2_0".equals(resource.getLibraryInfo().getVersion().toString()));
        assertTrue(resource.getLibraryInfo().getHelper() instanceof WebappResourceHelper);
        assertTrue("/resources/vLibrary/2_0".equals(resource.getLibraryInfo().getPath()));

        // validate the resource
        assertTrue(resource.getHelper() instanceof WebappResourceHelper);
        assertTrue(!resource.isCompressable());
        assertTrue(resource.getCompressedPath() == null);
        assertTrue("1_1".equals(resource.getVersion().toString()));
        assertTrue("duke.gif".equals(resource.getName()));
        assertTrue("/resources/vLibrary/2_0/duke.gif/1_1.gif".equals(resource.getPath()));
    }


    public void testWebappPathResource() throws Exception {
        ResourceInfo resource = manager.findResource("nvLibrary", "images/duke-nv.gif", "image/gif", getFacesContext());
        assertTrue(resource != null);
        assertTrue("images/duke-nv.gif".equals(resource.getName()));
        assertTrue(resource.getHelper() instanceof WebappResourceHelper);
        assertTrue(!resource.isCompressable());
        assertTrue(resource.getCompressedPath() == null);
        assertTrue("/resources/nvLibrary/images/duke-nv.gif".equals(resource.getPath()));
    }


    public void testJarNonVersionedResources() throws Exception {
        ResourceInfo resource = manager.findResource(null, "duke-jar-nv.gif", "image/gif", getFacesContext());
        assertTrue(resource != null);
        assertTrue(resource.getLibraryInfo() == null);
        assertTrue(resource.getHelper() instanceof ClasspathResourceHelper);
        assertTrue(resource.getVersion() == null);
        assertTrue(!resource.isCompressable());
        assertTrue(resource.getCompressedPath() == null);
        assertTrue("duke-jar-nv.gif".equals(resource.getName()));
        assertTrue("META-INF/resources/duke-jar-nv.gif".equals(resource.getPath()));
    }

    public void testJarVersionedResource() throws Exception {
        ResourceInfo resource = manager.findResource(null, "duke-jar.gif", "image/gif", getFacesContext());
        assertTrue(resource != null);
        assertTrue(resource.getLibraryInfo() == null);
        assertTrue(resource.getHelper() instanceof ClasspathResourceHelper);
        assertTrue(!resource.isCompressable());
        assertTrue(resource.getCompressedPath() == null);
        assertTrue("1_1".equals(resource.getVersion().toString()));
        assertTrue("duke-jar.gif".equals(resource.getName()));
        assertTrue("META-INF/resources/duke-jar.gif/1_1.gif".equals(resource.getPath()));
    }

    public void testJarNonVersionedLibraryVersionedResource() throws Exception {
        ResourceInfo resource = manager.findResource("nvLibrary-jar", "duke.gif", "image/gif", getFacesContext());
        assertTrue(resource != null);

        // validate the library
        assertTrue(resource.getLibraryInfo() != null);
        assertTrue("nvLibrary-jar".equals(resource.getLibraryInfo().getName()));
        assertTrue(resource.getLibraryInfo().getVersion() == null);
        assertTrue(resource.getLibraryInfo().getHelper() instanceof ClasspathResourceHelper);
        assertTrue("META-INF/resources/nvLibrary-jar".equals(resource.getLibraryInfo().getPath()));

        // validate the resource
        assertTrue(resource.getHelper() instanceof ClasspathResourceHelper);
        assertTrue(!resource.isCompressable());
        assertTrue(resource.getCompressedPath() == null);
        assertTrue("1_1".equals(resource.getVersion().toString()));
        assertTrue("duke.gif".equals(resource.getName()));
        assertTrue("META-INF/resources/nvLibrary-jar/duke.gif/1_1.gif".equals(resource.getPath()));
    }

    public void testJarNonVersionedLibraryNonVersionedResource() throws Exception {
        ResourceInfo resource = manager.findResource("nvLibrary-jar", "duke-nv.gif", "image/gif", getFacesContext());
        assertTrue(resource != null);

        // validate the library
        assertTrue(resource.getLibraryInfo() != null);
        assertTrue("nvLibrary-jar".equals(resource.getLibraryInfo().getName()));
        assertTrue(resource.getLibraryInfo().getVersion() == null);
        assertTrue(resource.getLibraryInfo().getHelper() instanceof ClasspathResourceHelper);
        assertTrue("META-INF/resources/nvLibrary-jar".equals(resource.getLibraryInfo().getPath()));

        // validate the resource
        assertTrue(resource.getHelper() instanceof ClasspathResourceHelper);
        assertTrue(!resource.isCompressable());
        assertTrue(resource.getCompressedPath() == null);
        assertTrue(resource.getVersion() == null);
        assertTrue("duke-nv.gif".equals(resource.getName()));
        assertTrue("META-INF/resources/nvLibrary-jar/duke-nv.gif".equals(resource.getPath()));
    }

    public void testJarVersionedLibraryNonVersionedResource() throws Exception {
        ResourceInfo resource = manager.findResource("vLibrary-jar", "duke-nv.gif", "image/gif", getFacesContext());
        assertTrue(resource != null);

        // validate the library
        assertTrue(resource.getLibraryInfo() != null);
        assertTrue("vLibrary-jar".equals(resource.getLibraryInfo().getName()));
        assertTrue("2_0".equals(resource.getLibraryInfo().getVersion().toString()));
        assertTrue(resource.getLibraryInfo().getHelper() instanceof ClasspathResourceHelper);
        assertTrue("META-INF/resources/vLibrary-jar/2_0".equals(resource.getLibraryInfo().getPath()));

        // validate the resource
        assertTrue(resource.getHelper() instanceof ClasspathResourceHelper);
        assertTrue(!resource.isCompressable());
        assertTrue(resource.getCompressedPath() == null);
        assertTrue(resource.getVersion() == null);
        assertTrue("duke-nv.gif".equals(resource.getName()));
        assertTrue("META-INF/resources/vLibrary-jar/2_0/duke-nv.gif".equals(resource.getPath()));
    }

    public void testJarVersionedLibraryVersionedResource() throws Exception {
        ResourceInfo resource = manager.findResource("vLibrary-jar", "duke.gif", "image/gif", getFacesContext());
        assertTrue(resource != null);

        // validate the library
        assertTrue(resource.getLibraryInfo() != null);
        assertTrue("vLibrary-jar".equals(resource.getLibraryInfo().getName()));
        assertTrue("2_0".equals(resource.getLibraryInfo().getVersion().toString()));
        assertTrue(resource.getLibraryInfo().getHelper() instanceof ClasspathResourceHelper);
        assertTrue("META-INF/resources/vLibrary-jar/2_0".equals(resource.getLibraryInfo().getPath()));

        // validate the resource
        assertTrue(resource.getHelper() instanceof ClasspathResourceHelper);
        assertTrue(!resource.isCompressable());
        assertTrue(resource.getCompressedPath() == null);
        assertTrue("1_1".equals(resource.getVersion().toString()));
        assertTrue("duke.gif".equals(resource.getName()));
        assertTrue("META-INF/resources/vLibrary-jar/2_0/duke.gif/1_1.gif".equals(resource.getPath()));
    }

    public void testNoExtensionVersionedResource() throws Exception {
        ResourceInfo resource = manager.findResource("vLibrary", "duke2.gif", "image/gif", getFacesContext());
        assertTrue(resource != null);

        // validate the library
        assertTrue(resource.getLibraryInfo() != null);
        assertTrue("vLibrary".equals(resource.getLibraryInfo().getName()));
        assertTrue("2_0".equals(resource.getLibraryInfo().getVersion().toString()));
        assertTrue(resource.getLibraryInfo().getHelper() instanceof WebappResourceHelper);
        assertTrue("/resources/vLibrary/2_0".equals(resource.getLibraryInfo().getPath()));

        // validate the resource
        assertTrue(resource.getHelper() instanceof WebappResourceHelper);
        assertTrue(!resource.isCompressable());
        assertTrue(resource.getCompressedPath() == null);
        assertTrue("1_1".equals(resource.getVersion().toString()));
        assertTrue(resource.getVersion().getExtension() == null);
        assertTrue("duke2.gif".equals(resource.getName()));
        assertTrue("/resources/vLibrary/2_0/duke2.gif/1_1".equals(resource.getPath()));   
    }


    public void testInvalidLibraryName() throws Exception {
        assertTrue(manager.findResource("noSuchLibrary", "duke.gif", "image/gif", getFacesContext()) == null);
    }

    public void testInvalidResourceName() throws Exception {
        assertTrue(manager.findResource(null, "duke.fig", null, getFacesContext()) == null);
        assertTrue(manager.findResource("nvLibrary", "duke.fig", null, getFacesContext()) == null);
    }

    public void testResourceInfoCompression() throws Exception {
        WebConfiguration config = WebConfiguration.getInstance();
        config.overrideContextInitParameter(WebConfiguration.WebContextInitParameter.CompressableMimeTypes, "image/gif,text/css");
        // create a new ResourceManager so that the mime type configuration is picked up
        ResourceManager manager = new ResourceManager();
        ResourceInfo resource = manager.findResource("nvLibrary", "images/duke-nv.gif", "image/gif", getFacesContext());
        assertTrue(resource != null);
        assertTrue(resource.isCompressable());
        assertTrue(compressionPathIsValid(resource));
        
        // ensure compression disabled for a content type that is null
        resource = manager.findResource("nvLibrary", "images/duke-nv.gif", "text/javascript", getFacesContext());
        assertTrue(resource != null);
        assertTrue(!resource.isCompressable());
        assertTrue(resource.getCompressedPath() == null);

        // if a resource is compressable, but the compressed result is larger
        // than the original resource, the returned ResourceInfo shouldn't
        // be marked as compressable and getCompressedPath() will be null
        resource = manager.findResource(null, "simple.css", "text/css", getFacesContext());
        assertTrue(resource != null);
        assertTrue(!resource.isCompressable());
        assertTrue(resource.getCompressedPath() == null);

    }


    // --------------------------------------------------------- Private Methods


    private boolean compressionPathIsValid(ResourceInfo resource)
    throws IOException {

        ExternalContext extContext = getFacesContext().getExternalContext();
        File tempDir = (File) extContext.getApplicationMap().get("javax.servlet.context.tempdir");
        File expected = new File(tempDir, "/jsf-compressed" + File.separatorChar + resource.getPath());
        return expected.getCanonicalPath().equals(resource.getCompressedPath());

    }
    



} // END TestResourceManager
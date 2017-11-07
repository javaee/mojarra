/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2017 Oracle and/or its affiliates. All rights reserved.
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.Override;
import java.lang.String;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.zip.GZIPOutputStream;

import javax.faces.application.*;
import javax.faces.application.Application;
import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.application.ResourceHandlerWrapper;
import javax.faces.application.ResourceWrapper;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import com.sun.faces.cactus.ServletFacesTestCase;
import com.sun.faces.config.WebConfiguration;
import com.sun.faces.util.Util;
import com.sun.faces.application.ApplicationAssociate;
import com.sun.faces.config.InitFacesContext;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import javax.faces.FactoryFinder;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.apache.cactus.WebRequest;
import org.apache.cactus.WebResponse;

/**
 * Tests com.sun.faces.application.resource.ResourceHandlerImpl
 */
public class TestResourceHandlerImpl extends ServletFacesTestCase {

    /* HTTP Date format required by the HTTP/1.1 RFC */
    private static final String RFC1123_DATE_PATTERN = "EEE, dd MMM yyyy HH:mm:ss zzz";

    private static final TimeZone GMT = TimeZone.getTimeZone("GMT");

    public TestResourceHandlerImpl() {
        super("TestResourceHandlerImpl");
        initLocalHostPath();
    }

    public TestResourceHandlerImpl(String name) {
        super(name);
        initLocalHostPath();
    }

    private String path = "localhost:8080";

    private void initLocalHostPath() {
        String containerPort = System.getProperty("container.port");
        if (null == containerPort || 0 == containerPort.length()) {
            containerPort = "8080";
        }
        path = "localhost:" + containerPort;
    }
    
    public void testAll() {
        
    }

    @Override
    public void setUp() {
        try {
            Method reInitializeFactoryManager = FactoryFinder.class.getDeclaredMethod("reInitializeFactoryManager", (Class<?>[]) null);
            reInitializeFactoryManager.setAccessible(true);
            reInitializeFactoryManager.invoke(null, (Object[]) null);

            FacesContext oldContext = FacesContext.getCurrentInstance();
            if (null != oldContext && (oldContext instanceof InitFacesContext)) {
                // JAVASERVERFACES-2140
                assert (Util.isUnitTestModeEnabled());
                System.out.println("Re-initializing ExternalContext with ServletContext from cactus: " + getConfig().getServletContext());
                System.out.flush();
                ((InitFacesContext) oldContext).reInitializeExternalContext(getConfig().getServletContext());
            }

        } catch (Exception e) {
            fail(e.getMessage());
        }
        super.setUp();
    }

    @Override
    public void tearDown() {
        super.tearDown();
    }

    // ------------------------------------------------------------ Test Methods

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

            // @Override
            // public boolean isReady() {
            // throw new UnsupportedOperationException("Not supported");
            // }
            //
            // @Override
            // public void setWriteListener(WriteListener wl) {
            // throw new UnsupportedOperationException("Not supported");
            // }
        }
    }

}

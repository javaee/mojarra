/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2012 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.java.net/public/CDDL+GPL_1_1.html
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
package com.sun.faces.test.servlet30.facesContext;

import java.io.IOException;
import java.io.Serializable;
import java.io.Writer;
import java.util.Locale;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseStream;
import javax.faces.context.ResponseWriter;
import static org.junit.Assert.*;

/**
 * The managed bean for the accessor tests.
 *
 * @author Manfred Riem (manfred.riem@oracle.com)
 */
@ManagedBean(name = "accessorBean")
@RequestScoped
public class AccessorBean implements Serializable {

    public String getAccessorResult1() {
        UIViewRoot oldRoot = FacesContext.getCurrentInstance().getViewRoot();
        UIViewRoot page = new UIViewRoot();
        page.setViewId("viewId");
        page.setLocale(Locale.US);
        FacesContext.getCurrentInstance().setViewRoot(page);
        UIViewRoot root = FacesContext.getCurrentInstance().getViewRoot();
        assertNotNull(root);
        assertEquals(root, FacesContext.getCurrentInstance().getViewRoot());
        FacesContext.getCurrentInstance().setViewRoot(oldRoot);
        return "PASSED";
    }

    public String getAccessorResult2() {
        ResponseStream oldStream = FacesContext.getCurrentInstance().getResponseStream();
        ResponseStream responseStream = new ResponseStream() {

            public void write(int b) {
            }
        };
        FacesContext.getCurrentInstance().setResponseStream(responseStream);
        assertNotNull(FacesContext.getCurrentInstance().getResponseStream());
        assertEquals(responseStream, FacesContext.getCurrentInstance().getResponseStream());
        if (oldStream != null) {
            FacesContext.getCurrentInstance().setResponseStream(oldStream);
        }
        return "PASSED";
    }

    public String getAccessorResult3() {
        ResponseWriter oldWriter = FacesContext.getCurrentInstance().getResponseWriter();
        ResponseWriter responseWriter = new ResponseWriter() {

            @Override
            public Writer append(CharSequence csq) throws IOException {
                return super.append(csq);
            }

            ;
            @Override
            public String getContentType() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public String getCharacterEncoding() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void flush() throws IOException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void startDocument() throws IOException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void endDocument() throws IOException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void startElement(String name, UIComponent component) throws IOException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void endElement(String name) throws IOException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void writeAttribute(String name, Object value, String property) throws IOException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void writeURIAttribute(String name, Object value, String property) throws IOException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void writeComment(Object comment) throws IOException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void writeText(Object text, String property) throws IOException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void writeText(char[] text, int off, int len) throws IOException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public ResponseWriter cloneWithWriter(Writer writer) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void write(char[] cbuf, int off, int len) throws IOException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void close() throws IOException {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };

        FacesContext.getCurrentInstance().setResponseWriter(responseWriter);
        assertNotNull(FacesContext.getCurrentInstance().getResponseWriter());
        assertEquals(responseWriter, FacesContext.getCurrentInstance().getResponseWriter());

        try {
            FacesContext.getCurrentInstance().setResponseWriter(null);
            fail();
        } catch (Exception exception) {
        }
        
        FacesContext.getCurrentInstance().setResponseWriter(oldWriter);
        return "PASSED";
    }
}

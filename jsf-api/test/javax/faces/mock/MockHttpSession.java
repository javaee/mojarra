/*
 * $Id: MockHttpSession.java,v 1.4 2005/08/22 22:08:24 ofung Exp $
 */

/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 * 
 * You can obtain a copy of the License at
 * https://javaserverfaces.dev.java.net/CDDL.html or
 * legal/CDDLv1.0.txt. 
 * See the License for the specific language governing
 * permission and limitations under the License.
 * 
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at legal/CDDLv1.0.txt.    
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 * 
 * [Name of File] [ver.__] [Date]
 * 
 * Copyright 2005 Sun Microsystems Inc. All Rights Reserved
 */

package javax.faces.mock;


import java.util.Enumeration;
import java.util.HashMap;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;



// Mock Object for HttpSession (Version 2.3)
public class MockHttpSession implements HttpSession {



    public MockHttpSession() {
        super();
    }


    public MockHttpSession(ServletContext servletContext) {
        super();
        setServletContext(servletContext);
    }



    protected HashMap attributes = new HashMap();
    protected ServletContext servletContext = null;


    // --------------------------------------------------------- Public Methods


    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }


    // ---------------------------------------------------- HttpSession Methods


    public Object getAttribute(String name) {
        return (attributes.get(name));
    }


    public Enumeration getAttributeNames() {
        return (new MockEnumeration(attributes.keySet().iterator()));
    }


    public long getCreationTime() {
        throw new UnsupportedOperationException();
    }


    public String getId() {
        throw new UnsupportedOperationException();
    }


    public long getLastAccessedTime() {
        throw new UnsupportedOperationException();
    }


    public int getMaxInactiveInterval() {
        throw new UnsupportedOperationException();
    }


    public ServletContext getServletContext() {
        return (this.servletContext);
    }


    public HttpSessionContext getSessionContext() {
        throw new UnsupportedOperationException();
    }


    public Object getValue(String name) {
        throw new UnsupportedOperationException();
    }


    public String[] getValueNames() {
        throw new UnsupportedOperationException();
    }


    public void invalidate() {
        throw new UnsupportedOperationException();
    }


    public boolean isNew() {
        throw new UnsupportedOperationException();
    }


    public void putValue(String name, Object value) {
        throw new UnsupportedOperationException();
    }


    public void removeAttribute(String name) {
        attributes.remove(name);
    }


    public void removeValue(String name) {
        throw new UnsupportedOperationException();
    }


    public void setAttribute(String name, Object value) {
        if (value == null) {
            attributes.remove(name);
        } else {
            attributes.put(name, value);
        }
    }


    public void setMaxInactiveInterval(int interval) {
        throw new UnsupportedOperationException();
    }


}

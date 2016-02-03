/*
 * $Id: MockHttpSession.java,v 1.3.38.2 2007/04/27 21:27:01 ofung Exp $
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

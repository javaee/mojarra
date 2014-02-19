/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2014 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.faces.mock;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.el.VariableResolver;
import javax.servlet.jsp.el.ExpressionEvaluator;
import javax.el.ELContext;
import static javax.servlet.jsp.PageContext.APPLICATION;
import static javax.servlet.jsp.PageContext.APPLICATION_SCOPE;
import static javax.servlet.jsp.PageContext.CONFIG;
import static javax.servlet.jsp.PageContext.EXCEPTION;
import static javax.servlet.jsp.PageContext.OUT;
import static javax.servlet.jsp.PageContext.PAGE;
import static javax.servlet.jsp.PageContext.PAGECONTEXT;
import static javax.servlet.jsp.PageContext.PAGE_SCOPE;
import static javax.servlet.jsp.PageContext.REQUEST;
import static javax.servlet.jsp.PageContext.REQUEST_SCOPE;
import static javax.servlet.jsp.PageContext.RESPONSE;
import static javax.servlet.jsp.PageContext.SESSION;
import static javax.servlet.jsp.PageContext.SESSION_SCOPE;

// Mock Object for PageContext
public class MockPageContext extends PageContext {

    private Servlet servlet = null;
    private ServletRequest request = null;
    private ServletResponse response = null;
    private HttpSession session = null;
    private String errorPageURL = null;
    private boolean needsSession = false;
    private int bufferSize = 0;
    private boolean autoFlush = false;
    private ServletConfig config = null;
    private ServletContext context = null;
    private JspWriter out = null;
    private Hashtable attributes = new Hashtable();

    // ---------------------------------------------------- PageContext Methods
    public void clearPageScope() {
        attributes.clear();
    }

    @Override
    public ELContext getELContext() {
        return null;
    }

    @Override
    public Object findAttribute(String name) {
        Object value = attributes.get(name);
        if (value == null) {
            value = request.getAttribute(name);
        }
        if ((value == null) && (session != null)) {
            value = session.getAttribute(name);
        }
        if (value == null) {
            value = context.getAttribute(name);
        }
        return (value);
    }

    @Override
    public void forward(String path) throws IOException, ServletException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object getAttribute(String name) {
        return (getAttribute(name, PAGE_SCOPE));
    }

    @Override
    public Object getAttribute(String name, int scope) {
        switch (scope) {
            case PAGE_SCOPE:
                return (attributes.get(name));
            case REQUEST_SCOPE:
                return (request.getAttribute(name));
            case SESSION_SCOPE:
                if (session == null) {
                    throw new IllegalArgumentException("No session for this request");
                }
                return (session.getAttribute(name));
            case APPLICATION_SCOPE:
                return (context.getAttribute(name));
            default:
                throw new IllegalArgumentException("Invalid scope " + scope);
        }
    }

    @Override
    public Enumeration getAttributeNamesInScope(int scope) {
        switch (scope) {
            case PAGE_SCOPE:
                return (attributes.keys());
            case REQUEST_SCOPE:
                return (request.getAttributeNames());
            case SESSION_SCOPE:
                if (session == null) {
                    throw new IllegalArgumentException("No session for this request");
                }
                return (session.getAttributeNames());
            case APPLICATION_SCOPE:
                return (context.getAttributeNames());
            default:
                throw new IllegalArgumentException("Invalid scope " + scope);
        }
    }

    @Override
    public int getAttributesScope(String name) {
        if (attributes.get(name) != null) {
            return (PAGE_SCOPE);
        } else if (request.getAttribute(name) != null) {
            return (REQUEST_SCOPE);
        } else if ((session != null) && (session.getAttribute(name) != null)) {
            return (SESSION_SCOPE);
        } else if (context.getAttribute(name) != null) {
            return (APPLICATION_SCOPE);
        } else {
            return (0);

        }
    }

    @Override
    public Exception getException() {
        return ((Exception) request.getAttribute(EXCEPTION));
    }

    @Override
    public JspWriter getOut() {
        return (this.out);
    }

    @Override
    public Object getPage() {
        return (this.servlet);
    }

    @Override
    public ServletRequest getRequest() {
        return (this.request);
    }

    @Override
    public ServletResponse getResponse() {
        return (this.response);
    }

    @Override
    public ServletConfig getServletConfig() {
        return (this.config);
    }

    @Override
    public ServletContext getServletContext() {
        return (this.context);
    }

    @Override
    public HttpSession getSession() {
        return (this.session);
    }

    @Override
    public void include(String relativeUrlPath, boolean flush)
            throws ServletException, IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void handlePageException(Exception e) throws IOException, ServletException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void handlePageException(Throwable t) throws IOException, ServletException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void include(String path) throws IOException, ServletException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void initialize(Servlet servlet, ServletRequest request,
            ServletResponse response, String errorPageURL,
            boolean needsSession, int bufferSize,
            boolean autoFlush)
            throws IOException, IllegalStateException, IllegalArgumentException {
        // Initialize state - passed values
        this.servlet = servlet;
        this.request = request;
        this.response = response;
        this.errorPageURL = errorPageURL;
        this.bufferSize = bufferSize;
        this.autoFlush = autoFlush;

        // Initialize state - derived values
        this.config = servlet.getServletConfig();
        this.context = config.getServletContext();
        if (request instanceof HttpServletRequest && needsSession) {
            this.session = ((HttpServletRequest) request).getSession();
        }
        this.out = new MockJspWriter(bufferSize, autoFlush);

        // Register predefined page scope attributes
        setAttribute(OUT, this.out);
        setAttribute(REQUEST, this.request);
        setAttribute(RESPONSE, this.response);
        if (session != null) {
            setAttribute(SESSION, session);
        }
        setAttribute(PAGE, servlet);
        setAttribute(CONFIG, config);
        setAttribute(PAGECONTEXT, this);
        setAttribute(APPLICATION, context);
    }

    @Override
    public VariableResolver getVariableResolver() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ExpressionEvaluator getExpressionEvaluator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public JspWriter popBody() {
        throw new UnsupportedOperationException();
    }

    @Override
    public BodyContent pushBody() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void release() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeAttribute(String name) {
        removeAttribute(name, PAGE_SCOPE);
    }

    @Override
    public void removeAttribute(String name, int scope) {
        switch (scope) {
            case PAGE_SCOPE:
                attributes.remove(name);
                break;
            case REQUEST_SCOPE:
                request.removeAttribute(name);
                break;
            case SESSION_SCOPE:
                if (session == null) {
                    throw new IllegalArgumentException("No session for this request");
                }
                session.removeAttribute(name);
                break;
            case APPLICATION_SCOPE:
                context.removeAttribute(name);
                break;
            default:
                throw new IllegalArgumentException("Invalid scope " + scope);
        }
    }

    @Override
    public void setAttribute(String name, Object value) {
        setAttribute(name, value, PAGE_SCOPE);
    }

    @Override
    public void setAttribute(String name, Object value, int scope) {
        switch (scope) {
            case PAGE_SCOPE:
                attributes.put(name, value);
                break;
            case REQUEST_SCOPE:
                request.setAttribute(name, value);
                break;
            case SESSION_SCOPE:
                if (session == null) {
                    throw new IllegalArgumentException("No session for this request");
                }
                session.setAttribute(name, value);
                break;
            case APPLICATION_SCOPE:
                context.setAttribute(name, value);
                break;
            default:
                throw new IllegalArgumentException("Invalid scope " + scope);
        }
    }
}

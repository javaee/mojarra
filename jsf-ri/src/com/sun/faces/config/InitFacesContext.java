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

package com.sun.faces.config;

import javax.faces.context.FacesContext;
import javax.faces.context.ExternalContext;
import javax.faces.context.ResponseStream;
import javax.faces.context.ResponseWriter;
import javax.faces.component.UIViewRoot;
import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;
import javax.faces.application.FacesMessage;
import javax.faces.FactoryFinder;
import javax.faces.render.RenderKit;
import javax.servlet.ServletContext;
import javax.el.ELContext;
import javax.el.ELResolver;
import javax.el.FunctionMapper;
import javax.el.VariableMapper;
import java.util.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.MalformedURLException;

/**
 * A special, minimal implementation of FacesContext used at application initialization time.
 * The ExternalContext returned by this FacesContext only exposes the ApplicationMap.
 */
class InitFacesContext extends FacesContext {

    private ExternalContext ec;
    private UIViewRoot viewRoot;
    private FacesContext orig;

    public InitFacesContext(ServletContext sc) {
        ec = new ServletContextAdapter(sc);
        orig = FacesContext.getCurrentInstance();
        setCurrentInstance(this);
    }

    public Application getApplication() {
        ApplicationFactory factory = (ApplicationFactory)
                FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
        return factory.getApplication();
    }

    public Iterator<String> getClientIdsWithMessages() {
        List<String> list = Collections.emptyList();
        return list.iterator();
    }

    public ExternalContext getExternalContext() {
        return ec;
    }

    public FacesMessage.Severity getMaximumSeverity() {
        return FacesMessage.SEVERITY_INFO;
    }

    public Iterator<FacesMessage> getMessages() {
        List<FacesMessage> list = Collections.emptyList();
        return list.iterator();
    }

    public Iterator<FacesMessage> getMessages(String clientId) {
        return getMessages();
    }

    public RenderKit getRenderKit() {
        return null;
    }

    public boolean getRenderResponse() {
        return true;
    }

    public boolean getResponseComplete() {
        return true;
    }

    public ResponseStream getResponseStream() {
        return null;
    }

    public void setResponseStream(ResponseStream responseStream) { }

    public ResponseWriter getResponseWriter() {
        return null;
    }

    public void setResponseWriter(ResponseWriter responseWriter) { }

    public UIViewRoot getViewRoot() {
        if (viewRoot == null) {
            viewRoot = new UIViewRoot();
            viewRoot.setLocale(Locale.getDefault());
        }
        return viewRoot;
    }

    public void setViewRoot(UIViewRoot root) { }

    public void addMessage(String clientId, FacesMessage message) { }

    public void release() {
        setCurrentInstance(orig);
    }

    public void renderResponse() { }

    public void responseComplete() { }

    @Override
    public ELContext getELContext() {
        return new ELContext() {
            public ELResolver getELResolver() {
                return null;
            }

            public FunctionMapper getFunctionMapper() {
                return null;
            }

            public VariableMapper getVariableMapper() {
                return null;
            }
        };
    }

    private static class ServletContextAdapter extends ExternalContext {

        private ServletContext servletContext = null;
        private ApplicationMap applicationMap = null;

        public ServletContextAdapter(ServletContext sc) {
            this.servletContext = sc;
        }

        public void dispatch(String path) throws IOException {
        }

        public String encodeActionURL(String url) {
            return null;
        }

        public String encodeNamespace(String name) {
            return null;
        }


        public String encodeResourceURL(String url) {
            return null;
        }

        @SuppressWarnings("unchecked")
        public Map<String, Object> getApplicationMap() {
            if (applicationMap == null) {
                applicationMap =
                        new ApplicationMap(servletContext);
            }
            return applicationMap;
        }

        public String getAuthType() {
            return null;
        }

        public Object getContext() {
            return servletContext;
        }

        public String getInitParameter(String name) {
            return null;
        }

        public Map getInitParameterMap() {
            return null;
        }

        public String getRemoteUser() {
            return null;
        }


        public Object getRequest() {
            return null;
        }

        @Override
        public void setRequest(Object request) {
        }

        public String getRequestContextPath() {
            return null;
        }

        public Map<String, Object> getRequestCookieMap() {
            return null;
        }

        public Map<String, String> getRequestHeaderMap() {
            return null;
        }


        public Map<String, String[]> getRequestHeaderValuesMap() {
            return null;
        }


        public Locale getRequestLocale() {
            return null;
        }

        public Iterator<Locale> getRequestLocales() {
            return null;
        }


        public Map<String, Object> getRequestMap() {
            return null;
        }


        public Map<String, String> getRequestParameterMap() {
            return null;
        }


        public Iterator<String> getRequestParameterNames() {
            return null;
        }


        public Map<String, String[]> getRequestParameterValuesMap() {
            return null;
        }


        public String getRequestPathInfo() {
            return null;
        }


        public String getRequestServletPath() {
            return null;
        }

        @Override
        public String getRequestContentType() {
            return null;
        }

        @Override
        public String getResponseContentType() {
            return null;
        }

        public URL getResource(String path) throws MalformedURLException {
            return null;
        }


        public InputStream getResourceAsStream(String path) {
            return null;
        }

        public Set<String> getResourcePaths(String path) {
            return null;
        }

        public Object getResponse() {
            return null;
        }

        @Override
        public void setResponse(Object response) {
        }

        public Object getSession(boolean create) {
            return null;
        }

        public Map<String, Object> getSessionMap() {
            return null;
        }

        public java.security.Principal getUserPrincipal() {
            return null;
        }

        public boolean isUserInRole(String role) {
            return false;
        }

        public void log(String message) {
        }

        public void log(String message, Throwable exception) {
        }

        public void redirect(String url) throws IOException {
        }

        @Override
        public String getRequestCharacterEncoding() {
            return null;
        }

        @Override
        public void setRequestCharacterEncoding(String requestCharacterEncoding) throws UnsupportedEncodingException {

        }

        @Override
        public String getResponseCharacterEncoding() {
            return null;
        }

        @Override
        public void setResponseCharacterEncoding(String responseCharacterEncoding) {
        }

    } // END ServletContextAdapter

    static class ApplicationMap extends java.util.AbstractMap {

        private final ServletContext servletContext;

        ApplicationMap(ServletContext servletContext) {
            this.servletContext = servletContext;
        }

        @Override
        public Object get(Object key) {
            if (key == null) {
                throw new NullPointerException();
            }
            return servletContext.getAttribute(key.toString());
        }


        @Override
        public Object put(Object key, Object value) {
            if (key == null) {
                throw new NullPointerException();
            }
            String keyString = key.toString();
            Object result = servletContext.getAttribute(keyString);
            servletContext.setAttribute(keyString, value);
            return (result);
        }


        @Override
        public Object remove(Object key) {
            if (key == null) {
                return null;
            }
            String keyString = key.toString();
            Object result = servletContext.getAttribute(keyString);
            servletContext.removeAttribute(keyString);
            return (result);
        }


        public Set entrySet() {
            throw new UnsupportedOperationException();
        }


        @Override
        public boolean equals(Object obj) {
            return !(obj == null || !(obj instanceof ApplicationMap))
                    && super.equals(obj);
        }


        @Override
        public int hashCode() {
            int hashCode = 7 * servletContext.hashCode();
            for (Object o : entrySet()) {
                hashCode += o.hashCode();
            }
            return hashCode;
        }


        @Override
        public void clear() {
            throw new UnsupportedOperationException();
        }


        @Override
        public void putAll(Map t) {
            throw new UnsupportedOperationException();
        }


        @Override
        public boolean containsKey(Object key) {
            return (servletContext.getAttribute(key.toString()) != null);
        }


    } // END ApplicationMap
}
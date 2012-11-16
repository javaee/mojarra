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

package com.sun.faces.config;

import com.sun.faces.RIConstants;
import javax.faces.context.FacesContext;
import javax.faces.context.ExternalContext;
import javax.faces.context.ResponseStream;
import javax.faces.context.ResponseWriter;
import javax.faces.component.UIViewRoot;
import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;
import javax.faces.application.FacesMessage;
import javax.faces.application.ProjectStage;
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
import java.lang.reflect.Field;
import java.net.URL;
import java.net.MalformedURLException;
import java.util.concurrent.ConcurrentHashMap;

import com.sun.faces.context.ApplicationMap;
import com.sun.faces.context.InitParameterMap;
import com.sun.faces.util.Util;

/**
 * A special, minimal implementation of FacesContext used at application initialization time.
 * The ExternalContext returned by this FacesContext only exposes the ApplicationMap.
 */
public class InitFacesContext extends FacesContext {

    private ServletContextAdapter ec;
    private UIViewRoot viewRoot;
    private FacesContext orig;
    private Map<Object,Object> attributes;
    private ELContext elContext = new ELContext() {
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

    public InitFacesContext(ServletContext sc) {
        ec = new ServletContextAdapter(sc);
        orig = FacesContext.getCurrentInstance();
        sc.setAttribute(INIT_FACES_CONTEXT_ATTR_NAME, this);
        InitFacesContext.cleanupInitMaps(sc);
        getThreadInitContextMap().put(Thread.currentThread(), this);
        getInitContextServletContextMap().put(this, sc);
    }
    
    public void reInitializeExternalContext(ServletContext sc) {
        assert(Util.isUnitTestModeEnabled());
        ec = new ServletContextAdapter(sc);
    }
    
    private static final String INIT_FACES_CONTEXT_ATTR_NAME = RIConstants.FACES_PREFIX + "InitFacesContext";
    
    static InitFacesContext getInstance(ServletContext sc) {
        InitFacesContext result = (InitFacesContext) sc.getAttribute(INIT_FACES_CONTEXT_ATTR_NAME);
        if (null != result) {
            result.callSetCurrentInstance();
        }
        
        return result;
    }

    void callSetCurrentInstance() {
        getThreadInitContextMap().put(Thread.currentThread(), this);
    }

    @Override
    public Map<Object, Object> getAttributes() {
        if (attributes == null) {
            attributes = new HashMap<Object,Object>();
        }
        return attributes;
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

    public List<FacesMessage> getMessageList() {
	return Collections.EMPTY_LIST;
    }


    public List<FacesMessage> getMessageList(String clientId) {
	return Collections.EMPTY_LIST;
    }


    @Override
    public boolean isProjectStage(ProjectStage stage) {
        if (stage == null) {
            throw new NullPointerException();
        }
        return stage.equals(getApplication().getProjectStage());
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

    @Override
    public boolean isValidationFailed() {
        return false;
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
            viewRoot.setViewId(RIConstants.FACES_PREFIX + "xhtml");
        }
        return viewRoot;
    }

    public void setViewRoot(UIViewRoot root) { }

    public void addMessage(String clientId, FacesMessage message) { }

    public void release() {
        setCurrentInstance(orig);
        if (null != ec) {
            Map<String, Object> appMap = ec.getApplicationMap();
            if (null != appMap) {
                if (appMap instanceof ApplicationMap) {
                    if (null != ((ApplicationMap)appMap).getContext()) {
                        appMap.remove(INIT_FACES_CONTEXT_ATTR_NAME);
                    }
                }
            }
            ec.release();
        }
        
        if (null != attributes) {
            attributes.clear();
            attributes = null;
        }
        elContext = null;
        if (null != viewRoot) {
            Map viewMap = viewRoot.getViewMap(false);
            if (null != viewMap) {
                viewMap.clear();
            }
        }
        viewRoot = null;
        orig = null;

    }

    public void renderResponse() { }

    public void responseComplete() { }

    @Override
    public void validationFailed() { }

    @Override
    public ELContext getELContext() {
       return elContext;
    }

    public void setELContext(ELContext elContext) {
        this.elContext = elContext;        
    }

    /**
     * Clean up entries from the threadInitContext and initContextServletContext maps 
     * using a ServletContext.  First remove entry(s) with matching ServletContext from
     * initContextSerlvetContext map.  Then remove entries from threadInitContext map
     * where the entry value(s) match the initFacesContext (associated with the ServletContext).
     */
    public static void cleanupInitMaps(ServletContext context) {
        Map <Thread, InitFacesContext>threadInitContext = InitFacesContext.getThreadInitContextMap();
        Map <InitFacesContext, ServletContext>initContextServletContext =
            InitFacesContext.getInitContextServletContextMap();
        Set<Map.Entry<InitFacesContext, ServletContext>> entries = initContextServletContext.entrySet();
        for (Iterator<Map.Entry<InitFacesContext, ServletContext>> iterator1 =
            entries.iterator(); iterator1.hasNext();) {
            Map.Entry<InitFacesContext, ServletContext> entry1 = (Map.Entry)iterator1.next();
            Object initContextKey = entry1.getKey();
            Object value1 = entry1.getValue();
            if (context == value1) {
                initContextServletContext.remove(initContextKey);
                Set<Map.Entry<Thread, InitFacesContext>> threadEntries = threadInitContext.entrySet();
                for (Iterator<Map.Entry<Thread, InitFacesContext>> iterator2 =
                    threadEntries.iterator(); iterator2.hasNext();) {
                    Map.Entry<Thread, InitFacesContext>  entry2 = (Map.Entry)iterator2.next();
                    Object thread = entry2.getKey();
                    Object initContextValue = entry2.getValue();
                    if (initContextKey == initContextValue) {
                        threadInitContext.remove(thread);
                    }
                }
            }
        }
    }

    private static class ServletContextAdapter extends ExternalContext {

        private ServletContext servletContext = null;
        private ApplicationMap applicationMap = null;
        private InitParameterMap initMap = null;

        public ServletContextAdapter(ServletContext sc) {
            this.servletContext = sc;
        }

        public void dispatch(String path) throws IOException {
        }
        
        private void release() {
            servletContext = null;
            applicationMap = null;
            initMap = null;
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

        public Map<String, Object> getApplicationMap() {
            if (applicationMap == null) {
                applicationMap =
                        new ApplicationMap(servletContext);
            }
            return applicationMap;
        }

        @Override
        public String getApplicationContextPath() {
            return servletContext.getContextPath();
        }
        
        public String getAuthType() {
            return null;
        }

        @Override
        public String getMimeType(String file) {
            return servletContext.getMimeType(file);
        }

        public Object getContext() {
            return servletContext;
        }

	public String getContextName() { 
	    return servletContext.getServletContextName();
	}

	

        public String getInitParameter(String name) {
            return servletContext.getInitParameter(name);
        }

        public Map<String,String> getInitParameterMap() {
            if (initMap == null) {
                initMap = new InitParameterMap(servletContext);
            }
            return initMap;
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

        public Map<String,Object> getRequestCookieMap() {
            return Collections.unmodifiableMap(Collections.<String,Object>emptyMap());
        }

        public Map<String,String> getRequestHeaderMap() {
            return Collections.unmodifiableMap(Collections.<String,String>emptyMap());
        }


        public Map<String,String[]> getRequestHeaderValuesMap() {
            return Collections.unmodifiableMap(Collections.<String,String[]>emptyMap());
        }


        public Locale getRequestLocale() {
            return null;
        }

        public Iterator<Locale> getRequestLocales() {
            return null;
        }


        public Map<String,Object> getRequestMap() {
            return Collections.emptyMap();
        }


        public Map<String,String> getRequestParameterMap() {
            return Collections.unmodifiableMap(Collections.<String,String>emptyMap());
        }


        public Iterator<String> getRequestParameterNames() {
            return Collections.<String>emptyList().iterator();
        }


        public Map<String,String[]> getRequestParameterValuesMap() {
            return Collections.unmodifiableMap(Collections.<String,String[]>emptyMap());
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

	public int getRequestContentLength() {
	    return -1;
	}

        public URL getResource(String path) throws MalformedURLException {
            return servletContext.getResource(path);
        }


        public InputStream getResourceAsStream(String path) {
            return servletContext.getResourceAsStream(path);
        }

        public Set<String> getResourcePaths(String path) {
            //noinspection unchecked
            return servletContext.getResourcePaths(path);
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

        public Map<String,Object> getSessionMap() {
            return Collections.emptyMap();
        }

        public java.security.Principal getUserPrincipal() {
            return null;
        }

        public boolean isUserInRole(String role) {
            return false;
        }

        public void log(String message) {
            servletContext.log(message);
        }

        public void log(String message, Throwable exception) {
            servletContext.log(message, exception);
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

        @Override
        public void setResponseHeader(String name, String value) {
        }

        @Override
        public void addResponseHeader(String name, String value) {
        }

        @Override
        public String encodePartialActionURL(String url) {
            return null; 
        }


    } // END ServletContextAdapter

    static Map getThreadInitContextMap() {
        ConcurrentHashMap threadInitContext = null;
        try {
            Field threadMap = FacesContext.class.getDeclaredField("threadInitContext");
            threadMap.setAccessible(true);
            threadInitContext = (ConcurrentHashMap)threadMap.get(null);
        } catch (Exception e) {}
        return threadInitContext;
    }

    static Map getInitContextServletContextMap() {
        ConcurrentHashMap initContextServletContext = null;
        try {
            Field initContextMap = FacesContext.class.getDeclaredField("initContextServletContext");
            initContextMap.setAccessible(true);
            initContextServletContext = (ConcurrentHashMap)initContextMap.get(null);
        } catch (Exception e) {}
        return initContextServletContext;
    }

}

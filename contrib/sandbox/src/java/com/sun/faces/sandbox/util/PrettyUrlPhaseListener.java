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
package com.sun.faces.sandbox.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.LifecycleFactory;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * @author Jason Lee
 */
public class PrettyUrlPhaseListener implements PhaseListener {   
    private static final long serialVersionUID = 1L;

    public static final String URL_PATTERNS_INIT_PARAM = "com.sun.faces.sandbox.urlPatterns";
    private Map<String, String> urlPatterns;

    public PhaseId getPhaseId() {
        loadUrlPatterns(FacesContext.getCurrentInstance());
        return PhaseId.RESTORE_VIEW;
    }   

    public void beforePhase(PhaseEvent event) {
        if (event.getPhaseId() == PhaseId.RESTORE_VIEW) {   
            FacesContext context = FacesContext.getCurrentInstance();
            ExpressionFactory ef = context.getApplication().getExpressionFactory();

            HttpServletRequest request = (HttpServletRequest)   
            context.getExternalContext().getRequest();   
            String uri = request.getRequestURI();
            String contextPath = request.getContextPath();
            uri = uri.substring(contextPath.length());
            if (uri != null) {
                for (Map.Entry<String, String> entry : urlPatterns.entrySet()) {
                    String urlPattern = entry.getKey().trim();
                    String viewId = entry.getValue().trim();
                    UrlMatcher um = new UrlMatcher(urlPattern);
                    Map<String, String> injections = um.getInjections(uri);

                    if (injections != null) {
                        // Set properties
                        for (Map.Entry<String, String> injection : injections.entrySet()) {
                            try {
                                String elExpression = injection.getKey();
                                String uriValue = injection.getValue();

                                ValueExpression ve = ef.createValueExpression(context.getELContext(), 
                                        elExpression, Object.class);
                                if (ve != null) {
                                    ve.setValue(context.getELContext(), 
                                            ef.coerceToType(URLDecoder.decode(uriValue, "UTF-8"), 
                                                    ve.getType(context.getELContext())));
                                }
                            } catch (UnsupportedEncodingException e) {
                                throw new FacesException(e);
                            }
                        }

                        PrettyUrlRequestWrapper wrapper = new PrettyUrlRequestWrapper(request);
                        wrapper.setViewId(viewId);
                        context.getExternalContext().setRequest(wrapper);
                        break;
                    }
                }
            }
        }   
    }

    protected void loadUrlPatterns(FacesContext context) {
        if (urlPatterns == null) {
            urlPatterns = new HashMap<String, String>();
            String patternInitParam = context.getExternalContext().getInitParameter(URL_PATTERNS_INIT_PARAM);
            if (patternInitParam != null) {
                String[] entries = patternInitParam.split("\n");
                for (String entry : entries) {
                    int index = entry.indexOf("=");
                    if (index > -1) {
                        String templateName = entry.substring(0, index);
                        String pattern = entry.substring(index+1);
                        urlPatterns.put(pattern.trim(), templateName.trim());
                    }
                }
            }

            // If no URL patterns have been registered, unregister the PL
            if (urlPatterns.size() == 0) {
                System.out.println("Removing PhaseListener instance");
                LifecycleFactory factory = (LifecycleFactory)
                FactoryFinder.getFactory(FactoryFinder.LIFECYCLE_FACTORY);
                // remove ourselves from the list of listeners maintained by
                // the lifecycle instances
                for(Iterator<String> i = factory.getLifecycleIds(); i.hasNext(); ) {
                    Lifecycle lifecycle = factory.getLifecycle(i.next());
                    lifecycle.removePhaseListener(this);
                }
            }
        }
    }

    public void afterPhase(PhaseEvent event) {
        // Nothing happens here
    }
}

class PrettyUrlRequestWrapper extends HttpServletRequestWrapper {   
    private String viewId;   

    @Override  
    public String getPathInfo() {   
        return viewId;   
    }

    @Override
    public String getServletPath() {
        String servletPath = super.getServletPath();
        if (servletPath.contains(viewId)) { 
            servletPath = servletPath.substring(0, servletPath.indexOf(viewId));
        }
        
        return servletPath; //"/";
    }
    
    

    public PrettyUrlRequestWrapper(HttpServletRequest request) {   
        super(request);   
    }   

    public void setViewId(String viewId) {   
        this.viewId = "/" + viewId;   
    }   
}  

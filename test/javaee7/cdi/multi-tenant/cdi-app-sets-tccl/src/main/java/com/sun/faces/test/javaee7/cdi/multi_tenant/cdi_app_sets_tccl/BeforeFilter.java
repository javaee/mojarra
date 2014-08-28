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

package com.sun.faces.test.javaee7.cdi.multi_tenant.cdi_app_sets_tccl;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLClassLoader;
import javax.faces.FactoryFinder;
import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextFactory;
import javax.faces.lifecycle.LifecycleFactory;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BeforeFilter implements Filter {
    
    private FilterConfig filterConfig = null;
    
    private static final String INIT_HAS_LIFECYCLE_KEY = "BeforeServlet_hasLifecycle";
    private static final String INIT_HAS_INITFACESCONTEXT_KEY = "BeforeServlet_hasInitFacesContext";
    
    private static final String REQUEST_HAS_LIFECYCLE = "BeforeServlet_requestHasLifecycle";
    private static final String REQUEST_HAS_FACESCONTEXT = "BeforeServlet_requestHasFacesContext";

    public BeforeFilter() {
    }    
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {
        
        Thread thread = Thread.currentThread();
        ClassLoader tccl = thread.getContextClassLoader();
        ClassLoader tcclp1 = new URLClassLoader(new URL[0], tccl); //new URLClassLoader(new URL [0]);
        thread.setContextClassLoader(tcclp1);
        
        HttpServletRequest req = (HttpServletRequest) request;
        LifecycleFactory lifecycle = (LifecycleFactory) FactoryFinder.getFactory(FactoryFinder.LIFECYCLE_FACTORY);
        req.setAttribute(REQUEST_HAS_LIFECYCLE, 
                (null != lifecycle) ? "TRUE":"FALSE");
        FacesContext facesContext = FacesContext.getCurrentInstance();
        req.setAttribute(REQUEST_HAS_FACESCONTEXT, 
                (null != facesContext) ? "TRUE":"FALSE");
        
        // Dispatching to JSF throws this exception:
        /**
         * 
         * 

java.lang.IllegalStateException: Singleton not set for java.net.URLClassLoader@511e5bf4
	at org.glassfish.weld.ACLSingletonProvider$ACLSingleton.get(ACLSingletonProvider.java:110)
	at org.jboss.weld.Container.instance(Container.java:54)
	at org.jboss.weld.jsf.ConversationAwareViewHandler.getConversationContext(ConversationAwareViewHandler.java:80)
	at org.jboss.weld.jsf.ConversationAwareViewHandler.getActionURL(ConversationAwareViewHandler.java:102)
	at com.sun.faces.renderkit.html_basic.FormRenderer.getActionStr(FormRenderer.java:250)
	at com.sun.faces.renderkit.html_basic.FormRenderer.encodeBegin(FormRenderer.java:143)
	at javax.faces.component.UIComponentBase.encodeBegin(UIComponentBase.java:864)
	at javax.faces.component.UIComponent.encodeAll(UIComponent.java:1854)
	at javax.faces.component.UIComponent.encodeAll(UIComponent.java:1859)
	at com.sun.faces.application.view.FaceletViewHandlingStrategy.renderView(FaceletViewHandlingStrategy.java:456)
	at com.sun.faces.application.view.MultiViewHandler.renderView(MultiViewHandler.java:133)
	at javax.faces.application.ViewHandlerWrapper.renderView(ViewHandlerWrapper.java:337)
	at com.sun.faces.lifecycle.RenderResponsePhase.execute(RenderResponsePhase.java:120)
	at com.sun.faces.lifecycle.Phase.doPhase(Phase.java:101)
	at com.sun.faces.lifecycle.LifecycleImpl.render(LifecycleImpl.java:219)
         * 
         */
        // I think this is a side-effect of Weld also not being resilient
        // to TCCL replacement.  To continue with the job of exercising 
        // the fix in FactoryFinder, we just exercise it directly here.
        // I confirmed this is fixed in Weld 2.2.2 Final, which is in GlassFish 4.0.1
        final boolean weldIsTCCLReplacementResilient = false;
        
        if (weldIsTCCLReplacementResilient) {
            try {
                chain.doFilter(request, response);
            } catch (Exception t) {
                throw new ServletException(t);
            } finally {
                thread.setContextClassLoader(tccl);
            }
        } else {
            FacesContextFactory fcFactory = (FacesContextFactory) 
                    FactoryFinder.getFactory(FactoryFinder.FACES_CONTEXT_FACTORY);
            HttpServletResponse resp = (HttpServletResponse) response;
            PrintWriter pw = resp.getWriter();
            try {
                if (null != fcFactory) {
                    pw.print("<html><body><p id=\"result\">SUCCESS</p></body></html>");
                } else {
                    pw.print("<html><body><p id=\"result\">FAILURE</p></body></html>");
                }
                resp.setStatus(200);
                pw.close();
            } catch (Exception e) {
            }
        }
        
    }

    public void destroy() {        
    }

    public void init(FilterConfig filterConfig) {        
        this.filterConfig = filterConfig;
        ServletContext sc = this.filterConfig.getServletContext();
        LifecycleFactory lifecycle = (LifecycleFactory) FactoryFinder.getFactory(FactoryFinder.LIFECYCLE_FACTORY);
        sc.setAttribute(INIT_HAS_LIFECYCLE_KEY, 
                (null != lifecycle) ? "TRUE":"FALSE");
        FacesContext initFacesContext = FacesContext.getCurrentInstance();
        sc.setAttribute(INIT_HAS_INITFACESCONTEXT_KEY, 
                (null != initFacesContext) ? "TRUE":"FALSE");
        
        
    }
    
}

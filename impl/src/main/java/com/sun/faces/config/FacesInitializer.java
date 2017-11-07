/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2009-2017 Oracle and/or its affiliates. All rights reserved.
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


import static com.sun.faces.RIConstants.ANNOTATED_CLASSES;
import static com.sun.faces.RIConstants.FACES_INITIALIZER_MAPPINGS_ADDED;
import static com.sun.faces.util.Util.isEmpty;
import static java.lang.Boolean.TRUE;

import java.net.MalformedURLException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.spi.CDI;
import javax.faces.annotation.FacesConfig;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.bean.ManagedBean;
import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponent;
import javax.faces.component.behavior.FacesBehavior;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.event.ListenerFor;
import javax.faces.event.ListenersFor;
import javax.faces.event.NamedEvent;
import javax.faces.event.PhaseListener;
import javax.faces.render.FacesBehaviorRenderer;
import javax.faces.render.Renderer;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.view.facelets.FaceletsResourceResolver;
import javax.faces.webapp.FacesServlet;
import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.HandlesTypes;
import javax.websocket.Endpoint;
import javax.websocket.server.ServerApplicationConfig;
import javax.websocket.server.ServerContainer;
import javax.websocket.server.ServerEndpoint;

import com.sun.faces.cdi.CdiExtension;

/**
 * Adds mappings <em>*.xhtml</em>, <em>/faces</em>, <em>*.jsf</em>, and <em>*.faces</em> for the
 * FacesServlet (if it hasn't already been mapped) if the following conditions
 * are met:
 *
 * <ul>
 *    <li>
 *       The <code>Set</code> of classes passed to this initializer is not
 *       empty, or
 *    </li>
 *    <li>
 *       /WEB-INF/faces-config.xml exists, or
 *    </li>
 *     <li>
 *       A CDI enabled bean with qualifier FacesConfig can be obtained
 *    </li>
 * </ul>
 */
@HandlesTypes({
    Converter.class,
    Endpoint.class,
    FaceletsResourceResolver.class,
    FacesBehavior.class,
    FacesBehaviorRenderer.class,
    FacesComponent.class,
    FacesConverter.class,
    FacesConfig.class, // Should actually be check for enabled bean, but difficult to guarantee, see SERVLET_SPEC-79
    FacesValidator.class,
    ListenerFor.class,
    ListenersFor.class,
    ManagedBean.class,
    NamedEvent.class,
    PhaseListener.class,
    Renderer.class,
    Resource.class,
    ResourceDependencies.class,
    ResourceDependency.class,
    ServerApplicationConfig.class,
    ServerEndpoint.class,
    UIComponent.class,
    Validator.class
})
public class FacesInitializer implements ServletContainerInitializer {

    // NOTE: Loggins should not be used with this class.

    private static final String FACES_SERVLET_CLASS = FacesServlet.class.getName();

    
    // -------------------------------- Methods from ServletContainerInitializer

    @Override
    public void onStartup(Set<Class<?>> classes, ServletContext servletContext) throws ServletException {

        Set<Class<?>> annotatedClasses = new HashSet<Class<?>>();
        if (classes != null) {
            annotatedClasses.addAll(classes);
        }
        servletContext.setAttribute(ANNOTATED_CLASSES, annotatedClasses);
        
        boolean appHasSomeJsfContent = appMayHaveSomeJsfContent(classes, servletContext);
        boolean appHasFacesServlet = getExistingFacesServletRegistration(servletContext) != null;

        if (appHasSomeJsfContent || appHasFacesServlet) {
            InitFacesContext initFacesContext = new InitFacesContext(servletContext);
            try {
                if (appHasSomeJsfContent) {
                    // Only look at mapping concerns if there is JSF content
                    handleMappingConcerns(servletContext);
                }
                // Other concerns also handled if there is an existing Faces Servlet mapping
                handleWebSocketConcerns(servletContext);
            } finally {
                // Bug 20458755: The InitFacesContext was not being cleaned up, resulting in
                // a partially constructed FacesContext being made available
                // to other code that re-uses this Thread at init time.
                initFacesContext.releaseCurrentInstance();
                initFacesContext.release();
            }
        }
    }
    
    
    // --------------------------------------------------------- Private Methods
    
    private boolean appMayHaveSomeJsfContent(Set<Class<?>> classes, ServletContext context) {

        if (!isEmpty(classes)) {
            return true;
        }

        // No JSF specific classes found, check for a WEB-INF/faces-config.xml
        try {
            if (context.getResource("/WEB-INF/faces-config.xml") != null) {
                return true;
            }
        } catch (MalformedURLException mue) {

        }
        
        // In the future remove FacesConfig.class from the @HandlesTypes annotation
        // and only check via CDI
        try {
            CDI<Object> cdi = null;
            try {
                cdi = CDI.current();
                
                if (cdi != null) {
                
                    Instance<CdiExtension> extension = cdi.select(CdiExtension.class);
                    
                    if (!extension.isAmbiguous() && !extension.isUnsatisfied()) {
                        return extension.get().isAddBeansForJSFImplicitObjects();
                    }
                }
                
            } catch (IllegalStateException e) {
                // Ignore, CDI not active for this module
            }
            
        } catch (Exception e) {
            // Any other exception; Ignore too
        }

        return false;
    }

    private void handleMappingConcerns(ServletContext servletContext) throws ServletException {
        
        ServletRegistration existingFacesServletRegistration = getExistingFacesServletRegistration(servletContext);
        if (existingFacesServletRegistration != null) {
            // FacesServlet has already been defined, so we're
            // not going to add additional mappings;
            if (isADFApplication()) {
                // For Bug 21114997 and 21322338 add additional mappings
                existingFacesServletRegistration.addMapping("*.xhtml", "*.jsf");
            }
            return;
        }

        ServletRegistration reg = servletContext.addServlet("FacesServlet", "javax.faces.webapp.FacesServlet");

        if ("true".equalsIgnoreCase(servletContext.getInitParameter("javax.faces.DISABLE_FACESSERVLET_TO_XHTML")) ) {
            reg.addMapping("/faces/*", "*.jsf", "*.faces");
        } else {
            reg.addMapping("/faces/*", "*.jsf", "*.faces", "*.xhtml");
        }

        servletContext.setAttribute(FACES_INITIALIZER_MAPPINGS_ADDED, TRUE);

        // The following line is temporary until we can solve an ordering
        // issue in V3.  Right now the JSP container looks for a mapping
        // of the FacesServlet in the web.xml.  If it's not present, then
        // it assumes that the application isn't a faces application.  In this
        // case the JSP container will not register the ConfigureListener
        // definition from our TLD nor will it parse cause or JSP TLDs to
        // be parsed.
        servletContext.addListener(com.sun.faces.config.ConfigureListener.class);
    }
    
    private ServletRegistration getExistingFacesServletRegistration(ServletContext servletContext) {
        Map<String,? extends ServletRegistration> existing = servletContext.getServletRegistrations();
        for (ServletRegistration registration : existing.values()) {
            if (FACES_SERVLET_CLASS.equals(registration.getClassName())) {
                return registration;
            }
        }
        
        return null;
    }

    private void handleWebSocketConcerns(ServletContext ctx) throws ServletException {
        if (ctx.getAttribute(ServerContainer.class.getName()) != null) {
            // Already initialized
            return;
        }

        if (!Boolean.valueOf(ctx.getInitParameter("javax.faces.ENABLE_WEBSOCKET_ENDPOINT"))) {
            // Register websocket endpoint is not enabled
            return;
        }

        ClassLoader cl = ctx.getClassLoader();

        Class<?> tyrusInitializerClass;
        try {
            tyrusInitializerClass = cl.loadClass("org.glassfish.tyrus.servlet.TyrusServletContainerInitializer");
        } catch (ClassNotFoundException cnfe) {
            // No possibility of WebSocket.
            return;
        }
        
        try {
            ServletContainerInitializer tyrusInitializer = (ServletContainerInitializer) tyrusInitializerClass.newInstance();
            Class<?> configClass = cl.loadClass("org.glassfish.tyrus.server.TyrusServerConfiguration");

            // List of classes must be non empty. TyrusServerConfiguration is ignored,
            // so we add that as class to trigger Tyrus to initialize
            HashSet<Class<?>> filteredClasses = new HashSet<>();
            filteredClasses.add(configClass);

            tyrusInitializer.onStartup(filteredClasses, ctx);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            throw new ServletException(ex);
        }

    }

    private boolean isADFApplication() {

        boolean hasResource = false;
        try {
            ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
            if (contextClassLoader != null) {
                hasResource = contextClassLoader.getResource("oracle/adf/view/rich/context/AdfFacesContext.class") != null;
            }
        } catch (Exception e) {
            // Intentionally swallow exception.  This should be logged
            // but for the comment at the top stating that Loggins should
            // not be used for this class.  I assume that means Logging, and
            // not Kenny Loggins.
        }
        
        return hasResource;
    }

}

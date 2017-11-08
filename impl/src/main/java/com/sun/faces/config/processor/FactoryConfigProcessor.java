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

package com.sun.faces.config.processor;

import static java.text.MessageFormat.format;
import static java.util.Arrays.asList;
import static java.util.logging.Level.FINE;

import java.text.MessageFormat;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.FactoryFinder;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sun.faces.application.InjectionApplicationFactory;
import com.sun.faces.config.ConfigurationException;
import com.sun.faces.config.InitFacesContext;
import com.sun.faces.config.manager.documents.DocumentInfo;
import com.sun.faces.context.InjectionFacesContextFactory;
import com.sun.faces.util.FacesLogger;

/**
 * <p>
 * This <code>ConfigProcessor</code> handles all elements defined under
 * <code>/faces-config/factory</code>.
 * </p>
 */
public class FactoryConfigProcessor extends AbstractConfigProcessor {

    private static final Logger LOGGER = FacesLogger.CONFIG.getLogger();

    /**
     * <code>/faces-config/factory</code>
     */
    private static final String FACTORY = "factory";

    /**
     * <code>/faces-config/factory/application-factory</code>
     */
    private static final String APPLICATION_FACTORY = "application-factory";

    /**
     * <code>/faces-config/factory/exception-handler-factory</code>
     */
    private static final String EXCEPTION_HANDLER_FACTORY = "exception-handler-factory";

    /**
     * <code>/faces-config/factory/flash-factory</code>
     */
    private static final String FLASH_FACTORY = "flash-factory";

    /**
     * <code>/faces-config/factory/visit-context-factory</code>
     */
    private static final String VISIT_CONTEXT_FACTORY = "visit-context-factory";

    /**
     * <code>/faces-config/factory/view-declaration-language-factory</code>
     */
    private static final String VIEW_DECLARATION_LANGUAGE_FACTORY = "view-declaration-language-factory";

    /**
     * <code>/faces-config/factory/tag-handler-helper-factory</code>
     */
    private static final String TAG_HANDLER_DELEGATE_FACTORY = "tag-handler-delegate-factory";

    /**
     * <code>/faces-config/factory/facelet-cache-factory</code>
     */
    private static final String FACELET_CACHE_FACTORY = "facelet-cache-factory";

    /**
     * <code>/faces-config/factory/faces-context-factory</code>
     */
    private static final String FACES_CONTEXT_FACTORY = "faces-context-factory";

    /**
     * <code>/faces-config/factory/client-window-factory</code>
     */
    private static final String CLIENT_WINDOW_FACTORY = "client-window-factory";

    /**
     * <code>/faces-config/factory/partial-view-context-factory</code>
     */
    private static final String PARTIAL_VIEW_CONTEXT_FACTORY = "partial-view-context-factory";

    /**
     * <code>faces-config/factory/lifecycle-factory</code>
     */
    private static final String LIFECYCLE_FACTORY = "lifecycle-factory";

    /**
     * <code>/faces-config/factory/render-kit-factory</code>
     */
    private static final String RENDER_KIT_FACTORY = "render-kit-factory";

    /**
     * <code>/faces-config/factory/external-context-factory</code>
     */
    private static final String EXTERNAL_CONTEXT_FACTORY = "external-context-factory";

    /**
     * <code>/faces-config/factory/flow-handler-factory</code>
     */
    private static final String FLOW_HANDLER_FACTORY = "flow-handler-factory";

    /**
     * <code>/faces-config/factory/search-expression-context-factory</code>
     */
    private static final String SEARCH_EXPRESSION_CONTEXT_FACTORY = "search-expression-context-factory";

    /**
     * <code>Array of Factory names for post-configuration validation.</code>
     */
    private final List<String> factoryNames = asList(
            FactoryFinder.APPLICATION_FACTORY, 
            FactoryFinder.CLIENT_WINDOW_FACTORY,
            FactoryFinder.EXCEPTION_HANDLER_FACTORY, 
            FactoryFinder.EXTERNAL_CONTEXT_FACTORY, 
            FactoryFinder.FACES_CONTEXT_FACTORY, 
            FactoryFinder.FLASH_FACTORY,
            FactoryFinder.LIFECYCLE_FACTORY, 
            FactoryFinder.VIEW_DECLARATION_LANGUAGE_FACTORY, 
            FactoryFinder.PARTIAL_VIEW_CONTEXT_FACTORY,
            FactoryFinder.RENDER_KIT_FACTORY, 
            FactoryFinder.VISIT_CONTEXT_FACTORY, 
            FactoryFinder.FACELET_CACHE_FACTORY,
            FactoryFinder.TAG_HANDLER_DELEGATE_FACTORY, 
            FactoryFinder.FLOW_HANDLER_FACTORY, 
            FactoryFinder.SEARCH_EXPRESSION_CONTEXT_FACTORY); 
    
    
    private boolean validateFactories = true;

    // ------------------------------------------------------------ Constructors

    public FactoryConfigProcessor() {
    }

    // Only called by the cactus test code
    public FactoryConfigProcessor(boolean validateFactories) {
        this.validateFactories = validateFactories;
    }

    // -------------------------------------------- Methods from ConfigProcessor

    /**
     * @see ConfigProcessor#process(javax.servlet.ServletContext,com.sun.faces.config.manager.documents.DocumentInfo[])
     */
    @Override
    public void process(ServletContext servletContext, FacesContext facesContext, DocumentInfo[] documentInfos) throws Exception {

        // track how many FacesContextFactory instances are being added
        // for this application
        AtomicInteger facesContextFactoryCount = new AtomicInteger(0);

        // track how many ApplicationFactory instances are being added
        // for this application
        AtomicInteger applicationFactoryCount = new AtomicInteger(0);

        for (int i = 0; i < documentInfos.length; i++) {
            if (LOGGER.isLoggable(FINE)) {
                LOGGER.log(FINE, format("Processing factory elements for document: ''{0}''", documentInfos[i].getSourceURI()));
            }
            
            Document document = documentInfos[i].getDocument();
            String namespace = document.getDocumentElement().getNamespaceURI();
            NodeList factories = document.getDocumentElement().getElementsByTagNameNS(namespace, FACTORY);
            if (factories != null && factories.getLength() > 0) {
                processFactories(factories, namespace, facesContextFactoryCount, applicationFactoryCount);
            }
        }

        // If there are more than one ApplicationFactory, FacesContextFactory,
        // or ExternalContextFactory defined, we will push our Injection
        // factories onto the factory list so that they are the top-level
        // factory. This allows us to inject the default instances into the
        // custom Application, FacesContext, or ExternalContext
        // implementations to get around version compatibility issues.
        // This *must* be called *before* verifyFactoriesExist() as once that
        // is called, we can't make any changes to the Factory delegation
        // chain.
        wrapFactories(applicationFactoryCount.get(), facesContextFactoryCount.get());

        // Validate that we actually have factories at this point.
        verifyFactoriesExist(servletContext);
    }

    // --------------------------------------------------------- Private Methods

    private void processFactories(NodeList factories, String namespace, AtomicInteger fcCount, AtomicInteger appCount) {

        for (int i = 0, size = factories.getLength(); i < size; i++) {
            Node factory = factories.item(i);
            NodeList children = ((Element) factory).getElementsByTagNameNS(namespace, "*");
            for (int c = 0, csize = children.getLength(); c < csize; c++) {
                Node childNode = children.item(c);
                switch (childNode.getLocalName()) {
                    case APPLICATION_FACTORY:
                        int cnt = appCount.incrementAndGet();
                        setFactory(FactoryFinder.APPLICATION_FACTORY, getNodeText(childNode));
                        break;
                    case EXCEPTION_HANDLER_FACTORY:
                        setFactory(FactoryFinder.EXCEPTION_HANDLER_FACTORY, getNodeText(childNode));
                        break;
                    case VISIT_CONTEXT_FACTORY:
                        setFactory(FactoryFinder.VISIT_CONTEXT_FACTORY, getNodeText(childNode));
                        break;
                    case LIFECYCLE_FACTORY:
                        setFactory(FactoryFinder.LIFECYCLE_FACTORY, getNodeText(childNode));
                        break;
                    case FLASH_FACTORY:
                        setFactory(FactoryFinder.FLASH_FACTORY, getNodeText(childNode));
                        break;
                    case CLIENT_WINDOW_FACTORY:
                        setFactory(FactoryFinder.CLIENT_WINDOW_FACTORY, getNodeText(childNode));
                        break;
                    case FACES_CONTEXT_FACTORY:
                        fcCount.incrementAndGet();
                        setFactory(FactoryFinder.FACES_CONTEXT_FACTORY, getNodeText(childNode));
                        break;
                    case RENDER_KIT_FACTORY:
                        setFactory(FactoryFinder.RENDER_KIT_FACTORY, getNodeText(childNode));
                        break;
                    case VIEW_DECLARATION_LANGUAGE_FACTORY:
                        setFactory(FactoryFinder.VIEW_DECLARATION_LANGUAGE_FACTORY, getNodeText(childNode));
                        break;
                    case TAG_HANDLER_DELEGATE_FACTORY:
                        setFactory(FactoryFinder.TAG_HANDLER_DELEGATE_FACTORY, getNodeText(childNode));
                        break;
                    case FACELET_CACHE_FACTORY:
                        setFactory(FactoryFinder.FACELET_CACHE_FACTORY, getNodeText(childNode));
                        break;
                    case EXTERNAL_CONTEXT_FACTORY:
                        setFactory(FactoryFinder.EXTERNAL_CONTEXT_FACTORY, getNodeText(childNode));
                        break;
                    case PARTIAL_VIEW_CONTEXT_FACTORY:
                        setFactory(FactoryFinder.PARTIAL_VIEW_CONTEXT_FACTORY, getNodeText(childNode));
                        break;
                    case FLOW_HANDLER_FACTORY:
                        setFactory(FactoryFinder.FLOW_HANDLER_FACTORY, getNodeText(childNode));
                        break;
                    case SEARCH_EXPRESSION_CONTEXT_FACTORY:
                        setFactory(FactoryFinder.SEARCH_EXPRESSION_CONTEXT_FACTORY, getNodeText(childNode));
                        break;
                }
            }
        }
    }

    private static void setFactory(String factoryName, String factoryImpl) {

        if (factoryName != null && factoryImpl != null) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE, MessageFormat.format("Calling FactoryFinder.setFactory({0}, {1})", factoryName, factoryImpl));
            }
            FactoryFinder.setFactory(factoryName, factoryImpl);
        }

    }

    private void verifyFactoriesExist(ServletContext servletContext) {

        if (validateFactories) {
            
            Deque<Exception> exceptions = new ConcurrentLinkedDeque<>();
            
            
            ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
            
            try {
                factoryNames.stream().forEach(e -> {
                    
                    Thread.currentThread().setContextClassLoader(contextClassLoader);
                    InitFacesContext.getInstance(servletContext);
                   
                    try {
                        FactoryFinder.getFactory(e);
                    } catch (Exception exception) {
                        exception.printStackTrace();
                        exceptions.add(new ConfigurationException(format("Factory ''{0}'' was not configured properly.", e), exception));
                    } finally {
                        Thread.currentThread().setContextClassLoader(null);
                    }
                    
                });
            } finally {
                Thread.currentThread().setContextClassLoader(contextClassLoader);
            }
            
        }
    }

    private void wrapFactories(int appCount, int fcCount) {

        if (appCount > 1) {
            addInjectionApplicationFactory();
        }
        
        if (fcCount > 1) {
            addInjectionFacesContextFactory();
        }

    }

    /**
     * Add the InjectionApplicationFactory as the top-level ApplicationFactory so that the default
     * instances, provided by {@link com.sun.faces.application.ApplicationFactoryImpl} can be
     * injected into the actual top-level FacesContext instance (that which is returned by the
     * InjectionFacesContextFactory's delegate).
     */
    private void addInjectionApplicationFactory() {
        FactoryFinder.setFactory(FactoryFinder.APPLICATION_FACTORY, InjectionApplicationFactory.class.getName());
    }

    /**
     * Add the InjectionFacesContextFactory as the top-level FacesContextFactory so that the default
     * instances, provided by {@link com.sun.faces.context.FacesContextFactoryImpl} can be injected
     * into the actual top-level FacesContext instance (that which is returned by the
     * InjectionFacesContextFactory's delegate).
     */
    private void addInjectionFacesContextFactory() {
        FactoryFinder.setFactory(FactoryFinder.FACES_CONTEXT_FACTORY, InjectionFacesContextFactory.class.getName());
    }

}

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

package com.sun.faces.config.processor;

import static java.text.MessageFormat.format;
import static java.util.logging.Level.FINE;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

import javax.faces.FactoryFinder;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseListener;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.LifecycleFactory;
import javax.servlet.ServletContext;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sun.faces.config.DocumentInfo;
import com.sun.faces.util.FacesLogger;

/**
 * <p>
 * This <code>ConfigProcessor</code> handles all elements defined under
 * <code>/faces-config/lifecycle</code>.
 * </p>
 */
public class LifecycleConfigProcessor extends AbstractConfigProcessor {

    private static final Logger LOGGER = FacesLogger.CONFIG.getLogger();

    /**
     * <p>
     * /faces-config/lifecycle
     * </p>
     */
    private static final String LIFECYCLE = "lifecycle";

    /**
     * <p>
     * /faces-config/lifecycle/phase-listener
     * </p>
     */
    private static final String PHASE_LISTENER = "phase-listener";
    private List<PhaseListener> appPhaseListeners;

    public LifecycleConfigProcessor() {
        appPhaseListeners = new CopyOnWriteArrayList<PhaseListener>();
    }


    // -------------------------------------------- Methods from ConfigProcessor

    /**
     * @see ConfigProcessor#process(javax.servlet.ServletContext,com.sun.faces.config.DocumentInfo[])
     */
    @Override
    public void process(ServletContext servletContext, FacesContext facesContext, DocumentInfo[] documentInfos) throws Exception {

        LifecycleFactory factory = (LifecycleFactory) FactoryFinder.getFactory(FactoryFinder.LIFECYCLE_FACTORY);

        for (int i = 0; i < documentInfos.length; i++) {
            if (LOGGER.isLoggable(FINE)) {
                LOGGER.log(FINE, format("Processing lifecycle elements for document: ''{0}''", documentInfos[i].getSourceURI()));
            }
            
            Document document = documentInfos[i].getDocument();
            String namespace = document.getDocumentElement().getNamespaceURI();
            NodeList lifecycles = document.getElementsByTagNameNS(namespace, LIFECYCLE);
            
            if (lifecycles != null) {
                for (int c = 0, csize = lifecycles.getLength(); c < csize; c++) {
                    Node lifecyleNode = lifecycles.item(c);
                    if (lifecyleNode.getNodeType() == Node.ELEMENT_NODE) {
                        NodeList listeners = ((Element) lifecyleNode).getElementsByTagNameNS(namespace, PHASE_LISTENER);
                        addPhaseListeners(servletContext, facesContext, factory, listeners);
                    }
                }
            }
        }

    }
    
    @Override
    public void destroy(ServletContext sc, FacesContext facesContext) {
        destroyInstances(sc, facesContext, appPhaseListeners);
    }
  

    // --------------------------------------------------------- Private Methods
    

    private void addPhaseListeners(ServletContext sc, FacesContext facesContext, LifecycleFactory factory, NodeList phaseListeners) {

        if (phaseListeners != null && phaseListeners.getLength() > 0) {
            for (int i = 0, size = phaseListeners.getLength(); i < size; i++) {
                Node phaseListenerNode = phaseListeners.item(i);
                String phaseListenerClassName = getNodeText(phaseListenerNode);
                
                if (phaseListenerClassName != null) {
                    boolean[] didPerformInjection = { false };
                    
                    PhaseListener phaseListener = (PhaseListener) 
                        createInstance(
                                sc, facesContext, phaseListenerClassName, 
                                PhaseListener.class, null, phaseListenerNode, 
                                true, didPerformInjection);
                    
                    if (phaseListener != null) {
                        if (didPerformInjection[0]) {
                            appPhaseListeners.add(phaseListener);
                        }
                        
                        for (Iterator<String> t = factory.getLifecycleIds(); t.hasNext();) {
                            String lfId = (String) t.next();
                            Lifecycle lifecycle = factory.getLifecycle(lfId);
                            if (LOGGER.isLoggable(FINE)) {
                                LOGGER.log(FINE, format("Adding PhaseListener ''{0}'' to lifecycle ''{0}}", phaseListenerClassName, lfId));
                            }
                            
                            lifecycle.addPhaseListener(phaseListener);
                        }
                    }
                }
            }
        }
    }
    
    private void destroyInstances(ServletContext sc, FacesContext facesContext, List<?> instances) {
        for (Object instance : instances) {
            destroyInstance(sc, facesContext, instance.getClass().getName(), instance);
        }
        
        instances.clear();
    }

}

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

package com.sun.faces.config.processor;

import com.sun.faces.util.FacesLogger;
import com.sun.faces.config.DocumentInfo;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Document;

import javax.faces.FactoryFinder;
import javax.faces.event.PhaseListener;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.LifecycleFactory;
import javax.servlet.ServletContext;
import java.text.MessageFormat;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * <p>
 *  This <code>ConfigProcessor</code> handles all elements defined under
 *  <code>/faces-config/lifecycle</code>.
 * </p>
 */
public class LifecycleConfigProcessor extends AbstractConfigProcessor {

    private static final Logger LOGGER = FacesLogger.CONFIG.getLogger();

    /**
     * <p>/faces-config/lifecycle</p>
     */
    private static final String LIFECYCLE = "lifecycle";

    /**
     * <p>/faces-config/lifecycle/phase-listener</p>
     */
    private static final String PHASE_LISTENER = "phase-listener";
    private List<PhaseListener> appPhaseListeners;

    public LifecycleConfigProcessor() {
        appPhaseListeners = new CopyOnWriteArrayList<PhaseListener>();
    }
    
    @Override
    public void destroy(ServletContext sc) {
        destroyInstances(sc, appPhaseListeners);
        
        destroyNext(sc);
    }
    
    private void destroyInstances(ServletContext sc, List instances) {
        for (Object cur : instances) {
            destroyInstance(sc, cur.getClass().getName(), cur);
        }
        instances.clear();
    }

    // -------------------------------------------- Methods from ConfigProcessor


    /**
     * @see ConfigProcessor#process(javax.servlet.ServletContext,com.sun.faces.config.DocumentInfo[])
     */
    public void process(ServletContext sc, DocumentInfo[] documentInfos)
    throws Exception {

        LifecycleFactory factory = (LifecycleFactory)
             FactoryFinder.getFactory(FactoryFinder.LIFECYCLE_FACTORY);

        for (int i = 0; i < documentInfos.length; i++) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE,
                           MessageFormat.format(
                                "Processing lifecycle elements for document: ''{0}''",
                                documentInfos[i].getSourceURI()));
            }
            Document document = documentInfos[i].getDocument();
            String namespace =
                 document.getDocumentElement().getNamespaceURI();
            NodeList lifecycles = 
                 document.getElementsByTagNameNS(namespace, LIFECYCLE);
            if (lifecycles != null) {
                for (int c = 0, csize = lifecycles.getLength(); c < csize; c++) {
                    Node n = lifecycles.item(c);
                    if (n.getNodeType() == Node.ELEMENT_NODE) {
                        NodeList listeners = ((Element) n).getElementsByTagNameNS(namespace,
                                                                                  PHASE_LISTENER);
                        addPhaseListeners(sc, factory, listeners);
                    }
                }
            }            
        }
        invokeNext(sc, documentInfos);

    }

    
    // --------------------------------------------------------- Private Methods


    private void addPhaseListeners(ServletContext sc, LifecycleFactory factory,
                                   NodeList phaseListeners) {

        if (phaseListeners != null && phaseListeners.getLength() > 0) {
            for (int i = 0, size = phaseListeners.getLength(); i < size; i++) {
                Node plNode = phaseListeners.item(i);
                String pl = getNodeText(plNode);
                if (pl != null) {
                    boolean [] didPerformInjection = { false };
                    PhaseListener plInstance = (PhaseListener) createInstance(sc, pl,
                                                       PhaseListener.class,
                                                       null,
                                                       plNode, true, didPerformInjection);
                    if (plInstance != null) {
                        if (didPerformInjection[0]) {
                            appPhaseListeners.add(plInstance);
                        }
                        for (Iterator t = factory.getLifecycleIds(); t.hasNext();)
                        {
                            String lfId = (String) t.next();
                            Lifecycle lifecycle = factory.getLifecycle(lfId);
                            if (LOGGER.isLoggable(Level.FINE)) {
                                LOGGER.log(Level.FINE,
                                           MessageFormat.format(
                                                "Adding PhaseListener ''{0}'' to lifecycle ''{0}}",
                                                pl,
                                                lfId));
                            }
                            lifecycle.addPhaseListener(plInstance);
                        }
                    }
                } 
            }
        }

    }

}

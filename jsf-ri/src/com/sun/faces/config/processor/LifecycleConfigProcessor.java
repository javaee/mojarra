/*
 * $Id: LifecycleConfigProcessor.java,v 1.2 2007/04/23 20:23:05 rlubke Exp $
 */

/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 *
 * You can obtain a copy of the License at
 * https://javaserverfaces.dev.java.net/CDDL.html or
 * legal/CDDLv1.0.txt.
 * See the License for the specific language governing
 * permission and limitations under the License.
 *
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at legal/CDDLv1.0.txt.
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 * [Name of File] [ver.__] [Date]
 *
 * Copyright 2007 Sun Microsystems Inc. All Rights Reserved
 */

package com.sun.faces.config.processor;

import com.sun.faces.util.FacesLogger;
import com.sun.faces.util.Util;
import com.sun.faces.config.WebConfiguration;
import com.sun.faces.config.ConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import javax.faces.FactoryFinder;
import javax.faces.event.PhaseListener;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.LifecycleFactory;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.text.MessageFormat;

/**
 * <p>
 *  This <code>ConfigProcessor</code> handles all elements defined under
 *  <code>/faces-config/lifecycle</code>.
 * </p>
 */
public class LifecycleConfigProcessor extends AbstractConfigProcessor {

    private static final Logger LOGGER =
         Logger.getLogger(FacesLogger.CONFIG.getLoggerName());

    private static final String JS_PHASE_LISTENER =
         "com.sun.faces.lifecycle.JsfJsResourcePhaseListener";

    /**
     * <p>/faces-config/lifecycle/phase-listener
     */
    private static final String PHASE_LISTENER = "/d:faces-config/d:lifecycle/d:phase-listener";


    // -------------------------------------------- Methods from ConfigProcessor


    /**
     * @see ConfigProcessor#process(org.w3c.dom.Document[])
     */
    public void process(Document[] documents)
    throws Exception {

        LifecycleFactory factory = (LifecycleFactory)
             FactoryFinder.getFactory(FactoryFinder.LIFECYCLE_FACTORY);

        for (int i = 0; i < documents.length; i++) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE,
                           MessageFormat.format(
                                "Processing lifecycle elements for document: ''{0}''",
                                documents[i].getDocumentURI()));
            }
            NodeList lifecycles = documents[i].getDocumentElement().getElementsByTagName("lifecycle");
            if (lifecycles != null) {
                for (int c = 0, csize = lifecycles.getLength(); c < csize; c++) {
                    Node n = lifecycles.item(c);
                    if (n.getNodeType() == Node.ELEMENT_NODE) {
                        NodeList listeners = ((Element) n).getElementsByTagName("phase-listener");
                        addPhaseListeners(factory, listeners);
                    }
                }
            }            
        }
        invokeNext(documents);

    }


    // --------------------------------------------------------- Private Methods


    private void addPhaseListeners(LifecycleFactory factory,
                                   NodeList phaseListeners) {

        WebConfiguration webConfig = WebConfiguration.getInstance();
        boolean jsPLEnabled = webConfig.getBooleanContextInitParameter(
             WebConfiguration.BooleanWebContextInitParameter.ExternalizeJavaScript);
        if (phaseListeners != null && phaseListeners.getLength() > 0) {
            for (int i = 0, size = phaseListeners.getLength(); i < size; i++) {
                String pl = getNodeText(phaseListeners.item(i));
                if (!jsPLEnabled && JS_PHASE_LISTENER.equals(pl)) {
                    continue;
                }
                if (pl != null) {
                    Object plInstance = Util.createInstance(pl);
                    if (plInstance != null) {
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
                            lifecycle
                                 .addPhaseListener((PhaseListener) plInstance);
                        }
                    }
                } 
            }
        }

    }

}

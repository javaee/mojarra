/*
 * $Id: LifecycleConfigProcessor.java,v 1.6 2007/06/25 20:57:20 rlubke Exp $
 */

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

package com.sun.faces.config.processor;

import static com.sun.faces.config.WebConfiguration.BooleanWebContextInitParameter;
import com.sun.faces.config.WebConfiguration;
import com.sun.faces.util.FacesLogger;
import com.sun.faces.util.Util;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.faces.FactoryFinder;
import javax.faces.event.PhaseListener;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.LifecycleFactory;
import java.text.MessageFormat;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * <p>
 *  This <code>ConfigProcessor</code> handles all elements defined under
 *  <code>/faces-config/lifecycle</code>.
 * </p>
 */
public class LifecycleConfigProcessor extends AbstractConfigProcessor {

    private static final Logger LOGGER = FacesLogger.CONFIG.getLogger();

    private static final String JS_PHASE_LISTENER =
         "com.sun.faces.lifecycle.JsfJsResourcePhaseListener";

    /**
     * <p>/faces-config/lifecycle</p>
     */
    private static final String LIFECYCLE = "lifecycle";

    /**
     * <p>/faces-config/lifecycle/phase-listener</p>
     */
    private static final String PHASE_LISTENER = "phase-listener";


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
            String namespace =
                 documents[i].getDocumentElement().getNamespaceURI();
            NodeList lifecycles = 
                 documents[i].getDocumentElement().getElementsByTagNameNS(namespace,
                                                                          LIFECYCLE);
            if (lifecycles != null) {
                for (int c = 0, csize = lifecycles.getLength(); c < csize; c++) {
                    Node n = lifecycles.item(c);
                    if (n.getNodeType() == Node.ELEMENT_NODE) {
                        NodeList listeners = ((Element) n).getElementsByTagNameNS(namespace,
                                                                                  PHASE_LISTENER);
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
        boolean jsPLEnabled = webConfig.isOptionEnabled(
             BooleanWebContextInitParameter.ExternalizeJavaScript);
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

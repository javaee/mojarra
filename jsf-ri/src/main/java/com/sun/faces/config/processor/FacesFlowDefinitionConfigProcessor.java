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
package com.sun.faces.config.processor;

import com.sun.faces.config.DocumentInfo;
import com.sun.faces.config.WebConfiguration;
import com.sun.faces.util.FacesLogger;
import java.text.MessageFormat;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

/**
 * <p>
 *  This <code>ConfigProcessor</code> handles all elements defined under
 *  <code>/faces-config/faces-flow-definition</code>.
 * </p>
 */
public class FacesFlowDefinitionConfigProcessor extends AbstractConfigProcessor {

    private static final Logger LOGGER = FacesLogger.CONFIG.getLogger();
    
    /**
     * <code>/faces-config/faces-flow-definition</code>
     */
    private static final String FACES_FLOW_DEFINITION = "faces-flow-definition";
    
    /**
     * <code>/faces-config/faces-flow-definition/default-flow-node</code>
     */
//    private static final String DEFAULT_FLOW_NODE = "default-flow-node";
    
    /**
     * <code>/faces-config/faces-flow-definition/view</code>
     */
    private static final String VIEW = "view";
    
    /**
     * <code>/faces-config/faces-flow-definition/view/page</code>
     */
    private static final String PAGE = "PAGE";
    
    private boolean validateFactories = true;

    public FacesFlowDefinitionConfigProcessor() {
    }

    public FacesFlowDefinitionConfigProcessor(boolean validateFactories) {
        this.validateFactories = validateFactories;
    }

    public void process(ServletContext sc, DocumentInfo[] documentInfos)
    throws Exception {

        // track how many faces-flow-definition instances are being added
        // for this application
        AtomicInteger facesFlowDefinitionCount = new AtomicInteger(0);
        
        for (int i = 0; i < documentInfos.length; i++) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE,
                           MessageFormat.format(
                                "Processing factory elements for document: ''{0}''",
                                documentInfos[i].getSourceURI()));
            }
            Document document = documentInfos[i].getDocument();
            String namespace = document.getDocumentElement()
                 .getNamespaceURI();
            NodeList flows = document.getDocumentElement()
                 .getElementsByTagNameNS(namespace, FACES_FLOW_DEFINITION);
            if (flows != null && flows.getLength() > 0) {
                processFacesFlowDefinitions(flows,
                                 /* namespace, */
                                 facesFlowDefinitionCount);
            }
        }
        
        if (0 < facesFlowDefinitionCount.get()) {
            // The flow feature is effectively disabled if no flows are detected
            // at startup time.  This is accomplished by adding a simple boolean
            // property to WebConfiguration that is checked whenever the feature
            // is implemented.  This causes an otherwise unnecessary ThreadLocal 
            // lookup.  Once we have the proper bootstrapping implemented,
            // I expect we can dispense with WebConfiguration.hasFlows.
            WebConfiguration config = WebConfiguration.getInstance(sc);
            config.setHasFlows(true);
            String optionValue = config.getOptionValue(WebConfiguration.WebContextInitParameter.ClientWindowMode);
            boolean clientWindowNeedsEnabling = false;
            if ("none".equals(optionValue)) {
                clientWindowNeedsEnabling = true;
                String featureName = 
                        WebConfiguration.WebContextInitParameter.ClientWindowMode.getQualifiedName();
                LOGGER.log(Level.WARNING, 
                        "{0} was set to none, but Faces Flows requires {0} is enabled.  Setting to ''url''.", new Object[]{featureName});
            } else if (null == optionValue) {
                clientWindowNeedsEnabling = true;
            }
            if (clientWindowNeedsEnabling) {
                config.setOptionValue(WebConfiguration.WebContextInitParameter.ClientWindowMode, "url");
            }
            
        }
        
        invokeNext(sc, documentInfos);
    }
    
    private void processFacesFlowDefinitions(NodeList factories, /*String namespace,*/
            AtomicInteger flowCount) {
        for (int i = 0, size = factories.getLength(); i < size; i++) {
            flowCount.incrementAndGet();
//            Node factory = factories.item(i);
//            NodeList children = ((Element) factory)
//                 .getElementsByTagNameNS(namespace, "*");
//            for (int c = 0, csize = children.getLength(); c < csize; c++) {
//                Node n = children.item(c);
//                if (DEFAULT_FLOW_NODE.equals(n.getLocalName())) {
//                } 
//            }
        }
        
    }
    
    
}

/*
 * $Id: FactoryConfigProcessor.java,v 1.3 2007/04/25 04:06:58 rlubke Exp $
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
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;

import javax.faces.FactoryFinder;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.text.MessageFormat;


/**
 * <p>
 *  This <code>ConfigProcessor</code> handles all elements defined under
 *  <code>/faces-config/factory</code>.
 * </p>
 */
public class FactoryConfigProcessor extends AbstractConfigProcessor {

    private static final Logger LOGGER = FacesLogger.CONFIG.getLogger();

    /**
     * <code>/faces-config/factory</code>
     */
    private static final String FACTORY = "factory";

    /**
     * <code>/faces-config/factory/application-factory
     */
    private static final String APPLICATION_FACTORY = "application-factory";

    /**
     * <code>/faces-config/factory/faces-context-factory
     */
    private static final String FACES_CONTEXT_FACTORY = "faces-context-factory";

    /**
     * <code>faces-config/factory/lifecycle-factory
     */
    private static final String LIFECYCLE_FACTORY = "lifecycle-factory";

    /**
     * <cod>/faces-config/factory/render-kit-factory
     */
    private static final String RENDER_KIT_FACTORY = "render-kit-factory";


    // -------------------------------------------- Methods from ConfigProcessor


    /**
     * @see ConfigProcessor#process(org.w3c.dom.Document[])
     */
    public void process(Document[] documents)
    throws Exception {

        for (int i = 0; i < documents.length; i++) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE,
                           MessageFormat.format(
                                "Processing factory elements for document: ''{0}''",
                                documents[i].getDocumentURI()));
            }
            String namespace = documents[i].getDocumentElement()
                 .getNamespaceURI();
            NodeList factories = documents[i].getDocumentElement()
                 .getElementsByTagNameNS(namespace, FACTORY);
            if (factories != null && factories.getLength() > 0) {
                processFactories(factories, namespace);
            }
        }

        invokeNext(documents);

    }

    // --------------------------------------------------------- Private Methods


    private void processFactories(NodeList factories, String namespace) {
        for (int i = 0, size = factories.getLength(); i < size; i++) {
            Node factory = factories.item(i);
            NodeList children = ((Element) factory)
                 .getElementsByTagNameNS(namespace, "*");
            for (int c = 0, csize = children.getLength(); c < csize; c++) {
                Node n = children.item(c);
                if (APPLICATION_FACTORY.equals(n.getLocalName())) {
                    setFactory(FactoryFinder.APPLICATION_FACTORY,
                               getNodeText(n));
                } else if (LIFECYCLE_FACTORY.equals(n.getLocalName())) {
                    setFactory(FactoryFinder.LIFECYCLE_FACTORY,
                               getNodeText(n));
                } else if (FACES_CONTEXT_FACTORY.equals(n.getLocalName())) {
                    setFactory(FactoryFinder.FACES_CONTEXT_FACTORY,
                               getNodeText(n));
                } else if (RENDER_KIT_FACTORY.equals(n.getLocalName())) {
                    setFactory(FactoryFinder.RENDER_KIT_FACTORY,
                               getNodeText(n));
                }
            }
        }
    }


    private static void setFactory(String factoryName, String factoryImpl) {

        if (factoryName != null && factoryImpl != null) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE,
                           MessageFormat.format(
                                "Calling FactoryFinder.setFactory({0}, {1}",
                                factoryName,
                                factoryImpl));
            }
            FactoryFinder.setFactory(factoryName, factoryImpl);
        }

    }

}

/*
 * $Id: FactoryConfigProcessor.java,v 1.4 2007/04/27 22:00:56 ofung Exp $
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

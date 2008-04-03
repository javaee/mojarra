/*
 * $Id: ComponentConfigProcessor.java,v 1.2 2007/04/24 19:04:21 rlubke Exp $
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

import javax.faces.application.Application;
import javax.xml.xpath.XPathExpressionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.text.MessageFormat;

/**
 * <p>
 *  This <code>ConfigProcessor</code> handles all elements defined under
 *  <code>/faces-config/component</code>.
 * </p>
 */
public class ComponentConfigProcessor extends AbstractConfigProcessor {

    private static final Logger LOGGER =
         Logger.getLogger(FacesLogger.CONFIG.getLoggerName());


    /**
     * <p>/faces-config/component</p>
     */
    private static final String COMPONENT = "component";

    /**
     * <p>/faces-config/component/component-type</p>
     */
    private static final String COMPONENT_TYPE = "component-type";

    /**
     * <p>/faces-config/component/component-class</p>
     */
    private static final String COMPONENT_CLASS = "component-class";


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
                                "Processing component elements for document: ''{0}''",
                                documents[i].getDocumentURI()));
            }
            String namespace = documents[i].getDocumentElement()
                 .getNamespaceURI();
            NodeList components = documents[i].getDocumentElement()
                 .getElementsByTagNameNS(namespace, COMPONENT);
            if (components != null && components.getLength() > 0) {
                addComponents(components, namespace);
            }
        }
        invokeNext(documents);

    }


    // --------------------------------------------------------- Private Methods


    private void addComponents(NodeList components, String namespace)
    throws XPathExpressionException {

        Application app = getApplication();
        for (int i = 0, size = components.getLength(); i < size; i++) {
            Node componentNode = components.item(i);
            NodeList children = ((Element) componentNode)
                 .getElementsByTagNameNS(namespace, "*");
            String componentType = null;
            String componentClass = null;
            for (int c = 0, csize = children.getLength(); c < csize; c++) {
                Node n = children.item(c);
                if (COMPONENT_TYPE.equals(n.getLocalName())) {
                    componentType = getNodeText(n);
                } else if (COMPONENT_CLASS.equals(n.getLocalName())) {
                    componentClass = getNodeText(n);
                }
            }

            if (componentType != null && componentClass != null) {
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.log(Level.FINE,
                               MessageFormat.format(
                                    "Calling Application.addComponent({0},{1})",
                                    componentType,
                                    componentClass));
                }
                app.addComponent(componentType, componentClass);
            }
        }
    }

}

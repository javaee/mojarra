/*
 * $Id: ConverterConfigProcessor.java,v 1.2 2007/04/24 19:04:21 rlubke Exp $
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
import com.sun.faces.config.ConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;

import javax.faces.application.Application;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.text.MessageFormat;

/**
 * <p>
 *  This <code>ConfigProcessor</code> handles all elements defined
 *  under <code>/faces-config/converter</code>.
 * </p>
 */
public class ConverterConfigProcessor extends AbstractConfigProcessor {

    private static final Logger LOGGER =
         Logger.getLogger(FacesLogger.CONFIG.getLoggerName());

    /**
     * <code>/faces-config/converter</code>
     */
    private static final String CONVERTER = "converter";

    /**
     * <code>/faces-config/converter/converter-id</code>
     * (mutually exclusive with converter-for-class)
     */
    private static final String CONVERTER_ID
         = "converter-id";

    /**
     * <code>/faces-config/converter/converter-for-class</code>
     * (mutually exclusive with converter-id)
     */
    private static final String CONVERTER_FOR_CLASS
         = "converter-for-class";

    /**
     * <code>/faces-config/converter/converter-class</code>
     */
    private static final String CONVERTER_CLASS
         = "converter-class";

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
                                "Processing converter elements for document: ''{0}''",
                                documents[i].getDocumentURI()));
            }
            String namespace = documents[i].getDocumentElement()
                 .getNamespaceURI();
            NodeList nodes = documents[i].getDocumentElement()
                 .getElementsByTagNameNS(namespace, CONVERTER);
            if (nodes != null && nodes.getLength() > 0) {
                addConverters(nodes, namespace);
            }
        }
        invokeNext(documents);

    }

    // --------------------------------------------------------- Private Methods


    private void addConverters(NodeList converters, String namespace) {

        Application application = getApplication();

        for (int i = 0, size = converters.getLength(); i < size; i++) {
            Node converter = converters.item(i);
            NodeList children = ((Element) converter)
                 .getElementsByTagNameNS(namespace, "*");
            String converterId = null;
            String converterClass = null;
            String converterForClass = null;
            for (int c = 0, csize = children.getLength(); c < csize; c++) {
                Node n = children.item(c);
                if (CONVERTER_ID.equals(n.getLocalName())) {
                    converterId = getNodeText(n);
                } else if (CONVERTER_CLASS.equals(n.getLocalName())) {
                    converterClass = getNodeText(n);
                } else if (CONVERTER_FOR_CLASS.equals(n.getLocalName())) {
                    converterForClass = getNodeText(n);
                }
            }

            if (converterId != null && converterClass != null) {
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.log(Level.FINE,
                               MessageFormat.format(
                                    "[Converter by ID] Calling Application.addConverter({0}, {1}",
                                    converterId,
                                    converterClass));
                }
                application.addConverter(converterId, converterClass);
            } else if (converterClass != null && converterForClass != null) {
                try {
                    Class cfcClass = Util.loadClass(converterForClass,
                                                    this.getClass());
                    if (LOGGER.isLoggable(Level.FINE)) {
                        LOGGER.log(Level.FINE,
                                   MessageFormat.format(
                                        "[Converter for Class] Calling Application.addConverter({0}, {1}",
                                        converterForClass,
                                        converterClass));
                    }
                    application.addConverter(cfcClass, converterClass);
                } catch (ClassNotFoundException cnfe) {
                    throw new ConfigurationException(cnfe);
                }
            }
        }
    }

}

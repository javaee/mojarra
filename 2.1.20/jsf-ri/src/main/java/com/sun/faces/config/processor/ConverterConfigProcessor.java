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

import com.sun.faces.config.ConfigurationException;
import com.sun.faces.config.Verifier;
import com.sun.faces.config.DocumentInfo;
import com.sun.faces.util.FacesLogger;
import com.sun.faces.util.Util;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Document;

import javax.faces.application.Application;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.servlet.ServletContext;

import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * <p>
 *  This <code>ConfigProcessor</code> handles all elements defined
 *  under <code>/faces-config/converter</code>.
 * </p>
 */
public class ConverterConfigProcessor extends AbstractConfigProcessor {

    private static final Logger LOGGER = FacesLogger.CONFIG.getLogger();

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
     * @see ConfigProcessor#process(javax.servlet.ServletContext,com.sun.faces.config.DocumentInfo[])
     */
    public void process(ServletContext sc, DocumentInfo[] documentInfos)
    throws Exception {

        // process annotated converters first as converters configured
        // via config files take precedence
        processAnnotations(FacesConverter.class);

        for (int i = 0; i < documentInfos.length; i++) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE,
                           MessageFormat.format(
                                "Processing converter elements for document: ''{0}''",
                                documentInfos[i].getSourceURI()));
            }
            Document document = documentInfos[i].getDocument();
            String namespace = document.getDocumentElement()
                 .getNamespaceURI();
            NodeList nodes = document.getDocumentElement()
                 .getElementsByTagNameNS(namespace, CONVERTER);
            if (nodes != null && nodes.getLength() > 0) {
                addConverters(nodes, namespace);
            }
        }
        invokeNext(sc, documentInfos);

    }

    // --------------------------------------------------------- Private Methods


    private void addConverters(NodeList converters, String namespace) {

        Application application = getApplication();
        Verifier verifier = Verifier.getCurrentInstance();
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
                if (verifier != null) {
                    verifier.validateObject(Verifier.ObjectType.CONVERTER,
                                            converterClass,
                                            Converter.class);
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
                    if (verifier != null) {
                        verifier.validateObject(Verifier.ObjectType.CONVERTER,
                                converterClass,
                                Converter.class);
                    }
                    application.addConverter(cfcClass, converterClass);
                } catch (ClassNotFoundException cnfe) {
                    throw new ConfigurationException(cnfe);
                }
            }
        }
    }

}

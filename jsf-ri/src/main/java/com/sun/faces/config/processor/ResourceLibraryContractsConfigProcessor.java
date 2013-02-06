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

import com.sun.faces.application.ApplicationAssociate;
import com.sun.faces.config.DocumentInfo;
import com.sun.faces.util.FacesLogger;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ResourceLibraryContractsConfigProcessor extends AbstractConfigProcessor {

    /**
     * Stores the logger.
     */
    private static final Logger LOGGER = FacesLogger.CONFIG.getLogger();
    /**
     * <code>/faces-config/resource-library-contracts</code>
     */
    private static final String RESOURCE_LIBRARY_CONTRACTS = "resource-library-contracts";

    /**
     * Constructor.
     */
    public ResourceLibraryContractsConfigProcessor() {
    }

    /**
     * Process the configuration documents.
     *
     * @param servletContext the servlet context.
     * @param documentInfos the document info(s).
     * @throws Exception when an error occurs.
     */
    @Override
    public void process(ServletContext servletContext, DocumentInfo[] documentInfos) throws Exception {

        HashMap<String, List<String>> map = new HashMap<String, List<String>>();
        for (int i = 0; i < documentInfos.length; i++) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE, MessageFormat.format(
                        "Processing factory elements for document: ''{0}''",
                        documentInfos[i].getSourceURI()));
            }
            Document document = documentInfos[i].getDocument();
            String namespace = document.getDocumentElement().getNamespaceURI();
            NodeList resourceLibraryContracts = document.getDocumentElement().getElementsByTagNameNS(namespace, RESOURCE_LIBRARY_CONTRACTS);
            if (resourceLibraryContracts != null && resourceLibraryContracts.getLength() > 0) {
                processResourceLibraryContracts(resourceLibraryContracts, map);
            }

        }

        if (!map.isEmpty()) {
            ApplicationAssociate associate = ApplicationAssociate.getCurrentInstance();
            associate.setResourceLibraryContracts(map);
        }

        invokeNext(servletContext, documentInfos);
    }

    /**
     * Process the resource library contracts.
     *
     * @param resourceLibraryContracts the resource library contracts.
     * @param map the set of resource library contracts.
     */
    private void processResourceLibraryContracts(NodeList resourceLibraryContracts, HashMap<String, List<String>> map) {
        XPath xpath = XPathFactory.newInstance().newXPath();
        xpath.setNamespaceContext(new FacesConfigNamespaceContext());
        for (int c = 0; c < resourceLibraryContracts.getLength(); c++) {
            Node node = resourceLibraryContracts.item(c);
            try {
                NodeList mappings = (NodeList) xpath.evaluate(".//ns1:contract-mapping", node, XPathConstants.NODESET);
                if (mappings != null) {
                    for (int m = 0; m < mappings.getLength(); m++) {
                        Node contractMapping = mappings.item(m);
                        NodeList urlPatterns = (NodeList) xpath.evaluate(".//ns1:url-pattern/text()", contractMapping, XPathConstants.NODESET);
                        if (urlPatterns != null) {
                            for (int p = 0; p < urlPatterns.getLength(); p++) {
                                String urlPattern = urlPatterns.item(p).getNodeValue().trim();

                                if (LOGGER.isLoggable(Level.INFO)) {
                                    LOGGER.log(Level.INFO, "Processing resource library contract mapping for url-pattern: {0}", urlPattern);
                                }

                                if (!map.containsKey(urlPattern)) {
                                    /*
                                     * If there is no urlPattern then add it to the list,
                                     */
                                    ArrayList<String> list = new ArrayList<String>();
                                    NodeList contracts = (NodeList) xpath.evaluate(".//ns1:contracts/text()", contractMapping, XPathConstants.NODESET);
                                    if (contracts != null && contracts.getLength() > 0) {
                                        for (int j = 0; j < contracts.getLength(); j++) {
                                            String[] contractStrings = contracts.item(j).getNodeValue().trim().split(",");
                                            for (int k = 0; k < contractStrings.length; k++) {
                                                if (!list.contains(contractStrings[k])) {
                                                    if (LOGGER.isLoggable(Level.INFO)) {
                                                        LOGGER.log(Level.INFO,
                                                                "Added contract: {0} for url-pattern: {1}",
                                                                new Object[]{contractStrings[k], urlPattern});
                                                    }
                                                    list.add(contractStrings[k]);
                                                } else {
                                                    /*
                                                     * We found the contract again in the list for the specified url-pattern.
                                                     */
                                                    if (LOGGER.isLoggable(Level.INFO)) {
                                                        LOGGER.log(Level.INFO,
                                                                "Duplicate contract: {0} found for url-pattern: {1}",
                                                                new Object[]{contractStrings[k], urlPattern});
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    if (!list.isEmpty()) {
                                        /*
                                         * Now add the url-pattern and its contracts.
                                         */
                                        map.put(urlPattern, list);
                                    } else {
                                        /*
                                         * The list was empty, log there were no contracts specified.
                                         */
                                        if (LOGGER.isLoggable(Level.INFO)) {
                                            LOGGER.log(Level.INFO, "No contracts found for url-pattern: {0}", urlPattern);
                                        }
                                    }
                                } else {
                                    /*
                                     * Otherwise log there is a duplicate url-pattern found.
                                     */
                                    if (LOGGER.isLoggable(Level.INFO)) {
                                        LOGGER.log(Level.INFO, "Duplicate url-patern found: {0}, ignoring it", urlPattern);
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (XPathExpressionException exception) {
                /*
                 * This particular exception will never happen since the 
                 * above valid XPath expressions never change, but the XPath 
                 * runtime defines it as a checked exception so we have to 
                 * deal with it.
                 */
                if (LOGGER.isLoggable(Level.FINEST)) {
                    LOGGER.log(Level.FINEST, "Unable to parse XPath expression", exception);
                }
            }
        }
    }

    private static class FacesConfigNamespaceContext implements NamespaceContext {

        @Override
        public String getNamespaceURI(String prefix) {
            return "http://java.sun.com/xml/ns/javaee";
        }

        @Override
        public String getPrefix(String namespaceURI) {
            return "ns1";
        }

        @Override
        public Iterator getPrefixes(String namespaceURI) {
            return Collections.emptyList().iterator();
        }
    }
}

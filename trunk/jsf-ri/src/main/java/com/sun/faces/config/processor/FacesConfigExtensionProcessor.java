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

import com.sun.faces.config.DocumentInfo;
import com.sun.faces.config.WebConfiguration;
import com.sun.faces.util.FacesLogger;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Document;

import javax.servlet.ServletContext;

import java.text.MessageFormat;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * <p>
 *  This <code>ConfigProcessor</code> handles all elements defined under
 *  <code>/faces-config/factory</code>.
 * </p>
 */
public class FacesConfigExtensionProcessor extends AbstractConfigProcessor {

    private static final Logger LOGGER = FacesLogger.CONFIG.getLogger();

    /**
     * <code>/faces-config/faces-config-extension</code>
     */
    private static final String FACES_CONFIG_EXTENSION = "faces-config-extension";

    /**
     * <code>/faces-config/faces-config-extension/facelets-processing</code>
     */
    private static final String FACELETS_PROCESSING = "facelets-processing";

    /**
     * <code>/faces-config/faces-config-extension/facelets-processing/file-extension</code>
     */
    private static final String FILE_EXTENSION = "file-extension";

    /**
     * <code>/faces-config/faces-config-extension/facelets-processing/process-as</code>
     */
    private static final String PROCESS_AS = "process-as";

    // ------------------------------------------------------------ Constructors

    
    public FacesConfigExtensionProcessor() { }


    // -------------------------------------------- Methods from ConfigProcessor


    /**
     * @see ConfigProcessor#process(javax.servlet.ServletContext,com.sun.faces.config.DocumentInfo[])
     */
    public void process(ServletContext sc, DocumentInfo[] documentInfos)
    throws Exception {

        for (int i = 0; i < documentInfos.length; i++) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE,
                           MessageFormat.format(
                                "Processing faces-config-extension elements for document: ''{0}''",
                                documentInfos[i].getSourceURI()));
            }
            Document document = documentInfos[i].getDocument();
            String namespace = document.getDocumentElement()
                 .getNamespaceURI();
            NodeList facesConfigExtensions = document.getDocumentElement()
                 .getElementsByTagNameNS(namespace, FACES_CONFIG_EXTENSION);
            if (facesConfigExtensions != null && facesConfigExtensions.getLength() > 0) {
                processFacesConfigExtensions(facesConfigExtensions,
                                 namespace, documentInfos[i]);
            }
        }

        // invoke the next config processor
        invokeNext(sc, documentInfos);

    }

    // --------------------------------------------------------- Private Methods


    private void processFacesConfigExtensions(NodeList facesConfigExtensions,
                                  String namespace, DocumentInfo info) {
        WebConfiguration config = null;

        for (int i = 0, size = facesConfigExtensions.getLength(); i < size; i++) {
            Node facesConfigExtension = facesConfigExtensions.item(i);
            NodeList children = ((Element) facesConfigExtension)
                 .getElementsByTagNameNS(namespace, "*");
            for (int c = 0, csize = children.getLength(); c < csize; c++) {
                Node n = children.item(c);
                if (FACELETS_PROCESSING.equals(n.getLocalName())) {
                    Node faceletsProcessing = n;
                    NodeList faceletsProcessingChildren = ((Element) faceletsProcessing)
                           .getElementsByTagNameNS(namespace, "*");
                    String fileExtension = null, processAs = null;
                    for (int fp = 0, fpsize = faceletsProcessingChildren.getLength(); fp < fpsize; fp++) {
                        Node childOfInterset = faceletsProcessingChildren.item(fp);
                        if (null == fileExtension &&
                            FILE_EXTENSION.equals(childOfInterset.getLocalName())) {
                            fileExtension = getNodeText(childOfInterset);
                        } else if (null == processAs &&
                                   PROCESS_AS.equals(childOfInterset.getLocalName())) {
                            processAs = getNodeText(childOfInterset);
                        } else {
                            if (LOGGER.isLoggable(Level.WARNING)) {
                                LOGGER.log(Level.WARNING,
                                        MessageFormat.format(
                                        "Processing faces-config-extension elements for document: ''{0}'', encountered unexpected configuration ''{1}'', ignoring and continuing",
                                        info.getSourceURI(), getNodeText(childOfInterset)));
                            }
                        }

                    }

                    if (null != fileExtension && null != processAs) {
                        if (null == config) {
                            config = WebConfiguration.getInstance();
                        }
                        Map<String, String> faceletsProcessingMappings =
                                config.getFacesConfigOptionValue(WebConfiguration.WebContextInitParameter.FaceletsProcessingFileExtensionProcessAs, true);
                        faceletsProcessingMappings.put(fileExtension, processAs);

                    } else {
                        if (LOGGER.isLoggable(Level.WARNING)) {
                            LOGGER.log(Level.WARNING,
                                    MessageFormat.format(
                                    "Processing faces-config-extension elements for document: ''{0}'', encountered <facelets-processing> elemnet without expected children",
                                    info.getSourceURI()));
                        }
                    }
                }
            }
        }

    }


}

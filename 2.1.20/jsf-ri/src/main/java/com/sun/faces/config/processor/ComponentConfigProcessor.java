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

import com.sun.faces.util.FacesLogger;
import com.sun.faces.config.Verifier;
import com.sun.faces.config.DocumentInfo;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import org.w3c.dom.Document;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.component.FacesComponent;
import javax.servlet.ServletContext;
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

    private static final Logger LOGGER = FacesLogger.CONFIG.getLogger();


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
     * @see ConfigProcessor#process(javax.servlet.ServletContext,com.sun.faces.config.DocumentInfo[])  @param sc
     * @param documentInfos
     */
    public void process(ServletContext sc, DocumentInfo[] documentInfos)
    throws Exception {

        // process annotated components first as components configured
        // via config files take precedence
        processAnnotations(FacesComponent.class);

        for (int i = 0; i < documentInfos.length; i++) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE,
                           MessageFormat.format(
                                "Processing component elements for document: ''{0}''",
                                documentInfos[i].getSourceURI()));
            }
            Document document = documentInfos[i].getDocument();
            String namespace = document.getDocumentElement()
                 .getNamespaceURI();
            NodeList components = document.getDocumentElement()
                 .getElementsByTagNameNS(namespace, COMPONENT);
            if (components != null && components.getLength() > 0) {
                addComponents(components, namespace);
            }
        }
        invokeNext(sc, documentInfos);

    }

    

    // --------------------------------------------------------- Private Methods


    private void addComponents(NodeList components, String namespace)
    throws XPathExpressionException {

        Application app = getApplication();
        Verifier verifier = Verifier.getCurrentInstance();
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
                if (verifier != null) {
                    verifier.validateObject(Verifier.ObjectType.COMPONENT,
                                            componentClass,
                                            UIComponent.class);
                }
                app.addComponent(componentType, componentClass);
            }
        }
    }

}

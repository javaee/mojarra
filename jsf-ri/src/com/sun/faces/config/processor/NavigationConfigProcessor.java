/*
 * $Id: NavigationConfigProcessor.java,v 1.3 2007/04/25 04:06:58 rlubke Exp $
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

import com.sun.faces.application.ApplicationAssociate;
import com.sun.faces.application.ConfigNavigationCase;
import com.sun.faces.util.FacesLogger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPathExpressionException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.text.MessageFormat;

/**
 * <p>
 *  This <code>ConfigProcessor</code> handles all elements defined under
 *  <code>/faces-config/managed-bean</code>.
 * </p>
 */
public class NavigationConfigProcessor extends AbstractConfigProcessor {

    private static final Logger LOGGER = FacesLogger.CONFIG.getLogger();

    /**
     * <p>/faces-config/navigation-rule</p>
     */
    private static final String NAVIGATION_RULE = "navigation-rule";

    /**
     * <p>/faces-config/navigation-rule/from-view-id</p>
     */
    private static final String FROM_VIEW_ID = "from-view-id";

    /**
     * <p>/faces-config/navigation-rule/navigation-case</p>
     */
    private static final String NAVIGATION_CASE = "navigation-case";

    /**
     * <p>/faces-config/navigation-rule/navigation-case/from-action</p>
     */
    private static final String FROM_ACTION = "from-action";

    /**
     * <p>/faces-config/navigation-rule/navigation-case/from-outcome</p>
     */
    private static final String FROM_OUTCOME = "from-outcome";

    /**
     * <p>/faces-config/navigation-rule/navigation-case/to-view-id</p>
     */
    private static final String TO_VIEW_ID = "to-view-id";

    /**
     * <p>/faces-config/navigation-rule/navigation-case/redirect</p>
     */
    private static final String REDIRECT = "redirect";

    /**
     * <p>If <code>from-view-id</code> is not defined.<p>
     */
    private static final String FROM_VIEW_ID_DEFAULT = "*";


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
                                "Processing navigation-rule elements for document: ''{0}''",
                                documents[i].getDocumentURI()));
            }
            String namespace = documents[i].getDocumentElement()
                 .getNamespaceURI();
            NodeList navigationRules = documents[i].getDocumentElement()
                 .getElementsByTagNameNS(namespace, NAVIGATION_RULE);
            if (navigationRules != null && navigationRules.getLength() > 0) {
                addNavigationRules(navigationRules);
            }

        }
        invokeNext(documents);

    }


    // --------------------------------------------------------- Private Methods


    private void addNavigationRules(NodeList navigationRules)
    throws XPathExpressionException {

            for (int i = 0, size = navigationRules.getLength(); i < size; i++) {
                Node navigationRule = navigationRules.item(i);
                if (navigationRule.getNodeType() == Node.ELEMENT_NODE) {
                    NodeList children = navigationRule.getChildNodes();
                    String fromViewId = FROM_VIEW_ID_DEFAULT;
                    List<Node> navigationCases = null;
                    for (int c = 0, csize = children.getLength();
                         c < csize;
                         c++) {
                        Node n = children.item(c);
                        if (n.getNodeType() == Node.ELEMENT_NODE) {
                            if (FROM_VIEW_ID.equals(n.getLocalName())) {
                                String t = getNodeText(n);
                                fromViewId = ((t == null)
                                              ? FROM_VIEW_ID_DEFAULT
                                              : t);
                            } else if (NAVIGATION_CASE.equals(n.getLocalName())) {
                                if (navigationCases == null) {
                                    navigationCases = new ArrayList(csize);
                                }
                                navigationCases.add(n);
                            }
                        }
                    }

                    if (LOGGER.isLoggable(Level.FINE)) {
                        LOGGER.log(Level.FINE,
                                   MessageFormat.format(
                                        "Processing navigation rule with 'from-view-id' of ''{0}''",
                                        fromViewId));
                    }
                    addNavigationCasesForRule(fromViewId,
                                              navigationCases);
                }
            }
    }


    private void addNavigationCasesForRule(String fromViewId,
                                           List<Node> navigationCases) {

        if (navigationCases != null && !navigationCases.isEmpty()) {
            ApplicationAssociate associate =
                 ApplicationAssociate.getCurrentInstance();

            for (Node navigationCase : navigationCases) {
                if (navigationCase.getNodeType() != Node.ELEMENT_NODE) {
                    continue;
                }
                NodeList children = navigationCase.getChildNodes();
                String outcome = null;
                String action = null;
                String toViewId = null;
                boolean redirect = false;
                for (int i = 0, size = children.getLength(); i < size; i++) {
                    Node n = children.item(i);
                    if (n.getNodeType() == Node.ELEMENT_NODE) {
                        if (FROM_OUTCOME.equals(n.getLocalName())) {
                            outcome = getNodeText(n);
                        } else if (FROM_ACTION.equals(n.getLocalName())) {
                            action = getNodeText(n);
                        } else if (TO_VIEW_ID.equals(n.getLocalName())) {
                            toViewId = getNodeText(n);
                        } else if (REDIRECT.equals(n.getLocalName())) {
                            redirect = true;
                        }
                    }
                }

                ConfigNavigationCase cnc =
                     new ConfigNavigationCase(fromViewId,
                                              action,
                                              outcome,
                                              toViewId,
                                              redirect);
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.log(Level.FINE,
                               MessageFormat.format("Adding NavigationCase: {0}",
                                                    cnc.toString()));
                }
                associate.addNavigationCase(cnc);
            }
        }
    }

}
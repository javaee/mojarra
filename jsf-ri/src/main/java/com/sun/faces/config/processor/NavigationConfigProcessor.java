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
import javax.faces.application.NavigationCase;
import javax.faces.application.NavigationHandler;
import javax.faces.application.ConfigurableNavigationHandler;

import com.sun.faces.util.FacesLogger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.servlet.ServletContext;
import javax.xml.xpath.XPathExpressionException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.text.MessageFormat;

import com.sun.faces.config.DocumentInfo;


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
     * <p>/faces-config/navigation-rule/navigation-case/if</p>
     */
    private static final String IF = "if";

    /**
     * <p>/faces-config/navigation-rule/navigation-case/to-view-id</p>
     */
    private static final String TO_VIEW_ID = "to-view-id";

    /**
     * <p>/faces-config/navigation-rule/navigation-case/to-flow-document-id</p>
     */
    private static final String TO_FLOW_DOCUMENT_ID = "to-flow-document-id";

    /**
     * <p>/faces-config/navigation-rule/navigation-case/redirect</p>
     */
    private static final String REDIRECT = "redirect";

    /**
     * <p>/faces-confg/navigation-rule/navigation-case/redirect/view-param</p>
     */
    private static final String VIEW_PARAM = "view-param";

    /**
     * <p>/faces-confg/navigation-rule/navigation-case/redirect/view-param/name</p>
     */
    private static final String VIEW_PARAM_NAME = "name";

    /**
     * <p>/faces-confg/navigation-rule/navigation-case/redirect/view-param/value</p>
     */
    private static final String VIEW_PARAM_VALUE = "value";

    /**
     * <p>/faces-config/navigation-rule/navigation-case/redirect[@include-page-params]</p>
     */
    private static final String INCLUDE_VIEW_PARAMS_ATTRIBUTE = "include-view-params";

    /**
     * <p>If <code>from-view-id</code> is not defined.<p>
     */
    private static final String FROM_VIEW_ID_DEFAULT = "*";


    // -------------------------------------------- Methods from ConfigProcessor


    /**
     * @see ConfigProcessor#process(javax.servlet.ServletContext,com.sun.faces.config.DocumentInfo[])
     */
    public void process(ServletContext sc, DocumentInfo[] documentInfos)
    throws Exception {

        NavigationHandler handler = getApplication().getNavigationHandler();
        for (DocumentInfo documentInfo : documentInfos) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE,
                        MessageFormat.format(
                                "Processing navigation-rule elements for document: ''{0}''",
                                documentInfo.getSourceURI()));
            }
            Document document = documentInfo.getDocument();
            String namespace = document.getDocumentElement()
                    .getNamespaceURI();
            NodeList navigationRules = document.getDocumentElement()
                    .getElementsByTagNameNS(namespace, NAVIGATION_RULE);
            if (navigationRules != null && navigationRules.getLength() > 0) {
                addNavigationRules(navigationRules, handler, sc);
            }

        }
        invokeNext(sc, documentInfos);

    }


    // --------------------------------------------------------- Private Methods


    private void addNavigationRules(NodeList navigationRules,
                                    NavigationHandler navHandler,
                                    ServletContext sc)
    throws XPathExpressionException {

            for (int i = 0, size = navigationRules.getLength(); i < size; i++) {
                Node navigationRule = navigationRules.item(i);
                if ((!("flow-definition".equals(navigationRule.getParentNode().getLocalName()))) &&
                    (navigationRule.getNodeType() == Node.ELEMENT_NODE)) {
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
                                if (!fromViewId.equals(FROM_VIEW_ID_DEFAULT) && fromViewId.charAt(0) != '/') {
                                    if (LOGGER.isLoggable(Level.WARNING)) {
                                        LOGGER.log(Level.WARNING,
                                                "jsf.config.navigation.from_view_id_leading_slash",
                                                new String[] { fromViewId });
                                    }
                                    fromViewId = '/' + fromViewId;
                                }
                            } else if (NAVIGATION_CASE.equals(n.getLocalName())) {
                                if (navigationCases == null) {
                                    navigationCases = new ArrayList<Node>(csize);
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
                            navigationCases,
                            navHandler,
                            sc);
                }
            }
    }


    private void addNavigationCasesForRule(String fromViewId,
                                           List<Node> navigationCases,
                                           NavigationHandler navHandler,
                                           ServletContext sc) {

        if (navigationCases != null && !navigationCases.isEmpty()) {
            ApplicationAssociate associate =
                 ApplicationAssociate.getInstance(sc);

            for (Node navigationCase : navigationCases) {
                if (navigationCase.getNodeType() != Node.ELEMENT_NODE) {
                    continue;
                }
                NodeList children = navigationCase.getChildNodes();
                String outcome = null;
                String action = null;
                String condition = null;
                String toViewId = null;
                String toFlowDocumentId = null;
                Map<String,List<String>> parameters = null;
                boolean redirect = false;
                boolean includeViewParams = false;
                for (int i = 0, size = children.getLength(); i < size; i++) {
                    Node n = children.item(i);
                    if (n.getNodeType() == Node.ELEMENT_NODE) {
                        if (FROM_OUTCOME.equals(n.getLocalName())) {
                            outcome = getNodeText(n);
                        } else if (FROM_ACTION.equals(n.getLocalName())) {
                            action = getNodeText(n);
                        } else if (IF.equals(n.getLocalName())) {
                            String expression = getNodeText(n);
                            if (SharedUtils.isExpression(expression) && !SharedUtils.isMixedExpression(expression)) {
                                condition = expression;
                            }
                            else {
                                if (LOGGER.isLoggable(Level.WARNING)) {
                                    LOGGER.log(Level.WARNING,
                                               "jsf.config.navigation.if_invalid_expression",
                                               new String[] { expression, fromViewId });
                                }
                            }
                        } else if (TO_VIEW_ID.equals(n.getLocalName())) {
                            String toViewIdString = getNodeText(n);
                            if (toViewIdString.charAt(0) != '/' && toViewIdString.charAt(0) != '#') {
                                if (LOGGER.isLoggable(Level.WARNING)) {
                                    LOGGER.log(Level.WARNING,
                                               "jsf.config.navigation.to_view_id_leading_slash",
                                               new String[] { toViewIdString,
                                                              fromViewId });
                                }
                                toViewId = '/' + toViewIdString;
                            } else {
                                toViewId = toViewIdString;
                            }
                        } else if (TO_FLOW_DOCUMENT_ID.equals(n.getLocalName())) {
                            toFlowDocumentId = getNodeText(n);
                        } else if (REDIRECT.equals(n.getLocalName())) {
                            parameters = processParameters(n.getChildNodes());
                            includeViewParams = isIncludeViewParams(n);
                            redirect = true;
                        }
                    }
                }

                NavigationCase cnc =
                     new NavigationCase(fromViewId,
                                        action,
                                        outcome,
                                        condition,
                                        toViewId,
                                        toFlowDocumentId,
                                        parameters,
                                        redirect,
                                        includeViewParams);
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.log(Level.FINE,
                               MessageFormat.format("Adding NavigationCase: {0}",
                                                    cnc.toString()));
                }

                // if the top-level NavigationHandler is an instance of
                // ConfigurableNavigationHandler, add the NavigationCases to
                // that instance as well as adding them to the application associate.
                // We have to add them to the ApplicationAssociate in the case
                // where the top-level NavigationHandler may be custom and delegates
                // to the default NavigationHandler implementation.  In 1.2, they
                // could be guaranteed that the default implementation had all
                // defined navigation mappings.
                if (navHandler instanceof ConfigurableNavigationHandler) {
                    ConfigurableNavigationHandler cnav = (ConfigurableNavigationHandler) navHandler;
                    Set<NavigationCase> cases = cnav.getNavigationCases().get(fromViewId);
                    if (cases == null) {
                        cases = new LinkedHashSet<NavigationCase>();
                        cnav.getNavigationCases().put(fromViewId, cases);
                    }
                    cases.add(cnc);
                }
                associate.addNavigationCase(cnc);
                
            }


        }

    }


    private Map<String,List<String>> processParameters(NodeList children) {

        Map<String,List<String>> parameters = null;

        if (children.getLength() > 0) {
            parameters = new LinkedHashMap<String,List<String>>(4, 1.0f);
            for (int i = 0, size = children.getLength(); i < size; i++) {
                Node n = children.item(i);
                if (n.getNodeType() == Node.ELEMENT_NODE) {
                    if (VIEW_PARAM.equals(n.getLocalName())) {
                        String name = null;
                        String value = null;
                        NodeList params = n.getChildNodes();
                        for (int j = 0, jsize = params.getLength(); j < jsize; j++) {
                            Node pn = params.item(j);
                            if (pn.getNodeType() == Node.ELEMENT_NODE) {
                                if (VIEW_PARAM_NAME.equals(pn.getLocalName())) {
                                    name = getNodeText(pn);
                                }
                                if (VIEW_PARAM_VALUE.equals(pn.getLocalName())) {
                                    value = getNodeText(pn);
                                }
                            }
                        }
                        if (name != null) {
                            List<String> values = parameters.get(name);
                            if (values == null && value != null) {
                                values = new ArrayList<String>(2);
                                parameters.put(name, values);
                            }
                            if (values != null) {
                                values.add(value);
                            }
                        }
                    }
                }
            }
        }
        return parameters;

    }


    private boolean isIncludeViewParams(Node n) {

        return Boolean.valueOf(getNodeText(n.getAttributes().getNamedItem(INCLUDE_VIEW_PARAMS_ATTRIBUTE)));

    }

}

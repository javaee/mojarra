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
import com.sun.faces.mgbean.BeanManager;
import com.sun.faces.mgbean.ManagedBeanInfo;
import com.sun.faces.util.FacesLogger;
import com.sun.faces.util.TypedCollections;
import com.sun.faces.el.ELUtils;
import com.sun.faces.config.DocumentInfo;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Document;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.bean.ManagedBean;
import javax.servlet.ServletContext;

/**
 * <p>
 *  This <code>ConfigProcessor</code> handles all elements defined under
 *  <code>/faces-config/managed-bean</code>.
 * </p>
 */
public class ManagedBeanConfigProcessor extends AbstractConfigProcessor {

    private static final Logger LOGGER = FacesLogger.CONFIG.getLogger();

    /**
     * <p>/faces-config/managed-bean</p>
     */
    private static final String MANAGED_BEAN =
         "managed-bean";

    /**
     * <p>/faces-config/managed-bean/description</p>
     */
    private static final String DESCRIPTION =
         "description";

    /**
     * <p>/faces-config/mananged-bean/managed-bean-name</p>
     */
    private static final String MGBEAN_NAME =
         "managed-bean-name";

    /**
     * <p>/faces-config/managed-bean/mananged-bean-class</p>
     */
    private static final String MGBEAN_CLASS =
         "managed-bean-class";

    /**
     * <p>/faces-config/managed-bean/managed-bean-scope</p>
     */
    private static final String MGBEAN_SCOPE =
         "managed-bean-scope";

    /**
     * <p>/faces-config/managed-bean/managed-property</p>
     */
    private static final String MG_PROPERTY =
         "managed-property";

    /**
     * <p>/faces-config/managed-bean/managed-property/property-name</p>
     */
    private static final String MG_PROPERTY_NAME =
         "property-name";

    /**
     * <p>/faces-config/managed-bean/managed-property/property-class</p>
     */
    private static final String MG_PROPERTY_TYPE =
         "property-class";

    /**
     * <p>Handles:
     * <ul>
     *   <li>/faces-config/managed-bean/map-entries/map-entry/null-value</li>
     *   <li>/faces-config/managed-bean/managed-property/null-value</li>
     *   <li>/faces-config/managed-bean/managed-property/map-entries/map-entry/null-value</li>
     *   <li>/faces-config/managed-bean/list-entries/null-value</li>
     *   <li>/faces-config/managed-bean/managed-property/list-entries/null-value</li>
     * </ul>
     * </p>
     */
    private static final String NULL_VALUE =
         "null-value";

    /**
     * <p>Handles:
     * <ul>
     *   <li>/faces-config/managed-bean/map-entries/map-entry/value</li>
     *   <li>/faces-config/managed-bean/managed-property/value</li>
     *   <li>/faces-config/managed-bean/managed-property/map-entries/map-entry/value</li>
     *   <li>/faces-config/managed-bean/list-entries/value</li>
     *   <li>/faces-config/managed-bean/managed-property/list-entries/value</li>
     * </ul>
     * </p>
     */
    private static final String VALUE =
         "value";


    /**
     * <p>Handles:
     * <ul>
     *   <li>/faces-config/managed-bean/managed-property/map-entries/map-entry/key</li>
     * </ul>
     * </p>
     */
    private static final String KEY =
          "key";

    /**
     * <p>Handles:
     * <ul>
     *   <li>/faces-config/managed-bean/map-entries/key-class</li>
     *   <li>/faces-config/managed-bean/managed-property/map-entries/key-class</li>
     * </ul>
     * </p>
     */
    private static final String MAP_KEY_CLASS =
         "key-class";

    /**
     * <p>Handles:
     * <ul>
     *   <li>/faces-config/managed-bean/map-entries/value-class</li>
     *   <li>/faces-config/managed-bean/managed-property/map-entries/value-class</li>
     *   <li>/faces-config/managed-bean/list-entries/value-class</li>
     *   <li>/faces-config/managed-bean/managed-property/list-entries/value-class</li>
     * </ul>
     * </p>
     */
    private static final String VALUE_CLASS =
         "value-class";

    /**
     * <p>Handles:
     * <ul>
     *   <li>/faces-config/managed-bean/map-entries/map-entry</li>
     *   <li>/faces-config/managed-bean/managed-property/map-entries/map-entry</li>
     * </ul>
     * </p>
     */
    private static final String MAP_ENTRY =
         "map-entry";

    /**
     * <p>Handles:
     * <ul>
     *   <li>/faces-config/managed-bean/map-entries</li>
     *   <li>/faces-config/managed-bean/managed-property/map-entries</li>
     * </ul>
     * </p>
     */
    private static final String MAP_ENTRIES =
         "map-entries";

    /**
     * <p>Handles:
     * <ul>
     *   <li>/faces-config/managed-bean/list-entries</li>
     *   <li>/faces-config/managed-bean/managed-property/list-entries</li>     
     * </ul>
     * </p>
     */
    private static final String LIST_ENTRIES =
         "list-entries";


    /**
     * <p>
     *  <code>eager</code> attribute defined in the managed-bean element.
     * </p>
     */
    private static final String EAGER_ATTRIBUTE = "eager";


    private static final String DEFAULT_SCOPE = "request";


    // -------------------------------------------- Methods from ConfigProcessor


    /**
     * @see ConfigProcessor#process(javax.servlet.ServletContext,com.sun.faces.config.DocumentInfo[])
     */
    public void process(ServletContext sc, DocumentInfo[] documentInfos)
    throws Exception {

        // process annotated managed beans first as managed beans configured
        // via config files take precedence
        processAnnotations(ManagedBean.class);
        
        BeanManager beanManager =
              ApplicationAssociate.getInstance(sc).getBeanManager();
        for (int i = 0; i < documentInfos.length; i++) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE,
                           MessageFormat.format(
                                "Processing managed-bean elements for document: ''{0}''",
                                documentInfos[i].getSourceURI()));
            }
            Document document = documentInfos[i].getDocument();
            String namespace = document.getDocumentElement().getNamespaceURI();
            NodeList managedBeans = document.getDocumentElement()
                 .getElementsByTagNameNS(namespace, MANAGED_BEAN);
            if (managedBeans != null && managedBeans.getLength() > 0) {
                for (int m = 0, size = managedBeans.getLength();
                     m < size;
                     m++) {
                    addManagedBean(beanManager,
                                   managedBeans.item(m));
                }

            }
        }
        beanManager.preProcessesBeans();
        invokeNext(sc, documentInfos);

    }


    // --------------------------------------------------------- Private Methods


    private void addManagedBean(BeanManager beanManager,
                                Node managedBean) {

        NodeList children = managedBean.getChildNodes();
        String beanName = null;
        String beanClass = null;
        String beanScope = null;
        ManagedBeanInfo.ListEntry listEntry = null;
        ManagedBeanInfo.MapEntry mapEntry = null;
        List<Node> managedProperties = null;
        List<Node> descriptions = null;

        for (int i = 0, size = children.getLength(); i < size; i++) {
            Node n = children.item(i);
            if (n.getNodeType() == Node.ELEMENT_NODE) {
                if (MGBEAN_NAME.equals(n.getLocalName())) {
                    beanName = getNodeText(n);
                } else if (MGBEAN_CLASS.equals(n.getLocalName())) {
                    beanClass = getNodeText(n);
                } else if (MGBEAN_SCOPE.equals(n.getLocalName())) {
                    beanScope = getNodeText(n);
                    if (beanScope == null) {
                        beanScope = DEFAULT_SCOPE;
                    }
                } else if (LIST_ENTRIES.equals(n.getLocalName())) {
                    listEntry = buildListEntry(n);
                } else if (MAP_ENTRIES.equals(n.getLocalName())) {
                    mapEntry = buildMapEntry(n);
                } else if (MG_PROPERTY.equals(n.getLocalName())) {
                    if (managedProperties == null) {
                        managedProperties = new ArrayList<Node>(size);
                    }
                    managedProperties.add(n);
                } else if (DESCRIPTION.equals(n.getLocalName())) {
                    if (descriptions == null) {
                        descriptions = new ArrayList<Node>(4);
                    }
                    descriptions.add(n);
                }
            }
        }

        if (LOGGER.isLoggable(Level.FINE)) {
	        LOGGER.log(Level.FINE,
	                   "Begin processing managed bean ''{0}''",
	                   beanName);
        }


        List<ManagedBeanInfo.ManagedProperty> properties = null;
        if (managedProperties != null && !managedProperties.isEmpty()) {
             properties = new ArrayList<ManagedBeanInfo.ManagedProperty>(
                 managedProperties.size());
            for (Node managedProperty : managedProperties) {
                properties.add(buildManagedProperty(managedProperty));
            }
        }

        beanManager.register(new ManagedBeanInfo(beanName,
                                                 beanClass,
                                                 beanScope,
                                                 isEager(managedBean,
                                                         beanName,
                                                         beanScope),
                                                 mapEntry,
                                                 listEntry,
                                                 properties,
                                                 getTextMap(descriptions)));

        if (LOGGER.isLoggable(Level.FINE)) {
	        LOGGER.log(Level.FINE,
	                   "Completed processing bean ''{0}''",
	                   beanName);
        }

    }


    private ManagedBeanInfo.ListEntry buildListEntry(Node listEntry) {

        if (listEntry != null) {
            String valueClass = "java.lang.String";
            List<String> values = null;
            NodeList children = listEntry.getChildNodes();
            for (int i = 0, size = children.getLength(); i < size; i++) {
                Node child = children.item(i);
                if (child.getNodeType() == Node.ELEMENT_NODE) {
                    if (VALUE_CLASS.equals(child.getLocalName())) {
                        valueClass = getNodeText(child);
                    } else if (VALUE.equals(child.getLocalName())) {
                        if (values == null) {
                            values = new ArrayList<String>(size);
                        }
                        values.add(getNodeText(child));
                    } else if (NULL_VALUE.equals(child.getLocalName())) {
                        if (values == null) {
                            values = new ArrayList<String>(size);
                        }
                        values.add(ManagedBeanInfo.NULL_VALUE);
                    }
                }
            }

            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE,
                           MessageFormat.format(
                                "Created ListEntry valueClass={1}, values={3}",
                                valueClass,
                                (values != null && !values.isEmpty())
                                ? values.toString()
                                : "none"));
            }
            return (new ManagedBeanInfo.ListEntry(valueClass,
                                                  (values == null)
                                                  ? TypedCollections.dynamicallyCastList(Collections.emptyList(), String.class)
                                                  : values));
        }

        return null;

    }


    private ManagedBeanInfo.MapEntry buildMapEntry(Node mapEntry) {

        if (mapEntry != null) {
            String valueClass = "java.lang.String";
            String keyClass = "java.lang.String";
            Map<String, String> entries = null;
            NodeList children = mapEntry.getChildNodes();
            for (int i = 0, size = children.getLength(); i < size; i++) {
                Node child = children.item(i);
                if (child.getNodeType() == Node.ELEMENT_NODE) {
                    if (VALUE_CLASS.equals(child.getLocalName())) {
                        valueClass = getNodeText(child);
                    } else if (MAP_KEY_CLASS.equals(child.getLocalName())) {
                        keyClass = getNodeText(child);
                    } else if (MAP_ENTRY.equals(child.getLocalName())) {
                        if (entries == null) {
                            entries = new LinkedHashMap<String,String>(8, 1.0f);
                        }
                        NodeList c = child.getChildNodes();
                        String key = null;
                        String value = null;
                        for (int j = 0, jsize = c.getLength(); j < jsize; j++) {
                            Node node = c.item(j);
                            if (node.getNodeType() == Node.ELEMENT_NODE) {
                                if (KEY.equals(node.getLocalName())) {
                                    key = getNodeText(node);
                                } else if (VALUE.equals(node.getLocalName())) {
                                    value = getNodeText(node);
                                } else
                                if (NULL_VALUE.equals(node.getLocalName())) {
                                    value = ManagedBeanInfo.NULL_VALUE;
                                }
                            }
                        }
                        entries.put(key, value);
                    }
                }
            }
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE,
                           MessageFormat.format(
                                "Created MapEntry keyClass={0}, valueClass={1}, entries={3}",
                                keyClass,
                                valueClass,
                                (entries != null)
                                ? entries.toString()
                                : "none"));
            }
            return (new ManagedBeanInfo.MapEntry(keyClass,
                                                 valueClass,
                                                 entries));

        }

        return null;
    }


    private ManagedBeanInfo.ManagedProperty buildManagedProperty(Node managedProperty) {

        if (managedProperty != null) {
            String propertyName = null;
            String propertyClass = null;
            String value = null;
            ManagedBeanInfo.MapEntry mapEntry = null;
            ManagedBeanInfo.ListEntry listEntry = null;
            NodeList children = managedProperty.getChildNodes();
            for (int i = 0, size = children.getLength(); i < size; i++) {
                Node child = children.item(i);
                if (child.getNodeType() == Node.ELEMENT_NODE) {
                    if (MG_PROPERTY_NAME.equals(child.getLocalName())) {
                        propertyName = getNodeText(child);
                    } else if (MG_PROPERTY_TYPE.equals(child.getLocalName())) {
                        propertyClass = getNodeText(child);
                    } else if (VALUE.equals(child.getLocalName())) {
                        value = getNodeText(child);
                    } else if (NULL_VALUE.equals(child.getLocalName())) {
                        value = ManagedBeanInfo.NULL_VALUE;
                    } else if (LIST_ENTRIES.equals(child.getLocalName())) {
                        listEntry = buildListEntry(child);
                    } else if (MAP_ENTRIES.equals(child.getLocalName())) {
                        mapEntry = buildMapEntry(child);
                    }
                }
            }

            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE,
                           MessageFormat.format(
                                "Adding ManagedProperty propertyName={0}, propertyClass={1}, propertyValue={2}, hasMapEntry={3}, hasListEntry={4}",
                                propertyName,
                                ((propertyClass != null)
                                 ? propertyClass
                                 : "inferred"),
                                value,
                                (mapEntry != null),
                                (listEntry != null)));
            }
            return new ManagedBeanInfo.ManagedProperty(propertyName,
                                                       propertyClass,
                                                       value,
                                                       mapEntry,
                                                       listEntry);
        }

        return null;
    }


    private boolean isEager(Node managedBean, String beanName, String scope) {

        NamedNodeMap attributes = managedBean.getAttributes();
        Node eagerNode = attributes.getNamedItem(EAGER_ATTRIBUTE);
        boolean eager = false;
        if (eagerNode != null) {
            eager = Boolean.valueOf(getNodeText(eagerNode));
            if (eager && (scope == null || !ELUtils.Scope.APPLICATION.toString().equals(scope))) {
                if (LOGGER.isLoggable(Level.WARNING)) {
                    LOGGER.log(Level.WARNING,
                               "jsf.configuration.illegal.eager.bean",
                               new Object[]{beanName, scope});
                }
                eager = false;
            }
        }

        return eager;

    }
   

}

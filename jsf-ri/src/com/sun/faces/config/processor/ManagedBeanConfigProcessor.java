/*
 * $Id: ManagedBeanConfigProcessor.java,v 1.2 2007/04/24 19:04:21 rlubke Exp $
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
import com.sun.faces.mgbean.BeanManager;
import com.sun.faces.mgbean.ManagedBeanInfo;
import com.sun.faces.util.FacesLogger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.text.MessageFormat;

/**
 * <p>
 *  This <code>ConfigProcessor</code> handles all elements defined under
 *  <code>/faces-config/managed-bean</code>.
 * </p>
 */
public class ManagedBeanConfigProcessor extends AbstractConfigProcessor {

    private static final Logger LOGGER =
         Logger.getLogger(FacesLogger.CONFIG.getLoggerName());

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


    private static final String DEFAULT_SCOPE = "request";


    // -------------------------------------------- Methods from ConfigProcessor


    /**
     * @see ConfigProcessor#process(org.w3c.dom.Document[])
     */
    public void process(Document[] documents)
    throws Exception {

        BeanManager beanManager = null;
        for (int i = 0; i < documents.length; i++) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE,
                           MessageFormat.format(
                                "Processing managed-bean elements for document: ''{0}''",
                                documents[i].getDocumentURI()));
            }
            String namespace = documents[i].getDocumentElement()
                 .getNamespaceURI();
            NodeList managedBeans = documents[i].getDocumentElement()
                 .getElementsByTagNameNS(namespace, MANAGED_BEAN);
            if (managedBeans != null && managedBeans.getLength() > 0) {
                beanManager =
                     ApplicationAssociate.getCurrentInstance().getBeanManager();
                for (int m = 0, size = managedBeans.getLength();
                     m < size;
                     m++) {
                    addManagedBean(beanManager,
                                   managedBeans.item(m),
                                   namespace);
                }

            }
        }
        if (beanManager != null) {
            beanManager.preProcessesBeans();
        }
        invokeNext(documents);

    }


    // --------------------------------------------------------- Private Methods


    private void addManagedBean(BeanManager beanManager,
                                Node managedBean,
                                String namespace) {

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

        LOGGER.log(Level.FINE,
                   "Begin processing managed bean ''{0}''",
                   beanName);


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
                                                 mapEntry,
                                                 listEntry,
                                                 properties,
                                                 getTextMap(descriptions)));


        LOGGER.log(Level.FINE,
                   "Completed processing bean ''{0}''",
                   beanName);

    }


    private ManagedBeanInfo.ListEntry buildListEntry(Node listEntry) {

        if (listEntry != null) {
            String valueClass = "java.lang.String";
            List<String> values = null;
            NodeList children = listEntry.getChildNodes();
            for (int i = 0, size = children.getLength(); i < size; i++) {
                Node child = children.item(i);
                if (child.getNodeType() == Node.ELEMENT_NODE) {
                    if ("value-class".equals(child.getLocalName())) {
                        valueClass = getNodeText(child);
                    } else if ("value".equals(child.getLocalName())) {
                        if (values == null) {
                            values = new ArrayList<String>(size);
                        }
                        values.add(getNodeText(child));
                    } else if ("null-value".equals(child.getLocalName())) {
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
                                                  ? Collections.EMPTY_LIST
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
                    if ("value-class".equals(child.getLocalName())) {
                        valueClass = getNodeText(child);
                    } else if ("key-class".equals(child.getLocalName())) {
                        keyClass = getNodeText(child);
                    } else if ("map-entry".equals(child.getLocalName())) {
                        if (entries == null) {
                            entries = new LinkedHashMap(8, 1.0f);
                        }
                        NodeList c = child.getChildNodes();
                        String key = null;
                        String value = null;
                        for (int j = 0, jsize = c.getLength(); j < jsize; j++) {
                            Node node = c.item(j);
                            if (node.getNodeType() == Node.ELEMENT_NODE) {
                                if ("key".equals(node.getLocalName())) {
                                    key = getNodeText(node);
                                } else if ("value".equals(node.getLocalName())) {
                                    value = getNodeText(node);
                                } else
                                if ("null-value".equals(node.getLocalName())) {
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
                    if ("property-name".equals(child.getLocalName())) {
                        propertyName = getNodeText(child);
                    } else if ("property-class".equals(child.getLocalName())) {
                        propertyClass = getNodeText(child);
                    } else if ("value".equals(child.getLocalName())) {
                        value = getNodeText(child);
                    } else if ("null-value".equals(child.getLocalName())) {
                        value = ManagedBeanInfo.NULL_VALUE;
                    } else if ("list-entries".equals(child.getLocalName())) {
                        listEntry = buildListEntry(child);
                    } else if ("map-entries".equals(child.getLocalName())) {
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
   

}
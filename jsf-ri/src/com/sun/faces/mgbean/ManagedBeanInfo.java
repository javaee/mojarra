/*
 * $Id: ManagedBeanInfo.java,v 1.1 2007/04/22 21:41:05 rlubke Exp $
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

package com.sun.faces.mgbean;

import java.util.List;
import java.util.Map;
import java.util.Locale;

/**
 * This class represents the parsed metadata for a <code>managed-bean</code>
 * entry within a faces-config.xml.
 */
public class ManagedBeanInfo {

    public static final String NULL_VALUE = "null_value";
    
    private String name;
    private String className;
    private String beanScope;
    private ManagedBeanInfo.MapEntry mapEntry;
    private ManagedBeanInfo.ListEntry listEntry;
    private List<ManagedBeanInfo.ManagedProperty> managedProperties;
    private Map<String,String> descriptions;

    public ManagedBeanInfo(String name,
                           String className,
                           String beanScope,
                           ManagedBeanInfo.MapEntry mapEntry,
                           ManagedBeanInfo.ListEntry listEntry,
                           List<ManagedBeanInfo.ManagedProperty> managedProperties,
                           Map<String,String> descriptions) {

        this.name = name;
        this.className = className;
        this.beanScope = beanScope;
        this.mapEntry = mapEntry;
        this.listEntry = listEntry;
        this.managedProperties = managedProperties;
        this.descriptions = descriptions;

    }

    public String getName() {
        return name;
    }

    public String getClassName() {
        return className;
    }

    public String getScope() {
        return beanScope;
    }

    public boolean hasMapEntry() {
        return (mapEntry != null);
    }

    public MapEntry getMapEntry() {
        return mapEntry;
    }

    public boolean hasListEntry() {
        return (listEntry != null);
    }

    public ListEntry getListEntry() {
        return listEntry;
    }

    public boolean hasManagedProperties() {
        return (managedProperties != null);
    }

    public List<ManagedBeanInfo.ManagedProperty> getManagedProperties() {
        return managedProperties;    
    }

    public Map<String,String> getDescriptions() {
        return descriptions;
    }

    // ----------------------------------------------------------- Inner Classes


    public static class MapEntry {

        private String keyClass;
        private String valueClass;
        private Map<String, String> entries;

        public MapEntry(String keyClass,
                        String valueClass,
                        Map<String,String> entries) {

            this.keyClass = keyClass;
            this.valueClass = valueClass;
            this.entries = entries;

        }

        public String getKeyClass() {
            return keyClass;
        }

        public String getValueClass() {
            return valueClass;
        }

        public Map<String,String> getEntries() {
            return entries;
        }

    }


    public static class ListEntry {

        private String valueClass;
        private List<String> values;

        public ListEntry(String valueClass,
                         List<String> values) {

            this.valueClass = valueClass;
            this.values = values;

        }

        public String getValueClass() {
            return valueClass;
        }

        public List<String> getValues() {
            return values;
        }

    }


    public static class ManagedProperty {

        private String propertyName;
        private String propertyClass;
        private String propertyValue;
        private ManagedBeanInfo.MapEntry mapEntry;
        private ManagedBeanInfo.ListEntry listEntry;

        public ManagedProperty(String propertyName,
                               String propertyClass,
                               String propertyValue,
                               ManagedBeanInfo.MapEntry mapEntry,
                               ManagedBeanInfo.ListEntry listEntry) {

            this.propertyName = propertyName;
            this.propertyClass = propertyClass;
            this.propertyValue = propertyValue;
            this.mapEntry = mapEntry;
            this.listEntry = listEntry;

        }

        public String getPropertyName() {
            return propertyName;
        }

        public String getPropertyClass() {
            return propertyClass;
        }

        public boolean hasPropertyValue() {
            return (propertyValue != null);
        }

        public String getPropertyValue() {
            return propertyValue;
        }                

        public boolean hasMapEntry() {
            return (mapEntry != null);
        }

        public ManagedBeanInfo.MapEntry getMapEntry() {
            return mapEntry;
        }

        public boolean hasListEntry() {
            return (listEntry != null);
        }

        public ManagedBeanInfo.ListEntry getListEntry() {
            return listEntry;
        }

    }
}


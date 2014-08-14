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

package com.sun.faces.mgbean;

import java.util.List;
import java.util.Map;

import com.sun.faces.el.ELUtils;

/**
 * This class represents the parsed metadata for a <code>managed-bean</code>
 * entry within a faces-config.xml.
 */
public class ManagedBeanInfo {

    public static final String NULL_VALUE = "null_value";
    
    private String name;
    private String className;
    private String beanScope;
    boolean eager;
    private ManagedBeanInfo.MapEntry mapEntry;
    private ManagedBeanInfo.ListEntry listEntry;
    private List<ManagedBeanInfo.ManagedProperty> managedProperties;
    private Map<String,String> descriptions;


    // ------------------------------------------------------------ Constructors


    public ManagedBeanInfo(String name,
                           String className,
                           String beanScope,
                           ManagedBeanInfo.MapEntry mapEntry,
                           ManagedBeanInfo.ListEntry listEntry,
                           List<ManagedBeanInfo.ManagedProperty> managedProperties,
                           Map<String,String> descriptions) {

        this(name,
             className,
             beanScope,
             false,
             mapEntry,
             listEntry,
             managedProperties,
             descriptions);

    }


    public ManagedBeanInfo(String name,
                           String className,
                           String beanScope,
                           boolean eager,
                           ManagedBeanInfo.MapEntry mapEntry,
                           ManagedBeanInfo.ListEntry listEntry,
                           List<ManagedBeanInfo.ManagedProperty> managedProperties,
                           Map<String,String> descriptions) {

        this.name = name;
        this.className = className;
        this.beanScope = beanScope;
        this.eager = eager;
        this.mapEntry = mapEntry;
        this.listEntry = listEntry;
        this.managedProperties = managedProperties;
        this.descriptions = descriptions;

        if (eager && !ELUtils.Scope.APPLICATION.toString().equals(beanScope)) {
            this.eager = false;
        }
        
    }


    // ---------------------------------------------------------- Public Methods


    public String getName() {
        return name;
    }

    public String getClassName() {
        return className;
    }

    public String getScope() {
        return beanScope;
    }

    public boolean isEager() {
        return eager;
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

    public ManagedBeanInfo clone(String name,
                                 String scope,
                                 boolean eager,
                                 ManagedBeanInfo source) {

        return new ManagedBeanInfo(name,
                                   source.className,
                                   scope,
                                   eager,
                                   source.mapEntry,
                                   source.listEntry,
                                   source.managedProperties,
                                   source.descriptions);

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

        private String propertyAlias;
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

        public ManagedProperty(String propertyAlias,
                               String propertyName,
                               String propertyClass,
                               String propertyValue,
                               ManagedBeanInfo.MapEntry mapEntry,
                               ManagedBeanInfo.ListEntry listEntry) {

            this(propertyName, propertyClass, propertyValue, mapEntry, listEntry);
            this.propertyAlias = propertyAlias;

        }

        public String getPropertyAlias() {
            return propertyAlias;
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


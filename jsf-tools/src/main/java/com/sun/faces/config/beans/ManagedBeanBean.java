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

package com.sun.faces.config.beans;


import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;


/**
 * <p>Configuration bean for <code>&lt;managed-bean&gt; element.</p>
 */

public class ManagedBeanBean extends FeatureBean
    implements ListEntriesHolder, MapEntriesHolder {


    // -------------------------------------------------------------- Properties


    private String managedBeanClass;
    public String getManagedBeanClass() { return managedBeanClass; }
    public void setManagedBeanClass(String managedBeanClass)
    { this.managedBeanClass = managedBeanClass; }


    private String managedBeanName;
    public String getManagedBeanName() { return managedBeanName; }
    public void setManagedBeanName(String managedBeanName)
    { this.managedBeanName = managedBeanName; }


    private String managedBeanScope;
    public String getManagedBeanScope() { return managedBeanScope; }
    public void setManagedBeanScope(String managedBeanScope)
    { this.managedBeanScope = managedBeanScope; }


    // -------------------------------------------------------------- Extensions


    // ----------------------------------------------- ListEntriesHolder Methods

    private ListEntriesBean listEntries;
    public ListEntriesBean getListEntries() { return listEntries; }
    public void setListEntries(ListEntriesBean listEntries)
    { this.listEntries = listEntries; }


    // ------------------------------------------- ManagedPropertyHolder Methods


    private List<ManagedPropertyBean> managedProperties = new ArrayList<ManagedPropertyBean>();


    public void addManagedProperty(ManagedPropertyBean descriptor) {
        managedProperties.add(descriptor);
    }


    public ManagedPropertyBean getManagedProperty(String name) {
	Iterator<ManagedPropertyBean> iter = managedProperties.iterator();
	ManagedPropertyBean cur = null;
	String  curName = null;
	while (iter.hasNext()) {
	    cur = iter.next();
	    if (null == cur) {
		continue;
	    }
	    curName = cur.getPropertyName();
	    // if the name is null, and we're looking for null
	    if (null == curName && null == name) {
		return cur;
	    }
	    // not a match
	    if (null == curName || null == name) {
		continue;
	    }
	    // guaranteed that both are non-null
	    if (curName.equals(name)) {
		return cur;
	    }
	}
	    
        return null;
    }


    public ManagedPropertyBean[] getManagedProperties() {
        ManagedPropertyBean results[] =
            new ManagedPropertyBean[managedProperties.size()];
        return (managedProperties.toArray(results));
    }


    public void removeManagedProperty(ManagedPropertyBean descriptor) {
	if (null == descriptor) {
	    return;
	}
	ManagedPropertyBean toRemove = 
	    getManagedProperty(descriptor.getPropertyName());
	if (null != toRemove) {
	    managedProperties.remove(toRemove);
	}
    }

    // ------------------------------------------------ MapEntriesHolder Methods

    private MapEntriesBean mapEntries;
    public MapEntriesBean getMapEntries() { return mapEntries; }
    public void setMapEntries(MapEntriesBean mapEntries)
    { this.mapEntries = mapEntries; }



    // ----------------------------------------------------------------- Methods


}

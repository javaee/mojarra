/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2010 Sun Microsystems, Inc. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 * 
 * Contributor(s):
 * 
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

package com.sun.faces.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;

/**
 * TreeStructure is a class that represents the structure of a UIComponent
 * instance. This class plays a key role in saving and restoring the structure
 * of the component tree.
 */
public class TreeStructure implements java.io.Serializable {

    private static final long serialVersionUID = 8320767450484935667L;

    ArrayList<TreeStructure> children = null;
    HashMap<String,TreeStructure> facets = null;
    String className = null;
    String id = null;


    public TreeStructure() {
    }


    public TreeStructure(UIComponent component) {
        Util.notNull("component", component);
        this.id = component.getId();
        className = component.getClass().getName();
    }


    /**
     * Returns the className of the UIComponent that this TreeStructure
     * represents.
     */
    public String getClazzName() {
        return className;
    }


    /**
     * Returns the iterator over className of the children that are attached to
     * the UIComponent that this TreeStructure represents.
     */
    public Iterator getChildren() {
        if (children != null) {
            return (children.iterator());
        } else {
            return (Collections.EMPTY_LIST.iterator());
        }
    }


    /**
     * Returns the iterator over className of the facets that are attached to
     * the UIComponent that this TreeStructure represents.
     */
    public Iterator getFacetNames() {
        if (facets != null) {
            return (facets.keySet().iterator());
        } else {
            return (Collections.EMPTY_LIST.iterator());
        }
    }


    /**
     * Adds treeStruct as a child of this TreeStructure instance.
     */
    public void addChild(TreeStructure treeStruct) {
        Util.notNull("treeStruct", treeStruct);
        if (children == null) {
            children = new ArrayList<TreeStructure>();
        }
        children.add(treeStruct);
    }


    /**
     * Adds treeStruct as a facet belonging to this TreeStructure instance.
     */
    public void addFacet(String facetName, TreeStructure treeStruct) {
        Util.notNull("facetName", facetName);
        Util.notNull("treeStruct", treeStruct);
        if (facets == null) {
            facets = new HashMap<String, TreeStructure>();
        }
        facets.put(facetName, treeStruct);
    }


    /**
     * Returns a TreeStructure representing a facetName by looking up
     * the facet list
     */
    public TreeStructure getTreeStructureForFacet(String facetName) {
        Util.notNull("facetName", facetName);
        if (facets != null) {
            return ((facets.get(facetName)));
        } else {
            return null;
        }
    }


    /**
     * Creates and returns the UIComponent that this TreeStructure
     * represents using the structure information available.
     */
    public UIComponent createComponent() {
        UIComponent component = null;
        // create the UIComponent based on the className stored.
        try {
            Class clazz = Util.loadClass(className, this);
            component = ((UIComponent) clazz.newInstance());
        } catch (Exception e) {
            Object params[] = {className};
            throw new FacesException(MessageUtils.getExceptionMessageString(
                MessageUtils.MISSING_CLASS_ERROR_MESSAGE_ID,
                params));
        }
        assert (component != null);
        component.setId(id);
        return component;
    }
}

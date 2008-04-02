/*
 * $Id: TreeStructure.java,v 1.6 2004/02/26 20:33:27 eburns Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.util;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

/**
 * TreeStructure is a class that represents the structure of a UIComponent
 * instance. This class plays a key role in saving and restoring the structure
 * of the component tree.
 */
public class TreeStructure implements java.io.Serializable {

    ArrayList children = null;
    HashMap facets = null;
    String className = null;
    String id = null;


    public TreeStructure() {
    }


    public TreeStructure(UIComponent component) {
        Util.parameterNonNull(component);
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
        Util.parameterNonNull(treeStruct);
        if (children == null) {
            children = new ArrayList();
        }
        children.add(treeStruct);
    }


    /**
     * Adds treeStruct as a facet belonging to this TreeStructure instance.
     */
    public void addFacet(String facetName, TreeStructure treeStruct) {
        Util.parameterNonNull(facetName);
        Util.parameterNonNull(treeStruct);
        if (facets == null) {
            facets = new HashMap();
        }
        facets.put(facetName, treeStruct);
    }


    /**
     * Returns a TreeStructure representing a facetName by looking up
     * the facet list
     */
    public TreeStructure getTreeStructureForFacet(String facetName) {
        Util.parameterNonNull(facetName);
        if (facets != null) {
            return ((TreeStructure) (facets.get(facetName)));
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
            throw new FacesException(Util.getExceptionMessage(
                Util.MISSING_CLASS_ERROR_MESSAGE_ID,
                params));
        }
        Util.doAssert(component != null);
        component.setId(id);
        return component;
    }
}

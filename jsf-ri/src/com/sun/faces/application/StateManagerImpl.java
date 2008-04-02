/* 
 * $Id: StateManagerImpl.java,v 1.2 2003/09/15 20:23:08 eburns Exp $ 
 */ 


/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */


// StateManagerImpl.java 

package com.sun.faces.application; 

import com.sun.faces.util.Util;
import com.sun.faces.util.TreeStructure;
import com.sun.faces.RIConstants;

import java.io.IOException; 
import java.io.Reader;
import java.util.Map;
import java.util.HashMap;
import java.util.Locale;
import java.util.Iterator;

import javax.faces.FacesException;
import javax.faces.component.base.UIViewRootBase;
import javax.faces.component.UIViewRoot;
import javax.faces.component.UIComponent;
import javax.faces.application.ViewHandler;
import javax.faces.application.StateManager;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;  
import javax.faces.application.StateManager.SerializedView;
import javax.faces.render.ResponseStateManager;

/** 
 * <B>StateManagerImpl</B> is the default implementation class for
 * StateManager.
 * @version $Id: StateManagerImpl.java,v 1.2 2003/09/15 20:23:08 eburns Exp $ 
 * 
 * @see javax.faces.application.ViewHandler 
 * 
 */ 
public class StateManagerImpl extends StateManager  { 
    
    public SerializedView saveSerializedView(FacesContext context) {
	SerializedView result = null;
        
        // irrespective of method to save the tree, if the root is transient
        // no state information needs to  be persisted.
        UIViewRoot viewRoot = context.getViewRoot();
        if (viewRoot.isTransient()) {
            return result;
        }
        if (!isSavingStateInClient(context)) {
            // honor the transient property and remove children from the tree
            // that are marked transient.
            removeTransientChildrenAndFacets((UIComponent)viewRoot);   
           
            Map sessionMap = Util.getSessionMap(context);
            String localeKey = RIConstants.REQUEST_LOCALE + "." + 
                    context.getViewRoot().getViewId();
            sessionMap.put(localeKey, context.getLocale());
            sessionMap.put(viewRoot.getViewId(), viewRoot); 
        } else {
	    result = new SerializedView(getTreeStructureToSave(context),
				    getComponentStateToSave(context));
	}
        return result;
    }
    
    protected void removeTransientChildrenAndFacets(UIComponent component) {
        UIComponent kid = null;
        // deal with children that are marked transient.
        Iterator kids = component.getChildren().iterator();
        while (kids.hasNext()) {
            kid = (UIComponent) kids.next();
            if (kid.isTransient()) {
                kids.remove();
            } else {
                removeTransientChildrenAndFacets(kid);
            }
        }
        // deal with facets that are marked transient.
        kids = component.getFacets().values().iterator();
        while (kids.hasNext()) {
            kid = (UIComponent) kids.next();
            if (kid.isTransient()) {
                kids.remove();
            } else {
                removeTransientChildrenAndFacets(kid);
            }
            
        }
    }
  
    protected Object getComponentStateToSave(FacesContext context){
        Object state = null;
        UIViewRoot viewRoot =  context.getViewRoot();
	state = viewRoot.processSaveState(context);
        return state;
    }
    
    
    protected Object getTreeStructureToSave(FacesContext context) {
        TreeStructure structRoot = null;
        UIComponent viewRoot = (UIComponent)context.getViewRoot();
        if (!(viewRoot.isTransient())) {
            structRoot = new TreeStructure(viewRoot);
            buildTreeStructureToSave(viewRoot, structRoot, true);
        }
        return structRoot;
    }
    
    
    public UIViewRoot restoreView(FacesContext context, String viewId) {
        UIViewRoot viewRoot = null;
        if (isSavingStateInClient(context)) {
            viewRoot = restoreTreeStructure(context, viewId);
            if (viewRoot != null) {
                 restoreComponentState(context, viewRoot);
            }
        } else {
            // restore tree from session.
            Map sessionMap = Util.getSessionMap(context);
            viewRoot = (UIViewRoot) sessionMap.get(viewId);
            if (viewRoot != null) {
                // restore locale
                String localeKey = RIConstants.REQUEST_LOCALE + "." + viewId;
                Locale locale = (Locale) sessionMap.get(localeKey);
                if (locale != null) {
                    context.setLocale(locale);
                }
            }
        }
        return viewRoot;
    }

    protected void restoreComponentState(FacesContext context, 
            UIViewRoot root) {
        Object state = (Util.getResponseStateManager(context)).
                getComponentStateToRestore(context);
	root.processRestoreState(context, state);
    }
   
    protected UIViewRoot restoreTreeStructure(FacesContext context, 
             String viewId) {
        UIComponent viewRoot = null;
        TreeStructure structRoot = null;
        structRoot =  (TreeStructure)((Util.getResponseStateManager(context)).
                getTreeStructureToRestore(context, viewId));
        if ( structRoot == null) {
            return null;
        }
        viewRoot = structRoot.createComponent();
        restoreComponentTreeStructure(structRoot, viewRoot,  true);
        return ((UIViewRoot) viewRoot);
    }
    
    public void writeState(FacesContext context, SerializedView state) throws IOException {
	Util.getResponseStateManager(context).writeState(context, state);
    }
    
    /**
     * Builds a hierarchy of TreeStrucure objects simulating the component
     * tree hierarchy.
     */
    public void  buildTreeStructureToSave(UIComponent component, 
            TreeStructure treeStructure, boolean isRoot) {
        // traverse the component hierarchy and save the tree structure 
        // information for every component.
        TreeStructure treeStructureChild= null;
        UIComponent kid = null;
        // if a component is marked transient do not persist its state.
        if (component.isTransient()) {
            return;
        }
        // we have already created the tree structure corresponding to root
        // so don't create it again
        if (isRoot) {
            treeStructureChild = treeStructure;
        } else {
            treeStructureChild = new TreeStructure(component);
        }
        // save the structure info of the children of the component 
        // being processed.
        Iterator kids = component.getChildren().iterator();
        while (kids.hasNext()) {
            kid = (UIComponent) kids.next();
            buildTreeStructureToSave(kid, treeStructureChild, false);
        }

        // we should not add the treeStructure object representing the root
        // to itself.
        if (!isRoot) {
            treeStructure.addChild(treeStructureChild);
        }
        
        // save structure info of the facets of the component currenly being 
        // processed.
        Iterator facets = component.getFacets().keySet().iterator();
        while (facets.hasNext()) {
            String facetName = (String) facets.next();
            UIComponent facetComponent = (UIComponent) component.getFacets().
                    get(facetName);
            if (!(facetComponent.isTransient())) {
                TreeStructure treeStructureFacet = 
                        new TreeStructure(facetComponent);
                treeStructureChild.addFacet(facetName, treeStructureFacet);
            }
        }
        
    }
    
    /**
     * Reconstitutes the component tree from TreeStructure hierarchy
     */
    public void restoreComponentTreeStructure(TreeStructure treeStructure, 
            UIComponent component, boolean isRoot) {
        // traverse the tree strucure hierarchy and restore component
        // component structure.
        UIComponent child = null;
        // root of the tree has been created already, so don't create it again.
        if (isRoot) {
            child = component;
        } else {
           child = treeStructure.createComponent(); 
        }
     
        if (!isRoot) {
            component.getChildren().add(child);
        }
        // restore the structure of the children of the component being processed.
        Iterator kids = treeStructure.getChildren();
        while (kids.hasNext()) {
            restoreComponentTreeStructure((TreeStructure) kids.next(), child, 
                    false);
        }
        
        // process facets
        Iterator facets = treeStructure.getFacetNames();
        while (facets.hasNext()) {
            // component that is a facet cannot have children. Hence no need
            // for recursion.
            String facetName = (String) facets.next();
            TreeStructure facetTreeStructure = 
                    treeStructure.getTreeStructureForFacet(facetName);
            UIComponent facetComponent = facetTreeStructure.createComponent();
            child.getFacets().put(facetName, facetComponent);
        }
    }
    
} 

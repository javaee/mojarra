/* 
 * $Id: StateManagerImpl.java,v 1.9 2003/10/21 03:56:28 eburns Exp $ 
 */ 


/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */


// StateManagerImpl.java 

package com.sun.faces.application; 

import com.sun.faces.RIConstants;
import com.sun.faces.util.TreeStructure;
import com.sun.faces.util.Util;

import javax.faces.application.StateManager;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

import com.sun.faces.RIConstants;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/** 
 * <B>StateManagerImpl</B> is the default implementation class for
 * StateManager.
 * @version $Id: StateManagerImpl.java,v 1.9 2003/10/21 03:56:28 eburns Exp $ 
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
            removeTransientChildrenAndFacets(context, viewRoot, new HashSet());
           
            Map sessionMap = Util.getSessionMap(context);
            sessionMap.put(viewRoot.getViewId(), viewRoot); 
        } else {
	    result = new SerializedView(getTreeStructureToSave(context),
				    getComponentStateToSave(context));
	}
        return result;
    }
    
    protected void removeTransientChildrenAndFacets(FacesContext context,
						    UIComponent component,
                                                    Set componentIds) {
        UIComponent kid;
        // deal with children that are marked transient.
        Iterator kids = component.getChildren().iterator();
        String id;
        while (kids.hasNext()) {
            kid = (UIComponent) kids.next();
            
	    if (null != kid.getId()) {
		// check for id uniqueness
		id = kid.getClientId(context);
		if (id != null && !componentIds.add(id)) {
		    throw new IllegalStateException(
		    Util.getExceptionMessage(
					Util.DUPLICATE_COMPONENT_ID_ERROR_ID,
					new Object[]{id})
		    );
		}
	    }
            
            if (kid.isTransient()) {
                kids.remove();
            } else {                
                removeTransientChildrenAndFacets(context, kid, componentIds);
            }
        }
        // deal with facets that are marked transient.
        kids = component.getFacets().values().iterator();
        while (kids.hasNext()) {
            kid = (UIComponent) kids.next();
            
	    if (null != kid.getId()) {
		// check for id uniqueness
		id = kid.getClientId(context);
		if (id != null && !componentIds.add(id)) {
		    throw new IllegalStateException(
                       Util.getExceptionMessage(
				   Util.DUPLICATE_COMPONENT_ID_ERROR_ID,
				   new Object[]{id})
		       );
		}
	    }

            if (kid.isTransient()) {
                kids.remove();
            } else {               
                removeTransientChildrenAndFacets(context, kid, componentIds);
            }
            
        }
    }
  
    protected Object getComponentStateToSave(FacesContext context){        
        UIViewRoot viewRoot =  context.getViewRoot();
	return viewRoot.processSaveState(context);       
    }
    
    
    protected Object getTreeStructureToSave(FacesContext context) {
        TreeStructure structRoot = null;
        UIComponent viewRoot = context.getViewRoot();
        if (!(viewRoot.isTransient())) {
            structRoot = new TreeStructure(viewRoot);
            buildTreeStructureToSave(context, viewRoot, structRoot);
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
        restoreComponentTreeStructure(structRoot, viewRoot);
        return ((UIViewRoot) viewRoot);
    }
    
    public void writeState(FacesContext context, SerializedView state) throws IOException {
	Util.getResponseStateManager(context).writeState(context, state);
    }
    
    /**
     * Builds a hierarchy of TreeStrucure objects simulating the component
     * tree hierarchy.
     */
    public void  buildTreeStructureToSave(FacesContext context,
					  UIComponent component, 
            TreeStructure treeStructure) {
        // traverse the component hierarchy and save the tree structure 
        // information for every component.
        
        // Set for catching duplicate IDs
        Set componentIds = new HashSet();
        
        // save the structure info of the children of the component 
        // being processed.
        Iterator kids = component.getChildren().iterator();
        String id;
        while (kids.hasNext()) {
            UIComponent kid = (UIComponent) kids.next();    
            
	    if (null != kid.getId()) {
		// check for id uniqueness
		id = kid.getClientId(context);
		if (id != null && !componentIds.add(id)) {
		    throw new IllegalStateException(
                    Util.getExceptionMessage(
					  Util.DUPLICATE_COMPONENT_ID_ERROR_ID,
					  new Object[]{id})
		    );
		}
	    }
            
            // if a component is marked transient do not persist its state as
            // well as its children.
            if (!kid.isTransient()) {                                               
                TreeStructure treeStructureChild = new TreeStructure(kid);
                treeStructure.addChild(treeStructureChild);
                buildTreeStructureToSave(context, kid, treeStructureChild);
            }
        }

        // save structure info of the facets of the component currenly being 
        // processed.
        Iterator facets = component.getFacets().keySet().iterator();
        while (facets.hasNext()) {
            String facetName = (String) facets.next();
            UIComponent facetComponent = (UIComponent) component.getFacets().
                    get(facetName);
            
	    if (null != facetComponent.getId()) {
		// check for id uniqueness
		id = facetComponent.getClientId(context);
		if (id != null && !componentIds.add(id)) {
		    throw new IllegalStateException(
                    Util.getExceptionMessage(
				       Util.DUPLICATE_COMPONENT_ID_ERROR_ID,
				       new Object[]{id})
		    );
		}
            }
            
            // if a facet is marked transient do not persist its state as well as
            // its children.
            if (!(facetComponent.isTransient())) {                                                
                TreeStructure treeStructureFacet = 
                        new TreeStructure(facetComponent);
                treeStructure.addFacet(facetName, treeStructureFacet);
                // process children of facet.
                buildTreeStructureToSave(context, 
					 facetComponent, treeStructureFacet);
            }
        }
    }
    
    /**
     * Reconstitutes the component tree from TreeStructure hierarchy
     */
    public void restoreComponentTreeStructure(TreeStructure treeStructure, 
            UIComponent component) {
        // traverse the tree strucure hierarchy and restore component
        // structure.
      
        // restore the structure of the children of the component being processed.
        Iterator kids = treeStructure.getChildren();
        while (kids.hasNext()) {
            TreeStructure kid = (TreeStructure) kids.next();
            UIComponent child = kid.createComponent(); 
            component.getChildren().add(child);
            restoreComponentTreeStructure(kid, child);
        }
        
        // process facets
        Iterator facets = treeStructure.getFacetNames();
        while (facets.hasNext()) {
            String facetName = (String) facets.next();
            TreeStructure facetTreeStructure = 
                    treeStructure.getTreeStructureForFacet(facetName);
            UIComponent facetComponent = facetTreeStructure.createComponent();
            component.getFacets().put(facetName, facetComponent);
            restoreComponentTreeStructure(facetTreeStructure, facetComponent);
        }
    }
    
} 

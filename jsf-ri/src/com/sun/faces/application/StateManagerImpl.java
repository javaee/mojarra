/* 
 * $Id: StateManagerImpl.java,v 1.25 2005/03/11 18:14:04 edburns Exp $ 
 */ 


/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */


// StateManagerImpl.java 

package com.sun.faces.application;

import com.sun.faces.RIConstants;
import com.sun.faces.util.TreeStructure;
import com.sun.faces.util.Util;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.collections.LRUMap;

import javax.faces.application.StateManager;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * <B>StateManagerImpl</B> is the default implementation class for
 * StateManager.
 *
 * @version $Id: StateManagerImpl.java,v 1.25 2005/03/11 18:14:04 edburns Exp $
 * @see javax.faces.application.ViewHandler
 */
public class StateManagerImpl extends StateManager {

    private static final Log log = LogFactory.getLog(StateManagerImpl.class);
    private static final String NUMBER_OF_VIEWS_IN_SESSION =
        RIConstants.FACES_PREFIX + "NUMBER_OF_VIEWS_IN_SESSION";
    private static final int DEFAULT_NUMBER_OF_VIEWS_IN_SESSION = 15;

    private static final String FACES_VIEW_LIST =
        RIConstants.FACES_PREFIX + "VIEW_LIST";
    
    private static final String ENABLE_HA_PARAM = "enableHighAvailability";
    
    private static final String JSF_ENABLE_HA_PARAM = 
        RIConstants.FACES_PREFIX + ENABLE_HA_PARAM;
    
    private static final String APPSERVER_ENABLE_HA_PARAM = 
        "com.sun.appserver." + ENABLE_HA_PARAM;
    

    /**
     * Number of views to be saved in session.
     */
    int noOfViews = 0;
    
    /**
     * value of <code>com.sun.faces.enableHighAvailability</code>
     * <code>com.sun.appserver.enableHighAvailability</code>
     * parameter
     */
    private Boolean haStateSavingSet = null;

    public SerializedView saveSerializedView(FacesContext context) 
        throws IllegalStateException{
        SerializedView result = null;
	Object treeStructure = null;
	Object componentState = null;
        
        // irrespective of method to save the tree, if the root is transient
        // no state information needs to  be persisted.
        UIViewRoot viewRoot = context.getViewRoot();
        if (viewRoot.isTransient()) {
            return result;
        }

        // honor the transient property and remove children from the tree
        // that are marked transient.
        removeTransientChildrenAndFacets(context, viewRoot, new HashSet());


	if (log.isDebugEnabled()) {
	    log.debug("Begin creating serialized view for " +
		      viewRoot.getViewId());
	}
	result = new SerializedView(treeStructure = 
				    getTreeStructureToSave(context),
				    componentState =
				    getComponentStateToSave(context));
	if (log.isDebugEnabled()) {
	    log.debug("End creating serialized view " +
		      viewRoot.getViewId());
	}
	if (!isSavingStateInClient(context)) {
	    String id = null;

	    synchronized (this) {
		id = createUniqueRequestId();
		LRUMap lruMap = null;
		Map sessionMap = Util.getSessionMap(context);
		Object stateArray[] = { treeStructure, componentState };
		
		if (null == (lruMap = (LRUMap) 
			     sessionMap.get(RIConstants.STATE_MAP))) {
		    lruMap = new LRUMap(15); // PENDING(edburns):
					  // configurable
		    
		    sessionMap.put(RIConstants.STATE_MAP, lruMap);
		}
		result = new SerializedView(id, null);
		lruMap.put(id, stateArray);
	    }
	}

        return result;
    }

    char requestIdSerial = 0;

    private String createUniqueRequestId() {
	if (requestIdSerial++ == Character.MAX_VALUE) {
	    requestIdSerial = 0;
	}
	
	return UIViewRoot.UNIQUE_ID_PREFIX + ((int) requestIdSerial);
    }


    protected void removeTransientChildrenAndFacets(FacesContext context,
        UIComponent component, Set componentIds) throws IllegalStateException{
        UIComponent kid;
        // deal with children that are marked transient.
        Iterator kids = component.getChildren().iterator();
        String id;
        while (kids.hasNext()) {
            kid = (UIComponent) kids.next();
	    // check for id uniqueness
	    id = kid.getClientId(context);
	    if (id != null && !componentIds.add(id)) {
		throw new IllegalStateException(Util.getExceptionMessageString(
                        Util.DUPLICATE_COMPONENT_ID_ERROR_ID,
                        new Object[]{id}));
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
	    // check for id uniqueness
	    id = kid.getClientId(context);
	    if (id != null && !componentIds.add(id)) {
		throw new IllegalStateException(Util.getExceptionMessageString(
                        Util.DUPLICATE_COMPONENT_ID_ERROR_ID,
                        new Object[]{id}));
	    }

            if (kid.isTransient()) {
                kids.remove();
            } else {
                removeTransientChildrenAndFacets(context, kid, componentIds);
            }

        }
    }


    protected Object getComponentStateToSave(FacesContext context) {
        UIViewRoot viewRoot = context.getViewRoot();
        return viewRoot.processSaveState(context);
    }


    protected Object getTreeStructureToSave(FacesContext context) {
        TreeStructure structRoot = null;
        UIComponent viewRoot = context.getViewRoot();
        if (!(viewRoot.isTransient())) {
            structRoot = new TreeStructure(viewRoot);
            buildTreeStructureToSave(context, viewRoot, structRoot, null);
        }
        return structRoot;
    }


    public UIViewRoot restoreView(FacesContext context, String viewId,
                                  String renderKitId) {
        if (null == renderKitId) {
            String message = Util.getExceptionMessageString
                (Util.NULL_PARAMETERS_ERROR_MESSAGE_ID);
            message = message + " renderKitId " + renderKitId;
            throw new IllegalArgumentException(message);
        }

        UIViewRoot viewRoot = null;
        if (isSavingStateInClient(context)) {
            // restore view from response.
            if (log.isDebugEnabled()) {
                log.debug("Begin restoring view from response " + viewId);
            }
            viewRoot = restoreTreeStructure(context, viewId, renderKitId);
            if (viewRoot != null) {
                restoreComponentState(context, viewRoot, renderKitId);
            } else {
                if (log.isDebugEnabled()) {
                    log.debug("Possibly a new request. Tree structure could not "
                        + " be restored for " + viewId);
                }
            }
            if (log.isDebugEnabled()) {
                log.debug("End restoring view from response " + viewId);
            }
        } else {
            // restore tree from session.
            // if high available state saving option is chosen, restore
            // the SerializedView from session instead of UIViewRoot.
	    Object id = ((Util.getResponseStateManager(context, renderKitId)).
			 getTreeStructureToRestore(context, viewId));

	    if (null != id) {
	    
		if (log.isDebugEnabled()) {
		    log.debug("Begin restoring view in session for viewId " + viewId);
		}
		
		Map lruMap, sessionMap = Util.getSessionMap(context);
		TreeStructure structRoot = null;
		Object [] stateArray = null;
		synchronized (this) {
		    lruMap = (Map) sessionMap.get(RIConstants.STATE_MAP);
		    stateArray = (Object []) lruMap.get(id);
		}
		structRoot = (TreeStructure)stateArray[0];
		viewRoot = (UIViewRoot) structRoot.createComponent();
		restoreComponentTreeStructure(structRoot, viewRoot);
		
		viewRoot.processRestoreState(context, stateArray[1]);
		
		if (log.isDebugEnabled()) {
		    log.debug("End restoring view in session for viewId " + viewId);
		}
	    }
        }
        return viewRoot;
    }


    protected void restoreComponentState(FacesContext context,
                                         UIViewRoot root, String renderKitId) {
        if (null == renderKitId) {
            String message = Util.getExceptionMessageString
                (Util.NULL_PARAMETERS_ERROR_MESSAGE_ID);
            message = message + " renderKitId " + renderKitId;
            throw new IllegalArgumentException(message);
        }
        Object state = (Util.getResponseStateManager(context, renderKitId)).
            getComponentStateToRestore(context);
        root.processRestoreState(context, state);
    }


    protected UIViewRoot restoreTreeStructure(FacesContext context,
                                              String viewId, String renderKitId) {
        if (null == renderKitId) {
            String message = Util.getExceptionMessageString
                (Util.NULL_PARAMETERS_ERROR_MESSAGE_ID);
            message = message + " renderKitId " + renderKitId;
            throw new IllegalArgumentException(message);
        }
        UIComponent viewRoot = null;
        TreeStructure structRoot = null;
        structRoot = (TreeStructure) ((Util.getResponseStateManager(context,
                                                                    renderKitId)).
            getTreeStructureToRestore(context, viewId));
        if (structRoot == null) {
            return null;
        }
        viewRoot = structRoot.createComponent();
        restoreComponentTreeStructure(structRoot, viewRoot);
        return ((UIViewRoot) viewRoot);
    }


    public void writeState(FacesContext context, SerializedView state)
        throws IOException {
	Util.getResponseStateManager(context,
            context.getViewRoot().getRenderKitId()).writeState(context, state);
    }


    /**
     * Builds a hierarchy of TreeStrucure objects simulating the component
     * tree hierarchy.
     */
    public void buildTreeStructureToSave(FacesContext context,
                                         UIComponent component,
                                         TreeStructure treeStructure,
                                         Set componentIds) {
        // traverse the component hierarchy and save the tree structure 
        // information for every component.
        
        // Set for catching duplicate IDs
        if (null == componentIds) {
            componentIds = new HashSet();
        }
        
        // save the structure info of the children of the component 
        // being processed.
        Iterator kids = component.getChildren().iterator();
        String id;
        while (kids.hasNext()) {
            UIComponent kid = (UIComponent) kids.next();

	    // check for id uniqueness
	    id = kid.getClientId(context);
	    if (id != null && !componentIds.add(id)) {
		throw new IllegalStateException(Util.getExceptionMessageString(
                        Util.DUPLICATE_COMPONENT_ID_ERROR_ID,
                        new Object[]{id}));
	    }
            
            // if a component is marked transient do not persist its state as
            // well as its children.
            if (!kid.isTransient()) {
                TreeStructure treeStructureChild = new TreeStructure(kid);
                treeStructure.addChild(treeStructureChild);
                buildTreeStructureToSave(context, kid, treeStructureChild,
                                         componentIds);
            }
        }

        // save structure info of the facets of the component currenly being 
        // processed.
        Iterator facets = component.getFacets().keySet().iterator();
        while (facets.hasNext()) {
            String facetName = (String) facets.next();
            UIComponent facetComponent = (UIComponent) component.getFacets().
                get(facetName);

	    // check for id uniqueness
	    id = facetComponent.getClientId(context);
	    if (id != null && !componentIds.add(id)) {
		throw new IllegalStateException(Util.getExceptionMessageString(
                        Util.DUPLICATE_COMPONENT_ID_ERROR_ID,
                        new Object[]{id}));
	    }
            
            // if a facet is marked transient do not persist its state as well as
            // its children.
            if (!(facetComponent.isTransient())) {
                TreeStructure treeStructureFacet =
                    new TreeStructure(facetComponent);
                treeStructure.addFacet(facetName, treeStructureFacet);
                // process children of facet.
                buildTreeStructureToSave(context,
                                         facetComponent, treeStructureFacet,
                                         componentIds);
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
    
    /**
     * Returns true one of <code>com.sun.faces.enableHighAvailability</code>
     * or <code>com.sun.appserver.enableHighAvailability</code>
     * servlet context parameter is set.
     */
    protected boolean isHAStateSavingSet(FacesContext context) {
	if (null != haStateSavingSet) {
	    return haStateSavingSet.booleanValue();
	}
	haStateSavingSet = Boolean.FALSE;

        String haStateSavingParam = context.getExternalContext().
            getInitParameter(JSF_ENABLE_HA_PARAM);
        if (haStateSavingParam != null){
	    haStateSavingSet = Boolean.valueOf(haStateSavingParam);
        } else {
            haStateSavingParam = context.getExternalContext().
            getInitParameter(APPSERVER_ENABLE_HA_PARAM);   
            if (haStateSavingParam != null){
	        haStateSavingSet = Boolean.valueOf(haStateSavingParam);
            }
        }
        return haStateSavingSet.booleanValue();
    }
    
    /**
     * Returns the <code> UIViewRoot</code> corresponding the 
     * <code> viewId </code> by restoring the view structure and state.
     */
    protected UIViewRoot restoreSerializedView(FacesContext context, 
        SerializedView sv, String viewId) {
        if ( sv == null) {
            if (log.isDebugEnabled()) {
                log.debug("Possibly a new request. Tree structure could not "
                        + " be restored for " + viewId);
            }
            return null;
        }
        TreeStructure structRoot = (TreeStructure) sv.getStructure();
        if (structRoot == null) {
            return null;
        }
        UIComponent viewRoot = structRoot.createComponent();
        if (viewRoot != null) {
             restoreComponentTreeStructure(structRoot, viewRoot);
             Object state = sv.getState();
             viewRoot.processRestoreState(context, state);
        }
        return ((UIViewRoot)viewRoot);
    }
}

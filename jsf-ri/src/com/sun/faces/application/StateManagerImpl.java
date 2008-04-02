/* 
 * $Id: StateManagerImpl.java,v 1.24 2004/08/05 20:02:19 jayashri Exp $ 
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
 * @version $Id: StateManagerImpl.java,v 1.24 2004/08/05 20:02:19 jayashri Exp $
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
        
        // irrespective of method to save the tree, if the root is transient
        // no state information needs to  be persisted.
        UIViewRoot viewRoot = context.getViewRoot();
        if (viewRoot.isTransient()) {
            return result;
        }

        // honor the transient property and remove children from the tree
        // that are marked transient.
        removeTransientChildrenAndFacets(context, viewRoot, new HashSet());

        if (!isSavingStateInClient(context)) {
            // save state in server
            if (log.isDebugEnabled()) {
                log.debug("Begin saving view in session for viewId " +
                          viewRoot.getViewId());
            }
            Map sessionMap = Util.getSessionMap(context);
            synchronized (this) {
                // viewList maintains a list of viewIds corresponding to 
                // all the views stored in session.
                ArrayList viewList = (ArrayList) sessionMap.get(
                    FACES_VIEW_LIST);
                if (viewList == null) {
                    viewList = new ArrayList();
                }
                // save the viewId in the viewList, so that we can keep
                // track how many views are stored in session. If the
                // number exceeds the limit, restoreView will remove the
                // oldest view upon postback.
		
		// only save unique view ids
		boolean foundMatch = false;
		for (int i = 0, size = viewList.size(); i < size; i++) {
		    if (viewList.get(i).equals(viewRoot.getViewId())) {
			foundMatch = true;
			break;
		    }
		}
		if (!foundMatch) {
		    viewList.add(viewRoot.getViewId());
		}
                sessionMap.put(FACES_VIEW_LIST, viewList);
            
                // if highly available state saving option is chosen, save
                // the SerializedView in session instead of UIViewRoot since
                // the UIComponent instances are not serializable.
                if (isHAStateSavingSet(context)) {
                    if (log.isDebugEnabled()) {
                        log.debug("Highly Available state saving option enabled ");
                        log.debug("Begin creating serialized view for " +
                          viewRoot.getViewId());
                    }
                    result = new SerializedView(getTreeStructureToSave(context),
                                        getComponentStateToSave(context));
                    if (log.isDebugEnabled()) {
                        log.debug("End creating serialized view " +
                          viewRoot.getViewId());
                    }
                    sessionMap.put(viewRoot.getViewId(), result);

                } else {
                    sessionMap.put(viewRoot.getViewId(), viewRoot);
                }
            }
            if (log.isDebugEnabled()) {
                log.debug("End saving view in session for viewId " +
                      viewRoot.getViewId());
            }
        } else {
            // save state in client
            if (log.isDebugEnabled()) {
                log.debug("Begin creating serialized view for " +
                          viewRoot.getViewId());
            }
            result = new SerializedView(getTreeStructureToSave(context),
                                        getComponentStateToSave(context));
            if (log.isDebugEnabled()) {
                log.debug("End creating serialized view " +
                          viewRoot.getViewId());
            }
        }
        return result;
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
            if (log.isDebugEnabled()) {
                log.debug("Begin restoring view in session for viewId " + viewId);
            }
            Map sessionMap = Util.getSessionMap(context);
            synchronized (this) {
                if (isHAStateSavingSet(context)) {
                    if (log.isDebugEnabled()) {
                        log.debug("High available state saving option enabled");
                        log.debug("Begin restoring serialized view for "+viewId);
                    }
                    SerializedView serializedView = (SerializedView) 
                        sessionMap.get(viewId);
                    viewRoot = restoreSerializedView(context, serializedView, 
                        viewId);
                    if (log.isDebugEnabled()) {
                        log.debug("End restoring serialized view " + viewId);
                    }

                } else {
                    viewRoot = (UIViewRoot) sessionMap.get(viewId);
                    if (log.isDebugEnabled()) {
                        log.debug(
                            "Restoring view from session for viewId " + viewId);
                    }
                }
                removeViewFromSession(context, viewRoot);
            }
            if (log.isDebugEnabled()) {
                log.debug("End restoring view in session for viewId " + viewId);
            }
        }
        return viewRoot;
    }


    /**
     * Ensures that number of views stored in session does not exceed the
     * specified number. If it exceeds, removes the oldest view from session
     * and updates the viewList that maintains a list of active viewIds.
     * PRECONDITION:  Number of views in session need to be checked only if
     * viewList exists in session.
     * POSTCONDITION: Number of views saved in session is always less than the
     * specified number or the default.
     */

    private void removeViewFromSession(FacesContext context, UIViewRoot viewRoot) {
        Map sessionMap = Util.getSessionMap(context);
        // viewList maintains a list of viewIds corresponding to all the views
        // stored in session.
        ArrayList viewList = (ArrayList) sessionMap.get(FACES_VIEW_LIST);
        if (viewList == null) {
            return;
        }
        // If the parameter NUMBER_OF_VIEWS_IN_SESSION is specified as a servlet
        // init parameter, use it otherwise use the default. Parse the
        // servletInitParameter only once, since its not going to change for the
        // lifetime of the servlet.
        if (noOfViews == 0) {
            noOfViews = DEFAULT_NUMBER_OF_VIEWS_IN_SESSION;
            String noViews = context.getExternalContext().
                getInitParameter(NUMBER_OF_VIEWS_IN_SESSION);
            if (noViews != null) {
                try {
                    noOfViews = Integer.valueOf(noViews).intValue();
                } catch (NumberFormatException nfe) {
                    noOfViews = DEFAULT_NUMBER_OF_VIEWS_IN_SESSION;
                    if (log.isDebugEnabled()) {
                        log.debug("Error parsing the servetInitParameter " +
                                  NUMBER_OF_VIEWS_IN_SESSION + ". Using the default " +
                                  noOfViews);
                    }
                }
            }
        }
        
        // if number of views in session exceeds the specified number
        // delete the oldest view in the list.
        if (viewList.size() > noOfViews) {
            String viewToRemove = (String) viewList.remove(0);
            sessionMap.remove(viewToRemove);
            if (log.isDebugEnabled()) {
                log.debug("Number of views in session exceeded specified number " +
                          noOfViews + ".Removing view " + viewToRemove);
            }
        }
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
        // only call thru on client case.
        if (isSavingStateInClient(context)) {
            Util.getResponseStateManager(context,
            context.getViewRoot().getRenderKitId()).writeState(context, state);
        }
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

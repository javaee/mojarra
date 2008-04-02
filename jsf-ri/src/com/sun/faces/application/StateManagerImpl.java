/* 
 * $Id: StateManagerImpl.java,v 1.40 2006/01/11 15:28:03 rlubke Exp $ 
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
 * Copyright 2005 Sun Microsystems Inc. All Rights Reserved
 */


// StateManagerImpl.java 

package com.sun.faces.application;

import com.sun.faces.RIConstants;
import com.sun.faces.renderkit.RenderKitUtils;
import com.sun.faces.util.TreeStructure;
import com.sun.faces.util.Util;
import com.sun.faces.util.LRUMap;
import com.sun.faces.util.MessageUtils;

import javax.faces.application.StateManager;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.render.ResponseStateManager;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import java.util.logging.Level;
import javax.faces.component.NamingContainer;

/**
 * <B>StateManagerImpl</B> is the default implementation class for
 * StateManager.
 *
 * @version $Id: StateManagerImpl.java,v 1.40 2006/01/11 15:28:03 rlubke Exp $
 * @see javax.faces.application.ViewHandler
 */
public class StateManagerImpl extends StateManager {

    // Log instance for this class
    private static Logger logger = Util.getLogger(Util.FACES_LOGGER 
            + Util.APPLICATION_LOGGER);
    
    private static final String NUMBER_OF_VIEWS_IN_SESSION =
        RIConstants.FACES_PREFIX + "NUMBER_OF_VIEWS_IN_SESSION";
    private static final int DEFAULT_NUMBER_OF_VIEWS_IN_SESSION = 15;

    private static final String NUMBER_OF_VIEWS_IN_LOGICAL_VIEW_IN_SESSION =
        RIConstants.FACES_PREFIX + "NUMBER_OF_VIEWS_IN_LOGICAL_VIEW_IN_SESSION";
    private static final int DEFAULT_NUMBER_OF_VIEWS_IN_LOGICAL_VIEW_IN_SESSION = 15;
    
    /**
     * Number of views in logical view to be saved in session.
     */
    int noOfViews = 0;
    int noOfViewsInLogicalView = 0;
    
    /**
     * Keyed by renderKitId, this Map contains a boolean that
     * indicates if the ResponseStateManager for the renderKitId
     * has non-deprecated methods.  True indicates the presence
     * of non-deprecated methods.
     */
    private HashMap<String,Boolean> responseStateManagerInfo = null;
    
    public Object saveView(FacesContext context) {
        return ( super.saveView(context) );
    }
    
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
	
	// honor the requirement to check for id uniqueness
	checkIdUniqueness(context, viewRoot, new HashSet<String>());

	
 	if (logger.isLoggable(Level.FINE)) {
            logger.fine("Begin creating serialized view for " 
                    + viewRoot.getViewId());
 	}
	result = new SerializedView(treeStructure = 
				    getTreeStructureToSave(context),
				    componentState =
				    getComponentStateToSave(context));
 	if (logger.isLoggable(Level.FINE)) {
            logger.fine("End creating serialized view " + viewRoot.getViewId());
 	}
        if (!isSavingStateInClient(context)) {
            //
            // Server Side state saving is handled stored in two nested LRU maps
            // in the session.
            //
            // The first map is called the LOGICAL_VIEW_MAP.  A logical view
            // is a top level view that may have one or more actual views inside
            // of it.  This will be the case when you have a frameset, or an
            // application that has multiple windows operating at the same time.
            // The LOGICAL_VIEW_MAP map contains 
            // an entry for each logical view, up to the limit specified by the
            // numberOfViewsParameter.  Each entry in the LOGICAL_VIEW_MAP
            // is an LRU Map, configured with the numberOfViewsInLogicalView
            // parameter.  
            //
            // The motivation for this is to allow better memory tuning for 
            // apps that need this multi-window behavior.
            
            String id = null,
                   idInActualMap = null,
                   idInLogicalMap = (String)
                context.getExternalContext().getRequestMap().get(RIConstants.LOGICAL_VIEW_MAP);
            LRUMap logicalMap = null, actualMap = null;
            int 
                logicalMapSize = getNumberOfViewsParameter(context),
                actualMapSize = getNumberOfViewsInLogicalViewParameter(context);
            
            Object stateArray[] = { treeStructure, componentState };
            Map<String,Object> sessionMap = Util.getSessionMap(context);
            
 	    synchronized (this) {
                if (null == (logicalMap = (LRUMap) sessionMap.get(RIConstants.LOGICAL_VIEW_MAP))) {
                    logicalMap = new LRUMap(logicalMapSize);
 		    sessionMap.put(RIConstants.LOGICAL_VIEW_MAP, logicalMap);
                }
                assert(null != logicalMap); 

                if (null == idInLogicalMap) {
                    idInLogicalMap = createUniqueRequestId();
                }
                assert(null != idInLogicalMap);
 
                idInActualMap = createUniqueRequestId();
 		if (null == (actualMap = (LRUMap) 
                        logicalMap.get(idInLogicalMap))) {
		    actualMap = new LRUMap(actualMapSize);
                    logicalMap.put(idInLogicalMap, actualMap);
 		}
                id = idInLogicalMap + NamingContainer.SEPARATOR_CHAR + 
                        idInActualMap;
		result = new SerializedView(id, null);
                actualMap.put(idInActualMap, stateArray);
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


    protected void checkIdUniqueness(FacesContext context,
        UIComponent component, Set<String> componentIds) throws IllegalStateException{
        UIComponent kid;
        // deal with children that are marked transient.
        Iterator<UIComponent> kids = component.getChildren().iterator();
        String id;
        while (kids.hasNext()) {
            kid = kids.next();
	    // check for id uniqueness
	    id = kid.getClientId(context);
	    if (id != null && !componentIds.add(id)) {
                if (logger.isLoggable(Level.SEVERE)) {
                    logger.log(Level.SEVERE,"jsf.duplicate_component_id_error",id);
                }
		throw new IllegalStateException(MessageUtils.getExceptionMessageString(
                        MessageUtils.DUPLICATE_COMPONENT_ID_ERROR_ID,
                        new Object[]{id}));
	    }

	    checkIdUniqueness(context, kid, componentIds);
        }
        // deal with facets that are marked transient.
        kids = component.getFacets().values().iterator();
        while (kids.hasNext()) {
            kid = kids.next();
	    // check for id uniqueness
	    id = kid.getClientId(context);
	    if (id != null && !componentIds.add(id)) {
                if (logger.isLoggable(Level.SEVERE)) {
                    logger.log(Level.SEVERE,"jsf.duplicate_component_id_error",id);
                }
		throw new IllegalStateException(MessageUtils.getExceptionMessageString(
                        MessageUtils.DUPLICATE_COMPONENT_ID_ERROR_ID,
                        new Object[]{id}));
	    }

	    checkIdUniqueness(context, kid, componentIds);

        }
    }


    protected Object getComponentStateToSave(FacesContext context) {
	return context.getViewRoot().processSaveState(context);
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
            String message = MessageUtils.getExceptionMessageString
                (MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID);
            message = message + " renderKitId " + renderKitId;
            throw new IllegalArgumentException(message);
        }

        UIViewRoot viewRoot = null;
        if (isSavingStateInClient(context)) {
            // restore view from response.
           if (logger.isLoggable(Level.FINE)) {
                logger.log(Level.FINE, "Begin restoring view from response " 
                        + viewId);
            }
            viewRoot = restoreTreeStructure(context, viewId, renderKitId);
            if (viewRoot != null) {
                restoreComponentState(context, viewRoot, renderKitId);
            } else {
                if (logger.isLoggable(Level.FINE)) {
                    logger.fine("Possibly a new request. Tree structure could not "
                            + " be restored for " + viewId);
                }
            }
            if (logger.isLoggable(Level.FINE)) {
                logger.fine("End restoring view from response " + viewId);
            }
        } else {
            // restore tree from session.
            // The ResponseStateManager implementation may be using the new methods or
            // deprecated methods.  We need to know which one to call.
            Object id = null;
            ResponseStateManager rsm = RenderKitUtils.getResponseStateManager(context, renderKitId);
            if (hasDeclaredMethod(responseStateManagerInfo, renderKitId, rsm, "getState")) {
                Object[] stateArray = (Object[])rsm.getState(context, viewId);
                id = stateArray[0];
            } else {
	        id = rsm.getTreeStructureToRestore(context, viewId);
            }

	    if (null != id) {
	        if (logger.isLoggable(Level.FINE)) {
                    logger.fine( "Begin restoring view in session for viewId " 
                            + viewId);
		}
                String idString = (String) id,
                       idInLogicalMap = null,
                       idInActualMap = null;
                
                int sep = idString.indexOf(NamingContainer.SEPARATOR_CHAR);
                assert(-1 != sep);
                assert(sep < idString.length());
                
                idInLogicalMap = idString.substring(0, sep);
                idInActualMap = idString.substring(sep + 1);
                		
                ExternalContext externalCtx = context.getExternalContext();
                Object sessionObj = externalCtx.getSession(false);
                
                // stop evaluating if the session is not available
                if (sessionObj == null) {
                    if (logger.isLoggable(Level.FINE)) {
                        logger.fine( "Can't Restore Server View State, session expired for viewId: "
                                + viewId);
                    }
                    return null;
                }
                
                Map logicalMap = null,
                actualMap = null,
                sessionMap = externalCtx.getSessionMap();
                
		TreeStructure structRoot = null;
		Object [] stateArray = null;
		synchronized (sessionObj) {
		    logicalMap = (Map) sessionMap.get(RIConstants.LOGICAL_VIEW_MAP);
                    if (logicalMap != null) {
                        actualMap = (Map) logicalMap.get(idInLogicalMap);
                        if (actualMap != null) {
                            context.getExternalContext().getRequestMap().put(RIConstants.LOGICAL_VIEW_MAP, 
                                idInLogicalMap);
                            stateArray = (Object []) actualMap.get(idInActualMap);
                        }
                    }
		}
                if (stateArray == null) {
                    if (logger.isLoggable(Level.FINE)) {
                        logger.fine( "Session Available, but View State does not exist for viewId: "
                            + viewId);
                    }
                    return null;
                }
		structRoot = (TreeStructure)stateArray[0];
		viewRoot = (UIViewRoot) structRoot.createComponent();
		restoreComponentTreeStructure(structRoot, viewRoot);
		
		viewRoot.processRestoreState(context, stateArray[1]);
		
		if (logger.isLoggable(Level.FINE)) {
                    logger.fine("End restoring view in session for viewId " 
                            + viewId);
		}
	    }
        }
        return viewRoot;
    }


    protected void restoreComponentState(FacesContext context,
                                         UIViewRoot root, String renderKitId) {
        if (null == renderKitId) {
            String message = MessageUtils.getExceptionMessageString
                (MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID);
            message = message + " renderKitId " + renderKitId;
            throw new IllegalArgumentException(message);
        }
        Object state = null;
        ResponseStateManager rsm = RenderKitUtils.getResponseStateManager(context, renderKitId);
        if (hasDeclaredMethod(responseStateManagerInfo, renderKitId, rsm, "getState")) {
            Object[] stateArray = (Object[])rsm.getState(context, root.getViewId());
            state = stateArray[1];
        } else {
	    state = rsm.getComponentStateToRestore(context);
        }
        root.processRestoreState(context, state);
    }


    protected UIViewRoot restoreTreeStructure(FacesContext context,
                                              String viewId, String renderKitId) {
        if (null == renderKitId) {
            String message = MessageUtils.getExceptionMessageString
                (MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID);
            message = message + " renderKitId " + renderKitId;
            throw new IllegalArgumentException(message);
        }
        UIComponent viewRoot = null;
        TreeStructure structRoot = null;
        ResponseStateManager rsm = RenderKitUtils.getResponseStateManager(context, renderKitId);
        if (hasDeclaredMethod(responseStateManagerInfo, renderKitId, rsm, "getState")) {
            Object[] stateArray = (Object[])rsm.getState(context, viewId);
            structRoot = (TreeStructure)stateArray[0];
        } else {
            structRoot = (TreeStructure)rsm.getTreeStructureToRestore(context, viewId);
        }
        if (structRoot == null) {
            return null;
        }
        viewRoot = structRoot.createComponent();
        restoreComponentTreeStructure(structRoot, viewRoot);
        return ((UIViewRoot) viewRoot);
    }

    public void writeState(FacesContext context, Object state)
        throws IOException {
        super.writeState(context, state);
    }
    
    public void writeState(FacesContext context, SerializedView state)
        throws IOException {
        String renderKitId = context.getViewRoot().getRenderKitId();
        ResponseStateManager rsm = RenderKitUtils.getResponseStateManager(context, renderKitId);
        if (hasDeclaredMethod(responseStateManagerInfo, renderKitId, rsm, "getState")) {
            Object[] stateArray = new Object[2];
            stateArray[0] = state.getStructure();
            stateArray[1] = state.getState();
            rsm.writeState(context, stateArray);
        } else {
            rsm.writeState(context, state);
        }
    }

    /**
     * Builds a hierarchy of TreeStrucure objects simulating the component
     * tree hierarchy.
     */
    public void buildTreeStructureToSave(FacesContext context,
                                         UIComponent component,
                                         TreeStructure treeStructure,
                                         Set<String> componentIds) {
        // traverse the component hierarchy and save the tree structure 
        // information for every component.
        
        // Set for catching duplicate IDs
        if (null == componentIds) {
            componentIds = new HashSet<String>();
        }
        
        // save the structure info of the children of the component 
        // being processed.
        Iterator<UIComponent> kids = component.getChildren().iterator();
        String id;
        while (kids.hasNext()) {
            UIComponent kid = kids.next();

	    // check for id uniqueness
	    id = kid.getClientId(context);
	    if (id != null && !componentIds.add(id)) {
                if (logger.isLoggable(Level.SEVERE)) {
                    logger.log(Level.SEVERE,"jsf.duplicate_component_id_error",id);
                }
		throw new IllegalStateException(MessageUtils.getExceptionMessageString(
                        MessageUtils.DUPLICATE_COMPONENT_ID_ERROR_ID,
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
        Iterator<String> facets = component.getFacets().keySet().iterator();
        while (facets.hasNext()) {
            String facetName = facets.next();
            UIComponent facetComponent = component.getFacets().
                get(facetName);

	    // check for id uniqueness
	    id = facetComponent.getClientId(context);
	    if (id != null && !componentIds.add(id)) {
                if (logger.isLoggable(Level.SEVERE)) {
                    logger.log(Level.SEVERE,"jsf.duplicate_component_id_error",
                            id);
                }
		throw new IllegalStateException(MessageUtils.getExceptionMessageString(
                        MessageUtils.DUPLICATE_COMPONENT_ID_ERROR_ID,
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
     * Returns the <code> UIViewRoot</code> corresponding the 
     * <code> viewId </code> by restoring the view structure and state.
     */
    protected UIViewRoot restoreSerializedView(FacesContext context, 
        SerializedView sv, String viewId) {
        if ( sv == null) {
            if (logger.isLoggable(Level.FINE)) {
                logger.fine( "Possibly a new request. Tree structure could not "
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
    
    /**
     * Returns the value of ServletContextInitParameter that specifies the
     * maximum number of logical views to be saved in session. If none is specified
     * returns <code>DEFAULT_NUMBER_OF_VIEWS_IN_SESSION</code>.
     */
    protected int getNumberOfViewsParameter(FacesContext context) {
        if (noOfViews != 0) { 
            return noOfViews;
        }
        noOfViews = DEFAULT_NUMBER_OF_VIEWS_IN_SESSION;
        String noOfViewsStr = context.getExternalContext().
                getInitParameter(NUMBER_OF_VIEWS_IN_SESSION);
        if (noOfViewsStr != null) {
            try {
                noOfViews = Integer.valueOf(noOfViewsStr).intValue();
            } catch (NumberFormatException nfe) {
                if (logger.isLoggable(Level.FINE)) {
                    logger.fine("Error parsing the servetInitParameter " +
                            NUMBER_OF_VIEWS_IN_SESSION + ". Using default " + 
                            noOfViews);
                }
            }
        } 
        return noOfViews;
    }

    /**
     * Returns the value of ServletContextInitParameter that specifies the
     * maximum number of views to be saved in this logical view. If none is specified
     * returns <code>DEFAULT_NUMBER_OF_VIEWS_IN_LOGICAL_VIEW_IN_SESSION</code>.
     */
    protected int getNumberOfViewsInLogicalViewParameter(FacesContext context) {
        if (noOfViewsInLogicalView != 0) { 
            return noOfViewsInLogicalView;
        }
        noOfViewsInLogicalView = DEFAULT_NUMBER_OF_VIEWS_IN_LOGICAL_VIEW_IN_SESSION;
        String noOfViewsStr = context.getExternalContext().
                getInitParameter(NUMBER_OF_VIEWS_IN_LOGICAL_VIEW_IN_SESSION);
        if (noOfViewsStr != null) {
            try {
                noOfViewsInLogicalView = Integer.valueOf(noOfViewsStr).intValue();
            } catch (NumberFormatException nfe) {
                if (logger.isLoggable(Level.FINE)) {
                    logger.fine("Error parsing the servetInitParameter " +
                            NUMBER_OF_VIEWS_IN_LOGICAL_VIEW_IN_SESSION + ". Using default " + 
                            noOfViewsInLogicalView);
                }
            }
        } 
        return noOfViewsInLogicalView;
    }

    /**
     * Looks for the presence of a declared method (by name) in the specified class and returns
     * a <code>boolean</code> outcome (true, if the method exists).
     *
     * @param resultMap A <code>Map</code> possibly containing the specified "key" argument, and the
     *                   corresponding <code>Boolean</code> value indicating the outcome of
     *                   the search.
     * @param key The object that will be used for the lookup.  This key will also be stored in the 
     *             <code>Map</code> with a corresponding <code>Boolean</code> value indicating
     *             the result of the search.
     * @param instance The instance of the class that will be used as the search domain.
     * @param methodName The name of the method we are looking for.
     */
    private boolean hasDeclaredMethod(Map<String,Boolean> resultMap, String key, ResponseStateManager instance, String methodName) {
        boolean result = false;
        if (resultMap == null) {
            resultMap = new HashMap<String, Boolean>();
        }
        Boolean value = resultMap.get(key);
        if (value != null) {
            return value;
        }
        result = Util.hasDeclaredMethod(instance, methodName);
        resultMap.put(key, result);
        return result;
    }
}

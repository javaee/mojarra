/* 
 * $Id: StateManagerImpl.java,v 1.47 2006/05/17 17:31:28 rlubke Exp $ 
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

import javax.faces.application.StateManager;
import javax.faces.component.NamingContainer;
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
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sun.faces.RIConstants;
import com.sun.faces.config.WebConfiguration;
import com.sun.faces.config.WebConfiguration.WebContextInitParameter;
import com.sun.faces.renderkit.RenderKitUtils;
import com.sun.faces.util.LRUMap;
import com.sun.faces.util.MessageUtils;
import com.sun.faces.util.TreeStructure;
import com.sun.faces.util.Util;

/**
 * <B>StateManagerImpl</B> is the default implementation class for
 * StateManager.
 *
 * @version $Id: StateManagerImpl.java,v 1.47 2006/05/17 17:31:28 rlubke Exp $
 * @see javax.faces.application.ViewHandler
 */
public class StateManagerImpl extends StateManager {


    // Log instance for this class
    private static final Logger LOGGER = 
          Util.getLogger(Util.FACES_LOGGER + Util.APPLICATION_LOGGER);   

    /**
     * Keyed by renderKitId, this Map contains a boolean that
     * indicates if the ResponseStateManager for the renderKitId
     * has non-deprecated methods.  True indicates the presence
     * of non-deprecated methods.
     */
    private HashMap<String, Boolean> responseStateManagerInfo = null;

    private char requestIdSerial = 0;

    /** Number of views in logical view to be saved in session. */
    private int noOfViews = 0;
    private int noOfViewsInLogicalView = 0;


    // ---------------------------------------------------------- Public Methods


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
                if (LOGGER.isLoggable(Level.SEVERE)) {
                    LOGGER.log(Level.SEVERE,
                               "jsf.duplicate_component_id_error",
                               id);
                }
                throw new IllegalStateException(
                      MessageUtils.getExceptionMessageString(
                        MessageUtils.DUPLICATE_COMPONENT_ID_ERROR_ID, id));
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
        for (String facetName : component.getFacets().keySet()) {
            UIComponent facetComponent = component.getFacets().
                  get(facetName);

            // check for id uniqueness
            id = facetComponent.getClientId(context);
            if (id != null && !componentIds.add(id)) {
                if (LOGGER.isLoggable(Level.SEVERE)) {
                    LOGGER.log(Level.SEVERE, "jsf.duplicate_component_id_error",
                               id);
                }
                throw new IllegalStateException(
                      MessageUtils.getExceptionMessageString(
                        MessageUtils.DUPLICATE_COMPONENT_ID_ERROR_ID, id));
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


    /** Reconstitutes the component tree from TreeStructure hierarchy */
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


    @SuppressWarnings("Deprecation")
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
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE, "Begin restoring view from response "
                                       + viewId);
            }
            viewRoot = restoreTreeStructure(context, viewId, renderKitId);
            if (viewRoot != null) {
                restoreComponentState(context, viewRoot, renderKitId);
            } else {
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.fine(
                          "Possibly a new request. Tree structure could not "
                          + " be restored for " + viewId);
                }
            }
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine("End restoring view from response " + viewId);
            }
        } else {
            // restore tree from session.
            // The ResponseStateManager implementation may be using the new 
            // methods or deprecated methods.  We need to know which one 
            // to call.
            Object id = null;
            ResponseStateManager rsm =
                  RenderKitUtils.getResponseStateManager(context, renderKitId);
            if (hasDeclaredMethod(responseStateManagerInfo,
                                  renderKitId,
                                  rsm,
                                  "getState")) {
                Object[] stateArray = (Object[]) rsm.getState(context, viewId);
                id = stateArray[0];
            } else {
                id = rsm.getTreeStructureToRestore(context, viewId);
            }

            if (null != id) {
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.fine("Begin restoring view in session for viewId "
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
                    if (LOGGER.isLoggable(Level.FINE)) {
                        LOGGER.fine(
                              "Can't Restore Server View State, session expired for viewId: "
                              + viewId);
                    }
                    return null;
                }


                TreeStructure structRoot = null;
                Object [] stateArray = null;
                synchronized (sessionObj) {
                    Map logicalMap = (Map) externalCtx.getSessionMap()
                          .get(RIConstants.LOGICAL_VIEW_MAP);
                    if (logicalMap != null) {
                        Map actualMap = (Map) logicalMap.get(idInLogicalMap);
                        if (actualMap != null) {
                            context.getExternalContext().getRequestMap()
                                  .put(RIConstants.LOGICAL_VIEW_MAP,
                                       idInLogicalMap);
                            stateArray =
                                  (Object []) actualMap.get(idInActualMap);
                        }
                    }
                }
                if (stateArray == null) {
                    if (LOGGER.isLoggable(Level.FINE)) {
                        LOGGER.fine(
                              "Session Available, but View State does not exist for viewId: "
                              + viewId);
                    }
                    return null;
                }
                structRoot = (TreeStructure) stateArray[0];
                viewRoot = (UIViewRoot) structRoot.createComponent();
                restoreComponentTreeStructure(structRoot, viewRoot);

                viewRoot.processRestoreState(context, stateArray[1]);

                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.fine("End restoring view in session for viewId "
                                + viewId);
                }
            }
        }
        return viewRoot;

    }


    @SuppressWarnings("Deprecation")
    public SerializedView saveSerializedView(FacesContext context)
          throws IllegalStateException {

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


        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Begin creating serialized view for "
                        + viewRoot.getViewId());
        }
        result = new SerializedView(treeStructure =
              getTreeStructureToSave(context),
                                    componentState =
                                          getComponentStateToSave(context));
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("End creating serialized view " + viewRoot.getViewId());
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
            int logicalMapSize = getNumberOfViewsParameter(context);
            int actualMapSize = getNumberOfViewsInLogicalViewParameter(context);

            Object stateArray[] = {treeStructure, componentState};
            ExternalContext externalContext = context.getExternalContext();
            Object sessionObj = externalContext.getSession(true);
            Map<String, Object> sessionMap = Util.getSessionMap(context);


            synchronized (sessionObj) {
                LRUMap<String, LRUMap<String, Object[]>> logicalMap;
                LRUMap<String, Object[]> actualMap;
                if (null == (logicalMap =
                      (LRUMap<String, LRUMap<String, Object[]>>) sessionMap
                            .get(RIConstants.LOGICAL_VIEW_MAP))) {
                    logicalMap =
                          new LRUMap<String, LRUMap<String, Object[]>>(
                                logicalMapSize);
                    sessionMap.put(RIConstants.LOGICAL_VIEW_MAP, logicalMap);
                }

                String idInLogicalMap = (String)
                      externalContext.getRequestMap()
                            .get(RIConstants.LOGICAL_VIEW_MAP);
                if (null == idInLogicalMap) {
                    idInLogicalMap = createUniqueRequestId();
                }
                assert(null != idInLogicalMap);

                String idInActualMap = createUniqueRequestId();
                if (null == (actualMap = logicalMap.get(idInLogicalMap))) {
                    actualMap = new LRUMap<String, Object[]>(actualMapSize);
                    logicalMap.put(idInLogicalMap, actualMap);
                }
                String id = idInLogicalMap + NamingContainer.SEPARATOR_CHAR +
                            idInActualMap;
                result = new SerializedView(id, null);
                actualMap.put(idInActualMap, stateArray);
            }
        }

        return result;

    }


    public Object saveView(FacesContext context) {

        return (super.saveView(context));

    }


    @SuppressWarnings("Deprecation")
    public void writeState(FacesContext context, SerializedView state)
          throws IOException {

        String renderKitId = context.getViewRoot().getRenderKitId();
        ResponseStateManager rsm =
              RenderKitUtils.getResponseStateManager(context, renderKitId);
        if (hasDeclaredMethod(responseStateManagerInfo,
                              renderKitId,
                              rsm,
                              "getState")) {
            Object[] stateArray = new Object[2];
            stateArray[0] = state.getStructure();
            stateArray[1] = state.getState();
            rsm.writeState(context, stateArray);
        } else {
            rsm.writeState(context, state);
        }

    }


    public void writeState(FacesContext context, Object state)
          throws IOException {

        super.writeState(context, state);

    }


    // ------------------------------------------------------- Protected Methods


    protected void checkIdUniqueness(FacesContext context,
                                     UIComponent component,
                                     Set<String> componentIds)
          throws IllegalStateException {

        // deal with children/facets that are marked transient.        
        for (Iterator<UIComponent> kids = component.getFacetsAndChildren();
             kids.hasNext();) {

            UIComponent kid = kids.next();
            // check for id uniqueness
            String id = kid.getClientId(context);
            if (componentIds.add(id)) {
                checkIdUniqueness(context, kid, componentIds);
            } else {
                if (LOGGER.isLoggable(Level.SEVERE)) {
                    LOGGER.log(Level.SEVERE,
                               "jsf.duplicate_component_id_error",
                               id);
                }
                throw new IllegalStateException(
                      MessageUtils.getExceptionMessageString(
                            MessageUtils.DUPLICATE_COMPONENT_ID_ERROR_ID, id));
            }
        }

    }


    @SuppressWarnings("Deprecation")
    protected Object getComponentStateToSave(FacesContext context) {

        return context.getViewRoot().processSaveState(context);

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
        WebConfiguration webConfig = 
              WebConfiguration.getInstance(context.getExternalContext());
        String noOfViewsStr = webConfig
              .getContextInitParameter(WebContextInitParameter.NumberOfLogicalViews);
        String defaultValue =
              WebContextInitParameter.NumberOfLogicalViews.getDefaultValue();
        try {
            noOfViewsInLogicalView = Integer.valueOf(noOfViewsStr);
        } catch (NumberFormatException nfe) {
            if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.fine("Error parsing the servetInitParameter "
                                +
                                WebContextInitParameter.NumberOfLogicalViews.getQualifiedName() 
                                + ". Using default "
                                +
                                noOfViewsInLogicalView);
            }
            try {
                noOfViewsInLogicalView = Integer.valueOf(defaultValue);
            } catch (NumberFormatException ne) {
                // won't occur
            }
        }        
       
        return noOfViewsInLogicalView;

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
        WebConfiguration webConfig = 
              WebConfiguration.getInstance(context.getExternalContext());
        String noOfViewsStr = webConfig
              .getContextInitParameter(WebContextInitParameter.NumberOfViews);
        String defaultValue =
              WebContextInitParameter.NumberOfViews.getDefaultValue();
        try {
            noOfViews = Integer.valueOf(noOfViewsStr);
        } catch (NumberFormatException nfe) {
            if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.fine("Error parsing the servetInitParameter "
                                +
                                WebContextInitParameter.NumberOfViews.getQualifiedName() 
                                + ". Using default "
                                +
                                noOfViews);
            }
            try {
                noOfViews = Integer.valueOf(defaultValue);
            } catch (NumberFormatException ne) {
                // won't occur
            }
        }        
       
        return noOfViews;        

    }


    @SuppressWarnings("Deprecation")
    protected Object getTreeStructureToSave(FacesContext context) {

        TreeStructure structRoot = null;
        UIComponent viewRoot = context.getViewRoot();
        if (!(viewRoot.isTransient())) {
            structRoot = new TreeStructure(viewRoot);
            buildTreeStructureToSave(context, viewRoot, structRoot, null);
        }
        return structRoot;

    }


    @SuppressWarnings("Deprecation")
    protected void restoreComponentState(FacesContext context,
                                         UIViewRoot root, String renderKitId) {

        if (null == renderKitId) {
            String message = MessageUtils.getExceptionMessageString
                  (MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID);
            message = message + " renderKitId " + renderKitId;
            throw new IllegalArgumentException(message);
        }
        Object state = null;
        ResponseStateManager rsm =
              RenderKitUtils.getResponseStateManager(context, renderKitId);
        if (hasDeclaredMethod(responseStateManagerInfo,
                              renderKitId,
                              rsm,
                              "getState")) {
            Object[] stateArray =
                  (Object[]) rsm.getState(context, root.getViewId());
            state = stateArray[1];
        } else {
            state = rsm.getComponentStateToRestore(context);
        }
        root.processRestoreState(context, state);

    }


    /**
     * Returns the <code> UIViewRoot</code> corresponding the
     * <code> viewId </code> by restoring the view structure and state.
     */
    @SuppressWarnings("Deprecation")
    protected UIViewRoot restoreSerializedView(FacesContext context,
                                               SerializedView sv,
                                               String viewId) {

        if (sv == null) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine("Possibly a new request. Tree structure could not "
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
        return ((UIViewRoot) viewRoot);

    }


    @SuppressWarnings("Deprecation")
    protected UIViewRoot restoreTreeStructure(FacesContext context,
                                              String viewId,
                                              String renderKitId) {

        if (null == renderKitId) {
            String message = MessageUtils.getExceptionMessageString
                  (MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID);
            message = message + " renderKitId " + renderKitId;
            throw new IllegalArgumentException(message);
        }
        UIComponent viewRoot = null;
        TreeStructure structRoot = null;
        ResponseStateManager rsm =
              RenderKitUtils.getResponseStateManager(context, renderKitId);
        if (hasDeclaredMethod(responseStateManagerInfo,
                              renderKitId,
                              rsm,
                              "getState")) {
            Object[] stateArray = (Object[]) rsm.getState(context, viewId);
            structRoot = (TreeStructure) stateArray[0];
        } else {
            structRoot = (TreeStructure) rsm
                  .getTreeStructureToRestore(context, viewId);
        }
        if (structRoot == null) {
            return null;
        }
        viewRoot = structRoot.createComponent();
        restoreComponentTreeStructure(structRoot, viewRoot);
        return ((UIViewRoot) viewRoot);

    }


    // --------------------------------------------------------- Private Methods


    private String createUniqueRequestId() {

        if (requestIdSerial++ == Character.MAX_VALUE) {
            requestIdSerial = 0;
        }
        return UIViewRoot.UNIQUE_ID_PREFIX + ((int) requestIdSerial);

    }


    /**
     * Looks for the presence of a declared method (by name) in the specified 
     * class and returns a <code>boolean</code> outcome (true, if the method 
     * exists).
     *
     * @param resultMap  A <code>Map</code> possibly containing the specified 
     *  "key" argument, and the corresponding <code>boolean</code> value 
     *  indicating the outcome of the search.
     * @param key  The object that will be used for the lookup.  This key 
     *  will also be stored in the <code>Map</code> with a corresponding 
     *  <code>Boolean</code> value indicating the result of the search.
     * @param instance The instance of the class that will be used as 
     *  the search domain.
     * @param methodName The name of the method we are looking for.
     */
    private boolean hasDeclaredMethod(Map<String, Boolean> resultMap,
                                      String key, ResponseStateManager instance,
                                      String methodName) {

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

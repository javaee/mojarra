/* 
 * $Id: StateManagerImpl.java,v 1.24.12.8 2007/04/27 21:27:38 ofung Exp $ 
 */

/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
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
import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sun.faces.RIConstants;
import com.sun.faces.util.LRUMap;
import com.sun.faces.util.TreeStructure;
import com.sun.faces.util.Util;

/**
 * <B>StateManagerImpl</B> is the default implementation class for
 * StateManager.
 *
 * @version $Id: StateManagerImpl.java,v 1.24.12.8 2007/04/27 21:27:38 ofung Exp $
 * @see javax.faces.application.ViewHandler
 */
public class StateManagerImpl extends StateManager {


    private static final Log LOG = LogFactory.getLog(StateManagerImpl.class);

    private static final String NUMBER_OF_VIEWS_IN_LOGICAL_VIEW_IN_SESSION =
          RIConstants
                .FACES_PREFIX
          + "NUMBER_OF_VIEWS_IN_LOGICAL_VIEW_IN_SESSION";
    private static final String NUMBER_OF_VIEWS_IN_SESSION =
          RIConstants.FACES_PREFIX + "NUMBER_OF_VIEWS_IN_SESSION";
    private static final int DEFAULT_NUMBER_OF_VIEWS_IN_LOGICAL_VIEW_IN_SESSION =
          15;
    private static final int DEFAULT_NUMBER_OF_VIEWS_IN_SESSION = 15;


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
                                         Set componentIds) {

        // traverse the component hierarchy and save the tree structure 
        // information for every component.

        // Set for catching duplicate IDs
        if (null == componentIds) {
            componentIds = new HashSet();
        }

        // save the structure info of the children of the component 
        // being processed.
        if (component.getChildCount() > 0) {
            for (Iterator kids = component.getChildren().iterator();
                  kids.hasNext(); ) {           
                UIComponent kid = (UIComponent) kids.next();

                // check for id uniqueness
                String id = kid.getClientId(context);
                if (id != null && !componentIds.add(id)) {
                    String messageString = Util.getExceptionMessageString(
                          Util.DUPLICATE_COMPONENT_ID_ERROR_ID,
                          new Object[]{id});
                    if (LOG.isErrorEnabled()) {
                        LOG.error(messageString);
                    }
                    throw new IllegalStateException(messageString);
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
        }

        // save structure info of the facets of the component currenly being 
        // processed.
        if (component.getFacets().size() > 0) {
            for (Iterator facets = component.getFacets().entrySet().iterator();
                  facets.hasNext(); ) {
                Map.Entry entry = (Map.Entry) facets.next();
                String facetName = (String) entry.getKey();
                UIComponent facetComponent = (UIComponent) entry.getValue();

                // check for id uniqueness
                String id = facetComponent.getClientId(context);
                if (id != null && !componentIds.add(id)) {
                    String messageString = Util.getExceptionMessageString(
                          Util.DUPLICATE_COMPONENT_ID_ERROR_ID,
                          new Object[]{id});
                    if (LOG.isErrorEnabled()) {
                        LOG.error(messageString);
                    }
                    throw new IllegalStateException(messageString);
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

    }


    /** Reconstitutes the component tree from TreeStructure hierarchy */
    public void restoreComponentTreeStructure(TreeStructure treeStructure,
                                              UIComponent component) {

        // traverse the tree strucure hierarchy and restore component
        // structure.

        // restore the structure of the children of the component being processed.
        for (Iterator i = treeStructure.getChildren(); i.hasNext(); ) {                
            TreeStructure kid = (TreeStructure) i.next();
            UIComponent child = kid.createComponent();
            component.getChildren().add(child);
            restoreComponentTreeStructure(kid, child);
        }

        // process facets
        for (Iterator i = treeStructure.getFacetNames(); i.hasNext(); ) {
            String facetName = (String) i.next();
            TreeStructure facetTreeStructure =
                  treeStructure.getTreeStructureForFacet(facetName);
            UIComponent facetComponent = facetTreeStructure.createComponent();
            component.getFacets().put(facetName, facetComponent);
            restoreComponentTreeStructure(facetTreeStructure, facetComponent);
        }

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
            if (LOG.isDebugEnabled()) {
                LOG.debug("Begin restoring view from response "
                          + viewId);
            }
            viewRoot = restoreTreeStructure(context, viewId, renderKitId);
            if (viewRoot != null) {
                restoreComponentState(context, viewRoot, renderKitId);
            } else {
                if (LOG.isDebugEnabled()) {
                    LOG.debug(
                          "Possibly a new request. Tree structure could not "
                          + " be restored for " + viewId);
                }
            }
            if (LOG.isDebugEnabled()) {
                LOG.debug("End restoring view from response " + viewId);
            }
        } else {
            // restore tree from session.                
            ResponseStateManager rsm =
                  Util.getResponseStateManager(context, renderKitId);
            Object id = rsm.getTreeStructureToRestore(context, viewId);

            if (null != id) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Begin restoring view in session for viewId "
                              + viewId);
                }
                String idString = (String) id;                     
                int sep = idString.indexOf(NamingContainer.SEPARATOR_CHAR);
                Util.doAssert(-1 != sep);
                Util.doAssert(sep < idString.length());

                String idInLogicalMap = idString.substring(0, sep);
                String idInActualMap = idString.substring(sep + 1);

                ExternalContext externalCtx = context.getExternalContext();
                Object sessionObj = externalCtx.getSession(false);

                // stop evaluating if the session is not available
                if (sessionObj == null) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug(
                              "Can't Restore Server View State, session expired for viewId: "
                              + viewId);
                    }
                    return null;
                }
                              
                Map sessionMap = externalCtx.getSessionMap();
               
                Object [] stateArray = null;
                synchronized (sessionObj) {
                    Map logicalMap =
                          (Map) sessionMap.get(RIConstants.LOGICAL_VIEW_MAP);
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
                    if (LOG.isDebugEnabled()) {
                        LOG.debug(
                              "Session Available, but View State does not exist for viewId: "
                              + viewId);
                    }
                    return null;
                }
                TreeStructure structRoot = (TreeStructure) stateArray[0];
                viewRoot = (UIViewRoot) structRoot.createComponent();
                restoreComponentTreeStructure(structRoot, viewRoot);

                viewRoot.processRestoreState(context, stateArray[1]);

                if (LOG.isDebugEnabled()) {
                    LOG.debug("End restoring view in session for viewId "
                              + viewId);
                }
            }
        }
        return viewRoot;

    }


    public SerializedView saveSerializedView(FacesContext context)
          throws IllegalStateException {

        // irrespective of method to save the tree, if the root is transient
        // no state information needs to  be persisted.
        UIViewRoot viewRoot = context.getViewRoot();
        if (viewRoot.isTransient()) {
            return null;
        }

        // honor the requirement to check for id uniqueness
        checkIdUniqueness(context, viewRoot, new HashSet());


        if (LOG.isDebugEnabled()) {
            LOG.debug("Begin creating serialized view for "
                      + viewRoot.getViewId());
        }

        Object treeStructure = getTreeStructureToSave(context);
        Object componentState = getComponentStateToSave(context);
        Util.doAssert(treeStructure instanceof Serializable);
        Util.doAssert(componentState instanceof Serializable);

        if (LOG.isDebugEnabled()) {
            LOG.debug("End creating serialized view " + viewRoot.getViewId());
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
            ExternalContext extContext = context.getExternalContext();
            String idInLogicalMap = (String) extContext.getRequestMap()
                  .get(RIConstants.LOGICAL_VIEW_MAP);
            LRUMap logicalMap;
            LRUMap actualMap;
            int logicalMapSize = getNumberOfViewsParameter(context);
            int actualMapSize = getNumberOfViewsInLogicalViewParameter(context);

            Object stateArray[] = {treeStructure, componentState};
            Object sessionObj = extContext.getSession(true);
            Map sessionMap = Util.getSessionMap(context);

            synchronized (sessionObj) {
                if (null == (logicalMap =
                      (LRUMap) sessionMap.get(RIConstants.LOGICAL_VIEW_MAP))) {
                    logicalMap = new LRUMap(logicalMapSize);
                    sessionMap.put(RIConstants.LOGICAL_VIEW_MAP, logicalMap);
                }

                if (null == idInLogicalMap) {
                    idInLogicalMap = createUniqueRequestId();
                }
                Util.doAssert(null != idInLogicalMap);

                String idInActualMap = createUniqueRequestId();
                if (null == (actualMap = (LRUMap)
                      logicalMap.get(idInLogicalMap))) {
                    actualMap = new LRUMap(actualMapSize);
                    logicalMap.put(idInLogicalMap, actualMap);
                }
                String id = idInLogicalMap + NamingContainer.SEPARATOR_CHAR +
                            idInActualMap;
                actualMap.put(idInActualMap, stateArray);
                return new SerializedView(id, null);
            }
        }

        return new SerializedView(treeStructure, componentState);

    }


    public void writeState(FacesContext context, SerializedView state)
          throws IOException {

        String renderKitId = context.getViewRoot().getRenderKitId();
        ResponseStateManager rsm =
              Util.getResponseStateManager(context, renderKitId);
        rsm.writeState(context, state);

    }

    // ------------------------------------------------------- Protected Methods


    protected void checkIdUniqueness(FacesContext context,
                                     UIComponent component,
                                     Set componentIds)
          throws IllegalStateException {

        // deal with children/facets that are marked transient. 
        for (Iterator i = component.getFacetsAndChildren(); i.hasNext();) {

            UIComponent kid = (UIComponent) i.next();
            // check for id uniqueness
            String id = kid.getClientId(context);
            if (componentIds.add(id)) {
                checkIdUniqueness(context, kid, componentIds);
            } else {
                if (id != null && !componentIds.add(id)) {
                    String messageString = Util.getExceptionMessageString(
                          Util.DUPLICATE_COMPONENT_ID_ERROR_ID,
                          new Object[]{id});
                    if (LOG.isErrorEnabled()) {
                        LOG.error(messageString);
                    }
                    throw new IllegalStateException(messageString);
                }
            }
        }

    }


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
        noOfViewsInLogicalView =
              DEFAULT_NUMBER_OF_VIEWS_IN_LOGICAL_VIEW_IN_SESSION;
        String noOfViewsStr = context.getExternalContext().
              getInitParameter(NUMBER_OF_VIEWS_IN_LOGICAL_VIEW_IN_SESSION);
        if (noOfViewsStr != null) {
            try {
                noOfViewsInLogicalView =
                      Integer.valueOf(noOfViewsStr).intValue();
            } catch (NumberFormatException nfe) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Error parsing the servetInitParameter "
                              +
                              NUMBER_OF_VIEWS_IN_LOGICAL_VIEW_IN_SESSION
                              + ". Using default "
                              +
                              noOfViewsInLogicalView);
                }
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
        noOfViews = DEFAULT_NUMBER_OF_VIEWS_IN_SESSION;
        String noOfViewsStr = context.getExternalContext().
              getInitParameter(NUMBER_OF_VIEWS_IN_SESSION);
        if (noOfViewsStr != null) {
            try {
                noOfViews = Integer.valueOf(noOfViewsStr).intValue();
            } catch (NumberFormatException nfe) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Error parsing the servetInitParameter " +
                              NUMBER_OF_VIEWS_IN_SESSION + ". Using default " +
                              noOfViews);
                }
            }
        }
        return noOfViews;

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


    protected void restoreComponentState(FacesContext context,
                                         UIViewRoot root, String renderKitId) {

        if (null == renderKitId) {
            String message = Util.getExceptionMessageString
                  (Util.NULL_PARAMETERS_ERROR_MESSAGE_ID);
            message = message + " renderKitId " + renderKitId;
            throw new IllegalArgumentException(message);
        }
        
        ResponseStateManager rsm =
              Util.getResponseStateManager(context, renderKitId);
        Object state = rsm.getComponentStateToRestore(context);
        root.processRestoreState(context, state);

    }


    /**
     * Returns the <code> UIViewRoot</code> corresponding the
     * <code> viewId </code> by restoring the view structure and state.
     */
    protected UIViewRoot restoreSerializedView(FacesContext context,
                                               SerializedView sv,
                                               String viewId) {

        if (sv == null) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Possibly a new request. Tree structure could not "
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


    protected UIViewRoot restoreTreeStructure(FacesContext context,
                                              String viewId,
                                              String renderKitId) {

        if (null == renderKitId) {
            String message = Util.getExceptionMessageString
                  (Util.NULL_PARAMETERS_ERROR_MESSAGE_ID);
            message = message + " renderKitId " + renderKitId;
            throw new IllegalArgumentException(message);
        }
        
        ResponseStateManager rsm =
              Util.getResponseStateManager(context, renderKitId);
        TreeStructure structRoot =
              (TreeStructure) rsm.getTreeStructureToRestore(context, viewId);
        if (structRoot == null) {
            return null;
        }
        UIComponent viewRoot = structRoot.createComponent();
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

}

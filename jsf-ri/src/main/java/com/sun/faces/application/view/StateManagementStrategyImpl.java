/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2011 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.faces.application.view;

import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.FactoryFinder;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.component.visit.VisitResult;
import javax.faces.context.FacesContext;
import javax.faces.render.ResponseStateManager;

import com.sun.faces.context.StateContext;
import com.sun.faces.facelets.tag.ui.UIDebug;
import com.sun.faces.renderkit.RenderKitUtils;
import com.sun.faces.util.ComponentStruct;
import com.sun.faces.util.FacesLogger;
import com.sun.faces.util.MessageUtils;
import com.sun.faces.util.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.io.IOException;
import java.io.Serializable;
import javax.faces.application.StateManager;
import javax.faces.component.ContextCallback;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitHint;
import javax.faces.view.StateManagementStrategy;
import javax.faces.FacesException;
import javax.faces.application.Application;
import javax.faces.view.ViewDeclarationLanguage;
import javax.faces.view.ViewDeclarationLanguageFactory;

/**
 * <p>
 * A <code>StateManager</code> implementation to meet the requirements
 * of the specification.
 * </p>
 *
 * <p>
 * For those who had compile dependencies on this class, we're sorry for any
 * inconvenience, but this had to be re-worked as the version you depended on
 * was incorrectly implemented.  
 * </p>
 */
public class StateManagementStrategyImpl extends StateManagementStrategy {

    private static final Logger LOGGER = FacesLogger.APPLICATION_VIEW.getLogger();

    private final ViewDeclarationLanguageFactory vdlFactory;

    private static final String CLIENTIDS_TO_REMOVE_NAME =
            "com.sun.faces.application.view.CLIENTIDS_TO_REMOVE";
    private static final String CLIENTIDS_TO_ADD_NAME =
            "com.sun.faces.application.view.CLIENTIDS_TO_ADD";

    private static String SKIP_ITERATION_HINT =
        "javax.faces.visit.SKIP_ITERATION";



    // ------------------------------------------------------------ Constructors


    /**
     * Create a new <code>StateManagerImpl</code> instance.
     */
    public StateManagementStrategyImpl() {

        vdlFactory = (ViewDeclarationLanguageFactory)
              FactoryFinder.getFactory(FactoryFinder.VIEW_DECLARATION_LANGUAGE_FACTORY);

    }

    
    // ----------------------------------------------- Methods from StateManager


    /**
     * @see {@link javax.faces.application.StateManager#saveView(javax.faces.context.FacesContext))
     */
    @Override
    public Object saveView(FacesContext context) {

        if (context == null) {
            return null;
        }

        // irrespective of method to save the tree, if the root is transient
        // no state information needs to  be persisted.
        UIViewRoot viewRoot = context.getViewRoot();
        if (viewRoot.isTransient()) {
            return null;
        }


        // honor the requirement to check for id uniqueness
        Util.checkIdUniqueness(context,
                               viewRoot,
                               new HashSet<String>(viewRoot.getChildCount() << 1));
        final Map<String,Serializable> stateMap = new HashMap<String,Serializable>();

        final StateContext stateContext = StateContext.getStateContext(context);

        // PENDING: This is included for those component frameworks that don't utilize the
        // new VisitHint(s) yet - but still wish to know that they should be non-iterating
        // during state saving.  It should be removed at some point.
        context.getAttributes().put(SKIP_ITERATION_HINT, true);

        Set<VisitHint> hints = EnumSet.of(VisitHint.SKIP_ITERATION);
        VisitContext visitContext = VisitContext.createVisitContext(context, null, hints);
        final FacesContext finalContext = context;
        try {
            viewRoot.visitTree(visitContext, new VisitCallback() {
                public VisitResult visit(VisitContext context, UIComponent target) {
                    VisitResult result = VisitResult.ACCEPT;
                    Serializable stateObj;
                    if (!target.isTransient()) {
                        if (stateContext.componentAddedDynamically(target)) {
                            stateObj = new StateHolderSaver(finalContext, target);                           

                        } else {
                            stateObj = (Serializable) target.saveState(context.getFacesContext());
                        }
                        if (null != stateObj) {
                            stateMap.put(target.getClientId(context.getFacesContext()), stateObj);
                        }
                    }    else {
                        return result;
                    }
                    return result;
                }
            });
        } finally {
            // PENDING: This is included for those component frameworks that don't utilize the
            // new VisitHint(s) yet - but still wish to know that they should be non-iterating
            // during state saving.  It should be removed at some point.
            context.getAttributes().remove(SKIP_ITERATION_HINT);
        }

        // handle dynamic adds/removes
        List<String> removeList = stateContext.getDynamicRemoves();
        if (null != removeList && !removeList.isEmpty()) {
            stateMap.put(CLIENTIDS_TO_REMOVE_NAME, (Serializable) removeList);
        }
        Map<String, ComponentStruct> addList = stateContext.getDynamicAdds();
        if (null != addList && !addList.isEmpty()) {
            List<Object> savedAddList = new ArrayList<Object>(addList.size());
            for (ComponentStruct s : addList.values()) {
                savedAddList.add(s.saveState(context));
            }
            stateMap.put(CLIENTIDS_TO_ADD_NAME, savedAddList.toArray());
        }
        if (UIDebug.isRecordStateSize(context)) {
            try {
                UIDebug.computeViewStateSize(context, stateMap);
            } catch (IOException ex) {
                LOGGER.log(Level.SEVERE, "Unable to obtain view state size for UIDebug", ex);
            }
        }
        return new Object[] { null, stateMap }; 

    }


    /**
     * @see {@link StateManager#restoreView(javax.faces.context.FacesContext, String, String)}
     */
    @Override
    public UIViewRoot restoreView(FacesContext context,
                                  String viewId,
                                  String renderKitId) {

        ResponseStateManager rsm =
              RenderKitUtils.getResponseStateManager(context, renderKitId);
        boolean processingEvents = context.isProcessingEvents();
        // Build the tree to initial state
        UIViewRoot viewRoot;
        
        Object[] rawState = (Object[]) rsm.getState(context, viewId);
        //noinspection unchecked
        final Map<String, Object> state = (rawState == null) ? Collections.EMPTY_MAP : (Map<String,Object>) rawState[1];

        try {
            ViewDeclarationLanguage vdl = vdlFactory.getViewDeclarationLanguage(viewId);
            viewRoot = vdl.getViewMetadata(context, viewId).createMetadataView(context);
            context.setViewRoot(viewRoot);
            
            // JAVASERVERFACES_SPEC_PUBLIC-787 - restore viewMap before buildView
            String cid = viewRoot.getClientId(context);
            Object stateObj = state.get(cid);
            if (! (stateObj instanceof StateHolderSaver)) {
                viewRoot.restoreViewScopeState(context, stateObj);
            }

            context.setProcessingEvents(true);
            vdl.buildView(context, viewRoot);
        } catch (IOException ioe) {
            throw new FacesException(ioe);
        }
        if (rawState == null) {
          return null; // trigger a ViewExpiredException
        }
        final StateContext stateContext = StateContext.getStateContext(context);

        if (null != state) {
            try {
                stateContext.setTrackViewModifications(false);
                final Application app = context.getApplication();
                // We need to clone the tree, otherwise we run the risk
                // of being left in a state where the restored
                // UIComponent instances are in the session instead
                // of the TreeNode instances.  This is a problem
                // for servers that persist session data since
                // UIComponent instances are not serializable.

    
                // PENDING: This is included for those component frameworks that don't utilize the
                // new VisitHint(s) yet - but still wish to know that they should be non-iterating
                // during state saving.  It should be removed at some point.
                context.getAttributes().put(SKIP_ITERATION_HINT, true);

                Set<VisitHint> hints = EnumSet.of(VisitHint.SKIP_ITERATION, VisitHint.EXECUTE_LIFECYCLE);
                VisitContext visitContext = VisitContext.createVisitContext(context, null, hints);
                viewRoot.visitTree(visitContext, new VisitCallback() {

                    public VisitResult visit(VisitContext context, UIComponent target) {
                        VisitResult result = VisitResult.ACCEPT;
                        String cid = target.getClientId(context.getFacesContext());
                        Object stateObj = state.get(cid);
                        if (stateObj != null && !stateContext.componentAddedDynamically(target)) {
                            boolean restoreStateNow = true;
                            if (stateObj instanceof StateHolderSaver) {
                                restoreStateNow = !((StateHolderSaver)stateObj).componentAddedDynamically();
                            }
                            if (restoreStateNow) {
                                try {
                                    target.restoreState(context.getFacesContext(),
                                            stateObj);
                                } catch (Exception e) {
                                    String msg =
                                            MessageUtils.getExceptionMessageString(
                                            MessageUtils.PARTIAL_STATE_ERROR_RESTORING_ID,
                                            cid,
                                            e.toString());
                                    throw new FacesException(msg, e);
                                }
                            }
                        }

                        return result;
                    }

                });



                // Handle dynamic add/removes
                //noinspection unchecked
                List<String> removeList = (List<String>) state.get(CLIENTIDS_TO_REMOVE_NAME);
                if (null != removeList && !removeList.isEmpty()) {
                    for (String cur : removeList) {
                        boolean trackMods = stateContext.trackViewModifications();
                        if (trackMods) {
                            stateContext.setTrackViewModifications(false);
                        }
                        viewRoot.invokeOnComponent(context, cur, new ContextCallback() {

                            public void invokeContextCallback(FacesContext context, UIComponent target) {
                                UIComponent parent = target.getParent();
                                if (null != parent) {
                                    parent.getChildren().remove(target);
                                }
                            }

                        });
                        if (trackMods) {
                            stateContext.setTrackViewModifications(true);
                        }
                    }
                }

                Object restoredAddList[] = (Object[]) state.get(CLIENTIDS_TO_ADD_NAME);
                if (restoredAddList != null && restoredAddList.length > 0) {
                    // Restore the list of added components
                    List<ComponentStruct> addList = new ArrayList<ComponentStruct>(restoredAddList.length);
                    for (Object aRestoredAddList : restoredAddList) {
                        ComponentStruct cur = new ComponentStruct();
                        cur.restoreState(context, aRestoredAddList);
                        addList.add(cur);
                    }
                    // restore the components themselves
                    for (ComponentStruct cur : addList) {
                        final ComponentStruct finalCur = cur;
                        // Find the parent

                        viewRoot.visitTree(visitContext, new VisitCallback() {
                            public VisitResult visit(VisitContext context, UIComponent target) {
                                VisitResult result = VisitResult.ACCEPT;
                                if (finalCur.parentClientId.equals(target.getClientId(context.getFacesContext()))) {
                                    StateHolderSaver saver = (StateHolderSaver) state.get(finalCur.clientId);
                                    UIComponent toAdd = (UIComponent) saver.restore(context.getFacesContext());
                                    int idx = finalCur.indexOfChildInParent;
                                    if (idx == -1) {
                                        // add facet to the parent
                                        target.getFacets().put(finalCur.facetName, toAdd);
                                    } else {
                                        // add the child to the parent at correct index
                                        try {
                                            target.getChildren().add(finalCur.indexOfChildInParent, toAdd);
                                        } catch (IndexOutOfBoundsException ioobe) {
                                            // the indexing within the parent list is off during the restore.
                                                // This is most likely due to a transient component added during
                                                // RENDER_REPONSE phase.
                                                if (LOGGER.isLoggable(Level.FINE)) {
                                                    LOGGER.log(Level.FINE,
                                                            "Unable to insert child with client ID {0} into parent with client ID {1} into list at index {2}.",
                                                            new Object[]{finalCur.clientId,
                                                                finalCur.parentClientId,
                                                                finalCur.indexOfChildInParent});
                                                }
                                                target.getChildren().add(toAdd);
                                        }


                                    }
                                   
                                   // Add back to dynamic adds list
                                    Map<String, ComponentStruct> dynamicAdds = stateContext.getDynamicAdds();
                                    assert(null != dynamicAdds);
                                    String clientId = toAdd.getClientId(context.getFacesContext());
                                    if (!dynamicAdds.containsKey(clientId)) {
                                        ComponentStruct toAddCS = new ComponentStruct();
                                        toAddCS.absorbComponent(context.getFacesContext(), toAdd);
                                        dynamicAdds.put(clientId, toAddCS);
                                    }
                                }

                                return result;
                            }
                        });
                    }
                }
            } finally {
                stateContext.setTrackViewModifications(true); 
                // PENDING: This is included for those component frameworks that don't utilize the
                // new VisitHint(s) yet - but still wish to know that they should be non-iterating
                // during state saving.  It should be removed at some point.
                context.getAttributes().remove(SKIP_ITERATION_HINT);
            }
        } else {
            viewRoot = null;
        }
        context.setProcessingEvents(processingEvents);
        return viewRoot;

    }




    // --------------------------------------------------------- Private Methods

}

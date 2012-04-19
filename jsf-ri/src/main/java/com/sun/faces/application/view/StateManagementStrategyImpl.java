/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2012 Oracle and/or its affiliates. All rights reserved.
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

import com.sun.faces.context.StateContext;
import com.sun.faces.renderkit.RenderKitUtils;
import com.sun.faces.util.ComponentStruct;
import com.sun.faces.util.FacesLogger;
import com.sun.faces.util.MessageUtils;
import com.sun.faces.util.Util;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.io.IOException;
import javax.faces.FactoryFinder;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.component.visit.VisitResult;
import javax.faces.context.FacesContext;
import javax.faces.render.ResponseStateManager;
import javax.faces.application.StateManager;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitHint;
import javax.faces.view.StateManagementStrategy;
import javax.faces.FacesException;
import javax.faces.application.Application;
import javax.faces.view.ViewDeclarationLanguage;
import javax.faces.view.ViewDeclarationLanguageFactory;
import static com.sun.faces.RIConstants.DYNAMIC_ACTIONS;
import static com.sun.faces.RIConstants.DYNAMIC_COMPONENT;
import javax.faces.component.ContextCallback;

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
        final Map<String,Object> stateMap = new HashMap<String,Object>();

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
                    Object stateObj;
                    if (!target.isTransient()) {
                        if (stateContext.componentAddedDynamically(target)) {
                            target.getAttributes().put(DYNAMIC_COMPONENT,
                                    new Integer(target.getParent().getChildren().indexOf(target)));
                            stateObj = new StateHolderSaver(finalContext, target);
                        } else {
                            stateObj = target.saveState(context.getFacesContext());
                        }
                        if (null != stateObj) {
                            stateMap.put(target.getClientId(context.getFacesContext()), stateObj);
                        }
                    }    else {
                        return VisitResult.REJECT;
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
    
        saveDynamicActions(context, stateContext, stateMap);
        clearDynamicActions(stateContext);
        return new Object[] { null, stateMap };
    }

    /**
     * Save the dynamic actions.
     * 
     * @param context the Faces context.
     * @param stateContext the state context.
     * @param stateMap the state.
     */
    private void saveDynamicActions(FacesContext context, StateContext stateContext, Map<String, Object> stateMap) {
        List<ComponentStruct> actions = stateContext.getDynamicActions();
        if (actions != null) {
            List<Object> savedActions = new ArrayList<Object>(actions.size());
            for(ComponentStruct action : actions) {
                savedActions.add(action.saveState(context));
            }
            stateMap.put(DYNAMIC_ACTIONS, savedActions);
        }
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
        try {
            ViewDeclarationLanguage vdl = vdlFactory.getViewDeclarationLanguage(viewId);
            viewRoot = vdl.getViewMetadata(context, viewId).createMetadataView(context);
            context.setViewRoot(viewRoot);
            context.setProcessingEvents(true);
            vdl.buildView(context, viewRoot);
        } catch (IOException ioe) {
            throw new FacesException(ioe);
        }
        Object[] rawState = (Object[]) rsm.getState(context, viewId);
        if (rawState == null) {
            return null; // trigger a ViewExpiredException
        }
        //noinspection unchecked
        final Map<String, Object> state = (Map<String,Object>) rawState[1];
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
                restoreDynamicActions(context, stateContext, state);
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

    /**
     * Restore the list of dynamic actions and replay them.
     * 
     * @param context the Faces context.
     * @param stateContext the state context.
     * @param stateMap the state.
     * @param viewRoot the view root.
     */
    private void restoreDynamicActions(FacesContext context, StateContext stateContext, Map<String, Object> stateMap) {
        List<Object> savedActions = (List<Object>) stateMap.get(DYNAMIC_ACTIONS);
        List<ComponentStruct> actions = stateContext.getDynamicActions();
        
        if (savedActions != null && !savedActions.isEmpty()) {
            for(Object object : savedActions) {
                ComponentStruct action = new ComponentStruct();
                action.restoreState(context, object);
                if (ComponentStruct.ADD.equals(action.action)) {
                    restoreDynamicAdd(context, stateMap, action);
                }
                if (ComponentStruct.REMOVE.equals(action.action)) {
                    restoreDynamicRemove(context, action);
                }
                pruneAndReAddToDynamicActions(actions, action);
            }
        }
    }

    /**
     * Methods that takes care of pruning and re-adding an action to the 
     * dynamic action list.
     * 
     * <p>
     *  If you remove a component, re-add it to the same parent and then remove 
     *  it again, you only have to capture the FIRST remove. Similarly if you 
     *  add a component, remove it, and then re-add it to the same parent you 
     *  only need to capture the LAST add.
     * </p>
     * 
     * @param dynamicActionList the dynamic action list.
     * @param struct the component struct to add.
     */
    private void pruneAndReAddToDynamicActions(List<ComponentStruct> dynamicActionList, ComponentStruct struct) {
        int firstIndex = dynamicActionList.indexOf(struct);
        if (firstIndex == -1) {
            dynamicActionList.add(struct);
        } else {
            int lastIndex = dynamicActionList.lastIndexOf(struct);
            if (lastIndex == -1 || lastIndex == firstIndex) {
                dynamicActionList.add(struct);
            } else {
                if (ComponentStruct.ADD.equals(struct.action)) {
                    dynamicActionList.remove(lastIndex);
                    dynamicActionList.remove(firstIndex);
                    dynamicActionList.add(struct);
                }
                if (ComponentStruct.REMOVE.equals(struct.action)) {
                    dynamicActionList.remove(lastIndex);
                }
            }
        }
    }
    
    /**
     * Method that takes care of restoring a dynamic add.
     * 
     * @param context the Faces context.
     * @param state the state.
     * @param struct the component struct.
     */
    private void restoreDynamicAdd(FacesContext context, Map<String, Object> state, ComponentStruct struct) {
        UIComponent parent = locateComponentByClientId(context, struct.parentClientId);

        if (parent != null) {
            UIComponent child = locateComponentByClientId(context, struct.clientId);

            /*
             * If Facelets engine restored the child before us we are going to 
             * use it, but we need to remove it before we can add it in the 
             * correct place.
             */
            if (child != null) {
                if (struct.facetName == null) {
                    parent.getChildren().remove(child);
                } else {
                    parent.getFacets().remove(struct.facetName);
                }
            }
            
            /*
             * The child was not build previously, so we are going to check
             * if the component was saved in the state.
             */
            if (child == null) {
                StateHolderSaver saver = (StateHolderSaver) state.get(struct.clientId);
                if (saver != null) {
                    child = (UIComponent) saver.restore(context);
                }
            }

            /*
             * Are we adding =BACK= in a component that was not in the state,
             * because it was added by the initial buildView and removed by
             * another dynamic action?
             */
            if (child == null) {
                StateContext stateContext = StateContext.getStateContext(context);
                child = stateContext.getDynamicComponents().get(struct.clientId);
            }

            /*
             * Now if we have the child we are going to add it back in.
             */
            if (child != null) {
                if (struct.facetName != null) {
                    parent.getFacets().put(struct.facetName, child);
                } else {
                    int childIndex = -1;
                    if (child.getAttributes().containsKey(DYNAMIC_COMPONENT)) {
                        childIndex = (Integer) child.getAttributes().get(DYNAMIC_COMPONENT);
                    }
                    child.setId(struct.id);
                    if (childIndex >= parent.getChildCount() || childIndex == -1) {
                        parent.getChildren().add(child);
                    } else {
                        parent.getChildren().add(childIndex, child);
                    }
                    child.getClientId();
                }
                child.getAttributes().put(DYNAMIC_COMPONENT, child.getParent().getChildren().indexOf(child));
            }
        }
    }
    
    /**
     * Method that takes care of restoring a dynamic remove.
     * 
     * @param context the Faces context.
     * @param struct the component struct.
     */
    private void restoreDynamicRemove(FacesContext context, ComponentStruct struct) {
        UIComponent child = locateComponentByClientId(context, struct.clientId);
        if (child != null) {
            StateContext stateContext = StateContext.getStateContext(context);
            stateContext.getDynamicComponents().put(struct.clientId, child);
            UIComponent parent = child.getParent();
            parent.getChildren().remove(child);
        }
    }
    
    /**
     * Find the given component in the component tree.
     * 
     * @param context the Faces context.
     * @param clientId the client id of the component to find.
     */
    private UIComponent locateComponentByClientId(final FacesContext context, final String clientId) {
        final List<UIComponent> found = new ArrayList<UIComponent>();
        UIComponent result = null;

        context.getViewRoot().invokeOnComponent(context, clientId, new ContextCallback() {

            public void invokeContextCallback(FacesContext context, UIComponent target) {
                found.add(target);
            }
        });

        if (found.isEmpty()) {
            /*
             * Since we did not find it the cheaper way we need to assume there
             * is a UINamingContainer that does not prepend its ID. So we are 
             * going to have to walk the tree to find it.
             */
            VisitContext visitContext = VisitContext.createVisitContext(context);
            context.getViewRoot().visitTree(visitContext, new VisitCallback() {
                public VisitResult visit(VisitContext visitContext, UIComponent component) {
                    VisitResult result = VisitResult.ACCEPT;
                    if (component.getClientId(visitContext.getFacesContext()).equals(clientId)) {
                        found.add(component); 
                        result = VisitResult.COMPLETE;
                    }
                    return result;
                }
            });
        }
        if (!found.isEmpty()) {
            result = found.get(0);
        }    
        return result;
    }

    /**
     * Reapply the dynamic actions after Facelets reapply.
     * 
     * <p>
     *   Note a precondition to this method is that tracking view modifications
     *   is turned off during the execution of this method. The caller of this
     *   method is responsible for turning tracking view modifications off and
     *   on as required.
     * </p>
     * 
     * @param context the Faces context.
     */
    public void reapplyDynamicActions(FacesContext context) {
        StateContext stateContext = StateContext.getStateContext(context);
        List<ComponentStruct> actions = stateContext.getDynamicActions();
        for(ComponentStruct action : actions) {
            if (ComponentStruct.REMOVE.equals(action.action)) {
                reapplyDynamicRemove(context, action);
            }
            if (ComponentStruct.ADD.equals(action.action)) {
                reapplyDynamicAdd(context, action);
            }
        }
    }

    /**
     * Reapply the dynamic remove after Facelets reapply.
     * 
     * @param context the Faces context.
     * @param struct the component struct.
     */
    private void reapplyDynamicRemove(FacesContext context, ComponentStruct struct) {
        UIComponent child = locateComponentByClientId(context, struct.clientId);
        if (child != null) {
            StateContext stateContext = StateContext.getStateContext(context);
            stateContext.getDynamicComponents().put(struct.clientId, child);
            UIComponent parent = child.getParent();
            parent.getChildren().remove(child);
        }
    }

    /*
     * Reapply the dynamic add after Facelets reapply.
     * 
     * @param context the Faces context.
     * @param struct the component struct.
     */
    private void reapplyDynamicAdd(FacesContext context, ComponentStruct struct) {
        UIComponent parent = locateComponentByClientId(context, struct.parentClientId);
        if (parent != null) {
            UIComponent child = locateComponentByClientId(context, struct.clientId);
            
            if (child == null) {
                StateContext stateContext = StateContext.getStateContext(context);
                child = stateContext.getDynamicComponents().get(struct.clientId);
            }
            
            if (child != null) {
                if (struct.facetName != null) {
                    parent.getFacets().remove(struct.facetName);
                    parent.getFacets().put(struct.facetName, child);
                    child.getClientId();
                } else {
                    int childIndex = -1;
                    if (child.getAttributes().containsKey(DYNAMIC_COMPONENT)) {
                        childIndex = (Integer) child.getAttributes().get(DYNAMIC_COMPONENT);
                    }
                    child.setId(struct.id);
                    if (childIndex >= parent.getChildCount() || childIndex == -1) {
                        parent.getChildren().add(child);
                    } else {
                        parent.getChildren().add(childIndex, child);
                    }
                    child.getClientId();
                    child.getAttributes().put(DYNAMIC_COMPONENT, child.getParent().getChildren().indexOf(child));
                }
            }
        }
    }

    /**
     * Clear out the dynamic actions.
     * 
     * @param stateContext the state context.
     */
    private void clearDynamicActions(StateContext stateContext) {
        if (stateContext != null && stateContext.getDynamicActions() != null) {
            stateContext.getDynamicActions().clear();
        }
    }
}

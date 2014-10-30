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
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.FacesException;
import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.component.UIViewRoot;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitHint;
import javax.faces.component.visit.VisitResult;
import javax.faces.context.FacesContext;
import javax.faces.render.ResponseStateManager;
import javax.faces.view.StateManagementStrategy;
import static com.sun.faces.RIConstants.DYNAMIC_ACTIONS;
import static com.sun.faces.RIConstants.DYNAMIC_COMPONENT;
import javax.faces.application.ProjectStage;

/**
 * The state management strategy for PSS.
 *
 * @author Manfred Riem (manfred.riem@oracle.com)
 */
public class FaceletPartialStateManagementStrategy extends StateManagementStrategy {

    /**
     * Stores the logger.
     */
    private static final Logger LOGGER = FacesLogger.APPLICATION_VIEW.getLogger();
    /**
     * Stores the skip hint.
     */
    private static String SKIP_ITERATION_HINT = "javax.faces.visit.SKIP_ITERATION";

    /**
     * Constructor.
     */
    public FaceletPartialStateManagementStrategy() {
        this(FacesContext.getCurrentInstance());
    }

    /**
     * Constructor.
     *
     * @param context the Faces context.
     */
    public FaceletPartialStateManagementStrategy(FacesContext context) {
    }

    /**
     * Find the given component in the component tree.
     *
     * @param context the Faces context.
     * @param clientId the client id of the component to find.
     */
    private UIComponent locateComponentByClientId(final FacesContext context, final UIComponent subTree, final String clientId) {
        if (LOGGER.isLoggable(Level.FINEST)) {
            LOGGER.log(Level.FINEST, "FaceletPartialStateManagementStrategy.locateComponentByClientId", clientId);
        }

        final List<UIComponent> found = new ArrayList<UIComponent>();
        UIComponent result = null;

        try {
            context.getAttributes().put(SKIP_ITERATION_HINT, true);
            Set<VisitHint> hints = EnumSet.of(VisitHint.SKIP_ITERATION);

            VisitContext visitContext = VisitContext.createVisitContext(context, null, hints);
            subTree.visitTree(visitContext, new VisitCallback() {

                public VisitResult visit(VisitContext visitContext, UIComponent component) {
                    VisitResult result = VisitResult.ACCEPT;
                    if (component.getClientId(visitContext.getFacesContext()).equals(clientId)) {
                        /*
                         * If the client id matches up we have found our match.
                         */
                        found.add(component);
                        result = VisitResult.COMPLETE;
                    } else if (component instanceof UIForm) {
                        /*
                         * If the component is a UIForm and it is prepending its
                         * id then we can short circuit out of here if the the
                         * client id of the component we are trying to find does
                         * not begin with the id of the UIForm.
                         */
                        UIForm form = (UIForm) component;
                        if (form.isPrependId() && !clientId.startsWith(form.getClientId(visitContext.getFacesContext()))) {
                            result = VisitResult.REJECT;
                        }
                    } else if (component instanceof NamingContainer &&
                        !clientId.startsWith(component.getClientId(visitContext.getFacesContext()))) {
                        /*
                         * If the component is a naming container then assume it
                         * is prepending its id so if our client id we are
                         * looking for does not start with the naming container
                         * id we can skip visiting this tree.
                         */
                        result = VisitResult.REJECT;
                    }

                    return result;
                }
            });
        } finally {
            context.getAttributes().remove(SKIP_ITERATION_HINT);
        }

        if (!found.isEmpty()) {
            result = found.get(0);
        }
        return result;
    }

    /**
     * Methods that takes care of pruning and re-adding an action to the dynamic
     * action list.
     *
     * <p> If you remove a component, re-add it to the same parent and then
     * remove it again, you only have to capture the FIRST remove. Similarly if
     * you add a component, remove it, and then re-add it to the same parent you
     * only need to capture the LAST add. </p>
     *
     * @param dynamicActionList the dynamic action list.
     * @param struct the component struct to add.
     */
    private void pruneAndReAddToDynamicActions(List<ComponentStruct> dynamicActionList, ComponentStruct struct) {
        if (LOGGER.isLoggable(Level.FINEST)) {
            LOGGER.finest("FaceletPartialStateManagementStrategy.pruneAndReAddToDynamicActions");
        }

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
     * Restore the list of dynamic actions and replay them.
     *
     * @param context the Faces context.
     * @param stateContext the state context.
     * @param stateMap the state.
     * @param viewRoot the view root.
     */
    private void restoreDynamicActions(FacesContext context, StateContext stateContext, Map<String, Object> stateMap) {
        if (LOGGER.isLoggable(Level.FINEST)) {
            LOGGER.finest("FaceletPartialStateManagementStrategy.restoreDynamicActions");
        }

        List<Object> savedActions = (List<Object>) stateMap.get(DYNAMIC_ACTIONS);
        List<ComponentStruct> actions = stateContext.getDynamicActions();

        if (savedActions != null && !savedActions.isEmpty()) {
            for (Object object : savedActions) {
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
     * Method that takes care of restoring a dynamic add.
     *
     * @param context the Faces context.
     * @param state the state.
     * @param struct the component struct.
     */
    private void restoreDynamicAdd(FacesContext context, Map<String, Object> state, ComponentStruct struct) {
        if (LOGGER.isLoggable(Level.FINEST)) {
            LOGGER.finest("FaceletPartialStateManagementStrategy.restoreDynamicAdd");
        }

        UIComponent parent = locateComponentByClientId(context, context.getViewRoot(), struct.parentClientId);

        if (parent != null) {
            UIComponent child = locateComponentByClientId(context, parent, struct.clientId);

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
             * The child was not build previously, so we are going to check if
             * the component was saved in the state.
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
            StateContext stateContext = StateContext.getStateContext(context);
            if (child == null) {
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
                stateContext.getDynamicComponents().put(struct.clientId, child);
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
        if (LOGGER.isLoggable(Level.FINEST)) {
            LOGGER.finest("FaceletPartialStateManagementStrategy.restoreDynamicRemove");
        }

        UIComponent child = locateComponentByClientId(context, context.getViewRoot(), struct.clientId);
        if (child != null) {
            StateContext stateContext = StateContext.getStateContext(context);
            stateContext.getDynamicComponents().put(struct.clientId, child);
            UIComponent parent = child.getParent();
            parent.getChildren().remove(child);
        }
    }

    /**
     * Restore the view.
     *
     * @param context the Faces context.
     * @param viewId the view id.
     * @param renderKitId the render kit id.
     * @return the view root.
     */
    @Override
    public UIViewRoot restoreView(FacesContext context, String viewId, String renderKitId) {

        if (LOGGER.isLoggable(Level.FINEST)) {
            LOGGER.log(Level.FINEST, "FaceletPartialStateManagementStrategy.restoreView", new Object[]{viewId, renderKitId});
        }

        ResponseStateManager rsm = RenderKitUtils.getResponseStateManager(context, renderKitId);
        boolean processingEvents = context.isProcessingEvents();
        UIViewRoot viewRoot = context.getViewRoot();

        Object[] rawState = (Object[]) rsm.getState(context, viewId);
        if (rawState == null) {
            return null;
        }

        final Map<String, Object> state = (Map<String, Object>) rawState[1];
        final StateContext stateContext = StateContext.getStateContext(context);

        if (state != null) {
            try {
                stateContext.setTrackViewModifications(false);

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
                                restoreStateNow = !((StateHolderSaver) stateObj).componentAddedDynamically();
                            }
                            if (restoreStateNow) {
                                try {
                                    target.restoreState(context.getFacesContext(), stateObj);
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
                context.getAttributes().remove(SKIP_ITERATION_HINT);
            }
        } else {
            viewRoot = null;
        }
        context.setProcessingEvents(processingEvents);
        return viewRoot;
    }

    /**
     * Save the dynamic actions.
     *
     * @param context the Faces context.
     * @param stateContext the state context.
     * @param stateMap the state.
     */
    private void saveDynamicActions(FacesContext context, StateContext stateContext, Map<String, Object> stateMap) {
        if (LOGGER.isLoggable(Level.FINEST)) {
            LOGGER.finest("FaceletPartialStateManagementStrategy.saveDynamicActions");
        }

        List<ComponentStruct> actions = stateContext.getDynamicActions();
        HashMap<String, UIComponent> componentMap = stateContext.getDynamicComponents();
        
        if (actions != null) {
            List<Object> savedActions = new ArrayList<Object>(actions.size());
            for (ComponentStruct action : actions) {
                UIComponent component = componentMap.get(action.clientId);
                if (component == null && context.isProjectStage(ProjectStage.Development)) {
                    LOGGER.log(
                            Level.WARNING,
                            "Unable to save dynamic action with clientId ''{0}'' because the UIComponent cannot be found",
                            action.clientId);
                }
                if (component != null) {
                    savedActions.add(action.saveState(context));
                }
            }
            stateMap.put(DYNAMIC_ACTIONS, savedActions);
        }
    }

    /**
     * Save the view.
     *
     * @param context the Faces context.
     * @return the saved view.
     */
    @Override
    public Object saveView(FacesContext context) {
        if (LOGGER.isLoggable(Level.FINEST)) {
            LOGGER.finest("FaceletPartialStateManagementStrategy.saveView");
        }

        if (context == null) {
            return null;
        }

        UIViewRoot viewRoot = context.getViewRoot();
        if (viewRoot.isTransient()) {
            return null;
        }

        Util.checkIdUniqueness(context, viewRoot, new HashSet<String>(viewRoot.getChildCount() << 1));

        final Map<String, Object> stateMap = new HashMap<String, Object>();
        final StateContext stateContext = StateContext.getStateContext(context);

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
                            target.getAttributes().put(DYNAMIC_COMPONENT, target.getParent().getChildren().indexOf(target));
                            stateObj = new StateHolderSaver(finalContext, target);
                        } else {
                            stateObj = target.saveState(context.getFacesContext());
                        }
                        if (stateObj != null) {
                            stateMap.put(target.getClientId(context.getFacesContext()), stateObj);
                        }
                    } else {
                        return VisitResult.REJECT;
                    }
                    return result;
                }
            });
        } finally {
            context.getAttributes().remove(SKIP_ITERATION_HINT);
        }

        saveDynamicActions(context, stateContext, stateMap);
        StateContext.release(context);
        return new Object[]{null, stateMap};
    }
}

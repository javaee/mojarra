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
import com.sun.faces.util.Util;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.FacesException;
import javax.faces.application.ProjectStage;
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

/**
 * A state management strategy for FSS.
 *
 * @author Manfred Riem (manfred.riem@oracle.com)
 */
public class FaceletFullStateManagementStrategy extends StateManagementStrategy {

    /**
     * Stores the logger.
     */
    private static final Logger LOGGER = FacesLogger.APPLICATION_VIEW.getLogger();
    /**
     * Stores the skip hint.
     */
    private static String SKIP_ITERATION_HINT = "javax.faces.visit.SKIP_ITERATION";
    /**
     * Stores the class map.
     */
    private Map<String, Class<?>> classMap;
    /**
     * Are we in development mode.
     */
    private boolean isDevelopmentMode;

    /**
     * Constructor.
     */
    public FaceletFullStateManagementStrategy() {
        this(FacesContext.getCurrentInstance());
    }

    /**
     * Constructor.
     *
     * @param context the Faces context.
     */
    public FaceletFullStateManagementStrategy(FacesContext context) {
        isDevelopmentMode = context.isProjectStage(ProjectStage.Development);
        classMap = new ConcurrentHashMap<String, Class<?>>(32);
    }

    /**
     * Capture the child.
     *
     * @param tree the tree.
     * @param parent the parent.
     * @param c the component.
     */
    private void captureChild(List<TreeNode> tree, int parent, UIComponent c) {

        if (!c.isTransient() && !c.getAttributes().containsKey(DYNAMIC_COMPONENT)) {
            TreeNode n = new TreeNode(parent, c);
            int pos = tree.size();
            tree.add(n);
            captureRest(tree, pos, c);
        }
    }

    /**
     * Capture the facet.
     *
     * @param tree the tree.
     * @param parent the parent.
     * @param name the facet name.
     * @param c the component.
     */
    private void captureFacet(List<TreeNode> tree, int parent, String name, UIComponent c) {

        if (!c.isTransient() && !c.getAttributes().containsKey(DYNAMIC_COMPONENT)) {
            FacetNode n = new FacetNode(parent, name, c);
            int pos = tree.size();
            tree.add(n);
            captureRest(tree, pos, c);
        }
    }

    /**
     * Capture the rest.
     *
     * @param tree the tree.
     * @param pos the position.
     * @param c the component.
     */
    private void captureRest(List<TreeNode> tree, int pos, UIComponent c) {

        int sz = c.getChildCount();
        if (sz > 0) {
            List<UIComponent> child = c.getChildren();
            for (int i = 0; i < sz; i++) {
                captureChild(tree, pos, child.get(i));
            }
        }

        sz = c.getFacetCount();
        if (sz > 0) {
            for (Map.Entry<String, UIComponent> entry : c.getFacets().entrySet()) {
                captureFacet(tree, pos, entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * Find the given component in the component tree.
     *
     * @param context the Faces context.
     * @param clientId the client id of the component to find.
     */
    private UIComponent locateComponentByClientId(final FacesContext context, final UIComponent subTree, final String clientId) {
        if (LOGGER.isLoggable(Level.FINEST)) {
            LOGGER.log(Level.FINEST, "FaceletFullStateManagementStrategy.locateComponentByClientId", clientId);
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
     * Create a new component instance.
     *
     * @param n the tree node.
     * @return the UI component.
     * @throws FacesException when a serious error occurs.
     */
    private UIComponent newInstance(TreeNode n) throws FacesException {
        if (LOGGER.isLoggable(Level.FINEST)) {
            LOGGER.log(Level.FINEST, "FaceletFullStateManagementStrategy.newInstance", n.componentType);
        }

        try {
            Class<?> t = ((classMap != null) ? classMap.get(n.componentType) : null);
            if (t == null) {
                t = Util.loadClass(n.componentType, n);
                if (t != null && classMap != null) {
                    classMap.put(n.componentType, t);
                } else {
                    if (!isDevelopmentMode) {
                        throw new NullPointerException();
                    }
                }
            }

            assert (t != null);
            UIComponent c = (UIComponent) t.newInstance();
            c.setId(n.id);

            return c;
        } catch (Exception e) {
            throw new FacesException(e);
        }
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
            LOGGER.finest("FaceletFullStateManagementStrategy.pruneAndReAddToDynamicActions");
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
     * Restore the component state.
     *
     * @param context the Faces context.
     * @param state the component state.
     */
    private void restoreComponentState(final FacesContext context, final HashMap<String, Object> state) {

        final StateContext stateContext = StateContext.getStateContext(context);
        final UIViewRoot viewRoot = context.getViewRoot();

        try {
            context.getAttributes().put(SKIP_ITERATION_HINT, true);
            Set<VisitHint> hints = EnumSet.of(VisitHint.SKIP_ITERATION);
            VisitContext visitContext = VisitContext.createVisitContext(context, null, hints);

            viewRoot.visitTree(visitContext, new VisitCallback() {

                public VisitResult visit(VisitContext visitContext, UIComponent component) {
                    VisitResult result = VisitResult.ACCEPT;

                    String cid = component.getClientId(context);
                    Object stateObj = state.get(cid);

                    if (stateObj != null && !stateContext.componentAddedDynamically(component)) {
                        boolean restoreStateNow = true;
                        if (stateObj instanceof StateHolderSaver) {
                            restoreStateNow = !((StateHolderSaver) stateObj).componentAddedDynamically();
                        }
                        if (restoreStateNow) {
                            try {
                                component.restoreState(context, stateObj);
                            } catch (Exception e) {
                                throw new FacesException(e);
                            }
                        }
                    }

                    return result;
                }
            });
        } finally {
            context.getAttributes().remove(SKIP_ITERATION_HINT);
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
    private void restoreDynamicActions(FacesContext context, StateContext stateContext, HashMap<String, Object> state) {
        if (LOGGER.isLoggable(Level.FINEST)) {
            LOGGER.finest("FaceletFullStateManagementStrategy.restoreDynamicActions");
        }

        UIViewRoot viewRoot = context.getViewRoot();
        List<Object> savedActions = (List<Object>) viewRoot.getAttributes().get(DYNAMIC_ACTIONS);
        List<ComponentStruct> actions = stateContext.getDynamicActions();

        if (savedActions != null && !savedActions.isEmpty()) {
            for (Object object : savedActions) {
                ComponentStruct action = new ComponentStruct();
                action.restoreState(context, object);
                if (ComponentStruct.ADD.equals(action.action)) {
                    restoreDynamicAdd(context, state, action);
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
            LOGGER.finest("FaceletFullStateManagementStrategy.restoreDynamicAdd");
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
            LOGGER.finest("FaceletFullStateManagementStrategy.restoreDynamicRemove");
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
     * Restore the component tree.
     *
     * @param context the Faces context.
     * @param renderKitId the render kit id.
     * @param tree the saved tree.
     * @return the view root.
     * @throws FacesException when a serious error occurs.
     */
    private UIViewRoot restoreTree(FacesContext context, String renderKitId, Object[] tree) throws FacesException {
        if (LOGGER.isLoggable(Level.FINEST)) {
            LOGGER.log(Level.FINEST, "FaceletFullStateManagementStrategy.restoreTree", renderKitId);
        }

        UIComponent c;
        FacetNode fn;
        TreeNode tn;
        for (int i = 0; i < tree.length; i++) {
            if (tree[i] instanceof FacetNode) {
                fn = (FacetNode) tree[i];
                c = newInstance(fn);
                tree[i] = c;
                if (i != fn.parent) {
                    ((UIComponent) tree[fn.parent]).getFacets().put(fn.facetName, c);
                }

            } else {
                tn = (TreeNode) tree[i];
                c = newInstance(tn);
                tree[i] = c;
                if (i != tn.parent) {
                    ((UIComponent) tree[tn.parent]).getChildren().add(c);
                } else {
                    assert (c instanceof UIViewRoot);
                    UIViewRoot viewRoot = (UIViewRoot) c;
                    context.setViewRoot(viewRoot);
                    viewRoot.setRenderKitId(renderKitId);
                }
            }
        }

        return (UIViewRoot) tree[0];
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
            LOGGER.log(Level.FINEST, "FaceletFullStateManagementStrategy.restoreView", new Object[]{viewId, renderKitId});
        }

        UIViewRoot result = null;

        ResponseStateManager rsm = RenderKitUtils.getResponseStateManager(context, renderKitId);
        Object[] state = (Object[]) rsm.getState(context, viewId);

        if (state != null && state.length >= 2) {
            /*
             * Restore the component tree.
             */
            if (state[0] != null) {
                result = restoreTree(context, renderKitId, ((Object[]) state[0]).clone());
                context.setViewRoot(result);
            }

            if (result != null) {
                StateContext stateContext = StateContext.getStateContext(context);
                stateContext.startTrackViewModifications(context, result);
                stateContext.setTrackViewModifications(false);

                try {
                    HashMap<String, Object> stateMap = (HashMap<String, Object>) state[1];
                    if (stateMap != null) {
                        /*
                         * Restore the component state.
                         */
                        restoreComponentState(context, stateMap);

                        /**
                         * Restore the dynamic actions.
                         */
                        restoreDynamicActions(context, stateContext, stateMap);
                    }
                } finally {
                    stateContext.setTrackViewModifications(true);
                }
            }
        }

        return result;
    }

    /**
     * Save the component state.
     *
     * @param context the Faces context.
     * @return the saved state.
     */
    private Object saveComponentState(FacesContext context) {

        final HashMap<String, Object> stateMap = new HashMap<String, Object>();
        final StateContext stateContext = StateContext.getStateContext(context);
        final UIViewRoot viewRoot = context.getViewRoot();
        final FacesContext finalContext = context;

        context.getAttributes().put(SKIP_ITERATION_HINT, true);
        Set<VisitHint> hints = EnumSet.of(VisitHint.SKIP_ITERATION);
        VisitContext visitContext = VisitContext.createVisitContext(context, null, hints);

        try {
            viewRoot.visitTree(visitContext, new VisitCallback() {

                public VisitResult visit(VisitContext context, UIComponent component) {
                    VisitResult result = VisitResult.ACCEPT;
                    Object stateObj;
                    if (!component.isTransient()) {
                        if (stateContext.componentAddedDynamically(component)) {
                            component.getAttributes().put(DYNAMIC_COMPONENT, new Integer(getProperChildIndex(component)));
                            stateObj = new StateHolderSaver(finalContext, component);
                        } else {
                            stateObj = component.saveState(finalContext);
                        }
                        if (stateObj != null) {
                            stateMap.put(component.getClientId(finalContext), stateObj);
                        }
                    } else {
                        result = VisitResult.REJECT;
                    }
                    return result;
                }
            });
        } finally {
            context.getAttributes().remove(SKIP_ITERATION_HINT);
        }

        return stateMap;
    }

    /**
     * Save the dynamic actions.
     *
     * @param context the Faces context.
     * @param stateContext the state context.
     * @param stateMap the state.
     */
    private void saveDynamicActions(FacesContext context, StateContext stateContext, UIViewRoot viewRoot) {
        if (LOGGER.isLoggable(Level.FINEST)) {
            LOGGER.finest("FaceletFullStateManagementStrategy.saveDynamicActions");
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
                if (component != null && !component.isTransient() && !hasTransientAncestor(component)) {
                    savedActions.add(action.saveState(context));
                }
            }
            viewRoot.getAttributes().put(DYNAMIC_ACTIONS, savedActions);
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
            LOGGER.finest("FaceletFullStateManagementStrategy.saveView");
        }

        Object[] result;
        UIViewRoot viewRoot = context.getViewRoot();

        /*
         * Check uniqueness.
         */
        Util.checkIdUniqueness(context, viewRoot, new HashSet<String>(viewRoot.getChildCount() << 1));

        /**
         * Save the dynamic actions.
         */
        StateContext stateContext = StateContext.getStateContext(context);
        saveDynamicActions(context, stateContext, viewRoot);

        /*
         * Save the component state.
         */
        Object state = saveComponentState(context);

        /*
         * Save the tree structure.
         */
        List<TreeNode> treeList = new ArrayList<TreeNode>(32);
        captureChild(treeList, 0, viewRoot);
        Object[] tree = treeList.toArray();

        result = new Object[]{tree, state};
        StateContext.release(context);        
        return result;
    }

    /**
     * Inner class used to store a facet in the saved component tree.
     */
    private static final class FacetNode extends TreeNode {

        /**
         * Stores the serial version UID.
         */
        private static final long serialVersionUID = -3777170310958005106L;
        /**
         * Stores the facet name.
         */
        public String facetName;

        /**
         * Constructor.
         */
        public FacetNode() {
        }

        /**
         * Constructor.
         *
         * @param parent the parent.
         * @param name the facet name.
         * @param c the component.
         */
        public FacetNode(int parent, String name, UIComponent c) {

            super(parent, c);
            this.facetName = name;
        }

        /**
         * Read the facet node in.
         *
         * @param in the object input.
         * @throws IOException when an I/O error occurs.
         * @throws ClassNotFoundException when the class could not be found.
         */
        @Override
        public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {

            super.readExternal(in);
            this.facetName = in.readUTF();

        }

        /**
         * Write the facet node out.
         *
         * @param out the object output.
         * @throws IOException when an I/O error occurs.
         */
        @Override
        public void writeExternal(ObjectOutput out) throws IOException {

            super.writeExternal(out);
            out.writeUTF(this.facetName);

        }
    }

    /**
     * Inner class used to store a node in the saved component tree.
     */
    private static class TreeNode implements Externalizable {

        /**
         * Stores the serial version UID.
         */
        private static final long serialVersionUID = -835775352718473281L;
        /**
         * Stores the NULL_ID constant.
         */
        private static final String NULL_ID = "";
        /**
         * Stores the component type.
         */
        public String componentType;
        /**
         * Stores the id.
         */
        public String id;
        /**
         * Stores the parent.
         */
        public int parent;

        /**
         * Constructor.
         */
        public TreeNode() {
        }

        /**
         * Constructor.
         *
         * @param parent the parent.
         * @param c the component.
         */
        public TreeNode(int parent, UIComponent c) {

            this.parent = parent;
            this.id = c.getId();
            this.componentType = c.getClass().getName();

        }

        /**
         * Read the tree node in.
         *
         * @param in the object input.
         * @throws IOException when an I/O error occurs.
         * @throws ClassNotFoundException when the class could not be found.
         */
        public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {

            this.parent = in.readInt();
            this.componentType = in.readUTF();
            this.id = in.readUTF();
            if (id.length() == 0) {
                id = null;
            }
        }

        /**
         * Write the tree node out.
         *
         * @param out the object output.
         * @throws IOException when an I/O error occurs.
         */
        public void writeExternal(ObjectOutput out) throws IOException {

            out.writeInt(this.parent);
            out.writeUTF(this.componentType);
            if (this.id != null) {
                out.writeUTF(this.id);
            } else {
                out.writeUTF(NULL_ID);
            }
        }
    }

    /**
     * Helper method that determines what the index of the given child component
     * will be taking transient siblings into account.
     *
     * @param component the UI component.
     * @return the calculated index.
     */
    private int getProperChildIndex(UIComponent component) {
        int result = -1;

        if (component.getParent().getChildren().indexOf(component) != -1) {
            UIComponent parent = component.getParent();
            int index = 0;
            Iterator<UIComponent> iterator = parent.getChildren().iterator();
            while (iterator.hasNext()) {
                UIComponent child = iterator.next();
                if (child == component) {
                    break;
                } else {
                    if (!child.isTransient()) {
                        index++;
                    }
                }
            }
            result = index;
        }

        return result;
    }

    /**
     * Does the give component have a transient ancestor.
     *
     * @param component the UI component.
     * @return true if it has a transient ancestor, false otherwise.
     */
    private boolean hasTransientAncestor(UIComponent component) {
        UIComponent parent = component.getParent();
        while (parent != null) {
            if (parent.isTransient()) {
                return true;
            }
            parent = parent.getParent();
        }
        return false;
    }
}

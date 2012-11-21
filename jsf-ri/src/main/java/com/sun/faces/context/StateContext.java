/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2012 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.faces.context;

import com.sun.faces.RIConstants;
import com.sun.faces.application.ApplicationAssociate;
import com.sun.faces.application.ApplicationStateInfo;
import com.sun.faces.util.ComponentStruct;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.PostAddToViewEvent;
import javax.faces.event.PreRemoveFromViewEvent;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import static com.sun.faces.RIConstants.DYNAMIC_CHILD_COUNT;
import static com.sun.faces.RIConstants.DYNAMIC_COMPONENT;
import com.sun.faces.util.FacesLogger;
import java.util.logging.Logger;
import javax.faces.FacesException;

/**
 * Context for dealing with partial state saving mechanics.
 */
public class StateContext {


    private static final String KEY = StateContext.class.getName() + "_KEY";
    
    private boolean partial;
    private boolean partialLocked;
    private boolean trackMods = true;
    private AddRemoveListener modListener;
    private ApplicationStateInfo stateInfo;
    private WeakReference<UIViewRoot> viewRootRef = new WeakReference<UIViewRoot>(null);

    private static final Logger LOGGER = FacesLogger.CONTEXT.getLogger();

    // ------------------------------------------------------------ Constructors


    private StateContext(ApplicationStateInfo stateInfo) {

        this.stateInfo = stateInfo;

    }


    // ---------------------------------------------------------- Public Methods

    /**
     * Release the state context.
     * 
     * @param facesContext the Faces context.
     */
    public static void release(FacesContext facesContext) {
        StateContext stateContext = (StateContext) facesContext.getAttributes().get(KEY);
        UIViewRoot viewRoot = facesContext.getViewRoot();
        if (viewRoot != null && stateContext.modListener != null) {
            viewRoot.unsubscribeFromViewEvent(PostAddToViewEvent.class, stateContext.modListener);
            viewRoot.unsubscribeFromViewEvent(PreRemoveFromViewEvent.class, stateContext.modListener);
        }
        facesContext.getAttributes().remove(KEY);
    }

    /**
     * @param ctx the <code>FacesContext</code> for the current request
     * @return <code>StateContext</code> for this request
     */
    public static StateContext getStateContext(FacesContext ctx) {

        StateContext stateCtx = (StateContext) ctx.getAttributes().get(KEY);
        if (stateCtx == null) {
            ApplicationAssociate associate = ApplicationAssociate.getCurrentInstance();
            ApplicationStateInfo info = associate.getApplicationStateInfo();
            stateCtx = new StateContext(info);
            ctx.getAttributes().put(KEY, stateCtx);
        }
        return stateCtx;

    }

    /**
     * @param ctx FacesContext.
     * @param viewId the view ID to check or null if viewId is unknown.
     * @return <code>true</code> if partial state saving should be used for the
     *  specified view ID, otherwise <code>false</code>
     */
    public boolean isPartialStateSaving(FacesContext ctx, String viewId) {
        // track UIViewRoot changes
        UIViewRoot root = ctx.getViewRoot();
        UIViewRoot refRoot = viewRootRef.get();
        if (root != refRoot) {
          // set weak reference to current viewRoot
          this.viewRootRef = new WeakReference<UIViewRoot>(root);

          // On first call in restore phase, viewRoot is null, so we treat the first 
          // change to not null not as a changing viewRoot.
          if (refRoot != null) {
            // view root changed in request processing - force usage of a 
            // new AddRemoveListener instance for the new viewId ...
            modListener = null;
            // ... and also force check for partial state saving for the new viewId
            partialLocked = false;
          }
        }

        if (!partialLocked) {
                  if (viewId == null) {
                          if (root != null) {
                                  viewId = root.getViewId();
                          } else {
                                  // View root has not yet been initialized.  Check to see whether
                                  // the target view id has been stashed away for us.
                                  viewId = (String)ctx.getAttributes().get(RIConstants.VIEWID_KEY_NAME);
                                }
                  }
        
                  partial = stateInfo.usePartialStateSaving(viewId);
                  partialLocked = true;
        }
        return partial;

    }


    /**
     * @return <code>true</code> if view modifications outside of the initial
     *  construction of the view are being tracked.
     */
    public boolean trackViewModifications() {

        return trackMods;

    }


    /**
     * Installs a <code>SystemEventListener</code> on the <code>UIViewRoot</code>
     * to track components added to or removed from the view.
     */
    public void startTrackViewModifications(FacesContext ctx, UIViewRoot root) {
        if (modListener == null) {
            if (root != null) {
                modListener = new AddRemoveListener(ctx);
                root.subscribeToViewEvent(PostAddToViewEvent.class, modListener);
                root.subscribeToViewEvent(PreRemoveFromViewEvent.class, modListener);
            } else {
                LOGGER.warning("Unable to attach AddRemoveListener to UIViewRoot because it is null");
            }
        }
        setTrackViewModifications(true);        
    }


    /**
     * Toggles the current modification tracking status.
     * 
     * @param trackMods if <code>true</code> and the listener installed by 
     * {@link #startTrackViewModifications(javax.faces.context.FacesContext, javax.faces.component.UIViewRoot)}
     * is present, then view modifications will be tracked.  If 
     * <code>false</code>, then modification events will be ignored.
     */
    public void setTrackViewModifications(boolean trackMods) {

        this.trackMods = trackMods;

    }


    /**
     * @param c the UIComponent to check
     * @return <code>true</code> if the component was added after the initial
     *  view construction
     */
    public boolean componentAddedDynamically(UIComponent c) {
        return c.getAttributes().containsKey(DYNAMIC_COMPONENT);
    }

    public int getIndexOfDynamicallyAddedChildInParent(UIComponent c) {
        int result = -1;
        Map<String, Object> attrs = c.getAttributes();
        if (attrs.containsKey(DYNAMIC_COMPONENT)) {
            result = (Integer) attrs.get(DYNAMIC_COMPONENT);
        }
        return result;
    }

    public boolean hasOneOrMoreDynamicChild(UIComponent parent) {
        return parent.getAttributes().containsKey(DYNAMIC_CHILD_COUNT);
    }

    private int incrementDynamicChildCount(UIComponent parent) {
        int result;
        Map<String, Object> attrs = parent.getAttributes();
        Integer cur = (Integer) attrs.get(DYNAMIC_CHILD_COUNT);
        if (null != cur) {
            result = cur++;
        } else {
            result = 1;
        }
        attrs.put(DYNAMIC_CHILD_COUNT, (Integer) result);

        return result;
    }

    private int decrementDynamicChildCount(UIComponent parent) {
        int result = 0;
        Map<String, Object> attrs = parent.getAttributes();
        Integer cur = (Integer) attrs.get(DYNAMIC_CHILD_COUNT);
        if (null != cur) {
            result = (0 < cur) ? cur-- : 0;

        }
        if (0 == result && null != cur){
            attrs.remove(DYNAMIC_CHILD_COUNT);
        }

        return result;
    }

    /**
     * Get the dynamic list (of adds and removes).
     */
    public List<ComponentStruct> getDynamicActions() {
        return ((modListener != null) ? modListener.getDynamicActions() : null);
    }

    /**
     * Get the hash map of dynamic components.
     * 
     * @return the hash map of dynamic components.
     */
    public HashMap<String, UIComponent> getDynamicComponents() {
        return ((modListener != null) ? modListener.getDynamicComponents() : null);
    }

    // ---------------------------------------------------------- Nested Classes


    /**
     * A system event listener which is used to listen for changes on the 
     * component tree after restore view and before rendering out the view.
     */
    public class AddRemoveListener implements SystemEventListener {

        /**
         * Stores the state context we work for,
         */
        private StateContext stateCtx;
        /**
         * Stores the list of adds/removes.
         */
        private List<ComponentStruct> dynamicActions;
        /**
         * Stores the hash map of dynamic components.
         */
        private transient HashMap<String, UIComponent> dynamicComponents;

        /**
         * Constructor.
         * 
         * @param context the Faces context. 
         */
        public AddRemoveListener(FacesContext context) {
            stateCtx = StateContext.getStateContext(context);
        }

        /**
         * Get the list of adds/removes.
         * 
         * @return the list of adds/removes.
         */
        public List<ComponentStruct> getDynamicActions() {
            synchronized(this) {
                if (dynamicActions == null) {
                    dynamicActions = new ArrayList<ComponentStruct>();
                }
            }
            return dynamicActions;
        }

        /**
         * Get the hash map of dynamic components.
         * 
         * @return the hash map of dynamic components.
         */
        public HashMap<String, UIComponent> getDynamicComponents() {
            synchronized(this) {
                if (dynamicComponents == null) {
                    dynamicComponents = new HashMap<String, UIComponent>();
                }
            }
            return dynamicComponents;
        }
        
        /**
         * Process the add/remove event.
         * 
         * @param event the add/remove event.
         * @throws AbortProcessingException when processing should be aborted.
         */
        public void processEvent(SystemEvent event)
              throws AbortProcessingException {
            FacesContext ctx = FacesContext.getCurrentInstance();
            if (event instanceof PreRemoveFromViewEvent) {
                if (stateCtx.trackViewModifications()) {
                    handleRemove(ctx, ((PreRemoveFromViewEvent) event).getComponent());
                }
            } else {
                if (stateCtx.trackViewModifications()) {
                    handleAdd(ctx, ((PostAddToViewEvent) event).getComponent());
                }
            }
        }

        /**
         * Are we listening for these particular changes.
         * 
         * <p>
         *  Note we are only interested in UIComponent adds/removes that are
         *  not the UIViewRoot itself.
         * </p>
         * 
         * @param source the source object we might be listening for.
         * @return true if the source is OK, false otherwise.
         */
        public boolean isListenerForSource(Object source) {
            return (source instanceof UIComponent && !(source instanceof UIViewRoot));
        }
        
        /**
         * Handle the remove.
         * 
         * @param context the Faces context.
         * @param component the UI component to add to the list as a REMOVE.
         */
        private void handleRemove(FacesContext context, UIComponent component) {
            if (component.isInView()) {
                decrementDynamicChildCount(component.getParent());
                ComponentStruct struct = new ComponentStruct();
                struct.action = ComponentStruct.REMOVE;
                struct.clientId = component.getClientId(context);
                struct.id = component.getId();
                handleAddRemoveWithAutoPrune(component, struct);
            }            
        }

        /**
         * Handle the add.
         * 
         * @param context the Faces context.
         * @param component the UI component to add to the list as an ADD.
         */
        private void handleAdd(FacesContext context, UIComponent component) {
            if (component.getParent() != null && component.getParent().isInView()) {
                String id = component.getId();

                /*
                * Since adding a component, can mean you are really reparenting 
                * it, we need to make sure the OLD clientId is not cached, we do 
                * that by setting the id.
                */
                if (id != null) {
                    component.setId(id);
                }

                if (component.getParent().getFacets().containsValue(component)) {
                    Map facets = component.getParent().getFacets();
                    Iterator entries = facets.entrySet().iterator();
                    while (entries.hasNext()) {
                        Map.Entry entry = (Map.Entry) entries.next();
                        if (entry.getValue() == component) {
                            incrementDynamicChildCount(component.getParent());
                            component.clearInitialState();
                            component.getAttributes().put(DYNAMIC_COMPONENT, component.getParent().getChildren().indexOf(component));
                            ComponentStruct struct = new ComponentStruct();
                            struct.action = ComponentStruct.ADD;
                            struct.facetName = entry.getKey().toString();
                            struct.parentClientId = component.getParent().getClientId(context);
                            struct.clientId = component.getClientId(context);
                            struct.id = component.getId();
                            handleAddRemoveWithAutoPrune(component, struct);
                        }
                    }
                }
                else {
                    incrementDynamicChildCount(component.getParent());
                    component.clearInitialState();
                    component.getAttributes().put(DYNAMIC_COMPONENT, component.getParent().getChildren().indexOf(component));
                    ComponentStruct struct = new ComponentStruct();
                    struct.action = ComponentStruct.ADD;
                    struct.parentClientId = component.getParent().getClientId(context);
                    struct.clientId = component.getClientId(context);
                    struct.id = component.getId();
                    handleAddRemoveWithAutoPrune(component, struct);
                }
            }
        }
        
        /**
         * Methods that takes care of pruning and adding an action to the 
         * dynamic action list.
         *
         * <pre>
         *  If you add a component and the dynamic action list does not contain
         *  the component yet then add it to the dynamic action list, regardless
         *  whether or not if was an ADD or REMOVE.
         * </pre>
         * 
         * <pre>
         *  Else if you add a component and it is already in the dynamic action 
         *  list and it is the only action for that client id in the dynamic 
         *  action list then:
         *   1) If the previous action was an ADD then
         *      a) If the current action is a REMOVE then remove the component 
         *         out of the dynamic action list.
         *      b) If the current action is an ADD then throw a FacesException.
         *   2) If the previous action was a REMOVE then
         *      a) If the current action is an ADD then add it to the dynamic
         *         action list.
         *      b) If the current action is a REMOVE then throw a FacesException.
         * </pre>
         * 
         * <pre>
         *  Else if a REMOVE and ADD where captured before then:
         *   1) If the current action is REMOVE then remove the last dynamic 
         *      action out of the dynamic action list.
         *   2) If the current action is ADD then throw a FacesException.
         * </pre>
         * 
         * @param component the UI component.
         * @param struct the dynamic action.
         */
        private void handleAddRemoveWithAutoPrune(UIComponent component, ComponentStruct struct) {

            List<ComponentStruct> actionList = getDynamicActions();
            HashMap<String, UIComponent> componentMap = getDynamicComponents();
            
            int firstIndex = actionList.indexOf(struct);
            if (firstIndex == -1) {
                actionList.add(struct);
                componentMap.put(struct.clientId, component);
            } else {
                int lastIndex = actionList.lastIndexOf(struct);
                if (lastIndex == firstIndex) {
                    ComponentStruct previousStruct = actionList.get(firstIndex);
                    if (ComponentStruct.ADD.equals(previousStruct.action)) {
                        if (ComponentStruct.ADD.equals(struct.action)) {
                            throw new FacesException("Cannot add the same component twice: " + struct.clientId);
                        }
                        if (ComponentStruct.REMOVE.equals(struct.action)) {
                            actionList.remove(firstIndex);
                            componentMap.remove(struct.clientId);
                        }
                    }
                    if (ComponentStruct.REMOVE.equals(previousStruct.action)) {
                        if (ComponentStruct.ADD.equals(struct.action)) {
                            actionList.add(struct);
                            componentMap.put(struct.clientId, component);                            
                        }
                        if (ComponentStruct.REMOVE.equals(struct.action)) {
                            throw new FacesException("Cannot remove the same component twice: " + struct.clientId);
                        }
                    }
                } else {
                    if (ComponentStruct.ADD.equals(struct.action)) {
                        throw new FacesException("Cannot add the same component twice: " + struct.clientId);
                    }
                    if (ComponentStruct.REMOVE.equals(struct.action)) {
                        actionList.remove(lastIndex);
                    }
                }
            }
        }

    } // END AddRemoveListener

} // END StateContext

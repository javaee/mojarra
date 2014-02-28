/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2010-2011 Oracle and/or its affiliates. All rights reserved.
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * Context for dealing with partial state saving mechanics.
 */
public class StateContext {


    private static final String KEY = StateContext.class.getName() + "_KEY";
    private static final String DYNAMIC_COMPONENT =
            StateContext.class.getName() + "_DYNAMIC_COMPONENT";
    private static final String HAS_ONE_OR_MORE_DYNAMIC_CHILD =
            StateContext.class.getName() + "_HAS_ONE_OR_MORE_DYNAMIC_CHILD";


    private boolean partial;
    private boolean partialLocked;
    private boolean trackMods = true;
    private AddRemoveListener modListener;
    private ApplicationStateInfo stateInfo;
    private WeakReference<UIViewRoot> viewRootRef = new WeakReference<UIViewRoot>(null);


    // ------------------------------------------------------------ Constructors


    private StateContext(ApplicationStateInfo stateInfo) {

        this.stateInfo = stateInfo;

    }


    // ---------------------------------------------------------- Public Methods


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
     * @param current FacesContext.
     * @param viewId the view ID to check or null if viewId is unknown.
     * @return <code>true</code> if partial state saving should be used for the
     *  specified view ID, otherwise <code>false</code>
     */
    public boolean partialStateSaving(FacesContext ctx, String viewId) {
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
    public void startTrackViewModifications() {

        if (modListener == null) {
            FacesContext ctx = FacesContext.getCurrentInstance();
            modListener = new AddRemoveListener(ctx);
            UIViewRoot root = ctx.getViewRoot();
            root.subscribeToViewEvent(PostAddToViewEvent.class, modListener);
            root.subscribeToViewEvent(PreRemoveFromViewEvent.class, modListener);
        }
        setTrackViewModifications(true);
        
    }


    /**
     * Toggles the current modification tracking status.
     * @param trackMods if <code>true</code> and the listener installed by
     *  {@link #startTrackViewModifications()} is present, then view modifications
     *  will be tracked.  If <code>false</code>, then modification events
     *  will be ignored.
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
        return parent.getAttributes().containsKey(HAS_ONE_OR_MORE_DYNAMIC_CHILD);
    }

    private int incrementDynamicChildCount(UIComponent parent) {
        int result = 0;
        Map<String, Object> attrs = parent.getAttributes();
        Integer cur = (Integer) attrs.get(HAS_ONE_OR_MORE_DYNAMIC_CHILD);
        if (null != cur) {
            result = cur++;
        } else {
            result = 1;
        }
        attrs.put(HAS_ONE_OR_MORE_DYNAMIC_CHILD, (Integer) result);

        return result;
    }

    private int decrementDynamicChildCount(UIComponent parent) {
        int result = 0;
        Map<String, Object> attrs = parent.getAttributes();
        Integer cur = (Integer) attrs.get(HAS_ONE_OR_MORE_DYNAMIC_CHILD);
        if (null != cur) {
            result = (0 < cur) ? cur-- : 0;

        }
        if (0 == result && null != cur){
            attrs.remove(HAS_ONE_OR_MORE_DYNAMIC_CHILD);
        }

        return result;
    }

    /**
     * @return a <code>Map</code> containing information about all components
     *  added after the initial view construction.
     */
    public Map<String,ComponentStruct> getDynamicAdds() {

        return ((modListener != null) ? modListener.getDynamicAdds() : null);

    }


    /**
     * @return a <code>List</code> of client IDs that have been removed after
     *  the initial view construction.
     */
    public List<String> getDynamicRemoves() {

        return ((modListener != null) ? modListener.dynamicRemoves : null);

    }


    // ---------------------------------------------------------- Nested Classes


    public class AddRemoveListener implements SystemEventListener {

        private StateContext stateCtx;
        private LinkedHashMap<String, ComponentStruct> dynamicAdds;
        private List<String> dynamicRemoves;


        // -------------------------------------------------------- Constructors


        public AddRemoveListener(FacesContext ctx) {
            stateCtx = StateContext.getStateContext(ctx);
        }

        // -------------------------------------------------------- Getters

        public Map<String, ComponentStruct> getDynamicAdds() {
            if (null == dynamicAdds) {
                dynamicAdds = new LinkedHashMap<String, ComponentStruct>();
            }

            return dynamicAdds;
        }

        // ------------------------------------ Methods From SystemEventListener


        public void processEvent(SystemEvent event)
              throws AbortProcessingException {
            FacesContext ctx = FacesContext.getCurrentInstance();
            if (event instanceof PreRemoveFromViewEvent) {
                if (stateCtx.trackViewModifications()) {
                    handleRemoveEvent(ctx, (PreRemoveFromViewEvent) event);
                }
            } else {
                if (stateCtx.trackViewModifications()) {
                    handleAddEvent(ctx, (PostAddToViewEvent) event);
                }
            }
        }

        public boolean isListenerForSource(Object source) {
            return (source instanceof UIComponent);
        }


        // ----------------------------------------------------- Private Methods


        private void handleRemoveEvent(FacesContext context,
                                       PreRemoveFromViewEvent event) {

            UIComponent removed = event.getComponent();
            if (removed.isTransient()) {
                return;
            }
            if (dynamicRemoves == null) {
                dynamicRemoves = new ArrayList<String>();
            }
            String clientId = event.getComponent().getClientId(context);
            if (dynamicAdds != null && dynamicAdds.containsKey(clientId)) {
                dynamicAdds.remove(clientId);
            }
            StateContext.this.decrementDynamicChildCount(removed.getParent());
            dynamicRemoves.add(clientId);
        }


        private void handleAddEvent(FacesContext context,
                                    PostAddToViewEvent event) {

            // if the root is transient, then no action is ever needed here
            if (context.getViewRoot().isTransient()) {
                return;
            }

            UIComponent added = event.getComponent();
            if (added.isTransient() || added instanceof UIViewRoot) {
                return;
            }

            // this component, while not transient may be a child or facet
            // of component that is.  We'll have to search the parent hierarchy
            // to the root to confirm.
            UIComponent parent = added.getParent();
            while (parent != null) {
                if (parent.isTransient()) {
                    return;
                }
                parent = parent.getParent();
            }

            parent = added.getParent();
            StateContext.this.incrementDynamicChildCount(parent);
            ComponentStruct toAdd = new ComponentStruct();
            toAdd.absorbComponent(context, added);

            if (dynamicRemoves != null) {
                dynamicRemoves.remove(toAdd.clientId);
            }
            added.getAttributes().put(DYNAMIC_COMPONENT, new Integer(toAdd.indexOfChildInParent));
            getDynamicAdds().put(toAdd.clientId, toAdd);

        }

    } // END AddRemoveListener

} // END StateContext

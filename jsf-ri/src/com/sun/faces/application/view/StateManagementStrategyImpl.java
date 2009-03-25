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

package com.sun.faces.application.view;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.component.visit.VisitResult;
import javax.faces.context.FacesContext;
import javax.faces.render.ResponseStateManager;

import com.sun.faces.io.FastStringWriter;
import com.sun.faces.renderkit.RenderKitUtils;
import com.sun.faces.util.DebugUtil;
import com.sun.faces.util.FacesLogger;
import com.sun.faces.util.MessageUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.io.IOException;
import javax.faces.application.StateManager;
import javax.faces.component.ContextCallback;
import javax.faces.component.StateHolder;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.event.*;
import javax.faces.webapp.pdl.StateManagementStrategy;
import javax.faces.webapp.pdl.PageDeclarationLanguage;
import javax.faces.FacesException;

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

    private final FaceletViewHandlingStrategy pdl;

    private static final String CLIENTIDS_TO_REMOVE_NAME = 
            "com.sun.faces.application.view.CLIENTIDS_TO_REMOVE";
    private static final String CLIENTIDS_TO_ADD_NAME = 
            "com.sun.faces.application.view.CLIENTIDS_TO_ADD";
    private static final String IGNORE_REMOVE_EVENT_NAME = 
            "com.sun.faces.application.view.IGNORE_REMOVE_EVENT";
    private static final String DYNAMIC_COMPONENT =
            "com.sun.faces.application.view.DYNAMIC_COMPONENT";

    private Set<String> fullStateViewIds;

    // ------------------------------------------------------------ Constructors


    /**
     * Create a new <code>StateManagerImpl</code> instance.
     */
    public StateManagementStrategyImpl(FaceletViewHandlingStrategy pdl,
                                       Set<String> fullStateViewIds) {
        this.pdl = pdl;
        this.fullStateViewIds = fullStateViewIds;
    }

    private List<String> getClientIdsToRemove(FacesContext context) {
        return this.getClientIdsToRemove(context, false);
    }

    private List<String> getClientIdsToRemove(FacesContext context, boolean create) {
        List<String> result = null;
        if ((null == (result = (List<String>) context.getAttributes().get(CLIENTIDS_TO_REMOVE_NAME))) && create) {
            result = new ArrayList<String>();
            context.getAttributes().put(CLIENTIDS_TO_REMOVE_NAME, result);
        }
        return result;
    }
    
    private class ComponentStruct implements StateHolder {
        String parentClientId;
        String clientId;
        int indexOfChildInParent = -1;
        String facetName;

        public boolean isTransient() {
            return false;
        }

        public void restoreState(FacesContext ctx, Object state) {
            Object s[] = (Object []) state;
            this.parentClientId = s[0].toString();
            this.clientId = s[1].toString();
            this.indexOfChildInParent = (Integer) s[2];
            this.facetName = (String) s[3];
        }

        public Object saveState(FacesContext arg0) {
            Object state[] = new Object[4];
            state[0] = this.parentClientId;
            state[1] = this.clientId;
            state[2] = this.indexOfChildInParent;
            state[3] = this.facetName;
            return state;
        }

        public void setTransient(boolean arg0) {
        }
        
        
    }
    
    private Map<String,ComponentStruct> getClientIdsToAdd(FacesContext context) {
        return this.getClientIdsToAdd(context, false);
    }

    private Map<String,ComponentStruct> getClientIdsToAdd(FacesContext context, boolean create) {
        Map<String,ComponentStruct> result = null;
        if ((null == (result = (Map<String,ComponentStruct>) context.getAttributes().get(CLIENTIDS_TO_ADD_NAME))) && create) {
            result = new HashMap<String,ComponentStruct>();
            context.getAttributes().put(CLIENTIDS_TO_ADD_NAME, result);
        }
        return result;
    }
    
    private void handleRemoveEvent(PreRemoveFromViewEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        List<String> idsToRemove = getClientIdsToRemove(context, true);
        idsToRemove.add(event.getComponent().getClientId(context));
    }
    
    private void handleAddEvent(PostAddToViewEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String,ComponentStruct> idsToAdd = getClientIdsToAdd(context, true);
        ComponentStruct toAdd = new ComponentStruct();
        UIComponent 
                parent,
                added = event.getComponent();
        toAdd.clientId = added.getClientId(context);
        toAdd.parentClientId = (parent = added.getParent()).getClientId(context);
        // this needs work
        int idx = parent.getChildren().indexOf(added);
        if (idx == -1) {
            // this must be a facet
            for (Map.Entry<String,UIComponent> facet : parent.getFacets().entrySet()) {
                if (facet.getValue() == added) {
                    toAdd.facetName = facet.getKey();
                    break;
                }
            }
        } else {
            toAdd.indexOfChildInParent = parent.getChildren().indexOf(added);
        }
        added.getAttributes().put(DYNAMIC_COMPONENT, Boolean.TRUE);
        idsToAdd.put(toAdd.clientId, toAdd);
    
    }
    
    private boolean isIgnoreRemoveEvent(FacesContext context) {
        boolean result = false;
        result = context.getAttributes().containsKey(IGNORE_REMOVE_EVENT_NAME);

        return result;
    }

    private void setIgnoreRemoveEvent(FacesContext context, boolean ignoreRemoveEvent) {

        if (!ignoreRemoveEvent) {
            context.getAttributes().remove(IGNORE_REMOVE_EVENT_NAME);
        } else {
            context.getAttributes().put(IGNORE_REMOVE_EVENT_NAME, Boolean.TRUE);
        }
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
        checkIdUniqueness(context,
                          viewRoot,
                          new HashSet<String>(viewRoot.getChildCount() << 1));
        final Map<String,Object> stateMap = new HashMap<String,Object>();
        
        VisitContext visitContext = VisitContext.createVisitContext(context);
        final FacesContext finalContext = context;
        viewRoot.visitTree(visitContext, new VisitCallback() {

            public VisitResult visit(VisitContext context, UIComponent target) {
                VisitResult result = VisitResult.ACCEPT;
                Object stateObj = null;
                if (!target.isTransient()) {
                    if (target.getAttributes().containsKey(DYNAMIC_COMPONENT)) {
                        stateObj = new StateHolderSaver(finalContext, target);
                    } else {
                        stateObj = target.saveState(context.getFacesContext());
                    }
                    if (null != stateObj) {
                        stateMap.put(target.getClientId(context.getFacesContext()),
                                                        stateObj);
                    }
                } else {
                    return result;
                }
                
                return result;
            }
            
        });
        
        // handle dynamic adds/removes
        List<String> removeList = getClientIdsToRemove(context);
        if (null != removeList && !removeList.isEmpty()) {
            stateMap.put(CLIENTIDS_TO_REMOVE_NAME, removeList);
        }
        Map<String,ComponentStruct> addList = getClientIdsToAdd(context);
        if (null != addList && !addList.isEmpty()) {
            List<Object> savedAddList = new ArrayList<Object>(addList.size());
            for (ComponentStruct s : addList.values()) {
                savedAddList.add(s.saveState(context));
            }
            stateMap.put(CLIENTIDS_TO_ADD_NAME, savedAddList.toArray());
        }
        //return stateMap;
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
        
        // Build the tree to initial state
        UIViewRoot viewRoot;

        try {
            viewRoot = pdl.getViewMetadata(context, viewId).createMetadataView(context);
            context.setViewRoot(viewRoot);
            pdl.buildView(context, viewRoot);
        } catch (IOException ioe) {
            throw new FacesException(ioe);
        }
        Object[] rawState = (Object[]) rsm.getState(context, viewId);
        final Map<String, Object> state = (Map<String,Object>) rawState[1];

        if (null != state) {
            // We need to clone the tree, otherwise we run the risk
            // of being left in a state where the restored
            // UIComponent instances are in the session instead
            // of the TreeNode instances.  This is a problem
            // for servers that persist session data since
            // UIComponent instances are not serializable.
            VisitContext visitContext = VisitContext.createVisitContext(context);
            viewRoot.visitTree(visitContext, new VisitCallback() {

                public VisitResult visit(VisitContext context, UIComponent target) {
                    VisitResult result = VisitResult.ACCEPT;
                    Object stateObj = state.get(target.getClientId(context.getFacesContext()));
                    if (stateObj != null && !target.getAttributes().containsKey(DYNAMIC_COMPONENT)) {
                        target.restoreState(context.getFacesContext(),
                                stateObj);
                    }

                    return result;
                }

            });
            
            // Handle dynamic add/removes
            
            List<String> removeList = (List<String>) state.get(CLIENTIDS_TO_REMOVE_NAME);
            if (null != removeList && !removeList.isEmpty()) {
                for (String cur : removeList) {
                    setIgnoreRemoveEvent(context, true);
                    viewRoot.invokeOnComponent(context, cur, new ContextCallback() {

                        public void invokeContextCallback(FacesContext context, UIComponent target) {
                            UIComponent parent = target.getParent();
                            if (null != parent) {
                                parent.getChildren().remove(target);
                            }
                        }
                        
                    });
                    setIgnoreRemoveEvent(context, false);
                }
            }
            Object restoredAddList[] = (Object []) state.get(CLIENTIDS_TO_ADD_NAME);
            if (null != restoredAddList && 0 < restoredAddList.length) {  
                // Restore the list of added components
                List<ComponentStruct> addList = new ArrayList<ComponentStruct>(restoredAddList.length);
                for (int i = 0; i < restoredAddList.length; i++) {
                    ComponentStruct cur = new ComponentStruct();
                    cur.restoreState(context, restoredAddList[i]);
                    addList.add(cur);
                }
                // restore the components themselves
                for (ComponentStruct cur : addList) {
                    final ComponentStruct finalCur = cur;
                    // Find the parent
                    viewRoot.invokeOnComponent(context, finalCur.parentClientId,
                            new ContextCallback() {
                        public void invokeContextCallback(FacesContext context, UIComponent parent) {
                            // Create the child
                            StateHolderSaver saver = (StateHolderSaver) state.get(finalCur.clientId);
                            UIComponent toAdd = (UIComponent) saver.restore(context);
                            int idx = finalCur.indexOfChildInParent;
                            if (idx == -1) {
                                // add facet to the parent
                                parent.getFacets().put(finalCur.facetName, toAdd);
                            } else {
                                // add the child to the parent at correct index
                                parent.getChildren().add(finalCur.indexOfChildInParent, toAdd);
                            }
                        }
                    });
                }
            }          
        } else {
            viewRoot = null;
        }

        return viewRoot;

    }


    // ---------------------------------------------------------- Public Methods


    public void notifyTrackChanges(UIViewRoot root) {

        SystemEventListener listener = new AddRemoveListener(this);
        root.subscribeToViewEvent(PostAddToViewEvent.class, listener);
        root.subscribeToViewEvent(PreRemoveFromViewEvent.class, listener);

    }




    // ------------------------------------------------------- Protected Methods

    // RELEASE_PENDING (this is duplicated in StateManagerImpl...
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


                    FastStringWriter writer = new FastStringWriter(128);
                    DebugUtil.simplePrintTree(context.getViewRoot(), id, writer);
                    LOGGER.severe(writer.toString());
                }

                String message =
                      MessageUtils.getExceptionMessageString(
                            MessageUtils.DUPLICATE_COMPONENT_ID_ERROR_ID, id);
                throw new IllegalStateException(message);
            }
        }

    }


    
    public static class AddRemoveListener implements SystemEventListener {

        private StateManagementStrategyImpl owner;
        
        public AddRemoveListener(StateManagementStrategyImpl owner) {
            this.owner = owner;
        }

        public void processEvent(SystemEvent event)
              throws AbortProcessingException {
            FacesContext context = FacesContext.getCurrentInstance();
            if (event instanceof PreRemoveFromViewEvent) {
                if (!owner.isIgnoreRemoveEvent(context)) {
                    owner.handleRemoveEvent((PreRemoveFromViewEvent) event);
                }
            } else {
                owner.handleAddEvent((PostAddToViewEvent) event);
            }
        }

        public boolean isListenerForSource(Object source) {
            return (source instanceof UIComponent);
        }
        
    }

} 

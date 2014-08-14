/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2010 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.faces.facelets.tag.composite;

import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.component.ContextCallback;
import javax.faces.component.UIComponent;
import javax.faces.component.behavior.ClientBehavior;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.event.*;
import javax.faces.render.Renderer;
import java.io.IOException;
import java.util.*;

/**
 * <p class="changed_added_2_0"></p>
 */
public class BehaviorHolderWrapper extends UIComponent implements
      ClientBehaviorHolder {

    private final UIComponent parent;
    private final String virtualEvent;
    private final String event;

    public BehaviorHolderWrapper(UIComponent parent, String virtualEvent, String event) {
        this.parent = parent;
        this.virtualEvent = virtualEvent;
        this.event = event;
    }

    /**
     * @see javax.faces.component.UIComponent#broadcast(javax.faces.event.FacesEvent)
     */
    public void broadcast(FacesEvent event) throws AbortProcessingException {
        parent.broadcast(event);
    }

    /**
     * @see javax.faces.component.UIComponent#decode(javax.faces.context.FacesContext)
     */
    public void decode(FacesContext context) {
        parent.decode(context);
    }

    /**
     * @see javax.faces.component.UIComponent#encodeBegin(javax.faces.context.FacesContext)
     */
    public void encodeBegin(FacesContext context) throws IOException {
        parent.encodeBegin(context);
    }

    /**
     * @see javax.faces.component.UIComponent#encodeChildren(javax.faces.context.FacesContext)
     */
    public void encodeChildren(FacesContext context) throws IOException {
        parent.encodeChildren(context);
    }

    /**
     * @see javax.faces.component.UIComponent#encodeEnd(javax.faces.context.FacesContext)
     */
    public void encodeEnd(FacesContext context) throws IOException {
        parent.encodeEnd(context);
    }

    /**
     * @see javax.faces.component.UIComponent#findComponent(java.lang.String)
     */
    public UIComponent findComponent(String expr) {
        return parent.findComponent(expr);
    }

    /**
     * @see javax.faces.component.UIComponent#getAttributes()
     */
    public Map<String, Object> getAttributes() {
        return parent.getAttributes();
    }

    @Override
    public Map<String, Object> getPassThroughAttributes(boolean create) {
        return parent.getPassThroughAttributes(create);
    }

    /**
     * @see javax.faces.component.UIComponent#getChildCount()
     */
    public int getChildCount() {
        return parent.getChildCount();
    }

    /**
     * @see javax.faces.component.UIComponent#getChildren()
     */
    public List<UIComponent> getChildren() {
        return parent.getChildren();
    }

    /**
     * @see javax.faces.component.UIComponent#getClientId(javax.faces.context.FacesContext)
     */
    public String getClientId(FacesContext context) {
        return parent.getClientId(context);
    }

    /**
     * @see javax.faces.component.UIComponent#getFacet(java.lang.String)
     */
    public UIComponent getFacet(String name) {
        return parent.getFacet(name);
    }

    /**
     * @see javax.faces.component.UIComponent#getFacets()
     */
    public Map<String, UIComponent> getFacets() {
        return parent.getFacets();
    }

    /**
     * @see javax.faces.component.UIComponent#getFacetsAndChildren()
     */
    public Iterator<UIComponent> getFacetsAndChildren() {
        return parent.getFacetsAndChildren();
    }

    /**
     * @see javax.faces.component.UIComponent#getFamily()
     */
    public String getFamily() {
        return parent.getFamily();
    }

    /**
     * @see javax.faces.component.UIComponent#getId()
     */
    public String getId() {
        return parent.getId();
    }

    /**
     * @see javax.faces.component.UIComponent#getParent()
     */
    public UIComponent getParent() {
        return parent.getParent();
    }

    /**
     * @see javax.faces.component.UIComponent#getRendererType()
     */
    public String getRendererType() {
        return parent.getRendererType();
    }

    /**
     * @see javax.faces.component.UIComponent#getRendersChildren()
     */
    public boolean getRendersChildren() {
        return parent.getRendersChildren();
    }

    /**
     * @see javax.faces.component.UIComponent#getValueBinding(java.lang.String)
     * @deprecated
     */
    public ValueBinding getValueBinding(String name) {
        return parent.getValueBinding(name);
    }

    /**
     * @see javax.faces.component.UIComponent#getValueExpression(java.lang.String)
     */
    public ValueExpression getValueExpression(String name) {
        return parent.getValueExpression(name);
    }

    /**
     * @see javax.faces.component.UIComponent#invokeOnComponent(javax.faces.context.FacesContext, java.lang.String, javax.faces.component.ContextCallback)
     */
    public boolean invokeOnComponent(FacesContext context,
                                     String clientId,
                                     ContextCallback callback)
    throws FacesException {
        return parent.invokeOnComponent(context, clientId, callback);
    }

    /**
     * @see javax.faces.component.UIComponent#isInView()
     */
    public boolean isInView() {
        return parent.isInView();
    }

    /**
     * @see javax.faces.component.UIComponent#isRendered()
     */
    public boolean isRendered() {
        return parent.isRendered();
    }

    /**
     * @see javax.faces.component.StateHolder#isTransient()
     */
    public boolean isTransient() {
        return parent.isTransient();
    }

    /**
     * @see javax.faces.component.UIComponent#processDecodes(javax.faces.context.FacesContext)
     */
    public void processDecodes(FacesContext context) {
        parent.processDecodes(context);
    }

    /**
     * @see javax.faces.component.UIComponent#processEvent(javax.faces.event.ComponentSystemEvent)
     */
    public void processEvent(ComponentSystemEvent event)
          throws AbortProcessingException {
        parent.processEvent(event);
    }

    /**
     * @see javax.faces.component.UIComponent#processRestoreState(javax.faces.context.FacesContext, java.lang.Object)
     */
    public void processRestoreState(FacesContext context, Object state) {
        parent.processRestoreState(context, state);
    }

    /**
     * @see javax.faces.component.UIComponent#processSaveState(javax.faces.context.FacesContext)
     */
    public Object processSaveState(FacesContext context) {
        return parent.processSaveState(context);
    }

    /**
     * @see javax.faces.component.UIComponent#processUpdates(javax.faces.context.FacesContext)
     */
    public void processUpdates(FacesContext context) {
        parent.processUpdates(context);
    }

    /**
     * @see javax.faces.component.UIComponent#processValidators(javax.faces.context.FacesContext)
     */
    public void processValidators(FacesContext context) {
        parent.processValidators(context);
    }

    /**
     * @see javax.faces.component.UIComponent#queueEvent(javax.faces.event.FacesEvent)
     */
    public void queueEvent(FacesEvent event) {
        parent.queueEvent(event);
    }

    /**
     * @see javax.faces.component.StateHolder#restoreState(javax.faces.context.FacesContext, java.lang.Object)
     */
    public void restoreState(FacesContext context, Object state) {
        if (context == null) {
            throw new NullPointerException();
        }
        parent.restoreState(context, state);
    }

    /**
     * @see javax.faces.component.StateHolder#saveState(javax.faces.context.FacesContext)
     */
    public Object saveState(FacesContext context) {
        if (context == null) {
            throw new NullPointerException();
        }
        return parent.saveState(context);
    }

    /**
     * @see javax.faces.component.UIComponent#setId(java.lang.String)
     */
    public void setId(String id) {
        parent.setId(id);
    }

    /**
     * @see javax.faces.component.UIComponent#setParent(javax.faces.component.UIComponent)
     */
    public void setParent(UIComponent parent) {
        parent.setParent(parent);
    }

    /**
     * @see javax.faces.component.UIComponent#setRendered(boolean)
     */
    public void setRendered(boolean rendered) {
        parent.setRendered(rendered);
    }

    /**
     * @see javax.faces.component.UIComponent#setRendererType(java.lang.String)
     */
    public void setRendererType(String rendererType) {
        parent.setRendererType(rendererType);
    }

    /**
     * @see javax.faces.component.StateHolder#setTransient(boolean)
     */
    public void setTransient(boolean newTransientValue) {
        parent.setTransient(newTransientValue);
    }

    /**
     * @see javax.faces.component.UIComponent#setValueBinding(java.lang.String, javax.faces.el.ValueBinding)
     * @deprecated
     */
    public void setValueBinding(String name, ValueBinding binding) {
        parent.setValueBinding(name, binding);
    }

    /**
     * @see javax.faces.component.UIComponent#setValueExpression(java.lang.String, javax.el.ValueExpression)
     */
    public void setValueExpression(String name, ValueExpression binding) {
        parent.setValueExpression(name, binding);
    }

    /**
     * @see javax.faces.component.UIComponent#subscribeToEvent(java.lang.Class, javax.faces.event.ComponentSystemEventListener)
     */
    public void subscribeToEvent(Class<? extends SystemEvent> eventClass,
                                 ComponentSystemEventListener componentListener) {
        parent.subscribeToEvent(eventClass, componentListener);
    }

    /**
     * @see javax.faces.component.UIComponent#unsubscribeFromEvent(java.lang.Class, javax.faces.event.ComponentSystemEventListener)
     */
    public void unsubscribeFromEvent(Class<? extends SystemEvent> eventClass,
                                     ComponentSystemEventListener componentListener) {
        parent.unsubscribeFromEvent(eventClass, componentListener);
    }

    /**
     * @see javax.faces.component.UIComponent#visitTree(javax.faces.component.visit.VisitContext, javax.faces.component.visit.VisitCallback)
     */
    public boolean visitTree(VisitContext context, VisitCallback callback) {
        return parent.visitTree(context, callback);
    }

    @Override
    protected void addFacesListener(FacesListener listener) {
        // no-op
    }

    @Override
    protected FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    @Override
    protected FacesListener[] getFacesListeners(Class clazz) {
        return new FacesListener[0];
    }

    @Override
    protected Renderer getRenderer(FacesContext context) {
        return null;
    }

    @Override
    protected void removeFacesListener(FacesListener listener) {
        // no-op
    }

    public void addClientBehavior(String eventName, ClientBehavior behavior) {
        if (parent instanceof ClientBehaviorHolder) {
            ClientBehaviorHolder parentHolder = (ClientBehaviorHolder) parent;
            if (virtualEvent.equals(eventName)) {
                parentHolder.addClientBehavior(event, behavior);
            }
        } else {
            throw new FacesException(
                  "Unable to attach behavior to non-ClientBehaviorHolder parent:"
                  + parent);
        }

    }

    public Map<String, List<ClientBehavior>> getClientBehaviors() {
        if (parent instanceof ClientBehaviorHolder) {
            ClientBehaviorHolder parentHolder = (ClientBehaviorHolder) parent;
            Map<String, List<ClientBehavior>> behaviors = new HashMap<String, List<ClientBehavior>>(
                  1);
            behaviors.put(virtualEvent, parentHolder.getClientBehaviors().get(event));
            return Collections.unmodifiableMap(behaviors);
        } else {
            throw new FacesException(
                  "Unable to get behaviors from non-ClientBehaviorHolder parent:"
                  + parent);
        }
    }

    public String getDefaultEventName() {
        return virtualEvent;
    }

    public Collection<String> getEventNames() {
        return Collections.singleton(virtualEvent);
    }


}

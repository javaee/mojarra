/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2008 Sun Microsystems, Inc. All rights reserved.
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
 *
 * This file incorporates work covered by the following copyright and
 * permission notice:
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sun.faces.facelets.tag.jsf;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.el.ELException;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.application.Application;
import javax.faces.component.ActionSource;
import javax.faces.component.AjaxBehavior;
import javax.faces.component.AjaxBehaviors;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.component.ValueHolder;
import javax.faces.context.FacesContext;

import javax.faces.webapp.pdl.facelets.FaceletContext;
import com.sun.faces.facelets.tag.MetaTagHandlerImpl;
import javax.faces.webapp.pdl.facelets.tag.TagAttribute;
import javax.faces.webapp.pdl.facelets.tag.TagException;
import javax.faces.webapp.pdl.facelets.tag.MetaRuleset;
import com.sun.faces.facelets.tag.jsf.core.FacetHandler;
import java.util.Map;
import javax.faces.component.UniqueIdVendor;
import javax.faces.event.InitialStateEvent;

/**
 * Implementation of the tag logic used in the JSF specification. This is your
 * golden hammer for wiring UIComponents to Facelets.
 * 
 * @author Jacob Hookom
 * @version $Id$
 */
public class ComponentHandler extends MetaTagHandlerImpl {

    private final static Logger log = Logger
            .getLogger("facelets.tag.component");
    
    private final TagAttribute binding;

    protected String componentType;

    protected final TagAttribute id;

    private final String rendererType;

    public ComponentHandler(ComponentConfig config) {
        super(config);
        this.componentType = config.getComponentType();
        this.rendererType = config.getRendererType();
        this.id = this.getAttribute("id");
        this.binding = this.getAttribute("binding");
    }

    /**
     * Method handles UIComponent tree creation in accordance with the JSF 1.2
     * spec.
     * <ol>
     * <li>First determines this UIComponent's id by calling
     * {@link #getId(FaceletContext) getId(FaceletContext)}.</li>
     * <li>Search the parent for an existing UIComponent of the id we just
     * grabbed</li>
     * <li>If found, {@link com.sun.faces.facelets.tag.jsf.ComponentSupport#markForDeletion(javax.faces.component.UIComponent) mark}
     * its children for deletion.</li>
     * <li>If <i>not</i> found, call
     * {@link #createComponent(FaceletContext) createComponent}.
     * <ol>
     * <li>Only here do we apply
     * {@link com.sun.faces.facelets.tag.MetaTagHandlerImpl#setAttributes(FaceletContext, Object)}</li>
     * <li>Set the UIComponent's id</li>
     * <li>Set the RendererType of this instance</li>
     * </ol>
     * </li>
     * <li>Now apply the nextHandler, passing the UIComponent we've
     * created/found.</li>
     * <li>Now add the UIComponent to the passed parent</li>
     * <li>Lastly, if the UIComponent already existed (found), then
     * {@link ComponentSupport#finalizeForDeletion(UIComponent) finalize} for deletion.</li>
     * </ol>
     * 
     * @see com.sun.faces.facelets.FaceletHandler#apply(com.sun.faces.facelets.FaceletContext, javax.faces.component.UIComponent)
     * 
     * @throws TagException
     *             if the UIComponent parent is null
     */
    public final void apply(FaceletContext ctx, UIComponent parent)
            throws IOException, FacesException, ELException {
        // make sure our parent is not null
        if (parent == null) {
            throw new TagException(this.tag, "Parent UIComponent was null");
        }
        
        // possible facet scoped
        String facetName = this.getFacetName(ctx, parent);

        // our id
        String id = ctx.generateUniqueId(this.tagId);
        
        FacesContext faces = ctx.getFacesContext();

        // grab our component
        UIComponent c = ComponentSupport.findChildByTagId(parent, id);
        boolean componentFound = false;
        if (c != null) {
            componentFound = true;
            // mark all children for cleaning
            if (log.isLoggable(Level.FINE)) {
                log.fine(this.tag
                        + " Component["+id+"] Found, marking children for cleanup");
            }
            ComponentSupport.markForDeletion(c);
        } else {
            c = this.createComponent(ctx);
            c.getClientId(faces);
            if (log.isLoggable(Level.FINE)) {
                log.fine(this.tag + " Component["+id+"] Created: "
                        + c.getClass().getName());
            }
            this.setAttributes(ctx, c);
            
            // mark it owned by a facelet instance
            c.getAttributes().put(ComponentSupport.MARK_CREATED, id);
            
            // assign our unique id
            if (this.id != null) {
                c.setId(this.id.getValue(ctx));
            } else {
                UIViewRoot root = ComponentSupport.getViewRoot(ctx, parent);
                if (root != null) {
                    String uid;
                    UIComponent ancestorNamingContainer = parent.getNamingContainer();
                    if (null != ancestorNamingContainer &&
                        ancestorNamingContainer instanceof UniqueIdVendor) {
                        uid = ((UniqueIdVendor)ancestorNamingContainer).createUniqueId(faces);
                    } else {
                        uid = root.createUniqueId();
                    }
                    c.setId(uid);
                }
            }
            
            if (this.rendererType != null) {
                c.setRendererType(this.rendererType);
            }

            // hook method
            this.onComponentCreated(ctx, c, parent);
        }
        c.pushComponentToEL(ctx.getFacesContext(), c);
        // first allow c to get populated
        this.applyNextHandler(ctx, c);

        // finish cleaning up orphaned children
        if (componentFound) {
            ComponentSupport.finalizeForDeletion(c);
            
            if (facetName == null) {
            	parent.getChildren().remove(c);
            }
        }
        String viewId = faces.getViewRoot().getViewId();
        if (faces.getApplication().getViewHandler().getPageDeclarationLanguage(faces, viewId).getStateManagementStrategy(faces, viewId).isPdlDeliversInitialStateEvent(faces)) {
            c.processEvent(getInitialStateEvent(faces, c));
        }
        this.onComponentPopulated(ctx, c, parent);

        // add to the tree afterwards
        // this allows children to determine if it's
        // been part of the tree or not yet
        if (facetName == null) {
        	parent.getChildren().add(c);
        } else {
        	parent.getFacets().put(facetName, c);
        }
        c.popComponentFromEL(ctx.getFacesContext());
        
    }
    
    private static final String INITIAL_STATE_EVENT_KEY = "facelets.tag.InitialStateEvent";
    
    private static InitialStateEvent getInitialStateEvent(FacesContext context,
            UIComponent source) {
        InitialStateEvent ise = null;
        Map<Object,Object> attrs = context.getAttributes();
        if (null == (ise = (InitialStateEvent) attrs.get(INITIAL_STATE_EVENT_KEY))) {
            ise = new InitialStateEvent(source);
            attrs.put(INITIAL_STATE_EVENT_KEY, ise);
        }
        else {
            ise.setComponent(source);
        }

        return ise;
    }
    
    /**
     * Return the Facet name we are scoped in, otherwise null
     * @param ctx
     * @return
     */
    protected final String getFacetName(FaceletContext ctx, UIComponent parent) {
    	return (String) parent.getAttributes().get(FacetHandler.KEY);
    }

    /**
     * If the binding attribute was specified, use that in conjuction with our
     * componentType String variable to call createComponent on the Application,
     * otherwise just pass the componentType String.
     * <p />
     * If the binding was used, then set the ValueExpression "binding" on the
     * created UIComponent.
     * 
     * @see Application#createComponent(javax.faces.el.ValueBinding,
     *      javax.faces.context.FacesContext, java.lang.String)
     * @see Application#createComponent(java.lang.String)
     * @param ctx
     *            FaceletContext to use in creating a component
     * @return
     */
    protected UIComponent createComponent(FaceletContext ctx) {
        UIComponent c = null;
        FacesContext faces = ctx.getFacesContext();
        Application app = faces.getApplication();
        if (this.binding != null) {
            ValueExpression ve = this.binding.getValueExpression(ctx,
                                                                 Object.class);
            c = app.createComponent(ve, faces, this.componentType, this.rendererType);
            if (c != null) {
                // Make sure the component supports 1.2
                c.setValueExpression("binding", ve);
            }
        } else {
            c = app.createComponent(faces, this.componentType, this.rendererType);
        }
        return c;
    }

    /**
     * If the id TagAttribute was specified, get it's value, otherwise generate
     * a unique id from our tagId.
     * 
     * @see TagAttribute#getValue(FaceletContext)
     * @param ctx
     *            FaceletContext to use
     * @return what should be a unique Id
     */
    protected String getId(FaceletContext ctx) {
        if (this.id != null) {
            return this.id.getValue(ctx);
        }
        return ctx.generateUniqueId(this.tagId);
    }

    protected MetaRuleset createMetaRuleset(Class type) {
        MetaRuleset m = super.createMetaRuleset(type);
        
        // ignore standard component attributes
        m.ignore("binding").ignore("id");
        
        // add auto wiring for attributes
        m.addRule(ComponentRule.Instance);
        
        // if it's an ActionSource
        if (ActionSource.class.isAssignableFrom(type)) {
            m.addRule(ActionSourceRule.Instance);
        }
        
        // if it's a ValueHolder
        if (ValueHolder.class.isAssignableFrom(type)) {
            m.addRule(ValueHolderRule.Instance);
            
            // if it's an EditableValueHolder
            if (EditableValueHolder.class.isAssignableFrom(type)) {
                m.ignore("submittedValue");
                m.ignore("valid");
                m.addRule(EditableValueHolderRule.Instance);
            }
        }
        
        return m;
    }
    
    /**
     * A hook method for allowing developers to do additional processing once Facelets
     * creates the component.  The 'setAttributes' method is still perferred, but this
     * method will provide the parent UIComponent before it's been added to the tree and
     * before any children have been added to the newly created UIComponent.
     * 
     * @param ctx
     * @param c
     * @param parent
     */
    protected void onComponentCreated(FaceletContext ctx, UIComponent c, UIComponent parent) {
        // Default Behavior  
        String facesEventType = getFacesEventType(c);
        if (facesEventType == null) {
           return;
        }
        AjaxBehaviors ajaxBehaviors = (AjaxBehaviors)ctx.getFacesContext().getAttributes().
            get(AjaxBehaviors.AJAX_BEHAVIORS);
        if (ajaxBehaviors != null) {
            AjaxBehavior ajaxBehavior = ajaxBehaviors.getBehaviorForEvent(facesEventType);
            if (ajaxBehavior != null) {
                c.getAttributes().put(AjaxBehavior.AJAX_BEHAVIOR, ajaxBehavior);
            }
        }
    }

    protected String getFacesEventType(UIComponent c) {
        String event = null;
        if (c instanceof EditableValueHolder) {
            event = AjaxBehavior.AJAX_VALUE_CHANGE;
        } else if (c instanceof ActionSource) {
            event = AjaxBehavior.AJAX_ACTION;
        } 
        return event;
    }

    protected void onComponentPopulated(FaceletContext ctx, UIComponent c, UIComponent parent) {
        // do nothing
    }

    protected void applyNextHandler(FaceletContext ctx, UIComponent c) 
            throws IOException, FacesException, ELException {
        // first allow c to get populated
        this.nextHandler.apply(ctx, c);
    }
}

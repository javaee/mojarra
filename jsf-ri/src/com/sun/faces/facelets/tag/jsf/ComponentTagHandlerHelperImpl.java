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

package com.sun.faces.facelets.tag.jsf;

import com.sun.faces.facelets.tag.MetaRulesetImpl;
import com.sun.faces.facelets.tag.jsf.core.FacetHandler;
import com.sun.faces.util.FacesLogger;
import com.sun.faces.util.Util;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.el.ELException;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.application.Application;
import javax.faces.component.ActionSource;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.component.ValueHolder;
import javax.faces.component.behavior.AjaxBehavior;
import javax.faces.component.behavior.AjaxBehaviors;
import javax.faces.component.behavior.BehaviorHolder;
import javax.faces.context.FacesContext;
import javax.faces.event.InitialStateEvent;
import javax.faces.webapp.pdl.facelets.FaceletContext;
import javax.faces.webapp.pdl.facelets.FaceletException;
import javax.faces.webapp.pdl.facelets.tag.ComponentConfig;
import javax.faces.webapp.pdl.facelets.tag.ComponentHandler;
import javax.faces.webapp.pdl.facelets.tag.MetaRuleset;
import javax.faces.webapp.pdl.facelets.tag.TagAttribute;
import javax.faces.webapp.pdl.facelets.tag.TagException;
import javax.faces.webapp.pdl.facelets.tag.TagHandlerHelper;

public class ComponentTagHandlerHelperImpl extends TagHandlerHelper {
    
    private static final String INITIAL_STATE_EVENT_KEY = "facelets.tag.InitialStateEvent";
    
    private ComponentHandler owner;
    
    private final static Logger log = FacesLogger.FACELETS_COMPONENT.getLogger();
    
    private final TagAttribute binding;

    protected String componentType;

    protected final TagAttribute id;

    private final String rendererType;
    
    private CreateComponentDelegate createComponentDelegate;


    public ComponentTagHandlerHelperImpl(ComponentHandler owner) {
        this.owner = owner;
        ComponentConfig config = owner.getComponentConfig();
        this.componentType = config.getComponentType();
        this.rendererType = config.getRendererType();
        this.id = owner.getTagAttribute("id");
        this.binding = owner.getTagAttribute("binding");
        
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
    
    @Override
    public void apply(FaceletContext ctx, UIComponent parent) throws IOException, FacesException, FaceletException, ELException {
        // make sure our parent is not null
        if (parent == null) {
            throw new TagException(owner.getTag(), "Parent UIComponent was null");
        }

        // our id
        String id = ctx.generateUniqueId(owner.getTagId());

        // grab our component
        UIComponent c = ComponentSupport.findChildByTagId(parent, id);
        boolean componentFound = false;
        if (c != null) {
            componentFound = true;
            // mark all children for cleaning
            if (log.isLoggable(Level.FINE)) {
                log.fine(owner.getTag()
                        + " Component["+id+"] Found, marking children for cleanup");
            }
            ComponentSupport.markForDeletion(c);
        } else {
            c = this.createComponent(ctx);
            if (log.isLoggable(Level.FINE)) {
                log.fine(owner.getTag() + " Component["+id+"] Created: "
                        + c.getClass().getName());
            }
            owner.setAttributes(ctx, c);
            
            // mark it owned by a facelet instance
            c.getAttributes().put(ComponentSupport.MARK_CREATED, id);
            
            // assign our unique id
            if (this.id != null) {
                c.setId(this.id.getValue(ctx));
            } else {
                UIViewRoot root = ComponentSupport.getViewRoot(ctx, parent);
                if (root != null) {
                    String uid = root.createUniqueId();
                    c.setId(uid);
                }
            }
            
            if (this.rendererType != null) {
                c.setRendererType(this.rendererType);
            }

            // hook method
            this.privateOnComponentCreated(ctx, c, parent);
            owner.onComponentCreated(ctx, c, parent);
        }
        c.pushComponentToEL(ctx.getFacesContext(), c);
        // first allow c to get populated
        owner.applyNextHandler(ctx, c);

        // finish cleaning up orphaned children
        if (componentFound) {
            ComponentSupport.finalizeForDeletion(c);
            
            if (getFacetName(ctx, parent) == null) {
                parent.getChildren().remove(c);
            }
        }
        
        c.processEvent(getInitialStateEvent(ctx.getFacesContext(), c));
        owner.onComponentPopulated(ctx, c, parent);

        // add to the tree afterwards
        // this allows children to determine if it's
        // been part of the tree or not yet
        ComponentSupport.addComponent(ctx, parent, c);
        c.popComponentFromEL(ctx.getFacesContext());
        
        
    }

    @Override
    public MetaRuleset createMetaRuleset(Class type) {
        Util.notNull("type", type);
        MetaRuleset m = new MetaRulesetImpl(owner.getTag(), type);

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
    
    void setCreateComponentDelegate(CreateComponentDelegate createComponentDelegate) {
        this.createComponentDelegate = createComponentDelegate;
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
    private UIComponent createComponent(FaceletContext ctx) {
        
        if (null != createComponentDelegate) {
            return createComponentDelegate.createComponent(ctx);
        }
        
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
     * A hook method for allowing developers to do additional processing once Facelets
     * creates the component.  The 'setAttributes' method is still perferred, but this
     * method will provide the parent UIComponent before it's been added to the tree and
     * before any children have been added to the newly created UIComponent.
     * 
     * @param ctx
     * @param c
     * @param parent
     */
    private void privateOnComponentCreated(FaceletContext ctx, UIComponent c, UIComponent parent) {
        AjaxBehaviors ajaxBehaviors = (AjaxBehaviors)ctx.getFacesContext().getAttributes().
            get(AjaxBehaviors.AJAX_BEHAVIORS);
        if (ajaxBehaviors != null) {
            AjaxBehavior ajaxBehavior = ajaxBehaviors.getCurrentBehavior();
            if (ajaxBehavior != null) {
                if (c instanceof BehaviorHolder) {
                    BehaviorHolder bHolder = (BehaviorHolder)c;
                    String event = bHolder.getDefaultEventName();
                    if (null != ajaxBehavior.getEvent()) {
                        event = ajaxBehavior.getEvent();
                    }
                    Collection eventNames = bHolder.getEventNames();
                    if (null != eventNames && eventNames.contains(event) || 
                        event.equals(bHolder.getDefaultEventName())) {
                        bHolder.addBehavior(event, ajaxBehavior);
                    }
                }
            }
        }
    }
    
    /**
     * Return the Facet name we are scoped in, otherwise null
     * @param ctx
     * @return
     */
    private final String getFacetName(FaceletContext ctx, UIComponent parent) {
        return (String) parent.getAttributes().get(FacetHandler.KEY);
    }
    
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
    
    
    interface CreateComponentDelegate {

        public UIComponent createComponent(FaceletContext ctx);
        
    }

}

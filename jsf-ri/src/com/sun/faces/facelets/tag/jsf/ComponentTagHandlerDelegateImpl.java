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

import com.sun.faces.component.behavior.AjaxBehaviors;
import com.sun.faces.facelets.tag.MetaRulesetImpl;
import com.sun.faces.facelets.tag.jsf.core.FacetHandler;
import com.sun.faces.util.FacesLogger;
import com.sun.faces.util.Util;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.el.ELException;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.application.Application;
import javax.faces.application.ProjectStage;
import javax.faces.component.*;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.FacesContext;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.FaceletException;
import javax.faces.view.facelets.ComponentConfig;
import javax.faces.view.facelets.ComponentHandler;
import javax.faces.view.facelets.MetaRuleset;
import javax.faces.view.facelets.TagAttribute;
import javax.faces.view.facelets.TagException;
import javax.faces.view.facelets.TagHandlerDelegate;

public class ComponentTagHandlerDelegateImpl extends TagHandlerDelegate {
    
    private ComponentHandler owner;
    
    private final static Logger log = FacesLogger.FACELETS_COMPONENT.getLogger();
    
    private final TagAttribute binding;

    protected String componentType;

    protected final TagAttribute id;

    private final String rendererType;
    
    private CreateComponentDelegate createComponentDelegate;


    public ComponentTagHandlerDelegateImpl(ComponentHandler owner) {
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
     * {@link javax.faces.view.facelets.ComponentHandler#getTagId()}.</li>
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

            if (ProjectStage.Development.equals(ctx.getFacesContext()
                  .getApplication().getProjectStage())) {
                // inject the location into the component
                c.getAttributes().put(UIComponent.VIEW_LOCATION_KEY,
                                      owner.getTag().getLocation());
            }
            
            // assign our unique id
            if (this.id != null) {
                c.setId(this.id.getValue(ctx));
            } else {
                UIViewRoot root = ComponentSupport.getViewRoot(ctx, parent);
                if (root != null) {
                    String uid;
                    UIComponent ancestorNamingContainer = parent
                          .getNamingContainer();
                    if (null != ancestorNamingContainer &&
                        ancestorNamingContainer instanceof UniqueIdVendor) {
                        uid = ((UniqueIdVendor) ancestorNamingContainer)
                              .createUniqueId(ctx.getFacesContext(), id);
                    } else {
                        uid = root.createUniqueId(ctx.getFacesContext(), id);
                    }
                    c.setId(uid);
                }

            }
            
            if (this.rendererType != null) {
                c.setRendererType(this.rendererType);
            }

            // hook method
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

        this.privateOnComponentPopulated(ctx, c, parent);
        owner.onComponentPopulated(ctx, c, parent);
        // add to the tree afterwards
        // this allows children to determine if it's
        // been part of the tree or not yet
        ComponentSupport.addComponent(ctx, parent, c);
        c.popComponentFromEL(ctx.getFacesContext());

        // RELEASE_PENDING - this is *ugly*.  We need to *not*
        // call PartialStateHolder.markInitialState() if the component
        // has a composite component parent under the assumption that
        // the CompositeComponentTagHandler will take care of it.
        if (Boolean.TRUE.equals(ctx.getFacesContext().getAttributes().get("partialStateSaving"))
              && UIComponent.getCurrentCompositeComponent(ctx.getFacesContext()) == null) {
            c.markInitialState();
        }
        
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

        // if it's a selectone or selectmany
        if (UISelectOne.class.isAssignableFrom(type) || UISelectMany.class.isAssignableFrom(type)) {
            m.addRule(RenderPropertyRule.Instance);
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

    /*
     * Internal hook that allows us to perform common processing for all
     * components after they are populated.  At the moment, the only common
     * processing we need to perform is applying wrapping AjaxBehaviors,
     * if any exist.
     */
    private void privateOnComponentPopulated(FaceletContext ctx, UIComponent c, UIComponent parent) {

        if (c instanceof ClientBehaviorHolder) {
            FacesContext context = ctx.getFacesContext();
            AjaxBehaviors ajaxBehaviors = AjaxBehaviors.getAjaxBehaviors(context, false);
            if (ajaxBehaviors != null) {
                ajaxBehaviors.addBehaviors(context, (ClientBehaviorHolder)c);
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

    interface CreateComponentDelegate {

        public UIComponent createComponent(FaceletContext ctx);
        
    }

}

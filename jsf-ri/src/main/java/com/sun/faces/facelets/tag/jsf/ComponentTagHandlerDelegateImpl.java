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

package com.sun.faces.facelets.tag.jsf;

import com.sun.faces.component.behavior.AjaxBehaviors;
import com.sun.faces.component.validator.ComponentValidators;
import com.sun.faces.component.CompositeComponentStackManager;
import com.sun.faces.context.StateContext;
import com.sun.faces.facelets.impl.IdMapper;
import com.sun.faces.facelets.tag.MetaRulesetImpl;
import com.sun.faces.facelets.tag.jsf.core.FacetHandler;
import com.sun.faces.util.FacesLogger;
import com.sun.faces.util.Util;
import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.application.ProjectStage;
import javax.faces.component.*;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.FacesContext;
import javax.faces.view.facelets.ComponentConfig;
import javax.faces.view.facelets.ComponentHandler;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.MetaRuleset;
import javax.faces.view.facelets.TagAttribute;
import javax.faces.view.facelets.TagException;
import javax.faces.view.facelets.TagHandlerDelegate;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import static com.sun.faces.RIConstants.DYNAMIC_COMPONENT;
import static com.sun.faces.component.CompositeComponentStackManager.StackType.TreeCreation;

public class ComponentTagHandlerDelegateImpl extends TagHandlerDelegate {
    
    private ComponentHandler owner;
    
    private final static Logger log = FacesLogger.FACELETS_COMPONENT.getLogger();
    
    private final TagAttribute binding;

    protected String componentType;

    protected final TagAttribute id;

    private final String rendererType;
    
    private CreateComponentDelegate createCompositeComponentDelegate;


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
    public void apply(FaceletContext ctx, UIComponent parent) throws IOException {
        FacesContext context = ctx.getFacesContext();

        // make sure our parent is not null
        if (parent == null) {
            throw new TagException(owner.getTag(), "Parent UIComponent was null");
        }

        // our id
        String id = ctx.generateUniqueId(owner.getTagId());

        // grab our component
        UIComponent c = findChild(ctx, parent, id);
        if (null == c &&
            context.isPostback() &&
            UIComponent.isCompositeComponent(parent) &&
            parent.getAttributes().get(id) != null) {
            c = findReparentedComponent(ctx, parent, id);
        }
        else {
            /**
             * If we found a child that is dynamic, the actual parent might 
             * have changed, so we need to remove it from the actual parent.
             * The reapplyDynamicActions will then replay the actions and
             * will make sure it ends up in the correct order.
             */
            if (c != null && c.getParent() != parent && 
                c.getAttributes().containsKey(DYNAMIC_COMPONENT)) {
                c.getParent().getChildren().remove(c);
            }
        }
        
        boolean componentFound = false;
        if (c != null) {
            componentFound = true;
                doExistingComponentActions(ctx, id, c);
        } else {
            c = this.createComponent(ctx);
            
            doNewComponentActions(ctx, id, c);
            assignUniqueId(ctx, parent, id, c);

            // hook method
            owner.onComponentCreated(ctx, c, parent);
        }

       CompositeComponentStackManager ccStackManager =
              CompositeComponentStackManager.getManager(context);
        boolean compcompPushed = pushComponentToEL(ctx, c, ccStackManager);

        if (ProjectStage.Development == context.getApplication().getProjectStage()) {
            ComponentSupport.setTagForComponent(context, c, this.owner.getTag());
        }

        // first allow c to get populated
        owner.applyNextHandler(ctx, c);

        // finish cleaning up orphaned children
        if (componentFound) {
               doOrphanedChildCleanup(ctx, parent, c);
            }

        this.privateOnComponentPopulated(ctx, c);
        owner.onComponentPopulated(ctx, c, parent);
        // add to the tree afterwards
        // this allows children to determine if it's
        // been part of the tree or not yet
        addComponentToView(ctx, parent, c, componentFound);
        adjustIndexOfDynamicChildren(context, c);
        popComponentFromEL(ctx, c, ccStackManager, compcompPushed);
    }

    private void adjustIndexOfDynamicChildren(FacesContext context, 
            UIComponent parent) {
        StateContext stateContext = StateContext.getStateContext(context);
        if (!stateContext.hasOneOrMoreDynamicChild(parent)) {
            return;
        }

        List<UIComponent> children = parent.getChildren();
        List<UIComponent> dynamicChildren = Collections.emptyList();

        for (UIComponent cur : children) {
            if (stateContext.componentAddedDynamically(cur)) {
                if (dynamicChildren.isEmpty()) {
                    dynamicChildren = new ArrayList<UIComponent>(children.size());
                }
                dynamicChildren.add(cur);
            }
        }
        
        // First remove all the dynamic children, this puts the non-dynamic children at
        // their original position 
        for (UIComponent cur : dynamicChildren) {
            int i = stateContext.getIndexOfDynamicallyAddedChildInParent(cur);
            if (-1 != i) {
                children.remove(cur);
            }
        }
        
        // Now that the non-dynamic children are in the correct position add the dynamic children
        // back in.
        for (UIComponent cur : dynamicChildren) {
            int i = stateContext.getIndexOfDynamicallyAddedChildInParent(cur);
            if (-1 != i) {
                if (i < children.size()) {
                    children.add(i, cur);
                } else {
                    children.add(cur);
                }
            }
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


        // ------------------------------------------------------- Protected Methods


    protected void addComponentToView(FaceletContext ctx,
                                      UIComponent parent,
                                      UIComponent c,
                                      boolean componentFound) {

        FacesContext context = ctx.getFacesContext();
        boolean suppressEvents = ComponentSupport.suppressViewModificationEvents(context);
        boolean compcomp = UIComponent.isCompositeComponent(c);

        if (suppressEvents && componentFound && !compcomp) {
            context.setProcessingEvents(false);
        }

        ComponentSupport.addComponent(ctx, parent, c);

        if (suppressEvents && componentFound && !compcomp) {
            context.setProcessingEvents(true);
        }

    }


    protected boolean pushComponentToEL(FaceletContext ctx,
                                        UIComponent c,
                                        CompositeComponentStackManager ccStackManager) {

        c.pushComponentToEL(ctx.getFacesContext(), c);
        boolean compcompPushed = false;

        if (UIComponent.isCompositeComponent(c)) {
            compcompPushed = ccStackManager.push(c, TreeCreation);
        }
        return compcompPushed;

    }


    protected void popComponentFromEL(FaceletContext ctx,
                                      UIComponent c,
                                      CompositeComponentStackManager ccStackManager,
                                      boolean compCompPushed) {

        c.popComponentFromEL(ctx.getFacesContext());
        if (compCompPushed) {
            ccStackManager.pop(TreeCreation);
        }

    }


    protected void doOrphanedChildCleanup(FaceletContext ctx,
                                          UIComponent parent,
                                          UIComponent c) {

        ComponentSupport.finalizeForDeletion(c);
        if (getFacetName(parent) == null) {
            FacesContext context = ctx.getFacesContext();
            boolean suppressEvents =
                  ComponentSupport.suppressViewModificationEvents(context);

            if (suppressEvents) {
                // if the component has already been found, it will be removed
                // and added back to the view.  We don't want to publish events
                // for this case.
                context.setProcessingEvents(false);
            }
            // suppress the remove event for this case since it will be re-added
            parent.getChildren().remove(c);
            if (suppressEvents) {
                // re-enable event processing
                context.setProcessingEvents(true);
            }
        }

    }


    protected void assignUniqueId(FaceletContext ctx,
                                  UIComponent parent,
                                  String id,
                                  UIComponent c) {

        if (this.id != null) {
            c.setId(this.id.getValue(ctx));
        } else {
            UIViewRoot root = ComponentSupport.getViewRoot(ctx, parent);
            if (root != null) {
                String uid;
                IdMapper mapper = IdMapper.getMapper(ctx.getFacesContext());
                String mid = ((mapper != null) ? mapper.getAliasedId(id) : id);
                UIComponent ancestorNamingContainer = parent
                      .getNamingContainer();
                if (null != ancestorNamingContainer &&
                    ancestorNamingContainer instanceof UniqueIdVendor) {
                    uid = ((UniqueIdVendor) ancestorNamingContainer)
                          .createUniqueId(ctx.getFacesContext(), mid);
                } else {
                    uid = root.createUniqueId(ctx.getFacesContext(), mid);
                }
                c.setId(uid);
            }

        }

        if (this.rendererType != null) {
            c.setRendererType(this.rendererType);
        }

    }


    protected void doNewComponentActions(FaceletContext ctx,
                                         String id,
                                         UIComponent c) {

        if (log.isLoggable(Level.FINE)) {
            log.fine(owner.getTag() + " Component["+id+"] Created: "
                    + c.getClass().getName());
        }
        // If this is NOT a composite component...
        if (null == createCompositeComponentDelegate) {
            // set the attributes and properties into the UIComponent instance.
            owner.setAttributes(ctx, c);
        }
        // otherwise, allow the composite component code to do it.

        // mark it owned by a facelet instance
        c.getAttributes().put(ComponentSupport.MARK_CREATED, id);

        if (ctx.getFacesContext().isProjectStage(ProjectStage.Development)) {
            // inject the location into the component
            c.getAttributes().put(UIComponent.VIEW_LOCATION_KEY,
                                  owner.getTag().getLocation());
        }

    }


    protected void doExistingComponentActions(FaceletContext ctx, String id, UIComponent c) {

        // mark all children for cleaning
        if (log.isLoggable(Level.FINE)) {
            log.fine(owner.getTag()
                     + " Component["
                     + id
                     + "] Found, marking children for cleanup");
        }
        ComponentSupport.markForDeletion(c);
        /*
         * Repply the id, for the case when the component tree was changed, and the id's are set explicitly.
         */
        if (this.id != null) {
     	   c.setId(this.id.getValue(ctx));
        }
    }


    @SuppressWarnings({"UnusedDeclaration"})
    protected UIComponent findChild(FaceletContext ctx,
                                    UIComponent parent,
                                    String tagId) {

        return ComponentSupport.findChildByTagId(parent, tagId);

    }

    @SuppressWarnings({"UnusedDeclaration"})
    protected UIComponent findReparentedComponent(FaceletContext ctx,
                                    UIComponent parent,
                                    String tagId) {
        return ComponentSupport.findChildByTagId(parent, tagId);
    }

    // ------------------------------------------------- Package Private Methods

    
    void setCreateCompositeComponentDelegate(CreateComponentDelegate createComponentDelegate) {
        this.createCompositeComponentDelegate = createComponentDelegate;
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
        
        if (null != createCompositeComponentDelegate) {
            return createCompositeComponentDelegate.createComponent(ctx);
        }
        
        UIComponent c;
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
    private void privateOnComponentPopulated(FaceletContext ctx, UIComponent c) {

        if (c instanceof ClientBehaviorHolder) {
            FacesContext context = ctx.getFacesContext();
            AjaxBehaviors ajaxBehaviors = AjaxBehaviors.getAjaxBehaviors(context, false);
            if (ajaxBehaviors != null) {
                ajaxBehaviors.addBehaviors(context, (ClientBehaviorHolder)c);
            }
        }
        if (c instanceof EditableValueHolder) {
            processValidators(ctx.getFacesContext(), (EditableValueHolder) c);
        }
    }


    /**
     * Process default validatior/wrapping validation information and install
     * <code>Validators</code> based off the result.
     */
    private void processValidators(FacesContext ctx,
                                   EditableValueHolder editableValueHolder) {

        ComponentValidators componentValidators =
              ComponentValidators.getValidators(ctx, false);
        if (componentValidators != null) {
            // process any elements on the stack.  
            componentValidators.addValidators(ctx, editableValueHolder);
        } else {
            // no custom handling required, so add the default validators
            ComponentValidators.addDefaultValidatorsToComponent(ctx, editableValueHolder);
        }

    }

    /**
     * @return the Facet name we are scoped in, otherwise null
     */
    private String getFacetName(UIComponent parent) {
        return (String) parent.getAttributes().get(FacetHandler.KEY);
    }




    interface CreateComponentDelegate {

        public UIComponent createComponent(FaceletContext ctx);
        public void setCompositeComponent(FacesContext context, UIComponent cc);
        public UIComponent getCompositeComponent(FacesContext context);
        
    }

}

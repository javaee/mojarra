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
import com.sun.faces.util.Util;

import java.beans.BeanDescriptor;
import java.beans.BeanInfo;
import java.io.IOException;
import java.util.List;

import javax.el.ELException;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.component.behavior.Behavior;
import javax.faces.component.behavior.ClientBehavior;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.FacesContext;
import javax.faces.view.AttachedObjectHandler;
import javax.faces.view.AttachedObjectTarget;
import javax.faces.view.BehaviorHolderAttachedObjectTarget;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.FaceletException;
import javax.faces.view.facelets.BehaviorHandler;
import javax.faces.view.facelets.MetaRuleset;
import javax.faces.view.facelets.TagAttribute;
import javax.faces.view.facelets.TagException;
import javax.faces.view.facelets.TagHandlerDelegate;

/**
 *
 * @author edburns
 */
class BehaviorTagHandlerDelegateImpl extends TagHandlerDelegate implements AttachedObjectHandler {
    
    private BehaviorHandler owner;

    public BehaviorTagHandlerDelegateImpl(BehaviorHandler owner) {
        this.owner = owner;
    }
    
    @Override
    public void apply(FaceletContext ctx, UIComponent parent)
        throws IOException, FacesException, FaceletException, ELException {
        // only process if it's been created
        if (parent == null || !(parent.getParent() == null)) {
            return;
        }
        if (UIComponent.isCompositeComponent(parent)) {
            // Check composite component event name:
            BeanInfo componentBeanInfo = (BeanInfo) parent.getAttributes().get(
                  UIComponent.BEANINFO_KEY);
            if (null == componentBeanInfo) {
                throw new TagException(
                      owner.getTag(),
                      "Error: enclosing composite component does not have BeanInfo attribute");
            }
            BeanDescriptor componentDescriptor = componentBeanInfo
                  .getBeanDescriptor();
            if (null == componentDescriptor) {
                throw new TagException(
                      owner.getTag(),
                      "Error: enclosing composite component BeanInfo does not have BeanDescriptor");
            }
            List<AttachedObjectTarget> targetList = (List<AttachedObjectTarget>)
                  componentDescriptor
                        .getValue(AttachedObjectTarget.ATTACHED_OBJECT_TARGETS_KEY);
            if (null == targetList) {
                throw new TagException(
                      owner.getTag(),
                      "Error: enclosing composite component does not support behavior events");
            }
            String eventName = owner.getEventName();
            boolean supportedEvent = false;
            for (AttachedObjectTarget target : targetList) {
                if (target instanceof BehaviorHolderAttachedObjectTarget) {
                    BehaviorHolderAttachedObjectTarget behaviorTarget = (BehaviorHolderAttachedObjectTarget) target;
                    if ((null != eventName && eventName.equals(behaviorTarget.getName()))
                        || (null == eventName && behaviorTarget.isDefaultEvent())) {
                        supportedEvent = true;
                        break;
                    }
                }
            }
            if (supportedEvent) {
                CompositeComponentTagHandler.getAttachedObjectHandlers(parent)
                      .add(owner);
            } else {
                throw new TagException(
                      owner.getTag(),
                      "Error: enclosing composite component does not support event "
                      + eventName);
            }

        } else if (parent instanceof ClientBehaviorHolder) {
            owner.applyAttachedObject(ctx.getFacesContext(), parent);
        }  else {
            throw new TagException(owner.getTag(), "Parent not an instance of ClientBehaviorHolder: " + parent);
        }

    }
    
    public void applyAttachedObject(FacesContext context, UIComponent parent) {
        FaceletContext ctx = (FaceletContext) context.getAttributes().get(FaceletContext.FACELET_CONTEXT_KEY);
        // cast to the ClientBehaviorHolder.
        ClientBehaviorHolder behaviorHolder = (ClientBehaviorHolder) parent;
        ValueExpression bindingExpr=null;
        Behavior behavior=null;
        if (null != owner.getBinding()){
            bindingExpr = owner.getBinding().getValueExpression(ctx, Behavior.class);
            behavior = (Behavior) bindingExpr.getValue(ctx);
        }
        if (null == behavior){
            if (null != owner.getBehaviorId()){
                behavior = ctx.getFacesContext().getApplication().createBehavior(owner.getBehaviorId());
                if (null == behavior){
                    throw new TagException(owner.getTag(),
                            "No Faces behavior defined for Id "+owner.getBehaviorId());
                }
                if (null != bindingExpr){
                    bindingExpr.setValue(ctx, behavior);
                }
            } else {
                throw new TagException(owner.getTag(),"No behaviorId defined");
            }
        }
        owner.setAttributes(ctx, behavior);

        if (behavior instanceof ClientBehavior) {
            behaviorHolder.addClientBehavior(getEventName(behaviorHolder), (ClientBehavior)behavior);
        }
    }

    
	
    public MetaRuleset createMetaRuleset(Class type) {
        Util.notNull("type", type);
        MetaRuleset m = new MetaRulesetImpl(owner.getTag(), type);
        m = m.ignore("event");
        return m.ignore("binding");
    }
    
    public String getFor() {
        String result = null;
        TagAttribute attr = owner.getTagAttribute("for");
        
        if (null != attr) {
            result = attr.getValue();
        }
        return result;
        
    }
    
    private String getEventName(ClientBehaviorHolder holder){
        String eventName;
        if (null != owner.getEvent()){
            eventName = owner.getEvent().getValue();
        } else {
            eventName = holder.getDefaultEventName();
        }
        if (null == eventName){
            throw new TagException(owner.getTag(), "The event name is not defined");
        }
        return eventName;
    }
    
}

/*
 * $Id$
 */

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

import java.io.IOException;

import javax.el.ELException;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.application.Resource;
import javax.faces.component.UIComponent;
import javax.faces.component.behavior.Behavior;
import javax.faces.component.behavior.BehaviorHolder;
import javax.faces.context.FacesContext;
import javax.faces.webapp.pdl.facelets.FaceletContext;
import javax.faces.webapp.pdl.facelets.FaceletException;
import javax.faces.webapp.pdl.facelets.tag.MetaRuleset;
import javax.faces.webapp.pdl.facelets.tag.TagAttribute;
import javax.faces.webapp.pdl.facelets.tag.TagConfig;
import javax.faces.webapp.pdl.facelets.tag.TagException;

import com.sun.faces.facelets.tag.MetaTagHandlerImpl;

/**
 * <p class="changed_added_2_0"></p>
 * @author asmirnov@exadel.com
 *
 */
public class BehaviorHandler extends MetaTagHandlerImpl implements AttachedBehaviorObjectHandler {

    private final TagAttribute binding;
    
    private final TagAttribute event;
    
    private String behaviorId;
	
    /**
     * <p class="changed_added_2_0"></p>
     * @param config
     * @deprecated
     */
    public BehaviorHandler(TagConfig config) {
        super(config);
        this.binding = this.getAttribute("binding");
        this.event = this.getAttribute("event");
        if (null != event && !event.isLiteral()){
            throw new TagException(this.tag, "The 'event' attribute for behavior tag have to be literal");
        }
    }
	
    public BehaviorHandler(BehaviorConfig config) {
        this((TagConfig) config);
        this.behaviorId = config.getBehaviorId();
    }

    public void apply(FaceletContext ctx, UIComponent parent)
        throws IOException, FacesException, FaceletException, ELException {
        // only process if it's been created
        if (parent == null || !(parent.getParent() == null)) {
            return;
        }
        if (parent instanceof BehaviorHolder) {
            applyAttachedObject(ctx.getFacesContext(), parent);
        } else if (parent.getAttributes().containsKey(Resource.COMPONENT_RESOURCE_KEY)) {
            if (null == getFor()) {
                // PENDING(): I18N
                throw new TagException(this.tag,
                    "behavior tags nested within composite components must have a non-null \"for\" attribute");
            }
            // Allow the composite component to know about the target component.
            CompositeComponentTagHandler.getAttachedObjectHandlers(parent).add(this);
        } else {
            throw new TagException(this.tag, "Parent not an instance of BehaviorHolder: " + parent);
        }

    }
	
    private String getEventName(BehaviorHolder holder){
        String eventName;
        if (null != event){
            eventName = event.getValue();
        } else {
            eventName = holder.getDefaultEventName();
        }
        if (null == eventName){
            throw new TagException(this.tag, "The event name is not defined");			
        }
        return eventName;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected MetaRuleset createMetaRuleset(Class type) {
        return super.createMetaRuleset(type).ignore("binding");
    }

    public void applyAttachedObject(FacesContext context, UIComponent parent) {
        FaceletContext ctx = (FaceletContext) context.getAttributes().get(FaceletContext.FACELET_CONTEXT_KEY);
        // cast to the BehaviorHolder.
        BehaviorHolder behaviorHolder = (BehaviorHolder) parent;
        ValueExpression bindingExpr=null;
        Behavior behavior=null;
        if (null != binding){
            bindingExpr = binding.getValueExpression(ctx, Behavior.class);
            behavior = (Behavior) bindingExpr.getValue(ctx);
        }
        if (null == behavior){
            if (null != this.behaviorId){
                behavior = ctx.getFacesContext().getApplication().createBehavior(behaviorId);
                if (null == behavior){
                    throw new TagException(this.tag,"No Faces behavior defined for Id "+this.behaviorId);
                }
                if (null != bindingExpr){
                    bindingExpr.setValue(ctx, behavior);
                }
            } else {
                throw new TagException(this.tag,"No behaviorId defined");
            }
        }
        this.setAttributes(ctx, behavior);
        behaviorHolder.addBehavior(getEventName(behaviorHolder), behavior);
    }

    public String getFor() {
        String result = null;
        TagAttribute attr = this.getAttribute("for");
        
        if (null != attr) {
            result = attr.getValue();
        }
        return result;
    }
}

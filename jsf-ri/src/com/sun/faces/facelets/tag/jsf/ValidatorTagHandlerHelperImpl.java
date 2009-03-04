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
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.el.ELException;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.application.Resource;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.webapp.pdl.facelets.FaceletContext;
import javax.faces.webapp.pdl.facelets.FaceletException;
import javax.faces.webapp.pdl.facelets.tag.MetaRuleset;
import javax.faces.webapp.pdl.facelets.tag.MetaTagHandler;
import javax.faces.webapp.pdl.facelets.tag.TagException;
import javax.faces.webapp.pdl.facelets.tag.TagHandlerHelper;

public class ValidatorTagHandlerHelperImpl extends TagHandlerHelper {

    private ValidateHandler owner = null;
    
    private SetValidatorDefaultsOnParentDelegate 
            setValidatorDefaultsOnParentDelegate;

    public ValidatorTagHandlerHelperImpl(MetaTagHandler owner) {
        this.owner = (ValidateHandler) owner;
    }
    
    public void setSetValidatorDefaultsOnParentDelegate(SetValidatorDefaultsOnParentDelegate delegate) {
        this.setValidatorDefaultsOnParentDelegate = delegate;
    }

    @Override
    public void apply(FaceletContext ctx, UIComponent parent) throws IOException, FacesException, FaceletException, ELException {

        // only process if it's been created
        if (parent == null || !(parent.getParent() == null)) {
            return;
        }

        if (parent instanceof EditableValueHolder) {
            applyAttachedObject(ctx.getFacesContext(), parent);
        } else if (parent.getAttributes().containsKey(Resource.COMPONENT_RESOURCE_KEY)) {
            if (null == owner.getFor()) {
                // PENDING(): I18N
                throw new TagException(owner.getTag(),
                        "validator tags nested within composite components must have a non-null \"for\" attribute");
            }
            // Allow the composite component to know about the target
            // component.
            CompositeComponentTagHandler.getAttachedObjectHandlers(parent).add(owner);
        } else {
            // NOTE no longer do we see this situation as invalid, instead it is used to capture branch defaults
            setValidatorDefaultsOnParent(ctx, parent, owner.isDisabled(ctx));
//            throw new TagException(this.tag,
//                    "Parent not an instance of EditableValueHolder: " + parent);
        }

    }

    @Override
    public MetaRuleset createMetaRuleset(Class type) {
        Util.notNull("type", type);
        MetaRuleset m = new MetaRulesetImpl(owner.getTag(), type);
        
        return m.ignore("binding").ignore("disabled");
    }
    
    
    public void applyAttachedObject(FacesContext context, UIComponent parent) {
        FaceletContext ctx = (FaceletContext) context.getAttributes().get(FaceletContext.FACELET_CONTEXT_KEY);

        if (owner.isDisabled(ctx)) {
            setDefaultValidatorIdStateForBranch(ctx, parent, false);
            return;
        }

        // cast to an EditableValueHolder
        EditableValueHolder evh = (EditableValueHolder) parent;
        ValueExpression ve = null;
        Validator v = null;
        if (owner.getBinding() != null) {
            ve = owner.getBinding().getValueExpression(ctx, Validator.class);
            v = (Validator) ve.getValue(ctx);
        }
        if (v == null) {
            v = this.createValidator(ctx);
            if (ve != null) {
                ve.setValue(ctx, v);
            }
        }
        if (v == null) {
            throw new TagException(owner.getTag(), "No Validator was created");
        }
        owner.setAttributes(ctx, v);
        evh.addValidator(v);
    }   
    
    
    /**
     * Template method for creating a Validator instance
     * 
     * @param ctx
     *            FaceletContext to use
     * @return a new Validator instance
     */
    private Validator createValidator(FaceletContext ctx) {
        String id = owner.getValidatorId(ctx);
        if (id == null) {
            throw new TagException(
                    owner.getTag(),
                    "A validator id was not specified. Typically the validator id is set in the constructor ValidateHandler(ValidatorConfig)");
        }
        return ctx.getFacesContext().getApplication().createValidator(id);
    }

    
    
    
    /**
     * <p>As this parent is not an EditableValueHolder or composite component, take this opportunity to set
     * properties on the parent component that can be shared by validators used in this branch of the
     * component tree. The most common use of this method is to enable or disable a default validator.</p>
     */
    private void setValidatorDefaultsOnParent(FaceletContext ctx, UIComponent parent, boolean disabled) {
        setDefaultValidatorIdStateForBranch(ctx, parent, !disabled);
        if (null != setValidatorDefaultsOnParentDelegate) {
            this.setValidatorDefaultsOnParentDelegate.setValidatorDefaultsOnParent(ctx, parent, disabled);
        }
    }

    /**
     * <p>Enable or disable a default validator for this branch of the component tree. This method will first
     * resolve the validator id and if it is non-null, it will delegate to a method which assigns the state
     * to the parent component.</p>
     */
    private void setDefaultValidatorIdStateForBranch(FaceletContext ctx, UIComponent parent, boolean state) {
         String id = owner.getValidatorId(ctx);
         // NOTE technically the validatorId should never be null
         if (id != null) {
             setDefaultValidatorIdStateForBranch(id, parent, state);
         }
    }

    /**
     * <p>Enable or disable a default validator for this branch of the component tree. This method uses the
     * supplied validator id to create an entry in the default validator id map in the attributes of the parent
     * component.</p>
     */
    private void setDefaultValidatorIdStateForBranch(String validatorId, UIComponent parent, boolean state) {
        Map<String, Boolean> defaultValidatorIds = (Map<String, Boolean>) parent.getAttributes().get(UIInput.DEFAULT_VALIDATOR_IDS_KEY);
        if (defaultValidatorIds == null) {
            defaultValidatorIds = new LinkedHashMap<String, Boolean>();
            parent.getAttributes().put(UIInput.DEFAULT_VALIDATOR_IDS_KEY, defaultValidatorIds);
        }

        defaultValidatorIds.put(validatorId, state);
    }
    
    public interface SetValidatorDefaultsOnParentDelegate {
        public void setValidatorDefaultsOnParent(FaceletContext ctx, UIComponent parent, boolean disabled);
    }

}

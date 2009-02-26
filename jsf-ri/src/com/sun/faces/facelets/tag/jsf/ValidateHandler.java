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

import javax.el.ELException;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.validator.Validator;

import javax.faces.webapp.pdl.facelets.FaceletContext;
import javax.faces.webapp.pdl.facelets.FaceletException;
import com.sun.faces.facelets.tag.MetaTagHandlerImpl;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.faces.webapp.pdl.facelets.tag.TagAttribute;
import javax.faces.webapp.pdl.facelets.tag.TagConfig;
import javax.faces.webapp.pdl.facelets.tag.TagException;
import javax.faces.webapp.pdl.facelets.tag.MetaRuleset;
import javax.faces.application.Resource;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.webapp.pdl.EditableValueHolderAttachedObjectHandler;

/**
 * Handles setting a Validator instance on a EditableValueHolder. Will wire all
 * attributes set to the Validator instance created/fetched. Uses the "binding"
 * attribute for grabbing instances to apply attributes to. <p/> Will only
 * set/create Validator is the passed UIComponent's parent is null, signifying
 * that it wasn't restored from an existing tree.
 * 
 * @author Jacob Hookom
 * @version $Id$
 */
public class ValidateHandler extends MetaTagHandlerImpl implements EditableValueHolderAttachedObjectHandler {

    private final TagAttribute binding;

    private final TagAttribute disabled;
    
    private String validatorId;

    /**
     * 
     * @param config
     * @deprecated
     */
    public ValidateHandler(TagConfig config) {
        super(config);
        this.binding = this.getAttribute("binding");
        this.disabled = this.getAttribute("disabled");
    }
    
    public ValidateHandler(ValidatorConfig config) {
        this((TagConfig) config);
        this.validatorId = config.getValidatorId();
    }

    /**
     * TODO
     * 
     * @see com.sun.faces.facelets.FaceletHandler#apply(com.sun.faces.facelets.FaceletContext, javax.faces.component.UIComponent)
     */
    public void apply(FaceletContext ctx, UIComponent parent)
            throws IOException, FacesException, FaceletException, ELException {

        // only process if it's been created
        if (parent == null || !(parent.getParent() == null)) {
            return;
        }

        if (parent instanceof EditableValueHolder) {
            applyAttachedObject(ctx.getFacesContext(), parent);
        } else if (parent.getAttributes().containsKey(Resource.COMPONENT_RESOURCE_KEY)) {
            if (null == getFor()) {
                // PENDING(): I18N
                throw new TagException(this.tag,
                        "validator tags nested within composite components must have a non-null \"for\" attribute");
            }
            // Allow the composite component to know about the target
            // component.
            CompositeComponentTagHandler.getAttachedObjectHandlers(parent).add(this);
        } else {
            // NOTE no longer do we see this situation as invalid, instead it is used to capture branch defaults
            setValidatorDefaultsOnParent(ctx, parent, isDisabled(ctx));
//            throw new TagException(this.tag,
//                    "Parent not an instance of EditableValueHolder: " + parent);
        }
    }

    protected boolean isDisabled(FaceletContext ctx) {
        return disabled != null ? Boolean.TRUE.equals(disabled.getBoolean(ctx)) : false;
    }

    /**
     * <p>Retrieve the id of the validator that is to be created an added to the parent <code>EditableValueHolder</code>.
     * All subclasses should override this method because it is important for Facelets to have a unique way of
     * identifying the validators that are added to this <code>EditableValueHolder</code> and allows exclusions
     * to work properly. An exclusion is a validator declaration that has the attribute "disabled" which resolves
     * to false, instructing Facelets not to register a default validator with the same id.</p>
     * <p>TODO could support binding by evaluating and reflecting its type for the value of the VALIDATOR_ID field,
     * though technically the validatorId is always required, even when using a binding</p>
     */
    protected String getValidatorId(FaceletContext ctx) {
        return validatorId;
    }

    /**
     * Template method for creating a Validator instance
     * 
     * @param ctx
     *            FaceletContext to use
     * @return a new Validator instance
     */
    protected Validator createValidator(FaceletContext ctx) {
        String id = getValidatorId(ctx);
        if (id == null) {
            throw new TagException(
                    this.tag,
                    "A validator id was not specified. Typically the validator id is set in the constructor ValidateHandler(ValidatorConfig)");
        }
        return ctx.getFacesContext().getApplication().createValidator(id);
    }

    protected MetaRuleset createMetaRuleset(Class type) {
        return super.createMetaRuleset(type).ignore("binding").ignore("disabled");
    }
    
    public void applyAttachedObject(FacesContext context, UIComponent parent) {
        FaceletContext ctx = (FaceletContext) context.getAttributes().get(FaceletContext.FACELET_CONTEXT_KEY);

        if (isDisabled(ctx)) {
            setDefaultValidatorIdStateForBranch(ctx, parent, false);
            return;
        }

        // cast to an EditableValueHolder
        EditableValueHolder evh = (EditableValueHolder) parent;
        ValueExpression ve = null;
        Validator v = null;
        if (this.binding != null) {
            ve = this.binding.getValueExpression(ctx, Validator.class);
            v = (Validator) ve.getValue(ctx);
        }
        if (v == null) {
            v = this.createValidator(ctx);
            if (ve != null) {
                ve.setValue(ctx, v);
            }
        }
        if (v == null) {
            throw new TagException(this.tag, "No Validator was created");
        }
        this.setAttributes(ctx, v);
        evh.addValidator(v);
    }    
    
    
    public String getFor() {
        String result = null;
        TagAttribute attr = this.getAttribute("for");
        
        if (null != attr) {
            result = attr.getValue();
        }
        return result;
        
    }

    /**
     * <p>As this parent is not an EditableValueHolder or composite component, take this opportunity to set
     * properties on the parent component that can be shared by validators used in this branch of the
     * component tree. The most common use of this method is to enable or disable a default validator.</p>
     */
    protected void setValidatorDefaultsOnParent(FaceletContext ctx, UIComponent parent, boolean disabled) {
        setDefaultValidatorIdStateForBranch(ctx, parent, !disabled);
    }

    /**
     * <p>Enable or disable a default validator for this branch of the component tree. This method will first
     * resolve the validator id and if it is non-null, it will delegate to a method which assigns the state
     * to the parent component.</p>
     */
    protected void setDefaultValidatorIdStateForBranch(FaceletContext ctx, UIComponent parent, boolean state) {
         String id = getValidatorId(ctx);
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
    protected void setDefaultValidatorIdStateForBranch(String validatorId, UIComponent parent, boolean state) {
        Map<String, Boolean> defaultValidatorIds = (Map<String, Boolean>) parent.getAttributes().get(UIInput.DEFAULT_VALIDATOR_IDS_KEY);
        if (defaultValidatorIds == null) {
            defaultValidatorIds = new LinkedHashMap<String, Boolean>();
            parent.getAttributes().put(UIInput.DEFAULT_VALIDATOR_IDS_KEY, defaultValidatorIds);
        }

        defaultValidatorIds.put(validatorId, state);
    }

}

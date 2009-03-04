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

import javax.faces.webapp.pdl.facelets.tag.ValidatorConfig;
import java.io.IOException;

import javax.el.ELException;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;

import javax.faces.webapp.pdl.facelets.FaceletContext;
import javax.faces.webapp.pdl.facelets.FaceletException;
import com.sun.faces.facelets.tag.MetaTagHandlerImpl;
import javax.faces.FactoryFinder;
import javax.faces.webapp.pdl.facelets.tag.TagAttribute;
import javax.faces.webapp.pdl.facelets.tag.TagConfig;
import javax.faces.webapp.pdl.facelets.tag.MetaRuleset;
import javax.faces.context.FacesContext;
import javax.faces.webapp.pdl.EditableValueHolderAttachedObjectHandler;
import javax.faces.webapp.pdl.facelets.tag.Tag;
import javax.faces.webapp.pdl.facelets.tag.TagHandlerHelper;
import javax.faces.webapp.pdl.facelets.tag.TagHandlerHelperFactory;

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
    
    private ValidatorTagHandlerHelperImpl helper;

    /**
     * 
     * @param config
     * @deprecated
     */
    public ValidateHandler(TagConfig config) {
        super(config);
        this.binding = this.getAttribute("binding");
        this.disabled = this.getAttribute("disabled");
        
        TagHandlerHelperFactory helperFactory = (TagHandlerHelperFactory)
                FactoryFinder.getFactory(FactoryFinder.TAG_HANDLER_HELPER_FACTORY);
        helper = (ValidatorTagHandlerHelperImpl) helperFactory.createValidatorHandlerHelper(this);
        
    }
    
    public ValidateHandler(ValidatorConfig config) {
        this((TagConfig) config);
        this.validatorId = config.getValidatorId();

        TagHandlerHelperFactory helperFactory = (TagHandlerHelperFactory)
                FactoryFinder.getFactory(FactoryFinder.TAG_HANDLER_HELPER_FACTORY);
        helper = (ValidatorTagHandlerHelperImpl) helperFactory.createValidatorHandlerHelper(this);
    }

    /**
     * TODO
     * 
     * @see com.sun.faces.facelets.FaceletHandler#apply(com.sun.faces.facelets.FaceletContext, javax.faces.component.UIComponent)
     */
    public void apply(FaceletContext ctx, UIComponent parent)
            throws IOException, FacesException, FaceletException, ELException {
        helper.apply(ctx, parent);
    }

    public boolean isDisabled(FaceletContext ctx) {
        return disabled != null ? Boolean.TRUE.equals(disabled.getBoolean(ctx)) : false;
    }

    public void applyAttachedObject(FacesContext ctx, UIComponent parent) {
        helper.applyAttachedObject(ctx, parent);
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
    public String getValidatorId(FaceletContext ctx) {
        return validatorId;
    }

    @Override
    protected MetaRuleset createMetaRuleset(Class type) {
        return helper.createMetaRuleset(type);
    }
    
    protected TagHandlerHelper getTagHandlerHelper() {
        return this.helper;
    }
    
    
    public String getFor() {
        String result = null;
        TagAttribute attr = this.getAttribute("for");
        
        if (null != attr) {
            result = attr.getValue();
        }
        return result;
        
    }
    
    public Tag getTag() {
        return this.tag;
    }
    
    public TagAttribute getBinding() {
        return this.binding;
    }

    
    @Override
    public void setAttributes(FaceletContext ctx, Object instance) {
        super.setAttributes(ctx, instance);
    }

}

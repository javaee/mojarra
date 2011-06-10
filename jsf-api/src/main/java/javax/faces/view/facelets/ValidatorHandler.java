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
 *
 *
 * This file incorporates work covered by the following copyright and
 * permission notice:
 *
 * Copyright 2005-2007 The Apache Software Foundation
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

package javax.faces.view.facelets;

import javax.faces.view.EditableValueHolderAttachedObjectHandler;

/**
 * <p class="changed_added_2_0">Handles setting a {@link
 * javax.faces.validator.Validator} instance on an {@link
 * javax.faces.component.EditableValueHolder} parent. Will wire all
 * attributes set to the <code>Validator</code> instance
 * created/fetched. Uses the "binding" attribute for grabbing instances
 * to apply attributes to.</p> 

 * <p>Will only set/create Validator is the passed UIComponent's parent
 * is null, signifying that it wasn't restored from an existing
 * tree.</p>

 */
public class ValidatorHandler extends FaceletsAttachedObjectHandler implements EditableValueHolderAttachedObjectHandler {

    private String validatorId;
    
    private TagHandlerDelegate helper;

    private ValidatorConfig config;


    /**
     * <p class="changed_added_2_0">Construct this instance around the configuration information in argument <code>config</code></p>
     * @param config the <code>TagConfig</code> subclass for this kind
     * of attached object.
     */
    public ValidatorHandler(ValidatorConfig config) {
        super(config);
        this.config = config;
        this.validatorId = config.getValidatorId();
    }

    /**
     * <p class="changed_added_2_0">Return the implementation specific
     * delegate instance that provides the bulk of the work for this
     * handler instance.</p>
     */
    @Override
    protected TagHandlerDelegate getTagHandlerDelegate() {
        if (null == helper) {
            helper = delegateFactory.createValidatorHandlerDelegate(this);
        }
        return helper;
    }

    /**
     * <p>Retrieve the id of the validator that is to be created and
     * added to the parent <code>EditableValueHolder</code>.  All
     * subclasses should override this method because it is important
     * for Facelets to have a unique way of identifying the validators
     * that are added to this <code>EditableValueHolder</code> and
     * allows exclusions to work properly. An exclusion is a validator
     * declaration that has the attribute "disabled" which resolves to
     * false, instructing Facelets not to register a default validator
     * with the same id.</p>
     */
    public String getValidatorId(FaceletContext ctx) {
        if (validatorId == null) {
            TagAttribute idAttr = getAttribute("validatorId");
            if (idAttr == null) {
                return null;
            } else {
                return idAttr.getValue(ctx);
            }
        }
        return validatorId;
    }


    /**
     * <p class="changed_added_2_0">Return the <code>TagConfig</code>
     * subclass used to configure this handler instance.</p>
     */
    public ValidatorConfig getValidatorConfig() {

        return config;
        
    }

}

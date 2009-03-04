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
package com.sun.faces.facelets.tag.jsf.core;

import javax.faces.webapp.pdl.facelets.tag.ValidatorHandler;
import com.sun.faces.facelets.tag.jsf.ValidatorTagHandlerHelperImpl;
import javax.faces.webapp.pdl.facelets.tag.ValidatorConfig;
import javax.faces.component.UIComponent;
import javax.faces.webapp.pdl.facelets.FaceletContext;
import javax.faces.webapp.pdl.facelets.tag.TagAttribute;
import javax.faces.validator.BeanValidator;

import com.sun.faces.facelets.tag.jsf.ValidatorTagHandlerHelperImpl.SetValidatorDefaultsOnParentDelegate;

/**
 * This handler is intended to be used to back the &lt;f:validateBean&gt;. In addition to serving its normal
 * role as a validator, this handler allows the tag to be placed at an arbitrary place in the tree for the
 * purpose of defining validation groups for that region.
 *
 * @author Dan Allen
 */
public class BeanValidateHandler extends ValidatorHandler implements SetValidatorDefaultsOnParentDelegate {

    private final TagAttribute validationGroups;

    public BeanValidateHandler(ValidatorConfig config) {
        super(config);
        validationGroups = getAttribute("validationGroups");
        ((ValidatorTagHandlerHelperImpl)this.getTagHandlerHelper()).setSetValidatorDefaultsOnParentDelegate(this);
    }

    protected String getValidationGroups(FaceletContext ctx) {
        return (validationGroups != null ? validationGroups.getValue(ctx) : null);
    }

    public void setValidatorDefaultsOnParent(FaceletContext ctx, UIComponent parent, boolean disabled) {
        // QUESTION should we register the validation groups if disabled attribute is true?
        // NOTE <f:validateBean/> and <f:validateBean validationGroups=""/> at this level have no effect on the validation groups
        String validationGroupsStr = getValidationGroups(ctx);
        if (validationGroupsStr != null && !validationGroupsStr.matches(BeanValidator.EMPTY_VALIDATION_GROUPS_PATTERN)) {
            parent.getAttributes().put(BeanValidator.VALIDATION_GROUPS_KEY, validationGroupsStr);
        }
    }

}
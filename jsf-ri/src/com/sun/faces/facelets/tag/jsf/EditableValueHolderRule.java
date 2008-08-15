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

import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.MethodExpressionValueChangeListener;
import javax.faces.event.ValueChangeEvent;
import javax.faces.validator.MethodExpressionValidator;

import com.sun.faces.facelets.FaceletContext;
import com.sun.faces.facelets.el.LegacyMethodBinding;
import com.sun.faces.facelets.tag.TagAttribute;
import com.sun.faces.facelets.tag.Metadata;
import com.sun.faces.facelets.tag.MetaRule;
import com.sun.faces.facelets.tag.MetadataTarget;
import com.sun.faces.facelets.util.FacesAPI;

/**
 * 
 * @author Jacob Hookom
 * @version $Id$
 */
public final class EditableValueHolderRule extends MetaRule {

    final static class LiteralValidatorMetadata extends Metadata {

        private final String validatorId;

        public LiteralValidatorMetadata(String validatorId) {
            this.validatorId = validatorId;
        }

        public void applyMetadata(FaceletContext ctx, Object instance) {
            ((EditableValueHolder) instance).addValidator(ctx.getFacesContext()
                    .getApplication().createValidator(this.validatorId));
        }
    }

    final static class ValueChangedExpressionMetadata extends Metadata {
        private final TagAttribute attr;

        public ValueChangedExpressionMetadata(TagAttribute attr) {
            this.attr = attr;
        }

        public void applyMetadata(FaceletContext ctx, Object instance) {
            ((EditableValueHolder) instance)
                    .addValueChangeListener(new MethodExpressionValueChangeListener(
                            this.attr.getMethodExpression(ctx, null,
                                    VALUECHANGE_SIG)));
        }
    }

    final static class ValueChangedBindingMetadata extends Metadata {
        private final TagAttribute attr;

        public ValueChangedBindingMetadata(TagAttribute attr) {
            this.attr = attr;
        }

        public void applyMetadata(FaceletContext ctx, Object instance) {
            ((EditableValueHolder) instance)
                    .setValueChangeListener(new LegacyMethodBinding(this.attr
                            .getMethodExpression(ctx, null, VALUECHANGE_SIG)));
        }
    }

    final static class ValidatorExpressionMetadata extends Metadata {
        private final TagAttribute attr;

        public ValidatorExpressionMetadata(TagAttribute attr) {
            this.attr = attr;
        }

        public void applyMetadata(FaceletContext ctx, Object instance) {
            ((EditableValueHolder) instance)
                    .addValidator(new MethodExpressionValidator(this.attr
                            .getMethodExpression(ctx, null, VALIDATOR_SIG)));
        }
    }

    final static class ValidatorBindingMetadata extends Metadata {
        private final TagAttribute attr;

        public ValidatorBindingMetadata(TagAttribute attr) {
            this.attr = attr;
        }

        public void applyMetadata(FaceletContext ctx, Object instance) {
            ((EditableValueHolder) instance)
                    .setValidator(new LegacyMethodBinding(this.attr
                            .getMethodExpression(ctx, null, VALIDATOR_SIG)));
        }
    }

    private final static Class[] VALIDATOR_SIG = new Class[] {
            FacesContext.class, UIComponent.class, Object.class };

    private final static Class[] VALUECHANGE_SIG = new Class[] { ValueChangeEvent.class };

    public final static EditableValueHolderRule Instance = new EditableValueHolderRule();

    public Metadata applyRule(String name, TagAttribute attribute,
            MetadataTarget meta) {

        if (meta.isTargetInstanceOf(EditableValueHolder.class)) {

            boolean elSupport = FacesAPI.getComponentVersion(meta
                    .getTargetClass()) >= 12;

            if ("validator".equals(name)) {
                if (attribute.isLiteral()) {
                    return new LiteralValidatorMetadata(attribute.getValue());
                } else if (elSupport) {
                    return new ValidatorExpressionMetadata(attribute);
                } else {
                    return new ValidatorBindingMetadata(attribute);
                }
            }

            if ("valueChangeListener".equals(name)) {
                if (elSupport) {
                    return new ValueChangedExpressionMetadata(attribute);
                } else {
                    return new ValueChangedBindingMetadata(attribute);
                }
            }

        }
        return null;
    }

}

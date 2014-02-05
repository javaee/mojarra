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
 */

package com.sun.faces.taglib.jsf_core;

import com.sun.faces.util.MessageUtils;

import javax.el.ValueExpression;
import javax.faces.component.StateHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.servlet.jsp.JspException;

/**
 * Basic implementation of <code>ValidatorELTag</code>.
 */
public class ValidatorTag extends AbstractValidatorTag {


    // --------------------------------------------- Methods from ValidatorELTag


    @Override
    protected Validator createValidator() throws JspException {       

        if (validatorId != null && validatorId.isLiteralText()) {
            return createValidator(validatorId,
                                   binding,
                                   FacesContext.getCurrentInstance());
        } else {
            return new BindingValidator(validatorId, binding);
        }

    }


    // ----------------------------------------------------------- Inner Classes


    public static class BindingValidator implements Validator, StateHolder {

        private ValueExpression binding;
        private ValueExpression validatorId;

        // -------------------------------------------------------- Constructors

        /**
         * <p>Only used during state restoration</p>
         */
        public BindingValidator() { }


        public BindingValidator(ValueExpression validatorId,
                                ValueExpression binding) {

            this.validatorId = validatorId;
            this.binding = binding;

        }


        // -------------------------------------------- Methods from StateHolder

        private Object[] state;
        public Object saveState(FacesContext context) {

            if (context == null) {
                throw new NullPointerException();
            }
            if (state == null) {
                state = new Object[2];
            }
            state[0] = validatorId;
            state[1] = binding;

            return state;
            
        }

        public void restoreState(FacesContext context, Object state) {

            if (context == null) {
                throw new NullPointerException();
            }
            this.state = (Object[]) state;
            if (this.state != null) {
                this.validatorId = (ValueExpression) this.state[0];
                this.binding = (ValueExpression) this.state[1];
            }

        }

        public boolean isTransient() {

            return false;

        }

        public void setTransient(boolean newTransientValue) {
            //no-op
        }


        // ---------------------------------------------- Methods from Validator


        /**
         * <p>Perform the correctness checks implemented by this
         * {@link javax.faces.validator.Validator} against the specified {@link javax.faces.component.UIComponent}.
         * If any violations are found, a {@link javax.faces.validator.ValidatorException}
         * will be thrown containing the {@link javax.faces.application.FacesMessage} describing
         * the failure.
         *
         * @param context   FacesContext for the request we are processing
         * @param component UIComponent we are checking for correctness
         * @param value     the value to validate
         * @throws javax.faces.validator.ValidatorException
         *                              if validation fails
         * @throws NullPointerException if <code>context</code>
         *                              or <code>component</code> is <code>null</code>
         */
        public void validate(FacesContext context,
                             UIComponent component,
                             Object value)
        throws ValidatorException {


           Validator instance = createValidator(validatorId, binding, context);


            if (instance != null) {
                instance.validate(context, component, value);
            } else {
                throw new ValidatorException(
                     MessageUtils.getExceptionMessage(
                          MessageUtils.CANNOT_VALIDATE_ID,
                          validatorId != null ? validatorId.getExpressionString() : "",
                          binding != null ? binding.getExpressionString() : ""));
            }

        }

    }
    
}

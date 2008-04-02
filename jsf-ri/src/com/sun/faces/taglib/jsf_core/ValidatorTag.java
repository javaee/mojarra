/*
 * $Id: ValidatorTag.java,v 1.6 2007/03/01 15:51:36 rlubke Exp $
 */

/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 * 
 * You can obtain a copy of the License at
 * https://javaserverfaces.dev.java.net/CDDL.html or
 * legal/CDDLv1.0.txt. 
 * See the License for the specific language governing
 * permission and limitations under the License.
 * 
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at legal/CDDLv1.0.txt.    
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 * 
 * [Name of File] [ver.__] [Date]
 * 
 * Copyright 2005 Sun Microsystems Inc. All Rights Reserved
 */

package com.sun.faces.taglib.jsf_core;

import com.sun.faces.util.MessageUtils;

import javax.el.ValueExpression;
import javax.faces.component.StateHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.ConverterException;
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

        return new BindingValidator(validatorId, binding);

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


        private Validator instance;


        // -------------------------------------------- Methods from StateHolder

        private Object[] state;
        public Object saveState(FacesContext context) {

            if (state == null) {
                state = new Object[2];
            }
            state[0] = validatorId;
            state[1] = binding;

            return state;
            
        }

        public void restoreState(FacesContext context, Object state) {

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

            if (instance == null) {
                instance = createValidator(validatorId, binding, context);
            }

            if (instance != null) {
                instance.validate(context, component, value);
            } else {
                throw new ConverterException(
                     MessageUtils.getExceptionMessage(
                          MessageUtils.CANNOT_CONVERT_ID));
            }

        }

    }
    
}

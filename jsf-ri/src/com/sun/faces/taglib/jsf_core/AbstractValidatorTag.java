/*
 * $Id: AbstractValidatorTag.java,v 1.1 2007/03/01 15:51:37 rlubke Exp $
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

import com.sun.faces.util.Util;

import javax.faces.webapp.ValidatorELTag;
import javax.faces.validator.Validator;
import javax.faces.context.FacesContext;
import javax.faces.FacesException;
import javax.servlet.jsp.JspException;
import javax.el.ELContext;
import javax.el.ValueExpression;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * <p>Base class for all <code>ValidatorTag<code>s.</p>
 */
public class AbstractValidatorTag extends ValidatorELTag {

     private static final Logger LOGGER =
             Util.getLogger(Util.FACES_LOGGER + Util.TAGLIB_LOGGER);

    /**
     * <p>The {@link javax.el.ValueExpression} that evaluates to an object that
     * implements {@link javax.faces.convert.Converter}.</p>
     */
    protected ValueExpression binding = null;


    /**
     * <p>The identifier of the {@link javax.faces.validator.Validator}
     * instance to be created.</p>
     */
    protected ValueExpression validatorId = null;    


    // ---------------------------------------------------------- Public Methods


    /**
     * <p>Set the expression that will be used to create a
     * {@link javax.el.ValueExpression} that references a backing bean property
     * of the {@link javax.faces.validator.Validator} instance to be created.</p>
     *
     * @param binding The new expression
     */
    public void setBinding(ValueExpression binding) {

        this.binding = binding;

    } // END setBinding


    /**
     * <p>Set the identifer of the {@link javax.faces.validator.Validator}
     * instance to be created.
     *
     * @param validatorId The identifier of the converter instance to be
     * created.
     */
    public void setValidatorId(ValueExpression validatorId) {

        this.validatorId = validatorId;

    } // END setValidatorId


    // --------------------------------------------- Methods from ValdiatorELTag


    protected Validator createValidator() throws JspException {

        try {
            return createValidator(validatorId,
                                   binding,
                                   FacesContext.getCurrentInstance());
        } catch (FacesException fe) {
            throw new JspException(fe.getCause());
        }

    }


    protected static Validator createValidator(ValueExpression validatorId,
                                               ValueExpression binding,
                                               FacesContext facesContext) {

        ELContext elContext = facesContext.getELContext();
        Validator validator = null;

        // If "binding" is set, use it to create a validator instance.
        if (binding != null) {
            try {
                validator = (Validator) binding.getValue(elContext);
                if (validator != null) {
                    return validator;
                }
            } catch (Exception e) {
                throw new FacesException(e);
            }
        }

        // If "validatorId" is set, use it to create the validator
        // instance.  If "validatorId" and "binding" are both set, store the
        // validator instance in the value of the property represented by
        // the ValueExpression 'binding'.
        if (validatorId != null) {
            try {
                String validatorIdVal = (String)
                     validatorId.getValue(elContext);
                validator = facesContext.getApplication()
                     .createValidator(validatorIdVal);
                if (validator != null && binding != null) {
                    binding.setValue(elContext, validator);
                }
            } catch (Exception e) {
                throw new FacesException(e);
            }
        }

        if (validator == null) {
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.log(Level.WARNING,
                     "jsf.core.taglib.validator.binding.validator_not_resolved",
                     new Object[]{validatorId != null ? validatorId.getExpressionString() : "",
                          binding != null ? binding.getExpressionString() : ""});
            }
        }

        return validator;

    }

}

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
import com.sun.faces.util.FacesLogger;

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

     private static final Logger LOGGER = FacesLogger.TAGLIB.getLogger();

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
                     MessageUtils.getExceptionMessageString(
                          MessageUtils.CANNOT_VALIDATE_ID,
                          validatorId != null ? validatorId.getExpressionString() : "",
                          binding != null ? binding.getExpressionString() : ""));
            }
        }

        return validator;

    }

}

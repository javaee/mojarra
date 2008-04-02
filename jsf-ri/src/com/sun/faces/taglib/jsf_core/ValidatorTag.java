/*
 * $Id: ValidatorTag.java,v 1.1 2005/05/05 20:51:27 edburns Exp $
 */

/*
 * Copyright 2005 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.taglib.jsf_core;

import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.webapp.ValidatorELTag;
import javax.servlet.jsp.JspException;

/**
 * Basic implementation of <code>ValidatorELTag</code>.
 */
public class ValidatorTag extends ValidatorELTag {


    // -------------------------------------------------------------- Attributes


    /**
     * <p>The identifier of the {@link javax.faces.validator.Validator}
     * instance to be created.</p>
     */
    private ValueExpression validatorId = null;

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


    /**
     * <p>The {@link javax.el.ValueExpression} that evaluates to an object that
     * implements {@link javax.faces.convert.Converter}.</p>
     */
    private ValueExpression binding = null;

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


    // -------------------------------------------- Methods from ConverterELTag


    protected Validator createValidator()
    throws JspException {

        FacesContext facesContext = FacesContext.getCurrentInstance();
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
                throw new JspException(e);
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
                throw new JspException(e);
            }
        }

        return validator;

    } // END createConverter


}

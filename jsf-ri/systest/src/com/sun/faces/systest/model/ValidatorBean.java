/*
 * $Id: ValidatorBean.java,v 1.1 2004/12/02 18:42:27 rogerk Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.systest.model;

import com.sun.faces.systest.TestValidator01;

import javax.faces.event.AbortProcessingException;
import javax.faces.validator.Validator;


public class ValidatorBean extends Object {

    public ValidatorBean() {
    }

    private Validator validator = null;
    public Validator getValidator() {
        if (validator == null) {
            return new TestValidator01();
        }
        return validator;
    }
    public void setValidator(Validator validator) {
        this.validator = validator;
    }

    private Validator doubleValidator = null;
    public Validator getDoubleValidator() {
        if (doubleValidator == null) {
            return new javax.faces.validator.DoubleRangeValidator();
        }
        return doubleValidator;
    }
    public void setDoubleValidator(Validator doubleValidator) {
        this.doubleValidator = doubleValidator;
    }

    private Validator lengthValidator = null;
    public Validator getLengthValidator() {
//        if (lengthValidator == null) {
//System.out.println("RETURN VAL INSTANCE..");
//            return new javax.faces.validator.LengthValidator();
//        }
        return lengthValidator;
    }
    public void setLengthValidator(Validator lengthValidator) {
        this.lengthValidator = lengthValidator;
System.out.println("SET VAL INSTANCE..");
    }

    private Validator longRangeValidator = null;
    public Validator getLongRangeValidator() {
        if (longRangeValidator == null) {
            return new javax.faces.validator.LongRangeValidator();
        }
        return longRangeValidator;
    }
    public void setLongRangeValidator(Validator longRangeValidator) {
        this.longRangeValidator = longRangeValidator;
    }

}

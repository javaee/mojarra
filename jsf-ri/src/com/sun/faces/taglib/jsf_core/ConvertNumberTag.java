/*
 * $Id: ConvertNumberTag.java,v 1.8 2004/02/06 18:55:41 rlubke Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.taglib.jsf_core;

import com.sun.faces.util.Util;

import javax.faces.convert.Converter;
import javax.faces.convert.NumberConverter;
import javax.faces.webapp.ConverterTag;
import javax.servlet.jsp.JspException;

import java.util.Locale;

/**
 * <p>ConvertNumberTag is a ConverterTag implementation for
 * javax.faces.convert.NumberConverter</p>
 *
 * @version $Id: ConvertNumberTag.java,v 1.8 2004/02/06 18:55:41 rlubke Exp $
 */

public class ConvertNumberTag extends ConverterTag {

    //
    // Protected Constants
    //

    //
    // Class Variables
    //

    //
    // Instance Variables
    //
    private String currencyCode;
    private String currencyCode_;
    private String currencySymbol;
    private String currencySymbol_;
    private boolean groupingUsed;
    private String groupingUsed_;
    private boolean integerOnly;
    private String integerOnly_;
    private int maxFractionDigits;
    private String maxFractionDigits_;
    private boolean maxFractionDigitsSpecified;
    private int maxIntegerDigits;
    private String maxIntegerDigits_;
    private boolean maxIntegerDigitsSpecified;
    private int minFractionDigits;
    private String minFractionDigits_;
    private boolean minFractionDigitsSpecified;
    private int minIntegerDigits;
    private String minIntegerDigits_;
    private boolean minIntegerDigitsSpecified;
    private String locale_;
    private Locale locale;
    private String pattern;
    private String pattern_;
    private String type;
    private String type_;


    // Attribute Instance Variables
    
    // Relationship Instance Variables

    //
    // Constructors and Initializers    
    //
    public ConvertNumberTag() {
        super();
        super.setConverterId("javax.faces.Number");
        init();
    }


    public void release() {
        super.release();
        init();
    }


    private void init() {
        currencyCode = null;
        currencyCode_ = null;
        currencySymbol = null;
        currencySymbol_ = null;
        groupingUsed = true;
        groupingUsed_ = null;
        integerOnly = false;
        integerOnly_ = null;
        maxFractionDigits = 0;
        maxFractionDigits_ = null;
        maxFractionDigitsSpecified = false;
        maxIntegerDigits = 0;
        maxIntegerDigits_ = null;
        maxIntegerDigitsSpecified = false;
        minFractionDigits = 0;
        minFractionDigits_ = null;
        minFractionDigitsSpecified = false;
        minIntegerDigits = 0;
        minIntegerDigits_ = null;
        minIntegerDigitsSpecified = false;
        locale = null;
        locale_ = null;
        pattern = null;
        pattern_ = null;
        type = "number";
        type_ = "number";
    }


    //
    // Class methods
    //

    //
    // General Methods
    //

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode_ = currencyCode;
    }


    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol_ = currencySymbol;
    }


    public void setGroupingUsed(String groupingUsed) {
        this.groupingUsed_ = groupingUsed;
    }


    public void setIntegerOnly(String integerOnly) {
        this.integerOnly_ = integerOnly;
    }


    public void setMaxFractionDigits(String maxFractionDigits) {
        this.maxFractionDigits_ = maxFractionDigits;
        this.maxFractionDigitsSpecified = true;
    }


    public void setMaxIntegerDigits(String maxIntegerDigits) {
        this.maxIntegerDigits_ = maxIntegerDigits;
        this.maxIntegerDigitsSpecified = true;
    }


    public void setMinFractionDigits(String minFractionDigits) {
        this.minFractionDigits_ = minFractionDigits;
        this.minFractionDigitsSpecified = true;
    }


    public void setMinIntegerDigits(String minIntegerDigits) {
        this.minIntegerDigits_ = minIntegerDigits;
    }


    public void setLocale(String locale) {
        this.locale_ = locale;
    }


    public void setPattern(String pattern) {
        this.pattern_ = pattern;
    }


    public void setType(String type) {
        this.type_ = type;
    }


    // 
    // Methods from ConverterTag
    // 

    protected Converter createConverter() throws JspException {

        NumberConverter result = null;

        result = (NumberConverter) super.createConverter();
        Util.doAssert(null != result);

        evaluateExpressions();
        result.setCurrencyCode(currencyCode);
        result.setCurrencySymbol(currencySymbol);
        result.setGroupingUsed(groupingUsed);
        result.setIntegerOnly(integerOnly);
        if (maxFractionDigitsSpecified) {
            result.setMaxFractionDigits(maxFractionDigits);
        }
        if (maxIntegerDigitsSpecified) {
            result.setMaxIntegerDigits(maxIntegerDigits);
        }
        if (minFractionDigitsSpecified) {
            result.setMinFractionDigits(minFractionDigits);
        }
        if (minIntegerDigitsSpecified) {
            result.setMinIntegerDigits(minIntegerDigits);
        }
        result.setLocale(locale);
        result.setPattern(pattern);
        result.setType(type);

        return result;
    }


    /* Evaluates expressions as necessary */
    private void evaluateExpressions() throws JspException {
        Integer intObj = null;

        if (currencyCode_ != null) {
            currencyCode = (String) Util.evaluateVBExpression(currencyCode_);
        }
        if (currencySymbol_ != null) {
            currencySymbol =
                (String) Util.evaluateVBExpression(currencySymbol_);
        }
        if (pattern_ != null) {
            pattern = (String) Util.evaluateVBExpression(pattern_);
        }
        if (type_ != null) {
            type = (String) Util.evaluateVBExpression(type_);
        }
        if (groupingUsed_ != null) {
            if (Util.isVBExpression(groupingUsed_)) {
                Boolean booleanObj = (Boolean) Util.evaluateVBExpression(
                    groupingUsed_);
                Util.doAssert(null != booleanObj);
                groupingUsed = booleanObj.booleanValue();
            } else {
                groupingUsed = new Boolean(groupingUsed_).booleanValue();
            }
        }
        if (integerOnly_ != null) {
            if (Util.isVBExpression(integerOnly_)) {
                Boolean booleanObj = (Boolean) Util.evaluateVBExpression(
                    integerOnly_);
                Util.doAssert(null != booleanObj);
                integerOnly = booleanObj.booleanValue();
            } else {
                integerOnly = new Boolean(integerOnly_).booleanValue();
            }
        }
        if (maxFractionDigits_ != null) {
            if (Util.isVBExpression(maxFractionDigits_)) {
                intObj =
                    (Integer) Util.evaluateVBExpression(maxFractionDigits_);
                Util.doAssert(null != intObj);
                maxFractionDigits = intObj.intValue();
            } else {
                maxFractionDigits = new Integer(maxFractionDigits_).intValue();
            }
        }
        if (maxIntegerDigits_ != null) {
            if (Util.isVBExpression(maxIntegerDigits_)) {
                intObj = (Integer) Util.evaluateVBExpression(maxIntegerDigits_);
                Util.doAssert(null != intObj);
                maxIntegerDigits = intObj.intValue();
            } else {
                maxIntegerDigits = new Integer(maxIntegerDigits_).intValue();
            }
        }
        if (minFractionDigits_ != null) {
            if (Util.isVBExpression(minFractionDigits_)) {
                intObj =
                    (Integer) Util.evaluateVBExpression(minFractionDigits_);
                Util.doAssert(null != intObj);
                minFractionDigits = intObj.intValue();
            } else {
                minFractionDigits = new Integer(minFractionDigits_).intValue();
            }
        }
        if (minIntegerDigits_ != null) {
            if (Util.isVBExpression(minIntegerDigits_)) {
                intObj = (Integer) Util.evaluateVBExpression(minIntegerDigits_);
                Util.doAssert(null != intObj);
                minIntegerDigits = intObj.intValue();
            } else {
                minIntegerDigits = new Integer(minIntegerDigits_).intValue();
            }
        }
        if (locale_ != null) {
            if (Util.isVBExpression(locale_)) {
                locale = (Locale) Util.evaluateVBExpression(locale_);
            } else {
                locale = new Locale(locale_, "");
            }
        }
    }

} // end of class ConvertNumberTag

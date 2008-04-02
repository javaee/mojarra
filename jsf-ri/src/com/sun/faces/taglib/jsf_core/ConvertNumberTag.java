/*
 * $Id: ConvertNumberTag.java,v 1.1 2003/08/25 05:39:47 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.taglib.jsf_core;

import java.util.Locale;

import javax.faces.convert.Converter;
import javax.faces.convert.NumberConverter;
import javax.faces.webapp.ConverterTag;

import javax.servlet.jsp.JspException;
import org.mozilla.util.Assert;

/**
 * <p>ConvertNumberTag is a ConverterTag implementation for
 * javax.faces.convert.NumberConverter</p>
 *
 *
 * @version $Id: ConvertNumberTag.java,v 1.1 2003/08/25 05:39:47 eburns Exp $
 * 
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
    private String currencySymbol;
    private boolean groupingUsed;
    private boolean integerOnly;
    private int maxFractionDigits;
    private boolean maxFractionDigitsSpecified;
    private int maxIntegerDigits;
    private boolean maxIntegerDigitsSpecified;
    private int minFractionDigits;
    private boolean minFractionDigitsSpecified;
    private int minIntegerDigits;
    private boolean minIntegerDigitsSpecified;
    private Locale parseLocale;
    private String pattern;
    private String type;


    // Attribute Instance Variables
    
    // Relationship Instance Variables

    //
    // Constructors and Initializers    
    //
    public ConvertNumberTag() {
        super();
        super.setId("Number");
        init();
    }

    public void release() {
        super.release();
        init();
    }

    private void init() {
        currencyCode = null;
        currencySymbol = null;
        groupingUsed = true;
        integerOnly = false;
        maxFractionDigits = 0;
        maxFractionDigitsSpecified = false;
        maxIntegerDigits = 0;
        maxIntegerDigitsSpecified = false;
        minFractionDigits = 0;
        minFractionDigitsSpecified = false;
        minIntegerDigits = 0;
        minIntegerDigitsSpecified = false;
        parseLocale = null;
        pattern = null;
        type = "number";
    }


    //
    // Class methods
    //

    //
    // General Methods
    //

    public String getCurrencyCode() {
        return (this.currencyCode);
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getCurrencySymbol() {
        return (this.currencySymbol);
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }

    public boolean isGroupingUsed() {
        return (this.groupingUsed);
    }

    public void setGroupingUsed(boolean groupingUsed) {
        this.groupingUsed = groupingUsed;
    }

    public boolean isIntegerOnly() {
        return (this.integerOnly);
    }

    public void setIntegerOnly(boolean integerOnly) {
        this.integerOnly = integerOnly;
    }

    public int getMaxFractionDigits() {
        return (this.maxFractionDigits);
    }

    public void setMaxFractionDigits(int maxFractionDigits) {
        this.maxFractionDigits = maxFractionDigits;
        this.maxFractionDigitsSpecified = true;
    }

    public int getMaxIntegerDigits() {
        return (this.maxIntegerDigits);
    }

    public void setMaxIntegerDigits(int maxIntegerDigits) {
        this.maxIntegerDigits = maxIntegerDigits;
        this.maxIntegerDigitsSpecified = true;
    }

    public int getMinFractionDigits() {
        return (this.minFractionDigits);
    }

    public void setMinFractionDigits(int minFractionDigits) {
        this.minFractionDigits = minFractionDigits;
        this.minFractionDigitsSpecified = true;
    }

    public int getMinIntegerDigits() {
        return (this.minIntegerDigits);
    }

    public void setMinIntegerDigits(int minIntegerDigits) {
        this.minIntegerDigits = minIntegerDigits;
        this.minIntegerDigitsSpecified = true;
    }

    public Locale getParseLocale() {
        return (this.parseLocale);
    }

    public void setParseLocale(Locale parseLocale) {
        this.parseLocale = parseLocale;
    }

    public String getPattern() {
        return (this.pattern);
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getType() {
        return (this.type);
    }

    public void setType(String type) {
        this.type = type;
    }


    // 
    // Methods from ConverterTag
    // 

    protected Converter createConverter() throws JspException {

        NumberConverter result = null;

        result = (NumberConverter) super.createConverter();
        Assert.assert_it(null != result);

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
        result.setParseLocale(parseLocale);
        result.setPattern(pattern);
        result.setType(type);

        return result;
    }



} // end of class ConvertNumberTag

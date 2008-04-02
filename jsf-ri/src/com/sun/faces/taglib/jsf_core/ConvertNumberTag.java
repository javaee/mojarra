/*
 * $Id: ConvertNumberTag.java,v 1.19 2007/03/01 15:51:37 rlubke Exp $
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

import java.util.Locale;

import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.el.ExpressionFactory;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.NumberConverter;
import javax.servlet.jsp.JspException;

import com.sun.faces.el.ELUtils;

/**
 * <p>ConvertNumberTag is a ConverterTag implementation for
 * javax.faces.convert.NumberConverter</p>
 *
 * @version $Id: ConvertNumberTag.java,v 1.19 2007/03/01 15:51:37 rlubke Exp $
 */

public class ConvertNumberTag extends AbstractConverterTag {

    private static final long serialVersionUID = -2710405278792415110L;
    private static ValueExpression CONVERTER_ID_EXPR = null;

    //
    // Instance Variables
    //

    private ValueExpression currencyCodeExpression;
    private ValueExpression currencySymbolExpression;
    private ValueExpression groupingUsedExpression;
    private ValueExpression integerOnlyExpression;
    private ValueExpression maxFractionDigitsExpression;
    private ValueExpression maxIntegerDigitsExpression;
    private ValueExpression minFractionDigitsExpression;
    private ValueExpression minIntegerDigitsExpression;
    private ValueExpression localeExpression;
    private ValueExpression patternExpression;
    private ValueExpression typeExpression;

    private String currencyCode;
    private String currencySymbol;
    private boolean groupingUsed;
    private boolean integerOnly;
    private int maxFractionDigits;
    private int maxIntegerDigits;
    private int minFractionDigits;
    private int minIntegerDigits;
    private Locale locale;
    private String pattern;
    private String type;

    private boolean maxFractionDigitsSpecified;
    private boolean maxIntegerDigitsSpecified;
    private boolean minFractionDigitsSpecified;
    private boolean minIntegerDigitsSpecified;

    // Attribute Instance Variables
    
    // Relationship Instance Variables

    //
    // Constructors and Initializers    
    //
    public ConvertNumberTag() {
        super();
        init();
    }


    public void release() {
        super.release();
        init();
    }


    private void init() {
        currencyCode = null;
        currencyCodeExpression = null;
        currencySymbol = null;
        currencySymbolExpression = null;
        groupingUsed = true;
        groupingUsedExpression = null;
        integerOnly = false;
        integerOnlyExpression = null;
        maxFractionDigits = 0;
        maxFractionDigitsExpression = null;
        maxFractionDigitsSpecified = false;
        maxIntegerDigits = 0;
        maxIntegerDigitsExpression = null;
        maxIntegerDigitsSpecified = false;
        minFractionDigits = 0;
        minFractionDigitsExpression = null;
        minFractionDigitsSpecified = false;
        minIntegerDigits = 0;
        minIntegerDigitsExpression = null;
        minIntegerDigitsSpecified = false;
        locale = null;
        localeExpression = null;
        pattern = null;
        patternExpression = null;
        type = "number";
        typeExpression = null;
        if (CONVERTER_ID_EXPR == null) {
            FacesContext context = FacesContext.getCurrentInstance();
            ExpressionFactory factory =
                context.getApplication().getExpressionFactory();
            CONVERTER_ID_EXPR =
                factory.createValueExpression(context.getELContext(), 
                    "javax.faces.Number", String.class);
        }
    }


    //
    // Class methods
    //

    //
    // General Methods
    //

    public void setCurrencyCode(ValueExpression currencyCode) {
        this.currencyCodeExpression = currencyCode;
    }


    public void setCurrencySymbol(ValueExpression currencySymbol) {
        this.currencySymbolExpression = currencySymbol;
    }


    public void setGroupingUsed(ValueExpression groupingUsed) {
        this.groupingUsedExpression = groupingUsed;
    }


    public void setIntegerOnly(ValueExpression integerOnly) {
        this.integerOnlyExpression = integerOnly;
    }


    public void setMaxFractionDigits(ValueExpression maxFractionDigits) {
        this.maxFractionDigitsExpression = maxFractionDigits;
        this.maxFractionDigitsSpecified = true;
    }


    public void setMaxIntegerDigits(ValueExpression maxIntegerDigits) {
        this.maxIntegerDigitsExpression = maxIntegerDigits;
        this.maxIntegerDigitsSpecified = true;
    }


    public void setMinFractionDigits(ValueExpression minFractionDigits) {
        this.minFractionDigitsExpression = minFractionDigits;
        this.minFractionDigitsSpecified = true;
    }


    public void setMinIntegerDigits(ValueExpression minIntegerDigits) {
        this.minIntegerDigitsExpression = minIntegerDigits;
    }


    public void setLocale(ValueExpression locale) {
        this.localeExpression = locale;
    }


    public void setPattern(ValueExpression pattern) {
        this.patternExpression = pattern;
    }


    public void setType(ValueExpression type) {
        this.typeExpression = type;
    }

    public int doStartTag() throws JspException {
        super.setConverterId(CONVERTER_ID_EXPR);
        return super.doStartTag();
    }

    // 
    // Methods from ConverterTag
    // 

    protected Converter createConverter() throws JspException {

        NumberConverter result = (NumberConverter) super.createConverter();
        assert (null != result);

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
    private void evaluateExpressions() {

        FacesContext facesContext = FacesContext.getCurrentInstance();
        ELContext elContext = facesContext.getELContext();

        if (currencyCodeExpression != null) {
            currencyCode = (String)
            ELUtils.evaluateValueExpression(currencyCodeExpression,
                elContext);
        }
        if (currencySymbolExpression != null) {
            currencySymbol = (String)
            ELUtils.evaluateValueExpression(currencySymbolExpression,
                elContext);
        }
        if (patternExpression != null) {
            pattern = (String)
            ELUtils.evaluateValueExpression(patternExpression,
                elContext);
        }
        if (typeExpression != null) {
            type = (String)
            ELUtils.evaluateValueExpression(typeExpression,
                elContext);
        }
        if (groupingUsedExpression != null) {
            if (groupingUsedExpression.isLiteralText()) {
                groupingUsed =
                Boolean.valueOf(
                    groupingUsedExpression.getExpressionString()).
                    booleanValue();
            } else {
                groupingUsed = ((Boolean)
                                   ELUtils.evaluateValueExpression(groupingUsedExpression,
                                       elContext)).booleanValue();
            }
        }
        if (integerOnlyExpression != null) {
            if (integerOnlyExpression.isLiteralText()) {
                integerOnly =
                Boolean.valueOf(
                    integerOnlyExpression.getExpressionString()).
                    booleanValue();
            } else {
                integerOnly = ((Boolean)
                                  ELUtils.evaluateValueExpression(integerOnlyExpression,
                                      elContext)).booleanValue();
            }
        }
        if (maxFractionDigitsExpression != null) {
            if (maxFractionDigitsExpression.isLiteralText()) {
                maxFractionDigits =
                Integer.valueOf(
                    maxFractionDigitsExpression.getExpressionString()).
                    intValue();
            } else {
                maxFractionDigits = ((Integer)
                                        ELUtils.evaluateValueExpression(maxFractionDigitsExpression,
                                            elContext)).intValue();
            }
        }
        if (maxIntegerDigitsExpression != null) {
            if (maxIntegerDigitsExpression.isLiteralText()) {
                maxIntegerDigits =
                Integer.valueOf(
                    maxIntegerDigitsExpression.getExpressionString()).
                    intValue();
            } else {
                maxIntegerDigits = ((Integer)
                                       ELUtils.evaluateValueExpression(maxIntegerDigitsExpression,
                                           elContext)).intValue();
            }
        }
        if (minFractionDigitsExpression != null) {
            if (minFractionDigitsExpression.isLiteralText()) {
                minFractionDigits =
                Integer.valueOf(
                    minFractionDigitsExpression.getExpressionString()).
                    intValue();
            } else {
                minFractionDigits = ((Integer)
                                        ELUtils.evaluateValueExpression(minFractionDigitsExpression,
                                            elContext)).intValue();
            }
        }
        if (minIntegerDigitsExpression != null) {
            if (minIntegerDigitsExpression.isLiteralText()) {
                minIntegerDigits =
                Integer.valueOf(
                    minIntegerDigitsExpression.getExpressionString()).
                    intValue();
            } else {
                minIntegerDigits = ((Integer)
                                       ELUtils.evaluateValueExpression(minIntegerDigitsExpression,
                                           elContext)).intValue();
            }
        }
        if (localeExpression != null) {
            if (localeExpression.isLiteralText()) {
                locale = new Locale(localeExpression.getExpressionString(),
                    "");
            } else {
                Locale loc = (Locale)
                ELUtils.evaluateValueExpression(localeExpression,
                    elContext);
                if (loc != null) {
                    locale = loc;
                } else {
                    locale = facesContext.getViewRoot().getLocale();
                }
            }
        }
    }

} // end of class ConvertNumberTag

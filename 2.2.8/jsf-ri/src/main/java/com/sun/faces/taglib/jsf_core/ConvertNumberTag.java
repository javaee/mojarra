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

import java.util.Arrays;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sun.faces.util.FacesLogger;
import com.sun.faces.util.MessageUtils;

import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.el.ExpressionFactory;

import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.NumberConverter;
import javax.servlet.jsp.JspException;

import com.sun.faces.el.ELUtils;

/**
 * <p>ConvertNumberTag is a ConverterTag implementation for
 * javax.faces.convert.NumberConverter</p>
 *
 */

public class ConvertNumberTag extends AbstractConverterTag {

    private static final long serialVersionUID = -2710405278792415110L;
    private static ValueExpression CONVERTER_ID_EXPR = null;
    
    private static final Logger LOGGER = FacesLogger.TAGLIB.getLogger();


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
                locale = getLocale(localeExpression.getExpressionString());
            } else {
                Object loc = ELUtils.evaluateValueExpression(localeExpression,
                                                          elContext);
                if (loc != null) {
                    if (loc instanceof String) {
                        locale = getLocale((String) loc);
                    } else if (loc instanceof Locale) {
                        locale = (Locale) loc;
                    } else {
                        Object[] params = {
                            "locale",
                            "java.lang.String or java.util.Locale",
                            loc.getClass().getName()
                        };
                        if (LOGGER.isLoggable(Level.SEVERE)) {
                            LOGGER.log(Level.SEVERE,
                                       "jsf.core.tags.eval_result_not_expected_type",
                                       params);
                        }
                        throw new FacesException(
                            MessageUtils.getExceptionMessageString(
                                MessageUtils.EVAL_ATTR_UNEXPECTED_TYPE, params));
                    }
                } else {
                    locale = facesContext.getViewRoot().getLocale();
                }
            }
        }

    }


    protected static Locale getLocale(String string) {
        if (string == null) {
            return Locale.getDefault();
        }

        if (string.length() > 2) {
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.log(Level.WARNING,
                           "jsf.core.taglib.invalid_locale_value",
                           string);
            }
        } else {
            String[] langs = Locale.getISOLanguages();
            Arrays.sort(langs);
            if (Arrays.binarySearch(langs, string) < 0) {
                if (LOGGER.isLoggable(Level.WARNING)) {
                    LOGGER.log(Level.WARNING,
                               "jsf.core.taglib.invalid_language",
                               string);
            }
        }
    }

        return new Locale(string, "");
    }
    
} // end of class ConvertNumberTag

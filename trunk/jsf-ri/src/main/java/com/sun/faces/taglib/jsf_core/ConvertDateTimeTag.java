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

import com.sun.faces.el.ELUtils;
import com.sun.faces.util.FacesLogger;
import com.sun.faces.util.MessageUtils;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.DateTimeConverter;
import javax.servlet.jsp.JspException;
import java.util.Arrays;
import java.util.Locale;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * <p>ConvertDateTimeTag is a ConverterTag implementation for
 * javax.faces.convert.DateTimeConverter</p>
 *
 */

public class ConvertDateTimeTag extends AbstractConverterTag {

    private static final long serialVersionUID = -5815655767093677438L;
    private static ValueExpression CONVERTER_ID_EXPR = null;

     private static final Logger LOGGER = FacesLogger.TAGLIB.getLogger();

    //
    // Instance Variables
    //

    private ValueExpression dateStyleExpression;
    private ValueExpression localeExpression;
    private ValueExpression patternExpression;
    private ValueExpression timeStyleExpression;
    private ValueExpression timeZoneExpression;
    private ValueExpression typeExpression;

    private String dateStyle;
    private Locale locale;
    private String pattern;
    private String timeStyle;
    private TimeZone timeZone;
    private String type;// Log instance for this class


    // Attribute Instance Variables

    // Relationship Instance Variables

    //
    // Constructors and Initializers    
    //
    public ConvertDateTimeTag() {
        super();
        init();
    }


    public void release() {
        super.release();
        init();
    }


    private void init() {
        dateStyle = "default";
        dateStyleExpression = null;
        locale = null;
        localeExpression = null;
        pattern = null;
        patternExpression = null;
        timeStyle = "default";
        timeStyleExpression = null;
        timeZone = null;
        timeZoneExpression = null;
        type = "date";
        typeExpression = null;
        if (CONVERTER_ID_EXPR == null) {
            FacesContext context = FacesContext.getCurrentInstance();
            ExpressionFactory factory = context.getApplication().
                    getExpressionFactory();
            CONVERTER_ID_EXPR = factory.createValueExpression(
                    context.getELContext(),"javax.faces.DateTime",String.class);
        }
    }

    //
    // Class methods
    //

    //
    // General Methods
    //

    public void setDateStyle(ValueExpression dateStyle) {
        this.dateStyleExpression = dateStyle;
    }


    public void setLocale(ValueExpression locale) {
        this.localeExpression = locale;
    }


    public void setPattern(ValueExpression pattern) {
        this.patternExpression = pattern;
    }


    public void setTimeStyle(ValueExpression timeStyle) {
        this.timeStyleExpression = timeStyle;
    }


    public void setTimeZone(ValueExpression timeZone) {
        this.timeZoneExpression = timeZone;
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

        DateTimeConverter result = (DateTimeConverter) super.createConverter();
        assert (null != result);

        evaluateExpressions();
        result.setDateStyle(dateStyle);
        result.setLocale(locale);
        result.setPattern(pattern);
        result.setTimeStyle(timeStyle);
        result.setTimeZone(timeZone);
        result.setType(type);

        return result;
    }


    /* Evaluates expressions as necessary */
    private void evaluateExpressions() {

        FacesContext facesContext = FacesContext.getCurrentInstance();
        ELContext elContext = facesContext.getELContext();

        if (dateStyleExpression != null) {
            dateStyle = (String)
            ELUtils.evaluateValueExpression(dateStyleExpression, elContext);
        }
        if (patternExpression != null) {
            pattern = (String)
            ELUtils.evaluateValueExpression(patternExpression, elContext);
        }
        if (timeStyleExpression != null) {
            timeStyle = (String)
            ELUtils.evaluateValueExpression(timeStyleExpression, elContext);
        }
        if (typeExpression != null) {
            type = (String)
            ELUtils.evaluateValueExpression(typeExpression, elContext);
        } else {
            if (timeStyleExpression != null) {
                if (dateStyleExpression != null) {
                    type = "both";
                } else {
                    type = "time";
                }
            } else {
                type = "date";
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
        if (timeZoneExpression != null) {
            if (timeZoneExpression.isLiteralText()) {
                timeZone =
                TimeZone.getTimeZone(
                    timeZoneExpression.getExpressionString());
            } else {
                Object tz = ELUtils.evaluateValueExpression(timeZoneExpression,
                                                         elContext);
                if (tz != null) {
                    if (tz instanceof String) {
                        timeZone = TimeZone.getTimeZone((String) tz);
                    } else if (tz instanceof TimeZone) {
                        timeZone = (TimeZone) tz;
                    } else {
                        Object[] params = {
                            "timeZone",
                            "java.lang.String or java.util.TimeZone",
                            tz.getClass().getName()
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
} // end of class ConvertDateTimeTag

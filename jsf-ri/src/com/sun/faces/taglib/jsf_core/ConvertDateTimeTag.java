/*
 * $Id: ConvertDateTimeTag.java,v 1.12 2004/04/06 14:18:19 eburns Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.taglib.jsf_core;

import com.sun.faces.util.Util;

import javax.faces.convert.Converter;
import javax.faces.convert.DateTimeConverter;
import javax.faces.webapp.ConverterTag;
import javax.servlet.jsp.JspException;

import java.util.Locale;
import java.util.TimeZone;


/**
 * <p>ConvertDateTimeTag is a ConverterTag implementation for
 * javax.faces.convert.DateTimeConverter</p>
 *
 * @version $Id: ConvertDateTimeTag.java,v 1.12 2004/04/06 14:18:19 eburns Exp $
 */

public class ConvertDateTimeTag extends ConverterTag {

    //
    // Protected Constants
    //

    //
    // Class Variables
    //

    //
    // Instance Variables
    //
    private String dateStyle;
    private String dateStyle_;
    private String locale_;
    private Locale locale;
    private String pattern;
    private String pattern_;
    private String timeStyle;
    private String timeStyle_;
    private String timeZone_;
    private TimeZone timeZone;
    private String type;
    private String type_;

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
        dateStyle_ = null;
        locale = null;
        locale_ = null;
        pattern = null;
        pattern_ = null;
        timeStyle = "default";
        timeStyle_ = null;
        timeZone = null;
        timeZone_ = null;
        type = "date";
        type_ = null;
    }

    //
    // Class methods
    //

    //
    // General Methods
    //

    public void setDateStyle(String dateStyle) {
        this.dateStyle_ = dateStyle;
    }


    public void setLocale(String locale) {
        this.locale_ = locale;
    }


    public void setPattern(String pattern) {
        this.pattern_ = pattern;
    }


    public void setTimeStyle(String timeStyle) {
        this.timeStyle_ = timeStyle;
    }


    public void setTimeZone(String timeZone) {
        this.timeZone_ = timeZone;
    }


    public void setType(String type) {
        this.type_ = type;
    }

    public int doStartTag() throws JspException {
        super.setConverterId("javax.faces.DateTime");
	return super.doStartTag();
    }

    // 
    // Methods from ConverterTag
    // 

    protected Converter createConverter() throws JspException {

        DateTimeConverter result = null;

        result = (DateTimeConverter) super.createConverter();
        Util.doAssert(null != result);

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
    private void evaluateExpressions() throws JspException {
        if (dateStyle_ != null) {
            dateStyle = (String) Util.evaluateVBExpression(dateStyle_);
        }
        if (pattern_ != null) {
            pattern = (String) Util.evaluateVBExpression(pattern_);
        }
        if (timeStyle_ != null) {
            timeStyle = (String) Util.evaluateVBExpression(timeStyle_);
        }
        if (type_ != null) {
            type = (String) Util.evaluateVBExpression(type_);
        } else {
            if (timeStyle_ != null) {
                if (dateStyle_ != null) {
                    type = "both";
                } else {
                    type = "time";
                }
            } else {
                type = "date";
            }
        }
        if (locale_ != null) {
            if (Util.isVBExpression(locale_)) {
                locale = (Locale) Util.evaluateVBExpression(locale_);
            } else {
                locale = new Locale(locale_, "");
            }
        }
        if (timeZone_ != null) {
            if (Util.isVBExpression(timeZone_)) {
                timeZone = (TimeZone) Util.evaluateVBExpression(timeZone_);
            } else {
                timeZone = TimeZone.getTimeZone(timeZone_);
            }
        }
    }

} // end of class ConvertDateTimeTag

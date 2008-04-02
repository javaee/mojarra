/*
 * $Id: ConvertDateTimeTag.java,v 1.3 2003/10/07 20:16:04 horwat Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.taglib.jsf_core;

import java.util.Locale;
import java.util.TimeZone;

import javax.faces.convert.Converter;
import javax.faces.convert.DateTimeConverter;
import javax.faces.webapp.ConverterTag;

import javax.servlet.jsp.JspException;
import org.mozilla.util.Assert;

import com.sun.faces.util.Util;


/**
 * <p>ConvertDateTimeTag is a ConverterTag implementation for 
 * javax.faces.convert.DateTimeConverter</p>
 *
 * @version $Id: ConvertDateTimeTag.java,v 1.3 2003/10/07 20:16:04 horwat Exp $
 * 
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
    private Locale parseLocale;
    private String pattern;
    private String pattern_;
    private String timeStyle;
    private String timeStyle_;
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
        super.setId("DateTime");
        init();
    }

    public void release() {
        super.release();
        init();
    }

    private void init() {
        dateStyle = "default";
        dateStyle_ = "default";
        parseLocale = null;
        pattern = null;
        pattern_ = null;
        timeStyle = "default";
        timeStyle_ = "default";
        timeZone = null;
        type = "date";
        type_ = "date";
    }

    //
    // Class methods
    //

    //
    // General Methods
    //

    public String getDateStyle() {
        return (this.dateStyle);
    }

    public void setDateStyle(String dateStyle) {
        this.dateStyle_ = dateStyle;
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
        this.pattern_ = pattern;
    }

    public String getTimeStyle() {
        return (this.timeStyle);
    }

    public void setTimeStyle(String timeStyle) {
        this.timeStyle_ = timeStyle;
    }

    public TimeZone getTimeZone() {
        return (this.timeZone);
    }

    public void setTimeZone(TimeZone timeZone) {
        this.timeZone = timeZone;
    }

    public String getType() {
        return (this.type);
    }

    public void setType(String type) {
        this.type_ = type;
    }


    // 
    // Methods from ConverterTag
    // 

    protected Converter createConverter() throws JspException {

        DateTimeConverter result = null;

        result = (DateTimeConverter) super.createConverter();
        Assert.assert_it(null != result);

        result.setDateStyle(dateStyle);
        result.setLocale(parseLocale);
        result.setPattern(pattern);
        result.setTimeStyle(timeStyle);
        result.setTimeZone(timeZone);
        result.setType(type);

        return result;
    }

    /* Evaluates expressions as necessary */
    private void evaluateExpressions() throws JspException {
        if (dateStyle_ != null) {
            dateStyle = Util.evaluateElExpression(dateStyle_, pageContext);
        }
        if (pattern_ != null) {
            pattern = Util.evaluateElExpression(pattern_, pageContext);
        }
        if (timeStyle_ != null) {
            timeStyle = Util.evaluateElExpression(timeStyle_, pageContext);
        }
        if (type_ != null) {
            type = Util.evaluateElExpression(type_, pageContext);
        }
    }

    //
    // Methods from TagSupport
    //

    public int doStartTag() throws JspException {
        // evaluate any expressions that we were passed
        evaluateExpressions();

        // chain to the parent implementation
        return super.doStartTag();
    }




} // end of class ConvertDateTimeTag

/*
 * $Id: ConvertDateTimeTag.java,v 1.1 2003/08/25 05:39:46 eburns Exp $
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


/**
 * <p>ConvertDateTimeTag is a ConverterTag implementation for 
 * javax.faces.convert.DateTimeConverter</p>
 *
 * @version $Id: ConvertDateTimeTag.java,v 1.1 2003/08/25 05:39:46 eburns Exp $
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
    private Locale parseLocale;
    private String pattern;
    private String timeStyle;
    private TimeZone timeZone;
    private String type;

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
        parseLocale = null;
        pattern = null;
        timeStyle = "default";
        timeZone = null;
        type = "date";
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
        this.dateStyle = dateStyle;
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

    public String getTimeStyle() {
        return (this.timeStyle);
    }

    public void setTimeStyle(String timeStyle) {
        this.timeStyle = timeStyle;
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
        this.type = type;
    }


    // 
    // Methods from ConverterTag
    // 

    protected Converter createConverter() throws JspException {

        DateTimeConverter result = null;

        result = (DateTimeConverter) super.createConverter();
        Assert.assert_it(null != result);

        result.setDateStyle(dateStyle);
        result.setParseLocale(parseLocale);
        result.setPattern(pattern);
        result.setTimeStyle(timeStyle);
        result.setTimeZone(timeZone);
        result.setType(type);

        return result;
    }



} // end of class ConvertDateTimeTag

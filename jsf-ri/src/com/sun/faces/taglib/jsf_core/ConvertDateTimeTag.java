/*
 * $Id: ConvertDateTimeTag.java,v 1.12.30.2 2007/04/27 21:27:47 ofung Exp $
 */

/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 * 
 * Contributor(s):
 * 
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
 * @version $Id: ConvertDateTimeTag.java,v 1.12.30.2 2007/04/27 21:27:47 ofung Exp $
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

/*
 * $Id: DateTimeRenderer.java,v 1.1 2002/08/15 23:23:00 eburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// DateTimeRenderer.java

package com.sun.faces.renderkit.html_basic;

import com.sun.faces.util.Util;

import java.util.Iterator;
import java.util.TimeZone;
import java.util.Date;
import java.util.Calendar;
import java.lang.Long;

import javax.faces.component.AttributeDescriptor;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.FacesException;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.Log;
import org.mozilla.util.ParameterCheck;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.ConversionException;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

import java.text.DateFormat;
import java.text.ParseException;

import com.sun.faces.renderkit.FormatPool;
import com.sun.faces.RIConstants;

/**
 *
 *  <B>DateTimeRenderer</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: DateTimeRenderer.java,v 1.1 2002/08/15 23:23:00 eburns Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class DateTimeRenderer extends DateRenderer {
    //
    // Protected Constants
    //

    //
    // Class Variables
    //

    //
    // Instance Variables
    //

    // Attribute Instance Variables


    // Relationship Instance Variables

    //
    // Constructors and Initializers    
    //

    public DateTimeRenderer() {
        super();
    }

    //
    // Class methods
    //

    //
    // General Methods
    //

    //
    // Methods From DateRenderer
    //

    protected Date parseDate(FacesContext context, 
			  UIComponent component, String newValue) throws ParseException {
	FormatPool formatPool = null;
	formatPool = (FormatPool)
	    context.getServletContext().getAttribute(RIConstants.FORMAT_POOL);
	Assert.assert_it(null != formatPool);
	return formatPool.dateTimeFormat_parse(context, component, newValue);
    }

    protected String formatDate(FacesContext context, 
			     UIComponent component, Date dateValue) {
	FormatPool formatPool = null;
	formatPool = (FormatPool)
	    context.getServletContext().getAttribute(RIConstants.FORMAT_POOL);
	Assert.assert_it(null != formatPool);
	return formatPool.dateTimeFormat_format(context, component, dateValue);
    }    

    // The testcase for this class is TestRenderers_2.java 

} // end of class DateTimeRenderer



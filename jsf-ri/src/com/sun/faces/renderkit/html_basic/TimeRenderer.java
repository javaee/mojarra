/*
 * $Id: TimeRenderer.java,v 1.2 2003/02/20 22:49:02 ofung Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TimeRenderer.java

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
 *  <B>TimeRenderer</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TimeRenderer.java,v 1.2 2003/02/20 22:49:02 ofung Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class TimeRenderer extends DateRenderer {
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

    public TimeRenderer() {
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
	return formatPool.timeFormat_parse(context, component, newValue);
    }

    protected String formatDate(FacesContext context, 
				UIComponent component, Date dateValue) {
	FormatPool formatPool = null;
	formatPool = (FormatPool)
	    context.getServletContext().getAttribute(RIConstants.FORMAT_POOL);
	Assert.assert_it(null != formatPool);
	return formatPool.timeFormat_format(context, component, dateValue);
    }    

    // The testcase for this class is TestRenderers_2.java 

} // end of class TimeRenderer



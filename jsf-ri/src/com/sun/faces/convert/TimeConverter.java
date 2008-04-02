/*
 * $Id: TimeConverter.java,v 1.3 2003/03/21 23:22:02 rkitain Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.convert;


import com.sun.faces.RIConstants;
import com.sun.faces.renderkit.FormatPool;
import com.sun.faces.util.Util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import org.mozilla.util.Assert;

/**
 * Implement converter support so that <code>&lt;input_text&gt;</code> can
 * be used to implement the equivalent of <code>&lt;input_time&gt;</code>
 */
public class TimeConverter extends DateConverter {

    protected Date parseDate(FacesContext context, UIComponent component, 
        String newValue) throws ParseException {
        Map applicationMap = context.getExternalContext().getApplicationMap();
        FormatPool formatPool = (FormatPool)applicationMap.get(RIConstants.FORMAT_POOL);
        Assert.assert_it(null != formatPool);
        return formatPool.timeFormat_parse(context, component, newValue);
    }

    protected String formatDate(FacesContext context, UIComponent component, 
        Date dateValue) {
        Map applicationMap = context.getExternalContext().getApplicationMap();
        FormatPool formatPool = (FormatPool)applicationMap.get(RIConstants.FORMAT_POOL);
        Assert.assert_it(null != formatPool);
        return formatPool.timeFormat_format(context, component, dateValue);
    }
}

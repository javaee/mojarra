/*
 * $Id: FormatPool.java,v 1.3 2002/08/13 18:29:48 jvisvanathan Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// FormatPool.java

package com.sun.faces.renderkit;

import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;

import java.util.Date;

import java.text.ParseException;

/**

 *  An instance of <B>FormatPool</B> maintains a pool of Format
 *  instances and exposes them via methods.  An implementation of
 *  FormatPool must be thread-safe.  The impetus for this interface is
 *  that Format instances are expensive to create, yet not thread-safe.
 *  This interface forces the user to not touch the Format instances
 *  directly, thus wrapping access to them in synchronized methods. <P>

 * <B>Lifetime And Scope</B> <P>

 * There is one instance of FormatPool per ServletContext.  It is
 * stored in the ServletContext attr set under the key
 * RIConstants.FORMAT_POOL at FacesContext creation time. <P>

 * <B>Usage</B> <P>

 * Renderers can get the FormatPool instance out of the ServletContext
 * and use it to help in formatting their text.  The intent is that this
 * interface obviates the need to use Format instances directly. <P>

<CODE><PRE>

String result = formatPool.dateFormat_format(getFacesContext(), input, date);
Date date = formatPool.dateFormat_parse(getFacesContext(), input, result);


</PRE></CODE>

 * We'll add methods to this interface/implementation as needed. <P>

 * @version $Id: FormatPool.java,v 1.3 2002/08/13 18:29:48 jvisvanathan Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public interface FormatPool
{

    public String dateFormat_format(FacesContext context, 
				    UIComponent component, Date date);

    public Date dateFormat_parse(FacesContext context, 
				 UIComponent component, String date) throws ParseException;
    
    public String numberFormat_format(FacesContext context, 
				    UIComponent component, Number number);

    public Number numberFormat_parse(FacesContext context, 
				 UIComponent component, String number) throws ParseException;


} // end of interface FormatPool

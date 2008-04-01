/*
 * $Id: UseFacesTag.java,v 1.2 2001/11/07 00:23:31 edburns Exp $
 *
 * Copyright 2000-2001 by Sun Microsystems, Inc.,
 * 901 San Antonio Road, Palo Alto, California, 94303, U.S.A.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Sun Microsystems, Inc. ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with Sun.
 */

// UseFacesTag.java

package com.sun.faces.taglib.html_basic;

import com.sun.faces.renderkit.html_basic.JspOutputMethod;
import com.sun.faces.renderkit.html_basic.HtmlBasicRenderKit;
import com.sun.faces.renderkit.html_basic.HtmlBasicRenderContext;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.Log;
import org.mozilla.util.ParameterCheck;

import javax.faces.FacesException;
import javax.faces.OutputMethod;
import javax.faces.RenderContext;
import javax.faces.RenderKit;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 *
 *  <B>UseFacesTag</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: UseFacesTag.java,v 1.2 2001/11/07 00:23:31 edburns Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class UseFacesTag extends TagSupport
{
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

public UseFacesTag()
{
    super();

    System.setProperty(RenderKit.DEFAULT_RENDERKIT_PROPERTY_NAME,
                       HtmlBasicRenderKit.class.getName());
    System.setProperty(RenderContext.DEFAULT_RENDERCONTEXT_PROPERTY_NAME,
                       HtmlBasicRenderContext.class.getName());
}

//
// Class methods
//

//
// General Methods
//

/**
 * This method initializes the <code>RenderContext</code>.
 * It is invoked when this tag is first encountered.
 * @return an integer value indicating subsequent page evaluation.
 * @throws a <code>JspException</code>
 */
public int doStartTag() throws JspException {

System.out.println("USEFACESTAG.DOSTARTTAG:GETTING RENDERCONTEXT");
    // Get the RenderContext from the session.  If it doesn't
    // exist, create one, and put it in the session.
    //
    RenderContext renderContext = null;
    renderContext = (RenderContext)pageContext.getSession().
        getAttribute("renderContext");
    if (renderContext == null) {
        try {
            renderContext = RenderContext.getRenderContext(
                pageContext.getRequest());
System.out.println("USEFACESTAG.DOSTARTTAG:GOT RENDERCONTEXT:"+renderContext);
        } catch (FacesException e) {
            throw new JspException(e.getMessage());
        }
    }

    JspOutputMethod outputMethod = new JspOutputMethod();
    outputMethod.setPageContext(pageContext);
System.out.println("USEFACESTAG.DOSTARTTAG:SET PAGECONTEXT");
    renderContext.setOutputMethod(outputMethod);
System.out.println("USEFACESTAG.DOSTARTTAG:SET OUTPUTMETHOD");
    pageContext.getSession().setAttribute("renderContext",
        renderContext);
System.out.println("USEFACESTAG.DOSTARTTAG:SET RENDERCONTEXT");
    return EVAL_BODY_INCLUDE;
}

// ----VERTIGO_TEST_START

//
// Test methods
//

public static void main(String [] args)
{
    Assert.setEnabled(true);
    UseFacesTag me = new UseFacesTag();
    Log.setApplicationName("UseFacesTag");
    Log.setApplicationVersion("0.0");
    Log.setApplicationVersionDate("$Id: UseFacesTag.java,v 1.2 2001/11/07 00:23:31 edburns Exp $");
    try {
        me.doStartTag(); 
    } catch (Exception e) {
        e.printStackTrace();
    }
}

// ----VERTIGO_TEST_END

} // end of class SelectBoolean_CheckboxTag

/*
 * $Id: UseFacesTag.java,v 1.12 2002/07/15 22:30:02 jvisvanathan Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// UseFacesTag.java

package com.sun.faces.taglib.html_basic;

import org.mozilla.util.Assert;
import org.mozilla.util.ParameterCheck;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.jsp.PageContext;
import javax.faces.context.FacesContext;
import javax.faces.tree.Tree;
import javax.servlet.http.HttpSession;
import com.sun.faces.RIConstants;

/**
 *
 *  All JSF component tags must be nested within UseFacesTag. This tag
 * does not have any renderers or attributes. It exists mainly to
 * save the state of the response tree once all tags have been rendered.
 *
 * @version $Id: UseFacesTag.java,v 1.12 2002/07/15 22:30:02 jvisvanathan Exp $
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
    }

    //
    // Class methods
    //

    // 
    // Accessors
    //

    //
    // General Methods
    //

    //
    // Methods from TagSupport
    //
    public int doStartTag() throws JspException {
        return EVAL_BODY_INCLUDE;
    }    
    
    public int doEndTag() throws JspException {
       
        // Look up the FacesContext instance for this request
        FacesContext context = (FacesContext)
            pageContext.getAttribute(FacesContext.FACES_CONTEXT_ATTR,
                                     PageContext.REQUEST_SCOPE);
        if (context == null) { // FIXME - i18n
            throw new JspException("Cannot find FacesContext");
        }
        // Save state in the user's session.
        HttpSession session = context.getHttpSession();
        session.setAttribute(RIConstants.REQUEST_LOCALE, context.getLocale());
        session.setAttribute(RIConstants.FACES_TREE, context.getResponseTree());
        return EVAL_PAGE;
    }    


} // end of class UseFacesTag

/*
 * $Id: BaseComponentBodyTag.java,v 1.4 2004/02/04 23:42:02 ofung Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// BaseComponentTag.java

package com.sun.faces.taglib;

import com.sun.faces.RIConstants;
import com.sun.faces.el.impl.ElException;
import com.sun.faces.el.impl.ExpressionInfo;
import com.sun.faces.el.impl.JspVariableResolver;
import com.sun.faces.util.Util;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIComponent;
import javax.faces.convert.Converter;
import javax.faces.webapp.UIComponentTag;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTag;

/**
 *
 *  <B>BaseComponentTag</B> is a base class for most tags in the Faces Tag
 *  library.  Its primary purpose is to centralize common tag functions
 *  to a single base class. <P>
 *
 */

public abstract class BaseComponentBodyTag extends BaseComponentTag implements BodyTag
{
    //
    // Protected Constants
    //

    // Log instance for this class
    protected static Log log = LogFactory.getLog(BaseComponentBodyTag.class);

    //
    // Class Variables
    //

    //
    // Instance Variables
    //

    // Relationship Instance Variables

    /**
     * <p>The <code>bodyContent</code> for this tag handler.</p>
     */
    protected BodyContent bodyContent = null;


    //
    // Constructors and Initializers    
    //

    public BaseComponentBodyTag()
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
    // Methods from BodyTag
    //

    public void doInitBody() throws JspException {

        ; // Default implementation does nothing

    }

    public void setBodyContent(BodyContent bodyContent) {
        this.bodyContent = bodyContent;
    }

    public BodyContent getBodyContent() {
        return (this.bodyContent);
    }

    public JspWriter getPreviousOut() {
        return (this.bodyContent.getEnclosingWriter());
    }


    //
    // Methods from Tag
    // 

    public void release() {
        bodyContent = null;
        super.release();
    }

    // 
    // methods from UIComponentTag
    // 

    protected int getDoStartValue() throws JspException {
        return (BodyTag.EVAL_BODY_BUFFERED);
    }

    public int doAfterBody() throws JspException {
        return (getDoAfterBodyValue());
    }

    protected int getDoEndValue() throws JspException {
        return (EVAL_PAGE);
    }

    protected int getDoAfterBodyValue() throws JspException {

        return (SKIP_BODY);

    }



} // end of class BaseComponentBodyTag

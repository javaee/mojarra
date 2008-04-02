/*
 * $Id: Graphic_ImageTag.java,v 1.17 2003/10/13 22:56:23 jvisvanathan Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.taglib.html_basic;

import com.sun.faces.taglib.BaseComponentTag;
import com.sun.faces.util.Util;

import javax.faces.component.UIComponent;
import javax.faces.component.UIGraphic;
import javax.servlet.jsp.JspException;



/**
 * This class is the tag handler that evaluates the 
 * <code>graphic_image</code> custom tag.
 */

public class Graphic_ImageTag extends BaseComponentTag 
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

    protected String url = null;
    protected String url_ = null;


    // Relationship Instance Variables

    //
    // Constructors and Initializers    
    //

    public Graphic_ImageTag()
    {
        super();
    }

    //
    // Class methods
    //

    public void setUrl(String newUrl) {
        url_ = newUrl;
    }

    // 
    // Accessors
    //

    //
    // General Methods
    //

    public String getRendererType() { 
        return "Image"; 
    }
    public String getComponentType() { 
        return "GraphicImage"; 
    }

    protected void overrideProperties(UIComponent component) {
	super.overrideProperties(component);
	UIGraphic graphic = (UIGraphic) component;
	
	if (null != url) {
	    graphic.setURL(url);
	}
        if (usemap != null) {
            component.getAttributes().put("usemap", usemap);
        }
        if (ismap) {
            component.getAttributes().put("ismap",
                    ismap ? Boolean.TRUE : Boolean.FALSE);
        }
        if (alt != null) {
            component.getAttributes().put("alt", alt);
        }
        if (width != null) {
            component.getAttributes().put("width", width);
        }
        if (height != null) {
            component.getAttributes().put("height", height);
        }
    }
    
    //
    // Methods from TagSupport
    // 
    /* Evaluates expressions as necessary */
    private void evaluateExpressions() throws JspException {
        if (url_ != null) {
            url = Util.evaluateElExpression(url_, pageContext);
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



} // end of class Graphic_ImageTag

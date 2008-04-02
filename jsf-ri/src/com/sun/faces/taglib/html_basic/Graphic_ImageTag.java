/*
 * $Id: Graphic_ImageTag.java,v 1.14 2003/10/06 19:06:46 horwat Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.taglib.html_basic;

import com.sun.faces.taglib.BaseComponentTag;

import javax.faces.component.UIComponent;
import javax.faces.component.UIGraphic;


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
        url = newUrl;
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
        if (ismap != null) {
            component.getAttributes().put("ismap", ismap);
        }
        if (alt != null) {
            component.getAttributes().put("alt", alt);
        }
    }
    
    //
    // Methods from TagSupport
    // 


} // end of class Graphic_ImageTag

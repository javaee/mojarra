/*
 * $Id: Graphic_ImageTag.java,v 1.8 2003/07/16 00:00:09 jvisvanathan Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.taglib.html_basic;

import com.sun.faces.taglib.FacesTag;

import javax.faces.component.UIComponent;
import javax.faces.component.UIGraphic;


/**
 * This class is the tag handler that evaluates the 
 * <code>graphic_image</code> custom tag.
 */

public class Graphic_ImageTag extends FacesTag 
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String newUrl) {
        url = newUrl;
    }

    // 
    // Accessors
    //

    //
    // General Methods
    //

    public String getLocalRendererType() { 
        return "Image"; 
    }
    public String getComponentType() { 
        return "Graphic"; 
    }

    protected void overrideProperties(UIComponent component) {
	super.overrideProperties(component);
	UIGraphic graphic = (UIGraphic) component;
	
	if (null != url) {
	    graphic.setURL(url);
	}
        if (usemap != null) {
            component.setAttribute("usemap", usemap);
        }
        if (ismap != null) {
            component.setAttribute("ismap", ismap);
        }
    }
    
    //
    // Methods from TagSupport
    // 


} // end of class Graphic_ImageTag

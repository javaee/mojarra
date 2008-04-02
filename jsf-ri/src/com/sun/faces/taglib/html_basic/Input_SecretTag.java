/*
 * $Id: Input_SecretTag.java,v 1.14 2003/10/07 13:05:34 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */


package com.sun.faces.taglib.html_basic;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;


/**
 * This class is the tag handler that evaluates the 
 * <code>input_secret</code> custom tag.
 */

public class Input_SecretTag extends Input_TextTag
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

    protected boolean redisplay = false;

    // Relationship Instance Variables

    //
    // Constructors and Initializers    
    //

    public Input_SecretTag()
    {
        super();
    }

    //
    // Class methods
    //

    public void setRedisplay(boolean newRedisplay) {
        redisplay = newRedisplay;
    }
    // 
    // Accessors
    //

    //
    // General Methods
    //

    public String getRendererType() { 
        return "Secret"; 
    }
    public String getComponentType() {
	return "InputSecret";
    }

    protected void overrideProperties(UIComponent component) {
        super.overrideProperties(component);
        UIInput input = (UIInput) component;

        if (redisplay) {
            input.getAttributes().put("redisplay", Boolean.valueOf(redisplay));
        }
    }


    //
    // Methods from TagSupport
    // 

} // end of class Input_SecretTag

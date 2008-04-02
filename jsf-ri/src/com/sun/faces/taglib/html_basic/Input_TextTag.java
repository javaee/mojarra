/*
 * $Id: Input_TextTag.java,v 1.4 2003/02/04 01:17:43 edburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// Input_TextTag.java

package com.sun.faces.taglib.html_basic;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;

/**
 *
 * @version $Id: Input_TextTag.java,v 1.4 2003/02/04 01:17:43 edburns Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class Input_TextTag extends InputTag
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
    private String converter = null;

// Attribute Instance Variables

// Relationship Instance Variables

//
// Constructors and Initializers    
//
    public Input_TextTag() {
        super();
    }

//
// Accessors
//
    public void setConverter(String converter) {
        this.converter = converter;
    }

//
// Class methods
//

//
// General Methods
//

    public String getLocalRendererType() { return "Text"; }

    public UIComponent createComponent() {
        return (new UIInput());
    }

    protected void overrideProperties(UIComponent component) {
        super.overrideProperties(component);
        if ((converter != null) &&
            (component.getAttribute(UIComponent.CONVERTER_ATTR) == null)) {
            component.setAttribute(UIComponent.CONVERTER_ATTR, converter);
        }
    }

//
// Methods from TagSupport
//
    


} // end of class Input_TextTag

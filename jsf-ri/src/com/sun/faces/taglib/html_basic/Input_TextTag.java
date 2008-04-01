/*
 * $Id: Input_TextTag.java,v 1.1 2002/08/13 22:55:28 rkitain Exp $
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
 * @version $Id: Input_TextTag.java,v 1.1 2002/08/13 22:55:28 rkitain Exp $
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

// Attribute Instance Variables

// Relationship Instance Variables

//
// Constructors and Initializers    
//

public Input_TextTag()
{
    super();
}

//
// Class methods
//

//
// General Methods
//

    public String getLocalRendererType() { return "TextRenderer"; }

    public UIComponent createComponent() {
        return (new UIInput());
    }

//
// Methods from TagSupport
//
    


} // end of class Input_TextTag

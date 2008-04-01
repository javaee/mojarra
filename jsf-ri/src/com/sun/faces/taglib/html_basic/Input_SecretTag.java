/*
 * $Id: Input_SecretTag.java,v 1.1 2002/08/13 22:55:28 rkitain Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// Input_SecretTag.java

package com.sun.faces.taglib.html_basic;

import javax.faces.component.UIComponent;

/**
 *
 * @version $Id: Input_SecretTag.java,v 1.1 2002/08/13 22:55:28 rkitain Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
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

// 
// Accessors
//

//
// General Methods
//

    public String getLocalRendererType() { return "SecretRenderer"; }

//
// Methods from TagSupport
// 


} // end of class Input_SecretTag

/*
 * $Id: TextEntry_SecretTag.java,v 1.20 2002/02/06 20:05:53 edburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TextEntry_SecretTag.java

package com.sun.faces.taglib.html_basic;

/**
 *
 *  <B>TextEntry_SecretTag</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TextEntry_SecretTag.java,v 1.20 2002/02/06 20:05:53 edburns Exp $
 * 
 *
 */

public class TextEntry_SecretTag extends TextEntry_InputTag
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

    public TextEntry_SecretTag()
    {
        super();
    }

    
    //
    // Class methods
    //

    //
    // General Methods
    //

    //
    // Methods from FacesTag
    //


    public String getRendererType() {
	return "SecretRenderer";
    }


} // end of class TextEntry_SecretTag

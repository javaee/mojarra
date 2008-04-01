/*
 * $Id: TextEntry_Input_ValueChangeTag.java,v 1.1 2002/07/23 19:37:30 eburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TextEntry_Input_ValueChangeTag.java

package basic;

import org.mozilla.util.Assert;
import org.mozilla.util.ParameterCheck;

import javax.servlet.jsp.JspException;

import javax.faces.component.UIComponent;
import javax.faces.component.UITextEntry;
import javax.faces.context.FacesContext;
import javax.faces.FacesException;

import com.sun.faces.util.Util;

import com.sun.faces.taglib.html_basic.TextEntry_InputTag;

/**
 *

 * @version $Id: TextEntry_Input_ValueChangeTag.java,v 1.1 2002/07/23 19:37:30 eburns Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class TextEntry_Input_ValueChangeTag extends TextEntry_InputTag
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

public TextEntry_Input_ValueChangeTag()
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

    public String getLocalRendererType() { return null; }

    public UIComponent createComponent() {
        return (new UIValueChangeTextEntry());
    }



//
// Methods from TagSupport
// 


} // end of class TextEntry_Input_ValueChangeTag

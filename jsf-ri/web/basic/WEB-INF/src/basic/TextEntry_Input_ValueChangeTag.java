/*
 * $Id: TextEntry_Input_ValueChangeTag.java,v 1.3 2002/08/14 19:11:36 rkitain Exp $
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
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.FacesException;

import com.sun.faces.util.Util;

import com.sun.faces.taglib.html_basic.Input_TextTag;

/**
 *

 * @version $Id: TextEntry_Input_ValueChangeTag.java,v 1.3 2002/08/14 19:11:36 rkitain Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class TextEntry_Input_ValueChangeTag extends Input_TextTag
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

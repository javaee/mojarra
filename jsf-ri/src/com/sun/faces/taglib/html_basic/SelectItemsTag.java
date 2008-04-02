/*
 * $Id: SelectItemsTag.java,v 1.6 2003/07/09 19:04:27 rlubke Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// SelectItemsTag.java

package com.sun.faces.taglib.html_basic;

import org.mozilla.util.Assert;
import org.mozilla.util.ParameterCheck;

import javax.servlet.jsp.JspException;
import javax.faces.component.UISelectOne;
import javax.faces.component.UIComponent;
import javax.faces.component.SelectItem;
import javax.faces.component.UISelectItems;
import javax.faces.context.FacesContext;
import javax.faces.FacesException;

import com.sun.faces.util.Util;
import com.sun.faces.taglib.FacesTag;
import com.sun.faces.RIConstants;

/**
 *
 *  <B>FacesTag</B> is a base class for most tags in the Faces Tag
 *  library.  Its primary purpose is to centralize common tag functions
 *  to a single base class. <P>
 *
 * @version $Id: SelectItemsTag.java,v 1.6 2003/07/09 19:04:27 rlubke Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class SelectItemsTag extends FacesTag
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

public SelectItemsTag()
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
    public String getComponentType() { return "SelectItems"; }


//
// Methods from FacesTag
//

} // end of class SelectItemsTag

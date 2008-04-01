
/**
 * $Id: SelectOne_MenuTag.java,v 1.1 2002/09/06 22:11:14 rkitain Exp $
 *
 * (C) Copyright International Business Machines Corp., 2001,2002
 * The source code for this program is not published or otherwise
 * divested of its trade secrets, irrespective of what has been
 * deposited with the U. S. Copyright Office.   
 */


// SelectMany_MenuTag.java

package com.sun.faces.taglib.html_basic;

import javax.faces.component.UIComponent;
import javax.faces.component.UISelectMany;
import javax.faces.component.UISelectOne;
import javax.servlet.jsp.JspException;

import com.sun.faces.taglib.FacesTag;

/**
 *
 *  <B>FacesTag</B> is a base class for most tags in the Faces Tag
 *  library.  Its primary purpose is to centralize common tag functions
 *  to a single base class. <P>
 *
 * @version $Id: SelectOne_MenuTag.java,v 1.1 2002/09/06 22:11:14 rkitain Exp $
 * 
 * @see	Blah
 * @see	Bloo
 * 
 *
 */

public class SelectOne_MenuTag extends FacesTag
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

public SelectOne_MenuTag()
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

    public String getLocalRendererType() { return "SelectOneMenuRenderer"; } 
    
    public UIComponent createComponent() {
        return (new UISelectOne());
    }

//
// Methods from TagSupport
// 

    public int doEndTag() throws JspException {
	UISelectOne component = (UISelectOne) getComponent();

	int rc = super.doEndTag();

	return rc;
    }


    protected void overrideProperties(UIComponent component) {
        super.overrideProperties(component);

        if (null == component.getAttribute("size")) {
            component.setAttribute("size", getSize());
        }
        if (null == component.getAttribute("onselect")) {
            component.setAttribute("onselect", getOnselect());
        }
        if (null == component.getAttribute("onchange")) {
            component.setAttribute("onchange", getOnchange());
        }
    }

} // end of class SelectMany_MenuTag

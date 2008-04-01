/**
 * $Id: SelectOneMenuRenderer.java,v 1.1 2002/09/06 22:10:28 rkitain Exp $
 *
 * (C) Copyright International Business Machines Corp., 2001,2002
 * The source code for this program is not published or otherwise
 * divested of its trade secrets, irrespective of what has been
 * deposited with the U. S. Copyright Office.   
 */

// SelectOneMenuRenderer.java

package com.sun.faces.renderkit.html_basic;

import javax.faces.component.UIComponent;
import javax.faces.component.UISelectOne;

import com.sun.faces.util.Util;

/**
 *
 *  <B>SelectOneMenuRenderer</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: SelectOneMenuRenderer.java,v 1.1 2002/09/06 22:10:28 rkitain Exp $
 * 
 * @see Blah
 * @see Bloo
 *
 */

public class SelectOneMenuRenderer extends SelectManyMenuRenderer {
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

	public SelectOneMenuRenderer() {
		super();
	}

	//
	// Class methods
	//

	//
	// General Methods
	//

	//
	// Methods From Renderer
	//

	public boolean supportsComponentType(String componentType) {
		if (componentType == null) {
			throw new NullPointerException(
				Util.getExceptionMessage(
					Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
		}
		return (componentType.equals(UISelectOne.TYPE));
	}


	String getMultipleText() {
		return "";
	}
	
	Object[] getCurrentSelectedValues(UIComponent component) {
		UISelectOne select = (UISelectOne) component;
		Object returnObjects[] = new Object[1];
		if (null != (returnObjects[0] = select.getSelectedValue()))
			return returnObjects;
		return null;
	}

} // end of class SelectOneMenuRenderer

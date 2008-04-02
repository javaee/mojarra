/*
 * $Id: ListboxRenderer.java,v 1.4 2002/09/14 17:22:17 edburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// ListboxRenderer.java

package com.sun.faces.renderkit.html_basic;

import javax.faces.component.UIComponent;
import javax.faces.component.UISelectOne;
import javax.faces.context.FacesContext;

import com.sun.faces.util.Util;

/**
 *
 *  <B>ListboxRenderer</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: ListboxRenderer.java,v 1.4 2002/09/14 17:22:17 edburns Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class ListboxRenderer extends SelectManyListboxRenderer {
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

    public ListboxRenderer() {
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
        if ( componentType == null ) {
            throw new NullPointerException(Util.getExceptionMessage(
                    Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
        return (componentType.equals(UISelectOne.TYPE));
    }

   
    protected String getMultipleText() {
        return "";
    }
	
    protected Object[] getCurrentSelectedValues(FacesContext context, 
						UIComponent component) {
        UISelectOne select = (UISelectOne) component;
        Object returnObjects[] = new Object[1];
        if (null != (returnObjects[0] = select.getSelectedValue()))
                return returnObjects;
        return null;
    }

} // end of class ListboxRenderer

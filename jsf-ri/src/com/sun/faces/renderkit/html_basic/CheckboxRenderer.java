/*
 * $Id: CheckboxRenderer.java,v 1.39 2002/12/18 20:54:59 eburns Exp $
 *
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// CheckboxRenderer.java

package com.sun.faces.renderkit.html_basic;

import com.sun.faces.RIConstants;
import com.sun.faces.util.Util;

import java.io.IOException;
import java.util.Iterator;

import javax.faces.component.UIComponent;
import javax.faces.component.UISelectBoolean;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;
import javax.faces.FacesException;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.ConvertUtils;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.Log;
import org.mozilla.util.ParameterCheck;

/**
 *
 *  <B>CheckboxRenderer</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: CheckboxRenderer.java,v 1.39 2002/12/18 20:54:59 eburns Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class CheckboxRenderer extends HtmlBasicRenderer {
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

    public CheckboxRenderer() {
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
            throw new NullPointerException(Util.getExceptionMessage(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
        return (componentType.equals(UISelectBoolean.TYPE));
    }

    public Object getConvertedValue(FacesContext context, UIComponent component,
            String newValue) throws IOException {
     
        //if there was nothing sent in the request the checkbox wasn't checked..
        if (newValue == null) {
            newValue = "false";
            // Otherwise, if the checkbox was checked, the value
            // coming in could be "on", "yes" or "true".
        } else if (newValue.equalsIgnoreCase("on") ||
            newValue.equalsIgnoreCase("yes") ||
            newValue.equalsIgnoreCase("true")) {
            newValue = "true";
        }
        Object convertedValue = Boolean.valueOf(newValue);
	return convertedValue;
    }

    public void encodeBegin(FacesContext context, UIComponent component) 
        throws IOException {
        if (context == null || component == null) {
            throw new NullPointerException(Util.getExceptionMessage(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
    }

    public void encodeChildren(FacesContext context, UIComponent component) 
        throws IOException {
        if (context == null || component == null) {
            throw new NullPointerException(Util.getExceptionMessage(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }

    }
    
    protected void getEndTextToRender(FacesContext context, UIComponent component,
            String currentValue, StringBuffer buffer ) {
 
	String selectbooleanClass = null;
        buffer.append("<input type=\"checkbox\" ");
        buffer.append(" name=\"");
        buffer.append(component.getClientId(context));
        buffer.append("\"");

        if (currentValue.equals("true")) {
            buffer.append(" checked ");
        }
        if (null != (selectbooleanClass = (String) 
		     component.getAttribute("selectbooleanClass"))) {
	    buffer.append(" class=\"" + selectbooleanClass + "\" ");
	}
        buffer.append(Util.renderPassthruAttributes(context, component));
        buffer.append(Util.renderBooleanPassthruAttributes(context, 
                component));
        buffer.append(">");    
    }

} // end of class CheckboxRenderer

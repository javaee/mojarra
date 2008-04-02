/*
 * $Id: TextRenderer.java,v 1.42 2003/02/19 00:31:52 rkitain Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TextRenderer.java

package com.sun.faces.renderkit.html_basic;

import com.sun.faces.util.Util;

import java.util.Iterator;

import javax.faces.component.AttributeDescriptor;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UIOutput;
import javax.faces.component.UISelectBoolean;
import javax.faces.component.UISelectOne;
import javax.faces.component.UISelectMany;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.Log;
import org.mozilla.util.ParameterCheck;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

import com.sun.faces.RIConstants;

/**
 *
 *  <B>TextRenderer</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TextRenderer.java,v 1.42 2003/02/19 00:31:52 rkitain Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class TextRenderer extends HtmlBasicInputRenderer {
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

    public TextRenderer() {
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
        return (componentType.equals(UIInput.TYPE) ||
            componentType.equals(UIOutput.TYPE));
    }

    public void encodeBegin(FacesContext context, UIComponent component) 
            throws IOException {
        if (context == null || component == null) {
            throw new NullPointerException(Util.getExceptionMessage(
                Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
    }

    public void encodeChildren(FacesContext context, UIComponent component) {
        if (context == null || component == null) {
            throw new NullPointerException(Util.getExceptionMessage(
                Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
    }

    protected void getEndTextToRender(FacesContext context, 
        UIComponent component, String currentValue, StringBuffer buffer ) {
        
	String styleClass = null;
        if ((null != (styleClass = (String) 
		      component.getAttribute("inputClass"))) || 
	    (null != (styleClass = (String) 
		      component.getAttribute("outputClass")))) {
	    buffer.append("<span class=\"" + styleClass + "\">");
	}
        if ((UIInput.TYPE.equals(component.getComponentType())) ||
            (component instanceof UIInput)) {
            buffer.append("<input type=\"text\"");
            buffer.append(" name=\"");
            buffer.append(component.getClientId(context));
            buffer.append("\"");

            // render default text specified
            if (currentValue != null) {
                buffer.append(" value=\"");
                buffer.append(currentValue);
                buffer.append("\"");
            }
            buffer.append(Util.renderPassthruAttributes(context, component));
            buffer.append(Util.renderBooleanPassthruAttributes(context, 
                component));
            buffer.append(">");            
        } else if ((UIOutput.TYPE.equals(component.getComponentType())) ||
            (component instanceof UIOutput)) {
            if (currentValue == null || currentValue == "") {
                try {
                    currentValue = getKeyAndLookupInBundle(context, component,
                                                       "key");
                } catch (java.util.MissingResourceException e) {
                    // Do nothing since the absence of a resource is not an
                    // error.
                    return;
                }
            }
            if (currentValue != null) {
                buffer.append(currentValue);
            }
        }
	if (null != styleClass) {
	    buffer.append("</span>");
	}
    }
    
    protected String getFormattedValue(FacesContext context, 
        UIComponent component, Object currentValue ) {

        Converter converter = getConverter(component);
        if (converter != null) {
            try {
                return converter.getAsString(context, component, currentValue);
            } catch (ConverterException e) {
                return currentValue.toString();
            }
        } else {
            return currentValue.toString();
        }
    }

    // The testcase for this class is TestRenderers_2.java 

} // end of class TextRenderer



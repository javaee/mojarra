/*
 * $Id: OptionListRenderer.java,v 1.29 2002/08/29 01:28:18 eburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// OptionListRenderer.java

package com.sun.faces.renderkit.html_basic;

import com.sun.faces.util.Util;

import java.io.IOException;
import java.util.Iterator;

import javax.faces.component.AttributeDescriptor;
import javax.faces.component.UIComponent;
import javax.faces.component.SelectItem;
import javax.faces.component.UISelectOne;
import javax.faces.context.ResponseWriter;
import javax.faces.context.FacesContext;
import javax.faces.render.Renderer;
import javax.faces.FacesException;

import com.sun.faces.util.Util;
import com.sun.faces.util.SelectItemWrapper;
import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.ConvertUtils;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.Log;
import org.mozilla.util.ParameterCheck;

/**
 *
 *  <B>OptionListRenderer</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: OptionListRenderer.java,v 1.29 2002/08/29 01:28:18 eburns Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class OptionListRenderer extends HtmlBasicRenderer {
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

    public OptionListRenderer() {
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
            throw new NullPointerException(Util.getExceptionMessage(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
        return (componentType.equals(UISelectOne.TYPE));
    }

    public void decode(FacesContext context, UIComponent component) 
        throws IOException {
        if (context == null || component == null) {
            throw new NullPointerException(Util.getExceptionMessage(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }

        String compoundId = component.getCompoundId();
        Assert.assert_it(compoundId != null );

        String newValue = context.getServletRequest().getParameter(compoundId);
        // currently we assume the model type to be of type string or 
        // convertible to string and localised by the application.
        component.setValue(newValue);
        component.setValid(true);
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

    public void encodeEnd(FacesContext context, UIComponent component) 
        throws IOException {
        String currentValue = null;
        UISelectOne selectOne = null;
        
        if (context == null || component == null) {
            throw new NullPointerException(Util.getExceptionMessage(
                    Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }

        Object currentObj = component.currentValue(context);
        if ( currentObj != null) {
            if (currentObj instanceof String) {
                currentValue = (String)currentObj;
            } else {
                currentValue = currentObj.toString();
            }
        }
        if (currentValue == null) {
            currentValue = "";
        }

        // cast component to UISelectOne.
        if ( supportsComponentType(component)) {
            selectOne = (UISelectOne) component;
        }
       
        StringBuffer buffer = new StringBuffer();
        buffer.append("<select name=\"");
        buffer.append(component.getCompoundId());
        buffer.append("\"");
       
        // render HTML 4.0 attributes if any for select tag
        buffer.append(Util.renderPassthruAttributes(context, component));
	buffer.append(Util.renderBooleanPassthruAttributes(context, component));
        
        buffer.append(">");
        Iterator items = Util.getSelectItemWrappers(context, selectOne);
	SelectItem curItem = null;
        SelectItemWrapper curItemWrapper = null;
        while (items.hasNext()) {
	    curItemWrapper = (SelectItemWrapper) items.next();
            curItem = curItemWrapper.getSelectItem();
            buffer.append("\t<option value=\"");
            buffer.append((String) curItem.getValue());
            buffer.append("\"");
            if (null != curItem.getValue() &&
		curItem.getValue().equals(currentValue)) {
                buffer.append(" selected=\"selected\"");
            }
            // render HTML 4.0 attributes if any for option tag
            buffer.append(Util.renderPassthruAttributes(context, 
                    curItemWrapper.getUISelectItem()));
	    buffer.append(Util.renderBooleanPassthruAttributes(context, 
                    curItemWrapper.getUISelectItem()));
            buffer.append(">");
            buffer.append(curItem.getLabel());
            buffer.append("</option>\n");
        }
        buffer.append("</select>");
        
        ResponseWriter writer = null;
        writer = context.getResponseWriter();
        Assert.assert_it(writer != null );
	writer.write(buffer.toString());
    }

} // end of class OptionListRenderer

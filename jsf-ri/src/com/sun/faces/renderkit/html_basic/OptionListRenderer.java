/*
 * $Id: OptionListRenderer.java,v 1.26 2002/08/21 19:26:03 jvisvanathan Exp $
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
 * @version $Id: OptionListRenderer.java,v 1.26 2002/08/21 19:26:03 jvisvanathan Exp $
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
        
        ResponseWriter writer = null;
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
       
        writer = context.getResponseWriter();
        Assert.assert_it(writer != null );

        // PENDING (visvan) handle nested labels
        writer.write("<select name=\"");
        writer.write(component.getCompoundId());
        writer.write("\"");
       
        // render HTML 4.0 attributes if any
        writer.write(Util.renderPassthruAttributes(context, component));
	writer.write(Util.renderBooleanPassthruAttributes(context, component));
        
        writer.write(">");
        Iterator items = Util.getSelectItems(context, selectOne);
	SelectItem curItem = null;

        while (items.hasNext()) {
	    curItem = (SelectItem) items.next();
            writer.write("\t<option value=\"");
            writer.write((String) curItem.getValue());
            writer.write("\"");
            if (null != curItem.getValue() &&
		curItem.getValue().equals(currentValue)) {
                writer.write(" selected=\"selected\"");
            }
            // PENDING (visvan) render HTML 4.0 attributes for Option tag.
            // can't do this right now,  because SelectItem doesn't store
            // attributes, UISelectItem does.
            writer.write(">");
            writer.write(curItem.getLabel());
            writer.write("</option>\n");
        }
        writer.write("</select>");
    }

} // end of class OptionListRenderer

/*
 * $Id: RadioRenderer.java,v 1.38 2003/03/13 01:06:33 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// RadioRenderer.java

package com.sun.faces.renderkit.html_basic;

import com.sun.faces.util.Util;

import java.util.Iterator;

import javax.faces.component.AttributeDescriptor;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;
import javax.faces.component.UIComponent;
import javax.faces.FacesException;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.Log;
import org.mozilla.util.ParameterCheck;

import javax.faces.component.UISelectOne;
import javax.faces.component.SelectItem;

import com.sun.faces.util.Util;
import com.sun.faces.util.SelectItemWrapper;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.ConversionException;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;


/**
 *
 *  <B>RadioRenderer</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: RadioRenderer.java,v 1.38 2003/03/13 01:06:33 eburns Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class RadioRenderer extends HtmlBasicRenderer {
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

    public RadioRenderer() {
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

    public void encodeBegin(FacesContext context, UIComponent component) 
            throws IOException {
        if (context == null || component == null) {
            throw new NullPointerException(Util.getExceptionMessage(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
    }

    public void encodeChildren(FacesContext context, UIComponent component) {
        if (context == null || component == null) {
            throw new NullPointerException(Util.getExceptionMessage(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
    }

   protected void getEndTextToRender(FacesContext context, UIComponent component,
            String currentValue, StringBuffer buffer ) {
       
        UISelectOne uiSelectOne = null;
	String alignStr = null;
	String borderStr = null;
	String selectoneClass = null;
	boolean alignVertical = false;
	int border = 0;

        // cast component to UISelectOne.
        if ( supportsComponentType(component)) {
            uiSelectOne = (UISelectOne) component;
        }    

        Iterator items = Util.getSelectItemWrappers(context, uiSelectOne);
	SelectItem curItem = null;
        SelectItemWrapper curItemWrapper = null;
        if ( items == null ) {
            return;
        }
        
        if (null != (alignStr = (String) uiSelectOne.getAttribute("layout"))) {
	    alignVertical = alignStr.equalsIgnoreCase("PAGE_DIRECTION") ? 
		true : false;
	}
	if (null != (borderStr = (String) uiSelectOne.getAttribute("border"))){
	    try {
		border = Integer.valueOf(borderStr).intValue();
	    }
	    catch (Throwable e) {
		border = 0;
	    }
	}
	if (null != (selectoneClass = (String) 
		     component.getAttribute("selectoneClass"))) {
	    buffer.append("<span class=\"" + selectoneClass + "\">");
	}
	
	buffer.append("<table border=\"" + border + "\">\n");
	if (!alignVertical) {
	    buffer.append("\t<tr>\n");
	}

	while (items.hasNext()) {
	    curItemWrapper = (SelectItemWrapper) items.next();
            curItem = curItemWrapper.getSelectItem();
	    if (alignVertical) {
		buffer.append("\t<tr>\n");
	    }
            buffer.append("<td><input type=\"radio\"");
            if (null != curItem.getValue() &&
		curItem.getValue().equals(currentValue)){
                buffer.append(" checked");
            }
            buffer.append(" name=\"");
            buffer.append(uiSelectOne.getClientId(context));
            buffer.append("\" value=\"");
            buffer.append((String) curItem.getValue());
            buffer.append("\"");
            // render HTML 4.0 attributes if any for radio tag.
            buffer.append(Util.renderPassthruAttributes(context, 
                    curItemWrapper.getUISelectItem()));
	    buffer.append(Util.renderBooleanPassthruAttributes(context, 
                    curItemWrapper.getUISelectItem()));
            buffer.append(">");
            
            String itemLabel = curItem.getLabel();
            if (itemLabel != null) {
                buffer.append(" ");
                buffer.append(itemLabel);
            }
            buffer.append("</td>\n");
	    if (alignVertical) {
		buffer.append("\t<tr>\n");
	    }
        }

	if (!alignVertical) {
	    buffer.append("\t</tr>\n");
	}
	buffer.append("</table>");

	if (null != selectoneClass) {
	    buffer.append("</span>");
	}
    }

} // end of class RadioRenderer

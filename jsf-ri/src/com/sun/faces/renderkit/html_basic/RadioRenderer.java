/*
 * $Id: RadioRenderer.java,v 1.26 2002/08/14 19:11:24 eburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
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
 * @version $Id: RadioRenderer.java,v 1.26 2002/08/14 19:11:24 eburns Exp $
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

    public void decode(FacesContext context, UIComponent component) 
           throws IOException {
        Object convertedValue = null;
        Class modelType = null;
        
        if (context == null || component == null) {
            throw new NullPointerException(Util.getExceptionMessage(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
        
        // PENDING (visvan) should we call supportsType to double check
        // componentType ??
        String compoundId = component.getCompoundId();
        Assert.assert_it(compoundId != null );
        
        String newValue = context.getServletRequest().getParameter(compoundId);
        String modelRef = component.getModelReference();
        
        // If modelReference String is null or newValue is null, type
        // conversion is not necessary. This is because default type
        // for UISelectOne component is String. Simply set local value.
        if ( newValue == null || modelRef == null ) {
            component.setValue(newValue);
            return;
        }
        
        // if we get here, type conversion is required.
        try {
            modelType = context.getModelType(modelRef);
        } catch (FacesException fe ) {
            // PENDING (visvan) log error
        }    
        Assert.assert_it(modelType != null );
        
        try {
            convertedValue = ConvertUtils.convert(newValue, modelType);
            component.setValid(true);
        } catch (ConversionException ce ) {
            addConversionErrorMessage( context, component, ce.getMessage()); 
        }    
            
        if ( convertedValue == null ) {
            // since conversion failed, don't modify the localValue.
            // set the value temporarily in an attribute so that encode can 
            // use this local state instead of local value.
            component.setAttribute("localState", newValue);
        } else {
            // conversion successful, set converted value as the local value.
            component.setValue(convertedValue);    
        }
    }

    public void encodeBegin(FacesContext context, UIComponent component) 
            throws IOException {
        if (context == null || component == null) {
            throw new NullPointerException(Util.getExceptionMessage(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
    }

    public void encodeChildren(FacesContext context, UIComponent component) {
        if (context == null || component == null) {
            throw new NullPointerException("context or component argument is null.");
        }
    }

    public void encodeEnd(FacesContext context, UIComponent component) throws IOException 
    {
        String currentValue = null;
        ResponseWriter writer = null;
        UISelectOne uiSelectOne = null;
	String alignStr = null;
	String borderStr = null;
	boolean alignVertical = false;
	int border = 0;

        if (context == null || component == null) {
            throw new NullPointerException(Util.getExceptionMessage(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
        
        // if localState attribute is set, then conversion failed, so use
        // that to reproduce the incorrect value. Otherwise use the current value
        // stored in component.
        Object localState = component.getAttribute("localState");
        if ( localState != null ) {
            currentValue = (String) localState;
        } else {
            Object currentObj = component.currentValue(context);
            if ( currentObj != null) {
                currentValue = ConvertUtils.convert(currentObj);
            }    
        }
      
        // cast component to UISelectOne.
        if ( supportsComponentType(component)) {
            uiSelectOne = (UISelectOne) component;
        }    

        Iterator items = Util.getSelectItems(context, uiSelectOne);
	SelectItem curItem = null;
        if ( items == null ) {
            return;
        }
        
        writer = context.getResponseWriter();
        Assert.assert_it(writer != null );
	
	if (null != (alignStr = (String) uiSelectOne.getAttribute("align"))) {
	    alignVertical = alignStr.equalsIgnoreCase("vertical") ? 
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
	
	writer.write("<TABLE BORDER=\"" + border + "\">\n");
	if (!alignVertical) {
	    writer.write("\t<TR>\n");
	}
        
	while (items.hasNext()) {
	    curItem = (SelectItem) items.next();
	    if (alignVertical) {
		writer.write("\t<TR>\n");
	    }
            writer.write("<TD><INPUT TYPE=\"RADIO\"");
            if (null != curItem.getValue() &&
		curItem.getValue().equals(currentValue)){
                writer.write(" CHECKED");
            }
            writer.write(" NAME=\"");
            writer.write(uiSelectOne.getCompoundId());
            writer.write("\" VALUE=\"");
            writer.write((String) curItem.getValue());
            writer.write("\">");
            String itemLabel = curItem.getLabel();
            if (itemLabel != null) {
                writer.write(" ");
                writer.write(itemLabel);
            }
            writer.write("</TD>\n");
	    if (alignVertical) {
		writer.write("\t<TR>\n");
	    }
        }

	if (!alignVertical) {
	    writer.write("\t</TR>\n");
	}
	writer.write("</TABLE>");


    }

} // end of class RadioRenderer

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

/*
 * $Id: MenuRenderer.java,v 1.16 2003/07/29 18:23:24 jvisvanathan Exp $
 *
 * (C) Copyright International Business Machines Corp., 2001,2002
 * The source code for this program is not published or otherwise
 * divested of its trade secrets, irrespective of what has been
 * deposited with the U. S. Copyright Office.   
 */

// MenuRenderer.java

package com.sun.faces.renderkit.html_basic;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import javax.faces.component.SelectItem;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UISelectMany;
import javax.faces.component.UISelectOne;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.ConverterException;

import org.mozilla.util.Assert;

import com.sun.faces.util.SelectItemWrapper;
import com.sun.faces.util.Util;

/**
 * <B>MenuRenderer</B> is a class that renders the current value of 
 * <code>UISelectOne<code> or <code>UISelectMany<code> component as a list of 
 * menu options.
 */
 
public class MenuRenderer extends HtmlBasicInputRenderer {
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

    public MenuRenderer() {
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

    public void decode(FacesContext context, UIComponent component)
        throws IOException {
        if (context == null || component == null) {
            throw new NullPointerException(
                Util.getExceptionMessage(
                    Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }

        setPreviousValue(component, ((UIInput)component).currentValue(context));

        String clientId = component.getClientId(context);
        Assert.assert_it(clientId != null);
        // currently we assume the model type to be of type string or 
        // convertible to string and localised by the application.
        if (component instanceof UISelectMany) {
            Map requestParameterValuesMap = context.getExternalContext().
                getRequestParameterValuesMap();
            String newValues[] = (String[])requestParameterValuesMap.
                get(clientId);
            setSelectManyValue(context, ((UISelectMany)component), newValues);
        } else {
            Map requestParameterMap = context.getExternalContext().
                getRequestParameterMap();
            String newValue = (String)requestParameterMap.get(clientId);
            setSelectOneValue(context, ((UISelectOne)component), newValue);
        }    
        return;
    }

    public void setSelectOneValue(FacesContext context, UISelectOne uiSelectOne,
            String newValue) throws ConverterException {
        Object convertedValue = null;
        if ( newValue == null ) {
            uiSelectOne.setValue(null);
            return;
        }
        try {
            convertedValue = getConvertedValue(context, uiSelectOne, newValue);   
            uiSelectOne.setValue(convertedValue);
        } catch (ConverterException ce) {
            uiSelectOne.setValue(newValue);
            addConversionErrorMessage(context, uiSelectOne, ce.getMessage());
            uiSelectOne.setValid(false);
            return;
        }
    }
    
    public void setSelectManyValue(FacesContext context, UISelectMany uiSelectMany,
            String[] newValues) throws ConverterException {
        if ( newValues == null ) {
            uiSelectMany.setValue(null);
            return;
        }
        Object[] convertedValues = new Object[newValues.length];
        // PENDING (visvan) One restriction for UISelectMany component is that
        // if there is converter set, we can't figure out the type using 
        // valueRef since valueRef is not pointing to array of selectedValues.
        // So, one has to explicitly set the connverter attribute for encoding
        // and decoding to happen correctly.
        for ( int i = 0; i < newValues.length; ++i ) {
            try {
                convertedValues[i] = getConvertedValue(context, uiSelectMany, 
                        newValues[i]); 
            } catch (ConverterException ce) {
                uiSelectMany.setValue(newValues);
                addConversionErrorMessage(context, uiSelectMany, 
                        ce.getMessage());
                uiSelectMany.setValid(false);
                return;
            }
        }
        uiSelectMany.setValue(convertedValues);
    }
    
    public void encodeBegin(FacesContext context, UIComponent component)
        throws IOException {
        if (context == null || component == null) {
            throw new NullPointerException(
                Util.getExceptionMessage(
                    Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
    }

    public void encodeChildren(FacesContext context, UIComponent component)
        throws IOException {
        if (context == null || component == null) {
            throw new NullPointerException(
                Util.getExceptionMessage(
                    Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
    }
    
    public void encodeEnd(FacesContext context, UIComponent component) 
            throws IOException {
                
        String styleString = null;
	StringBuffer buffer = null;
        ResponseWriter writer = null;
	
        if (context == null || component == null) {
            throw new NullPointerException(Util.getExceptionMessage(
                    Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
        
        // suppress rendering if "rendered" property on the component is
        // false.
        if (!component.isRendered()) {
            return;
        }    
          
        writer = context.getResponseWriter();
        Assert.assert_it(writer != null );
        
        // PENDING (visvan) here is where we'd hook in a buffer pooling scheme
        buffer = new StringBuffer(1000);
        styleString = getStyleString(component);
        if (styleString != null ) {
	    buffer.append("<span class=\"" + styleString + "\">");
	}
        getSelectBuffer(context, component, buffer);
        if (null != styleString) {
	    buffer.append("</span>");
	}
        writer.write(buffer.toString());
    }

    void getSelectBuffer(
        FacesContext context,
        UIComponent component,
        StringBuffer buff) {
        
        buff.append("<select name=\"");
        buff.append(component.getClientId(context));
        buff.append("\"");
        buff.append(getMultipleText(component));
        StringBuffer optionsBuffer = new StringBuffer(1000);
       
        int itemCount =
            getOptionBuffer(context, component, optionsBuffer);
        itemCount = getDisplaySize(itemCount, component);
        buff.append(Util.renderPassthruAttributes(context, component));
        buff.append(Util.renderBooleanPassthruAttributes(context, component));
        // do not render size attribute. It will be rendered as part of
        // pass through attributes.
        buff.append(">");
        buff.append(optionsBuffer.toString());
        buff.append("</select>");
    }

    int getOptionBuffer(
        FacesContext context, UIComponent component, StringBuffer buff) {
        Object selectedValues[] = getCurrentSelectedValues(context, component);
        int itemCount = 0;
        Iterator items = Util.getSelectItemWrappers(context, component);

        UIComponent curComponent;
        SelectItem curItem = null;
        SelectItemWrapper curItemWrapper = null;
        while (items.hasNext()) {
            itemCount++;
            curItemWrapper = (SelectItemWrapper) items.next();
            curItem = curItemWrapper.getSelectItem();
            curComponent = curItemWrapper.getUISelectItem();
            buff.append("\t<option value=\"");
            // In case a converter attribute is not specifed, we limit using 
            // the valueRef set on UISelectOne/UISelectMany component to lookup
            // the data type of value and not the individual UISelectItem to
            // UISelectItems component. We also limit the data type of all the
            // items to be the same.
            buff.append((getFormattedValue(context, component,
                    curItem.getValue())));
            buff.append("\"");
            buff.append(getSelectedText(curItem, selectedValues));
            buff.append(Util.renderPassthruAttributes(context, curComponent));
            buff.append(Util.renderBooleanPassthruAttributes(context, curComponent));
            buff.append(">");
            buff.append(curItem.getLabel());
            buff.append("</option>\n");
        }
        return itemCount;
    }

    String getSelectedText(SelectItem item, Object[] values) {
        if (null != values) {
            int len = values.length;
            for (int i = 0; i < len; i++) {
                if (values[i].equals(item.getValue())) {
                    return getSelectedTextString();
                }
            }
        }
        return "";
    }

    protected int getDisplaySize(int itemCount, UIComponent component) {
        // if size is not specified default to 1.
        itemCount = 1;
        if (null != component.getAttribute("size")) {
            Integer size = Integer.valueOf((String)component.getAttribute("size"));
            itemCount = size.intValue();
        } else {
             component.setAttribute("size", String.valueOf(itemCount));
        }     
        return itemCount;
    }
    
    String getSelectedTextString() {
        return " selected";
    }
	
    // To derive a selectOne type component from this, override
    // these methods.
    String getMultipleText(UIComponent component) {
        // PENDING (visvan) not sure if this is the best way to check for
        // component type.
        if (component instanceof UISelectMany) {
            return " multiple ";
        } 
        return "";
    }

    Object[] getCurrentSelectedValues(FacesContext context,
				      UIComponent component) {
        if (component instanceof UISelectMany) {
            UISelectMany select = (UISelectMany) component;
            return (Object []) select.currentValue(context);
        } 
        UISelectOne select = (UISelectOne) component;
	Object returnObjects[] = new Object[1];
	if (null != (returnObjects[0] = select.currentValue(context))) {
            return returnObjects;
        }    
	return null;
    }
    
    
    protected String getStyleString(UIComponent component ) {
        String styleString = null;
        // PENDING (visvan) not sure if this is the best way to check for
        // component type.
        if (component instanceof UISelectMany) {
            styleString = (String) component.getAttribute("selectmanyClass");
        } else {
            styleString = (String) component.getAttribute("selectoneClass");
        }    
        return styleString;     
    }
 } // end of class MenuRenderer

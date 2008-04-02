/*
 * $Id: MenuRenderer.java,v 1.8 2003/01/24 21:42:42 rkitain Exp $
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

import javax.faces.component.SelectItem;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UISelectMany;
import javax.faces.component.UISelectOne;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.mozilla.util.Assert;

import com.sun.faces.util.SelectItemWrapper;
import com.sun.faces.util.Util;

/**
 *
 *  <B>MenuRenderer</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: MenuRenderer.java,v 1.8 2003/01/24 21:42:42 rkitain Exp $
 * 
 * @see Blah
 * @see Bloo
 *
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

    public boolean supportsComponentType(String componentType) {
        if (componentType == null) {
            throw new NullPointerException(
                Util.getExceptionMessage(
                    Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
        return (componentType.equals(UISelectMany.TYPE) || 
                componentType.equals(UISelectOne.TYPE));
    }

    public void decode(FacesContext context, UIComponent component)
        throws IOException {
        if (context == null || component == null) {
            throw new NullPointerException(
                Util.getExceptionMessage(
                    Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }

        setPreviousValue(component, component.currentValue(context));

        String clientId = component.getClientId(context);
        Assert.assert_it(clientId != null);
        // currently we assume the model type to be of type string or 
        // convertible to string and localised by the application.
        if ( component.getComponentType().equals(UISelectMany.TYPE)) {
            String newValues[] =
                context.getServletRequest().getParameterValues(clientId);
            component.setValue(newValues);
        } else {
            String newValue =
                context.getServletRequest().getParameter(clientId);
            component.setValue(newValue);
        }    
        component.setValid(true);
	return;
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

    protected void getEndTextToRender(FacesContext context, UIComponent component,
            String currentValue, StringBuffer buffer ) {
      
	String styleString = null;
        styleString = getStyleString(component);
        if (styleString != null ) {
	    buffer.append("<span class=\"" + styleString + "\">");
	}
        getSelectBuffer(context, component, currentValue, buffer);
        if (null != styleString) {
	    buffer.append("</span>");
	}
    }

    void getSelectBuffer(
        FacesContext context,
        UIComponent component,
        String curValue,
        StringBuffer buff) {
        buff.append("<select name=\"");
        buff.append(component.getClientId(context));
        buff.append("\"");
        buff.append(getMultipleText(component));
        // PENDING (visvan) commenting it for now, in case we don't want to use
        // span
       /* String classStr;
        if (null != (classStr = (String) component.getAttribute("selectClass"))) {
            buff.append(" class=\"");
            buff.append(classStr);
            buff.append("\" ");
        } */

        StringBuffer optionsBuffer = new StringBuffer(1000);
        int itemCount =
            getOptionBuffer(context, component, curValue, optionsBuffer);
        itemCount = getDisplaySize(itemCount, component);
        buff.append(Util.renderPassthruAttributes(context, component));
        buff.append(Util.renderBooleanPassthruAttributes(context, component));
        // do not render size attribute. It will be rendered as part of
        // pass through attributes.
        buff.append(">");
        buff.append(optionsBuffer);
        buff.append("</select>");
    }

    int getOptionBuffer(
        FacesContext context,
        UIComponent component,
        String curValue,
        StringBuffer buff) {

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
            buff.append((String) curItem.getValue());
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
        if ( component.getComponentType().equals(UISelectMany.TYPE)) {
            return " multiple ";
        } 
        return "";
    }

    Object[] getCurrentSelectedValues(FacesContext context,
				      UIComponent component) {
         if ( component.getComponentType().equals(UISelectMany.TYPE)) {                              
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
        if ( (component.getComponentType()).equals(UISelectMany.TYPE)) {
            styleString = (String) component.getAttribute("selectmanyClass");
        } else {
            styleString = (String) component.getAttribute("selectoneClass");
        }    
        return styleString;     
    }
  
} // end of class MenuRenderer

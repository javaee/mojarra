/**
 * $Id: SelectManyMenuRenderer.java,v 1.1 2002/09/04 22:32:36 eburns Exp $
 *
 * (C) Copyright International Business Machines Corp., 2001,2002
 * The source code for this program is not published or otherwise
 * divested of its trade secrets, irrespective of what has been
 * deposited with the U. S. Copyright Office.   
 */

// SelectManyMenuRenderer.java

package com.sun.faces.renderkit.html_basic;

import java.io.IOException;
import java.util.Iterator;

import javax.faces.component.SelectItem;
import javax.faces.component.UIComponent;
import javax.faces.component.UISelectMany;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.mozilla.util.Assert;

import com.sun.faces.util.SelectItemWrapper;
import com.sun.faces.util.Util;

/**
 *
 *  <B>SelectManyMenuRenderer</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: SelectManyMenuRenderer.java,v 1.1 2002/09/04 22:32:36 eburns Exp $
 * 
 * @see Blah
 * @see Bloo
 *
 */

public class SelectManyMenuRenderer extends HtmlBasicRenderer {
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

    public SelectManyMenuRenderer() {
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
        return (componentType.equals(UISelectMany.TYPE));
    }

    public void decode(FacesContext context, UIComponent component)
        throws IOException {
        if (context == null || component == null) {
            throw new NullPointerException(
                Util.getExceptionMessage(
                    Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }

        String compoundId = component.getCompoundId();
        Assert.assert_it(compoundId != null);

        String newValues[] = 
	    context.getServletRequest().getParameterValues(compoundId);
        // currently we assume the model type to be of type string or 
        // convertible to string and localised by the application.
        component.setValue(newValues);
        component.setValid(true);
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
        String currentValue = null;

        if (context == null || component == null) {
            throw new NullPointerException(
                Util.getExceptionMessage(
                    Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }

        Object currentObj = component.currentValue(context);
        if (currentObj != null) {
            if (currentObj instanceof String) {
                currentValue = (String) currentObj;
            }
            else {
                currentValue = currentObj.toString();
            }
        }
        if (currentValue == null) {
            currentValue = "";
        }

        StringBuffer buffer = new StringBuffer();

        getSelectBuffer(
            context,
            (UISelectMany) component,
            currentValue,
            buffer);

        currentValue = buffer.toString();

        ResponseWriter writer = null;
        writer = context.getResponseWriter();
        Assert.assert_it(writer != null);
        writer.write(currentValue);
    }

    void getSelectBuffer(
        FacesContext context,
        UISelectMany component,
        String curValue,
        StringBuffer buff) {
        buff.append("<select name=\"");
        buff.append(component.getCompoundId());
        buff.append("\" multiple ");

        if (null != component.getAttribute("selectmanyClass"))
        {
            buff.append(" class=\"");
            buff.append(component.getAttribute("selectmanyClass"));
            buff.append("\" ");
        }

        buff.append(Util.renderPassthruAttributes(context, component));
        buff.append(Util.renderBooleanPassthruAttributes(context, component));

        StringBuffer optionsBuffer = new StringBuffer();
        int itemCount =
            getOptionBuffer(
                context,
                (UISelectMany) component,
                curValue,
                optionsBuffer);

        if (null != component.getAttribute("size"))
        {
            Integer size = (Integer) component.getAttribute("size");
            itemCount = size.intValue();
        }

        buff.append("size=\"");
        buff.append(itemCount);
        buff.append("\">");
        buff.append(optionsBuffer);
        buff.append("</select>");
    }

    int getOptionBuffer(
        FacesContext context,
        UISelectMany selectMany,
        String curValue,
        StringBuffer buff) {
	Object selectedValues[] = selectMany.getSelectedValues();
	int i = 0, len = 0;
        int itemCount = 0;
        Iterator items = Util.getSelectItemWrappers(context, selectMany);

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
	    // Wouldn't a C macro be nice here?
	    if (null != selectedValues) {
		len = selectedValues.length;
		for (i = 0; i < len; i++) {
		    if (selectedValues[i].equals(curItem.getValue())) {
			buff.append(" selected");
		    }
		}
            }
            
            buff.append(Util.renderPassthruAttributes(context,curComponent));
            buff.append(Util.renderBooleanPassthruAttributes(context, curComponent));
            buff.append(">");
            buff.append(curItem.getLabel());
            buff.append("</option>\n");
        }
        return itemCount;
    }

} // end of class SelectManyMenuRenderer

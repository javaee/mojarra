
/**
 * $Id: SelectManyCheckboxListRenderer.java,v 1.2 2002/09/06 22:10:27 rkitain Exp $
 *
 * (C) Copyright International Business Machines Corp., 2001,2002
 * The source code for this program is not published or otherwise
 * divested of its trade secrets, irrespective of what has been
 * deposited with the U. S. Copyright Office.   
 */

// SelectManyCheckboxListRenderer.java

package com.sun.faces.renderkit.html_basic;

import java.util.Iterator;

import javax.faces.component.SelectItem;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import com.sun.faces.util.SelectItemWrapper;
import com.sun.faces.util.Util;

/**
 *
 *  <B>SelectManyCheckboxListRenderer</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: SelectManyCheckboxListRenderer.java,v 1.2 2002/09/06 22:10:27 rkitain Exp $
 * 
 * @see Blah
 * @see Bloo
 *
 */

public class SelectManyCheckboxListRenderer extends SelectManyMenuRenderer {
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

    public SelectManyCheckboxListRenderer() {
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

    public final String PAGE_DIRECTION = "PAGE_START";
    public final String LINE_DIRECTION = "LINE_END";

    void getSelectBuffer(
        FacesContext context,
        UIComponent component,
        String curValue,
        StringBuffer buff) {
        String layoutStr;
        boolean layoutVertical = false;

        String classStr;

        if (null != (layoutStr = (String) component.getAttribute("labelAlign"))) {
            layoutVertical = layoutStr.equalsIgnoreCase(PAGE_DIRECTION) ? true : false;
        }

        if (null != (classStr = (String) component.getAttribute("selectClass"))) {
            buff.append("<span class=\"");
            buff.append(classStr);
            buff.append("\">");
        }

        buff.append(Util.renderPassthruAttributes(context, component));
        buff.append(Util.renderBooleanPassthruAttributes(context, component));

        Iterator items = Util.getSelectItemWrappers(context, component);
        SelectItem curItem = null;
        SelectItemWrapper curItemWrapper = null;
        UIComponent curComponent;
        Object selectedValues[] = getCurrentSelectedValues(component);

        while (items.hasNext()) {
            curItemWrapper = (SelectItemWrapper) items.next();
            curItem = curItemWrapper.getSelectItem();
            curComponent = curItemWrapper.getUISelectItem();

            buff.append("\n<label for=\"");
            buff.append(curComponent.getCompoundId());
            buff.append("\">");
            buff.append(curItem.getLabel());
            buff.append("<input name=\"");
            buff.append(curComponent.getCompoundId());
            buff.append("\" id=\"");
            buff.append(curComponent.getCompoundId());
            buff.append("\" value=\"");
            buff.append((String) curItem.getValue());
            buff.append("\" type=\"checkbox\"");
            buff.append(getSelectedText(curItem, selectedValues));
            buff.append(Util.renderPassthruAttributes(context, curComponent));
            buff.append(Util.renderBooleanPassthruAttributes(context, curComponent));
            buff.append("></label>");
            if (layoutVertical)
                buff.append("<br>");
        }

        if (null != classStr) {
            buff.append("</span>");
        }
    }
	
    String getSelectedTextString() {
        return " checked";
    }
	

} // end of class SelectManyCheckboxListRenderer

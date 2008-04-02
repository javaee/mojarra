/*
 * $Id: ListboxRenderer.java,v 1.5 2002/09/19 00:38:22 jvisvanathan Exp $
 *
 * (C) Copyright International Business Machines Corp., 2001,2002
 * The source code for this program is not published or otherwise
 * divested of its trade secrets, irrespective of what has been
 * deposited with the U. S. Copyright Office.   
 */

// ListboxRenderer.java

package com.sun.faces.renderkit.html_basic;

import java.util.Iterator;

import javax.faces.component.SelectItem;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import com.sun.faces.util.SelectItemWrapper;
import com.sun.faces.util.Util;

/**
 *
 *  <B>ListboxRenderer</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: ListboxRenderer.java,v 1.5 2002/09/19 00:38:22 jvisvanathan Exp $
 * 
 * @see Blah
 * @see Bloo
 *
 */

public class ListboxRenderer extends MenuRenderer {
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

    public ListboxRenderer() {
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

    int getOptionBuffer(
        FacesContext context,
        UIComponent component,
        String curValue,
        StringBuffer buff) {
            Iterator items = Util.getSelectItemWrappers(context, component);
            int itemCount = 0;
            Object selectedValues[] = getCurrentSelectedValues(context, 
							       component);

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
        
        protected int getDisplaySize(int itemCount, UIComponent component) {
            // display all items in the list.
            component.setAttribute("size", String.valueOf(itemCount));
            return itemCount;
        }

} // end of class ListboxRenderer

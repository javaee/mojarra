/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

/*
 * $Id: ListboxRenderer.java,v 1.12 2003/09/04 19:52:17 rkitain Exp $
 *
 * (C) Copyright International Business Machines Corp., 2001,2002
 * The source code for this program is not published or otherwise
 * divested of its trade secrets, irrespective of what has been
 * deposited with the U. S. Copyright Office.   
 */


package com.sun.faces.renderkit.html_basic;

import com.sun.faces.util.SelectItemWrapper;
import com.sun.faces.util.Util;

import java.io.IOException;
import java.util.Iterator;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.model.SelectItem;

import org.mozilla.util.Assert;

/**
 * <B>ListRenderer</B> is a class that renders the current value of 
 * <code>UISelectOne<code> or <code>UISelectMany<code> component as a list of 
 * options.
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

    void renderOptions (FacesContext context, UIComponent component) 
	throws IOException {

	ResponseWriter writer = context.getResponseWriter();
        Assert.assert_it(writer != null );            

        Iterator items = Util.getSelectItemWrappers(context, component);
        Object selectedValues[] = getCurrentSelectedValues(context, 
							       component);
        UIComponent curComponent;
        SelectItem curItem = null;
        SelectItemWrapper curItemWrapper = null;
            
        while (items.hasNext()) {
            curItemWrapper = (SelectItemWrapper) items.next();
            curItem = curItemWrapper.getSelectItem();
            curComponent = curItemWrapper.getUISelectItem();
	    writer.writeText("\t", null);
	    writer.startElement("option", curComponent);
	    writer.writeAttribute("value", 
	        getFormattedValue(context, component, curItem.getValue()), "value");
	    String selectText = getSelectedText(curItem, selectedValues);
	    if (!selectText.equals("")) {
	        writer.writeAttribute(selectText, new Boolean("true"), null);
	    }

            Util.renderPassThruAttributes(writer, curComponent);
            Util.renderBooleanPassThruAttributes(writer, curComponent);

	    writer.writeText(curItem.getLabel(), "label");
	    writer.endElement("option");
	    writer.writeText("\n", null);
        }
    }
        
    protected int getDisplaySize(int itemCount, UIComponent component) {
        // display all items in the list.
        component.setAttribute("size", String.valueOf(itemCount));
        return itemCount;
    }

} // end of class ListboxRenderer

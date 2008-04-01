/*
 * $Id: BasicTextEntry.java,v 1.1 2002/06/09 01:43:12 eburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package basic;

import java.io.IOException;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import javax.faces.component.UITextEntry;


/**

* This textEntry just demonstrates the extensible component mechanism of
* JSF.  It renders itself as a regular text field with the string
* "BASIC" before and after the text field.

 */

public class BasicTextEntry extends UITextEntry {


    // ------------------------------------------------------- Static Variables


    /**
     * The component type of this {@link UIComponent} subclass.
     */
    public static String LOCAL_TYPE = "BasicTextEntry";


    // ------------------------------------------------------------- Properties


    /**
     * <p>Return the component type of this <code>UIComponent</code>.</p>
     */
    public String getComponentType() {

        return (LOCAL_TYPE);

    }


    /**
     * <p>Render the current value of this component.</p>
     *
     * @param context FacesContext for the response we are creating
     *
     * @exception IOException if an input/output error occurs while rendering
     * @exception NullPointerException if <code>context</code>
     *  is <code>null</code>
     */
    public void encodeBegin(FacesContext context) throws IOException {

        if (context == null) {
            throw new NullPointerException();
        }
        Object value = currentValue(context);
        ResponseWriter writer = context.getResponseWriter();
	writer.write("<FONT COLOR=\"RED\">BASIC");
        writer.write("<input type=\"text\" name=\"");
        writer.write(getCompoundId());
        writer.write("\" value=\"");
        if (value != null) {
            writer.write(value.toString());
        }
        writer.write("\">");
	writer.write("BASIC</FONT>");
    }


}

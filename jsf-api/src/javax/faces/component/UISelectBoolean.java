/*
 * $Id: UISelectBoolean.java,v 1.25 2003/02/20 22:46:13 ofung Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


import java.io.IOException;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;


/**
 * <p><strong>UISelectBoolean</strong> is a {@link UIComponent} that
 * represents a single boolean (<code>true</code> or <code>false</code>) value.
 * It is most commonly rendered as a checkbox.</p>
 */

public class UISelectBoolean extends UIInput {


    // ------------------------------------------------------- Static Variables


    /**
     * <p>The component type of this {@link UIComponent} subclass.</p>
     */
    public static final String TYPE = "javax.faces.component.UISelectBoolean";


    // ------------------------------------------------------------- Attributes


    /**
     * <p>Return the local value of the selected state of this component.</p>
     */
    public boolean isSelected() {

        Boolean value = (Boolean) getAttribute("value");
        if (value != null) {
            return (value.booleanValue());
        } else {
            return (false);
        }

    }


    /**
     * <p>Set the local value of the selected state of this component.</p>
     *
     * @param selected The new selected state
     */
    public void setSelected(boolean selected) {

        if (selected) {
            setAttribute("value", Boolean.TRUE);
        } else {
            setAttribute("value", Boolean.FALSE);
        }

    }


    // ------------------------------------------------------------- Properties


    public String getComponentType() {

        return (TYPE);

    }


    // ---------------------------------------------------- UIComponent Methods


    public void decode(FacesContext context) throws IOException {

        if (context == null) {
            throw new NullPointerException();
        }

        // Delegate to our associated Renderer if needed
        setAttribute(UIInput.PREVIOUS_VALUE, currentValue(context));
        if (getRendererType() != null) {
            super.decode(context);
            return;
        }

        // Perform the default decoding
        Boolean newValue = Boolean.FALSE;
        String clientId = getClientId(context);
        if (context.getServletRequest().getParameter(clientId) != null) {
            newValue = Boolean.TRUE;
        }
        setValue(newValue);
        setValid(true);

    }


    public void encodeEnd(FacesContext context) throws IOException {

        if (context == null) {
            throw new NullPointerException();
        }

        // Delegate to our associated Renderer if needed
        if (getRendererType() != null) {
            super.encodeEnd(context);
            return;
        }

        // if rendered is false, do not perform default encoding.
        if (!isRendered()) {
            return;
        }

        // Perform the default encoding
        Boolean value = (Boolean) currentValue(context);
        if (value == null) {
            value = Boolean.FALSE;
        }
        ResponseWriter writer = context.getResponseWriter();
        writer.write("<input type=\"checkbox\" name=\"");
        writer.write(getClientId(context));
        writer.write("\"");
        if (value.booleanValue()) {
            writer.write(" checked=\"checked\"");
        }
        writer.write(">");

    }


}

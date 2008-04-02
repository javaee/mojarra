/*
 * $Id: UISelectBoolean.java,v 1.19 2002/12/17 23:30:53 eburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
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


    // ------------------------------------------------------------ Constructor


    /**
     * <p>Construct a new <code>UISelectBoolean</code> with a default
     * <code>selected</code> property of <code>false</code>.</p>
     */
    public UISelectBoolean() {

        super();

    }


    // ------------------------------------------------------- Static Variables


    /**
     * <p>The component type of this {@link UIComponent} subclass.</p>
     */
    public static final String TYPE = "javax.faces.component.UISelectBoolean";


    // ------------------------------------------------------------- Properties


    /**
     * <p>Return the component type of this <code>UIComponent</code>.</p>
     */
    public String getComponentType() {

        return (TYPE);

    }


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


    // ------------------------------------------- Lifecycle Processing Methods


    /**
     * <p>Decode the new value of this component from the incoming request.</p>
     *
     * @param context FacesContext for the request we are processing
     *
     * @exception IOException if an input/output error occurs while reading
     * @exception NullPointerException if <code>context</code>
     *  is <code>null</code>
     */
    public boolean decode(FacesContext context) throws IOException {

        if (context == null) {
            throw new NullPointerException();
        }

        // Delegate to our associated Renderer if needed
        if (getRendererType() != null) {
            return (super.decode(context));
        }

        // Perform the default decoding
        Boolean newValue = Boolean.FALSE;
        String clientId = getClientId(context);
        if (context.getServletRequest().getParameter(clientId) != null) {
            newValue = Boolean.TRUE;
        }
        setValue(newValue);
        setValid(true);
        return (true);

    }


    /**
     * <p>Render the current value of this component if the value of 
     * the rendered attribute is <code>true</code>. </p>
     *
     * @param context FacesContext for the response we are creating
     *
     * @exception IOException if an input/output error occurs while rendering
     * @exception NullPointerException if <code>context</code>
     *  is <code>null</code>
     */
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

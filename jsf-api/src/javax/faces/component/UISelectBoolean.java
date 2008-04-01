/*
 * $Id: UISelectBoolean.java,v 1.9 2002/06/07 20:13:16 craigmcc Exp $
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

public class UISelectBoolean extends UIComponent {


    // ------------------------------------------------------------ Constructor


    /**
     * <p>Construct a new <code>UISelectBoolean</code> with a default
     * <code>selected</code> property of <code>false</code>.</p>
     */
    public UISelectBoolean() {

        super();
        setSelected(false);

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

        setAttribute("value", new Boolean(selected));

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
    public void decode(FacesContext context) throws IOException {

        if (context == null) {
            throw new NullPointerException();
        }
        Boolean newValue = Boolean.FALSE;
        String compoundId = getCompoundId();
        if (context.getServletRequest().getParameter(compoundId) != null) {
            newValue = Boolean.TRUE;
        }
        setValue(newValue);

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
        Boolean value = (Boolean) currentValue(context);
        if (value == null) {
            value = Boolean.FALSE;
        }
        ResponseWriter writer = context.getResponseWriter();
        writer.write("<input type=\"checkbox\" name=\"");
        writer.write(getCompoundId());
        writer.write("\"");
        if (value.booleanValue()) {
            writer.write(" checked=\"checked\"");
        }
        writer.write(">");

    }


}

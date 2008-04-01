/*
 * $Id: UISelectBoolean.java,v 1.5 2002/05/22 17:47:26 craigmcc Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


import java.io.IOException;
import java.io.PrintWriter;
import javax.faces.context.FacesContext;


/**
 * <p><strong>UISelectBoolean</strong> is a {@link UIComponent} that
 * represents a single boolean (<code>true</code> or <code>false</code>) value.
 * It is most commonly rendered as a checkbox.</p>
 *
 * <p>The local value of the selected state of this component is stored
 * in the <code>value</code> property, and must be a
 * <code>java.lang.Boolean</code> (as must the model property corresponding
 * to any model reference for this component).</p>
 *
 * <p>For convenience, the local value of the selected state is accessible
 * via the <code>isSelected()</code> and <code>setSelected() methods.  The
 * <code>currentValue()</code> method should be used to retrieve the value
 * to be rendered.</p>
 *
 * <h3>Default Behavior</h3>
 *
 * <p>In the absence of a Renderer performing more sophisticated processing,
 * this component supports the following default functionality:</p>
 * <ul>
 * <li><em>decode()</em> - Set this component to true if a request parameter
 *     with our id is included in the request.</li>
 * <li><em>encodeBegin()</em> - Create an HTML
 *     <code>&lt;input type="checkbox"&gt;</code> element, which will be
 *     checked if <code>currentValue()</code> of this component is
 *     <code>true</code>.</li>
 * </ul>
 */

public class UISelectBoolean extends UIComponent {


    // ------------------------------------------------------- Static Variables


    /**
     * The component type of this {@link UIComponent} subclass.
     */
    public static final String TYPE = "SelectBoolean";


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
        if (context.getServletRequest().getParameter(getCompoundId()) != null) {
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
            throw new NullPointerException();
        }
        PrintWriter writer = context.getServletResponse().getWriter();
        writer.print("<input type=\"checkbox\" name=\"");
        writer.print(getCompoundId());
        writer.print("\"");
        if (value.booleanValue()) {
            writer.print(" checked=\"checked\"");
        }
        writer.print(">");

    }


}

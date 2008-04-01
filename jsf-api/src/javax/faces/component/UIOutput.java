/*
 * $Id: UIOutput.java,v 1.17 2002/08/30 20:11:20 craigmcc Exp $
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
 * <p><strong>UIOutput</strong> is a {@link UIComponent} that displays
 * output to the user.  The user cannot manipulate this component; it is
 * for display purposes only.  There are no restrictions on the data type
 * of the local value, or the object referenced by the model reference
 * expression (if any); however, individual <code>Renderer</code>s will
 * generally impose restrictions on the type of data they know how to
 * display.</p>
 */

public class UIOutput extends UIComponentBase {


    // ------------------------------------------------------- Static Variables


    /**
     * The component type of this {@link UIComponent} subclass.
     */
    public static final String TYPE = "javax.faces.component.UIOutput";


    // ------------------------------------------------------------- Properties


    /**
     * <p>Return the component type of this <code>UIComponent</code>.</p>
     */
    public String getComponentType() {

        return (TYPE);

    }


    // ------------------------------------------- Lifecycle Processing Methods


    /**
     * <p>This component is output only, so do not perform any decode
     * processing during the <em>Apply Request Values</em> phase of
     * the request processing lifecycle.</p>
     *
     * @param context FacesContext for the request we are processing
     *
     * @exception IOException if an input/output error occurs while rendering
     * @exception NullPointerException if <code>context</code>
     *  is <code>null</code>
     */
    public boolean decode(FacesContext context) throws IOException {

        if (context == null) {
            throw new NullPointerException();
        }
        setValid(true);
        return (true);

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
    public void encodeEnd(FacesContext context) throws IOException {

        if (context == null) {
            throw new NullPointerException();
        }

        // Delegate to our associated Renderer if needed
        if (getRendererType() != null) {
            super.encodeEnd(context);
            return;
        }

        // Perform the default encoding
        Object value = currentValue(context);
        if (value != null) {
            ResponseWriter writer = context.getResponseWriter();
            writer.write(value.toString());
        }

    }


    /**
     * <p>This component is output only, so do not update the model
     * even if there is a non-null <code>modelReference</code> expression.
     *
     * @param context FacesContext for the request we are processing
     */
    public void updateModel(FacesContext context) {

        ; // No action required

    }


}

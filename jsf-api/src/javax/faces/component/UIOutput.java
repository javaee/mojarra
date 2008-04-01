/*
 * $Id: UIOutput.java,v 1.2 2002/05/14 00:41:37 craigmcc Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


/**
 * <p><strong>UIOutput</strong> is a {@link UIComponent} that can display
 * output to the user.  The user cannot manipulate this component; it is
 * for display purposes only.  Any localization of the text to be rendered
 * is the responsibility of the application.</p>
 *
 * <p>The local value of the text to be displayed by this component
 * is stored in the <code>value</code> property, and must be a
 * <code>java.lang.String</code> (as must the model property corresponding
 * to any model reference for this component).</p>
 *
 * <p>For convenience, the local value of the text to be displayed is
 * accessible via the <code>getText()</code> and <code>setText()</code>
 * methods.  The <code>currentValue()</code> method should be used to
 * retrieve the value to be rendered.</p>
 */

public class UIOutput extends UIComponent {


    // ------------------------------------------------------- Static Variables


    /**
     * The component type of this {@link UIComponent} subclass.
     */
    public static final String TYPE = "Output";


    // ------------------------------------------------------------- Properties


    /**
     * <p>Return the component type of this <code>UIComponent</code>.</p>
     */
    public String getComponentType() {

        return (TYPE);

    }


    /**
     * <p>Return the local value of the text to be displayed.</p>
     */
    public String getText() {

        return ((String) getAttribute("value"));

    }


    /**
     * <p>Set the local value of the text to be displayed.</p>
     *
     * @param text The new text
     */
    public void setText(String text) {

        setAttribute("value", text);

    }


    // --------------------------------------------------------- Public Methods


}

/*
 * $Id: UISelectBoolean.java,v 1.2 2002/05/15 18:20:08 craigmcc Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


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


}

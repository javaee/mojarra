/*
 * $Id: UISelectBoolean.java,v 1.31 2004/01/21 19:23:16 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


/**
 * <p><strong>UISelectBoolean</strong> is a {@link UIComponent} that
 * represents a single boolean (<code>true</code> or <code>false</code>) value.
 * It is most commonly rendered as a checkbox.</p>
 *
 * <p>By default, the <code>rendererType</code> property must be set to
 * "<code>Checkbox</code>".  This value can be changed by calling the
 * <code>setRendererType()</code> method.</p>
 */

public class UISelectBoolean extends UIInput {


    // ------------------------------------------------------ Manifest Constants


    /**
     * <p>The standard component type for this component.</p>
     */
    public static final String COMPONENT_TYPE = "javax.faces.SelectBoolean";


    /**
     * <p>The standard component family for this component.</p>
     */
    public static final String COMPONENT_FAMILY = "javax.faces.SelectBoolean";


    // ------------------------------------------------------------ Constructors


    /**
     * <p>Create a new {@link UISelectBoolean} instance with default
     * property values.</p>
     */
    public UISelectBoolean() {

        super();
        setRendererType("Checkbox");

    }


    // -------------------------------------------------------------- Properties


    /**
     * <p>Return the local value of the selected state of this component.
     * This method is a typesafe alias for <code>getValue()</code>.</p>
     */
    public boolean isSelected() {

        Boolean value = (Boolean) getValue();
        if (value != null) {
            return (value.booleanValue());
        } else {
            return (false);
        }

    }


    /**
     * <p>Set the local value of the selected state of this component.
     * This method is a typesafe alias for <code>setValue()</code>.</p>
     *
     * @param selected The new selected state
     */
    public void setSelected(boolean selected) {

        if (selected) {
            setValue(Boolean.TRUE);
        } else {
            setValue(Boolean.FALSE);
        }

    }


}

/*
 * $Id: UISelectBoolean.java,v 1.38 2005/08/22 22:07:57 ofung Exp $
 */

/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 * 
 * You can obtain a copy of the License at
 * https://javaserverfaces.dev.java.net/CDDL.html or
 * legal/CDDLv1.0.txt. 
 * See the License for the specific language governing
 * permission and limitations under the License.
 * 
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at legal/CDDLv1.0.txt.    
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 * 
 * [Name of File] [ver.__] [Date]
 * 
 * Copyright 2005 Sun Microsystems Inc. All Rights Reserved
 */

package javax.faces.component;

import javax.el.ValueExpression;
import javax.faces.el.ValueBinding;


/**
 * <p><strong>UISelectBoolean</strong> is a {@link UIComponent} that
 * represents a single boolean (<code>true</code> or <code>false</code>) value.
 * It is most commonly rendered as a checkbox.</p>
 *
 * <p>By default, the <code>rendererType</code> property must be set to
 * "<code>javax.faces.Checkbox</code>".  This value can be changed by
 * calling the <code>setRendererType()</code> method.</p>
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
        setRendererType("javax.faces.Checkbox");

    }


    // -------------------------------------------------------------- Properties


    public String getFamily() {

        return (COMPONENT_FAMILY);

    }


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


    // ---------------------------------------------------------------- Bindings


    /**
     * <p>Return any {@link ValueBinding} set for <code>value</code> if a
     * {@link ValueBinding} for <code>selected</code> is requested; otherwise,
     * perform the default superclass processing for this method.</p>
     *
     * <p>Rely on the superclass implementation to wrap the returned
     * <code>ValueExpression</code> in a <code>ValueBinding</code>.</p>
     *
     * @param name Name of the attribute or property for which to retrieve
     *  a {@link ValueBinding}
     *
     * @exception NullPointerException if <code>name</code>
     *  is <code>null</code>
     *
     * @deprecated This has been replaced by {@link
     * #getValueExpression}.
     */
    public ValueBinding getValueBinding(String name) {

        if ("selected".equals(name)) {
            return (super.getValueBinding("value"));
        } else {
            return (super.getValueBinding(name));
        }

    }


    /**
     * <p>Store any {@link ValueBinding} specified for <code>selected</code>
     * under <code>value</code> instead; otherwise, perform the default
     * superclass processing for this method.</p>
     *
     * <p>Rely on the superclass implementation to wrap the argument
     * <code>ValueBinding</code> in a <code>ValueExpression</code>.</p>
     *
     * @param name Name of the attribute or property for which to set
     *  a {@link ValueBinding}
     * @param binding The {@link ValueBinding} to set, or <code>null</code>
     *  to remove any currently set {@link ValueBinding}
     *
     * @exception NullPointerException if <code>name</code>
     *  is <code>null</code>
     *
     * @deprecated This has been replaced by {@link #setValueExpression}.
     */
    public void setValueBinding(String name, ValueBinding binding) {

        if ("selected".equals(name)) {
            super.setValueBinding("value", binding);
        } else {
            super.setValueBinding(name, binding);
        }

    }

    /**
     * <p>Return any {@link ValueExpression} set for <code>value</code>
     * if a {@link ValueExpression} for <code>selected</code> is
     * requested; otherwise, perform the default superclass processing
     * for this method.</p>
     *
     * @param name Name of the attribute or property for which to retrieve
     *  a {@link ValueExpression}
     *
     * @exception NullPointerException if <code>name</code>
     *  is <code>null</code>
     * @since 1.2
     */
    public ValueExpression getValueExpression(String name) {

        if ("selected".equals(name)) {
            return (super.getValueExpression("value"));
        } else {
            return (super.getValueExpression(name));
        }

    }
    
    /**
     * <p>Store any {@link ValueExpression} specified for <code>selected</code>
     * under <code>value</code> instead; otherwise, perform the default
     * superclass processing for this method.</p>
     *
     * @param name Name of the attribute or property for which to set
     *  a {@link ValueExpression}
     * @param binding The {@link ValueExpression} to set, or <code>null</code>
     *  to remove any currently set {@link ValueExpression}
     *
     * @exception NullPointerException if <code>name</code>
     *  is <code>null</code>
     * @since 1.2
     */
    public void setValueExpression(String name, ValueExpression binding) {

        if ("selected".equals(name)) {
            super.setValueExpression("value", binding);
        } else {
            super.setValueExpression(name, binding);
        }

    }


}

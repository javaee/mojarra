/*
 * $Id: UIParameter.java,v 1.1 2002/08/15 18:26:59 craigmcc Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


/**
 * <p><strong>UIParameter</strong> is a {@link UIComponent} that represents
 * an optionally named configuration parameter for a parent component.  It has
 * no <code>decode()</code> or <code>encode()</code> behavior of its own.</p>
 *
 * <p>Parent components should retrieve the value of a parameter by calling
 * <code>currentValue()</code>.  In this way, the parameter value can be set
 * directly on the component (via <code>setValue()</code>), or retrieved
 * indirectly via the model reference expression.</p>
 *
 * <p>In some scenarios, it is necessary to provide a parameter name, in
 * addition to the parameter value that is accessible via the
 * <code>currentValue()</code> method.  Renderers that support parameter names
 * on their nested <code>UIParameter</code> child components should document
 * their use of this property.</p>
 */

public class UIParameter extends UIOutput {


    // ------------------------------------------------------- Static Variables


    /**
     * The component type of this {@link UIComponent} subclass.
     */
    public static final String TYPE = "javax.faces.component.UIParameter";


    // ------------------------------------------------------------- Properties


    /**
     * <p>Return the component type of this <code>UIComponent</code>.</p>
     */
    public String getComponentType() {

        return (TYPE);

    }


    /**
     * <p>Return the optional parameter name for this parameter.</p>
     */
    public String getName() {

        return ((String) getAttribute("name"));

    }


    /**
     * <p>Set the optional parameter name for this parameter.</p>
     *
     * @param name The new parameter name, or <code>null</code> for no name
     */
    public void setName(String name) {

        setAttribute("name", name);

    }


    // ------------------------------------------- Lifecycle Processing Methods


}

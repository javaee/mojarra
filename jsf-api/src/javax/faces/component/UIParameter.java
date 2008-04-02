/*
 * $Id: UIParameter.java,v 1.6 2003/04/29 18:51:31 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


/**
 * <p><strong>UIParameter</strong> is a {@link UIComponent} that represents
 * an optionally named configuration parameter for a parent component.</p>
 *
 * <p>Parent components should retrieve the value of a parameter by calling
 * <code>currentValue()</code>.  In this way, the parameter value can be set
 * directly on the component (via <code>setValue()</code>), or retrieved
 * indirectly via the model reference expression.</p>
 *
 * <p>In some scenarios, it is necessary to provide a parameter name, in
 * addition to the parameter value that is accessible via the
 * <code>currentValue()</code> method.
 * {@link javax.faces.render.Renderer}s that support parameter names on their
 * nested {@link UIParameter} child components should document
 * their use of this property.</p>
 */

public class UIParameter extends UIOutput {


    // ------------------------------------------------------- Static Variables

    // ----------------------------------------------------------- Constructors


    /**
     * <p>Create a new {@link UIParameter} instance with default property
     * values.</p>
     */
    public UIParameter() {

        super();
        setRendererType(null);

    }


    // ------------------------------------------------------------- Attributes


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


    // ------------------------------------------------------------- Properties

    /**
     * <p>Return <code>true</code> to indicate that no
     * {@link javax.faces.render.Renderer} needs to be associated
     * with this component.</p>
     */
    public boolean getRendersSelf() {

        return (true);

    }


}

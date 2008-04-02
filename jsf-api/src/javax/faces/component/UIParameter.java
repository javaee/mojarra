/*
 * $Id: UIParameter.java,v 1.3 2003/02/03 22:57:47 craigmcc Exp $
 */

/*
 * Copyright 2002-2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


import java.io.IOException;
import javax.faces.context.FacesContext;


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


    /**
     * The component type of this {@link UIComponent} subclass.
     */
    public static final String TYPE = "javax.faces.component.UIParameter";


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


    public String getComponentType() {

        return (TYPE);

    }


    /**
     * <p>Return <code>true</code> to indicate that no
     * {@link javax.faces.render.Renderer} needs to be associated
     * with this component.</p>
     */
    public boolean getRendersSelf() {

        return (true);

    }


    // ---------------------------------------------------- UIComponent Methods


    /**
     * <p>Override the default behavior and perform no encoding.</p>
     *
     * @param context {@link FacesContext} for the response we are creating
     *
     * @exception IOException if an input/output error occurs while rendering
     * @exception NullPointerException if <code>context</code>
     *  is <code>null</code>
     */
    public void encodeBegin(FacesContext context) throws IOException {

        if (context == null) {
            throw new NullPointerException();
        }

    }


    /**
     * <p>Override the default behavior and perform no encoding.</p>
     *
     * @param context {@link FacesContext} for the response we are creating
     *
     * @exception IOException if an input/output error occurs while rendering
     * @exception NullPointerException if <code>context</code>
     *  is <code>null</code>
     */
    public void encodeChildren(FacesContext context) throws IOException {

        if (context == null) {
            throw new NullPointerException();
        }

    }


    /**
     * <p>Override the default behavior and perform no encoding.</p>
     *
     * @param context {@link FacesContext} for the response we are creating
     *
     * @exception IOException if an input/output error occurs while rendering
     * @exception NullPointerException if <code>context</code>
     *  is <code>null</code>
     */
    public void encodeEnd(FacesContext context) throws IOException {

        if (context == null) {
            throw new NullPointerException();
        }

    }


}

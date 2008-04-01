/*
 * $Id: ComponentTag.java,v 1.1 2002/06/06 04:30:32 craigmcc Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.webapp;


import javax.faces.component.UIComponent;


/**
 * <p><strong>ComponentTag</strong> is a generic custom action that accepts
 * only a component identifier (<code>id</code>) and renderer type
 * (<code>rendererType</code>) attributes, and assumes that everything else
 * has been configured in the external metadata that describes the component
 * tree.</p>
 */

public final class ComponentTag extends FacesTag {


    // ------------------------------------------------------------- Properties


    /**
     * <p>The override renderer type.</p>
     */
    private String rendererType = null;


    /**
     * <p>Return the override renderer type.</p>
     */
    public String getRendererType() {

        return (this.rendererType);

    }


    /**
     * <p>Set the override renderer type.</p>
     *
     * @param rendererType The new override renderer type
     */
    public void setRendererType(String rendererType) {

        this.rendererType = rendererType;

    }


    // ------------------------------------------------------------ Tag Methods


    /**
     * <p>Release any resources allocated during the execution of this
     * tag handler.</p>
     */
    public void release() {

        super.release();
        this.rendererType = null;

    }


    // ------------------------------------------------------ Protected Methods


    /**
     * <p>Override properties of the specified component if the corresponding
     * properties of this tag handler were explicitly set.</p>
     *
     * <p>Tag subclasses that want to support additional override properties
     * must ensure that the base class <code>overrideProperties()</code>
     * method is still called.  A typical implementation that supports
     * extra properties <code>foo</code> and <code>bar</code> would look
     * something like this:</p>
     * <pre>
     * protected void overrideProperties(UIComponent component) {
     *   super.overrideProperties(component);
     *   if (foo != null) {
     *     component.setAttribute("foo", foo);
     *   }
     *   if (bar != null) {
     *     component.setAttribute("bar", bar);
     *   }
     * }
     * </pre>
     */
    protected void overrideProperties(UIComponent component) {

        super.overrideProperties(component);
        if (rendererType != null) {
            component.setRendererType(rendererType);
        }

    }


}

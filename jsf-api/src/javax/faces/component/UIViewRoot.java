/*
 * $Id: UIViewRoot.java,v 1.1 2003/08/22 14:03:12 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;

/**
 * <p><strong>UIViewRoot</strong> is the UIComponent that represents the
 * root of the UIComponent tree.  This component has no rendering, it
 * just serves as the root.</p>
 */

public interface UIViewRoot extends UIComponent, NamingContainer {

    // ------------------------------------------------------------- Properties

    /**
     * <p>Return the render kit identifier of the {@link
     * javax.faces.render.RenderKit} instance (if any) associated with
     * this view.</p>
     */
    public String getRenderKitId();

    /**
     * <p>Set the render kit identifier of the {@link
     * javax.faces.render.RenderKit} instance (if any) associated with
     * this view.</p>
     *
     * @param renderKitId The new {@link javax.faces.render.RenderKit}
     * identifier, or <code>null</code> to disassociate this view with
     * any specific {@link javax.faces.render.RenderKit} instance
     */
    public void setRenderKitId(String renderKitId);

    /**
     * <p>Return the view identifier for this view.</p>
     */
    public String getViewId();

    /**
     *
     * <p>Assign the view identifier for this view.</p>
     *
     */

    public void setViewId(String viewId);

}



	

    


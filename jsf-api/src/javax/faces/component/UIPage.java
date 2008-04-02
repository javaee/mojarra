/*
 * $Id: UIPage.java,v 1.4 2003/08/02 05:11:33 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;

/**
 * <p><strong>UIPage</strong> is the UIComponent that represents the
 * root of the UIComponent hierarchy.  This component has no rendering,
 * it just serves as the root.</p>
 */

public interface UIPage extends UIComponent, NamingContainer {

    // ------------------------------------------------------------- Properties

    /**
     * <p>Return the render kit identifier of the {@link
     * javax.faces.render.RenderKit} instance (if any) associated with
     * this tree.</p>
     */
    public String getRenderKitId();

    /**
     * <p>Set the render kit identifier of the {@link
     * javax.faces.render.RenderKit} instance (if any) associated with
     * this tree.</p>
     *
     * @param renderKitId The new {@link javax.faces.render.RenderKit}
     * identifier, or <code>null</code> to disassociate this tree with
     * any specific {@link javax.faces.render.RenderKit} instance
     */
    public void setRenderKitId(String renderKitId);

    /**
     * <p>Return the tree identifier for this tree.</p>
     */
    public String getTreeId();

    /**
     *
     * <p>Assign the tree identifier for this page.</p>
     *
     */

    public void setTreeId(String treeId);

}



	

    


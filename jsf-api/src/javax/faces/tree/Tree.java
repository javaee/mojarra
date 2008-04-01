/*
 * $Id: Tree.java,v 1.2 2002/05/20 17:17:35 craigmcc Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.tree;


import javax.faces.component.UIComponent;
import javax.faces.render.RenderKit;


/**
 * <p>A <strong>Tree</strong> is a container for a tree of {@link UIComponent}
 * nodes rooted at a node, along with associated metadata about those
 * {@link UIComponent}s.  Trees have unique identifiers, which associate
 * them with metadata information that can be acquired from external sources,
 * by means of a {@link TreeFactory} instance.  Tree identifiers must be
 * unique within the scope of a web application.</p>
 */

public abstract class Tree {


    // ------------------------------------------------------------- Properties


    /**
     * <p>Return the {@link RenderKit} instance (if any) associated with
     * this <code>Tree</code>.</p>
     */
    public abstract RenderKit getRenderKit();


    /**
     * <p>Set the {@link RenderKit} instance (if any) associated with
     * this <code>Tree</code>.</p>
     *
     * @param renderKit The new {@link RenderKit}, or <code>null</code>
     *  to disassociate this tree with any specific RenderKit instance
     */
    public abstract void setRenderKit(RenderKit renderKit);


    /**
     * <p>Return the root node of the component tree associated with
     * this <code>Tree</code>.</p>
     */
    public abstract UIComponent getRoot();


    /**
     * <p>Return the tree identifier for this <code>Tree</code>.</p>
     */
    public abstract String getTreeId();


    // --------------------------------------------------------- Public Methods


    /**
     * <p>Release any resources associated with this <code>Tree</code>
     * instance.  Faces implementations may choose to pool instances in the
     * associated {@link TreeFactory} to avoid repeated object creation
     * and garbage collection.
     */
    public abstract void release();


}

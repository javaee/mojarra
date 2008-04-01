/*
 * $Id: Tree.java,v 1.1 2002/05/14 15:02:29 craigmcc Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.tree;


import javax.faces.component.UIComponent;


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

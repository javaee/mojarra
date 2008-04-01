/*
 * $Id: UIContainer.java,v 1.1 2002/05/07 05:18:57 craigmcc Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;

import java.util.Iterator;


/**
 * <p><strong>UIContainer</strong> is the base class for all
 * {@link UIComponent} classes that also support child components.  These
 * components will typically represent complex user interface objects such
 * as tables and tree controls, but can also be used to represent any
 * desired grouping of components.</p>
 *
 * <p>A basic <code>UIContainer</code> has no additional render-dependent
 * or render-independent attributes.  However, subclasses of
 * <code>UIContainer</code> normally will support such attributes.</p>
 *
 * <p>The child {@link UIComponent}s of a <code>UIContainer</code> instance
 * are maintained in a well-defined order that is defined as those components
 * are added to, and removed from, the parent.  In addition, all of the
 * direct children of a <code>UIContainer</code> instance MUST have unique
 * values for the <code>id</code> attribute.</p>
 *
 * <p><strong>FIXME</strong> - Much more about responsibility
 * for child layout and all that stuff.</p>
 */

public abstract class UIContainer extends UIComponent {


    /**
     * The component type of this {@link UIComponent} subclass.
     */
    public static final String TYPE = "Container";


    // -------------------------------------------------- Child Support Methods


    /**
     * <p>Insert the specified {@link UIComponent} at the specified
     * position in the child list.</p>
     *
     * @param index Zero-relative index at which to add this
     *  {@link UIComponent}
     * @param component {@link UIComponent} to be added
     *
     * @exception IndexOutOfBoundsException if the index is out of range
     *  ((index < 0) || (index &gt;= size()))
     * @exception NullPointerException if <code>component</code> is null
     */
    public abstract void add(int index, UIComponent component);


    /**
     * <p>Append the specified {@link UIComponent} to the end of the
     * child list.</p>
     *
     * @param component {@link UIComponent} to be added
     *
     * @exception NullPointerException if <code>component</code> is null
     */
    public abstract void add(UIComponent component);


    /**
     * <p>Remove all child {@link UIComponent}s from the child list,
     * recursively performing this operation when a child {@link UIComponent}
     * is in fact a {@link UIContainer}.</p>
     */
    public abstract void clear();


    /**
     * <p>Return <code>true</code> if the specified {@link UIComponent}
     * is a direct child of this <code>UIContainer</code>; otherwise,
     * return <code>false</code>.</p>
     *
     * @param component {@link UIComponent} to be checked
     *
     * @exception NullPointerException if <code>component</code> is null
     */
    public abstract boolean contains(UIComponent component);


    /**
     * <p>Return the {@link UIComponent} at the specified position
     * in the child list.</p>
     *
     * @param index Position of the desired component
     *
     * @exception IndexOutOfBoundsException if index is out of range
     *  ((index &lt; 0) || (index &gt;= size()))
     */
    public abstract UIComponent get(int index);


    /**
     * <p>Return the index of the specified {@link UIComponent} in the
     * child list, or <code>-1</code> if this component is not a child.</p>
     *
     * @param component {@link UIComponent} to be checked
     *
     * @exception NullPointerException if <code>component</code> is null
     */
    public abstract int indexOf(UIComponent component);


    /**
     * <p>Return an <code>Iterator</code> over the child {@link UIComponent}s
     * of this <code>UIContainer</code> in the proper sequence.</p>
     */
    public abstract Iterator iterator();


    /**
     * <p>Remove the child {@link UIComponent} at the specified position
     * in the child list.</p>
     *
     * @param index Position of the desired component
     *
     * @exception NullPointerException if <code>component</code> is null
     */
    public abstract void remove(int index);


    /**
     * <p>Remove the child {@link UIComponent} from the child list.</p>
     *
     * @param component {@link UIComponent} to be removed
     *
     * @exception NullPointerException if <code>component</code> is null
     */
    public abstract void remove(UIComponent component);


    /**
     * <p>Replace the child {@link UIComponent} at the specified position
     * in the child list.</p>
     *
     * @param index Position of the desired component
     * @param component The new component
     *
     * @exception NullPointerException if <code>component</code> is null
     */
    public abstract void set(int index, UIComponent component);


    /**
     * <p>Return the number of {@link UIComponent}s on the child list.</p>
     */
    public abstract int size();


}

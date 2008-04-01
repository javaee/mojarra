/*
 * $Id: Renderer.java,v 1.7 2001/12/20 22:25:45 ofung Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces;

import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.util.Iterator;

/**
 * The interface for defining objects which render user-interface
 * components for a given render kit.  
 * <p>
 * A renderer object can support the rendering of one or more 
 * component types.  It defines the set of component types it
 * supports by providing methods which take either a WComponent
 * instance or component-type String and returns a boolean value
 * indicating whether it supports that type or not.
 * <p>
 * A renderer advertizes the set of render attributes it supports
 * for each component-type by providing a method which returns an
 * iterator of supported attribute names for a given component type.
 * A renderer provides meta-data by providing methods to obtain
 * JavaBeans PropertyDescriptor objects corresponding to those
 * supported attributes.
 *<p>
 * A renderer should be stateless, deriving all appropriate 
 * state for rendering from the WComponent parameter.
 *
 */

public interface Renderer {

    /**
     * This method is used to determine whether or not the component
     * type of the specified component is supported by this renderer.
     * @throws NullPointerException if c is null
     * @return a boolean value indicating whether or not the specified
     *         component type can be rendered by this renderer
     */
    public boolean supportsType(WComponent c);

    /**
     * This method is used to determine whether or not the specified
     * component type is supported by this renderer.
     * @throws NullPointerException if componentType is null
     * @return a boolean value indicating whether or not the specified
     *         component type can be rendered by this renderer
     */
    public boolean supportsType(String componentType);

    /**
     * Returns an iterator containing the names of this renderer's
     * supported attributes for the specified component type.  
     * This attribute list should contain all attributes used by this 
     * renderer during the rendering process for the component type.
     * @param componentType string representing the type of component
     * @return an iterator containing the Strings representing supported
     *          attribute names
     * @throws NullPointerException if componentType is null
     * @throws FacesException if the specified componentType is not
     *         supported by this renderer
     */
    public Iterator getSupportedAttributeNames(String componentType) throws FacesException;

    /**
     * Returns an iterator containing PropertyDescriptor objects
     * corresponding to this renderer's supported attributes
     * for the specified component type.  This attribute list should 
     * contain all attributes used by this renderer during the rendering 
     * process for the component type.
     * @param componentType string representing the type of component
     * @return an iterator containing the Strings representing supported
     *          attribute names
     * @throws NullPointerException if componentType is null
     * @throws FacesException if the specified componentType is not
     *         supported by this renderer
     */
    public Iterator getSupportedAttributes(String componentType) throws FacesException;

    /**
     * Returns a property descriptor for the specified attribute name.
     * @param attributeName the name of the render attribute
     * @throws NullPointerException if attributeName is null
     * @throws FacesException if the specified attributeName is not
     *         supported by this renderer
     * @return PropertyDescriptor object describing the named attribute
     */
    public PropertyDescriptor getAttributeDescriptor(String attributeName)
	throws FacesException;

    /**
     * Invoked to render the specified component using the specified 
     * render context.  An attribute value used during rendering
     * is obtained by first looking for a component-specific value
     * using c.getAttribute() and if not set directly on the component,
     * using the default value of that attribute defined by this renderer. 
     * @see WComponent#render
     * @see WComponent#getAttribute
     * @param rc the render context used to render the specified component
     * @param c the WComponent instance representing the component state
     *          being rendered
     * @throws IOException
     * @throws FacesException if the specified componentType is not
     *         supported by this renderer
     * @throws NullPointerException if rc or c is null
     */
    public void renderStart(RenderContext rc, WComponent c) 
        throws IOException, FacesException;

    /**
     * Invoked to render the children of the specified component using
     * the specified render context.  If this renderer supports rendering
     * of a component type which returns <code>true</code> from the
     * getPerformsLayout() method, it must generate output required to
     * layout the children of the specified component, as well as drive
     * the render process of those children by invoking renderAll() on
     * each child.  If the specified component type returns =
<code>false</code>
     * from getPerformsLayout() then this method should do nothing.
     * @see WComponent#renderAll
     * @see WComponent#renderChildren
     * @param rc the render context used to render the specified component
     * @param c the WComponent instance representing the component state
     *          being rendered
     * @throws IOException
     * @throws NullPointerException if rc or c is null
     */ 
    public void renderChildren(RenderContext rc, WComponent c) throws IOException;

    /**
     * Invoked after all of the specified component's descendents have
     * been rendered. 
     * @see WComponent#renderComplete
     * @param rc the render context used to render the specified component
     * @param c the WComponent instance representing the component state
     *          being rendered
     * @throws IOException
     * @throws NullPointerException if rc or c is null
     */
    public void renderComplete(RenderContext rc, WComponent c) throws IOException, FacesException;

 }


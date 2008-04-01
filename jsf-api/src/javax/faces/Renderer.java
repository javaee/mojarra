package javax.faces;

import java.io.IOException;
import java.util.Iterator;

/**
 * The interface for defining objects which render user-interfaces
 * components for a given render kit.
 *
 */

public interface Renderer {

    /**
     * Returns an iterator containing the supported attribute names
     * for the specified component type.  This attribute list should
     * contain all attributes used by this renderer during the
     * rendering process.
     * @param componentType string representing the type of component
     * @return an iterator containing the Strings representing supported
     *          attribute names
     * @throws NullPointerException if componentType is null
     * @throws FacesException if the specified componentType is not
     *         supported by this renderer
     */
    public Iterator getSupportedAttributeNames(String componentType);

    /**
     * Invoked to render the specified component using the specified 
     * render context.  
     * @param rc the render context used to render the specified component
     * @param c the WComponent instance representing the component state
     *          being rendered
     * @throws IOException
     * @throws NullPointerException if rc or c is null
     */
    public void renderStart(RenderContext rc, WComponent c) throws IOException;

    /**
     * Invoked to render the children of the specified component using
     * the specified render context.  A Renderer which can drive the
     * rendering of the component's children must take responsibility for
     * laying out the children during the rendering process and should
     * return true from getCanRenderChildren(). Renderers which do not 
     * have this capability should return false from getCanRenderChildren()
     * and implement this method to do nothing.
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
     * @param rc the render context used to render the specified component
     * @param c the WComponent instance representing the component state
     *          being rendered
     * @throws IOException
     * @throws NullPointerException if rc or c is null
     */
    public void renderEnd(RenderContext rc, WComponent c) throws IOException;

 
    /**
     * Returns a boolean value describing whether or not this renderer
     * has the capability to drive the rendering of the specified
     * component's children.  Renderers which support rendering the
     * component's children are responsible for rendering the layout
     * of those children.
     * @param rc the render context used to render the specified component
     * @param c the WComponent instance representing the component state
     *          being rendered
     * @throws IOException
     * @throws NullPointerException if rc or c is null
     */
    public boolean getCanRenderChildren(RenderContext rc, WComponent c);

}

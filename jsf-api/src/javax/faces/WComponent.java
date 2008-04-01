package javax.faces;

import java.io.IOException;
import java.util.Iterator;


/**
 * The base class for representing all state associated with a
 * user-interface component.  
 *
 * A WComponent instance may act as a "parent" container if one or
 * more WComponent instances are added as children.
 * A WComponent instance may act as a "child" if it is added to
 * one or more parent WComponent instances.
 */
public abstract class WComponent {
 
    /** Returns a String representing the type of this concrete
     * component class.  This read-only property is used by the RenderKit
     * instance as the name key for mapping a component type to its 
     * available renderers.  A concrete subclass should return a
     * unique String value which is descriptive of the functional
     * purpose of the component.
     *
     * @return a String representing the component functional type
     *         
     */
    // Aim10-25-01: hint-WTextEntry should return "TextEntry"
    public abstract String getType();
 
    /**
     * Returns an iteration containing all the attribute names
     * available with this component within the specified render
     * context.
     *
     * @param rc the render context used to render this component
     * @return an Iteration of attribute names on this component
     */   
    public Iterator getAttributeNames(RenderContext rc) {
        return null;
    }

    /**
     * Returns the component attribute with the given name
     * within the specified render context or null if there is no
     * attribute by that name.
     *
     * @param rc the render context used to render this component
     * @param attributeName a String specifying the name of the attribute
     * @return the Object bound to the attribute name, or null if the
     *          attribute does not exist.
     */
    public Object getAttribute(RenderContext rc, String attributeName) {
        return null;
    }

    /**
     * Binds an object to a given attribute name for this component
     * within the specified render context.
     *
     * @param rc the render context used to render this component
     * @param attributeName a String specifying the name of the attribute
     * @param value an Object representing the value of the attribute
     */
    public void setAttribute(RenderContext rc, String attributeName, Object value) {}

    /**
     * Adds the specified component as a child to this component.
     * The child will be added at the end of this component's sequence
     * of children.
     * @param child the WComponent to be added as a child
     * @throws NullPointerException if child is null
     */
    public void add(WComponent child) {}

    /**
     * Adds the specified component as a child to this component at
     * the specified index.
     * @param child the WComponent to be added as a child
     * @param index the integer indicating where in the sequence of
     *        children the child should be added
     * @throws NullPointerException if child is null
     * @throws IndexOutOfBoundsException if index < 0 or index > childCount
     */
    public void add(WComponent child, int index) {}

   /**
     * Adds the specified component as a child to this component with
     * the specified childID.  The childID is an abitrary String
     * which may be used internally by this parent component to logically
     * identify the child.  If a child already exists with this childID, 
     * it will be replaced with the new child specified in this method
     * call.
     *
     * @param child the WComponent representing the child to be added
     * @param childID a String which logically identifies the child within
     *        this parent component
     * @throws NullPointerException if child is null
     */ 
    // Aim10-25-01: Don't implement yet
    public void add(WComponent child, String childID) {}

    /** 
     * Removes the child component located at the specified index from
     * this component.
     * @param index the integer indicating the index of the child to be 
     *        removed 
     * @throws IndexOutOfBoundsException if index < 0 or index > childCount-1
     */ 
    public void remove(int index) {}

    /** 
     * Removes the child component associated with the specified logical
     * childID from this component.
     * @param childID a String which logically identifies the child within
     *        this parent component
     * @throws NullPointerException if childID is null
     */ 
    // Aim10-25-01: Don't implement yet
    public void remove(String childID) {}

    /**
     * Removes all child components from this component.
     */
    public void removeAll() {}

    /**
     * Sets the child components on this component to be the specified 
     * array of components.  If this component already contains children at
     * the time this method is invoked, they will first be removed
     * before the new children are added.
     * @param kids the array containing the components to add as children
     * @throws NullPointerException if kids is null
     */
    public void setChildren(WComponent[] kids) {}

    /**
     * Returns an iterator containing all the children of this component
     * for the specified render context.
     * @param rc the render context used to render this component
     */
    public Iterator getChildren(RenderContext rc) {
        return null;
    }

    /**
     * Returns an iterator containing Strings representing all the
     * childIDs of all children associated with childID for this component
     * for the specified render context.
     * @param rc the render context used to render this component
     */
    public Iterator getChildIDs(RenderContext rc) {
        return null;
    }

    /**
     * Returns the child at the specified index in this component
     * for the specified render context.
     * @param rc the render context used to render this component
     * @param index the integer indicating the index of the child
     * @throws IndexOutOfBoundsException if index < 0 or index > childCount-1
     */
    public WComponent getChild(RenderContext rc, int index) {
        return null;
    }

    /**
     * Returns the child associated with the specified logical childID
     * for the specified render context.  If no child exists with that
     * childID, returns null.
     * @param rc the render context used to render this component
     * @param childID a String which logically identifies the child within
     *        this parent component
     * @return the WComponent representing the child associated with the
     *          childID, or null if it does not exist
     * @throws NullPointerException if childID is null
     */
    // Aim10-25-01: don't implement yet
    public WComponent getChild(RenderContext rc, String childID) {
        return null;
    }

    /**
     * Returns the number of children in this component for the
     * specified render context.
     * @param rc the render context used to render this component
     * @return an integer indicating the number of children in this
     *          component.
     */
    public int getChildCount(RenderContext rc) {
        return 0;
    }

    /**
     * Invoked just prior to rendering this component.  Subclasses
     * should override this method if any processing is required
     * prior to rendering.
     * @param rc the render context used to render this component
     * @throws IOException // Aim10-25-01: under what conditions?
     */
    public void preRender(RenderContext rc) throws IOException {}

    /**
     * Renders the component.  By default it will obtain the
     * renderer from the appropriate RenderKit instance and invoke
     * renderStart() on the renderer.
     * @param rc the render context used to render this component
     * @throws IOException // Aim10-25-01: under what conditions?
     */
    public void render(RenderContext rc) throws IOException {}

    /**
     * Renders all children of this component.  By default it
     * will invoke renderChildren() on the renderer.
     * @param rc the render context used to render this component
     * @throws IOException // Aim10-25-01: under what conditions?
     */
    public void renderChildren(RenderContext rc) throws IOException {}

    /**
     * Invoked after this component and all of it's children are
     * rendered.  By default it will obtain the renderer from the
     * appropriate RenderKit instance and invoke renderEnd() on the
     * renderer.
     * @param rc the render context used to render this component
     * @throws IOException // Aim10-25-01: under what conditions?
     */
    public void postRender(RenderContext rc) throws IOException {}

}

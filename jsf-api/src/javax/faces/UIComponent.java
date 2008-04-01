/*
 * $Id: UIComponent.java,v 1.4 2002/02/14 03:55:52 edburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

/**
 * The base class for representing all state associated with a
 * user-interface component.  UIComponent instances are created
 * and managed in the server in order to serve the following core 
 * functions to a JavaServer Faces application:
 * <ol>
 * <li>Identify a component's functional type
 * <li>Store a unique identifier
 * <li>Store any instance-specific non-default state (attribute values, etc)
 * <li>
 * <li>If data-bound, maintain reference to 
 *     application &quot;model&quot; object
 * <li>Provide descriptive API for getting/setting any
 *     non-render-specific state
 * <li>Enable the creation and manipulation of component hierarchies
 * <li>Drive the rendering
 * <li>Provide event-handler registration
 * <li>Drive server-side event-processing
 * <li>Provide validator registration
 * <li>Drive server-side validation processing
 * </ol>
 * <p>
 */
public abstract class UIComponent {
 
    private Hashtable ht;
    private String id;
    private String rendererType;
    private String modelReference = null;

    public UIComponent() {
        ht = new Hashtable();
    }

    /** 
     * Returns a String representing the type of this concrete
     * component class.  This read-only property is used by the RenderKit
     * instance as the key for mapping a component type to its 
     * available renderers.  A concrete subclass must return a
     * String value which is descriptive of the functional purpose 
     * of the component.
     *
     * @return a String representing the component's functional type
     *         
     */

    // PENDING(edburns): Spec Compliance: instance into Scoped Namespace
    // per spec:
    // http://javaweb.sfbay.sun.com/~aim/faces/jsr-127-archive/msg00124.html
    // section "Configuring UI Components"

    public abstract String getType();

    /**
     * The id which identifies this component instance within the application.
     * If the component instance is being managed in the ObjectManager's
     * scoped namespace, this id should be equivelent to the id used to 
     * store the component in the scoped namespace.
     * @see ObjectManager#put
     * @see #setId
     * @return String containing the id of this component
     */
    public String getId() {
	return id;
    }

    /**
     * Sets the id of this component.
     * @see #getId
     * @param id String containing the id of the component
     */
    public void setId(String id) {
	this.id = id;
    }

    /**
     * The renderer type to be used to render this component.
     * When <code>render</code> is invoked on this component, the value of 
     * this property must match one of the advertized renderer types on the 
     * render kit specified in the render context.
     * @see RenderKit#getRendererTypesForComponent
     * @see #render
     * @return String containing the type of the renderer set for
     *         this component
     */
    public String getRendererType() {
	return rendererType;
    }

    /**
     * Sets the renderer type to be used to render this component.
     * @see #getRendererType
     * @param rendererType String containing the type of the renderer
     */
    public void setRendererType(String rendererType) {
        this.rendererType = rendererType;
    }
 
    /**
     * Returns an iterator containing all the attribute names
     * corresponding to attributes set specifically on this
     * component within the specified render context.
     *
     * @param rc the render context used to render this component
     * @return an Iteration of attribute names on this component
     */   
    public Iterator getAttributeNames(RenderContext rc) {
        Set set = ht.keySet();
        if (set.isEmpty()) {
            return null;
        } else {
            return set.iterator();
        }
    }

    /**
     * Returns the component attribute with the given name
     * within the specified render context or null if the
     * specified attribute is not set on this component.
     *
     * @param rc the render context used to render this component
     * @param attributeName a String specifying the name of the attribute
     * @return the Object bound to the attribute name, or null if the
     *          attribute does not exist.
     */
    public Object getAttribute(RenderContext rc, String attributeName) {
        return ht.get(attributeName);
    }

    /**
     * Binds an object to the specified attribute name for this component.
     *
     * @param attributeName a String specifying the name of the attribute
     * @param value an Object representing the value of the attribute
     */
    public void setAttribute(String attributeName, Object value) {
        if (attributeName != null && value != null) {
            ht.put(attributeName,value);
        }
        else if (null != attributeName && null == value) {
            ht.remove(attributeName);
        }
    }

    /**
     * Adds the specified component as a child to this component.
     * The child will be added at the end of this component's sequence
     * of children.
     * @param child the UIComponent to be added as a child
     * @throws NullPointerException if child is null
     */
    public void add(UIComponent child) {}

    /**
     * Adds the specified component as a child to this component at
     * the specified index.
     * @param child the UIComponent to be added as a child
     * @param index the integer indicating where in the sequence of
     *        children the child should be added
     * @throws NullPointerException if child is null
     * @throws IndexOutOfBoundsException if index < 0 or index > childCount
     */
    public void add(UIComponent child, int index) {}

   /**
     * Adds the specified component as a child to this component with
     * the specified childMarker.  The childMarker is an arbitrary Object
     * which may be used internally by this parent component to associate
     * information with this child.  
     *
     * @param child the UIComponent representing the child to be added
     * @param childMarker an Object which is associated with the child within
     *        this parent component
     * @throws NullPointerException if child is null
     */ 
    public void add(UIComponent child, Object childMarker) {}

    /** 
     * Removes the child component located at the specified index from
     * this component.
     * @param index the integer indicating the index of the child to be 
     *        removed 
     * @throws IndexOutOfBoundsException if index < 0 or index > childCount-1
     */ 
    public void remove(int index) {}

    /** 
     * Removes any child components which are associated with a childMarker
     * object which is equivelent to the specified child marker object. 
     * @param childMarker an Object which is associated with one or more children
     *        within this parent component
     * @throws NullPointerException if childMarker is null
     */ 
    public void remove(Object childMarker) {}

    /**
     * Removes all child components from this component.
     */
    public void removeAll() {}

    /**
     * Sets the child components on this component to be the specified 
     * array of components.  If this component already contains children at
     * the time this method is invoked, they will be removed by invoking
     * <code>removeAll</code> before the new children are added.
     * @param kids the array containing the components to add as children
     * @throws NullPointerException if kids is null
     */
    public void setChildren(UIComponent[] kids) {}

    /**
     * Returns an iterator containing all the children of this component
     * for the specified render context.  
     * @param rc the render context used to render this component, or null
     */
    public Iterator getChildren(RenderContext rc) {
        return null; //compile
    }

    /**
     * Returns an iterator containing Strings representing the ids
     * of all children of this component for the specified render context.
     * @param rc the render context used to render this component, or null
     */
    public Iterator getChildIds(RenderContext rc) {
        return null; //compile
    }

    /**
     * Returns an iterator containing child marker objects associated with
     * children of this component for the specified render context.
     * @param rc the render context used to render this component, or null
     */
    public Iterator getChildMarkers(RenderContext rc) {
        return null; //compile
    }

    /**
     * Returns the child at the specified index in this component
     * for the specified render context.
     * @param rc the render context used to render this component, or null
     * @param index the integer indicating the index of the child
     * @throws IndexOutOfBoundsException if index < 0 or index > childCount-1
     */
    public UIComponent getChild(RenderContext rc, int index) {
        return null; //compile
    }

    /**
     * Returns the child with the specified id, or null if no child
     * with that id exists.
     * @param rc the render context used to render this component, or null
     * @param id a String containing the id of the child being retrieved
     * @throws NullPointerException if id is null
     */
    public UIComponent getChild(RenderContext rc, String id) {
        return null; //compile
    }

    /**
     * Returns the first child which is associated with a childMarker object
     * equivelent to the specified childMarker object
     * for the specified render context.  If no child exists with that
     * childMarker, returns null.
     * @param rc the render context used to render this component, or null
     * @param childMarker Object which logically identifies the child within
     *        this parent component
     * @return the UIComponent representing the first child associated with the
     *          childMarker, or null if it does not exist
     * @throws NullPointerException if childMarker is null
     */
    // Aim10-25-01: don't implement yet
    public UIComponent getChild(RenderContext rc, Object childMarker) {
        return null; //compile
    }

    /**
     * Returns the number of children in this component for the
     * specified render context.
     * @param rc the render context used to render this component, or null
     * @return an integer indicating the number of children in this
     *          component.
     */
    public int getChildCount(RenderContext rc) {
        return 0;
    }

    /**
     * The rendersChildren attribute.  Returns <code>false</code>
     * by default.  A concrete subclass which supports the ability to
     * drive the layout and rendering of children should override this
     * method to return <code>true</code>.
     * @param rc the render context used to render this component, or null
     * @return boolean value indicating whether or not this component
     *         takes responsibility for laying out and rendering its
     *         children.
     */
    public boolean getRendersChildren(RenderContext rc) {
	return false;
    }

   /**
    * The shouldRender attribute.  
    * @return a boolean value indicating whether or not this component 
    *         should be rendered during a rendering pass
    */
    public boolean getShouldRender(RenderContext rc) {
	return true;
    }

    /**
     * Sets the shouldRender attribute.
     * @see #getShouldRender
     */
    public void setShouldRender(boolean shouldRender) {
    }

    /**
     * Returns whether or not this component is in a renderable hierarchy.
     * A hierarchy is renderable if this component and all it's ancestors
     * on the render context's render-stack have shouldRender set to 
     * <code>true</code>.
     * @param rc the render context used to render this component
     * @throws NullPointerException if rc is null
     * @return boolean value indicating whether or not this component is in
     *         a renderable hierarchy
     */
    public boolean isRenderable(RenderContext rc) {
	return true;
    }

    /**
     * Invokes full render processing on this component and its
     * children. This method should only be called on components
     * which return <code>true</code> from <code>getRendersChildren()</code>.
     * It calls the following sequence of methods, passing 
     * in the specified render context:
     * <ol>
     * <li>render(rc)
     * <li>renderChildren(rc)
     * <li>renderComplete(rc)
     * </ol>
     * This method will recursively drive the rendering process for
     * this component and all of its descendents, treating the
     * rendering of that hierarchy as an atomic process.  This 
     * method should only be called if the the portion of the user-interface
     * defined by this hierarchy is completely defined by this
     * hierarchy (i.e. no interleaved presentation markup).
     *
     * @see #getRendersChildren
     * @param rc the render context used to render this component
     * @throws NullPointerException if rc is null
     * @throws FacesException if rendersChildren is <code>false</code>
     * @throws IOException if input or output exception occurred
     */
    public void renderAll(RenderContext rc) throws IOException {}

    /**
     * Renders this component.  By default it will push this component
     * on the render context's render-stack and invoke <code>renderStart</code>
     * on the renderer corresponding to the rendererType property of
     * this component.  
     * This method renders this component only and does not perform
     * any recursive rendering on children.
     *
     * @param rc the render context used to render this component
     * @throws NullPointerException if rc is null
     * @throws FacesException if rendererType is not set to a valid renderer
     *         in the render context's render kit
     * @throws IOException if input or output exception occurred
     */
    public void render(RenderContext rc) throws IOException,
        FacesException {

        RenderKit rk = rc.getRenderKit();
        String rendererType = getRendererType();
        if (rendererType == null) {
            throw new FacesException("Renderer Type Not Set.");
        }
        Renderer r = rk.getRenderer(rendererType);
        rc.pushChild(this);
        r.renderStart(rc, this);
    }

    /**
     * Invoked from <code>renderAll</code> render the children of this component.  
     * By default it will invoke <code>renderChildren</code> on the renderer 
     * corresponding to the rendererType property of this component.  
     * If this component type does not support rendering the layout 
     * of its children, it should override this method to do nothing.
     * @see #getRendersChildren
     * @param rc the render context used to render this component
     * @throws NullPointerException if rc is null
     * @throws FacesException if rendererType is not set to a valid renderer
     *         in the render context's render kit
     * @throws IOException if input or output exception occurred
     */
    public void renderChildren(RenderContext rc) throws IOException {}

    /**
     * Invoked after this component and all of it's children are
     * rendered. By default it will invoke <code>renderComplete</code> on 
     * the renderer corresponding to the rendererType property of this component
     * and then pop this component off the render context's render-stack. 
     * 
     * @param rc the render context used to render this component
     * @throws NullPointerException if rc is null
     * @throws FacesException if rendererType is not set to a valid renderer
     *         in the render context's render kit
     * @throws IOException if input or output exception occurred
     */
    public void renderComplete(RenderContext rc) throws IOException,
        FacesException {
        RenderKit rk = rc.getRenderKit();
        String rendererType = getRendererType();
        if (rendererType == null) {
            throw new FacesException("Renderer Type Not Set.");
        }
        Renderer r = rk.getRenderer(rendererType);
        r.renderComplete(rc, this);
        rc.popChild();
    }

    /**
     * Will recursively validate all components in this hierarchy.
     */
    public void validateAll() {
    }

    /**
     * A component hierarchy is valid when it and all of its descendents are
     * either:
     * <ol>
     * <li>not Validatible 
     * <li>Validatible with <code>validState</code> == <code>Validatible.VALID</code>
     * </ol>
     * @return boolean value indicating whether this component hierarchy 
     *         is valid or not
     */
    public boolean isValid() {
        return true; //compile
    }

    /**
     * The model-reference property for this data-bound component.
     * This property contains a reference to the object which acts
     * as the data-source for this component.  The model-reference
     * must resolve to an object which implements one of the following types:
     * <ul>
     * <li><code>java.lang.String</code>
     * </ul>
     * @see #setModelReference
     * @return String containing the model-reference for this component
     */
    public String getModelReference() {
        return modelReference;
    }

    /**
     * Sets the model-reference property on this data-bound component.
     * @see #getModelReference
     * @param modelReference the String which contains a reference to the
     *        object which acts as the data-source for this component
     */
    public void setModelReference(String modelReference) {
        this.modelReference = modelReference;
    }

    /**

    * If localValue is non-null, we just return it.  Else, if we have a
    * model reference, we ask it for a value.  If it does, we cache it
    * in localValue.  Then we return localValue.

    */ 

    public Object getValue(RenderContext rc) {
	Object result = ht.get(Constants.REF_VALUE);
	if (null == result) {
	    result = pullValueFromModel(rc);
	}
	return result;
    }
    
    public void setValue(Object newValue) {
	setAttribute(Constants.REF_VALUE, newValue);
    }

    /**

    *   If we do not have a modelReference, do nothing.  Else, If
    *   localValue non-null, try to push localValue into model().  If
    *   successful, set localValue to null, else leave localValue alone.

    */

    public void pushValueToModel(RenderContext rc) {
	if (null == modelReference) {
	    return;
	}
	Object localValue = ht.get(Constants.REF_VALUE);
	
	if (null != localValue) {
            try {
                rc.getObjectAccessor().setObject(rc.getRequest(), 
						 modelReference, localValue); 
		setValue(null);
            } catch ( FacesException e ) {
		// Don't modify localValue in this case.
            }
	}
    }

    /**

    *   If we do not have a modelReference, do nothing.  If non-null
    *   value from model, overwrite local value, else do nothing. <P>

    * @return the value from the model

    */

    public Object pullValueFromModel(RenderContext rc) {
	Object result = null;

	if (null == modelReference) {
	    return result;
	}

	try {
	    result = rc.getObjectAccessor().getObject(rc.getRequest(),
						      modelReference);
	    if (null != result) {
		setValue(result);
	    }
	} catch ( FacesException e ) {
	    // PENDING (visvan) skip this exception ??
	}
	return result;
    }

} // End of class UIComponent


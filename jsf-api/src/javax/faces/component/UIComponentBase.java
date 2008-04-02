/*
 * $Id: UIComponentBase.java,v 1.31 2003/01/07 19:55:24 jvisvanathan Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.context.FacesContext;
import javax.faces.event.RequestEvent;
import javax.faces.event.RequestEventHandler;
import javax.faces.render.Renderer;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.faces.validator.Validator;


/**
 * <p><strong>UIComponentBase</strong> is a convenience base class that
 * implements the default concrete behavior of all methods defined by
 * {@link UIComponent} except <code>getComponentType()</code>.
 * Concrete subclasses of <code>UIComponentBase</code> must implement the
 * <code>getComponentType()</code> method.  Component types are used to
 * select the appropriate {@link Renderer} to be used for decoding and
 * encoding, if the <code>rendererType</code> property is non-null.</p>
 *
 * <p>By default, this class defines <code>getRendersChildren()</code> to
 * return <code>false</code>.  Subclasses that wish to manage the rendering
 * of their children should override this method to return <code>true</code>
 * instead.</p>
 */

public abstract class UIComponentBase implements UIComponent {


    // ------------------------------------------------------------- Attributes


    /**
     * <p>The set of attribute values, keyed by the corresponding attribute
     * name.</p>
     *
     * <p><strong>IMPLEMENTATION NOTE</strong> - The collection for attributes
     * must be lazily instantiated the first time that it is actually used,
     * in order to avoid wasted object creations.  This can be done by
     * calling <code>getAttributes()</code>.</p>
     *
     * <p><strong>IMPLEMENTATION NOTE</strong> - It is assumed that components
     * can only be modified in the context of a single thread.</p>
     */
    private HashMap attributes = null;


    /**
     * <p>Create (if necessary) and return the collection to be used for
     * attributes storage.</p>
     */
    private Map getAttributes() {

        if (attributes == null) {
            attributes = new HashMap();
        }
        return (attributes);

    }


    /**
     * <p>Have we allocated a collection for attributes yet?</p>
     */
    private boolean isAttributesAllocated() {

        return (attributes != null);

    }


    /**
     * <p>Return the value of the attribute with the specified name
     * (if any); otherwise, return <code>null</code>.</p>
     *
     * @param name Name of the requested attribute
     *
     * @exception NullPointerException if <code>name</code> is
     *  <code>null</code>
     */
    public Object getAttribute(String name) {

        // Validate method parameters
        if (name == null) {
            throw new NullPointerException("getAttribute");
        }

        // Special cases for read-only and special case attributes
        if ("componentType".equals(name)) {
            return (getComponentType());
        } else if ("rendersChildren".equals(name)) {
            if (getRendersChildren()) {
                return (Boolean.TRUE);
            } else {
                return (Boolean.FALSE);
            }
        } else if ("rendersSelf".equals(name)) {
            if (getRendersSelf()) {
                return (Boolean.TRUE);
            } else {
                return (Boolean.FALSE);
            }
        }

        // Return the selected attribute value
        if (!isAttributesAllocated()) {
            return (null);
        }
        return (getAttributes().get(name));

    }


    /**
     * <p>Return an <code>Iterator</code> over the names of all
     * currently defined attributes of this <code>UIComponent</code> that
     * have a non-null value.</p>
     */
    public Iterator getAttributeNames() {

        if (isAttributesAllocated()) {
            return (getAttributes().keySet().iterator());
        } else {
            return (Collections.EMPTY_LIST.iterator());
        }

    }


    /**
     * <p>Set the new value of the attribute with the specified name,
     * replacing any existing value for that name.</p>
     *
     * @param name Name of the requested attribute
     * @param value New value (or <code>null</code> to remove
     *  any attribute value for the specified name
     *
     * @exception IllegalArgumentException if <code>name</code> represents
     *  a read-only property of this component
     * @exception NullPointerException if <code>name</code>
     *  is <code>null</code>
     */
    public void setAttribute(String name, Object value) {

        // Validate method parameters
        if (name == null) {
            throw new NullPointerException("setAttribute");
        }
        if ("componentId".equals(name)) {
	    validateComponentId(name);
	}

        // Special cases for read-only pseudo-attributes
        if ("componentType".equals(name) ||
            "rendersChildren".equals(name) ||
            "rendersSelf".equals(name)) {
            throw new IllegalArgumentException(name);
        }

        // FIXME - special cases for setComponentId and setModel values

        // Set or remove the specified value
        if (value != null) {
            getAttributes().put(name, value);
        } else {
            getAttributes().remove(name);
        }

    }


    // ------------------------------------------------------------- Properties


    /**
     * <p>Return the identifier of this <code>UIComponent</code>.</p>
     */
    public String getComponentId() {

        return ((String) getAttribute("componentId"));

    }

    public String getClientId(FacesContext context) {
	String result = null;

	if (null != (result = (String) getAttribute("clientId"))) {
	    return result;
	}

        String rendererType = getRendererType();

	// if there is a Renderer for this component
        if (rendererType != null) {
	    // let the Renderer define the client id
            RenderKitFactory rkFactory = (RenderKitFactory)
                FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
            RenderKit renderKit = rkFactory.getRenderKit
                (context.getRequestTree().getRenderKitId());
            Renderer renderer = renderKit.getRenderer(rendererType);
            result = renderer.getClientId(context, this);
        } else {
	    // we have to define the client id ourselves
	    NamingContainer closestContainer = null;
	    UIComponent containerComponent = this;

	    // Search for an ancestor that is a naming container
	    while (null != (containerComponent = 
			    containerComponent.getParent())) {
		if (containerComponent instanceof NamingContainer) {
		    closestContainer = (NamingContainer) containerComponent;
		    break;
		}
	    }

	    // If none is found, see if this is a naming container
	    if (null == closestContainer && this instanceof NamingContainer) {
		closestContainer = (NamingContainer) this;
	    }
	    
	    if (null != closestContainer) {
		// If there is no componentId, generate one and store it
		if (null == (result = getComponentId())) {
		    // Don't call setComponentId() because it checks for
		    // uniqueness.  No need.
		    setAttribute("componentId",
				 result = closestContainer.generateClientId());
		}
		//
		// build the client side id
		//

		containerComponent = (UIComponent) closestContainer;
		// If this is the root naming container, break
		if (null != containerComponent.getParent()) {
		    result = containerComponent.getClientId(context) +
			SEPARATOR_CHAR + result;
		}
	    }
        }
	if (null == result) {
	    throw new NullPointerException();
	}
	setAttribute("clientId", result);
	return result;
    }



    /**
     * <p>Set the identifier of this <code>UIComponent</code>.
     *
     * @param componentId The new identifier
     *
     * @exception IllegalArgumentException is non-null and contains
     * invalid characters
     * @exception IllegalArgumentException if this
     * <code>UIComponent</code> instance is already in the tree, but
     * can't be added to the namespace of the closest ancestor that is a
     * naming container.
     */
    public void setComponentId(String componentId) {

	validateComponentId(componentId);

	String currentId = null;
	// Handle the case where we're re-naming a component
	if (null != (currentId = getComponentId())) {
	    maybeRemoveFromNearestNamingContainer(this);
	    setAttribute("clientId", null);
	}

        setAttribute("componentId", componentId);

	try {
	    // If we are already in the tree, make sure to add ourselves to
	    // the namespace.
	    maybeAddToNearestNamingContainer(this);
	}
	catch (IllegalArgumentException e) {
	    // If we can't be added to the namespace, roll-back our
	    // component-id
	    setAttribute("componentId", null);
	    throw e;
	}

    }


    /**
     * <p>Return the component type of this <code>UIComponent</code>.</p>
     */
    public abstract String getComponentType();

    /**
     * <p>Return the model reference expression of this
     * <code>UIComponent</code>, if any.</p>
     */
    public String getModelReference() {

        return ((String) getAttribute("modelReference"));

    }


    /**
     * <p>Set the model reference expression of this
     * <code>UIComponent</code>.</p>
     *
     * @param modelReference The new model reference expression, or
     *  <code>null</code> to disconnect this component from any model data
     */
    public void setModelReference(String modelReference) {

        setAttribute("modelReference", modelReference);

    }

    /**
     * <p>Return <code>true</code> if the value of the 'rendered' attribute 
     * is a Boolean representing <code>true</code> or <code>null</code>, 
     * otherwise return <code>false</code>.</p>
     */
    public boolean isRendered() {

        Boolean rendered = (Boolean) getAttribute("rendered");
        if (rendered != null) {
            return (rendered.booleanValue());
        } else {
            return (true);
        }
    }
    
    /**
     * <p>Set the rendered attribute of this <code>UIComponent</code>.</p>
     * 
     * @param rendered If <code>true</code> render this component.
     * Otherwise, do not render this component.
     */
    public void setRendered(boolean rendered) {
        if ( rendered ) {
            setAttribute("rendered", Boolean.TRUE);
        } else {
            setAttribute("rendered", Boolean.FALSE);  
        }
    }


    /**
     * <p>Return the parent <code>UIComponent</code> of this
     * <code>UIComponent</code>, if any.</p>
     */
    public UIComponent getParent() {

        return ((UIComponent) getAttribute("parent"));

    }


    /**
     * <p>Set the parent <code>UIComponent</code> of this
     * <code>UIComponent</code>.</p>
     *
     * @param parent The new parent, or <code>null</code> for the root node
     *  of a component tree
     */
    void setParent(UIComponent parent) {

        setAttribute("parent", parent);

    }


    /**
     * <p>Return the {@link Renderer} type for this <code>UIComponent</code>
     * (if any).</p>
     */
    public String getRendererType() {

        return ((String) getAttribute("rendererType"));

    }


    /**
     * <p>Set the {@link Renderer} type for this <code>UIComponent</code>,
     * or <code>null</code> for components that render themselves.</p>
     *
     * @param rendererType Logical identifier of the type of
     *  {@link Renderer} to use, or <code>null</code> for components
     *  that render themselves
     */
    public void setRendererType(String rendererType) {

        setAttribute("rendererType", rendererType);

    }


    /**
     * <p>Return a flag indicating whether this component is responsible
     * for rendering its child components.  The default implementation returns
     * <code>false</code>; components that want to return <code>true</code>
     * must override this method to do so. </p>
     */
    public boolean getRendersChildren() {

        return (false);

    }


    /**
     * <p>Return a flag indicating whether this component has concrete
     * implementations of the <code>decode()</code> and
     * <code>encodeXxx()</code> methods, and is therefore suitable for
     * use in the <em>direct implementation</em> programming model
     * for rendering.  The default implementation returns <code>false</code>;
     * components that want to return <code>true</code> must override
     * this method to do so.</p>
     */
    public boolean getRendersSelf() {

        return (false);

    }


    /**
     * <p>Return the current validity state of this component.  The validity
     * state of a component is adjusted at the following points during the
     * request processing lifecycle:</p>
     * <ul>
     * <li>During the <em>Apply Request Values</em> phase, set to
     *     <code>true</code> or <code>false</code> by <code>decode()</code>,
     *     based on the success or failure of decoding a new local value for
     *     this component.</li>
     * <li>During the <em>Process Validations</em> phase, set to
     *     <code>false</code> by <code>processValidations()</code> if any
     *     call to a <code>validate()</code> method returned
     *     <code>false</code>.</li>
     * <li>During the <em>Update Model Values</em> phase, set to
     *     <code>false</code> by <code>updateModel()</code> if any conversion
     *     or update error occurred.</li>
     * </ul>
     */
    public boolean isValid() {

        Boolean valid = (Boolean) getAttribute("valid");
        if (valid != null) {
            return (valid.booleanValue());
        } else {
            return (false);

        }

    }


    /**
     * <p>Define the value to be returned by the <code>isValid()</code>
     * method.  This method should only be called from the
     * <code>decode()</code> method of a {@link Renderer} instance to which
     * decoding has been delegated for this component.</p>
     *
     * @param valid The new <code>valid</code> value
     */
    protected void setValid(boolean valid) {

        if (valid) {
            setAttribute("valid", Boolean.TRUE);
        } else {
            setAttribute("valid", Boolean.FALSE);
        }

    }


    /**
     * <p>Return the local value of this <code>UIComponent</code>, if any.
     */
    public Object getValue() {

        return (getAttribute("value"));

    }


    /**
     * <p>Set the local value of this <code>UIComponent</code>.</p>
     *
     * @param value The new local value
     */
    public void setValue(Object value) {

        setAttribute("value", value);

    }


    /**
     * <p>Evaluate and return the current value of this component, according
     * to the following algorithm.</p>
     * <ul>
     * <li>If the <code>value</code> property has been set (corresponding
     *     to the local value for this component), return that; else</li>
     * <li>If the <code>model</code> property has been set, retrieve and
     *     return the corresponding model value, if possible; else</li>
     * <li>Return <code>null</code>.</li>
     * </ul>
     *
     * @param context FacesContext within which to evaluate the model
     *  reference expression, if necessary
     *
     * @exception NullPointerException if <code>context</code> is null
     */
    public Object currentValue(FacesContext context) {

        if (context == null) {
            throw new NullPointerException();
        }
        Object value = getValue();
        if (value != null) {
            return (value);
        }
        String modelReference = getModelReference();
        if (modelReference != null) {
            if (context != null) {
                return (context.getModelValue(modelReference));
            }
        }
        return (null);

    }


    // ------------------------------------------------ Tree Management Methods


    /**
     * <p>The set of child <code>UIComponent</code>s associated with this
     * component, in the order specified by how the components were added.</p>
     *
     * <p><strong>IMPLEMENTATION NOTE</strong> - The collection for children
     * must be lazily instantiated the first time that it is actually used,
     * in order to avoid wasted object creations.</p>
     *
     * <p><strong>IMPLEMENTATION NOTE</strong> - It is assumed that components
     * can only be modified in the context of a single thread.</p>
     */
    private ArrayList children = null;


    /**

    * <p>Verify that the argument UIComponent is safe to add to the tree.
    * This is true if and only if:</p>

    * 	<ul>

	  <li><p>toAdd is non-null</p>
	  </li>

	  <li><p>toAdd has a null component id, or, if non-null, the
	  component contains nothing but valid characters.</p></li>

	</ul>

     * @throws IllegalArgumentException if the argument UIComponent is
     * null

     * @throws IllegalArgumentException if the componentId of this
     * component is non-null and contains invalid characters.


    */
   
    private void validateComponentId(String componentId) {
	if (null == componentId) {
	    return;
	}

	// PENDING(edburns): here is where we check that the componentId
	// contains nothing but valid chars.

    }
            
    /**


    */

    private static NamingContainer findClosestNamingContainer(UIComponent start){
	UIComponent cur = start;
	NamingContainer closestContainer = null;

	// see if start is a naming container
	if (null == closestContainer && start instanceof NamingContainer) {
	    // If so, return it.
	    return (NamingContainer) start;
	}
	
	// Search for an ancestor that is a naming container
	while (null != (cur = cur.getParent())) {
	    if (cur instanceof NamingContainer) {
		closestContainer = (NamingContainer) cur;
		break;
	    }
	}

	return closestContainer;
    }

    /**

     * <p>If the component identifier of this UIComponent instance is
     * non-null, and contains all valid characters, find the closest
     * ancestor that is a naming container and try to add this component
     * identifier to the namespace. <p>

     * @return the closest ancestor to this UIComponent that is a naming
     * container, if this UIComponent was successfully added to the
     * namespace of that naming container, or null if no naming
     * container is necessary.

     * @param component the UIComponent instance to add

     * @exception IllegalArgumentException if the component identifier
     * of the new component is non-null, and is not unique in the
     * namespace of the closest ancestor that is a naming container.

     * @exception IllegalArgumentException if the component identifier
     * of the new component is non-null, and contains non-valid chars.

     */
    private NamingContainer maybeAddToNearestNamingContainer(UIComponent component) {
	String componentId = component.getComponentId();

	if (null == componentId) {
	    return null;
	}

	// PENDING(edburns): when we have a more robust naming container
	// proposal, we'll do something more intelligent here than just
	// "find the root and hope it is a naming container".

	NamingContainer closestContainer = null;

	if (null != (closestContainer = findClosestNamingContainer(this))) {
	    closestContainer.addComponentToNamespace(component);
	}
	return closestContainer;
    }

    /**

    * <p>If the componentId for this component is non-null, and is in the
    * namespace of the nearest naming container, it is removed from that
    * namespace.</p>

    * @return the naming container from which this UIComponent instance
    * was removed, or null if no naming container is found.

    * @param component the UIComponent instance to remove from the
    * nearest naming container.

    */

    private NamingContainer maybeRemoveFromNearestNamingContainer(UIComponent component) {
	String componentId = component.getComponentId();

	if (null == componentId) {
	    return null;
	}

	// PENDING(edburns): when we have a more robust naming container
	// proposal, we'll do something more intelligent here than just
	// "find the root and hope it is a naming container".

	NamingContainer closestContainer = null;

	if (null != (closestContainer = findClosestNamingContainer(this))) {
	    closestContainer.removeComponentFromNamespace(component);
	}
	return closestContainer;
    }

    /**
     * <p>Create (if necessary) and return an iterator over the child
     * components of this component.</p>
     */
    private List getChildList() {

        if (children == null) {
            children = new ArrayList();
        }
        return (children);

    }


    /**
     * <p>Have we allocated a collection for children yet?</p>
     */
    private boolean isChildrenAllocated() {

        return (children != null);

    }


    public void addChild(UIComponent component) {
	if (this == component) {
	    throw new IllegalArgumentException();
	}
	validateComponentId(component.getComponentId());
        maybeAddToNearestNamingContainer(component);
        getChildList().add(component);
        if (component instanceof UIComponentBase) { // FIXME - Hmmmm
            ((UIComponentBase) component).setParent(this);
        }

    }

    public void addChild(int index, UIComponent component) {
	if (this == component) {
	    throw new IllegalArgumentException();
	}
	validateComponentId(component.getComponentId());

	// Need to check bounds first because we don't want to add it to
	// the namespace if the index is out of bounds.
	if ((index < 0) || (getChildList().size() < index)) {
	    throw new IndexOutOfBoundsException();
	}
        maybeAddToNearestNamingContainer(component);
        getChildList().add(index, component);
        if (component instanceof UIComponentBase) { // FIXME - Hmmmm
            ((UIComponentBase) component).setParent(this);
        }

    }


    /**
     * <p>Remove all child <code>UIComponent</code>s from the child list.
     */
    public void clearChildren() {

        if (!isChildrenAllocated()) {
            return;
        }
        children.clear();

    }


    /**
     * <p>Return <code>true</code> if the specified <code>UIComponent</code>
     * is a direct child of this <code>UIComponent</code>; otherwise,
     * return <code>false</code>.</p>
     *
     * @param component Component to be checked
     *
     * @exception NullPointerException if <code>component</code> is null
     */
    public boolean containsChild(UIComponent component) {

        return (component.getParent() == this);

    }

    public UIComponent findComponent(String expr) {

        if (expr == null) {
            throw new NullPointerException("findChildren");
        }
	UIComponent node = this;
	NamingContainer namingContainer = null;

	if (null != (namingContainer = findClosestNamingContainer(this))) {
	    node = namingContainer.findComponentInNamespace(expr);
	}

        // Return the selected node
        return (node);

    }



    /**
     * <p>Return the <code>UIComponent</code> at the specified position
     * in the child list for this component.</p>
     *
     * @param index Position of the desired component
     *
     * @exception IndexOutOfBoundsException if index is out of range
     *  ((index &lt; 0) || (index &gt;= size()))
     */
    public UIComponent getChild(int index) {

        return ((UIComponent) getChildList().get(index));

    }


    /**
     * <p>Return the number of <code>UIComponent</code>s on the child list
     * for this component.</p>
     */
    public int getChildCount() {

        if (isChildrenAllocated()) {
            return (getChildList().size());
        } else {
            return (0);
        }

    }


    /**
     * <p>Return an <code>Iterator</code> over the child
     * <code>UIComponent</code>s of this <code>UIComonent</code>,
     * in the order of their position in the child list.  If this
     * component has no children, an empty <code>Iterator</code>
     * is returned.</p>
     */
    public Iterator getChildren() {

        if (isChildrenAllocated()) {
            return (getChildList().iterator());
        } else {
            return (Collections.EMPTY_LIST.iterator());
        }

    }


    /**
     * <p>Remove the child <code>UIComponent</code> at the specified position
     * in the child list for this component.</p>
     *
     * @param index Position of the component to be removed
     *
     * @exception IndexOutOfBoundsException if the index is out of range
     *  ((index < 0) || (index &gt;= size()))
     */
    public void removeChild(int index) {

        UIComponent kid = getChild(index);
        getChildList().remove(index);
        if (kid instanceof UIComponentBase) { // FIXME -- Hmmmmm
            ((UIComponentBase) kid).setParent(null);
        }

    }


    /**
     * <p>Remove the child <code>UIComponent</code> from the child list
     * for this component.</p>
     *
     * @param component Child component to be removed
     *
     * @exception IllegalArgumentException if <code>component</code> is
     *  not a child of this component
     * @exception NullPointerException if <code>component</code> is null
     */
    public void removeChild(UIComponent component) {

        if (component == null) {
            throw new NullPointerException("removeChild");
        }
        if (containsChild(component)) {
            getChildList().remove(component);
            if (component instanceof UIComponentBase) { // FIXME -- Hmmmmm
                ((UIComponentBase) component).setParent(null);
            }
        } else {
            throw new IllegalArgumentException("removeChild");
        }

    }


    // ----------------------------------------------- Facet Management Methods


    /**
     * <p>The set of facet <code>UIComponent</code>s associated with this
     * component, keyed by facet name.</p>
     *
     * <p><strong>IMPLEMENTATION NOTE</strong> - The collection for facets
     * must be lazily instantiated the first time that it is actually used,
     * in order to avoid wasted object creations.</p>
     *
     * <p><strong>IMPLEMENTATION NOTE</strong> - It is assumed that components
     * can only be modified in the context of a single thread.</p>
     */
    private HashMap facets = null;


    /**
     * <p>Create (if necessary) and return a Map of the facets associated
     * with this component.</p>
     */
    private Map getFacets() {

        if (facets == null) {
            facets = new HashMap();
        }
        return (facets);

    }


    /**
     * <p>Add the specified <code>UIComponent</code> as a facet associated
     * with the name specified by its <code>componentId</code>, replacing
     * any previous facet with that name.</p>
     *
     * @param facet The new facet {@link UIComponent}
     *
     * @exception IllegalArgumentException if the specified <code>facet</code>
     *  has a <code>componentId</code> that is <code>null</code>
     * @exception NullPointerException if <code>facet</code>
     *  is <code>null</code>
     */
    public void addFacet(UIComponent facet) {

        String name = facet.getComponentId(); // Will throw NPE if null
        if (name == null) {
            throw new NullPointerException();
        }
        getFacets().put(name, facet);

    }


    /**
     * <p>Remove all facet <code>UIComponent</code>s from this component.
     * </p>
     */
    public void clearFacets() {

        facets = null;

    }


    /**
     * <p>Return the facet <code>UIComponent</code> associated with the
     * specified name, if any.  Otherwise, return <code>null</code>.</p>
     *
     * @param name Name of the facet to be retrieved
     *
     * @exception NullPointerException if <code>name</code>
     *  is <code>null</code>
     */
    public UIComponent getFacet(String name) {

        if (name == null) {
            throw new NullPointerException();
        }
        return ((UIComponent) getFacets().get(name));

    }


    /**
     * <p>Return an <code>Iterator</code> over the names of the facet
     * <code>UIComponent</code>s of this <code>UIComponent</code>.  If
     * this component has no facets, an empty <code>Iterator</code> is
     * returned.</p>
     */
    public Iterator getFacetNames() {

        if (facets != null) {
            return (facets.keySet().iterator());
        } else {
            return (Collections.EMPTY_LIST.iterator());
        }

    }


    /**
     * <p>Remove the facet <code>UIComponent</code> associated with the
     * specified name, if there is one.</p>
     *
     * @param name Name of the facet to be removed
     *
     * @exception NullPointerException if <code>name</code>
     *  is <code>null</code>
     */
    public void removeFacet(String name) {

        if (name == null) {
            throw new NullPointerException();
        }
        if (facets != null) {
            facets.remove(name);
        }

    }
    
    /**
     * <p>Return an <code>Iterator</code> over the facet followed by child
     * <code>UIComponent</code>s of this <code>UIComponent</code>.
     * Facets are returned in an undefined order, followed by
     * all the children in the order they are stored in the child list. If this
     * component has no facets or children, an empty <code>Iterator</code>
     * is returned.</p>
     */
    public Iterator getChildrenAndFacets() {

        ArrayList childrenAndFacets = new ArrayList();
        if ( facets != null ) {
            Iterator facetsItr = (getFacets().keySet()).iterator();
            while ( facetsItr.hasNext()) {
                UIComponent kid = (UIComponent)facets.get(facetsItr.next());
                childrenAndFacets.add(kid);
            }
        }
        if (getChildCount() > 0 ) {
           Iterator kidsItr = getChildList().iterator();
            while ( kidsItr.hasNext()) {
                childrenAndFacets.add(kidsItr.next());
            }
        }
        if ( childrenAndFacets.size() == 0 ) {
            return (Collections.EMPTY_LIST.iterator());
        } else {
            return childrenAndFacets.iterator();
        }
    }


    // ------------------------------------------ Request Event Handler Methods


    /**
     * <p>The set of {@link RequestEventHandler}s associated with this
     * <code>UIComponent</code>.</p>
     */
    private ArrayList handlers = null;


    /**
     * <p>Add a {@link RequestEventHandler} instance to the set associated with
     * this <code>UIComponent</code>.</p>
     *
     * @param handler The {@link RequestEventHandler} to add
     *
     * @exception NullPointerException if <code>handler</code>
     *  is null
     */
    public void addRequestEventHandler(RequestEventHandler handler) {

        if (handler == null) {
            throw new NullPointerException();
        }
        if (handlers == null) {
            handlers = new ArrayList();
        }
        handlers.add(handler);

    }


    /**
     * <p>Clear any {@link RequestEventHandler}s that have been registered for
     * processing by this component.</p>
     */
    public void clearRequestEventHandlers() {

        handlers = null;

    }


    /**
     * <p>Return an <code>Iterator</code> over the {@link RequestEventHandler}s
     * associated with this <code>UIComponent</code>.</p>
     */
    public Iterator getRequestEventHandlers() {

        if (handlers != null) {
            return (handlers.iterator());
        } else {
            return (Collections.EMPTY_LIST.iterator());
        }

    }


    /**
     * <p>Remove a {@link RequestEventHandler} instance from the set associated with
     * this <code>UIComponent</code>, if it was previously associated.
     * Otherwise, do nothing.</p>
     *
     * @param handler The {@link RequestEventHandler} to remove
     */
    public void removeRequestEventHandler(RequestEventHandler handler) {

        if (handlers != null) {
            handlers.remove(handler);
        }

    }


    // ----------------------------------------------------- Validators Methods


    /**
     * <p>The set of {@link Validator}s associated with this
     * <code>UIComponent</code>.</p>
     */
    private ArrayList validators = null;


    /**
     * <p>Add a {@link Validator} instance to the set associated with
     * this <code>UIComponent</code>.</p>
     *
     * @param validator The {@link Validator} to add
     *
     * @exception NullPointerException if <code>validator</code>
     *  is null
     */
    public void addValidator(Validator validator) {

        if (validator == null) {
            throw new NullPointerException();
        }
        if (validators == null) {
            validators = new ArrayList();
        }
        validators.add(validator);

    }


    /**
     * <p>Clear any {@link Validator}s that have been registered for
     * processing by this component.</p>
     */
    public void clearValidators() {

        validators = null;

    }


    /**
     * <p>Return an <code>Iterator</code> over the {@link Validator}s
     * associated with this <code>UIComponent</code>.</p>
     */
    public Iterator getValidators() {

        if (validators != null) {
            return (validators.iterator());
        } else {
            return (Collections.EMPTY_LIST.iterator());
        }

    }


    /**
     * <p>Remove a {@link Validator} instance from the set associated with
     * this <code>UIComponent</code>, if it was previously associated.
     * Otherwise, do nothing.</p>
     *
     * @param validator The {@link Validator} to remove
     */
    public void removeValidator(Validator validator) {

        if (validators != null) {
            validators.remove(validator);
        }

    }


    // ------------------------------------------- Lifecycle Processing Methods


    /**
     * <p>Decode the current state of this <code>UIComponent</code> from the
     * request contained in the specified {@link FacesContext}, and attempt
     * to convert this state information into an object of the required type
     * for this component.  If conversion is successful, save the resulting
     * object via a call to <code>setValue()</code>, and set the
     * <code>valid</code> property of this component to <code>true</code>.
     * If conversion is not successful:</p>
     * <ul>
     * <li>Save the state information in such a way that encoding
     *     can reproduce the previous input (even though it was syntactically
     *     or semantically incorrect)</li>
     * <li>Add an appropriate conversion failure error message by calling
     *     <code>context.addMessage()</code>.</li>
     * <li>Set the <code>valid</code> property of this comonent
     *     to <code>false</code>.</li>
     * </ul>
     *
     * <p>During decoding, events may be enqueued for later processing
     * (by this component or some other component),  by calling
     * <code>addRequestEvent()</code> on the associated {@link FacesContext}.
     * </p>
     *
     * <p>The default behavior of this method is to delegate to the
     * associated {@link Renderer} if there is one; otherwise this method
     * simply returns <code>true</code>.</p>
     *
     * @param context FacesContext for the request we are processing
     *
     * @return <code>true</code> if conversion was successful, or
     *  <code>false</code> if conversion failed
     *
     * @exception IOException if an input/output error occurs during decoding
     * @exception NullPointerException if <code>context</code>
     *  is <code>null</code>
     */
    public boolean decode(FacesContext context) throws IOException {

        if (context == null) {
            throw new NullPointerException();
        }
        String rendererType = getRendererType();
        if (rendererType != null) {
            RenderKitFactory rkFactory = (RenderKitFactory)
                FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
            RenderKit renderKit = rkFactory.getRenderKit
                (context.getRequestTree().getRenderKitId());
            Renderer renderer = renderKit.getRenderer(rendererType);
            boolean result = renderer.decode(context, this);
            setValid(result);
            return(result);
        } else {
            setValid(true);
            return (true);
        }

    }


    /**
     * <p>Render the beginning of the current state of this
     * <code>UIComponent</code> to the response contained in the specified
     * {@link FacesContext}.  If the conversion attempted in a previous call
     * to <code>decode()</code> for this component failed, the state
     * information saved during execution of <code>decode()</code> should be
     * utilized to reproduce the incorrect input.  If the conversion was
     * successful, or if there was no previous call to <code>decode()</code>,
     * the value to be displayed should be acquired by calling
     * <code>currentValue()</code>, and rendering the value as appropriate.
     * </p>
     *
     * <p>The default behavior of this method is to delegate to the
     * associated {@link Renderer} if there is one; otherwise this method
     * does nothing.</p>
     *
     * @param context FacesContext for the response we are creating
     *
     * @exception IOException if an input/output error occurs while rendering
     * @exception NullPointerException if <code>context</code>
     *  is <code>null</code>
     */
    public void encodeBegin(FacesContext context) throws IOException {

        if (context == null) {
            throw new NullPointerException();
        }
        String rendererType = getRendererType();
        if (rendererType != null) {
            RenderKitFactory rkFactory = (RenderKitFactory)
                FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
            RenderKit renderKit = rkFactory.getRenderKit
                (context.getResponseTree().getRenderKitId());
            Renderer renderer = renderKit.getRenderer(rendererType);
            renderer.encodeBegin(context, this);
        }

    }


    /**
     * <p>Render the child components of this component, following the
     * rules described for <code>encodeBegin()</code> to acquire the
     * appropriate value to be rendered.  This method will only be called
     * if the <code>rendersChildren</code> property is <code>true</code>.</p>
     *
     * <p>The default behavior of this method is to delegate to the
     * associated {@link Renderer} if there is one; otherwise this method
     * does nothing.</p>
     *
     * @param context FacesContext for the response we are creating
     *
     * @exception IOException if an input/output error occurs while rendering
     * @exception NullPointerException if <code>context</code>
     *  is <code>null</code>
     */
    public void encodeChildren(FacesContext context) throws IOException {

        if (context == null) {
            throw new NullPointerException();
        }
        String rendererType = getRendererType();
        if (rendererType != null) {
            RenderKitFactory rkFactory = (RenderKitFactory)
                FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
            RenderKit renderKit = rkFactory.getRenderKit
                (context.getResponseTree().getRenderKitId());
            Renderer renderer = renderKit.getRenderer(rendererType);
            renderer.encodeChildren(context, this);
        }

    }


    /**
     * <p>Render the ending of the current state of this
     * <code>UIComponent</code>, following the rules described for
     * <code>encodeBegin()</code> to acquire the appropriate value
     * to be rendered.</p>
     *
     * <p>The default behavior of this method is to delegate to the
     * associated {@link Renderer} if there is one; otherwise this method
     * does nothing.</p>
     *
     * @param context FacesContext for the response we are creating
     *
     * @exception IOException if an input/output error occurs while rendering
     * @exception NullPointerException if <code>context</code>
     *  is <code>null</code>
     */
    public void encodeEnd(FacesContext context) throws IOException {

        if (context == null) {
            throw new NullPointerException();
        }
        String rendererType = getRendererType();
        if (rendererType != null) {
            RenderKitFactory rkFactory = (RenderKitFactory)
                FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
            RenderKit renderKit = rkFactory.getRenderKit
                (context.getResponseTree().getRenderKitId());
            Renderer renderer = renderKit.getRenderer(rendererType);
            renderer.encodeEnd(context, this);
        }

    }


    /**
     * <p>Process an individual event queued to this <code>UIComponent</code>.
     * The default implementation does nothing, but can be overridden by
     * subclasses of <code>UIComponent</code>.  Return <code>false</code> if
     * lifecycle processing should proceed directly to the <em>Render
     * Response</em> phase once all events have been processed for all
     * components, or <code>true</code> for the normal lifecycle flow.</p>
     *
     * @param context FacesContext for the request we are processing
     * @param event Event to be processed against this component
     *
     * @exception NullPointerException if <code>context</code> or
     *  <code>event</code> is <code>null</code>
     */
    public boolean processEvent(FacesContext context, RequestEvent event) {

        if ((context == null) || (event == null)) {
            throw new NullPointerException();
        }
        return (true); // Default implementation does nothing

    }


    /**
     * <p>Perform the following algorithm to update the model data
     * associated with this component, if any, as appropriate.</p>
     * <ul>
     * <li>If the <code>valid</code> property of this component is
     *     <code>false</code>, return <code>false</code>.</li>
     * <li>If the <code>modelReference</code> property of this component
     *     is <code>null</code>, return <code>true</code>.</li>
     * <li>Call the <code>setModelValue()</code> method on the specified
     *     {@link FacesContext} instance, passing this component's
     *     <code>modelReference</code> property and its local value.</li>
     * <li>If the <code>setModelValue()</code> method returns successfully:
     *     <ul>
     *     <li>Clear the local value of this component.</li>
     *     <li>Set the <code>valid</code> property of this component to
     *         <code>true</code>.</li>
     *     <li>Return <code>true</code> to the caller.</li>
     *     </ul></li>
     * <li>If the <code>setModelValue()</code> method call fails:
     *     <ul>
     *     <li>Enqueue error messages by calling <code>addMessage()</code>
     *         on the specified {@link FacesContext} instance.</li>
     *     <li>Set the <code>valid</code> property of this component to
     *         <code>false</code>.</li>
     *     <li>Return <code>false</code> to the caller.</li>
     *     </ul></li>
     * </ul>
     *
     * @param context FacesContext for the request we are processing
     *
     * @exception IllegalArgumentException if the <code>modelReference</code>
     *  property has invalid syntax for an expression
     * @exception NullPointerException if <code>context</code>
     *  is <code>null</code>
     */
    public boolean updateModel(FacesContext context) {

        if (context == null) {
            throw new NullPointerException();
        }
        if (!isValid()) {
            return (false);
        }
        String modelReference = getModelReference();
        if (modelReference == null) {
            return (true);
        }
        try {
            context.setModelValue(modelReference, getValue());
            setValid(true);
            setValue(null);
            return (true);
        } catch (FacesException e) {
            setValid(false);
            throw e;
        } catch (IllegalArgumentException e) {
            setValid(false);
            throw e;
        } catch (Exception e) {
            setValid(false);
            throw new FacesException(e);
        }

    }


    /**
     * <p>Perform any correctness checks that this component wishes to perform
     * on itself.  This method will be called, along with calls to all
     * {@link Validator}s registered on this component, during the
     * <em>Process Validations</em> phase of the request processing lifecycle.
     * If errors are encountered, appropriate <code>Message</code> instances
     * should be added to the {@link FacesContext} for the current request.
     *
     * @param context FacesContext for the request we are processing
     *
     * @return <code>false</code> if the <code>valid</code> 
     * @return <code>true</code> if all validations performed by this
     *  method passed successfully, or <code>false</code> if one or more
     *  validations performed by this method failed
     *
     * @exception NullPointerException if <code>context</code>
     *  is <code>null</code>
     */
    public boolean validate(FacesContext context) {

        if (context == null) {
            throw new NullPointerException();
        }
        return (true); // Default implementation simply returns true

    }


    // ----------------------------------------------- Lifecycle Phase Handlers


    /**
     * <p>Perform the request component tree processing required by the
     * <em>Apply Request Values</em> phase of the request processing
     * lifecycle for all facets of this component, all children of this
     * component, and this component itself, as follows.</p>
     * <ul>
     * <li>Call the <code>processDecodes()</code> method of all facets
     *     and children of this component, in the order determined by
     *     a call to <code>getChildrenAndFacets()</code>.</li>
     * <li>Call the <code>decode()</code> method of this component.</li>
     * </ul>
     *
     * <p>Return <code>false</code> if any <code>processDecodes()</code> or
     * <code>decode()</code> method call returned <code>false</code>.
     * Otherwise, return <code>true</code>.</p>
     *
     * @param context {@link FacesContext} for the request we are processing
     *
     * @exception IOException if an input/output error occurs during decoding
     * @exception NullPointerException if <code>context</code>
     *  is <code>null</code>
     */
    public boolean processDecodes(FacesContext context) throws IOException {

        if (context == null) {
            throw new NullPointerException();
        }
        boolean result = true;

        // process all facets and children of this component
        Iterator kids = getChildrenAndFacets();
        while (kids.hasNext()) {
            UIComponent kid = (UIComponent) kids.next();
            if (!kid.processDecodes(context)) {
                result = false;
            }
        }

        // Process this component itself
        if (!decode(context)) {
            result = false;
        }

        // Return the final result
        return (result);

    }


    /**
     * <p>Perform the request component tree processing required by the
     * <em>Handle Request Events</em> phase of the request processing
     * lifecycle for all facets of this component, all children of this
     * component, and this component itself, as follows.</p>
     * <ul>
     * <li>Call the <code>processEvents()</code> method of all facets
     *     and children of this component, in the order determined
     *     by a call to <code>getChildrenAndFacets()</code>.</li>
     * <li>For each event queued to this component:
     *     <ul>
     *     <li>Call the <code>processEvent()</code> method of each registered
     *         {@link RequestEventHandler} for this component.</li>
     *     <li>Call the <code>processEvent()</code> method of this
     *         component.</li>
     *     </ul></li>
     * </ul>
     *
     * <p>Return <code>false</code> if any <code>processEvents()</code> or
     * <code>processEvent()</code> method call returned <code>false</code>.
     * Otherwise, return <code>true</code>.</p>
     *
     * @param context {@link FacesContext} for the request we are processing
     *
     * @exception NullPointerException if <code>context</code>
     *  is <code>null</code>
     */
    public boolean processEvents(FacesContext context) {

        if (context == null) {
            throw new NullPointerException();
        }
        boolean result = true;

        // Process all facets and children of this component
        Iterator kids = getChildrenAndFacets();
        while (kids.hasNext()) {
            UIComponent kid = (UIComponent) kids.next();
            if (!kid.processEvents(context)) {
                result = false;
            }
        }

        // Process this component itself
        Iterator events = context.getRequestEvents(this);
        while (events.hasNext()) {
            RequestEvent event = (RequestEvent) events.next();
            if (!processEvent(context, event)) {
                result = false;
            }
            if (handlers != null) {
                Iterator handlers = getRequestEventHandlers();
                while (handlers.hasNext()) {
                    RequestEventHandler handler =
                        (RequestEventHandler) handlers.next();
                    if (!handler.processEvent(context, this, event)) {
                        result = false;
                    }
                }
            }
        }

        // Return the final result
        return (result);

    }


    /**
     * <p>Perform the request component tree processing required by the
     * <em>Process Validations</em> phase of the request processing
     * lifecycle for all facets of this component, all children of this
     * component, and this component itself, as follows.</p>
     * <ul>
     * <li>Call the <code>processValidators()</code> method of all facets
     *     and children of this component, in the order determined
     *     by a call to <code>getChildrenAndFacets()</code>.</li>
     * <li>If the <code>valid</code> property of this component is
     *     currently <code>true</code>:
     *     <ul>
     *     <li>Call the <code>validate()</code> method of each
     *         {@link Validator} instance associated with this component.</li>
     *     <li>Call the <code>validate()</code> method of this component.</li>
     *     <li>If any of the calls to a <code>validate()</code> method
     *         returned <code>false</code>, set the <code>valid</code>
     *         property of this component to <code>false</code>.</li>
     *     </ul></li>
     * </ul>
     *
     * <p>Return <code>false</code> if any <code>processValidators()</code>
     * method call returned <code>false</code>, or if the <code>valid</code>
     * property of this component is <code>false</code>.  Otherwise,
     * return <code>true</code>.</p>
     *
     * @param context {@link FacesContext} for the request we are processing
     *
     * @exception NullPointerException if <code>context</code>
     *  is <code>null</code>
     */
    public boolean processValidators(FacesContext context) {

        if (context == null) {
            throw new NullPointerException();
        }
        boolean result = true;

        // Process all the facets and children of this component
        Iterator kids = getChildrenAndFacets();
        while (kids.hasNext()) {
            UIComponent kid = (UIComponent) kids.next();
            if (!kid.processValidators(context)) {
                result = false;
            }
        }

        // Process this component itself
        if (isValid()) {
            if (!validate(context)) {
                setValid(false);
                result = false;
            }
            Iterator validators = getValidators();
            while (validators.hasNext()) {
                Validator validator = (Validator) validators.next();
                if (!validator.validate(context, this)) {
                    setValid(false);
                    result = false;
                }
            }
        } else {
            result = false;
        }

        // Return the final result
        return (result);

    }


    /**
     * <p>Perform the request component tree processing required by the
     * <em>Update Model Values</em> phase of the request processing
     * lifecycle for all facets of this component, all children of this
     * component, and this component itself, as follows.</p>
     * <ul>
     * <li>Call the <code>processUpdates()</code> method of all facets
     *     and children of this component, in the order determined
     *     by a call to <code>getChildrenAndFacets()</code>.</li>
     * <li>Call the <code>updateModel()</code> method of this component.</li>
     * </ul>
     *
     * <p>Return <code>false</code> if any <code>processUpdates()</code>
     * or <code>updateModel()</code> method call returned <code>false</code>.
     * Otherwise, return <code>true</code>.</p>
     *
     * @param context {@link FacesContext} for the request we are processing
     *
     * @exception NullPointerException if <code>context</code>
     *  is <code>null</code>
     */
    public boolean processUpdates(FacesContext context) {

        if (context == null) {
            throw new NullPointerException();
        }
        boolean result = true;

        // Process all facets and children of this component
        Iterator kids = getChildrenAndFacets();
        while (kids.hasNext()) {
            UIComponent kid = (UIComponent) kids.next();
            if (!kid.processUpdates(context)) {
                result = false;
            }
        }

        // Process this component itself
        if (!updateModel(context)) {
            result = false;
        }

        // Return the final result
        return (result);

    }


}

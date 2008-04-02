/*
 * $Id: UIComponentBase.java,v 1.40 2003/01/17 02:18:07 craigmcc Exp $
 */

/*
 * Copyright 2002-2003 Sun Microsystems, Inc. All rights reserved.
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
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;
import javax.faces.event.PhaseId;
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
                (context.getTree().getRenderKitId());
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
     * <p>Set the current validity state of this component.</p>
     *
     * @param valid The new validity state
     */
    public void setValid(boolean valid) {

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

    public void addFacet(String facetName, UIComponent facet) {

        if (null == facet || null == facetName) {
            throw new NullPointerException();
        }
        getFacets().put(facetName, facet);

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
    public Iterator getFacetsAndChildren() {

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
     * <p>Broadcast the specified {@link FacesEvent} to all registered
     * event listeners who have expressed an interest in events of this
     * type, for the specified {@link PhaseId}.  The order in which
     * registered listeners are notified is implementation dependent.</p>
     *
     * <p>After all interested listeners have been notified, return
     * <code>false</code> if this event does not have any listeners
     * interested in this event in future phases of the request processing
     * lifecycle.  Otherwise, return <code>true</code>.</p>
     *
     * <p>The default implementation throws IllegalArgumentException
     * (assuming that the parameters are non-null),
     * because {@link UIComponentBase} does not support any event types.</p>
     *
     * @param event The {@link FacesEvent} to be broadcast
     * @param phaseId The {@link PhaseId} of the current phase of the
     *  request processing lifecycle
     *
     * @exception AbortProcessingException Signal the JavaServer Faces
     *  implementation that no further processing on the current event
     *  should be performed
     * @exception IllegalArgumentException if the implementation class
     *  of this {@link FacesEvent} is not supported by this component
     * @exception IllegalStateException if PhaseId.ANY_PHASE is passed
     *  for the phase identifier
     * @exception NullPointerException if <code>event</code> or
     *  <code>phaseId</code> is <code>null</code>
     */
    public boolean broadcast(FacesEvent event, PhaseId phaseId)
        throws AbortProcessingException {

        if ((event == null) || (phaseId == null)) {
            throw new NullPointerException();
        }
        throw new IllegalArgumentException();

    }


    /**
     * <p>Decode the current state of this <code>UIComponent</code> from the
     * request contained in the specified {@link FacesContext}, and attempt
     * to convert this state information into an object of the required type
     * for this component.  If conversion is successful:</p>
     * <ul>
     * <li>Save the new local value of this component by calling
     *     <code>setValue()</code> and passing the new value.</li>
     * <li>Set the <code>valid</code> property of this component
     *     to <code>true</code>.</li>
     * </ul>
     *
     * <p>If conversion is not successful:</p>
     * <ul>
     * <li>Save state information in such a way that encoding
     *     can reproduce the previous input (even though it was syntactically
     *     or semantically incorrect)</li>
     * <li>Add an appropriate conversion failure error message by calling
     *     <code>context.addMessage()</code>.</li>
     * <li>Set the <code>valid</code> property of this comonent
     *     to <code>false</code>.</li>
     * </ul>
     *
     * <p>During decoding, events may be enqueued for later processing
     * (by event listeners who have registered an interest),  by calling
     * <code>addFacesEvent()</code> on the associated {@link FacesContext}.
     * </p>
     *
     * <p>The default behavior of this method is to check the parameter
     * for validity, and delegate to the associated {@link Renderer}
     * if there is one.  If there is no associated {@link Renderer}, the
     * <code>valid</code> property of this component is set to
     * <code>true</code>, and no further action is taken.</p>
     *
     * @param context {@link FacesContext} for the request we are processing
     *
     * @exception IOException if an input/output error occurs during decoding
     * @exception NullPointerException if <code>context</code>
     *  is <code>null</code>
     */
    public void decode(FacesContext context) throws IOException {

        if (context == null) {
            throw new NullPointerException();
        }
        String rendererType = getRendererType();
        if (rendererType != null) {
            RenderKitFactory rkFactory = (RenderKitFactory)
                FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
            RenderKit renderKit = rkFactory.getRenderKit
                (context.getTree().getRenderKitId());
            Renderer renderer = renderKit.getRenderer(rendererType);
            renderer.decode(context, this);
        } else {
            setValid(true);
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
                (context.getTree().getRenderKitId());
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
                (context.getTree().getRenderKitId());
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
                (context.getTree().getRenderKitId());
            Renderer renderer = renderKit.getRenderer(rendererType);
            renderer.encodeEnd(context, this);
        }

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
     *     </ul></li>
     * <li>If the <code>setModelValue()</code> method call fails:
     *     <ul>
     *     <li>Enqueue error messages by calling <code>addMessage()</code>
     *         on the specified {@link FacesContext} instance.</li>
     *     <li>Set the <code>valid</code> property of this component to
     *         <code>false</code>.</li>
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
    public void updateModel(FacesContext context) {

        if (context == null) {
            throw new NullPointerException();
        }
        if (!isValid()) {
            return;
        }
        String modelReference = getModelReference();
        if (modelReference == null) {
            return;
        }
        try {
            context.setModelValue(modelReference, getValue());
            setValue(null);
            return;
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
     * on itself.  This method will be called during the
     * <em>Process Validations</em> phase of the request processing
     * lifecycle.  If errors are encountered, appropriate <code>Message</code>
     * instances should be added to the {@link FacesContext} for the current
     * request, and the <code>valid</code> property of this {@link UIComponent}
     * should be set to <code>false</code>.</p>
     *
     * @param context FacesContext for the request we are processing
     *
     * @exception NullPointerException if <code>context</code>
     *  is <code>null</code>
     */
    public void validate(FacesContext context) {

        if (context == null) {
            throw new NullPointerException();
        }

    }


    // ----------------------------------------------- Lifecycle Phase Handlers


    /**
     * <p>Perform the component tree processing required by the
     * <em>Apply Request Values</em> phase of the request processing
     * lifecycle for all facets of this component, all children of this
     * component, and this component itself, as follows.</p>
     * <ul>
     * <li>Call the <code>processDecodes()</code> method of all facets
     *     of this component, in the order their names would be
     *     returned by a call to <code>getFacetNames()</code>.</li>
     * <li>Call the <code>processDecodes() method of all children
     *     of this component, in the order they would be returned
     *     by a call to <code>getChildren()</code>.</li>
     * <li>Call the <code>decode()</code> method of this component.</li>
     * <li>If the <code>valid</code> property of this {@link UIComponent}
     *     is now <code>false</code>, call
     *     <code>FacesContext.renderResponse()</code>
     *     to transfer control at the end of the current phase.</li>
     * </ul>
     *
     * @param context {@link FacesContext} for the request we are processing
     *
     * @exception IOException if an input/output error occurs during decoding
     * @exception NullPointerException if <code>context</code>
     *  is <code>null</code>
     */
    public void processDecodes(FacesContext context) throws IOException {

        if (context == null) {
            throw new NullPointerException();
        }

        // Process all facets and children of this component
        Iterator kids = getFacetsAndChildren();
        while (kids.hasNext()) {
            UIComponent kid = (UIComponent) kids.next();
            kid.processDecodes(context);
        }

        // Process this component itself
        decode(context);
        if (!isValid()) {
            context.renderResponse();
        }

    }


    /**
     * <p>Perform the component tree processing required by the
     * <em>Process Validations</em> phase of the request processing
     * lifecycle for all facets of this component, all children of this
     * component, and this component itself, as follows.</p>
     * <ul>
     * <li>Call the <code>processValidators()</code> method of all facets
     *     and children of this component, in the order determined
     *     by a call to <code>getFacetsAndChildren()</code>.</li>
     * <li>If the <code>valid</code> property of this component is
     *     currently <code>true</code>:
     *     <ul>
     *     <li>Call the <code>validate()</code> method of each
     *         {@link Validator} registered for this {@link UIComponent}.</li>
     *     <li>Call the <code>validate()</code> method of this component.</li>
     *     </ul></li>
     * </ul>
     *
     * @param context {@link FacesContext} for the request we are processing
     *
     * @exception NullPointerException if <code>context</code>
     *  is <code>null</code>
     */
    public void processValidators(FacesContext context) {

        if (context == null) {
            throw new NullPointerException();
        }

        // Process all the facets and children of this component
        Iterator kids = getFacetsAndChildren();
        while (kids.hasNext()) {
            UIComponent kid = (UIComponent) kids.next();
            kid.processValidators(context);
        }

        // Process this component itself
        if (isValid()) {
            Iterator validators = getValidators();
            while (validators.hasNext()) {
                Validator validator = (Validator) validators.next();
                validator.validate(context, this);
            }
            validate(context);
        }

    }


    /**
     * <p>Perform the component tree processing required by the
     * <em>Update Model Values</em> phase of the request processing
     * lifecycle for all facets of this component, all children of this
     * component, and this component itself, as follows.</p>
     * <ul>
     * <li>Call the <code>processUpdates()</code> method of all facets
     *     of this component, in the order their names would be
     *     returned by a call to <code>getFacetNames()</code>.</li>
     * <li>Call the <code>processUpdates()</code> method of all
     *     children of this component, in the order they would be
     *     returned by a call to <code>getChildren()</code>.</li>
     * <li>Call the <code>updateModel()</code> method of this component.</li>
     * <li>If the <code>valid</code> property of this {@link UIComponent}
     *     is now <code>false</code>, call
     *     <code>FacesContext.renderResponse()</code>
     *     to transfer control at the end of the current phase.</li>
     * </ul>
     *
     * @param context {@link FacesContext} for the request we are processing
     *
     * @exception NullPointerException if <code>context</code>
     *  is <code>null</code>
     */
    public void processUpdates(FacesContext context) {

        if (context == null) {
            throw new NullPointerException();
        }

        // Process all facets and children of this component
        Iterator kids = getFacetsAndChildren();
        while (kids.hasNext()) {
            UIComponent kid = (UIComponent) kids.next();
            kid.processUpdates(context);
        }

        // Process this component itself
        updateModel(context);
        if (!isValid()) {
            context.renderResponse();
        }

    }


}

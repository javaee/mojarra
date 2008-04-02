/*
 * $Id: UIComponentBase.java,v 1.44 2003/02/12 23:59:19 craigmcc Exp $
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
 * Concrete subclasses of {@link UIComponentBase} must implement the
 * <code>getComponentType()</code> method.  Component types are used to
 * select the appropriate {@link Renderer} to be used for decoding and
 * encoding, if the <code>rendererType</code> property is non-null.</p>
 *
 * <p>By default, this class defines <code>getRendersChildren()</code> to
 * return <code>false</code>.  Subclasses that wish to manage the rendering
 * of their children should override this method to return <code>true</code>
 * instead.</p>
 *
 * <p>By default, this class defines <code>getRendersSelf()</code> to
 * return <code>false</code>.  Subclasses that have directly implemented
 * decoding and encoding functionality, without the requirement for an
 * associated {@link Renderer}, should override this method to return
 * <code>true</code> instead.</p>
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

    public Iterator getAttributeNames() {

        if (isAttributesAllocated()) {
            return (getAttributes().keySet().iterator());
        } else {
            return (Collections.EMPTY_LIST.iterator());
        }

    }

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


    public String getClientId(FacesContext context) {
	String result = null;

	if (null != (result = (String) getAttribute(CLIENT_ID_ATTR))) {
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
	setAttribute(CLIENT_ID_ATTR, result);
	return result;
    }


    public String getComponentId() {

        return ((String) getAttribute("componentId"));

    }


    public void setComponentId(String componentId) {

	validateComponentId(componentId);

	String currentId = null;
	// Handle the case where we're re-naming a component
	if (null != (currentId = getComponentId())) {
	    maybeRemoveFromNearestNamingContainer(this);
	    setAttribute(CLIENT_ID_ATTR, null);
	}

        setAttribute("componentId", componentId);

	try {
	    // If we are already in the tree, make sure to add ourselves to
	    // the namespace.
	    maybeAddToNearestNamingContainer(this);
	}
	catch (IllegalStateException e) {
	    // If we can't be added to the namespace, roll-back our
	    // component-id
	    setAttribute("componentId", null);
	    throw e;
	}

    }


    public abstract String getComponentType();


    public String getModelReference() {

        return ((String) getAttribute("modelReference"));

    }


    public void setModelReference(String modelReference) {

        setAttribute("modelReference", modelReference);

    }


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


    public boolean isRendered() {

        Boolean rendered = (Boolean) getAttribute("rendered");
        if (rendered != null) {
            return (rendered.booleanValue());
        } else {
            return (true);
        }
    }
    

    public void setRendered(boolean rendered) {
        if ( rendered ) {
            setAttribute("rendered", Boolean.TRUE);
        } else {
            setAttribute("rendered", Boolean.FALSE);  
        }
    }


    public String getRendererType() {

        return ((String) getAttribute("rendererType"));

    }


    public void setRendererType(String rendererType) {

        setAttribute("rendererType", rendererType);

    }


    public boolean getRendersChildren() {

        return (false);

    }


    public boolean getRendersSelf() {

        return (false);

    }


    public boolean isValid() {

        Boolean valid = (Boolean) getAttribute("valid");
        if (valid != null) {
            return (valid.booleanValue());
        } else {
            return (false);

        }

    }


    public void setValid(boolean valid) {

        if (valid) {
            setAttribute("valid", Boolean.TRUE);
        } else {
            setAttribute("valid", Boolean.FALSE);
        }

    }


    public Object getValue() {

        return (getAttribute("value"));

    }


    public void setValue(Object value) {

        setAttribute("value", value);

    }


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
     * <p>Verify that the specified component id is safe to add to the tree.
     * </p>
     *
     * @param componentId The proposed component id to check for validity
     *
     * @exception IllegalArgumentException if <code>componentId</code>
     *  is <code>null</code> or contains invalid characters
     */
    private void validateComponentId(String componentId) {

	if (null == componentId) {
	    return;
	}

	// PENDING(edburns): here is where we check that the componentId
	// contains nothing but valid chars.

    }
            

    /**
     * <p>Find and return the closest naming container parent of the specified
     * {@link UIComponent}.</p>
     */
    private static NamingContainer findClosestNamingContainer
        (UIComponent start) {

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
     * <p>If the component identifier of this {@link UIComponent} instance is
     * non-null, and contains all valid characters, find the closest
     * ancestor that is a naming container and try to add this component
     * identifier to the namespace.</p>
     *
     * @param component The component to be added
     *
     * @return the closest ancestor to the specified {@link UIComponent}
     *  that is a naming container, if the specified {@link UIComponent} was
     *  successfully added to the namespace of that naming container, or
     *  <code>null</code> if no naming container is necessary.
     *
     * @exception IllegalArgumentException if the component identifier
     *  of the new component is non-null, and is not unique in the
     *  namespace of the closest ancestor that is a naming container.
     *
     * @exception IllegalArgumentException if the component identifier
     * of the new component is non-null, and contains non-valid chars.
     */
    private NamingContainer maybeAddToNearestNamingContainer
        (UIComponent component) {

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
     *
     * @param component the {@link UIComponent} instance to remove from the
     *  nearest naming container
     *
     * @return the {@link NamingContainer} from which this {@link UIComponent}
     *  instance was removed, or null if no naming container is found.
     */
    private NamingContainer maybeRemoveFromNearestNamingContainer
        (UIComponent component) {

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


    public void clearChildren() {

        if (!isChildrenAllocated()) {
            return;
        }
        children.clear();

    }


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


    public UIComponent getChild(int index) {

        return ((UIComponent) getChildList().get(index));

    }


    public int getChildCount() {

        if (isChildrenAllocated()) {
            return (getChildList().size());
        } else {
            return (0);
        }

    }


    public Iterator getChildren() {

        if (isChildrenAllocated()) {
            return (getChildList().iterator());
        } else {
            return (Collections.EMPTY_LIST.iterator());
        }

    }

    public void removeChild(int index) {

        UIComponent kid = getChild(index);
        getChildList().remove(index);
        if (kid instanceof UIComponentBase) { // FIXME -- Hmmmmm
            ((UIComponentBase) kid).setParent(null);
        }

    }

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
	facet.setAttribute(FACET_PARENT_ATTR, this);
        getFacets().put(facetName, facet);

    }


    public void clearFacets() {

	// PENDING(edburns): do we need to clear the FACET_PARENT_ATTR
	// attribute of each of the facets?  This would cost some time.

        facets = null;

    }


    public UIComponent getFacet(String name) {

        if (name == null) {
            throw new NullPointerException();
        }
        return ((UIComponent) getFacets().get(name));

    }


    public Iterator getFacetNames() {

        if (facets != null) {
            return (facets.keySet().iterator());
        } else {
            return (Collections.EMPTY_LIST.iterator());
        }

    }

    public void removeFacet(String name) {

        if (name == null) {
            throw new NullPointerException();
        }
	UIComponent facet = null;
        if (facets != null) {
	    if (null != (facet = (UIComponent) facets.remove(name))) {
		facet.setAttribute(FACET_PARENT_ATTR, null);
	    }
        }

    }

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


    public void addValidator(Validator validator) {

        if (validator == null) {
            throw new NullPointerException();
        }
        if (validators == null) {
            validators = new ArrayList();
        }
        validators.add(validator);

    }


    public void clearValidators() {

        validators = null;

    }


    public Iterator getValidators() {

        if (validators != null) {
            return (validators.iterator());
        } else {
            return (Collections.EMPTY_LIST.iterator());
        }

    }

    public void removeValidator(Validator validator) {

        if (validators != null) {
            validators.remove(validator);
        }

    }


    // ------------------------------------------- Lifecycle Processing Methods


    public boolean broadcast(FacesEvent event, PhaseId phaseId)
        throws AbortProcessingException {

        if ((event == null) || (phaseId == null)) {
            throw new NullPointerException();
        }
        throw new IllegalArgumentException();

    }

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


    public void validate(FacesContext context) {

        if (context == null) {
            throw new NullPointerException();
        }

    }


    // ----------------------------------------------- Lifecycle Phase Handlers

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

    }


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
        if (!isValid()) {
            context.renderResponse();
        }

    }


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

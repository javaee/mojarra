/*
 * $Id: UIComponentBase.java,v 1.57 2003/07/15 00:03:20 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
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
 * {@link UIComponent}.</p>
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
            throw new NullPointerException();
        }

        // PENDING(craigmcc) - Should we reflectively retrieve component
        // property values that correspond to the specified name?

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
            throw new NullPointerException();
        }

        // PENDING(craigmcc) - should we disallow setting attributes that match
        // UIComponent property names?

        // PENDING(craigmcc) - should we reflectively call property setters
        // for corresponding UIComponent properties?

        // Set or remove the specified value
        if (value != null) {
            getAttributes().put(name, value);
        } else {
            if (isAttributesAllocated()) {
                getAttributes().remove(name);
            }
        }

    }


    // ------------------------------------------------------------- Properties


    /**
     * <p>The assigned client identifier for this component.</p>
     */
    private String clientId = null;


    public String getClientId(FacesContext context) {

	// Return any previously calculated client identifier
        if (clientId != null) {
            return (clientId);
        }

	// if there is a Renderer for this component
        String rendererType = getRendererType();
        if (rendererType != null) {

	    // let the Renderer define the client id
	    Renderer renderer = getRenderer(context);
            clientId = renderer.getClientId(context, this);

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

		// If there is no componentId, ask our naming container
		if (componentId == null) {
		    clientId = closestContainer.generateClientId();
		} else {
                    clientId = componentId;
                }

		// build up the client side id
		containerComponent = (UIComponent) closestContainer;

		// If this is the root naming container, break
		if (null != containerComponent.getParent()) {
		    clientId = containerComponent.getClientId(context) +
			SEPARATOR_CHAR + clientId;
		}

	    }

        }
	if (null == clientId) {
	    throw new NullPointerException();
	}
	return (clientId);
    }


    /**
     * <p>The component identifier for this component.</p>
     */
    private String componentId = null;


    public String getComponentId() {
        return (this.componentId);
    }


    public void setComponentId(String componentId) {

	validateComponentId(componentId);

	String currentId = null;
	// Handle the case where we're re-naming a component
	if (null != (currentId = getComponentId())) {
	    maybeRemoveFromNearestNamingContainer(this);
            this.clientId = null;
	}

        this.componentId = componentId;

	try {
	    // If we are already in the tree, make sure to add ourselves to
	    // the namespace.
	    maybeAddToNearestNamingContainer(this);
	}
	catch (IllegalStateException e) {
	    // If we can't be added to the namespace, roll-back our
	    // component-id
            this.componentId = null;
	    throw e;
	}

    }


    /**
     * <p>The component reference expression for this component.</p>
     */
    private String componentRef = null;


    public String getComponentRef() {
	return (this.componentRef);
    }


    public void setComponentRef(String componentRef) {
	this.componentRef = componentRef;
    }


    /**
     * <p>The converter identifier for this component.</p>
     */
    private String converter = null;


    public String getConverter() {
        return (this.converter);
    }


    public void setConverter(String converter) {
        this.converter = converter;
    }


    /**
     * <p>The parent component for this component.</p>
     */
    private UIComponent parent = null;


    public UIComponent getParent() {
        return (this.parent);
    }


    public void setParent(UIComponent parent) {
        this.parent = parent;
    }


    /**
     * <p>The "should this component be rendered" flag.</p>
     */
    private boolean rendered = true;


    public boolean isRendered() {
        return (this.rendered);
    }
    

    public void setRendered(boolean rendered) {
        this.rendered = rendered;
    }


    /**
     * <p>The renderer type for this component.</p>
     */
    private String rendererType = null;


    public String getRendererType() {
        return (this.rendererType);
    }


    public void setRendererType(String rendererType) {
        this.rendererType = rendererType;
    }


    public boolean getRendersChildren() {
        return (false);
    }


    public boolean getRendersSelf() {
        return (false);
    }


    public boolean isValid() {
        return (true);
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
     * contains invalid characters.
     */
    private void validateComponentId(String componentId) {

	// PENDING(edburns): if the page author can assure us that all
	// the ids are valid, we could turn this off as a performance
	// enhancement.

	int i = 0, len;
	char [] chars = null;
	if (null == componentId || 0 == (len = componentId.length())) {
	    return;
	}
	chars = componentId.toCharArray();

	if (!Character.isLetter(chars[0])) {
	    throw new IllegalArgumentException();
	}
	
	for (i = 1; i < len; i++) {
	    if (!Character.isLetterOrDigit(chars[i])) {
		if ('-' != chars[i] && '_' != chars[i]) {
		    throw new IllegalArgumentException();
		}
	    }
	}		

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
        component.setParent(this);

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
        component.setParent(this);

    }


    public void clearChildren() {

        if (!isChildrenAllocated()) {
            return;
        }
        int n = children.size();
        for (int i = 0; i < n; i++) {
            ((UIComponent) children.get(i)).setParent(null);
        }
        children = null;

    }


    public boolean containsChild(UIComponent component) {

        if (!isChildrenAllocated()) {
            return (false);
        }
        int n = children.size();
        for (int i = 0; i < n; i++) {
            if ((UIComponent) children.get(i) == component) {
                return (true);
            }
        }
        return (false);

    }


    public UIComponent findComponent(String expr) {

        if (expr == null) {
            throw new NullPointerException();
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
        kid.setParent(null);

    }

    public void removeChild(UIComponent component) {

        if (component == null) {
            throw new NullPointerException("removeChild");
        }
        if (containsChild(component)) {
            getChildList().remove(component);
            component.setParent(null);
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
        UIComponent current = (UIComponent) getFacets().get(facetName);
        if (current != null) {
            current.setParent(null);
        }
        getFacets().put(facetName, facet);
        facet.setParent(this);

    }


    public void clearFacets() {

        Iterator facetNames = getFacetNames();
        while (facetNames.hasNext()) {
            getFacet((String) facetNames.next()).setParent(null);
        }
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
		facet.setParent(null);
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
            getRenderer(context).decode(context, this);
        } else if (this instanceof UIInput) {
            ((UIInput) this).setValid(true);
        }

    }

    public void encodeBegin(FacesContext context) throws IOException {

        if (context == null) {
            throw new NullPointerException();
        }
        String rendererType = getRendererType();
        if (rendererType != null) {
            getRenderer(context).encodeBegin(context, this);
        }

    }

    public void encodeChildren(FacesContext context) throws IOException {

        if (context == null) {
            throw new NullPointerException();
        }
        String rendererType = getRendererType();
        if (rendererType != null) {
	    getRenderer(context).encodeChildren(context, this);
        }

    }

    public void encodeEnd(FacesContext context) throws IOException {

        if (context == null) {
            throw new NullPointerException();
        }
        String rendererType = getRendererType();
        if (rendererType != null) {
	    getRenderer(context).encodeEnd(context, this);
        }

    }


    public void reconstitute(FacesContext context) throws IOException {

        if (context == null) {
            throw new NullPointerException();
        }

    }


    public void updateModel(FacesContext context) {

        if (context == null) {
            throw new NullPointerException();
        }

    }


    // ----------------------------------------------- Lifecycle Phase Handlers


    public void processReconstitutes(FacesContext context) throws IOException {

        if (context == null) {
            throw new NullPointerException();
        }

        // Process all facets and children of this component
        Iterator kids = getFacetsAndChildren();
        while (kids.hasNext()) {
            UIComponent kid = (UIComponent) kids.next();
            kid.processReconstitutes(context);
        }

        // Process this component itself
        reconstitute(context);

    }


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

	// Validate this component itself
	if (this instanceof UIInput) {
	    ((UIInput) this).validate(context);
	}

	// Advance to Render Response if this component is not valid
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


    // ------------------------------------------------------- Protected Methods


    /**
     * <p>Return the {@link Renderer} instance associated with this
     * {@link UIComponent}, if any; otherwise, return <code>null</code>.</p>
     *
     * @param context {@link FacesContext} for the current request
     */
    protected Renderer getRenderer(FacesContext context) {

	String rendererType = getRendererType();
	if (rendererType != null) {
            RenderKitFactory rkFactory = (RenderKitFactory)
                FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
            RenderKit renderKit = rkFactory.getRenderKit
                (context.getTree().getRenderKitId());
            return (renderKit.getRenderer(rendererType));
	} else {
	    return (null);
	}

    }


}

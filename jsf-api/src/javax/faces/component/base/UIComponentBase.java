/*
 * $Id: UIComponentBase.java,v 1.2 2003/07/26 17:54:48 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component.base;


import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
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
 */

public abstract class UIComponentBase implements UIComponent {


    // ------------------------------------------------------------- Attributes


    /**
     * <p>Each entry is an array of <code>PropertyDescriptor</code>s describing
     * the properties of a concrete {@link UIComponent} implementation, keyed
     * by the corresponding <code>java.lang.Class</code>.</p>
     *
     * <p><strong>IMPLEMENTATION NOTE</strong> - This is implemented as a
     * <code>WeakHashMap</code> so that, even if this class is embedded in a
     * container's class loader that is a parent to webapp class loaders,
     * references to the classes will eventually expire.</p>
     */
    private static WeakHashMap descriptors = new WeakHashMap();



    /**
     * <p>An empty argument list to be passed to reflection methods.</p>
     */
    private static Object empty[] = new Object[0];


    /**
     * <p>Return the <code>PropertyDescriptor</code> for the specified
     * property name for this {@link UIComponent}'s implementation class,
     * if any; otherwise, return <code>null</code>.</p>
     *
     * @param name Name of the property to return a descriptor for
     *
     * @exception FacesException if an introspection exception occurs
     */
    private PropertyDescriptor getPropertyDescriptor(String name) {

        PropertyDescriptor pd[] = getPropertyDescriptors();
        for (int i = 0; i < pd.length; i++) {
            if (name.equals(pd[i].getName())) {
                return (pd[i]);
            }
        }
        return (null);

    }


    /**
     * <p>Return an array of <code>PropertyDescriptors</code> for this
     * {@link UIComponent}'s implementation class.  If no descriptors
     * can be identified, a zero-length array will be returned.</p>
     *
     * @exception FacesException if an introspection exception occurs
     */
    private PropertyDescriptor[] getPropertyDescriptors() {

        synchronized (descriptors) {
            PropertyDescriptor pd[] =
                (PropertyDescriptor[]) descriptors.get(this.getClass());
            if (pd == null) {
                try {
                    pd = Introspector.getBeanInfo(this.getClass()).
                        getPropertyDescriptors();
                } catch (IntrospectionException e) {
                    throw new FacesException(e);
                }
                descriptors.put(this.getClass(), pd);
            }
            return (pd);
        }

    }


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

        // Return the specified property value (if this is a property)
        PropertyDescriptor pd = getPropertyDescriptor(name);
        if (pd != null) {
            try {
                Method readMethod = pd.getReadMethod();
                if (readMethod != null) {
                    return (readMethod.invoke(this, empty));
                }
            } catch (IllegalAccessException e) {
                throw new FacesException(e);
            } catch (InvocationTargetException e) {
                throw new FacesException(e.getTargetException());
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
            throw new NullPointerException();
        }

        // Set the specified property value (if this is a property)
        PropertyDescriptor pd = getPropertyDescriptor(name);
        if (pd != null) {
            try {
                Method writeMethod = pd.getWriteMethod();
                if (writeMethod != null) {
                    writeMethod.invoke(this, new Object[] { value });
                    return;
                }
            } catch (IllegalAccessException e) {
                throw new FacesException(e);
            } catch (InvocationTargetException e) {
                throw new FacesException(e.getTargetException());
            }
        }


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

		// If there is no component id, ask our naming container
		if (id == null) {
		    clientId = closestContainer.generateClientId();
		} else {
                    clientId = id;
                }

		// build up the client side id
		containerComponent = (UIComponent) closestContainer;

		// If this is the root naming container, break
		if (null != containerComponent.getParent()) {
		    clientId = containerComponent.getClientId(context) +
			NamingContainer.SEPARATOR_CHAR + clientId;
		}

	    }

        }
	if (null == clientId) {
	    throw new NullPointerException();
	}
	return (clientId);
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
     * <p>The component identifier for this component.</p>
     */
    private String id = null;


    public String getId() {
        return (this.id);
    }


    public void setId(String id) {

	validateId(id);
        NamingContainer naming = getNamingContainer();
	String currentId = getId();

	// Handle the case where we're renaming this component
        if ((currentId != null) && (naming != null)) {
            naming.removeComponentFromNamespace(this);
            this.clientId = null;
	}

        // Save the newly assigned component identifier
        this.id = id;
        if ((id != null) && (naming != null)) {
            naming.addComponentToNamespace(this);
        }

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


    // ------------------------------------------------ Tree Management Methods


    /*
     * <p>The <code>List</code> containing our child components.</p>
     */
    private List children = null;


    public List getChildren() {

        if (children == null) {
            children = new ArrayList() {

                    public void add(int index, Object element) {
                        if (element == null) {
                            throw new NullPointerException();
                        } else if (!(element instanceof UIComponent)) {
                            throw new ClassCastException();
                        } else if ((index < 0) || (index > size())) {
                            throw new IndexOutOfBoundsException();
                        } else {
                            UIComponent child =
                                (UIComponent) element;
                            String id = child.getId();
                            validateId(id);
                            NamingContainer naming = getNamingContainer();
                            validateMissing(naming, id);
                            if ((naming != null) && (id != null)) {
                                naming.addComponentToNamespace(child);
                            }
                            child.setParent(UIComponentBase.this);
                            super.add(index, child);
                        }
                    }

                    public boolean add(Object element) {
                        if (element == null) {
                            throw new NullPointerException();
                        } else if (!(element instanceof UIComponent)) {
                            throw new ClassCastException();
                        } else {
                            UIComponent child =
                                (UIComponent) element;
                            String id = child.getId();
                            validateId(id);
                            NamingContainer naming = getNamingContainer();
                            validateMissing(naming, id);
                            if ((naming != null) && (id != null)) {
                                naming.addComponentToNamespace(child);
                            }
                            child.setParent(UIComponentBase.this);
                            return (super.add(element));
                        }
                    }

                    public boolean addAll(Collection collection) {
                        Iterator elements = collection.iterator();
                        boolean changed = false;
                        while (elements.hasNext()) {
                            UIComponent element =
                                (UIComponent) elements.next();
                            if (element == null) {
                                throw new NullPointerException();
                            } else {
                                add(element);
                                changed = true;
                            }
                        }
                        return (changed);
                    }

                    public boolean addAll(int index, Collection collection) {
                        Iterator elements = collection.iterator();
                        boolean changed = false;
                        while (elements.hasNext()) {
                            UIComponent element =
                                (UIComponent) elements.next();
                            if (element == null) {
                                throw new NullPointerException();
                            } else {
                                add(index++, element);
                                changed = true;
                            }
                        }
                        return (changed);
                    }

                    public void clear() {
                        int n = size();
                        if (n < 1) {
                            return;
                        }
                        NamingContainer naming = getNamingContainer();
                        for (int i = 0; i < n; i++) {
                            UIComponent child = (UIComponent) get(i);
                            child.setParent(null);
                            if ((naming != null) && (child.getId() != null)) {
                                naming.removeComponentFromNamespace(child);
                            }
                        }
                        super.clear();
                    }

                    public Iterator iterator() {
                        // PENDING(craigmcc) - Custom remove support needed
                        return (super.iterator());
                    }

                    public ListIterator listIterator() {
                        // PENDING(craigmcc) - Custom remove support needed
                        return (super.listIterator());
                    }

                    public ListIterator listIterator(int index) {
                        // PENDING(craigmcc) - Custom remove support needed
                        return (super.listIterator(index));
                    }

                    public Object remove(int index) {
                        UIComponent child = (UIComponent) get(index);
                        super.remove(index);
                        child.setParent(null);
                        NamingContainer naming = getNamingContainer();
                        if (naming != null) {
                            naming.removeComponentFromNamespace(child);
                        }
                        return (child);
                    }

                    public boolean remove(Object element) {
                        if (element == null) {
                            throw new NullPointerException();
                        } else if (!(element instanceof UIComponent)) {
                            return (false);
                        }
                        if (super.remove(element)) {
                            UIComponent child = (UIComponent) element;
                            child.setParent(null);
                            NamingContainer naming = getNamingContainer();
                            if (naming != null) {
                                naming.removeComponentFromNamespace(child);
                            }
                            return (true);
                        } else {
                            return (false);
                        }
                    }

                    public boolean removeAll(Collection collection) {
                        boolean result = false;
                        Iterator elements = collection.iterator();
                        while (elements.hasNext()) {
                            if (remove(elements.next())) {
                                result = true;
                            }
                        }
                        return (result);
                    }

                    public boolean retainAll(Collection collection) {
                        // PENDING(craigmcc) - Custom remove support needed
                        return (super.retainAll(collection));
                    }

                    public Object set(int index, Object element) {
                        if (element == null) {
                            throw new NullPointerException();
                        } else if (!(element instanceof UIComponent)) {
                            throw new ClassCastException();
                        } else if ((index < 0) || (index >= size())) {
                            throw new IndexOutOfBoundsException();
                        } else {
                            UIComponent child =
                                (UIComponent) element;
                            String id = child.getId();
                            validateId(id);
                            NamingContainer naming = getNamingContainer();
                            validateMissing(naming, id);
                            UIComponent previous =
                                (UIComponent) get(index);
                            previous.setParent(null);
                            if (naming != null) {
                                naming.removeComponentFromNamespace(previous);
                                naming.addComponentToNamespace(child);
                            }
                            child.setParent(UIComponentBase.this);
                            super.set(index, element);
                            return (previous);
                        }
                    }

                    // Throw IllegalStateException if id already present
                    private void validateMissing(NamingContainer naming,
                                                 String id) {
                        if ((naming == null) || (id == null)) {
                            return;
                        }
                        if (naming.findComponentInNamespace(id) != null) {
                            throw new IllegalStateException(id);
                        }
                    }

                };

        }
        return (children);

    }


    /**
     * <p>Find and return the closest parent {@link UIComponent} (which might
     * in fact be this one) that implements {@link NamingContainer}.  If no
     * such {@link NamingContainer} can be found, return <code>null</code>.</p>
     */
    private NamingContainer getNamingContainer() {

        NamingContainer closest = null;
        UIComponent component = UIComponentBase.this;
        while ((closest == null) && (component != null)) {
            if (component instanceof NamingContainer) {
                closest = (NamingContainer) component;
                break;
            }
            component = component.getParent();
        }
        return (closest);

    }


    /**
     * <p>Throw <code>IllegalArgumentException</code> if the specified
     * component identifier is not syntactically valie or <code>null</code>.
     * </p>
     *
     * @param id The component identifier to test
     */
    private void validateId(String id) {

        if (id == null) {
            return;
        }
        int n = id.length();
        if (n < 1) {
            throw new IllegalArgumentException();
        }
        for (int i = 0; i < n; i++) {
            char c = id.charAt(i);
            if (i == 0) {
                if (!Character.isLetter(c)) {
                                    throw new IllegalArgumentException(id);
                }
            } else {
                if (!Character.isLetter(c) &&
                    !Character.isDigit(c) &&
                    (c != '-') && (c != '_')) {
                    throw new IllegalArgumentException(id);
                }
            }
        }

    }


    public UIComponent findComponent(String expr) {

        if (expr == null) {
            throw new NullPointerException();
        }
	NamingContainer naming = getNamingContainer();
        if (naming != null) {
	    return (naming.findComponentInNamespace(expr));
	} else {
            return (null);
        }

    }


    // ----------------------------------------------- Facet Management Methods


    /*
     * <p>The <code>Map</code> containing our related facet components.</p>
     */
    private Map facets = null;


    public Map getFacets() {

        if (facets == null) {
            facets = new HashMap() {

                    public void clear() {
                        Iterator values = values().iterator();
                        while (values.hasNext()) {
                            UIComponent value = (UIComponent) values.next();
                            value.setParent(null);
                        }
                        super.clear();
                    }

                    public Set entrySet() {
                        // PENDING(craigmcc) - Custom remove support needed
                        return (super.entrySet());
                    }

                    public Set keySet() {
                        // PENDING(craigmcc) - Custom remove support needed
                        return (super.keySet());
                    }

                    public Object put(Object key, Object value) {
                        if ((key == null) || (value == null)) {
                            throw new NullPointerException();
                        } else if (!(key instanceof String) ||
                                   !(value instanceof UIComponent)) {
                            throw new ClassCastException();
                        }
                        UIComponent previous = (UIComponent) super.get(key);
                        if (previous != null) {
                            previous.setParent(null);
                        }
                        UIComponent current = (UIComponent) value;
                        current.setParent(UIComponentBase.this);
                        return (super.put(key, value));
                    }

                    public void putAll(Map map) {
                        if (map == null) {
                            throw new NullPointerException();
                        }
                        Iterator keys = map.keySet().iterator();
                        while (keys.hasNext()) {
                            Object key = keys.next();
                            put(key, map.get(key));
                        }
                    }

                    public Object remove(Object key) {
                        UIComponent previous = (UIComponent) get(key);
                        if (previous != null) {
                            previous.setParent(null);
                        }
                        super.remove(key);
                        return (previous);
                    }

                    public Collection values() {
                        // PENDING(craigmcc) - Custom remove support needed
                        return (super.values());
                    }

                };

        }
        return (facets);

    }


    public Iterator getFacetsAndChildren() {

        List combined = null;
        if (this.facets != null) {
            Iterator facets = getFacets().values().iterator();
            while (facets.hasNext()) {
                if (combined == null) {
                    combined = new ArrayList();
                }
                combined.add(facets.next());
            }
        }
        if (this.children != null) {
            Iterator kids = getChildren().iterator();
            while (kids.hasNext()) {
                if (combined == null) {
                    combined = new ArrayList();
                }
                combined.add(kids.next());
            }
        }
        if (combined == null) {
            return (Collections.EMPTY_LIST.iterator());
        } else {
            // PENDING(craigmcc) - need to disable remove() method
            return (combined.iterator());
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
        if ((this instanceof UIInput) &&
            !((UIInput) this).isValid()) {
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
        if (this instanceof UIInput) {
            ((UIInput) this).updateModel(context);
            if (!((UIInput) this).isValid()) {
                context.renderResponse();
            }
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

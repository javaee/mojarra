/*
 * $Id: UIComponentBase.java,v 1.9 2003/09/04 03:52:49 eburns Exp $
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
import javax.faces.event.FacesListener;
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
	NamingContainer closestContainer = null;
	UIComponent containerComponent = this;

	// note that this method uses different logic to locate the
	// closest naming container than what is implemented in
	// findClosestNamingContainer().  This is intentional and
	// necessary to support the requirement of separating when you
	// get a client id from when you add the child to the tree.

	// if there is a Renderer for this component
        String rendererType = getRendererType();
        if (rendererType != null) {

	    // let the Renderer define the client id
	    Renderer renderer = getRenderer(context);
            clientId = renderer.getClientId(context, this);

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

        } else {

	    // we have to define the client id ourselves

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

	if (null != closestContainer && null != id) {
	    // ensure we've been added to the namespace.
	    if (null == closestContainer.findComponentInNamespace(id)){
		closestContainer.addComponentToNamespace(this);
	    }
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
                            addRecursive(getNamingContainer(), child);
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
                            addRecursive(getNamingContainer(), child);
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
                            removeRecursive(naming, child);
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
                        removeRecursive(getNamingContainer(), child);
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
                            removeRecursive(getNamingContainer(), child);
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
                                removeRecursive(naming, previous);
                                addRecursive(naming, child);
                            }
                            child.setParent(UIComponentBase.this);
                            super.set(index, element);
                            return (previous);
                        }
                    }

                    // Add any named children to this naming container
                    private void addRecursive(NamingContainer naming,
                                              UIComponent component) {
                        if ((naming == null) || (component == null)) {
                            return;
                        }
                        String id = component.getId();
                        if (id != null) {
                            validateId(id);
                            validateMissing(naming, id);
                            naming.addComponentToNamespace(component);
                        }
                        Iterator kids = component.getChildren().iterator();
                        while (kids.hasNext()) {
                            addRecursive(naming, (UIComponent) kids.next());
                        }
                    }


                    // Remove any named children from this naming container
                    private void removeRecursive(NamingContainer naming,
                                                 UIComponent component) {
                        if ((naming == null) || (component == null)) {
                            return;
                        }
                        if (component.getId() != null) {
                            naming.removeComponentFromNamespace(component);
                        }
                        Iterator kids = component.getChildren().iterator();
                        while (kids.hasNext()) {
                            removeRecursive(naming, (UIComponent) kids.next());
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
        if (listeners == null) {
            return (false);
        }

        // Broadcast the event to interested listeners
        broadcast(event, listeners[PhaseId.ANY_PHASE.getOrdinal()]);
        broadcast(event, listeners[phaseId.getOrdinal()]);

        // Determine whether there are any registered listeners for later phases
        // that are interested in this event
        for (int i = phaseId.getOrdinal() + 1; i < listeners.length; i++) {
            if ((listeners[i] != null) && (listeners[i].size() > 0)) {
                int n = listeners[i].size();
                for (int j = 0; j < n; j++) {
                    FacesListener listener = (FacesListener)
                        listeners[i].get(j);
                    if (event.isAppropriateListener(listener)) {
                        return (true);
                    }
                }
            }
        }
        return (false);

    }

    /**
     * <p>Broadcast this {@link FacesEvent} to all {@link FacesListener}s in
     * the specified <code>List</code> (if any).  If the <code>list</code>
     * is <code>null</code>, no action is taken.</p>
     *
     * <p>For each listener in the specified list, this method must first
     * call <code>isAppropriateListener(FacesListener)</code> to determine
     * whether this listener is interested in the current event, and (if it
     * is) this method must call <code>processListener(FacesListener)</code> to
     * actually broadcast the event.  Individual {@link FacesEvent} classes
     * must implement these two abstract methods appropriately.</p>
     *
     * @param event {@link FacesEvent} to be broadcast
     * @param list List of {@link FacesListener}s to notify (if any)
     *
     * @exception AbortProcessingException Signal the JavaServer Faces
     *  implementation that no further processing on the current event
     *  should be performed
     */
    private void broadcast(FacesEvent event, List list) {

        if (list == null) {
            return;
        }
        Iterator listeners = list.iterator();
        while (listeners.hasNext()) {
            FacesListener listener = (FacesListener) listeners.next();
            if (event.isAppropriateListener(listener)) {
                event.processListener(listener);
            }
        }

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

    // ------------------------------------------------- Event Listener Methods


    /**
     * <p>Each element of this array is a <code>List</code> of registered
     * {@link FacesListener}s for an ordinal {@link PhaseId} value.  This
     * data structure is lazily instantiated as necessary.</p>
     */
    protected List listeners[];


    /**
     * <p>Add the specified {@link FacesListener} to the set of listeners
     * registered to receive event notifications from this {@link UIComponent}.
     * It is expected that {@link UIComponent} classes acting as event sources
     * will have corresponding typesafe APIs for registering listeners of the
     * required type, and the implementation of those registration methods
     * will delegate to this method.  For example:</p>
     * <pre>
     * public class FooEvent extends FacesEvent {
     *   ...
     *   protected boolean isAppropriateListener(FacesListener listener) {
     *     return (listener instanceof FooListener);
     *   }
     *   protected void processListener(FacesListener listener) {
     *     ((FooListener) listener).processFoo(this);
     *   }
     *   ...
     * }
     *
     * public interface FooListener extends FacesListener {
     *   public PhaseId getPhaseId();
     *   public void processFoo(FooEvent event);
     * }
     *
     * public class FooComponent extends UIComponentBase {
     *   ...
     *   public void addFooListener(FooListener listener) {
     *     addFacesListener(listener);
     *   }
     *   public void removeFooListener(FooListener listener) {
     *     removeFacesListener(listener);
     *   }
     *   ...
     * }
     * </pre>
     *
     * @param listener The {@link FacesListener} to be registered
     *
     * @exception NullPointerExcepton if <code>listener</code>
     *  is <code>null</code>
     */
    protected void addFacesListener(FacesListener listener) {

        if (listener == null) {
            throw new NullPointerException();
        }
        if (listeners == null) {
            listeners = new List[PhaseId.VALUES.size()];
        }
        int ordinal = listener.getPhaseId().getOrdinal();
        if (listeners[ordinal] == null) {
            listeners[ordinal] = new ArrayList();
        }
        listeners[ordinal].add(listener);

    }


    /**
     * <p>Remove the specified {@link FacesListener} from the set of listeners
     * registered to receive event notifications from this {@link UIComponent}.
     *
     * @param listener The {@link FacesListener} to be deregistered
     *
     * @exception NullPointerException if <code>listener</code>
     *  is <code>null</code>
     */
    protected void removeFacesListener(FacesListener listener) {

        if (listener == null) {
            throw new NullPointerException();
        }
        if (listeners == null) {
            return;
        }
        int ordinal = listener.getPhaseId().getOrdinal();
        if (listeners[ordinal] != null) {
            listeners[ordinal].remove(listener);
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

    private static final int MY_STATE = 0;
    private static final int CHILD_STATE = 1;

    public Object processGetState(FacesContext context) throws IOException {

        if (context == null) {
            throw new NullPointerException();
        }
	Object [] stateStruct = new Object[2];
	Object [] childState = null;

        // Process all facets and children of this component
	int i = 0, len = getFacets().values().size() + getChildren().size();
	childState = new Object[len];
	stateStruct[CHILD_STATE] = childState;
        Iterator kids = getFacetsAndChildren();
        while (kids.hasNext()) {
            UIComponent kid = (UIComponent) kids.next();
	    childState[i++] = kid.processGetState(context);
        }

        // Process this component itself
        stateStruct[MY_STATE] = getState(context);
	return stateStruct;
    }

    public void processRestoreState(FacesContext context, 
				    Object state) throws IOException {

        if (context == null) {
            throw new NullPointerException();
        }

	Object [] stateStruct = (Object []) state;
	Object [] childState = (Object []) stateStruct[CHILD_STATE];
	int i = 0;

        // Process all facets and children of this component
        Iterator kids = getFacetsAndChildren();
        while (kids.hasNext()) {
            UIComponent kid = (UIComponent) kids.next();
            kid.processRestoreState(context, childState[i++]);
        }

        // Process this component itself
        restoreState(context, stateStruct[MY_STATE]);

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
                (context.getViewRoot().getRenderKitId());
            return (renderKit.getRenderer(rendererType));
	} else {
	    return (null);
	}

    }


    // ----------------------------------------------------- StateHolder Methods


    public Object getState(FacesContext context) {

        Object values[] = new Object[7];
        values[0] = attributes;
        values[1] = getClientId(context);
        values[2] = componentRef;
        values[3] = rendered ? Boolean.TRUE : Boolean.FALSE;
        values[4] = rendererType;
        values[5] =
            context.getApplication().getViewHandler().getStateManager().
            getAttachedObjectState(context, this, null, listeners);
        values[6] = transientFlag ? Boolean.TRUE : Boolean.FALSE;
        return (values);

    }


    public void restoreState(FacesContext context, Object state)
        throws IOException {

        Object values[] = (Object[]) state;
        attributes = (HashMap) values[0];
        clientId = (String) values[1];
        componentRef = (String) values[2];
        rendered = ((Boolean) values[3]).booleanValue();
        rendererType = (String) values[4];
        listeners = (List[])
            context.getApplication().getViewHandler().getStateManager().
            restoreAttachedObjectState(context, values[5]);
        transientFlag = ((Boolean) values[6]).booleanValue();

    }


    /**
     * <p>Flag indicating a desire to now participate in state saving.</p>
     */
    private boolean transientFlag = false;


    public boolean isTransient() {

        return (this.transientFlag);

    }


    public void setTransient(boolean transientFlag) {

        this.transientFlag = transientFlag;

    }


}

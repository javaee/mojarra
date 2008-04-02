/*
 * $Id: UIComponentBase.java,v 1.113 2005/08/03 21:43:10 edburns Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.AbstractCollection;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.el.ELContext;
import javax.el.ELException;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;
import javax.faces.event.FacesListener;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.faces.render.Renderer;


/**
 * <p><strong>UIComponentBase</strong> is a convenience base class that
 * implements the default concrete behavior of all methods defined by
 * {@link UIComponent}.</p>
 *
 * <p>By default, this class defines <code>getRendersChildren()</code>
 * to find the renderer for this component and call its
 * <code>getRendersChildren()</code> method.  The default implementation
 * on the <code>Renderer</code> returns <code>false</code>.  As of
 * version 1.2 of the JavaServer Faces Specification, component authors
 * are encouraged to return <code>true</code> from this method and rely
 * on the implementation of {@link #encodeChildren} in this class and in
 * the Renderer ({@link Renderer#encodeChildren}).  Subclasses that wish
 * to manage the rendering of their children should override this method
 * to return <code>true</code> instead.</p>
 */

public abstract class UIComponentBase extends UIComponent {


    // -------------------------------------------------------------- Attributes

    private static Logger log = Logger.getLogger("javax.faces.component");


    /**
     * <p>Each entry is an map of <code>PropertyDescriptor</code>s describing
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
     * Reference to the map of <code>PropertyDescriptor</code>s for this class
     * in the <code>descriptors<code> <code>Map<code>.
     */
    private Map pdMap = null; 

    /**
     * <p>An empty argument list to be passed to reflection methods.</p>
     */
    private static Object empty[] = new Object[0];

    public UIComponentBase() {
        synchronized(descriptors) {
            pdMap = (Map) descriptors.get(this.getClass());
            if (pdMap != null) {
                return;
            } 
            // load the property descriptors for this class.
            PropertyDescriptor pd[] = getPropertyDescriptors();
            if (pd != null) {
                pdMap = new HashMap(pd.length);
                for (int i = 0; i < pd.length; i++) {
                    pdMap.put(pd[i].getName(), pd[i]);    
                }
                descriptors.put(this.getClass(),pdMap);
            }
        }
    }

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
        if (pdMap != null) {
            return ((PropertyDescriptor)pdMap.get(name));
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
        PropertyDescriptor[] pd = null;
        try {
            pd = Introspector.getBeanInfo(this.getClass()).
                getPropertyDescriptors();
        } catch (IntrospectionException e) {
            throw new FacesException(e);
        }
        return (pd);
    }


    /**
     * <p>The <code>Map</code> containing our attributes, keyed by
     * attribute name.</p>
     */
    private Map attributes = null;


    public Map getAttributes() {

        if (attributes == null) {
            attributes = new AttributesMap();
        }
        return (attributes);

    }


    // ---------------------------------------------------------------- Bindings


    // The set of ValueExpressions for this component, keyed by property
    // name This collection is lazily instantiated
    private Map bindings = null;


    /**
     * {@inheritDoc}
     * @exception NullPointerException {@inheritDoc}
     * @deprecated This has been replaced by {@link #getValueExpression}.
     */ 
    public ValueBinding getValueBinding(String name) {

	if (name == null) {
	    throw new NullPointerException();
	}
	ValueBinding result = null;
	ValueExpression ve = null;

	if (null != (ve = getValueExpression(name))) {
	    // if the ValueExpression is an instance of our private
	    // wrapper class.
	    if (ve.getClass() == ValueExpressionValueBindingAdapter.class) {
		result = ((ValueExpressionValueBindingAdapter)ve).getWrapped();
	    }
	    else {
		// otherwise, this is a real ValueExpression.  Wrap it
		// in a ValueBinding.
		result = new ValueBindingValueExpressionAdapter(ve);
	    }
	}
	return result;
    }


    /**
     * {@inheritDoc}
     * @exception IllegalArgumentException {@inheritDoc}
     * @exception NullPointerException {@inheritDoc}
     * @deprecated This has been replaced by {@link #setValueExpression}.
     */ 
    public void setValueBinding(String name, ValueBinding binding) {
	if (name == null) {
	    throw new NullPointerException();
	} 
	if (binding != null) {
	    ValueExpressionValueBindingAdapter adapter = 
		new ValueExpressionValueBindingAdapter(binding);
	    setValueExpression(name, adapter);
	} else {
	    setValueExpression(name, null);
	}

    }

    /**
     * {@inheritDoc}
     * @since 1.2
     * @exception NullPointerException {@inheritDoc}
     */ 
    public ValueExpression getValueExpression(String name) {

        if (name == null) {
            throw new NullPointerException();
        }
        if (bindings == null) {
            return (null);
        } else {
            return ((ValueExpression) bindings.get(name));
        }

    }

    /**
     * {@inheritDoc}
     * @since 1.2
     * @exception IllegalArgumentException {@inheritDoc}
     * @exception NullPointerException {@inheritDoc}
     */ 
    public void setValueExpression(String name, ValueExpression binding) {

        if (name == null) {
            throw new NullPointerException();
        } else if ("id".equals(name) || "parent".equals(name)) {
            throw new IllegalArgumentException();
        }
        if (binding != null) {
            if (!binding.isLiteralText()) {
                if (bindings == null) {
                    bindings = new HashMap();
                }
                bindings.put(name, binding);
            } else {
                ELContext context =
                    FacesContext.getCurrentInstance().getELContext();
                try {
                    getAttributes().put(name, binding.getValue(context));
                } catch (ELException ele) {
                    throw new FacesException(ele);
                }
            }
        } else {
            if (bindings != null) {
                bindings.remove(name);
                if (bindings.size() == 0) {
                    bindings = null;
                }
            }
        }

    }
    
    // -------------------------------------------------------------- Properties


    /**
     * <p>The assigned client identifier for this component.</p>
     */
    private String clientId = null;

    
    /**
     * @exception NullPointerException {@inheritDoc}
     */ 
    public String getClientId(FacesContext context) {

        if (context == null) {
            throw new NullPointerException();
        }

        // if the clientId is not yet set
        if (this.clientId == null) {
            UIComponent parent = this.getNamingContainer();
            String parentId = null;
            
            // give the parent the opportunity to first
            // grab a unique clientId
            if (parent != null) {
                parentId = parent.getContainerClientId(context);
            }
            
            // now resolve our own client id
            this.clientId = this.id;
            if (this.clientId == null) {
                this.clientId = context.getViewRoot().createUniqueId();
            }
            if (parentId != null) {
                this.clientId = parentId + NamingContainer.SEPARATOR_CHAR + this.clientId;
            }
            
            // allow the renderer to convert the clientId
            Renderer renderer = this.getRenderer(context);
            if (renderer != null) {
                this.clientId = renderer.convertClientId(context, this.clientId);
            }
        }
        return this.clientId;
    }
    
    /**
     * @exception NullPointerException {@inheritDoc}
     */
    public String getContainerClientId(FacesContext context) {
        if (context == null) {
            throw new NullPointerException();
        }
        return this.getClientId(context);
    }
    
    /**
     * <p>Private utilitity method for finding this
     * <code>UIComponent</code>'s parent <code>NamingContainer</code>.
     * This method may return <code>null</code> if there is not a
     * parent <code>NamingContainer</code></p>
     * 
     * @return the parent <code>NamingContainer</code>
     */
    private UIComponent getNamingContainer() {
        UIComponent namingContainer = this.getParent();
        while (namingContainer != null) {
            if (namingContainer instanceof NamingContainer) {
                return namingContainer;
            }
            namingContainer = namingContainer.getParent();
        }
        return null;
    }



    /**
     * <p>The component identifier for this component.</p>
     */
    private String id = null;


    public String getId() {

	    return (id);

    }


    /**
     * @exception IllegalArgumentException {@inheritDoc}
     * @exception IllegalStateException {@inheritDoc}    
     */ 
    public void setId(String id) {
        
        validateId(id);
        this.id = id;
        this.clientId = null; // Erase any cached value

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
    private boolean renderedSet = false;

    public boolean isRendered() {

	if (renderedSet) {
	    return (rendered);
	}
	ValueExpression ve = getValueExpression("rendered");
	if (ve != null) {
	    boolean result = false;
	    try {
		result = !Boolean.FALSE.equals(ve.getValue(getFacesContext().getELContext()));
	    }
	    catch (ELException e) {
		throw new FacesException(e);
	    }
	    return result;
	} else {
	    return (this.rendered);
	}

    }
    

    public void setRendered(boolean rendered) {

        this.rendered = rendered;
	this.renderedSet = true;

    }


    /**
     * <p>The renderer type for this component.</p>
     */
    private String rendererType = null;


    public String getRendererType() {

	if (this.rendererType != null) {
	    return (this.rendererType);
	}
	ValueExpression ve = getValueExpression("rendererType");
	if (ve != null) {
	    String result = null;
	    try {
		result = (String)ve.getValue(getFacesContext().getELContext());
	    }
	    catch (ELException e) {
		throw new FacesException(e);
	    }
	    return result;
	} else {
	    return (null);
	}

    }


    public void setRendererType(String rendererType) {

        this.rendererType = rendererType;

    }


    public boolean getRendersChildren() {
        boolean result = false;

        Renderer renderer = null;
        if (getRendererType() != null) {
            if (null != 
                (renderer = getRenderer(getFacesContext()))) {
                result = renderer.getRendersChildren();
            }
        }
        return result;

    }


    // ------------------------------------------------- Tree Management Methods


    /*
     * <p>The <code>List</code> containing our child components.</p>
     */
    private List children = null;


    public List getChildren() {

        if (children == null) {
            children = new ChildrenList();
        }
        return (children);

    }


    // Do not allocate the children List to answer this question
    public int getChildCount() {

        if (children != null) {
            return (children.size());
        } else {
            return (0);
        }

    }


    /**
     * <p>If the specified {@link UIComponent} has a non-null parent,
     * remove it as a child or facet (as appropriate) of that parent.
     * As a result, the <code>parent</code> property will always be
     * <code>null</code> when this method returns.</p>
     *
     * @param component {@link UIComponent} to have any parent erased
     */
    private void eraseParent(UIComponent component) {

        UIComponent parent = component.getParent();
        if (parent == null) {
            return;
        }
        if (parent.getChildCount() > 0) {
            List children = parent.getChildren();
            int index = children.indexOf(component);
            if (index >= 0) {
                children.remove(index);
                return;
            }
        }
        if (parent.getFacetCount() > 0) {
            Map facets = parent.getFacets();
            Iterator entries = facets.entrySet().iterator();
            while (entries.hasNext()) {
                Map.Entry entry = (Map.Entry) entries.next();
                if (entry.getValue() == component) {
                    entries.remove();
                    return;
                }
            }
        }

        // Throw an exception for the "cannot happen" case
        throw new IllegalStateException("Parent was not null, " +
                                        "but this component not related");

    }

    /**
     * <p>Throw <code>IllegalArgumentException</code> if the specified
     * component identifier is non-<code>null</code> and not
     * syntactically valid.  </p>
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
                if (!Character.isLetter(c) && (c != '_')) {
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


    private static String separatorString =
        "" + NamingContainer.SEPARATOR_CHAR;


    /**
     * @exception NullPointerException {@inheritDoc}
     */ 
    public UIComponent findComponent(String expr) {

        if (expr == null) {
            throw new NullPointerException();
        }

        // Identify the base component from which we will perform our search
        UIComponent base = this;
        if (expr.startsWith(separatorString)) {
            // Absolute searches start at the root of the tree
            while (base.getParent() != null) {
                base = base.getParent();
            }
            // Treat remainder of the expression as relative
            expr = expr.substring(1);
        } else {
            // Relative expressions start at the closest NamingContainer or root
            while (base.getParent() != null) {
                if (base instanceof NamingContainer) {
                    break;
                }
                base = base.getParent();
            }
        }

        // Evaluate the search expression (now guaranteed to be relative)
        String id = null;
        UIComponent result = null;
        while (expr.length() > 0) {
            int separator = expr.indexOf(NamingContainer.SEPARATOR_CHAR);
            if (separator >= 0) {
                id = expr.substring(0, separator);
                expr = expr.substring(separator + 1);
            } else {
                id = expr;
                expr = "";
            }
            result = findComponent(base, id);
            if ((result == null) || (expr.length() == 0)) {
                break; // Missing intermediate node or this is the last node
            }
            if (result instanceof NamingContainer) {
                result = result.findComponent(expr);
                break;
            } else {
                throw new IllegalArgumentException(id);
            }
        }

        // Return the final result of our search
        return (result);

    }

    
    /**
     * <p>Return the {@link UIComponent} (if any) with the specified
     * <code>id</code>, searching recursively starting at the specified
     * <code>base</code>, and examining the base component itself, followed
     * by examining all the base component's facets and children (unless
     * the base component is a {@link NamingContainer}, in which case the
     * recursive scan is skipped.</p>
     *
     * @param base Base {@link UIComponent} from which to search
     * @param id Component identifier to be matched
     */
    private UIComponent findComponent(UIComponent base, String id) {

        // Is the "base" component itself the match we are looking for?
        if (id.equals(base.getId())) {
            return (base);
        }

        // Search through our facets and children
        UIComponent kid = null;
        UIComponent result = null;
        Iterator kids = base.getFacetsAndChildren();
        while (kids.hasNext() && (result == null)) {
            kid = (UIComponent) kids.next();
            if (!(kid instanceof NamingContainer)) {
                result = findComponent(kid, id);
                if (result != null) {
                    break;
                }
            } else if (id.equals(kid.getId())) {
                result = kid;
                break;
            }
        }
        return (result);

    }


    // ------------------------------------------------ Facet Management Methods


    /*
     * <p>The <code>Map</code> containing our related facet components.</p>
     */
    private Map facets = null;


    public Map getFacets() {

        if (facets == null) {
            facets = new FacetsMap();
        }
        return (facets);

    }
    
    // Do not allocate the children List to answer this question
    public int getFacetCount() {

        if (facets != null) {
            return (facets.size());
        } else {
            return (0);
        }

    }


    // Do not allocate the facets Map to answer this question
    public UIComponent getFacet(String name) {

        if (facets != null) {
            return ((UIComponent) facets.get(name));
        } else {
            return (null);
        }

    }


    public Iterator getFacetsAndChildren() {
        
        if (this.getChildCount() == 0 && this.getFacetCount() == 0) {
            return EMPTY_ITERATOR;
        }
        return new FacetsAndChildrenIterator(this);
    }


    // -------------------------------------------- Lifecycle Processing Methods

    /**
     * @exception AbortProcessingException {@inheritDoc}
     * @exception IllegalStateException {@inheritDoc}
     * @exception NullPointerException {@inheritDoc}  
     */ 
    public void broadcast(FacesEvent event)
        throws AbortProcessingException {

        if (event == null) {
            throw new NullPointerException();
        }
        if (listeners == null) {
            return;
        }

        Iterator iter = listeners.iterator();
        while (iter.hasNext()) {
            FacesListener listener = (FacesListener) iter.next();
            if (event.isAppropriateListener(listener)) {
                event.processListener(listener);
            }
        }
    }


    /**
     * @exception NullPointerException {@inheritDoc}     
     */ 
    public void decode(FacesContext context) {

        if (context == null) {
            throw new NullPointerException();
        }
        String rendererType = getRendererType();
        if (rendererType != null) {
            getRenderer(context).decode(context, this);
        }
    }


    /**
     * @exception NullPointerException {@inheritDoc}   
     */ 
    public void encodeBegin(FacesContext context) throws IOException {

        if (context == null) {
            throw new NullPointerException();
        }
        if (!isRendered()) {
            return;
        }
        String rendererType = getRendererType();
        if (rendererType != null) {
            getRenderer(context).encodeBegin(context, this);
        }

    }

    /**
     * @exception NullPointerException {@inheritDoc}     
     */ 
    public void encodeChildren(FacesContext context) throws IOException {

        if (context == null) {
            throw new NullPointerException();
        }
        if (!isRendered()) {
            return;
        }
        String rendererType = getRendererType();
        if (rendererType != null) {
            getRenderer(context).encodeChildren(context, this);
        }

    }


    /**
     * @exception IOException {@inheritDoc}   
     * @exception NullPointerException {@inheritDoc}   
     */ 
    public void encodeEnd(FacesContext context) throws IOException {

        if (context == null) {
            throw new NullPointerException();
        }
        if (!isRendered()) {
            return;
        }
        String rendererType = getRendererType();
        if (rendererType != null) {
            getRenderer(context).encodeEnd(context, this);
        }

    }

    /**
     * @exception IOException {@inheritDoc}   
     * @exception NullPointerException {@inheritDoc}   
     */
    
    public void encodeAll(FacesContext context) throws IOException {
	if (!isRendered()) {
	    return;
	}

	encodeBegin(context);
	if (getRendersChildren()) {
	    encodeChildren(context);
	}
	else if (this.getChildCount() > 0) {
	    Iterator kids = getChildren().iterator();
	    while (kids.hasNext()) {
    		UIComponent kid = (UIComponent) kids.next();
    		kid.encodeAll(context);
	    }
	}
	
	encodeEnd(context);
    }

    // -------------------------------------------------- Event Listener Methods


    /**
     * <p>Our {@link javax.faces.event.FacesListener}s.  This data
     * structure is lazily instantiated as necessary.</p>
     */
    private List listeners;


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
     * @exception NullPointerException if <code>listener</code>
     *  is <code>null</code>
     */
    protected void addFacesListener(FacesListener listener) {

        if (listener == null) {
            throw new NullPointerException();
        }
        if (listeners == null) {
            listeners = new ArrayList();
        }
        listeners.add(listener);

    }


    /**
     * @exception IllegalArgumentException {@inheritDoc}
     * @exception NullPointerException {@inheritDoc}  
     */ 
    protected FacesListener[] getFacesListeners(Class clazz) {
        if (clazz == null) {
            throw new NullPointerException();
        }
        if (!FacesListener.class.isAssignableFrom(clazz)) {
            throw new IllegalArgumentException();
        }
        if (listeners == null) {
            return ((FacesListener[]) 
                java.lang.reflect.Array.newInstance(clazz, 0));
        }

        List results = new ArrayList();
	Iterator items = listeners.iterator();
	while (items.hasNext()) {
	    FacesListener item = (FacesListener) items.next();
	    if (clazz.isAssignableFrom(item.getClass())) {
		results.add(item);
	    }
	}
	
        return ((FacesListener[]) results.toArray
                ((Object []) java.lang.reflect.Array.newInstance(clazz, 
								 results.size())));

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
	listeners.remove(listener);
    }

    /**
     * @exception IllegalStateException {@inheritDoc}
     * @exception NullPointerException {@inheritDoc}  
     */ 
    public void queueEvent(FacesEvent event) {

        if (event == null) {
            throw new NullPointerException();
        }
        UIComponent parent = getParent();
        if (parent == null) {
            throw new IllegalStateException();
        } else {
            parent.queueEvent(event);
        }

    }


    // ------------------------------------------------ Lifecycle Phase Handlers


    /**
     * @exception NullPointerException {@inheritDoc}     
     */ 
    public void processDecodes(FacesContext context) {

        if (context == null) {
            throw new NullPointerException();
        }

        // Skip processing if our rendered flag is false
        if (!isRendered()) {
            return;
        }

        // Process all facets and children of this component
        Iterator kids = getFacetsAndChildren();
        while (kids.hasNext()) {
            UIComponent kid = (UIComponent) kids.next();
            kid.processDecodes(context);
        }

        // Process this component itself
        try {
            decode(context);
        } catch (RuntimeException e) {
            context.renderResponse();
            throw e;
        }

    }


    /**
     * @exception NullPointerException {@inheritDoc}    
     */ 
    public void processValidators(FacesContext context) {

        if (context == null) {
            throw new NullPointerException();
        }

        // Skip processing if our rendered flag is false
        if (!isRendered()) {
            return;
        }

        // Process all the facets and children of this component
        Iterator kids = getFacetsAndChildren();
        while (kids.hasNext()) {
            UIComponent kid = (UIComponent) kids.next();
            kid.processValidators(context);
        }
    }


    /**
     * @exception NullPointerException {@inheritDoc}     
     */ 
    public void processUpdates(FacesContext context) {

        if (context == null) {
            throw new NullPointerException();
        }

        // Skip processing if our rendered flag is false
        if (!isRendered()) {
            return;
        }

        // Process all facets and children of this component
        Iterator kids = getFacetsAndChildren();
        while (kids.hasNext()) {
            UIComponent kid = (UIComponent) kids.next();
            kid.processUpdates(context);
        }
    }

    private static final int MY_STATE = 0;
    private static final int CHILD_STATE = 1;

    /**
     * @exception NullPointerException {@inheritDoc}         
     */ 
    public Object processSaveState(FacesContext context) {
        
        if (context == null) {
            throw new NullPointerException();
        }
        if (this.isTransient()) {
            return null;
        }
        Object [] stateStruct = new Object[2];
        Object [] childState = EMPTY_ARRAY;
        
        // Process this component itself
        stateStruct[MY_STATE] = saveState(context);
        
        // determine if we have any children to store
        int count = this.getChildCount() + this.getFacetCount();
        if (count > 0) {
            
            // this arraylist will store state
            List stateList = new ArrayList(count);
            
            // if we have children, add them to the stateList
            if (this.getChildCount() > 0) {
                Iterator kids = getChildren().iterator();
                UIComponent kid;
                while (kids.hasNext()) {
                    kid = (UIComponent) kids.next();
                    if (!kid.isTransient()) {
                        stateList.add(kid.processSaveState(context));
                    }
                }
            }
            
            // if we have facets, add them to the stateList
            if (this.getFacetCount() > 0) {
                Iterator myFacets = getFacets().entrySet().iterator();
                UIComponent facet = null;
                Object facetState = null;
                Object[] facetSaveState = null;
                Map.Entry entry = null;
                while (myFacets.hasNext()) {
                    entry = (Map.Entry) myFacets.next();
                    facet = (UIComponent) entry.getValue();
                    if (!facet.isTransient()) {
                        facetState = facet.processSaveState(context);
                        facetSaveState = new Object[2];
                        facetSaveState[0] = entry.getKey();
                        facetSaveState[1] = facetState;
                        stateList.add(facetSaveState);
                    }
                }
            }
            
            // finally, capture the stateList and replace the original,
            // empty Object array
            childState = stateList.toArray();
        }
        
        stateStruct[CHILD_STATE] = childState;
        return stateStruct;
    }
    
    /**
     * @exception NullPointerException {@inheritDoc}
     */
    public void processRestoreState(FacesContext context,
                                    Object state) {
        if (context == null) {
            throw new NullPointerException();
        }
        
        Object [] stateStruct = (Object []) state;
        Object [] childState = (Object []) stateStruct[CHILD_STATE];
        
        // Process this component itself
        restoreState(context, stateStruct[MY_STATE]);
        
        int i = 0;
        
        // Process all the children of this component
        if (this.getChildCount() > 0) {
            Iterator kids = getChildren().iterator();
            while (kids.hasNext()) {
                UIComponent kid = (UIComponent) kids.next();
    	    Object currentState = childState[i++];
    	    if (currentState == null) {
    	        continue;
    	    }
                kid.processRestoreState(context, currentState);
            }
        }
        
        // process all of the facets of this component
        if (this.getFacetCount() > 0) {
            int facetsSize = getFacets().size();
            int j = 0;
            Object[] facetSaveState = null;
            String facetName = null;
            UIComponent facet = null;
            Object facetState = null;
            while (j < facetsSize) {
                if (null != (facetSaveState = (Object[])childState[i++])) {
                    facetName = (String) facetSaveState[0];
                    facetState = facetSaveState[1];
                    facet = (UIComponent) getFacets().get(facetName);
                    facet.processRestoreState(context, facetState);
                }
                ++j;
            }
        }
    }
    
    // ------------------------------------------------------- Protected Methods


    protected FacesContext getFacesContext() {

	// PENDING(edburns): we can't use the cache ivar because we
	// don't always know when to clear it.  For example, in the
	// "save state in server" case, the UIComponent instances stick
	// around between requests, yielding stale facesContext
	// references.  If there was some way to clear the facesContext
	// cache ivar for each node in the tree *after* the
	// render-response phase, then we could keep a cache ivar.  As
	// it is now, we must always use the Thread Local Storage
	// solution.

	return FacesContext.getCurrentInstance();

    }


    protected Renderer getRenderer(FacesContext context) {

        String rendererType = getRendererType();
	Renderer result = null;
        if (rendererType != null) {
            RenderKitFactory rkFactory = (RenderKitFactory)
                FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
            RenderKit renderKit = rkFactory.getRenderKit
                (context, context.getViewRoot().getRenderKitId());
	    result = (renderKit.getRenderer(getFamily(), rendererType));
	    if (null == result) {
		if (log.isLoggable(Level.FINE)) {
		    // PENDING(edburns): I18N
		    log.fine("Can't get Renderer for type " + rendererType);
		}
	    }
        } else {
	    if (log.isLoggable(Level.FINE)) {
		String id = this.getId();
		id = (null != id) ? id : this.getClass().getName();
		// PENDING(edburns): I18N
		log.fine("No renderer-type for component " + id);
	    }
	}
	return result;
    }


    // ----------------------------------------------------- StateHolder Methods


    public Object saveState(FacesContext context) {

        Object values[] = new Object[8];
        // copy over "attributes" to a temporary map, so that
        // any references maintained due to "attributes" being an inner class
        // is not saved.
        if ( attributes != null ) {
            HashMap attributesCopy = new HashMap(attributes);
            values[0] = attributesCopy;
        }
        values[1] = saveBindingsState(context);
        values[2] = clientId;
        values[3] = id;
        values[4] = rendered ? Boolean.TRUE : Boolean.FALSE;
        values[5] = renderedSet ? Boolean.TRUE : Boolean.FALSE;
        values[6] = rendererType;
        values[7] = saveAttachedState(context, listeners);
        assert(!transientFlag);
        
        return (values);
    }


    public void restoreState(FacesContext context, Object state) {

        Object values[] = (Object[]) state;
        // we need to get the map that knows how to handle attribute/property 
        // transparency before we restore its values.
        attributes = getAttributes();
        if ( values[0] != null ) {
            HashMap attributesCopy = (HashMap)values[0];
            for (Iterator i = attributesCopy.entrySet().iterator();
                 i.hasNext(); ) {
                Map.Entry entry = (Map.Entry) i.next();
                attributes.put(entry.getKey(), entry.getValue());
            }
        }
	bindings = restoreBindingsState(context, values[1]);
        clientId = (String) values[2];
        id = (String) values[3];
        rendered = ((Boolean) values[4]).booleanValue();
        renderedSet = ((Boolean) values[5]).booleanValue();
        rendererType = (String) values[6];
        List restoredListeners = null;
        if (null != (restoredListeners = (List)
                     restoreAttachedState(context, values[7]))) {
            // if there were some listeners registered prior to this
            // method being invoked, merge them with the list to be
            // restored.
            if (null != listeners) {
		listeners.addAll(restoredListeners);
            }
            else {
                listeners = restoredListeners;
            }
        }
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

    // -------------------------------------- Helper methods for state saving

    // --------- methods used by UIComponents to save their attached Objects.

    /**
     *
     * <p>This method is called by {@link UIComponent} subclasses that
     * want to save one or more attached objects.  It is a convenience
     * method that does the work of saving attached objects that may or
     * may not implement the {@link StateHolder} interface.  Using this
     * method implies the use of {@link #restoreAttachedState} to restore
     * the attached objects.</p>
     *
     * <p>This method supports saving  attached objects of the following
     * type: <code>Object</code>s,
     * <code>null</code> values, and <code>Lists</code> of these
     * objects.  If any contained objects are not <code>Lists</code>
     * and do not implement {@link StateHolder}, they must have
     * zero-argument public constructors.  The exact structure of the
     * returned object is undefined and opaque, but will be serializable.
     * </p>
     *
     * @param context the {@link FacesContext} for this request.
     *
     * @param attachedObject the object, which may be a
     * <code>List</code> instance, or an Object.  The
     * <code>attachedObject</code> (or the elements that comprise
     * <code>attachedObject</code> may implement {@link StateHolder}.
     *
     * @exception NullPointerException if the context argument is null.
     *
     */

    public static Object saveAttachedState(FacesContext context,
                                           Object attachedObject) {
        if (null == context) {
            throw new NullPointerException();
        }
        if (null == attachedObject) {
            return null;
        }
        Object result = null;
        List
            attachedList = null,
            resultList = null;
        Iterator listIter = null;

        if (attachedObject instanceof List) {
            attachedList = (List) attachedObject;
            resultList = new ArrayList(attachedList.size());
            listIter = attachedList.iterator();
	    Object cur = null;
            while (listIter.hasNext()) {
		if (null != (cur = listIter.next())) {
		    resultList.add(new StateHolderSaver(context, cur));
		}
            }
            result = resultList;
        }
        else {
            result = new StateHolderSaver(context, attachedObject);
        }

        return result;
    }
    
    /**
     *
     * <p>This method is called by {@link UIComponent} subclasses that
     * need to restore the objects they saved using {@link
     * #saveAttachedState}.  This method is tightly coupled with {@link
     * #saveAttachedState}.</p>
     *
     * <p>This method supports restoring all attached objects types
     * supported by {@link #saveAttachedState}.</p>
     *
     * @param context the {@link FacesContext} for this request
     *
     * @param stateObj the opaque object returned from {@link
     * #saveAttachedState}
     *
     * @exception NullPointerException if context is null.
     *
     * @exception IllegalStateException if the object is not 
     *   previously returned by {@link #saveAttachedState}.
     *
     */
    
    public static Object restoreAttachedState(FacesContext context,
                                              Object stateObj) throws IllegalStateException {
        if (null == context) {
            throw new NullPointerException();
        }
        if (null == stateObj) {
            return null;
        }
        Object result = null;
        List 
            stateList = null,
            resultList = null;
        Iterator iter = null;
        StateHolderSaver saver = null;

	if (stateObj instanceof List) {
            stateList = (List) stateObj;
            resultList = new ArrayList(stateList.size());
            iter = stateList.iterator();
            while (iter.hasNext()) {
                try {
                    saver = (StateHolderSaver) iter.next();
                }
                catch (ClassCastException cce) {
                    throw new IllegalStateException("Unknown object type");
                }
                resultList.add(saver.restore(context));
            }
            result = resultList;
        }
        else if (stateObj instanceof StateHolderSaver) {
            saver = (StateHolderSaver) stateObj;
            result = saver.restore(context);
        }
        else {
            throw new IllegalStateException("Unknown object type");
        }
        return result;
    }

    private Map restoreBindingsState(FacesContext context, Object state) {

	if (state == null) {
	    return (null);
	}
	Object values[] = (Object[]) state;
	String names[] = (String[]) values[0];
	Object states[] = (Object[]) values[1];
	Map bindings = new HashMap();
	for (int i = 0; i < names.length; i++) {
	    bindings.put(names[i],
			 restoreAttachedState(context, states[i]));
	}
	return (bindings);

    }


    private Object saveBindingsState(FacesContext context) {

	if (bindings == null) {
	    return (null);
	}
	List names = new ArrayList();
	List states = new ArrayList();
	Iterator keys = bindings.keySet().iterator();
	while (keys.hasNext()) {
	    String key = (String) keys.next();
	    ValueExpression binding = (ValueExpression) bindings.get(key);
	    names.add(key);
	    states.add(saveAttachedState(context, binding));
	}
	Object values[] = new Object[2];
	values[0] = names.toArray(new String[names.size()]);
	values[1] = states.toArray(new Object[states.size()]);
	return (values);

    }


    // --------------------------------------------------------- Private Classes

    // For state saving
    private final static Object[] EMPTY_ARRAY = new Object[0];
    
    // Empty iterator for short circuiting operations
    private final static Iterator EMPTY_ITERATOR = new Iterator() {
    
        public void remove() {
            throw new UnsupportedOperationException();
        }
    
        public Object next() {
            throw new NoSuchElementException("Empty Iterator");
        }
    
        public boolean hasNext() {
            return false;
        }
    };

    // Private implementation of Map that supports the functionality
    // required by UIComponent.getFacets()
    private class AttributesMap extends HashMap {

        public boolean containsKey(Object key) {
            PropertyDescriptor pd =
                getPropertyDescriptor((String) key);
            if (pd == null) {
                return (super.containsKey(key));
            } else {
                return (false);
            }
        }

        public Object get(Object key) {
            if (key == null) {
                throw new NullPointerException();
            }
            String name = (String) key;
            PropertyDescriptor pd =
                getPropertyDescriptor(name);
            if (pd != null) {
                try {
                    Method readMethod = pd.getReadMethod();
                    if (readMethod != null) {
                        return (readMethod.invoke
                                (UIComponentBase.this, empty));
                    } else {
                        throw new IllegalArgumentException(name);
                    }
                } catch (IllegalAccessException e) {
                    throw new FacesException(e);
                } catch (InvocationTargetException e) {
                    throw new FacesException
                        (e.getTargetException());
                }
            } else if (super.containsKey(name)) {
                return (super.get(key));
            }
            ValueExpression ve = getValueExpression(name);
            if (ve != null) {
		Object result = null;
		try {
		    result = ve.getValue(getFacesContext().getELContext());
		    return result;
		}
		catch (ELException e) {
		    throw new FacesException(e);
		}
            }
            return (null);
        }

        public Object put(Object key, Object value) {
            if (key == null) {
                throw new NullPointerException();
            }
            String name = (String) key;
            PropertyDescriptor pd =
                getPropertyDescriptor(name);
            if (pd != null) {
                try {
                    Object result = null;
                    Method readMethod = pd.getReadMethod();
                    if (readMethod != null) {
                        result = readMethod.invoke
                            (UIComponentBase.this, empty);
                    }
                    Method writeMethod = pd.getWriteMethod();
                    if (writeMethod != null) {
                        writeMethod.invoke
                            (UIComponentBase.this,
                             new Object[] { value });
                    } else {
                        throw new IllegalArgumentException();
                    }
                    return (result);
                } catch (IllegalAccessException e) {
                    throw new FacesException(e);
                } catch (InvocationTargetException e) {
                    throw new FacesException
                        (e.getTargetException());
                }
            } else {
                if (value == null) {
                    throw new NullPointerException();
                }
                return (super.put(key, value));
            }
        }

        public void putAll(Map map) {
            if (map == null) {
                throw new NullPointerException();
            }
            for (Iterator i = map.entrySet().iterator(); i.hasNext(); ) {
                Map.Entry entry = (Map.Entry) i.next();
                put(entry.getKey(), entry.getValue());
            }
        }

        public Object remove(Object key) {
            if (key == null) {
                throw new NullPointerException();
            }
            String name = (String) key;
            PropertyDescriptor pd =
                getPropertyDescriptor(name);
            if (pd != null) {
                throw new IllegalArgumentException(name);
            } else {
                return (super.remove(key));
            }
        }

    }


    // Private implementation of List that supports the functionality
    // required by UIComponent.getChildren()
    private class ChildrenList extends ArrayList {

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
                eraseParent(child);
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
                eraseParent(child);
                child.setParent(UIComponentBase.this);
                return (super.add(element));
            }
        }

        public boolean addAll(Collection collection) {
            Iterator elements = (new ArrayList(collection)).iterator();
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
            Iterator elements = (new ArrayList(collection)).iterator();
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
            for (int i = 0; i < n; i++) {
                UIComponent child = (UIComponent) get(i);
                child.setParent(null);
            }
            super.clear();
        }

        public Iterator iterator() {
            return (new ChildrenListIterator(this));
        }

        public ListIterator listIterator() {
            return (new ChildrenListIterator(this));
        }

        public ListIterator listIterator(int index) {
            return (new ChildrenListIterator(this, index));
        }

        public Object remove(int index) {
            UIComponent child = (UIComponent) get(index);
            super.remove(index);
            child.setParent(null);
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
            boolean modified = false;
            Iterator items = iterator();
            while (items.hasNext()) {
                if (!collection.contains(items.next())) {
                    items.remove();
                    modified = true;
                }
            }
            return (modified);
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
                eraseParent(child);
                UIComponent previous =
                    (UIComponent) get(index);
                previous.setParent(null);
                child.setParent(UIComponentBase.this);
                super.set(index, element);
                return (previous);
            }
        }

    }


    // Private implementation of ListIterator for ChildrenList
    private static class ChildrenListIterator implements ListIterator {


        public ChildrenListIterator(ChildrenList list) {
            this.list = list;
            this.index = 0;
        }

        public ChildrenListIterator(ChildrenList list, int index) {
            this.list = list;
            if ((index < 0) || (index >= list.size())) {
                throw new IndexOutOfBoundsException("" + index);
            } else {
                this.index = index;
            }
        }


        private ChildrenList list;
        private int index;
        private int last = -1; // Index last returned by next() or previous()

        // Iterator methods

        public boolean hasNext() {
            return (index < list.size());
        }

        public Object next() {
            try {
                Object o = list.get(index);
                last = index++;
                return (o);
            } catch (IndexOutOfBoundsException e) {
                throw new NoSuchElementException("" + index);
            }
        }

        public void remove() {
            if (last == -1) {
                throw new IllegalStateException();
            }
            list.remove(last);
            if (last < index) {
                index--;
            }
            last = -1;
        }

        // ListIterator methods

        public void add(Object o) {
            last = -1;
            list.add(index++, o);
        }

        public boolean hasPrevious() {
            return (index > 1);
        }

        public int nextIndex() {
            return (index);
        }

        public Object previous() {
            try {
                int current = index - 1;
                Object o = list.get(current);
                last = current;
                index = current;
                return (o);
            } catch (IndexOutOfBoundsException e) {
                throw new NoSuchElementException();
            }
        }

        public int previousIndex() {
            return (index - 1);
        }

        public void set(Object o) {
            if (last == -1) {
                throw new IllegalStateException();
            }
            list.set(last, o);
        }

    }


    // Private implementation of Iterator for getFacetsAndChildren()
    private final static class FacetsAndChildrenIterator implements Iterator {
        
        private Iterator iterator;
        private boolean childMode;
        private UIComponent c;

        public FacetsAndChildrenIterator(UIComponent c) {
            this.c = c;
            this.childMode = false;
        }
        
        private void update() {
            if (this.iterator == null) {
                // we must guarantee that 'iterator' is never null
                if (this.c.getFacetCount() != 0) {
                    this.iterator = this.c.getFacets().values().iterator();
                    this.childMode = false;
                } else if (this.c.getChildCount() != 0) {
                    this.iterator = this.c.getChildren().iterator();
                    this.childMode = true;
                } else {
                    this.iterator = EMPTY_ITERATOR;
                    this.childMode = true;
                }
            } else if (!this.childMode
                    && !this.iterator.hasNext()
                    && this.c.getChildCount() != 0) {
                this.iterator = this.c.getChildren().iterator();
                this.childMode = true;
            }
        }

        public boolean hasNext() {
            this.update();
            return this.iterator.hasNext();
        }

        public Object next() {
            this.update();
            return this.iterator.next();
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

    }


    // Private implementation of Map that supports the functionality
    // required by UIComponent.getFacets()
    private class FacetsMap extends HashMap {

        public void clear() {
            Iterator keys = keySet().iterator();
            while (keys.hasNext()) {
                String key = (String) keys.next();
                keys.remove();
            }
            super.clear();
        }

        public Set entrySet() {
            return (new FacetsMapEntrySet(this));
        }

        public Set keySet() {
            return (new FacetsMapKeySet(this));
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
            eraseParent(current);
            current.setParent(UIComponentBase.this);
            return (super.put(key, value));
        }

        public void putAll(Map map) {
            if (map == null) {
                throw new NullPointerException();
            }            
            for (Iterator i = map.entrySet().iterator(); i.hasNext(); ) {
                Map.Entry entry = (Map.Entry) i.next();
                put(entry.getKey(), entry.getValue());
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
            return (new FacetsMapValues(this));
        }

        Iterator keySetIterator() {
            return ((new ArrayList(super.keySet())).iterator());
        }

    }


    // Private implementation of Set for FacetsMap.getEntrySet()
    private static class FacetsMapEntrySet extends AbstractSet {

        public FacetsMapEntrySet(FacetsMap map) {
            this.map = map;
        }

        private FacetsMap map = null;

        public boolean add(Object o) {
            throw new UnsupportedOperationException();
        }

        public boolean add(Collection c) {
            throw new UnsupportedOperationException();
        }

        public void clear() {
            map.clear();
        }

        public boolean contains(Object o) {
            if (o == null) {
                throw new NullPointerException();
            }
            if (!(o instanceof Map.Entry)) {
                return (false);
            }
            Map.Entry e = (Map.Entry) o;
            Object k = e.getKey();
            Object v = e.getValue();
            if (!map.containsKey(k)) {
                return (false);
            }
            if (v == null) {
                return (map.get(k) == null);
            } else {
                return (v.equals(map.get(k)));
            }
        }

        public boolean isEmpty() {
            return (map.isEmpty());
        }

        public Iterator iterator() {
            return (new FacetsMapEntrySetIterator(map));
        }

        public boolean remove(Object o) {
            if (o == null) {
                throw new NullPointerException();
            }
            if (!(o instanceof Map.Entry)) {
                return (false);
            }
            Object k = ((Map.Entry) o).getKey();
            if (map.containsKey(k)) {
                map.remove(k);
                return (true);
            } else {
                return (false);
            }
        }

        public boolean removeAll(Collection c) {
            boolean result = false;
            Iterator v = c.iterator();
            while (v.hasNext()) {
                if (remove(v.next())) {
                    result = true;
                }
            }
            return (result);
        }

        public boolean retainAll(Collection c) {
            boolean result = false;
            Iterator v = iterator();
            while (v.hasNext()) {
                if (!c.contains(v.next())) {
                    v.remove();
                    result = true;
                }
            }
            return (result);
        }

        public int size() {
            return (map.size());
        }

    }


    // Private implementation of Map.Entry for FacetsMapEntrySet
    private static class FacetsMapEntrySetEntry implements Map.Entry {

        public FacetsMapEntrySetEntry(FacetsMap map, Object key) {
            this.map = map;
            this.key = key;
        }

        private FacetsMap map;
        private Object key;

        public boolean equals(Object o) {
            if (o == null) {
                return (false);
            }
            if (!(o instanceof Map.Entry)) {
                return (false);
            }
            Map.Entry e = (Map.Entry) o;
            if (key == null) {
                if (e.getKey() != null) {
                    return (false);
                }
            } else {
                if (!key.equals(e.getKey())) {
                    return (false);
                }
            }
            Object v = map.get(key);
            if (v == null) {
                if (e.getValue() != null) {
                    return (false);
                }
            } else {
                if (!v.equals(e.getValue())) {
                    return (false);
                }
            }
            return (true);
        }

        public Object getKey() {
            return (key);
        }

        public Object getValue() {
            return (map.get(key));
        }

        public int hashCode() {
            Object value = map.get(key);
            return (((key == null) ? 0 : key.hashCode()) ^
                    ((value == null) ? 0 : value.hashCode()));
        }

        public Object setValue(Object value) {
            Object previous = map.get(key);
            map.put(key, value);
            return (previous);
        }

    }


    // Private implementation of Set for FacetsMap.getEntrySet().iterator()
    private static class FacetsMapEntrySetIterator implements Iterator {

        public FacetsMapEntrySetIterator(FacetsMap map) {
            this.map = map;
            this.iterator = map.keySetIterator();
        }

        private FacetsMap map = null;
        private Iterator iterator = null;
        private Object last = null;

        public boolean hasNext() {
            return (iterator.hasNext());
        }

        public Object next() {
            last = new FacetsMapEntrySetEntry(map, iterator.next());
            return (last);
        }

        public void remove() {
            if (last == null) {
                throw new IllegalStateException();
            }
            map.remove(((Map.Entry) last).getKey());
            last = null;
        }

    }


    // Private implementation of Set for FacetsMap.getKeySet()
    private static class FacetsMapKeySet extends AbstractSet {

        public FacetsMapKeySet(FacetsMap map) {
            this.map = map;
        }

        private FacetsMap map = null;

        public boolean add(Object o) {
            throw new UnsupportedOperationException();
        }

        public boolean add(Collection c) {
            throw new UnsupportedOperationException();
        }

        public void clear() {
            map.clear();
        }

        public boolean contains(Object o) {
            return (map.containsKey(o));
        }

        public boolean containsAll(Collection c) {
            Iterator v = c.iterator();
            while (v.hasNext()) {
                if (!map.containsKey(v.next())) {
                    return (false);
                }
            }
            return (true);
        }

        public boolean isEmpty() {
            return (map.size() == 0);
        }

        public Iterator iterator() {
            return (new FacetsMapKeySetIterator(map));
        }

        public boolean remove(Object o) {
            if (map.containsKey(o)) {
                map.remove(o);
                return (true);
            } else {
                return (false);
            }
        }

        public boolean removeAll(Collection c) {
            boolean result = false;
            Iterator v = c.iterator();
            while (v.hasNext()) {
                Object o = v.next();
                if (map.containsKey(o)) {
                    map.remove(o);
                    result = true;
                }
            }
            return (result);
        }

        public boolean retainAll(Collection c) {
            boolean result = false;
            Iterator v = iterator();
            while (v.hasNext()) {
                if (!c.contains(v.next())) {
                    v.remove();
                    result = true;
                }
            }
            return (result);
        }

        public int size() {
            return (map.size());
        }

    }


    // Private implementation of Set for FacetsMap.getKeySet().iterator()
    private static class FacetsMapKeySetIterator implements Iterator {

        public FacetsMapKeySetIterator(FacetsMap map) {
            this.map = map;
            this.iterator = map.keySetIterator();
        }

        private FacetsMap map = null;
        private Iterator iterator = null;
        private Object last = null;

        public boolean hasNext() {
            return (iterator.hasNext());
        }

        public Object next() {
            last = iterator.next();
            return (last);
        }

        public void remove() {
            if (last == null) {
                throw new IllegalStateException();
            }
            map.remove(last);
            last = null;
        }

    }


    // Private implementation of Collection for FacetsMap.values()
    private static class FacetsMapValues extends AbstractCollection {

        public FacetsMapValues(FacetsMap map) {
            this.map = map;
        }

        private FacetsMap map;

        public boolean add(Object o) {
            throw new UnsupportedOperationException();
        }

        public boolean addAll(Collection c) {
            throw new UnsupportedOperationException();
        }

        public void clear() {
            map.clear();
        }

        public boolean isEmpty() {
            return (map.size() == 0);
        }

        public Iterator iterator() {
            return (new FacetsMapValuesIterator(map));
        }

        public int size() {
            return (map.size());
        }


    }


    // Private implementation of Iterator for FacetsMap.values().iterator()
    private static class FacetsMapValuesIterator implements Iterator {

        public FacetsMapValuesIterator(FacetsMap map) {
            this.map = map;
            this.iterator = map.keySetIterator();
        }

        private FacetsMap map = null;
        private Iterator iterator = null;
        private Object last = null;

        public boolean hasNext() {
            return (iterator.hasNext());
        }

        public Object next() {
            last = iterator.next();
            return (map.get(last));
        }

        public void remove() {
            if (last == null) {
                throw new IllegalStateException();
            }
            map.remove(last);
            last = null;
        }

    }


   

}

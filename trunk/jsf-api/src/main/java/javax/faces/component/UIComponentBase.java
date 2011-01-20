/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2010 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package javax.faces.component;

import javax.el.ELException;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.application.Application;
import javax.faces.component.behavior.Behavior;
import javax.faces.component.behavior.ClientBehavior;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.event.*;
import javax.faces.render.Renderer;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Array;
import java.lang.reflect.Modifier;
import java.util.AbstractCollection;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * <p><strong class="changed_modified_2_0 changed_modified_2_0_rev_a changed_added_2_1">UIComponentBase</strong> is a
 * convenience base class that implements the default concrete behavior
 * of all methods defined by {@link UIComponent}.</p>
 * <p/>
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

    private static Logger LOGGER = Logger.getLogger("javax.faces.component",
            "javax.faces.LogStrings");

    private static final String ADDED = UIComponentBase.class.getName() + ".ADDED";


    /**
     * <p>Each entry is an map of <code>PropertyDescriptor</code>s describing
     * the properties of a concrete {@link UIComponent} implementation, keyed
     * by the corresponding <code>java.lang.Class</code>.</p>
     * <p/>
     * <p><strong>IMPLEMENTATION NOTE</strong> - This is implemented as a
     * <code>WeakHashMap</code> so that, even if this class is embedded in a
     * container's class loader that is a parent to webapp class loaders,
     * references to the classes will eventually expire.</p>
     */
    @SuppressWarnings({"CollectionWithoutInitialCapacity"})
    private static Map<Class<?>, Map<String, PropertyDescriptor>>
            descriptors =
            new WeakHashMap<Class<?>, Map<String, PropertyDescriptor>>();

    /**
     * Reference to the map of <code>PropertyDescriptor</code>s for this class
     * in the <code>descriptors<code> <code>Map<code>.
     */
    private Map<String, PropertyDescriptor> pdMap = null;

    private Map<Class<? extends SystemEvent>, List<SystemEventListener>> listenersByEventClass;

    /**
     * <p>An EMPTY_OBJECT_ARRAY argument list to be passed to reflection methods.</p>
     */
    private static final Object EMPTY_OBJECT_ARRAY[] = new Object[0];

    public UIComponentBase() {
        populateDescriptorsMapIfNecessary();
    }

    private void populateDescriptorsMapIfNecessary() {
        Class<?> clazz = this.getClass();
        pdMap = descriptors.get(clazz);
        if (null != pdMap) {
            return;
        }

        // load the property descriptors for this class.
        PropertyDescriptor pd[] = getPropertyDescriptors();
        if (pd != null) {
            pdMap = new HashMap<String, PropertyDescriptor>(pd.length, 1.0f);
            for (PropertyDescriptor aPd : pd) {
                pdMap.put(aPd.getName(), aPd);
            }
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE, "fine.component.populating_descriptor_map",
                        new Object[]{clazz,
                                Thread.currentThread().getName()});
            }

            // Check again
            Map<String, PropertyDescriptor> reCheckMap =
                    descriptors.get(clazz);
            if (null != reCheckMap) {
                return;
            }
            descriptors.put(clazz, pdMap);
        }


    }


    /**
     * <p>Return an array of <code>PropertyDescriptors</code> for this
     * {@link UIComponent}'s implementation class.  If no descriptors
     * can be identified, a zero-length array will be returned.</p>
     *
     * @throws FacesException if an introspection exception occurs
     */
    private PropertyDescriptor[] getPropertyDescriptors() {
        PropertyDescriptor[] pd;
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
    private AttributesMap attributes = null;


    public Map<String, Object> getAttributes() {

        if (attributes == null) {
            attributes = new AttributesMap(this);
        }
        return (attributes);

    }


    // ---------------------------------------------------------------- Bindings


    /**
     * {@inheritDoc}
     *
     * @throws NullPointerException {@inheritDoc}
     * @deprecated This has been replaced by {@link #getValueExpression}.
     */
    public ValueBinding getValueBinding(String name) {

        if (name == null) {
            throw new NullPointerException();
        }
        ValueBinding result = null;
        ValueExpression ve;

        if (null != (ve = getValueExpression(name))) {
            // if the ValueExpression is an instance of our private
            // wrapper class.
            if (ve.getClass().equals(ValueExpressionValueBindingAdapter.class)) {
                result = ((ValueExpressionValueBindingAdapter) ve).getWrapped();
            } else {
                // otherwise, this is a real ValueExpression.  Wrap it
                // in a ValueBinding.
                result = new ValueBindingValueExpressionAdapter(ve);
            }
        }
        return result;
    }


    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException {@inheritDoc}
     * @throws NullPointerException     {@inheritDoc}
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


    // -------------------------------------------------------------- Properties


    /**
     * <p>The assigned client identifier for this component.</p>
     */
    private String clientId = null;


    /**
     * @throws NullPointerException {@inheritDoc}
     */
    public String getClientId(FacesContext context) {

        if (context == null) {
            throw new NullPointerException();
        }

        // if the clientId is not yet set
        if (this.clientId == null) {
            UIComponent namingContainerAncestor =
                    this.getNamingContainerAncestor();
            UIComponent parent = namingContainerAncestor;
            String parentId = null;

            // give the parent the opportunity to first
            // grab a unique clientId
            if (parent != null) {
                parentId = parent.getContainerClientId(context);
            }

            // now resolve our own client id
            this.clientId = getId();
            if (this.clientId == null) {
                String generatedId;
                if (null != namingContainerAncestor &&
                    namingContainerAncestor instanceof UniqueIdVendor) {
                    generatedId = ((UniqueIdVendor)namingContainerAncestor).createUniqueId(context, null);
                }
                else {
                    generatedId = context.getViewRoot().createUniqueId();
                }
                setId(generatedId);
                this.clientId = getId();
            }
            if (parentId != null) {
                StringBuilder idBuilder =
                        new StringBuilder(parentId.length()
                                + 1
                                + this.clientId.length());
                this.clientId = idBuilder.append(parentId)
                        .append(UINamingContainer.getSeparatorChar(context))
                        .append(this.clientId).toString();
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
     * <p>The component identifier for this component.</p>
     */
    private String id = null;


    public String getId() {

        return (id);

    }

    private UIComponent getNamingContainerAncestor() {
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
     * @throws IllegalArgumentException {@inheritDoc}
     * @throws IllegalStateException    {@inheritDoc}
     */
    public void setId(String id) {

        // if the current ID is not null, and the passed
        // argument is the same, no need to validate it
        // as it has already been validated.
        if (this.id == null || !(this.id.equals(id))) {
            validateId(id);
            this.id = id;
        }

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


        if (parent == null) {
            if (this.parent != null) {
                doPreRemoveProcessing(FacesContext.getCurrentInstance(), this);
                this.parent = parent;
            }
            compositeParent = null;
        } else {
            this.parent = parent;
            if (this.getAttributes().get(ADDED) == null) {
                // add an attribute to this component here to indiciate that
                // it's being processed.  If we don't do this, and the component
                // is re-parented, the events could fire again in certain cases
                // and cause a stack overflow.
                this.getAttributes().put(ADDED, Boolean.TRUE);
                doPostAddProcessing(FacesContext.getCurrentInstance(), this);
                // remove the attribute once we've returned from the event
                // processing.
                this.getAttributes().remove(ADDED);
            }
        }

    }



    public boolean isRendered() {
        
	return Boolean.valueOf(getStateHelper().eval(PropertyKeys.rendered, Boolean.TRUE).toString());        
    }


    public void setRendered(boolean rendered) {
        getStateHelper().put(PropertyKeys.rendered, rendered);
    }


    public String getRendererType() {

        return (String) getStateHelper().eval(PropertyKeys.rendererType);
        
    }


    public void setRendererType(String rendererType) {

        getStateHelper().put(PropertyKeys.rendererType, rendererType);

    }


    public boolean getRendersChildren() {
        boolean result = false;

        Renderer renderer;
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
    private List<UIComponent> children = null;


    public List<UIComponent> getChildren() {

        if (children == null) {
            children = new ChildrenList(this);
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
    private static void eraseParent(UIComponent component) {

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
                //noinspection ObjectEquality
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
    private static void validateId(String id) {

        if (id == null) {
            return;
        }
        int n = id.length();
        if (n < 1) {
            throw new IllegalArgumentException("Empty id attribute is not allowed");
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


    /**
     * @throws NullPointerException {@inheritDoc}
     */
    public UIComponent findComponent(String expr) {
        if (expr == null) {
            throw new NullPointerException();
        }

        FacesContext ctx = FacesContext.getCurrentInstance();
        final char sepChar = UINamingContainer.getSeparatorChar(ctx);
        final String SEPARATOR_STRING = String.valueOf(sepChar);

        if (expr.length() == 0) {
            // if an empty value is provided, fail fast.
            throw new IllegalArgumentException("\"\"");
        }

        // Identify the base component from which we will perform our search
        UIComponent base = this;
        if (expr.charAt(0) == sepChar) {
            // Absolute searches start at the root of the tree
            while (base.getParent() != null) {
                base = base.getParent();
            }
            // Treat remainder of the expression as relative
            expr = expr.substring(1);
        } else if (!(base instanceof NamingContainer)) {
            // Relative expressions start at the closest NamingContainer or root
            while (base.getParent() != null) {
                if (base instanceof NamingContainer) {
                    break;
                }
                base = base.getParent();
            }
        }

        // Evaluate the search expression (now guaranteed to be relative)
        UIComponent result = null;
        String[] segments = expr.split(SEPARATOR_STRING);
        for (int i = 0, length = (segments.length - 1);
             i < segments.length;
             i++, length--) {
            result = findComponent(base, segments[i], (i == 0));
            // the first element of the expression may match base.id
            // (vs. a child if of base)
            if (i == 0 && result == null &&
                    segments[i].equals(base.getId())) {
                result = base;
            }
            if (result != null && (!(result instanceof NamingContainer)) && length > 0) {
                throw new IllegalArgumentException(segments[i]);
            }
            if (result == null) {
                break;
            }
            base = result;
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
     * @param id   Component identifier to be matched
     */
    private static UIComponent findComponent(UIComponent base,
                                             String id,
                                             boolean checkId) {
        if (checkId && id.equals(base.getId())) {
            return base;
        }
        // Search through our facets and children
        UIComponent result = null;
        for (Iterator i = base.getFacetsAndChildren(); i.hasNext();) {
            UIComponent kid = (UIComponent) i.next();
            if (!(kid instanceof NamingContainer)) {
                if (checkId && id.equals(kid.getId())) {
                    result = kid;
                    break;
                }
                result = findComponent(kid, id, true);
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

    /**
     * {@inheritDoc}
     *
     * @throws NullPointerException {@inheritDoc}
     * @throws FacesException       {@inheritDoc}
     * @since 1.2
     */
    public boolean invokeOnComponent(FacesContext context, String clientId,
                                     ContextCallback callback)
            throws FacesException {
        return super.invokeOnComponent(context, clientId, callback);
    }


    // ------------------------------------------------ Facet Management Methods


    /*
     * <p>The <code>Map</code> containing our related facet components.</p>
     */
    private Map<String, UIComponent> facets = null;


    public Map<String, UIComponent> getFacets() {

        if (facets == null) {
            facets = new FacetsMap(this);
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
            return (facets.get(name));
        } else {
            return (null);
        }

    }


    public Iterator<UIComponent> getFacetsAndChildren() {

        Iterator<UIComponent> result;
        int childCount = this.getChildCount(),
                facetCount = this.getFacetCount();
        // If there are neither facets nor children
        if (0 == childCount && 0 == facetCount) {
            result = EMPTY_ITERATOR;
        }
        // If there are only facets and no children
        else if (0 == childCount) {
            Collection<UIComponent> unmodifiable =
                    Collections.unmodifiableCollection(getFacets().values());
            result = unmodifiable.iterator();
        }
        // If there are only children and no facets
        else if (0 == facetCount) {
            List<UIComponent> unmodifiable =
                    Collections.unmodifiableList(getChildren());
            result = unmodifiable.iterator();
        }
        // If there are both children and facets
        else {
            result = new FacetsAndChildrenIterator(this);
        }
        return result;
    }


    // -------------------------------------------- Lifecycle Processing Methods

    /**
     * @throws AbortProcessingException {@inheritDoc}
     * @throws IllegalStateException    {@inheritDoc}
     * @throws NullPointerException     {@inheritDoc}
     */
    public void broadcast(FacesEvent event)
        throws AbortProcessingException {

        if (event == null) {
            throw new NullPointerException();
        }
        if (event instanceof BehaviorEvent) {
            BehaviorEvent behaviorEvent = (BehaviorEvent) event;
            Behavior behavior = behaviorEvent.getBehavior();
            behavior.broadcast(behaviorEvent);
        }

        if (listeners == null) {
            return;
        }

        for (FacesListener listener : listeners.asArray(FacesListener.class)) {
            if (event.isAppropriateListener(listener)) {
                event.processListener(listener);
            }
        }
    }


    /**
     * @throws NullPointerException {@inheritDoc}
     */
    public void decode(FacesContext context) {

        if (context == null) {
            throw new NullPointerException();
        }
        String rendererType = getRendererType();
        if (rendererType != null) {
            Renderer renderer = this.getRenderer(context);
            if (renderer != null) {
                renderer.decode(context, this);
            } else {
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.fine("Can't get Renderer for type " + rendererType);
                }
            }
        }
    }


    /**
     * @throws NullPointerException {@inheritDoc}
     */
    public void encodeBegin(FacesContext context) throws IOException {

        if (context == null) {
            throw new NullPointerException();
        }

        pushComponentToEL(context, null);

        if (!isRendered()) {
            return;
        }

        context.getApplication().publishEvent(context,
                                              PreRenderComponentEvent.class,
                                              this);

        String rendererType = getRendererType();
        if (rendererType != null) {
            Renderer renderer = this.getRenderer(context);
            if (renderer != null) {
                renderer.encodeBegin(context, this);
            } else {
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.fine("Can't get Renderer for type " + rendererType);
                }
            }
        }

    }

    /**
     * @throws NullPointerException {@inheritDoc}
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
            Renderer renderer = this.getRenderer(context);
            if (renderer != null) {
                renderer.encodeChildren(context, this);
            }
            // We've already logged for this component
        } else {
            if (getChildCount() > 0) {
                for (UIComponent child : getChildren()) {
                    child.encodeAll(context);
                }
            }
        }
    }


    /**
     * @throws IOException          {@inheritDoc}
     * @throws NullPointerException {@inheritDoc}
     */
    public void encodeEnd(FacesContext context) throws IOException {

        if (context == null) {
            throw new NullPointerException();
        }
        if (!isRendered()) {
            popComponentFromEL(context);
            return;
        }
        String rendererType = getRendererType();
        if (rendererType != null) {
            Renderer renderer = this.getRenderer(context);
            if (renderer != null) {
                renderer.encodeEnd(context, this);
            } else {
                // We've already logged for this component
            }
        }
        popComponentFromEL(context);

    }

    // -------------------------------------------------- Event Listener Methods

    private AttachedObjectListHolder<FacesListener> listeners;

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
     * <p/>
     * public interface FooListener extends FacesListener {
     *   public void processFoo(FooEvent event);
     * }
     * <p/>
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
     * @throws NullPointerException if <code>listener</code>
     *                              is <code>null</code>
     */
    protected void addFacesListener(FacesListener listener) {

        if (listener == null) {
            throw new NullPointerException();
        }

        if (listeners == null) {
            listeners = new AttachedObjectListHolder<FacesListener>();
        }

        listeners.add(listener);

    }


    /**
     * @throws IllegalArgumentException {@inheritDoc}
     * @throws NullPointerException     {@inheritDoc}
     */
    protected FacesListener[] getFacesListeners(Class clazz) {

        if (clazz == null) {
            throw new NullPointerException();
        }
        if (!FacesListener.class.isAssignableFrom(clazz)) {
            throw new IllegalArgumentException();
        }

        if (this.listeners == null) {
            return (FacesListener[]) Array.newInstance(clazz, 0);
        }
        FacesListener[] listeners = this.listeners.asArray(FacesListener.class);
        if (listeners.length == 0) {
            return (FacesListener[]) Array.newInstance(clazz, 0);
        }

        List<FacesListener> results = new ArrayList<FacesListener>(listeners.length);
        for (FacesListener listener : listeners) {
            if (((Class<?>) clazz).isAssignableFrom(listener.getClass())) {
                results.add(listener);
            }
        }

        return (results.toArray
                ((FacesListener[]) Array.newInstance(clazz,
                        results.size())));

    }


    /**
     * <p>Remove the specified {@link FacesListener} from the set of listeners
     * registered to receive event notifications from this {@link UIComponent}.
     *
     * @param listener The {@link FacesListener} to be deregistered
     * @throws NullPointerException if <code>listener</code>
     *                              is <code>null</code>
     */
    protected void removeFacesListener(FacesListener listener) {

        if (listener == null) {
            throw new NullPointerException();
        }

        if (listeners != null) {
            listeners.remove(listener);
        }

    }

    /**
     * @throws IllegalStateException {@inheritDoc}
     * @throws NullPointerException  {@inheritDoc}
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

    /**
     * <p class="changed_added_2_1">Install the listener instance
     * referenced by argument <code>componentListener</code> as a
     * listener for events of type <code>eventClass</code> originating
     * from this specific instance of <code>UIComponent</code>.  The
     * default implementation creates an inner {@link
     * SystemEventListener} instance that wraps argument
     * <code>componentListener</code> as the <code>listener</code>
     * argument.  This inner class must call through to the argument
     * <code>componentListener</code> in its implementation of {@link
     * SystemEventListener#processEvent} and its implementation of
     * {@link SystemEventListener#isListenerForSource} must return
     * true if the instance class of this <code>UIComponent</code> is
     * assignable from the argument to
     * <code>isListenerForSource</code>.</p>
     *
     * @param eventClass the <code>Class</code> of event for which
     * <code>listener</code> must be fired.
     * @param componentListener the implementation of {@link
     * javax.faces.event.ComponentSystemEventListener} whose {@link
     * javax.faces.event.ComponentSystemEventListener#processEvent} method must be called
     * when events of type <code>facesEventClass</code> are fired.
     *
     * @throws <code>NullPointerException</code> if any of the
     * arguments are <code>null</code>.
     *
     * @since 2.1
     */
    public void subscribeToEvent(Class<? extends SystemEvent> eventClass,
                                 ComponentSystemEventListener componentListener) {

        if (eventClass == null) {
            throw new NullPointerException();
        }
        if (componentListener == null) {
            throw new NullPointerException();
        }

        if (initialStateMarked()) {
            initialState = false;
        }
        if (null == listenersByEventClass) {
            listenersByEventClass = new HashMap<Class<? extends SystemEvent>,
                                                List<SystemEventListener>>(3, 1.0f);
        }
        SystemEventListener facesLifecycleListener =
              new ComponentSystemEventListenerAdapter(componentListener, this);
        List<SystemEventListener> listenersForEventClass =
              listenersByEventClass.get(eventClass);
        if (listenersForEventClass == null) {
            listenersForEventClass = new ArrayList<SystemEventListener>(3);
            listenersByEventClass.put(eventClass, listenersForEventClass);
        }
        if (!listenersForEventClass.contains(facesLifecycleListener)) {
            listenersForEventClass.add(facesLifecycleListener);
        }

    }

    /**
     * <p class="changed_added_2_1">Remove the listener instance
     *     referenced by argument <code>componentListener</code> as a
     *     listener for events of type <code>eventClass</code>
     *     originating from this specific instance of
     *     <code>UIComponent</code>.  When doing the comparison to
     *     determine if an existing listener is equal to the argument
     *     <code>componentListener</code> (and thus must be removed),
     *     the <code>equals()</code> method on the <em>existing
     *     listener</em> must be invoked, passing the argument
     *     <code>componentListener</code>, rather than the other way
     *     around.</p>
     *
     * @param eventClass the <code>Class</code> of event for which
     * <code>listener</code> must be removed.
     * @param componentListener the implementation of {@link
     * ComponentSystemEventListener} whose {@link
     * ComponentSystemEventListener#processEvent} method must no longer be called
     * when events of type <code>eventClass</code> are fired.
     *
     * @throws <code>NullPointerException</code> if any of the
     * arguments are <code>null</code>.
     *
     * @since 2.1
     */
    public void unsubscribeFromEvent(Class<? extends SystemEvent> eventClass,
                                     ComponentSystemEventListener componentListener) {

        if (eventClass == null) {
            throw new NullPointerException();
        }
        if (componentListener == null) {
            throw new NullPointerException();
        }

        List<SystemEventListener> listeners =
              getListenersForEventClass(eventClass);
        if (listeners != null && !listeners.isEmpty()) {
            for (Iterator<SystemEventListener> i = listeners.iterator(); i.hasNext();) {
                SystemEventListener item = i.next();
                ComponentSystemEventListenerAdapter csla =
                      (ComponentSystemEventListenerAdapter) item;
                ComponentSystemEventListener l = csla.getWrapped();
                if (l.equals(componentListener)) {
                    i.remove();
                    break;
                }
            }
        }

    }

    /**
     * <p class="changed_added_2_1">Return the
     * <code>SystemEventListener</code> instances registered on this
     * <code>UIComponent</code> instance that are interested in events
     * of type <code>eventClass</code>.</p>
     *
     * @param eventClass the <code>Class</code> of event for which the
     * listeners must be returned.

     * @throws NullPointerException if argument <code>eventClass</code> is <code>null</code>.
     *
     * @since 2.1
     */
    public List<SystemEventListener> getListenersForEventClass(Class<? extends SystemEvent> eventClass) {

        if (eventClass == null) {
            throw new NullPointerException();
        }
        List<SystemEventListener> result = null;
        if (listenersByEventClass != null) {
            result = listenersByEventClass.get(eventClass);
        }
        return result;

    }


    // ------------------------------------------------ Lifecycle Phase Handlers


    /**
     * @throws NullPointerException {@inheritDoc}
     */
    public void processDecodes(FacesContext context) {

        if (context == null) {
            throw new NullPointerException();
        }

        // Skip processing if our rendered flag is false
        if (!isRendered()) {
            return;
        }

        pushComponentToEL(context, null);

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
        } finally {
            popComponentFromEL(context);
        }

    }


    /**
     * @throws NullPointerException {@inheritDoc}
     */
    public void processValidators(FacesContext context) {

        if (context == null) {
            throw new NullPointerException();
        }

        // Skip processing if our rendered flag is false
        if (!isRendered()) {
            return;
        }

        pushComponentToEL(context, null);

        Application app = context.getApplication();
        app.publishEvent(context, PreValidateEvent.class, this);
        // Process all the facets and children of this component
        Iterator kids = getFacetsAndChildren();
        while (kids.hasNext()) {
            UIComponent kid = (UIComponent) kids.next();
            kid.processValidators(context);
        }
        app.publishEvent(context, PostValidateEvent.class, this);
        popComponentFromEL(context);

    }


    /**
     * @throws NullPointerException {@inheritDoc}
     */
    public void processUpdates(FacesContext context) {

        if (context == null) {
            throw new NullPointerException();
        }

        // Skip processing if our rendered flag is false
        if (!isRendered()) {
            return;
        }

        pushComponentToEL(context, null);

        // Process all facets and children of this component
        Iterator kids = getFacetsAndChildren();
        while (kids.hasNext()) {
            UIComponent kid = (UIComponent) kids.next();
            kid.processUpdates(context);

        }
        popComponentFromEL(context);
        
    }

    private static final int MY_STATE = 0;
    private static final int CHILD_STATE = 1;

    /**
     * @throws NullPointerException {@inheritDoc}
     */
    public Object processSaveState(FacesContext context) {

        if (context == null) {
            throw new NullPointerException();
        }
        if (this.isTransient()) {
            return null;
        }
        Object[] stateStruct = new Object[2];
        Object[] childState = EMPTY_ARRAY;

        pushComponentToEL(context, null);

        // Process this component itself
        stateStruct[MY_STATE] = saveState(context);

        // determine if we have any children to store
        int count = this.getChildCount() + this.getFacetCount();
        if (count > 0) {

            // this arraylist will store state
            List<Object> stateList = new ArrayList<Object>(count);

            // if we have children, add them to the stateList
            if (this.getChildCount() > 0) {
                Iterator kids = getChildren().iterator();
                UIComponent kid;
                while (kids.hasNext()) {
                    kid = (UIComponent) kids.next();
                    if (!kid.isTransient()) {
                        stateList.add(kid.processSaveState(context));
                        popComponentFromEL(context);
                    }
                }
            }

            pushComponentToEL(context, null);

            // if we have facets, add them to the stateList
            if (this.getFacetCount() > 0) {
                Iterator myFacets = getFacets().entrySet().iterator();
                UIComponent facet;
                Object facetState;
                Object[] facetSaveState;
                Map.Entry entry;
                while (myFacets.hasNext()) {
                    entry = (Map.Entry) myFacets.next();
                    facet = (UIComponent) entry.getValue();
                    if (!facet.isTransient()) {
                        facetState = facet.processSaveState(context);
                        popComponentFromEL(context);
                        facetSaveState = new Object[2];
                        facetSaveState[0] = entry.getKey();
                        facetSaveState[1] = facetState;
                        stateList.add(facetSaveState);
                    }
                }
            }

            // finally, capture the stateList and replace the original,
            // EMPTY_OBJECT_ARRAY Object array
            childState = stateList.toArray();
        }

        stateStruct[CHILD_STATE] = childState;
        return stateStruct;
    }

    /**
     * @throws NullPointerException {@inheritDoc}
     */
    public void processRestoreState(FacesContext context,
                                    Object state) {
        if (context == null) {
            throw new NullPointerException();
        }

        Object[] stateStruct = (Object[]) state;
        Object[] childState = (Object[]) stateStruct[CHILD_STATE];

        // Process this component itself
        restoreState(context, stateStruct[MY_STATE]);

        int i = 0;

        // Process all the children of this component
        if (this.getChildCount() > 0) {
            for (UIComponent kid : getChildren()) {
                if (kid.isTransient()) {
                    continue;
                }
                Object currentState = childState[i++];
                if (currentState == null) {
                    continue;
                }
                pushComponentToEL(context, null);
                kid.processRestoreState(context, currentState);
                popComponentFromEL(context);
            }
        }

        // process all of the facets of this component
        if (this.getFacetCount() > 0) {
            int facetsSize = getFacets().size();
            int j = 0;
            Object[] facetSaveState;
            String facetName;
            UIComponent facet;
            Object facetState;
            while (j < facetsSize) {
                if (null != (facetSaveState = (Object[]) childState[i++])) {
                    facetName = (String) facetSaveState[0];
                    facetState = facetSaveState[1];
                    facet = getFacets().get(facetName);
                    pushComponentToEL(context, null);
                    facet.processRestoreState(context, facetState);
                    popComponentFromEL(context);
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
            result = context.getRenderKit().getRenderer(getFamily(),
                    rendererType);
            if (null == result) {
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.fine("Can't get Renderer for type " + rendererType);
                }
            }
        } else {
            if (LOGGER.isLoggable(Level.FINE)) {
                String id = this.getId();
                id = (null != id) ? id : this.getClass().getName();
                LOGGER.fine("No renderer-type for component " + id);
            }
        }
        return result;
    }


    // ---------------------------------------------- PartialStateHolder Methods

    /**
     * <p class="changed_added_2_0">For each of the attached objects on
     * this instance that implement {@link PartialStateHolder}, call
     * {@link PartialStateHolder#markInitialState} on the attached object.</p> 
     * @since 2.0
     */
    @Override
    public void markInitialState() {
        super.markInitialState();

        if (listeners != null) {
            listeners.markInitialState();
        }
        if (listenersByEventClass != null) {
            for (List<SystemEventListener> listener : listenersByEventClass.values()) {
                if (listener instanceof PartialStateHolder) {
                    ((PartialStateHolder) listener).markInitialState();
                }
            }
        }
        if (behaviors != null) {
            for (Entry<String, List<ClientBehavior>> entry : behaviors.entrySet()) {
                for (ClientBehavior behavior : entry.getValue()) {
                    if (behavior instanceof PartialStateHolder) {
                        ((PartialStateHolder) behavior).markInitialState();
                    }
                }
            }
        }
    }


    /**
     * <p class="changed_added_2_0">For each of the attached objects on
     * this instance that implement {@link PartialStateHolder}, call
     * {@link PartialStateHolder#clearInitialState} on the attached object.</p> 
     * @since 2.0
     */
    @Override
    public void clearInitialState() {
        super.clearInitialState();
        if (listeners != null) {
            listeners.clearInitialState();
        }
        if (listenersByEventClass != null) {
            for (List<SystemEventListener> listener : listenersByEventClass.values()) {
                if (listener instanceof PartialStateHolder) {
                    ((PartialStateHolder) listener).clearInitialState();
                }
            }
        }
        if (behaviors != null) {
            for (Entry<String, List<ClientBehavior>> entry : behaviors.entrySet()) {
                for (ClientBehavior behavior : entry.getValue()) {
                    if (behavior instanceof PartialStateHolder) {
                        ((PartialStateHolder) behavior).clearInitialState();
                    }
                }
            }
        }
    }


    public Object saveState(FacesContext context) {
        Object[] values = null;
        if (context == null) {
            throw new NullPointerException();
        }
        assert (!transientFlag);
        if (initialStateMarked()) {
            Object savedFacesListeners = ((listeners != null) ? listeners.saveState(context) : null);
            Object savedSysEventListeners = saveSystemEventListeners(context);
            Object savedBehaviors = saveBehaviorsState(context);
            Object savedBindings = null;
            if (bindings != null) {
                savedBindings = saveBindingsState(context);
            }
            Object savedHelper = null;
            if (stateHelper != null) {
                savedHelper = stateHelper.saveState(context);
            }
            if (savedFacesListeners == null
                  && savedSysEventListeners == null
                  && savedBehaviors == null
                  && savedBindings == null
                  && savedHelper == null) {
                return null;
            } else {
                if (values == null || values.length != 5) {
                    values = new Object[5];
                }

                // since we're saving partial state, skip id and clientId
                // as this will be reconstructed from the template execution
                // when the view is restored
                values[0] = savedFacesListeners;
                values[1] = savedSysEventListeners;
                values[2] = savedBehaviors;
                values[3] = savedBindings;
                values[4] = savedHelper;
                return values;
            }
        } else {
            if (values == null || values.length != 6) {
                values = new Object[6];
            }

            values[0] = ((listeners != null) ? listeners.saveState(context) : null);
            values[1] = saveSystemEventListeners(context);
            values[2] = saveBehaviorsState(context);
            if (bindings != null) {
                values[3] = saveBindingsState(context);
            }
            if (stateHelper != null) {
                values[4] = stateHelper.saveState(context);
            }
            values[5] = id;

            return (values);
        }
    }


    public void restoreState(FacesContext context, Object state) {

        if (context == null) {
            throw new NullPointerException();
        }

        if (state == null) {
            return;
        }
        Object[] values = (Object[]) state;

        if (values[0] != null) {
            if (listeners == null) {
                listeners = new AttachedObjectListHolder<FacesListener>();
            }
            listeners.restoreState(context, values[0]);
        }
        if (values[1] != null) {
            Map m = restoreSystemEventListeners(context, values[1]);
            if (listenersByEventClass != null) {
                listenersByEventClass.putAll(m);
            } else {
                listenersByEventClass = m;
            }
        }
        if (values[2] != null) {
            behaviors = restoreBehaviorsState(context, values[2]);
        }
        if (values[3] != null) {
            bindings = restoreBindingsState(context, values[3]);
        }
        if(values[4] != null) {
            getStateHelper().restoreState(context, values[4]);
        }
        if (values.length == 6) {
            // this means we've saved full state and need to do a little more
            // work to finish the job
            if (values[5] != null) {
                id = (String) values[5];
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
     * <p class="changed_modified_2_0">This method is called by {@link
     * UIComponent} subclasses that want to save one or more attached
     * objects.  It is a convenience method that does the work of saving
     * attached objects that may or may not implement the {@link
     * StateHolder} interface.  Using this method implies the use of
     * {@link #restoreAttachedState} to restore the attached
     * objects.</p>
     * <p/>
     * <p>This method supports saving attached objects of the following
     * type: <code>Object</code>s, <code>null</code> values, and <code
     * class="changed_modified_2_0">Collection</code>s of these objects.
     * If any contained objects are not <code
     * class="changed_modified_2_0">Collection</code>s and do not
     * implement {@link StateHolder}, they must have zero-argument
     * public constructors.  The exact structure of the returned object
     * is undefined and opaque, but will be serializable.  </p>
     *
     * @param context        the {@link FacesContext} for this request.
     * @param attachedObject the object, which may be a
     *                       <code>List</code> instance, or an Object.  The
     *                       <code>attachedObject</code> (or the elements that comprise
     *                       <code>attachedObject</code> may implement {@link StateHolder}.
     * @throws NullPointerException if the context argument is null.
     */

    public static Object saveAttachedState(FacesContext context,
                                           Object attachedObject) {
        if (null == context) {
            throw new NullPointerException();
        }
        if (null == attachedObject) {
            return null;
        }
        Object result;
        Class mapOrCollectionClass = attachedObject.getClass();
        boolean newWillSucceed = true;
        // first, test for newability of the class.
        try {
            int modifiers = mapOrCollectionClass.getModifiers();
            newWillSucceed = Modifier.isPublic(modifiers);
            if (newWillSucceed) {
                newWillSucceed = null != mapOrCollectionClass.getConstructor();
            }
        } catch (Exception e) {
            newWillSucceed = false;
        }


        if (newWillSucceed && attachedObject instanceof Collection) {
            Collection attachedCollection = (Collection) attachedObject;
            List<StateHolderSaver> resultList = null;
            for (Object item : attachedCollection) {
                if (item != null) {
                    if (item instanceof StateHolder && ((StateHolder) item).isTransient()) {
                        continue;
                    }
                    if (resultList == null) {
                        resultList = new ArrayList<StateHolderSaver>(attachedCollection.size() + 1);
                        resultList.add(new StateHolderSaver(context, mapOrCollectionClass));
                    }
                    resultList.add(new StateHolderSaver(context, item));
                }
            }
            result = resultList;
        } else if (newWillSucceed && attachedObject instanceof Map) {
            Map<Object, Object> attachedMap = (Map<Object, Object>) attachedObject;
            List<StateHolderSaver> resultList = null;
            Object key, value;
            for (Map.Entry<Object, Object> entry : attachedMap.entrySet()) {
                key = entry.getKey();
                if (key instanceof StateHolder && ((StateHolder)key).isTransient()) {
                    continue;
                }
                value = entry.getValue();
                if (value instanceof StateHolder && ((StateHolder)value).isTransient()) {
                    continue;
                }
                if (resultList == null) {
                    resultList = new ArrayList<StateHolderSaver>(attachedMap.size()*2 + 1);
                    resultList.add(new StateHolderSaver(context, mapOrCollectionClass));
                }
                resultList.add(new StateHolderSaver(context, key));
                resultList.add(new StateHolderSaver(context, value));
            }
            result = resultList;
        } else {
            result = new StateHolderSaver(context, attachedObject);
        }

        return result;
    }

    /**
     * <p>This method is called by {@link UIComponent} subclasses that
     * need to restore the objects they saved using {@link
     * #saveAttachedState}.  This method is tightly coupled with {@link
     * #saveAttachedState}.</p>
     * <p/>
     * <p>This method supports restoring all attached objects types
     * supported by {@link #saveAttachedState}.</p>
     *
     * @param context  the {@link FacesContext} for this request
     * @param stateObj the opaque object returned from {@link
     *                 #saveAttachedState}
     * @throws NullPointerException  if context is null.
     * @throws IllegalStateException if the object is not
     *                               previously returned by {@link #saveAttachedState}.
     */

    public static Object restoreAttachedState(FacesContext context,
                                              Object stateObj)
            throws IllegalStateException {
        if (null == context) {
            throw new NullPointerException();
        }
        if (null == stateObj) {
            return null;
        }
        Object result;

        if (stateObj instanceof List) {
            List<StateHolderSaver> stateList = (List<StateHolderSaver>) stateObj;
            StateHolderSaver collectionSaver = stateList.get(0);
            Class mapOrCollection = (Class) collectionSaver.restore(context);
            if (Collection.class.isAssignableFrom(mapOrCollection)) {
                Collection<Object> retCollection = null;
                try {
                    retCollection = (Collection<Object>) mapOrCollection.newInstance();
                } catch (Exception e) {
                    if (LOGGER.isLoggable(Level.SEVERE)) {
                        LOGGER.log(Level.SEVERE, e.toString(), e);
                    }
                    throw new IllegalStateException("Unknown object type");
                }
                for (int i = 1, len = stateList.size(); i < len; i++) {
                    try {
                        retCollection.add(stateList.get(i).restore(context));
                    } catch (ClassCastException cce) {
                        if (LOGGER.isLoggable(Level.SEVERE)) {
                            LOGGER.log(Level.SEVERE, cce.toString(), cce);
                        }
                        throw new IllegalStateException("Unknown object type");
                    }
                }
                result = retCollection;
            } else {
                // If we were doing assertions: assert(mapOrList.isAssignableFrom(Map.class));
                Map<Object, Object> retMap = null;
                try {
                    retMap = (Map<Object,Object>) mapOrCollection.newInstance();
                } catch (Exception e) {
                    if (LOGGER.isLoggable(Level.SEVERE)) {
                        LOGGER.log(Level.SEVERE, e.toString(), e);
                    }
                    throw new IllegalStateException("Unknown object type");
                }
                for (int i = 1, len = stateList.size(); i < len; i+=2) {
                    try {
                        retMap.put(stateList.get(i).restore(context),
                                   stateList.get(i+1).restore(context));
                    } catch (ClassCastException cce) {
                        if (LOGGER.isLoggable(Level.SEVERE)) {
                            LOGGER.log(Level.SEVERE, cce.toString(), cce);
                        }
                        throw new IllegalStateException("Unknown object type");
                    }
                }
                result = retMap;

            }
        } else if (stateObj instanceof StateHolderSaver) {
            StateHolderSaver saver = (StateHolderSaver) stateObj;
            result = saver.restore(context);
        } else {
            throw new IllegalStateException("Unknown object type");
        }
        return result;
    }

    private static Map<String, ValueExpression> restoreBindingsState(FacesContext context, Object state) {

        if (state == null) {
            return (null);
        }
        Object values[] = (Object[]) state;
        String names[] = (String[]) values[0];
        Object states[] = (Object[]) values[1];
        Map<String, ValueExpression> bindings = new HashMap<String, ValueExpression>(names.length);
        for (int i = 0; i < names.length; i++) {
            bindings.put(names[i],
                    (ValueExpression) restoreAttachedState(context, states[i]));
        }
        return (bindings);

    }


    private Object saveBindingsState(FacesContext context) {

        if (bindings == null) {
            return (null);
        }

        Object values[] = new Object[2];
        values[0] = bindings.keySet().toArray(new String[bindings.size()]);

        Object[] bindingValues = bindings.values().toArray();
        for (int i = 0; i < bindingValues.length; i++) {
            bindingValues[i] = saveAttachedState(context, bindingValues[i]);
        }

        values[1] = bindingValues;

        return (values);

    }

    private Object saveSystemEventListeners(FacesContext ctx) {

        if (listenersByEventClass == null) {
            return null;
        }

        int size = listenersByEventClass.size();
        Object listeners[][] = new Object[size][2];
        int idx = 0;
        boolean savedState = false;
        for (Entry<Class<? extends SystemEvent>, List<SystemEventListener>> e : listenersByEventClass.entrySet()) {
            Object[] target = listeners[idx++];
            target[0] = e.getKey();
            target[1] = saveAttachedState(ctx, e.getValue());
            if (target[1] == null) {
                target[0] = null;
            } else {
                savedState = true;
            }
        }

        return ((savedState) ? listeners : null);

    }


    private Map<Class<? extends SystemEvent>, List<SystemEventListener>> restoreSystemEventListeners(FacesContext ctx, Object state) {

        if (state == null) {
            return null;
        }

        Object[][] listeners = (Object[][]) state;
        Map<Class<? extends SystemEvent>, List<SystemEventListener>> m =
                new HashMap<Class<? extends SystemEvent>, List<SystemEventListener>>(listeners.length, 1.0f);
        for (int i = 0, len = listeners.length; i < len; i++) {
            Object[] source = listeners[i];
            m.put((Class<? extends SystemEvent>) source[0],
                    (List<SystemEventListener>) restoreAttachedState(ctx, source[1]));
        }

        return m;
    }


    Map<String, PropertyDescriptor> getDescriptorMap() {
        return pdMap;
    }


    private void doPostAddProcessing(FacesContext context, UIComponent added) {

        if (parent.isInView()) {
            publishAfterViewEvents(context, context.getApplication(), added);
        }

    }

    private void doPreRemoveProcessing(FacesContext context,
                                       UIComponent toRemove) {

        if (parent.isInView()) {
            disconnectFromView(context, context.getApplication(), toRemove);
        }

    }

    //------------------------------------------------------------- BehaviorHolder stub methods.

    /**
     * behaviors associated with this component.
     */
    private BehaviorsMap behaviors;

    /**
     * <p class="changed_added_2_0">This is a default implementation of
     * {@link javax.faces.component.behavior.ClientBehaviorHolder#addClientBehavior}.
     * <code>UIComponent</code> does not implement the
     * {@link javax.faces.component.behavior.ClientBehaviorHolder} interface,
     * but provides default implementations for the methods defined by
     * {@link javax.faces.component.behavior.ClientBehaviorHolder} to simplify
     * subclass implementations.   Subclasses that wish to support the
     * {@link javax.faces.component.behavior.ClientBehaviorHolder} contract must
     * declare that the subclass implements
     * {@link javax.faces.component.behavior.ClientBehaviorHolder}, and must provide
     * an implementation of
     * {@link javax.faces.component.behavior.ClientBehaviorHolder#getEventNames}.</p>
     *
     * @param eventName the logical name of the client-side event to attach
     *        the behavior to.
     * @param  behavior the {@link javax.faces.component.behavior.Behavior} 
     * instance to attach for the specified event name.
     *
     * @since 2.0
     */
    public void addClientBehavior(String eventName, ClientBehavior behavior) {
        assertClientBehaviorHolder();
        // First, make sure that the event is supported.  We don't want
        // to bother attaching behaviors for unsupported events.

        Collection<String> eventNames = getEventNames();

        // getClientEventNames() is spec'ed to require a non-null Set.
        // If getClientEventNames() returns null, throw an exception
        // to indicate that the API in not being used properly.
        if (eventNames == null) {
            throw new IllegalStateException(
                "Attempting to add a Behavior to a component " +
                "that does not support any event types. "     +
                  "getEventTypes() must return a non-null Set.");
        }

        if (eventNames.contains(eventName)) {

            if (initialStateMarked()) {
                // a Behavior has been added dynamically.  Update existing
                // Behaviors, if any, to save their full state.
                if (behaviors != null) {
                    for (Entry<String, List<ClientBehavior>> entry : behaviors
                          .entrySet()) {
                        for (ClientBehavior b : entry.getValue()) {
                            if (b instanceof PartialStateHolder) {
                                ((PartialStateHolder) behavior).clearInitialState();
                            }
                        }
                    }
                }
            }
            // We've got an event that we support, create our Map
            // if necessary

            if (null == behaviors) {
                // Typically we only have a small number of behaviors for
                // any component - in most cases only 1.  Using a very small
                // initial capacity so that we keep the footprint to a minimum.
                Map<String, List<ClientBehavior>> modifiableMap =
                    new HashMap<String, List<ClientBehavior>>(5,1.0f);
                behaviors = new BehaviorsMap(modifiableMap);
            }

            List<ClientBehavior> eventBehaviours = behaviors.get(eventName);

            if (null == eventBehaviours) {
                // Again using small initial capacity - we typically
                // only have 1 Behavior per event type.
                eventBehaviours = new ArrayList<ClientBehavior>(3);
                behaviors.getModifiableMap().put(eventName, eventBehaviours);
            }

            eventBehaviours.add(behavior);
        }
    }

    /**
     * <p class="changed_added_2_0">This is a default implementation of
     * {@link javax.faces.component.behavior.ClientBehaviorHolder#getEventNames}.
     * <code>UIComponent</code> does not implement the
     * {@link javax.faces.component.behavior.ClientBehaviorHolder} interface,
     * but provides default implementations for the methods defined by
     * {@link javax.faces.component.behavior.ClientBehaviorHolder} to simplify
     * subclass implementations.   Subclasses that wish to support the
     * {@link javax.faces.component.behavior.ClientBehaviorHolder} contract
     * must declare that the subclass implements
     * {@link javax.faces.component.behavior.ClientBehaviorHolder}, and must
     * override this method to return a non-Empty <code>Collection</code>
     * of the client event names that the component supports.</p>
     *
     * @since 2.0
     */
    public Collection<String> getEventNames() {
        assertClientBehaviorHolder();
        // Note: we intentionally return null here even though this
        // is not a valid value.  The result is that addClientBehavior()
        // will fail with an IllegalStateException if getEventNames()
        // is not overridden.  This should make it obvious to the
        // component author that something is wrong.
        return null;
    }

    /**
     * <p class="changed_added_2_0">This is a default implementation of
     * {@link javax.faces.component.behavior.ClientBehaviorHolder#getClientBehaviors}.
     * <code>UIComponent</code> does not implement the
     * {@link javax.faces.component.behavior.ClientBehaviorHolder} interface,
     * but provides default implementations for the methods defined by
     * {@link javax.faces.component.behavior.ClientBehaviorHolder} to simplify
     * subclass implementations.   Subclasses that wish to support the
     * {@link javax.faces.component.behavior.ClientBehaviorHolder} contract
     * must declare that the subclass implements
     * {@link javax.faces.component.behavior.ClientBehaviorHolder}, and must add
     * an implementation of
     * {@link javax.faces.component.behavior.ClientBehaviorHolder#getEventNames}.</p>
     *
     * @since 2.0
     */
    public Map<String, List<ClientBehavior>> getClientBehaviors() {
        if (null == behaviors) {
            return Collections.emptyMap();
        }

        return behaviors;
    }

    /**
     * <p class="changed_added_2_0">This is a default implementation of
     * {@link javax.faces.component.behavior.ClientBehaviorHolder#getDefaultEventName}.
     * <code>UIComponent</code> does not implement the
     * {@link javax.faces.component.behavior.ClientBehaviorHolder} interface,
     * but provides default implementations for the methods defined by
     * {@link javax.faces.component.behavior.ClientBehaviorHolder} to simplify
     * subclass implementations.   Subclasses that wish to support the
     * {@link javax.faces.component.behavior.ClientBehaviorHolder} contract
     * must declare that the subclass implements
     * {@link javax.faces.component.behavior.ClientBehaviorHolder}, and must
     * provide an implementation of
     * {@link javax.faces.component.behavior.ClientBehaviorHolder#getEventNames}.</p>
     */
    public String getDefaultEventName() {
        assertClientBehaviorHolder();
        // Our default implementation just returns null - no default
        // event name;
        return null;
    }

    /**
     * {@link UIComponentBase} has stub methods from the {@link ClientBehaviorHolder} interface,
     * but these method should be used only with componets that really implement holder interface.
     * For an any other classes this method throws {@link IllegalStateException}
     * @throws IllegalStateException
     */
    private void assertClientBehaviorHolder() {
        if (!isClientBehaviorHolder()) {
            throw new IllegalStateException(
                "Attempting to use a Behavior feature with a component " +
                "that does not support any event types. "     +
                "Component must implement BehaviourHolder interface.");
        }
    }

    /**
     * @return true if component implements {@link ClientBehaviorHolder} interface.
     */
    private boolean isClientBehaviorHolder() {
        return ClientBehaviorHolder.class.isInstance(this);
    }

    /**
     * Save state of the behaviors map.
     * @param context the {@link FacesContext} for this request.
     * @return map converted to the array of <code>Object</code> or null if no behaviors have been set.
     */
    private Object saveBehaviorsState(FacesContext context){
        Object state = null;
        if (null != behaviors && behaviors.size() >0){
            boolean stateWritten = false;
            Object[] attachedBehaviors = new Object[behaviors.size()];
            int i = 0;
            for (List<ClientBehavior> eventBehaviors : behaviors.values()) {
                // we need to take different action depending on whether
                // or not markInitialState() was called.  If it's not called,
                // assume JSF 1.2 style state saving and call through to
                // saveAttachedState(), otherwise, call saveState() on the
                // behaviors directly.
                Object[] attachedEventBehaviors = new Object[eventBehaviors.size()];
                for (int j = 0; j < attachedEventBehaviors.length; j++) {
                    attachedEventBehaviors[j] = ((initialStateMarked())
                                                 ? saveBehavior(context, eventBehaviors.get(j))
                                                 : saveAttachedState(context, eventBehaviors.get(j)));
                    if (!stateWritten) {
                        stateWritten = (attachedEventBehaviors[j] != null);
                    }
                }
                attachedBehaviors[i++] = attachedEventBehaviors;
            }
            if (stateWritten) {
                state = new Object[]{behaviors.keySet().toArray(new String[behaviors.size()]),attachedBehaviors};
            }
        }
        return state;
    }

    /**
     * @param context the {@link FacesContext} for this request.
     * @param state saved state of the {@link Behavior}'s attached to the component.
     * @return restored <code>Map</code> of the behaviors.
     */
    private BehaviorsMap restoreBehaviorsState(FacesContext context, Object state) {

        if (null != state) {
            Object[] values = (Object[]) state;
            String[] names = (String[])values[0];
            Object[] attachedBehaviors = (Object[]) values[1];
            // we need to take different action depending on whether
            // or not markInitialState() was called.  If it's not called,
            // assume JSF 1.2 style state saving and call through to
            // restoreAttachedState(), otherwise, call restoreState() on the
            // behaviors directly.
            if (!initialStateMarked()) {
                Map<String, List<ClientBehavior>> modifiableMap = new HashMap<String, List<ClientBehavior>>(
                      names.length,
                      1.0f);
                for (int i = 0; i < attachedBehaviors.length; i++) {
                    Object[] attachedEventBehaviors = (Object[]) attachedBehaviors[i];
                    ArrayList<ClientBehavior> eventBehaviors =
                          new ArrayList<ClientBehavior>(attachedBehaviors.length);
                    for (int j = 0; j < attachedEventBehaviors.length; j++) {
                        eventBehaviors.add((ClientBehavior) restoreAttachedState(context,
                                                                                 attachedEventBehaviors[j]));
                    }

                    modifiableMap.put(names[i], eventBehaviors);
                }

                return new BehaviorsMap(modifiableMap);
            } else {
                for (int i = 0, len = names.length; i < len; i++) {
                    // assume the behaviors have already been populated by
                    // execution of the template.  Process the state in the
                    // same order that the names were saved.
                    List<ClientBehavior> existingBehaviors =
                          behaviors.get(names[i]);
                    restoreBehaviors(context, existingBehaviors, (Object[]) attachedBehaviors[i]);
                }
                return behaviors;
            }
        }
        return null;
    }

    private Object saveBehavior(FacesContext ctx, ClientBehavior behavior) {

        // if the Behavior isn't a StateHolder, do nothing as it will be
        // added to the BehaviorMap when the template is re-executed.
        return ((behavior instanceof StateHolder)
                ? ((StateHolder) behavior).saveState(ctx)
                : null);

    }


    private void restoreBehaviors(FacesContext ctx, List<ClientBehavior> existingBehaviors, Object[] state) {

        // this method assumes a one to one correspondence in both length and
        // order.
        for (int i = 0, len = state.length; i < len; i++) {
            ClientBehavior behavior = existingBehaviors.get(i);
            if (state[i] == null) {
                // nothing to do...move on
                continue;
            }
            // if the Behavior is a StateHolder, invoke restoreState
            // passing in the current state.  If it's not, just ignore
            // it and move along.
            if (behavior instanceof StateHolder) {
                ((StateHolder) behavior).restoreState(ctx, state[i]);
            }
        }
    }


    private static void publishAfterViewEvents(FacesContext context,
                                               Application application,
                                               UIComponent component) {

        component.setInView(true);
        try {
            component.pushComponentToEL(context, component);
            application.publishEvent(context, PostAddToViewEvent.class, component);
            if (component.getChildCount() > 0) {
                Collection<UIComponent> clist =
                      new ArrayList<UIComponent>(component.getChildren());
                for (UIComponent c : clist) {
                    publishAfterViewEvents(context, application, c);
                }
            }

            if (component.getFacetCount() > 0) {
                Collection<UIComponent> clist =
                      new ArrayList<UIComponent>(component.getFacets().values());
                for (UIComponent c : clist) {
                    publishAfterViewEvents(context, application, c);
                }
            }
        } finally {
            component.popComponentFromEL(context);
        }

    }


    private static void disconnectFromView(FacesContext context,
                                           Application application,
                                           UIComponent component) {

        application.publishEvent(context,
                                 PreRemoveFromViewEvent.class,
                                 component);
        component.setInView(false);
        component.compositeParent = null;
        if (component.getChildCount() > 0) {
            List<UIComponent> children = component.getChildren();
            for (UIComponent c : children) {
                disconnectFromView(context, application, c);
            }
        }
        if (component.getFacetCount() > 0) {
            Map<String, UIComponent> facets = component.getFacets();
            for (UIComponent c : facets.values()) {
                disconnectFromView(context, application, c);
            }
        }

    }


    // --------------------------------------------------------- Private Classes

    // For state saving
    private final static Object[] EMPTY_ARRAY = new Object[0];

    // Empty iterator for short circuiting operations
    private final static Iterator<UIComponent> EMPTY_ITERATOR = new Iterator<UIComponent>() {

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public UIComponent next() {
            throw new NoSuchElementException("Empty Iterator");
        }

        public boolean hasNext() {
            return false;
        }
    };

    // Private implementation of Map that supports the functionality
    // required by UIComponent.getFacets()
    // HISTORY:
    //   Versions 1.333 and older used inheritence to provide the
    //     basic map functionality.  This was wasteful since a
    //     component could be completely configured via ValueExpressions
    //     or (Bindings) which means an EMPTY_OBJECT_ARRAY Map would always be
    //     present when it wasn't needed.  By using composition,
    //     we control if and when the Map is instantiated thereby
    //     reducing uneeded object allocation.  This change also
    //     has a nice side effect in state saving since we no
    //     longer need to duplicate the map, we just provide the
    //     private 'attributes' map directly to the state saving process.
    private static class AttributesMap implements Map<String, Object>, Serializable {

        // this KEY is special to the AttributesMap - this allows the implementation
        // to access the the List containing the attributes that have been set
        private static final String ATTRIBUTES_THAT_ARE_SET_KEY =
                UIComponentBase.class.getName() + ".attributesThatAreSet";

        //private Map<String, Object> attributes;
        private transient Map<String, PropertyDescriptor> pdMap;
        private transient UIComponent component;
        private static final long serialVersionUID = -6773035086539772945L;

        // -------------------------------------------------------- Constructors

        private AttributesMap(UIComponent component) {

            this.component = component;
            this.pdMap = ((UIComponentBase) component).getDescriptorMap();

        }

        public boolean containsKey(Object keyObj) {
            if (ATTRIBUTES_THAT_ARE_SET_KEY.equals(keyObj)) {
                return true;
            }
            String key = (String) keyObj;
            PropertyDescriptor pd =
                    getPropertyDescriptor(key);
            if (pd == null) {
                Map<String,Object> attributes = (Map<String,Object>)
                      component.getStateHelper().get(PropertyKeys.attributes);
                if (attributes != null) {
                    return attributes.containsKey(key);
                } else {
                    return (false);
                }
            } else {
                return (false);
            }
        }

        public Object get(Object keyObj) {
            String key = (String) keyObj;
            Object result = null;
            if (key == null) {
                throw new NullPointerException();
            }
            if (ATTRIBUTES_THAT_ARE_SET_KEY.equals(key)) {
                result = component.getStateHelper().get(UIComponent.PropertyKeysPrivate.attributesThatAreSet);
            }
            Map<String,Object> attributes = (Map<String,Object>)
                  component.getStateHelper().get(PropertyKeys.attributes);
            if (null == result) {
                PropertyDescriptor pd =
                        getPropertyDescriptor(key);
                if (pd != null) {
                    try {
                        Method readMethod = pd.getReadMethod();
                        if (readMethod != null) {
                            result = (readMethod.invoke(component,
                                    EMPTY_OBJECT_ARRAY));
                        } else {
                            throw new IllegalArgumentException(key);
                        }
                    } catch (IllegalAccessException e) {
                        throw new FacesException(e);
                    } catch (InvocationTargetException e) {
                        throw new FacesException(e.getTargetException());
                    }
                } else if (attributes != null) {
                    if (attributes.containsKey(key)) {
                        result = attributes.get(key);
                    }
                }
            }
            if (null == result) {
                ValueExpression ve = component.getValueExpression(key);
                if (ve != null) {
                    try {
                        result = ve.getValue(component.getFacesContext().getELContext());
                    } catch (ELException e) {
                        throw new FacesException(e);
                    }
                }
            }

            return result;
        }

        public Object put(String keyValue, Object value) {
            if (keyValue == null) {
                throw new NullPointerException();
            }

            if (ATTRIBUTES_THAT_ARE_SET_KEY.equals(keyValue)) {
                if (component.attributesThatAreSet == null) {
                    if (value instanceof List) {
                        component.getStateHelper().put(UIComponent.PropertyKeysPrivate.attributesThatAreSet,
                                                       value);
                    }
                }
                return null;
            }

            PropertyDescriptor pd =
                    getPropertyDescriptor(keyValue);
            if (pd != null) {
                try {
                    Object result = null;
                    Method readMethod = pd.getReadMethod();
                    if (readMethod != null) {
                        result = readMethod.invoke
                                (component, EMPTY_OBJECT_ARRAY);
                    }
                    Method writeMethod = pd.getWriteMethod();
                    if (writeMethod != null) {
                        writeMethod.invoke
                                (component, value);
                    } else {
                        // TODO: i18n
                        throw new IllegalArgumentException("Setter not found for property " + keyValue);
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

                List<String> sProperties =
                      (List<String>) component.getStateHelper().get(PropertyKeysPrivate.attributesThatAreSet);
                if (sProperties == null) {
                    component.getStateHelper().add(PropertyKeysPrivate.attributesThatAreSet, keyValue);
                } else if (!sProperties.contains(keyValue)) {
                    component.getStateHelper().add(PropertyKeysPrivate.attributesThatAreSet, keyValue);
                }
                return putAttribute(keyValue, value);
            }
        }

        public void putAll(Map<? extends String, ?> map) {
            if (map == null) {
                throw new NullPointerException();
            }

            for (Map.Entry<? extends String, ?> entry : map.entrySet()) {
                this.put(entry.getKey(), entry.getValue());
            }
        }

        public Object remove(Object keyObj) {
            String key = (String) keyObj;
            if (key == null) {
                throw new NullPointerException();
            }
            if (ATTRIBUTES_THAT_ARE_SET_KEY.equals(key)) {
                return null;
            }
            PropertyDescriptor pd =
                    getPropertyDescriptor(key);
            if (pd != null) {
                throw new IllegalArgumentException(key);
            } else {
                Map<String,Object> attributes = getAttributes();
                if (attributes != null) {
                    component.getStateHelper().remove(UIComponent.PropertyKeysPrivate.attributesThatAreSet,
                                                      key);
                    return (component.getStateHelper().remove(PropertyKeys.attributes,
                                                              key));
                } else {
                    return null;
                }
            }
        }


        public int size() {
            Map attributes = getAttributes();
            return (attributes != null ? attributes.size() : 0);
        }

        public boolean isEmpty() {
            Map attributes = getAttributes();
            return (attributes == null || attributes.isEmpty());
        }

        public boolean containsValue(java.lang.Object value) {
            Map attributes= getAttributes();
            return (attributes != null && attributes.containsValue(value));
        }

        public void clear() {
            component.getStateHelper().remove(PropertyKeys.attributes);
            component.getStateHelper().remove(PropertyKeysPrivate.attributesThatAreSet);
        }

        public Set<String> keySet() {
            Map<String,Object> attributes = getAttributes();
            if (attributes != null)
                return Collections.unmodifiableSet(attributes.keySet());
            return Collections.emptySet();
        }

        public Collection<Object> values() {
            Map<String,Object> attributes = getAttributes();
            if (attributes != null)
                return Collections.unmodifiableCollection(attributes.values());
            return Collections.emptyList();
        }

        public Set<Entry<String, Object>> entrySet() {
            Map<String,Object> attributes = getAttributes();
            if (attributes != null)
                return Collections.unmodifiableSet(attributes.entrySet());
            return Collections.emptySet();
        }

        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }

            if (!(o instanceof Map)) {
                return false;
            }
            Map t = (Map) o;
            if (t.size() != size()) {
                return false;
            }

            try {
                for (Object e : entrySet()) {
                    Entry entry = (Entry) e;
                    Object key = entry.getKey();
                    Object value = entry.getValue();
                    if (value == null) {
                        if (!(t.get(key) == null && t.containsKey(key))) {
                            return false;
                        }
                    } else {
                        if (!value.equals(t.get(key))) {
                            return false;
                        }
                    }
                }
            } catch (ClassCastException unused) {
                return false;
            } catch (NullPointerException unused) {
                return false;
            }

            return true;
        }


        public int hashCode() {
            int h = 0;
            for (Object o : entrySet()) {
                h += o.hashCode();
            }
            return h;
        }

        private Map<String,Object> getAttributes() {
            return (Map<String,Object>) component.getStateHelper().get(
                  PropertyKeys.attributes);
        }

        private Object putAttribute(String key, Object value) {
            return component.getStateHelper().put(PropertyKeys.attributes,
                                                  key,
                                                  value);
        }


        /**
         * <p>Return the <code>PropertyDescriptor</code> for the specified
         * property name for this {@link UIComponent}'s implementation class,
         * if any; otherwise, return <code>null</code>.</p>
         *
         * @param name Name of the property to return a descriptor for
         * @throws FacesException if an introspection exception occurs
         */
        PropertyDescriptor getPropertyDescriptor(String name) {
            if (pdMap != null) {
                return (pdMap.get(name));
            }
            return (null);
        }

        // ----------------------------------------------- Serialization Methods

        // This is dependent on serialization occuring with in a
        // a Faces request, however, since UIComponentBase.{save,restore}State()
        // doesn't actually serialize the AttributesMap, these methods are here
        // purely to be good citizens.

        private void writeObject(ObjectOutputStream out) throws IOException {
            out.writeObject(component.getClass());
            //noinspection NonSerializableObjectPassedToObjectStream
            out.writeObject(component.saveState(FacesContext.getCurrentInstance()));
        }

        private void readObject(ObjectInputStream in)
                throws IOException, ClassNotFoundException {
            //noinspection unchecked
            Class clazz = (Class) in.readObject();
            try {
                component = (UIComponent) clazz.newInstance();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            component.restoreState(FacesContext.getCurrentInstance(), in.readObject());
        }
    }


    // Private implementation of List that supports the functionality
    // required by UIComponent.getChildren()
    private static class ChildrenList extends ArrayList<UIComponent> {

        private UIComponent component;

        public ChildrenList(UIComponent component) {
            super(6);
            this.component = component;
        }

        public void add(int index, UIComponent element) {
            if (element == null) {
                throw new NullPointerException();
            } else if ((index < 0) || (index > size())) {
                throw new IndexOutOfBoundsException();
            } else {
                eraseParent(element);
                super.add(index, element);
                element.setParent(component);

            }
        }

        public boolean add(UIComponent element) {
            if (element == null) {
                throw new NullPointerException();
            } else {
                eraseParent(element);
                boolean result = super.add(element);
                element.setParent(component);
                return result;
            }
        }

        public boolean addAll(Collection<? extends UIComponent> collection) {
            Iterator<UIComponent> elements =
                    (new ArrayList<UIComponent>(collection)).iterator();
            boolean changed = false;
            while (elements.hasNext()) {
                UIComponent element = elements.next();
                if (element == null) {
                    throw new NullPointerException();
                } else {
                    add(element);
                    changed = true;
                }
            }
            return (changed);
        }

        public boolean addAll(int index, Collection<? extends UIComponent> collection) {
            Iterator<UIComponent> elements =
                    (new ArrayList<UIComponent>(collection)).iterator();
            boolean changed = false;
            while (elements.hasNext()) {
                UIComponent element = elements.next();
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
                UIComponent child = get(i);
                child.setParent(null);
            }
            super.clear();
        }

        public Iterator<UIComponent> iterator() {
            return (new ChildrenListIterator(this));
        }

        public ListIterator<UIComponent> listIterator() {
            return (new ChildrenListIterator(this));
        }

        public ListIterator<UIComponent> listIterator(int index) {
            return (new ChildrenListIterator(this, index));
        }

        public UIComponent remove(int index) {
            UIComponent child = get(index);
            super.remove(index);
            child.setParent(null);
            return (child);
        }

        public boolean remove(Object elementObj) {
            UIComponent element = (UIComponent) elementObj;
            if (element == null) {
                throw new NullPointerException();
            }

            if (super.remove(element)) {
                element.setParent(null);
                return (true);
            } else {
                return (false);
            }
        }

        public boolean removeAll(Collection<?> collection) {
            boolean result = false;
            for (Object elements : collection) {
                if (remove(elements)) {
                    result = true;
                }
            }
            return (result);
        }

        public boolean retainAll(Collection<?> collection) {
            boolean modified = false;
            Iterator<?> items = iterator();
            while (items.hasNext()) {
                if (!collection.contains(items.next())) {
                    items.remove();
                    modified = true;
                }
            }
            return (modified);
        }

        public UIComponent set(int index, UIComponent element) {
            if (element == null) {
                throw new NullPointerException();
            } else if ((index < 0) || (index >= size())) {
                throw new IndexOutOfBoundsException();
            } else {
                eraseParent(element);
                UIComponent previous = get(index);
                super.set(index, element);
                previous.setParent(null);
                element.setParent(component);
                return (previous);
            }
        }
    }


    // Private implementation of ListIterator for ChildrenList
    private static class ChildrenListIterator implements ListIterator<UIComponent> {


        public ChildrenListIterator(ChildrenList list) {
            this.list = list;
            this.index = 0;
        }

        public ChildrenListIterator(ChildrenList list, int index) {
            this.list = list;
            if ((index < 0) || (index > list.size())) {
                throw new IndexOutOfBoundsException(String.valueOf(index));
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

        public UIComponent next() {
            try {
                UIComponent o = list.get(index);
                last = index++;
                return (o);
            } catch (IndexOutOfBoundsException e) {
                throw new NoSuchElementException(String.valueOf(index));
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

        public void add(UIComponent o) {
            last = -1;
            list.add(index++, o);
        }

        public boolean hasPrevious() {
            return (index > 1);
        }

        public int nextIndex() {
            return (index);
        }

        public UIComponent previous() {
            try {
                int current = index - 1;
                UIComponent o = list.get(current);
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

        public void set(UIComponent o) {
            if (last == -1) {
                throw new IllegalStateException();
            }
            list.set(last, o);
        }

    }


    // Private implementation of Iterator for getFacetsAndChildren()
    private final static class FacetsAndChildrenIterator implements Iterator<UIComponent> {

        private Iterator<UIComponent> iterator;
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

        public UIComponent next() {
            this.update();
            return this.iterator.next();
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

    }


    // Private implementation of Map that supports the functionality
    // required by UIComponent.getFacets()
    private static class FacetsMap extends HashMap<String, UIComponent> {

        private UIComponent component;

        public FacetsMap(UIComponent component) {
            super(3, 1.0f);
            this.component = component;
        }

        public void clear() {
            Iterator<String> keys = keySet().iterator();
            while (keys.hasNext()) {
                keys.next();
                keys.remove();
            }
            super.clear();
        }

        public Set<Map.Entry<String, UIComponent>> entrySet() {
            return (new FacetsMapEntrySet(this));
        }

        public Set<String> keySet() {
            return (new FacetsMapKeySet(this));
        }

        public UIComponent put(String key, UIComponent value) {
            if ((key == null) || (value == null)) {
                throw new NullPointerException();
            } else //noinspection ConstantConditions
                if (!(key instanceof String) ||
                        !(value instanceof UIComponent)) {
                    throw new ClassCastException();
                }
            UIComponent previous = super.get(key);
            if (previous != null) {
                previous.setParent(null);
            }
            eraseParent(value);
            UIComponent result = super.put(key, value);
            value.setParent(component);

            return (result);
        }

        public void putAll(Map<? extends String, ? extends UIComponent> map) {
            if (map == null) {
                throw new NullPointerException();
            }
            for (Map.Entry<? extends String, ? extends UIComponent> entry : map.entrySet()) {
                put(entry.getKey(), entry.getValue());
            }
        }

        public UIComponent remove(Object key) {
            UIComponent previous = get(key);
            if (previous != null) {
                previous.setParent(null);
            }
            super.remove(key);
            return (previous);
        }

        public Collection<UIComponent> values() {
            return (new FacetsMapValues(this));
        }

        Iterator<String> keySetIterator() {
            return ((new ArrayList<String>(super.keySet())).iterator());
        }

    }


    // Private implementation of Set for FacetsMap.getEntrySet()
    private static class FacetsMapEntrySet extends AbstractSet<Map.Entry<String, UIComponent>> {

        public FacetsMapEntrySet(FacetsMap map) {
            this.map = map;
        }

        private FacetsMap map = null;

        public boolean add(Map.Entry<String, UIComponent> o) {
            throw new UnsupportedOperationException();
        }

        public boolean addAll(Collection<? extends Map.Entry<String, UIComponent>> c) {
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

        public Iterator<Map.Entry<String, UIComponent>> iterator() {
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
            for (Object element : c) {
                if (remove(element)) {
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
    private static class FacetsMapEntrySetEntry implements Map.Entry<String, UIComponent> {

        public FacetsMapEntrySetEntry(FacetsMap map, String key) {
            this.map = map;
            this.key = key;
        }

        private FacetsMap map;
        private String key;

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
            UIComponent v = map.get(key);
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

        public String getKey() {
            return (key);
        }

        public UIComponent getValue() {
            return (map.get(key));
        }

        public int hashCode() {
            Object value = map.get(key);
            return (((key == null) ? 0 : key.hashCode()) ^
                    ((value == null) ? 0 : value.hashCode()));
        }

        public UIComponent setValue(UIComponent value) {
            UIComponent previous = map.get(key);
            map.put(key, value);
            return (previous);
        }

    }


    // Private implementation of Set for FacetsMap.getEntrySet().iterator()
    private static class FacetsMapEntrySetIterator implements Iterator<Map.Entry<String, UIComponent>> {

        public FacetsMapEntrySetIterator(FacetsMap map) {
            this.map = map;
            this.iterator = map.keySetIterator();
        }

        private FacetsMap map = null;
        private Iterator<String> iterator = null;
        private Map.Entry<String, UIComponent> last = null;

        public boolean hasNext() {
            return (iterator.hasNext());
        }

        public Map.Entry<String, UIComponent> next() {
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
    private static class FacetsMapKeySet extends AbstractSet<String> {

        public FacetsMapKeySet(FacetsMap map) {
            this.map = map;
        }

        private FacetsMap map = null;

        public boolean add(String o) {
            throw new UnsupportedOperationException();
        }

        public boolean addAll(Collection<? extends String> c) {
            throw new UnsupportedOperationException();
        }

        public void clear() {
            map.clear();
        }

        public boolean contains(Object o) {
            return (map.containsKey(o));
        }

        public boolean containsAll(Collection c) {
            for (Object item : c) {
                if (!map.containsKey(item)) {
                    return (false);
                }
            }
            return (true);
        }

        public boolean isEmpty() {
            return (map.isEmpty());
        }

        public Iterator<String> iterator() {
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
            for (Object item : c) {
                if (map.containsKey(item)) {
                    map.remove(item);
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
    private static class FacetsMapKeySetIterator implements Iterator<String> {

        public FacetsMapKeySetIterator(FacetsMap map) {
            this.map = map;
            this.iterator = map.keySetIterator();
        }

        private FacetsMap map = null;
        private Iterator<String> iterator = null;
        private String last = null;

        public boolean hasNext() {
            return (iterator.hasNext());
        }

        public String next() {
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
    private static class FacetsMapValues extends AbstractCollection<UIComponent> {

        public FacetsMapValues(FacetsMap map) {
            this.map = map;
        }

        private FacetsMap map;

        public boolean add(UIComponent o) {
            throw new UnsupportedOperationException();
        }

        public boolean addAll(Collection c) {
            throw new UnsupportedOperationException();
        }

        public void clear() {
            map.clear();
        }

        public boolean isEmpty() {
            return (map.isEmpty());
        }

        public Iterator<UIComponent> iterator() {
            return (new FacetsMapValuesIterator(map));
        }

        public int size() {
            return (map.size());
        }


    }


    // Private implementation of Iterator for FacetsMap.values().iterator()
    private static class FacetsMapValuesIterator implements Iterator<UIComponent> {

        public FacetsMapValuesIterator(FacetsMap map) {
            this.map = map;
            this.iterator = map.keySetIterator();
        }

        private FacetsMap map = null;
        private Iterator<String> iterator = null;
        private Object last = null;

        public boolean hasNext() {
            return (iterator.hasNext());
        }

        public UIComponent next() {
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

    // Private static member class that provide access to Behaviors.
    // Note that this Map must be unmodifiable to the external world,
    // but UIComponentBase itself needs to be able to write to the Map.
    // We solve these requirements wrapping the underlying modifiable
    // Map inside of a unmodifiable map and providing private access to
    // the underlying (modifable) Map
    private static class BehaviorsMap extends AbstractMap<String, List<ClientBehavior>>{
        private Map<String, List<ClientBehavior>> unmodifiableMap;
        private Map<String, List<ClientBehavior>> modifiableMap;

        private BehaviorsMap(Map<String, List<ClientBehavior>> modifiableMap) {
            this.modifiableMap = modifiableMap;
            this.unmodifiableMap = Collections.unmodifiableMap(modifiableMap);
        }

        public Set<Entry<String, List<ClientBehavior>>> entrySet() {
            return unmodifiableMap.entrySet();
        }

        private Map<String, List<ClientBehavior>> getModifiableMap() {
            return modifiableMap;
        }
    }

}

/*
 * $Id: UIComponent.java,v 1.58 2002/12/17 23:30:51 eburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


import java.io.IOException;
import java.io.Serializable;
import java.util.Iterator;
import javax.faces.context.FacesContext;
import javax.faces.event.RequestEvent;
import javax.faces.event.RequestEventHandler;
import javax.faces.render.Renderer;
import javax.faces.validator.Validator;


/**
 * <p><strong>UIComponent</strong> is the interface for all user interface
 * components in JavaServer Faces.  The set of <code>UIComponent</code>
 * instances associated with a particular request or response are typically
 * organized into trees under a root <code>UIComponent</code> that represents
 * the entire request or response.</p>
 *
 * <p>For the convenience of component developers, {@link UIComponentBase}
 * provides the default behavior that is specified for a
 * <code>UIComponent</code>, and is the base class for all of the standard
 * <code>UIComponent</code> implementations.</p>
 */

public interface UIComponent extends Serializable {

    public static char SEPARATOR_CHAR = '.';


    // ------------------------------------------------------------- Attributes


    /**
     * <p>Return the value of the attribute with the specified name
     * (if any); otherwise, return <code>null</code>.</p>
     *
     * @param name Name of the requested attribute
     *
     * @exception NullPointerException if <code>name</code> is
     *  <code>null</code>
     */
    public Object getAttribute(String name);


    /**
     * <p>Return an <code>Iterator</code> over the names of all
     * currently defined attributes of this <code>UIComponent</code> that
     * have a non-null value.</p>
     */
    public Iterator getAttributeNames();


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
    public void setAttribute(String name, Object value);


    // ------------------------------------------------------------- Properties


    /**
     * <p>Return the identifier of this <code>UIComponent</code>.</p>
     */
    public String getComponentId();

    /**

    * <p>Get the client side identifier for this <code>UIComponent</code> 
    * instance.</p>

    * <p>Look up this component's "clientId" attribute.  If non-null,
    * return it.  If null, see if we have a {@link Renderer} and if so,
    * delegate to it.  If we don't have a Renderer, get the component id
    * for this <code>UIComponent</code>.  If null, generate one using
    * the closest naming container that is an ancestor of this
    * UIComponent, then set the generated id as the componentId of this
    * UIComponent.  Prepend to the component id the component ids of
    * each naming container up to, but not including, the root,
    * separated by the SEPARATOR_CHAR.  In all cases, save the result as
    * the value of the "clientId" attribute.</p>

    * @throws NullPointerException if for any reason the clientSideId
    * can not be obtained.

    */

    public String getClientId(FacesContext context);


    /**
     * <p>Set the identifier of this <code>UIComponent</code>.
     *
     * @param componentId The new identifier
     *
     * @exception IllegalArgumentException if <code>componentId</code>
     *  is zero length or contains invalid characters
     * @exception NullPointerException if <code>componentId</code>
     *  is <code>null</code>
     * @exception IllegalArgumentException if this
     * <code>UIComponent</code> instance is already in the tree and and
     * is not unique within the namespace of the closest ancestor that
     * is a naming container.

     */
    public void setComponentId(String componentId);


    /**
     * <p>Return the component type of this <code>UIComponent</code>.</p>
     */
    public String getComponentType();


    /**
     * <p>Return the model reference expression of this
     * <code>UIComponent</code>, if any.</p>
     */
    public String getModelReference();


    /**
     * <p>Set the model reference expression of this
     * <code>UIComponent</code>.</p>
     *
     * @param modelReference The new model reference expression, or
     *  <code>null</code> to disconnect this component from any model data
     */
    public void setModelReference(String modelReference);


    /**
     * <p>Set the rendered attribute of this <code>UIComponent</code>.</p>
     * 
     * @param rendered If <code>true</code> render this component.
     * Otherwise, do not render this component.
     */
    public void setRendered(boolean rendered);

    
    /**
     * <p>Return <code>true</code> if the value of the 'rendered' attribute 
     * is a Boolean representing <code>true</code> or <code>null</code>, 
     * otherwise return <code>false</code>.</p>
     */
    public boolean isRendered();


    /**
     * <p>Return the parent <code>UIComponent</code> of this
     * <code>UIComponent</code>, if any.</p>
     */
    public UIComponent getParent();


    /**
     * <p>Return the {@link Renderer} type for this <code>UIComponent</code>
     * (if any).</p>
     */
    public String getRendererType();


    /**
     * <p>Set the {@link Renderer} type for this <code>UIComponent</code>,
     * or <code>null</code> for components that render themselves.</p>
     *
     * @param rendererType Logical identifier of the type of
     *  {@link Renderer} to use, or <code>null</code> for components
     *  that render themselves
     */
    public void setRendererType(String rendererType);


    /**
     * <p>Return a flag indicating whether this component is responsible
     * for rendering its child components.  The default implementation returns
     * <code>false</code>; components that want to return <code>true</code>
     * must override this method to do so. </p>
     */
    public boolean getRendersChildren();


    /**
     * <p>Return a flag indicating whether this component has concrete
     * implementations of the <code>decode()</code> and
     * <code>encodeXxx()</code> methods, and is therefore suitable for
     * use in the <em>direct implementation</em> programming model
     * for rendering.  The default implementation returns <code>false</code>;
     * components that want to return <code>true</code> must override
     * this method to do so.</p>
     */
    public boolean getRendersSelf();


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
    public boolean isValid();


    /**
     * <p>Return the local value of this <code>UIComponent</code>, if any.
     */
    public Object getValue();


    /**
     * <p>Set the local value of this <code>UIComponent</code>.</p>
     *
     * @param value The new local value
     */
    public void setValue(Object value);


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
    public Object currentValue(FacesContext context);


    // ------------------------------------------------ Tree Management Methods


    /**
     * <p>Append the specified <code>UIComponent</code> to the end of the
     * child list for this component.</p>

     * <p>If the child to be added has a non-null and valid component
     * identifier, the identifier is added to the namespace of the
     * closest ancestor that is a naming container.</p>

     *
     * @param component {@link UIComponent} to be added
     *
     * @exception IllegalArgumentException if the component identifier
     * of the new component is non-null, and is not unique in the
     * namespace of the closest ancestor that is a naming container.
     * @exception NullPointerException if <code>component</code> is null
     */
    public void addChild(UIComponent component);


    /**
     * <p>Insert the specified <code>UIComponent</code> at the specified
     * position in the child list for this component.</p>

     * <p>If the child to be added has a non-null and valid component
     * identifier, the identifier is added to the namespace of the
     * closest ancestor that is a naming container.</p>

     * @param index Zero-relative index at which to add this
     *  <code>UIComponent</code>
     * @param component Component to be added
     *
     * @exception IllegalArgumentException if the component identifier
     * of the new component is non-null, and is not unique in the
     * namespace of the closest ancestor that is a naming container.
     * @exception IndexOutOfBoundsException if the index is out of range
     *  ((index < 0) || (index &gt; size()))
     * @exception NullPointerException if <code>component</code> is null
     */
    public void addChild(int index, UIComponent component);


    /**
     * <p>Remove all child <code>UIComponent</code>s from the child list.</p>
     */
    public void clearChildren();


    /**
     * <p>Return <code>true</code> if the specified <code>UIComponent</code>
     * is a direct child of this <code>UIComponent</code>; otherwise,
     * return <code>false</code>.</p>
     *
     * @param component Component to be checked
     *
     * @exception NullPointerException if <code>component</code> is null
     */
    public boolean containsChild(UIComponent component);


    /**

     * <p>Find a component in the current component tree by asking 
     * each naming container in the tree from this component to the root 
     * for the named component.</p>

     * @return the found component, or null if the component was not
     * found.

     * @param expr The component id of the component to find

     * @exception NullPointerException if <code>expr</code>
     *  is <code>null</code>
     */
    public UIComponent findComponent(String expr);


    /**
     * <p>Return the <code>UIComponent</code> at the specified position
     * in the child list for this component.</p>
     *
     * @param index Position of the desired component
     *
     * @exception IndexOutOfBoundsException if index is out of range
     *  ((index &lt; 0) || (index &gt;= size()))
     */
    public UIComponent getChild(int index);


    /**
     * <p>Return the number of <code>UIComponent</code>s on the child list
     * for this component.</p>
     */
    public int getChildCount();


    /**
     * <p>Return an <code>Iterator</code> over the child
     * <code>UIComponent</code>s of this <code>UIComponent</code>,
     * in the order of their position in the child list.  If this
     * component has no children, an empty <code>Iterator</code>
     * is returned.</p>
     */
    public Iterator getChildren();


    /**
     * <p>Remove the child <code>UIComponent</code> at the specified position
     * in the child list for this component.</p>
     *
     * @param index Position of the component to be removed
     *
     * @exception IndexOutOfBoundsException if the index is out of range
     *  ((index < 0) || (index &gt;= size()))
     */
    public void removeChild(int index);


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
    public void removeChild(UIComponent component);


    // ----------------------------------------------- Facet Management Methods


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
    public void addFacet(UIComponent facet);


    /**
     * <p>Remove all facet <code>UIComponent</code>s from this component.
     * </p>
     */
    public void clearFacets();


    /**
     * <p>Return the facet <code>UIComponent</code> associated with the
     * specified name, if any.  Otherwise, return <code>null</code>.</p>
     *
     * @param name Name of the facet to be retrieved
     *
     * @exception NullPointerException if <code>name</code>
     *  is <code>null</code>
     */
    public UIComponent getFacet(String name);


    /**
     * <p>Return an <code>Iterator</code> over the names of the facet
     * <code>UIComponent</code>s of this <code>UIComponent</code>.  If
     * this component has no facets, an empty <code>Iterator</code> is
     * returned.</p>
     */
    public Iterator getFacetNames();


    /**
     * <p>Remove the facet <code>UIComponent</code> associated with the
     * specified name, if there is one.</p>
     *
     * @param name Name of the facet to be removed
     *
     * @exception NullPointerException if <code>name</code>
     *  is <code>null</code>
     */
    public void removeFacet(String name);


    // ------------------------------------------ Request Event Handler Methods


    /**
     * <p>Add a {@link RequestEventHandler} instance to the set associated with
     * this <code>UIComponent</code>.</p>
     *
     * @param handler The {@link RequestEventHandler} to add
     *
     * @exception NullPointerException if <code>handler</code>
     *  is null
     */
    public void addRequestEventHandler(RequestEventHandler handler);


    /**
     * <p>Clear any {@link RequestEventHandler}s that have been registered for
     * processing by this component.</p>
     */
    public void clearRequestEventHandlers();


    /**
     * <p>Return an <code>Iterator</code> over the {@link RequestEventHandler}s
     * associated with this <code>UIComponent</code>.</p>
     */
    public Iterator getRequestEventHandlers();


    /**
     * <p>Remove a {@link RequestEventHandler} instance from the set associated with
     * this <code>UIComponent</code>, if it was previously associated.
     * Otherwise, do nothing.</p>
     *
     * @param handler The {@link RequestEventHandler} to remove
     */
    public void removeRequestEventHandler(RequestEventHandler handler);


    // ----------------------------------------------------- Validators Methods


    /**
     * <p>Add a {@link Validator} instance to the set associated with
     * this <code>UIComponent</code>.</p>
     *
     * @param validator The {@link Validator} to add
     *
     * @exception NullPointerException if <code>validator</code>
     *  is null
     */
    public void addValidator(Validator validator);


    /**
     * <p>Clear any {@link Validator}s that have been registered for
     * processing by this component.</p>
     */
    public void clearValidators();


    /**
     * <p>Return an <code>Iterator</code> over the {@link Validator}s
     * associated with this <code>UIComponent</code>.</p>
     */
    public Iterator getValidators();


    /**
     * <p>Remove a {@link Validator} instance from the set associated with
     * this <code>UIComponent</code>, if it was previously associated.
     * Otherwise, do nothing.</p>
     *
     * @param validator The {@link Validator} to remove
     */
    public void removeValidator(Validator validator);


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
     * <li>Set the <code>valid</code> property of this component
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
    public boolean decode(FacesContext context) throws IOException;


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
    public void encodeBegin(FacesContext context) throws IOException;


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
    public void encodeChildren(FacesContext context) throws IOException;


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
    public void encodeEnd(FacesContext context) throws IOException;


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
    public boolean processEvent(FacesContext context, RequestEvent event);


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
    public boolean updateModel(FacesContext context);


    /**
     * <p>Perform any correctness checks that this component wishes to perform
     * on itself.  This method will be called, along with calls to all
     * {@link Validator}s registered on this component, during the
     * <em>Process Validations</em> phase of the request processing lifecycle.
     * If errors are encountered, appropriate <code>Message</code> instances
     * should be added to the {@link FacesContext} for the current request,
     * and <code>setValid(false)</code> should be called.</p>
     *
     * @param context FacesContext for the request we are processing
     *
     * @return <code>true</code> if all validations performed by this
     *  method passed successfully, or <code>false</code> if one or more
     *  validations performed by this method failed
     *
     * @exception NullPointerException if <code>context</code>
     *  is <code>null</code>
     */
    public boolean validate(FacesContext context);


    // ----------------------------------------------- Lifecycle Phase Handlers


    /**
     * <p>Perform the request component tree processing required by the
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
    public boolean processDecodes(FacesContext context) throws IOException;


    /**
     * <p>Perform the request component tree processing required by the
     * <em>Handle Request Events</em> phase of the request processing
     * lifecycle for all facets of this component, all children of this
     * component, and this component itself, as follows.</p>
     * <ul>
     * <li>Call the <code>processEvents()</code> method of all facets
     *     of this component, in the order their names would be
     *     returned by a call to <code>getFacetNames()</code>.</li>
     * <li>Call the <code>processEvents()</code> method of all children
     *     of this component, in the order they would be returned
     *     by a call to <code>getChildren()</code>.</li>
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
    public boolean processEvents(FacesContext context);


    /**
     * <p>Perform the request component tree processing required by the
     * <em>Process Validations</em> phase of the request processing
     * lifecycle for all facets of this component, all children of this
     * component, and this component itself, as follows.</p>
     * <ul>
     * <li>Call the <code>processValidators()</code> method of all facets
     *     of this component, in the order their names would be
     *     returned by a call to <code>getFacetNames()</code>.</li>
     * <li>Call the <code>processValidators()</code> method of all
     *     children of this component, in the order they would be
     *     returned by a call to <code>getChildren()</code>.</li>
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
    public boolean processValidators(FacesContext context);


    /**
     * <p>Perform the request component tree processing required by the
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
    public boolean processUpdates(FacesContext context);


}

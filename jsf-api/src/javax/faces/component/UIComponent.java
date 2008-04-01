/*
 * $Id: UIComponent.java,v 1.38 2002/07/16 22:11:28 craigmcc Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


import java.io.IOException;
import java.util.Iterator;
import javax.faces.context.FacesContext;
import javax.faces.event.FacesEvent;
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

public interface UIComponent {


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
     * currently defined attributes of this <code>UIComponent</code>.
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
     * <p>Set the identifier of this <code>UIComponent</code>.
     *
     * @param componentId The new identifier
     *
     * @exception IllegalArgumentException if <code>componentId</code>
     *  is zero length or contains invalid characters
     * @exception NullPointerException if <code>componentId</code>
     *  is <code>null</code>
     */
    public void setComponentId(String componentId);


    /**
     * <p>Return the component type of this <code>UIComponent</code>.</p>
     */
    public String getComponentType();


    /**
     * <p>Return the <em>compound identifier</em> of this component.</p>
     */
    public String getCompoundId();


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
     *
     * @param component {@link UIComponent} to be added
     *
     * @exception IllegalArgumentException if the component identifier
     *  of the new component is not unique within the children of
     *  this component
     * @exception NullPointerException if <code>component</code> is null
     */
    public void addChild(UIComponent component);


    /**
     * <p>Insert the specified <code>UIComponent</code> at the specified
     * position in the child list for this component.</p>
     *
     * @param index Zero-relative index at which to add this
     *  <code>UIComponent</code>
     * @param component Component to be added
     *
     * @exception IllegalArgumentException if the component identifier
     *  of the new component is not unique within the children of
     *  this component
     * @exception IndexOutOfBoundsException if the index is out of range
     *  ((index < 0) || (index &gt;= size()))
     * @exception NullPointerException if <code>component</code> is null
     */
    public void addChild(int index, UIComponent component);


    /**
     * <p>Remove all child <code>UIComponent</code>s from the child list.
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
     * <p>Find a related component in the current component tree by evaluating
     * the specified navigation expression (which may be absolute or relative)
     * to locate the requested component, which is then returned.
     * Valid expression values are:</p>
     * <ul>
     * <li><em>Absolute Path</em> (<code>/a/b/c</code>) - Expressions that
     *     start with a slash begin at the root component of the current tree,
     *     and match exactly against the <code>compoundId</code> of the
     *     selected component.</li>
     * <li><em>Root Component</em> - (<code>/</code>) - An expression with
     *     only a slash selects the root component of the current tree.</li>
     * <li><em>Relative Path</em> - (<code>a/b</code>) - Start at the current
     *     component (rather than the root), and navigate downward.</li>
     * <li><em>Special Path Elements</em> - A path element with a single
     *     period (".") selects the current component, while a path with two
     *     periods ("..") selects the parent of the current node.</li>
     * </ul>
     *
     * @param expr Navigation expression to interpret
     *
     * @exception IllegalArgumentException if the syntax of <code>expr</code>
     *  is invalid
     * @exception IllegalArgumentException if <code>expr</code> attempts to
     *  cause navigation to a component that does not exist
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
     * <code>UIComponent</code>s of this <code>UIComonent</code>,
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
     * object via a call to <code>getValue()</code>.  If conversion is not
     * successful:</p>
     * <ul>
     * <li>Save the state information in such a way that encoding
     *     can reproduce the previous input (even though it was syntactically
     *     or semantically incorrect)</li>
     * <li>Add an appropriate conversion failure error message by calling
     *     <code>context.addMessage()</code>.</li>
     * </ul>
     *
     * <p>During decoding, events may be enqueued for later processing
     * (by this component or some other component),  by calling
     * <code>addRequestEvent()</code> on the associated {@link FacesContext}.
     * </p>
     *
     * @param context FacesContext for the request we are processing
     *
     * @exception IOException if an input/output error occurs during decoding
     * @exception NullPointerException if <code>context</code>
     *  is <code>null</code>
     */
    public void decode(FacesContext context) throws IOException;


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
     * subclasses of <code>UIComponent</code>.  Return <code>true</code> if
     * lifecycle processing should proceed directly to the <em>Render
     * Response</em> phase once all events have been processed for all
     * components, or <code>false</code> for the normal lifecycle flow.</p>
     *
     * @param context FacesContext for the request we are processing
     * @param event Event to be processed against this component
     */
    public boolean processEvent(FacesContext context, FacesEvent event);


    /**
     * <p>Process all events queued to this <code>UIComponent</code>, by
     * calling the <code>processEvent()</code> method of the component itself,
     * followed by the <code>processEvent()</code> method of each registered
     * {@link RequestEventHandler}, for each of them.
     * Normally, component writers will not override this method -- it is
     * primarily available for use by tools.  Component writers should
     * override the <code>processEvent()</code> method instead.</p>
     *
     * <p>Return <code>true</code> if the <code>processEvent()</code> method
     * call for any queued event returned <code>true</code>; otherwise,
     * return <code>false</code>.</p>
     *
     * @param context FacesContext for the request we are processing
     */
    public boolean processEvents(FacesContext context);


    /**
     * <p>Perform all validations for this component, by calling the
     * <code>validate()</code> method of the component itself, followed by
     * the <code>validate()</code> method of each registered {@link Validator}.
     * Normally, component writers will not override this method -- it is
     * primarily available for use by tools.  Component writers should
     * override the <code>validate()</code> method instead.</p>
     *
     * @param context FacesContext for the request we are processing
     */
    public void processValidators(FacesContext context);


    /**
     * <p>If this <code>UIComponent</code> has a non-null
     * <code>modelReference</code> property, use the
     * <code>setModelValue()</code> method of the specified
     * {@link FacesContext} to update the corresponding model data
     * from the current value of this component.  This method can be
     * overridden by custom component classes when more complex update
     * logic is required.</p>
     *
     * @param context FacesContext for the request we are processing
     */
    public void updateModel(FacesContext context);


    /**
     * <p>Perform any correctness checks that this component wishes to perform
     * on itself.  This method will be called, along with calls to all
     * {@link Validator}s registered on this component, during the
     * <em>Process Validations</em> phase of the request processing lifecycle.
     * If errors are encountered, appropriate <code>Message</code> instances
     * should be added to the {@link FacesContext} for the current request.
     * </p>
     *
     * @param context FacesContext for the request we are processing
     */
    public void validate(FacesContext context);


}

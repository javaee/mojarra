/*
 * $Id: UIComponent.java,v 1.80 2003/04/29 18:51:29 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


import java.io.IOException;
import java.io.Serializable;
import java.util.Iterator;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;
import javax.faces.event.PhaseId;
import javax.faces.render.Renderer;
import javax.faces.validator.Validator;


/**
 * <p><strong>UIComponent</strong> is the base interface for all user interface
 * components in JavaServer Faces.  The set of {@link UIComponent}
 * instances associated with a particular request and response are
 * organized into a tree under a root {@link UIComponent} that represents
 * the entire content of the request or response.</p>
 *
 * <p>For the convenience of component developers, {@link UIComponentBase}
 * provides the default behavior that is specified for a
 * {@link UIComponent}, and is the base class for all of the standard
 * {@link UIComponent} implementations.  Component writers are encouraged
 * to subclass {@link UIComponentBase}, instead of directly implementing this
 * interface, to reduce the impact of any future changes to the method
 * signatures of this interface.</p>
 */

public interface UIComponent extends Serializable {


    // ----------------------------------------------------- Manifest Constants


    /**
     * <p>The separator character used in component identifiers to demarcate
     * navigation to a child naming container.</p>
     */
    public static final char SEPARATOR_CHAR = '.';


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
     * currently defined attributes of this {@link UIComponent} that
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
     * <p>Return a client-side identifier for this component, generating
     * one if necessary.  Generation will be delegated to the associated
     * {@link javax.faces.render.Renderer} (if there is one).</p>
     *
     * @param context The {@link FacesContext} for the current request
     *
     * @exception NullPointerException if <code>context</code>
     *  is <code>null</code>
     */
    public String getClientId(FacesContext context);


    /**
     * <p>Return the identifier of this {@link UIComponent}.</p>
     */
    public String getComponentId();


    /**
     * <p>Set the identifier of this {@link UIComponent}.
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
     * <p>Return the converter id of the {@link javax.faces.convert.Converter}
     * that is registered for this component.</p>
     */
    public String getConverter();


    /**
     * <p>Set the converter id of the {@link javax.faces.convert.Converter}
     * that is registered for this component, or <code>null</code> to indicate
     * that there is no registered Converter.</p>
     *
     * @param converter New converter identifier (or <code>null</code>)
     */
    public void setConverter(String converter);


    /**
     * <p>Return the parent {@link UIComponent} of this
     * <code>UIComponent</code>, if any.</p>
     */
    public UIComponent getParent();


    /**
     * <p>Set the parent <code>UIComponent</code> of this
     * <code>UIComponent</code>.</p>
     *
     * @param parent The new parent, or <code>null</code> for the root node
     *  of a component tree
     */
    public void setParent(UIComponent parent);


    /**
     * <p>Return <code>true</code> if this component (and its children)
     * should be rendered during the <em>Render Response</em> phase
     * of the request processing lifecycle.</p>
     */
    public boolean isRendered();


    /**
     * <p>Set the <code>rendered</code> property of this
     * {@link UIComponent}.</p>
     * 
     * @param rendered If <code>true</code> render this component;
     *  otherwise, do not render this component
     */
    public void setRendered(boolean rendered);

    
    /**
     * <p>Return the {@link Renderer} type for this {@link UIComponent}
     * (if any).</p>
     */
    public String getRendererType();


    /**
     * <p>Set the {@link Renderer} type for this {@link UIComponent},
     * or <code>null</code> for components that render themselves.</p>
     *
     * @param rendererType Logical identifier of the type of
     *  {@link Renderer} to use, or <code>null</code> for components
     *  that render themselves
     */
    public void setRendererType(String rendererType);


    /**
     * <p>Return a flag indicating whether this component is responsible
     * for rendering its child components.</p>
     */
    public boolean getRendersChildren();


    /**
     * <p>Return a flag indicating whether this component has concrete
     * implementations of the <code>decode()</code> and
     * <code>encodeXxx()</code> methods, and is therefore suitable for
     * use in the <em>direct implementation</em> programming model
     * for rendering.</p>
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
     * <p>Set the current validity state of this component.</p>
     *
     * @param valid The new validity state
     */
    public void setValid(boolean valid);


    // ------------------------------------------------ Tree Management Methods


    /**
     * <p>Append the specified {@link UIComponent} to the end of the
     * child list for this component.</p>
     *
     * <p>If the child to be added has a non-null and valid component
     * identifier, the identifier is added to the namespace of the
     * closest ancestor that is a naming container.</p>
     *
     * <p>It <em>is</em> valid for a component to have a null component
     * identifier or a component identifier that is the empty
     * string.</p>
     *
     *
     * @param component {@link UIComponent} to be added
     *
     * @exception IllegalStateException if the component identifier
     *  of the new component is non-null, and is not unique in the
     *  namespace of the closest ancestor that is a naming container.
     * @exception IllegalArgumentException if the component is null.

     * @exception NullPointerException if <code>component</code>
     *  is <code>null</code>
     */
    public void addChild(UIComponent component);


    /**
     * <p>Insert the specified {@link UIComponent} at the specified
     * position in the child list for this component.</p>
     *
     * <p>If the child to be added has a non-null and valid component
     * identifier, the identifier is added to the namespace of the
     * closest ancestor that is a naming container.</p>
     *
     * <p>It <em>is</em> valid for a component to have a null component
     * identifier or a component identifier that is the empty
     * string.</p>
     *
     * @param index Zero-relative index at which to add this
     *  {@link UIComponent}
     * @param component The {@link UIComponent} to be added
     *
     * @exception IllegalStateException if the component identifier
     *  of the new component is non-null, and is not unique in the
     *  namespace of the closest ancestor that is a naming container.
     * @exception IllegalArgumentException if the component is null.
     * @exception IndexOutOfBoundsException if the index is out of range
     *  ((index < 0) || (index &gt; size()))
     * @exception NullPointerException if <code>component</code> is null
     */
    public void addChild(int index, UIComponent component);


    /**
     * <p>Remove all child {@link UIComponent}s from the child list.</p>
     */
    public void clearChildren();


    /**
     * <p>Return <code>true</code> if the specified {@link UIComponent}
     * is a direct child of this {@link UIComponent}; otherwise,
     * return <code>false</code>.</p>
     *
     * @param component {@link UIComponent} to be checked
     *
     * @exception NullPointerException if <code>component</code>
     *  is <code>null</code>
     */
    public boolean containsChild(UIComponent component);


    /**
     * <p>Find the {@link UIComponent} named by the specified expression,
     * if any is found.  This is done by locating the closest parent
     * {@link UIComponent} that is a {@link NamingContainer}, and
     * calling its <code>findComponentInNamespace()</code> method.</p>
     *
     * <p>The specified <code>expr</code> may contain either a
     * component identifier, or a set of component identifiers separated
     * by SEPARATOR_CHAR characters.</p>
     *
     * @param expr Expression identifying the {@link UIComponent}
     *  to be returned
     *
     * @return the found {@link UIComponent}, or <code>null</code>
     *  if the component was not found.
     *
     * @exception NullPointerException if <code>expr</code>
     *  is <code>null</code>
     */
    public UIComponent findComponent(String expr);


    /**
     * <p>Return the {@link UIComponent} at the specified position
     * in the child list for this component.</p>
     *
     * @param index Position of the desired component
     *
     * @exception IndexOutOfBoundsException if index is out of range
     *  ((index &lt; 0) || (index &gt;= size()))
     */
    public UIComponent getChild(int index);


    /**
     * <p>Return the number of {@link UIComponent}s on the child list
     * for this component.</p>
     */
    public int getChildCount();


    /**
     * <p>Return an <code>Iterator</code> over the child
     * {@link UIComponent}s of this {@link UIComponent},
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
     * <p>Remove the child {@link UIComponent} from the child list
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
     * <p>Add the specified {@link UIComponent} as a facet
     * associated with the name specified by the <code>facetName</code>
     * argument, replacing any previous facet with that name.  The
     * newly added <code>facet</code> will have its <code>parent</code>
     * property set to this component.</p>
     *
     * @param facetName The name of this facet
     * @param facet The new facet {@link UIComponent}
     *
     * @exception NullPointerException if the either of the
     * <code>facetName</code> or <code>facet</code> arguments are
     * <code>null</code>.
     */
    public void addFacet(String facetName, UIComponent facet);


    /**
     * <p>Remove all facet {@link UIComponent}s from this component.
     * </p>
     */
    public void clearFacets();


    /**
     * <p>Return the facet {@link UIComponent} associated with the
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
     * {@link UIComponent}s of this {@link UIComponent}.  If
     * this component has no facets, an empty <code>Iterator</code> is
     * returned.</p>
     */
    public Iterator getFacetNames();


    /**
     * <p>Remove the facet {@link UIComponent} associated with the
     * specified name, if there is one.  The removed <code>facet</code>
     * will have its <code>parent</code> property cleared.</p>
     *
     * @param name Name of the facet to be removed
     *
     * @exception NullPointerException if <code>name</code>
     *  is <code>null</code>
     */
    public void removeFacet(String name);
    

    /**
     * <p>Return an <code>Iterator</code> over the facet followed by child
     * {@link UIComponent}s of this {@link UIComponent}.
     * Facets are returned in an undefined order, followed by
     * all the children in the order they are stored in the child list. If this
     * component has no facets or children, an empty <code>Iterator</code>
     * is returned.</p>
     */
    public Iterator getFacetsAndChildren();
    
    
    // ----------------------------------------------------- Validators Methods


    /**
     * <p>Add a {@link Validator} instance to the set associated with
     * this {@link UIComponent}.</p>
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
     * associated with this {@link UIComponent}.</p>
     */
    public Iterator getValidators();


    /**
     * <p>Remove a {@link Validator} instance from the set associated with
     * this {@link UIComponent}, if it was previously associated.
     * Otherwise, do nothing.</p>
     *
     * @param validator The {@link Validator} to remove
     */
    public void removeValidator(Validator validator);


    // ------------------------------------------- Lifecycle Processing Methods


    /**
     * <p>Broadcast the specified {@link FacesEvent} to all registered
     * event listeners who have expressed an interest in events of this
     * type, for the specified {@link PhaseId} (or for any phase, if the
     * listener returns <code>PhaseId.ANY_PHASE</code> from its
     * <code>getPhaseId()</code> method.  The order in which
     * registered listeners are notified is implementation dependent.</p>
     *
     * <p>After all interested listeners have been notified, return
     * <code>false</code> if this event does not have any listeners
     * interested in this event in future phases of the request processing
     * lifecycle.  Otherwise, return <code>true</code>.</p>
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
    public abstract boolean broadcast(FacesEvent event, PhaseId phaseId)
        throws AbortProcessingException;


    /**
     * <p>Decode the current state of this {@link UIComponent} from the
     * request contained in the specified {@link FacesContext}, and attempt
     * to convert this state information into an object of the required type
     * for this component (optionally using the registered
     * {@link javax.faces.convert.Converter} for this component, if there
     * is one.</p>
     *
     * <p>If conversion is successful:</p>
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
     * @param context {@link FacesContext} for the request we are processing
     *
     * @exception IOException if an input/output error occurs during decoding
     * @exception NullPointerException if <code>context</code>
     *  is <code>null</code>
     */
    public void decode(FacesContext context) throws IOException;


    /**
     * <p>Render the beginning of the current state of this
     * {@link UIComponent} to the response contained in the specified
     * {@link FacesContext}.  If the conversion attempted in a previous call
     * to <code>decode()</code> for this component failed, the state
     * information saved during execution of <code>decode()</code> should be
     * utilized to reproduce the incorrect input.  If the conversion was
     * successful, or if there was no previous call to <code>decode()</code>,
     * the value to be displayed should be acquired by calling
     * <code>currentValue()</code>, and rendering the value as appropriate.
     * </p>
     *
     * @param context {@link FacesContext} for the response we are creating
     *
     * @exception IOException if an input/output error occurs while rendering
     * @exception NullPointerException if <code>context</code>
     *  is <code>null</code>
     */
    public void encodeBegin(FacesContext context) throws IOException;


    /**
     * <p>Render the child {@link UIComponent}s of this {@link UIComponent},
     * following the rules described for <code>encodeBegin()</code> to acquire
     * the appropriate value to be rendered.  This method will only be called
     * if the <code>rendersChildren</code> property is <code>true</code>.</p>
     *
     * @param context {@link FacesContext} for the response we are creating
     *
     * @exception IOException if an input/output error occurs while rendering
     * @exception NullPointerException if <code>context</code>
     *  is <code>null</code>
     */
    public void encodeChildren(FacesContext context) throws IOException;


    /**
     * <p>Render the ending of the current state of this
     * {@link UIComponent}, following the rules described for
     * <code>encodeBegin()</code> to acquire the appropriate value
     * to be rendered.</p>
     *
     * @param context {@link FacesContext} for the response we are creating
     *
     * @exception IOException if an input/output error occurs while rendering
     * @exception NullPointerException if <code>context</code>
     *  is <code>null</code>
     */
    public void encodeEnd(FacesContext context) throws IOException;


    /**
     * <p>Perform any processing required to correct the state of this
     * component as a result of the owning component tree having been
     * reconstructed during <em>Reconstitute Component Tree</em> phase
     * of the request processing lifecycle.  The default implementation
     * of this method does nothing.</p>
     *
     * @param context {@link FacesContext} for the request we are processing
     *
     * @exception IOException if an input/output error occurs during processing
     * @exception NullPointerException if <code>context</code>
     *  is <code>null</code>
     */
    public void reconstitute(FacesContext context) throws IOException;


    /**
     * <p>Update the model data associated with this {@link UIComponent},
     * if any.  The default implementation in {@link UIComponentBase} does
     * no processing; concrete implementations of components that represent
     * controls that can be altered by users (such as {@link UIInput} must
     * provide an apppropriate implementation of this method.</p>
     *
     * @param context {@link FacesContext} for the request we are processing
     *
     * @exception NullPointerException if <code>context</code>
     *  is <code>null</code>
     */
    public void updateModel(FacesContext context);


    /**
     * <p>Perform any correctness checks that this component wishes to perform
     * on itself.  This method will be called during the
     * <em>Process Validations</em> phase of the request processing
     * lifecycle.  If errors are encountered, appropriate <code>Message</code>
     * instances should be added to the {@link FacesContext} for the current
     * request, and the <code>valid</code> property of this {@link UIComponent}
     * should be set to <code>false</code>.</p>
     *
     * @param context {@link FacesContext} for the request we are processing
     *
     * @exception NullPointerException if <code>context</code>
     *  is <code>null</code>
     */
    public void validate(FacesContext context);


    // ----------------------------------------------- Lifecycle Phase Handlers


    /**
     * <p>Perform the component tree processing required by the
     * <em>Reconstitute Component Tree</em> phase of the request processing
     * lifecycle for all facets of this component, all children of this
     * component, and this component itself, as follows.</p>
     * <ul>
     * <li>Call the <code>processReconstitutes()</code> method of all facets
     *     and children of this {@link UIComponent} in the order determined
     *     by a call to <code>getFacetsAndChildren()</code>.</li>
     * <li>Call the <code>reconstitute()</code> method of this component.</li>
     * </ul>
     *
     * @param context {@link FacesContext} for the request we are processing
     *
     * @exception IOException if an input/output error occurs during processing
     * @exception NullPointerException if <code>context</code>
     *  is <code>null</code>
     */
    public void processReconstitutes(FacesContext context) throws IOException;


    /**
     * <p>Perform the component tree processing required by the
     * <em>Apply Request Values</em> phase of the request processing
     * lifecycle for all facets of this component, all children of this
     * component, and this component itself, as follows.</p>
     * <ul>
     * <li>Call the <code>processDecodes()</code> method of all facets
     *     and children of this {@link UIComponent}, in the order determined
     *     by a call to <code>getFacetsAndChildren()</code>.</li>
     * <li>Call the <code>decode()</code> method of this component.</li>
     *
     * @param context {@link FacesContext} for the request we are processing
     *
     * @exception IOException if an input/output error occurs during decoding
     * @exception NullPointerException if <code>context</code>
     *  is <code>null</code>
     */
    public void processDecodes(FacesContext context) throws IOException;


    /**
     * <p>Perform the component tree processing required by the
     * <em>Process Validations</em> phase of the request processing
     * lifecycle for all facets of this component, all children of this
     * component, and this component itself, as follows.</p>
     * <ul>
     * <li>Call the <code>processValidators()</code> method of all facets
     *     and children of this {@link UIComponent}, in the order determined
     *     by a call to <code>getFacetsAndChildren()</code>.</li>
     * <li>If the <code>valid</code> property of this component is
     *     currently <code>true</code>:
     *     <ul>
     *     <li>Call the <code>validate()</code> method of each
     *         {@link Validator} registered for this {@link UIComponent}.</li>
     *     <li>Call the <code>validate()</code> method of this component.</li>
     *     <li>Set the <code>valid</code> property of this component
     *         to the result returned from the <code>validate()</code>
     *         method.</li>
     *     </ul></li>
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
    public void processValidators(FacesContext context);


    /**
     * <p>Perform the component tree processing required by the
     * <em>Update Model Values</em> phase of the request processing
     * lifecycle for all facets of this component, all children of this
     * component, and this component itself, as follows.</p>
     * <ul>
     * <li>Call the <code>processUpdates()</code> method of all facets
     *     and children of this {@link UIComponent}, in the order determined
     *     by a call to <code>getFacetsAndChildren()</code>.</li>
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
    public void processUpdates(FacesContext context);


}

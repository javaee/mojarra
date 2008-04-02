/*
 * $Id: UIComponent.java,v 1.113 2003/11/01 00:46:05 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;
import javax.faces.event.FacesListener;
import javax.faces.event.PhaseId;
import javax.faces.render.Renderer;


/**
 * <p><strong>UIComponent</strong> is the base class for all user interface
 * components in JavaServer Faces.  The set of {@link UIComponent} instances
 * associated with a particular request and response are organized into a
 * component tree under a {@link UIViewRoot} that represents
 * the entire content of the request or response.</p>
 *
 * <p>For the convenience of component developers,
 * {@link UIComponentBase} provides the default
 * behavior that is specified for a {@link UIComponent}, and is the base class
 * for all of the concrete {@link UIComponent} "base" implementations.
 * Component writers are encouraged to subclass
 * {@link UIComponentBase}, instead of directly
 * implementing this abstract class, to reduce the impact of any future changes
 * to the method signatures.</p>
 */

public abstract class UIComponent implements StateHolder {

    // -------------------------------------------------------------- Attributes


    /**
     * <p>Return a mutable <code>Map</code> representing the attributes
     * (and properties, see below) associated wth this {@link UIComponent},
     * keyed by attribute name (which must be a String).  The returned
     * implementation must support all of the standard and optional
     * <code>Map</code> methods, plus support the following additional
     * requirements:</p>
     * <ul>
     * <li>The <code>Map</code> implementation must implement
     *     the <code>java.io.Serializable</code> interface.</li>
     * <li>Any attempt to add a <code>null</code> key or value must
     *     throw a <code>NullPointerException</code>.</li>
     * <li>Any attempt to add a key that is not a String must throw
     *     a <code>ClassCastException</code>.</li>
     * <li>If the attribute name specified as a key matches a property
     *     of this {@link UIComponent}'s implementation class, the following
     *     methods will have special behavior:
     *     <ul>
     *     <li><code>containsKey</code> - Return <code>false</code>.</li>
     *     <li><code>get()</code> - If the property is readable, call
     *         the getter method and return the returned value (wrapping
     *         primitive values in their corresponding wrapper classes);
     *         otherwise throw <code>IllegalArgumentException</code>.</li>
     *     <li><code>put()</code> - If the property is writeable, call
     *         the setter method to set the corresponding value (unwrapping
     *         primitive values in their corresponding wrapper classes).
     *         If the property is not writeable, or an attempt is made to
     *         set a property of primitive type to <code>null</code>,
     *         throw <code>IllegalArgumentException</code>.</li>
     *     <li><code>remove</code> - Throw
     *         <code>IllegalArgumentException</code>.</li>
     *     </ul></li>
     * </ul>
     */
    public abstract Map getAttributes();


    // -------------------------------------------------------------- Properties


    /**
     * <p>Return a client-side identifier for this component, generating
     * one if necessary.  The associated {@link
     * javax.faces.render.Renderer}, if present, will be asked to
     * convert the clientId to a form suitable for transmission to the
     * client.</p>
     *
     * @param context The {@link FacesContext} for the current request
     *
     * @exception NullPointerException if <code>context</code>
     *  is <code>null</code>
     */
    public abstract String getClientId(FacesContext context);


    /**
     * <p>Return the component reference expression for this {@link UIComponent}
     * (if any).</p>
     */
    public abstract String getComponentRef();


    /**
     * <p>Set the component reference expression for this {@link UIComponent}.
     * </p>
     *
     * @param componentRef The new component reference expression, or
     *  <code>null</code> for no component reference expression
     */
    public abstract void setComponentRef(String componentRef);


    /**
     * <p>Return the component identifier of this {@link UIComponent}.</p>
     */
    public abstract String getId();


    /**
     * <p>Set the component identifier of this {@link UIComponent} (if any).
     * Component identifiers must obey the following syntax restrictions:</p>
     * <ul>
     * <li>Must not be a zero-length String.</li>
     * <li>First character must be a letter or an underscore ('_').</li>
     * <li>Subsequent characters must be a letter, a digit,
     *     an underscore ('_'), or a dash ('-').</li>
     * <li>
     * </ul>
     *
     * <p>Component identifiers must also obey the following semantic
     * restrictions (note that this restriction is <strong>NOT</strong>
     * enforced by the <code>setId()</code> implementation):</p>
     * <ul>
     * <li>The specified identifier must be unique among all the components
     *     (including facets) that are descendents of the nearest ancestor
     *     {@link UIComponent} that is a {@link NamingContainer}, or within
     *     the scope of the entire component tree if there is no such
     *     ancestor that is a {@link NamingContainer}.</li>
     * </ul>
     *
     * @param id The new component identifier, or <code>null</code> to indicate
     *  that this {@link UIComponent} does not have a component identifier
     *
     * @exception IllegalArgumentException if <code>id</code> is not
     *  syntactically valid
     */
    public abstract void setId(String id);


    /**
     * <p>Return the parent {@link UIComponent} of this
     * <code>UIComponent</code>, if any.</p>
     */
    public abstract UIComponent getParent();


    /**
     * <p>Set the parent <code>UIComponent</code> of this
     * <code>UIComponent</code>.</p>
     *
     * @param parent The new parent, or <code>null</code> for the root node
     *  of a component tree
     */
    public abstract void setParent(UIComponent parent);


    /**
     * <p>Return <code>true</code> if this component (and its children)
     * should be rendered during the <em>Render Response</em> phase
     * of the request processing lifecycle.</p>
     */
    public abstract boolean isRendered();


    /**
     * <p>Set the <code>rendered</code> property of this
     * {@link UIComponent}.</p>
     * 
     * @param rendered If <code>true</code> render this component;
     *  otherwise, do not render this component
     */
    public abstract void setRendered(boolean rendered);

    
    /**
     * <p>Return the {@link Renderer} type for this {@link UIComponent}
     * (if any).</p>
     */
    public abstract String getRendererType();


    /**
     * <p>Set the {@link Renderer} type for this {@link UIComponent},
     * or <code>null</code> for components that render themselves.</p>
     *
     * @param rendererType Logical identifier of the type of
     *  {@link Renderer} to use, or <code>null</code> for components
     *  that render themselves
     */
    public abstract void setRendererType(String rendererType);


    /**
     * <p>Return a flag indicating whether this component is responsible
     * for rendering its child components.  The default implementation
     * in {@link UIComponentBase#getRendersChildren} tries to find the
     * renderer for this component.  If it does, it calls {@link
     * Renderer#getRendersChildren} and returns the result.  If it
     * doesn't, it returns false.</p>
     */
    public abstract boolean getRendersChildren();


    // ------------------------------------------------- Tree Management Methods


    /**
     * <p>Return a mutable <code>List</code> representing the child
     * {@link UIComponent}s associated with this component.  The returned
     * implementation must support all of the standard and optional
     * <code>List</code> methods, plus support the following additional
     * requirements:</p>
     * <ul>
     * <li>The <code>List</code> implementation must implement
     *     the <code>java.io.Serializable</code> interface.</li>
     * <li>Any attempt to add a <code>null</code> must throw
     *     a NullPointerException</li>
     * <li>Any attempt to add an object that does not implement
     *     {@link UIComponent} must throw a ClassCastException.</li>
     * <li>Whenever a new child component is added, the <code>parent</code>
     *     property of the child must be set to this component instance.</li>
     * <li>Whenever an existing child component is removed, the
     *     <code>parent</code> property of the child must be set to
     *     <code>null</code>.</li>
     * </ul>
     */
    public abstract List getChildren();


    /**
     * <p>Return the number of child {@link UIComponent}s that are associated
     * with this {@link UIComponent}.</p>
     */
    public abstract int getChildCount();


    /**
     * <p>Search for and return the {@link UIComponent} with an <code>id</code>
     * that matches the specified search expression (if any), according to the
     * algorithm described below.</p>
     *
     * <p>Component identifiers are required to be unique within the scope of
     * the closest ancestor {@link NamingContainer} that encloses this
     * component (which might be this component itself).  If there are no
     * {@link NamingContainer} components in the ancestry of this component,
     * the root component in the tree is treated as if it were a
     * {@link NamingContainer}, whether or not its class actually implements
     * the {@link NamingContainer} interface.</p>
     *
     * <p>A <em>search expression</em> consists of either an
     * identifier (which is matched exactly against the <code>id</code>
     * property of a {@link UIComponent}, or a series of such identifiers
     * linked by the {@link NamingContainer#SEPARATOR_CHAR} character value.
     * The search algorithm operates as follows:</p>
     * <ul>
     * <li>Identify the {@link UIComponent} that will be the base for searching,
     *     by stopping as soon as one of the following conditions is met:
     *     <ul>
     *     <li>If the search expression begins with the the separator character
     *         (called an "absolute" search expression),
     *         the base will be the root {@link UIComponent} of the component
     *         tree.  The leading separator character will be stripped off,
     *         and the remainder of the search expression will be treated as
     *         a "relative" search expression as described below.</li>
     *     <li>Otherwise, if this {@link UIComponent} is a
     *         {@link NamingContainer} it will serve as the basis.</li>
     *     <li>Otherwise, search up the parents of this component.  If
     *         a {@link NamingContainer} is encountered, it will be the base.
     *         </li>
     *     <li>Otherwise (if no {@link NamingContainer} is encountered)
     *         the root {@link UIComponent} will be the base.</li>
     *     </ul></li>
     * <li>The search expression (possibly modified in the previous step) is now
     *     a "relative" search expression that will be used to locate the
     *     component (if any) that has an <code>id</code> that matches, within
     *     the scope of the base component.  The match is performed as follows:
     *     <ul>
     *     <li>If the search expression is a simple identifier, this value is
     *         compared to the <code>id</code> property, and then recursively
     *         through the facets and children of the base {@link UIComponent}
     *         (except that if a descendant {@link NamingContainer} is found,
     *         its own facets and children are not searched).</li>
     *     <li>If the search expression includes more than one identifier
     *         separated by the separator character, the first identifier is
     *         used to locate a {@link NamingContainer} by the rules in the
     *         previous bullet point.  Then, the <code>findComponent()</code>
     *         method of this {@link NamingContainer} will be called, passing
     *         the remainder of the search expression.</li>
     *     </ul></li>
     * </ul>
     *
     * @param expr Search expression identifying the {@link UIComponent}
     *  to be returned
     *
     * @return the found {@link UIComponent}, or <code>null</code>
     *  if the component was not found.
     *
     * @exception IllegalArgumentException if an intermediate identifier
     *  in a search expression identifies a {@link UIComponent} that is
     *  not a {@link NamingContainer}
     * @exception NullPointerException if <code>expr</code>
     *  is <code>null</code>
     */
    public abstract UIComponent findComponent(String expr);


    // ------------------------------------------------ Facet Management Methods


    /**
     * <p>Return a mutable <code>Map</code> representing the facet
     * {@link UIComponent}s associated with this {@link UIComponent},
     * keyed by facet name (which must be a String).  The returned
     * implementation must support all of the standard and optional
     * <code>Map</code> methods, plus support the following additional
     * requirements:</p>
     * <ul>
     * <li>The <code>Map</code> implementation must implement
     *     the <code>java.io.Serializable</code> interface.</li>
     * <li>Any attempt to add a <code>null</code> key or value must
     *     throw a NullPointerException.</li>
     * <li>Any attempt to add a key that is not a String must throw
     *     a ClassCastException.</li>
     * <li>Any attempt to add a value that is not a {@link UIComponent}
     *     must throw a ClassCastException.</li>
     * <li>Whenever a new facet {@link UIComponent} is added:
     *     <ul>
     *     <li>The <code>parent</code> property of the component must be set to
     *         this component instance.</li>
     *     </ul></li>
     * <li>Whenever an existing facet {@link UIComponent} is removed:
     *     <ul>
     *     <li>The <code>parent</code> property of the facet must be
     *         set to <code>null</code>.</li>
     *     </ul></li>
     * </ul>
     */
    public abstract Map getFacets();


    /**
     * <p>Convenience method to return the named facet, if it exists,
     * or <code>null</code> otherwise.</p>
     *
     * @param name Name of the desired facet
     */
    public abstract UIComponent getFacet(String name);


    /**
     * <p>Return an <code>Iterator</code> over the facet followed by child
     * {@link UIComponent}s of this {@link UIComponent}.
     * Facets are returned in an undefined order, followed by
     * all the children in the order they are stored in the child list. If this
     * component has no facets or children, an empty <code>Iterator</code>
     * is returned.</p>
     *
     * <p>The returned <code>Iterator</code> must not support the
     * <code>remove()</code> operation.</p>
     */
    public abstract Iterator getFacetsAndChildren();
    
    
    // -------------------------------------------- Lifecycle Processing Methods


    /**
     * <p>Broadcast the specified {@link FacesEvent} to all registered
     * event listeners who have expressed an interest in events of this
     * type, for the specified {@link PhaseId}.  The order in which registered
     * listeners are notified must be:</p>
     * <ul>
     * <li>Listeners whose <code>getPhaseId()</code> method returns
     *     <code>PhaseId.ANY_PHASE</code>, in the order that they were
     *     registered.  However, the implementation must ensure that the same
     *     event is not delivered to the same <code>PhaseId.ANY_PHASE</code>
     *     listener more than once.</li>
     * <li>Listeners whose <code>getPhaseId()</code> method returns
     *     the <code>PhaseId</code> specified on this method call,
     *     in the order that they were registered.</li>
     * </ul>
     *
     * <p>After all interested listeners have been notified, return
     * <code>false</code> if this event does not have any listeners
     * interested in this event in future phases of the request processing
     * lifecycle.  Otherwise, return <code>true</code>.  Note that listeners
     * registered for <code>PhaseId.ANY_PHASE</code> are not counted as
     * being interested in this event in a future phase (because they have
     * already been notified about it.  However, if there are other
     * listeners registered for a specific future phase, the
     * <code>ANY_PHASE</code> listener will be notified of the event
     * more than once.</p>
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
     * <code>queueEvent()</code>.</p>
     *
     * @param context {@link FacesContext} for the request we are processing
     *
     * @exception NullPointerException if <code>context</code>
     *  is <code>null</code>
     */
    public abstract void decode(FacesContext context);


    /**
     * <p>If our <code>rendered</code> property is <code>true</code>,
     * render the beginning of the current state of this
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
    public abstract void encodeBegin(FacesContext context) throws IOException;


    /**
     * <p>If our <code>rendered</code> property is <code>true</code>,
     * render the child {@link UIComponent}s of this {@link UIComponent},
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
    public abstract void encodeChildren(FacesContext context) throws IOException;


    /**
     * <p>If our <code>rendered</code> property is <code>true</code>,
     * render the ending of the current state of this
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
    public abstract void encodeEnd(FacesContext context) throws IOException;


    // -------------------------------------------------- Event Listener Methods


    /**
     * <p>Add the specified {@link FacesListener} to the set of listeners
     * registered to receive event notifications from this {@link UIComponent}.
     * It is expected that {@link UIComponent} classes acting as event sources
     * will have corresponding typesafe APIs for registering listeners of the
     * required type, and the implementation of those registration methods
     * will delegate to this method.  For example:</p>
     * <pre>
     * public class FooEvent extends FacesEvent { ... }
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
     * @exception NullPointerException if <code>listener</code>
     *  is <code>null</code>
     */
    protected abstract void addFacesListener(FacesListener listener);


    /**
     * <p>Return the set of registered {@link FacesListener}s that are
     * instances of the specified class.  If there are no such registered
     * listeners, a zero-length array is returned.</p>
     *
     * @param clazz Class that must be implemented by a {@link FacesListener}
     *  for it to be returned
     *
     * @exception IllegalArgumentException if <code>class</code> is not,
     *  and does not implement, {@link FacesListener}
     * @exception NullPointerException if <code>clazz</code>
     *  is <code>null</code>
     */
    protected abstract FacesListener[] getFacesListeners(Class clazz);


    /**
     * <p>Remove the specified {@link FacesListener} from the set of listeners
     * registered to receive event notifications from this {@link UIComponent}.
     *
     * @param listener The {@link FacesListener} to be deregistered
     *
     * @exception NullPointerException if <code>listener</code>
     *  is <code>null</code>
     */
    protected abstract void removeFacesListener(FacesListener listener);


    /**
     * <p>Queue an event for broadcast at the end of the current request
     * processing lifecycle phase.  The default implementation in
     * {@link UIComponentBase} must delegate this call to the
     * <code>queueEvent()</code> method of the parent {@link UIComponent}.</p>
     *
     * @param event {@link FacesEvent} to be queued
     *
     * @exception IllegalStateException if this component is not a
     *  descendant of a {@link UIViewRoot}
     * @exception NullPointerException if <code>event</code>
     *  is <code>null</code>
     */
    public abstract void queueEvent(FacesEvent event);


    // ------------------------------------------------ Lifecycle Phase Handlers


    /**
     * <p>Perform the component tree processing required by the
     * <em>Restore View</em> phase of the request processing
     * lifecycle for all facets of this component, all children of this
     * component, and this component itself, as follows.</p>
     * <ul>
     * <li>Call the <code>processRestoreState()</code> method of all
     * facets and children of this {@link UIComponent} in the order
     * determined by a call to <code>getFacetsAndChildren()</code>.</li>
     * <li>Call the <code>restoreState()</code> method of this component.</li>
     * </ul>
     *
     * @param context {@link FacesContext} for the request we are processing
     *
     * @exception NullPointerException if <code>context</code>
     *  is <code>null</code>
     */
    public abstract void processRestoreState(FacesContext context,
                                             Object state);


    /**
     * <p>Perform the component tree processing required by the
     * <em>Apply Request Values</em> phase of the request processing
     * lifecycle for all facets of this component, all children of this
     * component, and this component itself, as follows.</p>
     * <ul>
     * <li>If the <code>rendered</code> property of this {@link UIComponent}
     *     is <code>false</code>, skip further processing.</li>
     * <li>Call the <code>processDecodes()</code> method of all facets
     *     and children of this {@link UIComponent}, in the order determined
     *     by a call to <code>getFacetsAndChildren()</code>.</li>
     * <li>Call the <code>decode()</code> method of this component,
     *     if this component's <code>rendered</code> property is
     *     <code>true</code> and it is not nested in a parent component
     *     whose <code>rendersChildren</code> property is <code>true</code>
     *     but whose <code>rendered</code> property is <code>false</code>.</li>
     * <li>If a <code>RuntimeException</code> is thrown during
     *     decode processing, call {@link FacesContext#renderResponse}
     *     and re-throw the exception.</li>
     * </ul>
     *
     * @param context {@link FacesContext} for the request we are processing
     *
     * @exception NullPointerException if <code>context</code>
     *  is <code>null</code>
     */
    public abstract void processDecodes(FacesContext context);


    /**
     * <p>Perform the component tree processing required by the
     * <em>Process Validations</em> phase of the request processing
     * lifecycle for all facets of this component, all children of this
     * component, and this component itself, as follows.</p>
     * <ul>
     * <li>If the <code>rendered</code> property of this {@link UIComponent}
     *     is <code>false</code>, skip further processing.</li>
     * <li>Call the <code>processValidators()</code> method of all facets
     *     and children of this {@link UIComponent}, in the order determined
     *     by a call to <code>getFacetsAndChildren()</code>.</li>
     * <li>If the current component is an {@link UIInput}, call its
     *     <code>validate()</code> method, if this component's
     *     <code>rendered</code> property is <code>true</code> and it is
     *     not nested in a parent component whose <code>rendersChildren</code>
     *     property is <code>true</code> but whose <code>rendered</code>
     *     property is <code>false</code>.</li>
     * <li>If the <code>isValid()</code> method of this component returns
     *     <code>false</code>, call the <code>renderResponse()</code> method
     *     on the {@link FacesContext} instance for this request.</li>
     * <li>If a <code>RuntimeException</code> is thrown during
     *     validation processing, call {@link FacesContext#renderResponse}
     *     and re-throw the exception.</li>
     *
     * </ul>
     *
     * @param context {@link FacesContext} for the request we are processing
     *
     * @exception NullPointerException if <code>context</code>
     *  is <code>null</code>
     */
    public abstract void processValidators(FacesContext context);


    /**
     * <p>Perform the component tree processing required by the
     * <em>Update Model Values</em> phase of the request processing
     * lifecycle for all facets of this component, all children of this
     * component, and this component itself, as follows.</p>
     * <ul>
     * <li>If the <code>rendered</code> property of this {@link UIComponent}
     *     is <code>false</code>, skip further processing.</li>
     * <li>Call the <code>processUpdates()</code> method of all facets
     *     and children of this {@link UIComponent}, in the order determined
     *     by a call to <code>getFacetsAndChildren()</code>.</li>
     * <li>Call the <code>updateModel()</code> method of this component,
     *     if this component's <code>rendered</code> property is
     *     <code>true</code> and it is not nested in a parent component
     *     whose <code>rendersChildren</code> property is <code>true</code>
     *     but whose <code>rendered</code> property is <code>false</code>.</li>
     * <li>If the <code>valid</code> property of this {@link UIComponent}
     *     is now <code>false</code>, call
     *     <code>FacesContext.renderResponse()</code>
     *     to transfer control at the end of the current phase.</li>
     * <li>If a <code>RuntimeException</code> is thrown during
     *     update model processing, call {@link FacesContext#renderResponse}
     *     and re-throw the exception.</li>
     * </ul>
     *
     * @param context {@link FacesContext} for the request we are processing
     *
     * @exception NullPointerException if <code>context</code>
     *  is <code>null</code>
     */
    public abstract void processUpdates(FacesContext context);


    /**
     * <p>Perform the component tree processing required by the state
     * saving portion of the <em>Render Response</em> phase of the
     * request processing lifecycle for all facets of this component,
     * all children of this component, and this component itself, as
     * follows.</p> <ul>
     *
     * <li>consult the <code>transient</code> property of this
     * component.  If true, just return.</li>
     *
     * <li>Call the <code>processSaveState()</code> method of all
     * facets and children of this {@link UIComponent} in the order
     * determined by a call to <code>getFacetsAndChildren()</code>.</li>
     *
     * <li>Call the <code>saveState()</code> method of this component.</li>
     *
     * <li>Encapsulate the child state and your state into a
     * Serializable Object and return it.</li> 
     *
     * </ul>
     *
     * @param context {@link FacesContext} for the request we are processing
     *
     * @exception NullPointerException if <code>context</code>
     *  is <code>null</code>
     */
    public abstract Object processSaveState(FacesContext context);


}

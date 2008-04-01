/*
 * $Id: Lifecycle.java,v 1.4 2002/05/15 01:03:52 craigmcc Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.lifecycle;

import java.util.SortedMap;
import javax.faces.FacesException;     // FIXME - subpackage?
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;


/**
 * <p><strong>Lifecycle</strong> is a state machine that manages the
 * processing of the entire lifecycle of a particular JavaServer Faces
 * request.  It is responsible for executing all {@link Phase}s that have
 * been registered, in ascending order of phase identifiers, unless otherwise
 * directed by the value returned by execution of each {@link Phase}.</p>
 *
 * <h3>Phase and PhaseListener Implementations</h3>
 *
 * <p>Portable implementations of JavaServer Faces will register
 * {@link Phase} implementations for all of the standard phase identifiers
 * described below.  These {@link Phase} implementations must perform the
 * functionality described for each phase identifier.  Each such implementation
 * should normally return <code>Lifecycle.NEXT_PHASE</code> to request the
 * execution of the next phase in registered identifier sequence.</p>
 *
 * <p><strong>FIXME</strong> - Move the details of the processing required
 * in each phase to the spec chapter on Request Processing Lifecycle.</p>
 *
 * <p>Applications (and JavaServer Faces implementations) may additionally
 * register custom {@link Phase} implementations.  If such a registration
 * replaces the Faces-provided {@link Phase} implementation for a standard
 * phase, it is the application's responsibility to perform the functionality
 * described for that standard phase, in addition to its own.</p>
 *
 * <p>Several of the standard phases involve walking down the component
 * tree for the current request (in top-down, left-to-right order), and
 * calling an appropriate event handling method.  In each case, the
 * underlying components have the option to delegate event handling to an
 * external object, referenced by the <code>lifecycleHandler</code> property
 * of the component.  If a particular component has no registered
 * <code>lifecycleHandler</code>, the corresponding method on the component
 * itself must be called instead.</p>
 *
 * <p><strong>FIXME</strong> - Representation of phase ids
 * as integers?</p>
 *
 * <h3>Lifecycle</h3>
 *
 * <p>A typical JavaServer Faces implementation will instantiate a single
 * instance of <code>Lifecycle</code> that is utilized to process all requests
 * for a particular web application.  Advanced implementations may choose to
 * utilize different <code>Lifecycle</code> instances for different types of
 * requests -- the choice of which implementation to use is made in the
 * <code>newInstance()</code> method of {@link FacesContextFactory}.  All
 * <code>Lifecycle</code> instances shall be available for the lifetime
 * of the web application.</p>
 *
 * <p>Because <code>Lifecycle</code> instances are shared, they must be
 * programmed in a thread-safe manner.  A {@link FacesContext} parameter
 * will be passed to methods as needed, to provide access to the state
 * information for a particular request.</p>
 */

public abstract class Lifecycle {


    // ------------------------------------------------------ Phase Identifiers


    /**
     * Phase identifier that indicates processing for the current request
     * has been completed.
     */
    public static final int EXIT_PHASE = -1;


    /**
     * Phase identifier that selects the next phase, in ascending sequence.
     */
    public static final int NEXT_PHASE = 0;


    /**
     * <p>Phase identifier for <em>Create Request Tree</em>.  This
     * {@link Phase} must perform the following actions:</p>
     * <ul>
     * <li>Calculate the logical page identifer of the previous
     *     JavaServer Faces response that created this page (typically
     *     extracted from the request URI and/or request parameters),
     *     and save this value by calling the <code>setRequestPageId()</code>
     *     method of {@link FacesContext}.</p>
     * <li>Look up component tree metadata the corresponds to the request
     *     page identifier, construct a corresponding component tree, and
     *     save this value by calling the <code>setRequestTree()</code>
     *     method of {@link FacesContext}.</p>
     * </ul>
     */
    public static final int CREATE_REQUEST_TREE_PHASE = 10;


    /**
     * <p>Phase identifier for <em>Reconstitute Request Tree</em>.  This
     * {@link Phase} must ensure that all components of the request tree
     * reflect the render-independent properties, and render-dependent
     * attributes, that were current as of the previous response.  If
     * the previous response caused state information to be serialized into
     * the response page (and included in this subsequent request), it will
     * typically be deserialized here and used to restore state, along with
     * any state information saved in the user's session.</p>
     */
    public static final int RECONSTITUTE_REQUEST_TREE_PHASE = 20;


    /**
     * <p>Phase identifier for <em>Apply Request Values</em>.  This
     * {@link Phase} must notify each {@link UIComponent} in the request tree
     * (in top-down, left-to-right order) via a call to the
     * <code>applyRequestValues()</code> method of that component.</p>
     */
    public static final int APPLY_REQUEST_VALUES_PHASE = 30;


    /**
     * <p>Phase identifier for <em>Handle Request Events</em>.  This
     * {@link Phase} must notify each {@link UIComponent} in the request tree
     * (in top-down, left-to-right order), via a call to the
     * <code>handleRequestEvents()</code> method of each component,
     * that it has the opportunity to
     * handle any events that were queued for that component.</p>
     *
     * <p>If one or more event handlers determines that the sole purpose of
     * this request to the server was to modify the render-dependent state
     * of a particular component (such as expanding or contracting a node
     * in a tree control), they will typically cause this {@link Phase} to
     * return the value <code>Lifecycle.RENDER_RESPONSE_PHASE</code>.
     * (<strong>FIXME</strong> how to make this easy?)  Otherwise, this
     * {@link Phase} should return <code>Lifecycle.NEXT_PHASE</code> in the
     * usual way.</p>
     */
    public static final int HANDLE_REQUEST_EVENTS_PHASE = 40;


    /**
     * <p>Phase identifier for <em>Process Validations</em>.  This
     * {@link Phase} must request each {@link UIComponent} in the request tree
     * (in top-down, left-to-right order), via a call to the
     * <code>processValidations()</code> method, to perform all validation
     * checks associated with that component (<strong>FIXME</strong> - specify
     * the mechanism by which validations are registered).</p>
     *
     * <p>If one or more validation errors have been accumulated, this
     * {@link Phase} will typically return
     * <code>Lifecycle.RENDER_RESPONSE_PHASE</code>, typically to the original
     * input page but optionally elsewhere.</p>
     */
    public static final int PROCESS_VALIDATIONS_PHASE = 50;


    /**
     * <p>Phase identifier for <em>Update Model Values</em>.  This
     * {@link Phase} must request each {@link UIComponent} in the request tree
     * (in top-down, left-to-right order), via a call to the
     * <code>updateModelValues</code> method, to update any corresponding
     * model data that is associated with this component.</p>
     *
     * <p><strong>FIXME</strong> - Specify handling of conversion exceptions
     * that are encountered here (most of them <em>should</em> have been
     * caught by the conversion attempt during validations, but ...).</p>
     */
    public static final int UPDATE_MODEL_VALUES_PHASE = 60;


    /**
     * <p>Phase identifier for <em>Invoke Application</em>.  This
     * {@link Phase} is responsible for invoking event handler(s) in the
     * web application that is utilizing JavaServer Faces, for the purpose
     * of performing the requested transactions.  An application will
     * typically perform one or more of the followin actions:</p>
     * <ul>
     * <li>Perform the business logic associated with the incoming request
     *     (typically based on some sort of mapping or dispatch action).</li>
     * <li>Select the page identifier of the response page to be created
     *     (defaults to the same as the request page identifier.</li>
     * <li>If the response page identifier is different from the request
     *     page identifier, construct a new response component tree by using
     *     the response page identifier to look up the corresponding metadata
     *     and construct a tree with default values.</li>
     * <li>Customize the response tree as desired.</li>
     * </ul>
     *
     * <p><strong>FIXME</strong> - Is there one application event handler,
     * or are there many (i.e. dispatch off request URI or a request parameter
     * value)?  Is there at most one application "event" per request, or do we
     * allow components to queue up several of them?  Is this really just a
     * matter of processing any <code>UICommand</code> event(s)?</p>
     */
    public static final int INVOKE_APPLICATION_PHASE = 70;


    /**
     * <p>Phase identifier for <em>Render Response</em>.  This
     * {@link Phase} will utilize the selected response page identifier to
     * invoke the appropriate rendering technology (for example, if JSP
     * pages are utilized, it will perform a
     * <code>RequestDispatcher.forward()</code> to the appropriate JSP page).
     * Typically, this {@link Phase} will return
     * <code>Lifecycle.EXIT_PHASE</code> to signal completion of the request
     * processing lifecycle.</p>
     *
     * <p><strong>FIXME</strong> - Where does serializing the current state
     * information fit in?  Is it a separate phase prior to this one?</p>
     */
    public static final int RENDER_RESPONSE_PHASE = 80;


    // ------------------------------------------------------------- Properties


    /**
     * <p>Return the unique lifecycle identifier for this
     * <code>Lifecycle</code> instance.  The default <code>Lifecycle</code>
     * instance for a given JavaServer Faces implementation shall
     * return a zero-length String.</p>
     */
    public abstract String getLifecycleId();


    // ------------------------------------------------------ Execution Methods


    /**
     * <p>Execute the {@link Phase}s registered for this <code>Lifecycle</code>
     * instance, according to the following algorithm.</p>
     * <ol>
     * <li>Select the registered {@link Phase} with the lowest phase
     *     identifier value.</li>
     * <li>Call the <code>entering()</code> method of each registered
     *     {@link PhaseListener}.</li>
     * <li>Call the <code>execute()</code> method of the currently selected
     *     phase.</li>
     * <li>Call the <code>exiting()</code> method of each registered
     *     {@link PhaseListener}.</li>
     * <li>Evaluate the identifier returned by the <code>execute()</code>
     *     method of the {@link Phase} that was just executed, and proceed
     *     as follows:
     *     <ul>
     *     <li><em>Phase.EXIT_PHASE</em> - Exit from this method.</li>
     *     <li><em>Phase.NEXT_PHASE</em> - Select the registered
     *         {@link Phase} with the next higher phase identifier,
     *         and return to Step 2.</li>
     *     <li><em>Any Other Value</em> - Select the {@link Phase}
     *         specified by the returned identifier (throwing an exception
     *         if there is no such registered {@link Phase}), and
     *         proceed to Step 2.</li>
     *     </ul></li>
     * </ol>
     *
     * @param context FacesContext for the current request being processed
     *
     * @exception FacesException if a {@link Phase} returned an invalid
     *  phase identifier
     * @exception FacesException if a {@link Phase} threw such an exception
     *  from its <code>execute()</code> method
     */
    public abstract void execute(FacesContext context) throws FacesException;


    // ---------------------------------------------------------- Phase Support


    /**
     * <p>Deregister an existing {@link Phase} instance for its defined phase
     * identifier.</p>
     *
     * <p><strong>FIXME</strong> - does this method need to be public?</p>
     *
     * @param phase Existing {@link Phase} instance to be deregistered
     *
     * @exception NullPointerException if <code>phase</code>
     *  is <code>null</code>
     */
    public abstract void deregister(Phase phase);


    /**
     * <p>Look up and return the {@link Phase} instance for the specified
     * phase identifier, if any; otherwise, return <code>null</code>.</p>
     *
     * <p><strong>FIXME</strong> - does this method need to be public?</p>
     *
     * @exception IllegalArgumentException if one of the special phase
     *  identifiers (EXIT_PHASE, NEXT_PHASE) is requested
     */
    public abstract Phase getPhase(int phaseId);


    /**
     * <p>Return a <code>SortedMap</code> of all the {@link Phase} instances
     * registered for this <code>Lifecycle</code>, keyed by the phase
     * identifier (wrapped in a <code>java.lang.Integer</code>).  If there are
     * no registered {@link Phase}s, an empty map is returned.  Because the
     * returned map is sorted, the order in which the keys are returned by
     * an iterator over the <code>keySet</code> is the <em>natural order</em>
     * in which the registered {@link Phase}s will be executed.</p>
     *
     * <p><strong>FIXME</strong> - does this method need to be public?</p>
     *
     * <p><strong>FIXME</strong> - Should the returned map be immutable?</p>
     */
    public abstract SortedMap getPhases();


    /**
     * <p>Register a new {@link Phase} instance for its defined phase
     * identifier, replacing any previously registered instance.</p>
     *
     * <p><strong>FIXME</strong> - does this method need to be public?
     * Should this registration perhaps be on {@link LifecycleFactory}
     * instead, keyed to the logical lifecycle id?</p>
     *
     * @param phase New {@link Phase} instance to be registered
     *
     * @exception IllegalArgumentException if one of the special phase
     *  identifiers (EXIT_PHASE, NEXT_PHASE) is requested
     * @exception NullPointerException if <code>phase</code>
     *  is <code>null</code>
     */
    public abstract void register(Phase phase);


    // -------------------------------------------------- PhaseListener Support

    /**
     * <p>Register a new {@link PhaseListener} that will receive notification
     * before and after the execution of each {@link Phase} of the request
     * processing lifecycle for this request.</p>
     *
     * <p><strong>FIXME</strong> - Should this registration perhaps be on
     * {@link LifecycleFactory} instead, keyed to the logical lifecycle id?</p>
     *
     * @param listener The {@link PhaseListener} instance to be added
     */
    public abstract void addPhaseListener(PhaseListener listener);


    /**
     * <p>Deregister a {@link PhaseListener} that was receiving notification
     * before and after the execution of each {@link Phase} of the request
     * processing lifecycle for this request.</p>
     *
     * @param listener The {@link PhaseListener} instance to be removed
     */
    public abstract void removePhaseListener(PhaseListener listener);


}

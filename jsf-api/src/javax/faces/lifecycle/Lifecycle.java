/*
 * $Id: Lifecycle.java,v 1.6 2002/05/15 23:57:01 craigmcc Exp $
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
 * <p>An instance of <code>Lifecycle</code> is created by calling the
 * <code>createLifecycle()</code> method of {@link LifecycleFactory}, for
 * a specified lifecycle identifier.  Subsequent calls to
 * <code>createLifecycle()</code> will return the same instance (i.e.
 * it is a per-web-application Singleton.  Because this instance is
 * shared across multiple simultaneous requests, it must be implemented
 * in a thread-safe manner.</p>
 *
 * <p>The set of {@link Phase} instances associated with a particular
 * <code>Lifecycle</code> instance, as well as the order that they are
 * executed in, must be configured during execution of the
 * <code>createLifecycle()</code> method in {@link LifecycleFactory}.  For
 * each standard phase identifier, a JavaServer Faces implementation will
 * provide a default {@link Phase} instance that implements the
 * required behavior of that phase.  In addition, it is possible for an
 * application to register additional {@link Phase} instances, to be executed
 * before or after the standard instance, for a particular phase identifier,
 * by calling the <code>registerBefore()</code> or <code>registerAfter()</code>
 * methods of {@link LifecycleFactory}.  Note that registration of custom
 * {@link Phase} instances must be completed before the call to
 * <code>createLifecycle()</code> that creates this <code>Lifecycle</code>
 * instance.</p>
 *
 * <p>The <code>execute()</code> method of <code>Lifecycle</code> is run
 * once per incoming request processed by a Faces application, according
 * to the following algorithm:</p>
 * <blockquote>
 * <p><strong>FIXME</strong> - Describe the algorithm used to identify
 * which phase instances are called in which order (including transitions
 * to <em>Render Response</em> starting with custom phases registered to
 * run before the standard one), and when phase listeners are invoked.</p>
 * </blockquote>
 */

public abstract class Lifecycle {


    // ------------------------------------------------------ Phase Identifiers


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
    public static final int CREATE_REQUEST_TREE_PHASE = 0;


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
    public static final int RECONSTITUTE_REQUEST_TREE_PHASE = 1;


    /**
     * <p>Phase identifier for <em>Apply Request Values</em>.  This
     * {@link Phase} must notify each {@link UIComponent} in the request tree
     * (in top-down, left-to-right order) via a call to the
     * <code>applyRequestValues()</code> method of that component.</p>
     */
    public static final int APPLY_REQUEST_VALUES_PHASE = 2;


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
    public static final int HANDLE_REQUEST_EVENTS_PHASE = 3;


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
    public static final int PROCESS_VALIDATIONS_PHASE = 4;


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
    public static final int UPDATE_MODEL_VALUES_PHASE = 5;


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
    public static final int INVOKE_APPLICATION_PHASE = 6;


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
    public static final int RENDER_RESPONSE_PHASE = 7;


    // --------------------------------------------------------- Public Methods


    /**
     * <p>Execute the {@link Phase}s registered for this <code>Lifecycle</code>
     * instance, according to the algorithm described in the class description.
     * As phases are executed, call the <code>setPhaseId()</code> method of
     * {@link FacesContext} as transitions between phases occur.</p>
     *
     * <p><strong>FIXME</strong> - setPhaseId() to what value when done?</p>
     *
     * @param context FacesContext for the current request being processed
     *
     * @exception IllegalStateException if a {@link Phase} returned an invalid
     *  state change value from the <code>execute()</code> method
     * @exception FacesException if a {@link Phase} threw such an exception
     *  from its <code>execute()</code> method
     */
    public abstract void execute(FacesContext context) throws FacesException;


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

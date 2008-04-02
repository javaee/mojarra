/*
 * $Id: UIViewRoot.java,v 1.40 2005/11/29 16:20:12 rlubke Exp $
 */

/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 * 
 * You can obtain a copy of the License at
 * https://javaserverfaces.dev.java.net/CDDL.html or
 * legal/CDDLv1.0.txt. 
 * See the License for the specific language governing
 * permission and limitations under the License.
 * 
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at legal/CDDLv1.0.txt.    
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 * 
 * [Name of File] [ver.__] [Date]
 * 
 * Copyright 2005 Sun Microsystems Inc. All Rights Reserved
 */

package javax.faces.component;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import javax.faces.FactoryFinder;
import javax.faces.FacesException;
import javax.faces.lifecycle.LifecycleFactory;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseListener;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.faces.webapp.FacesServlet;

import javax.el.ValueExpression;
import javax.el.MethodExpression;
import javax.el.ELException;


/**
 * <p><strong>UIViewRoot</strong> is the UIComponent that represents the
 * root of the UIComponent tree.  This component has no rendering, it
 * just serves as the root of the component tree, and as a place to hang
 * per-view {@link PhaseListener}s.</p>
 *
 * <p>For each of the following lifecycle phase methods:</p>
 *
 * 	<ul>
 *
 *	  <li><p>{@link #processDecodes} </p></li>
 *
 *	  <li><p>{@link #processValidators} </p></li>
 *
 *	  <li><p>{@link #processUpdates} </p></li>
 *
 *	  <li><p>{@link #processApplication} </p></li>
 *
 *	  <li><p>RenderResponse, via {@link #encodeBegin} and {@link
 *	  #encodeEnd} </p></li>
 *
 *	</ul>
 *
 * <p>Take the following action regarding
 * <code>PhaseListener</code>s.</p>
 *
 * <ul>
 *
 * <p>Initialize a state flag to <code>false</code>.</p>
 *
 * <p>If {@link #getBeforePhaseListener} returns non-<code>null</code>,
 * invoke the listener, passing in the correct corresponding {@link
 * PhaseId} for this phase.</p>
 *
 * <p>Upon return from the listener, call {@link
 * FacesContext#getResponseComplete} and {@link
 * FacesContext#getRenderResponse}.  If either return <code>true</code>
 * set the internal state flag to <code>true</code>. </p>
 *
 * <p>If or one or more listeners have been added by a call to {@link
 * #addPhaseListener}, invoke the <code>beforePhase</code> method on
 * each one whose {@link PhaseListener#getPhaseId} matches the current
 * phaseId, passing in the same <code>PhaseId</code> as in the previous
 * step.</p>
 *
 * <p>Upon return from each listener, call {@link
 * FacesContext#getResponseComplete} and {@link
 * FacesContext#getRenderResponse}.  If either return <code>true</code>
 * set the internal state flag to <code>true</code>. </p>
 *
 *
 * <p>Execute any processing for this phase if the internal state flag
 * was not set.</p>
 *
 * <p>If {@link #getAfterPhaseListener} returns non-<code>null</code>,
 * invoke the listener, passing in the correct corresponding {@link
 * PhaseId} for this phase.</p>
 *
 * <p>If or one or more listeners have been added by a call to {@link
 * #addPhaseListener}, invoke the <code>afterPhase</code> method on each
 * one whose {@link PhaseListener#getPhaseId} matches the current
 * phaseId, passing in the same <code>PhaseId</code> as in the previous
 * step.</p>
 *
 *
 * </ul>
 */

public class UIViewRoot extends UIComponentBase {


    // ------------------------------------------------------ Manifest Constants
    

    /**
     * <p>The standard component type for this component.</p>
     */
    public static final String COMPONENT_TYPE = "javax.faces.ViewRoot";


    /**
     * <p>The standard component family for this component.</p>
     */
    public static final String COMPONENT_FAMILY = "javax.faces.ViewRoot";


    /**
     * <p>The prefix that will be used for identifiers generated
     * by the <code>createUniqueId()</code> method.
     */
    static public final String UNIQUE_ID_PREFIX = "_id";
    
    private static Lifecycle lifecycle;
    

    // ------------------------------------------------------------ Constructors


    /**
     * <p>Create a new {@link UIViewRoot} instance with default property
     * values.</p>
     */
    public UIViewRoot() {

        super();
        setRendererType(null);

    }


    // ------------------------------------------------------ Instance Variables

    private int lastId = 0;

    /**
     * <p>Set and cleared during the lifetime of a lifecycle phase.  Has
     * no meaning between phases.  If <code>true</code>, the lifecycle
     * processing for the current phase must not take place.</p>
     */

    private boolean skipPhase;


    // -------------------------------------------------------------- Properties


    // -------------------------------------------------------------- Properties


    public String getFamily() {

        return (COMPONENT_FAMILY);

    }


    /**
     * <p>The render kit identifier of the {@link RenderKit} associated
     * wth this view.</p>
     */
    private String renderKitId = null;


    /**
     * <p>Return the render kit identifier of the {@link RenderKit}
     * associated with this view.  Unless explicitly set, as in {@link
     * javax.faces.application.ViewHandler#createView}, the returned
     * value will be <code>null.</code></p>
     */
    public String getRenderKitId() {

	String result = null;
	if (null != renderKitId) {
	    result = this.renderKitId;
	}
	else {
	    ValueExpression vb = getValueExpression("renderKitId");
	    FacesContext context = getFacesContext();
	    if (vb != null) {
		try {
		    result = (String) vb.getValue(context.getELContext());
		}
		catch (ELException e) {
		    // PENDING(edburns): log this
		    result = null;
		}
	    } 
	    else {
	        result = null;
	    }
	}
	return result;
    }


    /**
     * <p>Set the render kit identifier of the {@link RenderKit}
     * associated with this view.  This method may be called at any time
     * between the end of <em>Apply Request Values</em> phase of the
     * request processing lifecycle (i.e. when events are being broadcast)
     * and the beginning of the <em>Render Response</em> phase.</p>
     *
     * @param renderKitId The new {@link RenderKit} identifier,
     *  or <code>null</code> to disassociate this view with any
     *  specific {@link RenderKit} instance
     */
    public void setRenderKitId(String renderKitId) {

        this.renderKitId = renderKitId;

    }


    /**
     * <p>The view identifier of this view.</p>
     */
    private String viewId = null;


    /**
     * <p>Return the view identifier for this view.</p>
     */
    public String getViewId() {

        return (this.viewId);

    }


    /**
     *
     * <p>Set the view identifier for this view.</p>
     *
     * @param viewId The new view identifier
     */
    public void setViewId(String viewId) {

        this.viewId = viewId;

    }


    // ------------------------------------------------ Event Management Methods

    private MethodExpression beforePhase = null;
    private MethodExpression afterPhase = null;

    /**
     * @return the {@link MethodExpression} that will be invoked before
     * this view is rendered.
     *
     */

    public MethodExpression getBeforePhaseListener() {
	return beforePhase;
    }

    /**
     * <p>Allow an arbitrary method to be called for the "beforePhase"
     * event as the UIViewRoot runs through its lifecycle.  This method
     * will be called for all phases except {@link
     * PhaseId#RESTORE_VIEW}.  Unlike a true {@link PhaseListener},
     * this approach doesn't allow for only receiving {@link
     * PhaseEvent}s for a given phase.</p>
     *
     * <p>The method must conform to the signature of {@link
     * PhaseListener#beforePhase}.</p>
     *
     * @param newBeforePhase the {@link MethodExpression} that will be
     * invoked before this view is rendered.
     *
     */

    public void setBeforePhaseListener(MethodExpression newBeforePhase) {
	beforePhase = newBeforePhase;
    }

    /**
     * @return the {@link MethodExpression} that will be invoked after
     * this view is rendered.
     *
     */

    public MethodExpression getAfterPhaseListener() {
	return afterPhase;
    }

    /**
     * <p>Allow an arbitrary method to be called for the "afterPhase"
     * event as the UIViewRoot runs through its lifecycle.  This method
     * will be called for all phases except {@link
     * PhaseId#RESTORE_VIEW}.  Unlike a true {@link PhaseListener},
     * this approach doesn't allow for only receiving {@link
     * PhaseEvent}s for a given phase.</p>
     *
     * <p>The method must conform to the signature of {@link
     * PhaseListener#afterPhase}.</p>
     *
     * @param newAfterPhase the {@link MethodExpression} that will be
     * invoked after this view is rendered.  
     *
     */

    public void setAfterPhaseListener(MethodExpression newAfterPhase) {
	afterPhase = newAfterPhase;
    }

    private List<PhaseListener> phaseListeners = null;

    public void removePhaseListener(PhaseListener toRemove) {
	if (null != phaseListeners) {
	    phaseListeners.remove(toRemove);
	}
    }

    public void addPhaseListener(PhaseListener newPhaseListener) {
	if (null == phaseListeners) {
	    phaseListeners = new ArrayList<PhaseListener>();
	}
	phaseListeners.add(newPhaseListener);
    }

    /**
     * <p>An array of Lists of events that have been queued for later
     * broadcast, with one List for each lifecycle phase.  The list
     * indices match the ordinals of the PhaseId instances.  This
     * instance is lazily instantiated.  This list is
     * <strong>NOT</strong> part of the state that is saved and restored
     * for this component.</p>
     */
    private transient List events[] = null;


    /**
     * <p>Override the default {@link UIComponentBase#queueEvent} behavior to
     * accumulate the queued events for later broadcaster.</p>
     *
     * @param event {@link FacesEvent} to be queued
     *
     * @exception IllegalStateException if this component is not a
     *  descendant of a {@link UIViewRoot}
     * @exception NullPointerException if <code>event</code>
     *  is <code>null</code>
     */
    public void queueEvent(FacesEvent event) {
	
        if (event == null) {
            throw new NullPointerException();
        }
	int
	    i = 0,
	    len = 0;
        // We are a UIViewRoot, so no need to check for the ISE
        if (events == null) {
	    events = new List[len = PhaseId.VALUES.size()];
	    for (i = 0; i < len; i++) {
		events[i] = new ArrayList<FacesEvent>(5);
	    }
        }
        events[event.getPhaseId().getOrdinal()].add(event);
    }


    /**
     * <p>Broadcast any events that have been queued.</p>
     *
     * @param context {@link FacesContext} for the current request
     * @param phaseId {@link PhaseId} of the current phase
     */
    private void broadcastEvents(FacesContext context, PhaseId phaseId) {
	List<FacesEvent> eventsForPhaseId = null;

	if (null == events) {
	    // no events have been queued
	    return;
	}
	boolean 
	    hasMoreAnyPhaseEvents = true,
	    hasMoreCurrentPhaseEvents = true;

	eventsForPhaseId = events[PhaseId.ANY_PHASE.getOrdinal()];
	
	// keep iterating till we have no more events to broadcast.
	// This is necessary for events that cause other events to be
	// queued.  PENDING(edburns): here's where we'd put in a check
	// to prevent infinite event queueing.
	do {
	    // broadcast the ANY_PHASE events first
	    if (null != eventsForPhaseId) {
		// We cannot use an Iterator because we will get
		// ConcurrentModificationException errors, so fake it
		int cursor = 0;
		while (cursor < eventsForPhaseId.size()) {
		    FacesEvent event =
			eventsForPhaseId.get(cursor);
		    UIComponent source = event.getComponent();
                    try {
                        source.broadcast(event);
                    } catch (AbortProcessingException e) {
                        ; // A "return" here would abort remaining events too
                    }
		    eventsForPhaseId.remove(cursor); // Stay at current position
		}
	    }
	    
	    // then broadcast the events for this phase.
	    if (null != (eventsForPhaseId = events[phaseId.getOrdinal()])) {
		// We cannot use an Iterator because we will get
		// ConcurrentModificationException errors, so fake it
		int cursor = 0;
		while (cursor < eventsForPhaseId.size()) {
		    FacesEvent event = 
			eventsForPhaseId.get(cursor);
		    UIComponent source = event.getComponent();
                    try {
                        source.broadcast(event);
                    } catch (AbortProcessingException e) {
                        ; // A "return" here would abort remaining events too
                    }
		    eventsForPhaseId.remove(cursor); // Stay at current position
		}
	    }

	    // true if we have any more ANY_PHASE events
	    hasMoreAnyPhaseEvents = 
		(null != (eventsForPhaseId = 
			  events[PhaseId.ANY_PHASE.getOrdinal()])) &&
		eventsForPhaseId.size() > 0;
	    // true if we have any more events for the argument phaseId
	    hasMoreCurrentPhaseEvents = 
		(null != events[phaseId.getOrdinal()]) &&
		events[phaseId.getOrdinal()].size() > 0;

	} while (hasMoreAnyPhaseEvents || hasMoreCurrentPhaseEvents);

    }


    // ------------------------------------------------ Lifecycle Phase Handlers


    /**
     * <p>Override the default {@link UIComponentBase#processDecodes}
     * behavior to broadcast any queued events after the default
     * processing has been completed and to clear out any events
     * for later phases if the event processing for this phase caused {@link
     * FacesContext#renderResponse} or {@link FacesContext#responseComplete}
     * to be called.</p>
     *
     * @param context {@link FacesContext} for the request we are processing
     *
     * @exception NullPointerException if <code>context</code>
     *  is <code>null</code>
     */
    public void processDecodes(FacesContext context) {
	skipPhase = false;
	// avoid creating the PhaseEvent if possible by doing redundant
	// null checks.
	if (null != beforePhase || null != phaseListeners) {
	    notifyPhaseListeners(context, PhaseId.APPLY_REQUEST_VALUES, true);
	}
	if (!skipPhase) {
	    super.processDecodes(context);
	    broadcastEvents(context, PhaseId.APPLY_REQUEST_VALUES);
	}
	// clear out the events if we're skipping to render-response
        // or if there is a response complete signal.
	if (context.getRenderResponse() || context.getResponseComplete()) {
            if (events != null) {
                for (int i=0; i<PhaseId.VALUES.size(); i++) {
                    List<FacesEvent> eventList = events[i];
                    if (eventList != null) {
                        eventList.clear();
                    }
                }
                events = null;
            }
	}
        
	// avoid creating the PhaseEvent if possible by doing redundant
	// null checks.
	if (null != beforePhase || null != phaseListeners) {
	    notifyPhaseListeners(context, PhaseId.APPLY_REQUEST_VALUES, false);
	}

    }

    /**
     * <p>Override the default {@link UIComponentBase#encodeBegin}
     * behavior.  If
     * {@link #getBeforePhaseListener} returns non-<code>null</code>,
     * invoke it, passing a {@link PhaseEvent} for the {@link
     * PhaseId#RENDER_RESPONSE} phase.  If the internal list populated
     * by calls to {@link #addPhaseListener} is non-empty, any listeners
     * in that list must have their {@link PhaseListener#beforePhase}
     * method called, passing the <code>PhaseEvent</code>.  Any errors
     * that occur during invocation of any of the the beforePhase
     * listeners must be logged and swallowed.  After listeners are invoked
     * call superclass processing.</p>
     *
    */

    public void encodeBegin(FacesContext context) throws IOException {

	skipPhase = false;
	// avoid creating the PhaseEvent if possible by doing redundant
	// null checks.
	if (null != beforePhase || null != phaseListeners) {
	    notifyPhaseListeners(context, PhaseId.RENDER_RESPONSE, true);
	}
	
	if (!skipPhase) {
	    super.encodeBegin(context);
	}
    }

    /**
     * <p>Override the default {@link UIComponentBase#encodeEnd}
     * behavior.  If {@link #getAfterPhaseListener} returns
     * non-<code>null</code>, invoke it, passing a {@link PhaseEvent}
     * for the {@link PhaseId#RENDER_RESPONSE} phase.  Any errors that
     * occur during invocation of the afterPhase listener must be
     * logged and swallowed.</p>
     */

    public void encodeEnd(FacesContext context) throws IOException {
	super.encodeEnd(context);

	// avoid creating the PhaseEvent if possible by doing redundant
	// null checks.
	if (null != afterPhase || null != phaseListeners) {
	    notifyPhaseListeners(context, PhaseId.RENDER_RESPONSE, false);
	}
	
    }

    /**
     * <p>Utility method that notifies phaseListeners for the given
     * phaseId.  Assumes that either or both the MethodExpression or
     * phaseListeners data structure are non-null.</p>
     *
     * @param context the context for this request
     *
     * @param phaseId the {@link PhaseId} of the current phase
     *
     * @param isBefore, if true, notify beforePhase listeners.  Notify
     * afterPhase listeners otherwise.
     */

    private void notifyPhaseListeners(FacesContext context,
				      PhaseId phaseId,
				      boolean isBefore) {
	PhaseEvent event = createPhaseEvent(context, phaseId);
	
	boolean hasPhaseMethodExpression = 
	    (isBefore && (null != beforePhase)) ||
	    (!isBefore && (null != afterPhase));
	MethodExpression expression = isBefore ? beforePhase : afterPhase;

	if (hasPhaseMethodExpression) {
	    try {
		expression.invoke(context.getELContext(), new Object [] { event });
		skipPhase = context.getResponseComplete() ||
		    context.getRenderResponse();
	    }
	    catch (Exception e) {
		// PENDING(edburns): log this
	    }
	}
	if (null != phaseListeners) {
	    Iterator<PhaseListener> iter = phaseListeners.iterator();
	    PhaseListener curListener = null;
	    while (iter.hasNext()) {
		curListener = iter.next();
		if (phaseId == curListener.getPhaseId() ||
		    PhaseId.ANY_PHASE == curListener.getPhaseId()) {
		    try {
			if (isBefore) {
			    curListener.beforePhase(event);
			}
			else {
			    curListener.afterPhase(event);
			}
			skipPhase = context.getResponseComplete() ||
			    context.getRenderResponse();
		    }
		    catch (Exception e) {
			// PENDING(edburns): log this
		    }
		}
	    }
	}
    }

    private PhaseEvent createPhaseEvent(FacesContext context,
                                        PhaseId phaseId) throws FacesException {

        if (lifecycle == null) {
            LifecycleFactory lifecycleFactory = (LifecycleFactory)
                  FactoryFinder.getFactory(FactoryFinder.LIFECYCLE_FACTORY);
            String lifecycleId =
                  context.getExternalContext()
                        .getInitParameter(FacesServlet.LIFECYCLE_ID_ATTR);
            if (lifecycleId == null) {
                lifecycleId = LifecycleFactory.DEFAULT_LIFECYCLE;
            }
            lifecycle = lifecycleFactory.getLifecycle(lifecycleId);
        }
        
        return (new PhaseEvent(context, phaseId, lifecycle));
        
    }


    /**
     * <p>Override the default {@link UIComponentBase#processValidators}
     * behavior to broadcast any queued events after the default
     * processing has been completed and to clear out any events
     * for later phases if the event processing for this phase caused {@link
     * FacesContext#renderResponse} or {@link FacesContext#responseComplete}
     * to be called.</p>
     *
     * @param context {@link FacesContext} for the request we are processing
     *
     * @exception NullPointerException if <code>context</code>
     *  is <code>null</code>
     */
    public void processValidators(FacesContext context) {
	skipPhase = false;
	// avoid creating the PhaseEvent if possible by doing redundant
	// null checks.
	if (null != beforePhase || null != phaseListeners) {
	    notifyPhaseListeners(context, PhaseId.PROCESS_VALIDATIONS, true);
	}
	if (!skipPhase) {
	    super.processValidators(context);
	    broadcastEvents(context, PhaseId.PROCESS_VALIDATIONS);
	}
        // clear out the events if we're skipping to render-response
        // or if there is a response complete signal.
	if (context.getRenderResponse() || context.getResponseComplete()) {
            if (events != null) {
                for (int i=0; i<PhaseId.VALUES.size(); i++) {
                    List<FacesEvent> eventList = events[i];
                    if (eventList != null) {
                        eventList.clear();
                    }
                }
                events = null;
            }
	}

	// avoid creating the PhaseEvent if possible by doing redundant
	// null checks.
	if (null != beforePhase || null != phaseListeners) {
	    notifyPhaseListeners(context, PhaseId.PROCESS_VALIDATIONS, false);
	}
    }


    /**
     * <p>Override the default {@link UIComponentBase} behavior to broadcast
     * any queued events after the default processing has been completed
     * and to clear out any events for later phases if the event processing 
     * for this phase caused {@link FacesContext#renderResponse} or 
     * {@link FacesContext#responseComplete} to be called.</p>
     *
     * @param context {@link FacesContext} for the request we are processing
     *
     * @exception NullPointerException if <code>context</code>
     *  is <code>null</code>
     */
    public void processUpdates(FacesContext context) {
	skipPhase = false;
	// avoid creating the PhaseEvent if possible by doing redundant
	// null checks.
	if (null != beforePhase || null != phaseListeners) {
	    notifyPhaseListeners(context, PhaseId.UPDATE_MODEL_VALUES, true);
	}
	if (!skipPhase) {
	    super.processUpdates(context);
	    broadcastEvents(context, PhaseId.UPDATE_MODEL_VALUES);
	}
        // clear out the events if we're skipping to render-response
        // or if there is a response complete signal.
        if (context.getRenderResponse() || context.getResponseComplete()) {
            if (events != null) {
                for (int i=0; i<PhaseId.VALUES.size(); i++) {
                    List eventList = events[i];
                    if (eventList != null) {
                        eventList.clear();
                    }
                }
                events = null;
            }
	}
      
	// avoid creating the PhaseEvent if possible by doing redundant
	// null checks.
	if (null != beforePhase || null != phaseListeners) {
	    notifyPhaseListeners(context, PhaseId.UPDATE_MODEL_VALUES, false);
	}
    }


    /**
     * <p>Broadcast any events that have been queued for the <em>Invoke
     * Application</em> phase of the request processing lifecycle
     * and to clear out any events for later phases if the event processing 
     * for this phase caused {@link FacesContext#renderResponse} or 
     * {@link FacesContext#responseComplete} to be called.</p>
     *
     * @param context {@link FacesContext} for the request we are processing
     *
     * @exception NullPointerException if <code>context</code>
     *  is <code>null</code>
     */
    public void processApplication(FacesContext context) {
	skipPhase = false;
	// avoid creating the PhaseEvent if possible by doing redundant
	// null checks.
	if (null != beforePhase || null != phaseListeners) {
	    notifyPhaseListeners(context, PhaseId.INVOKE_APPLICATION, true);
	}

	if (!skipPhase) {
	    // NOTE - no tree walk is performed; this is a UIViewRoot-only operation
	    broadcastEvents(context, PhaseId.INVOKE_APPLICATION);
	}
        // clear out the events if we're skipping to render-response
        // or if there is a response complete signal.
        if (context.getRenderResponse() || context.getResponseComplete()) {
            if (events != null) {
                for (int i=0; i<PhaseId.VALUES.size(); i++) {
                    List eventList = events[i];
                    if (eventList != null) {
                        eventList.clear();
                    }
                }
                events = null;
            }
	}

	// avoid creating the PhaseEvent if possible by doing redundant
	// null checks.
	if (null != beforePhase || null != phaseListeners) {
	    notifyPhaseListeners(context, PhaseId.INVOKE_APPLICATION, false);
	}
    }

    /**
     * <p>Generate an identifier for a component.  The identifier will
     * be prefixed with UNIQUE_ID_PREFIX, and will be unique within
     * this UIViewRoot.</p>
     */
    public String createUniqueId() {
	return UNIQUE_ID_PREFIX + lastId++;
    }
    
    /*
     * <p>The locale for this view.</p>
     */
    private Locale locale = null;
    
    /**
     * <p>Return the <code>Locale</code> to be used in localizing the
     * response being created for this view.</p>
     *
     * <p>Algorithm:</p>
     *
     * <p>If we have a <code>locale</code> ivar, return it.  If we have
     * a value expression for "locale", get its value.  If the value is
     * <code>null</code>, return the result of calling {@link
     * javax.faces.application.ViewHandler#calculateLocale}.  If the
     * value is an instance of <code>java.util.Locale</code> return it.
     * If the value is a String, convert it to a
     * <code>java.util.Locale</code> and return it.  If there is no
     * value expression for "locale", return the result of calling {@link
     * javax.faces.application.ViewHandler#calculateLocale}.</p>
     *
     * @return The current <code>Locale</code> obtained by executing the
     * above algorithm.
     */
    public Locale getLocale() {
	Locale result = null;
	if (null != locale) {
	    result = this.locale;
	}
	else {
	    ValueExpression vb = getValueExpression("locale");
	    FacesContext context = getFacesContext();
	    if (vb != null) {
                Object resultLocale = null;

		try {
		    resultLocale = vb.getValue(context.getELContext());
		}
		catch (ELException e) {
		    // PENDING(edburns): log this
		}

		if (null == resultLocale) {
		    result = 
			context.getApplication().getViewHandler().calculateLocale(context);
		}
		else if ( resultLocale instanceof Locale) {
                    result = (Locale)resultLocale;
                } else if ( resultLocale instanceof String) {
                    result = getLocaleFromString((String)resultLocale);
                }
	    } 
	    else {
		result = 
		    context.getApplication().getViewHandler().calculateLocale(context);
	    }
	}
	return result;
    }


    /**
     * Returns the locale represented by the expression.
     * @param localeExpr a String in the format specified by JSTL Specification
     *                   as follows:
     *                   "A String value is interpreted as the printable 
     *                    representation of a locale, which must contain a 
     *                    two-letter (lower-case) language code (as defined by 
     *                    ISO-639), and may contain a two-letter (upper-case)
     *                    country code (as defined by ISO-3166). Language and 
     *                    country codes must be separated by hyphen (???-???) or 
     *                    underscore (???_???)."
     * @return Locale instance cosntructed from the expression.
     */
    private Locale getLocaleFromString(String localeExpr) {
        Locale result = Locale.getDefault();
        if (localeExpr.indexOf("_") == -1 || localeExpr.indexOf("-") == -1)  {
            // expression has just language code in it. make sure the 
            // expression contains exactly 2 characters.
            if (localeExpr.length() == 2) {
                result = new Locale(localeExpr, "");
            }
        } else {
            // expression has country code in it. make sure the expression 
            // contains exactly 5 characters.
            if (localeExpr.length() == 5) {
                // get the language and country to construct the locale.
                String language = localeExpr.substring(0,1);
                String country = localeExpr.substring(3,4);
                result = new Locale(language,country);
            }
        }
        return result;
    }
    
    /**
     * <p>Set the <code>Locale</code> to be used in localizing the
     * response being created for this view. </p>
     *
     * @param locale The new localization Locale
     */
    public void setLocale(Locale locale) {
        this.locale = locale;
        // Make sure to appraise the EL of this switch in Locale.
        FacesContext.getCurrentInstance().getELContext().setLocale(locale);
    }
    
    // ----------------------------------------------------- StateHolder Methods


    public Object saveState(FacesContext context) {

        Object values[] = new Object[8];
        values[0] = super.saveState(context);
        values[1] = renderKitId;
        values[2] = viewId;
        values[3] = locale;
        values[4] = new Integer(lastId);
        values[5] = saveAttachedState(context, beforePhase);
        values[6] = saveAttachedState(context, afterPhase);
        values[7] = saveAttachedState(context, phaseListeners);
        return (values);

    }


    public void restoreState(FacesContext context, Object state) {

        Object values[] = (Object[]) state;
        super.restoreState(context, values[0]);
        renderKitId = (String) values[1];
        viewId = (String) values[2];
        locale = (Locale)values[3];
        lastId = ((Integer)values[4]).intValue();
	beforePhase = (MethodExpression) restoreAttachedState(context, values[5]);
	afterPhase = (MethodExpression) restoreAttachedState(context, values[6]);
	phaseListeners = (List) restoreAttachedState(context, values[7]);

    }


}

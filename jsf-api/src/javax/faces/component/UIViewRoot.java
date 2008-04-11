/*
 * $Id: UIViewRoot.java,v 1.51 2008/03/17 22:25:48 rlubke Exp $
 */

/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 * 
 * Contributor(s):
 * 
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
import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.LifecycleFactory;
import javax.faces.render.RenderKit;
import javax.faces.webapp.FacesServlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * <p><strong>UIViewRoot</strong> is the UIComponent that represents the
 * root of the UIComponent tree.  This component has no rendering, it
 * just serves as the root of the component tree, and as a place to hang
 * per-view {@link PhaseListener}s.</p>
 * <p/>
 * <p>For each of the following lifecycle phase methods:</p>
 * <p/>
 * <ul>
 * <p/>
 * <li><p>{@link #processDecodes} </p></li>
 * <p/>
 * <li><p>{@link #processValidators} </p></li>
 * <p/>
 * <li><p>{@link #processUpdates} </p></li>
 * <p/>
 * <li><p>{@link #processApplication} </p></li>
 * <p/>
 * <li><p>RenderResponse, via {@link #encodeBegin} and {@link
 * #encodeEnd} </p></li>
 * <p/>
 * </ul>
 * <p/>
 * <p>Take the following action regarding
 * <code>PhaseListener</code>s.</p>
 * <p/>
 * <ul>
 * <p/>
 * <p>Initialize a state flag to <code>false</code>.</p>
 * <p/>
 * <p>If {@link #getBeforePhaseListener} returns non-<code>null</code>,
 * invoke the listener, passing in the correct corresponding {@link
 * PhaseId} for this phase.</p>
 * <p/>
 * <p>Upon return from the listener, call {@link
 * FacesContext#getResponseComplete} and {@link
 * FacesContext#getRenderResponse}.  If either return <code>true</code>
 * set the internal state flag to <code>true</code>. </p>
 * <p/>
 * <p>If or one or more listeners have been added by a call to {@link
 * #addPhaseListener}, invoke the <code>beforePhase</code> method on
 * each one whose {@link PhaseListener#getPhaseId} matches the current
 * phaseId, passing in the same <code>PhaseId</code> as in the previous
 * step.</p>
 * <p/>
 * <p>Upon return from each listener, call {@link
 * FacesContext#getResponseComplete} and {@link
 * FacesContext#getRenderResponse}.  If either return <code>true</code>
 * set the internal state flag to <code>true</code>. </p>
 * <p/>
 * <p/>
 * <p>Execute any processing for this phase if the internal state flag
 * was not set.</p>
 * <p/>
 * <p>If {@link #getAfterPhaseListener} returns non-<code>null</code>,
 * invoke the listener, passing in the correct corresponding {@link
 * PhaseId} for this phase.</p>
 * <p/>
 * <p>If or one or more listeners have been added by a call to {@link
 * #addPhaseListener}, invoke the <code>afterPhase</code> method on each
 * one whose {@link PhaseListener#getPhaseId} matches the current
 * phaseId, passing in the same <code>PhaseId</code> as in the previous
 * step.</p>
 * <p/>
 * <p/>
 * </ul>
 */

public class UIViewRoot extends UIComponentBase {

    // ------------------------------------------------------ Manifest Constants


    /** <p>The standard component type for this component.</p> */
    public static final String COMPONENT_TYPE = "javax.faces.ViewRoot";


    /** <p>The standard component family for this component.</p> */
    public static final String COMPONENT_FAMILY = "javax.faces.ViewRoot";


    /**
     * <p>The prefix that will be used for identifiers generated
     * by the <code>createUniqueId()</code> method.
     */
    static public final String UNIQUE_ID_PREFIX = "j_id";

    private static Lifecycle lifecycle;

    private static final Logger LOGGER =
          Logger.getLogger("javax.faces", "javax.faces.LogStrings");

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

        String result;
        if (null != renderKitId) {
            result = this.renderKitId;
        } else {
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
            } else {
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
     *                    or <code>null</code> to disassociate this view with any
     *                    specific {@link RenderKit} instance
     */
    public void setRenderKitId(String renderKitId) {

        this.renderKitId = renderKitId;

    }


    /** <p>The view identifier of this view.</p> */
    private String viewId = null;


    /** <p>Return the view identifier for this view.</p> */
    public String getViewId() {

        return (this.viewId);

    }


    /**
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
     *         this view is rendered.
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
     * <p/>
     * <p>The method must conform to the signature of {@link
     * PhaseListener#beforePhase}.</p>
     *
     * @param newBeforePhase the {@link MethodExpression} that will be
     *                       invoked before this view is rendered.
     */

    public void setBeforePhaseListener(MethodExpression newBeforePhase) {
        beforePhase = newBeforePhase;
    }

    /**
     * @return the {@link MethodExpression} that will be invoked after
     *         this view is rendered.
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
     * <p/>
     * <p>The method must conform to the signature of {@link
     * PhaseListener#afterPhase}.</p>
     *
     * @param newAfterPhase the {@link MethodExpression} that will be
     *                      invoked after this view is rendered.
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
            //noinspection CollectionWithoutInitialCapacity
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
    private List<List<FacesEvent>> events = null;


    /**
     * <p>Override the default {@link UIComponentBase#queueEvent} behavior to
     * accumulate the queued events for later broadcasting.</p>
     *
     * @param event {@link FacesEvent} to be queued
     *
     * @throws IllegalStateException if this component is not a
     *                               descendant of a {@link UIViewRoot}
     * @throws NullPointerException  if <code>event</code>
     *                               is <code>null</code>
     */
    public void queueEvent(FacesEvent event) {

        if (event == null) {
            throw new NullPointerException();
        }
        int i;
        int len = PhaseId.VALUES.size();
        // We are a UIViewRoot, so no need to check for the ISE
        if (events == null) {
            List<List<FacesEvent>> events = new ArrayList<List<FacesEvent>>(len);
            for (i = 0; i < len; i++) {
                events.add(new ArrayList<FacesEvent>(5));
            }
            this.events = events;
        }
        events.get(event.getPhaseId().getOrdinal()).add(event);
    }


    /**
     * <p>Broadcast any events that have been queued.</p>
     *
     * @param context {@link FacesContext} for the current request
     * @param phaseId {@link PhaseId} of the current phase
     */
    private void broadcastEvents(FacesContext context, PhaseId phaseId) {

        if (null == events) {
            // no events have been queued
            return;
        }
        boolean hasMoreAnyPhaseEvents;
        boolean hasMoreCurrentPhaseEvents;

        List<FacesEvent> eventsForPhaseId =
             events.get(PhaseId.ANY_PHASE.getOrdinal());

        // keep iterating till we have no more events to broadcast.
        // This is necessary for events that cause other events to be
        // queued.  PENDING(edburns): here's where we'd put in a check
        // to prevent infinite event queueing.
        do {
            // broadcast the ANY_PHASE events first
            if (null != eventsForPhaseId) {
                // We cannot use an Iterator because we will get
                // ConcurrentModificationException errors, so fake it
                while (!eventsForPhaseId.isEmpty()) {
                    FacesEvent event =
                          eventsForPhaseId.get(0);
                    UIComponent source = event.getComponent();
                    try {
                        source.broadcast(event);
                    } catch (AbortProcessingException e) {
                        if (LOGGER.isLoggable(Level.SEVERE)) {
                            UIComponent component = event.getComponent();
                            String id = "";
                            if (component != null) {
                                id = component.getId();
                                if (id == null) {
                                    id = component.getClientId(context);
                                }
                            }
                            LOGGER.log(Level.SEVERE,
                                       "error.component.abortprocessing_thrown",
                                       new Object[]{event.getClass().getName(),
                                                    phaseId.toString(),
                                                    id});
                            LOGGER.log(Level.SEVERE, e.toString(), e);
                        }
                    }
                    eventsForPhaseId.remove(0); // Stay at current position
                }
            }

            // then broadcast the events for this phase.
            if (null != (eventsForPhaseId = events.get(phaseId.getOrdinal()))) {
                // We cannot use an Iterator because we will get
                // ConcurrentModificationException errors, so fake it
                while (!eventsForPhaseId.isEmpty()) {
                    FacesEvent event = eventsForPhaseId.get(0);
                    UIComponent source = event.getComponent();
                    try {
                        source.broadcast(event);
                    } catch (AbortProcessingException e) {
                        ; // A "return" here would abort remaining events too
                    }
                    eventsForPhaseId.remove(0); // Stay at current position
                }
            }

            // true if we have any more ANY_PHASE events
            hasMoreAnyPhaseEvents =
                  (null != (eventsForPhaseId =
                        events.get(PhaseId.ANY_PHASE.getOrdinal()))) &&
                        !eventsForPhaseId.isEmpty();
            // true if we have any more events for the argument phaseId
            hasMoreCurrentPhaseEvents =
                  (null != events.get(phaseId.getOrdinal())) &&
                  !events.get(phaseId.getOrdinal()).isEmpty();

        } while (hasMoreAnyPhaseEvents || hasMoreCurrentPhaseEvents);

    }

    // ------------------------------------------------ Lifecycle Phase Handlers

    // avoid creating the PhaseEvent if possible by doing redundant
    // null checks.
    private void notifyBefore(FacesContext context, PhaseId phaseId) {
        if (null != beforePhase || null != phaseListeners) {
            notifyPhaseListeners(context, phaseId, true);
        }
    }
    
    // avoid creating the PhaseEvent if possible by doing redundant
    // null checks.
    private void notifyAfter(FacesContext context, PhaseId phaseId) {
        if (null != afterPhase || null != phaseListeners) {
            notifyPhaseListeners(context, phaseId, false);
        }
    }

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
     * @throws NullPointerException if <code>context</code>
     *                              is <code>null</code>
     */
    @Override
    public void processDecodes(FacesContext context) {
        skipPhase = false;
        notifyBefore(context, PhaseId.APPLY_REQUEST_VALUES);
        if (!skipPhase) {
            super.processDecodes(context);
            broadcastEvents(context, PhaseId.APPLY_REQUEST_VALUES);
        }
        clearFacesEvents(context);
        notifyAfter(context, PhaseId.APPLY_REQUEST_VALUES);
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
     */
    @Override
    public void encodeBegin(FacesContext context) throws IOException {

        skipPhase = false;
        notifyBefore(context, PhaseId.RENDER_RESPONSE);

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
    @Override
    public void encodeEnd(FacesContext context) throws IOException {
        super.encodeEnd(context);

        notifyAfter(context, PhaseId.RENDER_RESPONSE);
    }

    /**
     * <p>Utility method that notifies phaseListeners for the given
     * phaseId.  Assumes that either or both the MethodExpression or
     * phaseListeners data structure are non-null.</p>
     *
     * @param context   the context for this request
     * @param phaseId   the {@link PhaseId} of the current phase
     * @param isBefore, if true, notify beforePhase listeners.  Notify
     *                  afterPhase listeners otherwise.
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
                expression.invoke(context.getELContext(), new Object[]{event});
                skipPhase = context.getResponseComplete() ||
                            context.getRenderResponse();
            }
            catch (Exception e) {
                // PENDING(edburns): log this
            }
        }
        if (null != phaseListeners) {
            Iterator<PhaseListener> iter = phaseListeners.iterator();
            PhaseListener curListener;
            while (iter.hasNext()) {
                curListener = iter.next();
                if (phaseId == curListener.getPhaseId() ||
                    PhaseId.ANY_PHASE == curListener.getPhaseId()) {
                    try {
                        if (isBefore) {
                            curListener.beforePhase(event);
                        } else {
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

    private static PhaseEvent createPhaseEvent(FacesContext context,
                                               PhaseId phaseId)
    throws FacesException {

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
     * @throws NullPointerException if <code>context</code>
     *                              is <code>null</code>
     */
    @Override
    public void processValidators(FacesContext context) {
        skipPhase = false;
        notifyBefore(context, PhaseId.PROCESS_VALIDATIONS);
        if (!skipPhase) {
            super.processValidators(context);
            broadcastEvents(context, PhaseId.PROCESS_VALIDATIONS);
        }
        clearFacesEvents(context);
        notifyAfter(context, PhaseId.PROCESS_VALIDATIONS);
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
     * @throws NullPointerException if <code>context</code>
     *                              is <code>null</code>
     */
    @Override
    public void processUpdates(FacesContext context) {
        skipPhase = false;
        notifyBefore(context, PhaseId.UPDATE_MODEL_VALUES);
        if (!skipPhase) {
            super.processUpdates(context);
            broadcastEvents(context, PhaseId.UPDATE_MODEL_VALUES);
        }
        clearFacesEvents(context);
        notifyAfter(context, PhaseId.UPDATE_MODEL_VALUES);
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
     * @throws NullPointerException if <code>context</code>
     *                              is <code>null</code>
     */
    public void processApplication(FacesContext context) {
        skipPhase = false;
        notifyBefore(context, PhaseId.INVOKE_APPLICATION);

        if (!skipPhase) {
            // NOTE - no tree walk is performed; this is a UIViewRoot-only operation
            broadcastEvents(context, PhaseId.INVOKE_APPLICATION);
        }
        clearFacesEvents(context);
        notifyAfter(context, PhaseId.INVOKE_APPLICATION);
    }


    // clear out the events if we're skipping to render-response
    // or if there is a response complete signal.
    private void clearFacesEvents(FacesContext context) {
        if (context.getRenderResponse() || context.getResponseComplete()) {
            if (events != null) {
                for (List<FacesEvent> eventList : events) {
                    if (eventList != null) {
                        eventList.clear();
                    }
                }
                events = null;
            }
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
     * <p/>
     * <p>Algorithm:</p>
     * <p/>
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
     *         above algorithm.
     */
    public Locale getLocale() {
        Locale result = null;
        if (null != locale) {
            result = this.locale;
        } else {
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
                          context.getApplication().getViewHandler()
                                .calculateLocale(context);
                } else if (resultLocale instanceof Locale) {
                    result = (Locale) resultLocale;
                } else if (resultLocale instanceof String) {
                    result = getLocaleFromString((String) resultLocale);
                }
            } else {
                result =
                      context.getApplication().getViewHandler()
                            .calculateLocale(context);
            }
        }
        return result;
    }


    // W3C XML specification refers to IETF RFC 1766 for language code
    // structure, therefore the value for the xml:lang attribute should
    // be in the form of language or language-country or
    // language-country-variant.

    private static Locale getLocaleFromString(String localeStr)
        throws IllegalArgumentException {
        // length must be at least 2.
        if (null == localeStr || localeStr.length() < 2) {
            throw new IllegalArgumentException("Illegal locale String: " +
                                               localeStr);
        }

        Locale result = null;
        String lang = null;
        String country = null;
        String variant = null;
        char[] seps = {
            '-',
            '_'
        };
        int inputLength = localeStr.length();
        int i = 0;
        int j = 0;

        // to have a language, the length must be >= 2
        if ((inputLength >= 2) &&
            ((i = indexOfSet(localeStr, seps, 0)) == -1)) {
            // we have only Language, no country or variant
            if (2 != localeStr.length()) {
                throw new
                    IllegalArgumentException("Illegal locale String: " +
                                             localeStr);
            }
            lang = localeStr.toLowerCase();
        }

        // we have a separator, it must be either '-' or '_'
        if (i != -1) {
            lang = localeStr.substring(0, i);
            // look for the country sep.
            // to have a country, the length must be >= 5
            if ((inputLength >= 5) &&
                (-1 == (j = indexOfSet(localeStr, seps, i + 1)))) {
                // no further separators, length must be 5
                if (inputLength != 5) {
                    throw new
                        IllegalArgumentException("Illegal locale String: " +
                                                 localeStr);
                }
                country = localeStr.substring(i + 1);
            }
            if (j != -1) {
                country = localeStr.substring(i + 1, j);
                // if we have enough separators for language, locale,
                // and variant, the length must be >= 8.
                if (inputLength >= 8) {
                    variant = localeStr.substring(j + 1);
                } else {
                    throw new
                        IllegalArgumentException("Illegal locale String: " +
                                                 localeStr);
                }
            }
        }
        if (variant != null && country != null && lang != null) {
            result = new Locale(lang, country, variant);
        } else if (lang != null && country != null) {
            result = new Locale(lang, country);
        } else if (lang != null) {
            result = new Locale(lang, "");
        }
        return result;
    }


    /**
     * @param str local string
     * @param set the substring
     * @param fromIndex starting index
     * @return starting at <code>fromIndex</code>, the index of the
     *         first occurrence of any substring from <code>set</code> in
     *         <code>toSearch</code>, or -1 if no such match is found
     */
    private static int indexOfSet(String str, char[] set, int fromIndex) {
        int result = -1;
        for (int i = fromIndex, len = str.length(); i < len; i++) {
            for (int j = 0, innerLen = set.length; j < innerLen; j++) {
                if (str.charAt(i) == set[j]) {
                    result = i;
                    break;
                }
            }
            if (-1 != result) {
                break;
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


    private Object[] values;

    @Override
    public Object saveState(FacesContext context) {

        if (values == null) {
            values = new Object[8];
        }

        values[0] = super.saveState(context);
        values[1] = renderKitId;
        values[2] = viewId;
        values[3] = locale;
        values[4] = lastId;
        values[5] = saveAttachedState(context, beforePhase);
        values[6] = saveAttachedState(context, afterPhase);
        values[7] = saveAttachedState(context, phaseListeners);
        return (values);

    }

    @Override
    public void restoreState(FacesContext context, Object state) {

        values = (Object[]) state;
        super.restoreState(context, values[0]);
        renderKitId = (String) values[1];
        viewId = (String) values[2];
        locale = (Locale) values[3];
        lastId = ((Integer) values[4]).intValue();
        beforePhase =
              (MethodExpression) restoreAttachedState(context, values[5]);
        afterPhase =
              (MethodExpression) restoreAttachedState(context, values[6]);
        phaseListeners = TypedCollections.dynamicallyCastList((List)
              restoreAttachedState(context, values[7]), PhaseListener.class);
    }


}

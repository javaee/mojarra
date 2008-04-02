/*
 * $Id: UIViewRoot.java,v 1.27 2004/04/05 18:26:00 rkitain Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;
import javax.faces.event.PhaseId;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.faces.el.ValueBinding;


/**
 * <p><strong>UIViewRoot</strong> is the UIComponent that represents the
 * root of the UIComponent tree.  This component has no rendering, it
 * just serves as the root of the component tree.</p>
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
     * associated with this view.  Unless explicitly set, this will be the
     * value defined by calling 
     * {@link javax.faces.application.ViewHandler#calculateRenderKitId}.</p>
     */
    public String getRenderKitId() {

	String result = null;
	if (null != renderKitId) {
	    result = this.renderKitId;
	}
	else {
	    ValueBinding vb = getValueBinding("renderKitId");
	    FacesContext context = getFacesContext();
	    if (vb != null) {
		result = (String) vb.getValue(context);
	    } 
	    else {
	        result = context.getApplication().getViewHandler().calculateRenderKitId(context);
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
		events[i] = new ArrayList(5);
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
	List eventsForPhaseId = null;

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
		    FacesEvent event = (FacesEvent)
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
			(FacesEvent) eventsForPhaseId.get(cursor);
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
     * FacesContext#renderResponse} to be called.</p>
     *
     * @param context {@link FacesContext} for the request we are processing
     *
     * @exception NullPointerException if <code>context</code>
     *  is <code>null</code>
     */
    public void processDecodes(FacesContext context) {
        super.processDecodes(context);
        broadcastEvents(context, PhaseId.APPLY_REQUEST_VALUES);
	// clear out the events if we're skipping to render-response
	if (context.getRenderResponse()) {
	    events = null;
	}

    }

    /**
     * <p>Override the default {@link UIComponentBase#encodeBegin}
     * behavior to reset the mechanism used in {@link #createUniqueId}
     * before falling through to the standard superclass processing.</p>
     *
     */

    public void encodeBegin(FacesContext context) throws IOException {
	lastId = 0;
	super.encodeBegin(context);
    }


    /**
     * <p>Override the default {@link UIComponentBase#processValidators}
     * behavior to broadcast any queued events after the default
     * processing has been completed and to clear out any events
     * for later phases if the event processing for this phase caused {@link
     * FacesContext#renderResponse} to be called.</p>
     *
     * @param context {@link FacesContext} for the request we are processing
     *
     * @exception NullPointerException if <code>context</code>
     *  is <code>null</code>
     */
    public void processValidators(FacesContext context) {

        super.processValidators(context);
        broadcastEvents(context, PhaseId.PROCESS_VALIDATIONS);
	// clear out the events if we're skipping to render-response
	if (context.getRenderResponse()) {
	    events = null;
	}

    }


    /**
     * <p>Override the default {@link UIComponentBase} behavior to broadcast
     * any queued events after the default processing has been completed.</p>
     *
     * @param context {@link FacesContext} for the request we are processing
     *
     * @exception NullPointerException if <code>context</code>
     *  is <code>null</code>
     */
    public void processUpdates(FacesContext context) {

        super.processUpdates(context);
        broadcastEvents(context, PhaseId.UPDATE_MODEL_VALUES);

    }


    /**
     * <p>Broadcast any events that have been queued for the <em>Invoke
     * Application</em> phase of the request processing lifecycle.</p>
     *
     * @param context {@link FacesContext} for the request we are processing
     *
     * @exception NullPointerException if <code>context</code>
     *  is <code>null</code>
     */
    public void processApplication(FacesContext context) {

        // NOTE - no tree walk is performed; this is a UIViewRoot-only operation
        broadcastEvents(context, PhaseId.INVOKE_APPLICATION);

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
     * a value binding for "locale", get its value.  If the value is
     * <code>null</code>, return the result of calling {@link
     * javax.faces.application.ViewHandler#calculateLocale}.  If the
     * value is an instance of <code>java.util.Locale</code> return it.
     * If the value is a String, convert it to a
     * <code>java.util.Locale</code> and return it.  If there is no
     * value binding for "locale", return the result of calling {@link
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
	    ValueBinding vb = getValueBinding("locale");
	    FacesContext context = getFacesContext();
	    if (vb != null) {
                Object resultLocale = vb.getValue(context);
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
     *                    country codes must be separated by hyphen (�-�) or 
     *                    underscore (�_�)."
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
    }
    
    // ----------------------------------------------------- StateHolder Methods


    public Object saveState(FacesContext context) {

        Object values[] = new Object[4];
        values[0] = super.saveState(context);
        values[1] = renderKitId;
        values[2] = viewId;
        values[3] = locale;
        return (values);

    }


    public void restoreState(FacesContext context, Object state) {

        Object values[] = (Object[]) state;
        super.restoreState(context, values[0]);
        renderKitId = (String) values[1];
        viewId = (String) values[2];
        locale = (Locale)values[3];

    }


}

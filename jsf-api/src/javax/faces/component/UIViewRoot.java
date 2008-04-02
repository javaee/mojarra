/*
 * $Id: UIViewRoot.java,v 1.9 2003/10/15 18:11:35 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import javax.faces.context.FacesContext;
import javax.faces.event.FacesEvent;
import javax.faces.event.PhaseId;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;


/**
 * <p><strong>UIViewRoot</strong> is the UIComponent that represents the
 * root of the UIComponent tree.  This component has no rendering, it
 * just serves as the root of the component tree.</p>
 */

public class UIViewRoot extends UIComponentBase {


    // ------------------------------------------------------ Constants
    
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


    /**
     * <p>The render kit identifier of the {@link RenderKit} associated
     * wth this view.</p>
     */
    private String renderKitId = RenderKitFactory.DEFAULT_RENDER_KIT;


    /**
     * <p>Return the render kit identifier of the {@link RenderKit}
     * associated with this view.</p>
     */
    public String getRenderKitId() {

        return (this.renderKitId);

    }


    /**
     * <p>Set the render kit identifier of the {@link RenderKit}
     * associated with this view.</p>
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
     * <p>The list of events that have been queued for later broadcast.  This
     * instance is lazily instantiated.  This list is <strong>NOT</strong>
     * part of the state that is saved and restored for this component.</p>
     */
    private transient List events = null;


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
        // We are a UIViewRoot, so no need to check for the ISE
        if (events == null) {
            events = new ArrayList(5);
        }
        events.add(event);

    }


    /**
     * <p>Broadcast any events that have been queued.</p>
     *
     * @param context {@link FacesContext} for the current request
     * @param phaseId {@link PhaseId} of the current phase
     */
    private void broadcastEvents(FacesContext context, PhaseId phaseId) {

        if (events == null) {
            return;
        }

        // We cannot use an Iterator because we will get
        // ConcurrentModificationException errors, so fake it
        int cursor = 0;
        while (cursor < events.size()) {
            FacesEvent event = (FacesEvent) events.get(cursor);
            UIComponent source = event.getComponent();
            if (!source.broadcast(event, phaseId)) {
                events.remove(cursor); // Stay at current position
            } else {
                cursor++; // Advance to next event
            }
        }

        if (events.size() < 1) {
            events = null;
        }

    }


    // ------------------------------------------------ Lifecycle Phase Handlers


    /**
     * <p>Override the default {@link UIComponentBase#processDecodes} behavior
     * to broadcast any queued events after the default processing has been
     * completed.</p>
     *
     * @param context {@link FacesContext} for the request we are processing
     *
     * @exception NullPointerException if <code>context</code>
     *  is <code>null</code>
     */
    public void processDecodes(FacesContext context) {

        super.processDecodes(context);
        broadcastEvents(context, PhaseId.APPLY_REQUEST_VALUES);

    }


    /**
     * <p>Override the default {@link UIComponentBase#processValidators}
     * behavior to broadcast any queued events after the default processing
     * has been completed.</p>
     *
     * @param context {@link FacesContext} for the request we are processing
     *
     * @exception NullPointerException if <code>context</code>
     *  is <code>null</code>
     */
    public void processValidators(FacesContext context) {

        super.processValidators(context);
        broadcastEvents(context, PhaseId.PROCESS_VALIDATIONS);

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
     */
    public Locale getLocale() {
        if (locale == null ) {
            return Locale.getDefault();
        }
        return locale;
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

        Object values[] = new Object[3];
        values[0] = super.saveState(context);
        values[1] = renderKitId;
        values[2] = viewId;
        return (values);

    }


    public void restoreState(FacesContext context, Object state)
        throws IOException {

        Object values[] = (Object[]) state;
        super.restoreState(context, values[0]);
        renderKitId = (String) values[1];
        viewId = (String) values[2];

    }


}

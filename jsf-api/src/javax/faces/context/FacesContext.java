/*
 * $Id: FacesContext.java,v 1.35 2003/01/16 23:27:36 craigmcc Exp $
 */

/*
 * Copyright 2002-2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.context;


import java.util.Iterator;
import java.util.Locale;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.event.ApplicationEvent;
import javax.faces.event.FacesEvent;
import javax.faces.lifecycle.ApplicationHandler;
import javax.faces.lifecycle.ViewHandler;
import javax.faces.tree.Tree;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpSession;


/**
 * <p><strong>FacesContext</strong> contains all of the per-request state
 * information related to the processing of a single JavaServer Faces request,
 * and the rendering of the corresponding response.  It is passed to, and
 * potentially modified by, each phase of the request processing lifecycle.</p>
 *
 * <p>A <code>FacesContext</code> instance is associated with a particular
 * request at the beginning of request processing, by a call to the
 * <code>getFacesContext()</code> method of the {@link FacesContextFactory}
 * instance associated with the current web application.  The instance
 * remains active until its <code>release()</code> method is called, after
 * which no further references to this instance are allowed.  While a
 * <code>FacesContext</code> instance is active, it must not be referenced
 * from any thread other than the one upon which the servlet container
 * executing this web application utilizes for the processing of this request.
 * </p>
 */

public abstract class FacesContext {


    // ----------------------------------------------------- Manifest Constants


    /**
     * <p>The name of the request attribute under which the
     * <code>FacesContext</code> instance for the current request will
     * be stored.</p>
     */
    public static final String FACES_CONTEXT_ATTR =
        "javax.faces.context.FacesContext";


    // ------------------------------------------------------------- Properties


    /**
     * <p>Return an <code>Iterator</code> over the {@link ApplicationEvent}s
     * for events that should be handled by the application during the
     * <em>Invoke Application</em> phase of the request processing
     * lifecycle.</p>
     */
    public abstract Iterator getApplicationEvents();


    /**
     * <p>Return the number of {@link ApplicationEvent}s that have been queued
     * tot he application, or zero if no such events have been queued.</p>
     */
    public abstract int getApplicationEventsCount();


    /**
     * <p>Return the {@link ApplicationHandler} instance to be utilized during
     * the <em>Invoke Application</em> phase of the request processing
     * lifecycle.</p>
     */
    public abstract ApplicationHandler getApplicationHandler();


    /**
     * <p>Return an <code>Iterator</code> over the {@link FacesEvent}s that
     * have been queued, in the order that they were queued.  The
     * <code>Iterator</code> returned by this method must support the
     * following functionality:</p>
     * <ul>
     * <li>The <code>remove()</code> operation may be utilized by the
     *     JSF implementation to remove the current event when it has
     *     been completely handled.</li>
     * <li>Event handlers may add new events to the list while it is
     *     being iterated over, by calling <code>addFacesEvent()</code>.</li>
     * </ul>
     *
     * <p>If no events have been queued, an empty <code>Iterator</code>
     * must be returned.
     */
    public abstract Iterator getFacesEvents();


    /**
     * <p>Return the <code>HttpSession</code> instance for the session
     * associated with the current request (if any); otherwise, return
     * <code>null</code>.</p>
     */
    public abstract HttpSession getHttpSession();


    /**
     * <p>Return the <code>Locale</code> to be used in localizing the
     * response being created for this <code>FacesContext</code>.</p>
     */
    public abstract Locale getLocale();


    /**
     * <p>Set the <code>Locale</code> to be used in localizing the
     * response being created for this <code>FacesContext</code>.  If not
     * set, the default Locale for our servlet container will be used.</p>
     *
     * @param locale The new localization Locale
     */
    public abstract void setLocale(Locale locale);


    /**
     * <p>Return the maximum severity level recorded on any {@link Message}s
     * that has been queued, whether or not they are associated with any
     * specific {@link UIComponent}.  If no such messages have been queued,
     * return a value less than <code>Message.SEVERITY_INFO</code>.</p>
     */
    public abstract int getMaximumSeverity();


    /**
     * <p>Return an <code>Iterator</code> over the {@link Message}s that have
     * been queued, whether or not they are associated with any specific
     * {@link UIComponent}.  If no such messages have been queued, return an
     * empty <code>Iterator</code>.</p>
     */
    public abstract Iterator getMessages();


    /**
     * <p>Return an <code>Iterator</code> over the {@link Message}s that have
     * been queued that are associated with the specified {@link UIComponent},
     * (if <code>component</code> is not <code>null</code>), or over the
     * {@link Message}s that have been queued that are not associated with any
     * specific {@link UIComponent} (if <code>component</code> is
     * <code>null</code>).  If no such messages have been queued, return an
     * empty <code>Iterator</code>.</p>
     *
     * @param component The {@link UIComponent} for which messages are
     *  requested, or <code>null</code> for messages not associated with
     *  any component
     */
    public abstract Iterator getMessages(UIComponent component);


    /**
     * <p>Return the {@link ResponseStream} to which components should
     * direct their binary output.  Within a given response, components
     * can use either the ResponseStream or the ResponseWriter,
     * but not both.
     */
    public abstract ResponseStream getResponseStream();


    /**
     * <p>Set the {@link ResponseStream} to which components should
     * direct their binary output.
     *
     * @param responseStream The new ResponseStream for this response
     *
     * @exception NullPointerException if <code>responseStream</code>
     *  is <code>null</code>
     */
    public abstract void setResponseStream(ResponseStream responseStream);


    /**
     * <p>Return the {@link ResponseWriter} to which components should
     * direct their character-based output.  Within a given response,
     * components can use either the ResponseStream or the ResponseWriter,
     * but not both.
     */
    public abstract ResponseWriter getResponseWriter();


    /**
     * <p>Set the {@link ResponseWriter} to which components should
     * direct their character-based output.
     *
     * @param responseWriter The new ResponseWriter for this response
     *
     * @exception NullPointerException if <code>responseWriter</code>
     *  is <code>null</code>
     */
    public abstract void setResponseWriter(ResponseWriter responseWriter);


    /**
     * <p>Return the <code>ServletContext</code> object for the web application
     * associated with this request.</p>
     */
    public abstract ServletContext getServletContext();


    /**
     * <p>Return the <code>ServletRequest</code> object representing the
     * current request that is being processed.</p>
     */
    public abstract ServletRequest getServletRequest();


    /**
     * <p>Return the <code>ServletResponse</code> object representing the
     * current response that is being rendered.</p>
     */
    public abstract ServletResponse getServletResponse();


    /**
     * <p>Return the component {@link Tree} that is associated with the
     * this request.
     * </p>
     */
    public abstract Tree getTree();


    /**
     * <p>Set the {@link Tree} that is associated with this request.
     * This method can only be called by the application handler (or a
     * class that the handler calls), and only during the <em>Invoke
     * Application</em> phase of the request processing lifecycle.</p>
     *
     * @param tree The new component tree
     *
     * @exception IllegalStateException if this method is called more than
     *  once without a call to <code>release()</code> in beween
     * @exception NullPointerException if <code>tree</code>
     *  is <code>null</code>
     */
    public abstract void setTree(Tree tree);


    /**
     * <p>Return the {@link ViewHandler} instance to be utilized during
     * the <em>Render Response</em> phase of the request processing
     * lifecycle.</p>
     */
    public abstract ViewHandler getViewHandler();


    // --------------------------------------------------------- Public Methods


    /**
     * <p>Append a {@link ApplicationEvent} to the set of events that should be
     * processed by the application during the <em>Invoke Application</em>
     * phase of the request processing lifecycle.</p>
     *
     * @param event The event to be appended
     *
     * @exception NullPointerException if <code>event</code>
     *  is <code>null</code>
     */
    public abstract void addApplicationEvent(ApplicationEvent event);


    /**
     * <p>Append a {@link FacesEvent} to the set of events that should be
     * processed at the end of the current phase of the request processing
     * lifecycle.</p>
     *
     * @param event The event to be queued
     *
     * @exception NullPointerException if <code>event</code>
     *  is <code>null</code>
     */
    public abstract void addFacesEvent(FacesEvent event);


    /**
     * <p>Append a {@link Message} to the set of messages associated with
     * the specified {@link UIComponent}, if <code>component</code> is
     * not <code>null</code>.  If <code>component</code> is <code>null</code>,
     * this {@link Message} is assumed to not be associated with any
     * specific component instance.</p>
     *
     * @param component The component with which this message is associated
     *  (if any)
     * @param message The message to be appended
     *
     * @exception NullPointerException if <code>message</code>
     *  is <code>null</code>
     */
    public abstract void addMessage(UIComponent component, Message message);


    /**
     * <p>Evaluate the specified model reference expression, and return the
     * expected type of the corresponding value, if it can be determined;
     * otherwise, return <code>null</code>.</p>
     *
     * @param modelReference Model reference expression to be evaluated
     *
     * @exception FacesException if an error occurs during expression
     *  evaluation
     * @exception IllegalArgumentException if the model reference
     *  expression is invalid
     * @exception NullPointerException if <code>modelReference</code>
     *  is <code>null</code>
     */
    public abstract Class getModelType(String modelReference)
        throws FacesException;


    /**
     * <p>Evaluate the specified model reference expression, and return the
     * corresponding data value (which may be null).  No data type conversion
     * is performed.</p>
     *
     * @param modelReference Model reference to be evaluated
     *
     * @exception FacesException if an error occurs during expression
     *  evaluation
     * @exception IllegalArgumentException if the model reference
     *  expression is invalid
     * @exception NullPointerException if <code>modelReference</code>
     *  is <code>null</code>
     */
    public abstract Object getModelValue(String modelReference)
        throws FacesException;


    /**
     * <p>Evaluate the specified model reference expression, and set the
     * corresponding data value (which may be null).  No data type conversion
     * is performed.</p>
     *
     * @param modelReference Model reference to be evaluated
     * @param value New model data to be stored in the model
     *
     * @exception FacesException if an error occurs during expression
     *  evaluation
     * @exception IllegalArgumentException if the model reference
     *  expression is invalid
     * @exception NullPointerException if <code>modelReference</code>
     *  is <code>null</code>
     */
    public abstract void setModelValue(String modelReference, Object value)
        throws FacesException;


    /**
     * <p>Release any resources associated with this <code>FacesContext</code>
     * instance.  Faces implementations may choose to pool instances in the
     * associated {@link FacesContextFactory} to avoid repeated object creation
     * and garbage collection.</p>
     */
    public abstract void release();


    /**
     * <p>Signal the JavaSerer faces implementation that, as soon as the
     * current phase of the request processing lifecycle has been completed,
     * control should be passed to the <em>Render Response</em> phase,
     * bypassing any phases that have not been executed yet.</p>
     */
    public abstract void renderResponse();


    /**
     * <p>Signal the JavaServer Faces implementation that the HTTP response
     * for this request has already been generated (such as an HTTP redirect),
     * and that the request processing lifecycle should be terminated as soon
     * as the current phase is completed.</p>
     */
    public abstract void responseComplete();


    // --------------------------------------------------------- Static Methods


    /**
     * <p>The <code>ThreadLocal</code> variable used to record the
     * {@link FacesContext} instance for each processing thread.</p>
     */
    private static ThreadLocal instance = new ThreadLocal() {
            protected Object initialValue() { return (null); }
        };


    /**
     * <p>Return the {@link FacesContext} instance for the request that is
     * being processed by the current thread.</p>
     */
    public static FacesContext getCurrentInstance() {

        return ((FacesContext) instance.get());

    }


    /**
     * <p>Set the {@link FacesContext} instance for the request that is
     * being processed by the current thread.</p>
     *
     * @param context The {@link FacesContext} instance for the current thread
     */
    protected static void setCurrentInstance(FacesContext context) {

        instance.set(context);

    }


}

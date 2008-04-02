/*
 * $Id: FacesContext.java,v 1.57 2004/01/15 06:03:32 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.context;


import java.util.Iterator;
import java.util.Locale;
import javax.faces.FacesException;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.render.RenderKit;


/**
 * <p><strong>FacesContext</strong> contains all of the per-request state
 * information related to the processing of a single JavaServer Faces request,
 * and the rendering of the corresponding response.  It is passed to, and
 * potentially modified by, each phase of the request processing lifecycle.</p>
 *
 * <p>A {@link FacesContext} instance is associated with a particular
 * request at the beginning of request processing, by a call to the
 * <code>getFacesContext()</code> method of the {@link FacesContextFactory}
 * instance associated with the current web application.  The instance
 * remains active until its <code>release()</code> method is called, after
 * which no further references to this instance are allowed.  While a
 * {@link FacesContext} instance is active, it must not be referenced
 * from any thread other than the one upon which the servlet container
 * executing this web application utilizes for the processing of this request.
 * </p>
 */

public abstract class FacesContext {


    // -------------------------------------------------------------- Properties


    /**
     * <p>Return the {@link Application} instance associated with this
     * web application.</p>
     *
     * @exception IllegalStateException if this method is called after
     *  this instance has been released
     */
    public abstract Application getApplication();


    /**
     * <p>Return an <code>Iterator</code> over the client identifiers for
     * which at least one {@link FacesMessage} has been queued.  If there are no
     * such client identifiers, an empty <code>Iterator</code> is returned.
     * If any messages have been queued that were not associated with any
     * specific client identifier, a <code>null</code> value will be included
     * in the iterated values.</p>
     *
     * @exception IllegalStateException if this method is called after
     *  this instance has been released
     */
    public abstract Iterator getClientIdsWithMessages();


    /**
     * <p>Return the {@link ExternalContext} instance for this
     * <code>FacesContext</code> instance.</p>
     *
     * @exception IllegalStateException if this method is called after
     *  this instance has been released
     */
    public abstract ExternalContext getExternalContext();


    /**
     * <p>Return the maximum severity level recorded on any
     * {@link FacesMessage}s that has been queued, whether or not they are
     * associated with any specific {@link UIComponent}.  If no such messages
     * have been queued, return <code>null</code>.</p>
     *
     * @exception IllegalStateException if this method is called after
     *  this instance has been released
     */
    public abstract Severity getMaximumSeverity();


    /**
     * <p>Return an <code>Iterator</code> over the {@link FacesMessage}s
     * that have been queued, whether or not they are associated with any
     * specific client identifier.  If no such messages have been queued,
     * return an empty <code>Iterator</code>.</p>
     *
     * @exception IllegalStateException if this method is called after
     *  this instance has been released
     */
    public abstract Iterator getMessages();


    /**
     * <p>Return an <code>Iterator</code> over the {@link FacesMessage}s that
     * have been queued that are associated with the specified client identifier
     * (if <code>clientId</code> is not <code>null</code>), or over the
     * {@link FacesMessage}s that have been queued that are not associated with
     * any specific client identifier (if <code>clientId</code> is
     * <code>null</code>).  If no such messages have been queued, return an
     * empty <code>Iterator</code>.</p>
     *
     * @param clientId The client identifier for which messages are
     *  requested, or <code>null</code> for messages not associated with
     *  any client identifier
     *
     * @exception IllegalStateException if this method is called after
     *  this instance has been released
     */
    public abstract Iterator getMessages(String clientId);


    /**
     * <p>Return the {@link RenderKit} instance for the render kit identifier
     * specified on our {@link UIViewRoot}, if there is one.  If there is no
     * current {@link UIViewRoot}, if the {@link UIViewRoot} does not have a
     * specified <code>renderKitId</code>, or if there is no {@link RenderKit}
     * for the specified identifier, return <code>null</code> instead.</p>
     */
    public abstract RenderKit getRenderKit();


    /**
     * <p>Return <code>true</code> if the <code>renderResponse()</code>
     * method has been called for the current request.</p>
     *
     * @exception IllegalStateException if this method is called after
     *  this instance has been released
     */
    public abstract boolean getRenderResponse();


    /**
     * <p>Return <code>true</code> if the <code>responseComplete()</code>
     * method has been called for the current request.</p>
     *
     * @exception IllegalStateException if this method is called after
     *  this instance has been released
     */
    public abstract boolean getResponseComplete();


    /**
     * <p>Return the {@link ResponseStream} to which components should
     * direct their binary output.  Within a given response, components
     * can use either the ResponseStream or the ResponseWriter,
     * but not both.
     *
     * @exception IllegalStateException if this method is called after
     *  this instance has been released
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
     *
     * @exception IllegalStateException if this method is called after
     *  this instance has been released
     */
    public abstract void setResponseStream(ResponseStream responseStream);


    /**
     * <p>Return the {@link ResponseWriter} to which components should
     * direct their character-based output.  Within a given response,
     * components can use either the ResponseStream or the ResponseWriter,
     * but not both.
     *
     * @exception IllegalStateException if this method is called after
     *  this instance has been released
     */
    public abstract ResponseWriter getResponseWriter();


    /**
     * <p>Set the {@link ResponseWriter} to which components should
     * direct their character-based output.
     *
     * @param responseWriter The new ResponseWriter for this response
     *
     * @exception IllegalStateException if this method is called after
     *  this instance has been released
     * @exception NullPointerException if <code>responseWriter</code>
     *  is <code>null</code>
     */
    public abstract void setResponseWriter(ResponseWriter responseWriter);


    /**
     * <p>Return the root component that is associated with the this request.
     * </p>
     *
     * @exception IllegalStateException if this method is called after
     *  this instance has been released
     */
    public abstract UIViewRoot getViewRoot();


    /**
     * <p>Set the root component that is associated with this request.
     * This method can only be called by the application handler (or a
     * class that the handler calls), and only during the <em>Invoke
     * Application</em> phase of the request processing lifecycle.</p>
     *
     * @param root The new component {@link UIViewRoot} component
     *
     * @exception IllegalStateException if this method is called after
     *  this instance has been released
     * @exception NullPointerException if <code>root</code>
     *  is <code>null</code>
     */
    public abstract void setViewRoot(UIViewRoot root);


    // ---------------------------------------------------------- Public Methods


    /**
     * <p>Append a {@link FacesMessage} to the set of messages associated with
     * the specified client identifier, if <code>clientId</code> is
     * not <code>null</code>.  If <code>clientId</code> is <code>null</code>,
     * this {@link FacesMessage} is assumed to not be associated with any
     * specific component instance.</p>
     *
     * @param clientId The client identifier with which this message is
     *  associated (if any)
     * @param message The message to be appended
     *
     * @exception IllegalStateException if this method is called after
     *  this instance has been released
     * @exception NullPointerException if <code>message</code>
     *  is <code>null</code>
     */
    public abstract void addMessage(String clientId, FacesMessage message);


    /**
     * <p>Release any resources associated with this
     * <code>FacesContext</code> instance.  Faces implementations may
     * choose to pool instances in the associated {@link
     * FacesContextFactory} to avoid repeated object creation and
     * garbage collection.  After <code>release()</code> is called on a
     * <code>FacesContext</code> instance (until the
     * <code>FacesContext</code> instance has been recycled by the
     * implementation for re-use), calling any other methods will cause
     * an <code>IllegalStateException</code> to be thrown.</p>
     *
     * <p>The implementation must call {@link #setCurrentInstance}
     * passing <code>null</code> to remove the association between this
     * thread and this dead <code>FacesContext</code> instance.</p>
     *
     * @exception IllegalStateException if this method is called after
     *  this instance has been released
     */
    public abstract void release();


    /**
     * <p>Signal the JavaServer faces implementation that, as soon as the
     * current phase of the request processing lifecycle has been completed,
     * control should be passed to the <em>Render Response</em> phase,
     * bypassing any phases that have not been executed yet.</p>
     *
     * @exception IllegalStateException if this method is called after
     *  this instance has been released
     */
    public abstract void renderResponse();


    /**
     * <p>Signal the JavaServer Faces implementation that the HTTP response
     * for this request has already been generated (such as an HTTP redirect),
     * and that the request processing lifecycle should be terminated as soon
     * as the current phase is completed.</p>
     *
     * @exception IllegalStateException if this method is called after
     *  this instance has been released
     */
    public abstract void responseComplete();


    // ---------------------------------------------------------- Static Methods


    /**
     * <p>The <code>ThreadLocal</code> variable used to record the
     * {@link FacesContext} instance for each processing thread.</p>
     */
    private static ThreadLocal instance = new ThreadLocal() {
            protected Object initialValue() { return (null); }
        };


    /**
     * <p>Return the {@link FacesContext} instance for the request that is
     * being processed by the current thread, if any.</p>
     */
    public static FacesContext getCurrentInstance() {

        return ((FacesContext) instance.get());

    }


    /**
     * <p>Set the {@link FacesContext} instance for the request that is
     * being processed by the current thread.</p>
     *
     * @param context The {@link FacesContext} instance for the current
     * thread, or <code>null</code> if this thread no longer has a
     * <code>FacesContext</code> instance.
     *
     */
    protected static void setCurrentInstance(FacesContext context) {

        instance.set(context);

    }


}

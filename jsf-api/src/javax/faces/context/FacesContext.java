/*
 * $Id: FacesContext.java,v 1.18 2002/06/08 02:24:17 craigmcc Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.context;


import java.util.Iterator;
import java.util.Locale;
import javax.faces.FacesException;     // FIXME - subpackage?
import javax.faces.event.FacesEvent;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.tree.Tree;
import javax.servlet.ServletRequest;
import javax.servlet.ServletContext;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpSession;


/**
 * <p><strong>FacesContext</strong> contains all of the per-request state
 * information related to the processing of a single JavaServer Faces request,
 * and the rendering of the corresponding response.  It is passed to, and
 * potentially modified by, each phase of the request processing lifecycle.</p>
 *
 * <h3>Lifecycle</h3>
 *
 * <p>A <code>FacesContext</code> instance is associated with a particular
 * request at the beginning of request processing, by a call to the
 * <code>createFacesContext()</code> method of the {@link FacesContextFactory}
 * instance associated with the current web application.  The instance
 * remains active until its <code>release()</code> method is called, after
 * which no further references to this instance are allowed.  While a
 * <code>FacesContext</code> instance is active, it MUST NOT be referenced
 * from any thread other than the one upon which the servlet container
 * executing this web application utilizes for the processing of this request.
 * </p>
 *
 * <p><strong>FIXME</strong> - Specify starting contents of all properties
 * when returned from <code>FacesContextFactory.createFacesContext()</code>,
 * versus what changes are expected in each phase of the request processing
 * lifecycle.</p>
 *
 * <p><strong>FIXME</strong> - ObjectManager et. al.</p>
 *
 * <p><strong>FIXME</strong> - Do we need direct access to the
 * output stream or writer associated with our response so it can be cached?
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
     * <p>Return an <code>Iterator</code> over the {@link FacesEvent}s for
     * events that should be handled by the application during the
     * <em>Invoke Application</em> phase of the request processing lifecycle.
     * </p>
     */
    public abstract Iterator getApplicationEvents();


    /**
     * <p>Return the <code>HttpSession</code> instance for the session
     * associated with the current request (if any); otherwise, return
     * <code>null</code>.</p>
     */
    public abstract HttpSession getHttpSession();


    /**
     * <p>Return the {@link Lifecycle} instance that is managing the
     * processing of the request represented by this <code>FacesContext</code>
     * instance.</p>
     */
    public abstract Lifecycle getLifecycle();


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
     * <p>Return the {@link MessageList} instance containing validation (and
     * other) messages that have been accumulated during the processing of
     * the current request.</p>
     */
    public abstract MessageList getMessageList();


    /**
     * <p>Return the phase identifier representing the current phase in the
     * request processing lifecycle.</p>
     */
    public abstract int getPhaseId();


    /**
     * <p>Set the phase identifier representing the current phase in the
     * request processing lifecycle.  This should only be called by the
     * <code>execute()</code> method of the {@link Lifecycle} instance that
     * is processing the current request.
     *
     * @param phaseId The new phase identifier
     */
    public abstract void setPhaseId(int phaseId);


    /**
     * <p>Return the {@link Tree} that is associated with the inbound request.
     * </p>
     */
    public abstract Tree getRequestTree();


    /**
     * <p>Set the {@link Tree} that is associated with the inbound request.
     * </p>
     *
     * <p><strong>FIXME</strong> - Does this method need to be public?</p>
     *
     * @param tree The new inbound request tree
     *
     * @exception IllegalStateException if this method is called more than
     *  once without a call to <code>release()</code> in beween
     * @exception NullPointerException if <code>tree</code>
     *  is <code>null</code>
     */
    public abstract void setRequestTree(Tree tree);


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
     */
    public abstract void setResponseStream(ResponseStream responseStream);


    /**
     * <p>Return the {@link Tree} that is associated with the outbound
     * response.  Unless otherwise specified (by a call to
     * <code>setResponseTree()</code>, this will return the same
     * {@link Tree} returned by <code>getRequestTree()</code>.</p>
     */
    public abstract Tree getResponseTree();


    /**
     * <p>Set the {@link Tree} that is the associated with the
     * outbound response.</p>
     *
     * @param tree The new outbound response tree
     *
     * @exception NullPointerException if <code>tree</code>
     *  is <code>null</code>
     */
    public abstract void setResponseTree(Tree tree);


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


    // --------------------------------------------------------- Public Methods


    /**
     * <p>Append a {@link FacesEvent} to the set of events that should be
     * processed by the application during the <em>Invoke Application</em>
     * phase of the request processing lifecycle.</p>
     *
     * @param event The event to be appended
     */
    public abstract void addApplicationEvent(FacesEvent event);


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
     * @exception NullPointerException if <code>model</code>
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
     * @exception NullPointerException if <code>model</code>
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
     * @exception NullPointerException if <code>model</code>
     *  is <code>null</code>
     */
    public abstract void setModelValue(String model, Object value)
        throws FacesException;


    /**
     * <p>Release any resources associated with this <code>FacesContext</code>
     * instance.  Faces implementations may choose to pool instances in the
     * associated {@link FacesContextFactory} to avoid repeated object creation
     * and garbage collection.</p>
     */
    public abstract void release();


}

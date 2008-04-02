/*
 * $Id: ViewHandler.java,v 1.20 2003/10/22 04:43:03 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.application;

import java.util.Locale;
import java.io.IOException;
import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.faces.component.UIViewRoot;


/**
 * <p><strong>ViewHandler</strong> is the pluggablity mechanism for
 * allowing implementations of or applications using the JavaServer
 * Faces specification to provide their own handling of the activities
 * in the <em>Render Response</em> and <em>Restore View</em>
 * phases of the request processing lifecycle.  This allows for
 * implementations to support different response generation
 * technologies, and state saving/restoring approaches.  JSF
 * implementations, or JSF-based applications, can register an instance
 * of this interface by calling the <code>setViewHandler()</code> method
 * of the {@link Application} prior to the application receiving its
 * first request from a client.  </p>
 *
 * <p>The <code>ViewHandler</code> implementation must provide a
 * reference to an implementation of a {@link StateManager}, which is
 * used for saving and restoring the state of a Faces UI between
 * requests.</p>
 *
 * <p>A default implementation of <code>ViewHandler</code> must be
 * provided by the JSF implementation, which will be utilized unless
 * <code>setViewHandler()</code> is called to establish a different one.
 * This default implementation is designed to render JavaServer Faces
 * JSP pages.  During <em>Render Response</em>, this default instance
 * will treat the <code>viewId</code> property of the response view as a
 * context-relative path (after prefixing it with a slash), and will
 * perform a {@link javax.faces.context.ExternalContext#dispatchMessage}
 * call to that path.</p>
 *
 * <p>Please see {@link StateManager} for information on how the
 * <code>ViewHandler</code> uses the {@link StateManager}. </p>
 */

public interface ViewHandler {

    /**
     * <p>The key, in the session's attribute set, under which the
     * response character encoding is stored and retrieved.</p>
     *
     */
    public static String CHARACTER_ENCODING_KEY = 
	"javax.faces.request.charset";


    /**
     * <p>Allow the web application to define an alternate suffix for
     * pages containing JSF content.  If this init parameter is not
     * specified, the default value is taken from the value of the
     * constant {@link #DEFAULT_SUFFIX}.</p>
     *
     */

    public static String DEFAULT_SUFFIX_PARAM_NAME = 
	"javax.faces.DEFAULT_SUFFIX";

    /**
     * <p>The value to use for the default suffix if the webapp is using
     * url suffix mapping.</p>
     */

    public static String DEFAULT_SUFFIX = ".jsp";


    /**
     * <p>Perform whatever actions are required to render the response
     * view to the <code>ServletResponse</code> associated
     * with the specified {@link UIViewRoot}.  Also perform required
     * actions to save the state of the response between requests, using
     * the {@link StateManager}.  This method is responsible for
     * ensuring that the {@link FacesContext} has been provided with
     * valid {@link javax.faces.context.ResponseWriter} and {@link
     * javax.faces.context.ResponseStream} instances for the current
     * request.</p>
     *
     * <p>The default implementation of <code>renderView</code> must use
     * <code>HttpServletRequest.getServletPath()</code> in conjunction
     * with the one or more <code>url-pattern</code> mappings obtained
     * from the deployment descriptor for this web application to
     * determine which mapping was used to invoke the {@link
     * javax.faces.webapp.FacesServlet} If a prefix mapping was used,
     * this method simply calls {@link
     * javax.faces.context.ExternalContext#dispatchMessage} passing the
     * <code>viewId</code> of the argument <code>viewToRender</code>.
     * If a suffix mapping was used, the default implementation must
     * check the servlet context init parameter named by the value of
     * the constant {@link #DEFAULT_SUFFIX_PARAM_NAME}.  If this
     * parameter is not defined, use {@link #DEFAULT_SUFFIX} as the
     * suffix.  If the <code>viewId</code> of the argument
     * <code>viewToRender</code> has a suffix, replace it with the new
     * suffix.  Otherwise append the new suffix.  Then call {@link
     * javax.faces.context.ExternalContext#dispatchMessage} on the
     * result.</p>
     *
     * <p>The default implementation of <code>ViewHandler</code>, being
     * designed for Faces pages written as JSPs, must cause the response
     * <code>Locale</code> to be set to that of the {@link UIViewRoot}
     * during the <code>doStartTag()</code> method for the
     * <code>&lt;f:view&gt;</code> tag.  The default implementation must
     * also call <code>getCharacterEncoding()</code> on the
     * <code>ServletResponse</code> and store the result in the session
     * (if one is present already) under the key with the name equal to
     * the default value of the constant {@link
     * #CHARACTER_ENCODING_KEY}.  This must happen during the
     * <code>doEndTag()</code> method for the
     * <code>&lt;f:view&gt;</code> tag.</p>
     *
     * @param context {@link FacesContext} for the current request
     *
     * @param viewToRender the view to render
     *
     * @exception IOException if an input/output error occurs
     * @exception NullPointerException if <code>context</code> or
     * <code>viewToRender</code> is <code>null</code>
     * @exception FacesException if a servlet error occurs
     */
    public void renderView(FacesContext context, UIViewRoot viewToRender)
        throws IOException, FacesException;

    /**
     * <p>Perform whatever actions are required to restore the view
     * associated with the specified {@link FacesContext} and viewId.
     * This method may call through to {@link StateManager#restoreView}.</p>
     *
     * <p>This method must be called from the <em>Restore View</em>
     * phase of the request processing lifecycle.</p>
     *
     * <p>It is the caller's responsibility to make sure the returned
     * <code>UIViewRoot</code> is stored in the
     * <code>FacesContext</code> as the new root.</p>
     *
     * <p>If this is an initial request - usually marked by a lack of
     * available state for this view - <code>restoreView()</code> must
     * call <code>FacesContext.renderResponse()</code> to cause the
     * intervening phases between <em>Restore View</em> and <em>Render
     * Response</em> to be skipped.<p>
     *
     * <p>Before returning the {@link UIViewRoot}, the default
     * implementation must attempt to set the character encoding for
     * this request.  First, the <code>Content-Type</code> header is
     * consulted.  If a character encoding can be found there, set it as
     * the request character encoding.  Otherwise, look in the session
     * (if present) under the key given by the value of {@link
     * #CHARACTER_ENCODING_KEY}.  If there is a value, this must have
     * been set by a previous run through the
     * <code>&lt;f:view&gt;</code> tag.  Set this value as the request
     * character encoding.</p>
     *
     * @param context {@link FacesContext} for the current request
     * @param viewId the view identifier for the current request
     *
     * @exception NullPointerException if <code>context</code>
     *  is <code>null</code>
     * @exception FacesException if a servlet error occurs
     */
    public UIViewRoot restoreView(FacesContext context, String viewId);

    /**
     * <p>Create an and return new {@link UIViewRoot} instance
     * initialized with information from the argument
     * <code>FacesContext</code> and <code>viewId</code>.</p>
     *
     * <p>This method must be called from {@link
     * NavigationHandler#handleNavigation}.</p>
     *
     * <p>PENDING(edburns): do we formalize that the renderkitId should
     * be set into the ViewRoot here?</p>
     *
     * @exception NullPointerException if <code>context</code>
     *  is <code>null</code>
     *
     */

    public UIViewRoot createView(FacesContext context, String viewId);

    /**
     * @return the {@link StateManager} instance for this
     * <code>ViewHandler</code>.
     */
    public StateManager getStateManager();

    /**
     * <p>Must be called once for each call to {@link
     * javax.faces.component.UIForm#encodeEnd}.  May write out the
     * actual state by immediately calling {@link
     * StateManager#writeState}, or may write a state marker specific to
     * the <code>ViewHandler</code> to be replaced later (when the
     * <code>ViewHandler</code> is ready).  A ViewHandler that writes "a
     * state marker" might not even write a state marker, but just note
     * an index into the content.</p>
     *
     * @exception NullPointerException if <code>context</code>
     *  is <code>null</code>
     */

    public void writeState(FacesContext context) throws IOException;

    /**
     * <p>Returns the context relative path of the argument
     * <code>viewId</code>, including any <code>url-pattern</code>
     * prefix or extension mapping defined by the application.</p>
     *
     * <p>The default implementation must examine the result of
     * <code>HttpServletRequest.getServletPath()</code> in conjunction
     * with the one or more </code>url-pattern</code> mappings obtained from 
     * this application's deployment descriptor to determine which mapping 
     * was used to invoke the {@link javax.faces.webapp.FacesServlet}.  
     * If the mapping is a prefix mapping, prepend the prefix mapping 
     * to the viewId, making sure to take any wildcards into account.  
     * If the mapping is an extension mapping, replace the extension 
     * in the <code>viewId</code> with the extension of the appropriate mapping.
     * If the <code>viewId</code> has no extension, append the appropriate
     * mapping.  It should be noted that if the deployment descriptor
     * contains multiple suffix mappings, the mapping used in the replacement
     * is implementation dependent. The default implementation expects the 
     * argument <code>viewId</code> to be a context relative path, starting 
     * with '<code>/</code>'.  If this is not the case, the default 
     * implementation throws <code>IllegalArgumentException</code>.</p>
     *
     * @param context the {@link FacesContext} for this request.
     *
     * @param viewId the context relative path of this viewId.
     *
     * @exception NullPointerException if <code>context</code> or
     * <code>viewId</code> is <code>null</code>.
     *
     * @exception IllegalArgumentException if <code>viewId</code> is not
     * valid for this <code>ViewHandler</code>.
     *
     */

    public String getViewIdPath(FacesContext context, String viewId);

    /** 
     * Returns a locale to use for the client. Attempts to find a
     * match based on the HTTP header Accept-language as retrieved by
     * ExternalContext.getLocales() and Application.getSupportedLocales(),
     * using the matching algorithm as described in JSTL 8.3.2. If no
     * match is found, returns Application.getDefaultLocale unless null,
     * otherwise Locale.getDefault().
     * 
     * This method is invoked by the default view handler on the first
     * incoming JSF request. On any subsequent requests, the default view
     * handler uses the locale from the incoming UIViewRoot.
     *
     * @param context {@link FacesContext} for the current request
     * 
     * @exception NullPointerException if <code>context</code> is 
     *  <code>null</code>
     */
     public Locale calculateLocale(FacesContext context);

}

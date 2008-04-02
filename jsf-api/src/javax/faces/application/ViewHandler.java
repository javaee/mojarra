/*
 * $Id: ViewHandler.java,v 1.40 2005/04/06 02:39:45 edburns Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.application;

import java.util.Locale;
import java.io.IOException;
import javax.faces.FacesException;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.component.UIForm;
import javax.faces.component.UIViewRoot;
import javax.faces.render.Renderer;
import javax.faces.render.RenderKitFactory;


/**
 * <p><strong>ViewHandler</strong> is the pluggablity mechanism for
 * allowing implementations of or applications using the JavaServer
 * Faces specification to provide their own handling of the activities
 * in the <em>Render Response</em> and <em>Restore View</em>
 * phases of the request processing lifecycle.  This allows for
 * implementations to support different response generation
 * technologies, as well as alternative strategies for saving and
 * restoring the state of each view.</p>
 *
 * <p>Please see {@link StateManager} for information on how the
 * <code>ViewHandler</code> interacts the {@link StateManager}. </p>
 */

public abstract class ViewHandler {


    // ------------------------------------------------------ Manifest Constants


    /**
     * <p>The key, in the session's attribute set, under which the
     * response character encoding may be stored and retrieved.</p>
     *
     */
    public static final String CHARACTER_ENCODING_KEY = 
	"javax.faces.request.charset";


    /**
     * <p>Allow the web application to define an alternate suffix for
     * pages containing JSF content.  If this init parameter is not
     * specified, the default value is taken from the value of the
     * constant {@link #DEFAULT_SUFFIX}.</p>
     *
     */
    public static final String DEFAULT_SUFFIX_PARAM_NAME = 
	"javax.faces.DEFAULT_SUFFIX";


    /**
     * <p>The value to use for the default extension if the webapp is using
     * url extension mapping.</p>
     */
    public static final String DEFAULT_SUFFIX = ".jsp";


    // ---------------------------------------------------------- Public Methods


    /** 
     * <p>Returns an appropriate {@link Locale} to use for this and
     * subsequent requests for the current client.</p>
     *
     * @param context {@link FacesContext} for the current request
     * 
     * @exception NullPointerException if <code>context</code> is 
     *  <code>null</code>
     */
     public abstract Locale calculateLocale(FacesContext context);


    /** 
     * <p>Return an appropriate <code>renderKitId</code> for this and
     * subsequent requests from the current client.  It is an error for
     * this method to return <code>null</code>.</p>
     *
     * <p>The default return value is {@link
     * javax.faces.render.RenderKitFactory#HTML_BASIC_RENDER_KIT}.</p>
     *
     * @param context {@link FacesContext} for the current request
     * 
     * @exception NullPointerException if <code>context</code> is 
     *  <code>null</code>
     */
    public abstract String calculateRenderKitId(FacesContext context);


    /**
     * <p>Create and return a new {@link UIViewRoot} instance
     * initialized with information from the argument
     * <code>FacesContext</code> and <code>viewId</code>.</p>
     *
     * <p>If there is an existing <code>ViewRoot</code> available on the
     * {@link FacesContext}, this method must copy its
     * <code>locale</code> and <code>renderKitId</code> to this new view
     * root.  If not, this method must call {@link #calculateLocale} and
     * {@link #calculateRenderKitId}, and store the results as the
     * values of the  <code>locale</code> and <code>renderKitId</code>,
     * proeprties, respectively, of the newly created
     * <code>UIViewRoot</code>.</p>
     *
     * @exception NullPointerException if <code>context</code>
     *  is <code>null</code>
     */
    public abstract UIViewRoot createView(FacesContext context, String viewId);


    /**
     * <p>Return a URL suitable for rendering (after optional encoding
     * performed by the <code>encodeActionURL()</code> method of
     * {@link ExternalContext}) that selects the specified view identifier.</p>
     *
     * @param context {@link FacesContext} for this request
     * @param viewId View identifier of the desired view
     *
     * @exception IllegalArgumentException if <code>viewId</code> is not
     *  valid for this <code>ViewHandler</code>.
     * @exception NullPointerException if <code>context</code> or
     *  <code>viewId</code> is <code>null</code>.
     */
    public abstract String getActionURL(FacesContext context, String viewId);


    /**
     * <p>Return a URL suitable for rendering (after optional encoding
     * perfomed by the <code>encodeResourceURL()</code> method of
     * {@link ExternalContext}) that selects the specifed web application
     * resource.  If the specified path starts with a slash, it must be
     * treated as context relative; otherwise, it must be treated as relative
     * to the action URL of the current view.</p>
     *
     * @param context {@link FacesContext} for the current request
     * @param path Resource path to convert to a URL
     *
     * @exception IllegalArgumentException if <code>viewId</code> is not
     *  valid for this <code>ViewHandler</code>.
     * @exception NullPointerException if <code>context</code> or
     *  <code>path</code> is <code>null</code>.
     */
    public abstract String getResourceURL(FacesContext context, String path);


    /**
     * <p>Perform whatever actions are required to render the response
     * view to the response object associated with the
     * current {@link FacesContext}.</p>
     *
     * @param context {@link FacesContext} for the current request
     * @param viewToRender the view to render
     *
     * @exception IOException if an input/output error occurs
     * @exception NullPointerException if <code>context</code> or
     * <code>viewToRender</code> is <code>null</code>
     * @exception FacesException if a servlet error occurs
     */
    public abstract void renderView(FacesContext context, UIViewRoot viewToRender)
        throws IOException, FacesException;


    /**
     * <p>Perform whatever actions are required to restore the view
     * associated with the specified {@link FacesContext} and
     * <code>viewId</code>.  It may delegate to the <code>restoreView</code>
     * of the associated {@link StateManager} to do the actual work of
     * restoring the view.  If there is no available state for the
     * specified <code>viewId</code>, return <code>null</code>.</p>
     *
     * @param context {@link FacesContext} for the current request
     * @param viewId the view identifier for the current request
     *
     * @exception NullPointerException if <code>context</code>
     *  is <code>null</code>
     * @exception FacesException if a servlet error occurs
     */
    public abstract UIViewRoot restoreView(FacesContext context, String viewId);


    /**
     * <p>Take any appropriate action to either immediately
     * write out the current state information (by calling
     * {@link StateManager#writeState}, or noting where state information
     * should later be written.</p>
     *
     * @param context {@link FacesContext} for the current request
     *
     * @exception IOException if an input/output error occurs
     * @exception NullPointerException if <code>context</code>
     *  is <code>null</code>
     */
    public abstract void writeState(FacesContext context) throws IOException;


}

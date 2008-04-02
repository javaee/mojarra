/*
 * $Id: ViewHandler.java,v 1.42 2005/08/22 22:07:52 ofung Exp $
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

package javax.faces.application;

import java.io.UnsupportedEncodingException;
import java.util.Locale;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.FacesException;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.component.UIViewRoot;



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

    private static Logger log = Logger.getLogger("javax.faces.application");
    

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
      * <p>Returns the correct character encoding to be used for this request.</p>
      *
      * <p>The following algorithm is employed.</p>
      *
      * <ul>
      *
      * <li><p>Examine the <code>Content-Type</code> request header.  If it has 
      * a <code>charset</code> parameter, extract it and return that as the 
      * encoding.</p></li>
      *
      * <li><p>If no <code>charset</code> parameter was found, check for the 
      * existence of a session by calling {@link ExternalContext#getSession(boolean)} 
      * passing <code>false</code> as the argument.  If that method returns 
      * <code>true</code>, get the session Map by calling 
      * {@link ExternalContext#getSessionMap} and look for a value under the 
      * key given by the value of the symbolic constant 
      * {@link ViewHandler#CHARACTER_ENCODING_KEY}.
      * If present, return the value, converted to String.</p></li>
      *
      * <li><p>Otherwise, return <code>null</code></p></li>
      *
      * </ul>
      */ 
     
     public String calculateCharacterEncoding(FacesContext context) {
         ExternalContext extContext = context.getExternalContext();
         Map<String,String> headerMap = extContext.getRequestHeaderMap();
         String contentType = headerMap.get("Content-Type");
         String charEnc = null;
         
         // look for a charset in the Content-Type header first.
         if (null != contentType) {
             // see if this header had a charset
             String charsetStr = "charset=";
             int len = charsetStr.length();
             int idx = contentType.indexOf(charsetStr);
             
             // if we have a charset in this Content-Type header AND it
             // has a non-zero length.
             if (idx != -1 && idx + len < contentType.length()) {
                 charEnc = contentType.substring(idx + len);
             }
         }
         
         // failing that, look in the session for a previously saved one
         if (null == charEnc) {
             if (null != extContext.getSession(false)) {
                 charEnc = (String) extContext.getSessionMap().get
                         (ViewHandler.CHARACTER_ENCODING_KEY);
             }
         }
         
         return charEnc;
     }


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
     *
     * <p>Initialize the view for the request processing lifecycle.</p>
     *
     * <p>This method must be called at the beginning of the <em>Restore
     * View Phase</em> of the Request Processing Lifecycle.  It is responsible 
     * for performing any per-request initialization necessary to the operation
     * of the lifycecle.</p>
     *
     * <p>The default implementation calls {@link #calculateCharacterEncoding}
     * and passes the result, if non-<code>null</code> into the
     * {@link ExternalContext#setRequestCharacterEncoding} method.
     *
     * @exception {@link FacesException} if a problem occurs setting the encoding,
     * such as the <code>UnsupportedEncodingException</code> thrown 
     * by the underlying Servlet or Portlet technology when the encoding is not
     * supported.
     *
     */
    
    public void initView(FacesContext context) throws FacesException {
        String encoding = calculateCharacterEncoding(context);
        if (null != encoding) {
            try {
                context.getExternalContext().setRequestCharacterEncoding(encoding);
            } catch (UnsupportedEncodingException e) {
                // PENDING(edburns): I18N
                String message = "Can't set encoding to: " + encoding +
                        " Exception:" + e.getMessage();
                if (log.isLoggable(Level.WARNING)) {
                    log.fine(message);
                }
                throw new FacesException(message, e);
                
            }
        }
    }
    

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

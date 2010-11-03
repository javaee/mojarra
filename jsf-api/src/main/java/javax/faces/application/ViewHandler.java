/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2010 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
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

package javax.faces.application;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ActionSource2AttachedObjectHandler;
import javax.faces.view.ActionSource2AttachedObjectTarget;
import javax.faces.view.AttachedObjectHandler;
import javax.faces.view.AttachedObjectTarget;
import javax.faces.view.EditableValueHolderAttachedObjectHandler;
import javax.faces.view.EditableValueHolderAttachedObjectTarget;
import javax.faces.view.ViewDeclarationLanguage;
import javax.faces.view.ValueHolderAttachedObjectHandler;
import javax.faces.view.ValueHolderAttachedObjectTarget;



/**
 * <p><strong><span
 * class="changed_modified_2_0 changed_modified_2_1">ViewHandler</span></strong> is the
 * pluggablity mechanism for allowing implementations of or applications
 * using the JavaServer Faces specification to provide their own
 * handling of the activities in the <em>Render Response</em> and
 * <em>Restore View</em> phases of the request processing lifecycle.
 * This allows for implementations to support different response
 * generation technologies, as well as alternative strategies for saving
 * and restoring the state of each view.  <span class="changed_added_2_0">An
 * implementation
 * of this class must be thread-safe.</span></p>
 *
 * <p>Please see {@link StateManager} for information on how the
 * <code>ViewHandler</code> interacts the {@link StateManager}. </p>

 * <p class="changed_added_2_0">Version 2 of the specification formally
 * introduced the concept of <em>View Declaration Language</em>.  A View
 * Declaration Language (VDL) is a syntax used to declare user
 * interfaces comprised of instances of JSF {@link UIComponent}s.  Any
 * of the responsibilities of the <code>ViewHandler</code> that
 * specifically deal with the VDL sub-system are now the domain of the
 * VDL implementation. These responsibilities are defined on the {@link
 * ViewDeclarationLanguage} class.  The <code>ViewHandler</code>
 * provides {@link #getViewDeclarationLanguage} as a convenience method
 * to access the VDL implementation given a <code>viewId</code>.</p>
 *
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
     * <p><span class="changed_modified_2_0">Allow</span> the web
     * application to define a <span class="changed_modified_2_0">list
     * of alternate suffixes</span> for pages containing JSF content.
     * <span class="changed_modified_2_0">This list is a space separated
     * list of values of the form
     * <i><code>.&lt;extension&gt;</code></i>.  The first physical
     * resource whose extension matches one of the configured extensions
     * will be the suffix used to create the view ID.</span> If this
     * init parameter is not specified, the default value is taken from
     * the value of the constant {@link #DEFAULT_SUFFIX}.</p>
     */
    public static final String DEFAULT_SUFFIX_PARAM_NAME = 
	"javax.faces.DEFAULT_SUFFIX";


    /**
     * <p class="changed_modified_2_1">The value to use for the default
     * extension if the webapp is using url extension mapping.</p>
     */
    public static final String DEFAULT_SUFFIX = ".xhtml .view.xml .jsp";
    
    /**
     * <p class="changed_added_2_0">Allow the web application to define an
     * alternate suffix for Facelet based XHTML pages containing JSF content.
     * If this init parameter is not specified, the default value is
     * taken from the value of the constant {@link #DEFAULT_FACELETS_SUFFIX}</p>
     * 
     * @since 2.0
     */
    
    public static final String FACELETS_SUFFIX_PARAM_NAME = 
            "javax.faces.FACELETS_SUFFIX";
    
    /**
     * <p class="changed_added_2_0">The value to use for the default extension 
     * for Facelet based XHTML pages if the webapp is using
     * url extension mapping.</p>
     * 
     * @since 2.0
     */
    public static final String DEFAULT_FACELETS_SUFFIX = ".xhtml";
    
    /**
     * <p class="changed_added_2_0">Allow the web application to define
     * a semicolon (;) separated list of strings that is used to forcibly
     * declare that certain pages in the application must be interpreted
     * as using Facelets, regardless of their extension.  Each entry in the 
     * semicolon (;) separated list of strings is either a file extension, as in 
     * <code>*.xhtml</code>, or a resource prefix (starting with '/' and 
     * interpreted as relative to the web application root), as in 
     * <code>/user/*</code>.  The latter class of entry can also take the form
     * of <code>/&lt;filename&gt;.&lt;extension&gt;*</code> such as
     * <code>/login.jsp*</code>.  The runtime must also consider the
     * <code>facelets.VIEW_MAPPINGS</code> param name as an alias to this
     * param name for backwards compatibility with existing Facelets 
     * applications.</p>
     * 
     * @since 2.0
     */
    
    public static final String FACELETS_VIEW_MAPPINGS_PARAM_NAME = 
            "javax.faces.FACELETS_VIEW_MAPPINGS";

    // ---------------------------------------------------------- Public Methods


    /** 
     * <p>Returns an appropriate {@link Locale} to use for this and
     * subsequent requests for the current client.</p>
     *
     * @param context {@link FacesContext} for the current request
     * 
     * @throws NullPointerException if <code>context</code> is 
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
      *
      * @since 1.2
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
                 charEnc = (String) extContext.getSessionMap().get(CHARACTER_ENCODING_KEY);
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
     * @throws NullPointerException if <code>context</code> is 
     *  <code>null</code>
     */
    public abstract String calculateRenderKitId(FacesContext context);


    /**
     * <p><strong class="changed_modified_2_0">Create</strong> and
     * return a new {@link UIViewRoot} instance initialized with
     * information from the argument <code>FacesContext</code> and
     * <code>viewId</code>.  <span class="changed_modified_2_0">Locate
     * the {@link ViewDeclarationLanguage} implementation for the VDL
     * used in the view.  The argument <code>viewId</code> must be
     * converted to a physical <code>viewId</code> that can refer to an
     * actual resource suitable for use by the
     * <code>ViewDeclarationLanguage</code> {@link
     * ViewDeclarationLanguage#createView}, which must be called by
     * this method.</span>

     * @throws NullPointerException if <code>context</code>
     *  is <code>null</code>
     */
    public abstract UIViewRoot createView(FacesContext context, String viewId);

    /**
     * <p class="changed_added_2_0">Derive and return the viewId from
     * the current request, or the argument input by following the
     * algorithm defined in specification section JSF.7.5.2.</p>
     *
     * <p>The default implementation of this method simply returns
     * rawViewId unchanged.</p>
     *
     * @param context the <code>FacesContext</code> for this request
     *
     * @param rawViewId the <code>viewId</code> to derive,
     *
     * @since 2.0
     */
    public String deriveViewId(FacesContext context, String rawViewId) {

        return rawViewId;

    }

    /**
     * <p class="changed_added_2_1">Derive and return the viewId from
     * the current request, or the argument input by following the
     * algorithm defined in specification section JSF.7.5.2.  Note that
     * unlike <code>deriveViewId()</code>, this method does not require that
     * a physical view be present.</p>
     *
     * <p>The default implementation of this method simply returns
     * rawViewId unchanged.</p>
     *
     * @param context the <code>FacesContext</code> for this request
     *
     * @param rawViewId the <code>viewId</code> to derive,
     *
     * @since 2.1
     */
    public String deriveLogicalViewId(FacesContext context, String rawViewId) {

        return rawViewId;

    }
    

    /**
     * <p class="changed_modified_2_0">If the value returned from this
     * method is used as the <code>file</code> argument to the
     * four-argument constructor for <code>java.net.URL</code> (assuming
     * appropriate values are used for the first three arguments), then
     * a client making a request to the <code>toExternalForm()</code> of
     * that <code>URL</code> will select the argument
     * <code>viewId</code> for traversing the JSF lifecycle.  Please see
     * section JSF.7.5.2 for the complete specification.</p>
     *
     * @param context {@link FacesContext} for this request
     * @param viewId View identifier of the desired view
     *
     * @throws IllegalArgumentException if <code>viewId</code> is not
     * valid for this <code>ViewHandler</code>, or does not start with
     * "/".
     * @throws NullPointerException if <code>context</code> or
     *  <code>viewId</code> is <code>null</code>.
     */
    public abstract String getActionURL(FacesContext context, String viewId);


    /**
     * <p class="changed_modified_2_0">If the value returned from this
     * method is used as the <code>file</code> argument to the
     * four-argument constructor for <code>java.net.URL</code> (assuming
     * appropriate values are used for the first three arguments), then
     * a client making a request to the <code>toExternalForm()</code> of
     * that <code>URL</code> will select the argument <code>path</code>
     * for direct rendering.  If the specified path starts with a slash,
     * it must be treated as context relative; otherwise, it must be
     * treated as relative to the action URL of the current view.</p>
     *
     * @param context {@link FacesContext} for the current request
     * @param path Resource path to convert to a URL
     *
     * @throws IllegalArgumentException if <code>viewId</code> is not
     *  valid for this <code>ViewHandler</code>.
     * @throws NullPointerException if <code>context</code> or
     *  <code>path</code> is <code>null</code>.
     */
    public abstract String getResourceURL(FacesContext context, String path);


    /**
     * <p class="changed_added_2_0"> Return a JSF action URL derived
     * from the <code>viewId</code> argument that is suitable to be used
     * by the {@link NavigationHandler} to issue a redirect request to
     * the URL using a NonFaces request.  Compiliant implementations
     * must implement this method as specified in section JSF.7.5.2.
     * The default implementation simply calls through to {@link
     * #getActionURL}, passing the arguments <code>context</code> and
     * <code>viewId</code>.</p>
     *
     * @param context           The FacesContext processing this request
     * @param viewId            The view identifier of the target page
     * @param parameters        A mapping of parameter names to one or more values
     * @param includeViewParams A flag indicating whether view parameters should be encoded into this URL
     * @since 2.0
     */
    public String getRedirectURL(FacesContext context,
                                 String viewId,
                                 Map<String,List<String>>parameters,
                                 boolean includeViewParams) {
        
        return getActionURL(context, viewId);

    }


    /**

     * <p class="changed_added_2_0"> Return a JSF action URL derived
     * from the viewId argument that is suitable to be used as the
     * target of a link in a JSF response. Compiliant implementations
     * must implement this method as specified in section JSF.7.5.2.
     * The default implementation simply calls through to {@link
     * #getActionURL}, passing the arguments <code>context</code> and
     * <code>viewId</code>.</p>
     *
     * @param context           The FacesContext processing this request
     * @param viewId            The view identifier of the target page
     * @param parameters        A mapping of parameter names to one or more values
     * @param includeViewParams A flag indicating whether view parameters should be encoded into this URL
     *
     * @since 2.0
     */
    public String getBookmarkableURL(FacesContext context,
                                     String viewId,
                                     Map<String,List<String>> parameters,
                                     boolean includeViewParams) {

        return getActionURL(context, viewId);

    }


    /**
     * <p class="changed_added_2_0"><span class="changed_modified_2_1">Return</span>
     * the {@link ViewDeclarationLanguage} instance used for this <code>ViewHandler</code>
     * instance.</p>
     * 
     * <div class="changed_added_2_0">
     * 
     * <p>The default implementation must use {@link
     * javax.faces.view.ViewDeclarationLanguageFactory#getViewDeclarationLanguage}
     * to obtain the appropriate <code>ViewDeclarationLanguage</code>
     * implementation for the argument <code>viewId</code>.  Any
     * exceptions thrown as a result of invoking that method must not be
     * swallowed.</p>
     * 
     * <p>The default implementation of this method returns null.</p>
     * 
     * </div>
     *
     * @param context the <code>FacesContext</code> for this request.
     *
     * @param viewId <span class="changed_modified_2_1">the logical view
     * id, as returned from {@link #deriveLogicalViewId} for which the
     * <code>ViewDeclarationLanguage</code> should be returned.</span>

     * @since 2.0
     */
    public ViewDeclarationLanguage getViewDeclarationLanguage(FacesContext context,
                                                              String viewId) {

        return null;

    }

    
    /**
     *
     * <p><span class="changed_modified_2_0">Initialize</span> the view
     * for the request processing lifecycle.</p>
     *
     * <p>This method must be called at the beginning of the <em>Restore
     * View Phase</em> of the Request Processing Lifecycle.  It is responsible 
     * for performing any per-request initialization necessary to the operation
     * of the lifycecle.</p>
     *
     * <p class="changed_modified_2_0">The default implementation must
     * perform the following actions.  If {@link
     * ExternalContext#getRequestCharacterEncoding} returns
     * <code>null</code>, call {@link #calculateCharacterEncoding} and
     * pass the result, if non-<code>null</code>, into the {@link
     * ExternalContext#setRequestCharacterEncoding} method.  If {@link
     * ExternalContext#getRequestCharacterEncoding} returns
     * non-<code>null</code> take no action.</p>

     * @throws FacesException if a problem occurs setting the encoding,
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
     * <p><span class="changed_modified_2_0">Perform</span> whatever
     * actions are required to render the response view to the response
     * object associated with the current {@link FacesContext}.</p>

     * <p class="changed_added_2_0">Otherwise, the default
     * implementation must obtain a reference to the {@link
     * ViewDeclarationLanguage} for the <code>viewId</code> of the
     * argument <code>viewToRender</code> and call its {@link
     * ViewDeclarationLanguage#renderView} method, returning the result
     * and not swallowing any exceptions thrown by that method.</p>
     *
     * @param context {@link FacesContext} for the current request
     * @param viewToRender the view to render
     *
     * @throws IOException if an input/output error occurs
     * @throws NullPointerException if <code>context</code> or
     * <code>viewToRender</code> is <code>null</code>
     * @throws FacesException if a servlet error occurs
     */
    public abstract void renderView(FacesContext context, UIViewRoot viewToRender)
        throws IOException, FacesException;


    /**
     * <p><span class="changed_modified_2_0">Perform</span> whatever
     * actions are required to restore the view associated with the
     * specified {@link FacesContext} and <code>viewId</code>.  It may
     * delegate to the <code>restoreView</code> of the associated {@link
     * StateManager} to do the actual work of restoring the view.  If
     * there is no available state for the specified
     * <code>viewId</code>, return <code>null</code>.</p>

     * <p class="changed_added_2_0">Otherwise, the default implementation
     * must obtain a reference to the {@link ViewDeclarationLanguage}
     * for this <code>viewId</code> and call its {@link
     * ViewDeclarationLanguage#restoreView} method, returning the result
     * and not swallowing any exceptions thrown by that method.</p>

     * @param context {@link FacesContext} for the current request
     * @param viewId the view identifier for the current request
     *
     * @throws NullPointerException if <code>context</code>
     *  is <code>null</code>
     * @throws FacesException if a servlet error occurs
     */
    public abstract UIViewRoot restoreView(FacesContext context, String viewId);

    
    /**
     * <p>Take any appropriate action to either immediately
     * write out the current state information (by calling
     * {@link StateManager#writeState}, or noting where state information
     * should later be written.</p>
     *
     * <p class="changed_added_2_0">This method must do nothing if the current
     * request is an <code>Ajax</code> request.  When responding to 
     * <code>Ajax</code> requests, the state is obtained by calling
     * {@link StateManager#getViewState}
     * and then written into the <code>Ajax</code> response during final
     * encoding 
     * ({@link javax.faces.component.UIViewRoot#encodeEnd}. 
     * </p>
     *
     * @param context {@link FacesContext} for the current request
     *
     * @throws IOException if an input/output error occurs
     * @throws NullPointerException if <code>context</code>
     *  is <code>null</code>
     */
    public abstract void writeState(FacesContext context) throws IOException;


}

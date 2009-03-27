/*
 * $Id: ViewHandler.java,v 1.46.4.1 2008/03/14 02:40:57 edburns Exp $
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
 * class="changed_modified_2_0">ViewHandler</span></strong> is the
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
     * <p>The value to use for the default extension if the webapp is using
     * url extension mapping.</p>
     */
    public static final String DEFAULT_SUFFIX = ".xhtml .jsp";
    
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
     * <code>viewId</code>.</p>
     *
     * <p>If there is an existing <code>ViewRoot</code> available on the
     * {@link FacesContext}, this method must copy its
     * <code>locale</code> and <code>renderKitId</code> to this new view
     * root.  If not, this method must call {@link #calculateLocale} and
     * {@link #calculateRenderKitId}, and store the results as the
     * values of the  <code>locale</code> and <code>renderKitId</code>,
     * proeprties, respectively, of the newly created
     * <code>UIViewRoot</code>.</p>

     * <p class="changed_added_2_0">If the view is written using
     * Facelets, the markup comprising the view must be executed, with
     * the UIComponent instances in the view being encountered in the
     * same depth-first order as in other lifecycle methods defined on
     * {@link javax.faces.component.UIComponent}, and added to the view
     * (but not rendered) during the traversal.  The runtime must
     * guarantee that the view must be fully populated before the
     * <code>afterPhase()</code> method of any {@link
     * javax.faces.event.PhaseListener}s attached to the application or
     * to the <code>UIViewRoot</code> (via {@link
     * UIViewRoot#setAfterPhaseListener} or {@link
     * UIViewRoot#addPhaseListener}) are called.  IMPORTANT: the new
     * <code>UIViewRoot</code> instance must be passed to {@link
     * javax.faces.context.FacesContext#setViewRoot}
     * <strong>before</strong> the execution of the Facelets view
     * resulting in tree creation.  This enables the broadest possible
     * range of implementations for how tree creation is actually
     * implemented.</p>
     *
     * @throws NullPointerException if <code>context</code>
     *  is <code>null</code>
     */
    public abstract UIViewRoot createView(FacesContext context, String viewId);

    /**
     * <p class="changed_added_2_0">Derive and return the viewId from
     * the current request, or the argument input by following the
     * @since 2.0
     * algorithm defined in specification section JSF.7.5.2.</p>
     *
     * @param context the <code>FacesContext</code> for this request

     * @param input the input candidate <code>viewId</code> to derive,
     * or <code>null</code> to use the information in the current
     * request to derive the <code>viewId</code>.
     *
     */
    public String deriveViewId(FacesContext context, String input) {
        throw new UnsupportedOperationException("The default implementation must override this method");
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
     * <p class="changed_added_2_0">
     * Return a JSF action URL derived from the <code>viewId</code> argument that
     * is suitable to be used by the {@link NavigationHandler} to issue a redirect request to the URL using a
     * NonFaces request. The requirements for how to build the JSF action URL are described below.
     * </p>
     *
     * <p>
     * To build the JSF action URL the value of the viewId argument should be
     * fed through the {@link #getActionURL(FacesContext, String)} method to produce a URL; 
     * this URL may include parameters which are passed untouched to {@link ExternalContext#encodeRedirectURL}
     * as part of the baseURL. The additional parameters, represented as a <code>Map</code> of
     * parameter names to one or more values, are collected as follows. If the <code>includeViewParams</code> 
     * argument is <code>true</code>, the view parameters (i.e., {@link javax.faces.component.UIViewParameter} 
     * components) are read from the target page. The value of the each page parameter is retrieved by calling
     * {@link javax.faces.component.UIViewParameter#getStringValue(FacesContext)} and forming a parameter. 
     * The view parameters, if included, are merged with the <code>parameters</code> argument.  When a parameter 
     * is contributed by more than one of the previously mentioned source, the parameter with the highest 
     * precendence is used, replacing all parameters with the same name from the lower precendence source. The
     * order of precendence for parameters, from lowest to highest, is view parameters, the
     * parameters argument (parameter overrides).
     * The parameters are encoded into the query string of the URL by delegating to the method
     * {@link ExternalContext#encodeRedirectURL(String, java.util.Map)} .  Finally, the result
     * is encoded by calling {@link ExternalContext#encodeActionURL(String)}.
     * </p>
     *
     * <p>
     *  The default implementation returns the result of {@link #getActionURL(javax.faces.context.FacesContext, String)}.
     * </p>
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
     * <p class="changed_added_2_0">
     * Return a JSF action URL derived from the viewId argument that
     * is suitable to be used as the target of a link in a JSF response. The URL if activated,
     * would issue a NonFaces request to the viewId mapped in the action URL. The intent is that
     * this URL can serve as a bookmark for that JSF view. The requirements for how to build the
     * JSF action URL are described below.
     * </p>
     *
     * <p>
     * To build the JSF action URL the value of the viewId argument without the query string should be
     * fed through the ViewHandler#getActionURL(FacesContext, String) method method to produce a URL; this URL may include parameters which are passed untouched to encodeBookmarkableUrl as part of the baseURL. The additional parameters, represented as
     * Parameter objects, are collected as follows. If the includeViewParams argument is true,
     * the view parameters (i.e., UIViewParameter components) are read from the target page. The
     * value of the each page parameter is retrieved by calling
     * UIViewParameter#getStringValue(FacesContext) and forming a parameter. The view
     * parameters, if included, are merged the <code>parameters</code> argument.  When a parameter is contributed by more
     * than one of the previously mentioned source, the parameter with the highest precendence is
     * used, replacing all parameters with the same name from the lower precendence source. The
     * order of precendence for parameters, from lowest to highest, is view parameters, the
     * parameters argument (parameter overrides).
     * The parameters are encoded into the query string of the URL by delegating to the method
     * {@link ExternalContext#encodeBookmarkableURL(String, java.util.Map)}.  Finally, the result
     * is encoded by calling ExternalContext#encodeActionURL(String).
     * </p>
     *
     * <p>
     * The default implementation returns the result of {@link ViewHandler#getActionURL(javax.faces.context.FacesContext, String)}.
     * </p>
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
     * <p class="changed_added_2_0">Return the {@link
     * ViewDeclarationLanguage} instance used for this <code>ViewHandler</code>
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
     * <p>An implementation is provided that will throw
     * <code>UnsupportedOperationException</code>.  A Faces implementation
     * compliant with version 2.0 and beyond of the specification must 
     * override this method.</p>
     * 
     * </div>

     * @since 2.0
     */
    public ViewDeclarationLanguage getViewDeclarationLanguage(FacesContext context,
                                                              String viewId) {

        throw new UnsupportedOperationException();

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
     * <p class="changed_added_2_0">Assuming the component metadata for
     * argument <code>topLevelComponent</code> has been made available
     * by an earlier call to {@link
     * ViewDeclarationLanguage#getComponentMetadata}, leverage the
     * component metadata for the purpose of re-targeting attached
     * objects from the top level composite component to the individual
     * {@link AttachedObjectTarget} instances inside the composite
     * component.  This method must be called by the {@link
     * ViewDeclarationLanguage} implementation when creating the
     * <code>UIComponent</code> tree when a composite component usage is
     * encountered.</p>
     *
     * <div class="changed_added_2_0">
     *
     * <p>An algorithm semantically equivalent to the following must be
     * implemented.</p>
     *
     *<ul>
     *
     *<li><p>Obtain the metadata for the composite component.
     *Currently this entails getting the value of the {@link
     *UIComponent#BEANINFO_KEY} component attribute, which will be
     *an instance of <code>BeanInfo</code>.  If the metadata cannot
     *be found, log an error message and return.</p></li>
     *
     *<li><p>Get the <code>BeanDescriptor</code> from the
     *<code>BeanInfo</code>.</p></li>
     *
     *<li><p>Get the value of the {@link
     *AttachedObjectTarget#ATTACHED_OBJECT_TARGETS_KEY} from the
     *<code>BeanDescriptor</code>'s <code>getValue()</code> method.
     *This will be a <code>List&lt;{@link
     *AttachedObjectTarget}&gt;</code>.  Let this be
     *<em>targetList</em>.</p></li>
     *
     *<li><p>For each <em>curHandler</em> entry in the argument
     *<code>handlers</code></p>
     *
     *<ul>
     *
     *<li><p>Let <em>forAttributeValue</em> be the return from
     *{@link AttachedObjectHandler#getFor}.  </p></li>
     *
     *<li><p>For each <em>curTarget</em> entry in
     *<em>targetList</em>, the first of the following items that
     *causes a match will take this action:</p>
     *
     *<p style="margin-left: 3em;">For each <code>UIComponent</code> in the
     *list returned from <em>curTarget.getTargets()</em>, call
     *<em>curHandler.<a
     *href="AttachedObjectHandler.html#applyAttachedObject">applyAttachedObject()</a></em>,
     *passing the <code>FacesContext</code> and the
     *<code>UIComponent</code>.</p>
     *
     *<p>and cause this inner loop to terminate.</p>
     *
     *<ul>
     *
     *<li><p>If <em>curHandler</em> is an instance of {@link
     *ActionSource2AttachedObjectHandler} and <em>curTarget</em> is
     *an instance of {@link ActionSource2AttachedObjectTarget},
     *consider it a match.</p></li>
     *
     *<li><p>If <em>curHandler</em> is an instance of {@link
     *EditableValueHolderAttachedObjectHandler} and <em>curTarget</em> is
     *an instance of {@link EditableValueHolderAttachedObjectTarget},
     *consider it a match.</p></li>
     *
     *<li><p>If <em>curHandler</em> is an instance of {@link
     *ValueHolderAttachedObjectHandler} and <em>curTarget</em> is
     *an instance of {@link ValueHolderAttachedObjectTarget},
     *consider it a match.</p></li>
     *
     *</ul>
     *</li>
     *</ul>
     *</li>
     *</ul>
     *
     * <p>An implementation is provided that will throw
     * <code>UnsupportedOperationException</code>.  A Faces implementation
     * compliant with version 2.0 and beyond of the specification must 
     * override this method.</p>
     *
     * </div>
     *
     * @param context the FacesContext for this request.
     *
     * @param topLevelComponent The UIComponent in the view to which the
     * attached objects must be attached.  This UIComponent must have
     * its component metadata already associated and available from via
     * the JavaBeans API.
     *
     * @throws NullPointerException if any of the arguments are
     * <code>null</code>.

     * @since 2.0
     *
     */
    public void retargetAttachedObjects(FacesContext context,
                                        UIComponent topLevelComponent,
                                        List<AttachedObjectHandler> handlers)  {
        
        // no-op

    }


    /**
     * <p class="changed_added_2_0">Assuming the component metadata for
     * argument <code>topLevelComponent</code> has been made available
     * by an earlier call to {@link
     * ViewDeclarationLanguage#getComponentMetadata}, leverage the
     * component metadata for the purpose of re-targeting any method
     * expressions from the top level component to the appropriate inner
     * component.  For each attribute that is a
     * <code>MethodExpression</code> (as indicated by the presence of a
     * "<code>method-signature</code>" attribute and the absence of a
     * "<code>type</code>" attribute), the following action must be
     * taken:</p>

     * <div class="changed_added_2_0">
     *
     * <ul>
     *
     * <li><p>Get the value of the <em>targets</em> attribute.  If the
     * value is a <code>ValueExpression</code> evaluate it.  If there is
     * no <em>targets</em> attribute, let the name of the metadata
     * element be the evaluated value of the <em>targets
     * attribute.</em></p></li>
     * 
     * <li><p>Interpret <em>targets</em> as a space (not tab) separated
     * list of ids. For each entry in the list:</p>
     * 
     * <ul>
     *
     * <li><p>Find the inner component of the
     * <em>topLevelComponent</em> with the id equal to
     * the current list entry.  For discussion, this component is called
     * <em>target</em>.  If not found, log and error and continue to
     * the next attribute.</p></li>
     *
     * <li><p>For discussion the declared name of the attribute is
     * called <em>name</em>.</p></li>
     *
     * <li><p>In the attributes map of the
     * <em>topLevelComponent</em>, look up the entry under the key
     * <em>name</em>.  Assume the result is a
     * <code>ValueExpression</code>.  For discussion, this is
     * <em>attributeValueExpression</em>.  If not found, log an error
     * and continue to the next attribute.</p></li>
     *
     * <li><p>If <em>name</em> is equal to the string "action", or
     * "actionListener" without the quotes, assume <em>target</em> is
     * an {@link javax.faces.component.ActionSource2}.</p></li>
     *
     * <li><p>If <em>name</em> is equal to the string "validator", or
     * "valueChangeListener" without the quotes, assume
     * <em>target</em> is an {@link
     * javax.faces.component.EditableValueHolder}.</p></li>
     *
     * <li><p>Call <code>getExpressionString()</code> on the
     * <em>attributeValueExpression</em> and use that string to
     * create a <code>MethodExpression</code> of the appropriate
     * signature for <em>name</em>.</p></li>
     *
     * <li><p>If <em>name</em> is not equal to any of the previously
     * listed strings, call <code>getExpressionString()</code> on the
     * <em>attributeValueExpression</em> and use that string to
     * create a <code>MethodExpression</code> where the signature is
     * created based on the value of the
     * "<code>method-signature</code>" attribute of the
     * <code>&lt;composite:attribute /&gt;</code> tag.</p></li>
     *
     * <li><p>Let the resultant <code>MethodExpression</code> be
     * called <em>attributeMethodExpression</em> for discussion.
     * </p></li>
     *
     * <li><p>If <em>name</em> is equal to the string "action"
     * without the quotes, call {@link
     * javax.faces.component.ActionSource2#setActionExpression} on
     * <em>target</em>, passing <em>attributeMethodExpression</em>.</p></li>
     *
     * <li><p>If <em>name</em> is equal to the string
     * "actionListener" without the quotes, call {@link
     * javax.faces.component.ActionSource#addActionListener} on
     * <em>target</em>, passing <em>attributeMethodExpression</em>
     * wrapped in a {@link javax.faces.event.MethodExpressionActionListener}.</p></li>
     *
     * <li><p>If <em>name</em> is equal to the string
     * "validator" without the quotes, call {@link
     * javax.faces.component.EditableValueHolder#addValidator} on <em>target</em>,
     * passing <em>attributeMethodExpression</em> wrapped in a {@link
     * javax.faces.validator.MethodExpressionValidator}.</p></li>
     *
     * <li><p>If <em>name</em> is equal to the string
     * "valueChangeListener" without the quotes, call {@link
     * javax.faces.component.EditableValueHolder#addValueChangeListener} on
     * <em>target</em>, passing <em>attributeMethodExpression</em> wrapped in a
     * {@link javax.faces.event.MethodExpressionValueChangeListener}.</p></li>
     *
     * <li><p>Otherwise, assume that the <code>MethodExpression</code>
     * should be placed in the components attribute set.  The runtme
     * must create the <code>MethodExpression</code> instance based on
     * the value of the "<code>method-signature</code>"
     * attribute.</p></li>

     * </ul>
     * 
     * </li>
     * 
     * </ul>
     *
     * <p>An implementation is provided that will throw
     * <code>UnsupportedOperationException</code>.  A Faces implementation
     * compliant with version 2.0 and beyond of the specification must 
     * override this method.</p>
     *
     * </div>
     *
     * @param context the FacesContext for this request.
     *
     * @param topLevelComponent The UIComponent in the view to which the
     * attached objects must be attached.  This UIComponent must have
     * its component metadata already associated and available from via
     * the JavaBeans API.
     *
     * @throws NullPointerException if <code>context</code>
     * or <code>topLevelComponent</code> is <code>null</code>.
     *
     * @since 2.0
     */
    public void retargetMethodExpressions(FacesContext context,
                                          UIComponent topLevelComponent) {

        // no-op
        
    }
    
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

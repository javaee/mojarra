/*
 * $Id: ExternalContext.java,v 1.3 2003/03/22 01:09:33 eburns Exp $
 */
 
/*
 * Licensed Material - Property of IBM 
 * (C) Copyright IBM Corp. 2002, 2003 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure 
 * restricted by GSA ADP Schedule Contract with IBM Corp. 
 */

package javax.faces.context;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;
import java.util.Map;

import javax.faces.FacesException;
import javax.servlet.http.Cookie;


/**
   
* <p>This class allows the Faces API to be unaware of the nature of its
* containing environment.  For example, this class allows Faces to run
* in either a Servlet or a Portlet.  This class can be seen as an
* extension of FacesContext.

* @author Brendan Murray
* @author Ed Burns <ed.burns@sun.com>
* @version 0.1
* 
*/
public abstract class ExternalContext {
    
    /**
     * @return the <code>HttpSession</code> instance for the session
     * associated with the current request (if any); otherwise, return
     * <code>null</code>.
     
     * @param create true - to create a new session for this request if
     * necessary; false to return null if there's no current session
     
    */
    public abstract Object getSession(boolean create);
    
    /**
     * @return the <code>ServletContext</code> object for the web
     * application associated with this request.
     */
    public abstract Object getContext();
    
    
    /**
     * @return the <code>ServletRequest</code> object representing the
     * current request that is being processed.
     */
    public abstract Object getRequest();
    
    
    /**
     * @return the <code>ServletResponse</code> object representing the
     * current response that is being rendered.
     */
    public abstract Object getResponse();
    
    /**
       
    * <p>Calling <code>get(key)</code> on the <code>Map</code> obtained
    * from this method is equivalent to getting the
    * <code>ServletContext</code> for this request and calling
    * <code>getAttribute(key)</code> on it.  Similar rules apply for
    * other <code>Map</code> methods where they make sense.</p>

    * @return a <code>Map</code> that wraps the ServletContext's
    * attribute set.
    
    */
    public abstract Map getApplicationMap();
    
    /**
     
     * <p>Calling <code>get(key)</code> on the <code>Map</code> obtained
     * from this method is equivalent to getting the
     * <code>HttpSession</code> for this request and calling
     * <code>getAttribute(key)</code> on it.  Similar rules apply for *
     * other <code>Map</code> methods where they make sense.</p>
     
    * @return a <code>Map</code> that wraps the HttpSession's
    * attribute set.
    
    */
    public abstract Map getSessionMap();
    
    /**
     
     * <p>Calling <code>get(key)</code> on the <code>Map</code> obtained
     * from this method is equivalent to getting the
     * <code>ServletRequest</code> for this request and calling
     * <code>getAttribute(key)</code> on it.  Similar rules apply for *
     * other <code>Map</code> methods where they make sense.</p>

    * @return a <code>Map</code> that wraps the Request's
    * attribute set.
     
     */
    public abstract Map getRequestMap();
    
    /**
       
    * <p>Calling <code>get(key)</code> on the <code>Map</code> obtained
    * from this method is equivalent to getting the
    * <code>ServletRequest</code> for this request and calling
    * <code>getParameter(key)</code> on it.  Similar rules apply for
    * other <code>Map</code> methods where they make sense.</p>
    
    * @return a <code>Map</code> of all the parameters sent with this request.
    
    */
    
    public abstract Map getRequestParameterMap();
    
    /**
       
    * <p>Similar to <code>getRequestParameterMap</code>, but useful for those
    * parameters that can have multiple values with one key.</p>
    
    * <p>Calling <code>get(key)</code> on the <code>Map</code> obtained
    * from this method is equivalent to getting the
    * <code>ServletRequest</code> for this request and calling
    * <code>getParameterValues(key)</code> on it.  Similar rules apply
    * for other <code>Map</code> methods where they make sense.</p>

    * @return a <code>Map</code> that wraps the getParameterValues()
    * result set.
    
    */
    
    public abstract Map getRequestParameterValuesMap();

    /**

    * This is simply a wrapper for
    * <code>ServletRequest.getParameterNames()</code>.  We took the
    * liberty of returning an <code>Iterator</code> instead of an
    * <code>Enumeration</code>.

    * @return an Iterator of the names of the parameters for this request.

    */
    
    public abstract Iterator getRequestParameterNames();

    /**

    * <p>Calling <code>get(key)</code> on the <code>Map</code> obtained
    * from this method is equivalent to getting the
    * <code>ServletRequest</code> for this request and calling
    * <code>getHeader(key)</code> on it.  Similar rules apply for
    * other Map methods where they make sense.</p>
    
    * @return a <code>Map</code> that wraps the Header set for this
    * request.


    */ 
    
    public abstract Map getRequestHeaderMap();

    /**

    * <p>Calling <code>get(key)</code> on the <code>Map</code> obtained
    * from this method is equivalent to getting the
    * <code>ServletRequest</code> for this request and calling
    * <code>getHeader<em>s</em>(key)</code> on it.  Similar rules apply for
    * other Map methods where they make sense.</p>
    
    * @return a <code>Map</code> that wraps the Header set for this
    * request, but calls <code>getHeader<em>s</em>()</code> instead of
    * <code>getHeader()</code>.

    */ 
    
    public abstract Map getRequestHeaderValuesMap();

    /**

    * <p>Calling <code>get(key)</code> on the <code>Map</code> obtained
    * from this method is equivalent to getting the
    * <code>ServletRequest</code> for this request and looking through
    * the <code>Cookie[]</code> returned by calling
    * <code>getCookies()</code> and finding a cookie with the name of
    * <code>key</code>.  Similar rules apply for other Map methods where
    * they make sense.</p>
    
    * @return a <code>Map</code> that wraps the Cookie set for this
    * request.

    */ 

    public abstract Map getRequestCookieMap();

    /**

    * <p>A wrapper for <code>ServletRequest.getLocale()</code>.</p>

    */
    
    public abstract Locale getRequestLocale();
    
    /**

    * <p>A wrapper for <code>ServletRequest.getPathInfo()</code>.</p>

    */
    
    public abstract String getRequestPathInfo();
    
    /**

    * <p>A wrapper for <code>HttpServletRequest.getContextPath()</code>.</p>

    */
    
    public abstract String getRequestContextPath();

    /**

    * <p>A wrapper for <code>HttpServletRequest.getCookies()</code>.</p>

    */
    
    public abstract Cookie[] getRequestCookies();

    /**

    * <p>A wrapper for <code>ServletContext.getInitParameter()</code>.</p>

    */
    
    public abstract String getInitParameter(String name);

    /**
     
     * <p>Calling <code>get(key)</code> on the <code>Map</code> obtained
     * from this method is equivalent to getting the
     * <code>ServletContext</code> for this request and calling
     * <code>getInitParam(key)</code> on it.  Similar rules apply for
     * other <code>Map</code> methods where they make sense.</p>
     
    * @return a <code>Map</code> that wraps the ServletContext's
    * init parameter set.
    
    */

    public abstract Map getInitParameterMap();

    /**

    * <p>A wrapper for <code>ServletContext.getResourcePaths()</code>.</p>

    */
    
    public abstract Set getResourcePaths(String path);

    /**

    * <p>A wrapper for <code>ServletContext.getResourceAsStream()</code>.</p>

    */
    
    public abstract InputStream getResourceAsStream(String path);
    
    /**
     * <p>Force any URL that causes an action to work within a portal/portlet. 
     * This causes the URL to have the required redirection for the specific
     * portal to be included</p>
     *
     * @param sb The input URL to be reformatted
     */
    public abstract String encodeActionURL(String sb);
    
    /**
     * <p>Force any URL that references a resource to work within a
     * portal/portlet. This causes the URL to have the required
     * redirection for the specific portal to be included. In reality,
     * it simply returns an absolute URL.</p>
     *
     * @param sb The input URL to be reformatted
     */

    public abstract String encodeResourceURL(String sb);

    /**

    * PENDING(edburns): this does nothing for Servlets.  What does it to
    * for Portlets?

    */ 

    public abstract String encodeNamespace(String aValue);

    /**

    * <p>A wrapper for <code>ServletResponse.encodeURL()</code>.</p>

    */
    
    public abstract String encodeURL(String url);

    /**
      * <p>Dispatch a request to the apropriate context. In the
      * case of servlets, this is done via "forward", but for
      * portlets, it must use "include".</p>
      *
      * @param requestURI The input URI of the request tree.
      */

    public abstract void dispatchMessage(String requestURI) throws IOException, FacesException;
    
}

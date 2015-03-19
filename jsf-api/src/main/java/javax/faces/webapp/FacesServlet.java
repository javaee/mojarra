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

package javax.faces.webapp;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Set;
import java.util.HashSet;
import java.util.List;

import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.application.ResourceHandler;
import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextFactory;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.LifecycleFactory;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.UnavailableException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * <p><strong class="changed_modified_2_0 changed_modified_2_0_rev_a
 * changed_modified_2_1 changed_modified_2_2 changed_modified_2_2_mr1">FacesServlet</strong> is a
 * servlet that manages the request processing lifecycle for web
 * applications that are utilizing JavaServer Faces to construct the
 * user interface.</p>
 *
 * <div class="changed_added_2_1">
 *
 * <p>If the application is running in a Servlet 3.0 (and beyond)
 * container, the runtime must provide an implementation of the {@link
 * javax.servlet.ServletContainerInitializer} interface that declares
 * the following classes in its {@link
 * javax.servlet.annotation.HandlesTypes} annotation.</p>

 * <ul>
 
 * <li>{@link javax.faces.application.ResourceDependencies}</li>

 * <li>{@link javax.faces.application.ResourceDependency}</li>

 * <li>javax.faces.bean.ManagedBean</li>

 * <li>{@link javax.faces.component.FacesComponent}</li>

 * <li>{@link javax.faces.component.UIComponent}</li>

 * <li>{@link javax.faces.convert.Converter}</li>

 * <li>{@link javax.faces.convert.FacesConverter}</li>

 * <li>{@link javax.faces.event.ListenerFor}</li>

 * <li>{@link javax.faces.event.ListenersFor}</li>

 * <li>{@link javax.faces.render.FacesBehaviorRenderer}</li>

 * <li>{@link javax.faces.render.Renderer}</li>

 * <li>{@link javax.faces.validator.FacesValidator}</li>

 * <li>{@link javax.faces.validator.Validator}</li>

 * </ul>

 * <p>This servlet must automatically be mapped if it is
 * <strong>not</strong> explicitly mapped in <code>web.xml</code> or
 * <code>web-fragment.xml</code> and one or more of the following
 * conditions are true.</p>

 * <ul>

 * 	  <li><p>A <code>faces-config.xml</code> file is found in
 * 	  <code>WEB-INF</code> </p></li>

 * 	  <li><p>A <code>faces-config.xml</code> file is found in the
 * 	  <code>META-INF</code> directory of a jar in the application's
 * 	  classpath.</p></li>

 * 	  <li><p>A filename ending in <code>.faces-config.xml</code> is
 * 	  found in the <code>META-INF</code> directory of a jar in the
 * 	  application's classpath.</p></li>

 * 	  <li><p>The <code>javax.faces.CONFIG_FILES</code> context param
 * 	  is declared in <code>web.xml</code> or
 * 	  <code>web-fragment.xml</code>.</p></li>

 *        <li><p>The <code>Set</code> of classes passed to the
 *        <code>onStartup()</code> method of the
 *        <code>ServletContainerInitializer</code> implementation is not
 *        empty.</p></li>
	
 * </ul>

 * <p>If the runtime determines that the servlet must be automatically
 * mapped, it must be mapped to the following
 * &lt;<code>url-pattern</code>&gt; entries.</p>

 * 	<ul>

 *         <li>/faces/*</li>
 *         <li>*.jsf</li>
 *         <li>*.faces</li>
 *         <li class="changed_added_2_3">*.xhtml</li>
 *	</ul>

 * </div>
 * 
 * <p class="changed_added_2_2_mr1">Note that the automatic mapping to {@code *.xhtml}
 * can be disabled with the context param "{@code javax.faces.DISABLE_FACESSERVLET_TO_XHTML}".
 * The runtime must consult this parameter to tell if the automatic
 * mapping of the {@code FacesServlet} to the extension {@code *.xhtml}
 * should be disabled.  The implementation must disable this automatic
 * mapping if and only if the value of this parameter is equal, ignoring
 * case, to {@code true}.</p>

 * <div class="changed_added_2_2">
 * 
 * <p>This class must be annotated with {@code javax.servlet.annotation.MultipartConfig}.
 * This causes the Servlet container in which the JSF implementation is running
 * to correctly handle multipart form data.</p>

 * <p><strong>Some security considerations relating to this class</strong></p>

 * <p>The topic of web application security is a cross-cutting concern
 * and every aspect of the specification address it.  However, as with
 * any framework, the application developer needs to pay careful
 * attention to security.  Please consider these topics among the rest
 * of the security concerns for the application.  This is by no means a
 * complete list of security concerns, and is no substitute for a
 * thorough application level security review.</p>
 *
 * <ul>

 * <p><strong>Prefix mappings and the <code>FacesServlet</code></strong></p>

 * <p>If the <code>FacesServlet</code> is mapped using a prefix
 * <code>&lt;url-pattern&gt;</code>, such as
 * <code>&lt;url-pattern&gt;/faces/*&lt;/url-pattern&gt;</code>,
 * something must be done to prevent access to the view source without
 * its first being processed by the <code>FacesServlet</code>.  One
 * common approach is to apply a &lt;security-constraint&gt; to all
 * facelet files and flow definition files.  Please see the
 * <strong>Deployment Descriptor</strong> chapter of the Java Servlet
 * Specification for more information the use of
 * &lt;security-constraint&gt;.</p>

 * <p><strong>Allowable HTTP Methods</strong></p>

 * <p>The JSF specification only requires the use of the GET and POST
 * http methods.  If your web application does not require any other
 * http methods, such as PUT and DELETE, please consider restricting the
 * allowable http methods using the &lt;http-method&gt; and
 * &lt;http-method-omission&gt; elements.  Please see the
 * <strong>Security</strong> of the Java Servlet Specification for more
 * information the use of these elements.</p>


 * </ul>
 *
 * </div>
 */
@MultipartConfig
public final class FacesServlet implements Servlet {
    
    /*
     * A white space separated list of case sensitive HTTP method names
     * that are allowed to be processed by this servlet. * means allow all
     */
    private static final String ALLOWED_HTTP_METHODS_ATTR =
            "com.sun.faces.allowedHttpMethods";
    
    // Http method names must be upper case. http://www.w3.org/Protocols/HTTP/NoteMethodCS.html
    // List of valid methods in Http 1.1 http://www.w3.org/Protocols/rfc2616/rfc2616-sec9.html#sec9

    private enum HttpMethod {
        
        OPTIONS("OPTIONS"),
        GET("GET"),
        HEAD("HEAD"),
        POST("POST"),
        PUT("PUT"),
        DELETE("DELETE"),
        TRACE("TRACE"),
        CONNECT("CONNECT");
        
        private String name;
        
        HttpMethod(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
        
    }


    private Set<String> allowedUnknownHttpMethods;
    private Set<HttpMethod> allowedKnownHttpMethods;
    final private Set<HttpMethod> defaultAllowedHttpMethods = 
            EnumSet.range(HttpMethod.OPTIONS, HttpMethod.CONNECT);
    private Set<HttpMethod> allHttpMethods;

    private boolean allowAllMethods;

    /**
     * <p>Context initialization parameter name for a comma delimited list
     * of context-relative resource paths (in addition to
     * <code>/WEB-INF/faces-config.xml</code> which is loaded automatically
     * if it exists) containing JavaServer Faces configuration information.</p>
     */
    public static final String CONFIG_FILES_ATTR =
        "javax.faces.CONFIG_FILES";


    /**
     * <p>Context initialization parameter name for the lifecycle identifier
     * of the {@link Lifecycle} instance to be utilized.</p>
     */
    public static final String LIFECYCLE_ID_ATTR =
        "javax.faces.LIFECYCLE_ID";


    /**
     * The <code>Logger</code> for this class.
     */
    private static final Logger LOGGER =
          Logger.getLogger("javax.faces.webapp", "javax.faces.LogStrings");


    /**
     * <p>Factory for {@link FacesContext} instances.</p>
     */
    private FacesContextFactory facesContextFactory = null;


    /**
     * <p>The {@link Lifecycle} instance to use for request processing.</p>
     */
    private Lifecycle lifecycle = null;


    /**
     * <p>The <code>ServletConfig</code> instance for this servlet.</p>
     */
    private ServletConfig servletConfig = null;

    /**
     * From GLASSFISH-15632.  If true, the FacesContext instance
     * left over from startup time has been released.  
     */
    private boolean initFacesContextReleased = false;
    

    /**
     * <p>Release all resources acquired at startup time.</p>
     */
    public void destroy() {

        facesContextFactory = null;
        lifecycle = null;
        servletConfig = null;
        uninitHttpMethodValidityVerification();

    }


    /**
     * <p>Return the <code>ServletConfig</code> instance for this servlet.</p>
     */
    public ServletConfig getServletConfig() {

        return (this.servletConfig);

    }


    /**
     * <p>Return information about this Servlet.</p>
     */
    public String getServletInfo() {

        return (this.getClass().getName());

    }


    /**
     * <p>Acquire the factory instances we will require.</p>
     *
     * @throws ServletException if, for any reason, the startup of
     * this Faces application failed.  This includes errors in the
     * config file that is parsed before or during the processing of
     * this <code>init()</code> method.
     */
    public void init(ServletConfig servletConfig) throws ServletException {

        // Save our ServletConfig instance
        this.servletConfig = servletConfig;

        // Acquire our FacesContextFactory instance
        try {
            facesContextFactory = (FacesContextFactory)
                FactoryFinder.getFactory
                (FactoryFinder.FACES_CONTEXT_FACTORY);
        } catch (FacesException e) {
            ResourceBundle rb = LOGGER.getResourceBundle();
            String msg = rb.getString("severe.webapp.facesservlet.init_failed");
            Throwable rootCause = (e.getCause() != null) ? e.getCause() : e;
            LOGGER.log(Level.SEVERE, msg, rootCause);
            throw new UnavailableException(msg);
        }

        // Acquire our Lifecycle instance
        try {
            LifecycleFactory lifecycleFactory = (LifecycleFactory)
                FactoryFinder.getFactory(FactoryFinder.LIFECYCLE_FACTORY);
            String lifecycleId ;

            // First look in the servlet init-param set
            if (null == (lifecycleId = servletConfig.getInitParameter(LIFECYCLE_ID_ATTR))) {
                // If not found, look in the context-param set 
                lifecycleId = servletConfig.getServletContext().getInitParameter
                    (LIFECYCLE_ID_ATTR);
            }

            if (lifecycleId == null) {
                lifecycleId = LifecycleFactory.DEFAULT_LIFECYCLE;
            }
            lifecycle = lifecycleFactory.getLifecycle(lifecycleId);
            initHttpMethodValidityVerification();
        } catch (FacesException e) {
            Throwable rootCause = e.getCause();
            if (rootCause == null) {
                throw e;
            } else {
                throw new ServletException(e.getMessage(), rootCause);
            }
        }

    }

    private void initHttpMethodValidityVerification() {

        assert (null == allowedUnknownHttpMethods);
        assert (null != defaultAllowedHttpMethods);
        assert (null == allHttpMethods);
        allHttpMethods = EnumSet.allOf(HttpMethod.class);

        // Configure our permitted HTTP methods

        allowedUnknownHttpMethods = Collections.emptySet();
        allowedKnownHttpMethods = defaultAllowedHttpMethods;
        
        String[] methods;
        String allowedHttpMethodsString = servletConfig.getServletContext().getInitParameter(ALLOWED_HTTP_METHODS_ATTR);
        if (null != allowedHttpMethodsString) {
            methods = allowedHttpMethodsString.split("\\s+");
            assert (null != methods); // assuming split always returns a non-null array result
            allowedUnknownHttpMethods = new HashSet(methods.length);
            List<String> allowedKnownHttpMethodsStringList = new ArrayList<String>();
            // validate input against allHttpMethods data structure
            for (String cur : methods) {
                if (cur.equals("*")) {
                    allowAllMethods = true;
                    allowedUnknownHttpMethods = Collections.emptySet();
                    return;
                }
                boolean isKnownHttpMethod;
                try {
                    HttpMethod.valueOf(cur);
                    isKnownHttpMethod = true;
                } catch (IllegalArgumentException e) {
                    isKnownHttpMethod = false;
                }
                
                if (!isKnownHttpMethod) {
                    if (LOGGER.isLoggable(Level.WARNING)) {
                        HttpMethod [] values = HttpMethod.values();
                        Object [] arg = new Object[values.length + 1];
                        arg[0] = cur;
                        System.arraycopy(values, HttpMethod.OPTIONS.ordinal(), 
                                         arg, 1, values.length);
                        LOGGER.log(Level.WARNING,
                                "warning.webapp.facesservlet.init_invalid_http_method",
                                arg);
                    }
                    // prevent duplicates
                    if (!allowedUnknownHttpMethods.contains(cur)) {
                        allowedUnknownHttpMethods.add(cur);
                    }
                } else {
                    // prevent duplicates
                    if (!allowedKnownHttpMethodsStringList.contains(cur)) {
                        allowedKnownHttpMethodsStringList.add(cur);
                    }
                }
            }
            // Optimally initialize allowedKnownHttpMethods
            if (5 == allowedKnownHttpMethodsStringList.size()) {
                allowedKnownHttpMethods = EnumSet.of(
                        HttpMethod.valueOf(allowedKnownHttpMethodsStringList.get(0)),
                        HttpMethod.valueOf(allowedKnownHttpMethodsStringList.get(1)),
                        HttpMethod.valueOf(allowedKnownHttpMethodsStringList.get(2)),
                        HttpMethod.valueOf(allowedKnownHttpMethodsStringList.get(3)),
                        HttpMethod.valueOf(allowedKnownHttpMethodsStringList.get(4))
                        );
            } else if (4 == allowedKnownHttpMethodsStringList.size()) {
                allowedKnownHttpMethods = EnumSet.of(
                        HttpMethod.valueOf(allowedKnownHttpMethodsStringList.get(0)),
                        HttpMethod.valueOf(allowedKnownHttpMethodsStringList.get(1)),
                        HttpMethod.valueOf(allowedKnownHttpMethodsStringList.get(2)),
                        HttpMethod.valueOf(allowedKnownHttpMethodsStringList.get(3))
                        );
                
            } else if (3 == allowedKnownHttpMethodsStringList.size()) {
                allowedKnownHttpMethods = EnumSet.of(
                        HttpMethod.valueOf(allowedKnownHttpMethodsStringList.get(0)),
                        HttpMethod.valueOf(allowedKnownHttpMethodsStringList.get(1)),
                        HttpMethod.valueOf(allowedKnownHttpMethodsStringList.get(2))
                        );
                
            } else if (2 == allowedKnownHttpMethodsStringList.size()) {
                allowedKnownHttpMethods = EnumSet.of(
                        HttpMethod.valueOf(allowedKnownHttpMethodsStringList.get(0)),
                        HttpMethod.valueOf(allowedKnownHttpMethodsStringList.get(1))
                        );
                
            } else if (1 == allowedKnownHttpMethodsStringList.size()) {
                allowedKnownHttpMethods = EnumSet.of(
                        HttpMethod.valueOf(allowedKnownHttpMethodsStringList.get(0))
                        );
                
            } else {
                List<HttpMethod> restList = 
                        new ArrayList<HttpMethod>(allowedKnownHttpMethodsStringList.size() - 1);
                for (int i = 1; i < allowedKnownHttpMethodsStringList.size() - 1; i++) {
                    restList.add(HttpMethod.valueOf(
                            allowedKnownHttpMethodsStringList.get(i)
                            ));
                }
                HttpMethod first = HttpMethod.valueOf(allowedKnownHttpMethodsStringList.get(0));
                HttpMethod [] rest = new HttpMethod[restList.size()];
                restList.toArray(rest);
                allowedKnownHttpMethods = EnumSet.of(first, rest);
                
            } 
        }
    }

    private void uninitHttpMethodValidityVerification() {
        assert (null != allowedUnknownHttpMethods);
        assert (null != defaultAllowedHttpMethods);
        assert (null != allHttpMethods);

        allowedUnknownHttpMethods.clear();
        allowedUnknownHttpMethods = null;
        allowedKnownHttpMethods.clear();
        allowedKnownHttpMethods = null;
        allHttpMethods.clear();
        allHttpMethods = null;

    }


    /**
     * <p class="changed_modified_2_0"><span
     * class="changed_modified_2_2">Process</span> an incoming request,
     * and create the corresponding response according to the following
     * specification.</p>
     * 
     * <div class="changed_modified_2_0">
     *
     * <p>If the <code>request</code> and <code>response</code>
     * arguments to this method are not instances of
     * <code>HttpServletRequest</code> and
     * <code>HttpServletResponse</code>, respectively, the results of
     * invoking this method are undefined.</p>
     *
     * <p>This method must respond to requests that <span
     * class="changed_modified_2_2">contain</span> the following
     * strings by invoking the <code>sendError</code> method on the
     * response argument (cast to <code>HttpServletResponse</code>),
     * passing the code <code>HttpServletResponse.SC_NOT_FOUND</code> as
     * the argument. </p>
     *
     * <ul>
     *
<pre><code>
/WEB-INF/
/WEB-INF
/META-INF/
/META-INF
</code></pre>
     *
     * </ul>
     *
     
     * <p>If none of the cases described above in the specification for
     * this method apply to the servicing of this request, the following
     * action must be taken to service the request.</p>

     * <p>Acquire a {@link FacesContext} instance for this request.</p>

     * <p>Acquire the <code>ResourceHandler</code> for this request by
     * calling {@link
     * javax.faces.application.Application#getResourceHandler}.  Call
     * {@link
     * javax.faces.application.ResourceHandler#isResourceRequest}.  If
     * this returns <code>true</code> call {@link
     * javax.faces.application.ResourceHandler#handleResourceRequest}.
     * If this returns <code>false</code>, <span
     * class="changed_added_2_2">call {@link
     * javax.faces.lifecycle.Lifecycle#attachWindow} followed by </span>
     * {@link javax.faces.lifecycle.Lifecycle#execute} followed by
     * {@link javax.faces.lifecycle.Lifecycle#render}.  If a {@link
     * javax.faces.FacesException} is thrown in either case, extract the
     * cause from the <code>FacesException</code>.  If the cause is
     * <code>null</code> extract the message from the
     * <code>FacesException</code>, put it inside of a new
     * <code>ServletException</code> instance, and pass the
     * <code>FacesException</code> instance as the root cause, then
     * rethrow the <code>ServletException</code> instance.  If the cause
     * is an instance of <code>ServletException</code>, rethrow the
     * cause.  If the cause is an instance of <code>IOException</code>,
     * rethrow the cause.  Otherwise, create a new
     * <code>ServletException</code> instance, passing the message from
     * the cause, as the first argument, and the cause itself as the
     * second argument.</p>

     * <p class="changed_modified_2_0_rev_a">The implementation must
     * make it so {@link javax.faces.context.FacesContext#release} is
     * called within a finally block as late as possible in the
     * processing for the JSF related portion of this request.</p>

     * </div>
     *
     * @param req The servlet request we are processing
     * @param resp The servlet response we are creating
     *
     * @throws IOException if an input/output error occurs during processing
     * @throws ServletException if a servlet error occurs during processing

     */
    @Override
    public void service(ServletRequest req,
                        ServletResponse resp)
        throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        requestStart(request.getRequestURI()); // V3 Probe hook
        
        if (!isHttpMethodValid(request)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        if (Thread.currentThread().isInterrupted()) {
            if (LOGGER.isLoggable(Level.FINER)) {
                LOGGER.log(Level.FINE, "Thread {0} given to FacesServlet.service() in interrupted state", 
                        Thread.currentThread().getName());
            }
        }

        // If prefix mapped, then ensure requests for /WEB-INF are
        // not processed.
        String pathInfo = request.getPathInfo();
        if (pathInfo != null) {
            pathInfo = pathInfo.toUpperCase();
            if (pathInfo.contains("/WEB-INF/")
                || pathInfo.contains("/WEB-INF")
                || pathInfo.contains("/META-INF/")
                || pathInfo.contains("/META-INF")) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
        }

        if (!initFacesContextReleased) {
            FacesContext initFacesContext = FacesContext.getCurrentInstance();
            if (null != initFacesContext) {
                initFacesContext.release();
            }
            initFacesContextReleased = true;
        }
        
        // Acquire the FacesContext instance for this request
        FacesContext context = facesContextFactory.getFacesContext
              (servletConfig.getServletContext(), request, response, lifecycle);

        // Execute the request processing lifecycle for this request
        try {
            ResourceHandler handler =
                  context.getApplication().getResourceHandler();
            if (handler.isResourceRequest(context)) {
                handler.handleResourceRequest(context);
            } else {
                lifecycle.attachWindow(context);
                lifecycle.execute(context);
                lifecycle.render(context);
            }
        } catch (FacesException e) {
            Throwable t = e.getCause();
            if (t == null) {
                throw new ServletException(e.getMessage(), e);
            } else {
                if (t instanceof ServletException) {
                    throw ((ServletException) t);
                } else if (t instanceof IOException) {
                    throw ((IOException) t);
                } else {
                    throw new ServletException(t.getMessage(), t);
                }
            }
        }
        finally {
            // Release the FacesContext instance for this request
            context.release();
        }

        requestEnd(); // V3 Probe hook
    }

    private boolean isHttpMethodValid(HttpServletRequest request) {
        boolean result = false;
        if (allowAllMethods) {
            result = true;
        } else {
            String requestMethodString = request.getMethod();
            HttpMethod requestMethod = null;
            boolean isKnownHttpMethod;
            try {
                requestMethod = HttpMethod.valueOf(requestMethodString);
                isKnownHttpMethod = true;
            } catch (IllegalArgumentException e) {
                isKnownHttpMethod = false;
            }
            if (isKnownHttpMethod) {
                result = allowedKnownHttpMethods.contains(requestMethod);
            } else {
                result = allowedUnknownHttpMethods.contains(requestMethodString);
            }
            
        }

        return result;
    }


    // --------------------------------------------------------- Private Methods


    /**
     * DO NOT REMOVE. Necessary for V3 probe monitoring.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    private void requestStart(String requestUri) { }


    /**
     * DO NOT REMOVE. Necessary for V3 probe monitoring.
     */
    private void requestEnd() { }
    
    }    

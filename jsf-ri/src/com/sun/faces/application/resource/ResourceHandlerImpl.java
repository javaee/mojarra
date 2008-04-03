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


package com.sun.faces.application.resource;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.application.ProjectStage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import com.sun.faces.config.WebConfiguration;
import com.sun.faces.config.WebConfiguration.WebContextInitParameter;
import com.sun.faces.util.FacesLogger;
import com.sun.faces.util.Util;
import com.sun.faces.util.RequestStateManager;

/**
 * This is the default implementation of {@link ResourceHandler}.
 */
public class ResourceHandlerImpl extends ResourceHandler {

    // Log instance for this class
    private static final Logger LOGGER = FacesLogger.APPLICATION.getLogger();

    ResourceManager manager = new ResourceManager();
    List<Pattern> excludePatterns;
    private long creationTime;
    private WebConfiguration webconfig;

    // ------------------------------------------------------------ Constructors


    /**
     * Creates a new instance of ResourceHandlerImpl
     */
    public ResourceHandlerImpl() {

        creationTime = System.currentTimeMillis();
        webconfig = WebConfiguration.getInstance();
        initExclusions();

    }


    // ------------------------------------------- Methods from Resource Handler


    /**
     * @see ResourceHandler#createResource(String)
     */
    public Resource createResource(String resourceName) {

        Util.notNull("resourceName", resourceName);

        return createResource(resourceName, null, null);

    }


    /**
     * @see ResourceHandler#createResource(String, String)
     */
    public Resource createResource(String resourceName, String libraryName) {

        Util.notNull("resourceName", resourceName);

        return createResource(resourceName, libraryName, null);

    }


    /**
     * @see ResourceHandler#createResource(String, String, String)
     */
    public Resource createResource(String resourceName,
                                   String libraryName,
                                   String contentType) {

        Util.notNull("resourceName", resourceName);

        ResourceInfo info = manager.findResource(libraryName,
                                                 resourceName,
                                                 FacesContext.getCurrentInstance());
        return ((info != null)
                ? new ResourceImpl(this, info, (contentType != null)
                                               ? contentType
                                               : getContentType(resourceName))
                : null);

    }


    /**
     * @see ResourceHandler#isResourceRequest(javax.faces.context.FacesContext)
     */
    public boolean isResourceRequest(FacesContext context) {

        Boolean isResourceRequest = (Boolean)
              RequestStateManager.get(context,
                                      RequestStateManager.RESOURCE_REQUEST);
        if (isResourceRequest == null) {
            String resourceId = normalizeResourceRequest(context);
            isResourceRequest = (resourceId != null
                                 ? resourceId
                  .startsWith(ResourceHandler.RESOURCE_IDENTIFIER)
                                 : Boolean.FALSE);
            RequestStateManager.set(context,
                                    RequestStateManager.RESOURCE_REQUEST,
                                    isResourceRequest);
        }

        return (isResourceRequest);

    }


    /**
     * @see javax.faces.application.ResourceHandler#handleResourceRequest(javax.faces.context.FacesContext)
     */
    public void handleResourceRequest(FacesContext context) throws IOException {

        String resourceId = normalizeResourceRequest(context);
        // handleResourceRequest called for a non-resource request,
        // bail out.
        if (resourceId == null) {
            return;
        }
        
        ExternalContext extContext = context.getExternalContext();
        // this case should be safe in both the standard Servlet
        // and portlet environments
        HttpServletResponse response =
                  (HttpServletResponse) extContext.getResponse();

        if (isExcluded(resourceId)) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        assert (null != resourceId);
        assert (resourceId.startsWith(ResourceHandler.RESOURCE_IDENTIFIER));

        Resource resource = null;
        String resourceName = null;
        String libraryName = null;
        if (ResourceHandler.RESOURCE_IDENTIFIER.length() < resourceId.length()) {
            resourceName = resourceId.substring(RESOURCE_IDENTIFIER.length() + 1);
            assert(resourceName != null);
            libraryName = context.getExternalContext().getRequestParameterMap()
                  .get("ln");
            resource = createResource(resourceName, libraryName);
        }

        if (resource != null) {
            if (resource.userAgentNeedsUpdate(context)) {
                ReadableByteChannel resourceChannel = null;
                WritableByteChannel out = null;
                ByteBuffer buf = allocateByteBuffer();
                try {
                    InputStream in = resource.getInputStream();
                    if (in == null) {
                        send404(context, response, resourceName, libraryName);
                        return;
                    }
                    resourceChannel =
                          Channels.newChannel(resource.getInputStream());
                    out = Channels.newChannel(response.getOutputStream());
                    response.setBufferSize(buf.capacity());
                    String contentType = resource.getContentType();
                    if (contentType != null) {
                        response.setContentType(resource.getContentType());
                    }
                    handleHeaders(resource, response);

                    int size = 0;
                    for (int thisRead = resourceChannel.read(buf), totalWritten = 0;
                         thisRead != -1;
                         thisRead = resourceChannel.read(buf)) {

                        buf.rewind();
                        buf.limit(thisRead);
                        do {
                            totalWritten += out.write(buf);
                        } while (totalWritten < size);
                        buf.clear();
                        size += thisRead;

                    }

                    response.setContentLength(size);

                } catch (IOException ioe) {
                    send404(context, response, resourceName, libraryName, ioe);
                } finally {
                    if (out != null) {
                        out.close();
                    }
                    if (resourceChannel != null) {
                        resourceChannel.close();
                    }
                }
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
            }

        } else {
            send404(context, response, resourceName, libraryName);
        }

    }

    private void send404(FacesContext ctx,
                         HttpServletResponse response,
                         String resourceName,
                         String libraryName) {

        send404(ctx, response, resourceName, libraryName, null);

    }

    private void send404(FacesContext ctx,
                         HttpServletResponse response,
                         String resourceName,
                         String libraryName,
                         Throwable t) {

        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        Level level;
        
        if (ctx.getApplication().getProjectStage() != ProjectStage.Production) {
            level = Level.WARNING;
        } else {
            level = ((t != null) ? Level.WARNING : Level.FINE);
        }

        if (libraryName != null) {
            if (LOGGER.isLoggable(level)) {
                LOGGER.log(level,
                           "jsf.application.resource.unable_to_serve_from_library",
                           new Object[]{resourceName, libraryName});
                if (t != null) {
                    LOGGER.log(level, "", t);
                }
            }
        } else {
            if (LOGGER.isLoggable(level)) {
                LOGGER.log(level,
                           "jsf.application.resource.unable_to_serve",
                           new Object[]{resourceName});
                if (t != null) {
                    LOGGER.log(level, "", t);
                }
            }
        }

    }

    // ------------------------------------------------- Package Private Methods


    /**
     * This method is leveraged by {@link ResourceImpl} to detemine if a resource
     * has been upated.  In short, a resource has been updated if the timestamp
     * is newer than the timestamp of the ResourceHandler creation time.
     * @return the time when the ResourceHandler was instantiated (in milliseconds)
     */
    long getCreationTime() {

        return creationTime;

    }


    /**
     * This method is here soley for the purpose of unit testing and will
     * not be invoked during normal runtime.
     * @param creationTime the time in milliseconds
     */
    void setCreationTime(long creationTime) {

        this.creationTime = creationTime;

    }


    /**
     * Utility method leveraged by ResourceImpl to reduce the cost of
     * looking up the WebConfiguration per-instance.
     * @return the {@link WebConfiguration} for this application
     */
    WebConfiguration getWebConfig() {

        return webconfig;
        
    }

    
    // --------------------------------------------------------- Private Methods


    /**
     * @param resourceName the resource of interest.  The resourceName in question
     *  may consist of zero or more path elements such that resourceName could
     *  be something like path1/path2/resource.jpg or resource.jpg
     * @return the content type for this resource
     */
    private String getContentType(String resourceName) {

        return FacesContext.getCurrentInstance().getExternalContext().getMimeType(resourceName);

    }


    /**
     * Normalize the request path to exclude JSF invocation information.
     * If the FacesServlet servicing this request was prefix mapped, then
     * the path to the FacesServlet will be removed.
     * If the FacesServlet servicing this request was extension mapped, then
     * the extension will be trimmed off.
     * @param context the <code>FacesContext</code> for the current request
     * @return the request path without JSF invocation information
     */
    private String normalizeResourceRequest(FacesContext context) {

        String path;
        String facesServletMapping = Util.getFacesMapping(context);
        // If it is extension mapped
        if (!Util.isPrefixMapped(facesServletMapping)) {
            path = context.getExternalContext().getRequestServletPath();
            // strip off the extension
            int i = path.lastIndexOf(".");
            if (0 < i) {
                path = path.substring(0, i);
            }
        } else {
            path = context.getExternalContext().getRequestPathInfo();
        }
        return path;

    }


    /**
     * @param resourceId the normalized request path as returned by
     *  {@link #normalizeResourceRequest(javax.faces.context.FacesContext)}
     * @return <code>true</code> if the request matces an excluded resource,
     *  otherwise <code>false</code>
     */
    private boolean isExcluded(String resourceId) {
        for (Pattern pattern : excludePatterns) {
            if (pattern.matcher(resourceId).matches()) {
                return true;
            }
        }
        return false;
    }


    /**
     * Initialize the exclusions for this application.
     * If no explicit exclusions are configured, the defaults of
     * <ul>
     * <li>.class</li>
     * <li>.properties</li>
     * <li>.xhtml</li>
     * <li>.jsp</li>
     * <li>.jspx</li>
     * <ul>
     * will be used.
     */
    private void initExclusions() {

        String excludesParam = webconfig
              .getOptionValue(WebContextInitParameter.ResourceExcludes);
        String[] patterns = Util.split(excludesParam, " ");
        excludePatterns = new ArrayList<Pattern>(patterns.length);
        for (String pattern : patterns) {
            excludePatterns.add(Pattern.compile(".*\\" + pattern));
        }
        
    }


    private void handleHeaders(Resource resource,
                               HttpServletResponse response) {

        for (Map.Entry<String, String> cur :
             resource.getResponseHeaders().entrySet()) {
                response.setHeader(cur.getKey(), cur.getValue());
        }

    }

    private ByteBuffer allocateByteBuffer() {

        int size;
        try {
            size = Integer.parseInt(webconfig.getOptionValue(WebContextInitParameter.ResourceBufferSize));
        } catch (NumberFormatException nfe) {
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.log(Level.WARNING,
                           "jsf.application.resource.invalid_resource_buffer_size",
                           new Object[] {
                               webconfig.getOptionValue(WebContextInitParameter.ResourceBufferSize),
                               WebContextInitParameter.ResourceBufferSize.getQualifiedName(),
                               WebContextInitParameter.ResourceBufferSize.getDefaultValue()
                           });
            }
            size = Integer.parseInt(WebContextInitParameter.ResourceBufferSize.getDefaultValue());
        }
        return ByteBuffer.allocate(size);

    }
}

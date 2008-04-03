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

import javax.activation.MimetypesFileTypeMap;
import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import com.sun.faces.config.WebConfiguration;
import com.sun.faces.config.WebConfiguration.WebContextInitParameter;
import com.sun.faces.util.FacesLogger;
import com.sun.faces.util.Util;

/**
 * This is the default implementation of {@link ResourceHandler}.
 */
public class ResourceHandlerImpl extends ResourceHandler {

    // Log instance for this class
    private static final Logger LOGGER = FacesLogger.APPLICATION.getLogger();
    private static final String NORMALIZED_ID_KEY =
          ResourceHandlerImpl.class.getName() + "NORMALIZED_ID";

    ResourceManager manager = new ResourceManager();
    List<Pattern> excludePatterns;
    MimetypesFileTypeMap mimeTypeMap;
    private long creationTime;
    private WebConfiguration webconfig;

    // ------------------------------------------------------------ Constructors


    /**
     * Creates a new instance of ResourceHandlerImpl
     */
    public ResourceHandlerImpl() {

        creationTime = System.currentTimeMillis();
        webconfig = WebConfiguration.getInstance();
        mimeTypeMap = new MimetypesFileTypeMap();
        mimeTypeMap.addMimeTypes("text/javascript js JS");
        mimeTypeMap.addMimeTypes("text/css css CSS");
        initExclusions();

    }


    // ------------------------------------------- Methods from Resource Handler


    /**
     * @see ResourceHandler#createResource(String)
     */
    public Resource createResource(String resourceName) {

        String contentType = getContentTypeFromResourceName(resourceName);
        return createResource(resourceName, null, contentType);

    }


    /**
     * @see ResourceHandler#createResource(String, String)
     */
    public Resource createResource(String resourceName, String libraryName) {

        String contentType = getContentTypeFromResourceName(resourceName);
        return createResource(resourceName, libraryName, contentType);

    }


    /**
     * @see ResourceHandler#createResource(String, String, String)
     */
    public Resource createResource(String resourceName,
                                   String libraryName,
                                   String contentType) {


        ResourceInfo info = manager.findResource(libraryName,
                                                 resourceName,
                                                 FacesContext.getCurrentInstance());
        return ((info != null)
                ? new ResourceImpl(this, info, contentType)
                : null);

    }


    /**
     * @see ResourceHandler#isResourceRequest(javax.faces.context.FacesContext)
     */
    public boolean isResourceRequest(FacesContext context) {

        String resourceId = normalizeResourceRequest(context);
        return (resourceId.startsWith(ResourceHandler.RESOURCE_IDENTIFIER));

    }


    /**
     * @see javax.faces.application.ResourceHandler#handleResourceRequest(javax.faces.context.FacesContext)
     */
    public void handleResourceRequest(FacesContext context) throws IOException {

        String resourceId = normalizeResourceRequest(context);
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
        if (ResourceHandler.RESOURCE_IDENTIFIER.length() < resourceId.length()) {
            String resourceName = resourceId.substring(RESOURCE_IDENTIFIER.length() + 1);
            String libraryName = context.getExternalContext().getRequestParameterMap()
                  .get("ln");
            resource = createResource(resourceName, libraryName);
        }

        if (resource != null) {
            if (resource.userAgentNeedsUpdate(context)) {
                ReadableByteChannel resourceChannel = null;
                WritableByteChannel out = null;
                ByteBuffer buf = allocateByteBuffer();
                try {
                    resourceChannel =
                          Channels.newChannel(resource.getInputStream());
                    out = Channels.newChannel(response.getOutputStream());
                    response.setBufferSize(buf.capacity());
                    response.setContentType(resource.getContentType());
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
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    if (resource.getLibraryName() != null) {
                        if (LOGGER.isLoggable(Level.WARNING)) {
                            LOGGER.log(Level.WARNING,
                                       "jsf.application.resource.unable_to_serve_from_library",
                                       new Object[]{resource.getResourceName(),
                                                    resource.getLibraryName()});
                            LOGGER.log(Level.WARNING, "", ioe);
                        }
                    } else {
                        if (LOGGER.isLoggable(Level.WARNING)) {
                            LOGGER.log(Level.WARNING,
                                       "jsf.application.resource.unable_to_serve",
                                       new Object[]{resource.getResourceName()});
                            LOGGER.log(Level.WARNING, "", ioe);
                        }
                    }
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
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
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
    private String getContentTypeFromResourceName(String resourceName) {

        String res = resourceName;
        if (res.contains("/")) {
            res = res.substring(res.lastIndexOf('/') + 1);
        }
        return mimeTypeMap.getContentType(res);

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

        ExternalContext extCtx = context.getExternalContext();
        String path = (String) extCtx.getRequestMap().get(NORMALIZED_ID_KEY);
        if (path == null) {
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
            extCtx.getRequestMap().put(NORMALIZED_ID_KEY, path);
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

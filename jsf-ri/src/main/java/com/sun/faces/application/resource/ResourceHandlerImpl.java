/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2013 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.faces.application.resource;

import com.sun.faces.application.ApplicationAssociate;
import com.sun.faces.config.WebConfiguration;
import static com.sun.faces.config.WebConfiguration.WebContextInitParameter.*;
import com.sun.faces.util.FacesLogger;
import com.sun.faces.util.RequestStateManager;
import com.sun.faces.util.Util;

import javax.faces.application.ProjectStage;
import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
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

/**
 * This is the default implementation of {@link ResourceHandler}.
 */
public class ResourceHandlerImpl extends ResourceHandler {

    // Log instance for this class
    private static final Logger LOGGER = FacesLogger.APPLICATION.getLogger();

    ResourceManager manager;
    List<Pattern> excludePatterns;
    private long creationTime;
    private long maxAge;
    private WebConfiguration webconfig;

    // ------------------------------------------------------------ Constructors


    /**
     * Creates a new instance of ResourceHandlerImpl
     */
    public ResourceHandlerImpl() {

        creationTime = System.currentTimeMillis();
        webconfig = WebConfiguration.getInstance();
        ExternalContext extContext = FacesContext.getCurrentInstance().getExternalContext();
        manager = ApplicationAssociate.getInstance(extContext).getResourceManager();
        initExclusions(extContext.getApplicationMap());
        initMaxAge();

    }


    // ------------------------------------------- Methods from Resource Handler


    /**
     * @see ResourceHandler#createResource(String)
     */
    public Resource createResource(String resourceName) {

        Util.notNull("resourceName", resourceName);

        return createResource(resourceName, null, null);

    }

    @Override
    public Resource createViewResource(FacesContext facesContext, String resourceName) {

        Util.notNull("resourceName", resourceName);

        boolean development = facesContext.isProjectStage(ProjectStage.Development);

        String ctype = getContentType(facesContext, resourceName);
        ResourceInfo info = manager.findResource(null,
                                                 resourceName,
                                                 ctype,
                                                 true,
                                                 facesContext);
        if (info == null) {
            // prevent message from being when we're dealing with
            // groovy is present and Application.createComponent()
            // tries to resolve a .groovy file as backing UIComponent.
            if (!development && "application/x-groovy".equals(ctype)) {
                return null;
            }
            logMissingResource(facesContext, resourceName, null, null);
            return null;
        } else {
            return new ResourceImpl(info, ctype, creationTime, maxAge);
        }
    }
    
    

    /**
     * @see ResourceHandler#createResourceFromId(String)
     */
    @Override
    public Resource createResourceFromId(String resourceId) {
        Util.notNull("resourceId", resourceId);
        FacesContext ctx = FacesContext.getCurrentInstance();

        boolean development = ctx.isProjectStage(ProjectStage.Development);
        
        ResourceInfo info = manager.findResource(resourceId);
        String ctype = getContentType(ctx, resourceId);
        if (info == null) {
            // prevent message from being when we're dealing with
            // groovy is present and Application.createComponent()
            // tries to resolve a .groovy file as backing UIComponent.
            if (!development && "application/x-groovy".equals(ctype)) {
                return null;
            }
            logMissingResource(ctx, resourceId, null);
            return null;
        } else {
            return new ResourceImpl(info, ctype, creationTime, maxAge);
        }
        
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
        FacesContext ctx = FacesContext.getCurrentInstance();

        boolean development = ctx.isProjectStage(ProjectStage.Development);

        String ctype = ((contentType != null)
                        ? contentType
                        : getContentType(ctx, resourceName));
        ResourceInfo info = manager.findResource(libraryName,
                                                 resourceName,
                                                 ctype,
                                                 ctx);
        if (info == null) {
            // prevent message from being when we're dealing with
            // groovy is present and Application.createComponent()
            // tries to resolve a .groovy file as backing UIComponent.
            if (!development && "application/x-groovy".equals(ctype)) {
                return null;
            }
            logMissingResource(ctx, resourceName, libraryName, null);
            return null;
        } else {
            return new ResourceImpl(info, ctype, creationTime, maxAge);
        }

    }

    @Override
    public boolean libraryExists(String libraryName) {

        if (libraryName.contains("../")) {
            return false;
        }
        FacesContext context = FacesContext.getCurrentInstance();
        // PENDING(fcaputo) do we need to iterate over the contracts here? I don't think so.
        LibraryInfo info = manager.findLibrary(libraryName, null, null, context);
        if (null == info) {
            info = manager.findLibraryOnClasspathWithZipDirectoryEntryScan(libraryName, null, null, context, true);

        }
        return (info != null);

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
                                 ? resourceId.startsWith(RESOURCE_IDENTIFIER)
                                 : Boolean.FALSE);
            RequestStateManager.set(context,
                                    RequestStateManager.RESOURCE_REQUEST,
                                    isResourceRequest);
        }

        return (isResourceRequest);

    }

    @Override
    public String getRendererTypeForResourceName(String resourceName) {
        String rendererType = null;
        
        String contentType = getContentType(FacesContext.getCurrentInstance(),
                                            resourceName);
        if (null != contentType) {
            contentType = contentType.toLowerCase();
            if (-1 != contentType.indexOf("javascript")) {
                rendererType = "javax.faces.resource.Script";
            }
            else if (-1 != contentType.indexOf("css")) {
                rendererType = "javax.faces.resource.Stylesheet";
            }
        }
        return rendererType;
    }
    
    



    /**
     * @see javax.faces.application.ResourceHandler#handleResourceRequest(javax.faces.context.FacesContext)
     */
    @Override
    public void handleResourceRequest(FacesContext context) throws IOException {

        String resourceId = normalizeResourceRequest(context);
        // handleResourceRequest called for a non-resource request,
        // bail out.
        if (resourceId == null) {
            return;
        }
        
        ExternalContext extContext = context.getExternalContext();

        if (isExcluded(resourceId)) {
            extContext.setResponseStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        assert (null != resourceId);
        assert (resourceId.startsWith(RESOURCE_IDENTIFIER));

        Resource resource = null;
        String resourceName = null;
        String libraryName = null;
        if (ResourceHandler.RESOURCE_IDENTIFIER.length() < resourceId.length()) {
            resourceName = resourceId.substring(RESOURCE_IDENTIFIER.length() + 1);
            assert(resourceName != null);
            libraryName = context.getExternalContext().getRequestParameterMap()
                  .get("ln");
            
            boolean createResource;
            
            if (libraryName != null) {
                createResource = libraryNameIsSafe(libraryName);
                if (!createResource) {
                    send404(context, resourceName, libraryName, true);
                    return;
                }
            } else {
                createResource = true;
            }
            if (createResource) {
                resource = context.getApplication().getResourceHandler().createResource(resourceName, libraryName);
            }
        }

        if (resource != null) {
            if (resource.userAgentNeedsUpdate(context)) {
                ReadableByteChannel resourceChannel = null;
                WritableByteChannel out = null;
                ByteBuffer buf = allocateByteBuffer();
                try {
                    InputStream in = resource.getInputStream();
                    if (in == null) {
                        send404(context, resourceName, libraryName, true);
                        return;
                    }
                    resourceChannel =
                          Channels.newChannel(in);
                    out = Channels.newChannel(extContext.getResponseOutputStream());
                    extContext.setResponseBufferSize(buf.capacity());
                    String contentType = resource.getContentType();
                    if (contentType != null) {
                        extContext.setResponseContentType(resource.getContentType());
                    }
                    handleHeaders(context, resource);

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

                    if (!extContext.isResponseCommitted()) {
                        extContext.setResponseContentLength(size);
                    }

                } catch (IOException ioe) {
                    send404(context, resourceName, libraryName, ioe, true);
                } finally {
                    if (out != null) {
                        try {
                            out.close();
                        } catch (IOException ignored) {
                            // Maybe log a warning here?
                        }
                    }
                    if (resourceChannel != null) {
                        resourceChannel.close();
                    }
                }
            } else {
                send304(context);
            }

        } else {
            // already logged elsewhere
            send404(context, resourceName, libraryName, false);
        }

    }
    
    private boolean libraryNameIsSafe(String libraryName) {
        assert(null != libraryName);
        boolean result;
        
        result = !(libraryName.startsWith(".") || 
                
                   libraryName.startsWith("/") ||
                   libraryName.contains("/") ||
                
                   libraryName.startsWith("\\") ||
                   libraryName.contains("\\") ||
                
                   libraryName.startsWith("%2e") ||
                
                   libraryName.startsWith("%2f") ||
                   libraryName.contains("%2f") ||
                
                   libraryName.startsWith("%5c") ||
                   libraryName.contains("%5c") ||
                
                   libraryName.startsWith("\\u002e") ||
                
                   libraryName.startsWith("\\u002f") ||
                   libraryName.contains("\\u002f") ||
                
                   libraryName.startsWith("\\u005c") ||
                   libraryName.contains("\\u005c"));
        
        return result;
    }

    private void send404(FacesContext ctx,
                         String resourceName,
                         String libraryName,
                         boolean logMessage) {

        send404(ctx, resourceName, libraryName, null, logMessage);

    }


    private void send404(FacesContext ctx,
                         String resourceName,
                         String libraryName,
                         Throwable t,
                         boolean logMessage) {

        ctx.getExternalContext().setResponseStatus(HttpServletResponse.SC_NOT_FOUND);
        if (logMessage) {
            logMissingResource(ctx, resourceName, libraryName, t);
        }


    }


    private void send304(FacesContext ctx) {

        ctx.getExternalContext().setResponseStatus(HttpServletResponse.SC_NOT_MODIFIED);

    }



    // ------------------------------------------------- Package Private Methods


    /**
     * This method is leveraged by {@link ResourceImpl} to detemine if a resource
     * has been upated.  In short, a resource has been updated if the timestamp
     * is newer than the timestamp of the ResourceHandler creation time.
     * @return the time when the ResourceHandler was instantiated (in milliseconds)
     */
    @SuppressWarnings({"UnusedDeclaration"})
    long getCreationTime() {

        return creationTime;

    }


    /**
     * This method is here soley for the purpose of unit testing and will
     * not be invoked during normal runtime.
     * @param creationTime the time in milliseconds
     */
    @SuppressWarnings({"UnusedDeclaration"})
    void setCreationTime(long creationTime) {

        this.creationTime = creationTime;

    }


    /**
     * Utility method leveraged by ResourceImpl to reduce the cost of
     * looking up the WebConfiguration per-instance.
     * @return the {@link WebConfiguration} for this application
     */
    @SuppressWarnings({"UnusedDeclaration"})
    WebConfiguration getWebConfig() {

        return webconfig;
        
    }

    
    // --------------------------------------------------------- Private Methods


    /**
     * Log a message indicating a particular resource (reference by name and/or
     * library) could not be found.  If this was due to an exception, the exception
     * provided will be logged as well.
     *
     * @param ctx the {@link FacesContext} for the current request
     * @param resourceName the resource name
     * @param libraryName the resource library
     * @param t the exception caught when attempting to find the resource
     */
    private void logMissingResource(FacesContext ctx,
                                    String resourceName,
                                    String libraryName,
                                    Throwable t) {

        Level level;
        if (!ctx.isProjectStage(ProjectStage.Production)) {
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

    /**
     * Log a message indicating a particular resource (reference by name and/or
     * library) could not be found.  If this was due to an exception, the exception
     * provided will be logged as well.
     *
     * @param ctx the {@link FacesContext} for the current request
     * @param resourceName the resource name
     * @param libraryName the resource library
     * @param t the exception caught when attempting to find the resource
     */
    private void logMissingResource(FacesContext ctx,
                                    String resourceId,
                                    Throwable t) {

        Level level;
        if (!ctx.isProjectStage(ProjectStage.Production)) {
            level = Level.WARNING;
        } else {
            level = ((t != null) ? Level.WARNING : Level.FINE);
        }

        if (LOGGER.isLoggable(level)) {
                LOGGER.log(level,
                           "jsf.application.resource.unable_to_serve",
                           new Object[]{resourceId});
                if (t != null) {
                    LOGGER.log(level, "", t);
                }
        }

    }


    /**
     * @param resourceName the resource of interest.  The resourceName in question
     *  may consist of zero or more path elements such that resourceName could
     *  be something like path1/path2/resource.jpg or resource.jpg
     * @return the content type for this resource
     */
    private String getContentType(FacesContext ctx, String resourceName) {

        return ctx.getExternalContext().getMimeType(resourceName);

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
    private void initExclusions(Map<String, Object> appMap) {

        String excludesParam = webconfig
              .getOptionValue(ResourceExcludes);
        String[] patterns = Util.split(appMap, excludesParam, " ");
        excludePatterns = new ArrayList<Pattern>(patterns.length);
        for (String pattern : patterns) {
            excludePatterns.add(Pattern.compile(".*\\" + pattern));
        }
        
    }

    private void initMaxAge() {

        maxAge = Long.parseLong(webconfig.getOptionValue(DefaultResourceMaxAge));

    }


    private void handleHeaders(FacesContext ctx,
                               Resource resource) {

        ExternalContext extContext = ctx.getExternalContext();
        for (Map.Entry<String, String> cur :
             resource.getResponseHeaders().entrySet()) {
                extContext.setResponseHeader(cur.getKey(), cur.getValue());
        }

    }

    private ByteBuffer allocateByteBuffer() {

        int size;
        try {
            size = Integer.parseInt(webconfig.getOptionValue(ResourceBufferSize));
        } catch (NumberFormatException nfe) {
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.log(Level.WARNING,
                           "jsf.application.resource.invalid_resource_buffer_size",
                           new Object[] {
                               webconfig.getOptionValue(ResourceBufferSize),
                               ResourceBufferSize.getQualifiedName(),
                               ResourceBufferSize.getDefaultValue()
                           });
            }
            size = Integer.parseInt(ResourceBufferSize.getDefaultValue());
        }
        return ByteBuffer.allocate(size);

    }
}

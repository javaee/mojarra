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
import com.sun.faces.util.FacesLogger;
import com.sun.faces.util.Util;

/**
 * This is the default implementation of {@link ResourceHandler}.
 */
public class ResourceHandlerImpl extends ResourceHandler {

    // Log instance for this class
    private static final Logger LOGGER = FacesLogger.APPLICATION.getLogger();

    ResourceManager manager = new ResourceManager();
    List<Pattern> excludePatterns;
    MimetypesFileTypeMap mimeTypeMap;
    private final long creationTime;
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
        ExternalContext extContext =
              FacesContext.getCurrentInstance().getExternalContext();
        String excludesParam =
              extContext.getInitParameter("javax.faces.resource.EXCLUDES");
        excludePatterns = new ArrayList<Pattern>();
        if (null != excludesParam) {
            String[] patterns = Util.split(excludesParam, " ");
            for (String pattern : patterns) {
                excludePatterns.add(Pattern.compile(".*\\" + pattern));
            }
        } else {
            excludePatterns.add(Pattern.compile(".*\\.class"));
            excludePatterns.add(Pattern.compile(".*\\.properties"));
            excludePatterns.add(Pattern.compile(".*\\.xhtml"));
            excludePatterns.add(Pattern.compile(".*\\.jsp"));
        }


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

        String viewId = normalizeViewId(context);
        boolean result = false;
        boolean matchesExcludeEntry = false;
        // Step 1, see if the viewId matches an exclude
        for (Pattern cur : excludePatterns) {
            if (matchesExcludeEntry = cur.matcher(viewId).matches()) {
                break;
            }
        }
        // Step 2, if the viewId does not match an exclude,
        // see if it starts with javax.faces.resource
        if (!matchesExcludeEntry) {
            result = viewId.startsWith("/javax.faces.resource");
        }
        return result;

    }


    /**
     * @see javax.faces.application.ResourceHandler#handleResourceRequest(javax.faces.context.FacesContext)
     */
    public void handleResourceRequest(FacesContext context) throws IOException {

        // TODO merge ViewHandler stuff from jsf-extendsions to here

        // PENDING REVIEW
        String viewId = normalizeViewId(context);
        String resourceName;
        String libraryName;
        //String localePrefix;
        Resource resource = null;
        assert (null != viewId);
        assert (viewId.startsWith("/javax.faces.resource/"));
        if ("/javax.faces.resource/".length() < viewId.length()) {

            resourceName = viewId.substring("/javax.faces.resource/".length());
            libraryName = context.getExternalContext().getRequestParameterMap()
                  .get("ln");
            resource = createResource(resourceName, libraryName);
        }

        if (resource != null) {

            ExternalContext extContext = context.getExternalContext();
            HttpServletResponse response =
                  (HttpServletResponse) extContext.getResponse();

            if (resource.userAgentNeedsUpdate(context)) {
                ReadableByteChannel resourceChannel;
                WritableByteChannel out;
                ByteBuffer buf = ByteBuffer.allocate(8192);
                try {
                    resourceChannel =
                          Channels.newChannel(resource.getInputStream());
                    out = Channels.newChannel(response.getOutputStream());
                    int thisRead, totalWritten = 0, size = 0;
                    response.setContentType(resource.getContentType());

                    for (Map.Entry<String, String> cur :
                         resource.getResponseHeaders().entrySet()) {
                        response.setHeader(cur.getKey(), cur.getValue());
                    }
                    while (-1 != (thisRead = resourceChannel.read(buf))) {
                        buf.rewind();
                        buf.limit(thisRead);
                        do {
                            totalWritten += out.write(buf);
                        } while (totalWritten < size);
                        buf.clear();
                        size += thisRead;
                    }
                    response.setContentLength(size);
                    resourceChannel.close();
                    out.close();

                } catch (IOException ioe) {
                    response.setStatus(404);
                    String message;
                    if (null != resource.getLibraryName()) {
                        message = "Unable to serve resource "
                                  + resource.getResourceName()
                                  +
                                  " in library "
                                  + resource.getLibraryName();
                    } else {
                        message = "Unable to serve resource " + resource
                              .getResourceName();
                    }
                    LOGGER.log(Level.WARNING, message, ioe);
                }
            } else {
                response.setStatus(304);
            }

        }


    }

    // ------------------------------------------------- Package Private Methods


    long getCreationTime() {

        return creationTime;

    }


    WebConfiguration getWebConfig() {

        return webconfig;
        
    }

    // --------------------------------------------------------- Private Methods


    private String getContentTypeFromResourceName(String resourceName) {

        String res = resourceName;
        if (res.contains("/")) {
            res = res.substring(res.lastIndexOf('/') + 1);
        }
        return mimeTypeMap.getContentType(res);

    }


    private String normalizeViewId(FacesContext context) {

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

    // ----------------------------------------------------------- Inner Classes


}

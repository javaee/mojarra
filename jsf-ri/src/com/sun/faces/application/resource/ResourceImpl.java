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
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import javax.faces.application.Resource;
import javax.faces.context.FacesContext;

import com.sun.faces.config.WebConfiguration;
import com.sun.faces.util.Util;

/**
 * Default implementation of {@link javax.faces.application.Resource}.
 */
public class ResourceImpl extends Resource {

    /* HTTP Date format required by the HTTP/1.1 RFC */
    private static final String RFC1123_DATE_PATTERN =
          "EEE, dd MMM yyyy HH:mm:ss zzz";

    private static final TimeZone GMT = TimeZone.getTimeZone("GMT");

    /* The ResourceHandler implementation */
    private ResourceHandlerImpl owner;

    /* The meta data on the resource */
    private ResourceInfo resourceInfo;




    // -------------------------------------------------------- Constructors


    /**
     * Creates a new instance of ResourceBase
     */
    public ResourceImpl(ResourceHandlerImpl owner,
                        ResourceInfo resourceInfo,
                        String contentType) {

        this.owner = owner;
        this.resourceInfo = resourceInfo;
        super.setResourceName(resourceInfo.getName());
        super.setLibraryName(resourceInfo.getLibraryInfo() != null
                             ? resourceInfo.getLibraryInfo().getName()
                             : null);
        super.setContentType(contentType);

    }


    // ------------------------------------------------------ Public Methods


    /**
     * @see javax.faces.application.Resource#getInputStream()
     */
    public InputStream getInputStream() throws IOException {
        FacesContext ctx = FacesContext.getCurrentInstance();
        return resourceInfo.getHelper().getInputStream(resourceInfo, ctx);
    }


    /**
     * @see javax.faces.application.Resource#getURL()
     */
    public URL getURL() {
        FacesContext ctx = FacesContext.getCurrentInstance();
        return resourceInfo.getHelper().getURL(resourceInfo, ctx);
    }


    /**
     * @see javax.faces.application.Resource#getResponseHeaders()
     */
    public Map<String, String> getResponseHeaders() {

        HashMap<String,String> map = new HashMap<String,String>(3, 1.0f);

        long expiresTime = new Date().getTime() +
                  Long.parseLong(owner.getWebConfig().getOptionValue(
                        WebConfiguration.WebContextInitParameter.DefaultResourceMaxAge));
        SimpleDateFormat format = new SimpleDateFormat(RFC1123_DATE_PATTERN, Locale.US);
        format.setTimeZone(GMT);
        map.put("Expires", format.format(new Date(expiresTime)));

        URL url = resourceInfo.getHelper().getURL(resourceInfo, FacesContext.getCurrentInstance());
        try {
            URLConnection conn = url.openConnection();
            long lastModified = conn.getLastModified();
            long contentLength = conn.getContentLength();
            if (lastModified == 0) {
                lastModified = owner.getCreationTime();
            }
            map.put("Last-Modified", format.format(new Date(lastModified)));
            if (lastModified != 0 && contentLength != -1) {
                map.put("ETag", "W/\"" + contentLength + '-' + lastModified + '"');
            }
        } catch (IOException ignored) { }

        return map;
    }


    /**
     * @see javax.faces.application.Resource#getURI()
     */
    public String getURI() {

        String uri;
        FacesContext context = FacesContext.getCurrentInstance();
        String facesServletMapping = Util.getFacesMapping(context);
        // If it is extension mapped
        if (Util.isPrefixMapped(facesServletMapping)) {
            uri = facesServletMapping + "/javax.faces.resource/" +
                  getResourceName();
        } else {
            uri = "/javax.faces.resource/" + getResourceName() +
                  facesServletMapping;
        }
        boolean queryStarted = false;
        if (null != getLibraryName()) {
            queryStarted = true;
            uri += "?ln=" + getLibraryName();
        }
        String version = "";
        if (resourceInfo.getLibraryInfo() != null && resourceInfo.getLibraryInfo().getVersion() != null) {
            version += resourceInfo.getLibraryInfo().getVersion();
        }
        if (resourceInfo.getVersion() != null) {
            version += resourceInfo.getVersion();
        }
        if (version.length() > 0) {
            uri += ((queryStarted) ? "&v=" : "?v=") + version;
        }
        uri = context.getApplication().getViewHandler()
              .getResourceURL(context,
                              uri);

        return uri;

    }


    /**
     * @see javax.faces.application.Resource#userAgentNeedsUpdate(javax.faces.context.FacesContext)
     */
    public boolean userAgentNeedsUpdate(FacesContext context) {

        Map<String,String> requestHeaders =
              context.getExternalContext().getRequestHeaderMap();
        return ((!requestHeaders.containsKey("If-Modified-Since"))
                || (resourceInfo.getHelper()
                      .getLastModified(resourceInfo, context) > owner
                      .getCreationTime()));

    }


}

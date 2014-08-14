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

import java.io.IOException;
import java.io.InputStream;
import java.io.Externalizable;
import java.io.ObjectOutput;
import java.io.ObjectInput;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.Collections;
import java.util.logging.Logger;

import javax.faces.application.Resource;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import com.sun.faces.util.Util;
import com.sun.faces.application.ApplicationAssociate;
import com.sun.faces.util.FacesLogger;
import java.util.logging.Level;

import javax.faces.application.ResourceHandler;
import javax.faces.application.ProjectStage;
import javax.servlet.http.HttpServletRequest;

/**
 * Default implementation of {@link javax.faces.application.Resource}.
 * The ResourceImpl instance itself has the same lifespan as the
 * request, however, the ResourceInfo instances that back this object
 * are cached by the ResourceManager to reduce the time spent scanning
 * for resources.
 */
public class ResourceImpl extends Resource implements Externalizable {

    // Log instance for this class
    private static final Logger LOGGER = FacesLogger.APPLICATION.getLogger();

    /* HTTP Date format required by the HTTP/1.1 RFC */
    private static final String RFC1123_DATE_PATTERN =
          "EEE, dd MMM yyyy HH:mm:ss zzz";

    private static final String IF_MODIFIED_SINCE = "If-Modified-Since";

    private static final TimeZone GMT = TimeZone.getTimeZone("GMT");


    /* The meta data on the resource */
    private transient ResourceInfo resourceInfo;

    /*
     * Response headers that need to be added by the ResourceManager
     * implementation.
     */
    private transient Map<String,String> responseHeaders;

    
    /**
     * Time when this application was started.  This is used to generate
     * expiration headers.
     */
    private long initialTime;


    /**
     * Lifespan of this resource for caching purposes.
     */
    private long maxAge;


    // ------------------------------------------------------------ Constructors


    /**
     * Necessary for serialization.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public ResourceImpl() { }


    /**
     * Creates a new instance of ResourceBase
     */
    public ResourceImpl(ResourceInfo resourceInfo,
                        String contentType,
                        long initialTime,
                        long maxAge) {

        this.resourceInfo = resourceInfo;
        super.setResourceName(resourceInfo.getName());
        super.setLibraryName(resourceInfo.getLibraryInfo() != null
                             ? resourceInfo.getLibraryInfo().getName()
                             : null);
        super.setContentType(contentType);
        this.initialTime = initialTime;
        this.maxAge = maxAge;

    }

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ResourceImpl resource = (ResourceImpl) o;

        return resourceInfo.equals(resource.resourceInfo);

    }

    @Override
    public int hashCode() {

        return resourceInfo.hashCode();

    }


    // --------------------------------------------------- Methods from Resource


    /**
     * @see javax.faces.application.Resource#getInputStream()
     */
    public InputStream getInputStream() throws IOException {
		initResourceInfo();
        return resourceInfo.getHelper().getInputStream(resourceInfo,
                                                       FacesContext.getCurrentInstance());

    }
    


    /**
     * @see javax.faces.application.Resource#getURL()
     */
    public URL getURL() {
        FacesContext ctx = FacesContext.getCurrentInstance();
        return resourceInfo.getHelper().getURL(resourceInfo, ctx);
    }


    /**
     * <p>
     * Implementation note.  Any values added to getResponseHeaders()
     * will only be visible across multiple calls to this method when
     * servicing a resource request (i.e. {@link ResourceHandler#isResourceRequest(javax.faces.context.FacesContext)}
     * returns <code>true</code>).  If we're not servicing a resource request,
     * an empty Map will be returned and the values added are effectively thrown
     * away.
     * </p>
     * 
     * @see javax.faces.application.Resource#getResponseHeaders()
     */
    public Map<String, String> getResponseHeaders() {

        if (isResourceRequest()) {
            if (responseHeaders == null)
            responseHeaders = new HashMap<String, String>(6, 1.0f);

            long expiresTime;
            FacesContext ctx = FacesContext.getCurrentInstance();

            if (ctx.isProjectStage(ProjectStage.Development)) {
                expiresTime = new Date().getTime();
            } else {
                expiresTime = new Date().getTime() + maxAge;
            }
            SimpleDateFormat format =
                  new SimpleDateFormat(RFC1123_DATE_PATTERN, Locale.US);
            format.setTimeZone(GMT);
            responseHeaders.put("Expires", format.format(new Date(expiresTime)));

            URL url = getURL();
            InputStream in = null;
            try {
                URLConnection conn = url.openConnection();
                conn.setUseCaches(false);
                conn.connect();
                in = conn.getInputStream();
                long lastModified = Util.getLastModified(url);
                long contentLength = conn.getContentLength();
                if (lastModified == 0) {
                    lastModified = initialTime;
                }
                responseHeaders.put("Last-Modified", format.format(new Date(lastModified)));
                if (lastModified != 0 && contentLength != -1) {
                    responseHeaders.put("ETag", "W/\""
                                    + contentLength
                                    + '-'
                                    + lastModified
                                    + '"');
                }
            } catch (IOException ioe) { 
                if (LOGGER.isLoggable(Level.FINEST)) {
                    LOGGER.log(Level.FINEST, "Closing stream", ioe);
                }
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException ioe) { 
                        if (LOGGER.isLoggable(Level.FINEST)) {
                            LOGGER.log(Level.FINEST, "Closing stream", ioe);
                        }
                    }
                }
            }
            return responseHeaders;
        } else {
            return Collections.emptyMap();
        }

    }


    /**
     * @see javax.faces.application.Resource#getRequestPath()
     */
    public String getRequestPath() {

        String uri;
        FacesContext context = FacesContext.getCurrentInstance();
        String facesServletMapping = Util.getFacesMapping(context);
        // If it is extension mapped
        if (Util.isPrefixMapped(facesServletMapping)) {
            uri = facesServletMapping + ResourceHandler.RESOURCE_IDENTIFIER + '/' +
                  getResourceName();
        } else {
            uri = ResourceHandler.RESOURCE_IDENTIFIER + '/' + getResourceName() +
                  facesServletMapping;
        }
        boolean queryStarted = false;
        if (null != getLibraryName()) {
            queryStarted = true;
            uri += "?ln=" + getLibraryName();
        }
        String version = "";
        initResourceInfo();
        if (resourceInfo.getLibraryInfo() != null && resourceInfo.getLibraryInfo().getVersion() != null) {
            version += resourceInfo.getLibraryInfo().getVersion().toString();
        }
        if (resourceInfo.getVersion() != null) {
            version += resourceInfo.getVersion().toString();
        }
        if (version.length() > 0) {
            uri += ((queryStarted) ? "&v=" : "?v=") + version;
            queryStarted = true;
        }
        String localePrefix = resourceInfo.getLocalePrefix();
        if (localePrefix != null) {
            uri += ((queryStarted) ? "&loc=" : "?loc=") + localePrefix;
            queryStarted = true;
        }
        
        String contract = resourceInfo.getContract();
        if (contract != null) {
            uri += ((queryStarted) ? "&con=" : "?con=") + contract;
            queryStarted = true;
        }
        
        if ("jsf.js".equals(getResourceName()) && "javax.faces".equals(getLibraryName())) {
            ProjectStage stage = context.getApplication().getProjectStage();
            switch (stage) {
                case Development:
                    uri += ((queryStarted) ? "&stage=Development" : "?stage=Development" );
                    break;
                case SystemTest:
                    uri += ((queryStarted) ? "&stage=SystemTest" : "?stage=SystemTest" );
                    break;
                case UnitTest:
                    uri += ((queryStarted) ? "&stage=UnitTest" : "?stage=UnitTest" );
                    break;
                default:
                    assert(stage.equals(ProjectStage.Production));
            }
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
        
        // PENDING(edburns): this is a sub-optimal implementation choice
        // done in the interest of prototyping.  It's never a good idea 
        // to do a switch statement based on the type of an object.
        
        if (resourceInfo instanceof FaceletResourceInfo) {
            return true;
        }

        // http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html
        // 14.25 If-Modified-Since

        // if the requested variant has not been modified since the time
        // specified in this field, an entity will not be returned from the
        // server; instead, a 304 (not modified) response will be returned
        // without any message-body.

        // A date which is later than the server's current time is
        // invalid.

        Map<String,String> requestHeaders =
              context.getExternalContext().getRequestHeaderMap();

        if (requestHeaders.containsKey(IF_MODIFIED_SINCE)) {
            initResourceInfo();
            /*
             * Make sure that we strip the milliseconds out of what comes back
             * from the getLastModified call for a resource as the 
             * 'If-Modified-Since' header does not use milliseconds.
             */
            long lastModifiedOfResource = (((ClientResourceInfo)resourceInfo).getLastModified(context) / 1000) * 1000;
            long lastModifiedHeader = getIfModifiedHeader(context.getExternalContext());
            if (0 == lastModifiedOfResource) {
                long startupTime = ApplicationAssociate.getInstance(context.getExternalContext()).getTimeOfInstantiation();
                return startupTime > lastModifiedHeader;
            } else {
                return lastModifiedOfResource > lastModifiedHeader;
            }
        }
        return true;

    }


    // --------------------------------------------------------- Private Methods


    /*
     * This method should only be called if the 'If-Modified-Since' header
     * is present in the request header map.
     */
    private long getIfModifiedHeader(ExternalContext extcontext) {

        Object request = extcontext.getRequest();
        if (request instanceof HttpServletRequest) {
            // try to use the container where we can.  V3 for instance
            // has a FastHttpDateFormat format/parse implementation
            // which is more than likely more performant than SimpleDateFormat
            // (otherwise, why would it be there?).
            return ((HttpServletRequest) request).getDateHeader(IF_MODIFIED_SINCE);
        } else {
            SimpleDateFormat format =
                  new SimpleDateFormat(RFC1123_DATE_PATTERN, Locale.US);
            try {
                Date ifModifiedSinceDate = format.parse(extcontext.getRequestHeaderMap().get(IF_MODIFIED_SINCE));
                return ifModifiedSinceDate.getTime();
            } catch (ParseException ex) {
                if (LOGGER.isLoggable(Level.WARNING)) {
                    LOGGER.log(Level.WARNING,
                               "jsf.application.resource.invalid_if_modified_since_header",
                               new Object[]{
                                     extcontext.getRequestHeaderMap().get(IF_MODIFIED_SINCE)
                               });
                    if (ex != null) {
                        LOGGER.log(Level.WARNING, "", ex);
                    }
                }
                return -1;
            }
        }

    }


    // --------------------------------------------- Methods from Externalizable


    public void writeExternal(ObjectOutput out) throws IOException {

        out.writeObject(getResourceName());
        out.writeObject(getLibraryName());
        out.writeObject(getContentType());
        out.writeLong(initialTime);
        out.writeLong(maxAge);

    }

    public void readExternal(ObjectInput in)
    throws IOException, ClassNotFoundException {

        setResourceName((String) in.readObject());
        setLibraryName((String) in.readObject());
        setContentType((String) in.readObject());
        initialTime = in.readLong();
        maxAge = in.readLong();
    }

    private void initResourceInfo(){
        if(resourceInfo!=null){
            return;
        }
        ResourceManager manager =
                ApplicationAssociate.getInstance(FacesContext.getCurrentInstance().getExternalContext()).getResourceManager();
        resourceInfo = manager.findResource(getLibraryName(),
                getResourceName(),
                getContentType(),
                FacesContext.getCurrentInstance());
    }
	
    // --------------------------------------------------------- Private Methods


    private boolean isResourceRequest() {

        FacesContext ctx = FacesContext.getCurrentInstance();
        return (ctx.getApplication().getResourceHandler().isResourceRequest(ctx));

    }

}

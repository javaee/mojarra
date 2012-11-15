/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2012 Oracle and/or its affiliates. All rights reserved.
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
 *
 *
 * This file incorporates work covered by the following copyright and
 * permission notice:
 *
 * Copyright 2005-2007 The Apache Software Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sun.faces.facelets.impl;

import com.sun.faces.RIConstants;
import com.sun.faces.context.FacesFileNotFoundException;
import java.net.MalformedURLException;
import javax.faces.component.UIComponent;
import javax.faces.view.facelets.Facelet;
import javax.faces.view.facelets.FaceletCache;
import javax.faces.view.facelets.FaceletFactory;
import com.sun.faces.facelets.compiler.Compiler;
import com.sun.faces.util.Cache;
import com.sun.faces.util.FacesLogger;
import com.sun.faces.util.Util;

import java.io.ByteArrayInputStream;
import java.io.File;
import javax.faces.view.facelets.FaceletHandler;
import javax.faces.view.facelets.ResourceResolver;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URL;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.faces.FacesException;
import javax.faces.application.Application;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;


/**
 * Default FaceletFactory implementation.
 *
 * @author Jacob Hookom
 * @version $Id: DefaultFaceletFactory.java,v 1.10 2007/04/09 01:13:17 youngm
 *          Exp $
 */
public class DefaultFaceletFactory extends FaceletFactory {

    protected final static Logger log = FacesLogger.FACELETS_FACTORY.getLogger();

    private Compiler compiler;

    private Map<String, URL> relativeLocations;

    // We continue to use a ResourceResolver just in case someone
    // provides a custom one.  The DefaultResourceResolver simply uses
    // the ResourceHandler to do its work.
    private ResourceResolver resolver;

    private  URL baseUrl;
    
    private long refreshPeriod;

    private FaceletCache<DefaultFacelet> cache;

    Cache<String,IdMapper> idMappers;
    


    // ------------------------------------------------------------ Constructors

    public DefaultFaceletFactory() {
        this.compiler = null;
        this.resolver = null;
        this.refreshPeriod = -1;
        this.cache = null;
        this.baseUrl = null;
    }
    
    
    public DefaultFaceletFactory(Compiler compiler, ResourceResolver resolver)
    throws IOException {

        this(compiler, resolver, -1, null);

    }


    public DefaultFaceletFactory(Compiler compiler,
                                 ResourceResolver resolver,
                                 long refreshPeriod) {
        this(compiler, resolver, refreshPeriod, null);
    }

    public DefaultFaceletFactory(Compiler compiler,
                                 ResourceResolver resolver,
                                 long refreshPeriod,
                                 FaceletCache cache) {
        this.init(compiler, resolver, refreshPeriod, cache);
    }
    
    public final void init(Compiler compiler,
            ResourceResolver resolver,
            long refreshPeriod,
            FaceletCache cache) {
        Util.notNull("compiler", compiler);
        Util.notNull("resolver", resolver);
        this.compiler = compiler;
        this.relativeLocations = new ConcurrentHashMap<String, URL>();
        this.resolver = resolver;
        this.baseUrl = resolver.resolveUrl("/");
        this.idMappers = new Cache<String,IdMapper>(new IdMapperFactory());
        // this.location = url;
        refreshPeriod = (refreshPeriod >= 0) ? refreshPeriod * 1000 : -1;
        this.refreshPeriod = refreshPeriod;
        if (log.isLoggable(Level.FINE)) {
            log.log(Level.FINE, "Using ResourceResolver: {0}", resolver);
            log.log(Level.FINE, "Using Refresh Period: {0}", refreshPeriod);
        }
        
        // We can cast to the FaceletCache<DefaultFacelet> here because we know
        // that the Generics information is only used at compile time, and all cache
        // implementations will be using instance factories provided by us and returning DefaultFacelet
        this.cache = (FaceletCache<DefaultFacelet>)cache;
        
        // Create instance factories for the  cache, so that the cache can
        // create Facelets and Metadata Facelets
        FaceletCache.MemberFactory<DefaultFacelet> faceletFactory =
            new FaceletCache.MemberFactory<DefaultFacelet>() {
                public DefaultFacelet newInstance(final URL key) throws IOException {
                    return createFacelet(key);
                }
            };
        FaceletCache.MemberFactory<DefaultFacelet> metadataFaceletFactory =
            new FaceletCache.MemberFactory<DefaultFacelet>() {
                public DefaultFacelet newInstance(final URL key) throws IOException {
                    return createMetadataFacelet(key);
                }
            };
        try {
            // We must call this method using reflection because it is protected.
            Method m = FaceletCache.class.getDeclaredMethod("setMemberFactories", FaceletCache.MemberFactory.class, FaceletCache.MemberFactory.class);
            m.setAccessible(true);
            m.invoke(this.cache, faceletFactory, metadataFaceletFactory);
        } catch (Exception ex) {
            if (log.isLoggable(Level.SEVERE)) {
                log.log(Level.SEVERE, null, ex);
            }
            throw new FacesException(ex);
        } 
        
    }

    /*
      * (non-Javadoc)
      *
      * @see com.sun.facelets.FaceletFactory#getResourceResolver
      */
    public ResourceResolver getResourceResolver() {
        return resolver;
    }

    
    /*
      * (non-Javadoc)
      *
      * @see com.sun.facelets.FaceletFactory#getFacelet(java.lang.String)
      */
    @Override
    public Facelet getFacelet(FacesContext context, String uri) throws IOException {

        return this.getFacelet(context, resolveURL(uri));

    }


    @Override
    public Facelet getMetadataFacelet(FacesContext context, String uri) throws IOException {

        return this.getMetadataFacelet(context, resolveURL(uri));

    }


    /**
     * Resolves a path based on the passed URL. If the path starts with '/', then
     * resolve the path against {@link javax.faces.context.ExternalContext#getResource(java.lang.String)
     * javax.faces.context.ExternalContext#getResource(java.lang.String)}.
     * Otherwise create a new URL via {@link URL#URL(java.net.URL,
     * java.lang.String) URL(URL, String)}.
     *
     * @param source base to resolve from
     * @param path   relative path to the source
     *
     * @return resolved URL
     *
     * @throws IOException
     */
    public URL resolveURL(URL source, String path) throws IOException {
        if (path.startsWith("/")) {
            URL url = this.resolver.resolveUrl(path);
            if (url == null) {
                throw new FacesFileNotFoundException(path
                                                + " Not Found in ExternalContext as a Resource");
            }
            return url;
        } else {
            return new URL(source, path);
        }
    }

    /**
     * Create a Facelet from the passed URL. This method checks if the cached
     * Facelet needs to be refreshed before returning. If so, uses the passed URL
     * to build a new instance;
     *
     * @param url source url
     *
     * @return Facelet instance
     *
     * @throws IOException
     * @throws FaceletException
     * @throws FacesException
     * @throws ELException
     */
    @Override
    public Facelet getFacelet(FacesContext context, URL url) throws IOException {
        return this.cache.getFacelet(url);
    }

    @Override
    public Facelet getMetadataFacelet(FacesContext context, URL url) throws IOException {
        return this.cache.getViewMetadataFacelet(url);
    }

    public boolean needsToBeRefreshed(URL url) {
        return !this.cache.isFaceletCached(url);
    }

    

    private URL resolveURL(String uri) throws IOException {

        URL url = this.relativeLocations.get(uri);
        if (url == null) {
            url = this.resolveURL(this.baseUrl, uri);
            if (url != null) {
                this.relativeLocations.put(uri, url);
            } else {
                throw new IOException("'" + uri + "' not found.");
            }
        }
        return url;

    }

    @Override
    public UIComponent createComponent(FacesContext context, String taglibURI, String tagName, 
    Map<String, Object> attributes) {
        UIComponent result = null;
        Application app = context.getApplication();
        ExternalContext extContext = context.getExternalContext();
        File tmpDir = (File) extContext.getApplicationMap().get("javax.servlet.context.tempdir");
        File tempFile = null;
        OutputStreamWriter osw = null;
        try {
            
            // create a temporary file in that directory
            tempFile = File.createTempFile("mojarra", ".tmp", tmpDir);
            osw = new OutputStreamWriter(new FileOutputStream(tempFile), RIConstants.CHAR_ENCODING);
            osw.append("<?xml version='1.0' encoding='");
            osw.append(RIConstants.CHAR_ENCODING);
            osw.append("' ?>");
            osw.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">");
            osw.append("<html xmlns=\"http://www.w3.org/1999/xhtml\"\n");
            osw.append("      xmlns:j=\"").append(taglibURI).append("\">");
            osw.append("  <j:").append(tagName).append(" ");
            if (!attributes.isEmpty()) {
                for (Map.Entry<String,Object> attr : attributes.entrySet()) {
                    osw.append(attr.getKey()).append("=\"").append(attr.getValue().toString()).append("\"").append(" ");
                }
            }
            String tempId = context.getViewRoot().createUniqueId(context, tagName);
            osw.append(" id=\"").append(tempId).append("\" />");
            osw.append("</html>");
            try {
                osw.flush();
                osw.close();
            } catch (IOException ex) {
            }
                  
            URL fabricatedFaceletPage = tempFile.toURI().toURL();
            Facelet f = createFacelet(fabricatedFaceletPage);
            UIComponent tmp = (UIComponent)
                    app.createComponent("javax.faces.NamingContainer");
            tmp.setId(context.getViewRoot().createUniqueId());
            f.apply(context, tmp);
                result = tmp.findComponent(tempId);
            tmp.getChildren().clear();
            osw = null;
            
        } catch (MalformedURLException ex) {
            
        } catch (IOException ioe) {
            
        } finally {
            if (null != osw) {
                try {
                    osw.close();
                } catch (IOException ex) {
                }
            }
            if (null != tempFile) {
//                boolean successful = 
                    tempFile.delete();
//                if (!successful) {
//                    
//                }
            }
        }
        
        try {
            byte [] faceletPage = "facelet".getBytes(RIConstants.CHAR_ENCODING);
            ByteArrayInputStream bais = new ByteArrayInputStream(faceletPage);
        } catch (UnsupportedEncodingException uee) {
            if (log.isLoggable(Level.SEVERE)) {
                log.log(Level.SEVERE, "Unsupported encoding when creating component for " + tagName + " in " + taglibURI,
                        uee);
            }
        }
               
        result.setId(null);        
        return result;
    }
    
    


    /**
     * Uses the internal Compiler reference to build a Facelet given the passed
     * URL.
     *
     * @param url source
     *
     * @return a Facelet instance
     *
     * @throws IOException
     * @throws FaceletException
     * @throws FacesException
     * @throws ELException
     */
    private DefaultFacelet createFacelet(URL url) throws IOException {
        if (log.isLoggable(Level.FINE)) {
            log.fine("Creating Facelet for: " + url);
        }
        String escapedBaseURL = Pattern.quote(this.baseUrl.getFile());
        String alias = '/' + url.getFile().replaceFirst(escapedBaseURL, "");
        try {
            FaceletHandler h = this.compiler.compile(url, alias);
            return new DefaultFacelet(this,
                                      this.compiler.createExpressionFactory(),
                                      url,
                                      alias,
                                      h);
        } catch (FileNotFoundException fnfe) {
            throw new FileNotFoundException("Facelet "
                                            + alias
                                            + " not found at: "
                                            + url.toExternalForm());
        }
    }

    private DefaultFacelet createMetadataFacelet(URL url) throws IOException {

        if (log.isLoggable(Level.FINE)) {
            log.fine("Creating Metadata Facelet for: " + url);
        }
        String escapedBaseURL = Pattern.quote(this.baseUrl.getFile());
        String alias = '/' + url.getFile().replaceFirst(escapedBaseURL, "");
        try {
            FaceletHandler h = this.compiler.metadataCompile(url, alias);
            return new DefaultFacelet(this,
                                      this.compiler.createExpressionFactory(),
                                      url,
                                      alias,
                                      h);
        } catch (FileNotFoundException fnfe) {
            throw new FileNotFoundException("Facelet "
                                            + alias
                                            + " not found at: "
                                            + url.toExternalForm());
        }

    }


    public long getRefreshPeriod() {
        return this.refreshPeriod;
    }


    // ---------------------------------------------------------- Nested Classes


    private static final class IdMapperFactory implements Cache.Factory<String,IdMapper> {


        // ------------------------------------------ Methods from Cache.Factory


        public IdMapper newInstance(String arg) throws InterruptedException {

            return new IdMapper();

        }

    }    
    
}

/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2017 Oracle and/or its affiliates. All rights reserved.
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

import static com.sun.faces.RIConstants.FLOW_IN_JAR_PREFIX;
import static com.sun.faces.config.WebConfiguration.META_INF_CONTRACTS_DIR;
import static com.sun.faces.config.WebConfiguration.WebContextInitParameter.FaceletsSuffix;
import static com.sun.faces.config.WebConfiguration.WebContextInitParameter.WebAppContractsDirectory;
import static java.util.Spliterator.DISTINCT;
import static java.util.Spliterators.spliteratorUnknownSize;
import static java.util.stream.StreamSupport.stream;
import static javax.faces.application.ResourceVisitOption.TOP_LEVEL_VIEWS_ONLY;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

import javax.faces.FacesException;
import javax.faces.application.ResourceVisitOption;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.flow.Flow;

import com.sun.faces.application.ApplicationAssociate;
import com.sun.faces.config.WebConfiguration;
import com.sun.faces.util.Util;

public class FaceletWebappResourceHelper extends ResourceHelper {
    
    private static final String[] RESTRICTED_DIRECTORIES = { "/WEB-INF/", "/META-INF/" };
    
    private final String webAppContractsDirectory;
    private final String[] configuredExtensions;

    public FaceletWebappResourceHelper() {
        WebConfiguration webConfig = WebConfiguration.getInstance();
        webAppContractsDirectory = webConfig.getOptionValue(WebAppContractsDirectory);
        configuredExtensions = webConfig.getOptionValue(FaceletsSuffix, " ");
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof FaceletWebappResourceHelper;
    }

    @Override
    public int hashCode() {
        return 3;
    }

    @Override
    public LibraryInfo findLibrary(String libraryName, String localePrefix, String contract, FacesContext ctx) {
        // FCAPUTO libraries are handled by WebappResourceHelper
        return null;
    }

    @Override
    public ResourceInfo findResource(LibraryInfo library, String resourceName, String localePrefix, boolean compressable, FacesContext ctx) {
        if (localePrefix != null) {
            // FCAPUTO localized facelets are not yet allowed
            return null;
        }
        
        FaceletResourceInfo result = null;
        try {
            
            List<String> contracts = ctx.getResourceLibraryContracts();
            ContractInfo [] outContract = new ContractInfo[1];
            boolean[] outDoNotCache = new boolean[1];

            URL url = null;
            
            // if the library is not null, we must not consider contracts here!
            if (library == null && !contracts.isEmpty()) {
                url = findResourceInfoConsideringContracts(ctx, resourceName, outContract, contracts);
            }
            
            if (url == null) {
                url = Resource.getResourceUrl(ctx, createPath(library, resourceName));
            }
            
            if (url == null) {
                url = findResourceUrlConsideringFlows(resourceName, outDoNotCache);
            }
            
            if (url != null) {
                result = new FaceletResourceInfo(outContract[0], resourceName, null, this, url);
                result.setDoNotCache(outDoNotCache[0]);
            }
        } catch (IOException ex) {
            throw new FacesException(ex);
        } 
        
        return result;
    }
 
    public Stream<String> getViewResources(FacesContext facesContext, String path, int maxDepth, ResourceVisitOption... options) {
        return stream(
            spliteratorUnknownSize(
                new ResourcePathsIterator(path, maxDepth, configuredExtensions, getRestrictedDirectories(options), facesContext.getExternalContext()), 
                DISTINCT
            ), 
            false
        );
    }
    
    private static String[] getRestrictedDirectories(final ResourceVisitOption... options) {
        for (ResourceVisitOption option : options) {
            if (option == TOP_LEVEL_VIEWS_ONLY) {
                return RESTRICTED_DIRECTORIES;
            }
        }
        
        return null;
    }
    
    private String createPath(LibraryInfo library, String resourceName) {
        String path = resourceName;
        if (library != null) {
            path = library.getPath() + "/" + resourceName;
        } else {
            // prepend the leading '/' if necessary.
            if (path.charAt(0) != '/') {
                path = "/" + path;
            }
        }
        
        return path;
    }
    
    private URL findResourceInfoConsideringContracts(FacesContext ctx, String baseResourceName, ContractInfo [] outContract, List<String> contracts) throws MalformedURLException {
        URL url = null;
        String resourceName;
        
        for (String contract : contracts) {
            if (baseResourceName.startsWith("/")) {
                resourceName = webAppContractsDirectory + "/" + contract + baseResourceName;
            } else {
                resourceName = webAppContractsDirectory + "/" + contract + "/" + baseResourceName;
            }
            
            url = Resource.getResourceUrl(ctx, resourceName);
            
            if (url != null) {
                outContract[0] = new ContractInfo(contract);
                break;
            } else {
                if (baseResourceName.startsWith("/")) {
                    resourceName = META_INF_CONTRACTS_DIR + "/" + contract + baseResourceName;
                } else {
                    resourceName = META_INF_CONTRACTS_DIR + "/" + contract + "/" + baseResourceName;
                }
                url = Util.getCurrentLoader(this).getResource(resourceName);
                if (url != null) {
                    outContract[0] = new ContractInfo(contract);
                    break;
                }                
            }
            
        }
        
        return url;
    }
    
    private URL findResourceUrlConsideringFlows(String resourceName, boolean[] outDoNotCache) throws IOException {
        
        URL url = null;
        
        ClassLoader cl = Util.getCurrentLoader(this);
        Enumeration<URL> matches = cl.getResources(FLOW_IN_JAR_PREFIX + resourceName);
        try {
            url = matches.nextElement();
        } catch (NoSuchElementException nsee) {
            url = null;
        }
        
        if (url != null && matches.hasMoreElements()) {
            boolean keepGoing = true;
            FacesContext context = FacesContext.getCurrentInstance();
            Flow currentFlow = context.getApplication().getFlowHandler().getCurrentFlow(context);

            do {
                if (currentFlow != null && 0 < currentFlow.getDefiningDocumentId().length()) {
                    String definingDocumentId = currentFlow.getDefiningDocumentId();
                    ExternalContext extContext = context.getExternalContext();
                    ApplicationAssociate associate = ApplicationAssociate.getInstance(extContext);
                    if (associate.urlIsRelatedToDefiningDocumentInJar(url, definingDocumentId)) {
                        keepGoing = false;
                        outDoNotCache[0] = true;
                    } else {
                        if (matches.hasMoreElements()) {
                            url = matches.nextElement();
                        } else {
                            keepGoing = false;
                        }
                    }
                } else {
                    keepGoing = false;
                }
            } while (keepGoing);
        }
        
        return url;
    }
    

    @Override
    public String getBaseResourcePath() {
        return "";
    }

    @Override
    public String getBaseContractsPath() {
        return webAppContractsDirectory;
    }

    @Override
    protected InputStream getNonCompressedInputStream(ResourceInfo info, FacesContext ctx) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public URL getURL(ResourceInfo resource, FacesContext ctx) {
        return ((FaceletResourceInfo)resource).getUrl();
    }
    
}

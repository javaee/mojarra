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

 */
package com.sun.faces.application.resource;

import com.sun.faces.RIConstants;
import com.sun.faces.application.ApplicationAssociate;
import com.sun.faces.config.WebConfiguration;
import com.sun.faces.util.Util;

import javax.faces.FacesException;
import javax.faces.application.Application;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.flow.Flow;
import javax.faces.flow.FlowHandler;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.List;
import java.util.NoSuchElementException;

public class FaceletWebappResourceHelper extends ResourceHelper {
    
    private final String webAppContractsDirectory;
    private static final String META_INF_CONTRACTS_DIR = WebConfiguration.META_INF_CONTRACTS_DIR;

    public FaceletWebappResourceHelper() {
        WebConfiguration webConfig = WebConfiguration.getInstance();
        webAppContractsDirectory = webConfig.getOptionValue(WebConfiguration.WebContextInitParameter.WebAppContractsDirectory);
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
        if(localePrefix != null) {
            // FCAPUTO localized facelets are not yet allowed
            return null;
        }
        FaceletResourceInfo result = null;
        try {
            String path = resourceName;
            if (null != library) {
                path = library.getPath() + "/" + resourceName;
            } else {
                // prepend the leading '/' if necessary.
                if ('/' != path.charAt(0)) {
                    path = "/" + path;
                }
            }
            
            List<String> contracts = ctx.getResourceLibraryContracts();
            ContractInfo [] outContract = new ContractInfo[1];
            outContract[0] = null;
            boolean doNotCache = false;

            URL url = null;
            // if the library is not null, we must not consider contracts here!
            if (library == null && !contracts.isEmpty()) {
                url = findResourceInfoConsideringContracts(ctx, resourceName,
                        outContract,
                        contracts);
            }
            if (null == url) {
                url = Resource.getResourceUrl(ctx, path);
            }
            
            if (null == url) {
                ClassLoader cl = Util.getCurrentLoader(this);
                Enumeration<URL> matches = cl.getResources(RIConstants.FLOW_IN_JAR_PREFIX + resourceName);
                try {
                    url = matches.nextElement();
                } catch (NoSuchElementException nsee) {
                    url = null;
                }
                if (null != url && matches.hasMoreElements()) {
                    boolean keepGoing = true;
                    FacesContext context = FacesContext.getCurrentInstance();
                    Application application = context.getApplication();
                    FlowHandler fh = application.getFlowHandler();
                    Flow currentFlow = fh.getCurrentFlow(context);
                    do {
                        if (null != currentFlow && 0 < currentFlow.getDefiningDocumentId().length()) {
                            String definingDocumentId = currentFlow.getDefiningDocumentId();
                            ExternalContext extContext = context.getExternalContext();
                            ApplicationAssociate associate = ApplicationAssociate.getInstance(extContext);
                            if (associate.urlIsRelatedToDefiningDocumentInJar(url, definingDocumentId)) {
                                keepGoing = false;
                                doNotCache = true;
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
            }
            
            if (null != url) {
                result = new FaceletResourceInfo(outContract[0], resourceName, null, this, url);
                if (doNotCache) {
                    result.setDoNotCache(doNotCache);
                }
            }
        } catch (IOException ex) {
            throw new FacesException(ex);
        } 
        
        return result;
    }
    
    private URL findResourceInfoConsideringContracts(FacesContext ctx,
            String baseResourceName,
            ContractInfo [] outContract,
            List<String> contracts) throws MalformedURLException {
        URL url = null;
        String resourceName;
        
        for (String curContract : contracts) {
            if (baseResourceName.startsWith("/")) {
                resourceName = webAppContractsDirectory + "/" + curContract + baseResourceName;
            } else {
                resourceName = webAppContractsDirectory + "/" + curContract + "/" + baseResourceName;
            }
            url = Resource.getResourceUrl(ctx, resourceName);
            if (null != url) {
                outContract[0] = new ContractInfo(curContract);
                break;
            } else {
                if (baseResourceName.startsWith("/")) {
                    resourceName = META_INF_CONTRACTS_DIR + "/" + curContract + baseResourceName;
                } else {
                    resourceName = META_INF_CONTRACTS_DIR + "/" + curContract + "/" + baseResourceName;
                }
                url = Util.getCurrentLoader(this).getResource(resourceName);
                if (null != url) {
                    outContract[0] = new ContractInfo(curContract);
                    break;
                }                
            }
            
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

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

package com.sun.faces.application.resource;

import com.sun.faces.config.WebConfiguration;
import com.sun.faces.util.FacesLogger;

import javax.faces.FacesException;
import javax.faces.application.ProjectStage;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.sun.faces.config.WebConfiguration.BooleanWebContextInitParameter.CacheResourceModificationTimestamp;
import com.sun.faces.facelets.impl.DefaultResourceResolver;
import javax.faces.view.facelets.ResourceResolver;

/**
 * <p>
 * A {@link ResourceHelper} implementation for finding/serving resources
 * found within <code>&lt;contextroot&gt;/resources</code> directory of a
 * web application.
 * </p>
 *
 * @since 2.0
 */
public class WebappResourceHelper extends ResourceHelper {

    private static final Logger LOGGER = FacesLogger.RESOURCE.getLogger();

    private String BASE_RESOURCE_PATH;
    
    private String BASE_CONTRACTS_PATH;

    private boolean cacheTimestamp;


    // ------------------------------------------------------------ Constructors


    public WebappResourceHelper() {

        WebConfiguration webconfig = WebConfiguration.getInstance();
        cacheTimestamp = webconfig.isOptionEnabled(CacheResourceModificationTimestamp);
        BASE_RESOURCE_PATH = webconfig.getOptionValue(WebConfiguration.WebContextInitParameter.WebAppResourcesDirectory);
        BASE_CONTRACTS_PATH = webconfig.getOptionValue(WebConfiguration.WebContextInitParameter.WebAppContractsDirectory);

    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final WebappResourceHelper other = (WebappResourceHelper) obj;
        if ((this.BASE_RESOURCE_PATH == null) ? (other.BASE_RESOURCE_PATH != null) : !this.BASE_RESOURCE_PATH.equals(other.BASE_RESOURCE_PATH)) {
            return false;
        }
        if ((this.BASE_CONTRACTS_PATH == null) ? (other.BASE_CONTRACTS_PATH != null) : !this.BASE_CONTRACTS_PATH.equals(other.BASE_CONTRACTS_PATH)) {
            return false;
        }
        if (this.cacheTimestamp != other.cacheTimestamp) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + (this.BASE_RESOURCE_PATH != null ? this.BASE_RESOURCE_PATH.hashCode() : 0);
        hash = 37 * hash + (this.BASE_CONTRACTS_PATH != null ? this.BASE_CONTRACTS_PATH.hashCode() : 0);
        hash = 37 * hash + (this.cacheTimestamp ? 1 : 0);
        return hash;
    }

    

    // --------------------------------------------- Methods from ResourceHelper


    /**
     * @see com.sun.faces.application.resource.ResourceHelper#getBaseResourcePath()
     */
    public String getBaseResourcePath() {

        return BASE_RESOURCE_PATH;

    }

    @Override
    public String getBaseContractsPath() {
        return BASE_CONTRACTS_PATH;
    }
    
    /**
     * @see ResourceHelper#getNonCompressedInputStream(com.sun.faces.application.resource.ResourceInfo, javax.faces.context.FacesContext) 
     */
    protected InputStream getNonCompressedInputStream(ResourceInfo resource, FacesContext ctx)
    throws IOException {

        return ctx.getExternalContext().getResourceAsStream(resource.getPath());

    }


    /**
     * @see ResourceHelper#getURL(com.sun.faces.application.resource.ResourceInfo, javax.faces.context.FacesContext) 
     */
    public URL getURL(ResourceInfo resource, FacesContext ctx) {
        ResourceResolver nonDefaultResourceResolver = (ResourceResolver) ctx.getAttributes().get(DefaultResourceResolver.NON_DEFAULT_RESOURCE_RESOLVER_PARAM_NAME);
        String path = resource.getPath();
        if (null != nonDefaultResourceResolver) {
            return nonDefaultResourceResolver.resolveUrl(path);
        }

        try {
            return ctx.getExternalContext().getResource(path);
        } catch (MalformedURLException e) {
            return null;
        }

    }
    
    


    /**
     * @see ResourceHelper#findLibrary(String, String, String, javax.faces.context.FacesContext)
     */
    public LibraryInfo findLibrary(String libraryName,
                                   String localePrefix,
                                   String contract, FacesContext ctx) {

        String path;

        if (localePrefix == null) {
            path = getBasePath(contract) + '/' + libraryName;
        } else {
            path = getBasePath(contract)
                   + '/'
                   + localePrefix
                   + '/'
                   + libraryName;
        }
        Set<String> resourcePaths =
              ctx.getExternalContext().getResourcePaths(path);
        // it could be possible that there exists an empty directory
        // that is representing the library, but if it's empty, treat it
        // as non-existant and return null.
        if (resourcePaths != null && !resourcePaths.isEmpty()) {
            VersionInfo version = getVersion(resourcePaths, false);
                return new LibraryInfo(libraryName, version, localePrefix, contract, this);
        }

        return null;
    }


    /**
     * @see ResourceHelper#findResource(LibraryInfo, String, String, boolean, javax.faces.context.FacesContext)
     */
    public ResourceInfo findResource(LibraryInfo library,
                                     String resourceName,
                                     String localePrefix,
                                     boolean compressable,
                                     FacesContext ctx) {
        
        resourceName = trimLeadingSlash(resourceName);
        ContractInfo [] outContract = new ContractInfo[1];
        outContract[0] = null;
        
        String basePath = findPathConsideringContracts(library, resourceName, 
                localePrefix, outContract, ctx);
        
        if (null == basePath) {
        
            if (library != null) {
                // PENDING(fcaputo) no need to iterate over the contracts, if we have a library
                basePath = library.getPath(localePrefix) + '/' + resourceName;
            } else {
                if (localePrefix == null) {
                    basePath = getBaseResourcePath() + '/' + resourceName;
                } else {
                    basePath = getBaseResourcePath()
                            + '/'
                            + localePrefix
                            + '/'
                            + resourceName;
                }
            }
            
            // first check to see if the resource exists, if not, return null.  Let
            // the caller decide what to do.
            try {
                if (ctx.getExternalContext().getResource(basePath) == null) {
                    return null;
                }
            } catch (MalformedURLException e) {
                throw new FacesException(e);
            }
        }

        // we got to hear, so we know the resource exists (either as a directory
        // or file)
        Set<String> resourcePaths =
              ctx.getExternalContext().getResourcePaths(basePath);
        // if getResourcePaths returns null or an empty set, this means that we have
        // a non-directory resource, therefor, this resource isn't versioned.
        ClientResourceInfo value;
        if (resourcePaths == null || resourcePaths.size() == 0) {
            if (library != null) {
                value = new ClientResourceInfo(library,
                                         outContract[0],
                                         resourceName,
                                         null,
                                         compressable,
                                         resourceSupportsEL(resourceName, library.getName(), ctx),
                                         ctx.isProjectStage(ProjectStage.Development),
                                         cacheTimestamp);
            } else {
                value = new ClientResourceInfo(outContract[0], 
                                         resourceName,
                                         null,
                                         localePrefix,
                                         this,
                                         compressable,
                                         resourceSupportsEL(resourceName, null, ctx),
                                         ctx.isProjectStage(ProjectStage.Development),
                                         cacheTimestamp);
            }
        } else {
            // ok, subdirectories exist, so find the latest 'version' directory
            VersionInfo version = getVersion(resourcePaths, true);
            if (version == null && LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.log(Level.WARNING,
                        "jsf.application.resource.unable_to_determine_resource_version.",
                        resourceName);
            }
            if (library != null) {
                value = new ClientResourceInfo(library,
                                         outContract[0],
                                         resourceName,
                                         version,
                                         compressable,
                                         resourceSupportsEL(resourceName, library.getName(), ctx),
                                         ctx.isProjectStage(ProjectStage.Development),
                                         cacheTimestamp);
            } else {
                value = new ClientResourceInfo(outContract[0], 
                                         resourceName,
                                         version,
                                         localePrefix,
                                         this,
                                         compressable,
                                         resourceSupportsEL(resourceName, null, ctx),
                                         ctx.isProjectStage(ProjectStage.Development),
                                         cacheTimestamp);
            }
        }

        if (value.isCompressable()) {
            value = handleCompression(value);
        }
        return value;

    }

    private String findPathConsideringContracts(LibraryInfo library,
                                     String resourceName,
                                     String localePrefix,
                                     ContractInfo [] outContract,
                                     FacesContext ctx) {
        UIViewRoot root = ctx.getViewRoot();
        List<String> contracts = null;

        if (library != null) {
            if(library.getContract() == null) {
                contracts = Collections.emptyList();
            } else {
                contracts = new ArrayList<String>(1);
                contracts.add(library.getContract());
            }
        } else if (root == null) {
            String contractName = ctx.getExternalContext().getRequestParameterMap()
                  .get("con");
            if (null != contractName && 0 < contractName.length()) {
                contracts = new ArrayList<String>();
                contracts.add(contractName);
            } else {
                return null;
            }
        } else {
        	contracts = ctx.getResourceLibraryContracts();
        }

        String basePath = null;
        
        for (String curContract : contracts) {
        
            if (library != null) {
                // PENDING(fcaputo) no need to iterate over the contracts, if we have a library
                basePath = library.getPath(localePrefix) + '/' + resourceName;
            } else {
                if (localePrefix == null) {
                    basePath = getBaseContractsPath() + '/' + curContract + '/' + resourceName;
                } else {
                    basePath = getBaseContractsPath()
                            + '/' + curContract 
                            + '/'
                            + localePrefix
                            + '/'
                            + resourceName;
                }
            }
            
            try {
                if (ctx.getExternalContext().getResource(basePath) != null) {
                    outContract[0] = new ContractInfo(curContract);
                    break;
                } else {
                    basePath = null;
                }
            } catch (MalformedURLException e) {
                throw new FacesException(e);
            }
        }
            
        return basePath;
    }

}

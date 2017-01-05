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
 * https://glassfish.java.net/public/CDDL+GPL_1_1.html
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

import static com.sun.faces.config.WebConfiguration.META_INF_CONTRACTS_DIR;
import static com.sun.faces.config.WebConfiguration.BooleanWebContextInitParameter.CacheResourceModificationTimestamp;
import static com.sun.faces.config.WebConfiguration.BooleanWebContextInitParameter.EnableMissingResourceLibraryDetection;
import static javax.faces.application.ProjectStage.Development;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.faces.application.ProjectStage;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.view.facelets.ResourceResolver;

import com.sun.faces.config.WebConfiguration;
import com.sun.faces.facelets.impl.DefaultResourceResolver;
import com.sun.faces.util.Util;


/**
 * <p>
 * A {@link ResourceHelper} implementation for finding/serving resources
 * found on the classpath within the <code>META-INF/resources directory.
 * </p>
 *
 * @since 2.0
 */
public class ClasspathResourceHelper extends ResourceHelper {


    private static final String BASE_RESOURCE_PATH = "META-INF/resources";
    private boolean cacheTimestamp;
    private volatile ZipDirectoryEntryScanner libraryScanner;
    private boolean enableMissingResourceLibraryDetection;



    // ------------------------------------------------------------ Constructors


    public ClasspathResourceHelper() {

        WebConfiguration webconfig = WebConfiguration.getInstance();
        cacheTimestamp = webconfig.isOptionEnabled(CacheResourceModificationTimestamp);
        enableMissingResourceLibraryDetection =
                webconfig.isOptionEnabled(EnableMissingResourceLibraryDetection);

    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ClasspathResourceHelper other = (ClasspathResourceHelper) obj;
        if (this.cacheTimestamp != other.cacheTimestamp) {
            return false;
        }
        if (this.enableMissingResourceLibraryDetection != other.enableMissingResourceLibraryDetection) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + (this.cacheTimestamp ? 1 : 0);
        hash = 67 * hash + (this.enableMissingResourceLibraryDetection ? 1 : 0);
        return hash;
    }

    

    // --------------------------------------------- Methods from ResourceHelper


    /**
     * @see com.sun.faces.application.resource.ResourceHelper#getBaseResourcePath()
     */
    @Override
    public String getBaseResourcePath() {
        return BASE_RESOURCE_PATH;
    }

    @Override
    public String getBaseContractsPath() {
        return META_INF_CONTRACTS_DIR;
    }
    
    /**
     * @see ResourceHelper#getNonCompressedInputStream(com.sun.faces.application.resource.ResourceInfo, javax.faces.context.FacesContext)
     */
    @Override
    protected InputStream getNonCompressedInputStream(ResourceInfo resource, FacesContext ctx) throws IOException {

        InputStream in = null;
        
        if (ctx.isProjectStage(Development)) {
            ClassLoader loader = Util.getCurrentLoader(getClass());
            String path = resource.getPath();
            if (loader.getResource(path) != null) {
                in = loader.getResource(path).openStream();
            }
            if (in == null && getClass().getClassLoader().getResource(path) != null) {
                in = getClass().getClassLoader().getResource(path).openStream();
            }
        } else {        
            ClassLoader loader = Util.getCurrentLoader(getClass());
            String path = resource.getPath();
            in = loader.getResourceAsStream(path);
            if (in == null) {
                in = getClass().getClassLoader().getResourceAsStream(path);
            }
        }
        return in;
    }


    /**
     * @see ResourceHelper#getURL(com.sun.faces.application.resource.ResourceInfo, javax.faces.context.FacesContext)
     */
    @Override
    public URL getURL(ResourceInfo resource, FacesContext ctx) {
        ResourceResolver nonDefaultResourceResolver = (ResourceResolver) ctx.getAttributes().get(DefaultResourceResolver.NON_DEFAULT_RESOURCE_RESOLVER_PARAM_NAME);
        String path = resource.getPath();
        URL url = null;
        if (null != nonDefaultResourceResolver) {
            url = nonDefaultResourceResolver.resolveUrl(path);
        }
        if (null == url) {
            ClassLoader loader = Util.getCurrentLoader(this.getClass());
            url = loader.getResource(path);
            if (url == null) {
                // try using this class' loader (necessary when running in OSGi)
                url = this.getClass().getClassLoader().getResource(resource.getPath());
            }
        }
        return url;

    }

    
    /**
     * @see ResourceHelper#findLibrary(String, String, String, javax.faces.context.FacesContext)
     */
    @Override
    public LibraryInfo findLibrary(String libraryName, String localePrefix, String contract, FacesContext ctx) {

        ClassLoader loader = Util.getCurrentLoader(this);
        String basePath;
        if (localePrefix == null) {
            basePath = getBasePath(contract) + '/' + libraryName + '/';
        } else {
            basePath = getBasePath(contract)
                       + '/'
                       + localePrefix
                       + '/'
                       + libraryName
                       + '/';
        }

        URL basePathURL = loader.getResource(basePath);
        if (basePathURL == null) {
            // try using this class' loader (necessary when running in OSGi)
            basePathURL = this.getClass().getClassLoader().getResource(basePath);
            if (basePathURL == null) {
                return null;
            }
        }

        return new LibraryInfo(libraryName, null, localePrefix, contract, this);
        
    }

    public LibraryInfo findLibraryWithZipDirectoryEntryScan(String libraryName,
                                                            String localePrefix,
                                                            String contract, FacesContext ctx, boolean forceScan) {

        ClassLoader loader = Util.getCurrentLoader(this);
        String basePath;
        if (localePrefix == null) {
            basePath = getBasePath(contract) + '/' + libraryName + '/';
        } else {
            basePath = getBasePath(contract)
                       + '/'
                       + localePrefix
                       + '/'
                       + libraryName
                       + '/';
        }

        URL basePathURL = loader.getResource(basePath);
        if (basePathURL == null) {
            // try using this class' loader (necessary when running in OSGi)
            basePathURL = this.getClass().getClassLoader().getResource(basePath);
            if (basePathURL == null) {
                if (null != localePrefix && libraryName.equals("javax.faces")) {
                    return null;
                }
                if (enableMissingResourceLibraryDetection || forceScan) {
                    if (null == libraryScanner) {
                        libraryScanner = new ZipDirectoryEntryScanner();
                    }
                    if (!libraryScanner.libraryExists(libraryName, localePrefix)) {
                        return null;
                    }
                }
            }
        }

        return new LibraryInfo(libraryName, null, localePrefix, contract, this);
    }

    /**
     * @see ResourceHelper#findResource(LibraryInfo, String, String, boolean, javax.faces.context.FacesContext)
     */
    @Override
    public ResourceInfo findResource(LibraryInfo library,
                                     String resourceName,
                                     String localePrefix,
                                     boolean compressable,
                                     FacesContext ctx) {

        resourceName = trimLeadingSlash(resourceName);
        ContractInfo [] outContract = new ContractInfo[1];
        outContract[0] = null;
        String [] outBasePath = new String[1];
        outBasePath[0] = null;
        
        ClassLoader loader = Util.getCurrentLoader(this);
        URL basePathURL = findPathConsideringContracts(loader, library, resourceName, 
                localePrefix, outContract, outBasePath, ctx);
        String basePath = outBasePath[0];
        if (null == basePathURL) {
            basePath = deriveBasePath(library, resourceName, localePrefix);
            basePathURL = loader.getResource(basePath);
        }

        if (null == basePathURL) {
            // try using this class' loader (necessary when running in OSGi)
            basePathURL = this.getClass().getClassLoader().getResource(basePath);
            if (basePathURL == null) {
                // Try it without the localePrefix
                if (library != null) {
                    basePath = library.getPath(null) + '/' + resourceName;
                } else {
                    basePath = getBaseResourcePath() + '/' + resourceName;
                }
                basePathURL = loader.getResource(basePath);
                if (basePathURL == null) {
                    // try using this class' loader (necessary when running in OSGi)
                    basePathURL = this.getClass().getClassLoader().getResource(basePath);
                    if (basePathURL == null) {
                        return null;
                    }
                }

                localePrefix = null;
            }
        }

        ClientResourceInfo value;

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
        
        if (value.isCompressable()) {
            value = handleCompression(value);
        }
        return value;

    }
    
    private String deriveBasePath(LibraryInfo library,
            String resourceName,
            String localePrefix) {
        String basePath = null;
        if (library != null) {
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
        return basePath;
    }

    private URL findPathConsideringContracts(ClassLoader loader, 
                                     LibraryInfo library,
                                     String resourceName,
                                     String localePrefix,
                                     ContractInfo [] outContract,
                                     String [] outBasePath,
                                     FacesContext ctx) {
        UIViewRoot root = ctx.getViewRoot();
        List<String> contracts = null;
        URL result = null;
        
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
                contracts = new ArrayList<>();
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
            
            if (null != (result = loader.getResource(basePath))) {
                outContract[0] = new ContractInfo(curContract);
                outBasePath[0] = basePath;
                break;
            } else {
                basePath = null;
            }
        }
            
        return result;
    }

}

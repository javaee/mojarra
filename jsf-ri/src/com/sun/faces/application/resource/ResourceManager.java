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

import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.context.FacesContext;

import com.sun.faces.util.FacesLogger;
import com.sun.faces.util.Util;

import javax.faces.application.ResourceHandler;

/**
 * This class is used to lookup {@link ResourceInfo} instances
 * and cache any that are successfully looked up to reduce the
 * computational overhead with the scanning/version checking.
 *
 * @since 2.0
 *
 * RELEASE_PENDING (rlubke)
 *                 - implement the ability to add additional ResourceHelpers
 *                 - implement library/resource caching
 *                 - add logging where appropriate
 */
public class ResourceManager {

    private Logger LOGGER = FacesLogger.APPLICATION.getLogger();

    private ResourceHelper webappHelper = WebappResourceHelper.getInstance();
    private ResourceHelper classpathHelper = ClasspathResourceHelper.getInstance();

    private Map<String, LibraryInfo> libraries =
          new ConcurrentHashMap<String, LibraryInfo>();
    private final Lock lock = new ReentrantLock();

    // ------------------------------------------------------ Public Methods


    /**
     * <p>
     * Attempt to lookup a {@link ResourceInfo} based on the specified
     * <code>libraryName<code> and <code>resourceName</code>
     * </p>
     * @param libraryName the name of the library (if any)
     * @param resourceName the name of the resource
     * @param ctx the {@link FacesContext} for the current request
     * @return a {@link ResourceInfo} if a resource if found matching the
     *  provided arguments, otherwise, return <code>null</code>
     */
    public ResourceInfo findResource(String libraryName,
                                     String resourceName,
                                     FacesContext ctx) {

        LibraryInfo library = null;
        String localePrefix = getLocalePrefix(ctx);
        if (libraryName != null) {
            library = findLibrary(libraryName, localePrefix, ctx);
            if (library == null && localePrefix != null) {
                // no localized library found.  Try to find
                // a library that isn't localized.
                library = findLibrary(libraryName, null, ctx);
            }
            if (library == null) {
                return null;
            }
        }

        String resName = trimLeadingSlash(resourceName);
        ResourceInfo info = findResource(library,
                                         resName,
                                         localePrefix,
                                         ctx);
        if (info == null && localePrefix != null) {
            // no localized resource found, try to find a
            // resource that isn't localized
            info = findResource(library, resName, null, ctx);
        }
        return info;

    }


    // ----------------------------------------------------- Private Methods


    /**
     * <p> Attempt to lookup and return a {@link LibraryInfo} based on the
     * specified <code>arguments</code>.
     * <p/>
     * <p> The lookup process will first search the file system of the web
     * application.  If the library is not found, then it processed to
     * searching the classpath.</p>
     * <p/>
     * <p> If a library is found, this method will return a {@link
     * LibraryInfo} instance that contains the name, version, and {@link
     * ResourceHelper}.</p>
     *
     * @param libraryName the library to find
     * @param localePrefix the prefix for the desired locale
     * @param ctx         the {@link FacesContext} for the current request
     *
     * @return the Library instance for the specified library
     */
    private LibraryInfo findLibrary(String libraryName,
                                    String localePrefix,
                                    FacesContext ctx) {

        /*LibraryInfo library = libraries.get(libraryName);
        if (library == null) {
            lock.lock();
            try {
                library = libraries.get(libraryName);
                if (library == null) {
                    library = searchWebapp(libraryName, ctx);
                    if (library != null) {
                        libraries.put(libraryName, library);
                        return library;
                    } else {
                        try {
                            library = searchClasspath(libraryName);
                        } catch (Exception e) {
                            throw new LibraryNotFoundException(libraryName, e);
                        }
                    }
                }
            } finally {
                lock.unlock();
            }
        }*/

        LibraryInfo library = webappHelper.findLibrary(libraryName,
                                                       localePrefix,
                                                       ctx);
        if (library == null) {
            library = classpathHelper.findLibrary(libraryName,
                                                  localePrefix,
                                                  ctx);
        }

        // if not library is found at this point, let the caller deal with it
        return library;
    }


   /**
     * <p> Attempt to lookup and return a {@link ResourceInfo} based on the
     * specified <code>arguments</code>.
     * <p/>
     * <p> The lookup process will first search the file system of the web
     * application.  If the library is not found, then it processed to
     * searching the classpath.</p>
     * <p/>
     * <p> If a library is found, this method will return a {@link
     * LibraryInfo} instance that contains the name, version, and {@link
     * ResourceHelper}.</p>
     *
     * @param library the library the resource should be found in
     * @param resourceName the name of the resource
     * @param localePrefix the prefix for the desired locale
     * @param ctx         the {@link FacesContext} for the current request
     *
     * @return the Library instance for the specified library
     */
    private ResourceInfo findResource(LibraryInfo library,
                                      String resourceName,
                                      String localePrefix,
                                      FacesContext ctx) {
        if (library != null) {
            return library.getHelper().findResource(library,
                                                    resourceName,
                                                    localePrefix,
                                                    ctx);
        } else {
            ResourceInfo resource = webappHelper.findResource(null,
                                                              resourceName,
                                                              localePrefix,
                                                              ctx);
            if (resource == null) {
                resource = classpathHelper.findResource(null,
                                                        resourceName,
                                                        localePrefix,
                                                        ctx);
            }
            return resource;
        }

    }


    /**
     * <p>
     * Obtains the application configured message resources for the current
     * request locale.  If a ResourceBundle is found and contains the key
     * <code>javax.faces.resource.localePrefix</code>, use the value associated
     * with that key as the prefix for locale specific resources.
     * </p>
     *
     * <p>
     * For example, say the request locale is en_US, and
     * <code>javax.faces.resourceLocalePrefix</code> is found with a value of
     * <code>en</code>, a resource path within a web application might look like
     * <code>/resources/en/corp/images/greetings.jpg</code>
     * </p>
     *
     * @param context the {@link FacesContext} for the current request
     * @return the localePrefix based on the current request, or <code>null</code>
     *  if no prefix can be determined
     */
    private String getLocalePrefix(FacesContext context) {

        String localePrefix = null;
        String appBundleName = context.getApplication().getMessageBundle();
        if (null != appBundleName) {
            Locale locale = 
                  context.getApplication().getViewHandler().calculateLocale(context);
                try {
                    ResourceBundle appBundle =
                          ResourceBundle.getBundle(appBundleName,
                                                   locale,
                                                   Util.getCurrentLoader(ResourceManager.class));
                    localePrefix =
                          appBundle
                                .getString(ResourceHandler.LOCALE_PREFIX);
                } catch (MissingResourceException ignored) { }
        }
        return localePrefix;

    }

    private String trimLeadingSlash(String s) {
        if (s.charAt(0) == '/') {
            return s.substring(1);
        } else {
            return s;
        }
    }

}

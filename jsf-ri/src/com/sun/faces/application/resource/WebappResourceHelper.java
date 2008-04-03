package com.sun.faces.application.resource;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.FacesException;
import javax.faces.context.FacesContext;

import com.sun.faces.util.FacesLogger;

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

    private static final WebappResourceHelper INSTANCE = new WebappResourceHelper();
    private static final Logger LOGGER = FacesLogger.APPLICATION.getLogger();

    private static final String BASE_RESOURCE_PATH = "/resources";


    // ------------------------------------------------------------ Constructors


    protected WebappResourceHelper() { }


    // ---------------------------------------------------------- Public Methods


    public static WebappResourceHelper getInstance() {

        return INSTANCE;

    }


    // --------------------------------------------- Methods from ResourceHelper


    /**
     * @see com.sun.faces.application.resource.ResourceHelper#getBaseResourcePath()
     */
    public String getBaseResourcePath() {

        return BASE_RESOURCE_PATH;

    }


    /**
     * @see ResourceHelper#getInputStream(ResourceInfo,javax.faces.context.FacesContext)
     */
    protected InputStream getNonCompressedInputStream(ResourceInfo resource, FacesContext ctx)
    throws IOException {

        return ctx.getExternalContext().getResourceAsStream(resource.getPath());

    }


    /**
     * @see ResourceHelper#getURL(ResourceInfo, javax.faces.context.FacesContext)
     */
    public URL getURL(ResourceInfo resource, FacesContext ctx) {

        try {
            return ctx.getExternalContext().getResource(resource.getPath());
        } catch (MalformedURLException e) {
            return null;
        }

    }


    /**
     * @see ResourceHelper#findLibrary(String, String, javax.faces.context.FacesContext)
     */
    public LibraryInfo findLibrary(String libraryName,
                                   String localePrefix,
                                   FacesContext ctx) {

        String path;
        if (localePrefix == null) {
            path = getBaseResourcePath() + '/' + libraryName;
        } else {
            path = getBaseResourcePath()
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
                return new LibraryInfo(libraryName, version, localePrefix, this);
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

        String basePath;
        if (library != null) {
            basePath = library.getPath() + '/' + resourceName;
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

        // we got to hear, so we know the resource exists (either as a directory
        // or file)
        Set<String> resourcePaths =
              ctx.getExternalContext().getResourcePaths(basePath);
        // if getResourcePaths returns null or an empty set, this means that we have
        // a non-directory resource, therefor, this resource isn't versioned.
        ResourceInfo value;
        if (resourcePaths == null || resourcePaths.size() == 0) {
            if (library != null) {
                value = new ResourceInfo(library,
                                         resourceName,
                                         null,
                                         compressable);
            } else {
                value = new ResourceInfo(resourceName,
                                         null,
                                         localePrefix,
                                         this,
                                         compressable);
            }
        } else {
            // ok, subdirectories exist, so find the latest 'version' directory
            VersionInfo version = getVersion(resourcePaths, true);
            if (version == null) {
                if (LOGGER.isLoggable(Level.WARNING)) {
                    LOGGER.log(Level.WARNING,
                               "jsf.application.resource.unable_to_determine_resource_version.",
                               resourceName);
                    return null;
                }
            }
            if (library != null) {
                value = new ResourceInfo(library,
                                         resourceName,
                                         version,
                                         compressable);
            } else {
                value = new ResourceInfo(resourceName,
                                         version,
                                         localePrefix,
                                         this,
                                         compressable);
            }
        }

        if (value.isCompressable()) {
            value = handleCompression(value);
        }
        return value;

    }


}

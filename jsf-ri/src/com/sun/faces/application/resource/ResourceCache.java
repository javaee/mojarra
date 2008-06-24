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
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ScheduledFuture;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContext;

import com.sun.faces.config.WebConfiguration;
import com.sun.faces.config.WebConfiguration.WebContextInitParameter;
import com.sun.faces.util.FacesLogger;
import com.sun.faces.util.Util;
import com.sun.faces.application.ApplicationAssociate;

/**
 * <p>
 * This is the caching mechanism for caching ResourceInfo instances to
 * offset the cost of looking up the resource.
 * </p>
 *
 * <p>
 * This cache uses a background thread to check for modifications to the underlying
 * webapp or JAR files containing resources.  This check is periodic, configurable
 * via context init param <code>com.sun.faces.resourceUpdateCheckPeriod</code>.  Through
 * this config option, the cache can also be made static or completely disabled.
 * If the value of of this option is <code>0</code>, then no check will be made
 * making the cache static.  If value of this option is <code>less than 0</code>,
 * then no caching will be perfomed.  Otherwise, the value of the option will
 * be the number of minutes between modification checks.
 * </p>
 */
public class ResourceCache {

    private static final Logger LOGGER = FacesLogger.RESOURCE.getLogger();

    /**
     * Thread pool size for the case where JSF is installed as a server-wide
     * library.
     */
    private static final int SHARED_THREAD_COUNT = 5;

    /**
     * Thread pool size for the case where JSF is installed as a library of
     * a web-application.
     */
    private static final int NON_SHARED_THREAD_COUNT = 1;

    /**
     * Shared {@link ScheduledThreadPoolExecutor} service.
     */
    private static ScheduledThreadPoolExecutor service;

    /**
     * The <code>ResourceInfo<code> cache.
     */
    private ConcurrentMap<CompositeKey,ResourceInfo> resourceCache;

    /**
     * Monitors that detect resource modifications.
     */
    private List<ResourceMonitor> monitors;

    /**
     * The MonitorTask for this instance.
     */
    private ScheduledFuture monitorTask;

    /**
     * ServletContext name.
     */
    private String contextName;

    /**
     * Has this cache been shutdown.
     */
    private boolean shutdown;


    // ------------------------------------------------------------ Constructors


    /**
     * Constructs a new ResourceCache.
     */
    public ResourceCache() {

        WebConfiguration config = WebConfiguration.getInstance();
        assert (config != null);
        ServletContext sc = config.getServletContext();
        contextName = sc.getContextPath();
        shutdown = false;
        int checkPeriod = getCheckPeriod(config);
        if (checkPeriod >= 0) {
            resourceCache = new ConcurrentHashMap<CompositeKey,ResourceInfo>();
        }
        if (checkPeriod >= 1) {
            initExecutor((checkPeriod * 1000 * 60));
            initMonitors(sc);
        }
        ApplicationAssociate associate = ApplicationAssociate.getInstance(sc);
        associate.setResourceCache(this);

    }


    // ---------------------------------------------------------- Public Methods


    /**
     * Add the {@link ResourceInfo} to the internal cache.
     * @param key resource key
     * @param info resource metadata
     */
    public ResourceInfo add(CompositeKey key, ResourceInfo info) {

        if (shutdown) {
            throw new IllegalStateException("ResourceCache has been terminated");
        }

        Util.notNull("key", key);
        Util.notNull("info", info);

        if (resourceCache != null) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE, "Caching ResourceInfo: {0}", key);
            }
            return resourceCache.put(key, info);
        }
        return null;

    }


    /**
     * @param key resource key
     * @return the {@link ResourceInfo} associated with <code>key<code>
     *  if any.
     */
    public ResourceInfo get(CompositeKey key) {

        if (shutdown) {
            throw new IllegalStateException("ResourceCache has been terminated");
        }

        Util.notNull("key", key);

        return ((resourceCache != null) ? resourceCache.get(key) : null);

    }


    /**
     * <p>Empty the cache.</p>
     */
    public void clear() {

        if (shutdown) {
            throw new IllegalStateException("ResourceCache has been terminated");
        }

        if (resourceCache != null) {
            resourceCache.clear();
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE, "Cache Cleared");
            }
        }

    }


    /**
     * Clears the cache and cancels the MonitorTask associated with this
     * ResourcecCache.  This method must be called before the application
     * is undeployed to ensure the task is cancelled and ultimately purged().
     */
    public void shutdown() {

        if (shutdown) {
            return;
        }
        shutdown = true;
        if (resourceCache != null) {
            resourceCache.clear();
            resourceCache = null;
        }
        if (service != null) {
            if (monitorTask != null) {
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.log(Level.FINE,
                               "[{0}] Cancelling ResourceCache update task...",
                               contextName);
                }
                monitorTask.cancel(true);
                monitorTask = null;
            }
            service.purge();
        }

    }


    // --------------------------------------------------------- Private Methods


    private synchronized void initExecutor(long period) {

        if (service == null) {
            int poolSize = getThreadPoolSize();
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE,
                           "Created new static ScheduledExecutorService with a pool size of {0}",
                           poolSize);                
            }
            service = new ScheduledThreadPoolExecutor(poolSize);
        }
        monitorTask = service.scheduleWithFixedDelay(new MonitorTask(contextName),
                                       period,
                                       period,
                                       TimeUnit.MILLISECONDS);

    }


    private int getThreadPoolSize() {

        ClassLoader thisClassLoader = ResourceCache.class.getClassLoader();
        return (thisClassLoader
                  .equals(Thread.currentThread().getContextClassLoader())
                                  ? NON_SHARED_THREAD_COUNT
                                  : SHARED_THREAD_COUNT);

    }


    private int getCheckPeriod(WebConfiguration webConfig) {

        String val = webConfig.getOptionValue(WebContextInitParameter.ResourceUpdateCheckPeriod);
        try {
            return Integer.parseInt(val);
        } catch (NumberFormatException nfe) {
            return Integer.parseInt(WebContextInitParameter.ResourceUpdateCheckPeriod.getDefaultValue());
        }

    }


    private void initMonitors(ServletContext sc) {

        monitors = new ArrayList<ResourceMonitor>();
        monitors.add(new WebappResourceMonitor(sc, "/resources/"));
        monitors.add(new WebappResourceMonitor(sc, "/WEB-INF/classes/META-INF/resources/"));
        ClassLoader loader = Util.getCurrentLoader(this.getClass());
        try {
            Enumeration<URL> urls = loader.getResources("META-INF/resources");
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                if (!"jar".equals(url.getProtocol())) {
                    // RELEASE_PENDING - should be a better way to handle this case
                    String urlString = url.toString();
                    if (urlString.contains("/WEB-INF/classes/META-INF/resources")
                        || urlString.contains("jsf-ri-runtime.xml")) {
                        continue;
                    }
                    if (LOGGER.isLoggable(Level.WARNING)) {
                        LOGGER.log(Level.WARNING,
                                   "Unhandled URL: {0}",
                                   url);
                    }
                    continue;
                }
                if (url.toString().contains("jsf-impl.jar")) {
                    continue;
                }
                try {
                    monitors.add(new JarResourceMonitor(url));
                } catch (IOException ioe) {
                    if (LOGGER.isLoggable(Level.SEVERE)) {
                        LOGGER.log(Level.SEVERE,
                                   "IOException occurred setting up JarResourceMonitor for URL {0}.  Updates to this resource will be ignored.",
                                   url.toExternalForm());
                    }
                }
            }
        } catch (IOException ioe) {
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE,
                           "Classpath resource monitoring unavailable.",
                           ioe);
            }
        }
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.log(Level.FINE,
                       "[{0}] Registered ResouceMonitors:",
                       sc.getContextPath());
            for (ResourceMonitor monitor : monitors) {
                LOGGER.log(Level.FINE, monitor.toString());
            }
        }

    }


    // ----------------------------------------------------------- Inner Classes


    /**
     * {@link Runnable} to check for updates from the existing
     * #monitors.  If any modifications are detected, clear
     * #resourceCache and return.
     */
    private final class MonitorTask implements Runnable {

        private final String contextPath;

        public MonitorTask(String contextPath) {

            this.contextPath = contextPath;

        }


        // ----------------------------------------------- Methods from Runnable


        public void run() {

            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE,
                           "[{0}] Starting modification search",
                           contextPath);
            }
            for (ResourceMonitor monitor : monitors) {
                if (monitor.hasBeenModified()) {
                    if (LOGGER.isLoggable(Level.FINE)) {
                        LOGGER.log(Level.FINE,
                                   "[{0}] Modifications found, clearing cache",
                                   contextPath);
                    }
                    resourceCache.clear();
                    return;
                }
            }

        }

    }  // END MonitorTask

    ////////////////////////////////////////////////////////////////////////////

    private interface ResourceMonitor {

        /**
         * @return <cod>true</code> if the resource being monitored has been
         *  modified, otherwise return <code>false</code>
         */
        public boolean hasBeenModified();

    } // END ResourceMonitor

    ////////////////////////////////////////////////////////////////////////////

    /**
     * <p>
     * This {@link com.sun.faces.application.resource.ResourceCache.ResourceMonitor}
     * tracks any changes made to a filesystem within a webapplication.  The term <code>filesystem</code>
     * encompasses the root directory and subdirectories and content stored within.
     * </p>
     *
     * <p>
     * IMPLEMENTATION NOTE:  Since this will typically be called from outside
     *  the request processing lifecycle, we have no access to the {@link javax.faces.context.FacesContext},
     *  so we cache a reference to the ServletContext to leverage {@link ServletContext#getResourcePaths(String)}
     *  for modification detection.  This should be an acceptable practice within
     *  Portlet environments.
     * </p>
     */
    private static class WebappResourceMonitor implements ResourceMonitor {

        private Map<String,Set<String>> layout;
        private ServletContext sc;
        private String startPath;


        // -------------------------------------------------------- Constructors


        /**
         * Construct a new WebappResourceMonitor for a <code> the specified
         * <code>filesystem</code>.
         * @param sc the {@link ServletContext} for this application
         * @param startPath the root directory of the <code>filesystem</code>
         *  being monitored
         *
         * @throws IllegalArgumentException if <code>startPath</code> doesn't
         *  start and end with <code>/</code>
         */
        public WebappResourceMonitor(ServletContext sc, String startPath) {

            if (startPath.charAt(0) != '/'
                  && startPath.charAt(startPath.length() - 1) != '/') {
                throw new IllegalArgumentException("startPath must start and end with '/'");
            }
            this.startPath = startPath;
            this.sc = sc;
            layout = new HashMap<String,Set<String>>();
            createSnapshot(layout);

        }


        // -------------------------------------------------Methods from Monitor


        /**
         * @see com.sun.faces.application.resource.ResourceCache.ResourceMonitor#hasBeenModified()
         */
        public boolean hasBeenModified() {

            Map<String, Set<String>> temp = new HashMap<String, Set<String>>();
            createSnapshot(temp);
            boolean modified = !layout.equals(temp);
            if (modified) {
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.log(Level.FINE,
                               "[{0}] Change detected in webapp filesystem",
                               new Object[] { sc.getContextPath(), startPath });
                }
                layout = temp;
            }
            return modified;

        }


        // ------------------------------------------------------ Public Methods


        @Override
        public String toString() {

            return "[WebappResourceMonitor] Monitoring->"
                   + sc.getContextPath()
                   + ':'
                   + startPath;

        }


        // ----------------------------------------------------- Private Methods


        private void createSnapshot(Map<String,Set<String>> target) {

            createSnapshot(target, startPath);

        }


        @SuppressWarnings({"unchecked"})
        private void createSnapshot(Map<String, Set<String>> target,
                                    String startPath) {

            Set<String> paths = sc.getResourcePaths(startPath);
            if (paths == null) {
                return;
            }
            target.put(startPath, paths);
            for (String path : paths) {
                if (path.charAt(path.length() - 1) == '/') {
                    createSnapshot(target, path);
                }
            }

        }

    } // END WebappResourceMonitor

    ////////////////////////////////////////////////////////////////////////////

    /**
     * Instances of this {@link ResourceMonitor} are associated with a URL
     * to a JAR file.  If the timestamp of the JAR changes, #hasBeenModified
     * will return <code>true</code>
     */
    private static class JarResourceMonitor implements ResourceMonitor {

        private URL jarFileURL;
        private long currentTimeStamp;


        // -------------------------------------------------------- Constructors


        /**
         * Constructs a new JarResourceMonitor.
         *
         * @param url source URL from which the URL to owning JAR will be
         *  obtained.
         */
        public JarResourceMonitor(URL url) throws IOException {

            Util.notNull("url", url);
            if (!"jar".equals(url.getProtocol())) {
                throw new IllegalArgumentException("URL protocol must be 'jar' -> "
                                                   + url.toExternalForm());
            }
            InputStream in = null;
            try {
                URLConnection conn = url.openConnection();
                conn.setUseCaches(false);
                conn.connect();
                in = conn.getInputStream();
                jarFileURL = ((JarURLConnection) conn).getJarFileURL();
                if (jarFileURL == null) {
                    throw new IllegalStateException("Unable to obtain URL to Jar");
                }
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (Exception ignored) { }
                }
            }
            currentTimeStamp = getLastModified();
            
        }


        // ---------------------------------------- Methods from ResourceMonitor


        /**
         * @see com.sun.faces.application.resource.ResourceCache.ResourceMonitor#hasBeenModified()
         */
        public boolean hasBeenModified() {

            long ts = getLastModified();
            if (ts > currentTimeStamp) {
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.log(Level.FINE,
                               "Timestamp for JAR ({0}) changed.",
                               jarFileURL.toExternalForm());
                }
                currentTimeStamp = ts;
                return true;
            }
            return false;

        }


        // ------------------------------------------------------ Public Methods


        @Override
        public String toString() {

            return "[JarResourceMonitor] Monitoring->" + jarFileURL.toExternalForm();
            
        }

        // ----------------------------------------------------- Private Methods


        private long getLastModified() {

            InputStream in = null;
            try {
                URLConnection conn = jarFileURL.openConnection();
                conn.connect();
                in = conn.getInputStream();
                return conn.getLastModified();
            } catch (IOException ioe) {
                if (LOGGER.isLoggable(Level.SEVERE)) {
                    LOGGER.log(Level.SEVERE,
                               "Unable to check JAR timestamp.",
                               ioe);
                }
                return this.currentTimeStamp;
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException ignored) { }
                }
            }

        }

    }

    ////////////////////////////////////////////////////////////////////////////

    /**
     * Represents a composite of a resource name, library, and localePrefix.
     */
    public static class CompositeKey {

        private static final String EMPTY = "";

        private final String name;
        private final String library;
        private final String localePrefix;


        // -------------------------------------------------------- Constructors


        public CompositeKey(String name,
                             String library,
                             String localePrefix) {

            this.name = ((name != null) ? name : EMPTY);
            this.library = ((library != null) ? library : EMPTY);
            this.localePrefix = ((localePrefix != null) ? localePrefix : EMPTY);

        }


        // ------------------------------------------------------ Public Methods


        @Override
        public int hashCode() {

            return (name.hashCode()
                      + library.hashCode()
                      + localePrefix.hashCode());

        }


        @Override
        public boolean equals(Object obj) {

            if (!(obj instanceof CompositeKey)) {
                return false;
            } else {
                CompositeKey key = (CompositeKey) obj;
                return (name.equals(key.name)
                        && library.equals(key.library)
                        && localePrefix.equals(key.localePrefix));
            }

        }


        @Override
        public String toString() {

            return "[name=" + name + ",library=" + library + ",localePrefix=" + localePrefix + ']';

        }

    } // END CompositeKey

} // END ResourceCache

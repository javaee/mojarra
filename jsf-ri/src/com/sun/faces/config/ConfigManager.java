/*
 * $Id: ConfigManager.java,v 1.1 2007/04/22 21:41:04 rlubke Exp $
 */

/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 *
 * You can obtain a copy of the License at
 * https://javaserverfaces.dev.java.net/CDDL.html or
 * legal/CDDLv1.0.txt.
 * See the License for the specific language governing
 * permission and limitations under the License.
 *
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at legal/CDDLv1.0.txt.
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 * [Name of File] [ver.__] [Date]
 *
 * Copyright 2007 Sun Microsystems Inc. All Rights Reserved
 */

package com.sun.faces.config;

import com.sun.faces.config.configprovider.MetaInfResourceProvider;
import com.sun.faces.config.configprovider.RIConfigResourceProvider;
import com.sun.faces.config.configprovider.WebResourceProvider;
import com.sun.faces.config.processor.ApplicationConfigProcessor;
import com.sun.faces.config.processor.ComponentConfigProcessor;
import com.sun.faces.config.processor.ConfigProcessor;
import com.sun.faces.config.processor.ConverterConfigProcessor;
import com.sun.faces.config.processor.FactoryConfigProcessor;
import com.sun.faces.config.processor.LifecycleConfigProcessor;
import com.sun.faces.config.processor.ManagedBeanConfigProcessor;
import com.sun.faces.config.processor.NavigationConfigProcessor;
import com.sun.faces.config.processor.RenderKitConfigProcessor;
import com.sun.faces.config.processor.ValidatorConfigProcessor;
import com.sun.faces.spi.ConfigurationResourceProvider;
import com.sun.faces.util.MessageUtils;
import com.sun.faces.util.Timer;
import org.w3c.dom.Document;

import javax.servlet.ServletContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

/**
 * <p>
 *  This class manages the initialization of each web application that uses
 *  JSF.
 * </p>
 */
public class ConfigManager {

    /**
     * <p>
     *  The list of resource providers.  By default, this contains a provider
     *  for the RI, and two providers to satisfy the requirements of the
     *  specification.
     * </p>
     */
    private static final List<ConfigurationResourceProvider> RESOURCE_PROVIDERS
         = new ArrayList(3);

    /**
     * <p>
     *  The <code>ConfigManager</code> will multithread the calls to the
     *  <code>ConfigurationResourceProvider</code>s as well as any calls
     *  to parse a resources into a DOM.  By default, we'll use only 5 threads
     *  per web application.
     * </p>
     */
    private static final int NUMBER_OF_TASK_THREADS = 5;

    /**
     * <p>
     *  There is only once instance of <code>ConfigManager</code>.
     * <p>
     */
    private static final ConfigManager CONFIG_MANAGER = new ConfigManager();

    /**
     * <p>
     *   Contains each <code>ServletContext</code> that we've initialized.
     *   The <code>ServletContext</code> will be removed when the application
     *   is destroyed.
     * </p>
     */
    @SuppressWarnings({"CollectionWithoutInitialCapacity"})
    private List<ServletContext> initializedContexts =
         new ArrayList();

    /**
     * <p>
     *  The chain of {@link ConfigProcessor}, used to initialize JSF.
     * </p>
     */
    private static final ConfigProcessor CONFIG_PROCESSOR_CHAIN;

    static {
        RESOURCE_PROVIDERS.add(new RIConfigResourceProvider());
        RESOURCE_PROVIDERS.add(new MetaInfResourceProvider());
        RESOURCE_PROVIDERS.add(new WebResourceProvider());
        ConfigProcessor[] configProcessors = {
             new FactoryConfigProcessor(),
             new LifecycleConfigProcessor(),
             new ApplicationConfigProcessor(),
             new ComponentConfigProcessor(),
             new ConverterConfigProcessor(),
             new ValidatorConfigProcessor(),
             new ManagedBeanConfigProcessor(),
             new RenderKitConfigProcessor(),
             new NavigationConfigProcessor()
        };
        for (int i = 0; i < configProcessors.length; i++) {
            ConfigProcessor p = configProcessors[i];
            if ((i + 1) < configProcessors.length) {
                p.setNext(configProcessors[i + 1]);
            }
        }
        CONFIG_PROCESSOR_CHAIN = configProcessors[0];
    }


    // ---------------------------------------------------------- Public Methods


    /**
     * @return a <code>ConfigManager</code> instance
     */
    public static ConfigManager getInstance() {

        return CONFIG_MANAGER;

    }


    /**
     * <p>
     *   This method bootstraps JSF based on the parsed configuration resources.
     * </p>
     *
     * @param sc the <code>ServletContext</code> for the application that
     *  requires initialization
     */
    public void initialize(ServletContext sc) {

        if (!hasBeenInitialized(sc)) {
            initializedContexts.add(sc);
            try {                
                CONFIG_PROCESSOR_CHAIN.process(getConfigDocuments(sc));
            } catch (Exception e) {               
                throw new ConfigurationException(
                     MessageUtils.getExceptionMessageString(
                          MessageUtils.ERROR_PROCESSING_CONFIG_ID),
                     e);
            }
        }

    }


    /**
     * <p>
     *   This method will remove any information about the application.
     * </p>
     * @param sc the <code>ServletContext</code> for the application that
     *  needs to be removed
     */
    public void destory(ServletContext sc) {

        initializedContexts.remove(sc);
        
    }


    /**
     * @param sc the <code>ServletContext</code> for the application in question
     * @return <code>true</code> if this application has already been initialized,
     *  otherwise returns </code>fase</code>
     */
    public boolean hasBeenInitialized(ServletContext sc) {
        return (initializedContexts.contains(sc));
    }


    // --------------------------------------------------------- Private Methods


    /**
     * <p>
     *   Obtains an array of <code>Document</code>s to be processed
     *   by {@link ConfigManager#CONFIG_PROCESSOR_CHAIN}.
     * </p>
     *
     * @param sc the <code>ServletContext</code> for the application to be
     *  processed
     * @return an array of <code>Document</code>s
     */
    private static Document[] getConfigDocuments(ServletContext sc) {

        ExecutorService executor =
             Executors.newFixedThreadPool(NUMBER_OF_TASK_THREADS);
        
        List<FutureTask<List<URL>>> urlTasks =
             new ArrayList<FutureTask<List<URL>>>(RESOURCE_PROVIDERS.size());
        for (ConfigurationResourceProvider p : RESOURCE_PROVIDERS) {
            FutureTask<List<URL>> t =
                 new FutureTask<List<URL>>(new URLTask(p, sc));
            urlTasks.add(t);
            executor.execute(t);
        }

        List<FutureTask<Document>> docTasks =
             new ArrayList<FutureTask<Document>>(RESOURCE_PROVIDERS.size() << 1);
        DocumentBuilderFactory factory = DbfFactory.getFactory(true);
        for (FutureTask<List<URL>> t : urlTasks) {
            try {
                List<URL> l = t.get();                
                for (URL u : l) {
                    FutureTask<Document> d =
                         new FutureTask<Document>(new ParseTask(factory, u));
                    docTasks.add(d);
                    executor.execute(d);
                }
            } catch (ExecutionException e) {
                throw new ConfigurationException(e);
            } catch (InterruptedException e) {
                ;
            }
        }

        List<Document> docs = new ArrayList(docTasks.size());
        for (FutureTask<Document> t : docTasks) {
            try {
                docs.add(t.get());
            } catch (ExecutionException e) {
                throw new ConfigurationException(e);
            } catch (InterruptedException e) {
                ;
            }
        }

        executor.shutdown();
        return docs.toArray(new Document[docs.size()]);

    }

    // ----------------------------------------------------------- Inner Classes


    /**
     * <p>
     *  This <code>Callable</code> will be used by {@link ConfigManager#getConfigDocuments(javax.servlet.ServletContext)}.
     *  It represents a single configuration resource to be parsed into a DOM.
     * </p>
     */
    private static class ParseTask implements Callable<Document> {

        private URL documentURL;
        private DocumentBuilderFactory factory;

        // -------------------------------------------------------- Constructors


        /**
         * <p>
         *   Constructs a new ParseTask instance
         * </p>
         * @param factory a DocumentBuilderFactory configured with the desired
         *  parse settings
         * @param documentURL a URL to the configuration resource to be parsed
         */
        public ParseTask(DocumentBuilderFactory factory, URL documentURL) {

            this.documentURL = documentURL;
            this.factory = factory;

        }


        // ----------------------------------------------- Methods from Callable


        /**
         * @return the result of the parse operation (a DOM)
         * @throws Exception if an error occurs during the parsing process
         */
        public Document call() throws Exception {
            DocumentBuilder b = factory.newDocumentBuilder();
            b.setEntityResolver(DbfFactory.FACES_ENTITY_RESOLVER);
            InputStream stream = getInputStream();
            try {
                Timer timer = Timer.getInstance();
                if (timer != null) {
                    timer.startTiming();
                }

                Document d = b.parse(stream, documentURL.toExternalForm());

                if (timer != null) {
                    timer.stopTiming();
                    timer.logResult("Parse " + documentURL.toExternalForm());
                }
              
                return d;
            } finally {
                if (stream != null) {
                    try {
                        stream.close();
                    } catch (IOException ioe) {
                        ;
                    }
                }
            }
        }


        // ----------------------------------------------------- Private Methods


        /**
         * @return an <code>InputStream</code> to the resource referred to by
         *  {@link documentURL}
         * @throws IOException if an error occurs
         */
        private InputStream getInputStream() throws IOException {

            URLConnection conn = documentURL.openConnection();
            conn.setUseCaches(false);
            return new BufferedInputStream(conn.getInputStream());

        }

    } // END ParseTask


    /**
     * <p>
     *  This <code>Callable</code> will be used by {@link ConfigManager#getConfigDocuments(javax.servlet.ServletContext)}.
     *  It represents one or more URLs to configuration resources that require
     *  processing.
     * </p>
     */
    private static class URLTask implements Callable<List<URL>> {

        private ConfigurationResourceProvider provider;
        private ServletContext sc;


        // -------------------------------------------------------- Constructors


        /**
         * Constructs a new <code>URLTask</code> instance.
         * @param provider the <code>ConfigurationResourceProvider</code> from
         *  which zero or more <code>URL</code>s will be returned
         * @param sc the <code>ServletContext</code> of the current application
         */
        public URLTask(ConfigurationResourceProvider provider,
                       ServletContext sc) {
            this.provider = provider;
            this.sc = sc;
        }


        // ----------------------------------------------- Methods from Callable


        /**
         * @return zero or more <code>URL</code> instances
         * @throws Exception if an Exception is thrown by the underlying
         *  <code>ConfigurationResourceProvider</code> 
         */
        public List<URL> call() throws Exception {
            return provider.getResources(sc);
        }

    } // END URLTask
}

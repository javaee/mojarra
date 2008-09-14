/*
 * $Id: ConfigManager.java,v 1.24 2008/03/05 21:34:52 rlubke Exp $
 */

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

package com.sun.faces.config;

import static com.sun.faces.config.WebConfiguration.BooleanWebContextInitParameter.ValidateFacesConfigFiles;
import static com.sun.faces.config.WebConfiguration.BooleanWebContextInitParameter.DisableFaceletJSFViewHandler;
import com.sun.faces.config.configprovider.ConfigurationResourceProvider;
import com.sun.faces.config.configprovider.MetaInfFacesConfigResourceProvider;
import com.sun.faces.config.configprovider.MojarraFacesConfigResourceProvider;
import com.sun.faces.config.configprovider.WebFacesConfigResourceProvider;
import com.sun.faces.config.configprovider.MetaInfFaceletTaglibraryConfigProvider;
import com.sun.faces.config.configprovider.WebFaceletTaglibResourceProvider;
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
import com.sun.faces.config.processor.FaceletTaglibConfigProcessor;
import com.sun.faces.util.FacesLogger;
import com.sun.faces.util.Timer;
import com.sun.faces.application.ApplicationAssociate;
import com.sun.faces.application.annotation.AnnotationManager;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.event.ApplicationPostConstructEvent;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.Attr;
import org.w3c.dom.Node;

/**
 * <p>
 *  This class manages the initialization of each web application that uses
 *  JSF.
 * </p>
 */
public class ConfigManager {

    private static final Logger LOGGER = FacesLogger.CONFIG.getLogger();

    /**
     * <p>
     * A List of resource providers that search for faces-config documents.
     * By default, this contains a provider for the Mojarra, and two other
     * providers to satisfy the requirements of the specification.
     * </p>
     */
    private static final List<ConfigurationResourceProvider> FACES_CONFIG_RESOURCE_PROVIDERS;

    /**
     * <p>
     * A List of resource providers that search for faces-config documents.
     * By default, this contains a provider for the Mojarra, and one other
     * providers to satisfy the requirements of the specification.
     * </p>
     */
    private static final List<ConfigurationResourceProvider> FACELET_TAGLIBRARY_RESOURCE_PROVIDERS;

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
         new CopyOnWriteArrayList<ServletContext>();

    /**
     * <p>
     *  The chain of {@link ConfigProcessor} instances to processing of
     *  faces-config documents.
     * </p>
     */
    private static final ConfigProcessor FACES_CONFIG_PROCESSOR_CHAIN;


    /**
     * <p>
     *  The chain of {@link ConfigProcessor} instances to processing of
     *  facelet-taglib documents.
     * </p>
     */
    private static final ConfigProcessor FACELET_TAGLIB_CONFIG_PROCESSOR_CHAIN;

    /**
     * Stylesheet to convert 1.0 and 1.1 based faces-config documents
     * to our private 1.1 schema for validation.
     */
    private static final String FACES_TO_1_1_PRIVATE_XSL =
          "/com/sun/faces/jsf1_0-1_1toSchema.xsl";

    /**
     * Stylesheet to convert 1.0 facelet-taglib documents
     * from 1.0 to 2.0 for schema validation purposes.
     */
    private static final String FACELETS_TO_2_0_XSL =
          "/com/sun/faces/facelets1_0-2_0toSchema.xsl";

    private static final String FACES_CONFIG_1_X_DEFAULT_NS =
          "http://java.sun.com/JSF/Configuration";

    private static final String FACELETS_1_0_DEFAULT_NS =
          "http://java.sun.com/JSF/Facelet";


    static {

        // initialize the resource providers for faces-config documents
        List<ConfigurationResourceProvider> facesConfigProviders =
          new ArrayList<ConfigurationResourceProvider>(3);
        facesConfigProviders.add(new MojarraFacesConfigResourceProvider());
        facesConfigProviders.add(new MetaInfFacesConfigResourceProvider());
        facesConfigProviders.add(new WebFacesConfigResourceProvider());
        FACES_CONFIG_RESOURCE_PROVIDERS = Collections.unmodifiableList(facesConfigProviders);

        // initialize the resource providers for facelet-taglib documents
        List<ConfigurationResourceProvider> faceletTaglibProviders =
              new ArrayList<ConfigurationResourceProvider>(3);
        faceletTaglibProviders.add(new MetaInfFaceletTaglibraryConfigProvider());
        faceletTaglibProviders.add(new WebFaceletTaglibResourceProvider());
        FACELET_TAGLIBRARY_RESOURCE_PROVIDERS = Collections.unmodifiableList(faceletTaglibProviders);

        // initialize the config processors for faces-config documents
        ConfigProcessor[] configProcessors = {
             new FactoryConfigProcessor(),
             new LifecycleConfigProcessor(),
             new ApplicationConfigProcessor(),
             new ComponentConfigProcessor(),
             new ConverterConfigProcessor(),
             new ValidatorConfigProcessor(),
             new ManagedBeanConfigProcessor(),
             new RenderKitConfigProcessor(),
             new NavigationConfigProcessor(),
        };
        for (int i = 0; i < configProcessors.length; i++) {
            ConfigProcessor p = configProcessors[i];
            if ((i + 1) < configProcessors.length) {
                p.setNext(configProcessors[i + 1]);
            }
        }
        FACES_CONFIG_PROCESSOR_CHAIN = configProcessors[0];

        // initialize the config processor for facelet-taglib documents
        FACELET_TAGLIB_CONFIG_PROCESSOR_CHAIN = new FaceletTaglibConfigProcessor();
        
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
                WebConfiguration webConfig = WebConfiguration.getInstance(sc);
                boolean validating = webConfig.isOptionEnabled(ValidateFacesConfigFiles);
                boolean isFacesPDLDisabled = webConfig.isOptionEnabled(DisableFaceletJSFViewHandler);
                ExecutorService executor = createExecutorService();

                // execut the Task responsible for finding annotation classes
                Future<Set<String>> annotationScan =
                      executor.submit(new AnnotationScanTask(sc));

                Document[] facesDocuments =
                      getConfigDocuments(sc,
                                         FACES_CONFIG_RESOURCE_PROVIDERS,
                                         executor,
                                         validating);
                if (isFacesPDLDisabled) {
                    // if not explicitly disabled, make a sanity check against
                    // /WEB-INF/faces-config.xml
                    isFacesPDLDisabled = isFacesApp20(facesDocuments[facesDocuments.length - 1]);
                    webConfig.overrideContextInitParameter(DisableFaceletJSFViewHandler, isFacesPDLDisabled);
                }
                FACES_CONFIG_PROCESSOR_CHAIN.process(
                      getConfigDocuments(sc,
                                         FACES_CONFIG_RESOURCE_PROVIDERS,
                                         executor,
                                         validating));
                if (!isFacesPDLDisabled) {
                    FACELET_TAGLIB_CONFIG_PROCESSOR_CHAIN.process(
                          getConfigDocuments(sc,
                                             FACELET_TAGLIBRARY_RESOURCE_PROVIDERS,
                                             executor,
                                             validating));
                }

                processAnnotatedClasses(sc, annotationScan.get());

                executor.shutdown();
                publishPostConfigEvent();
            } catch (Exception e) {
                // clear out any configured factories
                releaseFactories();
                if (LOGGER.isLoggable(Level.INFO)) {
                    LOGGER.log(Level.INFO,
                               "Unsanitized stacktrace from failed start...",
                               e);
                }
                Throwable t = unwind(e);
                throw new ConfigurationException("CONFIGURATION FAILED! " + t.getMessage(),
                                                 t);
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

        releaseFactories();
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


    private void processAnnotatedClasses(ServletContext sc, Set<String> annotatedClasses) {

        if (!annotatedClasses.isEmpty()) {
            ApplicationAssociate associate = ApplicationAssociate.getInstance(sc);
            if (associate != null) {
                AnnotationManager manager = associate.getAnnotationManager();
                manager.applyConfigAnntations(FacesContext.getCurrentInstance(),
                                              annotatedClasses);
            }
        }

    }

    /**
     * Publishes a {@link javax.faces.event.ApplicationPostConstructEvent} event for the current
     * {@link Application} instance.
     */
    private void publishPostConfigEvent() {

        FacesContext ctx = FacesContext.getCurrentInstance();
        Application app = ctx.getApplication();
        app.publishEvent(ApplicationPostConstructEvent.class,
                         Application.class,
                         app);

    }


    /**
     * <p>
     *   Obtains an array of <code>Document</code>s to be processed
     *   by {@link ConfigManager#FACES_CONFIG_PROCESSOR_CHAIN}.
     * </p>
     *
     * @param sc the <code>ServletContext</code> for the application to be
     *  processed
     * @param providers <code>List</code> of <code>ConfigurationResourceProvider</code>
     *  instances that provide the URL of the documents to parse.
     * @param executor the <code>ExecutorService</code> used to dispatch parse
     *  request to
     * @param validating flag indicating whether or not the documents
     *  should be validated
     * @return an array of <code>Document</code>s
     */
    private static Document[] getConfigDocuments(ServletContext sc,
                                                 List<ConfigurationResourceProvider> providers,
                                                 ExecutorService executor,
                                                 boolean validating) {

        List<FutureTask<Collection<URL>>> urlTasks =
             new ArrayList<FutureTask<Collection<URL>>>(providers.size());
        for (ConfigurationResourceProvider p : providers) {
            FutureTask<Collection<URL>> t =
                 new FutureTask<Collection<URL>>(new URLTask(p, sc));
            urlTasks.add(t);
            executor.execute(t);
        }

        List<FutureTask<Document>> docTasks =
             new ArrayList<FutureTask<Document>>(providers.size() << 1);

        for (FutureTask<Collection<URL>> t : urlTasks) {
            try {
                Collection<URL> l = t.get();
                for (URL u : l) {
                    FutureTask<Document> d =
                         new FutureTask<Document>(new ParseTask(validating, u));
                    docTasks.add(d);
                    executor.execute(d);
                }
            } catch (InterruptedException ignored) {
            } catch (Exception e) {
                throw new ConfigurationException(e);
            }
        }

        List<Document> docs = new ArrayList<Document>(docTasks.size());
        for (FutureTask<Document> t : docTasks) {
            try {
                docs.add(t.get());
            } catch (ExecutionException e) {
                throw new ConfigurationException(e);
            } catch (InterruptedException ignored) { }
        }

        return docs.toArray(new Document[docs.size()]);

    }


    /**
     *
     */
    private static ExecutorService createExecutorService() {

        return Executors.newFixedThreadPool(NUMBER_OF_TASK_THREADS);

    }


    /**
     * @param throwable Throwable
     * @return the root cause of this error
     */
    private Throwable unwind(Throwable throwable) {

          Throwable t = null;
          if (throwable != null) {
              t =  unwind(throwable.getCause());
              if (t == null) {
                  t = throwable;
              }
          }
          return t;

    }


    /**
     * Calls through to {@link javax.faces.FactoryFinder#releaseFactories()}
     * ignoring any exceptions.
     */
    private void releaseFactories() {
        try {
            FactoryFinder.releaseFactories();
        } catch (FacesException ignored) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE,
                           "Exception thrown from FactoryFinder.releaseFactories()",
                           ignored);
            }
        }
    }


    private boolean isFacesApp20(Document document) {

        String url = document.getDocumentURI();
        if (url != null && url.contains("/WEB-INF/faces-config.xml")) {
            String version = document.getDocumentElement()
                  .getAttributeNS(document.getNamespaceURI(), "version");
            if (version != null) {
                Double v = Double.parseDouble(version);
                Double twoOh = 2.0d;
                return !(v.compareTo(twoOh) < 0);
            }
        }

        return true;  // no faces-config.xml or version so assume 2.0

    }


    // ----------------------------------------------------------- Inner Classes


    /**
     * Scans the class files within a web application returning a <code>Set</code>
     * of classes that have been annotated with a standard Faces annotation.
     */
    private static class AnnotationScanTask implements Callable<Set<String>> {

        private ServletContext sc;


        // -------------------------------------------------------- Constructors


        public AnnotationScanTask(ServletContext sc) {

            this.sc = sc;

        }


        // ----------------------------------------------- Methods from Callable


        public Set<String> call() throws Exception {

            AnnotationScanner scanner = new AnnotationScanner(sc);
            return scanner.getAnnotatedClasses();

        }


    } // END AnnotationScanTask


    /**
     * <p>
     *  This <code>Callable</code> will be used by {@link ConfigManager#getConfigDocuments(javax.servlet.ServletContext, java.util.List, java.util.concurrent.ExecutorService, boolean)}.
     *  It represents a single configuration resource to be parsed into a DOM.
     * </p>
     */
    private static class ParseTask implements Callable<Document> {
        private static final String JAVAEE_SCHEMA_DEFAULT_NS =
            "http://java.sun.com/xml/ns/javaee";
        private URL documentURL;
        private DocumentBuilderFactory factory;
        private boolean validating;

        // -------------------------------------------------------- Constructors


        /**
         * <p>
         *   Constructs a new ParseTask instance
         * </p>
         *
         * @param validating whether or not we're validating
         * @param documentURL a URL to the configuration resource to be parsed
         * @throws Exception general error
         */
        public ParseTask(boolean validating, URL documentURL)
        throws Exception {

            this.documentURL = documentURL;
            this.factory = DbfFactory.getFactory();
            this.validating = validating;

        }


        // ----------------------------------------------- Methods from Callable


        /**
         * @return the result of the parse operation (a DOM)
         * @throws Exception if an error occurs during the parsing process
         */
        public Document call() throws Exception {

            try {
                Timer timer = Timer.getInstance();
                if (timer != null) {
                    timer.startTiming();
                }

                Document d = getDocument();

                if (timer != null) {
                    timer.stopTiming();
                    timer.logResult("Parse " + documentURL.toExternalForm());
                }
              
                return d;
            } catch (Exception e) {
                throw new ConfigurationException(MessageFormat.format(
                     "Unable to parse document ''{0}'': {1}",
                     documentURL.toExternalForm(),
                     e.getMessage()), e);
            }
        }


        // ----------------------------------------------------- Private Methods


        /**
         * @return <code>Document</code> based on <code>documentURL</code>.
         * @throws Exception if an error occurs during the process of building a
         *  <code>Document</code>
         */
        private Document getDocument() throws Exception {
            if (validating) {  // the Schema won't be null if validation is enabled.
                DocumentBuilder db = getNonValidatingBuilder();
                DOMSource domSource
                     = new DOMSource(db.parse(getInputStream(documentURL),
                                                    documentURL.toExternalForm()));

                /*
                 * If the Document in question is 1.2 (i.e. it has a namespace matching
                 * JAVAEE_SCHEMA_DEFAULT_NS, then perform validation using the cached schema
                 * and return.  Otherwise we assume a 1.0 or 1.1 faces-config in which case
                 * we need to transform it to reference a special 1.1 schema before validating.
                 */
                Node documentElement = ((Document) domSource.getNode()).getDocumentElement();
                String documentNS = documentElement.getNamespaceURI();
                if (JAVAEE_SCHEMA_DEFAULT_NS.equals(documentNS)) {
                    Attr version = (Attr)
                            documentElement.getAttributes().getNamedItem("version");
                    DbfFactory.FacesSchema schema;
                    if (version != null) {
                        String versionStr = version.getValue();
                        if ("2.0".equals(versionStr)) {
                            if ("facelet-taglib".equals(documentElement.getLocalName())) {
                                schema = DbfFactory.FacesSchema.FACELET_TAGLIB_20;
                            } else {
                                schema = DbfFactory.FacesSchema.FACES_20;
                            }
                        } else if ("1.2".equals(versionStr)) {
                            schema = DbfFactory.FacesSchema.FACES_12;
                        } else {
                            throw new ConfigurationException("Unknown Schema version: " + versionStr);
                        }
                        DocumentBuilder builder = getBuilderForSchema(schema);
                        builder.getSchema().newValidator().validate(domSource);
                        return ((Document) domSource.getNode());
                    } else {
                        // this shouldn't happen, but...
                        throw new ConfigurationException("No document version available.");
                    }
                } else {
                    DOMResult domResult = new DOMResult();
                    Transformer transformer = getTransformer(documentNS);
                    transformer.transform(domSource, domResult);
                    DbfFactory.FacesSchema schemaToApply;
                    if (documentNS.equals(FACES_CONFIG_1_X_DEFAULT_NS)) {
                        schemaToApply = DbfFactory.FacesSchema.FACES_11;
                    } else if (documentNS.equals(FACELETS_1_0_DEFAULT_NS)) {
                        schemaToApply = DbfFactory.FacesSchema.FACELET_TAGLIB_20;
                    } else {
                        throw new IllegalStateException();
                    }
                    DocumentBuilder builder = getBuilderForSchema(schemaToApply);
                    builder.getSchema().newValidator().validate(new DOMSource(domResult.getNode()));
                    return (Document) domResult.getNode();
                }
            } else {
                // validation isn't required, parse and return
                DocumentBuilder builder = getNonValidatingBuilder();
                InputSource is = new InputSource(getInputStream(documentURL));
                is.setSystemId(documentURL.toExternalForm());
                return builder.parse(is);
            }
        }


        /**
         * Obtain a <code>Transformer</code> using the style sheet
         * referenced by the <code>XSL</code> constant.
         *
         * @return a new Tranformer instance
         * @throws Exception if a Tranformer instance could not be created
         */
        private static Transformer getTransformer(String documentNS)
        throws Exception {

            TransformerFactory factory = TransformerFactory.newInstance();
            String xslToApply;
            if (documentNS.equals(FACES_CONFIG_1_X_DEFAULT_NS)) {
                xslToApply = FACES_TO_1_1_PRIVATE_XSL;
            } else if (documentNS.equals(FACELETS_1_0_DEFAULT_NS)) {
                xslToApply = FACELETS_TO_2_0_XSL;
            } else {
                throw new IllegalStateException();
            }
            return factory
                 .newTransformer(new StreamSource(getInputStream(ConfigManager
                      .class.getResource(xslToApply))));

        }


        /**
         * @return an <code>InputStream</code> to the resource referred to by
         *         <code>url</code>
         * @param url source <code>URL</code>
         * @throws IOException if an error occurs
         */
        private static InputStream getInputStream(URL url) throws IOException {

            URLConnection conn = url.openConnection();
            conn.setUseCaches(false);
            return new BufferedInputStream(conn.getInputStream());

        }



        private DocumentBuilder getNonValidatingBuilder() throws Exception {

            DocumentBuilderFactory tFactory = DbfFactory.getFactory();
            tFactory.setValidating(false);
            DocumentBuilder tBuilder = tFactory.newDocumentBuilder();
            tBuilder.setEntityResolver(DbfFactory.FACES_ENTITY_RESOLVER);
            tBuilder.setErrorHandler(DbfFactory.FACES_ERROR_HANDLER);
            return tBuilder;

        }

        private DocumentBuilder getBuilderForSchema(DbfFactory.FacesSchema schema)
        throws Exception {
            factory.setSchema(schema.getSchema());
            DocumentBuilder builder = factory.newDocumentBuilder();
            builder.setEntityResolver(DbfFactory.FACES_ENTITY_RESOLVER);
            builder.setErrorHandler(DbfFactory.FACES_ERROR_HANDLER);
            return builder;
        }

    } // END ParseTask


    /**
     * <p>
     *  This <code>Callable</code> will be used by {@link ConfigManager#getConfigDocuments(javax.servlet.ServletContext, java.util.List, java.util.concurrent.ExecutorService, boolean)}.
     *  It represents one or more URLs to configuration resources that require
     *  processing.
     * </p>
     */
    private static class URLTask implements Callable<Collection<URL>> {

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
        public Collection<URL> call() throws Exception {
            return provider.getResources(sc);
        }

    } // END URLTask
}

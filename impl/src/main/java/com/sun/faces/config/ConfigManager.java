/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2015 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.faces.config;

import static com.sun.faces.RIConstants.ANNOTATED_CLASSES;
import static com.sun.faces.RIConstants.FACES_PREFIX;
import static com.sun.faces.RIConstants.JAVAEE_XMLNS;
import static com.sun.faces.config.DbfFactory.FACES_ENTITY_RESOLVER;
import static com.sun.faces.config.DbfFactory.FACES_ERROR_HANDLER;
import static com.sun.faces.config.WebConfiguration.BooleanWebContextInitParameter.DisableFaceletJSFViewHandler;
import static com.sun.faces.config.WebConfiguration.BooleanWebContextInitParameter.DisableFaceletJSFViewHandlerDeprecated;
import static com.sun.faces.config.WebConfiguration.BooleanWebContextInitParameter.EnableThreading;
import static com.sun.faces.config.WebConfiguration.BooleanWebContextInitParameter.ValidateFacesConfigFiles;
import static com.sun.faces.spi.ConfigurationResourceProviderFactory.createProviders;
import static com.sun.faces.spi.ConfigurationResourceProviderFactory.ProviderType.FaceletConfig;
import static com.sun.faces.spi.ConfigurationResourceProviderFactory.ProviderType.FacesConfig;
import static com.sun.faces.util.Util.createTransformerFactory;
import static com.sun.faces.util.Util.isEmpty;
import static java.text.MessageFormat.format;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;
import static java.util.Collections.emptySet;
import static java.util.Collections.unmodifiableList;
import static java.util.logging.Level.FINE;
import static java.util.logging.Level.INFO;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.el.ELContext;
import javax.el.ELContextEvent;
import javax.el.ELContextListener;
import javax.el.ExpressionFactory;
import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.application.ApplicationConfigurationPopulator;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.PostConstructApplicationEvent;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;

import org.w3c.dom.Attr;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXParseException;

import com.sun.faces.RIConstants;
import com.sun.faces.config.configprovider.MetaInfFaceletTaglibraryConfigProvider;
import com.sun.faces.config.configprovider.MetaInfFacesConfigResourceProvider;
import com.sun.faces.config.configprovider.WebAppFlowConfigResourceProvider;
import com.sun.faces.config.configprovider.WebFaceletTaglibResourceProvider;
import com.sun.faces.config.configprovider.WebFacesConfigResourceProvider;
import com.sun.faces.config.processor.ApplicationConfigProcessor;
import com.sun.faces.config.processor.BehaviorConfigProcessor;
import com.sun.faces.config.processor.ComponentConfigProcessor;
import com.sun.faces.config.processor.ConfigProcessor;
import com.sun.faces.config.processor.ConverterConfigProcessor;
import com.sun.faces.config.processor.FaceletTaglibConfigProcessor;
import com.sun.faces.config.processor.FacesConfigExtensionProcessor;
import com.sun.faces.config.processor.FacesFlowDefinitionConfigProcessor;
import com.sun.faces.config.processor.FactoryConfigProcessor;
import com.sun.faces.config.processor.LifecycleConfigProcessor;
import com.sun.faces.config.processor.ManagedBeanConfigProcessor;
import com.sun.faces.config.processor.NavigationConfigProcessor;
import com.sun.faces.config.processor.ProtectedViewsConfigProcessor;
import com.sun.faces.config.processor.RenderKitConfigProcessor;
import com.sun.faces.config.processor.ResourceLibraryContractsConfigProcessor;
import com.sun.faces.config.processor.ValidatorConfigProcessor;
import com.sun.faces.el.ELContextImpl;
import com.sun.faces.el.ELUtils;
import com.sun.faces.spi.AnnotationProvider;
import com.sun.faces.spi.AnnotationProviderFactory;
import com.sun.faces.spi.ConfigurationResourceProvider;
import com.sun.faces.spi.ConfigurationResourceProviderFactory;
import com.sun.faces.spi.HighAvailabilityEnabler;
import com.sun.faces.spi.InjectionProvider;
import com.sun.faces.spi.InjectionProviderFactory;
import com.sun.faces.spi.ThreadContext;
import com.sun.faces.util.FacesLogger;
import com.sun.faces.util.Timer;
import com.sun.faces.util.Util;

/**
 * <p>
 *  This class manages the initialization of each web application that uses
 *  JSF.
 * </p>
 */
public class ConfigManager {
    
    private static final Logger LOGGER = FacesLogger.CONFIG.getLogger();
    
    /**
     * The initialization time FacesContext scoped key under which the
     * InjectionProvider is stored.
     */
    public static final String INJECTION_PROVIDER_KEY = ConfigManager.class.getName() + "_INJECTION_PROVIDER_TASK";

    /**
     * Name of the attribute added by ParseTask to indicate a
     * {@link Document} instance as a representation of
     * <code>/WEB-INF/faces-config.xml</code>.
     */
    public static final String WEB_INF_MARKER = "com.sun.faces.webinf";

    private static final Pattern JAR_PATTERN = Pattern.compile("(.*/(\\S*\\.jar)).*(/faces-config.xml|/*.\\.faces-config.xml)");

    /**
     * <p>
     *  The <code>ConfigManager</code> will multithread the calls to the
     *  <code>ConfigurationResourceProvider</code>s as well as any calls
     *  to parse a resources into a DOM.  By default, we'll use only 5 threads
     *  per web application.
     * </p>
     */
    private static final int NUMBER_OF_TASK_THREADS = 5;

    private static final String CONFIG_MANAGER_INSTANCE_KEY = FACES_PREFIX + "CONFIG_MANAGER_KEY";

    /**
     * The application-scoped key under which the Future responsible for annotation
     * scanning is associated with.
     */
    private static final String ANNOTATIONS_SCAN_TASK_KEY = ConfigManager.class.getName() + "_ANNOTATION_SCAN_TASK";

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
    
    /**
     * <p>
     *   Contains each <code>ServletContext</code> that we've initialized.
     *   The <code>ServletContext</code> will be removed when the application
     *   is destroyed.
     * </p>
     */
    private List<ServletContext> initializedContexts = new CopyOnWriteArrayList<>();

    private final List<ConfigProcessor> configProcessors = unmodifiableList(asList(
            new FactoryConfigProcessor(),
            new LifecycleConfigProcessor(),
            new ApplicationConfigProcessor(),
            new ComponentConfigProcessor(),
            new ConverterConfigProcessor(),
            new ValidatorConfigProcessor(),
            new ManagedBeanConfigProcessor(),
            new RenderKitConfigProcessor(),
            new NavigationConfigProcessor(),
            new BehaviorConfigProcessor(),
            new FacesConfigExtensionProcessor(),
            new ProtectedViewsConfigProcessor(),
            new FacesFlowDefinitionConfigProcessor(),
            new ResourceLibraryContractsConfigProcessor()));
    
    /**
     * <p>
     * A List of resource providers that search for faces-config documents.
     * By default, this contains a provider for the Mojarra, and two other
     * providers to satisfy the requirements of the specification.
     * </p>
     */
    private final List<ConfigurationResourceProvider> facesConfigProviders = unmodifiableList(asList(
            new MetaInfFacesConfigResourceProvider(),
            new WebAppFlowConfigResourceProvider(),
            new WebFacesConfigResourceProvider()
    ));

    /**
     * <p>
     * A List of resource providers that search for faces-config documents.
     * By default, this contains a provider for the Mojarra, and one other
     * providers to satisfy the requirements of the specification.
     * </p>
     */
    private final List<ConfigurationResourceProvider> facesletsTagLibConfigProviders = unmodifiableList(asList(
            new MetaInfFaceletTaglibraryConfigProvider(),
            new WebFaceletTaglibResourceProvider()
    ));
    
    /**
     * <p>
     *  The chain of {@link ConfigProcessor} instances to processing of
     *  facelet-taglib documents.
     * </p>
     */
    private final ConfigProcessor faceletTaglibConfigProcessor = new FaceletTaglibConfigProcessor();

    

    // ---------------------------------------------------------- Public Methods

    public static ConfigManager createInstance(ServletContext servletContext) {
        ConfigManager result = new ConfigManager();
        servletContext.setAttribute(CONFIG_MANAGER_INSTANCE_KEY, result);
        return result;
    }
    
    /**
     * @return a <code>ConfigManager</code> instance
     */
    public static ConfigManager getInstance(ServletContext servletContext) {
        return (ConfigManager) servletContext.getAttribute(CONFIG_MANAGER_INSTANCE_KEY);
    }
    
    public static void removeInstance(ServletContext servletContext) {
        servletContext.removeAttribute(CONFIG_MANAGER_INSTANCE_KEY);
    }

    /**
     * <p>
     *   This method bootstraps JSF based on the parsed configuration resources.
     * </p>
     *
     * @param servletContext the <code>ServletContext</code> for the application that
     *  requires initialization
     */
    public void initialize(ServletContext servletContext, InitFacesContext facesContext) {

        if (!hasBeenInitialized(servletContext)) {
            
            initializedContexts.add(servletContext);
            initializeConfigProcessers(servletContext, facesContext);
            ExecutorService executor = null;
            
            try {
                WebConfiguration webConfig = WebConfiguration.getInstance(servletContext);
                boolean validating = webConfig.isOptionEnabled(ValidateFacesConfigFiles);
                
                if (useThreads(servletContext)) {
                    executor = createExecutorService();
                }

                DocumentInfo[] facesDocuments = getConfigDocuments(servletContext, getFacesConfigResourceProviders(), executor, validating);
                
                // Merge the programmatic documents into the faces documents array
                facesDocuments = mergeDocuments(facesDocuments, getProgrammaticDocuments());
                
                FacesConfigInfo lastFacesConfigInfo = new FacesConfigInfo(facesDocuments[facesDocuments.length - 1]);

                facesDocuments = sortDocuments(facesDocuments, lastFacesConfigInfo);

                InjectionProvider containerConnector = InjectionProviderFactory.createInstance(facesContext.getExternalContext());
                facesContext.getAttributes().put(INJECTION_PROVIDER_KEY, containerConnector);

                boolean isFaceletsDisabled = isFaceletsDisabled(webConfig, lastFacesConfigInfo);
                
                if (!lastFacesConfigInfo.isWebInfFacesConfig() || !lastFacesConfigInfo.isMetadataComplete()) {
                    findAnnotations(facesDocuments, containerConnector, servletContext, facesContext, executor);
                }

                // See if the app is running in a HA enabled env               
                if (containerConnector instanceof HighAvailabilityEnabler) {                   
                    ((HighAvailabilityEnabler)containerConnector).enableHighAvailability(servletContext);
                }
               
                
                // Process the ordered and merged documents
                // This invokes a chain or processors where each processor grabs its own elements of interest
                // from each document.
                
                boolean hasKey = facesContext.getExternalContext().getApplicationMap().containsKey(RIConstants.FACES_PREFIX + "ApplicationAssociate");
                
                DocumentInfo[] facesDocuments2 = facesDocuments;
                configProcessors.subList(0, 3).stream().forEach(e -> {
                    try {
                        

                        boolean hasKeyx = facesContext.getExternalContext().getApplicationMap().containsKey(RIConstants.FACES_PREFIX + "ApplicationAssociate");
                        
                        e.process(servletContext, facesContext, facesDocuments2);
                    } catch (Exception e2) {
                        // TODO Auto-generated catch block
                        e2.printStackTrace();
                    }
                });
                
                long parentThreadId = Thread.currentThread().getId();
                ClassLoader parentContextClassLoader = Thread.currentThread().getContextClassLoader();
                
                ThreadContext threadContext = getThreadContext(containerConnector);
                Object parentWebContext = threadContext != null ? threadContext.getParentWebContext() : null;
                            
                configProcessors.subList(3, configProcessors.size()).stream().forEach(e -> {
                    
                    long currentThreadId = Thread.currentThread().getId();
                    
                    InitFacesContext initFacesContext = null;
                    if (currentThreadId != parentThreadId) {
                        Thread.currentThread().setContextClassLoader(parentContextClassLoader);
                        initFacesContext = InitFacesContext.getInstance(servletContext);
                        if (parentWebContext != null) {
                            threadContext.propagateWebContextToChild(parentWebContext);
                        }
                    
                    } else {
                        initFacesContext = facesContext;
                    }
                    
                    boolean hasKeyx = initFacesContext.getExternalContext().getApplicationMap().containsKey(RIConstants.FACES_PREFIX + "ApplicationAssociate");
                    
                    try {
                        e.process(servletContext, facesContext, facesDocuments2);
                    } catch (Exception e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    } finally {
                        if (currentThreadId != parentThreadId) {
                            Thread.currentThread().setContextClassLoader(null);
                            if (parentWebContext != null) {
                                threadContext.clearChildContext();
                            }
                        }
                        
                    }
                });
                
                
                if (!isFaceletsDisabled) {
                    faceletTaglibConfigProcessor.process(
                          servletContext, 
                          facesContext,
                          getConfigDocuments(
                              servletContext,
                              getFaceletConfigResourceProviders(),
                              executor,
                              validating));
                }

            } catch (Exception e) {
                // Clear out any configured factories
                releaseFactories();
                
                Throwable t = e;
                if (!(e instanceof ConfigurationException)) {
                    t = new ConfigurationException("CONFIGURATION FAILED! " + t.getMessage(), t);
                }
                
                throw (ConfigurationException)t;
            } finally {
                if (executor != null) {
                    executor.shutdown();
                }
                servletContext.removeAttribute(ANNOTATIONS_SCAN_TASK_KEY);
            }
        }

        DbfFactory.removeSchemaMap(servletContext);
    }
    
    
    
    // --------------------------------------------------------- Private Methods
    
    private ThreadContext getThreadContext(InjectionProvider containerConnector) {
        if (containerConnector instanceof ThreadContext) {
            return (ThreadContext) containerConnector;
        }
        
        return null;
    }
    
    
    private void initializeConfigProcessers(ServletContext servletContext, FacesContext facesContext) {
        configProcessors.stream().parallel().forEach(e -> e.initializeClassMetadataMap(servletContext, facesContext));
    }
    
    private List<ApplicationConfigurationPopulator> getConfigPopulators() {
        
        List<ApplicationConfigurationPopulator> configPopulators = new ArrayList<>();
        
        try {
            configPopulators.add( (ApplicationConfigurationPopulator)
                Class.forName("com.sun.faces.config.configpopulator.JsfRIRuntimePopulator_Generated")
                     .newInstance());
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e1) {
            throw new FacesException(e1);
        }
        
        ServiceLoader.load(ApplicationConfigurationPopulator.class)
                     .forEach(e -> configPopulators.add(e));
        
        return configPopulators;
    }
    
    private DOMImplementation createDOMImplementation() throws ParserConfigurationException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setNamespaceAware(true);
        
        return documentBuilderFactory.newDocumentBuilder()
                                     .getDOMImplementation();
    }
    
    private Document createEmptyFacesConfigDocument(DOMImplementation domImpl) {
        Document document = domImpl.createDocument(JAVAEE_XMLNS, "faces-config", null);
        
        Attr versionAttribute = document.createAttribute("version");
        versionAttribute.setValue("2.2");
        document.getDocumentElement()
                .getAttributes()
                .setNamedItem(versionAttribute);
        
        return document;
    }
    
    private List<DocumentInfo> getProgrammaticDocuments() throws ParserConfigurationException {
        
        List<DocumentInfo> programmaticDocuments = new ArrayList<>();
        
        DOMImplementation domImpl = createDOMImplementation();
        for (ApplicationConfigurationPopulator populator : getConfigPopulators()) {
            
            Document facesConfigDoc = createEmptyFacesConfigDocument(domImpl);
            
            try {
                populator.populateApplicationConfiguration(facesConfigDoc);
                
                programmaticDocuments.add(new DocumentInfo(facesConfigDoc, null));
            } catch (Throwable e) {
                if (LOGGER.isLoggable(INFO)) {
                    LOGGER.log(INFO, 
                        "{0} thrown when invoking {1}.populateApplicationConfigurationResources: {2}", 
                        new String [] {
                            e.getClass().getName(),
                            populator.getClass().getName(),
                            e.getMessage()
                        }
                    );
                }
            }
        }
        
        return programmaticDocuments;
    }
    
    private DocumentInfo[] mergeDocuments(DocumentInfo[] facesDocuments, List<DocumentInfo> programmaticDocuments) {
        
        if (programmaticDocuments.isEmpty()) {
            return facesDocuments;
        }
        
        if (isEmpty(facesDocuments)) {
            return programmaticDocuments.toArray(new DocumentInfo[0]);
        }
            
        List<DocumentInfo> mergedDocuments = new ArrayList<>(facesDocuments.length + programmaticDocuments.size());
        
        // The first programmaticDocuments element represents jsf-ri-runtime, 
        // and should be the first one in the merged list
        mergedDocuments.add(programmaticDocuments.get(0));
        
        // Copy the existing facesDocuments next to the merged list
        mergedDocuments.addAll(asList(facesDocuments));
        
        // Copy the programmaticDocuments next, but skip the first one as we've already added that
        mergedDocuments.addAll(programmaticDocuments.subList(1, programmaticDocuments.size()));
        
        return mergedDocuments.toArray(new DocumentInfo[0]);
    }
    
    
    /**
     * Execute the Task responsible for finding annotation classes
     * 
     */
    private void findAnnotations(DocumentInfo[] facesDocuments, InjectionProvider containerConnector, ServletContext servletContext, InitFacesContext context, ExecutorService executor) {
        
        ProvideMetadataToAnnotationScanTask taskMetadata = new ProvideMetadataToAnnotationScanTask(facesDocuments, containerConnector);
        
        Future<Map<Class<? extends Annotation>, Set<Class<?>>>> annotationScan;
        
        if (executor != null) {
            annotationScan = executor.submit(new AnnotationScanTask(servletContext, context, taskMetadata));
        } else {
            annotationScan = new FutureTask<>(new AnnotationScanTask(servletContext, context, taskMetadata));
            ((FutureTask<Map<Class<? extends Annotation>, Set<Class<?>>>>) annotationScan).run();
        }
        
        pushTaskToContext(servletContext, annotationScan);
    }

    /**
     * This method will remove any information about the application.
     * 
     * @param facesContext
     *            the <code>FacesContext</code> for the application that needs to be removed
     * @param servletContext
     *            the <code>ServletContext</code> for the application that needs to be removed
     */
    public void destroy(ServletContext servletContext, FacesContext facesContext) {
        configProcessors.stream().forEach(e -> e.destroy(servletContext, facesContext));
        initializedContexts.remove(servletContext);
    }

    /**
     * @param sc
     *            the <code>ServletContext</code> for the application in question
     * @return <code>true</code> if this application has already been initialized, otherwise returns </code>fase</code>
     */
    public boolean hasBeenInitialized(ServletContext sc) {
        return initializedContexts.contains(sc);
    }

    /**
     * @return the results of the annotation scan task
     */
    public static Map<Class<? extends Annotation>, Set<Class<?>>> getAnnotatedClasses(FacesContext ctx) {

        Map<String, Object> appMap = ctx.getExternalContext().getApplicationMap();

        @SuppressWarnings("unchecked")
        Future<Map<Class<? extends Annotation>, Set<Class<?>>>> scanTask = (Future<Map<Class<? extends Annotation>, Set<Class<?>>>>) 
            appMap.get(ANNOTATIONS_SCAN_TASK_KEY);
        
        try {
            return scanTask != null ? scanTask.get() : emptyMap();
        } catch (InterruptedException | ExecutionException e) {
            throw new FacesException(e);
        }

    }

    private static boolean useThreads(ServletContext ctx) {
        return WebConfiguration.getInstance(ctx).isOptionEnabled(EnableThreading);
    }

    private List<ConfigurationResourceProvider> getFacesConfigResourceProviders() {
        return getConfigurationResourceProviders(facesConfigProviders, FacesConfig);
    }

    private List<ConfigurationResourceProvider> getFaceletConfigResourceProviders() {
        return getConfigurationResourceProviders(facesletsTagLibConfigProviders, FaceletConfig);
    }

    private List<ConfigurationResourceProvider> getConfigurationResourceProviders(List<ConfigurationResourceProvider> defaultProviders, ConfigurationResourceProviderFactory.ProviderType providerType) {

        ConfigurationResourceProvider[] customProviders = createProviders(providerType);
        if (customProviders.length == 0) {
            return defaultProviders;
        } 
            
        List<ConfigurationResourceProvider> providers = new ArrayList<>(defaultProviders);
        
        // Insert the custom providers after the META-INF providers and
        // before those that scan /WEB-INF
        providers.addAll((defaultProviders.size() - 1), asList(customProviders));
        
        return unmodifiableList(providers);
    }

    /**
     * <p>
     * Sort the <code>faces-config</code> documents found on the classpath
     * and those specified by the <code>javax.faces.CONFIG_FILES</code> context
     * init parameter.
     * </p>
     *
     * @param facesDocuments an array of <em>all</em> <code>faces-config</code>
     *  documents
     * @param webInfFacesConfig FacesConfigInfo representing the WEB-INF/faces-config.xml
     *  for this app
     *
     * @return the sorted documents
     */
    private DocumentInfo[] sortDocuments(DocumentInfo[] facesDocuments, FacesConfigInfo webInfFacesConfig) {

        int len = webInfFacesConfig.isWebInfFacesConfig() ? facesDocuments.length - 1 : facesDocuments.length;

        List<String> absoluteOrdering = webInfFacesConfig.getAbsoluteOrdering();

        if (len > 1) {
            List<DocumentOrderingWrapper> list = new ArrayList<>();
            for (int i = 1; i < len; i++) {
                list.add(new DocumentOrderingWrapper(facesDocuments[i]));
            }
            
            DocumentOrderingWrapper[] ordering = list.toArray(new DocumentOrderingWrapper[list.size()]);
            if (absoluteOrdering == null) {
                DocumentOrderingWrapper.sort(ordering);
                
                // Sorting complete, now update the appropriate locations within
                // the original array with the sorted documentation.
                for (int i = 1; i < len; i++) {
                    facesDocuments[i] = ordering[i - 1].getDocument();
                }
                
                return facesDocuments;
            } else {
                DocumentOrderingWrapper[] result = DocumentOrderingWrapper.sort(ordering, absoluteOrdering);
                DocumentInfo[] ret = new DocumentInfo[
                                          webInfFacesConfig.isWebInfFacesConfig() ? 
                                          result.length + 2 : 
                                          result.length + 1];
                
                for (int i = 1; i < len; i++) {
                    ret[i] = result[i - 1].getDocument();
                }
                
                // Add the impl specific config file
                ret[0] = facesDocuments[0];
                
                // Add the WEB-INF if necessary
                if (webInfFacesConfig.isWebInfFacesConfig()) {
                    ret[ret.length - 1] = facesDocuments[facesDocuments.length - 1];
                }
                return ret;
            }
        }

        return facesDocuments;
    }
    
    /**
     * Utility method to check if JSF 2.0 Facelets should be disabled, but that doesn't perform <em>the</em> 
     * check unless <code>lastFacesConfigInfo</code> is indeed *the* WEB-INF/faces-config.xml
     * 
     * @param webConfig configuration for this application
     * @param lastFacesConfigInfo object representing WEB-INF/faces-config.xml
     * @return <code>true</code> if Facelets should be disabled
     */
    private boolean isFaceletsDisabled(WebConfiguration webConfig, FacesConfigInfo lastFacesConfigInfo) {
        if (lastFacesConfigInfo.isWebInfFacesConfig()) {
            return _isFaceletsDisabled(webConfig, lastFacesConfigInfo);
        } 
            
        return 
            webConfig.isOptionEnabled(DisableFaceletJSFViewHandler) || 
            webConfig.isOptionEnabled(DisableFaceletJSFViewHandlerDeprecated);
    }


    /**
     * Utility method to check if JSF 2.0 Facelets should be disabled.
     * 
     * <p>
     * If it's not explicitly disabled by the context init parameter, then
     * check the version of the WEB-INF/faces-config.xml document.  If the version
     * is less than 2.0, then override the default value for the context init
     * parameter so that other parts of the system that use that config option
     * will know it has been disabled.
     * </p>
     *
     * <p>
     * <em>NOTE:</em> Since this method overrides a configuration value, it should
     * be called before *any* document parsing is performed the configuration
     * value may be queried by the <code>ConfigParser</code>s.
     * </p>
     *
     * @param webconfig configuration for this application
     * @param facesConfigInfo object representing WEB-INF/faces-config.xml
     * @return <code>true</code> if Facelets should be disabled
     */
    private boolean _isFaceletsDisabled(WebConfiguration webconfig, FacesConfigInfo facesConfigInfo) {

        boolean isFaceletsDisabled = 
            webconfig.isOptionEnabled(DisableFaceletJSFViewHandler) ||
            webconfig.isOptionEnabled(DisableFaceletJSFViewHandlerDeprecated);
        
        if (!isFaceletsDisabled) {
            // if not explicitly disabled, make a sanity check against
            // /WEB-INF/faces-config.xml
            isFaceletsDisabled = !facesConfigInfo.isVersionGreaterOrEqual(2.0);
            webconfig.overrideContextInitParameter(DisableFaceletJSFViewHandler, isFaceletsDisabled);
        }
        
        return isFaceletsDisabled;
    }


    /**
     * Push the provided <code>Future</code> to the specified <code>ServletContext</code>.
     */
    private void pushTaskToContext(ServletContext sc, Future<Map<Class<? extends Annotation>, Set<Class<?>>>> scanTask) {
        sc.setAttribute(ANNOTATIONS_SCAN_TASK_KEY, scanTask);
    }

    /**
     * Publishes a {@link javax.faces.event.PostConstructApplicationEvent} event for the current
     * {@link Application} instance.
     */
    void publishPostConfigEvent() {

        FacesContext ctx = FacesContext.getCurrentInstance();
        Application app = ctx.getApplication();
        
        if (null == ((InitFacesContext) ctx).getELContext()) {
            ELContext elContext = new ELContextImpl(app.getELResolver());
            elContext.putContext(FacesContext.class, ctx);
            ExpressionFactory exFactory = ELUtils.getDefaultExpressionFactory(ctx);
            if (null != exFactory) {
                elContext.putContext(ExpressionFactory.class, exFactory);
            }
            
            UIViewRoot root = ctx.getViewRoot();
            if (null != root) {
                elContext.setLocale(root.getLocale());
            }
            
            ELContextListener[] listeners = app.getELContextListeners();
            if (listeners.length > 0) {
                ELContextEvent event = new ELContextEvent(elContext);
                for (ELContextListener listener : listeners) {
                    listener.contextCreated(event);
                }
            }
            
            ((InitFacesContext) ctx).setELContext(elContext);
        }

        app.publishEvent(ctx, PostConstructApplicationEvent.class, Application.class, app);
    }


    /**
     * <p>
     *   Obtains an array of <code>Document</code>s to be processed
     * </p>
     *
     * @param servletContext the <code>ServletContext</code> for the application to be
     *  processed
     * @param providers <code>List</code> of <code>ConfigurationResourceProvider</code>
     *  instances that provide the URL of the documents to parse.
     * @param executor the <code>ExecutorService</code> used to dispatch parse
     *  request to
     * @param validating flag indicating whether or not the documents
     *  should be validated
     * @return an array of <code>DocumentInfo</code>s
     */
    private static DocumentInfo[] getConfigDocuments(ServletContext servletContext, List<ConfigurationResourceProvider> providers, ExecutorService executor, boolean validating) {
        
        // Query all configuration providers to give us a URL to the configuration they are providing
        
        List<FutureTask<Collection<URI>>> uriTasks = new ArrayList<>(providers.size());
        
        for (ConfigurationResourceProvider provider : providers) {
            FutureTask<Collection<URI>> uriTask = new FutureTask<>(new URITask(provider, servletContext));
            uriTasks.add(uriTask);
            
            if (executor != null) {
                executor.execute(uriTask);
            } else {
                uriTask.run();
            }
        }

        // Load and XML parse all documents to which the URLs that we collected above point to
        
        List<FutureTask<DocumentInfo>> docTasks = new ArrayList<>(providers.size() << 1);

        for (FutureTask<Collection<URI>> uriTask : uriTasks) {
            try {
                for (URI uri : uriTask.get()) {
                    FutureTask<DocumentInfo> docTask = new FutureTask<>(new ParseTask(servletContext, validating, uri));
                    docTasks.add(docTask);
                    
                    if (executor != null) {
                        executor.execute(docTask);
                    } else {
                        docTask.run();
                    }
                }
            } catch (InterruptedException ignored) {
            } catch (Exception e) {
                throw new ConfigurationException(e);
            }
        }

        // Collect the results of the documents we parsed above 
        
        List<DocumentInfo> docs = new ArrayList<>(docTasks.size());
        for (FutureTask<DocumentInfo> docTask : docTasks) {
            try {
                docs.add(docTask.get());
            } catch (ExecutionException e) {
                throw new ConfigurationException(e);
            } catch (InterruptedException ignored) {
            }
        }

        return docs.toArray(new DocumentInfo[docs.size()]);
    }


    /**
     * Create a new <code>ExecutorService</code> with
     * {@link #NUMBER_OF_TASK_THREADS} threads.
     */
    private static ExecutorService createExecutorService() {

        int tc = Runtime.getRuntime().availableProcessors();
        if (tc > NUMBER_OF_TASK_THREADS) {
            tc = NUMBER_OF_TASK_THREADS;
        }
        
        try {
            return (ExecutorService) new InitialContext().lookup("java:comp/env/concurrent/ThreadPool");
        } catch (NamingException e) {
            // Ignore
        }
        
        return Executors.newFixedThreadPool(tc);
    }

    /**
     * Calls through to {@link javax.faces.FactoryFinder#releaseFactories()}
     * ignoring any exceptions.
     */
    private void releaseFactories() {
        try {
            FactoryFinder.releaseFactories();
        } catch (FacesException ignored) {
            LOGGER.log(FINE, "Exception thrown from FactoryFinder.releaseFactories()", ignored);
        }
    }


    // ----------------------------------------------------------- Inner Classes

    private static final class ProvideMetadataToAnnotationScanTask {
        
        DocumentInfo [] documentInfos;
        InjectionProvider containerConnector;
        Set<URI> uris;
        Set<String> jarNames;

        private ProvideMetadataToAnnotationScanTask(DocumentInfo [] documentInfos, InjectionProvider containerConnector) {
            this.documentInfos = documentInfos;
            this.containerConnector = containerConnector;
        }

        private void initializeIvars(Set<Class<?>> annotatedSet) {
            if (uris != null || jarNames != null) {
                assert(null != uris && null != jarNames);
                return;
            }
            
            uris = new HashSet<>(documentInfos.length);
            jarNames = new HashSet<>(documentInfos.length);
            
            for (DocumentInfo docInfo : documentInfos) {
                
                URI sourceURI = docInfo.getSourceURI();
                Matcher jarMatcher = JAR_PATTERN.matcher(sourceURI == null ? "" : sourceURI.toString());
                
                if (jarMatcher.matches()) {
                    String jarName = jarMatcher.group(2);
                    if (!jarNames.contains(jarName)) {
                        FacesConfigInfo configInfo = new FacesConfigInfo(docInfo);
                        if (!configInfo.isMetadataComplete()) {
                            uris.add(sourceURI);
                            jarNames.add(jarName);
                        } else {
                            /*
                             * Because the container annotation scanning does not 
                             * know anything about faces-config.xml metadata-complete
                             * the annotatedSet of classes will include classes that
                             * are not supposed to be included.
                             *
                             * The code below looks at the CodeSource of the class
                             * and determines whether or not it should be removed
                             * from the annotatedSet because the faces-config.xml that
                             * owns it has metadata-complete="true".
                             */
                            ArrayList<Class<?>> toRemove = new ArrayList<>(1);
                            String sourceURIString = sourceURI.toString();
                            if (annotatedSet != null) {
                                for (Class<?> clazz : annotatedSet) {
                                    if (sourceURIString.contains(clazz.getProtectionDomain().getCodeSource().getLocation().toString())) {
                                        toRemove.add(clazz);
                                    }
                                }
                                annotatedSet.removeAll(toRemove);
                            }
                        }
                    }
                }
            }
        }

        private Set<URI> getAnnotationScanURIs(Set<Class<?>> annotatedSet) {
            initializeIvars(annotatedSet);

            return uris;

        }

        private Set<String> getJarNames(Set<Class<?>> annotatedSet) {
            initializeIvars(annotatedSet);

            return jarNames;
        }

        private com.sun.faces.spi.AnnotationScanner getAnnotationScanner() {
            com.sun.faces.spi.AnnotationScanner result = null;
            if (this.containerConnector instanceof com.sun.faces.spi.AnnotationScanner) {
                result = (com.sun.faces.spi.AnnotationScanner) this.containerConnector;
            }
            return result;
        }
    }


    /**
     * Scans the class files within a web application returning a <code>Set</code>
     * of classes that have been annotated with a standard Faces annotation.
     */
    private static class AnnotationScanTask implements Callable<Map<Class<? extends Annotation>,Set<Class<?>>>> {

        private InitFacesContext facesContext;
        private AnnotationProvider provider;
        private ProvideMetadataToAnnotationScanTask metadataGetter;
        private Set<Class<?>> annotatedSet;

        
        // -------------------------------------------------------- Constructors


        @SuppressWarnings("unchecked")
        public AnnotationScanTask(ServletContext servletContext, InitFacesContext facesContext, ProvideMetadataToAnnotationScanTask metadataGetter) {
            this.facesContext = facesContext;
            this.provider = AnnotationProviderFactory.createAnnotationProvider(servletContext);
            this.metadataGetter = metadataGetter;
            this.annotatedSet = (Set<Class<?>>) servletContext.getAttribute(ANNOTATED_CLASSES);
        }


        // ----------------------------------------------- Methods from Callable


        @Override
        public Map<Class<? extends Annotation>, Set<Class<?>>> call() throws Exception {

            Timer t = Timer.getInstance();
            if (t != null) {
                t.startTiming();
            }

            // We are executing on a different thread.
            facesContext.addInitContextEntryForCurrentThread();
            Set<URI> scanUris = null;
            com.sun.faces.spi.AnnotationScanner annotationScanner = metadataGetter.getAnnotationScanner();

            // This is where we discover what kind of InjectionProvider we have.
            if (provider instanceof DelegatingAnnotationProvider && annotationScanner != null) {
                // This InjectionProvider is capable of annotation scanning *and*
                // injection.
                ((DelegatingAnnotationProvider) provider).setAnnotationScanner(annotationScanner, metadataGetter.getJarNames(annotatedSet));
                scanUris = emptySet();
            } else {
                // This InjectionProvider is capable of annotation scanning only
                scanUris = metadataGetter.getAnnotationScanURIs(annotatedSet);
            }
            
            // AnnotationScanner scanner = new AnnotationScanner(sc);
            Map<Class<? extends Annotation>, Set<Class<?>>> annotatedClasses = provider.getAnnotatedClasses(scanUris);

            if (t != null) {
                t.stopTiming();
                t.logResult("Configuration annotation scan complete.");
            }

            return annotatedClasses;
        }

    } // END AnnotationScanTask


    /**
     * <p>
     *  This <code>Callable</code> will be used by {@link ConfigManager#getConfigDocuments(javax.servlet.ServletContext, java.util.List, java.util.concurrent.ExecutorService, boolean)}.
     *  It represents a single configuration resource to be parsed into a DOM.
     * </p>
     */
    private static class ParseTask implements Callable<DocumentInfo> {
        
        private static final String JAVAEE_SCHEMA_LEGACY_DEFAULT_NS = "http://java.sun.com/xml/ns/javaee";
        private static final String JAVAEE_SCHEMA_DEFAULT_NS = "http://xmlns.jcp.org/xml/ns/javaee";
        private static final String EMPTY_FACES_CONFIG = "com/sun/faces/empty-faces-config.xml";
        private static final String FACES_CONFIG_TAGNAME = "faces-config";
        private static final String FACELET_TAGLIB_TAGNAME = "facelet-taglib";
        private ServletContext servletContext;
        private URI documentURI;
        private DocumentBuilderFactory factory;
        private boolean validating;
        
        // -------------------------------------------------------- Constructors


        /**
         * <p>
         *   Constructs a new ParseTask instance
         * </p>
         *
         * @param servletContext the servlet context.
         * @param validating whether or not we're validating
         * @param documentURI a URL to the configuration resource to be parsed
         * @throws Exception general error
         */
        public ParseTask(ServletContext servletContext, boolean validating, URI documentURI) throws Exception {
            this.servletContext = servletContext;
            this.documentURI = documentURI;
            this.validating = validating;
        }


        // ----------------------------------------------- Methods from Callable


        /**
         * @return the result of the parse operation (a DOM)
         * @throws Exception if an error occurs during the parsing process
         */
        @Override
        public DocumentInfo call() throws Exception {

            try {
                Timer timer = Timer.getInstance();
                if (timer != null) {
                    timer.startTiming();
                }

                Document document = getDocument();

                if (timer != null) {
                    timer.stopTiming();
                    timer.logResult("Parse " + documentURI.toURL().toExternalForm());
                }

                return new DocumentInfo(document, documentURI);
            } catch (Exception e) {
                throw new ConfigurationException(
                        format("Unable to parse document ''{0}'': {1}", documentURI.toURL().toExternalForm(), e.getMessage()),
                        e);
            }
        }


        // ----------------------------------------------------- Private Methods


        /**
         * @return <code>Document</code> based on <code>documentURI</code>.
         * @throws Exception if an error occurs during the process of building a
         *  <code>Document</code>
         */
        private Document getDocument() throws Exception {

            Document returnDoc;
            DocumentBuilder db = getNonValidatingBuilder();
            URL documentURL = documentURI.toURL();
            InputSource is = new InputSource(getInputStream(documentURL));
            is.setSystemId(documentURI.toURL().toExternalForm());
            Document doc = null;

            try {
                doc = db.parse(is);
            } catch (SAXParseException spe) {
                // [mojarra-1693]
                // Test if this is a zero length or whitespace only faces-config.xml file.
                // If so, just make an empty Document
                InputStream stream = is.getByteStream();
                stream.close();

                is = new InputSource(getInputStream(documentURL));
                stream = is.getByteStream();
                if (streamIsZeroLengthOrEmpty(stream) &&
                    documentURL.toExternalForm().endsWith("faces-config.xml")) {
                    ClassLoader loader = this.getClass().getClassLoader();
                    is = new InputSource(getInputStream(loader.getResource(EMPTY_FACES_CONFIG)));
                    doc = db.parse(is);
                } 

            }
            
            String documentNS = null;
            if (null == doc) {
                if (FacesFlowDefinitionConfigProcessor.uriIsFlowDefinition(documentURI)) {
                    documentNS = RIConstants.JAVAEE_XMLNS;
                    doc = FacesFlowDefinitionConfigProcessor.synthesizeEmptyFlowDefinition(documentURI);
                }
            } else {
                Element documentElement = doc.getDocumentElement();
                documentNS = documentElement.getNamespaceURI();
                String rootElementTagName = documentElement.getTagName();
                
                boolean isNonFacesConfigDocument = !FACES_CONFIG_TAGNAME.equals(rootElementTagName) &&
                        !FACELET_TAGLIB_TAGNAME.equals(rootElementTagName);
                
                if (isNonFacesConfigDocument) {
                    ClassLoader loader = this.getClass().getClassLoader();
                    is = new InputSource(getInputStream(loader.getResource(EMPTY_FACES_CONFIG)));
                    doc = db.parse(is);
                    if (LOGGER.isLoggable(Level.WARNING)) {
                        LOGGER.log(Level.WARNING, MessageFormat.format("Config document {0} with namespace URI {1} is not a faces-config or facelet-taglib file.  Ignoring.", 
                                documentURI.toURL().toExternalForm(),
                                documentNS));
                    }
                    return doc;
                }
            }
            
            if (validating && documentNS != null) {
                DOMSource domSource = new DOMSource(doc, documentURL.toExternalForm());

                /*
                 * If the Document in question is 1.2 (i.e. it has a namespace matching
                 * JAVAEE_SCHEMA_DEFAULT_NS, then perform validation using the cached schema
                 * and return.  Otherwise we assume a 1.0 or 1.1 faces-config in which case
                 * we need to transform it to reference a special 1.1 schema before validating.
                 */
                Node documentElement = ((Document) domSource.getNode()).getDocumentElement();
                switch (documentNS) {
                    case JAVAEE_SCHEMA_DEFAULT_NS:
                        {
                            Attr version = (Attr)
                                    documentElement.getAttributes().getNamedItem("version");
                            Schema schema;
                            if (version != null) {
                                String versionStr = version.getValue();
                                switch (versionStr) {
                                    case "2.2":
                                        if ("facelet-taglib".equals(documentElement.getLocalName())) {
                                            schema = DbfFactory.getSchema(servletContext, DbfFactory.FacesSchema.FACELET_TAGLIB_22);
                                        } else {
                                            schema = DbfFactory.getSchema(servletContext, DbfFactory.FacesSchema.FACES_22);
                                        }   break;
                                    case "2.3":
                                        if ("facelet-taglib".equals(documentElement.getLocalName())) {
                                            schema = DbfFactory.getSchema(servletContext, DbfFactory.FacesSchema.FACELET_TAGLIB_22);
                                        } else {
                                            schema = DbfFactory.getSchema(servletContext, DbfFactory.FacesSchema.FACES_23);
                                        }   break;
                                    default:
                                        throw new ConfigurationException("Unknown Schema version: " + versionStr);
                                }
                                DocumentBuilder builder = getBuilderForSchema(schema);
                                if (builder.isValidating()) {
                                    builder.getSchema().newValidator().validate(domSource);
                                    returnDoc = ((Document) domSource.getNode());
                                } else {
                                    returnDoc = ((Document) domSource.getNode());
                                }
                            } else {
                                // this shouldn't happen, but...
                                throw new ConfigurationException("No document version available.");
                            }       break;
                        }
                    case JAVAEE_SCHEMA_LEGACY_DEFAULT_NS:
                    {
                        Attr version = (Attr)
                                documentElement.getAttributes().getNamedItem("version");
                        Schema schema;
                        if (version != null) {
                            String versionStr = version.getValue();
                            switch (versionStr) {
                                case "2.0":
                                    if ("facelet-taglib".equals(documentElement.getLocalName())) {
                                        schema = DbfFactory.getSchema(servletContext, DbfFactory.FacesSchema.FACELET_TAGLIB_20);
                                    } else {
                                        schema = DbfFactory.getSchema(servletContext, DbfFactory.FacesSchema.FACES_20);
                                    }   break;
                                case "2.1":
                                    if ("facelet-taglib".equals(documentElement.getLocalName())) {
                                        schema = DbfFactory.getSchema(servletContext, DbfFactory.FacesSchema.FACELET_TAGLIB_20);
                                    } else {
                                        schema = DbfFactory.getSchema(servletContext, DbfFactory.FacesSchema.FACES_21);
                                    }   break;
                                case "1.2":
                                    schema = DbfFactory.getSchema(servletContext, DbfFactory.FacesSchema.FACES_12);
                                    break;
                                default:
                                    throw new ConfigurationException("Unknown Schema version: " + versionStr);
                            }
                            DocumentBuilder builder = getBuilderForSchema(schema);
                            if (builder.isValidating()) {
                                builder.getSchema().newValidator().validate(domSource);
                                returnDoc = ((Document) domSource.getNode());
                            } else {
                                returnDoc = ((Document) domSource.getNode());
                            }
                        } else {
                            // this shouldn't happen, but...
                            throw new ConfigurationException("No document version available.");
                        }       break;
                    }
                    default:
                        DOMResult domResult = new DOMResult();
                        Transformer transformer = getTransformer(documentNS);
                        transformer.transform(domSource, domResult);
                        
                        // copy the source document URI to the transformed result
                        // so that processes that need to build URLs relative to the
                        // document will work as expected.
                        ((Document) domResult.getNode()).setDocumentURI(((Document) domSource.getNode()).getDocumentURI());
                        Schema schemaToApply;
                        
                        switch (documentNS) {
                            case FACES_CONFIG_1_X_DEFAULT_NS:
                                schemaToApply = DbfFactory.getSchema(servletContext, DbfFactory.FacesSchema.FACES_11);
                                break;
                            case FACELETS_1_0_DEFAULT_NS:
                                schemaToApply = DbfFactory.getSchema(servletContext, DbfFactory.FacesSchema.FACELET_TAGLIB_20);
                                break;
                            default:
                                throw new IllegalStateException();
                        }
                        
                        DocumentBuilder builder = getBuilderForSchema(schemaToApply);
                        if (builder.isValidating()) {
                            builder.getSchema().newValidator().validate(new DOMSource(domResult.getNode()));
                            returnDoc = (Document) domResult.getNode();
                        } else {
                            returnDoc = (Document) domResult.getNode();
                        }
                }
            } else {
                returnDoc = doc;
            }

            // mark this document as the parsed representation of the
            // WEB-INF/faces-config.xml.  This is used later in the configuration
            // processing.
            if (documentURL.toExternalForm().contains("/WEB-INF/faces-config.xml")) {
                Attr webInf = returnDoc.createAttribute(WEB_INF_MARKER);
                webInf.setValue("true");
                returnDoc.getDocumentElement().getAttributes().setNamedItem(webInf);
            }
            
            return returnDoc;

        }

        private boolean streamIsZeroLengthOrEmpty(InputStream is) throws IOException {
            boolean isZeroLengthOrEmpty = (0 == is.available());
            final int size = 1024;
            byte[] b = new byte[size];
            String s;
            while (!isZeroLengthOrEmpty && -1 != is.read(b, 0, size)) {
                s = (new String(b, RIConstants.CHAR_ENCODING)).trim();
                isZeroLengthOrEmpty = 0 == s.length();
                b[0] = 0;
                for (int i = 1; i < size; i += i) {
                    System.arraycopy(b, 0, b, i, ((size - i) < i) ? (size - i) : i);
                }
            }

            return isZeroLengthOrEmpty;
        }

        /**
         * Obtain a <code>Transformer</code> using the style sheet
         * referenced by the <code>XSL</code> constant.
         *
         * @return a new Tranformer instance
         * @throws Exception if a Tranformer instance could not be created
         */
        private static Transformer getTransformer(String documentNS) throws Exception {

            TransformerFactory factory = createTransformerFactory();

            String xslToApply;
            switch (documentNS) {
                case FACES_CONFIG_1_X_DEFAULT_NS:
                    xslToApply = FACES_TO_1_1_PRIVATE_XSL;
                    break;
                case FACELETS_1_0_DEFAULT_NS:
                    xslToApply = FACELETS_TO_2_0_XSL;
                    break;
                default:
                    throw new IllegalStateException();
            }
            
            return factory.newTransformer(new StreamSource(getInputStream(ConfigManager.class.getResource(xslToApply))));
        }


        /**
         * @return an <code>InputStream</code> to the resource referred to by
         *         <code>url</code>
         * @param url source <code>URL</code>
         * @throws IOException if an error occurs
         */
        private static InputStream getInputStream(URL url) throws IOException {
            URLConnection connection = url.openConnection();
            connection.setUseCaches(false);
            
            return new BufferedInputStream(connection.getInputStream());
        }

        private DocumentBuilder getNonValidatingBuilder() throws Exception {
            DocumentBuilderFactory tFactory = DbfFactory.getFactory();
            tFactory.setValidating(false);
            
            DocumentBuilder tBuilder = tFactory.newDocumentBuilder();
            tBuilder.setEntityResolver(FACES_ENTITY_RESOLVER);
            tBuilder.setErrorHandler(FACES_ERROR_HANDLER);
            
            return tBuilder;
        }

        private DocumentBuilder getBuilderForSchema(Schema schema) throws Exception {
            this.factory = DbfFactory.getFactory();

            try {
                factory.setSchema(schema);
            } catch (UnsupportedOperationException upe) {
                return getNonValidatingBuilder();
            }
            
            DocumentBuilder builder = factory.newDocumentBuilder();
            builder.setEntityResolver(FACES_ENTITY_RESOLVER);
            builder.setErrorHandler(FACES_ERROR_HANDLER);
            
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
    private static class URITask implements Callable<Collection<URI>> {

        private ConfigurationResourceProvider provider;
        private ServletContext sc;


        // -------------------------------------------------------- Constructors


        /**
         * Constructs a new <code>URITask</code> instance.
         * @param provider the <code>ConfigurationResourceProvider</code> from
         *  which zero or more <code>URL</code>s will be returned
         * @param sc the <code>ServletContext</code> of the current application
         */
        public URITask(ConfigurationResourceProvider provider, ServletContext sc) {
            this.provider = provider;
            this.sc = sc;
        }


        // ----------------------------------------------- Methods from Callable


        /**
         * @return zero or more <code>URL</code> instances
         * @throws Exception if an Exception is thrown by the underlying
         *  <code>ConfigurationResourceProvider</code> 
         */
        @SuppressWarnings("unchecked")
        @Override
        public Collection<URI> call() throws Exception {
            Collection<?> untypedCollection = provider.getResources(sc);
            Iterator<?> untypedCollectionIterator = untypedCollection.iterator();
            
            Collection<URI> result = emptyList();
            
            if (untypedCollectionIterator.hasNext()) {
                Object cur = untypedCollectionIterator.next();
                
                // Account for older versions of the provider that return Collection<URL>.
                if (cur instanceof URL) {
                    result = new ArrayList<>(untypedCollection.size());
                    result.add(new URI(((URL)cur).toExternalForm()));
                    while (untypedCollectionIterator.hasNext()) {
                        cur = untypedCollectionIterator.next();
                        result.add(new URI(((URL)cur).toExternalForm()));
                    }
                } else {
                    result = (Collection<URI>) untypedCollection;
                }
            }

            return result;
        }

    } // END URITask


}

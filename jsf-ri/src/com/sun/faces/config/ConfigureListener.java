/*
 * $Id: ConfigureListener.java,v 1.87 2006/11/06 19:59:15 rlubke Exp $
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
 * Copyright 2005 Sun Microsystems Inc. All Rights Reserved
 */

package com.sun.faces.config;

import javax.el.CompositeELResolver;
import javax.el.ELResolver;
import javax.el.ExpressionFactory;
import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;
import javax.faces.application.NavigationHandler;
import javax.faces.application.StateManager;
import javax.faces.application.ViewHandler;
import javax.faces.context.ExternalContext;
import javax.faces.el.PropertyResolver;
import javax.faces.el.VariableResolver;
import javax.faces.event.ActionListener;
import javax.faces.event.PhaseListener;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.LifecycleFactory;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.faces.render.Renderer;
import javax.faces.webapp.FacesServlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.jsp.JspApplicationContext;
import javax.servlet.jsp.JspFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import com.sun.faces.RIConstants;
import com.sun.faces.application.ApplicationAssociate;
import com.sun.faces.application.ConfigNavigationCase;
import com.sun.faces.config.WebConfiguration.BooleanWebContextInitParameter;
import com.sun.faces.config.WebConfiguration.WebContextInitParameter;
import com.sun.faces.config.beans.ApplicationBean;
import com.sun.faces.config.beans.ComponentBean;
import com.sun.faces.config.beans.ConverterBean;
import com.sun.faces.config.beans.FacesConfigBean;
import com.sun.faces.config.beans.FactoryBean;
import com.sun.faces.config.beans.LifecycleBean;
import com.sun.faces.config.beans.LocaleConfigBean;
import com.sun.faces.config.beans.ManagedBeanBean;
import com.sun.faces.config.beans.NavigationCaseBean;
import com.sun.faces.config.beans.NavigationRuleBean;
import com.sun.faces.config.beans.RenderKitBean;
import com.sun.faces.config.beans.RendererBean;
import com.sun.faces.config.beans.ResourceBundleBean;
import com.sun.faces.config.beans.ValidatorBean;
import com.sun.faces.config.rules.FacesConfigRuleSet;
import com.sun.faces.el.ChainAwareVariableResolver;
import com.sun.faces.el.DummyPropertyResolverImpl;
import com.sun.faces.el.DummyVariableResolverImpl;
import com.sun.faces.el.FacesCompositeELResolver;
import com.sun.faces.renderkit.JsfJsResourcePhaseListener;
import com.sun.faces.spi.ManagedBeanFactory;
import com.sun.faces.util.MessageUtils;
import com.sun.faces.util.Util;
import com.sun.faces.util.ReflectionUtils;
import com.sun.org.apache.commons.digester.Digester;

/**
 * <p>Parse all relevant JavaServer Faces configuration resources, and
 * configure the Reference Implementation runtime environment.</p>
 * <p/>
 */
@SuppressWarnings({"ForLoopReplaceableByForEach"}) 
public class ConfigureListener implements ServletContextListener {


    // -------------------------------------------------------- Static Variables

    /**
     * <p><code>ServletContext</code> attribute key.</p>
     */
    protected static final String FACES_CONFIG_BEAN_KEY =
        RIConstants.FACES_PREFIX + "FACES_CONFIG_BEAN";

    /**
     * <p>The path to the RI main configuration file.</p>
     */
    protected static final String JSF_RI_CONFIG =
            "com/sun/faces/jsf-ri-runtime.xml";    

    /**
     * <p>The resource path for faces-config files included in the
     * <code>META-INF</code> directory of JAR files.</p>
     */
    protected static final String META_INF_RESOURCES =
            "META-INF/faces-config.xml";

    /**
     * <p>The resource path for the faces configuration in the
     * <code>WEB-INF</code> directory of an application.</p>
     */
    protected static final String WEB_INF_RESOURCE =
            "/WEB-INF/faces-config.xml";
        
    
    protected WebConfiguration webConfig;

    /**
     * <p>All known factory names.</p>
     */
    private static final String[] FACTORY_NAMES = {
        FactoryFinder.APPLICATION_FACTORY,
        FactoryFinder.FACES_CONTEXT_FACTORY,
        FactoryFinder.LIFECYCLE_FACTORY,
        FactoryFinder.RENDER_KIT_FACTORY
    };

    /**
     * <p>Array of known primitive types.</p>
     */
    private static final Class[] PRIM_CLASSES_TO_CONVERT = {
        java.lang.Boolean.TYPE,
        java.lang.Byte.TYPE,
        java.lang.Character.TYPE,
        java.lang.Double.TYPE,
        java.lang.Float.TYPE,
        java.lang.Integer.TYPE,
        java.lang.Long.TYPE,
        java.lang.Short.TYPE
    };

    /**
     * <p>Array of known converters for primitive types.</p>
     */
    private static final String[] CONVERTERS_FOR_PRIMS = {
        "javax.faces.convert.BooleanConverter",
        "javax.faces.convert.ByteConverter",
        "javax.faces.convert.CharacterConverter",
        "javax.faces.convert.DoubleConverter",
        "javax.faces.convert.FloatConverter",
        "javax.faces.convert.IntegerConverter",
        "javax.faces.convert.LongConverter",
        "javax.faces.convert.ShortConverter"
    };


    /**
     * <p>The set of <code>ClassLoader</code> instances that have
     * already been configured by this <code>ConfigureListener</code>.</p>
     */
    private static final Set<ClassLoader> LOADERS = new HashSet<ClassLoader>();


    /**
     * <p>The <code>Log</code> instance for this class.</p>
     */    
    private static final Logger LOGGER = Util.getLogger(Util.FACES_LOGGER 
            + Util.CONFIG_LOGGER);   

    private ArrayList<ELResolver> elResolversFromFacesConfig = null; 
    private Application application;
    private ApplicationAssociate associate;
   
    
    // ------------------------------------------ ServletContextListener Methods

    /**
     * <p>This ivar is used to convey the ServletContext instance to the
     * ApplicationAssociate ctor, which is executed when the Application
     * is instantiated.  Note that this data bridge is only used by the
     * Sun RI ApplicationImpl.  If the user replaces ApplicationFactory,
     * and chooses to decorate the existing Application instance, this
     * data bridge is used.  However, if the user replaces
     * ApplicationFactory and entirely replaces the ApplicationInstance,
     * this data-bridge is not used. </p>
     *
     */

    private static ThreadLocal<ServletContextAdapter> tlsExternalContext =
          new ThreadLocal<ServletContextAdapter>() {
              protected ServletContextAdapter initialValue() {
                  return (null);
              }
          };

    static ThreadLocal getThreadLocalExternalContext() {
        if (Util.isUnitTestModeEnabled()) {
            return tlsExternalContext;
        }
        return null;
    }

    /**
     * A subclass of ConfigureListener can override isFeatureEnabled and reset
     * one of the boolean values.  This method lets the user know that this 
     * happened for a particular feature.
     */
    public void logOverriddenContextConfigValues() {
        for (BooleanWebContextInitParameter param : BooleanWebContextInitParameter.values()) {

            if (webConfig.getBooleanContextInitParameter(param) != isFeatureEnabled(param)) {
                if (LOGGER.isLoggable(Level.INFO)) {
                    LOGGER.log(Level.INFO,
                               (isFeatureEnabled(param)
                                ? "jsf.config.webconfig.configinfo.reset.enabled"
                                : "jsf.config.webconfig.configinfo.reset.disabled"),
                               new Object[]{webConfig.getServletContextName(),
                                            param.getQualifiedName()});
                }
            }
        }
    }

    /**
     * <p>During the execution of {@link #contextInitialized}, this
     * method will return the ServletContext instance.</p>
     */

    public static ExternalContext getExternalContextDuringInitialize() {
        return tlsExternalContext.get();
    }   

    public void contextInitialized(ServletContextEvent sce) {        
        ServletContext context = sce.getServletContext();
        webConfig = WebConfiguration.getInstance(context);
        logOverriddenContextConfigValues();
        Digester digester = null;
        boolean initialized = false;
        
        // store the servletContext instance in Thread local Storage.
        // This enables our Application's ApplicationAssociate to locate
        // it so it can store the ApplicationAssociate in the
        // ServletContext.
        tlsExternalContext.set(new ServletContextAdapter(context));

        try {
            
            if (LOGGER.isLoggable(Level.INFO)) {
                LOGGER.log(Level.INFO,
                           "jsf.config.listener.version",
                           getServletContextIdentifier(context));
            }

            // Check to see if the FacesServlet is present in the
            // web.xml.   If it is, perform faces configuration as normal,
            // otherwise, simply return.
            if (!isFeatureEnabled(BooleanWebContextInitParameter.ForceLoadFacesConfigFiles))
            {
                WebXmlProcessor processor = new WebXmlProcessor(context);
                if (!processor.isFacesServletPresent()) {
                    if (LOGGER.isLoggable(Level.FINE)) {
                        LOGGER.fine(
                              "No FacesServlet found in deployment descriptor -"
                              +
                              " bypassing configuration.");
                    }
                    return;
                }

                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.fine("FacesServlet found in deployment descriptor -"
                                +
                                " processing configuration.");
                }
            }                       

            // Prepare local variables we will need
            FacesConfigBean fcb = new FacesConfigBean();            

            // see if we're operating in the unit test environment
            try {
                if (Util.isUnitTestModeEnabled()) {
                    // if so, put the fcb in the servletContext
                    context.setAttribute(FACES_CONFIG_BEAN_KEY, fcb);
                }
            }
            catch (Exception e) {
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.fine("Can't query for test environment");
                }
            }

            // see if we need to disable our TLValidator
            Util.setHtmlTLVActive(
                  isFeatureEnabled(BooleanWebContextInitParameter.EnableHtmlTagLibraryValidator));
            
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine("contextInitialized("
                            + context.getServletContextName()
                            + ")");
            }

            // Ensure that we initialize a particular application ony once.
            // Note that we need to cache this in a local variable so we 
            // can check it from our finally block.
            if (initialized = initialized()) {
                return;
            }

            // Step 1, configure a Digester instance we can use
            digester = digester(isFeatureEnabled(BooleanWebContextInitParameter.ValidateFacesConfigFiles));
            
            // Step 2, parse the RI configuration resource
            Bootstrapper bootstrapper = new Bootstrapper();
            bootstrapper.populateFacesConfigBean(fcb);
            
            // Step 3, parse any "/META-INF/faces-config.xml" resources        
            SortedMap<String, URL> sortedJarMap = new TreeMap<String, URL>();
            List<URL> unsortedResourceList = new ArrayList<URL>();
                          
            try {
                for (Enumeration<URL> items = Util.getCurrentLoader(this)
                      .getResources(META_INF_RESOURCES);
                     items.hasMoreElements();) {
               
                    URL nextElement = items.nextElement();
                    String jarUrl = nextElement.toString();
                    String jarName = null;
                    int resourceIndex;
                    // If this resource has a faces-config file inside of it
                    if (-1 != (resourceIndex =
                          jarUrl.indexOf(META_INF_RESOURCES))) {
                        // Search backwards for the previous occurrence of File.SEPARATOR
                        int sepIndex = resourceIndex - 2;
                        char sep = ' ';
                        while (0 < sepIndex) {
                            sep = jarUrl.charAt(sepIndex);
                            if ('/' == sep) {
                                break;
                            }
                            sepIndex--;
                        }
                        if ('/' == sep) {
                            jarName =
                                  jarUrl.substring(sepIndex + 1, resourceIndex);
                        }
                    }
                    if (null != jarName) {
                        sortedJarMap.put(jarName, nextElement);
                    } else {
                        unsortedResourceList.add(0, nextElement);
                    }
                }
            } catch (IOException e) {
                throw new FacesException(e);
            }
            // Load the sorted resources first:          
            URL url = null;
            for (Entry<String, URL> entry : sortedJarMap.entrySet()) {
                url = entry.getValue();
                parse(digester, url, fcb);
            }
            // Then load the unsorted resources           
            for (Iterator<URL> resources = unsortedResourceList.iterator();
                 resources.hasNext(); ) {            
                url = resources.next();
                parse(digester, url, fcb);
            }

            // Step 4, parse any context-relative resources specified in
            // the web application deployment descriptor
            String paths =
                  context.getInitParameter(FacesServlet.CONFIG_FILES_ATTR);
            if (paths != null) {                               
                for (String token : Util.split(paths.trim(), ",")) {
                    if (!WEB_INF_RESOURCE.equals(token.trim())) {
                        url = getContextURLForPath(context, token.trim());
                        if (url != null) {
                            parse(digester, url, fcb);
                        }
                    }

                }
            }

            // Step 5, parse "/WEB-INF/faces-config.xml" if it exists
            url = getContextURLForPath(context, WEB_INF_RESOURCE);
            if (url != null) {
                parse(digester, url, fcb);
            }

            // Step 6, use the accumulated configuration beans to configure the RI
            try {
                configure(context, fcb);
            } catch (FacesException e) {
                e.printStackTrace();
                throw e;
            } catch (Exception e) {
                e.printStackTrace();
                throw new FacesException(e);
            }

            // Step 7, verify that all the configured factories are available
            // and optionall that configured objects can be created
            verifyFactories();
            if (isFeatureEnabled(BooleanWebContextInitParameter.VerifyFacesConfigObjects)) {
                verifyObjects(context, fcb);
            }
            // Step 8, register FacesCompositeELResolver and ELContextListener with
            // Jsp.
            registerELResolverAndListenerWithJsp(context);
        } finally {
            if (!initialized) {
                JSFVersionTracker tracker = getJSFVersionTracker();
                if (null != tracker) {
                    tracker.publishInstanceToApplication();
                }
                releaseDigester(digester);
            }
          
            if (associate != null) {
                associate.setContextName(getServletContextIdentifier(context));
            }
                  
            tlsExternalContext.set(null);          
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE,
                           "jsf.config.listener.version.complete",
                           getServletContextIdentifier(context));
            }
            
        }        
    }


    public void contextDestroyed(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();
        try {            
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine("contextDestroyed("
                            + context.getServletContextName()
                            + ')');
            }           

            // Release any allocated application resources
            FactoryFinder.releaseFactories();
            ReflectionUtils.clearCache(Util.getCurrentLoader(this));
            tlsExternalContext.set(new ServletContextAdapter(context));
            ApplicationAssociate
                  .clearInstance(tlsExternalContext.get());

            // Release the initialization mark on this web application
            release();
        } finally {
            tlsExternalContext.set(null);    
            WebConfiguration.clear(context);
        }

    }    

    // --------------------------------------------------------- Private Methods

    

    /**
     * <p>Return the implementation-specific <code>Application</code>
     * instance for this application.  You must <strong>NOT</strong>
     * call this method prior to configuring the appropriate
     * <code>ApplicationFactory</code> class.</p>
     */
    private Application application() {

        if (application == null) {
            ApplicationFactory afactory = (ApplicationFactory)
                FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
            application = afactory.getApplication();
        }
        return application;

    }            


    /**
     * <p>Configure the JavaServer Faces reference implementation based on
     * the accumulated configuration beans.</p>
     *
     * @param context <code>ServletContext</code> for this web application
     * @param config  <code>FacesConfigBean</code> that is the root of the
     *                tree of configuration information
     */
    protected void configure(ServletContext context, FacesConfigBean config)
    throws Exception {
        configure(config.getFactory());
        configure(config.getLifecycle());
        configure(config.getApplication());
        configure(config.getComponents());
        configure(config.getConvertersByClass());
        configure(config.getConvertersById());
        configure(config.getManagedBeans());
        configure(config.getNavigationRules());
        configure(config.getRenderKits());
        configure(config.getValidators());

    }


    /**
     * <p>Configure the application objects for this application.</p>
     *
     * @param config   <code>ApplicationBean</code> that contains our
     *                 configuration information
     */
    @SuppressWarnings("deprecation")
    private void configure(ApplicationBean config)
        throws Exception {

        if (config == null) {
            return;
        }
        Application application = application();
        associate =
                ApplicationAssociate.getInstance(getExternalContextDuringInitialize());                              

        // Configure scalar properties

        configure(config.getLocaleConfig());
        configure(config.getResourceBundles());

        String value = config.getMessageBundle();
        if (value != null) {
            if (LOGGER.isLoggable(Level.FINER)) {
                LOGGER.finer("setMessageBundle(" + value + ')');
            }
            application.setMessageBundle(value);
        }

        value = config.getDefaultRenderKitId();
        if (value != null) {
            if (LOGGER.isLoggable(Level.FINER)) {
                LOGGER.finer("setDefaultRenderKitId(" + value + ')');
            }
            application.setDefaultRenderKitId(value);
        }

        // Configure chains of handlers

        String[] values = config.getActionListeners();
        if ((values != null) && (values.length > 0)) {
            for (int i = 0, len = values.length; i < len; i++) {
               if (LOGGER.isLoggable(Level.FINER)) {
                   LOGGER.finer("setActionListener(" + values[i] + ')');
                }
                Object instance = Util.createInstance
                    (values[i], ActionListener.class,
                     application.getActionListener());
                if (instance != null) {
                    application.setActionListener((ActionListener) instance);
                }
            }
        }

        values = config.getNavigationHandlers();
        if ((values != null) && (values.length > 0)) {
            for (int i = 0, len = values.length; i < len; i++) {
                if (LOGGER.isLoggable(Level.FINER)) {
                    LOGGER.finer("setNavigationHandler(" + values[i] + ')');
                }
                Object instance = Util.createInstance
                    (values[i], NavigationHandler.class,
                     application.getNavigationHandler());
                if (instance != null) {
                    application.setNavigationHandler
                        ((NavigationHandler) instance);
                }
            }
        }

        values = config.getPropertyResolvers();
        if ((values != null) && (values.length > 0)) {
            // initialize the prevInChain to DummyPropertyResolver instance 
            // to satisfy decorator pattern as well as satisfy the requirements 
            // of unified EL. This resolver sets propertyResolved to false on
            // invocation of its method, so that variable resolution can move
            // further in the chain.
            Object prevInChain = new DummyPropertyResolverImpl();            
            for (int i = 0, len = values.length; i < len; i++) {
                if (LOGGER.isLoggable(Level.FINER)) {
                    LOGGER.finer("setPropertyResolver(" + values[i] + ')');
                }
                prevInChain = Util.createInstance(values[i],
                                               PropertyResolver.class, 
                                               prevInChain);
            }
            PropertyResolver legacyPRChainHead= (PropertyResolver) prevInChain; 
            if (associate != null) {
                associate.setLegacyPRChainHead(legacyPRChainHead);
            }
        }
        
        // process custom el-resolver elements if any
        values = config.getELResolvers();
        if ((values != null) && (values.length > 0)) {
            for (int i = 0, len = values.length; i < len; i++) {
                if (LOGGER.isLoggable(Level.FINER)) {
                    LOGGER.finer("setELResolver(" + values[i] + ')');
                }
                Object instance = Util.createInstance(values[i]);
                if (elResolversFromFacesConfig == null) {
                    elResolversFromFacesConfig = 
                        new ArrayList<ELResolver>(values.length);
                }
                if (null != instance) {
                    elResolversFromFacesConfig.add((ELResolver) instance);
                }
            }
            associate.setELResolversFromFacesConfig(elResolversFromFacesConfig);
        }

        values = config.getStateManagers();
        if ((values != null) && (values.length > 0)) {
            for (int i = 0, len = values.length; i < len; i++) {
                if (LOGGER.isLoggable(Level.FINER)) {
                    LOGGER.finer("setStateManager(" + values[i] + ')');
                }
                Object instance = Util.createInstance
                    (values[i], StateManager.class,
                     application.getStateManager());
                if (instance != null) {
                    application.setStateManager
                        ((StateManager) instance);
                }
            }
        }
                
        values = config.getVariableResolvers();
        if ((values != null) && (values.length > 0)) {
            // initialize the prevInChain to DummyVariableResolver instance 
            // to satisfy decorator pattern as well as satisfy the requirements 
            // of unified EL. This resolver sets propertyResolved to false on
            // invocation of its method, so that variable resolution can move
            // further in the chain.
            Object prevInChain = new DummyVariableResolverImpl();
            for (int i = 0, len = values.length; i < values.length; i++) {
                if (LOGGER.isLoggable(Level.FINER)) {
                    LOGGER.finer("setVariableResolver(" + values[i] + ')');
                }
                // If this is the first custom VariableResolver in the chain,
                // make sure to pass the ChainAwareVariableResolver to its ctor.
                Object instance;
                if (0 == i) {
                    instance = Util.createInstance
                            (values[i], VariableResolver.class, 
                            new ChainAwareVariableResolver());
                }
                else {
                    instance = Util.createInstance
                            (values[i], VariableResolver.class, prevInChain);
                }
                prevInChain = instance;
            }
            VariableResolver legacyVRChainHead = (VariableResolver) prevInChain;
            if (associate != null) {
                associate.setLegacyVRChainHead(legacyVRChainHead);
            }
        }

        values = config.getViewHandlers();
        if ((values != null) && (values.length > 0)) {
            for (int i = 0, len = values.length; i < len; i++) {
                if (LOGGER.isLoggable(Level.FINER)) {
                    LOGGER.finer("setViewHandler(" + values[i] + ')');
                }
                Object instance = Util.createInstance
                    (values[i], ViewHandler.class,
                     application.getViewHandler());               
                if (instance != null) {
                    application.setViewHandler
                        ((ViewHandler) instance);
                }
            }
        }

    }


    /**
     * <p>Configure all registered components.</p>
     *
     * @param config Array of <code>ComponentBean</code> that contains
     *               our configuration information
     */
    private void configure(ComponentBean[] config) throws Exception {

        if (config == null) {
            return;
        }
        Application application = application();

        for (int i = 0, len = config.length; i < len; i++) {
            if (LOGGER.isLoggable(Level.FINER)) {
                    LOGGER.finer("addComponent(" +
                          config[i].getComponentType() + ',' +
                          config[i].getComponentClass() + ')');
            }
            application.addComponent(config[i].getComponentType(),
                                     config[i].getComponentClass());
        }

    }


    /**
     * <p>Configure all registered converters.</p>
     *
     * @param config Array of <code>ConverterBean</code> that contains
     *               our configuration information
     */
    private void configure(ConverterBean[] config) throws Exception {
       
        Application application = application();

        // at a minimum, configure the primitive converters
        for (int i = 0, len = PRIM_CLASSES_TO_CONVERT.length; i < len; i++) {
            if (LOGGER.isLoggable(Level.FINER)) {
                LOGGER.finer("addConverterByClass(" +
                          PRIM_CLASSES_TO_CONVERT[i] + ',' +
                          CONVERTERS_FOR_PRIMS[i] + ')');
            }
            application.addConverter(PRIM_CLASSES_TO_CONVERT[i],
                                     CONVERTERS_FOR_PRIMS[i]);
        }

        if (config == null) {
            return;
        }

        for (int i = 0, len = config.length; i < len; i++) {
            if (config[i].getConverterId() != null) {
                if (LOGGER.isLoggable(Level.FINER)) {
                    LOGGER.finer("addConverterById(" +
                              config[i].getConverterId() + ',' +
                              config[i].getConverterClass() + ')');
                }
                application.addConverter(config[i].getConverterId(),
                                         config[i].getConverterClass());
            } else {
                if (LOGGER.isLoggable(Level.FINER)) {
                    LOGGER.finer("addConverterByClass(" +
                              config[i].getConverterForClass() + ',' +
                              config[i].getConverterClass() + ')');
                }
                Class clazz = Util.loadClass(config[i].getConverterForClass(),
                                             this);
                application.addConverter(clazz,
                                         config[i].getConverterClass());
            }
        }

    }


    /**
     * <p>Configure the object factories for this application.</p>
     *
     * @param config <code>FactoryBean</code> that contains our
     *               configuration information
     */
    private void configure(FactoryBean config) throws Exception {

        if (config == null) {
            return;
        }

        for (String value : config.getApplicationFactories()) {            
            if (value != null) {
                if (LOGGER.isLoggable(Level.FINER)) {
                    LOGGER.finer("setApplicationFactory(" + value + ')');
                }
                FactoryFinder.setFactory(FactoryFinder.APPLICATION_FACTORY,
                                         value);
            }
        }

        for (String value : config.getFacesContextFactories()) { 
            if (value != null) {
                if (LOGGER.isLoggable(Level.FINER)) {
                    LOGGER.finer("setFacesContextFactory(" + value + ')');
                }
                FactoryFinder.setFactory(FactoryFinder.FACES_CONTEXT_FACTORY,
                                         value);
            }
        }
      
       for (String value : config.getLifecycleFactories()) { 
            if (value != null) {
                if (LOGGER.isLoggable(Level.FINER)) {
                    LOGGER.finer("setLifecycleFactory(" + value + ')');
                }
                FactoryFinder.setFactory(FactoryFinder.LIFECYCLE_FACTORY,
                                         value);
            }
        }
       
       for (String value : config.getRenderKitFactories()) { 
            if (value != null) {
                if (LOGGER.isLoggable(Level.FINER)) {
                    LOGGER.finer("setRenderKitFactory(" + value + ')');
                }
                FactoryFinder.setFactory(FactoryFinder.RENDER_KIT_FACTORY,
                                         value);
            }
        }

    }


    /**
     * <p>Configure the lifecycle listeners for this application.</p>
     *
     * @param config <code>LifecycleBean</code> that contains our
     */
    private void configure(LifecycleBean config) throws Exception {

        if (config == null) {
            return;
        }
        String[] listeners = config.getPhaseListeners();
        LifecycleFactory factory = (LifecycleFactory)
            FactoryFinder.getFactory(FactoryFinder.LIFECYCLE_FACTORY);
        
        for (Iterator<String> iter = factory.getLifecycleIds(); iter.hasNext();) {       
            String lifecycleId = iter.next();
            if (lifecycleId == null) {
                lifecycleId = LifecycleFactory.DEFAULT_LIFECYCLE;
            }
            Lifecycle lifecycle =
                    factory.getLifecycle(lifecycleId);
            if (webConfig.getBooleanContextInitParameter(
                  BooleanWebContextInitParameter.ExternalizeJavaScript)) {
                lifecycle.addPhaseListener(new JsfJsResourcePhaseListener());
            }
            for (int i = 0, len = listeners.length; i < len; i++) {
                if (LOGGER.isLoggable(Level.FINER)) {
                    LOGGER.finer("addPhaseListener(" + listeners[i] + ')');
                }
                try {
                    Class clazz = Util.loadClass(listeners[i], this);
                    lifecycle.addPhaseListener((PhaseListener) clazz.newInstance());
                } catch (Exception e) {
                    Object [] args = { "phase-listener: " + listeners[i] };
                    String message =
                        MessageUtils.getExceptionMessageString(MessageUtils.CANT_INSTANTIATE_CLASS_ERROR_MESSAGE_ID,  args);
                    LOGGER.log(Level.SEVERE, message);
                    throw e;
                }
            }
        }
    }


    /**
     * <p>Configure the locale support for this application.</p>
     *
     * @param config <code>LocaleConfigBean</code> that contains our
     *               configuration information
     */
    private void configure(LocaleConfigBean config) throws Exception {

        if (config == null) {
            return;
        }
        Application application = application();
        String value;
        String[] values;

        value = config.getDefaultLocale();
        if (value != null) {
            if (LOGGER.isLoggable(Level.FINER)) {
                LOGGER.finer("setDefaultLocale(" + value + ')');
            }
            application.setDefaultLocale
                (Util.getLocaleFromString(value));
        }

        values = config.getSupportedLocales();
        if ((values != null) && (values.length > 0)) {
            List<Locale> locales = new ArrayList<Locale>();
            for (int i = 0, len = values.length; i < len; i++) {
                if (LOGGER.isLoggable(Level.FINER)) {
                    LOGGER.finer("addSupportedLocale(" + values[i] + ')');
                }
                locales.add(Util.getLocaleFromString(values[i]));
            }
            application.setSupportedLocales(locales);
        }

    }


    /**
     * <p>Configure all registered managed beans.</p>
     *
     * @param config Array of <code>ManagedBeanBean</code> that contains
     *               our configuration information
     */ 
    private void configure(ManagedBeanBean[] config) throws Exception {
        
        if (config == null || associate == null) {
            return;
        }     

        for (int i = 0, len = config.length; i < len; i++) {
            if (LOGGER.isLoggable(Level.FINER)) {
                LOGGER.finer("addManagedBean(" +
                          config[i].getManagedBeanName() + ',' +
                          config[i].getManagedBeanClass() + ')');
            }
            ManagedBeanFactory mbf = new ManagedBeanFactoryImpl(config[i]);
            
            // See if the RI specific MANAGED_BEAN_FACTORY_DECORATOR is available
            String mbfdClassName = 
                  webConfig.getContextInitParameter(
                        WebContextInitParameter.ManagedBeanFactoryDecorator);
            if (mbfdClassName != null) {
                ManagedBeanFactory newMbf = 
                        (ManagedBeanFactory) Util.createInstance(mbfdClassName, 
                            ManagedBeanFactory.class, mbf);
                if (null != newMbf) {
                    mbf = newMbf;
                }
            }
            associate.addManagedBeanFactory(config[i].getManagedBeanName(),
                                            mbf);
        }
    }


    /**
     * <p>Configure all registered navigation rules.</p>
     *
     * @param config Array of <code>NavigationRuleBean</code> that contains
     *               our configuration information
     */
    private void configure(NavigationRuleBean[] config) {

        if (config == null || associate == null) {
            return;
        }                 

        for (int i = 0, len = config.length; i < len; i++) {
            if (LOGGER.isLoggable(Level.FINER)) {
                LOGGER.finer("addNavigationRule(" +
                          config[i].getFromViewId() + ')');
            }
            NavigationCaseBean[] ncb = config[i].getNavigationCases();
            for (int j = 0, len2 = ncb.length; j < len2; j++) {
                if (LOGGER.isLoggable(Level.FINER)) {
                    LOGGER.finer("addNavigationCase(" +
                              ncb[j].getFromAction() + ',' +
                              ncb[j].getFromOutcome() + ',' +
                              ncb[j].isRedirect() + ',' +
                              ncb[j].getToViewId() + ')');
                }
                ConfigNavigationCase cnc = new ConfigNavigationCase();
                if (config[i].getFromViewId() == null) {
                    cnc.setFromViewId("*");
                } else {
                    cnc.setFromViewId(config[i].getFromViewId());
                }
                cnc.setFromAction(ncb[j].getFromAction());
                String fromAction = ncb[j].getFromAction();
                if (fromAction == null) {
                    fromAction = "-";
                }
                cnc.setFromOutcome(ncb[j].getFromOutcome());
                String fromOutcome = ncb[j].getFromOutcome();
                if (fromOutcome == null) {
                    fromOutcome = "-";
                }
                cnc.setToViewId(ncb[j].getToViewId());                
               
                cnc.setKey(cnc.getFromViewId() + fromAction + fromOutcome);
                if (ncb[j].isRedirect()) {
                    cnc.setRedirect("true");
                } else {
                    cnc.setRedirect(null);
                }
                associate.addNavigationCase(cnc);
            }
        }

    }


    /**
     * <p>Configure all registered renderers for this renderkit.</p>
     *
     * @param config Array of <code>RendererBean</code> that contains
     *               our configuration information
     * @param rk     RenderKit to be configured
     */
    private void configure(RendererBean[] config, RenderKit rk)
        throws Exception {

        if (config == null) {
            return;
        }

        for (int i = 0, len = config.length; i < len; i++) {
            if (LOGGER.isLoggable(Level.FINER)) {
                LOGGER.finer("addRenderer(" +
                          config[i].getComponentFamily() + ',' +
                          config[i].getRendererType() + ',' +
                          config[i].getRendererClass() + ')');
            }
            try {
                Renderer r = (Renderer)
                Util.loadClass(
                        config[i].getRendererClass(), this).newInstance();
                rk.addRenderer(config[i].getComponentFamily(),
                        config[i].getRendererType(),
                        r);
            }
            catch (Exception e) {
                Object [] args = { "component-family: " + 
                        config[i].getComponentFamily() + 
                        " renderer-type: " + config[i].getRendererType() + 
                        " renderer-class: " + config[i].getRendererClass() };
                String message = 
                        MessageUtils.getExceptionMessageString(MessageUtils.CANT_INSTANTIATE_CLASS_ERROR_MESSAGE_ID,  args);
                LOGGER.log(Level.SEVERE, message);
                throw e;
            }
        }

    }


    /**
     * <p>Configure all registered renderKits.</p>
     *
     * @param config Array of <code>RenderKitBean</code> that contains
     *               our configuration information
     */
    private void configure(RenderKitBean[] config) throws Exception {

        if (config == null) {
            return;
        }
        RenderKitFactory rkFactory = (RenderKitFactory)
            FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);

        for (int i = 0, len = config.length; i < len; i++) {
            RenderKit rk = rkFactory.getRenderKit
                (null, config[i].getRenderKitId());
            if (rk == null) {
                if (LOGGER.isLoggable(Level.FINER)) {
                    LOGGER.finer("createRenderKit(" +
                              config[i].getRenderKitId() + ',' +
                              config[i].getRenderKitClass() + ')');
                }
                if (config[i].getRenderKitClass() == null) {
                    throw new IllegalArgumentException// PENDING - i18n
                        ("No renderKitClass for renderKit " +
                         config[i].getRenderKitId());
                }
                try {
                    rk = (RenderKit)
                    Util.loadClass(
                            config[i].getRenderKitClass(), this).newInstance();
                    rkFactory.addRenderKit(config[i].getRenderKitId(), rk);
                } catch (Exception e) {
                    Object [] args = { "render-kit-id: " +
                            config[i].getRenderKitId() +
                            " render-kit-class: " + config[i].getRenderKitClass() };
                    String message =
                        MessageUtils.getExceptionMessageString(MessageUtils.CANT_INSTANTIATE_CLASS_ERROR_MESSAGE_ID,  args);
                    LOGGER.log(Level.SEVERE, message);
                    throw e;
                            
                }
            } else {
                if (LOGGER.isLoggable(Level.FINER)) {
                    LOGGER.finer("getRenderKit(" +
                              config[i].getRenderKitId() + ')');
                }
            }
            configure(config[i].getRenderers(), rk);
        }

    }

    /**
     * <p>Configure all registered ResourceBundles.</p>
     *
     * @param config Array of <code>ResourceBundleBean</code> that contains
     *               our configuration information
     */
    private void configure(ResourceBundleBean[] config) throws Exception {

        if (config == null || associate == null) {
            return;
        }
                
        String baseName;
        String var;
        
        for (int i = 0, len = config.length; i < len; i++) {
            if ((null == (baseName = config[i].getBasename())) ||
                (null == (var = config[i].getVar()))) {
                String message = "Application ResourceBundle: null base-name or var." +
                              "base-name:" + baseName + 
                              "var:" + config[i].getVar();
                // PENDING(edburns): I18N all levels above info
                if (LOGGER.isLoggable(Level.WARNING)) {
                    LOGGER.finer(message);
                }
                throw new IllegalArgumentException(message);
            }
            associate.addResourceBundleBean(var, config[i]);
        }
        
    }


    /**
     * <p>Configure all registered validators.</p>
     *
     * @param config Array of <code>ValidatorBean</code> that contains
     *               our configuration information
     */
    private void configure(ValidatorBean[] config) throws Exception {

        if (config == null) {
            return;
        }
        Application application = application();

        for (int i = 0, len = config.length; i < len; i++) {
            if (LOGGER.isLoggable(Level.FINER)) {
                LOGGER.finer("addValidator(" +
                          config[i].getValidatorId() + ',' +
                          config[i].getValidatorClass() + ')');
            }
            application.addValidator(config[i].getValidatorId(),
                                     config[i].getValidatorClass());
        }

    }


    /**
     * <p>Configure and return a <code>Digester</code> instance suitable for
     * parsing the runtime configuration information we need.</p>
     *
     * @param validateXml if true, validation is turned on during parsing.
     */
    protected Digester digester(boolean validateXml) {

        Digester digester;
        DigesterFactory.VersionListener listener = null;
        final JSFVersionTracker tracker = getJSFVersionTracker();
        
        if (null != tracker) {
            listener =
                    new DigesterFactory.VersionListener() {
                public void takeActionOnGrammar(String grammar) {
                    tracker.pushJSFVersionNumberFromGrammar(grammar);
                }

                public void takeActionOnArtifact(String artifactName) {
                    tracker.putTrackedClassName(artifactName);
                }
            };
        }
            
        try {
            if (null != listener) {
                digester = DigesterFactory.newInstance(validateXml, 
                        listener).createDigester();
            }
            else {
                digester = DigesterFactory.newInstance(validateXml).createDigester();
            }
        }
        catch (RuntimeException e) {
            Object [] args = { "Digester" };
            String message =
                   MessageUtils.getExceptionMessageString(MessageUtils.CANT_INSTANTIATE_CLASS_ERROR_MESSAGE_ID,  args);
            LOGGER.log(Level.SEVERE, message);
            throw e;
        }

        // Configure parsing rules
        // PENDING - Read from file?
        digester.addRuleSet(new FacesConfigRuleSet(false, false, true));

        // Push an initial FacesConfigBean onto the stack
        digester.push(new FacesConfigBean());

        return (digester);

    }
    
    protected void releaseDigester(Digester toRelease) {
        DigesterFactory.releaseDigester(toRelease);
    }
    
    private JSFVersionTracker versionTracker = null;

    /**
     * <p>This is the single access point for accessing the
     * JSFVersionTracker instance during initialization.  It returns
     * null if the feature has been disabled via user configuration.</p>
     */ 
    
    private JSFVersionTracker getJSFVersionTracker() {
        // If the feature is disabled...
        if (isFeatureEnabled(BooleanWebContextInitParameter.DisableArtifactVersioning)) {
            // make sure the tracker is released.
            versionTracker = null;
            return null;
        }
        
        if (null == versionTracker) {
            versionTracker = new JSFVersionTracker();
        }
        return versionTracker;
    }
        

    /**
     * <p>Verify that all of the required factory objects are available.</p>
     *
     * @throws FacesException if a factory cannot be created
     */
    private void verifyFactories() throws FacesException {

        for (int i = 0, len = FACTORY_NAMES.length; i < len; i++) {
            try {
                FactoryFinder.getFactory(FACTORY_NAMES[i]);
            } catch (Exception e) {
                throw new FacesException(e);
            }
        }

    }


    /**
     * <p>Return <code>true</code> if this web application has already
     * been initialized.  If it has not been initialized, also record
     * the initialization of this application until removed by a call to
     * <code>release()</code>.</p>
     */
    private boolean initialized() {

        // Initialize at most once per web application class loader
        ClassLoader cl = Util.getCurrentLoader(this);
        synchronized (LOADERS) {
            if (!LOADERS.contains(cl)) {
                LOADERS.add(cl);
                if (LOGGER.isLoggable(Level.FINER)) {
                    LOGGER.finer("Initializing this webapp");
                }
                return false;
            } else {
                if (LOGGER.isLoggable(Level.FINER)) {
                    LOGGER.finer("Listener already completed for this webapp");
                }
                return true;
            }
        }

    }


    /**
     * <p>Verify that all of the application-defined objects that have been
     * configured can be successfully instantiated.</p>
     *
     * @param context <code>ServletContext</code> instance for this application
     * @param fcb     <code>FacesConfigBean</code> containing the
     *                configuration information
     * @throws FacesException if an application-defined object cannot
     *                        be instantiated
     */
    private void verifyObjects(ServletContext context, FacesConfigBean fcb)
        throws FacesException {

        if (LOGGER.isLoggable(Level.FINER)) {
            LOGGER.finer("Verifying application objects");
        }

        ApplicationFactory af = (ApplicationFactory)
            FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
        Application app = af.getApplication();
        RenderKitFactory rkf = (RenderKitFactory)
            FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
        boolean success = true;

        // Check components
        ComponentBean[] comp = fcb.getComponents();
        for (int i = 0, len = comp.length; i < len; i++) {
            try {
                app.createComponent(comp[i].getComponentType());
            } catch (Exception e) {
                context.log(comp[i].getComponentClass(), e);
                success = false;
            }
        }

        // Check converters
        ConverterBean[] conv1 = fcb.getConvertersByClass();
        Class clazz;
        for (int i = 0, len = conv1.length; i < len; i++) {
            try {
                clazz = Util.loadClass(conv1[i].getConverterForClass(), this);
                app.createConverter(clazz);
            } catch (Exception e) {
                context.log(conv1[i].getConverterForClass(), e);
                clazz = null;
                success = false;
            }
            try {
                app.createConverter(clazz);
            } catch (Exception e) {
                context.log(conv1[i].getConverterClass(), e);
                success = false;
            }
        }
        ConverterBean[] conv2 = fcb.getConvertersById();
        for (int i = 0, len = conv2.length; i < len; i++) {
            try {
                app.createConverter(conv2[i].getConverterId());
            } catch (Exception e) {
                context.log(conv2[i].getConverterClass());
                success = false;
            }
        }

        // Check renderers
        RenderKitBean[] rkb = fcb.getRenderKits();
        RenderKit rk;
        for (int i = 0, len = rkb.length; i < len; i++) {
            try {
                rk = rkf.getRenderKit(null, rkb[i].getRenderKitId());
                RendererBean[] rb = rkb[i].getRenderers();
                for (int j = 0, len2 = rb.length; j < len2; j++) {
                    try {
                        rk.getRenderer(rb[j].getComponentFamily(),
                                       rb[j].getRendererType());
                    } catch (Exception e) {
                        context.log(rb[j].getRendererClass(), e);
                        success = false;
                    }
                }
            } catch (Exception e) {
                context.log(rkb[i].getRenderKitId());
                success = false;
            }
        }

        // Check validators
        ValidatorBean[] val = fcb.getValidators();
        for (int i = 0, len = val.length; i < len; i++) {
            try {
                app.createValidator(val[i].getValidatorId());
            } catch (Exception e) {
                context.log(val[i].getValidatorClass(), e);
                success = false;
            }
        }

        // Throw an exception on any failures
        if (!success) {
            String message;
            try {
                message = MessageUtils.getExceptionMessageString
                    (MessageUtils.OBJECT_CREATION_ERROR_ID);
            } catch (Exception ee) {
                message = "One or more configured application objects " +
                    "cannot be created.  See your web application logs " +
                    "for details.";
            }
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.warning(message);
            }
            throw new FacesException(message);
        } else {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine(
                    "Application object verification completed successfully");
            }
        }

    }


    /**
     * <p>Parse the configuration resource at the specified URL, using
     * the specified <code>Digester</code> instance.</p>
     *
     * @param digester Digester to use for parsing
     * @param url      URL of the configuration resource to be parsed
     * @param fcb      FacesConfigBean to accumulate results
     */
    protected void parse(Digester digester, URL url, FacesConfigBean fcb) {

        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("parse(" + url.toExternalForm() + ')');
        }

        InputStream stream = null;
        URLConnection conn;        
        InputSource source;
        JSFVersionTracker tracker = getJSFVersionTracker();
        try {
            conn = url.openConnection();
            conn.setUseCaches(false);
            stream = new BufferedInputStream(conn.getInputStream());
            source = new InputSource(url.toExternalForm());
            source.setSystemId(url.toExternalForm());
            source.setByteStream(stream);            
            digester.clear();
            digester.push(fcb);
            if (null != tracker) {
                tracker.startParse();
            }
            digester.parse(source);
        } 
        catch (Exception e) {
            int ln = -1;
            int cn = -1;            
            if (e instanceof SAXParseException) {
                SAXParseException spe = (SAXParseException) e;
                ln = spe.getLineNumber();
                cn = spe.getColumnNumber();                           
            }
            String message;
            try {
                message = MessageUtils.getExceptionMessageString
                    (MessageUtils.CANT_PARSE_FILE_ERROR_MESSAGE_ID,
                     url.toExternalForm(),
                     ln,
                     cn,
                     e.getMessage());
            } catch (Exception ee) {
                message = "Can't parse configuration file: " 
                          + url.toExternalForm() + ": Error at line "
                          + ln + " column " + cn 
                          + ": " + e.getMessage();
            }
            throw new FacesException(message);
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (Exception e) {
                    // ignore
                }
            }
            if (null != tracker) {
                tracker.endParse();
            }
            stream = null;
        }
        
        

    }
    
    /**
     * <p>Determines if a particular feature, configured via the web
     * deployment descriptor as a <code>true/false</code> value, is
     * enabled or not.</p>
     * @param param the <code>BooleanWebContextInitParameter</code> 
     *  of interest
     *
     * @return <code>true</code> if the feature in question is enabled, otherwise
     *  <code>false</code>
     */
    protected boolean isFeatureEnabled(BooleanWebContextInitParameter param) {
        return webConfig.getBooleanContextInitParameter(param);      
    }

    private String getServletContextIdentifier(ServletContext context) {
        if (context.getMajorVersion() == 2 && context.getMinorVersion() < 5) {
            return context.getServletContextName();
        } else {
            return context.getContextPath();
        }
    }

    /**
     * <p>Return the URL for the given path.</p>
     * @param context the <code>ServletContext</code> of the current application
     * @param path the resource path
     * @return the URL for the given resource or <code>null</code>
     */
    private URL getContextURLForPath(ServletContext context, String path) {
        try {
            return context.getResource(path);
        } catch (MalformedURLException mue) {
            throw new FacesException(mue);
        }
    }
    
    private static boolean isJspTwoOne() {
        
        if (JspFactory.getDefaultFactory() == null) {
            return false;
        }
        try {
            JspFactory.class.getMethod("getJspApplicationContext", 
                                       ServletContext.class );
        } catch (NoSuchMethodException nsme) {
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.warning(MessageUtils.getExceptionMessageString(MessageUtils.INCORRECT_JSP_VERSION_ID,
               "getJspApplicationContext"));
            }
            return false;
        }
        return true;
    }

    public void registerELResolverAndListenerWithJsp(ServletContext context) {               
        
        // check if JSP 2.1
        if (!isJspTwoOne()) {
            
            // not JSP 2.1
            
            // first try to load a factory defined in web.xml
            String elFactType = webConfig.getContextInitParameter(
                  WebContextInitParameter.ExpressionFactory);
            if (elFactType == null || "".equals(elFactType.trim())) {
                // else use EL-RI
                elFactType = WebContextInitParameter.ExpressionFactory
                      .getDefaultValue();
            }
            
            try {
                ExpressionFactory factory = (ExpressionFactory) Class.forName(
                        elFactType).newInstance();
                if (associate != null) {
                    associate.setExpressionFactory(factory);
                }
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error Instantiating ExpressionFactory", e);
            }                        
        
        } else {
            
            // Is JSP 2.1
            
            // JSP 2.1 specific check
            if (JspFactory.getDefaultFactory().getJspApplicationContext(context) == null) {
                return;
            }
            
            // register an empty resolver for now. It will be populated after the 
            // first request is serviced.
            CompositeELResolver compositeELResolverForJsp = 
                new FacesCompositeELResolver(FacesCompositeELResolver.ELResolverChainType.JSP);   
            if (associate != null) {
                associate.setFacesELResolverForJsp(compositeELResolverForJsp);
            }
                    
            // get JspApplicationContext.
            JspApplicationContext jspAppContext = JspFactory.getDefaultFactory()
                    .getJspApplicationContext(context);
    
            // cache the ExpressionFactory instance in ApplicationAssociate
            if (associate != null) {
                associate.setExpressionFactory(jspAppContext.getExpressionFactory());
            }
    
            // register compositeELResolver with JSP
            try {
                jspAppContext.addELResolver(compositeELResolverForJsp);
            }
            catch (IllegalStateException e) {
                if (!Util.isUnitTestModeEnabled()) {
                    throw e;
                }
            }
    
            // register JSF ELContextListenerImpl with Jsp
            ELContextListenerImpl elContextListener = new ELContextListenerImpl();
            jspAppContext.addELContextListener(elContextListener);
         }
    }

    /**
     * <p>
     * Release the mark that this web application has been initialized.
     * </p>
     */
    private void release() {

        ClassLoader cl = Util.getCurrentLoader(this);
        synchronized (LOADERS) {
            LOADERS.remove(cl);
        }

    }


    public static class ServletContextAdapter extends ExternalContext {
        
        private ServletContext servletContext = null;
        private ApplicationMap applicationMap = null;
        
        public ServletContextAdapter(ServletContext sc) {
            this.servletContext = sc;
        }
        
        public void dispatch(String path) throws IOException {
        }
    
        public String encodeActionURL(String url) {
            return null;
        }   

        public String encodeNamespace(String name) {
            return null;
        }


        public String encodeResourceURL(String url) {
            return null;
        }

       @SuppressWarnings("unchecked")
       public Map<String,Object> getApplicationMap() {
            if (applicationMap == null) {
                applicationMap = 
                    new ApplicationMap(servletContext);
            }
            return applicationMap;
        }
        
        public String getAuthType() {
            return null;
        }

        public Object getContext() {
            return servletContext;
        }

        public String getInitParameter(String name) {
            return null;
        }

        public Map getInitParameterMap() {
            return null;
        }

        public String getRemoteUser() {
            return null;
        }


        public Object getRequest() {
            return null;
        }

	public void setRequest(Object request) {
	}

        public String getRequestContextPath() {
            return null;
        }

        public Map<String,Object> getRequestCookieMap() {
            return null;
        }

        public Map<String,String> getRequestHeaderMap() {
            return null;
        }


        public Map<String,String[]> getRequestHeaderValuesMap() {
            return null;
        }


        public Locale getRequestLocale() {
            return null;
        }

        public Iterator<Locale> getRequestLocales() {
            return null;
        }



        public Map<String,Object> getRequestMap() {
            return null;
        }


        public Map<String,String> getRequestParameterMap() {
            return null;
        }


        public Iterator<String> getRequestParameterNames() {
            return null;
        }


        public Map<String,String[]> getRequestParameterValuesMap() {
            return null;
        }


        public String getRequestPathInfo() {
            return null;
        }


        public String getRequestServletPath() {
            return null;
        }
        
         
    public String getRequestContentType() {
        return null;
    }

    public String getResponseContentType() {
        return null;
    }

        public URL getResource(String path) throws MalformedURLException {
            return null;
        }


        public InputStream getResourceAsStream(String path) {
            return null;
        }

        public Set<String> getResourcePaths(String path) {
            return null;
        }

        public Object getResponse() {
            return null;
        }

        public void setResponse(Object response) {
        }

        public Object getSession(boolean create) {
            return null;
        }

        public Map<String,Object> getSessionMap() {
            return null;
        }

        public java.security.Principal getUserPrincipal() {
            return null;
        }
        
        public boolean isUserInRole(String role) {
            return false;
        }

        public void log(String message) {
        }
        
        public void log(String message, Throwable exception){
        }
        
        public void redirect(String url) throws IOException {
        }

        public String getRequestCharacterEncoding() {
            return null;
        }

        public void setRequestCharacterEncoding(String requestCharacterEncoding) throws UnsupportedEncodingException {

        }

        public String getResponseCharacterEncoding() {
            return null;
        }

        public void setResponseCharacterEncoding(String responseCharacterEncoding) {
        }

    }
    
    static class ApplicationMap extends java.util.AbstractMap {

        private final ServletContext servletContext;

        ApplicationMap(ServletContext servletContext) {
            this.servletContext = servletContext;
        }


        public Object get(Object key) {
            if (key == null) {
                throw new NullPointerException();
            }
            return servletContext.getAttribute(key.toString());
        }


        public Object put(Object key, Object value) {
            if (key == null) {
                throw new NullPointerException();
            }
            String keyString = key.toString();
            Object result = servletContext.getAttribute(keyString);
            servletContext.setAttribute(keyString, value);
            return (result);
        }


        public Object remove(Object key) {
            if (key == null) {
                return null;
            }
            String keyString = key.toString();
            Object result = servletContext.getAttribute(keyString);
            servletContext.removeAttribute(keyString);
            return (result);
        }


        public Set entrySet() {
           throw new UnsupportedOperationException();
        }


        public boolean equals(Object obj) {
            if (obj == null || !(obj instanceof ApplicationMap))
                return false;
            return super.equals(obj);
        }


        public int hashCode() {
            int hashCode = 7 * servletContext.hashCode();
            for (Object o : entrySet()) {
                hashCode += o.hashCode();
            }
            return hashCode;
        }
        
        public void clear() {
            throw new UnsupportedOperationException();
        }

        public void putAll(Map t) {
            throw new UnsupportedOperationException();
        }
       

    } // END ApplicationMap


    /**
     * <p>Processes a web application's deployment descriptor looking
     * for a reference to <code>javax.faces.webapp.FacesServlet</code>.</p>
     */
    private static class WebXmlProcessor {

        private static final String WEB_XML_PATH = "/WEB-INF/web.xml";

        private boolean facesServletPresent;


        /**
         * <p>When instantiated, the web.xml of the current application
         * will be scanned looking for a references to the
         * <code>FacesServlet</code>.  <code>isFacesServletPresent()</code>
         * will return the appropriate value based on the scan.</p>
         * @param context the <code>ServletContext</code> for the application
         *  of interest
         */
        WebXmlProcessor(ServletContext context) {

            if (context != null) {
                scanForFacesServlet(context);
            }

        } // END WebXmlProcessor


        /**
         * @return <code>true</code> if the <code>WebXmlProcessor</code>
         * detected a <code>FacesServlet</code> entry, otherwise return
         * <code>false</code>.</p>        
         */
        boolean isFacesServletPresent() {

            return facesServletPresent;

        } // END isFacesServletPresent


        /**
         * <p>Parse the web.xml for the current application and scan
         * for a FacesServlet entry, if found, set the
         * <code>facesServletPresent</code> property to true.
         * @param context
         */
        private void scanForFacesServlet(ServletContext context) {

            SAXParserFactory factory = getConfiguredFactory();
            try {
                SAXParser parser = factory.newSAXParser();
                parser.parse(context.getResourceAsStream(WEB_XML_PATH),
                              new WebXmlHandler());
            } catch (Exception e) {
                // This probably won't happen since the container would
                // catch it before we would, but, if we catch an exception
                // processing the web.xml, set facesServletFound to true to
                // default to our previous behavior of processing the faces
                // configuration.
                if (LOGGER.isLoggable(Level.WARNING)) {
                    LOGGER.warning("Unable to process deployment descriptor for" +
                        " context '" + context.getServletContextName() + '\'');
                }
                facesServletPresent = true;
            }

        } // END scanForFacesServlet

        /**
         * <p>Return a <code>SAXParserFactory</code> instance that is
         * non-validating and is namespace aware.</p>
         * @return configured <code>SAXParserFactory</code>
         */
        private SAXParserFactory getConfiguredFactory() {

            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setValidating(false);
            factory.setNamespaceAware(true);
            return factory;

        } // END getConfiguredFactory


        /**
         * <p>A simple SAX handler to process the elements of interested
         * within a web application's deployment descriptor.</p>
         */
        private class WebXmlHandler extends DefaultHandler {

            private static final String SERVLET_CLASS = "servlet-class";
            private static final String FACES_SERVLET =
                "javax.faces.webapp.FacesServlet";

            private boolean servletClassFound;
            private StringBuffer content;

            public InputSource resolveEntity(String publicId, String systemId)
            throws SAXException {

                return new InputSource(new StringReader(""));

            } // END resolveEntity


            public void startElement(String uri, String localName,
                                     String qName, Attributes attributes)
            throws SAXException {

                if (!facesServletPresent) {
                    if (SERVLET_CLASS.equals(localName)) {
                        servletClassFound = true;
                        content = new StringBuffer();
                    } else {
                        servletClassFound = false;
                    }
                }

            } // END startElement


            public void characters(char[] ch, int start, int length)
            throws SAXException {

                if (servletClassFound && !facesServletPresent) {
                    content.append(ch, start, length);
                }

            } // END characters


            public void endElement(String uri, String localName, String qName)
            throws SAXException {

                if (servletClassFound && !facesServletPresent) {
                    if (FACES_SERVLET.equals(content.toString().trim())) {
                        facesServletPresent = true;
                    }
                }

            } // END endElement

        } // END WebXmlHandler

    } // END WebXmlProcessor

}


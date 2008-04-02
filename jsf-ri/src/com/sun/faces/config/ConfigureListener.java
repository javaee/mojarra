/*
 * $Id: ConfigureListener.java,v 1.17 2004/05/11 19:28:41 rkitain Exp $
 */
/*
 * Copyright 2004 Sun Microsystems, Inc. All Rights Reserved.
 * 
 * Redistribution and use in source and binary forms, with or
 * without modification, are permitted provided that the following
 * conditions are met:
 * 
 * - Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 * 
 * - Redistribution in binary form must reproduce the above
 *   copyright notice, this list of conditions and the following
 *   disclaimer in the documentation and/or other materials
 *   provided with the distribution.
 *    
 * Neither the name of Sun Microsystems, Inc. or the names of
 * contributors may be used to endorse or promote products derived
 * from this software without specific prior written permission.
 *  
 * This software is provided "AS IS," without a warranty of any
 * kind. ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND
 * WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY
 * EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY
 * DAMAGES OR LIABILITIES SUFFERED BY LICENSEE AS A RESULT OF OR
 * RELATING TO USE, MODIFICATION OR DISTRIBUTION OF THIS SOFTWARE OR
 * ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE
 * FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT,
 * SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF
 * THE USE OF OR INABILITY TO USE THIS SOFTWARE, EVEN IF SUN HAS
 * BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 *  
 * You acknowledge that this software is not designed, licensed or
 * intended for use in the design, construction, operation or
 * maintenance of any nuclear facility.
 */

package com.sun.faces.config;

import com.sun.faces.RIConstants;
import com.sun.faces.application.ApplicationAssociate;
import com.sun.faces.application.ConfigNavigationCase;
import com.sun.faces.application.ViewHandlerImpl;
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
import com.sun.faces.config.beans.ValidatorBean;
import com.sun.faces.config.rules.FacesConfigRuleSet;
import com.sun.faces.util.Util;
import org.apache.commons.digester.Digester;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;
import javax.faces.application.NavigationHandler;
import javax.faces.application.StateManager;
import javax.faces.application.ViewHandler;
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

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;


/**
 * <p>Parse all relevant JavaServer Faces configuration resources, and
 * configure the Reference Implementation runtime environment.</p>
 * <p/>
 */
public class ConfigureListener implements ServletContextListener {


    // -------------------------------------------------------- Static Variables

    protected static String FACES_CONFIG_BEAN_KEY = RIConstants.FACES_PREFIX +
	"FACES_CONFIG_BEAN";


    /**
     * <p>The set of <code>ClassLoader</code> instances that have
     * already been configured by this <code>ConfigureListener</code>.</p>
     */
    private static Set loaders = new HashSet();


    /**
     * <p>The <code>Log</code> instance for this class.</p>
     */
    private static Log log = LogFactory.getLog(ConfigureListener.class);


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

    private static ThreadLocal tlsServletContext = new ThreadLocal() {
            protected Object initialValue() { return (null); }
        };

    static ThreadLocal getThreadLocalServletContext() {
        if (RIConstants.IS_UNIT_TEST_MODE) {
	    return tlsServletContext;
	}
	return null;
    }

    /**
     * <p>During the execution of {@link #contextInitialized}, this
     * method will return the ServletContext instance.</p>
     */

    public static ServletContext getServletContextDuringInitialize() {
	return (ServletContext) tlsServletContext.get();
    }

    public void contextInitialized(ServletContextEvent sce) {
        // Prepare local variables we will need
        Digester digester = null;
        FacesConfigBean fcb = new FacesConfigBean();
        ServletContext context = sce.getServletContext();

	// store the servletContext instance in Thread local Storage.
	// This enables our Application's ApplicationAssociate to locate
	// it so it can store the ApplicationAssociate in the
	// ServletContext.
	tlsServletContext.set(context);

	// see if we're operating in the unit test environment
	try {
	    if (RIConstants.IS_UNIT_TEST_MODE) {
		// if so, put the fcb in the servletContext
		context.setAttribute(FACES_CONFIG_BEAN_KEY, fcb);
	    }
	}
	catch (Exception e) {
	    if (log.isDebugEnabled()) {
		log.debug("Can't query for test environment");
	    }
	}

	// see if we need to disable our TLValidator
	String enableHtmlTLValidator = null;
	if (null != (enableHtmlTLValidator = 
		     context.getInitParameter(RIConstants.ENABLE_HTML_TLV))) {
	    if (enableHtmlTLValidator.toString().equals("true")) {
		RIConstants.setHtmlTagLibValidatorActive(true);
	    }
	}
        URL url = null;
        if (log.isDebugEnabled()) {
            log.debug("contextInitialized(" + context.getServletContextName()
                      + ")");
        }

        // Ensure that we initialize a particular application ony once
        if (initialized(context)) {
            return;
        }

        // Step 0, parse the url-pattern information for the
        // FacesServlet.  This information is passed onto the
        // ConfigParser for later use.
        WebXmlParser webXmlParser = new WebXmlParser(context);
        List mappings = webXmlParser.getFacesServletMappings();

        // Step 1, configure a Digester instance we can use
        try {
            boolean validateXml = validateTheXml(context);
            digester = digester(validateXml);
        } catch (MalformedURLException e) {
            throw new FacesException(e); // PENDING - add message
        }

        // Step 2, parse the RI configuration resource
        try {
            url = Util.getCurrentLoader(this).getResource
                (RIConstants.JSF_RI_CONFIG);
            parse(digester, url, fcb);
        } catch (Exception e) {
            String message = null;
            try {
                message = Util.getExceptionMessageString
                    (Util.CANT_PARSE_FILE_ERROR_MESSAGE_ID,
                     new Object[]{url.toExternalForm()});
            } catch (Exception ee) {
                message = "Can't parse configuration file:" +
                    url.toExternalForm();
            }
            log.warn(message, e);
            throw new FacesException(message, e);
        }

        // Step 3, parse the Standard HTML RenderKit
        try {
            url = Util.getCurrentLoader(this).getResource
                (RIConstants.JSF_RI_STANDARD);
            parse(digester, url, fcb);
        } catch (Exception e) {
            String message = null;
            try {
                message = Util.getExceptionMessageString
                    (Util.CANT_PARSE_FILE_ERROR_MESSAGE_ID,
                     new Object[]{url.toExternalForm()});
            } catch (Exception ee) {
                message = "Can't parse configuration file:" +
                    url.toExternalForm();
            }
            log.warn(message, e);
            throw new FacesException(message, e);
        }

        // Step 4, parse any "/META-INF/faces-config.xml" resources
        Iterator resources;
        try {
            List list = new LinkedList();
            Enumeration items = Util.getCurrentLoader(this).getResources
                ("META-INF/faces-config.xml");
            while (items.hasMoreElements()) {
                list.add(0, items.nextElement());
            }
            resources = list.iterator();
        } catch (IOException e) {
            String message = null;
            try {
                message = Util.getExceptionMessageString
                    (Util.CANT_PARSE_FILE_ERROR_MESSAGE_ID,
                     new Object[]{"/META-INF/faces-config.xml"});
            } catch (Exception ee) {
                message = "Can't parse configuration file:" +
                    "/META-INF/faces-config.xml";
            }
            log.warn(message, e);
            throw new FacesException(message, e);
        }
        while (resources.hasNext()) {
            url = (URL) resources.next();
            try {
                parse(digester, url, fcb);
            } catch (Exception e) {
                String message = null;
                try {
                    message = Util.getExceptionMessageString
                        (Util.CANT_PARSE_FILE_ERROR_MESSAGE_ID,
                         new Object[]{url.toExternalForm()});
                } catch (Exception ee) {
                    message = "Can't parse configuration file:" +
                        url.toExternalForm();
                }
                log.warn(message, e);
                throw new FacesException(message, e);
            }
        }

        // Step 5, parse any context-relative resources specified in
        // the web application deployment descriptor
        String paths =
            context.getInitParameter(FacesServlet.CONFIG_FILES_ATTR);
        if (paths != null) {
            paths = paths.trim();
            String path;
            while (paths.length() > 0) {

                // Identify the next resource path to load
                int comma = paths.indexOf(",");
                if (comma >= 0) {
                    path = paths.substring(0, comma).trim();
                    paths = paths.substring(comma + 1).trim();
                } else {
                    path = paths.trim();
                    paths = "";
                }
                if (path.length() < 1) {
                    break;
                }

                try {
                    url = context.getResource(path);
                    parse(digester, url, fcb);
                } catch (Exception e) {
                    String message = null;
                    try {
                        message = Util.getExceptionMessageString
                            (Util.CANT_PARSE_FILE_ERROR_MESSAGE_ID,
                             new Object[]{path});
                    } catch (Exception ee) {
                        message = "Can't parse configuration file:" + path;
                    }
                    log.warn(message, e);
                    throw new FacesException(message, e);
                }
            }
        }

        // Step 6, parse "/WEB-INF/faces-config.xml" if it exists
        try {
            url = context.getResource("/WEB-INF/faces-config.xml");
            if (url != null) {
                parse(digester, url, fcb);
            }
        } catch (Exception e) {
            String message = null;
            try {
                message = Util.getExceptionMessageString
                    (Util.CANT_PARSE_FILE_ERROR_MESSAGE_ID,
                     new Object[]{url.toExternalForm()});
            } catch (Exception ee) {
                message = "Can't parse configuration file:" +
                    url.toExternalForm();
            }
            log.warn(message, e);
            throw new FacesException(message, e);
        }

        // Step 7, use the accumulated configuration beans to configure the RI
        try {
            configure(context, fcb, mappings);
        } catch (FacesException e) {
            throw e;
        } catch (Exception e) {
            throw new FacesException(e);
        }

        // Step 8, verify that all the configured factories are available
        // and optionall that configured objects can be created
        verifyFactories();
        if (shouldVerifyObjects(context)) {
            verifyObjects(context, fcb);
        }

        context.setAttribute(RIConstants.CONFIG_ATTR, Boolean.TRUE);
	tlsServletContext.set(null);
    }


    public void contextDestroyed(ServletContextEvent sce) {

        ServletContext context = sce.getServletContext();
        if (log.isDebugEnabled()) {
            log.debug("contextDestroyed(" + context.getServletContextName()
                      + ")");
        }

        // Release any allocated application resources
        context.removeAttribute(RIConstants.CONFIG_ATTR);
	FactoryFinder.releaseFactories();
	ApplicationAssociate.clearInstance(context);
	tlsServletContext.set(null);

        // Release the initialization mark on this web application
        release(context);

    }


    // --------------------------------------------------------- Private Methods


    /**
     * <p>Return the implementation-specific <code>Application</code>
     * instance for this application.  You must <strong>NOT</strong>
     * call this method prior to configuring the appropriate
     * <code>ApplicationFactory</code> class.</p>
     */
    private Application application() {

        ApplicationFactory afactory = (ApplicationFactory)
            FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
        return afactory.getApplication();

    }


    /**
     * <p>Configure the JavaServer Faces reference implementation based on
     * the accumulated configuration beans.</p>
     *
     * @param context <code>ServletContext</code> for this web application
     * @param config  <code>FacesConfigBean</code> that is the root of the
     *                tree of configuration information
     */
    protected void configure(ServletContext context, FacesConfigBean config,
                           List mappings) throws Exception {
        configure(config.getFactory());
        configure(config.getLifecycle());

        configure(config.getApplication(), mappings);
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
     * @param mappings List of mappings for <code>FacesServlet</code>
     */
    private void configure(ApplicationBean config, List mappings)
        throws Exception {

        if (config == null) {
            return;
        }
        Application application = application();
        Object instance;
        String value;
        String values[];

        // Configure scalar properties

        configure(config.getLocaleConfig());

        value = config.getMessageBundle();
        if (value != null) {
            if (log.isTraceEnabled()) {
                log.trace("setMessageBundle(" + value + ")");
            }
            application.setMessageBundle(value);
        }

        value = config.getDefaultRenderKitId();
        if (value != null) {
            if (log.isTraceEnabled()) {
                log.trace("setDefaultRenderKitId(" + value + ")");
            }
            application.setDefaultRenderKitId(value);
        }

        // Configure chains of handlers

        values = config.getActionListeners();
        if ((values != null) && (values.length > 0)) {
            for (int i = 0; i < values.length; i++) {
                if (log.isTraceEnabled()) {
                    log.trace("setActionListener(" + values[i] + ")");
                }
                instance = Util.createInstance
                    (values[i], ActionListener.class,
                     application.getActionListener());
                if (instance != null) {
                    application.setActionListener((ActionListener) instance);
                }
            }
        }

        values = config.getNavigationHandlers();
        if ((values != null) && (values.length > 0)) {
            for (int i = 0; i < values.length; i++) {
                if (log.isTraceEnabled()) {
                    log.trace("setNavigationHandler(" + values[i] + ")");
                }
                instance = Util.createInstance
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
            for (int i = 0; i < values.length; i++) {
                if (log.isTraceEnabled()) {
                    log.trace("setPropertyResolver(" + values[i] + ")");
                }
                instance = Util.createInstance
                    (values[i], PropertyResolver.class,
                     application.getPropertyResolver());
                if (instance != null) {
                    application.setPropertyResolver
                        ((PropertyResolver) instance);
                }
            }
        }

        values = config.getStateManagers();
        if ((values != null) && (values.length > 0)) {
            for (int i = 0; i < values.length; i++) {
                if (log.isTraceEnabled()) {
                    log.trace("setStateManager(" + values[i] + ")");
                }
                instance = Util.createInstance
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
            for (int i = 0; i < values.length; i++) {
                if (log.isTraceEnabled()) {
                    log.trace("setVariableResolver(" + values[i] + ")");
                }
                instance = Util.createInstance
                    (values[i], VariableResolver.class,
                     application.getVariableResolver());
                if (instance != null) {
                    application.setVariableResolver
                        ((VariableResolver) instance);
                }
            }
        }

        values = config.getViewHandlers();
        if ((values != null) && (values.length > 0)) {
            for (int i = 0; i < values.length; i++) {
                if (log.isTraceEnabled()) {
                    log.trace("setViewHandler(" + values[i] + ")");
                }
                instance = Util.createInstance
                    (values[i], ViewHandler.class,
                     application.getViewHandler());
                if (instance instanceof ViewHandlerImpl) {
                    ((ViewHandlerImpl) instance).setFacesMapping(mappings);
                }
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
    private void configure(ComponentBean config[]) throws Exception {

        if (config == null) {
            return;
        }
        Application application = application();

        for (int i = 0; i < config.length; i++) {
            if (log.isTraceEnabled()) {
                log.trace("addComponent(" +
                          config[i].getComponentType() + "," +
                          config[i].getComponentClass() + ")");
            }
            application.addComponent(config[i].getComponentType(),
                                     config[i].getComponentClass());
        }

    }


    private static Class primitiveClassesToConvert[] = {
        java.lang.Boolean.TYPE,
        java.lang.Byte.TYPE,
        java.lang.Character.TYPE,
        java.lang.Double.TYPE,
        java.lang.Float.TYPE,
        java.lang.Integer.TYPE,
        java.lang.Long.TYPE,
        java.lang.Short.TYPE
    };

    private static String convertersForPrimitives[] = {
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
     * <p>Configure all registered converters.</p>
     *
     * @param config Array of <code>ConverterBean</code> that contains
     *               our configuration information
     */
    private void configure(ConverterBean config[]) throws Exception {
        int i = 0, len = 0;
        Application application = application();

        // at a minimum, configure the primitive converters
        for (i = 0, len = primitiveClassesToConvert.length; i < len; i++) {
            if (log.isTraceEnabled()) {
                log.trace("addConverterByClass(" +
                          primitiveClassesToConvert[i].toString() + "," +
                          convertersForPrimitives[i].toString() + ")");
            }
            application.addConverter(primitiveClassesToConvert[i],
                                     convertersForPrimitives[i]);
        }

        if (config == null) {
            return;
        }

        for (i = 0, len = config.length; i < len; i++) {
            if (config[i].getConverterId() != null) {
                if (log.isTraceEnabled()) {
                    log.trace("addConverterById(" +
                              config[i].getConverterId() + "," +
                              config[i].getConverterClass() + ")");
                }
                application.addConverter(config[i].getConverterId(),
                                         config[i].getConverterClass());
            } else {
                if (log.isTraceEnabled()) {
                    log.trace("addConverterByClass(" +
                              config[i].getConverterForClass() + "," +
                              config[i].getConverterClass() + ")");
                }
                Class clazz = Util.getCurrentLoader(this).loadClass
                    (config[i].getConverterForClass());
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
	Iterator iter = null;
        String value;

	iter = config.getApplicationFactories().iterator();
	while (iter.hasNext()) {
	    value = (String) iter.next();
	    if (value != null) {
		if (log.isTraceEnabled()) {
		    log.trace("setApplicationFactory(" + value + ")");
		}
		FactoryFinder.setFactory(FactoryFinder.APPLICATION_FACTORY,
					 value);
	    }
	}

	iter = config.getFacesContextFactories().iterator();
	while (iter.hasNext()) {
	    value = (String) iter.next();
	    if (value != null) {
		if (log.isTraceEnabled()) {
		    log.trace("setFacesContextFactory(" + value + ")");
		}
		FactoryFinder.setFactory(FactoryFinder.FACES_CONTEXT_FACTORY,
					 value);
	    }
	}

	iter = config.getLifecycleFactories().iterator();
	while (iter.hasNext()) {
	    value = (String) iter.next();
	    if (value != null) {
		if (log.isTraceEnabled()) {
		    log.trace("setLifecycleFactory(" + value + ")");
		}
		FactoryFinder.setFactory(FactoryFinder.LIFECYCLE_FACTORY,
					 value);
	    }
	}

	iter = config.getRenderKitFactories().iterator();
	while (iter.hasNext()) {
	    value = (String) iter.next();
	    if (value != null) {
		if (log.isTraceEnabled()) {
		    log.trace("setRenderKitFactory(" + value + ")");
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
     *               configuration information
     */
    private void configure(LifecycleBean config) throws Exception {

        if (config == null) {
            return;
        }
        String listeners[] = config.getPhaseListeners();
        LifecycleFactory factory = (LifecycleFactory)
            FactoryFinder.getFactory(FactoryFinder.LIFECYCLE_FACTORY);
        Lifecycle lifecycle =
            factory.getLifecycle(LifecycleFactory.DEFAULT_LIFECYCLE);
        for (int i = 0; i < listeners.length; i++) {
            if (log.isTraceEnabled()) {
                log.trace("addPhaseListener(" + listeners[i] + ")");
            }
            ClassLoader cl = Util.getCurrentLoader(this);
            Class clazz = cl.loadClass(listeners[i]);
            lifecycle.addPhaseListener((PhaseListener) clazz.newInstance());
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
        String values[];

        value = config.getDefaultLocale();
        if (value != null) {
            if (log.isTraceEnabled()) {
                if (log.isTraceEnabled()) {
                    log.trace("setDefaultLocale(" + value + ")");
                }
                application.setDefaultLocale
                    (Util.getLocaleFromString(value));
            }
        }

        values = config.getSupportedLocales();
        if ((values != null) && (values.length > 0)) {
            List locales = new ArrayList();
            for (int i = 0; i < values.length; i++) {
                if (log.isTraceEnabled()) {
                    log.trace("addSupportedLocale(" + values[i] + ")");
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
    // PENDING - the code below is a start at converting new-style config beans
    // back to old style ones so we don't have to modify the functional code.
    // It is not clear that this is the lower-effort choice, however.
    private void configure(ManagedBeanBean config[]) throws Exception {
        if (config == null) {
            return;
        }
        Application application = application();
	ApplicationAssociate associate = 
	    ApplicationAssociate.getInstance(getServletContextDuringInitialize());

	if (null == associate) {
	    return;
	}

        for (int i = 0; i < config.length; i++) {
            if (log.isTraceEnabled()) {
                log.trace("addManagedBean(" +
                          config[i].getManagedBeanName() + "," +
                          config[i].getManagedBeanClass() + ")");
            }
            ManagedBeanFactory mbf = new ManagedBeanFactory(config[i]);
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
    private void configure(NavigationRuleBean config[]) throws Exception {

        if (config == null) {
            return;
        }
        Application application = application();
	ApplicationAssociate associate = 
	    ApplicationAssociate.getInstance(getServletContextDuringInitialize());
	
	if (null == associate) {
	    return;
	}

        for (int i = 0; i < config.length; i++) {
            if (log.isTraceEnabled()) {
                log.trace("addNavigationRule(" +
                          config[i].getFromViewId() + ")");
            }
            NavigationCaseBean ncb[] = config[i].getNavigationCases();
            for (int j = 0; j < ncb.length; j++) {
                if (log.isTraceEnabled()) {
                    log.trace("addNavigationCase(" +
                              ncb[j].getFromAction() + "," +
                              ncb[j].getFromOutcome() + "," +
                              ncb[j].isRedirect() + "," +
                              ncb[j].getToViewId() + ")");
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
                String toViewId = ncb[j].getToViewId();
                if (toViewId == null) {
                    toViewId = "-";
                }
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
    private void configure(RendererBean config[], RenderKit rk)
        throws Exception {

        if (config == null) {
            return;
        }

        for (int i = 0; i < config.length; i++) {
            if (log.isTraceEnabled()) {
                log.trace("addRenderer(" +
                          config[i].getComponentFamily() + "," +
                          config[i].getRendererType() + "," +
                          config[i].getRendererClass() + ")");
            }
            Renderer r = (Renderer)
                Util.getCurrentLoader(this).
                loadClass(config[i].getRendererClass()).
                newInstance();
            rk.addRenderer(config[i].getComponentFamily(),
                           config[i].getRendererType(),
                           r);
        }

    }


    /**
     * <p>Configure all registered renderKits.</p>
     *
     * @param config Array of <code>RenderKitBean</code> that contains
     *               our configuration information
     */
    private void configure(RenderKitBean config[]) throws Exception {

        if (config == null) {
            return;
        }
        RenderKitFactory rkFactory = (RenderKitFactory)
            FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);

        for (int i = 0; i < config.length; i++) {
            RenderKit rk = rkFactory.getRenderKit
                (null, config[i].getRenderKitId());
            if (rk == null) {
                if (log.isTraceEnabled()) {
                    log.trace("createRenderKit(" +
                              config[i].getRenderKitId() + "," +
                              config[i].getRenderKitClass() + ")");
                }
                if (config[i].getRenderKitClass() == null) {
                    throw new IllegalArgumentException// PENDING - i18n
                        ("No renderKitClass for renderKit " +
                         config[i].getRenderKitId());
                }
                rk = (RenderKit)
                    Util.getCurrentLoader(this).
                    loadClass(config[i].getRenderKitClass()).
                    newInstance();
                rkFactory.addRenderKit(config[i].getRenderKitId(), rk);
            } else {
                if (log.isTraceEnabled()) {
                    log.trace("getRenderKit(" +
                              config[i].getRenderKitId() + ")");
                }
            }
            configure(config[i].getRenderers(), rk);
        }

    }


    /**
     * <p>Configure all registered validators.</p>
     *
     * @param config Array of <code>ValidatorBean</code> that contains
     *               our configuration information
     */
    private void configure(ValidatorBean config[]) throws Exception {

        if (config == null) {
            return;
        }
        Application application = application();

        for (int i = 0; i < config.length; i++) {
            if (log.isTraceEnabled()) {
                log.trace("addValidator(" +
                          config[i].getValidatorId() + "," +
                          config[i].getValidatorClass() + ")");
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
     * @throws MalformedURLException if a URL cannot be formed correctly
     */
    protected Digester digester(boolean validateXml)
        throws MalformedURLException {
        Digester digester = new Digester();

        // Configure basic properties
        digester.setNamespaceAware(false);
        digester.setUseContextClassLoader(true);
        digester.setValidating(validateXml);

        // Configure parsing rules
        digester.addRuleSet(new FacesConfigRuleSet(false, false, true));

        // Configure preregistered entities
        URL url = this.getClass().getResource
            ("/com/sun/faces/web-facesconfig_1_1.dtd");
        digester.register
            ("-//Sun Microsystems, Inc.//DTD JavaServer Faces Config 1.1//EN",
             url.toString());

        // Push an initial FacesConfigBean onto the stack
        digester.push(new FacesConfigBean());

        return (digester);

    }


    private String factoryNames[] =
        {
            FactoryFinder.APPLICATION_FACTORY,
            FactoryFinder.FACES_CONTEXT_FACTORY,
            FactoryFinder.LIFECYCLE_FACTORY,
            FactoryFinder.RENDER_KIT_FACTORY
        };


    /**
     * <p>Verify that all of the required factory objects are available.</p>
     *
     * @throws FacesException if a factory cannot be created
     */
    private void verifyFactories() throws FacesException {

        for (int i = 0, len = factoryNames.length; i < len; i++) {
            try {
                Object factory = FactoryFinder.getFactory(factoryNames[i]);
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
     *
     * @param context <code>ServletContext</code> for this web application
     */
    private boolean initialized(ServletContext context) {

        // Initialize at most once per web application class loader
        ClassLoader cl = Util.getCurrentLoader(this);
        synchronized (loaders) {
            if (!loaders.contains(cl)) {
                loaders.add(cl);
                if (log.isTraceEnabled()) {
                    log.trace("Initializing this webapp");
                }
                return false;
            } else {
                if (log.isTraceEnabled()) {
                    log.trace("Listener already completed for this webapp");
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

        if (log.isDebugEnabled()) {
            log.debug("Verifying application objects");
        }

        ApplicationFactory af = (ApplicationFactory)
            FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
        Application app = af.getApplication();
        RenderKitFactory rkf = (RenderKitFactory)
            FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
        boolean success = true;

        // Check components
        ComponentBean comp[] = fcb.getComponents();
        for (int i = 0, len = comp.length; i < len; i++) {
            try {
                app.createComponent(comp[i].getComponentType());
            } catch (Exception e) {
                context.log(comp[i].getComponentClass(), e);
                success = false;
            }
        }

        // Check converters
        ConverterBean conv1[] = fcb.getConvertersByClass();
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
        ConverterBean conv2[] = fcb.getConvertersById();
        for (int i = 0, len = conv2.length; i < len; i++) {
            try {
                app.createConverter(conv2[i].getConverterId());
            } catch (Exception e) {
                context.log(conv2[i].getConverterClass());
                success = false;
            }
        }

        // Check renderers
        RenderKitBean rkb[] = fcb.getRenderKits();
        RenderKit rk;
        for (int i = 0, len = rkb.length; i < len; i++) {
            try {
                rk = rkf.getRenderKit(null, rkb[i].getRenderKitId());
                RendererBean rb[] = rkb[i].getRenderers();
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
        ValidatorBean val[] = fcb.getValidators();
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
                message = Util.getExceptionMessageString
                    (Util.OBJECT_CREATION_ERROR_ID,
                     new Object[]{});
            } catch (Exception ee) {
                message = "One or more configured application objects " +
                    "cannot be created.  See your web application logs " +
                    "for details.";
            }
            log.warn(message);
            throw new FacesException(message);
        } else {
            if (log.isInfoEnabled()) {
                log.info(
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
     * @throws IOException  if an input/output error occurs
     * @throws SAXException if an XML parsing error occurs
     */
    protected void parse(Digester digester, URL url, FacesConfigBean fcb)
        throws IOException, SAXException {

        if (log.isDebugEnabled()) {
            log.debug("parse(" + url.toExternalForm() + ")");
        }

        URLConnection conn = null;
        InputStream stream = null;
        InputSource source = null;
        try {
            conn = url.openConnection();
            conn.setUseCaches(false);
            stream = conn.getInputStream();
            source = new InputSource(url.toExternalForm());
            source.setByteStream(stream);
            digester.clear();
            digester.push(fcb);
            digester.parse(source);
            stream.close();
            stream = null;
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (Exception e) {
                    ;
                }
            }
            stream = null;
        }

    }


    /**
     * <p>Determine if we will turn on validation during parsing.
     *
     * @param sc the servlet context
     */
    protected boolean validateTheXml(ServletContext sc) {
        String validateXml = sc.getInitParameter(RIConstants.VALIDATE_XML);
        if (validateXml != null) {
            if (!(validateXml.equals("true")) &&
                !(validateXml.equals("false"))) {
                Object[] obj = new Object[1];
                obj[0] = "validateXml";
                throw new FacesException(Util.getExceptionMessageString(
                    Util.INVALID_INIT_PARAM_ERROR_MESSAGE_ID, obj));
            }
        } else {
            validateXml = "false";
        }
        return new Boolean(validateXml).booleanValue();
    }


    /**
     * <p>Determine if we will verify objects after configuration.
     *
     * @param sc the servlet context
     */
    private boolean shouldVerifyObjects(ServletContext sc) {
        String validateXml = sc.getInitParameter(RIConstants.VERIFY_OBJECTS);
        if (validateXml != null) {
            if (!(validateXml.equals("true")) &&
                !(validateXml.equals("false"))) {
                Object[] obj = new Object[1];
                obj[0] = "validateXml";
                throw new FacesException(Util.getExceptionMessageString(
                    Util.INVALID_INIT_PARAM_ERROR_MESSAGE_ID, obj));
            }
        } else {
            validateXml = "false";
        }
        return new Boolean(validateXml).booleanValue();
    }


    /**
     * <p>Release the mark that this web application has been initialized.</p>
     *
     * @param context <code>ServletContext</code> for this web application
     */
    private void release(ServletContext context) {

        ClassLoader cl = Util.getCurrentLoader(this);
        synchronized (loaders) {
            loaders.remove(cl);
        }

    }


} 

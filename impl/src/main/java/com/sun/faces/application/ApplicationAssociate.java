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

package com.sun.faces.application;

import static com.sun.faces.RIConstants.FACES_CONFIG_VERSION;
import static com.sun.faces.RIConstants.FACES_PREFIX;
import static com.sun.faces.config.ConfigManager.getAnnotatedClasses;
import static com.sun.faces.config.WebConfiguration.BooleanWebContextInitParameter.DisableFaceletJSFViewHandler;
import static com.sun.faces.config.WebConfiguration.BooleanWebContextInitParameter.DisableFaceletJSFViewHandlerDeprecated;
import static com.sun.faces.config.WebConfiguration.BooleanWebContextInitParameter.EnableFaceletsResourceResolverResolveCompositeComponents;
import static com.sun.faces.config.WebConfiguration.BooleanWebContextInitParameter.EnableLazyBeanValidation;
import static com.sun.faces.config.WebConfiguration.BooleanWebContextInitParameter.FaceletsSkipComments;
import static com.sun.faces.config.WebConfiguration.WebContextInitParameter.FaceletCache;
import static com.sun.faces.config.WebConfiguration.WebContextInitParameter.FaceletsDecorators;
import static com.sun.faces.config.WebConfiguration.WebContextInitParameter.FaceletsDefaultRefreshPeriod;
import static com.sun.faces.config.WebConfiguration.WebContextInitParameter.FaceletsDefaultRefreshPeriodDeprecated;
import static com.sun.faces.config.WebConfiguration.WebContextInitParameter.FaceletsResourceResolver;
import static com.sun.faces.el.ELUtils.buildFacesResolver;
import static com.sun.faces.el.FacesCompositeELResolver.ELResolverChainType.Faces;
import static com.sun.faces.facelets.util.ReflectionUtil.decorateInstance;
import static com.sun.faces.facelets.util.ReflectionUtil.forName;
import static com.sun.faces.lifecycle.ELResolverInitPhaseListener.populateFacesELResolverForJsp;
import static com.sun.faces.util.MessageUtils.APPLICATION_ASSOCIATE_EXISTS_ID;
import static com.sun.faces.util.MessageUtils.getExceptionMessageString;
import static com.sun.faces.util.Util.getFacesConfigXmlVersion;
import static com.sun.faces.util.Util.isCdiAvailable;
import static com.sun.faces.util.Util.split;
import static java.lang.Long.parseLong;
import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;
import static java.util.logging.Level.FINE;
import static java.util.logging.Level.INFO;
import static java.util.logging.Level.SEVERE;
import static javax.faces.FactoryFinder.FACELET_CACHE_FACTORY;
import static javax.faces.FactoryFinder.FLOW_HANDLER_FACTORY;
import static javax.faces.application.ProjectStage.Development;
import static javax.faces.application.ProjectStage.Production;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import javax.el.CompositeELResolver;
import javax.el.ELResolver;
import javax.el.ExpressionFactory;
import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.application.NavigationCase;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.component.search.SearchExpressionHandler;
import javax.faces.component.search.SearchKeywordResolver;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.el.PropertyResolver;
import javax.faces.el.VariableResolver;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.PostConstructApplicationEvent;
import javax.faces.event.PreDestroyCustomScopeEvent;
import javax.faces.event.ScopeContext;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;
import javax.faces.flow.FlowHandler;
import javax.faces.flow.FlowHandlerFactory;
import javax.faces.view.facelets.FaceletCache;
import javax.faces.view.facelets.FaceletCacheFactory;
import javax.faces.view.facelets.FaceletsResourceResolver;
import javax.faces.view.facelets.ResourceResolver;
import javax.faces.view.facelets.TagDecorator;
import javax.servlet.ServletContext;

import com.sun.faces.RIConstants;
import com.sun.faces.application.annotation.AnnotationManager;
import com.sun.faces.application.annotation.FacesComponentUsage;
import com.sun.faces.application.resource.ResourceCache;
import com.sun.faces.application.resource.ResourceManager;
import com.sun.faces.component.search.SearchExpressionHandlerImpl;
import com.sun.faces.config.ConfigManager;
import com.sun.faces.config.WebConfiguration;
import com.sun.faces.el.DemuxCompositeELResolver;
import com.sun.faces.el.FacesCompositeELResolver;
import com.sun.faces.el.VariableResolverChainWrapper;
import com.sun.faces.facelets.PrivateApiFaceletCacheAdapter;
import com.sun.faces.facelets.compiler.Compiler;
import com.sun.faces.facelets.compiler.SAXCompiler;
import com.sun.faces.facelets.impl.DefaultFaceletFactory;
import com.sun.faces.facelets.impl.DefaultResourceResolver;
import com.sun.faces.facelets.tag.composite.CompositeLibrary;
import com.sun.faces.facelets.tag.jsf.PassThroughAttributeLibrary;
import com.sun.faces.facelets.tag.jsf.PassThroughElementLibrary;
import com.sun.faces.facelets.tag.jsf.core.CoreLibrary;
import com.sun.faces.facelets.tag.jsf.html.HtmlLibrary;
import com.sun.faces.facelets.tag.jstl.core.JstlCoreLibrary;
import com.sun.faces.facelets.tag.jstl.fn.JstlFunction;
import com.sun.faces.facelets.tag.ui.UILibrary;
import com.sun.faces.facelets.util.DevTools;
import com.sun.faces.facelets.util.FunctionLibrary;
import com.sun.faces.facelets.util.ReflectionUtil;
import com.sun.faces.mgbean.BeanManager;
import com.sun.faces.spi.InjectionProvider;
import com.sun.faces.util.FacesLogger;

/**
 * <p>
 * Break out the things that are associated with the Application, but need to be present even when
 * the user has replaced the Application instance.
 * </p>
 * <p/>
 * <p>
 * For example: the user replaces ApplicationFactory, and wants to intercept calls to
 * createValueExpression() and createMethodExpression() for certain kinds of expressions, but allow
 * the existing application to handle the rest.
 * </p>
 */
public class ApplicationAssociate {

    private static final Logger LOGGER = FacesLogger.APPLICATION.getLogger();

    private ApplicationImpl app = null;

    /**
     * Overall Map containing <code>from-view-id</code> key and <code>Set</code> of
     * <code>NavigationCase</code> objects for that key; The <code>from-view-id</code> strings in
     * this map will be stored as specified in the configuration file - some of them will have a
     * trailing asterisk "*" signifying wild card, and some may be specified as an asterisk "*".
     */
    private Map<String, Set<NavigationCase>> navigationMap = null;

    /*
     * The FacesComponentTagLibrary uses the information in this map to help it fabricate tag
     * handlers for components annotated with FacesComponent. Key: namespace
     */
    private Map<String, List<FacesComponentUsage>> facesComponentsByNamespace = null;

    // Flag indicating that a response has been rendered.
    private boolean responseRendered = false;

    private static final String ASSOCIATE_KEY = RIConstants.FACES_PREFIX + "ApplicationAssociate";

    private static ThreadLocal<ApplicationAssociate> instance = new ThreadLocal<ApplicationAssociate>() {
        @Override
        protected ApplicationAssociate initialValue() {
            return null;
        }
    };

    private List<ELResolver> elResolversFromFacesConfig = null;

    private List<SearchKeywordResolver> searchKeywordResolversFromFacesConfig = null;

    @SuppressWarnings("deprecation")
    private VariableResolver legacyVRChainHead = null;

    private VariableResolverChainWrapper legacyVRChainHeadWrapperForJsp = null;

    private VariableResolverChainWrapper legacyVRChainHeadWrapperForFaces = null;

    @SuppressWarnings("deprecation")
    private PropertyResolver legacyPRChainHead = null;
    private ExpressionFactory expressionFactory = null;

    @SuppressWarnings("deprecation")
    private PropertyResolver legacyPropertyResolver = null;

    @SuppressWarnings("deprecation")
    private VariableResolver legacyVariableResolver = null;
    private FacesCompositeELResolver facesELResolverForJsp = null;

    private InjectionProvider injectionProvider;
    private ResourceCache resourceCache;

    private String contextName;
    private boolean requestServiced;
    private boolean errorPagePresent;

    private BeanManager beanManager;
    private AnnotationManager annotationManager;
    private boolean devModeEnabled;
    private Compiler compiler;
    private DefaultFaceletFactory faceletFactory;
    private ResourceManager resourceManager;
    private ApplicationStateInfo applicationStateInfo;

    private PropertyEditorHelper propertyEditorHelper;

    private NamedEventManager namedEventManager;

    private WebConfiguration webConfig;

    private FlowHandler flowHandler;

    private SearchExpressionHandler searchExpressionHandler;

    private Map<String, String> definingDocumentIdsToTruncatedJarUrls;

    private long timeOfInstantiation;

    public ApplicationAssociate(ApplicationImpl appImpl) {
        app = appImpl;

        propertyEditorHelper = new PropertyEditorHelper(appImpl);

        FacesContext ctx = FacesContext.getCurrentInstance();
        if (ctx == null) {
            throw new IllegalStateException("ApplicationAssociate ctor not called in same callstack as ConfigureListener.contextInitialized()");
        }
        ExternalContext externalContext = ctx.getExternalContext();
        if (externalContext.getApplicationMap().get(ASSOCIATE_KEY) != null) {
            throw new IllegalStateException(getExceptionMessageString(APPLICATION_ASSOCIATE_EXISTS_ID));
        }
        Map<String, Object> appMap = externalContext.getApplicationMap();
        appMap.put(ASSOCIATE_KEY, this);

        navigationMap = new ConcurrentHashMap<>();
        injectionProvider = (InjectionProvider) ctx.getAttributes().get(ConfigManager.INJECTION_PROVIDER_KEY);
        webConfig = WebConfiguration.getInstance(externalContext);
        beanManager = new BeanManager(injectionProvider, webConfig.isOptionEnabled(EnableLazyBeanValidation));
        // install the bean manager as a system event listener for custom
        // scopes being destoryed.
        app.subscribeToEvent(PreDestroyCustomScopeEvent.class, ScopeContext.class, beanManager);
        annotationManager = new AnnotationManager();

        devModeEnabled = appImpl.getProjectStage() == Development;

        if (!devModeEnabled) {
            resourceCache = new ResourceCache();
        }

        resourceManager = new ResourceManager(appMap, resourceCache);
        namedEventManager = new NamedEventManager();
        applicationStateInfo = new ApplicationStateInfo();

        appImpl.subscribeToEvent(PostConstructApplicationEvent.class, Application.class, new PostConstructApplicationListener());

        definingDocumentIdsToTruncatedJarUrls = new ConcurrentHashMap<>();
        timeOfInstantiation = System.currentTimeMillis();
    }

    public Application getApplication() {
        return app;
    }

    private Map<String, List<String>> resourceLibraryContracts;

    public void setResourceLibraryContracts(HashMap<String, List<String>> map) {
        this.resourceLibraryContracts = map;
    }

    private class PostConstructApplicationListener implements SystemEventListener {

        @Override
        public boolean isListenerForSource(Object source) {
            return source instanceof Application;
        }

        @Override
        public void processEvent(SystemEvent event) throws AbortProcessingException {
            ApplicationAssociate.this.initializeFacelets();

            if (ApplicationAssociate.this.flowHandler == null) {
                FlowHandlerFactory flowHandlerFactory = (FlowHandlerFactory) FactoryFinder.getFactory(FLOW_HANDLER_FACTORY);
                ApplicationAssociate.this.flowHandler = flowHandlerFactory.createFlowHandler(FacesContext.getCurrentInstance());
            }

            if (ApplicationAssociate.this.searchExpressionHandler == null) {
                ApplicationAssociate.this.searchExpressionHandler = new SearchExpressionHandlerImpl();
            }

            FacesContext context = FacesContext.getCurrentInstance();
            if (isCdiAvailable(context)) {
                try {
                    new JavaFlowLoaderHelper().loadFlows(context, ApplicationAssociate.this.flowHandler);
                } catch (IOException ex) {
                    LOGGER.log(SEVERE, null, ex);
                }
            }

            // cause the Facelet VDL to be instantiated eagerly, so it can
            // become aware of the resource library contracts

            ViewHandler viewHandler = context.getApplication().getViewHandler();

            // FindBugs: ignore the return value, this is just to get the
            // ctor called at this time.
            viewHandler.getViewDeclarationLanguage(context, FACES_PREFIX + "xhtml");

            String facesConfigVersion = getFacesConfigXmlVersion(context);
            context.getExternalContext().getApplicationMap().put(FACES_CONFIG_VERSION, facesConfigVersion);
        }

    }

    public void initializeFacelets() {
        if (compiler != null) {
            return;
        }

        FacesContext ctx = FacesContext.getCurrentInstance();

        if (!webConfig.isOptionEnabled(DisableFaceletJSFViewHandler) && !webConfig.isOptionEnabled(DisableFaceletJSFViewHandlerDeprecated)) {
            Map<String, Object> appMap = ctx.getExternalContext().getApplicationMap();
            compiler = createCompiler(appMap, webConfig);
            faceletFactory = createFaceletFactory(ctx, compiler, webConfig);
        }

    }

    public static ApplicationAssociate getInstance(ExternalContext externalContext) {
        if (externalContext == null) {
            return null;
        }

        return (ApplicationAssociate) externalContext.getApplicationMap().get(ASSOCIATE_KEY);
    }

    public long getTimeOfInstantiation() {
        return timeOfInstantiation;
    }

    public static ApplicationAssociate getInstance(ServletContext context) {
        if (context == null) {
            return null;
        }

        return (ApplicationAssociate) context.getAttribute(ASSOCIATE_KEY);
    }

    public static void setCurrentInstance(ApplicationAssociate associate) {
        if (associate == null) {
            instance.remove();
        } else {
            instance.set(associate);
        }
    }

    public static ApplicationAssociate getCurrentInstance() {

        ApplicationAssociate associate = instance.get();
        if (associate == null) {
            // Fallback to ExternalContext lookup
            FacesContext facesContext = FacesContext.getCurrentInstance();
            if (facesContext != null) {
                ExternalContext extContext = facesContext.getExternalContext();
                if (extContext != null) {
                    return ApplicationAssociate.getInstance(extContext);
                }
            }
        }

        return associate;
    }

    public ApplicationStateInfo getApplicationStateInfo() {
        return applicationStateInfo;
    }

    public ResourceManager getResourceManager() {
        return resourceManager;
    }

    // Return the resource library contracts and mappings from the
    // application configuration resources
    public Map<String, List<String>> getResourceLibraryContracts() {
        return resourceLibraryContracts;
    }

    public void setResourceManager(ResourceManager resourceManager) {
        this.resourceManager = resourceManager;
    }

    public ResourceCache getResourceCache() {
        return resourceCache;
    }

    public AnnotationManager getAnnotationManager() {
        return annotationManager;
    }

    public Compiler getCompiler() {
        if (compiler == null) {
            initializeFacelets();
        }

        return compiler;
    }

    public boolean isErrorPagePresent() {
        return errorPagePresent;
    }

    public void setErrorPagePresent(boolean errorPagePresent) {
        this.errorPagePresent = errorPagePresent;
    }

    public DefaultFaceletFactory getFaceletFactory() {
        return faceletFactory;
    }

    public static void clearInstance(ExternalContext externalContext) {
        Map<String, Object> applicationMap = externalContext.getApplicationMap();
        ApplicationAssociate me = (ApplicationAssociate) applicationMap.get(ASSOCIATE_KEY);

        if (me != null && me.resourceBundles != null) {
            me.resourceBundles.clear();
        }

        applicationMap.remove(ASSOCIATE_KEY);
    }

    public static void clearInstance(ServletContext servletContext) {
        ApplicationAssociate me = (ApplicationAssociate) servletContext.getAttribute(ASSOCIATE_KEY);

        if (me != null && me.resourceBundles != null) {
            me.resourceBundles.clear();
        }

        servletContext.removeAttribute(ASSOCIATE_KEY);
    }

    public BeanManager getBeanManager() {
        return beanManager;
    }

    public void initializeELResolverChains() {
        // 1. initialize the chains with default values
        if (app.compositeELResolver == null) {
            app.compositeELResolver = new DemuxCompositeELResolver(Faces);
            buildFacesResolver(app.compositeELResolver, this);
            populateFacesELResolverForJsp(app, this);
        }
    }

    public void installProgrammaticallyAddedResolvers() {
        // Ensure custom resolvers are inserted at the correct place.
        VariableResolver variableResolver = this.getLegacyVariableResolver();
        if (variableResolver != null) {
            getLegacyVRChainHeadWrapperForJsp().setWrapped(variableResolver);
            getLegacyVRChainHeadWrapperForFaces().setWrapped(variableResolver);
        }
    }

    public boolean isDevModeEnabled() {
        return devModeEnabled;
    }

    /**
     * Obtain the PropertyEditorHelper instance for this app.
     *
     * @return The PropertyEditorHeler instance for this app.
     */
    public PropertyEditorHelper getPropertyEditorHelper() {
        return propertyEditorHelper;
    }

    /**
     * This method is called by <code>ConfigureListener</code> and will contain any
     * <code>VariableResolvers</code> defined within faces-config configuration files.
     *
     * @param resolver VariableResolver
     */
    @SuppressWarnings("deprecation")
    public void setLegacyVRChainHead(VariableResolver resolver) {
        this.legacyVRChainHead = resolver;
    }

    @SuppressWarnings("deprecation")
    public VariableResolver getLegacyVRChainHead() {
        return legacyVRChainHead;
    }

    public VariableResolverChainWrapper getLegacyVRChainHeadWrapperForJsp() {
        return legacyVRChainHeadWrapperForJsp;
    }

    public void setLegacyVRChainHeadWrapperForJsp(VariableResolverChainWrapper legacyVRChainHeadWrapper) {
        this.legacyVRChainHeadWrapperForJsp = legacyVRChainHeadWrapper;
    }

    public VariableResolverChainWrapper getLegacyVRChainHeadWrapperForFaces() {
        return legacyVRChainHeadWrapperForFaces;
    }

    public void setLegacyVRChainHeadWrapperForFaces(VariableResolverChainWrapper legacyVRChainHeadWrapperForFaces) {
        this.legacyVRChainHeadWrapperForFaces = legacyVRChainHeadWrapperForFaces;
    }

    /**
     * This method is called by <code>ConfigureListener</code> and will contain any
     * <code>PropertyResolvers</code> defined within faces-config configuration files.
     *
     * @param resolver PropertyResolver
     */
    @SuppressWarnings("deprecation")
    public void setLegacyPRChainHead(PropertyResolver resolver) {
        this.legacyPRChainHead = resolver;
    }

    @SuppressWarnings("deprecation")
    public PropertyResolver getLegacyPRChainHead() {
        return legacyPRChainHead;
    }

    public FacesCompositeELResolver getFacesELResolverForJsp() {
        return facesELResolverForJsp;
    }

    public FlowHandler getFlowHandler() {
        return flowHandler;
    }

    public void setFlowHandler(FlowHandler flowHandler) {
        this.flowHandler = flowHandler;
    }

    public SearchExpressionHandler getSearchExpressionHandler() {
        return searchExpressionHandler;
    }

    public void setSearchExpressionHandler(SearchExpressionHandler searchExpressionHandler) {
        this.searchExpressionHandler = searchExpressionHandler;
    }

    public void setFacesELResolverForJsp(FacesCompositeELResolver celr) {
        facesELResolverForJsp = celr;
    }

    public void setELResolversFromFacesConfig(List<ELResolver> resolvers) {
        this.elResolversFromFacesConfig = resolvers;
    }

    public List<ELResolver> getELResolversFromFacesConfig() {
        return elResolversFromFacesConfig;
    }

    public void setSearchKeywordResolversFromFacesConfig(List<SearchKeywordResolver> searchKeywordResolversFromFacesConfig) {
        this.searchKeywordResolversFromFacesConfig = searchKeywordResolversFromFacesConfig;
    }

    public List<SearchKeywordResolver> getSearchKeywordResolversFromFacesConfig() {
        return searchKeywordResolversFromFacesConfig;
    }

    public void setExpressionFactory(ExpressionFactory expressionFactory) {
        this.expressionFactory = expressionFactory;
    }

    public ExpressionFactory getExpressionFactory() {
        return this.expressionFactory;
    }

    public CompositeELResolver getApplicationELResolvers() {
        return app.getApplicationELResolvers();
    }

    public InjectionProvider getInjectionProvider() {
        return injectionProvider;
    }

    public void setContextName(String contextName) {
        this.contextName = contextName;
    }

    public String getContextName() {
        return contextName;
    }

    /**
     * Maintains the PropertyResolver called through Application.setPropertyResolver()
     * 
     * @param resolver PropertyResolver
     */
    @SuppressWarnings("deprecation")
    public void setLegacyPropertyResolver(PropertyResolver resolver) {
        this.legacyPropertyResolver = resolver;
    }

    /**
     * @return the PropertyResolver called through Application.getPropertyResolver()
     */
    @SuppressWarnings("deprecation")
    public PropertyResolver getLegacyPropertyResolver() {
        return legacyPropertyResolver;
    }

    /**
     * Maintains the PropertyResolver called through Application.setVariableResolver()
     * 
     * @param resolver VariableResolver
     */
    @SuppressWarnings("deprecation")
    public void setLegacyVariableResolver(VariableResolver resolver) {
        this.legacyVariableResolver = resolver;
    }

    /**
     * @return the VariableResolver called through Application.getVariableResolver()
     */
    @SuppressWarnings("deprecation")
    public VariableResolver getLegacyVariableResolver() {
        return legacyVariableResolver;
    }

    /**
     * Called by application code to indicate we've processed the first request to the application.
     */
    public void setRequestServiced() {
        this.requestServiced = true;
    }

    /**
     * @return <code>true</code> if we've processed a request, otherwise <code>false</code>
     */
    public boolean hasRequestBeenServiced() {
        return requestServiced;
    }

    public void addFacesComponent(FacesComponentUsage facesComponentUsage) {
        if (facesComponentsByNamespace == null) {
            facesComponentsByNamespace = new HashMap<>();
        }

        facesComponentsByNamespace.computeIfAbsent(facesComponentUsage.getAnnotation().namespace(), k -> new ArrayList<>()).add(facesComponentUsage);
    }

    public List<FacesComponentUsage> getComponentsForNamespace(String namespace) {
        if (facesComponentsByNamespace != null && facesComponentsByNamespace.containsKey(namespace)) {
            return facesComponentsByNamespace.get(namespace);
        }

        return emptyList();
    }

    /**
     * Add a navigation case to the internal case set. If a case set does not already exist in the
     * case list map containing this case (identified by <code>from-view-id</code>), start a new
     * list, add the case to it, and store the set in the case set map. If a case set already
     * exists, overwrite the previous case.
     *
     * @param navigationCase the navigation case containing navigation mapping information from the
     *            configuration file.
     */
    public void addNavigationCase(NavigationCase navigationCase) {

        // If there already is a case existing for the fromviewid/fromaction.fromoutcome
        // combination,
        // replace it ... (last one wins).
        navigationMap.computeIfAbsent(navigationCase.getFromViewId(), k -> new LinkedHashSet<>()).add(navigationCase);
    }

    public NamedEventManager getNamedEventManager() {
        return namedEventManager;
    }

    /**
     * Return a <code>Map</code> of navigation mappings loaded from the configuration system. The
     * key for the returned <code>Map</code> is <code>from-view-id</code>, and the value is a
     * <code>List</code> of navigation cases.
     *
     * @return Map the map of navigation mappings.
     */
    public Map<String, Set<NavigationCase>> getNavigationCaseListMappings() {
        if (navigationMap == null) {
            return emptyMap();
        }

        return navigationMap;
    }

    public ResourceBundle getResourceBundle(FacesContext context, String var) {
        ApplicationResourceBundle bundle = resourceBundles.get(var);

        if (bundle == null) {
            return null;
        }

        // Start out with the default locale
        Locale defaultLocale = Locale.getDefault();
        Locale locale = defaultLocale;

        // See if this FacesContext has a ViewRoot
        UIViewRoot root = context.getViewRoot();
        if (root != null) {
            locale = root.getLocale();
            if (locale == null) {
                // If the ViewRoot has no Locale, fall back to the default.
                locale = defaultLocale;
            }
        }

        return bundle.getResourceBundle(locale);
    }

    /**
     * keys: <var> element from faces-config
     * <p>
     * <p/>
     * values: ResourceBundleBean instances.
     */

    Map<String, ApplicationResourceBundle> resourceBundles = new HashMap<>();

    public void addResourceBundle(String var, ApplicationResourceBundle bundle) {
        resourceBundles.put(var, bundle);
    }

    public Map<String, ApplicationResourceBundle> getResourceBundles() {
        return resourceBundles;
    }

    // This is called by ViewHandlerImpl.renderView().
    public void responseRendered() {
        responseRendered = true;
    }

    public boolean isResponseRendered() {
        return responseRendered;
    }

    public boolean urlIsRelatedToDefiningDocumentInJar(URL candidateUrl, String definingDocumentId) {
        boolean result = false;
        String match = definingDocumentIdsToTruncatedJarUrls.get(definingDocumentId);
        if (match != null) {
            String candidate = candidateUrl.toExternalForm();
            if (candidate != null) {
                int i = candidate.lastIndexOf("/META-INF");
                if (i == -1) {
                    throw new FacesException("Invalid url for application configuration resources file with respect to faces flows");
                }
                candidate = candidate.substring(0, i);
                result = candidate.equals(match);
            }
        }

        return result;
    }

    public void relateUrlToDefiningDocumentInJar(URL url, String definingDocumentId) {
        String candidate = url.toExternalForm();
        int i = candidate.lastIndexOf("/META-INF");
        if (i == -1) {
            return;
        }
        candidate = candidate.substring(0, i);

        definingDocumentIdsToTruncatedJarUrls.put(definingDocumentId, candidate);
    }

    protected DefaultFaceletFactory createFaceletFactory(FacesContext ctx, Compiler compiler, WebConfiguration webConfig) {

        // refresh period
        boolean isProduction = app.getProjectStage() == Production;
        String refreshPeriod;
        if (webConfig.isSet(FaceletsDefaultRefreshPeriod) || webConfig.isSet(FaceletsDefaultRefreshPeriodDeprecated)) {
            refreshPeriod = webConfig.getOptionValue(FaceletsDefaultRefreshPeriod);
        } else if (isProduction) {
            refreshPeriod = "-1";
        } else {
            refreshPeriod = FaceletsDefaultRefreshPeriod.getDefaultValue();
        }

        long period = parseLong(refreshPeriod);

        // resource resolver
        ResourceResolver defaultResourceResolver = new DefaultResourceResolver(app.getResourceHandler());
        ResourceResolver resolver = defaultResourceResolver;

        String resolverName = webConfig.getOptionValue(FaceletsResourceResolver);
        if (resolverName != null && resolverName.length() > 0) {
            resolver = (ResourceResolver) ReflectionUtil.decorateInstance(resolverName, ResourceResolver.class, resolver);
        } else {

            Set<? extends Class<?>> resourceResolvers = getAnnotatedClasses(ctx).get(FaceletsResourceResolver.class);
            if ((null != resourceResolvers) && !resourceResolvers.isEmpty()) {
                Class<?> resolverClass = resourceResolvers.iterator().next();
                if (resourceResolvers.size() > 1 && LOGGER.isLoggable(SEVERE)) {
                    LOGGER.log(SEVERE, "Found more than one class " + "annotated with FaceletsResourceResolver.  Will " + "use {0} and ignore the others",
                            resolverClass);
                }
                resolver = (ResourceResolver) decorateInstance(resolverClass, ResourceResolver.class, resolver);
            }
        }

        // If our resourceResolver is not the one we created above
        // and the use of this ResousrecResolver for Composite Components
        // is acceptable.
        if (!(resolver == defaultResourceResolver) && webConfig.isOptionEnabled(EnableFaceletsResourceResolverResolveCompositeComponents)) {
            ctx.getExternalContext().getApplicationMap().put(DefaultResourceResolver.NON_DEFAULT_RESOURCE_RESOLVER_PARAM_NAME, resolver);
        }

        FaceletCache cache = null;
        String faceletCacheName = webConfig.getOptionValue(FaceletCache);
        if (faceletCacheName != null && faceletCacheName.length() > 0) {
            try {
                com.sun.faces.facelets.FaceletCache privateApiCache = (com.sun.faces.facelets.FaceletCache) ReflectionUtil.forName(faceletCacheName)
                        .newInstance();
                cache = new PrivateApiFaceletCacheAdapter(privateApiCache);
            } catch (ClassCastException e) {
                if (LOGGER.isLoggable(INFO)) {
                    LOGGER.log(INFO, "Please remove context-param when using javax.faces.view.facelets.FaceletCache class with name:" + faceletCacheName
                            + "and use the new FaceletCacheFactory API", e);
                }
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                if (LOGGER.isLoggable(SEVERE)) {
                    LOGGER.log(SEVERE, "Error Loading Facelet cache: " + faceletCacheName, e);
                }
            }
        }

        if (cache == null) {
            FaceletCacheFactory cacheFactory = (FaceletCacheFactory) FactoryFinder.getFactory(FACELET_CACHE_FACTORY);
            cache = cacheFactory.getFaceletCache();
        }

        DefaultFaceletFactory toReturn = new DefaultFaceletFactory();
        toReturn.init(compiler, resolver, period, cache);

        return toReturn;
    }

    protected Compiler createCompiler(Map<String, Object> appMap, WebConfiguration webConfig) {

        Compiler newCompiler = new SAXCompiler();

        loadDecorators(appMap, newCompiler);

        // Skip params?
        newCompiler.setTrimmingComments(webConfig.isOptionEnabled(FaceletsSkipComments));

        addTagLibraries(newCompiler);

        return newCompiler;
    }

    protected void loadDecorators(Map<String, Object> appMap, Compiler newCompiler) {
        String decoratorsParamValue = webConfig.getOptionValue(FaceletsDecorators);

        if (decoratorsParamValue != null) {
            for (String decorator : split(appMap, decoratorsParamValue.trim(), ";")) {
                try {
                    newCompiler.addTagDecorator((TagDecorator) forName(decorator).newInstance());

                    if (LOGGER.isLoggable(FINE)) {
                        LOGGER.log(FINE, "Successfully Loaded Decorator: {0}", decorator);
                    }
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                    if (LOGGER.isLoggable(SEVERE)) {
                        LOGGER.log(SEVERE, "Error Loading Decorator: " + decorator, e);
                    }
                }
            }
        }
    }

    protected void addTagLibraries(Compiler newCompiler) {
        newCompiler.addTagLibrary(new CoreLibrary());
        newCompiler.addTagLibrary(new CoreLibrary(CoreLibrary.XMLNSNamespace));

        newCompiler.addTagLibrary(new HtmlLibrary());
        newCompiler.addTagLibrary(new HtmlLibrary(HtmlLibrary.XMLNSNamespace));

        newCompiler.addTagLibrary(new UILibrary());
        newCompiler.addTagLibrary(new UILibrary(UILibrary.XMLNSNamespace));

        newCompiler.addTagLibrary(new JstlCoreLibrary());
        newCompiler.addTagLibrary(new JstlCoreLibrary(JstlCoreLibrary.IncorrectNamespace));
        newCompiler.addTagLibrary(new JstlCoreLibrary(JstlCoreLibrary.XMLNSNamespace));

        newCompiler.addTagLibrary(new PassThroughAttributeLibrary());
        newCompiler.addTagLibrary(new PassThroughElementLibrary());

        newCompiler.addTagLibrary(new FunctionLibrary(JstlFunction.class, FunctionLibrary.Namespace));
        newCompiler.addTagLibrary(new FunctionLibrary(JstlFunction.class, FunctionLibrary.XMLNSNamespace));
        if (isDevModeEnabled()) {
            newCompiler.addTagLibrary(new FunctionLibrary(DevTools.class, DevTools.Namespace));
            newCompiler.addTagLibrary(new FunctionLibrary(DevTools.class, DevTools.NewNamespace));
        }

        newCompiler.addTagLibrary(new CompositeLibrary());
        newCompiler.addTagLibrary(new CompositeLibrary(CompositeLibrary.XMLNSNamespace));
    }

}

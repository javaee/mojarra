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
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
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

import com.sun.faces.RIConstants;
import com.sun.faces.scripting.groovy.GroovyHelper;
import com.sun.faces.application.resource.ResourceCache;
import com.sun.faces.application.resource.ResourceManager;
import com.sun.faces.application.annotation.AnnotationManager;
import com.sun.faces.application.annotation.FacesComponentUsage;
import com.sun.faces.config.ConfigManager;
import com.sun.faces.config.WebConfiguration;
import com.sun.faces.facelets.compiler.Compiler;
import com.sun.faces.facelets.compiler.SAXCompiler;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.SystemEvent;
import javax.faces.view.facelets.TagDecorator;
import com.sun.faces.facelets.tag.composite.CompositeLibrary;
import com.sun.faces.facelets.tag.jstl.core.JstlCoreLibrary;
import com.sun.faces.facelets.tag.jstl.fn.JstlFunction;
import com.sun.faces.facelets.tag.ui.UILibrary;
import com.sun.faces.facelets.tag.jsf.core.CoreLibrary;
import com.sun.faces.facelets.tag.jsf.html.HtmlLibrary;
import com.sun.faces.facelets.util.ReflectionUtil;
import com.sun.faces.facelets.util.FunctionLibrary;
import com.sun.faces.facelets.util.DevTools;
import javax.faces.view.facelets.ResourceResolver;
import javax.faces.view.facelets.FaceletCache;
import com.sun.faces.facelets.impl.DefaultResourceResolver;
import com.sun.faces.facelets.impl.DefaultFaceletFactory;
import com.sun.faces.mgbean.BeanManager;
import com.sun.faces.spi.InjectionProvider;
import com.sun.faces.util.MessageUtils;
import com.sun.faces.util.Util;
import com.sun.faces.util.FacesLogger;
import static com.sun.faces.config.WebConfiguration.BooleanWebContextInitParameter;
import static com.sun.faces.config.WebConfiguration.WebContextInitParameter.*;
import static com.sun.faces.config.WebConfiguration.BooleanWebContextInitParameter.DisableFaceletJSFViewHandler;
import static com.sun.faces.config.WebConfiguration.BooleanWebContextInitParameter.EnableLazyBeanValidation;
import static com.sun.faces.config.WebConfiguration.BooleanWebContextInitParameter.EnableFaceletsResourceResolverResolveCompositeComponents;
import com.sun.faces.el.DemuxCompositeELResolver;
import com.sun.faces.el.ELUtils;
import com.sun.faces.el.FacesCompositeELResolver;
import com.sun.faces.el.VariableResolverChainWrapper;
import com.sun.faces.facelets.PrivateApiFaceletCacheAdapter;
import com.sun.faces.facelets.tag.jsf.PassThroughAttributeLibrary;
import com.sun.faces.facelets.tag.jsf.PassThroughElementLibrary;
import com.sun.faces.lifecycle.ELResolverInitPhaseListener;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import javax.el.CompositeELResolver;
import javax.el.ELResolver;
import javax.el.ExpressionFactory;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.el.PropertyResolver;
import javax.faces.el.VariableResolver;
import javax.faces.application.ProjectStage;
import javax.faces.event.PreDestroyCustomScopeEvent;
import javax.faces.event.ScopeContext;
import javax.servlet.ServletContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.LinkedHashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.concurrent.ConcurrentHashMap;
import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.application.NavigationCase;
import javax.faces.application.ViewHandler;
import javax.faces.component.FacesComponent;
import javax.faces.event.PostConstructApplicationEvent;
import javax.faces.event.SystemEventListener;
import javax.faces.flow.FlowHandler;
import javax.faces.flow.FlowHandlerFactory;
import javax.faces.view.facelets.FaceletCacheFactory;
import javax.faces.view.facelets.FaceletsResourceResolver;

/**
 * <p>Break out the things that are associated with the Application, but
 * need to be present even when the user has replaced the Application
 * instance.</p>
 * <p/>
 * <p>For example: the user replaces ApplicationFactory, and wants to
 * intercept calls to createValueExpression() and createMethodExpression() for
 * certain kinds of expressions, but allow the existing application to
 * handle the rest.</p>
 */

public class ApplicationAssociate {

    private static final Logger LOGGER = FacesLogger.APPLICATION.getLogger();

    private ApplicationImpl app = null;

    /**
     * Overall Map containing <code>from-view-id</code> key and
     * <code>Set</code> of <code>NavigationCase</code>
     * objects for that key; The <code>from-view-id</code> strings in
     * this map will be stored as specified in the configuration file -
     * some of them will have a trailing asterisk "*" signifying wild
     * card, and some may be specified as an asterisk "*".
     */
    private Map<String, Set<NavigationCase>> navigationMap = null;
    
    /*
     * The FacesComponentTagLibrary uses the information in this map 
     * to help it fabricate tag handlers for components annotated with
     * FacesComponent.
     * Key: namespace
     */
    private Map<String, List<FacesComponentUsage>> facesComponentsByNamespace = null;

    // Flag indicating that a response has been rendered.
    private boolean responseRendered = false;

    private static final String ASSOCIATE_KEY = RIConstants.FACES_PREFIX +
         "ApplicationAssociate";

    private static ThreadLocal<ApplicationAssociate> instance =
        new ThreadLocal<ApplicationAssociate>() {
            protected ApplicationAssociate initialValue() {
                return (null);
            }
        };

    private List<ELResolver> elResolversFromFacesConfig = null;

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
    private GroovyHelper groovyHelper;
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
    
    private Map<String, String> definingDocumentIdsToTruncatedJarUrls;
    
    private long timeOfInstantiation;

    public ApplicationAssociate(ApplicationImpl appImpl) {
        app = appImpl;

        propertyEditorHelper = new PropertyEditorHelper(appImpl);

        FacesContext ctx = FacesContext.getCurrentInstance();
        if (ctx == null) {
            throw new IllegalStateException(
                    "ApplicationAssociate ctor not called in same callstack as ConfigureListener.contextInitialized()");
        }
        ExternalContext externalContext = ctx.getExternalContext();
        if (null != externalContext.getApplicationMap().get(ASSOCIATE_KEY)) {
            throw new IllegalStateException(
                 MessageUtils.getExceptionMessageString(
                      MessageUtils.APPLICATION_ASSOCIATE_EXISTS_ID));
        }
        Map<String, Object> appMap = externalContext.getApplicationMap();
        appMap.put(ASSOCIATE_KEY, this);
        //noinspection CollectionWithoutInitialCapacity
        navigationMap = new ConcurrentHashMap<String, Set<NavigationCase>>();
        injectionProvider = (InjectionProvider) ctx.getAttributes().get(ConfigManager.INJECTION_PROVIDER_KEY);
        webConfig = WebConfiguration.getInstance(externalContext);
        beanManager = new BeanManager(injectionProvider,
                                      webConfig.isOptionEnabled(
                                           EnableLazyBeanValidation));
        // install the bean manager as a system event listener for custom
        // scopes being destoryed.
        app.subscribeToEvent(PreDestroyCustomScopeEvent.class,
                             ScopeContext.class,
                             beanManager);
        annotationManager = new AnnotationManager();

        groovyHelper = GroovyHelper.getCurrentInstance();

        devModeEnabled = (appImpl.getProjectStage() == ProjectStage.Development);

        if (!devModeEnabled) {
            resourceCache = new ResourceCache();
        }

        resourceManager = new ResourceManager(appMap, resourceCache);
        namedEventManager = new NamedEventManager();
        applicationStateInfo = new ApplicationStateInfo();
        
        appImpl.subscribeToEvent(PostConstructApplicationEvent.class,
                         Application.class, new PostConstructApplicationListener());
        
        definingDocumentIdsToTruncatedJarUrls = new ConcurrentHashMap<String, String>();
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
            
            if (null == ApplicationAssociate.this.flowHandler) {
                FlowHandlerFactory flowHandlerFactory = (FlowHandlerFactory) FactoryFinder.getFactory(FactoryFinder.FLOW_HANDLER_FACTORY);
                ApplicationAssociate.this.flowHandler = flowHandlerFactory.createFlowHandler(FacesContext.getCurrentInstance());
            }

            FacesContext context = FacesContext.getCurrentInstance();
            if (Util.isCDIAvailable(context.getExternalContext().getApplicationMap())) {
                try {
                    JavaFlowLoaderHelper flowLoader = new JavaFlowLoaderHelper();
                    flowLoader.loadFlows(context, ApplicationAssociate.this.flowHandler);
                } catch (IOException ex) {
                    LOGGER.log(Level.SEVERE, null, ex);
                }
            }
            // cause the Facelet VDL to be instantiated eagerly, so it can 
            // become aware of the resource library contracts
            
            ViewHandler viewHandler = context.getApplication().getViewHandler();

            // FindBugs: ignore the return value, this is just to get the 
            // ctor called at this time.
            viewHandler.getViewDeclarationLanguage(context, 
                    RIConstants.FACES_PREFIX + "xhtml");

        }
        
    }
    
    public void initializeFacelets() {
        if (null != compiler) {
            return;
        }
        
        FacesContext ctx = FacesContext.getCurrentInstance();
        
        if (!webConfig.isOptionEnabled(DisableFaceletJSFViewHandler)) {
            Map<String, Object> appMap = ctx.getExternalContext().getApplicationMap();
            compiler = createCompiler(appMap, webConfig);
            faceletFactory = createFaceletFactory(ctx, compiler, webConfig);
        }
        
    }

    public static ApplicationAssociate getInstance(ExternalContext
         externalContext) {
        if (externalContext == null) {
            return null;
        }
        Map applicationMap = externalContext.getApplicationMap();
        return ((ApplicationAssociate)
             applicationMap.get(ASSOCIATE_KEY));
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
            FacesContext fc = FacesContext.getCurrentInstance();
            if (fc != null) {
                ExternalContext extContext = fc.getExternalContext();
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
        if (null == compiler) {
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

    public static void clearInstance(ExternalContext
         externalContext) {
        Map applicationMap = externalContext.getApplicationMap();
        ApplicationAssociate me = (ApplicationAssociate) applicationMap.get(ASSOCIATE_KEY);
        if (null != me && null != me.resourceBundles) {
            me.resourceBundles.clear();
        }
        applicationMap.remove(ASSOCIATE_KEY);
    }

    public static void clearInstance(ServletContext sc) {
        ApplicationAssociate me = (ApplicationAssociate) sc.getAttribute(ASSOCIATE_KEY);
        if (null != me && null != me.resourceBundles) {
            me.resourceBundles.clear();
        }
        sc.removeAttribute(ASSOCIATE_KEY);    
    }


    public BeanManager getBeanManager() {
        return beanManager;
    }

    public GroovyHelper getGroovyHelper() {
        return groovyHelper;
    }

    public void initializeELResolverChains() {
        // 1. initialize the chains with default values
        if (null == app.compositeELResolver) {
            app.compositeELResolver =
                    new DemuxCompositeELResolver(
                    FacesCompositeELResolver.ELResolverChainType.Faces);
            ELUtils.buildFacesResolver(app.compositeELResolver, this);
            ELResolverInitPhaseListener.populateFacesELResolverForJsp(app,
                    this);
        }
    }

    public void installProgrammaticallyAddedResolvers() {
        // Ensure custom resolvers are inserted at the correct place.
        VariableResolver vr = this.getLegacyVariableResolver();
        if (null != vr) {
            assert(null != this.getLegacyVRChainHeadWrapperForJsp());
            this.getLegacyVRChainHeadWrapperForJsp().setWrapped(vr);
            assert(null != this.getLegacyVRChainHeadWrapperForFaces());
            this.getLegacyVRChainHeadWrapperForFaces().setWrapped(vr);
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
     * This method is called by <code>ConfigureListener</code> and will
     * contain any <code>VariableResolvers</code> defined within
     * faces-config configuration files.
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
     * This method is called by <code>ConfigureListener</code> and will
     * contain any <code>PropertyResolvers</code> defined within
     * faces-config configuration files.
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

    public void setFacesELResolverForJsp(FacesCompositeELResolver celr) {
        facesELResolverForJsp = celr;
    }

    public void setELResolversFromFacesConfig(List<ELResolver> resolvers) {
        this.elResolversFromFacesConfig = resolvers;
    }

    public List<ELResolver> getELResolversFromFacesConfig() {
        return elResolversFromFacesConfig;
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
     * Maintains the PropertyResolver called through
     * Application.setPropertyResolver()
     * @param resolver PropertyResolver
     */
    @SuppressWarnings("deprecation")
    public void setLegacyPropertyResolver(PropertyResolver resolver) {
        this.legacyPropertyResolver = resolver;
    }

    /**
     * @return the PropertyResolver called through
     * Application.getPropertyResolver()
     */
    @SuppressWarnings("deprecation")
    public PropertyResolver getLegacyPropertyResolver() {
        return legacyPropertyResolver;
    }

    /**
     * Maintains the PropertyResolver called through
     * Application.setVariableResolver()
     * @param resolver VariableResolver
     */
    @SuppressWarnings("deprecation")
    public void setLegacyVariableResolver(VariableResolver resolver) {
        this.legacyVariableResolver = resolver;
    }

    /**
     * @return the VariableResolver called through
     * Application.getVariableResolver()
     */
    @SuppressWarnings("deprecation")
    public VariableResolver getLegacyVariableResolver() {
        return legacyVariableResolver;
    }


    /**
     * Called by application code to indicate we've processed the
     * first request to the application.
     */
    public void setRequestServiced() {
        this.requestServiced = true;
    }

    /**
     * @return <code>true</code> if we've processed a request, otherwise
     *         <code>false</code>
     */
    public boolean hasRequestBeenServiced() {
        return requestServiced;
    }
    
    public void addFacesComponent(FacesComponentUsage facesComponentUsage) {
        FacesComponent facesComponent = facesComponentUsage.getAnnotation();
        assert(facesComponent.createTag());
        if (null == facesComponentsByNamespace) {
            facesComponentsByNamespace = new HashMap<String, List<FacesComponentUsage>>();
        }

        List<FacesComponentUsage> componentsInNamespace = null;
        final String namespace = facesComponent.namespace();
        
        if (!facesComponentsByNamespace.containsKey(facesComponent.namespace())) {
            componentsInNamespace = new ArrayList<FacesComponentUsage>();
            facesComponentsByNamespace.put(namespace, componentsInNamespace);
        } else {
            componentsInNamespace = facesComponentsByNamespace.get(namespace);
        }
        componentsInNamespace.add(facesComponentUsage);

    }
    
    public List<FacesComponentUsage> getComponentsForNamespace(String ns) {
        List<FacesComponentUsage> result = Collections.emptyList();
        if (null != facesComponentsByNamespace && facesComponentsByNamespace.containsKey(ns)) {
            result = facesComponentsByNamespace.get(ns);
        }
       
        return result;
    }
    
    /**
     * Add a navigation case to the internal case set.  If a case set
     * does not already exist in the case list map containing this case
     * (identified by <code>from-view-id</code>), start a new list,
     * add the case to it, and store the set in the case set map.
     * If a case set already exists, overwrite the previous case.
     *
     * @param navigationCase the navigation case containing navigation
     *                       mapping information from the configuration file.
     */
    public void addNavigationCase(NavigationCase navigationCase) {

        String fromViewId = navigationCase.getFromViewId();
        Set<NavigationCase> caseSet = navigationMap.get(fromViewId);
        if (caseSet == null) {
            //noinspection CollectionWithoutInitialCapacity
            caseSet = new LinkedHashSet<NavigationCase>();
            caseSet.add(navigationCase);
            navigationMap.put(fromViewId, caseSet);
        } else {
            // if there already is a case existing for the
            // fromviewid/fromaction.fromoutcome combination,
            // replace it ...  (last one wins).
            caseSet.add(navigationCase);
        }

    }
    
    

    public NamedEventManager getNamedEventManager() {
        return namedEventManager;
    }


    /**
     * Return a <code>Map</code> of navigation mappings loaded from
     * the configuration system.  The key for the returned <code>Map</code>
     * is <code>from-view-id</code>, and the value is a <code>List</code>
     * of navigation cases.
     *
     * @return Map the map of navigation mappings.
     */
    public Map<String, Set<NavigationCase>> getNavigationCaseListMappings() {
        if (navigationMap == null) {
            return Collections.emptyMap();
        }
        return navigationMap;
    }


    public ResourceBundle getResourceBundle(FacesContext context,
                                            String var) {
        ApplicationResourceBundle bundle = resourceBundles.get(var);
        if (bundle == null) {
            return null;
        }
        UIViewRoot root;
        // Start out with the default locale
        Locale locale;
        Locale defaultLocale = Locale.getDefault();
        locale = defaultLocale;
        // See if this FacesContext has a ViewRoot
        root = context.getViewRoot();
        if (null != root) {
            locale = root.getLocale();
            if (null == root.getLocale()){
                // If the ViewRoot has no Locale, fall back to the default.
                locale = defaultLocale;
            }
        }
        assert (null != locale);
        //ResourceBundleBean bean = resourceBundles.get(var);
        return bundle.getResourceBundle(locale);

    }

    /**
     * keys: <var> element from faces-config<p>
     * <p/>
     * values: ResourceBundleBean instances.
     */

    @SuppressWarnings({"CollectionWithoutInitialCapacity"})
    Map<String, ApplicationResourceBundle> resourceBundles =
         new HashMap<String, ApplicationResourceBundle>();

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
        if (null != match) {
            String candidate = candidateUrl.toExternalForm();
            if (null != candidate && null != match) {
                int i = candidate.lastIndexOf("/META-INF");
                if (-1 == i) {
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
        if (-1 == i) {
            return;
        }
        candidate = candidate.substring(0, i);
        
        definingDocumentIdsToTruncatedJarUrls.put(definingDocumentId, candidate);
    }


    protected DefaultFaceletFactory createFaceletFactory(FacesContext ctx,
            Compiler c, WebConfiguration webConfig) {

        // refresh period
        boolean isProduction = app.getProjectStage() == ProjectStage.Production;
        String refreshPeriod;
        if (webConfig.isSet(FaceletsDefaultRefreshPeriod) || webConfig.isSet(FaceletsDefaultRefreshPeriodDeprecated)) {
            refreshPeriod = webConfig.getOptionValue(FaceletsDefaultRefreshPeriod);
        } else if (isProduction) {
            refreshPeriod = "-1";
        } else {
            refreshPeriod = FaceletsDefaultRefreshPeriod.getDefaultValue();
        }
        
        long period = Long.parseLong(refreshPeriod);

        // resource resolver
        ResourceResolver defaultResourceResolver = new DefaultResourceResolver(app.getResourceHandler());
        ResourceResolver resolver = defaultResourceResolver;

        String resolverName = webConfig.getOptionValue(FaceletsResourceResolver);
        if (resolverName != null && resolverName.length() > 0) {
            resolver = (ResourceResolver) 
                    ReflectionUtil.decorateInstance(resolverName,
                    ResourceResolver.class,
                    resolver);
        } else {
        
            Set<? extends Class> resourceResolvers = 
                    ConfigManager.getAnnotatedClasses(ctx).get(FaceletsResourceResolver.class);
            if ((null != resourceResolvers) && !resourceResolvers.isEmpty()) {
                Class resolverClass = resourceResolvers.iterator().next();
                if (1 < resourceResolvers.size() && LOGGER.isLoggable(Level.SEVERE)) {
                    LOGGER.log(Level.SEVERE, "Found more than one class " + 
                        "annotated with FaceletsResourceResolver.  Will " + 
                        "use {0} and ignore the others", resolverClass);
                }
                resolver = (ResourceResolver) 
                        ReflectionUtil.decorateInstance(resolverClass,
                        ResourceResolver.class,
                        resolver);
            } 
        }
        
        // If our resourceResolver is not the one we created above
        // and the use of this ResousrecResolver for Composite Components
        // is acceptable.
        if (!(resolver == defaultResourceResolver) && webConfig.isOptionEnabled(EnableFaceletsResourceResolverResolveCompositeComponents)) {
            ctx.getExternalContext().getApplicationMap().put(DefaultResourceResolver.NON_DEFAULT_RESOURCE_RESOLVER_PARAM_NAME,
                    resolver);
        }
        
        FaceletCache cache = null;
        String faceletCacheName = webConfig.getOptionValue(FaceletCache);
        if (faceletCacheName != null && faceletCacheName.length() > 0) {
            try {
                com.sun.faces.facelets.FaceletCache privateApiCache =
                        (com.sun.faces.facelets.FaceletCache)ReflectionUtil.forName(faceletCacheName)
                                          .newInstance();
                cache = new PrivateApiFaceletCacheAdapter(privateApiCache);
            } catch(ClassCastException e) {
                if (LOGGER.isLoggable(Level.INFO)) {
                    LOGGER.log(Level.INFO,
                            "Please remove context-param when using javax.faces.view.facelets.FaceletCache class with name:" 
                                    + faceletCacheName + "and use the new FaceletCacheFactory API", e);
                }
            } catch(Exception e) {
                if (LOGGER.isLoggable(Level.SEVERE)) {
                    LOGGER.log(Level.SEVERE,
                               "Error Loading Facelet cache: " + faceletCacheName,
                               e);
                }
            }
        }
        if (null == cache) {
            FaceletCacheFactory cacheFactory = (FaceletCacheFactory)
                    FactoryFinder.getFactory(FactoryFinder.FACELET_CACHE_FACTORY);
            cache = cacheFactory.getFaceletCache();
        }

        // Resource.getResourceUrl(ctx,"/")
        DefaultFaceletFactory toReturn = new DefaultFaceletFactory();
        toReturn.init(c, resolver, period, cache);
                
        return toReturn;

    }


    protected Compiler createCompiler(Map<String, Object> appMap, WebConfiguration webConfig) {

        Compiler c = new SAXCompiler();

        // load decorators
        String decParam = webConfig
              .getOptionValue(FaceletsDecorators);
        if (decParam != null) {
            decParam = decParam.trim();
            String[] decs = Util.split(appMap, decParam, ";");
            TagDecorator decObj;
            for (String decorator : decs) {
                try {
                    decObj = (TagDecorator) ReflectionUtil.forName(decorator)
                          .newInstance();
                    c.addTagDecorator(decObj);

                    if (LOGGER.isLoggable(Level.FINE)) {
                        LOGGER.log(Level.FINE,
                                   "Successfully Loaded Decorator: {0}",
                                   decorator);
                    }
                } catch (Exception e) {
                    if (LOGGER.isLoggable(Level.SEVERE)) {
                        LOGGER.log(Level.SEVERE,
                                   "Error Loading Decorator: " + decorator,
                                   e);
                    }
                }
            }
        }

        // skip params?
        c.setTrimmingComments(
              webConfig.isOptionEnabled(
                    BooleanWebContextInitParameter.FaceletsSkipComments));

        c.addTagLibrary(new CoreLibrary());
        c.addTagLibrary(new CoreLibrary(CoreLibrary.XMLNSNamespace));
        c.addTagLibrary(new HtmlLibrary());
        c.addTagLibrary(new HtmlLibrary(HtmlLibrary.XMLNSNamespace));
        c.addTagLibrary(new UILibrary());
        c.addTagLibrary(new UILibrary(UILibrary.XMLNSNamespace));
        c.addTagLibrary(new JstlCoreLibrary());
        c.addTagLibrary(new JstlCoreLibrary(JstlCoreLibrary.IncorrectNamespace));
        c.addTagLibrary(new JstlCoreLibrary(JstlCoreLibrary.XMLNSNamespace));
        c.addTagLibrary(new PassThroughAttributeLibrary());
        c.addTagLibrary(new PassThroughElementLibrary());
        c.addTagLibrary(new FunctionLibrary(JstlFunction.class, FunctionLibrary.Namespace));
        c.addTagLibrary(new FunctionLibrary(JstlFunction.class, FunctionLibrary.XMLNSNamespace));
        if (isDevModeEnabled()) {
            c.addTagLibrary(new FunctionLibrary(DevTools.class, DevTools.Namespace));
            c.addTagLibrary(new FunctionLibrary(DevTools.class, DevTools.NewNamespace));
        }
        c.addTagLibrary(new CompositeLibrary());
        c.addTagLibrary(new CompositeLibrary(CompositeLibrary.XMLNSNamespace));

        return c;

    }


}

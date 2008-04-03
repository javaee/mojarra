/*
 * $Id: ApplicationAssociate.java,v 1.46 2007/04/25 04:06:59 rlubke Exp $
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

package com.sun.faces.application;

import com.sun.faces.RIConstants;
import com.sun.faces.config.WebConfiguration;
import com.sun.faces.mgbean.BeanManager;
import com.sun.faces.spi.InjectionProvider;
import com.sun.faces.spi.InjectionProviderFactory;
import com.sun.faces.util.MessageUtils;
import com.sun.faces.util.Util;
import com.sun.faces.util.FacesLogger;

import javax.el.CompositeELResolver;
import javax.el.ELResolver;
import javax.el.ExpressionFactory;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.el.PropertyResolver;
import javax.faces.el.VariableResolver;
import javax.servlet.ServletContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeSet;
import java.util.logging.Logger;

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

    // Log instance for this class
    private static final Logger LOGGER = FacesLogger.APPLICATION.getLogger();

    private static final String APPLICATION_IMPL_ATTR_NAME = RIConstants.FACES_PREFIX +
         "ApplicationImpl";

    private ApplicationImpl app = null;




    /**
     * Overall Map containing <code>from-view-id</code> key and
     * <code>ArrayList</code> of <code>ConfigNavigationCase</code>
     * objects for that key; The <code>from-view-id</code> strings in
     * this map will be stored as specified in the configuration file -
     * some of them will have a trailing asterisk "*" signifying wild
     * card, and some may be specified as an asterisk "*".
     */
    private Map<String, List<ConfigNavigationCase>> caseListMap = null;

    /**
     * The List that contains all view identifier strings ending in an
     * asterisk "*".  The entries are stored without the trailing
     * asterisk.
     */
    private TreeSet<String> wildcardMatchList = null;

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

    @SuppressWarnings("deprecation")
    private PropertyResolver legacyPRChainHead = null;
    private ExpressionFactory expressionFactory = null;

    @SuppressWarnings("deprecation")
    private PropertyResolver legacyPropertyResolver = null;

    @SuppressWarnings("deprecation")
    private VariableResolver legacyVariableResolver = null;
    private CompositeELResolver facesELResolverForJsp = null;

    private InjectionProvider injectionProvider;

    private String contextName;
    private boolean requestServiced;

    private BeanManager beanManager;

    public ApplicationAssociate(ApplicationImpl appImpl) {
        app = appImpl;

        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        if (externalContext == null) {
            throw new IllegalStateException(
                 MessageUtils.getExceptionMessageString(
                      MessageUtils.APPLICATION_ASSOCIATE_CTOR_WRONG_CALLSTACK_ID));
        }

        if (null != externalContext.getApplicationMap().get(ASSOCIATE_KEY)) {
            throw new IllegalStateException(
                 MessageUtils.getExceptionMessageString(
                      MessageUtils.APPLICATION_ASSOCIATE_EXISTS_ID));
        }
        externalContext.getApplicationMap().put(APPLICATION_IMPL_ATTR_NAME,
             appImpl);
        externalContext.getApplicationMap().put(ASSOCIATE_KEY, this);
        //noinspection CollectionWithoutInitialCapacity
        caseListMap = new HashMap<String, List<ConfigNavigationCase>>();
        wildcardMatchList = new TreeSet<String>(new SortIt());
        injectionProvider = InjectionProviderFactory.createInstance(externalContext);
        WebConfiguration webConfig = WebConfiguration.getInstance(externalContext);
        beanManager = new BeanManager(injectionProvider,
                                      webConfig.getBooleanContextInitParameter(
                                           WebConfiguration.BooleanWebContextInitParameter.EnableLazyBeanValidation));
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

    public static ApplicationAssociate getInstance(ServletContext context) {
        if (context == null) {
            return null;
        }
        return (ApplicationAssociate) context.getAttribute(ASSOCIATE_KEY);
    }

    public static void setCurrentInstance(ApplicationAssociate associate) {
        instance.set(associate);
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

    public static void clearInstance(ExternalContext
         externalContext) {
        Map applicationMap = externalContext.getApplicationMap();
        ApplicationAssociate me = (ApplicationAssociate) applicationMap.get(ASSOCIATE_KEY);
        if (null != me) {
            if (null != me.resourceBundles) {
                me.resourceBundles.clear();
            }
        }
        applicationMap.remove(ASSOCIATE_KEY);        
    }


    public BeanManager getBeanManager() {
        return beanManager;
    }

    /**
     * This method is called by <code>ConfigureListener</code> and will
     * contain any <code>VariableResolvers</code> defined within
     * faces-config configuration files.
     *
     * @param resolver
     */
    @SuppressWarnings("deprecation")
    public void setLegacyVRChainHead(VariableResolver resolver) {
        this.legacyVRChainHead = resolver;
    }

    @SuppressWarnings("deprecation")
    public VariableResolver getLegacyVRChainHead() {
        return legacyVRChainHead;
    }

    /**
     * This method is called by <code>ConfigureListener</code> and will
     * contain any <code>PropertyResolvers</code> defined within
     * faces-config configuration files.
     *
     * @param resolver
     */
    @SuppressWarnings("deprecation")
    public void setLegacyPRChainHead(PropertyResolver resolver) {
        this.legacyPRChainHead = resolver;
    }

    @SuppressWarnings("deprecation")
    public PropertyResolver getLegacyPRChainHead() {
        return legacyPRChainHead;
    }

    public CompositeELResolver getFacesELResolverForJsp() {
        return facesELResolverForJsp;
    }

    public void setFacesELResolverForJsp(CompositeELResolver celr) {
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

    public List<ELResolver> getApplicationELResolvers() {
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
     */
    @SuppressWarnings("deprecation")
    public void setLegacyPropertyResolver(PropertyResolver resolver) {
        this.legacyPropertyResolver = resolver;
    }

    /**
     * Returns the PropertyResolver called through
     * Application.getPropertyResolver()
     */
    @SuppressWarnings("deprecation")
    public PropertyResolver getLegacyPropertyResolver() {
        return legacyPropertyResolver;
    }

    /**
     * Maintains the PropertyResolver called through
     * Application.setVariableResolver()
     */
    @SuppressWarnings("deprecation")
    public void setLegacyVariableResolver(VariableResolver resolver) {
        this.legacyVariableResolver = resolver;
    }

    /**
     * Returns the VariableResolver called through
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


    /**
     * Add a navigation case to the internal case list.  If a case list
     * does not already exist in the case list map containing this case
     * (identified by <code>from-view-id</code>), start a new list,
     * add the case to it, and store the list in the case list map.
     * If a case list already exists, see if a case entry exists in the list
     * with a matching <code>from-view-id</code><code>from-action</code>
     * <code>from-outcome</code> combination.  If there is suach an entry,
     * overwrite it with this new case.  Otherwise, add the case to the list.
     *
     * @param navigationCase the navigation case containing navigation
     *                       mapping information from the configuration file.
     */
    public void addNavigationCase(ConfigNavigationCase navigationCase) {

        String fromViewId = navigationCase.getFromViewId();
        List<ConfigNavigationCase> caseList = caseListMap.get(fromViewId);
        if (caseList == null) {
            //noinspection CollectionWithoutInitialCapacity
            caseList = new ArrayList<ConfigNavigationCase>();
            caseList.add(navigationCase);
            caseListMap.put(fromViewId, caseList);
        } else {
            String key = navigationCase.getKey();
            boolean foundIt = false;
            for (int i = 0; i < caseList.size(); i++) {
                ConfigNavigationCase navCase = caseList.get(i);
                // if there already is a case existing for the
                // fromviewid/fromaction.fromoutcome combination,
                // replace it ...  (last one wins).
                //
                if (key.equals(navCase.getKey())) {
                    caseList.set(i, navigationCase);
                    foundIt = true;
                    break;
                }
            }
            if (!foundIt) {
                caseList.add(navigationCase);
            }
        }
        if (fromViewId.endsWith("*")) {
            fromViewId =
                 fromViewId.substring(0, fromViewId.lastIndexOf('*'));
            wildcardMatchList.add(fromViewId);
        }

    }


    /**
     * Return a <code>Map</code> of navigation mappings loaded from
     * the configuration system.  The key for the returned <code>Map</code>
     * is <code>from-view-id</code>, and the value is a <code>List</code>
     * of navigation cases.
     *
     * @return Map the map of navigation mappings.
     */
    public Map<String, List<ConfigNavigationCase>> getNavigationCaseListMappings() {
        if (caseListMap == null) {
            return Collections.emptyMap();
        }
        return caseListMap;
    }


    /**
     * Return all navigation mappings whose <code>from-view-id</code>
     * contained a trailing "*".
     *
     * @return <code>TreeSet</code> The navigation mappings sorted in
     *         descending order.
     */
    public TreeSet<String> getNavigationWildCardList() {
        return wildcardMatchList;
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
        if (null != (root = context.getViewRoot())) {
            // If so, ask it for its Locale
            if (null == (locale = root.getLocale())) {
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
    void responseRendered() {
        responseRendered = true;
    }

    boolean isResponseRendered() {
        return responseRendered;
    }


    /**
     * This Comparator class will help sort the <code>ConfigNavigationCase</code> objects
     * based on their <code>fromViewId</code> properties in descending order -
     * largest string to smallest string.
     */
    static class SortIt implements Comparator<String> {

        public int compare(String fromViewId1, String fromViewId2) {
            return -(fromViewId1.compareTo(fromViewId2));
        }
    }

    /**
     * Holds value of property JSFVersionTracker.
     */
    private com.sun.faces.config.JSFVersionTracker JSFVersionTracker;

    /**
     * Getter for property JSFVersionTracker.
     *
     * @return Value of property JSFVersionTracker.
     */
    public com.sun.faces.config.JSFVersionTracker getJSFVersionTracker() {
        return this.JSFVersionTracker;
    }

    /**
     * Setter for property JSFVersionTracker.
     *
     * @param JSFVersionTracker New value of property JSFVersionTracker.
     */
    public void setJSFVersionTracker(com.sun.faces.config.JSFVersionTracker JSFVersionTracker) {
        this.JSFVersionTracker = JSFVersionTracker;
    }

}

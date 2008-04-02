/*
 * $Id: ApplicationAssociate.java,v 1.24 2006/01/11 15:28:02 rlubke Exp $
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.el.CompositeELResolver;
import javax.el.ExpressionFactory;
import javax.faces.FacesException;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.el.PropertyResolver;
import javax.faces.el.VariableResolver;

import com.sun.faces.RIConstants;
import com.sun.faces.config.ConfigureListener;
import com.sun.faces.spi.ManagedBeanFactory;
import com.sun.faces.spi.ManagedBeanFactory.Scope;
import com.sun.faces.config.beans.ResourceBundleBean;
import com.sun.faces.util.Util;
import com.sun.faces.util.MessageUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.component.UIViewRoot;
import javax.servlet.ServletContext;

/**
 * <p>Break out the things that are associated with the Application, but
 * need to be present even when the user has replaced the Application
 * instance.</p>
 * 
 * <p>For example: the user replaces ApplicationFactory, and wants to
 * intercept calls to createValueExpression() and createMethodExpression() for
 * certain kinds of expressions, but allow the existing application to
 * handle the rest.</p>
 */

public class ApplicationAssociate {

    // Log instance for this class
    private static Logger logger = Util.getLogger(Util.FACES_LOGGER 
            + Util.APPLICATION_LOGGER);

    private ApplicationImpl app = null;
    
    //
    // This map stores "managed bean name" | "managed bean factory"
    // mappings.
    //
    private Map<String, ManagedBeanFactory> managedBeanFactoriesMap = null;
    
    /**
     * Simple struct encapsulating a managed bean instance with its list 
     * of PreDestroy methods.
     */
    private static class ManagedBeanPreDestroyStruct extends Object {
        private Object bean = null;
        private List<Method> preDestroys = null;
        ManagedBeanPreDestroyStruct(Object bean, List<Method> preDestroys) {
            this.bean = bean;
            this.preDestroys = preDestroys;
        }
        void clear() {
            this.bean = null;
            this.preDestroys = null;
        }
    }
    
    /**
     * An array of size four (one for each scope) of Maps.  Each Map maps from
     * managedBeanName to an instance of ManagedBeanPreDestroyStruct.
     */
    
    private Map<String, ManagedBeanPreDestroyStruct> preDestroyMethods[] = null;

    // These maps stores "navigation rule" mappings.
    //

    /**
     * Overall Map containing <code>from-view-id</code> key and
     * <code>ArrayList</code> of <code>ConfigNavigationCase</code>
     * objects for that key; The <code>from-view-id</code> strings in
     * this map will be stored as specified in the configuration file -
     * some of them will have a trailing asterisk "*" signifying wild
     * card, and some may be specified as an asterisk "*".
     */
    private Map<String,List<ConfigNavigationCase>> caseListMap = null;

    /**
     * The List that contains the <code>ConfigNavigationCase</code>
     * objects for a <code>from-view-id</code>.
     */
    private List<ConfigNavigationCase> caseList = null;

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

    private ArrayList elResolversFromFacesConfig = null;
    private VariableResolver legacyVRChainHead = null;
    private PropertyResolver legacyPRChainHead = null;
    private ExpressionFactory expressionFactory = null;
    
    private PropertyResolver legacyPropertyResolver = null;
    private VariableResolver legacyVariableResolver = null;
    private CompositeELResolver facesELResolverForJsp = null;

    public ApplicationAssociate(ApplicationImpl appImpl) {
	app = appImpl;
        
        ExternalContext externalContext;
        if (null == (externalContext =
                     ConfigureListener.getExternalContextDuringInitialize())) {
            throw new IllegalStateException(
                MessageUtils.getExceptionMessageString(
                    MessageUtils.APPLICATION_ASSOCIATE_CTOR_WRONG_CALLSTACK_ID));
        }

        if (null != externalContext.getApplicationMap().get(ASSOCIATE_KEY)) {
            throw new IllegalStateException(
                MessageUtils.getExceptionMessageString(
                    MessageUtils.APPLICATION_ASSOCIATE_EXISTS_ID));
        }
        externalContext.getApplicationMap().put(ASSOCIATE_KEY, this);
        managedBeanFactoriesMap = new HashMap<String, ManagedBeanFactory>();
        preDestroyMethods = new Map[4];
        preDestroyMethods[Scope.REQUEST.ordinal()] = 
                new HashMap<String, ManagedBeanPreDestroyStruct>();
        preDestroyMethods[Scope.SESSION.ordinal()] = 
                new HashMap<String, ManagedBeanPreDestroyStruct>();
        preDestroyMethods[Scope.APPLICATION.ordinal()] = 
                new HashMap<String, ManagedBeanPreDestroyStruct>();
        caseListMap = new HashMap<String,List<ConfigNavigationCase>>();
        wildcardMatchList = new TreeSet<String>(new SortIt());

    }
    
    public static ApplicationAssociate getInstance(ExternalContext 
        externalContext) {
        Map applicationMap = externalContext.getApplicationMap();    
	return ((ApplicationAssociate) 
		applicationMap.get(ASSOCIATE_KEY));
    }
    
    public static ApplicationAssociate getInstance(ServletContext context) {
        return (ApplicationAssociate) context.getAttribute(ASSOCIATE_KEY);
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

    public void setLegacyVRChainHead(VariableResolver resolver) {
        this.legacyVRChainHead = resolver;   
    }
    
    public VariableResolver getLegacyVRChainHead() {
        return legacyVRChainHead;
    }
        
    public void setLegacyPRChainHead(PropertyResolver resolver) {
        this.legacyPRChainHead = resolver;   
    }
           
    public PropertyResolver getLegacyPRChainHead() {
        return legacyPRChainHead;
    }
    
    public CompositeELResolver getFacesELResolverForJsp() {
        return facesELResolverForJsp;
    }
        
    public void setFacesELResolverForJsp(CompositeELResolver celr) {
        facesELResolverForJsp = celr;
    }
    
    public void setELResolversFromFacesConfig(ArrayList resolvers) {
        this.elResolversFromFacesConfig = resolvers;
    }
     
    public ArrayList geELResolversFromFacesConfig() {
         return elResolversFromFacesConfig;
    }
    
    public void setExpressionFactory(ExpressionFactory expressionFactory) {
        this.expressionFactory = expressionFactory;
    }
    
    public ExpressionFactory getExpressionFactory() {
        return this.expressionFactory;
    }
    
    public ArrayList getApplicationELResolvers() {
        return app.getApplicationELResolvers();
    }
    
    /**
     * Maintains the PropertyResolver called through 
     * Application.setPropertyResolver()
     */
    public void setLegacyPropertyResolver(PropertyResolver resolver){
        this.legacyPropertyResolver = resolver;
    }
    
     /**
     * Returns the PropertyResolver called through 
     * Application.getPropertyResolver()
     */
    public PropertyResolver getLegacyPropertyResolver(){
        return legacyPropertyResolver;
    }
    
    /**
     * Maintains the PropertyResolver called through 
     * Application.setVariableResolver()
     */
    public void setLegacyVariableResolver(VariableResolver resolver){
        this.legacyVariableResolver = resolver;
    }
    
    /**
     * Returns the VariableResolver called through 
     * Application.getVariableResolver()
     */
    public VariableResolver getLegacyVariableResolver(){
        return legacyVariableResolver;
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
        synchronized (this) {
            caseList = caseListMap.get(fromViewId);
            if (caseList == null) {
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
                    fromViewId.substring(0, fromViewId.lastIndexOf("*"));
                wildcardMatchList.add(fromViewId);
            }
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
    public Map<String,List<ConfigNavigationCase>> getNavigationCaseListMappings() {
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
        UIViewRoot root = null;
        // Start out with the default locale
        Locale locale = null, defaultLocale = Locale.getDefault();
        locale = defaultLocale;
        // See if this FacesContext has a ViewRoot
        if (null != (root = context.getViewRoot())) {
            // If so, ask it for its Locale
            if (null == (locale = root.getLocale())) {
                // If the ViewRoot has no Locale, fall back to the default.
                locale = defaultLocale;
            }
        }
        assert(null != locale);
        ResourceBundleBean bean = resourceBundles.get(var);
        String baseName = null;
        ResourceBundle result = null;
        
        if (null != bean) {
            baseName = bean.getBasename();
            if (null != baseName) {
                result =
                    ResourceBundle.getBundle(baseName,
                                             locale,
                                             Thread.currentThread().
                                                 getContextClassLoader());
            }
        }
        // PENDING(edburns): should cache these based on var/Locale pair for performance
        return result;
    }
    
    /**
     * keys: <var> element from faces-config<p>
     *
     * values: ResourceBundleBean instances.
     */
    
    Map<String,ResourceBundleBean> resourceBundles = new HashMap<String, ResourceBundleBean>();
    
    public void addResourceBundleBean(String var, ResourceBundleBean bean) {
        resourceBundles.put(var, bean);
    }

    public Map<String,ResourceBundleBean> getResourceBundleBeanMap() {
        return resourceBundles;
    }
    
    /**
     * <p>Adds a new mapping of managed bean name to a managed bean
     * factory instance.</p>
     *
     * @param managedBeanName the name of the managed bean that will
     *                        be created by the managed bean factory instance.
     * @param factory         the managed bean factory instance.
     */
    synchronized public void addManagedBeanFactory(String managedBeanName,
                                                   ManagedBeanFactory factory) {
        managedBeanFactoriesMap.put(managedBeanName, factory);
	factory.setManagedBeanFactoryMap(managedBeanFactoriesMap);
        if (logger.isLoggable(Level.FINE)) {
            logger.log(Level.FINE, "Added managedBeanFactory " + factory + 
                    " for" + managedBeanName);
        }
    }
    
    public Map<String,ManagedBeanFactory> getManagedBeanFactoryMap() {
        return managedBeanFactoriesMap;
    }



    /**
     * <p>The managedBeanFactories HashMap has been populated
     * with ManagedBeanFactoryImpl object keyed by the bean name.
     * Find the ManagedBeanFactoryImpl object and if it exists instantiate
     * the bean and store it in the appropriate scope, if any.</p>
     * 
     * @param context         The Faces context.
     * @param managedBeanName The name identifying the managed bean.
     * @return The managed bean.
     * @throws FacesException if the managed bean
     *                                   could not be created.
     */
    public Object createAndMaybeStoreManagedBeans(FacesContext context,
        String managedBeanName) throws FacesException {
        ManagedBeanFactory managedBean = managedBeanFactoriesMap.get(managedBeanName);
        if (managedBean == null) {
            if (logger.isLoggable(Level.FINE)) {
                logger.fine("Couldn't find a factory for " + managedBeanName);
            }
            return null;
        }
        
        Object bean = null;
        Scope scope = managedBean.getScope();
        
        boolean
            scopeIsApplication = false,
            scopeIsSession = false,
            scopeIsRequest = false;

        if ((scopeIsApplication = (scope == Scope.APPLICATION)) || 
            (scopeIsSession = (scope == Scope.SESSION))) {
            if (scopeIsApplication) {
                Map<String,Object> applicationMap = context.getExternalContext().
                        getApplicationMap();
                synchronized (applicationMap) {
                    try {
                        bean = managedBean.newInstance(context);
                        handlePostConstruct(managedBeanName, scope, bean);
                        if (logger.isLoggable(Level.FINE)) {
                            logger.fine("Created application scoped bean " + 
                                    managedBeanName + " successfully ");
                        }
                    } catch (Exception ex) {
                        Object[] params = {managedBeanName};
                        if (logger.isLoggable(Level.SEVERE)) {
                            logger.log(Level.SEVERE, 
                                    "jsf.managed_bean_creation_error", params);
                        }
                        throw new FacesException(ex);
                    }
                    //add bean to appropriate scope
                    applicationMap.put(managedBeanName, bean);
                } 
            } else {
                Map<String,Object> sessionMap = Util.getSessionMap(context);
                synchronized (sessionMap) {
                    try {
                        bean = managedBean.newInstance(context);
                        handlePostConstruct(managedBeanName, scope, bean);
                        if (logger.isLoggable(Level.FINE)) {
                            logger.fine("Created session scoped bean " 
                                    + managedBeanName + " successfully ");
                        }
                    } catch (Exception ex) {
                        Object[] params = {managedBeanName};
                        if (logger.isLoggable(Level.SEVERE)) {
                            logger.log(Level.SEVERE, 
                                    "jsf.managed_bean_creation_error", params);
                        }
                        throw new FacesException(ex);
                    }
                    //add bean to appropriate scope
                    sessionMap.put(managedBeanName, bean);
                }
            }              
        } else {
            scopeIsRequest = (scope == Scope.REQUEST);
            try {
                bean = managedBean.newInstance(context);
                if (scopeIsRequest) {
                    handlePostConstruct(managedBeanName, scope, bean);
                }
                if (logger.isLoggable(Level.FINE)) {
                    logger.log(Level.FINE, "Created bean " + managedBeanName + 
                              " successfully ");
                }
            } catch (Exception ex) {
                Object[] params = {managedBeanName};
                if (logger.isLoggable(Level.SEVERE)) {
                    logger.log(Level.SEVERE, 
                            "jsf.managed_bean_creation_error", params);
                }
                throw new FacesException(ex);
            }

            if (scopeIsRequest) {
                context.getExternalContext().
                    getRequestMap().put(managedBeanName, bean);
            }
        }
        return bean;
    }

    // This is called by ViewHandlerImpl.renderView().
    synchronized void responseRendered() {
        responseRendered = true;
    }

    boolean isResponseRendered() {
	return responseRendered;
    }

    /**
     * <p>Called from {@link #createAndMaybeStoreManagedBeans}.
     * Discover the methods on argument <code>bean</code> annotated with
     * <code>@PostConstruct</code> and <code>@PreDestroy</code> using
     * {@link Util#getMethodsWithAnnotation}.  This method returns a
     * possibly empty <code>List&lt;Method&gt;</code>.  For each method
     * in the postConstruct list, invoke it.  For each method for each
     * non NONE scoped bean in the preDestroyList, add to the
     * preDestroyMethods array in the proper scope.</p>
     *
     * @param beanName the name of the bean for which to call PostConstruct
     * annotated methods.  If <code>null</code>, all beans in argument
     * <code>scope</code> have their PreDestroy annotated methods
     * called.
     *
     * @param scope the managed bean scope in which to look for the bean
     * or beans.
     *
     * @param bean the actual managed bean instance.  Injection must
     * have already been performed.
     */

    private void handlePostConstruct(String beanName,
        Scope scope, Object bean) throws IllegalAccessException,
                     IllegalArgumentException,
                     InvocationTargetException {
        List<Method> postConstructs = Util.getMethodsWithAnnotation(bean, 
                PostConstruct.class);
        List<Method> preDestroys = Util.getMethodsWithAnnotation(bean, 
                PreDestroy.class);
        assert(null != postConstructs && null != preDestroys);
        
        for (Method method : postConstructs) {
            method.invoke(bean);
        }
        // We can't call @PreDestroy on NONE scoped beans.
        if (!preDestroys.isEmpty() && scope != Scope.NONE) {
            
            preDestroyMethods[scope.ordinal()].put(beanName, 
                    new ManagedBeanPreDestroyStruct(bean, preDestroys));
        }
        
    }

    /**
     * <p>Called from ExternalContextImpl, in the Map implementations
     * for request, session, and application maps, from the remove() and
     * clear() implementations in those maps.  Leverage the data stored
     * by the required previous invocation of {@link
     * #handlePostConstruct} to discover and invoke any postDestroy
     * annotated methods.  If argument <code>beanName</code> is null,
     * all managed-bean entries in argument <code>scope</code> have
     * their PreDestroy annotated methods invoked.  Otherwise, only the
     * managed bean named by argument <code>beanName</code> has its
     * PreDestroy annotated methods called.</p>
     *
     * @param beanName the name of the bean for which to call PreDestroy
     * annotated methods.  If <code>null</code>, all beans in argument
     * <code>scope</code> have their PreDestroy annotated methods
     * called.
     *
     * @param scope the managed bean scope in which to look for the bean
     * or beans.
     */ 
    
    public void handlePreDestroy(String beanName, Scope scope) throws
	IllegalAccessException,
	IllegalArgumentException,
	InvocationTargetException {
	// Get the pre-populated map for this Scope
        Map<String, ManagedBeanPreDestroyStruct> destroyMap =
	    preDestroyMethods[scope.ordinal()];
        // If we have no beans in the map, just return;
        if (destroyMap.isEmpty()) {
            return;
        }
        // If we have no beanName, invoke all the preDestroy methods for
        // all the managed-beans in the argument scope.
        if (null == beanName) {
            if (scope != Scope.REQUEST) {
                synchronized(scope) {
		    invokePreDestroyAndRemoveFromMap(destroyMap);
                }
            } 
	    else {
                assert(scope == Scope.REQUEST);
		invokePreDestroyAndRemoveFromMap(destroyMap);
            }
	} 
	else {
	    // Otherwise, only invoke the preDestroy methods for the
	    // argument beanName in the argument scope.
	    ManagedBeanPreDestroyStruct struct = destroyMap.get(beanName);
	    if (null != struct) {
		if (scope != Scope.REQUEST) {
		    synchronized(scope) {
			for (Method method : struct.preDestroys) {
			    method.invoke(struct.bean);
			}
			struct.preDestroys.clear();
			struct.clear();
			destroyMap.remove(beanName);
		    }
		}
		else {
		    assert(scope == Scope.REQUEST);
		    for (Method method : struct.preDestroys) {
			method.invoke(struct.bean);
		    }
		    struct.preDestroys.clear();
		    struct.clear();
		    destroyMap.remove(beanName);
		}
	    } // if null != struct
	}
    }

    private void invokePreDestroyAndRemoveFromMap(Map<String, ManagedBeanPreDestroyStruct> destroyMap) throws
	IllegalAccessException,
	IllegalArgumentException,
	InvocationTargetException {
	ArrayList<String> names = new ArrayList(destroyMap.size());
	// Go through the entries once and invoke the
	// annotated methods
	for (Map.Entry<String, ManagedBeanPreDestroyStruct> entry :
		 destroyMap.entrySet()) {
	    for (Method method : entry.getValue().preDestroys) {
		method.invoke(entry.getValue().bean);
	    }
	    names.add(entry.getKey());
	    entry.getValue().preDestroys.clear();
	    entry.getValue().clear();
	}
        
	// Go through the entries again, and remove them from
	// the map.
	for (String name : names) {
	    destroyMap.remove(name);
	}
    }
	


    


    /**
     * This Comparator class will help sort the <code>ConfigNavigationCase</code> objects
     * based on their <code>fromViewId</code> properties in descending order -
     * largest string to smallest string.
     */
    static class SortIt implements Comparator {

        public int compare(Object o1, Object o2) {
            String fromViewId1 = (String) o1;
            String fromViewId2 = (String) o2;
            return -(fromViewId1.compareTo(fromViewId2));
        }
    }

}

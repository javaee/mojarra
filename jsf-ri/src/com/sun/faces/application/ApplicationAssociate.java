/*
 * $Id: ApplicationAssociate.java,v 1.4 2004/10/12 14:39:48 rlubke Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.application;

import com.sun.faces.util.Util;
import com.sun.faces.util.InstancePool;
import com.sun.faces.RIConstants;
import com.sun.faces.config.ConfigureListener;

import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import java.util.Comparator;
import java.util.Collections;

import javax.faces.context.FacesContext;
import javax.faces.context.ExternalContext;
import javax.faces.FacesException;

import com.sun.faces.config.ManagedBeanFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <p>Break out the things that are associated with the Application, but
 * need to be present even when the user has replaced the Application
 * instance.</p>
 * 
 * <p>For example: the user replaces ApplicationFactory, and wants to
 * intercept calls to createValueBinding() and createMethodBinding() for
 * certain kinds of expressions, but allow the existing application to
 * handle the rest.</p>
 */

public class ApplicationAssociate extends Object {

    protected static Log log = LogFactory.getLog(ApplicationImpl.class);

    private ApplicationImpl app = null;

    //
    // This map stores "managed bean name" | "managed bean factory"
    // mappings.
    //
    private Map managedBeanFactoriesMap = null;

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
    private Map caseListMap = null;

    /**
     * The List that contains the <code>ConfigNavigationCase</code>
     * objects for a <code>from-view-id</code>.
     */
    private List caseList = null;

    /**
     * The List that contains all view identifier strings ending in an
     * asterisk "*".  The entries are stored without the trailing
     * asterisk.
     */
    private TreeSet wildcardMatchList = null;

    // Flag indicating that a response has been rendered.
    private boolean responseRendered = false;

    private static final String ASSOCIATE_KEY = RIConstants.FACES_PREFIX + 
	"ApplicationAssociate";


    /**
     * <p>Used by the EL system to pool instances of ExpressionInfo.
     * </p>
     */
    private InstancePool expressionInfoInstancePool;

    public ApplicationAssociate(ApplicationImpl appImpl) {
	app = appImpl;
	ExternalContext externalContext = null;
	if (null == (externalContext = 
		     ConfigureListener.getExternalContextDuringInitialize())) {
	    // PENDING I18N
	    throw new IllegalStateException("ApplicationAssociate ctor not called in same callstack as ConfigureListener.contextInitialized()");
	}
	// PENDING I18N
	if (null != externalContext.getApplicationMap().get(ASSOCIATE_KEY)) {
	    throw new IllegalStateException("ApplicationAssociate already exists for this webapp");
	}
	externalContext.getApplicationMap().put(ASSOCIATE_KEY, this);
        managedBeanFactoriesMap = new HashMap();
        caseListMap = new HashMap();
        wildcardMatchList = new TreeSet(new SortIt());
	
	expressionInfoInstancePool = new InstancePool();
    }
    
    public static ApplicationAssociate getInstance(ExternalContext 
        externalContext) {
        Map applicationMap = externalContext.getApplicationMap();    
	return ((ApplicationAssociate) 
		applicationMap.get(ASSOCIATE_KEY));
    }
    
    public static void clearInstance(ExternalContext 
        externalContext) {
        Map applicationMap = externalContext.getApplicationMap();
	applicationMap.remove(ASSOCIATE_KEY);
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
            caseList = (List) caseListMap.get(fromViewId);
            if (caseList == null) {
                caseList = new ArrayList();
                caseList.add(navigationCase);
                caseListMap.put(fromViewId, caseList);
            } else {
                String key = navigationCase.getKey();
                boolean foundIt = false;
                for (int i = 0; i < caseList.size(); i++) {
                    ConfigNavigationCase navCase =
                        (ConfigNavigationCase) caseList.get(i);
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
    public Map getNavigationCaseListMappings() {
        if (caseListMap == null) {
            return Collections.EMPTY_MAP;
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
    public TreeSet getNavigationWildCardList() {
        return wildcardMatchList;
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
        if (log.isTraceEnabled()) {
            log.trace("Added managedBeanFactory " + factory + " for" +
                      managedBeanName);
        }
    }





    /**
     * <p>The managedBeanFactories HashMap has been populated
     * with ManagedBeanFactory object keyed by the bean name.
     * Find the ManagedBeanFactory object and if it exists instantiate
     * the bean and store it in the appropriate scope, if any.</p>
     *
     * @param context         The Faces context.
     * @param managedBeanName The name identifying the managed bean.
     * @return The managed bean.
     * @throws FacesException if the managed bean
     *                                   could not be created.
     */
    synchronized public Object createAndMaybeStoreManagedBeans(FacesContext context,
                                                               String managedBeanName)
        throws FacesException {
        ManagedBeanFactory managedBean = (ManagedBeanFactory)
            managedBeanFactoriesMap.get(managedBeanName);
        if (managedBean == null) {
            if (log.isDebugEnabled()) {
                log.debug("Couldn't find a factory for " + managedBeanName);
            }
            return null;
        }
        
        Object bean = null;
        try {
            bean = managedBean.newInstance(context);
            if (log.isDebugEnabled()) {
                log.debug("Created bean " + managedBeanName + " successfully ");
            }
        } catch (Exception ex) {
            Object[] params = {ex.getMessage()};
            if (log.isErrorEnabled()) {
                log.error("Managedbean " + managedBeanName + 
                    " could not be created " + ex.getMessage(), ex);
            } 
            throw new FacesException(ex);
        }
        //add bean to appropriate scope
        String scope = managedBean.getScope();
        //scope cannot be null
        assert (null != scope);
        if (log.isTraceEnabled()) {
            log.trace("Storing " + managedBeanName + " in scope " + scope);
        }

        if (scope.equalsIgnoreCase(RIConstants.APPLICATION)) {
            context.getExternalContext().
                getApplicationMap().put(managedBeanName, bean);
        } else if (scope.equalsIgnoreCase(RIConstants.SESSION)) {
            Util.getSessionMap(context).put(managedBeanName, bean);
        } else if (scope.equalsIgnoreCase(RIConstants.REQUEST)) {
            context.getExternalContext().
                getRequestMap().put(managedBeanName, bean);
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
     * This Comparator class will help sort the <code>ConfigNavigationCase</code> objects
     * based on their <code>fromViewId</code> properties in descending order -
     * largest string to smallest string.
     */
    class SortIt implements Comparator {

        public int compare(Object o1, Object o2) {
            String fromViewId1 = (String) o1;
            String fromViewId2 = (String) o2;
            return -(fromViewId1.compareTo(fromViewId2));
        }
    }

    class CaseStruct {

        String viewId;
        ConfigNavigationCase navCase;
    }

    /**
     * <p>@return the {@link InstancePool} that is allocated for the
     * purposes of holding {@link com.sun.faces.el.impl.ExpressionInfo}
     * instances.  The ApplicationAssociate does nothing but serve as
     * the owning reference of the <code>InstancePool</code>.  Actual
     * usage of the <code>InstancePool</code> happens in the EL
     * package.</p>
     */ 

    public InstancePool getExpressionInfoInstancePool() {
	return expressionInfoInstancePool;
    }

}

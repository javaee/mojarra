/*
 * $Id: NavigationHandlerImpl.java,v 1.12 2003/08/22 16:49:39 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.application;

import com.sun.faces.config.ConfigNavigationCase;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import javax.faces.FactoryFinder;
import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;

import org.mozilla.util.Assert;

/**
 * <p><strong>NavigationHandlerImpl</strong> is the class that implements
 * default navigation handling. Refer to section 7.4.2 of the specification for 
 * more details.
 */
 
public class NavigationHandlerImpl extends NavigationHandler {

    //
    // Protected Constants
    //

    //
    // Class Variables
    //

    // Instance Variables

    /**
     * The original (entire) list of navigation cases.
     */

    private List navigationCases = null;

    /**
     * Overall Map containing <code>from-view-id</code> key and <code>ArrayList</code>
     *  of <code>ConfigNavigationCase</code> objects for that key; 
     * The <code>from-view-id</code> strings in this map will be stored as specified
     * in the configuration file - some of them will have a trailing asterisk "*"
     * signifying wild card, and some may be specified as an asterisk "*".
     */ 

    protected Map caseListMap = null;

    /**
     * The List that contains the <code>ConfigNavigationCase</code> objects for a
     * <code>from-view-id</code>.
     */ 

    private List caseList = null;

    /**
     * The List that contains all view identifier strings ending in an asterisk "*".
     * The entries are stored without the trailing asterisk. 
     */

    private TreeSet wildcardMatchList= null;


    /**
     * Constructor 
     */

    public NavigationHandlerImpl() {
        super();
	if (caseListMap == null) {
	   caseListMap = new HashMap();
	}
	if (wildcardMatchList == null) {
           wildcardMatchList = new TreeSet(new SortIt());
	}
    }

    /**
     * This method uses helper methods to set the new <code>view</code> identifier;
     * If the new <code>view</code> identifier could not be determined, then the original
     * <code>view</code> identifier is left alone.
     *
     * @param context The Faces Context
     * @param actionRef The action reference string
     * @param outcome The outcome string
     */

    public void handleNavigation(FacesContext context, String actionRef, String outcome) {
        // PENDING (rlubke) PROVIDE IMPLEMENTATION
    }
    
    /**
     * This method uses helper methods to determine the new <code>view</code> identifier.
     * Refer to section 7.4.2 of the specification for more details.
     *
     * @param context The Faces Context
     * @param actionRef The action reference string
     * @param outcome The outcome string
     *
     * @return The <code>view</code> identifier. 
     */
     private String getViewId(FacesContext context, String actionRef, String outcome) {
        String nextViewId = null;
        String viewId = context.getViewRoot().getViewId();
        nextViewId = findExactMatch(viewId, actionRef, outcome);
      
        if (nextViewId == null) {
            nextViewId = findWildCardMatch(viewId, actionRef, outcome);        
        }
      
        if (nextViewId == null) {
            nextViewId = findDefaultMatch(actionRef, outcome);            
        }
        return nextViewId;
    }


    public void addNavigationCase(ConfigNavigationCase navigationCase) {

        String fromViewId = navigationCase.getFromViewId();
        if (fromViewId == null) {
            fromViewId = "*";
        }
        if (fromViewId != null) {
            caseList = (List)caseListMap.get(fromViewId);
            if (caseList == null) {
                caseList = new ArrayList();
                caseList.add(navigationCase);
                caseListMap.put(fromViewId, caseList);
            } else {
                caseList.add(navigationCase);
            }
            if (fromViewId.endsWith("*")) {
                fromViewId = fromViewId.substring(0,fromViewId.lastIndexOf("*"));
                wildcardMatchList.add(fromViewId);
            }
        }
    }
        
        
    /**
     * This method finds the List of cases for the current <code>view</code> identifier.
     * After the cases are found, the <code>from-action-ref</code> and <code>from-outcome</code>
     * values are evaluated to determine the new <code>view</code> identifier.
     * Refer to section 7.4.2 of the specification for more details.
     *
     * @param viewId  The current <code>view</code> identifier.
     * @param actionRef The action reference string.
     * @param outcome The outcome string.
     *
     * @return The <code>view</code> identifier.
     */

    private String findExactMatch(String viewId, String actionRef, String outcome) {
        String returnViewId = null;

        Assert.assert_it(null != caseListMap);

        List caseList = (List)caseListMap.get(viewId);

        if (caseList == null) {
            return null;
        }

        // We've found an exact match for the viewId.  Now we need to evaluate
        // actionref/outcome in the following order:
        // 1) elements specifying both from-action-ref and from-outcome
        // 2) elements specifying only from-outcome
        // 3) elements specifying only from-action-ref
        // 4) elements where both from-action-ref and from-outcome are null


        returnViewId = determineViewFromActionRefOutcome(caseList, actionRef, outcome);

        return returnViewId;
    }

    /**
     * This method traverses the wild card match List (containing <code>from-view-id</code>
     * strings and finds the List of cases for each <code>from-view-id</code> string.
     * Refer to section 7.4.2 of the specification for more details.
     *
     * @param viewId  The current <code>view</code> identifier.
     * @param actionRef The action reference string.
     * @param outcome The outcome string.
     *
     * @return The <code>view</code> identifier.
     */

    private String findWildCardMatch(String viewId, String actionRef, String outcome) {
        String returnViewId = null;

        Assert.assert_it(null != wildcardMatchList);

	Iterator iter = wildcardMatchList.iterator();
	while (iter.hasNext()) {
	    String fromViewId = (String)iter.next();  
	    
            // See if the entire wildcard string (without the trailing "*" is
            // contained in the incoming viewId.  Ex: /foobar is contained with /foobarbaz
            // If so, then we have found our largest pattern match..
            // If not, then continue on to the next case;

            if (viewId.indexOf(fromViewId, 0) == -1) {
                continue;
            }

            // Append the trailing "*" so we can do our map lookup;

            String wcFromViewId = fromViewId + "*"; 
            List caseList = (List)caseListMap.get(wcFromViewId);

            if (caseList == null) {
                return null;
            }

            // If we've found a match, then we need to evaluate actionref/outcome in the following
            // order:  1)elements specifying both from-action-ref and from-outcome
            // 2) elements specifying only from-outcome
            // 3) elements specifying only from-action-ref
            // 4) elements where both from-action-ref and from-outcome are null

            returnViewId = determineViewFromActionRefOutcome(caseList, actionRef, outcome);
            if (returnViewId != null) {
                break;
            }
        } 
        return returnViewId;
    }

    /**
     * This method will extract the cases for which a <code>from-view-id</code> is
     * an asterisk "*".
     * Refer to section 7.4.2 of the specification for more details.
     *
     * @param actionRef The action reference string.
     * @param outcome The outcome string.
     *
     * @return The <code>view</code> identifier.
     */

    private String findDefaultMatch(String actionRef, String outcome) {
        String returnViewId = null;

        Assert.assert_it(null != caseListMap);

        List caseList = (List)caseListMap.get("*"); 

        if (caseList == null) {
            return null;
        }

        // We need to evaluate actionref/outcome in the follow
        // order:  1)elements specifying both from-action-ref and from-outcome
        // 2) elements specifying only from-outcome
        // 3) elements specifying only from-action-ref
        // 4) elements where both from-action-ref and from-outcome are null

        returnViewId = determineViewFromActionRefOutcome(caseList, actionRef, outcome);
        return returnViewId;
    }
        
    /**
     * This method will attempt to find the <code>view</code> identifier based on action reference
     * and outcome.  Refer to section 7.4.2 of the specification for more details.
     *
     * @param caseList The list of navigation cases.
     * @param actionRef The action reference string.
     * @param outcome The outcome string.
     *
     * @return The <code>view</code> identifier.
     */
     
    private String determineViewFromActionRefOutcome(List caseList, String actionRef, String outcome) {

        String returnViewId = null;

        String fromActionRef = null;
        String fromOutcome = null;
        String toViewId = null;
        for (int i = 0; i < caseList.size(); i++) {
            ConfigNavigationCase cnc = (ConfigNavigationCase) caseList.get(i);
            fromActionRef = cnc.getFromActionRef();
            fromOutcome = cnc.getFromOutcome();
            toViewId = cnc.getToViewId();
            if ((fromActionRef != null) && (fromOutcome != null)) {
                if ((fromActionRef.equals(actionRef)) && (fromOutcome.equals(outcome))) {
                    return toViewId;
                }
            }
        }

        for (int i = 0; i < caseList.size(); i++) {
            ConfigNavigationCase cnc = (ConfigNavigationCase) caseList.get(i);
            fromActionRef = cnc.getFromActionRef();
            fromOutcome = cnc.getFromOutcome();
            toViewId = cnc.getToViewId();
            if ((fromActionRef == null) && (fromOutcome != null)) {
                if (fromOutcome.equals(outcome)) {
                    return toViewId;
                }
            }
        }

        for (int i = 0; i < caseList.size(); i++) {
            ConfigNavigationCase cnc = (ConfigNavigationCase) caseList.get(i);
            fromActionRef = cnc.getFromActionRef();
            fromOutcome = cnc.getFromOutcome();
            toViewId = cnc.getToViewId();
            if ((fromActionRef != null) && (fromOutcome == null)) {
                if (fromActionRef.equals(actionRef)) {
                    return toViewId;
                }
            }
        }

        for (int i = 0; i < caseList.size(); i++) {
            ConfigNavigationCase cnc = (ConfigNavigationCase) caseList.get(i);
            fromActionRef = cnc.getFromActionRef();
            fromOutcome = cnc.getFromOutcome();
            toViewId = cnc.getToViewId();
            if ((fromActionRef == null) && (fromOutcome == null)) {
                return toViewId;
            }
        }

        return returnViewId;
    }

    /**
     * This Comparator class will help sort the <code>ConfigNavigationCase</code> objects
     * based on their <code>fromViewId</code> properties in descending order -
     * largest string to smallest string.
     */
    class SortIt implements Comparator {
        public int compare(Object o1, Object o2) {
            String fromViewId1 = (String)o1;
            String fromViewId2 = (String)o2;
            return -(fromViewId1.compareTo(fromViewId2));
        }
    }


}

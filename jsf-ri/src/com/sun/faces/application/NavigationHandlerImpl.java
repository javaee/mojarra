/*
 * $Id: NavigationHandlerImpl.java,v 1.10 2003/08/19 19:31:03 rlubke Exp $
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
     * Overall Map containing <code>from-tree-id</code> key and <code>ArrayList</code>
     *  of <code>ConfigNavigationCase</code> objects for that key; 
     * The <code>from-tree-id</code> strings in this map will be stored as specified
     * in the configuration file - some of them will have a trailing asterisk "*"
     * signifying wild card, and some may be specified as an asterisk "*".
     */ 

    protected Map caseListMap = null;

    /**
     * The List that contains the <code>ConfigNavigationCase</code> objects for a
     * <code>from-tree-id</code>.
     */ 

    private List caseList = null;

    /**
     * The List that contains all tree identifier strings ending in an asterisk "*".
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
     * This method uses helper methods to set the new <code>tree</code> identifier;
     * If the new <code>tree</code> identifier could not be determined, then the original
     * <code>tree</code> identifier is left alone.
     *
     * @param context The Faces Context
     * @param actionRef The action reference string
     * @param outcome The outcome string
     */

    public void handleNavigation(FacesContext context, String actionRef, String outcome) {
        // PENDING (rlubke) PROVIDE IMPLEMENTATION
    }

    public void addNavigationCase(ConfigNavigationCase navigationCase) {

        String fromTreeId = navigationCase.getFromTreeId();
        if (fromTreeId == null) {
            fromTreeId = "*";
        }
        if (fromTreeId != null) {
            caseList = (List)caseListMap.get(fromTreeId);
            if (caseList == null) {
                caseList = new ArrayList();
                caseList.add(navigationCase);
                caseListMap.put(fromTreeId, caseList);
            } else {
                caseList.add(navigationCase);
            }
            if (fromTreeId.endsWith("*")) {
                fromTreeId = fromTreeId.substring(0,fromTreeId.lastIndexOf("*"));
                wildcardMatchList.add(fromTreeId);
            }
        }
    }
        
        
    /**
     * This method finds the List of cases for the current <code>tree</code> identifier.
     * After the cases are found, the <code>from-action-ref</code> and <code>from-outcome</code>
     * values are evaluated to determine the new <code>tree</code> identifier.
     * Refer to section 7.4.2 of the specification for more details.
     *
     * @param treeId  The current <code>tree</code> identifier.
     * @param actionRef The action reference string.
     * @param outcome The outcome string.
     *
     * @return The <code>tree</code> identifier.
     */

    private String findExactMatch(String treeId, String actionRef, String outcome) {
        String returnTreeId = null;

        Assert.assert_it(null != caseListMap);

        List caseList = (List)caseListMap.get(treeId);

        if (caseList == null) {
            return null;
        }

        // We've found an exact match for the treeId.  Now we need to evaluate
        // actionref/outcome in the following order:
        // 1) elements specifying both from-action-ref and from-outcome
        // 2) elements specifying only from-outcome
        // 3) elements specifying only from-action-ref
        // 4) elements where both from-action-ref and from-outcome are null


        returnTreeId = determineTreeFromActionRefOutcome(caseList, actionRef, outcome);

        return returnTreeId;
    }

    /**
     * This method traverses the wild card match List (containing <code>from-tree-id</code>
     * strings and finds the List of cases for each <code>from-tree-id</code> string.
     * Refer to section 7.4.2 of the specification for more details.
     *
     * @param treeId  The current <code>tree</code> identifier.
     * @param actionRef The action reference string.
     * @param outcome The outcome string.
     *
     * @return The <code>tree</code> identifier.
     */

    private String findWildCardMatch(String treeId, String actionRef, String outcome) {
        String returnTreeId = null;

        Assert.assert_it(null != wildcardMatchList);

	Iterator iter = wildcardMatchList.iterator();
	while (iter.hasNext()) {
	    String fromTreeId = (String)iter.next();  
	    
            // See if the entire wildcard string (without the trailing "*" is
            // contained in the incoming treeId.  Ex: /foobar is contained with /foobarbaz
            // If so, then we have found our largest pattern match..
            // If not, then continue on to the next case;

            if (treeId.indexOf(fromTreeId, 0) == -1) {
                continue;
            }

            // Append the trailing "*" so we can do our map lookup;

            String wcFromTreeId = fromTreeId + "*"; 
            List caseList = (List)caseListMap.get(wcFromTreeId);

            if (caseList == null) {
                return null;
            }

            // If we've found a match, then we need to evaluate actionref/outcome in the following
            // order:  1)elements specifying both from-action-ref and from-outcome
            // 2) elements specifying only from-outcome
            // 3) elements specifying only from-action-ref
            // 4) elements where both from-action-ref and from-outcome are null

            returnTreeId = determineTreeFromActionRefOutcome(caseList, actionRef, outcome);
            if (returnTreeId != null) {
                break;
            }
        } 
        return returnTreeId;
    }

    /**
     * This method will extract the cases for which a <code>from-tree-id</code> is
     * an asterisk "*".
     * Refer to section 7.4.2 of the specification for more details.
     *
     * @param actionRef The action reference string.
     * @param outcome The outcome string.
     *
     * @return The <code>tree</code> identifier.
     */

    private String findDefaultMatch(String actionRef, String outcome) {
        String returnTreeId = null;

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

        returnTreeId = determineTreeFromActionRefOutcome(caseList, actionRef, outcome);
        return returnTreeId;
    }
        
    /**
     * This method will attempt to find the <code>tree</code> identifier based on action reference
     * and outcome.  Refer to section 7.4.2 of the specification for more details.
     *
     * @param caseList The list of navigation cases.
     * @param actionRef The action reference string.
     * @param outcome The outcome string.
     *
     * @return The <code>tree</code> identifier.
     */
     
    private String determineTreeFromActionRefOutcome(List caseList, String actionRef, String outcome) {

        String returnTreeId = null;

        String fromActionRef = null;
        String fromOutcome = null;
        String toTreeId = null;
        for (int i = 0; i < caseList.size(); i++) {
            ConfigNavigationCase cnc = (ConfigNavigationCase) caseList.get(i);
            fromActionRef = cnc.getFromActionRef();
            fromOutcome = cnc.getFromOutcome();
            toTreeId = cnc.getToTreeId();
            if ((fromActionRef != null) && (fromOutcome != null)) {
                if ((fromActionRef.equals(actionRef)) && (fromOutcome.equals(outcome))) {
                    return toTreeId;
                }
            }
        }

        for (int i = 0; i < caseList.size(); i++) {
            ConfigNavigationCase cnc = (ConfigNavigationCase) caseList.get(i);
            fromActionRef = cnc.getFromActionRef();
            fromOutcome = cnc.getFromOutcome();
            toTreeId = cnc.getToTreeId();
            if ((fromActionRef == null) && (fromOutcome != null)) {
                if (fromOutcome.equals(outcome)) {
                    return toTreeId;
                }
            }
        }

        for (int i = 0; i < caseList.size(); i++) {
            ConfigNavigationCase cnc = (ConfigNavigationCase) caseList.get(i);
            fromActionRef = cnc.getFromActionRef();
            fromOutcome = cnc.getFromOutcome();
            toTreeId = cnc.getToTreeId();
            if ((fromActionRef != null) && (fromOutcome == null)) {
                if (fromActionRef.equals(actionRef)) {
                    return toTreeId;
                }
            }
        }

        for (int i = 0; i < caseList.size(); i++) {
            ConfigNavigationCase cnc = (ConfigNavigationCase) caseList.get(i);
            fromActionRef = cnc.getFromActionRef();
            fromOutcome = cnc.getFromOutcome();
            toTreeId = cnc.getToTreeId();
            if ((fromActionRef == null) && (fromOutcome == null)) {
                return toTreeId;
            }
        }

        return returnTreeId;
    }

    /**
     * This Comparator class will help sort the <code>ConfigNavigationCase</code> objects
     * based on their <code>fromTreeId</code> properties in descending order -
     * largest string to smallest string.
     */
    class SortIt implements Comparator {
        public int compare(Object o1, Object o2) {
            String fromTreeId1 = (String)o1;
            String fromTreeId2 = (String)o2;
            return -(fromTreeId1.compareTo(fromTreeId2));
        }
    }


}

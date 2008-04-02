/*
 * $Id: NavigationHandlerImpl.java,v 1.6 2003/05/08 23:13:29 rkitain Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.application;

import com.sun.faces.config.ConfigBase;
import com.sun.faces.config.ConfigNavigationCase;
import com.sun.faces.util.Util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.FactoryFinder;
import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;
import javax.faces.tree.TreeFactory;

import org.mozilla.util.Assert;

public class NavigationHandlerImpl extends NavigationHandler {

    //
    // Protected Constants
    //

    //
    // Class Variables
    //

    // Instance Variables

    private ConfigBase configBase = null;

    // original list
    private List navigationCases = null;

    // overall Map containing from-tree-id key and ArrayList of ConfigNavigationCase 
    // objects for that key; 
    //
    private Map caseListMap = null;

    // the ArrayList containing the ConfigNavigationCase objects for the from-tree-id
    //
    private List caseList = null;

    // wildcard list - contains tree id strings ending in "*"
    //
    private List wildcardMatchList= null;


    // Relationship Instance Variables

    //
    // Constructors and Initializers
    //

    /**
     * Constructor 
     */
    public NavigationHandlerImpl() {
        super();
    }

    // This method builds the search lists used to determine the
    // new tree identifier.

    public void initialize(ConfigBase configBase) {
        if (null == configBase) {
            throw new RuntimeException(Util.getExceptionMessage(Util.NULL_CONFIGURATION_ERROR_MESSAGE_ID));
        }        
        navigationCases = configBase.getNavigationCases();
        buildSearchLists();
    }

    // This method uses helper methods to set the new tree identifier.
    // If the new tree identifier could not be determined, then the original
    // tree identifier is left alone;
 
    public void handleNavigation(FacesContext context, String actionRef, String outcome) {


        String newTreeId = getTreeId(context, actionRef, outcome);

        if (newTreeId != null) {
            TreeFactory treeFactory = (TreeFactory)
                FactoryFinder.getFactory(FactoryFinder.TREE_FACTORY);
            Assert.assert_it(null != treeFactory);
            context.setTree(treeFactory.getTree(context, newTreeId)); 
        }
    }

    // This method uses helper methods to determine the new tree identifier:
    // 1. By performing an exact match search with the current tree identifier;
    // 2. If not found, then by searching any wild card patterns (ex: /log*)
    // 3. And finally, if still not found, the match is performed against any
    //    "from-tree-id" pattern of just an "*";
    // If nothing turns up, then the current tree identifier will not change; 
 
    private String getTreeId(FacesContext context, String actionRef, String outcome) {
        String nextTreeId = null;
        String treeId = context.getTree().getTreeId();

        nextTreeId = findExactMatch(treeId, actionRef, outcome);
        if (nextTreeId == null) {
            nextTreeId = findWildCardMatch(treeId, actionRef, outcome);
        }

        if (nextTreeId == null) {
             nextTreeId = findDefaultMatch(actionRef, outcome);
        }

        return nextTreeId;
    }

        
    // This method finds the List of cases for the current tree identifier;
    // After the cases are found, then "from-action-ref" and "from-outcome" values
    // are evaluated to determine the new tree identifier;  

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

    // This method traverses the wild card match List (containing "from-tree-id" strings)
    // and finds the List of cases for each "from-tree-id"; After the cases are found,
    // then "from-action-ref" and "from-outcome" values are evaluated to determine the
    // new tree identifier; 

    private String findWildCardMatch(String treeId, String actionRef, String outcome) {
        String returnTreeId = null;

        Assert.assert_it(null != wildcardMatchList);

        for (int i=0; i<wildcardMatchList.size(); i++) {
            String fromTreeId = (String)wildcardMatchList.get(i);

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

    // This method will check the default match list;  Entries in this list have
    // a fromTreeId of "*";
 
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
        
    // This method will attempt to find the tree identifier based on action reference
    // and outcome;

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

    // This method builds two search structures:
    //     o case list Map - A Map containing all the "from-tree-id" values as 
    //       the key, and a List of ConfigNavigationCase objects for that key; 
    //     o wild card match list - A List of "from-tree-id" strings whose trailing
    //       character is an "*"; The trailing "*" is removed just before it is added
    //       to the list, and the List is sorted in descending order (longest -> shortest).

    private void buildSearchLists() {
        caseListMap = new HashMap();
        wildcardMatchList = new ArrayList();

        for (int i=0; i<navigationCases.size(); i++) {
            ConfigNavigationCase cnc = (ConfigNavigationCase)navigationCases.get(i);
            String fromTreeId = cnc.getFromTreeId();
            if (fromTreeId == null) {
                fromTreeId = "*";
            }
            if (fromTreeId != null) {
                caseList = (List)caseListMap.get(fromTreeId);
                if (caseList == null) {
                    caseList = new ArrayList();
                    caseList.add(cnc);
                    caseListMap.put(fromTreeId, caseList);
                } else {
                    caseList.add(cnc);
                }
                if (fromTreeId.endsWith("*")) {
                    fromTreeId = fromTreeId.substring(0,fromTreeId.lastIndexOf("*"));
                    wildcardMatchList.add(fromTreeId);
                }
            }
        }

        // Sort the wildcard list in descending order
        // ex: /foobar.jsp*
        //     /foobar*
        //     /foob*

        Collections.sort(wildcardMatchList, new SortIt());
    }

    // This Comparator class will help sort the ConfigNavigationCase objects
    // based on their "fromTreeId" properties in descending order - 
    // largest string to smallest string.

    class SortIt implements Comparator {
        public int compare(Object o1, Object o2) {
            String fromTreeId1 = (String)o1;
            String fromTreeId2 = (String)o2;
            return -(fromTreeId1.compareTo(fromTreeId2));
        }
    }


}

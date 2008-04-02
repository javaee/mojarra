/*
 * $Id: NavigationHandlerImpl.java,v 1.5 2003/05/05 15:19:01 rkitain Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.application;

import com.sun.faces.RIConstants;
import com.sun.faces.config.ConfigBase;
import com.sun.faces.config.ConfigNavigationCase;
import com.sun.faces.util.Util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
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

    // exact match Map - this java.util.Map contains:
    // treeid (key) java.util.List of cases for that tree (value) 
    private Map exactMatchMap = null;
    private List exactMatchCaseList = null;

    // contains "*" tree id(s)
    private List defaultMatchList = null;

    // wildcard list - contains tree id(s) ending in "*"
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

    // This method uses the search algorithms to return the new tree identifier.
 
    private String getTreeId(FacesContext context, String actionRef, String outcome) {
        String nextTreeId = null;
        String treeId = context.getTree().getTreeId();

        nextTreeId = findExactMatch(treeId, actionRef, outcome);
        if (nextTreeId == null) {
            nextTreeId = findWildCardMatch(treeId, actionRef, outcome);
        }

        if (nextTreeId == null) {
             nextTreeId = findDefaultMatch(treeId, actionRef, outcome);
        }

        return nextTreeId;
    }

        
    // This method will search the exact match list.

    private String findExactMatch(String treeId, String actionRef, String outcome) {
        String returnTreeId = null;

        List caseList = (List)exactMatchMap.get(treeId);

        if (caseList == null) {
            return null;
        }

        // We've found an exact match for the treeId.  Now we need to evaluate
        // actionref/outcome in the following order:
        // 1) elements specifying both from-action-ref and from-outcome
        // 2) elements specifying only from-outcome
        // 3) elements specifying only from-action-ref
        // 4) elements where both from-action-ref and from-outcome are null

        String fromActionRef = null;
        String fromOutcome = null;
        String toTreeId = null;

        if (actionRef != null && outcome != null) {
            for (int i = 0; i < caseList.size(); i++) {
                ConfigNavigationCase cnc = (ConfigNavigationCase) caseList.get(i);
                fromActionRef = cnc.getFromActionRef();
                fromOutcome = cnc.getFromOutcome();
                toTreeId = cnc.getToTreeId();
                if (fromActionRef != null && fromOutcome != null) {
                    if (fromActionRef.equals(actionRef) && fromOutcome.equals(outcome)) {
                        return toTreeId;
                    }
                }
            }
        } 

        if (outcome != null) {
            for (int i = 0; i < caseList.size(); i++) {
                ConfigNavigationCase cnc = (ConfigNavigationCase) caseList.get(i);
                fromActionRef = cnc.getFromActionRef();
                fromOutcome = cnc.getFromOutcome();
                toTreeId = cnc.getToTreeId();

                if (fromOutcome != null) {
                    if (fromOutcome.equals(outcome)) {
                        return toTreeId;
                    }
                }
            }
        }

        if (actionRef != null) {
           for (int i = 0; i < caseList.size(); i++) {
               ConfigNavigationCase cnc = (ConfigNavigationCase) caseList.get(i);
               fromActionRef = cnc.getFromActionRef();
               fromOutcome = cnc.getFromOutcome();
               toTreeId = cnc.getToTreeId();
                if (fromActionRef != null) {
                    if (fromActionRef.equals(actionRef)) {
                        return toTreeId;
                    }
                }
            }
        }

        if (actionRef == null && outcome == null) {
            for (int i = 0; i < caseList.size(); i++) {
                ConfigNavigationCase cnc = (ConfigNavigationCase) caseList.get(i);
                fromActionRef = cnc.getFromActionRef();
                fromOutcome = cnc.getFromOutcome();
                toTreeId = cnc.getToTreeId();
                if (fromActionRef == null && fromOutcome == null) {
                    return toTreeId;
                }
            }
        }

        return returnTreeId;
    }

    // This method will search the wild card list .

    private String findWildCardMatch(String treeId, String actionRef, String outcome) {
        String returnTreeId = null;
        for (int i=0; i<wildcardMatchList.size(); i++) {
            ConfigNavigationCase cnc = (ConfigNavigationCase)wildcardMatchList.get(i);
            String fromTreeId = cnc.getFromTreeId();
            String fromActionRef = cnc.getFromActionRef();
            String fromOutcome = cnc.getFromOutcome();  
            String toTreeId = cnc.getToTreeId();

            // See if the entire wildcard string (without the trailing "*" is
            // contained in the incoming treeId.  Ex: /foobar is contained with /foobarbaz
            // If so, then we have found our largest pattern match..
            // If not, then continue on to the next case;

            if (treeId.indexOf(fromTreeId, 0) == -1) {
                continue;
            }

            // If we've found a match, then we need to evaluate actionref/outcome in the following
            // order:  1)elements specifying both from-action-ref and from-outcome
            // 2) elements specifying only from-outcome
            // 3) elements specifying only from-action-ref
            // 4) elements where both from-action-ref and from-outcome are null

            returnTreeId = determineTreeFromActionRefOutcome(actionRef, outcome,
                fromActionRef, fromOutcome, toTreeId);
            if (returnTreeId != null) {
                break;
            }
        } 
        return returnTreeId;
    }

    // This method will check the default match list;  Entries in this list have
    // a fromTreeId of "*";
 
    private String findDefaultMatch(String treeId, String actionRef, String outcome) {
        String returnTreeId = null;

        for (int i=0; i<defaultMatchList.size(); i++) {
            ConfigNavigationCase cnc = (ConfigNavigationCase)defaultMatchList.get(i);
            String fromTreeId = cnc.getFromTreeId();
            String fromActionRef = cnc.getFromActionRef();
            String fromOutcome = cnc.getFromOutcome();
            String toTreeId = cnc.getToTreeId();


            // We need to evaluate actionref/outcome in the follow
            // order:  1)elements specifying both from-action-ref and from-outcome
            // 2) elements specifying only from-outcome
            // 3) elements specifying only from-action-ref
            // 4) elements where both from-action-ref and from-outcome are null

            returnTreeId = determineTreeFromActionRefOutcome(actionRef, outcome,
                fromActionRef, fromOutcome, toTreeId);
            if (returnTreeId != null) {
                break;
            }
        }
        return returnTreeId;
    }
        
    // This method will attempt to find the tree identifier based on action reference
    // and outcome;

    private String determineTreeFromActionRefOutcome(String actionRef, String outcome,
        String fromActionRef, String fromOutcome, String toTreeId) {

        String returnTreeId = null;

        if (actionRef != null && outcome != null) {
            if (fromActionRef != null && fromOutcome != null) {
                if (fromActionRef.equals(actionRef) && fromOutcome.equals(outcome)) {
                    returnTreeId = toTreeId;
                }
            }
        }

        if (outcome != null) {
            if (fromOutcome != null) {
                if (fromOutcome.equals(outcome)) {
                    returnTreeId = toTreeId;
                }
            }
        }

        if (actionRef != null) {
            if (fromActionRef != null) {
                if (fromActionRef.equals(actionRef)) {
                    returnTreeId = toTreeId;
                }
            }
        }

        if (fromActionRef == null && fromOutcome == null) {
            returnTreeId = toTreeId;
        }

        return returnTreeId;
    }

    // This method builds three search structures:
    //     o exact match Map - the tree id(s) in this Map do not contain
    //       a trailing "*" (wildcard)  (ex: /login.jsp)  This Map contains
    //       <from-tree-id> as the key, and a List of cases for that tree
    //       id as the value;
    //     o wild card match list - the tree id(s) in this list do contain
    //       a trailing "*" (wildcard) (ex: /login*)
    //     o default match list - the tree id(s) in this list are "*"
    // If the tree id contains an ending "*" (wildcard), the trailing "*"
    // is removed, and the tree id is reset in the ConfigNavigationCase object
    // before it is added to the wild card match list; 
    // A null fromTreeId is treated as global ("*");

    private void buildSearchLists() {
        exactMatchMap = new HashMap();
        exactMatchCaseList = new ArrayList();
        defaultMatchList = new ArrayList();
        wildcardMatchList = new ArrayList();

        for (int i=0; i<navigationCases.size(); i++) {
            ConfigNavigationCase cnc = (ConfigNavigationCase)navigationCases.get(i);
            String fromTreeId = cnc.getFromTreeId();
            if (fromTreeId == null) {
                fromTreeId = "*";
            }
            if (fromTreeId != null) {
                if (fromTreeId.equals("*")) {
                    defaultMatchList.add(cnc);
                } else if (!fromTreeId.endsWith("*")) {
                    List caseList = (List)exactMatchMap.get(fromTreeId);
                    if (caseList == null) {
                        exactMatchCaseList = new ArrayList();
                        exactMatchCaseList.add(cnc);
                        exactMatchMap.put(fromTreeId, exactMatchCaseList);
                    } else {
                        caseList.add(cnc);
                    } 
                } else {
                    fromTreeId = fromTreeId.substring(0,fromTreeId.lastIndexOf("*"));
                    ConfigNavigationCase cnc1 = new ConfigNavigationCase();
                    cnc1.setFromTreeId(fromTreeId);
                    cnc1.setFromActionRef(cnc.getFromActionRef());
                    cnc1.setFromOutcome(cnc.getFromOutcome());
                    cnc1.setToTreeId(cnc.getToTreeId());
                    wildcardMatchList.add(cnc1);
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
            ConfigNavigationCase cnc1 = (ConfigNavigationCase)o1;
            ConfigNavigationCase cnc2 = (ConfigNavigationCase)o2;
            return -(cnc1.getFromTreeId().compareTo(cnc2.getFromTreeId()));
        }
    }


}

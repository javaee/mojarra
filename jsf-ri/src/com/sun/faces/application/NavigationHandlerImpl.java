/*
 * $Id: NavigationHandlerImpl.java,v 1.58 2008/01/31 18:36:00 rlubke Exp $
 */

/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 * 
 * Contributor(s):
 * 
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

import javax.faces.FacesException;
import javax.faces.application.NavigationCase;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sun.faces.util.MessageUtils;
import com.sun.faces.util.Util;
import com.sun.faces.util.FacesLogger;
import javax.faces.application.ConfigurableNavigationHandler;
import javax.faces.application.FacesMessage;

/**
 * <p><strong>NavigationHandlerImpl</strong> is the class that implements
 * default navigation handling. Refer to section 7.4.2 of the specification for
 * more details.
 * PENDING: Make independent of ApplicationAssociate. 
 */

public class NavigationHandlerImpl extends ConfigurableNavigationHandler {

    // Log instance for this class
    private static final Logger logger = FacesLogger.APPLICATION.getLogger();

    /**
     * <code>Map</code> containing configured navigation cases.
     */
    private Map<String, Set<NavigationCase>> navigationMap;

    /**
     * <code>Set</code> containing wildcard navigation cases.
     */
    private Set<String> wildCardSet;

    /**
     * Flag indicating navigation cases properly consumed and available.
     */
    private boolean navigationConfigured;

    /**
     * Flag indicated the current mode.
     */
    private boolean development;
    private static final Pattern REDIRECT_EQUALS_TRUE = Pattern.compile("(?:\\?|&)(faces-redirect=true(&|$))");


    // ------------------------------------------------------------ Constructors


    /**
     * This constructor uses the current <code>ApplicationAssociate</code>
     * instance to obtain the navigation mappings used to make
     * navigational decisions.
     */
    public NavigationHandlerImpl() {
        super();
        if (logger.isLoggable(Level.FINE)) {
            logger.log(Level.FINE, "Created NavigationHandler instance ");
        }
        ApplicationAssociate associate = ApplicationAssociate.getInstance(
              FacesContext.getCurrentInstance().getExternalContext());
        if (associate != null) {
            navigationMap = associate.getNavigationCaseListMappings();
            wildCardSet = associate.getNavigationWildCardList();
            navigationConfigured = (wildCardSet != null &&
                                    navigationMap != null);
            development = associate.isDevModeEnabled();
        }
    }


    // ------------------------------ Methods from ConfigurableNavigationHandler


    /**
     * @see javax.faces.application.ConfigurableNavigationHandler#getNavigationCase(javax.faces.context.FacesContext, String, String)
     */
    @Override
    public NavigationCase getNavigationCase(FacesContext context, String fromAction, String outcome) {
        NavigationCase result = null;
        CaseStruct caseStruct = getViewId(context, fromAction, outcome);
        if (null != caseStruct) {
            result = caseStruct.navCase;
        }
        
        return result;
    }


    /**
     * @see javax.faces.application.ConfigurableNavigationHandler#getNavigationCases()
     */
    @Override
    public Map<String, Set<NavigationCase>> getNavigationCases() {
        return navigationMap;
    }


    // ------------------------------------------ Methods from NavigationHandler
    

    /**
     * @see javax.faces.application.NavigationHandler#handleNavigation(javax.faces.context.FacesContext, String, String)
     */
    public void handleNavigation(FacesContext context,
                                 String fromAction,
                                 String outcome) {

        if (context == null) {
            String message = MessageUtils.getExceptionMessageString
                (MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID, "context");
            throw new NullPointerException(message);
        }

        CaseStruct caseStruct = getViewId(context, fromAction, outcome);
        if (caseStruct != null) {
            ExternalContext extContext = context.getExternalContext();
            ViewHandler viewHandler = Util.getViewHandler(context);
            assert (null != viewHandler);

            if (caseStruct.navCase.isRedirect()) {
                // perform a 302 redirect.
                String newPath =
                    viewHandler.getActionURL(context, caseStruct.viewId);
                try {
                    if (logger.isLoggable(Level.FINE)) {
                        logger.fine("Redirecting to path " + newPath
                                    + " for outcome " + outcome +
                                    "and viewId " + caseStruct.viewId);
                    }
                    // encode the redirect to ensure session state
                    // is maintained
                    context.getFlash().setRedirect(true);
                    extContext.redirect(extContext.encodeActionURL(newPath));
                } catch (java.io.IOException ioe) {
                    if (logger.isLoggable(Level.SEVERE)) {
                        logger.log(Level.SEVERE,"jsf.redirect_failed_error",
                                   newPath);
                    }
                    throw new FacesException(ioe.getMessage(), ioe);
                }
                context.responseComplete();
               if (logger.isLoggable(Level.FINE)) {
                   logger.fine("Response complete for " + caseStruct.viewId);
               }
            } else {
                UIViewRoot newRoot = viewHandler.createView(context,
                                                            caseStruct.viewId);
                context.setViewRoot(newRoot);
                if (logger.isLoggable(Level.FINE)) {
                    logger.fine("Set new view in FacesContext for " +
                                caseStruct.viewId);
                }
            }
        } 
    }


    // --------------------------------------------------------- Private Methods


    /**
     * This method uses helper methods to determine the new <code>view</code> identifier.
     * Refer to section 7.4.2 of the specification for more details.
     *
     * @param ctx the @{link FacesContext} for the current request
     * @param fromAction The action reference string
     * @param outcome    The outcome string
     * @return The <code>view</code> identifier.
     */
    private CaseStruct getViewId(FacesContext ctx,
                                 String fromAction,
                                 String outcome) {
        
        UIViewRoot root = ctx.getViewRoot();

        
        String viewId = (root != null ? root.getViewId() : null);
        
        // if viewIdToTest is not null, use its value to find
        // a navigation match, otherwise look for a match
        // based soley on the fromAction and outcome
        CaseStruct caseStruct = null;
        if (viewId != null) {
            caseStruct = findExactMatch(ctx, viewId, fromAction, outcome);

            if (caseStruct == null) {
                caseStruct = findWildCardMatch(ctx, viewId, fromAction, outcome);
            }
        }

        if (caseStruct == null) {
            caseStruct = findDefaultMatch(ctx, fromAction, outcome);
        }
        
        // If the navigation rules do not have a match...
        if (caseStruct == null && outcome != null) {
            caseStruct = findImplicitMatch(ctx, root, fromAction, outcome);
        }

        // no navigation case fo
        if (caseStruct == null && outcome != null && development) {
            String key;
            Object[] params;
            if (fromAction == null) {
                key = MessageUtils.NAVIGATION_NO_MATCHING_OUTCOME_ID;
                params = new Object[] { viewId, outcome };
            } else {
                key = MessageUtils.NAVIGATION_NO_MATCHING_OUTCOME_ACTION_ID;
                params = new Object[] { viewId, fromAction, outcome };
            }
            FacesMessage m = MessageUtils.getExceptionMessage(key, params);
            m.setSeverity(FacesMessage.SEVERITY_WARN);
            ctx.addMessage(null, m);
        }
        return caseStruct;
    }


    /**
     * This method finds the List of cases for the current <code>view</code> identifier.
     * After the cases are found, the <code>from-action</code> and <code>from-outcome</code>
     * values are evaluated to determine the new <code>view</code> identifier.
     * Refer to section 7.4.2 of the specification for more details.
     *
     * @param ctx the {@link FacesContext} for the current request
     * @param viewId     The current <code>view</code> identifier.
     * @param fromAction The action reference string.
     * @param outcome    The outcome string.
     * @return The <code>view</code> identifier.
     */
    private CaseStruct findExactMatch(FacesContext ctx,
                                      String viewId,
                                      String fromAction,
                                      String outcome) {

        // if the user has elected to replace the Application instance
        // entirely
        if (!navigationConfigured) {
            return null;
        }

        Set<NavigationCase> caseSet = navigationMap.get(viewId);

        if (caseSet == null) {
            return null;
        }

        // We've found an exact match for the viewIdToTest.  Now we need to evaluate
        // from-action/outcome in the following order:
        // 1) elements specifying both from-action and from-outcome
        // 2) elements specifying only from-outcome
        // 3) elements specifying only from-action
        // 4) elements where both from-action and from-outcome are null


        return determineViewFromActionOutcome(ctx, caseSet, fromAction, outcome);
    }


    /**
     * This method traverses the wild card match List (containing <code>from-view-id</code>
     * strings and finds the List of cases for each <code>from-view-id</code> string.
     * Refer to section 7.4.2 of the specification for more details.
     *
     * @param ctx the {@link FacesContext} for the current request
     * @param viewId     The current <code>view</code> identifier.
     * @param fromAction The action reference string.
     * @param outcome    The outcome string.
     * @return The <code>view</code> identifier.
     */

    private CaseStruct findWildCardMatch(FacesContext ctx,
                                         String viewId,
                                         String fromAction,
                                         String outcome) {
        CaseStruct result = null;

        // if the user has elected to replace the Application instance
        // entirely
        if (!navigationConfigured) {
            return null;
        }

        for (String fromViewId : wildCardSet) {
            // See if the entire wildcard string (without the trailing "*" is
            // contained in the incoming viewIdToTest.  
            // Ex: /foobar is contained with /foobarbaz
            // If so, then we have found our largest pattern match..
            // If not, then continue on to the next case;

            if (!viewId.startsWith(fromViewId)) {
                continue;
            }

            // Append the trailing "*" so we can do our map lookup;

            String wcFromViewId = new StringBuilder(32).append(fromViewId).append('*').toString();
            Set<NavigationCase> ccaseSet = navigationMap.get(wcFromViewId);

            if (ccaseSet == null) {
                return null;
            }

            // If we've found a match, then we need to evaluate
            // from-action/outcome in the following order:
            // 1) elements specifying both from-action and from-outcome
            // 2) elements specifying only from-outcome
            // 3) elements specifying only from-action
            // 4) elements where both from-action and from-outcome are null

            result = determineViewFromActionOutcome(ctx,
                                                    ccaseSet,
                                                    fromAction,
                                                    outcome);
            if (result != null) {
                break;
            }
        }
        return result;
    }


    /**
     * This method will extract the cases for which a <code>from-view-id</code> is
     * an asterisk "*".
     * Refer to section 7.4.2 of the specification for more details.
     *
     * @param ctx the {@link FacesContext} for the current request
     * @param fromAction The action reference string.
     * @param outcome    The outcome string.
     * @return The <code>view</code> identifier.
     */

    private CaseStruct findDefaultMatch(FacesContext ctx,
                                        String fromAction,
                                        String outcome) {
        // if the user has elected to replace the Application instance
        // entirely
        if (!navigationConfigured) {
            return null;
        }

        Set<NavigationCase> caseSet = navigationMap.get("*");

        if (caseSet == null) {
            return null;
        }

        // We need to evaluate from-action/outcome in the follow
        // order:  1)elements specifying both from-action and from-outcome
        // 2) elements specifying only from-outcome
        // 3) elements specifying only from-action
        // 4) elements where both from-action and from-outcome are null

        return determineViewFromActionOutcome(ctx, caseSet, fromAction, outcome);
    }


    /**
     * <p>
     * Create a navigation case based on content within the outcome.
     * </p>
     *
     * @param context the {@link FacesContext} for the current request
     * @param root the {@link UIViewRoot} for the current request
     * @param fromAction the navigation action
     * @param outcome the navigation outcome
     * @return a CaseStruct representing the the navigation result based
     *  on the provided input
     */
    private CaseStruct findImplicitMatch(FacesContext context,
                                         UIViewRoot root,
                                         String fromAction,
                                         String outcome) {

        // look for an implicit match.
        String viewIdToTest = outcome;
        String currentViewId = root.getViewId();
        boolean isRedirect = false;

        int questionMark = viewIdToTest.indexOf('?');
        String queryString = null;
        if (-1 != questionMark) {
            queryString = viewIdToTest.substring(questionMark);
            viewIdToTest = viewIdToTest.substring(0, questionMark);

            Matcher m = REDIRECT_EQUALS_TRUE.matcher(queryString);
            if (m.find()) {
                isRedirect = true;
                queryString = queryString.replace(m.group(1), "");
            }

            // clean off dust
            if (queryString.endsWith("&")) {
                queryString =
                      queryString.substring(0, queryString.length() - 1);
            }

        }

        // If the viewIdToTest needs an extension, take one from the currentViewId.
        if (viewIdToTest.lastIndexOf('.') == -1) {
            int idx = currentViewId.lastIndexOf('.');
            if (idx != -1) {
                viewIdToTest = viewIdToTest + currentViewId.substring(idx);
            }
        }

        if (!viewIdToTest.startsWith("/")) {
            int lastSlash = currentViewId.lastIndexOf("/");
            if (lastSlash != -1) {
                currentViewId = currentViewId.substring(0, lastSlash + 1);
                viewIdToTest = currentViewId + viewIdToTest;
            } else {
                viewIdToTest = "/" + viewIdToTest;
            }
        }

        ViewHandler viewHandler = Util.getViewHandler(context);
        try {
            viewIdToTest = viewHandler.deriveViewId(context, viewIdToTest);
        } catch (UnsupportedOperationException e) {
            viewIdToTest = Util.deriveViewId(context, viewIdToTest);
        }

        if (null != viewIdToTest) {
            CaseStruct caseStruct = new CaseStruct();
            if (isRedirect && queryString != null) {
                // Tack back on the query string
                viewIdToTest = viewIdToTest + queryString;
            }
            caseStruct.viewId = viewIdToTest;
            caseStruct.navCase = new NavigationCase(currentViewId,
                    fromAction,
                    outcome,
                    null,
                    viewIdToTest,
                    isRedirect);
            return caseStruct;
        }

        return null;

    }


    /**
     * This method will attempt to find the <code>view</code> identifier based on action reference
     * and outcome.  Refer to section 7.4.2 of the specification for more details.
     * @param ctx the {@link FacesContext} for the current request
     * @param caseSet   The list of navigation cases.
     * @param fromAction The action reference string.
     * @param outcome    The outcome string.
     * @return The <code>view</code> identifier.
     */
    private CaseStruct determineViewFromActionOutcome(FacesContext ctx,
                                                      Set<NavigationCase> caseSet,
                                                      String fromAction,
                                                      String outcome) {

        CaseStruct result = new CaseStruct();
        boolean match = false;
        for (NavigationCase cnc : caseSet) {
            String cncFromAction = cnc.getFromAction();
            String cncFromOutcome = cnc.getFromOutcome();
            boolean cncHasCondition = cnc.hasCondition();
            String cncToViewId = cnc.getToViewId(ctx);
            if (cncToViewId.charAt(0) != '/') {
                if (logger.isLoggable(Level.WARNING)) {
                    logger.log(Level.WARNING,
                        "jsf.config.navigation.to_view_id_leading_slash",
                        new String[] { cncToViewId,
                                       cnc.getFromViewId() });
                }
                cncToViewId = '/' + cncToViewId;
            }
           
            if ((cncFromAction != null) && (cncFromOutcome != null)) {
                if ((cncFromAction.equals(fromAction)) &&
                    (cncFromOutcome.equals(outcome))) {
                    result.viewId = cncToViewId;
                    result.navCase = cnc;
                    match = true;
                }
            } else if ((cncFromAction == null) && (cncFromOutcome != null)) {
                if (cncFromOutcome.equals(outcome)) {
                    result.viewId = cncToViewId;
                    result.navCase = cnc;
                    match = true;
                }
            } else if ((cncFromAction != null) && (cncFromOutcome == null)) {
                if (cncFromAction.equals(fromAction) && (outcome != null || cncHasCondition)) {
                    result.viewId = cncToViewId;
                    result.navCase = cnc;
                    match = true;
                }
            } else if ((cncFromAction == null) && (cncFromOutcome == null)) {
                if (outcome != null || cncHasCondition) {
                    result.viewId = cncToViewId;
                    result.navCase = cnc;
                    match = true;
                }
            }

            if (match) {
                if (cncHasCondition && Boolean.FALSE.equals(cnc.getCondition(ctx))) {
                    match = false;
                } else {
                    return result;
                }
            }
        }

        return null;
    }


    private static class CaseStruct {
        String viewId;
        NavigationCase navCase;
    }

}

/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2012 Oracle and/or its affiliates. All rights reserved.
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

import javax.faces.FacesException;
import javax.faces.application.NavigationCase;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.PartialViewContext;

import java.util.*;
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
    private volatile NavigationMap navigationMap;


    /**
     * Flag indicated the current mode.
     */
    private boolean development;
    private static final Pattern REDIRECT_EQUALS_TRUE = Pattern.compile("(.*)(faces-redirect=true)(.*)");
    private static final Pattern INCLUDE_VIEW_PARAMS_EQUALS_TRUE = Pattern.compile("(.*)(includeViewParams=true)(.*)");


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
            development = associate.isDevModeEnabled();
        }

    }


    // ------------------------------ Methods from ConfigurableNavigationHandler


    /**
     * @see javax.faces.application.ConfigurableNavigationHandler#getNavigationCase(javax.faces.context.FacesContext, String, String)
     */
    @Override
    public NavigationCase getNavigationCase(FacesContext context, String fromAction, String outcome) {

        Util.notNull("context", context);
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

        if (navigationMap == null) {
            navigationMap = new NavigationMap();
        }
        return navigationMap;

    }


    // ------------------------------------------ Methods from NavigationHandler
    

    /**
     * @see javax.faces.application.NavigationHandler#handleNavigation(javax.faces.context.FacesContext, String, String)
     */
    public void handleNavigation(FacesContext context,
                                 String fromAction,
                                 String outcome) {

        Util.notNull("context", context);

        CaseStruct caseStruct = getViewId(context, fromAction, outcome);
        if (caseStruct != null) {
            ExternalContext extContext = context.getExternalContext();
            ViewHandler viewHandler = Util.getViewHandler(context);
            assert (null != viewHandler);
            if (caseStruct.navCase.isRedirect()) {
                // perform a 302 redirect.
                String redirectUrl =
                      viewHandler.getRedirectURL(context,
                                                 caseStruct.viewId,
                                                 SharedUtils.evaluateExpressions(context, caseStruct.navCase.getParameters()),
                                                 caseStruct.navCase.isIncludeViewParams());
                try {
                    if (logger.isLoggable(Level.FINE)) {
                        logger.fine("Redirecting to path " + redirectUrl
                                    + " for outcome " + outcome +
                                    "and viewId " + caseStruct.viewId);
                    }
                    // encode the redirect to ensure session state
                    // is maintained
                    clearViewMapIfNecessary(context.getViewRoot(), caseStruct.viewId);
                    updateRenderTargets(context, caseStruct.viewId);
                    extContext.getFlash().setRedirect(true);
                    extContext.redirect(redirectUrl);
                } catch (java.io.IOException ioe) {
                    if (logger.isLoggable(Level.FINE)) {
                        logger.log(Level.FINE,"jsf.redirect_failed_error",
                                   redirectUrl);
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
                updateRenderTargets(context, caseStruct.viewId);
                context.setViewRoot(newRoot);
                if (logger.isLoggable(Level.FINE)) {
                    logger.fine("Set new view in FacesContext for " +
                                caseStruct.viewId);
                }
            }
        } 
    }

    // --------------------------------------------------------- Private Methods

    private void initializeNavigationFromAssociate() {

        ApplicationAssociate associate = ApplicationAssociate.getCurrentInstance();
        if (associate != null) {
            Map<String,Set<NavigationCase>> m = associate.getNavigationCaseListMappings();
            navigationMap = new NavigationMap();
            if (m != null) {
                navigationMap.putAll(m);
            }
        }

    }

    /**
     * Calls <code>clear()</code> on the ViewMap (if available) if the view
     * ID of the UIViewRoot differs from <code>newId</code>
     */
    private void clearViewMapIfNecessary(UIViewRoot root, String newId) {

        if (root != null && !root.getViewId().equals(newId)) {
            Map<String, Object> viewMap = root.getViewMap(false);
            if (viewMap != null) {
                viewMap.clear();
            }
        }

    }


    private void updateRenderTargets(FacesContext ctx, String newId) {

        if (ctx.getViewRoot() == null || !ctx.getViewRoot().getViewId().equals(newId)) {
            PartialViewContext pctx = ctx.getPartialViewContext();
            if (!pctx.isRenderAll()) {
                pctx.setRenderAll(true);
            }
        }

    }


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

        if (navigationMap == null) {
            synchronized (this) {
                initializeNavigationFromAssociate();
            }
        }

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
        if (caseStruct == null && outcome != null && viewId != null) {
            // Treat empty string equivalent to null outcome.  JSF 2.0 Rev a
            // Changelog issue C063.
            if (caseStruct == null && 0 == outcome.length()) {
                outcome = null;
            } else {
                caseStruct = findImplicitMatch(ctx, viewId, fromAction, outcome);
            }
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

        for (String fromViewId : navigationMap.wildcardMatchList) {
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
     * @param viewId of the {@link UIViewRoot} for the current request
     * @param fromAction the navigation action
     * @param outcome the navigation outcome
     * @return a CaseStruct representing the the navigation result based
     *  on the provided input
     */
    private CaseStruct findImplicitMatch(FacesContext context,
                                         String viewId,
                                         String fromAction,
                                         String outcome) {

        // look for an implicit match.
        String viewIdToTest = outcome;
        String currentViewId = viewId;
        Map<String, List<String>> parameters = null;
        boolean isRedirect = false;
        boolean isIncludeViewParams = false;

        int questionMark = viewIdToTest.indexOf('?');
        String queryString;
        if (-1 != questionMark) {
            int viewIdLen = viewIdToTest.length();
            if (viewIdLen <= (questionMark+1)) {
                if (logger.isLoggable(Level.SEVERE)) {
                    logger.log(Level.SEVERE, "jsf.navigation_invalid_query_string",
                            viewIdToTest);
                }
                if (development) {
                    String key;
                    Object[] params;
                    key = MessageUtils.NAVIGATION_INVALID_QUERY_STRING_ID;
                    params = new Object[]{viewIdToTest};
                    FacesMessage m = MessageUtils.getExceptionMessage(key, params);
                    m.setSeverity(FacesMessage.SEVERITY_WARN);
                    context.addMessage(null, m);
                }
                queryString = null;
                viewIdToTest = viewIdToTest.substring(0, questionMark);
            } else {
                queryString = viewIdToTest.substring(questionMark + 1);
                viewIdToTest = viewIdToTest.substring(0, questionMark);

                Matcher m = REDIRECT_EQUALS_TRUE.matcher(queryString);
                if (m.find()) {
                    isRedirect = true;
                    queryString = queryString.replace(m.group(2), "");
                }
                m = INCLUDE_VIEW_PARAMS_EQUALS_TRUE.matcher(queryString);
                if (m.find()) {
                    isIncludeViewParams = true;
                    queryString = queryString.replace(m.group(2), "");
                }
            }

            if (queryString != null && queryString.length() > 0) {
                Map<String, Object> appMap = context.getExternalContext().getApplicationMap();

                String[] queryElements = Util.split(appMap, queryString, "&amp;|&");
                for (int i = 0, len = queryElements.length; i < len; i ++) {
                    String[] elements = Util.split(appMap, queryElements[i], "=");
                    if (elements.length == 2) {
                        if (parameters == null) {
                            parameters = new LinkedHashMap<String,List<String>>(len / 2, 1.0f);
                            List<String> values = new ArrayList<String>(2);
                            values.add(elements[1]);
                            parameters.put(elements[0], values);
                        } else {
                            List<String> values = parameters.get(elements[0]);
                            if (values == null) {
                                values = new ArrayList<String>(2);
                                parameters.put(elements[0], values);
                            }
                            values.add(elements[1]);
                        }
                    }
                }
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
        viewIdToTest = viewHandler.deriveViewId(context, viewIdToTest);

        if (null != viewIdToTest) {
            CaseStruct caseStruct = new CaseStruct();
            caseStruct.viewId = viewIdToTest;
            caseStruct.navCase = new NavigationCase(currentViewId,
                                                    fromAction,
                                                    outcome,
                                                    null,
                                                    viewIdToTest,
                                                    parameters,
                                                    isRedirect,
                                                    isIncludeViewParams);
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


    // ---------------------------------------------------------- Nested Classes


    private static class CaseStruct {
        String viewId;
        NavigationCase navCase;
    }


    private static final class NavigationMap extends AbstractMap<String,Set<NavigationCase>> {

        private HashMap<String,Set<NavigationCase>> navigationMap =
              new HashMap<String,Set<NavigationCase>>();
        private TreeSet<String> wildcardMatchList =
              new TreeSet<String>(new Comparator<String>() {
                  public int compare(String fromViewId1, String fromViewId2) {
                      return -(fromViewId1.compareTo(fromViewId2));
                  }
              });


        // ---------------------------------------------------- Methods from Map


        @Override
        public int size() {
            return navigationMap.size();
        }


        @Override
        public boolean isEmpty() {
            return navigationMap.isEmpty();
        }

        
        @Override
        public Set<NavigationCase> put(String key, Set<NavigationCase> value) {
            if (key == null) {
                throw new IllegalArgumentException(key);
            }
            if (value == null) {
                throw new IllegalArgumentException();
            }
            updateWildcards(key);
            Set<NavigationCase> existing = navigationMap.get(key);
            if (existing == null) {
                navigationMap.put(key, value);
                return null;
            } else {
                existing.addAll(value);
                return existing;
            }

        }

        @Override
        public void putAll(Map<? extends String, ? extends Set<NavigationCase>> m) {
            if (m == null) {
                return;
            }
            for (Map.Entry<? extends String, ? extends Set<NavigationCase>> entry : m.entrySet()) {
                String key = entry.getKey();
                updateWildcards(key);
                Set<NavigationCase> existing = navigationMap.get(key);
                if (existing == null) {
                    navigationMap.put(key, entry.getValue());
                } else {
                    existing.addAll(entry.getValue());
                }
            }
        }


        @Override
        public Set<String> keySet() {
            return new AbstractSet<String>() {

                public Iterator<String> iterator() {
                    return new Iterator<String>() {

                        Iterator<Map.Entry<String,Set<NavigationCase>>> i = entrySet().iterator();

                        public boolean hasNext() {
                            return i.hasNext();
                        }

                        public String next() {
                            return i.next().getKey();
                        }

                        public void remove() {
                            throw new UnsupportedOperationException();
                        }
                    };
                }

                public int size() {
                    return NavigationMap.this.size();
                }
            };
        }

        @Override
        public Collection<Set<NavigationCase>> values() {
            return new AbstractCollection<Set<NavigationCase>>() {

                public Iterator<Set<NavigationCase>> iterator() {
                    return new Iterator<Set<NavigationCase>>() {

                        Iterator<Map.Entry<String,Set<NavigationCase>>> i = entrySet().iterator();

                        public boolean hasNext() {
                            return i.hasNext();
                        }

                        public Set<NavigationCase> next() {
                            return i.next().getValue();
                        }

                        public void remove() {
                            throw new UnsupportedOperationException();
                        }
                    };
                }

                public int size() {
                    return NavigationMap.this.size();
                }
            };
        }

        public Set<Entry<String, Set<NavigationCase>>> entrySet() {
            return new AbstractSet<Entry<String, Set<NavigationCase>>>() {

                public Iterator<Entry<String, Set<NavigationCase>>> iterator() {

                    return new Iterator<Entry<String,Set<NavigationCase>>>() {

                        Iterator<Entry<String, Set<NavigationCase>>> i =
                              navigationMap.entrySet().iterator();

                        public boolean hasNext() {
                            return i.hasNext();
                        }

                        public Entry<String, Set<NavigationCase>> next() {
                            return i.next();
                        }

                        public void remove() {
                            throw new UnsupportedOperationException();
                        }
                    };
                }

                public int size() {
                    return NavigationMap.this.size();
                }
            };
        }


        // ----------------------------------------------------- Private Methods

        private void updateWildcards(String fromViewId) {

            if (!navigationMap.containsKey(fromViewId)) {
                if (fromViewId.endsWith("*")) {
                    wildcardMatchList.add(fromViewId.substring(0, fromViewId.lastIndexOf('*')));
                }
            }
            
        }

    }
}

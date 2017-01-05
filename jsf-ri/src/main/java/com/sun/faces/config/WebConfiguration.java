/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2016 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.java.net/public/CDDL+GPL_1_1.html
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

package com.sun.faces.config;

import static com.sun.faces.config.WebConfiguration.WebContextInitParameter.DefaultSuffix;
import static com.sun.faces.config.WebConfiguration.WebContextInitParameter.FaceletsSuffix;
import static java.util.Arrays.asList;
import static java.util.logging.Level.FINE;
import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static java.util.regex.Pattern.compile;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import javax.faces.FactoryFinder;
import javax.faces.application.ProjectStage;
import javax.faces.application.ResourceHandler;
import javax.faces.application.StateManager;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIInput;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.event.PhaseListener;
import javax.faces.lifecycle.ClientWindow;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.LifecycleFactory;
import javax.faces.push.PushContext;
import javax.faces.validator.BeanValidator;
import javax.faces.view.facelets.ResourceResolver;
import javax.faces.webapp.FacesServlet;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;

import com.sun.faces.application.ApplicationAssociate;
import com.sun.faces.application.view.FaceletViewHandlingStrategy;
import com.sun.faces.facelets.util.Classpath;
import com.sun.faces.lifecycle.HttpMethodRestrictionsPhaseListener;
import com.sun.faces.util.FacesLogger;
import com.sun.faces.util.Util;

/** Class Documentation */
public class WebConfiguration {

    // Log instance for this class
    private static final Logger LOGGER = FacesLogger.CONFIG.getLogger();

    // A Simple regular expression of allowable boolean values
    private static final Pattern ALLOWABLE_BOOLEANS = compile("true|false", CASE_INSENSITIVE);

    // Key under which we store our WebConfiguration instance.
    private static final String WEB_CONFIG_KEY =
          "com.sun.faces.config.WebConfiguration";

    public static final String META_INF_CONTRACTS_DIR = "META-INF" +
            WebContextInitParameter.WebAppContractsDirectory.getDefaultValue();

    private static final int META_INF_CONTRACTS_DIR_LEN = META_INF_CONTRACTS_DIR.length();

    private static final String RESOURCE_CONTRACT_SUFFIX = "/" + ResourceHandler.RESOURCE_CONTRACT_XML;

    // Logging level.  Defaults to FINE
    private Level loggingLevel = Level.FINE;

    private Map<BooleanWebContextInitParameter, Boolean> booleanContextParameters =
          new EnumMap<>(BooleanWebContextInitParameter.class);

    private Map<WebContextInitParameter, String> contextParameters =
          new EnumMap<>(WebContextInitParameter.class);

    private Map<WebContextInitParameter, Map<String, String>> facesConfigParameters =
          new EnumMap<>(WebContextInitParameter.class);

    private Map<WebEnvironmentEntry, String> envEntries =
          new EnumMap<>(WebEnvironmentEntry.class);

    private Map<WebContextInitParameter, String []> cachedListParams;

    private Set<String> setParams = new HashSet<>();

    private ServletContext servletContext;

    private ArrayList<DeferredLoggingAction> deferredLoggingActions;

    private FaceletsConfiguration faceletsConfig;

    private boolean hasFlows;


    // ------------------------------------------------------------ Constructors


    private WebConfiguration(ServletContext servletContext) {

        this.servletContext = servletContext;

        String contextName = getServletContextName();

        initSetList(servletContext);
        processBooleanParameters(servletContext, contextName);
        processInitParameters(servletContext, contextName);
        if (canProcessJndiEntries()) {
            processJndiEntries(contextName);
        }

        // build the cache of list type params
        cachedListParams = new HashMap<>(3);
        getOptionValue(WebContextInitParameter.ResourceExcludes, " ");
        getOptionValue(WebContextInitParameter.DefaultSuffix, " ");
        getOptionValue(WebContextInitParameter.FaceletsViewMappings, ";");
        getOptionValue(WebContextInitParameter.FaceletsSuffix, " ");
    }


    // ---------------------------------------------------------- Public Methods


    /**
     * Return the WebConfiguration instance for this application passing
     * the result of FacesContext.getCurrentInstance().getExternalContext()
     * to {@link #getInstance(javax.faces.context.ExternalContext)}.
     * @return the WebConfiguration for this application or <code>null</code>
     *  if no FacesContext is available.
     */
    public static WebConfiguration getInstance() {
        return getInstance(FacesContext.getCurrentInstance().getExternalContext());
    }

    /**
     * Return the WebConfiguration instance for this application.
     * @param extContext the ExternalContext for this request
     * @return the WebConfiguration for this application
     */
    public static WebConfiguration getInstance(ExternalContext extContext) {

        WebConfiguration config = (WebConfiguration) extContext.getApplicationMap().get(WEB_CONFIG_KEY);
        if (config == null) {
            return getInstance((ServletContext) extContext.getContext());
        } else {
            return config;
        }
    }

    /**
     * Return the WebConfiguration instance for this application.
     * @param servletContext the ServletContext
     * @return the WebConfiguration for this application or <code>null</code>
     *  if no WebConfiguration could be located
     */
    public static WebConfiguration getInstance(ServletContext servletContext) {

        WebConfiguration webConfig = (WebConfiguration) servletContext.getAttribute(WEB_CONFIG_KEY);

        if (webConfig == null) {
            webConfig = new WebConfiguration(servletContext);
            servletContext.setAttribute(WEB_CONFIG_KEY, webConfig);
        }
        
        return webConfig;
    }

    public static WebConfiguration getInstanceWithoutCreating(ServletContext servletContext) {
        return (WebConfiguration) servletContext.getAttribute(WEB_CONFIG_KEY);
    }

    /**
     * @return The <code>ServletContext</code> originally used to construct
     * this WebConfiguration instance
     */
    public ServletContext getServletContext() {
        return servletContext;
    }

    public boolean isHasFlows() {
        return hasFlows;
    }

    public void setHasFlows(boolean hasFlows) {
        this.hasFlows = hasFlows;
    }

    /**
     * Obtain the value of the specified boolean parameter
     * @param param the parameter of interest
     * @return the value of the specified boolean parameter
     */
    public boolean isOptionEnabled(BooleanWebContextInitParameter param) {

        if (booleanContextParameters.get(param) != null) {
            return booleanContextParameters.get(param);
        } else {
            return param.getDefaultValue();
        }
    }

    /**
     * Obtain the value of the specified parameter
     * @param param the parameter of interest
     * @return the value of the specified parameter
     */
    public String getOptionValue(WebContextInitParameter param) {
        String result = contextParameters.get(param);

        if (result == null) {
            WebContextInitParameter alternate = param.getAlternate();
            if (alternate != null) {
                result = contextParameters.get(alternate);
            }
        }

        return result;
    }

    public void setOptionValue(WebContextInitParameter param, String value) {
        contextParameters.put(param, value);
    }

    public void setOptionEnabled(BooleanWebContextInitParameter param, boolean value) {
        booleanContextParameters.put(param, value);
    }

    public FaceletsConfiguration getFaceletsConfiguration() {

        if (faceletsConfig == null) {
            faceletsConfig = new FaceletsConfiguration(this);
        }
        
        return faceletsConfig;
    }

    public Map<String, String> getFacesConfigOptionValue(WebContextInitParameter param, boolean create) {

        assert(facesConfigParameters != null);

        Map<String, String> result = facesConfigParameters.get(param);
        if (result == null) {
            if (create) {
                result = new ConcurrentHashMap<>(3);
                facesConfigParameters.put(param, result);
            } else {
                result = Collections.emptyMap();
            }
        }

        return result;
    }

    public Map<String, String> getFacesConfigOptionValue(WebContextInitParameter param) {
        return getFacesConfigOptionValue(param, false);
    }

    public String[] getOptionValue(WebContextInitParameter param, String sep) {
        String [] result;

        assert(cachedListParams != null);
        
        if ((result = cachedListParams.get(param)) == null) {
            String value = getOptionValue(param);
            if (value == null) {
                result = new String[0];
            } else {
                Map<String, Object> appMap = FacesContext.getCurrentInstance().getExternalContext().getApplicationMap();
                result = Util.split(appMap, value, sep);
            }
            cachedListParams.put(param, result);
        }

        return result;
    }

    /**
     * Obtain the value of the specified env-entry
     * @param entry the env-entry of interest
     * @return the value of the specified env-entry
     */
    public String getEnvironmentEntry(WebEnvironmentEntry entry) {
        return envEntries.get(entry);
    }

    /**
     * @param param the init parameter of interest
     * @return <code>true</code> if the parameter was explicitly set,
     *  otherwise, <code>false</code>
     */
    public boolean isSet(WebContextInitParameter param) {
        return isSet(param.getQualifiedName());
    }

    /**
     * @param param the init parameter of interest
     * @return <code>true</code> if the parameter was explicitly set,
     *  otherwise, <code>false</code>
     */
    public boolean isSet(BooleanWebContextInitParameter param) {
        return isSet(param.getQualifiedName());
    }

    /**
     * @return the name of this application
     */
    public String getServletContextName() {

        if (servletContext.getMajorVersion() == 2 && servletContext.getMinorVersion() <= 4) {
            return servletContext.getServletContextName();
        } 
            
        return servletContext.getContextPath();
    }


    public void overrideContextInitParameter(BooleanWebContextInitParameter param, boolean value) {

        if (param == null) {
            return;
        }
        
        boolean oldVal = booleanContextParameters.put(param, value);
        if (LOGGER.isLoggable(FINE) && oldVal != value) {
            LOGGER.log(FINE,
                       "Overriding init parameter {0}.  Changing from {1} to {2}.",
                       new Object[] { param.getQualifiedName(), oldVal, value});
        }

    }
      /**
       * To include the facelets suffix into the supported suffixes.
       *
       * @return merged suffixes including both default suffixes and the facelet suffixes.
       */
      public String[] getConfiguredExtensions() {
          String[] defaultSuffix  = getOptionValue(DefaultSuffix, " ");
          String[] faceletsSuffix = getOptionValue(FaceletsSuffix, " ");

          List<String> mergedList = new ArrayList<>(asList(defaultSuffix));
          mergedList.addAll(asList(faceletsSuffix));

          return mergedList.toArray(new String[0]);
    }

    public void overrideContextInitParameter(WebContextInitParameter param, String value) {

        if (param == null || value == null || value.length() == 0) {
            return;
        }
        
        value = value.trim();
        String oldVal = contextParameters.put(param, value);
        cachedListParams.remove(param);
        if (oldVal != null && LOGGER.isLoggable(FINE) && !(oldVal.equals(value))) {
            LOGGER.log(FINE,
                "Overriding init parameter {0}.  Changing from {1} to {2}.",
                new Object[]{param.getQualifiedName(), oldVal, value});
        }
    }

    public void doPostBringupActions() {

        if (deferredLoggingActions != null) {
            for (DeferredLoggingAction loggingAction : deferredLoggingActions) {
                loggingAction.log();
            }
        }

        // add the HttpMethodRestrictionPhaseListener if the parameter is enabled.
        boolean enabled = this.isOptionEnabled(BooleanWebContextInitParameter.EnableHttpMethodRestrictionPhaseListener);
        if (enabled) {
            LifecycleFactory factory = (LifecycleFactory) FactoryFinder.getFactory(FactoryFinder.LIFECYCLE_FACTORY);
            Iterator<String> ids = factory.getLifecycleIds();
            PhaseListener listener = null;
            Lifecycle cur;

            while (ids.hasNext()) {
                cur = factory.getLifecycle(ids.next());
                boolean foundExistingListenerInstance = false;
                for (PhaseListener curListener : cur.getPhaseListeners()) {
                    if (curListener instanceof HttpMethodRestrictionsPhaseListener) {
                        foundExistingListenerInstance = true;
                        break;
                    }
                }
                if (!foundExistingListenerInstance) {
                    if (null == listener) {
                        listener = new HttpMethodRestrictionsPhaseListener();
                    }
                    cur.addPhaseListener(listener);
                }
            }
        }

        discoverResourceLibraryContracts();

    }

    private void discoverResourceLibraryContracts() {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext extContex = context.getExternalContext();
        Set<String> foundContracts = new HashSet<>();
        Set<String> candidates;

        // Scan for "contractMappings" in the web app root
        String contractsDirName = getOptionValue(WebContextInitParameter.WebAppContractsDirectory);
        assert(null != contractsDirName);
        candidates = extContex.getResourcePaths(contractsDirName);
        if (null != candidates) {
            int contractsDirNameLen = contractsDirName.length();
            int end;
            for (String cur : candidates) {
                end = cur.length();
                if (cur.endsWith("/")) {
                    end--;
                }
                foundContracts.add(cur.substring(contractsDirNameLen + 1, end));
            }
        }

        // Scan for "META-INF" contractMappings in the classpath
        try {
            URL[] candidateURLs = Classpath.search(Util.getCurrentLoader(this),
                                          META_INF_CONTRACTS_DIR,
                                          RESOURCE_CONTRACT_SUFFIX,
                    Classpath.SearchAdvice.AllMatches);
            for (URL curURL : candidateURLs) {
                String cur = curURL.toExternalForm();

                int i = cur.indexOf(META_INF_CONTRACTS_DIR) + META_INF_CONTRACTS_DIR_LEN + 1;
                int j = cur.indexOf(RESOURCE_CONTRACT_SUFFIX);
                if (i < j) {
                    foundContracts.add(cur.substring(i,j));
                }

            }
        } catch (IOException ioe) {
            if (LOGGER.isLoggable(Level.FINEST)) {
                LOGGER.log(Level.FINEST, "Unable to scan " + META_INF_CONTRACTS_DIR, ioe);
            }
        }


        if (foundContracts.isEmpty()) {
            return;
        }

        Map<String, List<String>> contractMappings = new HashMap<>();

        ApplicationAssociate associate = ApplicationAssociate.getCurrentInstance();
        Map<String, List<String>> contractsFromConfig = associate.getResourceLibraryContracts();
        List<String> contractsToExpose;

        if (null != contractsFromConfig && !contractsFromConfig.isEmpty()) {
            List<String> contractsFromMapping;
            for (Map.Entry<String, List<String>> cur : contractsFromConfig.entrySet()) {
                // Verify that the contractsToExpose in this mapping actually exist
                // in the application.  If not, log a message.
                contractsFromMapping = cur.getValue();
                if (null == contractsFromMapping || contractsFromMapping.isEmpty()) {
                    if (LOGGER.isLoggable(Level.CONFIG)) {
                        LOGGER.log(Level.CONFIG, "resource library contract mapping for pattern {0} has no contracts.", cur.getKey());
                    }
                } else {
                    contractsToExpose = new ArrayList<>();
                    for (String curContractFromMapping : contractsFromMapping) {
                        if (foundContracts.contains(curContractFromMapping)) {
                            contractsToExpose.add(curContractFromMapping);
                        } else {
                            if (LOGGER.isLoggable(Level.CONFIG)) {
                                LOGGER.log(Level.CONFIG, "resource library contract mapping for pattern {0} exposes contract {1}, but that contract is not available to the application.",
                                        new String [] { cur.getKey(), curContractFromMapping });
                            }
                        }
                    }
                    if (!contractsToExpose.isEmpty()) {
                        contractMappings.put(cur.getKey(), contractsToExpose);
                    }
                }
            }
        } else {
            contractsToExpose = new ArrayList<>();
            contractsToExpose.addAll(foundContracts);
            contractMappings.put("*", contractsToExpose);
        }
        extContex.getApplicationMap().put(FaceletViewHandlingStrategy.RESOURCE_LIBRARY_CONTRACT_DATA_STRUCTURE_KEY,
                contractMappings);

    }


    // ------------------------------------------------- Package Private Methods


    static void clear(ServletContext servletContext) {

        servletContext.removeAttribute(WEB_CONFIG_KEY);

    }


    // --------------------------------------------------------- Private Methods


    /**
     * <p>Is the configured value valid against the default boolean pattern.</p>
     * @param param the boolean parameter
     * @param value the configured value
     * @return <code>true</code> if the value is valid,
     *  otherwise <code>false</code>
     */
    private boolean isValueValid(BooleanWebContextInitParameter param,
                                 String value) {

        if (!ALLOWABLE_BOOLEANS.matcher(value).matches()) {
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.log(Level.WARNING,
                           "jsf.config.webconfig.boolconfig.invalidvalue",
                           new Object[]{
                                 value,
                                 param.getQualifiedName(),
                                 "true|false",
                                 "true|false",
                                 param.getDefaultValue()
                           });
            }
            return false;
        }

        return true;

    }


    /**
     * <p>Process all boolean context initialization parameters.</p>
     * @param servletContext the ServletContext of interest
     * @param contextName the context name
     */
    private void processBooleanParameters(ServletContext servletContext,
                                          String contextName) {

        // process boolean contxt parameters
        for (BooleanWebContextInitParameter param : BooleanWebContextInitParameter
              .values()) {
            String strValue =
                  servletContext.getInitParameter(param.getQualifiedName());
            boolean value;

            if (strValue != null && strValue.length() > 0 && param.isDeprecated()) {
                BooleanWebContextInitParameter alternate = param.getAlternate();
                if (LOGGER.isLoggable(Level.WARNING)) {
                    if (alternate != null) {
                        queueLoggingAction(new DeferredBooleanParameterLoggingAction(
                              param,
                              Level.WARNING,
                              "jsf.config.webconfig.param.deprecated",
                              new Object[]{
                                    contextName,
                                    param.getQualifiedName(),
                                    alternate.getQualifiedName()}));

                    } else {
                        queueLoggingAction(new DeferredBooleanParameterLoggingAction(
                              param,
                              Level.WARNING,
                              "jsf.config.webconfig.param.deprecated.no_replacement",
                              new Object[]{
                                    contextName,
                                    param.getQualifiedName()}));

                    }
                }

                if (alternate != null) {
                    if (isValueValid(param, strValue)) {
                        value = Boolean.valueOf(strValue);
                    } else {
                        value = param.getDefaultValue();
                    }

                    if (LOGGER.isLoggable(Level.INFO) && alternate != null) {
                        queueLoggingAction(new DeferredBooleanParameterLoggingAction(
                              param,
                              Level.INFO,
                              ((value)
                               ? "jsf.config.webconfig.configinfo.reset.enabled"
                               : "jsf.config.webconfig.configinfo.reset.disabled"),
                              new Object[]{
                                    contextName,
                                    alternate.getQualifiedName()}));
                    }

                    booleanContextParameters.put(alternate, value);
                }
                continue;
            }

            if (!param.isDeprecated()) {
                if (strValue == null) {
                    value = param.getDefaultValue();
                } else {
                    if (isValueValid(param, strValue)) {
                        value = Boolean.valueOf(strValue);
                    } else {
                        value = param.getDefaultValue();
                    }
                }

                // first param processed should be
                // com.sun.faces.displayConfiguration
                if (BooleanWebContextInitParameter.DisplayConfiguration.equals(param) && value) {
                    loggingLevel = Level.INFO;
                }

                if (LOGGER.isLoggable(loggingLevel)) {
                    LOGGER.log(loggingLevel,
                               ((value)
                                ? "jsf.config.webconfig.boolconfiginfo.enabled"
                                : "jsf.config.webconfig.boolconfiginfo.disabled"),
                               new Object[]{contextName,
                                            param.getQualifiedName()});
                }

                booleanContextParameters.put(param, value);
            }

        }

    }


    /**
     * Adds all com.sun.faces init parameter names to a list.  This allows
     * callers to determine if a parameter was explicitly set.
     * @param servletContext the ServletContext of interest
     */
    private void initSetList(ServletContext servletContext) {

        for (Enumeration e = servletContext.getInitParameterNames();
              e.hasMoreElements(); ) {
            String name = e.nextElement().toString();
            if (name.startsWith("com.sun.faces") ||
                  name.startsWith("javax.faces")) {
                setParams.add(name);
            }
        }

    }


    /**
     * @param name the param name
     * @return <code>true</code> if the name was explicitly specified
     */
    private boolean isSet(String name) {

        return setParams.contains(name);

    }


    /**
     * <p>Process all non-boolean context initialization parameters.</p>
     * @param servletContext the ServletContext of interest
     * @param contextName the context name
     */
    private void processInitParameters(ServletContext servletContext,
                                       String contextName) {

        for (WebContextInitParameter param : WebContextInitParameter.values()) {
            String value =
                  servletContext.getInitParameter(param.getQualifiedName());

            if (value != null && value.length() > 0 && param.isDeprecated()) {
                WebContextInitParameter alternate = param.getAlternate();
                DeprecationLoggingStrategy strategy = param.getDeprecationLoggingStrategy();
                if (strategy == null || strategy.shouldBeLogged(this)) {
                    if (LOGGER.isLoggable(Level.WARNING)) {
                        if (alternate != null) {
                            queueLoggingAction(new DeferredParameterLoggingAction(param, Level.WARNING,
                                       "jsf.config.webconfig.param.deprecated",
                                       new Object[]{
                                             contextName,
                                             param.getQualifiedName(),
                                             alternate.getQualifiedName()}));

                        } else {
                            queueLoggingAction(new DeferredParameterLoggingAction(
                                  param,
                                  Level.WARNING,
                                  "jsf.config.webconfig.param.deprecated.no_replacement",
                                  new Object[]{
                                        contextName,
                                        param.getQualifiedName()}));
                        }
                    }
                }

                if (alternate != null) {
                    queueLoggingAction(
                          new DeferredParameterLoggingAction(param,
                                                             Level.INFO,
                                                             "jsf.config.webconfig.configinfo.reset",
                                                             new Object[]{
                                                                    contextName,
                                                                    alternate.getQualifiedName(),
                                                                    value}));

                    contextParameters.put(alternate, value);
                }
                continue;
            }

            if ((value == null || value.length() == 0) && !param.isDeprecated()) {
                value = param.getDefaultValue();
            }
            if (value == null || value.length() == 0) {
                continue;
            }

            if (value.length() > 0) {
                if (LOGGER.isLoggable(loggingLevel)) {
                    LOGGER.log(loggingLevel,
                               "jsf.config.webconfig.configinfo",
                               new Object[]{contextName,
                                            param.getQualifiedName(),
                                            value});

                }
                contextParameters.put(param, value);
            } else {
                if (LOGGER.isLoggable(loggingLevel)) {
                    LOGGER.log(loggingLevel,
                               "jsf.config.webconfig.option.notconfigured",
                               new Object[]{contextName,
                                            param.getQualifiedName()});
                }
            }

        }

    }


    /**
     * <p>Process all JNDI entries.</p>
     * @param contextName the context name
     */
    private void processJndiEntries(String contextName) {

        Context initialContext = null;
        try {
            initialContext = new InitialContext();
        } catch (NoClassDefFoundError nde) {
          // on google app engine InitialContext is forbidden to use and GAE throws NoClassDefFoundError
          if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.log(Level.FINE, nde.toString(), nde);
          }
        } catch (NamingException ne) {
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.log(Level.WARNING, ne.toString(), ne);
            }
        }

        if (initialContext != null) {
            // process environment entries
            for (WebEnvironmentEntry entry : WebEnvironmentEntry.values()) {
                String entryName = entry.getQualifiedName();
                String value = null;

                try {
                    value = (String) initialContext.lookup(entryName);
                } catch (NamingException root) {
                    if (LOGGER.isLoggable(Level.FINE)) {
                        LOGGER.fine(root.toString());
                    }
                }

                if (value != null) {
                    if (LOGGER.isLoggable(Level.INFO)) {
                        if (LOGGER
                                .isLoggable(loggingLevel)) {
                            LOGGER.log(loggingLevel,
                                    "jsf.config.webconfig.enventryinfo",
                                    new Object[]{contextName,
                                        entryName,
                                        value});
                        }
                    }
                    envEntries.put(entry, value);
                }
            }
        }
    }

    public boolean canProcessJndiEntries() {

        try {
            Util.getCurrentLoader(this).loadClass("javax.naming.InitialContext");
        } catch (Exception e) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine(
                      "javax.naming is unavailable.  JNDI entries related to Mojarra configuration will not be processed.");
            }
            return false;
        }
        return true;

    }


    private void queueLoggingAction(DeferredLoggingAction loggingAction) {

        if (deferredLoggingActions == null) {
            deferredLoggingActions = new ArrayList<>();
        }
        deferredLoggingActions.add(loggingAction);

    }


    // ------------------------------------------------------------------- Enums


    /**
     * <p>An <code>enum</code> of all non-boolean context initalization parameters
     * recognized by the implementation.</p>
     */
    public enum WebContextInitParameter {


        // implementation note:
        // if a parameter is to be deprecated,
        // then the <name>Deprecated enum element
        // *must* appear after the one that is taking
        // its place.  The reporting logic depends on this

        ManagedBeanFactoryDecorator(
              "com.sun.faces.managedBeanFactoryDecoratorClass",
              ""
        ),
        StateSavingMethod(
              StateManager.STATE_SAVING_METHOD_PARAM_NAME,
              "server"
        ),
        FaceletsSuffix(
                ViewHandler.FACELETS_SUFFIX_PARAM_NAME,
                ViewHandler.DEFAULT_FACELETS_SUFFIX
        ),
        DefaultSuffix(
              ViewHandler.DEFAULT_SUFFIX_PARAM_NAME,
              ViewHandler.DEFAULT_SUFFIX
        ),
        JavaxFacesConfigFiles(
              FacesServlet.CONFIG_FILES_ATTR,
              ""
        ),
        JavaxFacesProjectStage(
              ProjectStage.PROJECT_STAGE_PARAM_NAME,
              "Production"
        ),
        AlternateLifecycleId(
              FacesServlet.LIFECYCLE_ID_ATTR,
              ""
        ),
        ResourceExcludes(
            ResourceHandler.RESOURCE_EXCLUDES_PARAM_NAME,
            ResourceHandler.RESOURCE_EXCLUDES_DEFAULT_VALUE + " .groovy"
        ),
        NumberOfViews(
              "com.sun.faces.numberOfViewsInSession",
              "15"
        ),
        NumberOfViewsDeprecated(
              "com.sun.faces.NUMBER_OF_VIEWS_IN_SESSION",
              "15",
              true,
              NumberOfViews
        ),
        NumberOfLogicalViews(
              "com.sun.faces.numberOfLogicalViews",
              "15"
        ),
        NumberOfLogicalViewsDeprecated(
              "com.sun.faces.NUMBER_OF_VIEWS_IN_LOGICAL_VIEW_IN_SESSION",
              "15",
              true,
              NumberOfLogicalViews
        ),
        NumberOfConcurrentFlashUsers(
              "com.sun.faces.numberOfConcerrentFlashUsers",
              "5000"
        ),
        NumberOfFlashesBetweenFlashReapings(
              "com.sun.faces.numberOfFlashesBetweenFlashReapings",
              "5000"
        ),
        InjectionProviderClass(
                "com.sun.faces.injectionProvider",
                ""
        ),
        SerializationProviderClass(
              "com.sun.faces.serializationProvider",
              ""
        ),
        ResponseBufferSize(
              "com.sun.faces.responseBufferSize",
              "1024"
        ),
        FaceletsBufferSize(
              ViewHandler.FACELETS_BUFFER_SIZE_PARAM_NAME,
              "1024"
        ),
        FaceletsBufferSizeDeprecated(
              "facelets.BUFFER_SIZE",
              "1024",
              true,
              FaceletsBufferSize,
              new FaceletsConfigParamLoggingStrategy()
        ),
        ClientStateWriteBufferSize(
              "com.sun.faces.clientStateWriteBufferSize",
              "8192"
        ),
        ResourceBufferSize(
            "com.sun.faces.resourceBufferSize",
            "2048"
        ),
        ExpressionFactory(
              "com.sun.faces.expressionFactory",
              "com.sun.el.ExpressionFactoryImpl"
        ),
        ClientStateTimeout(
              "com.sun.faces.clientStateTimeout",
              ""
        ),
        DefaultResourceMaxAge(
              "com.sun.faces.defaultResourceMaxAge",
              "604800000" // 7 days
        ),
        ResourceUpdateCheckPeriod(
              "com.sun.faces.resourceUpdateCheckPeriod",
              "5" // in minutes
        ),
        CompressableMimeTypes(
              "com.sun.faces.compressableMimeTypes",
              ""
        ),
        DisableUnicodeEscaping(
            "com.sun.faces.disableUnicodeEscaping",
            "auto"
        ),
        FaceletsDefaultRefreshPeriod(
              ViewHandler.FACELETS_REFRESH_PERIOD_PARAM_NAME,
              "2"
        ),
        FaceletsDefaultRefreshPeriodDeprecated(
              "facelets.REFRESH_PERIOD",
              "2",
              true,
              FaceletsDefaultRefreshPeriod,
              new FaceletsConfigParamLoggingStrategy()
        ),
        FaceletsResourceResolver(
              ResourceResolver.FACELETS_RESOURCE_RESOLVER_PARAM_NAME,
              ""
        ),
        FaceletsResourceResolverDeprecated(
              "facelets.RESOURCE_RESOLVER",
              "",
              true,
              FaceletsResourceResolver,
              new FaceletsConfigParamLoggingStrategy()
        ),
         FaceletsViewMappings(
              ViewHandler.FACELETS_VIEW_MAPPINGS_PARAM_NAME,
              ""
        ),
        FaceletsViewMappingsDeprecated(
              "facelets.VIEW_MAPPINGS",
              "",
              true,
              FaceletsViewMappings,
              new FaceletsConfigParamLoggingStrategy()
        ),
        FaceletsLibraries(
              ViewHandler.FACELETS_LIBRARIES_PARAM_NAME,
              ""
        ),
        FaceletsLibrariesDeprecated(
              "facelets.LIBRARIES",
              "",
              true,
              FaceletsLibraries,
              new FaceletsConfigParamLoggingStrategy()
        ),
        FaceletsDecorators(
              ViewHandler.FACELETS_DECORATORS_PARAM_NAME,
              ""
        ),
        FaceletsDecoratorsDeprecated(
              "facelets.DECORATORS",
              "",
              true,
              FaceletsDecorators,
              new FaceletsConfigParamLoggingStrategy()
        ),
        DuplicateJARPattern(
            "com.sun.faces.duplicateJARPattern",
            ""
        ),
        ValidateEmptyFields(
              UIInput.VALIDATE_EMPTY_FIELDS_PARAM_NAME,
              "auto"
        ),
        FullStateSavingViewIds(
              StateManager.FULL_STATE_SAVING_VIEW_IDS_PARAM_NAME,
              ""
        ),
        AnnotationScanPackages(
              "com.sun.faces.annotationScanPackages",
              ""
        ),
        FaceletCache(
            "com.sun.faces.faceletCache",
            ""
        ),
        FaceletsProcessingFileExtensionProcessAs(
                "",
                ""
        ),
        ClientWindowMode(
              ClientWindow.CLIENT_WINDOW_MODE_PARAM_NAME,
              "none"
        ),
        WebAppResourcesDirectory(
              ResourceHandler.WEBAPP_RESOURCES_DIRECTORY_PARAM_NAME,
              "/resources"
        ),
        WebAppContractsDirectory(
              ResourceHandler.WEBAPP_CONTRACTS_DIRECTORY_PARAM_NAME,
              "/contracts"
        ),
        WebsocketEndpointPort(
            PushContext.WEBSOCKET_ENDPOINT_PORT_PARAM_NAME,
            ""
        );



        private String defaultValue;
        private String qualifiedName;
        private WebContextInitParameter alternate;
        private boolean deprecated;
        private DeprecationLoggingStrategy loggingStrategy;


    // ---------------------------------------------------------- Public Methods


        public String getDefaultValue() {

            return defaultValue;

        }


        public String getQualifiedName() {

            return qualifiedName;

        }


        DeprecationLoggingStrategy getDeprecationLoggingStrategy() {

            return loggingStrategy;

        }


    // ------------------------------------------------- Package Private Methods


        WebContextInitParameter(String qualifiedName,
                                String defaultValue) {

            this(qualifiedName, defaultValue, false, null);

        }


        WebContextInitParameter(String qualifiedName,
                                String defaultValue,
                                boolean deprecated,
                                WebContextInitParameter alternate) {

            this.qualifiedName = qualifiedName;
            this.defaultValue = defaultValue;
            this.deprecated = deprecated;
            this.alternate = alternate;

        }


        WebContextInitParameter(String qualifiedName,
                                String defaultValue,
                                boolean deprecated,
                                WebContextInitParameter alternate,
                                DeprecationLoggingStrategy loggingStrategy) {

            this(qualifiedName, defaultValue, deprecated, alternate);
            this.loggingStrategy = loggingStrategy;

        }


    // --------------------------------------------------------- Private Methods


        private WebContextInitParameter getAlternate() {

            return alternate;

        }


        private boolean isDeprecated() {

            return deprecated;

        }

    }

    /**
     * <p>An <code>enum</code> of all boolean context initalization parameters
     * recognized by the implementation.</p>
     */
    public enum BooleanWebContextInitParameter {


        // implementation note:
        // if a parameter is to be deprecated,
        // then the <name>Deprecated enum element
        // *must* appear after the one that is taking
        // its place.  The reporting logic depends on this

        AlwaysPerformValidationWhenRequiredTrue(
            UIInput.ALWAYS_PERFORM_VALIDATION_WHEN_REQUIRED_IS_TRUE,
            false),
        DisplayConfiguration(
              "com.sun.faces.displayConfiguration",
              false
        ),
        ValidateFacesConfigFiles(
              "com.sun.faces.validateXml",
              false
        ),
        VerifyFacesConfigObjects(
              "com.sun.faces.verifyObjects",
              false
        ),
        ForceLoadFacesConfigFiles(
              "com.sun.faces.forceLoadConfiguration",
              false
        ),
        DisableArtifactVersioning(
              "com.sun.faces.disableVersionTracking",
              false,
              true,
              null
        ),
        DisableClientStateEncryption(
              "com.sun.faces.disableClientStateEncryption",
              false
        ),
        DisableFacesServletAutomaticMapping(
                FacesServlet.DISABLE_FACESSERVLET_TO_XHTML_PARAM_NAME,
                false
        ),
        EnableClientStateDebugging(
              "com.sun.faces.enableClientStateDebugging",
              false
        ),
        EnableHtmlTagLibraryValidator(
              "com.sun.faces.enableHtmlTagLibValidator",
              false
        ),
        EnableCoreTagLibraryValidator(
              "com.sun.faces.enableCoreTagLibValidator",
              false
        ),
        PreferXHTMLContentType(
              "com.sun.faces.preferXHTML",
              false
        ),
        PreferXHTMLContextTypeDeprecated(
              "com.sun.faces.PreferXHTML",
              false,
              true,
              PreferXHTMLContentType
        ),
        CompressViewState(
              "com.sun.faces.compressViewState",
              true
        ),
        CompressViewStateDeprecated(
              "com.sun.faces.COMPRESS_STATE",
              true,
              true,
              CompressViewState
        ),
        CompressJavaScript(
            "com.sun.faces.compressJavaScript",
            true
        ),
        ExternalizeJavaScriptDeprecated(
            "com.sun.faces.externalizeJavaScript",
            true,
            true,
            null
        ),
        SendPoweredByHeader(
              "com.sun.faces.sendPoweredByHeader",
              false
        ),
        EnableJSStyleHiding(
            "com.sun.faces.enableJSStyleHiding",
             false
        ),
        EnableScriptInAttributeValue(
            "com.sun.faces.enableScriptsInAttributeValues",
             true
        ),
        WriteStateAtFormEnd(
            "com.sun.faces.writeStateAtFormEnd",
            true
        ),
        EnableLazyBeanValidation(
             "com.sun.faces.enableLazyBeanValidation",
             true
        ),
        EnableLoadBundle11Compatibility(
             "com.sun.faces.enabledLoadBundle11Compatibility",
             false
        ),
        EnableRestoreView11Compatibility(
              "com.sun.faces.enableRestoreView11Compatibility",
              false
        ),
        SerializeServerState(
              StateManager.SERIALIZE_SERVER_STATE_PARAM_NAME,
              false
        ),
        SerializeServerStateDeprecated(
              "com.sun.faces.serializeServerState",
              false,
               true,
                SerializeServerState
        ),
        EnableViewStateIdRendering(
            "com.sun.faces.enableViewStateIdRendering",
            true
        ),
        RegisterConverterPropertyEditors(
            "com.sun.faces.registerConverterPropertyEditors",
            false
        ),
        EnableGroovyScripting(
            "com.sun.faces.enableGroovyScripting",
            false
        ),
        DisableFaceletJSFViewHandler(
              ViewHandler.DISABLE_FACELET_JSF_VIEWHANDLER_PARAM_NAME,
              false
        ),
        DisableFaceletJSFViewHandlerDeprecated(
                "DISABLE_FACELET_JSF_VIEWHANDLER",
                false,
                true,
                DisableFaceletJSFViewHandler
        ),
        DisableDefaultBeanValidator(
                BeanValidator.DISABLE_DEFAULT_BEAN_VALIDATOR_PARAM_NAME,
                false),
        DateTimeConverterUsesSystemTimezone(
              Converter.DATETIMECONVERTER_DEFAULT_TIMEZONE_IS_SYSTEM_TIMEZONE_PARAM_NAME,
              false
        ),
        EnableHttpMethodRestrictionPhaseListener(
              "com.sun.faces.ENABLE_HTTP_METHOD_RESTRICTION_PHASE_LISTENER",
              false
        ),
        FaceletsSkipComments(
              ViewHandler.FACELETS_SKIP_COMMENTS_PARAM_NAME,
              false
        ),
        FaceletsSkipCommentsDeprecated(
              "facelets.SKIP_COMMENTS",
              false,
              true,
              FaceletsSkipComments,
              new FaceletsConfigParamLoggingStrategy()
        ),
        PartialStateSaving(
              StateManager.PARTIAL_STATE_SAVING_PARAM_NAME,
              true
        ),
        GenerateUniqueServerStateIds(
              "com.sun.faces.generateUniqueServerStateIds",
              true
        ),
        InterpretEmptyStringSubmittedValuesAsNull(
            UIInput.EMPTY_STRING_AS_NULL_PARAM_NAME,
            false
        ),
        AutoCompleteOffOnViewState(
              "com.sun.faces.autoCompleteOffOnViewState",
              true
        ),
        EnableThreading(
              "com.sun.faces.enableThreading",
              false
        ),
        AllowTextChildren(
            "com.sun.faces.allowTextChildren",
            false
        ),
        CacheResourceModificationTimestamp(
              "com.sun.faces.cacheResourceModificationTimestamp",
              false
        ),
        EnableAgressiveSessionDirtying(
              "com.sun.faces.enableAgressiveSessionDirtying",
              false
        ),
        EnableDistributable(
              "com.sun.faces.enableDistributable",
              false
        ),
        EnableFaceletsResourceResolverResolveCompositeComponents(
              "com.sun.faces.enableFaceletsResourceResolverCompositeComponents",
              false
        ),
        EnableMissingResourceLibraryDetection(
              "com.sun.faces.enableMissingResourceLibraryDetection",
              false
        ),
        DisableIdUniquenessCheck(
            "com.sun.faces.disableIdUniquenessCheck",
            false),
        EnableTransitionTimeNoOpFlash(
                "com.sun.faces.enableTransitionTimeNoOpFlash",
                false),
        ForceAlwaysWriteFlashCookie(
            "com.sun.faces.forceAlwaysWriteFlashCookie",
            false),
        ViewRootPhaseListenerQueuesException(
            UIViewRoot.VIEWROOT_PHASE_LISTENER_QUEUES_EXCEPTIONS_PARAM_NAME,
            false),
        EnableValidateWholeBean(
            BeanValidator.ENABLE_VALIDATE_WHOLE_BEAN_PARAM_NAME,
            false),
        EnableWebsocketEndpoint(
            PushContext.ENABLE_WEBSOCKET_ENDPOINT_PARAM_NAME,
            false),
        DisallowDoctypeDecl(
            "com.sun.faces.disallowDoctypeDecl",
            false);

        private BooleanWebContextInitParameter alternate;

        private String qualifiedName;
        private boolean defaultValue;
        private boolean deprecated;
        private DeprecationLoggingStrategy loggingStrategy;


    // ---------------------------------------------------------- Public Methods


        public boolean getDefaultValue() {

            return defaultValue;

        }


        public String getQualifiedName() {

            return qualifiedName;

        }


        DeprecationLoggingStrategy getDeprecationLoggingStrategy() {

            return loggingStrategy;

        }


    // ------------------------------------------------- Package Private Methods


        BooleanWebContextInitParameter(String qualifiedName,
                                       boolean defaultValue) {

            this(qualifiedName, defaultValue, false, null);

        }


        BooleanWebContextInitParameter(String qualifiedName,
                                       boolean defaultValue,
                                       boolean deprecated,
                                       BooleanWebContextInitParameter alternate) {

            this.qualifiedName = qualifiedName;
            this.defaultValue = defaultValue;
            this.deprecated = deprecated;
            this.alternate = alternate;

        }


        BooleanWebContextInitParameter(String qualifiedName,
                                      boolean defaultValue,
                                      boolean deprecated,
                                      BooleanWebContextInitParameter alternate,
                                      DeprecationLoggingStrategy loggingStrategy) {

            this(qualifiedName, defaultValue, deprecated, alternate);
            this.loggingStrategy = loggingStrategy;

        }


    // --------------------------------------------------------- Private Methods


        private BooleanWebContextInitParameter getAlternate() {

            return alternate;

        }


        private boolean isDeprecated() {

            return deprecated;

        }

    }

    /**
     * <p>An <code>enum</code> of all environment entries (specified in the
     * web.xml) recognized by the implemenetation.</p>
     */
    public enum WebEnvironmentEntry {


        ProjectStage(javax.faces.application.ProjectStage.PROJECT_STAGE_JNDI_NAME);

        private static final String JNDI_PREFIX = "java:comp/env/";
        private String qualifiedName;


    // ---------------------------------------------------------- Public Methods


         public String getQualifiedName() {

            return qualifiedName;

        }


    // ------------------------------------------------- Package Private Methods


        WebEnvironmentEntry(String qualifiedName) {

            if (qualifiedName.startsWith(JNDI_PREFIX)) {
                this.qualifiedName = qualifiedName;
            } else {
                this.qualifiedName = JNDI_PREFIX + qualifiedName;
            }

        }

    }

    /**
     * <p>An <code>enum</code> of all possible values for the <code>disableUnicodeEscaping</code>
     * configuration parameter.</p>
     */
    public enum DisableUnicodeEscaping {
        True("true"),
        False("false"),
        Auto("auto");

        private final String value;

        DisableUnicodeEscaping(String value) {
            this.value = value;
        }

        public static DisableUnicodeEscaping getByValue(String value)
        {
            for (DisableUnicodeEscaping disableUnicodeEscaping : DisableUnicodeEscaping.values()) {
                if (disableUnicodeEscaping.value.equals(value)) {
                    return disableUnicodeEscaping;
                }
            }

            return null;
        }
    }


    // ----------------------------------------------------------- Inner Classes


    private interface DeprecationLoggingStrategy {

        boolean shouldBeLogged(WebConfiguration configuration);

    }


    private static class FaceletsConfigParamLoggingStrategy implements DeprecationLoggingStrategy {

        @Override
        public boolean shouldBeLogged(WebConfiguration configuration) {
            return !configuration.isOptionEnabled(BooleanWebContextInitParameter.DisableFaceletJSFViewHandler) &&
                    !configuration.isOptionEnabled(BooleanWebContextInitParameter.DisableFaceletJSFViewHandlerDeprecated);
        }

    } // END FaceletsConfigParamLoggingStrategy


    private interface DeferredLoggingAction {

        void log();

    } // END DeferredLogginAction


    private class DeferredParameterLoggingAction implements DeferredLoggingAction {

        private WebContextInitParameter parameter;
        private Level loggingLevel;
        private String logKey;
        private Object[] params;


        DeferredParameterLoggingAction(WebContextInitParameter parameter,
                                       Level loggingLevel,
                                       String logKey,
                                       Object[] params) {

            this.parameter = parameter;
            this.loggingLevel = loggingLevel;
            this.logKey = logKey;
            this.params = params;

        }

        @Override
        public void log() {

            if (WebConfiguration.LOGGER.isLoggable(loggingLevel)) {
                DeprecationLoggingStrategy strategy = parameter.getDeprecationLoggingStrategy();
                if (strategy != null && strategy.shouldBeLogged(WebConfiguration.this)) {
                    WebConfiguration.LOGGER.log(loggingLevel, logKey, params);
                }
            }

        }

    } // END DeferredParameterLogginAction


    private class DeferredBooleanParameterLoggingAction implements DeferredLoggingAction {

        private BooleanWebContextInitParameter parameter;
        private Level loggingLevel;
        private String logKey;
        private Object[] params;

        DeferredBooleanParameterLoggingAction(BooleanWebContextInitParameter parameter,
                                              Level loggingLevel,
                                              String logKey,
                                              Object[] params) {
            this.parameter = parameter;
            this.loggingLevel = loggingLevel;
            this.logKey = logKey;
            this.params = params;
        }

        @Override
        public void log() {

            if (WebConfiguration.LOGGER.isLoggable(loggingLevel)) {
                DeprecationLoggingStrategy strategy = parameter.getDeprecationLoggingStrategy();
                if (strategy != null && strategy.shouldBeLogged(WebConfiguration.this)) {
                    WebConfiguration.LOGGER.log(loggingLevel, logKey, params);
                }
            }

        }

    } // END DeferredBooleanParameterLoggingAction

} // END WebConfiguration

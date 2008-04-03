/*
 * $Id: WebConfiguration.java,v 1.40 2008/02/16 05:30:41 rlubke Exp $
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

package com.sun.faces.config;

import com.sun.faces.RIConstants;
import com.sun.faces.util.FacesLogger;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.faces.application.ResourceHandler;


/** Class Documentation */
public class WebConfiguration {


    // Log instance for this class
    private static final Logger LOGGER = FacesLogger.CONFIG.getLogger();

    // A Simple regular expression of allowable boolean values
    private static final Pattern ALLOWABLE_BOOLEANS =
          Pattern.compile("true|false");

    // Key under which we store our WebConfiguration instance.
    private static final String WEB_CONFIG_KEY =
          "com.sun.faces.config.WebConfiguration";

    // Logging level.  Defaults to FINE
    private Level loggingLevel = Level.FINE;

    private Map<BooleanWebContextInitParameter, Boolean> booleanContextParameters =
          new EnumMap<BooleanWebContextInitParameter, Boolean>(BooleanWebContextInitParameter.class);

    private Map<WebContextInitParameter, String> contextParameters =
          new EnumMap<WebContextInitParameter, String>(WebContextInitParameter.class);

    private Map<WebEnvironmentEntry, String> envEntries =
          new EnumMap<WebEnvironmentEntry, String>(WebEnvironmentEntry.class);

    private List<String> setParams = new ArrayList<String>();

    private ServletContext servletContext;


    // ------------------------------------------------------------ Constructors


    private WebConfiguration(ServletContext servletContext) {

        this.servletContext = servletContext;

        String contextName = getServletContextName();

        initSetList(servletContext);
        processBooleanParameters(servletContext, contextName);
        processInitParameters(servletContext, contextName);
        processJndiEntries(contextName);

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

        FacesContext facesContext = FacesContext.getCurrentInstance();
        return getInstance(facesContext.getExternalContext());

    }


    /**
     * Return the WebConfiguration instance for this application.
     * @param extContext the ExternalContext for this request
     * @return the WebConfiguration for this application
     */
    public static WebConfiguration getInstance(ExternalContext extContext) {

        WebConfiguration config = (WebConfiguration) extContext.getApplicationMap()
              .get(WEB_CONFIG_KEY);
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

        WebConfiguration webConfig = (WebConfiguration)
              servletContext.getAttribute(WEB_CONFIG_KEY);

        if (webConfig == null) {
            webConfig = new WebConfiguration(servletContext);
            servletContext.setAttribute(WEB_CONFIG_KEY, webConfig);
        }
        return webConfig;

    }


    /**
     * @return The <code>ServletContext</code> originally used to construct
     * this WebConfiguration instance
     */
    public ServletContext getServletContext() {

        return servletContext;

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

        return contextParameters.get(param);

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

        if (servletContext.getMajorVersion() == 2
            && servletContext.getMinorVersion() <= 4) {
            return servletContext.getServletContextName();
        } else {
            return servletContext.getContextPath();
        }

    }


    public void overrideContextInitParameter(BooleanWebContextInitParameter param, boolean value) {

        if (param == null) {
            return;
        }
        boolean oldVal = booleanContextParameters.put(param, value);
        if (LOGGER.isLoggable(Level.INFO) && oldVal != value) {
            LOGGER.log(Level.INFO,
                       "Overriding init parameter {0}.  Changing from {1} to {2}.",
                       new Object[] { param.getQualifiedName(), oldVal, value});
        }

    }


    public void overrideContextInitParameter(WebContextInitParameter param, String value) {

        if (param == null || value == null || value.length() == 0) {
            return;
        }
        value = value.trim();
        String oldVal = contextParameters.put(param, value);
         if (LOGGER.isLoggable(Level.INFO) && !(oldVal.equals(value))) {
            LOGGER.log(Level.INFO,
                       "Overriding init parameter {0}.  Changing from {1} to {2}.",
                       new Object[] { param.getQualifiedName(), oldVal, value});
        }
        

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
                                 "true|false"
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
                        LOGGER.log(Level.WARNING,
                                   "jsf.config.webconfig.param.deprecated",
                                   new Object[]{
                                         contextName,
                                         param.getQualifiedName(),
                                         alternate.getQualifiedName()});
                    } else {
                        LOGGER.log(Level.WARNING,
                                   "jsf.config.webconfig.param.deprecated.no_replacement",
                                   new Object[]{
                                         contextName,
                                         param.getQualifiedName()});
                    }
                }

                if (alternate != null) {
                    if (isValueValid(param, strValue)) {
                        value = Boolean.valueOf(strValue);
                    } else {
                        value = param.getDefaultValue();
                    }

                    if (LOGGER.isLoggable(Level.INFO) && alternate != null) {
                        LOGGER.log(Level.INFO,
                                   ((value)
                                    ? "jsf.config.webconfig.configinfo.reset.enabled"
                                    : "jsf.config.webconfig.configinfo.reset.disabled"),
                                   new Object[]{contextName,
                                                alternate.getQualifiedName()});
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
                if (BooleanWebContextInitParameter.DisplayConfiguration
                      .equals(param) && value) {
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
            if (name.startsWith("com.sun.faces")) {
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
                 if (LOGGER.isLoggable(Level.WARNING)) {
                    if (alternate != null) {
                        LOGGER.log(Level.WARNING,
                                   "jsf.config.webconfig.param.deprecated",
                                   new Object[]{
                                         contextName,
                                         param.getQualifiedName(),
                                         alternate.getQualifiedName()});
                    } else {
                        LOGGER.log(Level.WARNING,
                                   "jsf.config.webconfig.param.deprecated.no_replacement",
                                   new Object[]{
                                         contextName,
                                         param.getQualifiedName()});
                    }
                }

                if (alternate != null) {
                    if (LOGGER.isLoggable(Level.INFO)) {
                        LOGGER.log(Level.INFO,
                                   "jsf.config.webconfig.configinfo.reset",
                                   new Object[]{contextName,
                                                alternate.getQualifiedName(),
                                                value});
                    }

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

        InitialContext initialContext = null;
        try {
            initialContext = new InitialContext();
        } catch (NamingException ne) {
            // log WARNING - unable to get JNDI initial context
        }

        if (initialContext != null) {
            // process environment entries
            for (WebEnvironmentEntry entry : WebEnvironmentEntry.values()) {
                String entryName = entry.getQualifiedName();

                try {
                    String value = (String) initialContext.lookup(entryName);
                    if (value != null) {
                        if (LOGGER.isLoggable(Level.INFO)) {
                            // special logic for ClientStateSavingPassword
                            if (!entry
                                  .equals(WebEnvironmentEntry.ClientStateSavingPassword))
                            {
                                if (LOGGER
                                      .isLoggable(loggingLevel)) {
                                    LOGGER.log(loggingLevel,
                                               "jsf.config.webconfig.enventryinfo",
                                               new Object[]{contextName,
                                                            entryName,
                                                            value});
                                }
                            } else {
                                if (LOGGER
                                      .isLoggable(loggingLevel)) {
                                    LOGGER.log(loggingLevel,
                                               "jsf.config.webconfig.enventry.clientencrypt",
                                               contextName);
                                }
                            }
                        }
                        envEntries.put(entry, value);
                    }
                } catch (NamingException ne) {
                    // log WARNING - unable to lookup value
                }
            }
        }

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
              "javax.faces.STATE_SAVING_METHOD",
              "server"
        ),
        JspDefaultSuffix(
              "javax.faces.DEFAULT_SUFFIX",
              ".jsp"
        ),
        JavaxFacesConfigFiles(
              "javax.faces.CONFIG_FILES",
              ""
        ),
        JavaxFacesProjectStage(
              "javax.faces.PROJECT_STAGE",
              "Production"
        ),
        AlternateLifecycleId(
              "javax.faces.LIFECYCLE_ID",
              ""
        ),
        ResourceExcludes(
            ResourceHandler.RESOURCE_EXCLUDES_PARAM_NAME,
            ResourceHandler.RESOURCE_EXCLUDES_DEFAULT_VALUE
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
              "604800"
        ),
        ResourceUpdateCheckPeriod(
              "com.sun.faces.resourceUpdateCheckPeriod",
              "5" // in minutes
        );


        private String defaultValue;
        private String qualifiedName;
        private WebContextInitParameter alternate;
        private boolean deprecated;


    // ---------------------------------------------------------- Public Methods


        public String getDefaultValue() {

            return defaultValue;

        }


        public String getQualifiedName() {

            return qualifiedName;

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
        EnableHtmlTagLibraryValidator(
              "com.sun.faces.enableHtmlTagLibValidator",
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
        ExternalizeJavaScript(
            "com.sun.faces.externalizeJavaScript",
            false
        ),
        SendPoweredByHeader(
              "com.sun.faces.sendPoweredByHeader",
              true
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
              "com.sun.faces.serializeServerState",
              false
        ),
        EnableViewStateIdRendering(
            "com.sun.faces.enableViewStateIdRendering",
            true
        );

        private BooleanWebContextInitParameter alternate;

        private String qualifiedName;
        private boolean defaultValue;
        private boolean deprecated;


    // ---------------------------------------------------------- Public Methods


        public boolean getDefaultValue() {

            return defaultValue;

        }


        public String getQualifiedName() {

            return qualifiedName;

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
     * web.xml) recognized by the implmenetation.</p>
     */
    public enum WebEnvironmentEntry {


        ClientStateSavingPassword("ClientStateSavingPassword");

        private static final String JNDI_PREFIX = "java:comp/env/";
        private String qualifiedName;


    // ---------------------------------------------------------- Public Methods


         public String getQualifiedName() {

            return qualifiedName;

        }


    // ------------------------------------------------- Package Private Methods


        WebEnvironmentEntry(String qualifiedName) {

            this.qualifiedName = JNDI_PREFIX 
                                 + RIConstants.FACES_PREFIX 
                                 + qualifiedName;

        }

    }

} // END WebConfiguration

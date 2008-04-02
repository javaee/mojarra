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
 * [WebConfiguration] [$Id: WebConfiguration.java,v 1.14 2006/09/06 20:44:05 rlubke Exp $] [Apr 2, 2006]
 * 
 * Copyright 2006 Sun Microsystems Inc. All Rights Reserved
 */

package com.sun.faces.config;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ExternalContext;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import com.sun.faces.util.Util;
import com.sun.faces.RIConstants;


/** Class Documentation */
public class WebConfiguration {


    // Log instance for this class
    private static final Logger LOGGER = Util.getLogger(Util.FACES_LOGGER
                                                        + Util.CONFIG_LOGGER);

    // A Simple regular expression of allowable boolean values
    private static final Pattern ALLOWABLE_BOOLEANS =
          Pattern.compile("true|false");

    // Key under which we store our WebConfiguration instance.
    private static final String WEB_CONFIG_KEY =
          "com.sun.faces.config.WebConfiguration";
    
    // Logging level.  Defaults to FINE
    private Level loggingLevel = Level.FINE;

    private Map<BooleanWebContextInitParameter, Boolean> booleanContextParameters =
          new HashMap<BooleanWebContextInitParameter, Boolean>(
                BooleanWebContextInitParameter.values().length);

    private Map<WebContextInitParameter, String> contextParameters =
          new HashMap<WebContextInitParameter, String>(WebContextInitParameter
                .values().length);

    private Map<WebEnvironmentEntry, String> envEntries =
          new HashMap<WebEnvironmentEntry, String>(WebEnvironmentEntry
                .values().length);

    private ServletContext servletContext;


    // ------------------------------------------------------------ Constructors


    private WebConfiguration(ServletContext servletContext) {

        this.servletContext = servletContext;

        String contextName = getServletContextName();
        
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
        if (facesContext == null) {
            return null;
        } else {
            return getInstance(facesContext.getExternalContext());
        }

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
    public boolean getBooleanContextInitParameter(
          BooleanWebContextInitParameter param) {

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
    public String getContextInitParameter(WebContextInitParameter param) {

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


    public void overrideContextInitParameter(BooleanWebContextInitParameter param) {

        // no-op for now

    }


    public void overrideContextInitParameter(WebContextInitParameter param) {

        // no-op for now

    }


    public void overrideEnvEntry(WebEnvironmentEntry entry) {

        // no-op for now

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
                    LOGGER.log(Level.WARNING,
                               "jsf.config.webconfig.param.deprecated",
                               new Object[]{
                                     contextName,
                                     param.getQualifiedName(),
                                     alternate.getQualifiedName()});
                }

                if (isValueValid(param, strValue)) {
                    value = Boolean.valueOf(strValue);
                } else {
                    value = param.getDefaultValue();
                }

                if (LOGGER.isLoggable(Level.INFO)) {
                    LOGGER.log(Level.INFO,
                               ((value)
                                ? "jsf.config.webconfig.configinfo.reset.enabled"
                                : "jsf.config.webconfig.configinfo.reset.disabled"),
                               new Object[]{contextName,
                                            alternate.getQualifiedName()});
                }

                booleanContextParameters.put(alternate, value);
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
                    LOGGER.log(Level.WARNING,
                               "jsf.config.webconfig.param.deprecated",
                               new Object[]{
                                     contextName,
                                     param.getQualifiedName(),
                                     alternate.getQualifiedName()});
                }

                if (LOGGER.isLoggable(Level.INFO)) {
                    LOGGER.log(Level.INFO,
                               "jsf.config.webconfig.configinfo.reset",
                               new Object[]{contextName,
                                            alternate.getQualifiedName(),
                                            value});
                }

                contextParameters.put(alternate, value);
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
        AlternateLifecycleId(
              "javax.faces.LIFECYCLE_ID",
              ""
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
              "4096"
        ),
        ClientStateWriteBufferSize(
              "com.sun.faces.clientStateWriteBufferSize",
              "8192"
        ),
        ExpressionFactory(
              "com.sun.faces.expressionFactory",
              "com.sun.el.ExpressionFactoryImpl"
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
              false
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

/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2017 Oracle and/or its affiliates. All rights reserved.
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

package javax.faces.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static javax.faces.annotation.FacesConfig.Version.JSF_2_2;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

import javax.enterprise.util.Nonbinding;
import javax.faces.application.ProjectStage;
import javax.faces.application.ResourceHandler;
import javax.faces.application.StateManager;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIInput;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.lifecycle.ClientWindow;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.push.PushContext;
import javax.faces.validator.BeanValidator;
import javax.faces.view.facelets.ResourceResolver;
import javax.faces.webapp.FacesServlet;
import javax.inject.Qualifier;

@Qualifier
@Target(TYPE)
@Retention(RUNTIME)
public @interface FacesConfig {

    public static enum Version {

        /**
         * Activates all features for JSF 2.2 and earlier, as long as JSF 2.2 doesn't provide newer versions
         */
        JSF_2_2,

        /**
         * Activates all features for JSF 2.3 and earlier, as long as JSF 2.3 doesn't provide newer versions
         */
        JSF_2_3,

        /**
         * Activates all features for the JSF version the implementation code corresponds with.
         */
        CURRENT
    }

    public static enum ContextParameter {

        /**
         * <code>javax.faces.ALWAYS_PERFORM_VALIDATION_WHEN_REQUIRED_IS_TRUE</code> as {@link Boolean}.
         * @see UIInput#ALWAYS_PERFORM_VALIDATION_WHEN_REQUIRED_IS_TRUE
         */
        ALWAYS_PERFORM_VALIDATION_WHEN_REQUIRED_IS_TRUE(UIInput.ALWAYS_PERFORM_VALIDATION_WHEN_REQUIRED_IS_TRUE, Boolean.class, false),

        /**
         * <code>javax.faces.CLIENT_WINDOW_MODE</code> as {@link String}.
         * @see ClientWindow#CLIENT_WINDOW_MODE_PARAM_NAME
         */
        CLIENT_WINDOW_MODE(ClientWindow.CLIENT_WINDOW_MODE_PARAM_NAME, String.class, "none"),

        /**
         * <code>javax.faces.CONFIG_FILES</code> as {@link String} array.
         * @see FacesServlet#CONFIG_FILES_ATTR
         */
        CONFIG_FILES(FacesServlet.CONFIG_FILES_ATTR, StringArray.COMMA_SEPARATED, null),

        /**
         * <code>javax.faces.DATETIMECONVERTER_DEFAULT_TIMEZONE_IS_SYSTEM_TIMEZONE</code> as {@link Boolean}.
         * @see Converter#DATETIMECONVERTER_DEFAULT_TIMEZONE_IS_SYSTEM_TIMEZONE_PARAM_NAME
         */
        DATETIMECONVERTER_DEFAULT_TIMEZONE_IS_SYSTEM_TIMEZONE(Converter.DATETIMECONVERTER_DEFAULT_TIMEZONE_IS_SYSTEM_TIMEZONE_PARAM_NAME, Boolean.class, false),

        /**
         * <code>javax.faces.validator.DISABLE_DEFAULT_BEAN_VALIDATOR</code> as {@link Boolean}.
         * @see BeanValidator#DISABLE_DEFAULT_BEAN_VALIDATOR_PARAM_NAME
         */
        DISABLE_DEFAULT_BEAN_VALIDATOR(BeanValidator.DISABLE_DEFAULT_BEAN_VALIDATOR_PARAM_NAME, Boolean.class, false),

        /**
         * <code>javax.faces.DISABLE_FACELET_JSF_VIEWHANDLER</code> as {@link Boolean}.
         * @see ViewHandler#DISABLE_FACELET_JSF_VIEWHANDLER_PARAM_NAME
         */
        DISABLE_FACELET_JSF_VIEWHANDLER(ViewHandler.DISABLE_FACELET_JSF_VIEWHANDLER_PARAM_NAME, Boolean.class, false),

        /**
         * <code>javax.faces.DISABLE_FACESSERVLET_TO_XHTML</code> as {@link Boolean}.
         * @see FacesServlet#DISABLE_FACESSERVLET_TO_XHTML_PARAM_NAME
         */
        DISABLE_FACESSERVLET_TO_XHTML(FacesServlet.DISABLE_FACESSERVLET_TO_XHTML_PARAM_NAME, Boolean.class, false),

        /**
         * <code>javax.faces.ENABLE_CDI_RESOLVER_CHAIN</code> as {@link Boolean}.
         */
        ENABLE_CDI_RESOLVER_CHAIN("javax.faces.ENABLE_CDI_RESOLVER_CHAIN", Boolean.class, false),

        /**
         * <code>javax.faces.validator.ENABLE_VALIDATE_WHOLE_BEAN</code> as {@link Boolean}.
         * @see BeanValidator#ENABLE_VALIDATE_WHOLE_BEAN_PARAM_NAME
         */
        ENABLE_VALIDATE_WHOLE_BEAN(BeanValidator.ENABLE_VALIDATE_WHOLE_BEAN_PARAM_NAME, Boolean.class, false),

        /**
         * <code>javax.faces.ENABLE_WEBSOCKET_ENDPOINT</code> as {@link Boolean}.
         * @see PushContext#ENABLE_WEBSOCKET_ENDPOINT_PARAM_NAME
         */
        ENABLE_WEBSOCKET_ENDPOINT(PushContext.ENABLE_WEBSOCKET_ENDPOINT_PARAM_NAME, Boolean.class, false),

        /**
         * <code>javax.faces.FACELETS_BUFFER_SIZE</code> as {@link Integer}.
         * @see ViewHandler#FACELETS_BUFFER_SIZE_PARAM_NAME
         */
        FACELETS_BUFFER_SIZE(ViewHandler.FACELETS_BUFFER_SIZE_PARAM_NAME, Integer.class, 1024),

        /**
         * <code>javax.faces.FACELETS_DECORATORS</code> as {@link String} array.
         * @see ViewHandler#FACELETS_DECORATORS_PARAM_NAME
         */
        FACELETS_DECORATORS(ViewHandler.FACELETS_DECORATORS_PARAM_NAME, StringArray.SEMICOLON_SEPARATED, null),

        /**
         * <code>javax.faces.FACELETS_LIBRARIES</code> as {@link String} array.
         * @see ViewHandler#FACELETS_LIBRARIES_PARAM_NAME
         */
        FACELETS_LIBRARIES(ViewHandler.FACELETS_LIBRARIES_PARAM_NAME, StringArray.SEMICOLON_SEPARATED, null),

        /**
         * <code>javax.faces.FACELETS_REFRESH_PERIOD</code> as {@link Integer}.
         * @see ViewHandler#FACELETS_REFRESH_PERIOD_PARAM_NAME
         */
        FACELETS_REFRESH_PERIOD(ViewHandler.FACELETS_REFRESH_PERIOD_PARAM_NAME, Integer.class, 2),

        /**
         * <code>javax.faces.FACELETS_RESOURCE_RESOLVER</code> as <code>Class&lt;ResourceResolver&gt;</code>.
         * @see ResourceResolver#FACELETS_RESOURCE_RESOLVER_PARAM_NAME
         */
        FACELETS_RESOURCE_RESOLVER(ResourceResolver.FACELETS_RESOURCE_RESOLVER_PARAM_NAME, ResourceResolver.class, null),

        /**
         * <code>javax.faces.FACELETS_SKIP_COMMENTS</code> as {@link Boolean}.
         * @see ViewHandler#FACELETS_SKIP_COMMENTS_PARAM_NAME
         */
        FACELETS_SKIP_COMMENTS(ViewHandler.FACELETS_SKIP_COMMENTS_PARAM_NAME, Boolean.class, false),

        /**
         * <code>javax.faces.FACELETS_SUFFIX</code> as {@link String}.
         * @see ViewHandler#FACELETS_SUFFIX_PARAM_NAME
         */
        FACELETS_SUFFIX(ViewHandler.FACELETS_SUFFIX_PARAM_NAME, String.class, ViewHandler.DEFAULT_FACELETS_SUFFIX),

        /**
         * <code>javax.faces.FACELETS_VIEW_MAPPINGS</code> as {@link String} array.
         * @see ViewHandler#FACELETS_VIEW_MAPPINGS_PARAM_NAME
         */
        FACELETS_VIEW_MAPPINGS(ViewHandler.FACELETS_VIEW_MAPPINGS_PARAM_NAME, StringArray.SEMICOLON_SEPARATED, null),

        /**
         * <code>javax.faces.FULL_STATE_SAVING_VIEW_IDS</code> as {@link String} array.
         * @see StateManager#FULL_STATE_SAVING_VIEW_IDS_PARAM_NAME
         */
        FULL_STATE_SAVING_VIEW_IDS(StateManager.FULL_STATE_SAVING_VIEW_IDS_PARAM_NAME, StringArray.COMMA_SEPARATED, null),

        /**
         * <code>javax.faces.INTERPRET_EMPTY_STRING_SUBMITTED_VALUES_AS_NULL</code> as {@link Boolean}.
         * @see UIInput#EMPTY_STRING_AS_NULL_PARAM_NAME
         */
        INTERPRET_EMPTY_STRING_SUBMITTED_VALUES_AS_NULL(UIInput.EMPTY_STRING_AS_NULL_PARAM_NAME, Boolean.class, false),

        /**
         * <code>javax.faces.LIFECYCLE_ID</code> as <code>Class&lt;Lifecycle&gt;</code>.
         * @see FacesServlet#LIFECYCLE_ID_ATTR
         */
        LIFECYCLE_ID(FacesServlet.LIFECYCLE_ID_ATTR, Lifecycle.class, null),

        /**
         * <code>javax.faces.PARTIAL_STATE_SAVING</code> as {@link Boolean}.
         * @see StateManager#PARTIAL_STATE_SAVING_PARAM_NAME
         */
        PARTIAL_STATE_SAVING(StateManager.PARTIAL_STATE_SAVING_PARAM_NAME, Boolean.class, true),

        /**
         * <code>javax.faces.PROJECT_STAGE</code> as {@link ProjectStage}.
         * @see ProjectStage#PROJECT_STAGE_PARAM_NAME
         */
        PROJECT_STAGE(ProjectStage.PROJECT_STAGE_PARAM_NAME, ProjectStage.class, ProjectStage.Production),

        /**
         * <code>javax.faces.RESOURCE_EXCLUDES</code> as {@link String} array.
         * @see ResourceHandler#RESOURCE_EXCLUDES_PARAM_NAME
         */
        RESOURCE_EXCLUDES(ResourceHandler.RESOURCE_EXCLUDES_PARAM_NAME, StringArray.SPACE_SEPARATED, new String[] { ResourceHandler.RESOURCE_EXCLUDES_DEFAULT_VALUE, ".groovy" }),

        /**
         * <code>javax.faces.SERIALIZE_SERVER_STATE</code> as {@link Boolean}.
         * @see StateManager#SERIALIZE_SERVER_STATE_PARAM_NAME
         */
        SERIALIZE_SERVER_STATE(StateManager.SERIALIZE_SERVER_STATE_PARAM_NAME, Boolean.class, false),

        /**
         * <code>javax.faces.STATE_SAVING_METHOD</code> as {@link String}.
         * @see StateManager#STATE_SAVING_METHOD_PARAM_NAME
         */
        STATE_SAVING_METHOD(StateManager.STATE_SAVING_METHOD_PARAM_NAME, String.class, "server"),

        /**
         * <code>javax.faces.VALIDATE_EMPTY_FIELDS</code> as {@link String}.
         * @see UIInput#VALIDATE_EMPTY_FIELDS_PARAM_NAME
         */
        VALIDATE_EMPTY_FIELDS(UIInput.VALIDATE_EMPTY_FIELDS_PARAM_NAME, String.class, "auto"),

        /**
         * <code>javax.faces.VIEWROOT_PHASE_LISTENER_QUEUES_EXCEPTIONS</code> as {@link Boolean}.
         * @see UIViewRoot#VIEWROOT_PHASE_LISTENER_QUEUES_EXCEPTIONS_PARAM_NAME
         */
        VIEWROOT_PHASE_LISTENER_QUEUES_EXCEPTIONS(UIViewRoot.VIEWROOT_PHASE_LISTENER_QUEUES_EXCEPTIONS_PARAM_NAME, Boolean.class, false),

        /**
         * <code>javax.faces.WEBAPP_CONTRACTS_DIRECTORY</code> as {@link Path}.
         * @see ResourceHandler#WEBAPP_CONTRACTS_DIRECTORY_PARAM_NAME
         */
        WEBAPP_CONTRACTS_DIRECTORY(ResourceHandler.WEBAPP_CONTRACTS_DIRECTORY_PARAM_NAME, Path.class, Paths.get("/contracts")),

        /**
         * <code>javax.faces.WEBAPP_RESOURCES_DIRECTORY</code> as {@link Path}.
         * @see ResourceHandler#WEBAPP_RESOURCES_DIRECTORY_PARAM_NAME
         */
        WEBAPP_RESOURCES_DIRECTORY(ResourceHandler.WEBAPP_RESOURCES_DIRECTORY_PARAM_NAME, Path.class, Paths.get("/resources")),

        /**
         * <code>javax.faces.WEBSOCKET_ENDPOINT_PORT</code> as {@link Integer}.
         * @see PushContext#WEBSOCKET_ENDPOINT_PORT_PARAM_NAME
         */
        WEBSOCKET_ENDPOINT_PORT(PushContext.WEBSOCKET_ENDPOINT_PORT_PARAM_NAME, Integer.class, null),

        ;

        private static final Map<String, Object> VALUES = new ConcurrentHashMap<>(ContextParameter.values().length);

        private enum StringArray {
            SPACE_SEPARATED(Pattern.compile("\\s+")),
            SEMICOLON_SEPARATED(Pattern.compile("\\s*;\\s*")),
            COMMA_SEPARATED(Pattern.compile("\\s*,\\s*"));

            private Pattern pattern;

            private StringArray(Pattern pattern) {
                this.pattern = pattern;
            }

            public String[] split(String value) {
                return pattern.split(value);
            }
        }

        private String name;
        private Class<?> type;
        private StringArray separated;
        private Object defaultValue;

        private <T> ContextParameter(String name, Class<T> type, T defaultValue) {
            this.name = name;
            this.type = type;
            this.defaultValue = defaultValue;
        }

        private ContextParameter(String name, StringArray separated, String[] defaultValue) {
            this(name, String[].class, defaultValue);
            this.separated = separated;
        }

        /**
         * <p>
         * Returns the name of the context parameter.
         * @return The name of the context parameter.
         */
        public String getName() {
            return name;
        }

        /**
         * <p>
         * Returns the expected type of the context parameter value.
         * Supported values are:
         * <ul>
         * <li>{@link String}
         * <li>{@link String} array
         * <li>{@link Boolean}
         * <li>{@link Integer}
         * <li>{@link Path}
         * <li>{@link Enum}
         * <li>{@link Class}
         * </ul>
         * @return The expected type of the context parameter value.
         */
        public Class<?> getType() {
            return type;
        }

        /**
         * <p>
         * Returns the value of the context parameter, converted to the expected type as indicated by {@link #getType()}.
         * @param <T> The expected return type.
         * @param context The involved faces context.
         * @return The value of the context parameter, converted to the expected type as indicated by {@link #getType()}.
         * @throws ClassCastException When inferred T is of wrong type. See {@link #getType()} for the correct type.
         */
        @SuppressWarnings("unchecked")
        public <T> T getValue(FacesContext context) {
            if (VALUES.containsKey(name)) {
                return (T) VALUES.get(name);
            }

            String param = context.getExternalContext().getInitParameter(name);
            Object value = null;

            if (param == null) {
                value = defaultValue;
            }
            else if (type == String.class) {
                value = param;
            }
            else if (type == String[].class) {
                value = separated.split(param);
            }
            else if (type == Boolean.class) {
                value = Boolean.valueOf(param);
            }
            else if (type == Integer.class) {
                try {
                    value = Integer.valueOf(param);
                }
                catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Context parameter '" + name + "' value '" + param + "' must represent a valid integer", e);
                }
            }
            else if (type == Path.class) {
                value = Paths.get(param);
            }
            else if (type.isEnum()) {
                for (Object constant : type.getEnumConstants()) {
                    if (constant.toString().equalsIgnoreCase(param)) {
                        value = constant;
                        break;
                    }
                }

                if (value == null) {
                    throw new IllegalArgumentException("Context parameter '" + name + "' value '" + param + "' must represent a valid enum of type " + type.getName());
                }
            }
            else {
                try {
                    value = Class.forName(param);
                }
                catch (ClassNotFoundException e) {
                    throw new IllegalArgumentException("Context parameter '" + name + "' value '" + param + "' must represent a valid fully qualified name", e);
                }
            }

            VALUES.putIfAbsent(name, value);
            return (T) value;
        }
    }


    @Nonbinding Version version() default JSF_2_2;

}
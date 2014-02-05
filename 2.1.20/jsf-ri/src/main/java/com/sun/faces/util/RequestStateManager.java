/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2010 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.faces.util;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;

import com.sun.faces.RIConstants;

import java.util.Map;
import java.util.HashMap;

/**
 * <p>
 * This helper class is used a central location for per-request state
 * that is needed by Mojarra.  This class leverages FacesContext.getAttributes()
 * which as added in 2.0 instead of the request scope to prevent the unecessary
 * triggering of ServletREquestAttributeListeners.
 * </p>
 */
public class RequestStateManager {

    /**
     * Attribute for storing any content within a page that is defined
     * after the closing f:view.
     */
    public static final String AFTER_VIEW_CONTENT =
          RIConstants.FACES_PREFIX + "AFTER_VIEW_CONTENT";

    /**
     * Attribute describing the current ELResolver chain type (either JSP
     * or Faces)
     */
    public static final String EL_RESOLVER_CHAIN_TYPE_NAME =
          RIConstants.FACES_PREFIX + "ELResolverChainType";

    /**
     * Attribute indicating the current component being processed.
     * This will be used when generating bytecode for custom converters.
     */
    public static final String TARGET_COMPONENT_ATTRIBUTE_NAME =
          RIConstants.FACES_PREFIX + "ComponentForValue";    

    /**
     * Attribute defining the {@link javax.faces.render.RenderKit} being used
     * for this request.
     */
    public static final String RENDER_KIT_IMPL_REQ =
          RIConstants.FACES_PREFIX + "renderKitImplForRequest";

    /**
     * This attribute is used by the StateMangaer during restore view.
     * The values are stored in the request for later use.
     */
    public static final String LOGICAL_VIEW_MAP =
          RIConstants.FACES_PREFIX + "logicalViewMap";

    /**
     * This attribute is used by the StateMangaer during restore view.
     * The values are stored in the request for later use.
     */
    public static final String ACTUAL_VIEW_MAP =
          RIConstants.FACES_PREFIX + "actualViewMap";

    /**
     * This attribute is used by the loadBundle tag for tracking views/subviews
     * within the logical view (this is only used when 1.1 compatibility is
     * enabled).
     */
    public static final String VIEWTAG_STACK_ATTR_NAME =
          RIConstants.FACES_PREFIX + "taglib.jsf_core.VIEWTAG_STACK";

    /**
     * Attribute to store the {@link javax.faces.webapp.FacesServlet} path of
     * the original request.
     */
    public static final String INVOCATION_PATH =
          RIConstants.FACES_PREFIX + "INVOCATION_PATH";

    /**
     * This attribute protects against infinite loops on expressions that
     * touch a custom legacy VariableResolver that delegates to its parent
     * VariableResolver.
     */
    public static final String REENTRANT_GUARD =
          RIConstants.FACES_PREFIX + "LegacyVariableResolver";

    /**
     * Leveraged by the RequestStateManager to allow deprecated ResponseStateManager
     * methods to continue to work if called.
     */
    public static final String FACES_VIEW_STATE =
          "com.sun.faces.FACES_VIEW_STATE";


    /**
     * Leveraged by ResourceHandlerImpl to denote whether or not a request
     * is a resource request.  A <code>Boolean</code> value will be assoicated
     * with this key.
     */
    public static final String RESOURCE_REQUEST =
          "com.sun.faces.RESOURCE_REQUEST";

    /**
     * Used to store the FaceletFactory as other components may need to
     * use it during their processing. 
     */
    public static final String FACELET_FACTORY =
          "com.sun.faces.FACELET_FACTORY";

    /**
     * Used to indicate whether or not jsf.js has already be rendered.
     */
    public static final String SCRIPT_STATE =
          "com.sun.faces.SCRIPT_STATE";


    /**
     * Used to communicate which validators have been disabled for a particular
     * nesting level within a view.
     */
    public static final String DISABLED_VALIDATORS =
          "com.sun.faces.DISABLED_VALIDATORS";


    /**
     * Used to store the Set of ResourceDependency annotations that have
     * been processed.
     */
    public static final String PROCESSED_RESOURCE_DEPENDENCIES =
          "com.sun.faces.PROCESSED_RESOURCE_DEPENDENCIES";


    private static final String[] RENDER_RESPONSE = {
          SCRIPT_STATE,
          PROCESSED_RESOURCE_DEPENDENCIES
    };

    /**
     * <p>The key under with the Map containing the implementation specific
     * attributes will be stored within the request.<p>
     */
    private static final String KEY =
          RequestStateManager.class.getName();


    private static final Map<PhaseId,String[]> PHASE_ATTRIBUTES =
        new HashMap<PhaseId,String[]>(2, 1.0f);

    static {
        PHASE_ATTRIBUTES.put(PhaseId.RENDER_RESPONSE, RENDER_RESPONSE);
    }


    // ---------------------------------------------------------- Public Methods


    /**
     * @param ctx the <code>FacesContext</code> for the current request
     * @param key the key for the value
     * @return the value associated with the specified key.
     */
    public static Object get(FacesContext ctx, String key) {

        if (ctx == null || key == null) {
            return null;
        }
        return ctx.getAttributes().get(key);

    }


    /**
     * <p>
     * Adds the specified key and value to the Map stored in the request.
     * If <code>value</code> is <code>null</code>, that key/value pair will
     * be removed from the Map.
     * </p>
     *
     * @param ctx the <code>FacesContext</code> for the current request
     * @param key the key for the value
     * @param value the value to store
     */
    public static void set(FacesContext ctx, String key, Object value) {

        if (ctx == null || key == null) {
            return;
        }
        if (value == null) {
            remove(ctx, key);
        }
        ctx.getAttributes().put(key, value);

    }


    /**
     * <p>
     * Remove the value associated with the specified key.
     * </p>
     *
     * @param ctx the <code>FacesContext</code> for the current request
     * @param key the key for the value
     * @return the value previous associated with the specified key, if any
     */
    public static Object remove(FacesContext ctx, String key) {

        if (ctx == null || key == null) {
            return null;
        }

        return ctx.getAttributes().remove(key);

    }


    /**
     * <p>
     * Remove all request state attributes associated that need to be cleared
     * before the execution of a particular lifecycle phase.
     * </p>
     * @param ctx the <code>FacesContext</code> for the current request
     * @param phaseId the phase used to obtain the associated attributes
     */
    public static void clearAttributesForPhase(FacesContext ctx,
                                               PhaseId phaseId) {

        if (ctx == null || phaseId == null) {
            return;
        }
        String[] phaseAttributes = PHASE_ATTRIBUTES.get(phaseId);
        if (phaseAttributes != null) {
            Map<Object,Object> attrs = ctx.getAttributes();
            for (String key : phaseAttributes) {
                attrs.remove(key);
            }
        }

    }


    /**
     * @param ctx the <code>FacesContext</code> for the current request
     * @param key the key for the value
     * @return true if the specified key exists in the Map
     */
    public static boolean containsKey(FacesContext ctx, String key) {

        return !(ctx == null || key == null) && ctx.getAttributes()
              .containsKey(key);

    }

    /**
     * @param ctx the <code>FacesContext</code> for the current request
     * @return the Map from the request containing the implementation specific
     *  attributes needed for processing
     */
    public static Map<String,Object> getStateMap(FacesContext ctx) {

        assert (ctx != null); // all callers guard against a null context
        Map<Object,Object> contextMap = ctx.getAttributes();
        //noinspection unchecked
        Map<String,Object> reqState = (Map<String,Object>) contextMap.get(KEY);
        if (reqState == null) {
            reqState = new HashMap<String,Object>();
            contextMap.put(KEY, reqState);
        }
        return reqState;

    }


}

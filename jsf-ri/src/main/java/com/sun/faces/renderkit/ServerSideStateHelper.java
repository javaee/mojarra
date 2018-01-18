/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2014 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.faces.renderkit;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.Map;
import java.security.SecureRandom;
import java.util.concurrent.atomic.AtomicInteger;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;

import javax.faces.context.FacesContext;
import javax.faces.context.ExternalContext;
import javax.faces.context.ResponseWriter;
import javax.faces.FacesException;
import javax.faces.component.NamingContainer;
import javax.faces.component.UIViewRoot;

import com.sun.faces.config.WebConfiguration.WebContextInitParameter;
import static com.sun.faces.config.WebConfiguration.BooleanWebContextInitParameter.SerializeServerStateDeprecated;
import static com.sun.faces.config.WebConfiguration.BooleanWebContextInitParameter.SerializeServerState;
import static com.sun.faces.config.WebConfiguration.BooleanWebContextInitParameter.GenerateUniqueServerStateIds;
import com.sun.faces.util.FacesLogger;
import com.sun.faces.util.TypedCollections;
import com.sun.faces.util.LRUMap;
import com.sun.faces.util.Util;
import com.sun.faces.util.RequestStateManager;

import static com.sun.faces.config.WebConfiguration.BooleanWebContextInitParameter.AutoCompleteOffOnViewState;
import static com.sun.faces.config.WebConfiguration.BooleanWebContextInitParameter.EnableViewStateIdRendering;
import static com.sun.faces.config.WebConfiguration.BooleanWebContextInitParameter.NamespaceParameters;
import static com.sun.faces.config.WebConfiguration.WebContextInitParameter.NumberOfLogicalViews;
import static com.sun.faces.config.WebConfiguration.WebContextInitParameter.NumberOfViews;
import com.sun.faces.config.WebConfiguration;
import java.util.Collections;
import javax.faces.render.ResponseStateManager;

/**
 * <p>
 * This <code>StateHelper</code> provides the functionality associated with server-side state saving,
 * though in actuallity, it is a hybrid between client and server.
 * </p>
 */
public class ServerSideStateHelper extends StateHelper {

    private static final Logger LOGGER = FacesLogger.APPLICATION.getLogger();

    /**
     * Key to store the <code>AtomicInteger</code> used to generate
     * unique state map keys.
     */
    public static final String STATEMANAGED_SERIAL_ID_KEY =
          ServerSideStateHelper.class.getName() + ".SerialId";

    /**
     * The top level attribute name for storing the state structures within
     * the session.
     */
    public static final String LOGICAL_VIEW_MAP =
          ServerSideStateHelper.class.getName() + ".LogicalViewMap";

    /**
     * The number of logical views as configured by the user.
     */
    protected final Integer numberOfLogicalViews;


    /**
     * The number of views as configured by the user.
     */
    protected final Integer numberOfViews;


    /**
     * Flag determining how server state IDs are generated.
     */
    protected boolean generateUniqueStateIds;


    /**
     * Flag determining whether or not javax.faces.ViewState should be namespaced.
     */
    protected boolean namespaceParameters;


    /**
     * Used to generate unique server state IDs.
     */
    protected final SecureRandom random;


    // ------------------------------------------------------------ Constructors


    /**
     * Construct a new <code>ServerSideStateHelper</code> instance.
     */
    public ServerSideStateHelper() {

        numberOfLogicalViews = getIntegerConfigValue(NumberOfLogicalViews);
        numberOfViews = getIntegerConfigValue(NumberOfViews);
        WebConfiguration webConfig = WebConfiguration.getInstance();
        generateUniqueStateIds =
              webConfig.isOptionEnabled(GenerateUniqueServerStateIds);
        if (generateUniqueStateIds) {
            // Construct secure RNG.
            random = new SecureRandom();
 
            // Make sure SecureRandom will seed itself safely by generating a random byte. This assures that an 
            // accidental invocation of setSeed will not break security.
            random.nextBytes(new byte[1]);
        } else {
            random = null;
        }
        namespaceParameters = webConfig.isOptionEnabled(NamespaceParameters);

    }


    // ------------------------------------------------ Methods from StateHelper


    /**
     * <p>
     * Stores the provided state within the session obtained from the provided
     * <code>FacesContext</code>
     * </p>
     *
     * <p>If <code>stateCapture</code> is <code>null</code>, the composite
     * key used to look up the actual and logical views will be written to
     * the client as a hidden field using the <code>ResponseWriter</code>
     * from the provided <code>FacesContext</code>.</p>
     *
     * <p>If <code>stateCapture</code> is not <code>null</code>, the composite
     * key will be appended to the <code>StringBuilder<code> without any markup
     * included or any content written to the client.
     */
    public void writeState(FacesContext ctx,
                           Object state,
                           StringBuilder stateCapture)
    throws IOException {

        Util.notNull("context", ctx);

        String id;
        
        UIViewRoot viewRoot = ctx.getViewRoot();

        if (!viewRoot.isTransient()) {
            if (!ctx.getAttributes().containsKey("com.sun.faces.ViewStateValue")) {
                Util.notNull("state", state);
                Object[] stateToWrite = (Object[]) state;
                ExternalContext externalContext = ctx.getExternalContext();
                Object sessionObj = externalContext.getSession(true);
                Map<String, Object> sessionMap = externalContext.getSessionMap();

                //noinspection SynchronizationOnLocalVariableOrMethodParameter
                synchronized (sessionObj) {
                    Map<String, Map> logicalMap = TypedCollections.dynamicallyCastMap(
                          (Map) sessionMap
                                .get(LOGICAL_VIEW_MAP), String.class, Map.class);
                    if (logicalMap == null) {
                        logicalMap = Collections.synchronizedMap(new LRUMap<String, Map>(numberOfLogicalViews));
                        sessionMap.put(LOGICAL_VIEW_MAP, logicalMap);
                    }

                    Object structure = stateToWrite[0];
                    Object savedState = handleSaveState(stateToWrite[1]);

                    String idInLogicalMap = (String)
                              RequestStateManager.get(ctx, RequestStateManager.LOGICAL_VIEW_MAP);
                    if (idInLogicalMap == null) {
                        idInLogicalMap = ((generateUniqueStateIds)
                                              ? createRandomId()
                                              : createIncrementalRequestId(ctx));
                    }
                    String idInActualMap = null;
                    if(ctx.getPartialViewContext().isPartialRequest()){
                        // If partial request, do not change actual view Id, because page not actually changed.
                        // Otherwise partial requests will soon overflow cache with values that would be never used.
                        idInActualMap = (String) RequestStateManager.get(ctx, RequestStateManager.ACTUAL_VIEW_MAP);
                    }
                    if (null == idInActualMap) {
                            idInActualMap = ((generateUniqueStateIds) ? createRandomId()
                                                        : createIncrementalRequestId(ctx));
                    }
                    Map<String, Object[]> actualMap =
                          TypedCollections.dynamicallyCastMap(
                                logicalMap.get(idInLogicalMap), String.class, Object[].class);
                    if (actualMap == null) {
                        actualMap = new LRUMap<String, Object[]>(numberOfViews);
                        logicalMap.put(idInLogicalMap, actualMap);
                    }

                    id = idInLogicalMap + ':' + idInActualMap;

                    Object[] stateArray = actualMap.get(idInActualMap);
                    // reuse the array if possible
                    if (stateArray != null) {
                        stateArray[0] = structure;
                        stateArray[1] = savedState;
                    } else {
                        actualMap.put(idInActualMap, new Object[]{ structure, savedState });
                    }

                    // always call put/setAttribute as we may be in a clustered environment.
                    sessionMap.put(LOGICAL_VIEW_MAP, logicalMap);
                    ctx.getAttributes().put("com.sun.faces.ViewStateValue", id);
                }
            } else {
                id = (String) ctx.getAttributes().get("com.sun.faces.ViewStateValue");
            }
        } else {
            id = "stateless";
        }
        
        if (stateCapture != null) {
            stateCapture.append(id);
        } else {
            ResponseWriter writer = ctx.getResponseWriter();

            writer.startElement("input", null);
            writer.writeAttribute("type", "hidden", null);

            String viewStateParam = ResponseStateManager.VIEW_STATE_PARAM;
            
            if ((namespaceParameters) && (viewRoot instanceof NamingContainer)) {
                String namingContainerId = viewRoot.getContainerClientId(ctx);
                if (namingContainerId != null) {
            	    viewStateParam = namingContainerId + viewStateParam;
                }
            }
            writer.writeAttribute("name", viewStateParam, null);
            if (webConfig.isOptionEnabled(EnableViewStateIdRendering)) {
                String viewStateId = Util.getViewStateId(ctx);
                writer.writeAttribute("id", viewStateId, null);
            }
            writer.writeAttribute("value", id, null);
            if (webConfig.isOptionEnabled(AutoCompleteOffOnViewState)) {
                writer.writeAttribute("autocomplete", "off", null);
            }
            writer.endElement("input");

            writeClientWindowField(ctx, writer); 
            writeRenderKitIdField(ctx, writer);
        }
    }


    /**
     * <p>Inspects the incoming request parameters for the standardized state
     * parameter name.  In this case, the parameter value will be the composite
     * ID generated by ServerSideStateHelper#writeState(FacesContext, Object, StringBuilder).</p>
     *
     * <p>The composite key will be used to find the appropriate view within the
     * session obtained from the provided <code>FacesContext</code>
     */
    public Object getState(FacesContext ctx, String viewId) {

        String compoundId = getStateParamValue(ctx);
        
        if (compoundId == null) {
            return null;
        }
        
        if ("stateless".equals(compoundId)) {
            return "stateless";
        }

        int sep = compoundId.indexOf(':');
        assert (sep != -1);
        assert (sep < compoundId.length());

        String idInLogicalMap = compoundId.substring(0, sep);
        String idInActualMap = compoundId.substring(sep + 1);

        ExternalContext externalCtx = ctx.getExternalContext();
        Object sessionObj = externalCtx.getSession(false);

        // stop evaluating if the session is not available
        if (sessionObj == null) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE,
                           "Unable to restore server side state for view ID {0} as no session is available",
                           viewId);
            }
            return null;
        }

        //noinspection SynchronizationOnLocalVariableOrMethodParameter
        synchronized (sessionObj) {
            Map logicalMap = (Map) externalCtx.getSessionMap() .get(LOGICAL_VIEW_MAP);
            if (logicalMap != null) {
                Map actualMap = (Map) logicalMap.get(idInLogicalMap);
                if (actualMap != null) {
                    RequestStateManager.set(ctx,
                                            RequestStateManager.LOGICAL_VIEW_MAP,
                                            idInLogicalMap);
                    
                    Object[] restoredState = new Object[2];
                    Object[] state = (Object[]) actualMap.get(idInActualMap);
                    if(state != null){
                        restoredState[0] = state[0];
                        restoredState[1] = state[1];
                        
                        RequestStateManager.set(ctx,
                                                RequestStateManager.ACTUAL_VIEW_MAP,
                                                idInActualMap);
                        if (state.length == 2 && state[1] != null) {
                            restoredState[1] = handleRestoreState(state[1]);
                        }
                    }

                    return restoredState;
                }
            }
        }

        return null;
        
    }

   
    // ------------------------------------------------------- Protected Methods


    /**
     * <p>Utility method for obtaining the <code>Integer</code> based configuration
     * values used to change the behavior of the <code>ServerSideStateHelper</code>.
     * @param param the paramter to parse
     * @return the Integer representation of the parameter value
     */
    protected Integer getIntegerConfigValue(WebContextInitParameter param) {

        String noOfViewsStr = webConfig.getOptionValue(param);
        Integer value = null;
        try {
            value = Integer.valueOf(noOfViewsStr);
        } catch (NumberFormatException nfe) {
            String defaultValue = param.getDefaultValue();
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.log(Level.WARNING,
                           "jsf.state.server.cannot.parse.int.option",
                           new Object[] { param.getQualifiedName(),
                                          defaultValue} );
            }
            try {
                value = Integer.valueOf(defaultValue);
            } catch (NumberFormatException ne) {
                if (LOGGER.isLoggable(Level.FINEST)) {
                    LOGGER.log(Level.FINEST, "Unable to convert number", ne);
                }
            }
        }

        return value;

    }


    /**
     * @param state the object returned from <code>UIView.processSaveState</code>
     * @return If {@link com.sun.faces.config.WebConfiguration.BooleanWebContextInitParameter#SerializeServerStateDeprecated} is
     *  <code>true</code>, serialize and return the state, otherwise, return
     *  <code>state</code> unchanged.
     */
    protected Object handleSaveState(Object state) {

        if (webConfig.isOptionEnabled(SerializeServerStateDeprecated) || webConfig.isOptionEnabled(SerializeServerState)) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
            ObjectOutputStream oas = null;
            try {
                oas = serialProvider
                      .createObjectOutputStream(((compressViewState)
                                                 ? new GZIPOutputStream(baos, 1024)
                                                 : baos));
                //noinspection NonSerializableObjectPassedToObjectStream
                oas.writeObject(state);
                oas.flush();
            } catch (Exception e) {
                throw new FacesException(e);
            } finally {
                if (oas != null) {
                    try {
                        oas.close();
                    } catch (IOException ioe) { 
                        if (LOGGER.isLoggable(Level.FINEST)) {
                            LOGGER.log(Level.FINEST, "Closing stream", ioe);
                        }
                    }
                }
            }
            return baos.toByteArray();
        } else {
            return state;
        }

    }


    /**
     * @param state the state as it was stored in the session
     * @return an object that can be passed to <code>UIViewRoot.processRestoreState</code>.
     *  If {@link com.sun.faces.config.WebConfiguration.BooleanWebContextInitParameter#SerializeServerStateDeprecated} de-serialize the
     *  state prior to returning it, otherwise return <code>state</code> as is.
     */
    protected Object handleRestoreState(Object state) {

        if (webConfig.isOptionEnabled(SerializeServerStateDeprecated) || webConfig.isOptionEnabled(SerializeServerState)) {
            ByteArrayInputStream bais = new ByteArrayInputStream((byte[]) state);
            ObjectInputStream ois = null;
            try {
                ois = serialProvider
                      .createObjectInputStream(((compressViewState)
                                                ? new GZIPInputStream(bais, 1024)
                                                : bais));
                return ois.readObject();
            } catch (Exception e) {
                throw new FacesException(e);
            } finally {
                if (ois != null) {
                    try {
                        ois.close();
                    } catch (IOException ioe) { 
                        if (LOGGER.isLoggable(Level.FINEST)) {
                            LOGGER.log(Level.FINEST, "Closing stream", ioe);
                        }
                    }
                }
            }
        } else {
            return state;
        }

    }


     /**
     * @param ctx the <code>FacesContext</code> for the current request
     * @return a unique ID for building the keys used to store
     *  views within a session
     */
    private String createIncrementalRequestId(FacesContext ctx) {

        Map<String, Object> sm = ctx.getExternalContext().getSessionMap();
        AtomicInteger idgen =
              (AtomicInteger) sm.get(STATEMANAGED_SERIAL_ID_KEY);
        if (idgen == null) {
            idgen = new AtomicInteger(1);
        }

        // always call put/setAttribute as we may be in a clustered environment.
        sm.put(STATEMANAGED_SERIAL_ID_KEY, idgen);
        return (UIViewRoot.UNIQUE_ID_PREFIX + idgen.getAndIncrement());

    }


    private String createRandomId() {

        return Long.valueOf(random.nextLong()).toString();

    }

    /**
     * Is stateless.
     * 
     * @param facesContext the Faces context.
     * @param viewId the view id.
     * @return true if stateless, false otherwise.
     * @throws IllegalStateException when the request was not a postback.
     */
    @Override
    public boolean isStateless(FacesContext facesContext, String viewId) throws IllegalStateException {
        if (facesContext.isPostback()) {
            Object stateObject = getState(facesContext, viewId);
            if (stateObject instanceof String && "stateless".equals((String) stateObject)) {
                return true;
            }

            return false;
        }
        
        throw new IllegalStateException("Cannot determine whether or not the request is stateless");
    }
}

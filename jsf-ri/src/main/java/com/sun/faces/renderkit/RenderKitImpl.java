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

// RenderKitImpl.java

package com.sun.faces.renderkit;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseStream;
import javax.faces.context.ResponseWriter;
import javax.faces.render.ClientBehaviorRenderer;
import javax.faces.render.RenderKit;
import javax.faces.render.Renderer;
import javax.faces.render.ResponseStateManager;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Collections;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.concurrent.ConcurrentHashMap;

import com.sun.faces.RIConstants;
import com.sun.faces.config.WebConfiguration;
import static com.sun.faces.config.WebConfiguration.WebContextInitParameter.DisableUnicodeEscaping;
import static com.sun.faces.config.WebConfiguration.BooleanWebContextInitParameter.EnableJSStyleHiding;
import static com.sun.faces.config.WebConfiguration.BooleanWebContextInitParameter.EnableScriptInAttributeValue;
import static com.sun.faces.config.WebConfiguration.BooleanWebContextInitParameter.PreferXHTMLContentType;
import com.sun.faces.renderkit.html_basic.HtmlResponseWriter;
import com.sun.faces.util.MessageUtils;
import com.sun.faces.util.Util;
import com.sun.faces.util.FacesLogger;

/**
 * <B>RenderKitImpl</B> is a class ...
 * <p/>
 * <B>Lifetime And Scope</B> <P>
 *
 */

public class RenderKitImpl extends RenderKit {

    private static final Logger LOGGER = FacesLogger.RENDERKIT.getLogger();

    private static final String SUPPORTED_CONTENT_TYPES =
         RIConstants.HTML_CONTENT_TYPE + ','
              + RIConstants.XHTML_CONTENT_TYPE + ','
              + RIConstants.APPLICATION_XML_CONTENT_TYPE + ','
              + RIConstants.TEXT_XML_CONTENT_TYPE;


    /**
     * Keys are String renderer family.  Values are HashMaps.  Nested
     * HashMap keys are Strings for the rendererType, and values are the
     * Renderer instances themselves.
     */

    private ConcurrentHashMap<String, HashMap<String, Renderer>> rendererFamilies =
         new ConcurrentHashMap<String, HashMap<String, Renderer>>();

    /**
     * For Behavior Renderers:
     * Keys are Strings for the behaviorRendererType, and values are the 
     * behaviorRenderer instances themselves.
     */

    private ConcurrentHashMap<String, ClientBehaviorRenderer> behaviorRenderers = 
        new ConcurrentHashMap<>();


    private ResponseStateManager responseStateManager =
         new ResponseStateManagerImpl();

    private WebConfiguration webConfig;

    public RenderKitImpl() {

        FacesContext context = FacesContext.getCurrentInstance();
        webConfig = WebConfiguration.getInstance(context.getExternalContext());

    }
    
    @Override
    public void addRenderer(String family,
                            String rendererType,
                            Renderer renderer) {

        Util.notNull("family", family);
        Util.notNull("rendererType", rendererType);
        Util.notNull("renderer", renderer);

        HashMap<String,Renderer> renderers = rendererFamilies.get(family);
        if (renderers == null) {
            renderers = new HashMap<>();
            rendererFamilies.put(family, renderers);
        }

        if (LOGGER.isLoggable(Level.FINE) && renderers.containsKey(rendererType)) {
            LOGGER.log(Level.FINE,
                       "rendererType {0} has already been registered for family {1}.  Replacing existing renderer class type {2} with {3}.",
                       new Object[] { rendererType, family, renderers.get(rendererType).getClass().getName(), renderer.getClass().getName() });
        }
        renderers.put(rendererType, renderer);

    }


    @Override
    public Renderer getRenderer(String family, String rendererType) {

        Util.notNull("family", family);
        Util.notNull("rendererType", rendererType);

        assert(rendererFamilies != null);

        HashMap<String,Renderer> renderers = rendererFamilies.get(family);
        return ((renderers != null) ? renderers.get(rendererType) : null);

    }

    @Override
    public void addClientBehaviorRenderer(String behaviorRendererType,
                                          ClientBehaviorRenderer behaviorRenderer) {

        Util.notNull("behaviorRendererType", behaviorRendererType);
        Util.notNull("behaviorRenderer", behaviorRenderer);

        if (LOGGER.isLoggable(Level.FINE) && behaviorRenderers.containsKey(behaviorRendererType)) {
            LOGGER.log(Level.FINE,
                       "behaviorRendererType {0} has already been registered.  Replacing existing behavior renderer class type {1} with {2}.",
                       new Object[] { behaviorRendererType, behaviorRenderers.get(behaviorRendererType).getClass().getName(), behaviorRenderer.getClass().getName() });
        }
        behaviorRenderers.put(behaviorRendererType, behaviorRenderer);
    
    }

    @Override
    public ClientBehaviorRenderer getClientBehaviorRenderer(String behaviorRendererType) {

        Util.notNull("behaviorRendererType", behaviorRendererType);
        
        return ((behaviorRenderers != null) ? behaviorRenderers.get(behaviorRendererType) : null);
            
    }   

    @Override
    public Iterator<String> getClientBehaviorRendererTypes() {
        if (null == behaviorRenderers) {
            Set<String> empty = Collections.emptySet();
            return empty.iterator();
        }
        return behaviorRenderers.keySet().iterator();
    }


    @Override
    public synchronized ResponseStateManager getResponseStateManager() {
        if (responseStateManager == null) {
            responseStateManager = new ResponseStateManagerImpl();
        }
        return responseStateManager;
    }


    @Override
    public ResponseWriter createResponseWriter(Writer writer,
                                               String desiredContentTypeList,
                                               String characterEncoding) {
        if (writer == null) {
            return null;
        }
        String contentType = null;
        boolean contentTypeNullFromResponse = false;
        FacesContext context = FacesContext.getCurrentInstance();

        // Step 1: Check the content type passed into this method 
        if (null != desiredContentTypeList) {
            contentType = getSupportedContentType(desiredContentTypeList);
        }

        // Step 2: Check the response content type
        if (null == desiredContentTypeList) {
            desiredContentTypeList =
                 context.getExternalContext().getResponseContentType();
            if (null != desiredContentTypeList) {
                contentType = getSupportedContentType(desiredContentTypeList);
                if (null == contentType) {
                    contentTypeNullFromResponse = true;
                }
            }
        }

        boolean isPartial = context.getPartialViewContext().isPartialRequest();

        // Step 3: Check the Accept Header content type
        // Evaluate the accept header in accordance with HTTP specification - 
        // Section 14.1
        // Preconditions for this (1 or 2):
        //  1. content type was not specified to begin with
        //  2. an unsupported content type was retrieved from the response 
        if (null == desiredContentTypeList || contentTypeNullFromResponse) {
            String[] typeArray =
                 context.getExternalContext().getRequestHeaderValuesMap().get("Accept");
            if (typeArray.length > 0) {
                StringBuffer buff = new StringBuffer();
                buff.append(typeArray[0]);
                for (int i = 1, len = typeArray.length; i < len; i++) {
                    buff.append(',');
                    buff.append(typeArray[i]);
                }
                desiredContentTypeList = buff.toString();
            }

            if (null != desiredContentTypeList) {
                desiredContentTypeList =
                      RenderKitUtils.determineContentType(desiredContentTypeList,
                                                          SUPPORTED_CONTENT_TYPES,
                                                          getDefaultContentType(isPartial, null));
                if (null != desiredContentTypeList) {
                    contentType = getSupportedContentType(desiredContentTypeList);
                }
            }
        }

        // Step 4: Default to text/html (or text/xml during Ajax)
        if (contentType == null) {
            if (null == desiredContentTypeList) {
                contentType = getDefaultContentType(isPartial, RIConstants.HTML_CONTENT_TYPE);
            } else {
                String[] desiredContentTypes =
                      contentTypeSplit(desiredContentTypeList);
                for (String desiredContentType : desiredContentTypes) {
                    if (RIConstants.ALL_MEDIA.equals(desiredContentType.trim())) {
                        contentType = getDefaultContentType(isPartial, RIConstants.HTML_CONTENT_TYPE);
                    }
                }
            }
        }

        if (null == contentType) {
            throw new IllegalArgumentException(MessageUtils.getExceptionMessageString(
                 MessageUtils.CONTENT_TYPE_ERROR_MESSAGE_ID));
        }

        if (characterEncoding == null) {
            characterEncoding = RIConstants.CHAR_ENCODING;
        }

        boolean scriptHiding = webConfig.isOptionEnabled(EnableJSStyleHiding);
        boolean scriptInAttributes = webConfig.isOptionEnabled( EnableScriptInAttributeValue);
        WebConfiguration.DisableUnicodeEscaping escaping =
              WebConfiguration.DisableUnicodeEscaping.getByValue(
                    webConfig.getOptionValue(DisableUnicodeEscaping));
        return new HtmlResponseWriter(writer,
                                      contentType,
                                      characterEncoding,
                                      scriptHiding,
                                      scriptInAttributes,
                                      escaping,
                                      isPartial);
    }


    private boolean preferXhtml() {

        return webConfig.isOptionEnabled(PreferXHTMLContentType);

    }


    private String getDefaultContentType(boolean isPartial, String defaultContentType) {
        if (isPartial) {
            return RIConstants.TEXT_XML_CONTENT_TYPE;
        }
        else if (preferXhtml()) {
            return RIConstants.XHTML_CONTENT_TYPE;
        }
        else {
            return defaultContentType;
        }
    }


    private String[] contentTypeSplit(String contentTypeString) {
        Map<String, Object> appMap = FacesContext.getCurrentInstance().getExternalContext().getApplicationMap();
        String[] result = Util.split(appMap, contentTypeString, ",");
        for (int i = 0; i < result.length; i++) {
            int semicolon = result[i].indexOf(";");
            if (-1 != semicolon) {
                result[i] = result[i].substring(0, semicolon);
            }
        }
        return result;
    }

    // Helper method that returns the content type if the desired content type is found in the
    // array of supported types. 

    private String getSupportedContentType(String desiredContentTypeList) {

        String contentType = null;
        String[] desiredTypes = contentTypeSplit(desiredContentTypeList);

        // For each entry in the desiredTypes array, check if it's a supported content type
        for (int i = 0, ilen = desiredTypes.length; i < ilen; i++) {
            String curDesiredType = desiredTypes[i];
            if (curDesiredType.contains(RIConstants.HTML_CONTENT_TYPE)) {
                contentType = RIConstants.HTML_CONTENT_TYPE;
            }
            else if (curDesiredType.contains(RIConstants.XHTML_CONTENT_TYPE) ||
                curDesiredType.contains(RIConstants.APPLICATION_XML_CONTENT_TYPE)) {
                contentType = RIConstants.XHTML_CONTENT_TYPE;
            }
            else if (curDesiredType.contains(RIConstants.TEXT_XML_CONTENT_TYPE)) {
                contentType = RIConstants.TEXT_XML_CONTENT_TYPE;
            }

            if (null != contentType) {
                break;
            }
        }

        return contentType;
    }


    @Override
    public ResponseStream createResponseStream(OutputStream out) {
        final OutputStream output = out;
        return new ResponseStream() {
            @Override
            public void write(int b) throws IOException {
                output.write(b);
            }


            @Override
            public void write(byte b[]) throws IOException {
                output.write(b);
            }


            @Override
            public void write(byte b[], int off, int len) throws IOException {
                output.write(b, off, len);
            }


            @Override
            public void flush() throws IOException {
                output.flush();
            }


            @Override
            public void close() throws IOException {
                output.close();
            }
        };
    }


    /**
     * @see javax.faces.render.RenderKit#getComponentFamilies()
     */
    @Override
    public Iterator<String> getComponentFamilies() {

        return rendererFamilies.keySet().iterator();

    }


    /**
     * @see javax.faces.render.RenderKit#getRendererTypes(String)  
     */
    @Override
    public Iterator<String> getRendererTypes(String componentFamily) {

        Map<String,Renderer> family = rendererFamilies.get(componentFamily);
        if (family != null) {
            return family.keySet().iterator();
        } else {
            Set<String> empty = Collections.emptySet();
            return empty.iterator();
        }

    }

    // The test for this class is in TestRenderKit.java

} // end of class RenderKitImpl


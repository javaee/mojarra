/*
 * $Id: RenderKitImpl.java,v 1.54 2007/06/25 20:57:21 rlubke Exp $
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

// RenderKitImpl.java

package com.sun.faces.renderkit;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseStream;
import javax.faces.context.ResponseWriter;
import javax.faces.render.RenderKit;
import javax.faces.render.Renderer;
import javax.faces.render.ResponseStateManager;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import com.sun.faces.RIConstants;
import com.sun.faces.config.WebConfiguration;
import com.sun.faces.config.WebConfiguration.BooleanWebContextInitParameter;
import com.sun.faces.renderkit.html_basic.HtmlResponseWriter;
import com.sun.faces.util.MessageUtils;
import com.sun.faces.util.Util;

/**
 * <B>RenderKitImpl</B> is a class ...
 * <p/>
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: RenderKitImpl.java,v 1.54 2007/06/25 20:57:21 rlubke Exp $
 */

public class RenderKitImpl extends RenderKit {

    private static final String[] SUPPORTED_CONTENT_TYPES_ARRAY =
         new String[]{
              RIConstants.HTML_CONTENT_TYPE,
              RIConstants.XHTML_CONTENT_TYPE,
              RIConstants.APPLICATION_XML_CONTENT_TYPE,
              RIConstants.TEXT_XML_CONTENT_TYPE
         };

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

    private ConcurrentHashMap<String, HashMap<Object, Renderer>> rendererFamilies =
         new ConcurrentHashMap<String, HashMap<Object, Renderer>>();

    private ResponseStateManager responseStateManager =
         new ResponseStateManagerImpl();
    private Boolean preferXHTML;
    private Boolean isScriptHidingEnabled;


    public void addRenderer(String family, String rendererType,
                            Renderer renderer) {
        if (family == null) {
            String message = MessageUtils.getExceptionMessageString
                 (MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID, "family");
            throw new NullPointerException(message);
        }
        if (rendererType == null) {
            String message = MessageUtils.getExceptionMessageString
                 (MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID, "rendererType");
            throw new NullPointerException(message);
        }
        if (renderer == null) {
            String message = MessageUtils.getExceptionMessageString
                 (MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID, "renderer");
            throw new NullPointerException(message);
        }
        HashMap<Object, Renderer> renderers = null;


        if (null == (renderers = rendererFamilies.get(family))) {
            rendererFamilies
                 .put(family, renderers = new HashMap<Object, Renderer>());
        }
        renderers.put(rendererType, renderer);

    }


    public Renderer getRenderer(String family, String rendererType) {

        if (rendererType == null) {
            String message = MessageUtils.getExceptionMessageString
                 (MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID, "rendererType");
            throw new NullPointerException(message);
        }
        if (family == null) {
            String message = MessageUtils.getExceptionMessageString
                 (MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID, "family");
            throw new NullPointerException(message);
        }

        assert(rendererFamilies != null);

        HashMap<Object, Renderer> renderers = null;
        Renderer renderer = null;

        if (null != (renderers = rendererFamilies.get(family))) {
            renderer = renderers.get(rendererType);
        }

        return renderer;
    }


    public synchronized ResponseStateManager getResponseStateManager() {
        if (responseStateManager == null) {
            responseStateManager = new ResponseStateManagerImpl();
        }
        return responseStateManager;
    }


    public ResponseWriter createResponseWriter(Writer writer,
                                               String desiredContentTypeList,
                                               String characterEncoding) {
        if (writer == null) {
            return null;
        }
        String contentType = null;
        boolean contentTypeNullFromResponse = false;
        FacesContext context = FacesContext.getCurrentInstance();

        // Step 0: Determine if we have a preference for XHTML   
        if (preferXHTML == null) {
            preferXHTML =
                 WebConfiguration.getInstance(context.getExternalContext())
                      .isOptionEnabled(BooleanWebContextInitParameter.PreferXHTMLContentType);
        }

        // Step 1: Check the content type passed into this method 
        if (null != desiredContentTypeList) {
            contentType = findMatch(
                 desiredContentTypeList,
                 SUPPORTED_CONTENT_TYPES_ARRAY);
        }

        // Step 2: Check the response content type
        if (null == desiredContentTypeList) {
            desiredContentTypeList =
                 context.getExternalContext().getResponseContentType();
            if (null != desiredContentTypeList) {
                contentType = findMatch(
                     desiredContentTypeList,
                     SUPPORTED_CONTENT_TYPES_ARRAY);
                if (null == contentType) {
                    contentTypeNullFromResponse = true;
                }
            }
        }

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
                if (preferXHTML) {
                    desiredContentTypeList = RenderKitUtils.determineContentType(
                         desiredContentTypeList, SUPPORTED_CONTENT_TYPES, RIConstants.XHTML_CONTENT_TYPE);
                } else {
                    desiredContentTypeList = RenderKitUtils.determineContentType(
                         desiredContentTypeList, SUPPORTED_CONTENT_TYPES, null);
                }
                if (null != desiredContentTypeList) {
                    contentType = findMatch(
                         desiredContentTypeList,
                         SUPPORTED_CONTENT_TYPES_ARRAY);
                }
            }
        }

        // Step 4: Default to text/html
        if (null == desiredContentTypeList ||
             RIConstants.ALL_MEDIA.equals(desiredContentTypeList)) {
            contentType = RIConstants.HTML_CONTENT_TYPE;
        }

        if (null == contentType) {
            throw new IllegalArgumentException(MessageUtils.getExceptionMessageString(
                 MessageUtils.CONTENT_TYPE_ERROR_MESSAGE_ID));
        }

        if (characterEncoding == null) {
            characterEncoding = RIConstants.CHAR_ENCODING;
        }

        if (isScriptHidingEnabled == null) {
            WebConfiguration webConfig =
                 WebConfiguration.getInstance(
                      context.getExternalContext());
            isScriptHidingEnabled = webConfig
                 .isOptionEnabled(
                      BooleanWebContextInitParameter.EnableJSStyleHiding);
        }

        return new HtmlResponseWriter(writer,
                                      contentType,
                                      characterEncoding,
             isScriptHidingEnabled);
    }

    private String[] contentTypeSplit(String contentTypeString) {
        String[] result = Util.split(contentTypeString, ",");
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

    private String findMatch(String desiredContentTypeList,
                             String[] supportedTypes) {
        String contentType = null;

        String[] desiredTypes = contentTypeSplit(desiredContentTypeList);
        String curContentType = null, curDesiredType = null;

        // For each entry in the desiredTypes array, look for a match in
        // the supportedTypes array
        for (int i = 0, ilen = desiredTypes.length; i < ilen; i++) {
            curDesiredType = desiredTypes[i];
            for (int j = 0, jlen = supportedTypes.length; j < jlen; j++) {
                curContentType = supportedTypes[j].trim();
                if (curDesiredType.contains(curContentType)) {
                    if (curContentType.contains(RIConstants.HTML_CONTENT_TYPE)) {
                        contentType = RIConstants.HTML_CONTENT_TYPE;
                    } else
                    if (curContentType.contains(RIConstants.XHTML_CONTENT_TYPE) ||
                         curContentType.contains(RIConstants.APPLICATION_XML_CONTENT_TYPE) ||
                         curContentType.contains(RIConstants.TEXT_XML_CONTENT_TYPE)) {
                        contentType = RIConstants.XHTML_CONTENT_TYPE;
                    }
                    break;
                }
            }
            if (null != contentType) {
                break;
            }
        }
        return contentType;
    }


    public ResponseStream createResponseStream(OutputStream out) {
        final OutputStream output = out;
        return new ResponseStream() {
            public void write(int b) throws IOException {
                output.write(b);
            }


            public void write(byte b[]) throws IOException {
                output.write(b);
            }


            public void write(byte b[], int off, int len) throws IOException {
                output.write(b, off, len);
            }


            public void flush() throws IOException {
                output.flush();
            }


            public void close() throws IOException {
                output.close();
            }
        };
    }
    // The test for this class is in TestRenderKit.java

} // end of class RenderKitImpl


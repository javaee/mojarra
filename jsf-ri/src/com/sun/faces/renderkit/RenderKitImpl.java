/*
 * $Id: RenderKitImpl.java,v 1.35 2006/03/29 22:38:35 rlubke Exp $
 */

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
 * [Name of File] [ver.__] [Date]
 * 
 * Copyright 2005 Sun Microsystems Inc. All Rights Reserved
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
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sun.faces.RIConstants;
import com.sun.faces.renderkit.html_basic.HtmlResponseWriter;
import com.sun.faces.util.MessageUtils;
import com.sun.faces.util.Util;

/**
 * <B>RenderKitImpl</B> is a class ...
 * <p/>
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: RenderKitImpl.java,v 1.35 2006/03/29 22:38:35 rlubke Exp $
 */

public class RenderKitImpl extends RenderKit {

    // Log instance for this class
    private static final Logger logger =
          Util.getLogger(Util.FACES_LOGGER + Util.RENDERKIT_LOGGER);
    private static String ALL_MEDIA = "*/*";
    private static String APPLICATION_XML_CONTENT_TYPE = "application/xml";
    private static String CHAR_ENCODING = "ISO-8859-1";

    // used for ResponseWriter creation;
    private static String HTML_CONTENT_TYPE = "text/html";
    private static String TEXT_XML_CONTENT_TYPE = "text/xml";
    private static String XHTML_CONTENT_TYPE = "application/xhtml+xml";

    /**
     * Keys are String renderer family.  Values are HashMaps.  Nested
     * HashMap keys are Strings for the rendererType, and values are the
     * Renderer instances themselves.
     */
    private HashMap<String, HashMap<Object, Renderer>> rendererFamilies;

    private ResponseStateManager responseStateManager = null;

    private boolean preferXhtml = false;

    // ------------------------------------------------------------ Constructors


    public RenderKitImpl() {

        super();
        rendererFamilies = new HashMap<String, HashMap<Object, Renderer>>();

    }

    // ---------------------------------------------------------- Public Methods


    public synchronized ResponseStateManager getResponseStateManager() {

        if (responseStateManager == null) {
            responseStateManager = new ResponseStateManagerImpl();
        }
        return responseStateManager;

    }


    public void addRenderer(String family, String rendererType,
                            Renderer renderer) {

        if (family == null || rendererType == null || renderer == null) {
            String message = MessageUtils.getExceptionMessageString
                  (MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID);
            message = message + " family " + family + " rendererType " +
                      rendererType + " renderer " + renderer;
            throw new NullPointerException(message);

        }
        HashMap<Object, Renderer> renderers = null;

        synchronized (rendererFamilies) {
            // PENDING(edburns): generics would be nice here.
            if (null == (renderers = rendererFamilies.get(family))) {
                rendererFamilies
                      .put(family, renderers = new HashMap<Object, Renderer>());
            }
            renderers.put(rendererType, renderer);
        }

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


    public ResponseWriter createResponseWriter(Writer writer,
                                               String desiredContentTypeList,
                                               String characterEncoding) {

        if (writer == null) {
            return null;
        }
        String contentType = null;
        boolean contentTypeNullFromResponse = false;
        FacesContext context = FacesContext.getCurrentInstance();

        String [] supportedTypes =
              {HTML_CONTENT_TYPE, XHTML_CONTENT_TYPE,
               APPLICATION_XML_CONTENT_TYPE, TEXT_XML_CONTENT_TYPE};

        // Step 0: Determine if we have a preference for XHTML
        String preferXhtmlParam = context.getExternalContext().getInitParameter(
              RIConstants.PREFER_XHTML);
        if (null != preferXhtmlParam) {
            if (!(preferXhtmlParam.equals("true")) &&
                !(preferXhtmlParam.equals("false"))) {
                if (logger.isLoggable(Level.WARNING)) {
                    logger.warning(MessageUtils.getExceptionMessageString(
                          MessageUtils.INVALID_INIT_PARAM_ERROR_MESSAGE_ID,
                          new Object[]{preferXhtmlParam,
                                       RIConstants.PREFER_XHTML}));
                }
            }
            preferXhtml = Boolean.valueOf(preferXhtmlParam);
        }

        // Step 1: Check the content type passed into this method 
        if (null != desiredContentTypeList) {
            contentType =
                  findMatch(context, desiredContentTypeList, supportedTypes);
        }

        // Step 2: Check the response content type
        if (null == desiredContentTypeList) {
            desiredContentTypeList =
                  context.getExternalContext().getResponseContentType();
            if (null != desiredContentTypeList) {
                contentType = findMatch(context,
                                        desiredContentTypeList,
                                        supportedTypes);
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
            String[] typeArray = (String[])
                  context.getExternalContext().getRequestHeaderValuesMap()
                        .get("Accept");
            if (typeArray.length > 0) {
                StringBuffer buff = new StringBuffer();
                buff.append(typeArray[0]);
                for (int i = 1; i < typeArray.length; i++) {
                    buff.append(',');
                    buff.append(typeArray[i]);
                }
                desiredContentTypeList = buff.toString();
            }

            if (null != desiredContentTypeList) {
                String supportedTypeString =
                      HTML_CONTENT_TYPE
                      + ','
                      + XHTML_CONTENT_TYPE
                      + ','
                      +
                      APPLICATION_XML_CONTENT_TYPE
                      + ','
                      + TEXT_XML_CONTENT_TYPE;
                desiredContentTypeList = RenderKitUtils.determineContentType(
                      desiredContentTypeList, supportedTypeString);
                if (null != desiredContentTypeList) {
                    contentType = findMatch(context,
                                            desiredContentTypeList,
                                            supportedTypes);
                }
            }
        }

        // Step 4: Default to text/html
        if (null == desiredContentTypeList ||
            desiredContentTypeList.equals(ALL_MEDIA)) {
            Map<String, Object> requestMap =
                  context.getExternalContext().getRequestMap();
            if (preferXhtml) {
                contentType = XHTML_CONTENT_TYPE;
                requestMap.put(RIConstants.CONTENT_TYPE_IS_XHTML, Boolean.TRUE);
            } else {
                contentType = HTML_CONTENT_TYPE;
                requestMap.put(RIConstants.CONTENT_TYPE_IS_HTML, Boolean.TRUE);
            }
        }

        if (null == contentType) {
            throw new IllegalArgumentException(MessageUtils.getExceptionMessageString(
                  MessageUtils.CONTENT_TYPE_ERROR_MESSAGE_ID));
        }

        if (characterEncoding == null) {
            characterEncoding = CHAR_ENCODING;
        }

        return new HtmlResponseWriter(writer, contentType, characterEncoding);

    }


    public Renderer getRenderer(String family, String rendererType) {

        if (rendererType == null || family == null) {
            String message = MessageUtils.getExceptionMessageString
                  (MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID);
            message = message + " family " + family + " rendererType " +
                      rendererType;
            throw new NullPointerException(message);
        }

        assert (rendererFamilies != null);

        HashMap<Object, Renderer> renderers = null;
        Renderer renderer = null;

        if (null != (renderers = rendererFamilies.get(family))) {
            renderer = renderers.get(rendererType);
        }

        return renderer;

    }

    // --------------------------------------------------------- Private Methods


    private String[] contentTypeSplit(String contentTypeString) {

        String [] result = contentTypeString.split(",");
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

    private String findMatch(FacesContext context,
                             String desiredContentTypeList,
                             String[] supportedTypes) {

        String contentType = null;

        Map<String, Object> requestMap =
              context.getExternalContext().getRequestMap();

        String [] desiredTypes = contentTypeSplit(desiredContentTypeList);
        String curContentType = null, curDesiredType = null;

        // For each entry in the desiredTypes array, look for a match in
        // the supportedTypes array
        for (int i = 0; i < desiredTypes.length; i++) {
            curDesiredType = desiredTypes[i];
            for (int j = 0; j < supportedTypes.length; j++) {
                curContentType = supportedTypes[j].trim();
                if (-1 != curDesiredType.indexOf(curContentType)) {
                    if (-1 != curContentType.indexOf(HTML_CONTENT_TYPE)) {
                        if (preferXhtml) {
                            contentType = XHTML_CONTENT_TYPE;
                            requestMap.put(RIConstants.CONTENT_TYPE_IS_XHTML,
                                           Boolean.TRUE);
                        } else {
                            contentType = HTML_CONTENT_TYPE;
                            requestMap.put(RIConstants.CONTENT_TYPE_IS_HTML,
                                           Boolean.TRUE);
                        }
                    } else
                    if (-1 != curContentType.indexOf(XHTML_CONTENT_TYPE) ||
                        -1 != curContentType
                              .indexOf(APPLICATION_XML_CONTENT_TYPE) ||
                                                                     -1
                                                                     != curContentType
                                                                           .indexOf(
                                                                                 TEXT_XML_CONTENT_TYPE))
                    {
                        contentType = XHTML_CONTENT_TYPE;
                        requestMap.put(RIConstants.CONTENT_TYPE_IS_XHTML,
                                       Boolean.TRUE);
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

    // The test for this class is in TestRenderKit.java

} // end of class RenderKitImpl


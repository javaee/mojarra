/*
 * $Id: RenderKitImpl.java,v 1.39 2006/05/10 20:03:23 rogerk Exp $
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

import com.sun.faces.RIConstants;
import com.sun.faces.config.WebConfiguration;
import com.sun.faces.config.WebConfiguration.BooleanWebContextInitParameter;
import com.sun.faces.renderkit.html_basic.HtmlResponseWriter;
import com.sun.faces.util.MessageUtils;
import com.sun.faces.util.Util;

import javax.faces.context.ResponseStream;
import javax.faces.context.ResponseWriter;
import javax.faces.context.FacesContext;
import javax.faces.render.RenderKit;
import javax.faces.render.Renderer;
import javax.faces.render.ResponseStateManager;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * <B>RenderKitImpl</B> is a class ...
 * <p/>
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: RenderKitImpl.java,v 1.39 2006/05/10 20:03:23 rogerk Exp $
 */

public class RenderKitImpl extends RenderKit {

//
// Protected Constants
//

//
// Class Variables
//

//
// Instance Variables
//
    // Log instance for this class
    private static final Logger logger =
            Util.getLogger(Util.FACES_LOGGER + Util.RENDERKIT_LOGGER);

//
// Ivars used during actual client lifetime
//

// Relationship Instance Variables

    /**
     * Keys are String renderer family.  Values are HashMaps.  Nested
     * HashMap keys are Strings for the rendererType, and values are the
     * Renderer instances themselves.
     */

    private HashMap<String,HashMap<Object,Renderer>> rendererFamilies;

    private ResponseStateManager responseStateManager = null;
    private Boolean preferXHTML;
//
// Constructors and Initializers    
//

    public RenderKitImpl() {
        super();
    rendererFamilies = new HashMap<String, HashMap<Object,Renderer>>();
    }


    //
    // Class methods
    //

    //
    // General Methods
    //

    //
    // Methods From RenderKit
    //

    public void addRenderer(String family, String rendererType,
                            Renderer renderer) {
        if (family == null || rendererType == null || renderer == null) {
            String message = MessageUtils.getExceptionMessageString
                (MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID);
            message = message + " family " + family + " rendererType " +
                      rendererType + " renderer " + renderer;
            throw new NullPointerException(message);

        }
    HashMap<Object,Renderer> renderers = null;

        synchronized (rendererFamilies) {
        // PENDING(edburns): generics would be nice here.
        if (null == (renderers = rendererFamilies.get(family))) {
        rendererFamilies.put(family, renderers = new HashMap<Object, Renderer>());
        }
            renderers.put(rendererType, renderer);
        }
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

    HashMap<Object,Renderer> renderers = null;
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

        String [] supportedTypes =
            { RIConstants.HTML_CONTENT_TYPE, RIConstants.XHTML_CONTENT_TYPE,
              RIConstants.APPLICATION_XML_CONTENT_TYPE, RIConstants.TEXT_XML_CONTENT_TYPE };

        // Step 0: Determine if we have a preference for XHTML   
        if (preferXHTML == null) {
            preferXHTML =
              WebConfiguration.getInstance(context.getExternalContext())
                    .getBooleanContextInitParameter(BooleanWebContextInitParameter.PreferXHTMLContentType);
        }

        // Step 1: Check the content type passed into this method 
        if (null != desiredContentTypeList) {
            contentType = findMatch(context, desiredContentTypeList, supportedTypes);
        }

        // Step 2: Check the response content type
    if (null == desiredContentTypeList) {
        desiredContentTypeList =
                    context.getExternalContext().getResponseContentType();
            if (null != desiredContentTypeList) {
                contentType = findMatch(context, desiredContentTypeList, supportedTypes);
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
                context.getExternalContext().getRequestHeaderValuesMap().get("Accept");
            if (typeArray.length > 0) {
                StringBuffer buff = new StringBuffer();
                buff.append(typeArray[0]);
                for (int i=1; i<typeArray.length; i++) {
                    buff.append(',');
                    buff.append(typeArray[i]);
                }
                desiredContentTypeList = buff.toString();
            }

            if (null != desiredContentTypeList) {
                String supportedTypeString =
                    RIConstants.HTML_CONTENT_TYPE + ',' + RIConstants.XHTML_CONTENT_TYPE + ',' +
                    RIConstants.APPLICATION_XML_CONTENT_TYPE + ',' + RIConstants.TEXT_XML_CONTENT_TYPE;
                if (preferXHTML) {
                    desiredContentTypeList = RenderKitUtils.determineContentType(
                        desiredContentTypeList, supportedTypeString, RIConstants.XHTML_CONTENT_TYPE);
                } else {
                    desiredContentTypeList = RenderKitUtils.determineContentType(
                        desiredContentTypeList, supportedTypeString, null);
                }
                if (null != desiredContentTypeList) {
                    contentType = findMatch(context, desiredContentTypeList, supportedTypes);
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

        return new HtmlResponseWriter(writer, contentType, characterEncoding);
    }

    private String[] contentTypeSplit(String contentTypeString) {
        String [] result = contentTypeString.split(",");
        for (int i = 0; i < result.length; i++) {
            int semicolon = result[i].indexOf(";");
            if (-1 != semicolon) {
                result[i] = result[i].substring(0,semicolon);
            }
        }
        return result;
    }

    // Helper method that returns the content type if the desired content type is found in the
    // array of supported types. 

    private String findMatch(FacesContext context, String desiredContentTypeList, String[] supportedTypes) {
        String contentType = null;

        Map<String,Object> requestMap = context.getExternalContext().getRequestMap();

        String [] desiredTypes = contentTypeSplit(desiredContentTypeList);
        String curContentType = null, curDesiredType = null;

        // For each entry in the desiredTypes array, look for a match in
        // the supportedTypes array
        for (int i = 0; i < desiredTypes.length; i++) {
            curDesiredType = desiredTypes[i];
            for (int j = 0; j < supportedTypes.length; j++) {
                curContentType = supportedTypes[j].trim();
                if (curDesiredType.contains(curContentType)) {
                    if (curContentType.contains(RIConstants.HTML_CONTENT_TYPE)) {
                        contentType = RIConstants.HTML_CONTENT_TYPE;
                    } else if (curContentType.contains(RIConstants.XHTML_CONTENT_TYPE) ||
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


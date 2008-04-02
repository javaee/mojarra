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

package renderkits.renderkit.xul;

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

/**
 * <p><B>XULRenderKit</B> is a class that houses a collection of <code>XUL</code>
 * renderers.  It also creates the <code>ResponseWriter</code> used to write
 * <code>XUL</code> markup.</p>
 */
public class XULRenderKit extends RenderKit {

//
// Protected Constants
//

//
// Class Variables
//

//
// Instance Variables
//
    // used for ResponseWriter creation;
    private static String XUL_CONTENT_TYPE = "application/vnd.mozilla.xul+xml";
    private static String ALL_MEDIA = "*/*";

    private static String CHAR_ENCODING = "ISO-8859-1";
    private static String CONTENT_TYPE_IS_XUL = "ContentTypeIsXUL";
//
// Ivars used during actual client lifetime
//

// Relationship Instance Variables

    /**
     * Keys are String renderer family.  Values are HashMaps.  Nested
     * HashMap keys are Strings for the rendererType, and values are the
     * Renderer instances themselves.
     */

    private HashMap<String, HashMap<Object, Renderer>> rendererFamilies;

    private ResponseStateManager responseStateManager = null;
//
// Constructors and Initializers    
//

    public XULRenderKit() {
        super();
        rendererFamilies = new HashMap<String, HashMap<Object, Renderer>>();
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
            // PENDING - i18n
            String message = "Argument Error: One or more parameters are null.";
            message = message + " family " + family + " rendererType " +
                      rendererType + " renderer " + renderer;
            throw new NullPointerException(message);

        }
        HashMap<Object, Renderer> renderers = null;

        synchronized (rendererFamilies) {
            if (null == (renderers = (HashMap) rendererFamilies.get(family))) {
                rendererFamilies
                      .put(family, renderers = new HashMap<Object, Renderer>());
            }
            renderers.put(rendererType, renderer);
        }
    }


    public Renderer getRenderer(String family, String rendererType) {

        if (rendererType == null || family == null) {
            // PENDING - i18n
            String message = "Argument Error: One or more parameters are null.";
            message = message + " family " + family + " rendererType " +
                      rendererType;
            throw new NullPointerException(message);
        }

        HashMap<Object, Renderer> renderers = null;
        Renderer renderer = null;

        if (null != (renderers = (HashMap) rendererFamilies.get(family))) {
            renderer = (Renderer) renderers.get(rendererType);
        }

        return renderer;
    }


    public synchronized ResponseStateManager getResponseStateManager() {
        if (responseStateManager == null) {
            responseStateManager = new XULResponseStateManager();
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
        FacesContext context = FacesContext.getCurrentInstance();

        String [] supportedTypes = {XUL_CONTENT_TYPE};
        String [] desiredTypes = null;

        // Obtain the desired content type list
        // first crack is the passed in list
        if (null == desiredContentTypeList) {
            // second crack is the response content type
            desiredContentTypeList =
                  context.getExternalContext().getResponseContentType();
        }
        if (null == desiredContentTypeList) {
            // third crack is the Accept header.
            desiredContentTypeList = (String)
                  context.getExternalContext().getRequestHeaderMap()
                        .get("Accept");
        }
        // fourth, default to "application/vnd.mozilla.xul+xml" 
        if (null == desiredContentTypeList ||
            desiredContentTypeList.equals(ALL_MEDIA)) {
            desiredContentTypeList = XUL_CONTENT_TYPE;
        }

        if (null != desiredContentTypeList) {
            Map<String, Object> requestMap =
                  context.getExternalContext().getRequestMap();

            desiredTypes = contentTypeSplit(desiredContentTypeList);
            String curContentType = null, curDesiredType = null;

            // For each entry in the desiredTypes array, look for a match in 
            // the supportedTypes array
            for (int i = 0; i < desiredTypes.length; i++) {
                curDesiredType = desiredTypes[i];
                for (int j = 0; j < supportedTypes.length; j++) {
                    curContentType = supportedTypes[j].trim();
                    if (-1 != curDesiredType.indexOf(curContentType)) {
                        contentType = curDesiredType;
                        break;
                    }
                }
                if (null != contentType) {
                    break;
                }
            }
            // If none of the contentTypes about which we know was in
            // desiredContentTypeList
            if (null == contentType) {
                // PENDING - i18n
                throw new IllegalArgumentException("Unrecognized Content Type.");
            }
        } else {
            // there was no argument contentType list, or Accept header
            contentType = XUL_CONTENT_TYPE;
        }

        if (characterEncoding == null) {
            characterEncoding = CHAR_ENCODING;
        }

        return new XULResponseWriter(writer, contentType, characterEncoding);
    }

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

} // end of class XULRenderKit


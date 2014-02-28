/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2011 Oracle and/or its affiliates. All rights reserved.
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

package renderkits.renderkit.svg;

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
 * <p><B>SVGRenderKit</B> is a class that houses a collection of <code>SVG</code>
 * renderers.  It also creates the <code>ResponseWriter</code> used to write
 * <code>SVG</code> markup.</p>
 */
public class SVGRenderKit extends RenderKit {

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
    private static String SVG_CONTENT_TYPE = "image/svg+xml";
    private static String APPLICATION_XML_CONTENT_TYPE = "application/xml";
    private static String TEXT_XML_CONTENT_TYPE = "text/xml";
    private static String ALL_MEDIA = "*/*";

    private static String CHAR_ENCODING = "ISO-8859-1";
    private static String CONTENT_TYPE_IS_SVG = "ContentTypeIsSVG";
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

    public SVGRenderKit() {
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
            if (null == (renderers = rendererFamilies.get(family))) {
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

        if (null != (renderers = rendererFamilies.get(family))) {
            renderer = renderers.get(rendererType);
        }

        return renderer;
    }


    public synchronized ResponseStateManager getResponseStateManager() {
        if (responseStateManager == null) {
            responseStateManager = new SVGResponseStateManager();
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

        String [] supportedTypes =
              {SVG_CONTENT_TYPE, APPLICATION_XML_CONTENT_TYPE,
               TEXT_XML_CONTENT_TYPE};
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
        // fourth, default to image/svg+xml 
        if (null == desiredContentTypeList ||
            desiredContentTypeList.equals(ALL_MEDIA)) {
            desiredContentTypeList = SVG_CONTENT_TYPE;
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
            contentType = SVG_CONTENT_TYPE;
        }

        if (characterEncoding == null) {
            characterEncoding = CHAR_ENCODING;
        }

        return new SVGResponseWriter(writer, contentType, characterEncoding);
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

} // end of class SVGRenderKit


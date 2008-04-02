/*
 * $Id: RenderKitImpl.java,v 1.30 2005/10/07 17:04:27 rogerk Exp $
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

import com.sun.faces.renderkit.html_basic.HtmlResponseWriter;
import com.sun.faces.util.Util;
import com.sun.faces.RIConstants;

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

/**
 * <B>RenderKitImpl</B> is a class ...
 * <p/>
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: RenderKitImpl.java,v 1.30 2005/10/07 17:04:27 rogerk Exp $
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
    // used for ResponseWriter creation;
    private static String HTML_CONTENT_TYPE = "text/html";
    private static String XHTML_CONTENT_TYPE = "application/xhtml+xml";
    private static String APPLICATION_XML_CONTENT_TYPE = "application/xml";
    private static String TEXT_XML_CONTENT_TYPE = "text/xml";
    private static String ALL_MEDIA = "*/*";
    private static String CHAR_ENCODING = "ISO-8859-1";
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
            String message = Util.getExceptionMessageString
                (Util.NULL_PARAMETERS_ERROR_MESSAGE_ID);
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
            String message = Util.getExceptionMessageString
                (Util.NULL_PARAMETERS_ERROR_MESSAGE_ID);
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
	FacesContext context = FacesContext.getCurrentInstance();
        
        String [] supportedTypes = 
            { HTML_CONTENT_TYPE, XHTML_CONTENT_TYPE, 
              APPLICATION_XML_CONTENT_TYPE, TEXT_XML_CONTENT_TYPE };
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
	      context.getExternalContext().getRequestHeaderMap().get("Accept");
        }

        // fourth, default to text/html
        if (null == desiredContentTypeList ||
            desiredContentTypeList.equals(ALL_MEDIA)) {
            desiredContentTypeList = HTML_CONTENT_TYPE;
        }
	if (null != desiredContentTypeList) {
	    Map<String,Object> requestMap = context.getExternalContext().getRequestMap();
	    
	    desiredTypes = contentTypeSplit(desiredContentTypeList);
	    String curContentType = null, curDesiredType = null;                       
            
            // For each entry in the desiredTypes array, look for a match in 
            // the supportedTypes array
	    for (int i = 0; i < desiredTypes.length; i++) {
                curDesiredType = desiredTypes[i];
                for (int j = 0; j < supportedTypes.length; j++) {
                    curContentType = supportedTypes[j].trim();
                    if (-1 != curDesiredType.indexOf(curContentType)) {
                        if (-1 != curContentType.indexOf(HTML_CONTENT_TYPE)) {
                            contentType = HTML_CONTENT_TYPE;
                            requestMap.put(RIConstants.CONTENT_TYPE_IS_HTML,
                                	   Boolean.TRUE);
                        }
                        else if (-1 != curContentType.indexOf(XHTML_CONTENT_TYPE) ||
                                 -1 != curContentType.indexOf(APPLICATION_XML_CONTENT_TYPE) ||
                                 -1 != curContentType.indexOf(TEXT_XML_CONTENT_TYPE)) {
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
	    // If none of the contentTypes about which we know was in
	    // desiredContentTypeList
	    if (null == contentType) {
                throw new IllegalArgumentException(Util.getExceptionMessageString(
                    Util.CONTENT_TYPE_ERROR_MESSAGE_ID));
	    }
	}
	else {
	    // there was no argument contentType list, or Accept header
	    contentType = HTML_CONTENT_TYPE;
	}

        if (characterEncoding == null) {
            characterEncoding = CHAR_ENCODING;
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


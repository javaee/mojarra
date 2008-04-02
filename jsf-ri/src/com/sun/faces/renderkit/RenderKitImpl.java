/*
 * $Id: RenderKitImpl.java,v 1.25 2004/12/17 17:09:42 edburns Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
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
 * @version $Id: RenderKitImpl.java,v 1.25 2004/12/17 17:09:42 edburns Exp $
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

    private HashMap rendererFamilies;

    private ResponseStateManager responseStateManager = null;
//
// Constructors and Initializers    
//

    public RenderKitImpl() {
        super();
	rendererFamilies = new HashMap();
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
	HashMap renderers = null;

        synchronized (rendererFamilies) {
	    // PENDING(edburns): generics would be nice here.
	    if (null == (renderers = (HashMap) rendererFamilies.get(family))) {
		rendererFamilies.put(family, renderers = new HashMap());
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

	HashMap renderers = null;
        Renderer renderer = null;

	if (null != (renderers = (HashMap) rendererFamilies.get(family))) {
	    renderer = (Renderer) renderers.get(rendererType);
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
					       String contentTypeList,
                                               String characterEncoding) {
        if (writer == null) {
            return null;
        }
        String contentType = null;
	FacesContext context = FacesContext.getCurrentInstance();

	// if no contentTypeList was passed
	if (null == contentTypeList) {
	    // use the Accept header.
	    contentTypeList = (String)
	      context.getExternalContext().getRequestHeaderMap().get("Accept");
	}

	if (null != contentTypeList) {
	    Map requestMap = context.getExternalContext().getRequestMap();

	    // search for the first occurrence of XHTML_CONTENT_TYPE or
	    // HTML_CONTENT_TYPE.  Choose whichever we find first.
	    String[] types = contentTypeList.split(",");
	    String curContentType = null;
	    
	    for (int i = 0; i < types.length; i++) {
		curContentType = types[i].trim();
		if (-1 != curContentType.indexOf(HTML_CONTENT_TYPE)) {
		    contentType = HTML_CONTENT_TYPE;
		    requestMap.put(RIConstants.CONTENT_TYPE_IS_HTML,
				   Boolean.TRUE);
		    break;
		}
		else if (-1 != curContentType.indexOf(XHTML_CONTENT_TYPE)) {
		    contentType = XHTML_CONTENT_TYPE;
		    requestMap.put(RIConstants.CONTENT_TYPE_IS_XHTML,
				   Boolean.TRUE);
		    break;
		}
	    }
	    // If none of the contentTypes about which we know was in
	    // contentTypeList
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


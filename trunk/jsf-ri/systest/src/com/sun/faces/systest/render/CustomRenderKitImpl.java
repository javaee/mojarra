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

// CustomRenderKitImpl.java

package com.sun.faces.systest.render;

import com.sun.faces.util.MessageUtils;
import com.sun.faces.renderkit.ResponseStateManagerImpl;

import javax.faces.context.ResponseStream;
import javax.faces.context.ResponseWriter;
import javax.faces.render.RenderKit;
import javax.faces.render.Renderer;
import javax.faces.render.ResponseStateManager;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.HashMap;

/**
 * <B>CustomRenderKitImpl</B> is a class ...
 * <p/>
 * <B>Lifetime And Scope</B> <P>
 *
 */

public class CustomRenderKitImpl extends RenderKit {

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

    public CustomRenderKitImpl() {
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


    public ResponseWriter createResponseWriter(Writer writer, String contentTypeList,
                                               String characterEncoding) {
        if (writer == null) {
            return null;
        }
        // Set the default content type to html;  However, if a content type list
        // argument was specified, make sure it contains an html content type;
        // PENDING(rogerk) ideally, we want to analyze the content type string
        // in more detail, to determine the preferred content type - as outlined in
        // http://www.ietf.org/rfc/rfc2616.txt?number=2616 - Section 14.1
        // (since this is not an html renderkit);
        //
        String contentType = HTML_CONTENT_TYPE;
        if (contentTypeList != null) {
            if (contentTypeList.indexOf(contentType) < 0) {
                throw new IllegalArgumentException(MessageUtils.getExceptionMessageString(
                    MessageUtils.CONTENT_TYPE_ERROR_MESSAGE_ID));
            }
        }
        if (characterEncoding == null) {
            characterEncoding = CHAR_ENCODING;
        }

        return new CustomResponseWriter(writer, contentType, characterEncoding);
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

} // end of class CustomRenderKitImpl


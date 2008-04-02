/*
 * $Id: RenderKitImpl.java,v 1.7 2003/08/19 19:31:14 rlubke Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// RenderKitImpl.java

package com.sun.faces.renderkit;

import com.sun.faces.renderkit.html_basic.HtmlResponseWriter;
import com.sun.faces.util.Util;

import org.xml.sax.InputSource;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.Log;
import org.mozilla.util.ParameterCheck;

import org.xml.sax.Attributes;

import java.io.IOException;
import java.io.Writer;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Stack;
import java.util.Set;
import java.util.NoSuchElementException;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.ResponseWriter;
import javax.faces.context.ResponseStream;
import javax.faces.render.RenderKit;
import javax.faces.render.Renderer;
import javax.faces.render.ResponseStateManager;

/**
 *
 *  <B>RenderKitImpl</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: RenderKitImpl.java,v 1.7 2003/08/19 19:31:14 rlubke Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
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

//
// Ivars used during actual client lifetime
//

// Relationship Instance Variables

    /**

    * Keys are String rendererType, values are HtmlBasicRenderer instances

    */

    private Hashtable renderersByRendererType;

//
// Constructors and Initializers    
//

    public RenderKitImpl() {
        super();
        renderersByRendererType = new Hashtable();
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

    /**
     * This method adds a <code>Renderer</code> with its associated 
     * <code>rendererType</code> to the internal map.
     *
     * @param rendererType The <code>Renderer</code> type.
     * @param renderer The <code>Renderer</code> instance.
     *
     * @throws NullPointerException if either parameter is null.
     * @throws IllegalArgumentException if the <code>rendererType</code>
     *  already exists.
     */
    public void addRenderer(String rendererType, Renderer renderer) {
        if (rendererType == null || renderer == null) {
            throw new NullPointerException(Util.getExceptionMessage(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }

	if (null != renderersByRendererType.get(rendererType)) {
	    Object params [] = { rendererType };
	    throw new IllegalArgumentException(Util.getExceptionMessage(
                Util.RENDERER_ALREADY_EXISTS_ERROR_MESSAGE_ID, params));
	}
	renderersByRendererType.put(rendererType, renderer);
    }

    /**
     * This method returns a renderer instance given a renderer type.
     *
     * @param rendererType The renderer type.
     * @return Renderer A Renderer instance.
     * @throws FacesException If the renderer instance is not found for
     *         the given renderer type.
     */
    public Renderer getRenderer(String rendererType) {

        if (rendererType == null) {
            throw new NullPointerException(Util.getExceptionMessage(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }

        Assert.assert_it(renderersByRendererType != null);

        Renderer renderer = (Renderer)renderersByRendererType.get(rendererType);
        if (renderer == null) {
	    Object [] params = { rendererType };
            throw new IllegalArgumentException(Util.getExceptionMessage(
                Util.RENDERER_NOT_FOUND_ERROR_MESSAGE_ID, params));
        }

        return renderer;
    }

    // PENDING (rlubke) PROVIDE IMPLEMENTATION
    public ResponseStateManager getResponseStateManager() {
        return null;  //To change body of implemented methods use Options | File Templates.
    }

    // PENDING (rlubke) PROVIDE IMPLEMENTATION
    public ResponseWriter createResponseWriter(Writer writer, String contentTypeList, String characterEncoding) {
        return null;  //To change body of implemented methods use Options | File Templates.
    }

    // PENDING (rlubke) PROVIDE IMPLEMENTATION
    public ResponseStream getResponseStream(OutputStream out) {
        return null;  //To change body of implemented methods use Options | File Templates.
    }

    /**
     * Create a new {@link ResponseWriter} instance from the provided
     * <code>Writer</code> and character encoding.
     *
     * @param writer The contained <code>Writer</code>.
     * @param characterEncoding such as "ISO-8859-1" for this {@link ResponseWriter}.
     *
     * @return a new {@link ResponseWriter}.
     */
    public ResponseWriter getResponseWriter(Writer writer, String characterEncoding) {
	if (writer != null) {
	    return new HtmlResponseWriter(writer, characterEncoding);
	}
        return null;
    }

    // The test for this class is in TestRenderKit.java

} // end of class RenderKitImpl


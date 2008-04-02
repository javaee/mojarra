/*
 * $Id: MockRenderKit.java,v 1.1 2003/07/20 00:41:45 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.mock;


import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.faces.component.UIComponent;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;
import javax.faces.render.RenderKit;
import java.io.Writer;


public class MockRenderKit extends RenderKit {


    private Map renderers = new HashMap();


    public void addRenderer(String rendererType, Renderer renderer) {
        if ((rendererType == null) || (renderer == null)) {
            throw new NullPointerException();
        }
        synchronized (renderers) {
            if (renderers.containsKey(rendererType)) {
                throw new IllegalArgumentException();
            }
            renderers.put(rendererType, renderer);
        }
    }


    public Renderer getRenderer(String rendererType) {
        if (rendererType == null) {
            throw new NullPointerException();
        }
        synchronized (renderers) {
            Renderer renderer = (Renderer) renderers.get(rendererType);
            if (renderer == null) {
                throw new IllegalArgumentException(rendererType);
            }
            return (renderer);
        }
    }


    public ResponseWriter getResponseWriter(Writer writer,
                                            String characterEncoding) {
        return new MockResponseWriter(writer, characterEncoding);
    }


}

/*
 * $Id: MockRenderKit.java,v 1.5 2003/08/18 22:45:27 eburns Exp $
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
import javax.faces.context.ResponseStream;
import javax.faces.render.Renderer;
import javax.faces.render.RenderKit;
import javax.faces.render.ResponseStateManager;
import java.io.Writer;
import java.io.OutputStream;
import java.io.IOException;


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


    public ResponseWriter createResponseWriter(Writer writer,
					       String contentTypeList,
					       String characterEncoding) {
        return new MockResponseWriter(writer, characterEncoding);
    }

    public ResponseStream getResponseStream(OutputStream out) {
	final OutputStream os = out;
	return new ResponseStream() {
		public void close() throws IOException {
		    os.close();
		}
		public void flush() throws IOException {
		    os.flush();
		}
		public void write(byte[] b) throws IOException {
		    os.write(b);
		}
		public void write(byte[] b, int off, int len) throws IOException {
		    os.write(b, off, len);
		}
		public void write(int b) throws IOException {
		    os.write(b);
		}
	    };
    }


    public ResponseStateManager getResponseStateManager() {
	return null;
    }


}

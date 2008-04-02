/*
 * $Id: MockRenderKit.java,v 1.13 2004/02/04 23:39:13 ofung Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.mock;


import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UIData;
import javax.faces.component.UIOutput;
import javax.faces.component.UIPanel;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.context.ResponseStream;
import javax.faces.render.Renderer;
import javax.faces.render.RenderKit;
import javax.faces.render.ResponseStateManager;
import java.io.Writer;
import java.io.OutputStream;
import java.io.IOException;


public class MockRenderKit extends RenderKit {

    public MockRenderKit() {
        addRenderer(UIData.COMPONENT_FAMILY, 
		    "javax.faces.Table", new TestRenderer(true));
	addRenderer(UIInput.COMPONENT_FAMILY, 
		    "TestRenderer", new TestRenderer());
        addRenderer(UIInput.COMPONENT_FAMILY, 
		    "javax.faces.Text", new TestRenderer());
	addRenderer(UIOutput.COMPONENT_FAMILY, 
		    "TestRenderer", new TestRenderer());
        addRenderer(UIOutput.COMPONENT_FAMILY, 
		    "javax.faces.Text", new TestRenderer());
        addRenderer(UIPanel.COMPONENT_FAMILY, 
		    "javax.faces.Grid", new TestRenderer(true));
    }


    private Map renderers = new HashMap();


    public void addRenderer(String family, String rendererType,
                            Renderer renderer) {
        if ((family == null) || (rendererType == null) || (renderer == null)) {
            throw new NullPointerException();
        }
        renderers.put(family + "|" + rendererType, renderer);
    }


    public Renderer getRenderer(String family, String rendererType) {
        if ((family == null) || (rendererType == null)) {
            throw new NullPointerException();
        }
        return ((Renderer) renderers.get(family + "|" + rendererType));
    }


    public ResponseWriter createResponseWriter(Writer writer,
					       String contentTypeList,
					       String characterEncoding) {
        return new MockResponseWriter(writer, characterEncoding);
    }

    public ResponseStream createResponseStream(OutputStream out) {
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


    class TestRenderer extends Renderer {

	private boolean rendersChildren = false;

	public TestRenderer() {}
       
	public TestRenderer(boolean rendersChildren) {
	    this.rendersChildren = rendersChildren;
	}

        public void decode(FacesContext context, UIComponent component) {

            if ((context == null) || (component == null)) {
                throw new NullPointerException();
            }

            if (!(component instanceof UIInput)) {
                return;
            }
            UIInput input = (UIInput) component;
            String clientId = input.getClientId(context);
            // System.err.println("decode(" + clientId + ")");

            // Decode incoming request parameters
            Map params = context.getExternalContext().getRequestParameterMap();
            if (params.containsKey(clientId)) {
                // System.err.println("  '" + input.currentValue(context) +
                //                    "' --> '" + params.get(clientId) + "'");
                input.setSubmittedValue((String) params.get(clientId));
            }

        }

        public void encodeBegin(FacesContext context, UIComponent component)
            throws IOException {

            if ((context == null) || (component == null)) {
                throw new NullPointerException();
            }
            ResponseWriter writer = context.getResponseWriter();
            writer.write
                ("<text id='" + component.getClientId(context) + "' value='" +
                 component.getAttributes().get("value") + "'/>\n");

        }

        public void encodeChildren(FacesContext context, UIComponent component)
            throws IOException {

            if ((context == null) || (component == null)) {
                throw new NullPointerException();
            }

        }

        public void encodeEnd(FacesContext context, UIComponent component)
            throws IOException {

            if ((context == null) || (component == null)) {
                throw new NullPointerException();
            }

        }

    }


}

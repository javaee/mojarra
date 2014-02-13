/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2014 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.faces.mock;

import java.util.HashMap;
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
        responseStateManager = new MockResponseStateManager();
    }

    private Map renderers = new HashMap();
    private ResponseStateManager responseStateManager = null;

    @Override
    public void addRenderer(String family, String rendererType,
            Renderer renderer) {
        if ((family == null) || (rendererType == null) || (renderer == null)) {
            throw new NullPointerException();
        }
        renderers.put(family + "|" + rendererType, renderer);
    }

    @Override
    public Renderer getRenderer(String family, String rendererType) {
        if ((family == null) || (rendererType == null)) {
            throw new NullPointerException();
        }
        return ((Renderer) renderers.get(family + "|" + rendererType));
    }

    @Override
    public ResponseWriter createResponseWriter(Writer writer,
            String contentTypeList,
            String characterEncoding) {
        return new MockResponseWriter(writer, characterEncoding);
    }

    @Override
    public ResponseStream createResponseStream(OutputStream out) {
        final OutputStream os = out;
        return new ResponseStream() {
            @Override
            public void close() throws IOException {
                os.close();
            }

            @Override
            public void flush() throws IOException {
                os.flush();
            }

            @Override
            public void write(byte[] b) throws IOException {
                os.write(b);
            }

            @Override
            public void write(byte[] b, int off, int len) throws IOException {
                os.write(b, off, len);
            }

            @Override
            public void write(int b) throws IOException {
                os.write(b);
            }
        };
    }

    @Override
    public ResponseStateManager getResponseStateManager() {
        return responseStateManager;
    }

    class TestRenderer extends Renderer {

        private boolean rendersChildren = false;

        public TestRenderer() {
        }

        public TestRenderer(boolean rendersChildren) {
            this.rendersChildren = rendersChildren;
        }

        @Override
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

        @Override
        public void encodeBegin(FacesContext context, UIComponent component)
                throws IOException {

            if ((context == null) || (component == null)) {
                throw new NullPointerException();
            }
            ResponseWriter writer = context.getResponseWriter();
            writer.write("<text id='" + component.getClientId(context) + "' value='"
                    + component.getAttributes().get("value") + "'/>\n");
        }

        @Override
        public void encodeChildren(FacesContext context, UIComponent component)
                throws IOException {
            if ((context == null) || (component == null)) {
                throw new NullPointerException();
            }
        }

        @Override
        public void encodeEnd(FacesContext context, UIComponent component)
                throws IOException {
            if ((context == null) || (component == null)) {
                throw new NullPointerException();
            }
        }
    }
}

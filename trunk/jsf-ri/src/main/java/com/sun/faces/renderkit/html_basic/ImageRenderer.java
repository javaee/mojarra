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

// ImageRenderer.java

package com.sun.faces.renderkit.html_basic;

import java.io.IOException;
import java.util.logging.Level;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import com.sun.faces.RIConstants;
import com.sun.faces.renderkit.Attribute;
import com.sun.faces.renderkit.AttributeManager;
import com.sun.faces.renderkit.RenderKitUtils;

/**
 * <B>ImageRenderer</B> is a class that handles the rendering of the graphic
 * ImageTag
 *
 */

public class ImageRenderer extends HtmlBasicRenderer {

    private static final Attribute[] ATTRIBUTES =
          AttributeManager.getAttributes(AttributeManager.Key.GRAPHICIMAGE);


    // ---------------------------------------------------------- Public Methods


    @Override
    public void encodeBegin(FacesContext context, UIComponent component)
          throws IOException {

        rendererParamsNotNull(context, component);

    }


    @Override
    public void encodeEnd(FacesContext context, UIComponent component)
          throws IOException {

        rendererParamsNotNull(context, component);

        if (!shouldEncode(component)) {
            return;
        }

        ResponseWriter writer = context.getResponseWriter();
        assert(writer != null);

        writer.startElement("img", component);
        writeIdAttributeIfNecessary(context, writer, component);
        writer.writeURIAttribute("src", RenderKitUtils.getImageSource(context, component, "value"), "value");
        // if we're writing XHTML and we have a null alt attribute
        if (writer.getContentType().equals(RIConstants.XHTML_CONTENT_TYPE) &&
            null == component.getAttributes().get("alt")) {
            // write out an empty alt
            writer.writeAttribute("alt", "", "alt");
        }

        RenderKitUtils.renderPassThruAttributes(context,
                                                writer,
                                                component,
                                                ATTRIBUTES);
        RenderKitUtils.renderXHTMLStyleBooleanAttributes(writer, component);
        String styleClass;
        if (null != (styleClass = (String)
              component.getAttributes().get("styleClass"))) {
            writer.writeAttribute("class", styleClass, "styleClass");
        }
        writer.endElement("img");
        if (logger.isLoggable(Level.FINER)) {
            logger.log(Level.FINER,
                       "End encoding component " + component.getId());
        }

    }

    // --------------------------------------------------------- Private Methods


    // The testcase for this class is TestRenderers_2.java

} // end of class ImageRenderer


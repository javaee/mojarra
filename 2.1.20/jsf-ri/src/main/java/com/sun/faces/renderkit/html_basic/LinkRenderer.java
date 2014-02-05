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

// LinkRenderer.java

package com.sun.faces.renderkit.html_basic;

import java.io.IOException;
import java.util.logging.Level;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import com.sun.faces.renderkit.Attribute;
import com.sun.faces.renderkit.AttributeManager;
import com.sun.faces.renderkit.RenderKitUtils;


/**
 * <B>LinkRenderer</B> acts as superclass for CommandLinkRenderer and
 * OutputLinkRenderer.
 */

public abstract class LinkRenderer extends HtmlBasicRenderer {


    private static final Attribute[] ATTRIBUTES =
          AttributeManager.getAttributes(AttributeManager.Key.COMMANDLINK);


    // ------------------------------------------------------- Protected Methods


    protected abstract void renderAsActive(FacesContext context,
                                           UIComponent component)
          throws IOException;


    protected void renderAsDisabled(FacesContext context, UIComponent component)
          throws IOException {

        ResponseWriter writer = context.getResponseWriter();
        assert(writer != null);

        writer.startElement("span", component);
        String writtenId =
              writeIdAttributeIfNecessary(context, writer, component);
        if (null != writtenId) {
            writer.writeAttribute("name", writtenId, "name");
        }

        RenderKitUtils.renderPassThruAttributes(context,
                                                writer,
                                                component,
                                                ATTRIBUTES);

        writeCommonLinkAttributes(writer, component);
        writeValue(component, writer);
        writer.flush();

    }


    protected void writeCommonLinkAttributes(ResponseWriter writer,
                                             UIComponent component)
          throws IOException {

        // handle styleClass
        String styleClass = (String)
              component.getAttributes().get("styleClass");
        if (styleClass != null) {
            writer.writeAttribute("class", styleClass, "styleClass");
        }

    }


    protected void writeValue(UIComponent component, ResponseWriter writer)
          throws IOException {

        Object v = getValue(component);
        String label = null;
        if (v != null) {
            label = v.toString();
        }        

        if (label != null && label.length() != 0) {
            if (logger.isLoggable(Level.FINE)) {
                logger.fine("Value to be rendered " + label);
            }
            writer.writeText(label, component, null);
        }

    }

} // end of class LinkRenderer

/*
 * $Id: LinkRenderer.java,v 1.15 2006/03/29 22:38:37 rlubke Exp $
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

// LinkRenderer.java

package com.sun.faces.renderkit.html_basic;

import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.ValueHolder;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import java.io.IOException;
import java.util.logging.Level;

import com.sun.faces.renderkit.RenderKitUtils;


/**
 * <B>LinkRenderer</B> acts as superclass for CommandLinkRenderer and
 * OutputLinkRenderer.
 */

public abstract class LinkRenderer extends HtmlBasicRenderer {

    // ------------------------------------------------------- Protected Methods


    protected abstract void renderAsActive(FacesContext context,
                                           UIComponent component)
          throws IOException;

    protected void renderAsDisabled(FacesContext context, UIComponent component)
          throws IOException {

        ResponseWriter writer = context.getResponseWriter();
        assert (writer != null);

        writer.startElement("span", component);
        String writtenId =
              writeIdAttributeIfNecessary(context, writer, component);
        if (null != writtenId) {
            writer.writeAttribute("name", writtenId, "name");
        }

        RenderKitUtils.renderPassThruAttributes(context, writer, component);

        writeCommonLinkAttributes(writer, component);
        writeValue(component, writer);
        writer.flush();

    }


    protected void writeCommonLinkAttributes(ResponseWriter writer,
                                             UIComponent component)
          throws IOException {

        // render type attribute that is common to only link renderers
        String type = (String) component.getAttributes().get("type");

        if (type != null) {
            writer.writeAttribute("type", type, "type");
        }

        // handle styleClass
        String styleClass = (String)
              component.getAttributes().get("styleClass");
        if (styleClass != null) {
            writer.writeAttribute("class", styleClass, "styleClass");
        }

    }


    protected void writeValue(UIComponent component, ResponseWriter writer)
          throws IOException {

        String label = null;
        if (component instanceof UICommand) {
            Object value = ((UICommand) component).getValue();
            if (value != null) {
                label = value.toString();
            }
        } else if (component instanceof ValueHolder) {
            Object value = ((ValueHolder) component).getValue();
            if (value != null) {
                label = value.toString();
            }
        }

        if (label != null && label.length() != 0) {
            if (logger.isLoggable(Level.FINE)) {
                logger.fine("Value to be rendered " + label);
            }
            writer.write(label);
        }

    }

} // end of class LinkRenderer

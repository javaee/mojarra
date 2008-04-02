/*
 * $Id: GroupRenderer.java,v 1.32 2006/09/01 17:30:55 rlubke Exp $
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

package com.sun.faces.renderkit.html_basic;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import java.io.IOException;
import java.util.Iterator;
import java.util.logging.Level;

import com.sun.faces.util.MessageUtils;

/**
 * Arbitrary grouping "renderer" that simply renders its children
 * recursively in the <code>encodeEnd()</code> method.
 *
 * @version $Id: GroupRenderer.java,v 1.32 2006/09/01 17:30:55 rlubke Exp $
 */
public class GroupRenderer extends HtmlBasicRenderer {

    // ---------------------------------------------------------- Public Methods


    public void encodeBegin(FacesContext context, UIComponent component)
          throws IOException {

        if (logger.isLoggable(Level.FINER)) {
            logger.log(Level.FINER, "Begin encoding component " +
                                    component.getId());
        }
        // suppress rendering if "rendered" property on the component is
        // false.
        if (!component.isRendered()) {
            if (logger.isLoggable(Level.FINE)) {
                logger.fine("End encoding component " +
                            component.getId() + " since " +
                            "rendered attribute is set to false ");
            }
            return;
        }

        // Render a span around this group if necessary
        String
              style = (String) component.getAttributes().get("style"),
              styleClass = (String) component.getAttributes().get("styleClass"),
              layout = (String) component.getAttributes().get("layout");
        ResponseWriter writer = context.getResponseWriter();

        if (divOrSpan(component)) {
            if ((layout != null) && (layout.equals("block"))) {
                writer.startElement("div", component);
            } else {
                writer.startElement("span", component);
            }
            writeIdAttributeIfNecessary(context, writer, component);
            if (styleClass != null) {
                writer.writeAttribute("class", styleClass, "styleClass");
            }
            if (style != null) {
                writer.writeAttribute("style", style, "style");
            }
        }

    }


    public void encodeChildren(FacesContext context, UIComponent component)
          throws IOException {

        if (logger.isLoggable(Level.FINER)) {
            logger.log(Level.FINER,
                       "Begin encoding children " + component.getId());
        }
        // suppress rendering if "rendered" property on the component is
        // false.
        if (!component.isRendered()) {
            if (logger.isLoggable(Level.FINE)) {
                logger.fine("End encoding component " +
                            component.getId() + " since " +
                            "rendered attribute is set to false ");
            }
            return;
        }

        // Render our children recursively
        Iterator<UIComponent> kids = getChildren(component);
        while (kids.hasNext()) {
            encodeRecursive(context, kids.next());
        }
        if (logger.isLoggable(Level.FINER)) {
            logger.log(Level.FINER,
                       "End encoding children " + component.getId());
        }

    }


    public void encodeEnd(FacesContext context, UIComponent component)
          throws IOException {

        if (context == null) {
            throw new NullPointerException(
                  MessageUtils.getExceptionMessageString(MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID,
                                                         "context"));
        }
        if (component == null) {
            throw new NullPointerException(
                  MessageUtils.getExceptionMessageString(MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID,
                                                         "component"));
        }
        // suppress rendering if "rendered" property on the component is
        // false.
        if (!component.isRendered()) {
            if (logger.isLoggable(Level.FINE)) {
                logger.fine("End encoding component " +
                            component.getId() + " since " +
                            "rendered attribute is set to false ");
            }
            return;
        }

        // Close our span element if necessary
        ResponseWriter writer = context.getResponseWriter();
        String layout = (String) component.getAttributes().get("layout");
        if (divOrSpan(component)) {
            if ((layout != null) && (layout.equals("block"))) {
                writer.endElement("div");
            } else {
                writer.endElement("span");
            }
        }
        if (logger.isLoggable(Level.FINER)) {
            logger.log(Level.FINER, "End encoding component " +
                                    component.getId());
        }

    }


    public boolean getRendersChildren() {

        return true;

    }

    // --------------------------------------------------------- Private Methods


    /**
     * <p>Return true if we need to render a div or span element around this group.
     *
     * @param component <code>UIComponent</code> for this group
     */
    private boolean divOrSpan(UIComponent component) {

        if (shouldWriteIdAttribute(component) ||
            (component.getAttributes().get("style") != null) ||
            (component.getAttributes().get("styleClass") != null)) {
            return (true);
        } else {
            return (false);
        }

    }

}

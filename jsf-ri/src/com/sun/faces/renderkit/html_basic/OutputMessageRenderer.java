/*
 * $Id: OutputMessageRenderer.java,v 1.29 2006/10/31 19:21:40 rlubke Exp $
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

// OutputMessageRenderer.java

package com.sun.faces.renderkit.html_basic;

import javax.faces.component.UIComponent;
import javax.faces.component.UIParameter;
import javax.faces.component.ValueHolder;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;

import com.sun.faces.renderkit.RenderKitUtils;
import com.sun.faces.util.MessageUtils;


/** <B>OutputMessageRenderer</B> is a class that renderes UIOutput */

public class OutputMessageRenderer extends HtmlBasicRenderer {

    // ---------------------------------------------------------- Public Methods


    public void encodeBegin(FacesContext context, UIComponent component)
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
        if (logger.isLoggable(Level.FINER)) {
            logger.log(Level.FINER,
                       "Begin encoding component " + component.getId());
        }

        String currentValue = null;
        String style = (String) component.getAttributes().get("style");
        String styleClass = (String) component.getAttributes().get("styleClass");
        String lang = (String) component.getAttributes().get("lang");
        String dir = (String) component.getAttributes().get("dir");
        String title = (String) component.getAttributes().get("title");

        ResponseWriter writer = context.getResponseWriter();
        assert(writer != null);

        // suppress rendering if "rendered" property on the component is
        // false.
        if (!component.isRendered()) {
            if (logger.isLoggable(Level.FINE)) {
                logger.fine("End encoding component " + component.getId() +
                            " since rendered attribute is set to false");
            }
            return;
        }
        Object currentObj = ((ValueHolder) component).getValue();
        if (currentObj != null) {
            if (currentObj instanceof String) {
                currentValue = (String) currentObj;
            } else {
                currentValue = currentObj.toString();
            }
        } else {
            // if the value is null, do not output anything.
            return;
        }

        ArrayList<Object> parameterList = new ArrayList<Object>();

        // get UIParameter children...

        Iterator<UIComponent> kids = component.getChildren().iterator();
        while (kids.hasNext()) {
            UIComponent kid = kids.next();

            //PENDING(rogerk) ignore if child is not UIParameter?

            if (!(kid instanceof UIParameter)) {
                continue;
            }

            parameterList.add(((UIParameter) kid).getValue());
        }

        // If at least one substitution parameter was specified,
        // use the string as a MessageFormat instance.
        String message = null;
        if (parameterList.size() > 0) {
            message = MessageFormat.format
                  (currentValue, parameterList.toArray
                        (new Object[parameterList.size()]));
        } else {
            message = currentValue;
        }

        boolean wroteSpan = false;
        if (styleClass != null
             || style != null
             || dir != null
             || lang != null
             || title != null
             || shouldWriteIdAttribute(component)) {
            writer.startElement("span", component);
            writeIdAttributeIfNecessary(context, writer, component);
            wroteSpan = true;

            if (style != null) {
                writer.writeAttribute("style", style, "style");
            }
            if (null != styleClass) {
                writer.writeAttribute("class", styleClass, "styleClass");
            }
            if (dir != null) {
                writer.writeAttribute("dir", dir, "dir");
            }
            if (lang != null) {
                writer.writeAttribute(RenderKitUtils.prefixAttribute(lang, writer),
                                      lang,
                                      "lang");
            }
            if (title != null) {
                writer.writeAttribute("title", title, "title");
            }
        }
        Boolean escape = Boolean.TRUE;
        Object val = component.getAttributes().get("escape");
        if (val != null) {
            if (val instanceof Boolean) {
                escape = (Boolean) val;
            } else if (val instanceof String) {
                try {
                    escape = Boolean.valueOf((String) val);
                } catch (Throwable e) {
                }
            }
        }
        if (escape) {
            writer.writeText(message, component, "value");
        } else {
            writer.write(message);
        }
        if (wroteSpan) {
            writer.endElement("span");
        }

        if (logger.isLoggable(Level.FINER)) {
            logger.log(Level.FINER,
                       "End encoding component " + component.getId());
        }

    }

} // end of class OutputMessageRenderer

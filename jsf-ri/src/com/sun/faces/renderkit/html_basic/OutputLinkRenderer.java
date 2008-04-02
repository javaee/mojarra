/*
 * $Id: OutputLinkRenderer.java,v 1.26 2006/03/29 22:38:38 rlubke Exp $
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

// OutputLinkRenderer.java

package com.sun.faces.renderkit.html_basic;

import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import java.io.IOException;
import java.util.Iterator;
import java.util.logging.Level;

import com.sun.faces.renderkit.RenderKitUtils;
import com.sun.faces.util.MessageUtils;


/**
 * <B>OutputLinkRenderer</B> is a class ...
 * <p/>
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: OutputLinkRenderer.java,v 1.26 2006/03/29 22:38:38 rlubke Exp $
 */

public class OutputLinkRenderer extends LinkRenderer {

    // ---------------------------------------------------------- Public Methods


    public void decode(FacesContext context, UIComponent component) {

        if (context == null || component == null) {
            throw new NullPointerException(MessageUtils.getExceptionMessageString(
                  MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }

        // take no action, this is an Output component.
        if (logger.isLoggable(Level.FINE)) {
            logger.fine("No decoding necessary since the component "
                        + component.getId() +
                        " is not an instance or a sub class of UIInput");
        }

    }


    public void encodeBegin(FacesContext context, UIComponent component)
          throws IOException {

        if (context == null || component == null) {
            throw new NullPointerException(
                  MessageUtils.getExceptionMessageString(MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
        if (logger.isLoggable(Level.FINER)) {
            logger.log(Level.FINER,
                       "Begin encoding component " + component.getId());
        }

        UIOutput output = (UIOutput) component;
        boolean componentDisabled = false;
        if (output.getAttributes().get("disabled") != null) {
            if ((output.getAttributes().get("disabled")).equals(Boolean.TRUE)) {
                componentDisabled = true;
            }
        }
        if (componentDisabled) {
            renderAsDisabled(context, output);
        } else {
            renderAsActive(context, output);
        }

    }


    public void encodeChildren(FacesContext context, UIComponent component)
          throws IOException {

        if (context == null || component == null) {
            throw new NullPointerException(
                  MessageUtils.getExceptionMessageString(MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
        if (logger.isLoggable(Level.FINER)) {
            logger.log(Level.FINER,
                       "Begin encoding children " + component.getId());
        }
        // suppress rendering if "rendered" property on the component is
        // false.
        if (!component.isRendered()) {
            if (logger.isLoggable(Level.FINE)) {
                logger.fine("End encoding component "
                            + component.getId() + " since " +
                            "rendered attribute is set to false ");
            }
            return;
        }
        Iterator<UIComponent> kids = component.getChildren().iterator();
        while (kids.hasNext()) {
            UIComponent kid = kids.next();
            kid.encodeBegin(context);
            if (kid.getRendersChildren()) {
                kid.encodeChildren(context);
            }
            kid.encodeEnd(context);
        }

    }


    public void encodeEnd(FacesContext context, UIComponent component)
          throws IOException {

        if (context == null || component == null) {
            throw new NullPointerException(
                  MessageUtils.getExceptionMessageString(MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }

        if (logger.isLoggable(Level.FINER)) {
            logger.log(Level.FINER, "End encoding " + component.getId());
        }
        // suppress rendering if "rendered" property on the component is
        // false.
        if (!component.isRendered()) {
            if (logger.isLoggable(Level.FINE)) {
                logger.fine("End encoding component "
                            + component.getId() + " since " +
                            "rendered attribute is set to false ");
            }
            return;
        }
        ResponseWriter writer = context.getResponseWriter();
        assert (writer != null);

        boolean componentDisabled = false;
        if (component.getAttributes().get("disabled") != null) {
            if ((component.getAttributes().get("disabled"))
                  .equals(Boolean.TRUE)) {
                componentDisabled = true;
            }
        }

        if (componentDisabled) {

            writer.endElement("span");

        } else {
            //Write Anchor inline elements
            //Done writing Anchor element
            writer.endElement("a");
        }

    }


    public boolean getRendersChildren() {

        return true;

    }

    // ------------------------------------------------------- Protected Methods


    protected Object getValue(UIComponent component) {

        return ((UIOutput) component).getValue();

    }


    protected void renderAsActive(FacesContext context, UIComponent component)
          throws IOException {

        String hrefVal = getCurrentValue(context, component);
        if (logger.isLoggable(Level.FINE)) {
            logger.fine("Value to be rendered " + hrefVal);
        }

        // suppress rendering if "rendered" property on the output is
        // false
        if (!component.isRendered()) {
            if (logger.isLoggable(Level.FINE)) {
                logger.fine("End encoding component "
                            + component.getId() + " since " +
                            "rendered attribute is set to false ");
            }
            return;
        }
        ResponseWriter writer = context.getResponseWriter();
        assert (writer != null);
        writer.startElement("a", component);
        String writtenId =
              writeIdAttributeIfNecessary(context, writer, component);
        if (null != writtenId) {
            writer.writeAttribute("name", writtenId, "name");
        }
        // render an empty value for href if it is not specified
        if (null == hrefVal || 0 == hrefVal.length()) {
            hrefVal = "";
        }

        //Write Anchor attributes

        Param paramList[] = getParamList(context, component);
        int
              i = 0,
              len = paramList.length;
        StringBuffer sb = new StringBuffer();
        sb.append(hrefVal);
        if (0 < len) {
            sb.append("?");
        }
        for (i = 0; i < len; i++) {
            if (0 != i) {
                sb.append("&");
            }
            sb.append(paramList[i].getName());
            sb.append("=");
            sb.append(paramList[i].getValue());
        }
        writer.writeURIAttribute("href",
                                 context.getExternalContext()
                                       .encodeResourceURL(sb.toString()),
                                 "href");
        RenderKitUtils.renderPassThruAttributes(context, writer, component);
        RenderKitUtils.renderXHTMLStyleBooleanAttributes(writer, component);

        writeCommonLinkAttributes(writer, component);

        writer.flush();

    }

} // end of class OutputLinkRenderer

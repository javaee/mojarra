/*
 * $Id: OutputLinkRenderer.java,v 1.38 2007/08/30 19:29:13 rlubke Exp $
 */

/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 * 
 * Contributor(s):
 * 
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

// OutputLinkRenderer.java

package com.sun.faces.renderkit.html_basic;

import java.io.IOException;
import java.util.logging.Level;

import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import com.sun.faces.renderkit.AttributeManager;
import com.sun.faces.renderkit.RenderKitUtils;
import com.sun.faces.util.Util;


/**
 * <B>OutputLinkRenderer</B> is a class ...
 * <p/>
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: OutputLinkRenderer.java,v 1.38 2007/08/30 19:29:13 rlubke Exp $
 */

public class OutputLinkRenderer extends LinkRenderer {


    private static final String[] ATTRIBUTES =
          AttributeManager.getAttributes(AttributeManager.Key.OUTPUTLINK);


    // ---------------------------------------------------------- Public Methods


    @Override
    public void decode(FacesContext context, UIComponent component) {

        rendererParamsNotNull(context, component);

    }


    @Override
    public void encodeBegin(FacesContext context, UIComponent component)
          throws IOException {

        rendererParamsNotNull(context, component);

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

    @Override
    public void encodeChildren(FacesContext context, UIComponent component)
          throws IOException {

        rendererParamsNotNull(context, component);

        if (!shouldEncodeChildren(component)) {
            return;
        }

        if (component.getChildCount() > 0) {
            for (UIComponent kid : component.getChildren()) {
                encodeRecursive(context, kid);
            }
        }

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

        if (Boolean.TRUE.equals(component.getAttributes().get("disabled"))) {
            writer.endElement("span");
        } else {
            //Write Anchor inline elements
            //Done writing Anchor element
            writer.endElement("a");
        }

    }


    @Override
    public boolean getRendersChildren() {

        return true;

    }

    // ------------------------------------------------------- Protected Methods


    @Override
    protected Object getValue(UIComponent component) {

        if (Util.componentIsDisabled(component)) {
            return null;
        } else {
            return ((UIOutput) component).getValue();
        }

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
        assert(writer != null);
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

        Param paramList[] = getParamList(component);
        StringBuffer sb = new StringBuffer();
        sb.append(hrefVal);
        boolean paramWritten = false;
        for (int i = 0, len = paramList.length; i < len; i++) {
            String pn = paramList[i].name;
            if (pn != null && pn.length() != 0) {
                String pv = paramList[i].value;
                sb.append((paramWritten) ? '&' : '?');              
                sb.append(pn);
                sb.append('=');
                if (pv != null && pv.length() != 0) {
                    sb.append(pv);
                }                
                paramWritten = true;
            }
        }
        writer.writeURIAttribute("href",
                                 context.getExternalContext()
                                       .encodeResourceURL(sb.toString()),
                                 "href");
        RenderKitUtils.renderPassThruAttributes(writer,
                                                component,
                                                ATTRIBUTES);
        RenderKitUtils.renderXHTMLStyleBooleanAttributes(writer, component);

        writeCommonLinkAttributes(writer, component);

        writer.flush();

    }

} // end of class OutputLinkRenderer

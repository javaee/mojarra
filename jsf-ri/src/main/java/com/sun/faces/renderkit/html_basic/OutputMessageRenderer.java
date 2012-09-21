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

// OutputMessageRenderer.java

package com.sun.faces.renderkit.html_basic;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.component.UIParameter;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import com.sun.faces.renderkit.RenderKitUtils;


/** <B>OutputMessageRenderer</B> is a class that renderes UIOutput */

public class OutputMessageRenderer extends HtmlBasicInputRenderer {

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

        String currentValue = getCurrentValue(context, component);
        // If null, do not putput anything - return.
        if (null == currentValue) {
            return;
        }
        int childCount = component.getChildCount();
        List<Object> parameterList;

        if (childCount > 0) {
            parameterList = new ArrayList<Object>(childCount);
            // get UIParameter children...

            for (UIComponent kid : component.getChildren()) {
                //PENDING(rogerk) ignore if child is not UIParameter?
                if (!(kid instanceof UIParameter)) {
                    continue;
                }

                parameterList.add(((UIParameter) kid).getValue());
            }
        } else {
            parameterList = Collections.emptyList();
        }

        // If at least one substitution parameter was specified,
        // use the string as a MessageFormat instance.
        String message;
        if (parameterList.size() > 0) {
            MessageFormat fmt = new MessageFormat(currentValue,
                                                  context.getViewRoot().getLocale());
            StringBuffer buf = new StringBuffer(currentValue.length() * 2);
            fmt.format(parameterList.toArray(new Object[parameterList.size()]),
                       buf,
                       null);
            message = buf.toString();
        } else {
            message = currentValue;
        }

        ResponseWriter writer = context.getResponseWriter();
        assert(writer != null);

        String style = (String) component.getAttributes().get("style");
        String styleClass = (String) component.getAttributes().get("styleClass");
        String lang = (String) component.getAttributes().get("lang");
        String dir = (String) component.getAttributes().get("dir");
        String title = (String) component.getAttributes().get("title");
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
                writer.writeAttribute(RenderKitUtils.prefixAttribute("lang", writer),
                                      lang,
                                      "lang");
            }
            if (title != null) {
                writer.writeAttribute("title", title, "title");
            }
        }

        Object val = component.getAttributes().get("escape");
        boolean escape = (val != null) && Boolean.valueOf(val.toString());
        
        if (escape) {
            writer.writeText(message, component, "value");
        } else {
            writer.write(message);
        }
        if (wroteSpan) {
            writer.endElement("span");
        }

    }

} // end of class OutputMessageRenderer

/*
 * $Id: CheckboxRenderer.java,v 1.85 2007/08/30 19:29:12 rlubke Exp $
 *
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

// CheckboxRenderer.java

package com.sun.faces.renderkit.html_basic;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.ConverterException;

import com.sun.faces.renderkit.AttributeManager;
import com.sun.faces.renderkit.RenderKitUtils;


/**
 * <B>CheckboxRenderer</B> is a class that renders the current value of
 * <code>UISelectBoolean<code> as a checkbox.
 */

public class CheckboxRenderer extends HtmlBasicInputRenderer {

    private static final String[] ATTRIBUTES =
          AttributeManager.getAttributes(AttributeManager.Key.SELECTBOOLEANCHECKBOX);

    // ---------------------------------------------------------- Public Methods


    @Override
    public void decode(FacesContext context, UIComponent component) {

        rendererParamsNotNull(context, component);

        if (!shouldDecode(component)) {
            return;
        }

        String clientId = component.getClientId(context);
        assert(clientId != null);
        // Convert the new value

        Map<String, String> requestParameterMap = context.getExternalContext()
              .getRequestParameterMap();
        String isChecked = isChecked(requestParameterMap.get(clientId));
        setSubmittedValue(component, isChecked);
        if (logger.isLoggable(Level.FINE)) {
            logger.log(Level.FINE,
                       "new value after decoding: {0}",
                       isChecked);
        }

    }


    @Override
    public void encodeBegin(FacesContext context, UIComponent component)
          throws IOException {

        rendererParamsNotNull(context, component);

    }


    @Override
    public Object getConvertedValue(FacesContext context,
                                    UIComponent component,
                                    Object submittedValue)
    throws ConverterException {

        String newValue = (String) submittedValue;
        return Boolean.valueOf(newValue);

    }

    // ------------------------------------------------------- Protected Methods


    @Override
    protected void getEndTextToRender(FacesContext context,
                                      UIComponent component,
                                      String currentValue) throws IOException {

        ResponseWriter writer = context.getResponseWriter();
        assert(writer != null);
        String styleClass;

        writer.startElement("input", component);
        writeIdAttributeIfNecessary(context, writer, component);
        writer.writeAttribute("type", "checkbox", "type");
        writer.writeAttribute("name", component.getClientId(context),
                              "clientId");

        if ("true".equals(currentValue)) {
            writer.writeAttribute("checked", Boolean.TRUE, "value");
        }
        if (null != (styleClass = (String)
              component.getAttributes().get("styleClass"))) {
            writer.writeAttribute("class", styleClass, "styleClass");
        }
        RenderKitUtils.renderPassThruAttributes(writer,
                                                component,
                                                ATTRIBUTES);
        RenderKitUtils.renderXHTMLStyleBooleanAttributes(writer, component);

        writer.endElement("input");

    }


    // --------------------------------------------------------- Private Methods

    /**
     * @param value the submitted value
     * @return "true" if the component was checked, otherise "false"
     */
    private static String isChecked(String value) {

        return Boolean.toString("on".equalsIgnoreCase(value)
                                || "yes".equalsIgnoreCase(value)
                                || "true".equalsIgnoreCase(value));

    }

} // end of class CheckboxRenderer

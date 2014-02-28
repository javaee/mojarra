/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2011 Oracle and/or its affiliates. All rights reserved.
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

package renderkits.renderkit.svg;

import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;
import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.component.ValueHolder;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import java.io.IOException;
import java.util.logging.Level;


/** <p>Render a <code>UIOutput</code> as a text label.</p> */

public class TextRenderer extends BaseRenderer {

    // ------------------------------------------------------- Renderer Methods


    public void encodeEnd(FacesContext context, UIComponent component)
          throws IOException {

        ResponseWriter writer = context.getResponseWriter();

        if (component instanceof UIOutput) {

            UIOutput output = (UIOutput) component;

            writer.write("<text");
            writeIdAttributeIfNecessary(context, writer, component);
            String x = (String) component.getAttributes().get("x");
            if (x != null) {
                writer.writeAttribute("x", x, "x");
            }
            String y = (String) component.getAttributes().get("y");
            if (y != null) {
                writer.writeAttribute("y", y, "y");
            }
            String dx = (String) component.getAttributes().get("dx");
            if (dx != null) {
                writer.writeAttribute("dx", dx, "dx");
            }
            String dy = (String) component.getAttributes().get("dy");
            if (dy != null) {
                writer.writeAttribute("dy", dy, "dy");
            }
            String rotate = (String) component.getAttributes().get("rotate");
            if (rotate != null) {
                writer.writeAttribute("rotate", rotate, "rotate");
            }
            String textLength =
                  (String) component.getAttributes().get("textLength");
            if (textLength != null) {
                writer.writeAttribute("textLength", textLength, "textLength");
            }
            String textAdjust =
                  (String) component.getAttributes().get("textAdjust");
            if (textAdjust != null) {
                writer.writeAttribute("textAdjust", textAdjust, "textAdjust");
            }
            String style = (String) component.getAttributes().get("style");
            if (style != null) {
                writer.writeAttribute("style", style, "style");
            }
            writer.write(">");
            writer.write(getCurrentValue(context, output));
            writer.write("</text>");
            writer.writeText("\n", null);

        }

    }

    private String getCurrentValue(FacesContext context,
                                   UIComponent component) {
        String currentValue = null;
        Object currentObj = getValue(component);
        if (currentObj != null) {
            currentValue = getFormattedValue(context, component, currentObj);
        }
        return currentValue;
    }

    private String getFormattedValue(FacesContext context,
                                     UIComponent component,
                                     Object currentValue)
          throws ConverterException {

        String result = null;
        Converter converter = null;

        // If there is a converter attribute, use it to to ask application
        // instance for a converter with this identifer.

        if (component instanceof ValueHolder) {
            converter = ((ValueHolder) component).getConverter();
        }

        // if value is null and no converter attribute is specified, then
        // return a zero length String.
        if (converter == null && currentValue == null) {
            return "";
        }
        if (converter == null) {
            // Do not look for "by-type" converters for Strings
            if (currentValue instanceof String) {
                return (String) currentValue;
            }

            // if converter attribute set, try to acquire a converter
            // using its class type.

            Class converterType = currentValue.getClass();
            converter = getConverterForClass(converterType);

            // if there is no default converter available for this identifier,
            // assume the model type to be String.
            if (converter == null && currentValue != null) {
                result = currentValue.toString();
                return result;
            }
        }

        if (converter != null) {
            result = converter.getAsString(context, component, currentValue);

            return result;
        } else {
            // throw converter exception if no converter can be
            // identified
            Object [] params = {
                  currentValue,
                  "null Converter"
            };

            throw new ConverterException("No converter could be identified.");
        }
    }

    private Converter getConverterForClass(Class converterClass) {
        if (converterClass == null) {
            return null;
        }
        try {
            ApplicationFactory aFactory =
                  (ApplicationFactory) FactoryFinder.getFactory(
                        FactoryFinder.APPLICATION_FACTORY);
            Application application = aFactory.getApplication();
            return (application.createConverter(converterClass));
        } catch (Exception e) {
            return (null);
        }
    }

    private Object getValue(UIComponent component) {
        if (component instanceof ValueHolder) {
            Object value = ((ValueHolder) component).getValue();
            if (logger.isLoggable(Level.FINE)) {
                logger.fine("component.getValue() returned " + value);
            }
            return value;
        }

        return null;
    }


} 

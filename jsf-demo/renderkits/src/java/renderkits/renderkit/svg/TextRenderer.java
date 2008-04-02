/*
 * Copyright 2005 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package renderkits.renderkit.svg;

import java.io.IOException;

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


/**
 * <p>Render a <code>UIOutput</code> as a text label.</p>
 */

public class TextRenderer extends BaseRenderer {


    // ------------------------------------------------------- Renderer Methods


    public void encodeEnd(FacesContext context, UIComponent component)
        throws IOException {

        ResponseWriter writer = context.getResponseWriter();

        if (component instanceof UIOutput) {

            UIOutput output = (UIOutput) component;

            writer.write("<text");
            String outputClass = (String) output.getAttributes().get("outputClass");
            if (outputClass == null) {
                outputClass = "sunText";
            }
            if (outputClass != null) {
                writer.write(" class=\"");
                writer.write(outputClass);
                writer.write("\"");
            }
            String x = (String)component.getAttributes().get("x");
            if (x != null) {
                writer.writeAttribute("x", x, "x");
            }
            String y = (String)component.getAttributes().get("y");
            if (y != null) {
                writer.writeAttribute("y", y, "y");
            }
            String dx = (String)component.getAttributes().get("dx");
            if (dx != null) {
                writer.writeAttribute("dx",dx, "dx");
            }
            String dy = (String)component.getAttributes().get("dy");
            if (dy != null) {
                writer.writeAttribute("dy",dy, "dy");
            }
            String rotate = (String)component.getAttributes().get("rotate");
            if (rotate != null) {
                writer.writeAttribute("rotate",rotate, "rotate");
            }
            String textLength = (String)component.getAttributes().get("textLength");
            if (textLength != null) {
                writer.writeAttribute("textLength",textLength, "textLength");
            }
            String textAdjust = (String)component.getAttributes().get("textAdjust");
            if (textAdjust != null) {
                writer.writeAttribute("textAdjust",textAdjust, "textAdjust");
            }
            writer.write(">");
            writer.write(getCurrentValue(context, output));
            writer.write("</text>");
            writer.writeText("\n", null);

        }

    }

    private String getCurrentValue(FacesContext context, UIComponent component) {
        String currentValue = null;
        Object currentObj = getValue(component);
        if (currentObj != null) {
            currentValue = getFormattedValue(context, component, currentObj);
        }
        return currentValue;
    }

    private String getFormattedValue(FacesContext context, UIComponent component,
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
            if (log.isDebugEnabled()) {
                log.debug("component.getValue() returned " + value);
            }
            return value;
        }
                                                                                                                       
        return null;
    }


                                                         
} 

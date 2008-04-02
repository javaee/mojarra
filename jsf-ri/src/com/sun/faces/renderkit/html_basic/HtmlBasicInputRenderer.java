/*
 * $Id: HtmlBasicInputRenderer.java,v 1.31 2006/01/11 15:28:08 rlubke Exp $
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

// HtmlBasicInputRenderer.java

package com.sun.faces.renderkit.html_basic;

import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.ValueHolder;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import com.sun.faces.util.MessageFactory;
import com.sun.faces.util.MessageUtils;

import java.util.logging.Level;

/**
 * <B>HtmlBasicInputRenderer</B> is a base class for implementing renderers
 * that support UIInput type components
 */

public abstract class HtmlBasicInputRenderer extends HtmlBasicRenderer {

    //
    // Protected Constants
    //
   
    //
    // Class Variables
    //

    //
    // Instance Variables
    //

    // Attribute Instance Variables

    // Relationship Instance Variables

    //
    // Constructors and Initializers    
    //

    public HtmlBasicInputRenderer() {
        super();
    }

    //
    // Class methods
    //

    //
    // General Methods
    //

    public void setSubmittedValue(UIComponent component, Object value) {
        if (component instanceof UIInput) {
            ((UIInput) component).setSubmittedValue(value);
            if (logger.isLoggable(Level.FINE)) {
                 logger.fine("Set submitted value " + value + " on component ");
            }
        }

    }


    protected Object getValue(UIComponent component) {
        if (component instanceof ValueHolder) {
            Object value = ((ValueHolder) component).getValue();
            if (logger.isLoggable(Level.FINE)) {
                 logger.fine("component.getValue() returned " + value);
            }
            return value;
        }

        return null;
    }


    public Object getConvertedValue(FacesContext context, UIComponent component,
                                    Object submittedValue)
        throws ConverterException {

        String newValue = (String) submittedValue;
        // if we have no local value, try to get the valueExpression.
        ValueExpression valueExpression = component.getValueExpression("value");

        Converter converter = null;
        Object result = null;
        // If there is a converter attribute, use it to to ask application
        // instance for a converter with this identifer.
        
        if (component instanceof ValueHolder) {
            converter = ((ValueHolder) component).getConverter();
        }

        if (null == converter && null != valueExpression) {
            Class converterType = valueExpression.getType(context.getELContext());
           // if converterType is null, assume the modelType is "String".
            if (converterType == null ||
                converterType == String.class ||
                converterType == Object.class) {
                if (logger.isLoggable(Level.FINE)) {
                    logger.fine("No conversion necessary for " + submittedValue
                              + "and converterType " + converterType +
                              " while decoding component " + component.getId());
                }
                return newValue;
            }
            // if getType returns a type for which we support a default
            // conversion, acquire an appropriate converter instance.

            try {
                Application application = context.getApplication();
                converter = application.createConverter(converterType);
                if (logger.isLoggable(Level.FINE)) {
                    logger.fine(
                        "Created converter " + converter + "of type " +
                        converterType +
                        " while decoding component " +
                        component.getId());
                }
            } catch (Exception e) {
                if (logger.isLoggable(Level.FINE)) {
                    logger.fine("Converter could not be instantiated for " +
                              converterType + " while " +
                              "decoding component " + component.getId());
                }
                return (null);
            }
        } else if (converter == null && valueExpression == null) {
        // if there is no valueExpression and converter attribute set,
        // assume the modelType as "String" since we have no way of
        // figuring out the type. So for the selectOne and
        // selectMany, converter has to be set if there is no
        // valueExpression attribute set on the component.
            if (logger.isLoggable(Level.FINE)) {
                 logger.fine("No conversion necessary for " + submittedValue +
                          " while decoding component " + component.getId() +
                          "since there is no explicitly registered converter and " +
                          "component value is not bound to a model property ");
            }
            return newValue;
        }

        if (converter != null) {
            result = converter.getAsObject(context, component, newValue);
            return result;
        } else {
            if (logger.isLoggable(Level.FINE)) {
                 logger.fine("Unexpected Converter exception " +
                          " while decoding component " + component.getId());
            }
            // throw converter exception.
	    Object [] params = {
		newValue,
		"null Converter"
	    };

            throw new ConverterException(MessageFactory.getMessage(
                context, MessageUtils.CONVERSION_ERROR_MESSAGE_ID, params));
        }
    }
} // end of class HtmlBasicInputRenderer

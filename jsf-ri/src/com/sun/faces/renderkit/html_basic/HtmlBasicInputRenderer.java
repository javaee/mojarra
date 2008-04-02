/*
 * $Id: HtmlBasicInputRenderer.java,v 1.23 2004/03/31 18:48:36 eburns Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// HtmlBasicInputRenderer.java

package com.sun.faces.renderkit.html_basic;

import com.sun.faces.util.Util;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.ValueHolder;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.el.ValueBinding;

/**
 * <B>HtmlBasicInputRenderer</B> is a base class for implementing renderers
 * that support UIInput type components
 */

public abstract class HtmlBasicInputRenderer extends HtmlBasicRenderer {

    //
    // Protected Constants
    //
    // Log instance for this class
    protected static Log log = LogFactory.getLog(HtmlBasicInputRenderer.class);
    
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
            if (log.isDebugEnabled()) {
                log.debug("Set submitted value " + value + " on component ");
            }
        }

    }


    protected Object getValue(UIComponent component) {
        if (component instanceof ValueHolder) {
            Object value = ((ValueHolder) component).getValue();
            if (log.isDebugEnabled()) {
                log.debug("component.getValue() returned " + value);
            }
            return value;
        }

        return null;
    }


    public Object getConvertedValue(FacesContext context, UIComponent component,
                                    Object submittedValue)
        throws ConverterException {

        String newValue = (String) submittedValue;
        // if we have no local value, try to get the valueBinding.
        ValueBinding valueBinding = component.getValueBinding("value");

        Converter converter = null;
        Object result = null;
        // If there is a converter attribute, use it to to ask application
        // instance for a converter with this identifer.
        
        if (component instanceof ValueHolder) {
            converter = ((ValueHolder) component).getConverter();
        }

        if (null == converter && null != valueBinding) {
            Class converterType = valueBinding.getType(context);
// if converterType is null, assume the modelType is "String".
            if (converterType == null ||
                converterType == String.class ||
                converterType == Object.class) {
                if (log.isDebugEnabled()) {
                    log.debug("No conversion necessary for " + submittedValue
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
                if (log.isDebugEnabled()) {
                    log.debug(
                        "Created converter " + converter + "of type " +
                        converterType +
                        " while decoding component " +
                        component.getId());
                }
            } catch (Exception e) {
                if (log.isDebugEnabled()) {
                    log.debug("Converter could not be instantiated for " +
                              converterType + " while " +
                              "decoding component " + component.getId());
                }
                return (null);
            }
        } else if (converter == null && valueBinding == null) {
// if there is no valueBinding and converter attribute set,
// assume the modelType as "String" since we have no way of
// figuring out the type. So for the selectOne and
// selectMany, converter has to be set if there is no
// valueBinding attribute set on the component.
            if (log.isDebugEnabled()) {
                log.debug("No conversion necessary for " + submittedValue +
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
            if (log.isDebugEnabled()) {
                log.debug("Unexpected Converter exception " +
                          " while decoding component " + component.getId());
            }
            // throw converter exception.
            throw new ConverterException(Util.getExceptionMessageString(
                Util.CONVERSION_ERROR_MESSAGE_ID));
        }
    }
} // end of class HtmlBasicInputRenderer

/*
 * $Id: HtmlBasicInputRenderer.java,v 1.26.36.2 2007/04/27 21:27:45 ofung Exp $
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

// HtmlBasicInputRenderer.java

package com.sun.faces.renderkit.html_basic;

import com.sun.faces.util.Util;
import com.sun.faces.util.MessageFactory;
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
	    Object [] params = {
		newValue,
		"null Converter"
	    };

            throw new ConverterException(MessageFactory.getMessage(
                context, Util.CONVERSION_ERROR_MESSAGE_ID, params));
        }
    }
} // end of class HtmlBasicInputRenderer

/*
 * $Id: SecretRenderer.java,v 1.27 2002/08/13 22:53:26 rkitain Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// SecretRenderer.java

package com.sun.faces.renderkit.html_basic;

import com.sun.faces.util.Util;

import java.io.IOException;
import java.util.Iterator;

import javax.faces.component.AttributeDescriptor;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;
import javax.faces.FacesException;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.ConversionException;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.Log;
import org.mozilla.util.ParameterCheck;

/**
 *
 *  <B>SecretRenderer</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: SecretRenderer.java,v 1.27 2002/08/13 22:53:26 rkitain Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class SecretRenderer extends HtmlBasicRenderer {
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

    public SecretRenderer() {
        super();
    }

    //
    // Class methods
    //

    //
    // General Methods
    //

    //
    // Methods From Renderer
    //

    public boolean supportsComponentType(String componentType) {
        if ( componentType == null ) {
            throw new NullPointerException(Util.getExceptionMessage(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
        return (componentType.equals(UIInput.TYPE));
    }

    public void decode(FacesContext context, UIComponent component) 
        throws IOException {
        if (context == null || component == null) {
            throw new NullPointerException(Util.getExceptionMessage(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }

        // PENDING (rogerk) should we call supportsType to double check
        // component Type ??

        String compoundId = component.getCompoundId();
        Assert.assert_it(compoundId != null );

        String newValue = context.getServletRequest().getParameter(compoundId);
        String modelRef = component.getModelReference();

        // If modelReference String is null or newValue is null, type
        // conversion is not necessary. This is because default type
        // for UIOutput component is String. Simply set local value.

        if ( newValue == null || modelRef == null ) {
            component.setValue(newValue);
            component.setValid(true);
            return;
        }

        // if we get here, type conversion is required.

        Class modelType = null;
        try {
            modelType = context.getModelType(modelRef);
        } catch (FacesException fe ) {
            // FIXME log error
        }
        Assert.assert_it(modelType != null );

        Object convertedValue = null;
        try {
            convertedValue = ConvertUtils.convert(newValue, modelType);
            component.setValid(true);
            component.setValue(convertedValue);
        } catch (ConversionException ce ) {
            component.setValue(newValue);
            component.setValid(false);
            addConversionErrorMessage( context, component, ce.getMessage()); 
        }
    }

    public void encodeBegin(FacesContext context, UIComponent component) 
        throws IOException {
        if (context == null || component == null) {
            throw new NullPointerException(Util.getExceptionMessage(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
    }

    public void encodeChildren(FacesContext context, UIComponent component) 
        throws IOException {
        if (context == null || component == null) {
            throw new NullPointerException(Util.getExceptionMessage(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
    }

    public void encodeEnd(FacesContext context, UIComponent component) 
        throws IOException {
        String currentValue = null;
        ResponseWriter writer = null;

        if (context == null || component == null) {
            throw new NullPointerException(Util.getExceptionMessage(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }

        Object currentObj = component.currentValue(context);
        if (currentObj != null) {
            if (currentValue instanceof String) {
                currentValue = (String)currentObj;
            } else { 
                currentValue = ConvertUtils.convert(currentObj);
            }
        } else {
            currentValue = "";
        }

        if (currentValue == null) {
            currentValue = "";
        }

        writer = context.getResponseWriter();
        Assert.assert_it(writer != null );

        writer.write("<input type=\"password\"");
        writer.write(" name=\"");
        writer.write(component.getCompoundId());
        writer.write("\"");

        // render default text specified
        if (currentValue != null) {
            writer.write(" value=\"");
            writer.write(currentValue);
            writer.write("\"");
        }
        writer.write(Util.renderPassthruAttributes(context, component));
        writer.write(Util.renderBooleanPassthruAttributes(context, component));
        writer.write(">");         
   }

} // end of class SecretRenderer

/*
 * $Id: SecretRenderer.java,v 1.21 2002/07/10 17:57:22 jvisvanathan Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// SecretRenderer.java

package com.sun.faces.renderkit.html_basic;

import java.io.IOException;
import java.util.Iterator;

import javax.faces.component.AttributeDescriptor;
import javax.faces.component.UIComponent;
import javax.faces.component.UITextEntry;
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
 * @version $Id: SecretRenderer.java,v 1.21 2002/07/10 17:57:22 jvisvanathan Exp $
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
             return false;
        }
        return (componentType.equals(UITextEntry.TYPE));
    }

    public void decode(FacesContext context, UIComponent component) 
        throws IOException {
        if (context == null) {
            throw new NullPointerException("Null FacesContext");
        }
        ParameterCheck.nonNull(component);

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
        } catch (ConversionException ce ) {
            //PENDING (rogerk) add error message to messageList
        }

        if ( convertedValue == null ) {
            // since conversion failed, don't modify the localValue.
            // set the value temporarily in an attribute so that encode can
            // use this local state instead of local value.
            // PENDING (visvan) confirm with Craig ??
            component.setAttribute("localState", newValue);
        } else {
            // conversion successful, set converted value as the local value.
            component.setValue(convertedValue);
        }
    }

    public void encodeBegin(FacesContext context, UIComponent component) 
        throws IOException {

        String currentValue = null;
        ResponseWriter writer = null;

        if (context == null) {
            throw new NullPointerException("Null FacesContext");
        }
        ParameterCheck.nonNull(component);

        // if localState attribute is set, then conversion failed, so use
        // that to reproduce the incorrect value. Otherwise use the
        // current value stored in component.

        Object localState = component.getAttribute("localState");
        if (localState != null) {
            currentValue = (String) localState;
        } else {
            Object currentObj = component.currentValue(context);
            if (currentObj != null) {
                currentValue = ConvertUtils.convert(currentObj);
            }
        }

        if (currentValue == null) {
            currentValue = "";
        }

        writer = context.getResponseWriter();
        Assert.assert_it(writer != null );

        writer.write("<INPUT TYPE=\"PASSWORD\"");
        writer.write(" NAME=\"");
        writer.write(component.getCompoundId());
        writer.write("\"");

        // render default text specified
        if (currentValue != null) {
            writer.write(" VALUE=\"");
            writer.write(currentValue);
            writer.write("\"");
        }

        //render size if specified
        String textField_size = (String)component.getAttribute("size");
        if (textField_size != null) {
            writer.write(" SIZE=\"");
            writer.write(textField_size);
            writer.write("\"");
        }

        //render maxlength if specified
        String textField_ml = (String)component.getAttribute("maxlength");
        if (textField_ml != null) {
            writer.write(" MAXLENGTH=\"");
            writer.write(textField_ml);
            writer.write("\"");
        }
        writer.write(">");
    }

    public void encodeChildren(FacesContext context, UIComponent component) 
        throws IOException {

    }

    public void encodeEnd(FacesContext context, UIComponent component) 
        throws IOException {

    }

} // end of class SecretRenderer

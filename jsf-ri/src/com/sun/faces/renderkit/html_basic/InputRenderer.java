/*
 * $Id: InputRenderer.java,v 1.22 2002/06/28 22:47:00 eburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// InputRenderer.java

package com.sun.faces.renderkit.html_basic;

import java.util.Iterator;

import javax.faces.component.AttributeDescriptor;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;
import javax.faces.component.UIComponent;
import javax.faces.component.UITextEntry;
import javax.faces.FacesException;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.Log;
import org.mozilla.util.ParameterCheck;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.ConversionException;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

/**
 *
 *  <B>InputRenderer</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: InputRenderer.java,v 1.22 2002/06/28 22:47:00 eburns Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class InputRenderer extends HtmlBasicRenderer {
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

    public InputRenderer() {
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
        Object convertedValue = null;
        Class modelType = null;
        
        if ( context == null ) {
            throw new NullPointerException("FacesContext is null");
        }    
        ParameterCheck.nonNull(component);
        
        // PENDING (visvan) should we call supportsType to double check
        // componentType ??
        String compoundId = component.getCompoundId();
        Assert.assert_it(compoundId != null );
        
        String newValue = context.getServletRequest().getParameter(compoundId);
        String modelRef = component.getModelReference();
       
        // If modelReference String is null or newValue is null, type
        // conversion is not necessary. This is because default type
        // for UITextEntry component is String. Simply set local value.
        if ( newValue == null || modelRef == null ) {
            component.setValue(newValue);
            return;
        }
        
        // if we get here, type conversion is required.
        try {
            modelType = context.getModelType(modelRef);
        } catch (FacesException fe ) {
            // PENDING (visvan) log error
        }    
        Assert.assert_it(modelType != null );
        
        try {
            convertedValue = ConvertUtils.convert(newValue, modelType);
        } catch (ConversionException ce ) {
            //PENDING (visvan) add error message to messageList
        }    
            
        if ( convertedValue == null ) {
            // since conversion failed, don't modify the localValue.
            // set the value temporarily in an attribute so that encode can 
            // use this local state instead of local value.
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
        
        if ( context == null ) {
            throw new NullPointerException("FacesContext is null");
        }
        ParameterCheck.nonNull(component);
        
        // if localState attribute is set, then conversion failed, so use
        // that to reproduce the incorrect value. Otherwise use the current value
        // stored in component.
        Object localState = component.getAttribute("localState");
        if ( localState != null ) {
            currentValue = (String) localState;
        } else {
            Object currentObj = component.currentValue(context);
            if ( currentObj != null) {
                currentValue = ConvertUtils.convert(currentObj);
            }    
        }
        
        writer = context.getResponseWriter();
        Assert.assert_it(writer != null );
        
        writer.write("<INPUT TYPE=\"text\"");
        writer.write(" NAME=\"");
        writer.write(component.getCompoundId());
        writer.write("\"");

        // render default text specified
        if ( currentValue != null ) {
            writer.write(" VALUE=\"");
            writer.write(currentValue);
            writer.write("\"");
        }
        //render size if specified
        String textField_size = (String)component.getAttribute("size");
        if ( textField_size != null ) {
            writer.write(" SIZE=\"");
            writer.write(textField_size);
            writer.write("\"");
        }
        //render maxlength if specified
        String textField_ml = (String)component.getAttribute("maxlength");
        if ( textField_ml != null ) {
            writer.write(" MAXLENGTH=\"");
            writer.write(textField_ml);
            writer.write("\"");
        }
        writer.write(">");
    }

    public void encodeChildren(FacesContext context, UIComponent component) {

    }

    public void encodeEnd(FacesContext context, UIComponent component) 
            throws IOException {
    }
    
    // The testcase for this class is TestRenderers_1.java 

} // end of class InputRenderer



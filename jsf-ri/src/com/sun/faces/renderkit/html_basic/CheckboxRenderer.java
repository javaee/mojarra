/*
 * $Id: CheckboxRenderer.java,v 1.24 2002/06/12 23:51:06 jvisvanathan Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// CheckboxRenderer.java

package com.sun.faces.renderkit.html_basic;

import com.sun.faces.RIConstants;

import java.io.IOException;
import java.util.Iterator;

import javax.faces.component.AttributeDescriptor;
import javax.faces.component.UIComponent;
import javax.faces.component.UISelectBoolean;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;
import javax.faces.FacesException;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.ConvertUtils;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.Log;
import org.mozilla.util.ParameterCheck;

/**
 *
 *  <B>CheckboxRenderer</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: CheckboxRenderer.java,v 1.24 2002/06/12 23:51:06 jvisvanathan Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class CheckboxRenderer extends Renderer {
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

    public CheckboxRenderer() {
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
    public AttributeDescriptor getAttributeDescriptor(
        UIComponent component, String name) {
        return null;
    }

    public AttributeDescriptor getAttributeDescriptor(
        String componentType, String name) {
        return null;
    }

    public Iterator getAttributeNames(UIComponent component) {
        return null;
    }

    public Iterator getAttributeNames(String componentType) {
        return null;
    }

    public boolean supportsComponentType(UIComponent c) {
        if (c == null) {
            return false;
        }
        return supportsComponentType(c.getComponentType());
    }

    public boolean supportsComponentType(String componentType) {
        if (componentType == null) {
            return false;
        }
        return (componentType.equals(UISelectBoolean.TYPE));
    }

    public void decode(FacesContext context, UIComponent component) 
        throws IOException {
        if (context == null) {
            throw new NullPointerException("Null FacesContext");
        }

        ParameterCheck.nonNull(component);

        String compoundId = component.getCompoundId();
        Assert.assert_it(compoundId != null );
        String newValue = context.getServletRequest().getParameter(compoundId);
        String modelRef = component.getModelReference();

        //PENDING(rogerk) if there was nothing sent in the request,
        //the checkbox wasn't checked..
        //
        if (newValue == null) {
            newValue = "false";

        // Otherwise, if the checkbox was checked, the value
        // coming in could be "on", "yes" or "true".

        } else if (newValue.equalsIgnoreCase("on") ||
            newValue.equalsIgnoreCase("yes") ||
            newValue.equalsIgnoreCase("true")) {
            newValue = "true";
        }

        // if there is no model,.. 

        if (modelRef == null) {
            component.setValue(Boolean.valueOf(newValue));
            return;
        } 

        // if there is a model, then convert to the model type.

        Class modelType = null;
        try {
            modelType = context.getModelType(modelRef);
        } catch (FacesException fe ) {
            //PENDING(rogerk) log error
        }
        Assert.assert_it(modelType != null );

        Object convertedValue = null;
        try {
            convertedValue = ConvertUtils.convert(newValue, modelType);
        } catch (ConversionException ce ) {
            //PENDING - FIXME - add error message to messageList
        }

        // PENDING(rogerk) store failed conversion value in other
        // "localstate" attribute??
        //
        if ( convertedValue == null ) {
            component.setAttribute("localState", newValue);
        } else {
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

        // Use "localState" (if it's set - indicating conversion
        // failure)
        //
        Object localState = component.getAttribute("localState");
        if ( localState != null ) {
            currentValue = (String) localState;
        } else {
            Object currentObj = component.currentValue(context);
            if ( currentObj != null) {
                currentValue = ConvertUtils.convert(currentObj);
            }
        }

        if (currentValue == null) {
            return;
        }

        writer = context.getResponseWriter();
        Assert.assert_it(writer != null );
        writer.write("<INPUT TYPE=\"CHECKBOX\" ");
        writer.write(" NAME=\"");
        writer.write(component.getCompoundId());
        writer.write("\"");

        UISelectBoolean boolComp = (UISelectBoolean)component;
        if (boolComp.isSelected()) {
            writer.write(" CHECKED ");
        }

        writer.write(">");
        String label = (String)component.getAttribute("label");
        if (label != null) {
            writer.write(" ");
            writer.write(label);
        }
    }

    public void encodeChildren(FacesContext context, UIComponent component) 
        throws IOException {

    }

    public void encodeEnd(FacesContext context, UIComponent component) 
        throws IOException {

    }

} // end of class CheckboxRenderer

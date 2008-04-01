/*
 * $Id: OptionListRenderer.java,v 1.16 2002/06/12 23:51:07 jvisvanathan Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// OptionListRenderer.java

package com.sun.faces.renderkit.html_basic;

import java.io.IOException;
import java.util.Iterator;

import javax.faces.component.AttributeDescriptor;
import javax.faces.component.UIComponent;
import javax.faces.component.SelectItem;
import javax.faces.component.UISelectOne;
import javax.faces.context.ResponseWriter;
import javax.faces.context.FacesContext;
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
 *  <B>OptionListRenderer</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: OptionListRenderer.java,v 1.16 2002/06/12 23:51:07 jvisvanathan Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class OptionListRenderer extends Renderer {
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

    public OptionListRenderer() {
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
        ParameterCheck.nonNull(c);
        return supportsComponentType(c.getComponentType());
    }

    public boolean supportsComponentType(String componentType) {
        ParameterCheck.nonNull(componentType);
        return (componentType.equals(UISelectOne.TYPE));
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

        if (newValue == null || modelRef == null) {
            component.setValue(newValue);
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

        UISelectOne selectOne = (UISelectOne)component;

        // Use "localState" (if it's set - indicating conversion
        // failure)
        //
        Object localState = selectOne.getAttribute("localState");
        if ( localState != null ) {
            currentValue = (String) localState;
        } else {
            Object currentObj = selectOne.currentValue(context);
            if ( currentObj != null) {
                currentValue = ConvertUtils.convert(currentObj);
            }
        }

        if (currentValue == null) {
            currentValue = "";
        }
        SelectItem items[] = selectOne.getItems();
        if (items == null) {
            items = (SelectItem[]) context.getModelValue(
                selectOne.getItemsModelReference());
        }
        if (items == null) {
            items = new SelectItem[0];
        }

        writer = context.getResponseWriter();
        Assert.assert_it(writer != null );
        writer.write("<SELECT NAME=\"");
        writer.write(component.getCompoundId());
        writer.write("\">");
        for (int i = 0; i < items.length; i++) {
            writer.write("<OPTION VALUE=\"");
            writer.write((String) items[i].getValue());
            writer.write("\"");
            if (currentValue.equals(items[i].getValue())) {
                writer.write(" selected=\"selected\"");
            }
            writer.write(">");
            writer.write(items[i].getLabel());
            writer.write("</OPTION>");
        }
        writer.write("</SELECT>");
    }

    public void encodeChildren(FacesContext context, UIComponent component) 
        throws IOException {

    }

    public void encodeEnd(FacesContext context, UIComponent component) 
        throws IOException {

    }

} // end of class OptionListRenderer

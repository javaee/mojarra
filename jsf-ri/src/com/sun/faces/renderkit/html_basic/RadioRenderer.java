/*
 * $Id: RadioRenderer.java,v 1.16 2002/06/05 17:00:58 jvisvanathan Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// RadioRenderer.java

package com.sun.faces.renderkit.html_basic;

import java.util.Iterator;

import javax.faces.component.AttributeDescriptor;
import javax.faces.context.FacesContext;
import javax.faces.render.Renderer;
import javax.faces.component.UIComponent;
import javax.faces.FacesException;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.Log;
import org.mozilla.util.ParameterCheck;

import javax.faces.component.UISelectOne;
import javax.faces.component.SelectItem;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.ConversionException;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.PrintWriter;
import java.io.IOException;


/**
 *
 *  <B>RadioRenderer</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: RadioRenderer.java,v 1.16 2002/06/05 17:00:58 jvisvanathan Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class RadioRenderer extends Renderer {
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

    public RadioRenderer() {
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
        if ( c == null ) {
            return false;
        }     
        return supportsComponentType(c.getComponentType());
    }

    public boolean supportsComponentType(String componentType) {
        if ( componentType == null ) {
            return false;
        }    
        return (componentType.equals(UISelectOne.TYPE));
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
        String modelRef = component.getModel();
        
        // If modelReference String is null or newValue is null, type
        // conversion is not necessary. This is because default type
        // for UISelectOne component is String. Simply set local value.
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
        PrintWriter writer = null;
        UISelectOne uiSelectOne = null;
        
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
      
        // cast component to UISelectOne.
        if ( supportsComponentType(component)) {
            uiSelectOne = (UISelectOne) component;
        }    
        SelectItem items[] = uiSelectOne.getItems();
        if (items == null) {
            String itemsModel = uiSelectOne.getItemsModel();
            if ( itemsModel != null ) {
                items = (SelectItem[]) context.getModelValue(itemsModel);
            }    
        }
        
        if ( items == null ) {
            return;
        }
        
        writer = context.getServletResponse().getWriter();
        Assert.assert_it(writer != null );
        
        for (int i = 0; i < items.length; i++) {
            writer.print("<INPUT TYPE=\"RADIO\"");
            if (currentValue != null && 
                    (currentValue.equals(items[i].getValue()))){
                writer.print(" CHECKED");
            }
            writer.print(" NAME=\"");
            writer.print(uiSelectOne.getCompoundId());
            writer.print("\" VALUE=\"");
            writer.print(items[i].getValue());
            writer.print("\">");
            String itemLabel = items[i].getLabel();
            if (itemLabel != null) {
                writer.print(" ");
                writer.print(itemLabel);
            }
            writer.print("\n");
        }
    }

    public void encodeChildren(FacesContext context, UIComponent component) {

    }

    public void encodeEnd(FacesContext context, UIComponent component) {

    }

} // end of class RadioRenderer
